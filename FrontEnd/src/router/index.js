import {createRouter, createWebHashHistory} from 'vue-router'
import ShowAllEvent from '../components/ShowAllEvent.vue'
import HomePage from '../components/Home.vue'
import AddEvent from '../components/AddEvent.vue'
import AboutUs from '../components/AboutUs.vue'
import Clinic from '../components/Clinic.vue'
import UserList from '../components/UserList.vue'

const routes = [
    { path: '/', component: HomePage },
    { path: '/show-all-events', component: ShowAllEvent },
    { path: '/user', component: UserList },
    { path: '/add-event', component: AddEvent },
    { path: '/about-us', component: AboutUs },
    { path: '/clinic', component: Clinic }
]

const history = createWebHashHistory('/sy6/');
const router = createRouter({
    // history: createWebHashHistory('/sy6/'),
    history, routes // short for `routes: routes`
})
export default router;