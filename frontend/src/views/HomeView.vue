<template>
  <div class="space-y-8">
    <!-- Hero -->
    <div class="text-center py-12">
      <h1 class="text-4xl font-bold text-gray-900 mb-4">
        Task Manager
      </h1>
      <p class="text-lg text-gray-600 mb-8">
        Gerencie seus projetos e tarefas com facilidade.
        <span class="text-primary-600">Vue 3 + Spring Boot</span>
      </p>
      <div class="flex justify-center gap-4">
        <router-link to="/projetos" class="btn-primary text-base">
          Ver Projetos
        </router-link>
        <a 
          href="http://localhost:8080/swagger-ui.html" 
          target="_blank"
          class="btn-secondary text-base"
        >
          API Docs
        </a>
      </div>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div class="card p-6">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-blue-100 text-blue-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z"/>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-500">Total de Projetos</p>
            <p class="text-2xl font-bold text-gray-900">{{ stats.projetos }}</p>
          </div>
        </div>
      </div>

      <div class="card p-6">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-yellow-100 text-yellow-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-500">Tarefas Pendentes</p>
            <p class="text-2xl font-bold text-gray-900">{{ stats.pendentes }}</p>
          </div>
        </div>
      </div>

      <div class="card p-6">
        <div class="flex items-center">
          <div class="p-3 rounded-full bg-green-100 text-green-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
          </div>
          <div class="ml-4">
            <p class="text-sm font-medium text-gray-500">Tarefas Concluídas</p>
            <p class="text-2xl font-bold text-gray-900">{{ stats.concluidas }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Quick Actions -->
    <div class="card p-6">
      <h2 class="text-lg font-semibold text-gray-900 mb-4">Ações Rápidas</h2>
      <div class="flex flex-wrap gap-3">
        <router-link to="/projetos" class="btn-primary">
          <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Novo Projeto
        </router-link>
        <router-link to="/tarefas" class="btn-secondary">
          <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
          </svg>
          Ver Tarefas
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { projetoService, tarefaService } from '../services/api'

const stats = ref({
  projetos: 0,
  pendentes: 0,
  concluidas: 0
})

onMounted(async () => {
  try {
    const [projetosRes, tarefasRes] = await Promise.all([
      projetoService.listar(),
      tarefaService.listar()
    ])
    
    stats.value.projetos = projetosRes.data.length
    
    const tarefas = tarefasRes.data
    stats.value.pendentes = tarefas.filter(t => t.status === 'PENDENTE').length
    stats.value.concluidas = tarefas.filter(t => t.status === 'CONCLUIDA').length
  } catch (error) {
    console.error('Erro ao carregar stats:', error)
  }
})
</script>
