package com.taskmanager.repository;

import com.taskmanager.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para Projeto - EQUIVALENTE ao Manager do Django ORM.
 * 
 * DJANGO EQUIVALENT:
 * ```python
 * # Django: Não precisa criar explicitamente
 * # Projeto.objects.all()  # <-- 'objects' é o Manager automático
 * # Projeto.objects.filter(nome__icontains='abc')
 * # Projeto.objects.get(id=1)
 * ```
 * 
 * SPRING DATA JPA:
 * Ao estender JpaRepository, ganhamos automaticamente métodos CRUD:
 * - save()        → Equivalente a create() / save()
 * - findById()    → Equivalente a get(pk=...)
 * - findAll()     → Equivalente a all()
 * - deleteById()  → Equivalente a delete()
 * - existsById()  → Equivalente a filter(pk=...).exists()
 * 
 * QUERY METHODS:
 * Basta declarar métodos com nomes específicos que o Spring gera a query:
 * - findByNomeContainingIgnoreCase → WHERE nome ILIKE %?%
 * - findByOrderByCreatedAtDesc     → ORDER BY created_at DESC
 * 
 * @Repository: Marca como componente de persistência (opcional aqui, pois
 * JpaRepository já é detectado, mas explícito ajuda na legibilidade)
 */
@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    
    /**
     * Busca projetos por nome (case-insensitive, partial match).
     * 
     * DJANGO:
     * Projeto.objects.filter(nome__icontains=nome)
     * 
     * SPRING:
     * Query Method - Spring gera automaticamente:
     * SELECT * FROM projetos WHERE LOWER(nome) LIKE LOWER('%' || ? || '%')
     */
    List<Projeto> findByNomeContainingIgnoreCase(String nome);
    
    /**
     * Busca projetos ordenados por data de criação decrescente.
     * 
     * DJANGO:
     * Projeto.objects.order_by('-created_at')
     */
    List<Projeto> findByOrderByCreatedAtDesc();
    
    /**
     * Verifica se existe projeto com o nome exato.
     * 
     * DJANGO:
     * Projeto.objects.filter(nome=nome).exists()
     */
    boolean existsByNome(String nome);
}
