import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

// Interceptors para logging/debug
api.interceptors.request.use(
  (config) => {
    console.log('📤 Request:', config.method?.toUpperCase(), config.url)
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
  (response) => {
    console.log('📥 Response:', response.status, response.config.url)
    return response
  },
  (error) => {
    console.error('❌ API Error:', error.response?.status, error.response?.data)
    return Promise.reject(error)
  }
)

// Projetos API
export const projetoService = {
  listar: () => api.get('/projetos'),
  obter: (id) => api.get(`/projetos/${id}`),
  criar: (data) => api.post('/projetos', data),
  atualizar: (id, data) => api.put(`/projetos/${id}`, data),
  deletar: (id) => api.delete(`/projetos/${id}`),
  listarTarefas: (id, params) => api.get(`/projetos/${id}/tarefas`, { params })
}

// Tarefas API
export const tarefaService = {
  listar: (params) => api.get('/tarefas', { params }),
  obter: (id) => api.get(`/tarefas/${id}`),
  criar: (data) => api.post('/tarefas', data),
  atualizar: (id, data) => api.put(`/tarefas/${id}`, data),
  deletar: (id) => api.delete(`/tarefas/${id}`),
  alterarStatus: (id, novoStatus) => api.patch(`/tarefas/${id}/status?novoStatus=${novoStatus}`)
}

export default api
