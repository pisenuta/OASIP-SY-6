import {createRouter, createWebHashHistory} from 'vue-router'
import ShowAllEvent from '../components/ShowAllEvent.vue'
import HomePage from '../components/Home.vue'
import AddEvent from '../components/AddEvent.vue'

const routes = [
    { path: '/', component: HomePage },
    { path: '/show-all-events', component: ShowAllEvent },
    { path: '/add-event', component: AddEvent }
]

const router = createRouter({
    history: createWebHashHistory('/sy6/'),
    routes, // short for `routes: routes`
})
export default router;