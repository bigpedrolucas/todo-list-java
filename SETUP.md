# Setup do Projeto

## Instalação de Dependências

### Ubuntu/Debian
```bash
# Atualizar repositórios
sudo apt update

# Instalar Java 21
sudo apt install -y openjdk-21-jdk

# Instalar Maven
sudo apt install -y maven

# Verificar instalações
java --version   # Deve mostrar Java 21
mvn --version    # Deve mostrar Maven 3.9+
```

### Fedora/RHEL
```bash
sudo dnf install java-21-openjdk maven
```

### Arch
```bash
sudo pacman -S jdk21-openjdk maven
```

## Executar o Projeto

```bash
# Na pasta do projeto
cd /home/pedro_santos/Documents/codata/projeto-java

# Compilar
mvn clean compile

# Executar
mvn spring-boot:run
```

## Verificar Funcionamento

Após iniciar, acede aos endpoints:

1. **Swagger UI** (documentação interativa):
   http://localhost:8080/swagger-ui.html

2. **H2 Console** (explorar dados):
   http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:taskmanagerdb`
   - User: `sa`
   - Password: (vazio)

3. **Testar API** (com curl):
   ```bash
   # Criar projeto
   curl -X POST http://localhost:8080/api/projetos \
     -H "Content-Type: application/json" \
     -d '{"nome": "Teste", "descricao": "Projeto de teste"}'
   
   # Listar projetos
   curl http://localhost:8080/api/projetos
   ```

## Troubleshooting

### "mvn: command not found"
Maven não está instalado ou não está no PATH. Seguir instruções acima.

### "Unsupported class file major version"
Java versão errada. Verificar:
```bash
java --version  # Deve ser Java 21
```

### Porta 8080 ocupada
```bash
# Matar processo na porta 8080
lsof -ti:8080 | xargs kill -9

# Ou mudar porta no application.yml:
server:
  port: 8081
```

## Estrutura do Projeto

```
projeto-java/
├── .planning/              # Documentação GSD
├── pom.xml                 # Dependências Maven
├── README.md               # Documentação principal
├── SETUP.md                # Este arquivo
└── src/
    └── main/
        ├── java/com/taskmanager/
        │   ├── TaskManagerApplication.java
        │   ├── entity/       # Modelos (Projeto, Tarefa)
        │   ├── repository/   # Acesso a dados
        │   ├── controller/   # API REST
        │   ├── dto/          # DTOs
        │   └── config/       # Configurações
        └── resources/
            └── application.yml
```
