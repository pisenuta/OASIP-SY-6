import { createApp } from 'vue'
import App from './App.vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';


/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'

/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

/* import specific icons */
import { faEye, faEyeSlash, faFileArrowDown, faTrashCan, faX } from '@fortawesome/free-solid-svg-icons'
import { faGithub } from '@fortawesome/free-brands-svg-icons';

/* add icons to the library */
library.add(faEye, faEyeSlash, faTrashCan, faGithub, faFileArrowDown, faX)

// createApp(App).mount('#app')
import router from './router'
const app = createApp(App)
// Make sure to _use_ the router instance to make the
// whole app router-aware.
app.use(router)
app.component('Datepicker', Datepicker);
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount('#app')


