# Task Manager API

API REST para gestão de projetos e tarefas — projeto de aprendizagem para transição Django → Spring Boot.

## 🎯 Objetivo

Mapear mentalmente conceitos de Django para Spring Boot, aprendendo Java através de analogias familiares.

## 📚 Mapeamento Django → Spring Boot

| Django | Spring Boot | Arquivo(s) |
|--------|-------------|------------|
| `models.py` | `@Entity` classes | `entity/Projeto.java`, `entity/Tarefa.java` |
| `views.py` (API) | `@RestController` | `controller/*Controller.java` |
| `serializers.py` | DTOs (`Record`) | `dto/*DTOs.java` |
| `urls.py` | `@RequestMapping` | Definido nos controllers |
| Django ORM | Spring Data JPA | `repository/*Repository.java` |
| `makemigrations` | `ddl-auto: create-drop` | `application.yml` |
| Django Admin | H2 Console | `/h2-console` (dev) |
| `settings.py` | `application.yml` | `resources/application.yml` |
| `manage.py runserver` | `mvn spring-boot:run` | Maven plugin |
| `requirements.txt` | `pom.xml` | Dependências Maven |

## 🚀 Como Executar

### Pré-requisitos
- Java 21+ (`java --version`)
- Maven 3.9+ (`mvn --version`)

### 1. Compilar e executar
```bash
mvn spring-boot:run
```

### 2. Acessar aplicação
- API Base: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:taskmanagerdb`
  - User: `sa`
  - Password: (vazio)

## 📡 Endpoints da API

### Projetos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/projetos` | Listar todos (ordenado por data) |
| GET | `/api/projetos/{id}` | Obter projeto por ID |
| POST | `/api/projetos` | Criar novo projeto |
| PUT | `/api/projetos/{id}` | Atualizar projeto |
| DELETE | `/api/projetos/{id}` | Deletar projeto |
| GET | `/api/projetos/{id}/tarefas` | Listar tarefas do projeto |

### Tarefas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/tarefas` | Listar todas (com filtros opcionais) |
| GET | `/api/tarefas/{id}` | Obter tarefa por ID |
| POST | `/api/tarefas` | Criar nova tarefa |
| PUT | `/api/tarefas/{id}` | Atualizar tarefa |
| DELETE | `/api/tarefas/{id}` | Deletar tarefa |
| PATCH | `/api/tarefas/{id}/status` | Alterar status rapidamente |

### Filtros (Query Parameters)
```
GET /api/tarefas?status=PENDENTE
GET /api/tarefas?prioridade=ALTA
GET /api/projetos/1/tarefas?status=EM_ANDAMENTO
```

## 🧪 Exemplos de Uso

### Criar Projeto
```bash
curl -X POST http://localhost:8080/api/projetos \
  -H "Content-Type: application/json" \
  -d '{"nome": "Website E-commerce", "descricao": "Loja virtual de eletrônicos"}'
```

### Criar Tarefa
```bash
curl -X POST http://localhost:8080/api/tarefas \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Configurar SSL",
    "descricao": "Instalar certificado HTTPS",
    "status": "PENDENTE",
    "prioridade": "ALTA",
    "projetoId": 1
  }'
```

### Listar Tarefas com Filtro
```bash
curl "http://localhost:8080/api/tarefas?status=PENDENTE&prioridade=ALTA"
```

## 🏗️ Arquitetura

```
src/main/java/com/taskmanager/
├── TaskManagerApplication.java     # Entry point
├── entity/                          # Models (JPA)
│   ├── Projeto.java
│   └── Tarefa.java
├── repository/                      # Data Access (Spring Data JPA)
│   ├── ProjetoRepository.java
│   └── TarefaRepository.java
├── controller/                      # REST Controllers
│   ├── ProjetoController.java
│   └── TarefaController.java
├── dto/                             # Data Transfer Objects
│   ├── ProjetoDTOs.java
│   └── TarefaDTOs.java
└── config/                          # Configurations
    └── GlobalExceptionHandler.java  # Error handling

src/main/resources/
├── application.yml                  # App configuration
└── static/                          # Static files (if any)
```

## 📝 Diferenças Chave Django → Java

### 1. Relacionamentos Bidirecionais
**Django:** Automático
```python
projeto.tarefas.all()  # Funciona automaticamente
```

**Java:** Sincronização manual necessária
```java
// Em Projeto.java
public void addTarefa(Tarefa tarefa) {
    tarefas.add(tarefa);
    tarefa.setProjeto(this);  // Sincroniza outro lado
}
```

### 2. Validação
**Django:** No model + forms/serializers
```python
nome = models.CharField(max_length=100)  # Validação no DB
# + validação no serializer
```

**Java:** Separação clara
```java
// Entidade: JPA constraints
@Column(nullable=false, length=100)

// DTO: Bean Validation
@NotBlank @Size(max=100)
```

### 3. QuerySet vs Repository
**Django:**
```python
Projeto.objects.filter(nome__icontains="web").order_by("-created_at")
```

**Java:**
```java
// Query Method (automático)
findByNomeContainingIgnoreCase(String nome)

// Query personalizada
@Query("SELECT p FROM Projeto p WHERE ...")
```

## 🔧 Stack Tecnológica

| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| Java | 21 LTS | Linguagem principal |
| Spring Boot | 3.2.5 | Framework web |
| Spring Data JPA | 3.2.5 | ORM |
| H2 Database | 2.2 | Banco dev (em memória) |
| Lombok | 1.18+ | Reduz boilerplate |
| SpringDoc OpenAPI | 2.3 | Documentação Swagger |
| Maven | 3.9+ | Build e dependências |

## 📖 Próximos Passos

1. **Fase 1:** ✅ Setup completo
2. **Fase 2:** ✅ Entidades JPA
3. **Fase 3:** ✅ API REST
4. **Fase 4:** Documentação Swagger + Tratamento de erros

### Funcionalidades Futuras (v2)
- Autenticação JWT
- Soft delete (paranoid models)
- PostgreSQL para produção
- Testes de integração
- Docker container

## 📄 Licença

Projeto de aprendizagem — livre para uso e modificação.
# todo-list-java
