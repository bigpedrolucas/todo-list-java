<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-gray-900">Todas as Tarefas</h1>
      <p class="text-gray-600">Visualize todas as tarefas</p>
    </div>

    <div class="flex flex-wrap gap-3">
      <select v-model="filtros.status" @change="carregar" class="form-input w-auto">
        <option value="">Todos os status</option>
        <option value="PENDENTE">Pendente</option>
        <option value="EM_ANDAMENTO">Em Andamento</option>
        <option value="CONCLUIDA">Concluída</option>
      </select>
      <button v-if="filtros.status" @click="limpar" class="btn-secondary">Limpar</button>
    </div>

    <div v-if="loading" class="flex justify-center py-12">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
    </div>

    <div v-else-if="tarefas.length" class="space-y-3">
      <div v-for="tarefa in tarefas" :key="tarefa.id" class="card p-4">
        <div class="flex items-center gap-3">
          <button @click="toggleStatus(tarefa)" class="flex-shrink-0">
            <div class="w-5 h-5 rounded border-2 flex items-center justify-center"
              :class="tarefa.status === 'CONCLUIDA' ? 'border-green-500 bg-green-500' : 'border-gray-300'">
              <svg v-if="tarefa.status === 'CONCLUIDA'" class="w-3 h-3 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
              </svg>
            </div>
          </button>
          <div class="flex-1">
            <span :class="tarefa.status === 'CONCLUIDA' ? 'line-through text-gray-500' : ''">{{ tarefa.titulo }}</span>
            <span class="text-xs text-gray-500 ml-2">{{ tarefa.projetoNome }}</span>
          </div>
          <span class="px-2 py-1 text-xs rounded" :class="{
            'bg-yellow-100 text-yellow-800': tarefa.status === 'PENDENTE',
            'bg-blue-100 text-blue-800': tarefa.status === 'EM_ANDAMENTO',
            'bg-green-100 text-green-800': tarefa.status === 'CONCLUIDA'
          }">{{ tarefa.statusDescricao }}</span>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-12 text-gray-500">Nenhuma tarefa encontrada</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { tarefaService } from '../services/api'

const tarefas = ref([])
const loading = ref(false)
const filtros = ref({ status: '' })

const carregar = async () => {
  loading.value = true
  const params = filtros.value.status ? { status: filtros.value.status } : {}
  const { data } = await tarefaService.listar(params)
  tarefas.value = data
  loading.value = false
}

const toggleStatus = async (t) => {
  const novo = t.status === 'CONCLUIDA' ? 'PENDENTE' : 'CONCLUIDA'
  await tarefaService.alterarStatus(t.id, novo)
  carregar()
}

const limpar = () => {
  filtros.value.status = ''
  carregar()
}

onMounted(carregar)
</script>
