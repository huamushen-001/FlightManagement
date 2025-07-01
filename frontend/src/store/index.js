import { createStore } from 'vuex'
import { crewMemberApi, flightApi, scheduleApi } from '../api'

/**
 * 全局状态管理，用于存储全局数据，mu:同步修改数据；ac：异步加载数据；ge:快速获取数据
 */
export default createStore({
  state: {
    crewMembers: [],
    flights: [],
    schedules: []
  },
  mutations: {
    SET_CREW_MEMBERS(state, crewMembers) {
      state.crewMembers = crewMembers
    },
    SET_FLIGHTS(state, flights) {
      state.flights = flights
    },
    SET_SCHEDULES(state, schedules) {
      state.schedules = schedules
    }
  },
  actions: {
    async fetchCrewMembers({ commit }) {
      try {
        const response = await crewMemberApi.getPage({ current: 1, size: 1000 })
        commit('SET_CREW_MEMBERS', response.data.data.records || [])
      } catch (error) {
        console.error('获取机组成员失败:', error)
      }
    },
    async fetchFlights({ commit }) {
      try {
        const response = await flightApi.getPage({ current: 1, size: 1000 })
        commit('SET_FLIGHTS', response.data.data.records || [])
      } catch (error) {
        console.error('获取航班信息失败:', error)
      }
    },
    async fetchSchedules({ commit }) {
      try {
        const response = await scheduleApi.getPage({ current: 1, size: 1000 })
        commit('SET_SCHEDULES', response.data.data.records || [])
      } catch (error) {
        console.error('获取排班信息失败:', error)
      }
    }
  },
  getters: {
    crewMembers: state => state.crewMembers,
    flights: state => state.flights,
    schedules: state => state.schedules
  }
}) 