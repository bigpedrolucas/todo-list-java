package com.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidade Tarefa - EQUIVALENTE a model no Django.
 * 
 * DJANGO EQUIVALENT:
 * ```python
 * class Status(models.TextChoices):
 *     PENDENTE = 'PENDENTE', 'Pendente'
 *     EM_ANDAMENTO = 'EM_ANDAMENTO', 'Em Andamento'
 *     CONCLUIDA = 'CONCLUIDA', 'Concluída'
 * 
 * class Prioridade(models.IntegerChoices):
 *     BAIXA = 1, 'Baixa'
 *     MEDIA = 2, 'Média'
 *     ALTA = 3, 'Alta'
 *     URGENTE = 4, 'Urgente'
 * 
 * class Tarefa(models.Model):
 *     titulo = models.CharField(max_length=200)
 *     descricao = models.TextField(blank=True)
 *     status = models.CharField(max_length=20, choices=Status.choices, default=Status.PENDENTE)
 *     prioridade = models.IntegerField(choices=Prioridade.choices, default=Prioridade.MEDIA)
 *     projeto = models.ForeignKey(Projeto, related_name='tarefas', on_delete=models.CASCADE)
 *     created_at = models.DateTimeField(auto_now_add=True)
 *     updated_at = models.DateTimeField(auto_now=True)
 *     
 *     def __str__(self):
 *         return f"{self.titulo} ({self.status})"
 * ```
 */
@Entity
@Table(name = "tarefas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarefa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    /**
     * Enum incorporado para Status.
     * 
     * @Enumerated(EnumType.STRING) → Armazena como VARCHAR ('PENDENTE')
     * Alternativa: EnumType.ORDINAL → Armazena como INT (0, 1, 2)
     * STRING é mais seguro (ordem dos enums pode mudar)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.PENDENTE;
    
    /**
     * Enum incorporado para Prioridade.
     * 
     * Note: Em Java, enums podem ter atributos!
     * Veja a definição abaixo da classe.
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private Prioridade prioridade = Prioridade.MEDIA;
    
    /**
     * Relacionamento N:1 com Projeto.
     * 
     * DJANGO EQUIVALENT:
     * projeto = models.ForeignKey(Projeto, related_name='tarefas', on_delete=models.CASCADE)
     * 
     * @ManyToOne: Muitas tarefas para um projeto (N:1)
     * @JoinColumn: Configura nome da coluna FK (opcional, padrão: projeto_id)
     * nullable = false → Equivalente a on_delete=models.CASCADE (requer projeto)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // ============ ENUMS ============
    
    /**
     * Enum Status - Equivalente a models.TextChoices no Django.
     * 
     * DJANGO:
     * class Status(models.TextChoices):
     *     PENDENTE = 'PENDENTE', 'Pendente'
     *     ...
     * 
     * JAVA:
     * Cada constante é uma instância do enum com seus atributos.
     */
    public enum Status {
        PENDENTE("Pendente"),
        EM_ANDAMENTO("Em Andamento"),
        CONCLUIDA("Concluída"),
        CANCELADA("Cancelada");
        
        private final String descricao;
        
        Status(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    /**
     * Enum Prioridade - Equivalente a models.IntegerChoices no Django.
     * 
     * DJANGO:
     * class Prioridade(models.IntegerChoices):
     *     BAIXA = 1, 'Baixa'
     *     ...
     * 
     * JAVA:
     * Atributo 'valor' permite ordenação numérica no banco.
     */
    public enum Prioridade {
        BAIXA(1, "Baixa"),
        MEDIA(2, "Média"),
        ALTA(3, "Alta"),
        URGENTE(4, "Urgente");
        
        private final int valor;
        private final String descricao;
        
        Prioridade(int valor, String descricao) {
            this.valor = valor;
            this.descricao = descricao;
        }
        
        public int getValor() {
            return valor;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
}
