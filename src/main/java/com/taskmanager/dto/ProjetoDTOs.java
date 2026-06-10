package com.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTOs (Data Transfer Objects) para Projeto.
 * 
 * DJANGO EQUIVALENT:
 * ```python
 * from rest_framework import serializers
 * 
 * class ProjetoCreateSerializer(serializers.ModelSerializer):
 *     class Meta:
 *         model = Projeto
 *         fields = ['nome', 'descricao']
 *         
 * class ProjetoResponseSerializer(serializers.ModelSerializer):
 *     class Meta:
 *         model = Projeto
 *         fields = ['id', 'nome', 'descricao', 'created_at', 'updated_at', 'tarefas_count']
 * ```
 * 
 * JAVA RECORDS (Java 16+):
 * - Declaracao concisa: automaticamente cria:
 *   * private final fields
 *   * Constructor com todos os campos
 *   * Getters (nome(), não getNome())
 *   * equals(), hashCode(), toString()
 * - Imutáveis por padrão (bom para concorrência)
 * - Menos boilerplate que classes tradicionais
 * 
 * Separamos em classes internas estáticas:
 * - CreateRequest: Dados para CRIAR projeto (entrada)
 * - UpdateRequest: Dados para ATUALIZAR projeto (entrada parcial)
 * - Response: Dados de RESPOSTA (saída, com id, timestamps)
 */
public final class ProjetoDTOs {
    
    // Construtor privado para impedir instanciação
    private ProjetoDTOs() {}
    
    /**
     * DTO para criar novo projeto (POST /api/projetos).
     * 
     * Validações:
     * - @NotBlank: Não pode ser null, vazio ou só espaços
     * - @Size: Mínimo/máximo de caracteres
     * 
     * DJANGO:
     * Similar a: serializers.CharField(required=True, max_length=100)
     */
    public record CreateRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,
        
        @Size(max = 1000, message = "Descrição não pode exceder 1000 caracteres")
        String descricao
    ) {}
    
    /**
     * DTO para atualizar projeto (PUT /api/projetos/{id}).
     * 
     * Note: Todos os campos são opcionais para permitir atualização parcial
     * se usarmos PATCH. Para PUT completo, todos devem ser enviados.
     */
    public record UpdateRequest(
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,
        
        @Size(max = 1000, message = "Descrição não pode exceder 1000 caracteres")
        String descricao
    ) {}
    
    /**
     * DTO de resposta para um projeto.
     * 
     * Inclui:
     * - Campos do projeto
     * - Timestamps
     * - Contagem de tarefas (evita carregar lista completa)
     * 
     * DJANGO:
     * serializers.IntegerField(source='tarefas.count', read_only=True)
     */
    public record Response(
        Long id,
        String nome,
        String descricao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long tarefasCount
    ) {}
    
    /**
     * DTO de resposta com lista de tarefas completa.
     * 
     * Usado quando queremos detalhes completos incluindo tarefas.
     */
    public record ResponseWithTarefas(
        Long id,
        String nome,
        String descricao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TarefaDTOs.Response> tarefas
    ) {}
    
    /**
     * DTO para listagem simplificada.
     * 
     * Usado em endpoints de listagem para reduzir payload.
     */
    public record ListItem(
        Long id,
        String nome,
        LocalDateTime createdAt,
        long tarefasCount
    ) {}
}
