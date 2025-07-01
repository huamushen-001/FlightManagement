import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response
  },
  error => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

// 机组成员相关API
export const crewMemberApi = {
  getPage: (params) => api.get('/crew-member/page', { params }),
  getById: (id) => api.get(`/crew-member/${id}`),
  add: (data) => api.post('/crew-member', data),
  update: (data) => api.put('/crew-member', data),
  delete: (id) => api.delete(`/crew-member/${id}`),
  getByPosition: (position) => api.get(`/crew-member/position/${position}`),
  getByQualification: (qualification) => api.get(`/crew-member/qualification/${qualification}`),
  updateFlightHours: (id, hours) => api.put(`/crew-member/${id}/flight-hours`, null, { params: { hours } })
}

// 航班相关API
export const flightApi = {
  getPage: (params) => api.get('/flight/page', { params }),
  getById: (id) => api.get(`/flight/${id}`),
  add: (data) => api.post('/flight', data),
  update: (data) => api.put('/flight', data),
  delete: (id) => api.delete(`/flight/${id}`),
  getByDateRange: (startDate, endDate) => api.get('/flight/date-range', { params: { startDate, endDate } }),
  getByRoute: (departureAirport, arrivalAirport) => api.get('/flight/route', { params: { departureAirport, arrivalAirport } })
}

// 排班相关API
export const scheduleApi = {
  getPage: (params) => api.get('/schedule/page', { params }),
  getById: (id) => api.get(`/schedule/${id}`),
  add: (data) => api.post('/schedule', data),
  update: (data) => api.put('/schedule', data),
  delete: (id) => api.delete(`/schedule/${id}`),
  getByCrewMemberId: (crewMemberId, startDate, endDate) => api.get(`/schedule/crew-member/${crewMemberId}`, { params: { startDate, endDate } }),
  getByFlightId: (flightId) => api.get(`/schedule/flight/${flightId}`),
  checkTimeConflict: (params) => api.get('/schedule/check-conflict', { params }),
  sumWorkHours: (crewMemberId, startDate, endDate) => api.get(`/schedule/work-hours/${crewMemberId}`, { params: { startDate, endDate } }),
  batchAssign: (data) => api.post('/schedule/batch', data)
}

// Excel导出相关API
export const excelApi = {
  exportCrewMembers: (ids) => api.get('/excel/crew-members', { params: { ids }, responseType: 'blob' }),
  exportFlights: (startDate, endDate) => api.get('/excel/flights', { params: { startDate, endDate }, responseType: 'blob' }),
  exportSchedule: (startDate, endDate) => api.get('/excel/schedule', { params: { startDate, endDate }, responseType: 'blob' }),
  exportStatistics: (startDate, endDate) => api.get('/excel/statistics', { params: { startDate, endDate }, responseType: 'blob' })
}

export default {
  crewMember: crewMemberApi,
  flight: flightApi,
  schedule: scheduleApi,
  excel: excelApi
} 