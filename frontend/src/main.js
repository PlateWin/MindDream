import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import './assets/styles/typography.css'
import './assets/styles/static-gradients.css'
import './assets/styles/apple-glass.css'
import './assets/styles/spline-integration.css'
import './assets/styles/main.css'
import './assets/styles/ghibli.css'
import './assets/styles/dream-deep.css'
import './assets/styles/performance.css'
import './assets/styles/apple-unified.css'

// Import Dream Flow Components
import GlassCard from './components/GlassCard.vue'
import DreamButton from './components/DreamButton.vue'
import DreamInput from './components/DreamInput.vue'
import DreamLoader from './components/DreamLoader.vue'
import DreamTag from './components/DreamTag.vue'

const app = createApp(App)

// Register Dream Flow Components Globally
app.component('GlassCard', GlassCard)
app.component('DreamButton', DreamButton)
app.component('DreamInput', DreamInput)
app.component('DreamLoader', DreamLoader)
app.component('DreamTag', DreamTag)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(ElementPlus)
app.use(router)
app.mount('#app')
