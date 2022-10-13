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
import { faEye, faEyeSlash, faTrashCan } from '@fortawesome/free-solid-svg-icons'
import { faGithub } from '@fortawesome/free-brands-svg-icons';

/* add icons to the library */
library.add(faEye, faEyeSlash, faTrashCan, faGithub)

// createApp(App).mount('#app')
import router from './router'
const app = createApp(App)
app.use(router)
app.component('Datepicker', Datepicker);
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount('#app')


