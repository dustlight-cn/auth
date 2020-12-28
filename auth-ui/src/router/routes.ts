import {RouteConfig} from 'vue-router';

const routes: RouteConfig[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {path: '', name: "Index", component: () => import('pages/Index.vue')},
      {path: 'personal-info', name: "personal-info", component: () => import('pages/main/PersonalInfo.vue')}
    ]
  },
  {
    path: '/',
    component: () => import('layouts/ConnectLayout.vue'),
    children: [
      {path: 'login', name: "login", component: () => import('pages/connect/SignIn.vue')},
      {path: 'join', name: "join", component: () => import('pages/connect/SignUp.vue')},
      {path: 'authorize', name: "authorize", component: () => import('pages/connect/Authorize.vue')},
      {path: 'reset-password', name: "resetPassword", component: () => import('pages/connect/ResetPassword.vue')}
    ]
  },
  {
    path: '/',
    component: () => import('layouts/DetailsLayout.vue'),
    children: [
      {path: 'personal-info/nickname', name: "nickname", component: () => import('pages/edit/Nickname.vue')}
    ]
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '*',
    component: () => import('src/pages/Error404.vue')
  }
];

export default routes;
