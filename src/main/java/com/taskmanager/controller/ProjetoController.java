package com.taskmanager.controller;

import com.taskmanager.dto.ProjetoDTOs;
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
 * Controller REST para Projetos.
 * 
 * DJANGO EQUIVALENT:
 * ```python
 * from rest_framework import viewsets, status
 * from rest_framework.decorators import action
 * 
 * class ProjetoViewSet(viewsets.ModelViewSet):
 *     queryset = Projeto.objects.all()
 *     serializer_class = ProjetoSerializer
 *     
 *     @action(detail=True, methods=['get'])
 *     def tarefas(self, request, pk=None):
 *         projeto = self.get_object()
 *         tarefas = projeto.tarefas.all()
 *         serializer = TarefaSerializer(tarefas, many=True)
 *         return Response(serializer.data)
 * ```
 * 
 * @RestController: Combina @Controller + @ResponseBody
 *   - @Controller: Marca como componente Spring MVC
 *   - @ResponseBody: Retorna JSON diretamente (não renderiza template)
 * 
 * @RequestMapping: Define URL base para todos os endpoints
 *   - Equivalente a: path('api/projetos/', include(...))
 * 
 * ANOTAÇÕES DE MÉTODO:
 * - @GetMapping    → GET    (listar, detalhes)
 * - @PostMapping   → POST   (criar)
 * - @PutMapping     → PUT    (atualizar completo)
 * - @PatchMapping   → PATCH  (atualizar parcial)
 * - @DeleteMapping  → DELETE (deletar)
 * 
 * ANOTAÇÕES DE PARÂMETRO:
 * - @PathVariable: Valor da URL (/projetos/{id})
 * - @RequestBody: Corpo da requisição (JSON)
 * - @Valid: Dispara validações do bean validation
 * - @RequestParam: Query parameters (?status=PENDENTE)
 */
@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {
    
    // Injeção de dependência via construtor (recomendado)
    // Django equivalent: injetar repositórios como dependências
    private final ProjetoRepository projetoRepository;
    private final TarefaRepository tarefaRepository;
    
    // Construtor com injeção - Spring resolve automaticamente
    public ProjetoController(ProjetoRepository projetoRepository, 
                             TarefaRepository tarefaRepository) {
        this.projetoRepository = projetoRepository;
        this.tarefaRepository = tarefaRepository;
    }
    
    // ============ CRUD PROJETOS ============
    
    /**
     * LISTAR TODOS OS PROJETOS
     * GET /api/projetos
     * 
     * DJANGO:
     * def list(self, request):
     *     projetos = Projeto.objects.all()
     *     serializer = ProjetoSerializer(projetos, many=True)
     *     return Response(serializer.data)
     */
    @GetMapping
    public ResponseEntity<List<ProjetoDTOs.ListItem>> listarTodos() {
        List<Projeto> projetos = projetoRepository.findByOrderByCreatedAtDesc();
        
        List<ProjetoDTOs.ListItem> response = projetos.stream()
            .map(p -> new ProjetoDTOs.ListItem(
                p.getId(),
                p.getNome(),
                p.getCreatedAt(),
                p.getTarefas().size()
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * OBTER DETALHES DE UM PROJETO
     * GET /api/projetos/{id}
     * 
     * DJANGO:
     * def retrieve(self, request, pk=None):
     *     projeto = get_object_or_404(Projeto, pk=pk)
     *     serializer = ProjetoSerializer(projeto)
     *     return Response(serializer.data)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjetoDTOs.Response> obterPorId(@PathVariable Long id) {
        Projeto projeto = projetoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Projeto não encontrado com ID: " + id
            ));
        
        ProjetoDTOs.Response response = new ProjetoDTOs.Response(
            projeto.getId(),
            projeto.getNome(),
            projeto.getDescricao(),
            projeto.getCreatedAt(),
            projeto.getUpdatedAt(),
            projeto.getTarefas().size()
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * CRIAR NOVO PROJETO
     * POST /api/projetos
     * 
     * DJANGO:
     * def create(self, request):
     *     serializer = ProjetoCreateSerializer(data=request.data)
     *     serializer.is_valid(raise_exception=True)
     *     projeto = serializer.save()
     *     return Response(ProjetoResponseSerializer(projeto).data, status=201)
     * 
     * @Valid: Dispara Bean Validation (@NotBlank, @Size, etc.)
     * Se inválido, lança MethodArgumentNotValidException → capturado pelo handler global
     */
    @PostMapping
    public ResponseEntity<ProjetoDTOs.Response> criar(
            @Valid @RequestBody ProjetoDTOs.CreateRequest request) {
        
        Projeto projeto = Projeto.builder()
            .nome(request.nome())
            .descricao(request.descricao())
            .build();
        
        Projeto salvo = projetoRepository.save(projeto);
        
        ProjetoDTOs.Response response = new ProjetoDTOs.Response(
            salvo.getId(),
            salvo.getNome(),
            salvo.getDescricao(),
            salvo.getCreatedAt(),
            salvo.getUpdatedAt(),
            0  // Novo projeto, sem tarefas
        );
        
        // 201 Created + Location header (opcional)
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * ATUALIZAR PROJETO
     * PUT /api/projetos/{id}
     * 
     * DJANGO:
     * def update(self, request, pk=None):
     *     projeto = get_object_or_404(Projeto, pk=pk)
     *     serializer = ProjetoSerializer(projeto, data=request.data)
     *     serializer.is_valid(raise_exception=True)
     *     serializer.save()
     *     return Response(serializer.data)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjetoDTOs.Response> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProjetoDTOs.UpdateRequest request) {
        
        Projeto projeto = projetoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Projeto não encontrado com ID: " + id
            ));
        
        // Atualiza apenas campos não-nulos (PATCH-like se quisermos)
        if (request.nome() != null) {
            projeto.setNome(request.nome());
        }
        if (request.descricao() != null) {
            projeto.setDescricao(request.descricao());
        }
        
        Projeto atualizado = projetoRepository.save(projeto);
        
        ProjetoDTOs.Response response = new ProjetoDTOs.Response(
            atualizado.getId(),
            atualizado.getNome(),
            atualizado.getDescricao(),
            atualizado.getCreatedAt(),
            atualizado.getUpdatedAt(),
            atualizado.getTarefas().size()
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * DELETAR PROJETO
     * DELETE /api/projetos/{id}
     * 
     * DJANGO:
     * def destroy(self, request, pk=None):
     *     projeto = get_object_or_404(Projeto, pk=pk)
     *     projeto.delete()
     *     return Response(status=204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Projeto projeto = projetoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Projeto não encontrado com ID: " + id
            ));
        
        projetoRepository.delete(projeto);
        
        return ResponseEntity.noContent().build();  // 204 No Content
    }
    
    // ============ ENDPOINTS DE TAREFAS ============
    
    /**
     * LISTAR TAREFAS DE UM PROJETO
     * GET /api/projetos/{id}/tarefas
     * 
     * DJANGO:
     * @action(detail=True)
     * def tarefas(self, request, pk=None):
     *     projeto = self.get_object()
     *     tarefas = projeto.tarefas.all()
     *     ...
     */
    @GetMapping("/{id}/tarefas")
    public ResponseEntity<List<TarefaDTOs.Response>> listarTarefas(
            @PathVariable Long id,
            @RequestParam(required = false) Tarefa.Status status,
            @RequestParam(required = false) Tarefa.Prioridade prioridade) {
        
        // Verifica se projeto existe
        if (!projetoRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Projeto não encontrado com ID: " + id
            );
        }
        
        List<Tarefa> tarefas;
        
        // Filtros opcionais
        if (status != null) {
            tarefas = tarefaRepository.findByProjetoIdAndStatus(id, status);
        } else if (prioridade != null) {
            // Ordena por prioridade se filtrando por prioridade
            tarefas = tarefaRepository.findByProjetoIdOrderByPrioridadeDesc(id);
        } else {
            tarefas = tarefaRepository.findByProjetoId(id);
        }
        
        List<TarefaDTOs.Response> response = tarefas.stream()
            .map(this::toTarefaResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    // ============ MÉTODOS AUXILIARES ============
    
    private TarefaDTOs.Response toTarefaResponse(Tarefa tarefa) {
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
