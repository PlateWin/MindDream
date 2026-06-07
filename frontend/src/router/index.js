import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
    },
    {
        path: '/dreams',
        name: 'DreamList',
        component: () => import('../views/DreamList.vue')
    },
    {
        path: '/dreams/:id',
        name: 'DreamDetail',
        component: () => import('../views/DreamDetail.vue')
    },
    {
        path: '/dreams/:id/analyze',
        name: 'AIAnalysis',
        component: () => import('../views/AIAnalysis.vue')
    },
    {
        path: '/search',
        name: 'Search',
        component: () => import('../views/Search.vue')
    },
    {
        path: '/statistics',
        name: 'Statistics',
        component: () => import('../views/Statistics.vue')
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('../views/NotFound.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        }
        return { top: 0, behavior: 'smooth' }
    }
})

export default router
