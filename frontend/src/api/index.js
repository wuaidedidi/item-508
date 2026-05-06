import request from '@/utils/request'

// Auth APIs
export const login = (data) => request.post('/auth/login', data)
export const register = (data) => request.post('/auth/register', data)
export const getUserInfo = () => request.get('/auth/info')
export const updateProfile = (data) => request.put('/auth/info', data)
export const changePassword = (data) => request.put('/auth/password', data)

// Health Profile APIs
export const getHealthProfile = () => request.get('/health/profile')
export const updateHealthProfile = (data) => request.put('/health/profile', data)

// Weight APIs
export const getWeightRecords = (params) => request.get('/health/weight', { params })
export const getRecentWeight = (limit = 7) => request.get('/health/weight/recent', { params: { limit } })
export const addWeightRecord = (data) => request.post('/health/weight', data)
export const updateWeightRecord = (id, data) => request.put(`/health/weight/${id}`, data)
export const deleteWeightRecord = (id) => request.delete(`/health/weight/${id}`)

// Blood Pressure APIs
export const getBloodPressureRecords = (params) => request.get('/health/blood-pressure', { params })
export const getRecentBloodPressure = (limit = 7) => request.get('/health/blood-pressure/recent', { params: { limit } })
export const addBloodPressureRecord = (data) => request.post('/health/blood-pressure', data)
export const updateBloodPressureRecord = (id, data) => request.put(`/health/blood-pressure/${id}`, data)
export const deleteBloodPressureRecord = (id) => request.delete(`/health/blood-pressure/${id}`)

// Blood Sugar APIs
export const getBloodSugarRecords = (params) => request.get('/health/blood-sugar', { params })
export const getRecentBloodSugar = (limit = 7) => request.get('/health/blood-sugar/recent', { params: { limit } })
export const addBloodSugarRecord = (data) => request.post('/health/blood-sugar', data)
export const updateBloodSugarRecord = (id, data) => request.put(`/health/blood-sugar/${id}`, data)
export const deleteBloodSugarRecord = (id) => request.delete(`/health/blood-sugar/${id}`)

// Heart Rate APIs
export const getHeartRateRecords = (params) => request.get('/health/heart-rate', { params })
export const getRecentHeartRate = (limit = 7) => request.get('/health/heart-rate/recent', { params: { limit } })
export const addHeartRateRecord = (data) => request.post('/health/heart-rate', data)
export const updateHeartRateRecord = (id, data) => request.put(`/health/heart-rate/${id}`, data)
export const deleteHeartRateRecord = (id) => request.delete(`/health/heart-rate/${id}`)

// Appointment APIs
export const getAppointments = (params) => request.get('/appointments', { params })
export const getUpcomingAppointments = (limit = 5) => request.get('/appointments/upcoming', { params: { limit } })
export const createAppointment = (data) => request.post('/appointments', data)
export const updateAppointment = (id, data) => request.put(`/appointments/${id}`, data)
export const cancelAppointment = (id) => request.put(`/appointments/${id}/cancel`)
export const deleteAppointment = (id) => request.delete(`/appointments/${id}`)

// Article APIs
export const getArticles = (params) => request.get('/articles', { params })
export const getRecommendedArticles = (limit = 6) => request.get('/articles/recommended', { params: { limit } })
export const getArticle = (id) => request.get(`/articles/${id}`)

// Admin APIs
export const getStatistics = () => request.get('/admin/statistics')
export const getUsers = (params) => request.get('/admin/users', { params })
export const updateUser = (id, data) => request.put(`/admin/users/${id}`, data)
export const updateUserStatus = (id, status) => request.put(`/admin/users/${id}/status`, { status })
export const deleteUser = (id) => request.delete(`/admin/users/${id}`)
export const getAdminArticles = (params) => request.get('/admin/articles', { params })
export const createArticle = (data) => request.post('/admin/articles', data)
export const updateArticle = (id, data) => request.put(`/admin/articles/${id}`, data)
export const deleteArticle = (id) => request.delete(`/admin/articles/${id}`)

// Default export for backward compatibility
export default {
  login,
  register,
  getUserInfo,
  updateProfile,
  changePassword,
  getHealthProfile,
  updateHealthProfile,
  getWeightRecords,
  getRecentWeight,
  addWeightRecord,
  updateWeightRecord,
  deleteWeightRecord,
  getBloodPressureRecords,
  getRecentBloodPressure,
  addBloodPressureRecord,
  updateBloodPressureRecord,
  deleteBloodPressureRecord,
  getBloodSugarRecords,
  getRecentBloodSugar,
  addBloodSugarRecord,
  updateBloodSugarRecord,
  deleteBloodSugarRecord,
  getHeartRateRecords,
  getRecentHeartRate,
  addHeartRateRecord,
  updateHeartRateRecord,
  deleteHeartRateRecord,
  getAppointments,
  getUpcomingAppointments,
  createAppointment,
  updateAppointment,
  cancelAppointment,
  deleteAppointment,
  getArticles,
  getRecommendedArticles,
  getArticle,
  getStatistics,
  getUsers,
  updateUser,
  updateUserStatus,
  deleteUser,
  getAdminArticles,
  createArticle,
  updateArticle,
  deleteArticle
}
