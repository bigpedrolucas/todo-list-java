import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProjetosView from '../views/ProjetosView.vue'
import ProjetoDetailView from '../views/ProjetoDetailView.vue'
import TarefasView from '../views/TarefasView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/projetos',
    name: 'projetos',
    component: ProjetosView
  },
  {
    path: '/projetos/:id',
    name: 'projeto-detail',
    component: ProjetoDetailView,
    props: true
  },
  {
    path: '/tarefas',
    name: 'tarefas',
    component: TarefasView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
