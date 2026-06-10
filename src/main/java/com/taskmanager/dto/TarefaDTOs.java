package com.taskmanager.dto;

import com.taskmanager.entity.Tarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTOs para Tarefa.
 * 
 * DJANGO EQUIVALENT:
 * ```python
 * class TarefaSerializer(serializers.ModelSerializer):
 *     status = serializers.ChoiceField(choices=Tarefa.Status.choices)
 *     prioridade = serializers.ChoiceField(choices=Tarefa.Prioridade.choices)
 *     
 *     class Meta:
 *         model = Tarefa
 *         fields = ['id', 'titulo', 'descricao', 'status', 'prioridade', 'projeto', ...]
 * ```
 */
public final class TarefaDTOs {
    
    private TarefaDTOs() {}
    
    /**
     * DTO para criar nova tarefa.
     * 
     * Note: projetoId deve ser validado (projeto deve existir)
     * @NotNull garante que o campo foi enviado (não é null)
     */
    public record CreateRequest(
        @NotBlank(message = "Título é obrigatório")
        @Size(min = 2, max = 200, message = "Título deve ter entre 2 e 200 caracteres")
        String titulo,
        
        @Size(max = 2000, message = "Descrição não pode exceder 2000 caracteres")
        String descricao,
        
        // Se null, usa o padrão definido na entidade (PENDENTE)
        Tarefa.Status status,
        
        // Se null, usa o padrão definido na entidade (MEDIA)
        Tarefa.Prioridade prioridade,
        
        @NotNull(message = "Projeto é obrigatório")
        Long projetoId
    ) {}
    
    /**
     * DTO para atualizar tarefa.
     * Todos os campos opcionais para atualização parcial.
     */
    public record UpdateRequest(
        @Size(min = 2, max = 200, message = "Título deve ter entre 2 e 200 caracteres")
        String titulo,
        
        @Size(max = 2000, message = "Descrição não pode exceder 2000 caracteres")
        String descricao,
        
        Tarefa.Status status,
        
        Tarefa.Prioridade prioridade
    ) {}
    
    /**
     * DTO de resposta para uma tarefa.
     * 
     * Inclui informações do projeto (aninhadas) para evitar múltiplas chamadas.
     * 
     * DJANGO:
     * ProjetoSerializer(nested) ou projto_nome (flat)
     */
    public record Response(
        Long id,
        String titulo,
        String descricao,
        String status,
        String statusDescricao,
        String prioridade,
        String prioridadeDescricao,
        Long projetoId,
        String projetoNome,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {}
    
    /**
     * DTO para listagem simplificada.
     */
    public record ListItem(
        Long id,
        String titulo,
        String status,
        String prioridade,
        LocalDateTime createdAt
    ) {}
    
    /**
     * DTO para filtros de busca.
     * 
     * Usado como query parameters em endpoints de listagem.
     * Todos opcionais para filtros flexíveis.
     */
    public record Filtros(
        Tarefa.Status status,
        Tarefa.Prioridade prioridade,
        String tituloContains
    ) {}
}
