package com.taskmanager.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de exceções.
 * 
 * DJANGO EQUIVALENT:
 * ```python
 * from rest_framework.views import exception_handler
 * from rest_framework.response import Response
 * 
 * def custom_exception_handler(exc, context):
 *     response = exception_handler(exc, context)
 *     if response is not None:
 *         response.data['timestamp'] = timezone.now()
 *         response.data['path'] = context['request'].path
 *     return response
 * ```
 * 
 * @RestControllerAdvice: Aplica a todos os @RestController
 * - Combina @ControllerAdvice + @ResponseBody
 * - "Advice" = intercepta e modifica comportamento
 * 
 * COMO FUNCIONA:
 * Quando qualquer controller lança exceção, este handler captura
 * e transforma em resposta HTTP padronizada.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Exceção de validação (@Valid falhou).
     * 
     * DJANGO:
     * serializer.is_valid(raise_exception=True) → retorna 400 com erros de campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        
        // Coleta todos os erros de campo
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Erro de Validação",
            "Dados de entrada inválidos. Verifique os campos.",
            fieldErrors
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * ResponseStatusException (usamos nos controllers).
     * 
     * DJANGO:
     * raise NotFound("Mensagem") → 404
     * raise ValidationError("Mensagem") → 400
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException ex) {
        
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            ex.getStatusCode().value(),
            ex.getStatusCode().toString(),
            ex.getReason(),
            null
        );
        
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    
    /**
     * Exceção genérica (fallback).
     * 
     * DJANGO:
     * Qualquer exceção não tratada → 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro Interno",
            "Ocorreu um erro inesperado. Tente novamente mais tarde.",
            null
        );
        
        // Em produção, logar o stack trace
        ex.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * Record para resposta de erro padronizada.
     * 
     * Estrutura similar ao padrão de APIs REST:
     * {
     *   "timestamp": "2025-06-08T10:30:00",
     *   "status": 400,
     *   "error": "Erro de Validação",
     *   "message": "Dados de entrada inválidos...",
     *   "fieldErrors": {
     *     "nome": "Nome é obrigatório",
     *     "email": "Email inválido"
     *   }
     * }
     */
    public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors
    ) {}
}
