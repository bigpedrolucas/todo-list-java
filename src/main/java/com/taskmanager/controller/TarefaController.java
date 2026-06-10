package com.taskmanager.controller;

import com.taskmanager.dto.TarefaDTOs;
import com.taskmanager.entity.Projeto;
import com.taskmanager.entity.Tarefa;
import com.taskmanager.repository.ProjetoRepository;
import com.taskmanager.repository.TarefaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para Tarefas.
 * 
 * DJANGO EQUIVALENT:
 * ```python
 * class TarefaViewSet(viewsets.ModelViewSet):
 *     queryset = Tarefa.objects.all()
 *     serializer_class = TarefaSerializer
 *     
 *     def get_queryset(self):
 *         # Filtros via query params
 *         queryset = super().get_queryset()
 *         status = self.request.query_params.get('status')
 *         if status:
 *             queryset = queryset.filter(status=status)
 *         return queryset
 * ```
 * 
 * Endpoints alternativos: Tarefas podem ser acessadas:
 * - Via ProjetoController: /api/projetos/{id}/tarefas (contexto de projeto)
 * - Via TarefaController: /api/tarefas (lista geral, filtros)
 */
@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    
    private final TarefaRepository tarefaRepository;
    private final ProjetoRepository projetoRepository;
    
    public TarefaController(TarefaRepository tarefaRepository,
                            ProjetoRepository projetoRepository) {
        this.tarefaRepository = tarefaRepository;
        this.projetoRepository = projetoRepository;
    }
    
    // ============ CRUD TAREFAS ============
    
    /**
     * LISTAR TODAS AS TAREFAS (com filtros opcionais)
     * GET /api/tarefas?status=PENDENTE&prioridade=ALTA
     * 
     * DJANGO:
     * def list(self, request):
     *     queryset = self.get_queryset()
     *     # Filtros de query params...
     *     serializer = TarefaSerializer(queryset, many=True)
     *     return Response(serializer.data)
     */
    @GetMapping
    public ResponseEntity<List<TarefaDTOs.Response>> listarTodos(
            @RequestParam(required = false) Tarefa.Status status,
            @RequestParam(required = false) Tarefa.Prioridade prioridade) {
        
        List<Tarefa> tarefas;
        
        // Aplica filtros se fornecidos
        if (status != null && prioridade != null) {
            // Ambos os filtros: busca manual ou query personalizada
            tarefas = tarefaRepository.findAll().stream()
                .filter(t -> t.getStatus() == status && t.getPrioridade() == prioridade)
                .collect(Collectors.toList());
        } else if (status != null) {
            // Filtrar por status em todos os projetos
            tarefas = tarefaRepository.findAll().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
        } else if (prioridade != null) {
            // Filtrar por prioridade
            tarefas = tarefaRepository.findAll().stream()
                .filter(t -> t.getPrioridade() == prioridade)
                .sorted((t1, t2) -> Integer.compare(
                    t2.getPrioridade().getValor(), 
                    t1.getPrioridade().getValor()
                ))
                .collect(Collectors.toList());
        } else {
            // Sem filtros
            tarefas = tarefaRepository.findAll();
        }
        
        List<TarefaDTOs.Response> response = tarefas.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * OBTER DETALHES DE UMA TAREFA
     * GET /api/tarefas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTOs.Response> obterPorId(@PathVariable Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Tarefa não encontrada com ID: " + id
            ));
        
        return ResponseEntity.ok(toResponse(tarefa));
    }
    
    /**
     * CRIAR NOVA TAREFA
     * POST /api/tarefas
     * 
     * Nota: Requer projetoId válido (relacionamento obrigatório)
     */
    @PostMapping
    public ResponseEntity<TarefaDTOs.Response> criar(
            @Valid @RequestBody TarefaDTOs.CreateRequest request) {
        
        // Busca e valida o projeto
        Projeto projeto = projetoRepository.findById(request.projetoId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Projeto não encontrado com ID: " + request.projetoId()
            ));
        
        // Cria a tarefa
        Tarefa tarefa = Tarefa.builder()
            .titulo(request.titulo())
            .descricao(request.descricao())
            .status(request.status() != null ? request.status() : Tarefa.Status.PENDENTE)
            .prioridade(request.prioridade() != null ? request.prioridade() : Tarefa.Prioridade.MEDIA)
            .projeto(projeto)
            .build();
        
        // Salva (e sincroniza relacionamento bidirecional)
        projeto.addTarefa(tarefa);
        Tarefa salva = tarefaRepository.save(tarefa);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salva));
    }
    
    /**
     * ATUALIZAR TAREFA
     * PUT /api/tarefas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTOs.Response> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TarefaDTOs.UpdateRequest request) {
        
        Tarefa tarefa = tarefaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Tarefa não encontrada com ID: " + id
            ));
        
        // Atualiza campos fornecidos
        if (request.titulo() != null) {
            tarefa.setTitulo(request.titulo());
        }
        if (request.descricao() != null) {
            tarefa.setDescricao(request.descricao());
        }
        if (request.status() != null) {
            tarefa.setStatus(request.status());
        }
        if (request.prioridade() != null) {
            tarefa.setPrioridade(request.prioridade());
        }
        
        Tarefa atualizada = tarefaRepository.save(tarefa);
        
        return ResponseEntity.ok(toResponse(atualizada));
    }
    
    /**
     * DELETAR TAREFA
     * DELETE /api/tarefas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Tarefa não encontrada com ID: " + id
            ));
        
        // Remove do projeto (sincroniza bidirecional)
        tarefa.getProjeto().removeTarefa(tarefa);
        
        tarefaRepository.delete(tarefa);
        
        return ResponseEntity.noContent().build();
    }
    
    // ============ ENDPOINTS ESPECIAIS ============
    
    /**
     * ALTERAR STATUS DA TAREFA (PATCH específico)
     * PATCH /api/tarefas/{id}/status?novoStatus=CONCLUIDA
     * 
     * Útil para atualizações rápidas de status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TarefaDTOs.Response> alterarStatus(
            @PathVariable Long id,
            @RequestParam Tarefa.Status novoStatus) {
        
        Tarefa tarefa = tarefaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Tarefa não encontrada com ID: " + id
            ));
        
        tarefa.setStatus(novoStatus);
        Tarefa atualizada = tarefaRepository.save(tarefa);
        
        return ResponseEntity.ok(toResponse(atualizada));
    }
    
    // ============ MÉTODO AUXILIAR ============
    
    private TarefaDTOs.Response toResponse(Tarefa tarefa) {
        return new TarefaDTOs.Response(
            tarefa.getId(),
            tarefa.getTitulo(),
            tarefa.getDescricao(),
            tarefa.getStatus().name(),
            tarefa.getStatus().getDescricao(),
            tarefa.getPrioridade().name(),
            tarefa.getPrioridade().getDescricao(),
            tarefa.getProjeto().getId(),
            tarefa.getProjeto().getNome(),
            tarefa.getCreatedAt(),
            tarefa.getUpdatedAt()
        );
    }
}
