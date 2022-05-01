import {createRouter, createWebHashHistory} from 'vue-router'
import ShowAllEvent from '../components/ShowAllEvent.vue'
import HomePage from '../components/Home.vue'

const routes = [
    { path: '/', component: HomePage },
    { path: '/ShowAllEvent', component: ShowAllEvent }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes, // short for `routes: routes`
})
export default router;