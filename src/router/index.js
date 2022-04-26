import {createRouter, createWebHashHistory} from 'vue-router'
import ShowAllEvent from '../components/ShowAllEvent.vue'

const routes = [
    { path: '/ShowAllEvent', component: ShowAllEvent }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes, // short for `routes: routes`
})
export default router;