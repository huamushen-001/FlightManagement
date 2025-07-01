import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import CrewMembers from '../views/CrewMembers.vue'
import Flights from '../views/Flights.vue'
import Schedules from '../views/Schedules.vue'
import Statistics from '../views/Statistics.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/crew-members',
    name: 'CrewMembers',
    component: CrewMembers
  },
  {
    path: '/flights',
    name: 'Flights',
    component: Flights
  },
  {
    path: '/schedules',
    name: 'Schedules',
    component: Schedules
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Statistics
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router 