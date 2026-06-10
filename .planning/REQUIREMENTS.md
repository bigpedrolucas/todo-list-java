# Requirements: Task Manager API

**Defined:** 2025-06-08
**Core Value:** Um desenvolvedor Django pode criar uma API REST funcional em Spring Boot

## v1 Requirements (MVP)

### Projetos

- [ ] **PROJ-01**: Criar projeto com nome e descrição
- [ ] **PROJ-02**: Listar todos os projetos (paginado)
- [ ] **PROJ-03**: Visualizar detalhes de um projeto
- [ ] **PROJ-04**: Atualizar nome/descrição do projeto
- [ ] **PROJ-05**: Deletar projeto (cascata: deleta tarefas associadas)

### Tarefas

- [ ] **TASK-01**: Criar tarefa vinculada a um projeto
- [ ] **TASK-02**: Listar tarefas de um projeto com filtros (status, prioridade)
- [ ] **TASK-03**: Visualizar detalhes de uma tarefa
- [ ] **TASK-04**: Atualizar tarefa (título, descrição, status, prioridade)
- [ ] **TASK-05**: Deletar tarefa
- [ ] **TASK-06**: Ordenar tarefas por prioridade ou data de criação

### API & Documentação

- [ ] **API-01**: Todos os endpoints retornam JSON adequado
- [ ] **API-02**: Endpoints documentados com OpenAPI/Swagger
- [ ] **API-03**: Tratamento de erros padronizado (400, 404, 500)
- [ ] **API-04**: Validações retornam mensagens claras

## v2 Requirements

### Autenticação

- **AUTH-01**: Registro de utilizadores
- **AUTH-02**: Login com JWT
- **AUTH-03**: Proteger endpoints com autenticação
- **AUTH-04**: Projetos pertencem a utilizadores

### Features Avançadas

- **ADV-01**: Soft delete (paranoid models)
- **ADV-02**: Audit logs (quem alterou o quê)
- **ADV-03**: WebSocket para notificações
- **ADV-04**: PostgreSQL em produção

## Out of Scope

| Feature | Reason |
|---------|--------|
| Frontend SPA | Foco em aprender backend Java primeiro |
| Docker/K8s | Manter setup simples |
| Email notifications | Complexidade extra não essencial |
| Mobile app | Fora do scope de aprendizagem |
| OAuth/Social login | JWT local é suficiente para v2 |
| File uploads | Manter foco em dados estruturados |

## Traceability

| Requirement | Phase | Status |
|-------------|-------|--------|
| PROJ-01 | Phase 1 | Pending |
| PROJ-02 | Phase 1 | Pending |
| PROJ-03 | Phase 1 | Pending |
| PROJ-04 | Phase 1 | Pending |
| PROJ-05 | Phase 1 | Pending |
| TASK-01 | Phase 2 | Pending |
| TASK-02 | Phase 2 | Pending |
| TASK-03 | Phase 2 | Pending |
| TASK-04 | Phase 2 | Pending |
| TASK-05 | Phase 2 | Pending |
| TASK-06 | Phase 2 | Pending |
| API-01 | Phase 3 | Pending |
| API-02 | Phase 3 | Pending |
| API-03 | Phase 3 | Pending |
| API-04 | Phase 3 | Pending |

**Coverage:**
- v1 requirements: 15 total
- Mapped to phases: 15
- Unmapped: 0 ✓

---
*Requirements defined: 2025-06-08*
*Last updated: 2025-06-08 after initial definition*
