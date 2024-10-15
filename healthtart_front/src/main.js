import { createApp } from 'vue';
import App from './App.vue';
import router from './router/router.js';  // Vue Router 가져오기    

const app = createApp(App);
app.use(router); // Vue router 사용
app.mount('#app');

