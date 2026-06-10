<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Projetos</h1>
        <p class="text-gray-600">Gerencie seus projetos</p>
      </div>
      <button @click="showModal = true" class="btn-primary">
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
        </svg>
        Novo Projeto
      </button>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="flex justify-center py-12">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
    </div>

    <!-- Projetos Grid -->
    <div v-else-if="projetos.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div 
        v-for="projeto in projetos" 
        :key="projeto.id"
        class="card hover:shadow-lg transition-shadow cursor-pointer"
        @click="verProjeto(projeto.id)"
      >
        <div class="p-6">
          <div class="flex justify-between items-start mb-4">
            <div class="p-2 bg-primary-100 rounded-lg">
              <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z"/>
              </svg>
            </div>
            <div class="flex gap-2">
              <button 
                @click.stop="editarProjeto(projeto)"
                class="p-1 text-gray-400 hover:text-primary-600"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                </svg>
              </button>
              <button 
                @click.stop="confirmarDelecao(projeto)"
                class="p-1 text-gray-400 hover:text-red-600"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                </svg>
              </button>
            </div>
          </div>
          
          <h3 class="text-lg font-semibold text-gray-900 mb-2">{{ projeto.nome }}</h3>
          <p class="text-gray-600 text-sm mb-4 line-clamp-2">{{ projeto.descricao || 'Sem descrição' }}</p>
          
          <div class="flex items-center justify-between text-sm text-gray-500">
            <span>{{ projeto.tarefasCount || 0 }} tarefas</span>
            <span>{{ formatarData(projeto.createdAt) }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="text-center py-12">
      <div class="mx-auto h-12 w-12 text-gray-400">
        <svg class="h-12 w-12" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"/>
        </svg>
      </div>
      <h3 class="mt-2 text-sm font-medium text-gray-900">Nenhum projeto</h3>
      <p class="mt-1 text-sm text-gray-500">Comece criando um novo projeto.</p>
    </div>

    <!-- Modal Criar/Editar -->
    <div v-if="showModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full mx-4">
        <div class="p-6">
          <h3 class="text-lg font-medium text-gray-900 mb-4">
            {{ projetoEditando ? 'Editar Projeto' : 'Novo Projeto' }}
          </h3>
          <form @submit.prevent="salvarProjeto">
            <div class="space-y-4">
              <div>
                <label class="form-label">Nome</label>
                <input 
                  v-model="form.nome" 
                  type="text" 
                  required
                  class="form-input"
                  placeholder="Nome do projeto"
                >
              </div>
              <div>
                <label class="form-label">Descrição</label>
                <textarea 
                  v-model="form.descricao" 
                  rows="3"
                  class="form-input"
                  placeholder="Descrição do projeto (opcional)"
                ></textarea>
              </div>
            </div>
            <div class="mt-6 flex justify-end gap-3">
              <button 
                type="button" 
                @click="fecharModal"
                class="btn-secondary"
              >
                Cancelar
              </button>
              <button 
                type="submit" 
                class="btn-primary"
                :disabled="salvando"
              >
                {{ salvando ? 'Salvando...' : 'Salvar' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Modal Confirmação Deleção -->
    <div v-if="showDeleteModal" class="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full mx-4 p-6">
        <div class="flex items-center justify-center w-12 h-12 mx-auto bg-red-100 rounded-full mb-4">
          <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
          </svg>
        </div>
        <h3 class="text-lg font-medium text-gray-900 text-center mb-2">Confirmar exclusão</h3>
        <p class="text-sm text-gray-500 text-center mb-6">
          Tem certeza que deseja excluir o projeto <strong>{{ projetoDeletando?.nome }}</strong>? 
          Todas as tarefas associadas também serão removidas.
        </p>
        <div class="flex justify-center gap-3">
          <button @click="showDeleteModal = false" class="btn-secondary">
            Cancelar
          </button>
          <button @click="deletarProjeto" class="btn-danger" :disabled="deletando">
            {{ deletando ? 'Excluindo...' : 'Excluir' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { projetoService } from '../services/api'

const router = useRouter()
const projetos = ref([])
const loading = ref(false)
const showModal = ref(false)
const showDeleteModal = ref(false)
const projetoEditando = ref(null)
const projetoDeletando = ref(null)
const salvando = ref(false)
const deletando = ref(false)

const form = ref({
  nome: '',
  descricao: ''
})

const carregarProjetos = async () => {
  loading.value = true
  try {
    const response = await projetoService.listar()
    projetos.value = response.data
  } catch (error) {
    console.error('Erro ao carregar projetos:', error)
    alert('Erro ao carregar projetos')
  } finally {
    loading.value = false
  }
}

const verProjeto = (id) => {
  router.push(`/projetos/${id}`)
}

const editarProjeto = (projeto) => {
  projetoEditando.value = projeto
  form.value = {
    nome: projeto.nome,
    descricao: projeto.descricao || ''
  }
  showModal.value = true
}

const confirmarDelecao = (projeto) => {
  projetoDeletando.value = projeto
  showDeleteModal.value = true
}

const salvarProjeto = async () => {
  salvando.value = true
  try {
    if (projetoEditando.value) {
      await projetoService.atualizar(projetoEditando.value.id, form.value)
    } else {
      await projetoService.criar(form.value)
    }
    await carregarProjetos()
    fecharModal()
  } catch (error) {
    console.error('Erro ao salvar projeto:', error)
    alert('Erro ao salvar projeto')
  } finally {
    salvando.value = false
  }
}

const deletarProjeto = async () => {
  deletando.value = true
  try {
    await projetoService.deletar(projetoDeletando.value.id)
    await carregarProjetos()
    showDeleteModal.value = false
    projetoDeletando.value = null
  } catch (error) {
    console.error('Erro ao deletar projeto:', error)
    alert('Erro ao excluir projeto')
  } finally {
    deletando.value = false
  }
}

const fecharModal = () => {
  showModal.value = false
  projetoEditando.value = null
  form.value = { nome: '', descricao: '' }
}

const formatarData = (data) => {
  if (!data) return ''
  return new Date(data).toLocaleDateString('pt-BR')
}

onMounted(carregarProjetos)
</script>
