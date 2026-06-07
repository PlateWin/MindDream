import axios from 'axios'

const api = axios.create({
    baseURL: '/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
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
        return response.data
    },
    error => {
        console.error('API Error:', error)
        return Promise.reject(error)
    }
)

// 梦境相关 API
export const dreamAPI = {
    // 获取梦境列表
    getDreams(page = 0, size = 10, sortBy = 'createdAt', sortOrder = 'desc') {
        return api.get('/dreams', { params: { page, size, sortBy, sortOrder } })
    },

    // 获取梦境详情
    getDreamById(id) {
        return api.get(`/dreams/${id}`)
    },

    // 高级搜索
    searchDreams(searchRequest) {
        return api.post('/dreams/search', searchRequest)
    },

    // 随机获取梦境
    getRandomDreams(limit = 1) {
        return api.get('/dreams/random', { params: { limit } })
    },

    // 获取总数
    getTotalCount() {
        return api.get('/dreams/count')
    },

    // 创建梦境
    createDream(dream) {
        return api.post('/dreams', dream)
    },

    // 删除梦境
    deleteDream(id) {
        return api.delete(`/dreams/${id}`)
    }
}

// 统计相关 API
export const statisticsAPI = {
    // 获取总体统计
    getOverview() {
        return api.get('/statistics/overview')
    },

    // 获取性别分布
    getGenderDistribution() {
        return api.get('/statistics/gender')
    },

    // 获取年龄分布
    getAgeDistribution() {
        return api.get('/statistics/age')
    },

    // 获取情感分布
    getEmotionDistribution() {
        return api.get('/statistics/emotions')
    },

    // 获取情感光谱（全部情感 + 维度分类）
    getEmotionSpectrum() {
        return api.get('/statistics/spectrum')
    },

    // 获取性别×情感交叉分析
    getGenderEmotionCross() {
        return api.get('/statistics/gender-emotions')
    },

    // 获取角色分布
    getCharacterDistribution() {
        return api.get('/statistics/characters')
    }
}

export default api
