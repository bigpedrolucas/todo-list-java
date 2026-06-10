<template>
  <div class="space-y-6" v-if="projeto">
    <!-- Breadcrumb + Header -->
    <div>
      <router-link 
        to="/projetos" 
        class="text-sm text-gray-500 hover:text-primary-600 flex items-center gap-1 mb-2"
      >
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
        </svg>
        Voltar para Projetos
      </router-link>
      <div class="flex justify-between items-start">
        <div>
          <h1 class="text-2xl font-bold text-gray-900">{{ projeto.nome }}</h1>
          <p class="text-gray-600 mt-1">{{ projeto.descricao || 'Sem descrição' }}</p>
        </div>
        <button @click="showModal = true" class="btn-primary">
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Nova Tarefa
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <div class="card p-4">
        <p class="text-sm text-gray-500">Total</p>
        <p class="text-2xl font-bold text-gray-900">{{ stats.total }}</p>
      </div>
      <div class="card p-4">
        <p class="text-sm text-gray-500">Pendentes</p>
        <p class="text-2xl font-bold text-yellow-600">{{ stats.pendentes }}</p>
      </div>
      <div class="card p-4">
        <p class="text-sm text-gray-500">Em Andamento</p>
        <p class="text-2xl font-bold text-blue-600">{{ stats.emAndamento }}</p>
      </div>
      <div class="card p-4">
        <p class="text-sm text-gray-500">Concluídas</p>
        <p class="text-2xl font-bold text-green-600">{{ stats.concluidas }}</p>
      </div>
    </div>

    <!-- Filtros -->
    <div class="flex flex-wrap gap-3">
      <select v-model="filtroStatus" @change="carregarTarefas" class="form-input w-auto">
        <option value="">Todos os status</option>
        <option value="PENDENTE">Pendente</option>
        <option value="EM_ANDAMENTO">Em Andamento</option>
        <option value="CONCLUIDA">Concluída</option>
        <option value="CANCELADA">Cancelada</option>
      </select>
      <select v-model="filtroPrioridade" @change="carregarTarefas" class="form-input w-auto">
        <option value="">Todas as prioridades</option>
        <option value="BAIXA">Baixa</option>
        <option value="MEDIA">Média</option>
        <option value="ALTA">Alta</option>
        <option value="URGENTE">Urgente</option>
      </select>
      <button 
        v-if="filtroStatus || filtroPrioridade"
        @click="limparFiltros"
        class="btn-secondary"
      >
        Limpar Filtros
      </button>
    </div>

    <!-- Lista de Tarefas -->
    <div class="card">
      <div v-if="loading" class="flex justify-center py-12">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
      </div>
      
      <div v-else-if="tarefas.length" class="divide-y divide-gray-200">
        <div 
          v-for="tarefa in tarefas" 
          :key="tarefa.id"
          class="p-6 hover:bg-gray-50 transition-colors"
        >
          <div class="flex items-start justify-between">
            <div class="flex items-start gap-4">
              <!-- Status Checkbox -->
              <button 
                @click="toggleStatus(tarefa)"
                class="mt-1 flex-shrink-0"
              >
                <div 
                  class="w-6 h-6 rounded-full border-2 flex items-center justify-center transition-colors"
                  :class="{
                    'border-green-500 bg-green-500': tarefa.status === 'CONCLUIDA',
                    'border-gray-300': tarefa.status !== 'CONCLUIDA'
                  }"
                >
                  <svg 
                    v-if="tarefa.status === 'CONCLUIDA'" 
                    class="w-4 h-4 text-white" 
                    fill="none" 
                    stroke="currentColor" 
                    viewBox="0 0 24 24"
                  >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
                  </svg>
                </div>
              </button>
              
              <div>
                <h3 
                  class="text-lg font-medium"
                  :class="{ 'text-gray-900': tarefa.status !== 'CONCLUIDA', 'text-gray-500 line-through': tarefa.status === 'CONCLUIDA' }"
                >
                  {{ tarefa.titulo }}
                </h3>
                <p class="text-gray-600 text-sm mt-1">{{ tarefa.descricao }}</p>
                <div class="flex items-center gap-3 mt-3">
                  <span 
                    class="px-2 py-1 text-xs font-medium rounded-full"
                    :class="{
                      'bg-yellow-100 text-yellow-800': tarefa.status === 'PENDENTE',
                      'bg-blue-100 text-blue-800': tarefa.status === 'EM_ANDAMENTO',
                      'bg-green-100 text-green-800': tarefa.status === 'CONCLUIDA',
                      'bg-gray-100 text-gray-800': tarefa.status === 'CANCELADA'
                    }"
                  >
                    {{ tarefa.statusDescricao }}
                  </span>
                  <span 
                    class="px-2 py-1 text-xs font-medium rounded-full"
                    :class="{
                      'bg-gray-100 text-gray-600': tarefa.prioridade === 'BAIXA',
                      'bg-blue-100 text-blue-600': tarefa.prioridade === 'MEDIA',
                      'bg-orange-100 text-orange-600': tarefa.prioridade === 'ALTA',
                      'bg-red-100 text-red-600': tarefa.prioridade === 'URGENTE'
                    }"
                  >
                    {{ tarefa.prioridadeDescricao }}
                  </span>
                  <span class="text-xs text-gray-500">
                    {{ formatarData(tarefa.createdAt) }}
                  </span>
                </div>
              </div>
            </div>
            
            <button 
              @click="deletarTarefa(tarefa.id)"
              class="p-2 text-gray-400 hover:text-red-600"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
              </svg>
            </button>
          </div>
        </div>
      </div>
      
      <div v-else class="text-center py-12">
        <p class="text-gray-500">Nenhuma tarefa encontrada</p>
        <button @click="showModal = true" class="btn-primary mt-4">
          Criar primeira tarefa
        </button>
      </div>
    </div>

    <!-- Modal Nova Tarefa -->
    <div v-if="showModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full mx-4">
        <div class="p-6">
          <h3 class="text-lg font-medium text-gray-900 mb-4">Nova Tarefa</h3>
          <form @submit.prevent="salvarTarefa">
            <div class="space-y-4">
              <div>
                <label class="form-label">Título *</label>
                <input 
                  v-model="form.titulo" 
                  type="text" 
                  required
                  class="form-input"
                  placeholder="Título da tarefa"
                >
              </div>
              <div>
                <label class="form-label">Descrição</label>
                <textarea 
                  v-model="form.descricao" 
                  rows="3"
                  class="form-input"
                  placeholder="Descrição (opcional)"
                ></textarea>
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="form-label">Status</label>
                  <select v-model="form.status" class="form-input">
                    <option value="PENDENTE">Pendente</option>
                    <option value="EM_ANDAMENTO">Em Andamento</option>
                    <option value="CONCLUIDA">Concluída</option>
                  </select>
                </div>
                <div>
                  <label class="form-label">Prioridade</label>
                  <select v-model="form.prioridade" class="form-input">
                    <option value="BAIXA">Baixa</option>
                    <option value="MEDIA">Média</option>
                    <option value="ALTA">Alta</option>
                    <option value="URGENTE">Urgente</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="mt-6 flex justify-end gap-3">
              <button 
                type="button" 
                @click="showModal = false"
                class="btn-secondary"
              >
                Cancelar
              </button>
              <button 
                type="submit" 
                class="btn-primary"
                :disabled="salvando"
              >
                {{ salvando ? 'Salvando...' : 'Criar Tarefa' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  
  <div v-else class="flex justify-center py-12">
    <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { projetoService, tarefaService } from '../services/api'

const route = useRoute()
const projetoId = route.params.id

const projeto = ref(null)
const tarefas = ref([])
const loading = ref(false)
const showModal = ref(false)
const salvando = ref(false)
const filtroStatus = ref('')
const filtroPrioridade = ref('')

const form = ref({
  titulo: '',
  descricao: '',
  status: 'PENDENTE',
  prioridade: 'MEDIA'
})

const stats = computed(() => ({
  total: tarefas.value.length,
  pendentes: tarefas.value.filter(t => t.status === 'PENDENTE').length,
  emAndamento: tarefas.value.filter(t => t.status === 'EM_ANDAMENTO').length,
  concluidas: tarefas.value.filter(t => t.status === 'CONCLUIDA').length
}))

const carregarProjeto = async () => {
  try {
    const response = await projetoService.obter(projetoId)
    projeto.value = response.data
  } catch (error) {
    console.error('Erro ao carregar projeto:', error)
  }
}

const carregarTarefas = async () => {
  loading.value = true
  try {
    const params = {}
    if (filtroStatus.value) params.status = filtroStatus.value
    if (filtroPrioridade.value) params.prioridade = filtroPrioridade.value
    
    const response = await projetoService.listarTarefas(projetoId, params)
    tarefas.value = response.data
  } catch (error) {
    console.error('Erro ao carregar tarefas:', error)
  } finally {
    loading.value = false
  }
}

const salvarTarefa = async () => {
  salvando.value = true
  try {
    await tarefaService.criar({
      ...form.value,
      projetoId: parseInt(projetoId)
    })
    await carregarTarefas()
    showModal.value = false
    form.value = {
      titulo: '',
      descricao: '',
      status: 'PENDENTE',
      prioridade: 'MEDIA'
    }
  } catch (error) {
    console.error('Erro ao criar tarefa:', error)
    alert('Erro ao criar tarefa')
  } finally {
    salvando.value = false
  }
}

const toggleStatus = async (tarefa) => {
  const novoStatus = tarefa.status === 'CONCLUIDA' ? 'PENDENTE' : 'CONCLUIDA'
  try {
    await tarefaService.alterarStatus(tarefa.id, novoStatus)
    await carregarTarefas()
  } catch (error) {
    console.error('Erro ao alterar status:', error)
  }
}

const deletarTarefa = async (id) => {
  if (!confirm('Tem certeza que deseja excluir esta tarefa?')) return
  try {
    await tarefaService.deletar(id)
    await carregarTarefas()
  } catch (error) {
    console.error('Erro ao deletar tarefa:', error)
  }
}

const limparFiltros = () => {
  filtroStatus.value = ''
  filtroPrioridade.value = ''
  carregarTarefas()
}

const formatarData = (data) => {
  if (!data) return ''
  return new Date(data).toLocaleDateString('pt-BR')
}

onMounted(() => {
  carregarProjeto()
  carregarTarefas()
})
</script>
