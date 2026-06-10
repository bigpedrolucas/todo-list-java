# Task Manager Frontend

Frontend Vue 3 + Tailwind CSS integrado com Spring Boot API.

## 🚀 Como Executar

### 1. Instalar Dependências
```bash
cd /home/pedro_santos/Documents/codata/projeto-java/frontend
npm install
```

### 2. Iniciar Backend (em outro terminal)
```bash
cd /home/pedro_santos/Documents/codata/projeto-java
mvn spring-boot:run
```

### 3. Iniciar Frontend
```bash
npm run dev
```

### 4. Acessar
- Frontend: http://localhost:5173
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

## 📁 Estrutura

```
frontend/
├── src/
│   ├── assets/main.css       # Tailwind + estilos
│   ├── components/           # Componentes reutilizáveis
│   ├── services/api.js       # Axios + API calls
│   ├── views/                # Páginas
│   │   ├── HomeView.vue
│   │   ├── ProjetosView.vue
│   │   ├── ProjetoDetailView.vue
│   │   └── TarefasView.vue
│   ├── router/index.js       # Vue Router
│   └── App.vue               # Layout principal
├── package.json
├── vite.config.js            # Proxy para API
├── tailwind.config.js
└── index.html
```

## 🔗 Integração API

O Vite está configurado com proxy:
```javascript
// vite.config.js
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

Todas as chamadas `/api/*` são redirecionadas automaticamente para o Spring Boot.

## 🛠️ Tecnologias

- Vue 3 (Composition API)
- Vue Router
- Pinia (state management)
- Axios
- Tailwind CSS
- Vite

## 📱 Funcionalidades

- ✅ Listar projetos
- ✅ Criar/editar/deletar projetos
- ✅ Criar tarefas em projetos
- ✅ Alterar status das tarefas (checkbox)
- ✅ Filtros por status e prioridade
- ✅ Dashboard com estatísticas
