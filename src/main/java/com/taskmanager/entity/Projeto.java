package com.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Projeto - EQUIVALENTE a model no Django.
 * 
 * DJANGO EQUIVALENT:
 * ```python
 * class Projeto(models.Model):
 *     nome = models.CharField(max_length=100)
 *     descricao = models.TextField(blank=True)
 *     created_at = models.DateTimeField(auto_now_add=True)
 *     updated_at = models.DateTimeField(auto_now=True)
 *     
 *     def __str__(self):
 *         return self.nome
 * ```
 * 
 * ANOTAÇÕES JPA EXPLICADAS:
 * - @Entity: Marca como classe de entidade (equivalente a models.Model)
 * - @Table: Configura nome da tabela (opcional, padrão é nome da classe)
 * - @Id: Chave primária (equivalente a AutoField ou BigAutoField)
 * - @GeneratedValue: Auto-incremento (equivalente a auto_now_add=True)
 * - @Column: Configurações de coluna (nullable, length, etc)
 * - @OneToMany: Relacionamento 1:N com Tarefa (equivalente a ForeignKey reverse)
 * - @CreationTimestamp / @UpdateTimestamp: Auto-set timestamps
 * 
 * LOMBOK EXPLICADO:
 * - @Getter / @Setter: Gera automaticamente (equivalente a properties em Python)
 * - @NoArgsConstructor: Construtor sem argumentos (exigido por JPA)
 * - @AllArgsConstructor: Construtor com todos os argumentos
 * - @Builder: Padrão Builder para construção fluente (tipo dataclass em Python)
 * - @Data: Combina @Getter, @Setter, @ToString, @EqualsAndHashCode
 */
@Entity
@Table(name = "projetos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projeto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Relacionamento 1:N com Tarefa.
     * 
     * DJANGO EQUIVALENT:
     * ```python
     * # Em Tarefa:
     * projeto = models.ForeignKey(Projeto, related_name='tarefas', on_delete=models.CASCADE)
     * ```
     * 
     * mappedBy = "projeto" → Tarefa tem o campo 'projeto' que é a FK
     * cascade = CascadeType.ALL → Operações em Projeto propagam para Tarefas
     * orphanRemoval = true → Deletar tarefa da lista = deleta do banco
     * fetch = FetchType.LAZY → Carrega tarefas sob demanda (padrão recomendado)
     */
    @OneToMany(
        mappedBy = "projeto",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Tarefa> tarefas = new ArrayList<>();
    
    /**
     * Método helper para adicionar tarefa mantendo consistência bidirecional.
     * 
     * IMPORTANTE: Em JPA, relacionamentos bidirecionais precisam
     * sincronização manual dos dois lados (diferente de Django).
     */
    public void addTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
        tarefa.setProjeto(this);
    }
    
    public void removeTarefa(Tarefa tarefa) {
        tarefas.remove(tarefa);
        tarefa.setProjeto(null);
    }
}
