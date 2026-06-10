# Task Manager API

## What This Is

API REST para gestão de projetos e tarefas, inspirada no Trello/Jira mas simplificada. Permite criar projetos, adicionar tarefas com status e prioridade, e gerenciar o fluxo de trabalho. Projeto de aprendizagem para transição de Django para Java/Spring Boot.

## Core Value

Um desenvolvedor Django pode criar uma API REST funcional em Spring Boot, entendendo o mapeamento de conceitos entre os frameworks.

## Requirements

### Validated

(None yet — ship to validate)

### Active

- [ ] CRUD completo de Projetos (criar, listar, atualizar, deletar)
- [ ] CRUD completo de Tarefas vinculadas a Projetos
- [ ] Listar tarefas por projeto com filtros (status, prioridade)
- [ ] Validação de dados de entrada
- [ ] Documentação da API (OpenAPI/Swagger)
- [ ] Banco de dados H2 em memória para desenvolvimento

### Out of Scope

- Autenticação JWT — será adicionada na Fase 2 para focar primeiro no core
- Frontend — API-only, foco em aprender backend Java
- Docker/Kubernetes — manter setup simples para aprendizagem
- Testes de integração completos — unitários são suficiente para v1
- Deploy em produção — foco em ambiente local de desenvolvimento

## Context

**Background do utilizador:**
- Experiência sólida com Django e Django REST Framework
- Conhece conceitos de ORM, serializadores, views, URLs, migrations
- Quer aprender Java através de "mapeamento mental" Django → Spring Boot

**Mapeamento Django → Spring Boot:**
| Django | Spring Boot |
|--------|-------------|
| `models.py` | Entidades JPA (`@Entity`) |
| `views.py` | Controllers REST (`@RestController`) |
| `urls.py` | `@RequestMapping` nas classes |
| `serializers.py` | DTOs + `Record` classes |
| Django ORM | Spring Data JPA |
| Migrations | Flyway (auto) |
| `admin.py` | H2 Console / futuro painel |
| `settings.py` | `application.yml` |

**Stack escolhida:**
- Java 21 (LTS, records, pattern matching)
- Spring Boot 3.2.x
- Spring Data JPA
- H2 Database (desenvolvimento)
- Maven (mais comum em Java)
- Lombok (reduzir boilerplate)

## Constraints

- **Tech Stack:** Java 21 + Spring Boot 3.2 — LTS e estável
- **Tempo:** Fases pequenas (30-60 min cada) para ritmo de aprendizagem
- **Complexidade:** Manter simples, evitar padrões enterprise complexos
- **Documentação:** Cada fase explica o "porquê" do mapeamento Django→Java

## Key Decisions

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| H2 em vez de PostgreSQL | Zero config, foco em aprender Spring Boot primeiro | — Pending |
| Records para DTOs | Java 21 feature, menos boilerplate que classes | — Pending |
| Lombok para entidades | Reduz boilerplate de getters/setters | — Pending |
| Sem autenticação na Fase 1 | Simplificar, focar em ORM e REST primeiro | — Pending |

---
*Last updated: 2025-06-08 after project initialization*
