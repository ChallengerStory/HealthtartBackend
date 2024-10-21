import { createRouter, createWebHistory } from 'vue-router';


import InbodyRoutes from './inbody';
import RoutineRoutes from './routine';
import MainPage from '@/views/MainPage.vue';

const routes = [
{
  // welcome or main 페이지
  path: "/", component: MainPage
},
...InbodyRoutes,
...RoutineRoutes,
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
