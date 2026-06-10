package com.taskmanager.repository;

import com.taskmanager.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Tarefa.
 * 
 * DJANGO EQUIVALENT:
 * ```python
 * class TarefaManager(models.Manager):
 *     def por_projeto(self, projeto_id):
 *         return self.filter(projeto_id=projeto_id)
 *     
 *     def por_status(self, status):
 *         return self.filter(status=status)
 *     
 *     def pendentes(self):
 *         return self.filter(status='PENDENTE')
 * ```
 */
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    
    /**
     * Busca tarefas de um projeto específico.
     * 
     * DJANGO:
     * Tarefa.objects.filter(projeto_id=projetoId)
     */
    List<Tarefa> findByProjetoId(Long projetoId);
    
    /**
     * Busca tarefas de um projeto filtrando por status.
     * 
     * DJANGO:
     * Tarefa.objects.filter(projeto_id=projetoId, status=status)
     * 
     * Note: Múltiplos parâmetros = AND automático
     */
    List<Tarefa> findByProjetoIdAndStatus(Long projetoId, Tarefa.Status status);
    
    /**
     * Busca tarefas ordenadas por prioridade (maior primeiro).
     * 
     * DJANGO:
     * Tarefa.objects.order_by('-prioridade')
     */
    List<Tarefa> findByProjetoIdOrderByPrioridadeDesc(Long projetoId);

    /**
     * Conta tarefas por status em um projeto.
     * 
     * DJANGO:
     * Tarefa.objects.filter(projeto_id=projetoId, status=status).count()
     */
    long countByProjetoIdAndStatus(Long projetoId, Tarefa.Status status);
    
    /**
     * Verifica se existe tarefa com título em um projeto.
     * 
     * DJANGO:
     * Tarefa.objects.filter(projeto_id=projetoId, titulo=titulo).exists()
     */
    boolean existsByProjetoIdAndTituloIgnoreCase(Long projetoId, String titulo);
}
