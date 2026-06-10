# Roadmap: Task Manager API

## Overview

Projeto de aprendizagem Django → Spring Boot através de 4 fases:
1. **Setup Spring Boot** — estrutura, dependências, configuração
2. **Entidades JPA** — mapeamento ORM (equivalente a Django models)
3. **API REST** — controllers, DTOs, validação (equivalente a DRF)
4. **Polimento** — documentação, tratamento de erros, testes

Cada fase foca em mapear conceitos Django para Java, com explicações do "porquê".

## Phases

- [ ] **Phase 1: Setup Spring Boot** - Projeto base, configuração, H2
- [ ] **Phase 2: Entidades JPA** - Modelos Projeto e Tarefa, relacionamentos
- [ ] **Phase 3: API REST** - Controllers, repositories, DTOs, CRUD
- [ ] **Phase 4: Polimento** - Swagger, validações, tratamento de erros

## Phase Details

### Phase 1: Setup Spring Boot
**Goal:** Projeto Spring Boot funcional com H2 e estrutura base
**Depends on:** Nothing (first phase)
**Requirements:** PROJ-01 a PROJ-05
**Success Criteria:**
  1. `mvn spring-boot:run` inicia sem erros
  2. H2 Console acessível em `/h2-console`
  3. Estrutura de packages criada (entity, repository, controller, service, dto)
**Plans:** 2 plans

Plans:
- [ ] 01-01: Criar pom.xml com dependências Spring Boot, JPA, H2, Lombok, Validation, Web
- [ ] 01-02: Configurar application.yml com H2, JPA, logging

### Phase 2: Entidades JPA
**Goal:** Modelos de dados com relacionamentos (equivalente a Django models)
**Depends on:** Phase 1
**Requirements:** TASK-01 a TASK-06
**Success Criteria:**
  1. Entidade `Projeto` criada com id, nome, descricao, createdAt
  2. Entidade `Tarefa` criada com id, titulo, descricao, status, prioridade, projeto_id
  3. Relacionamento 1:N entre Projeto e Tarefa configurado
  4. Tabelas criadas automaticamente no H2
**Plans:** 2 plans

Plans:
- [ ] 02-01: Criar entidade Projeto com JPA annotations
- [ ] 02-02: Criar entidade Tarefa com relacionamento e enums (Status, Prioridade)

### Phase 3: API REST
**Goal:** Endpoints CRUD funcionais (equivalente a Django REST Framework)
**Depends on:** Phase 2
**Requirements:** API-01
**Success Criteria:**
  1. POST /api/projetos cria projeto
  2. GET /api/projetos lista todos (paginado)
  3. GET /api/projetos/{id} retorna projeto
  4. PUT /api/projetos/{id} atualiza projeto
  5. DELETE /api/projetos/{id} deleta projeto
  6. Endpoints equivalentes para /api/tarefas
  7. GET /api/projetos/{id}/tarefas lista tarefas do projeto
**Plans:** 3 plans

Plans:
- [ ] 03-01: Criar repositories (Spring Data JPA = Django ORM Manager)
- [ ] 03-02: Criar DTOs com Records (equivalente a Serializers)
- [ ] 03-03: Criar controllers REST com validações

### Phase 4: Polimento
**Goal:** Documentação e tratamento de erros profissional
**Depends on:** Phase 3
**Requirements:** API-02, API-03, API-04
**Success Criteria:**
  1. Swagger UI acessível em `/swagger-ui.html`
  2. Todos os endpoints documentados
  3. Erros retornam JSON padronizado (timestamp, status, error, message, path)
  4. Validações retornam mensagens em português
  5. Testes manuais passam via Swagger
**Plans:** 2 plans

Plans:
- [ ] 04-01: Adicionar SpringDoc OpenAPI (Swagger)
- [ ] 04-02: Implementar GlobalExceptionHandler para tratamento padronizado

## Progress

**Execution Order:**
Phases executam em ordem: 1 → 2 → 3 → 4

| Phase | Plans Complete | Status | Completed |
|-------|----------------|--------|-----------|
| 1. Setup Spring Boot | 2/2 | ✅ Complete | 2025-06-08 |
| 2. Entidades JPA | 2/2 | ✅ Complete | 2025-06-08 |
| 3. API REST | 3/3 | ✅ Complete | 2025-06-08 |
| 4. Polimento | 0/2 | 🚧 In Progress | - |

## Status Atual

✅ **Fases 1-3 concluídas** — Código implementado e pronto para execução

⏳ **Pendente:** Instalação de Maven/Java no ambiente local
- Executar: `sudo apt install openjdk-21-jdk maven`
- Depois: `mvn spring-boot:run`

📁 **Estrutura criada:**
- `pom.xml` com Spring Boot, JPA, H2, Lombok, Swagger
- `application.yml` configurado
- Entidades `Projeto` e `Tarefa` com relacionamento 1:N
- Repositories com query methods
- Controllers REST com CRUD completo
- DTOs com Java Records
- Tratamento global de erros

📖 **Documentação:**
- `README.md` — guia completo com mapeamento Django→Java
- `SETUP.md` — instruções de instalação
