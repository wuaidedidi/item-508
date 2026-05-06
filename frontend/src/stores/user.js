import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  async function login(credentials) {
    const res = await api.login(credentials)
    if (res.code === 200) {
      token.value = res.data.token
      user.value = res.data
      localStorage.setItem('token', res.data.token)
      return true
    }
    throw new Error(res.message || '登录失败')
  }

  async function register(userData) {
    const res = await api.register(userData)
    if (res.code === 200) {
      return true
    }
    throw new Error(res.message || '注册失败')
  }

  async function fetchUserInfo() {
    if (!token.value) return
    try {
      const res = await api.getUserInfo()
      if (res.code === 200) {
        user.value = { ...user.value, ...res.data }
      }
    } catch (error) {
      logout()
    }
  }

  async function updateUserInfo(data) {
    const res = await api.updateProfile(data)
    if (res.code === 200) {
      user.value = { ...user.value, ...data }
      return true
    }
    throw new Error(res.message || '更新失败')
  }

  async function changePassword(data) {
    const res = await api.changePassword(data)
    if (res.code === 200) {
      return true
    }
    throw new Error(res.message || '修改密码失败')
  }

  function logout() {
    user.value = null
    token.value = ''
    localStorage.removeItem('token')
  }

  // Initialize user info if token exists
  if (token.value) {
    fetchUserInfo()
  }

  return {
    user,
    token,
    isLoggedIn,
    isAdmin,
    login,
    register,
    fetchUserInfo,
    updateUserInfo,
    changePassword,
    logout
  }
})
