import {RouteConfig} from 'vue-router';

const routes: RouteConfig[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {path: '', name: "Index", component: () => import('pages/Index.vue')},
      {path: 'personal-info', name: "personal-info", component: () => import('pages/main/PersonalInfo.vue')},
      {path: 'clients', name: "clients", component: () => import('pages/main/Clients.vue')},
      {path: 'users', name: "users", component: () => import('pages/main/Users.vue')},
      {path: 'system-settings', name: "system-settings", component: () => import('pages/main/SystemSettings.vue')}
    ]
  },
  {
    path: '/',
    component: () => import('layouts/ConnectLayout.vue'),
    children: [
      {path: 'login', name: "login", component: () => import('pages/connect/SignIn.vue')},
      {path: 'join', name: "join", component: () => import('pages/connect/SignUp.vue')},
      {path: 'authorize', name: "authorize", component: () => import('pages/connect/Authorize.vue')},
      {path: 'reset-password', name: "reset-password", component: () => import('pages/connect/ResetPassword.vue')}
    ]
  },
  {
    path: '/',
    component: () => import('layouts/DetailsLayout.vue'),
    children: [
      {path: 'personal-info/nickname', name: "nickname", component: () => import('pages/edit/Nickname.vue')},
      {path: 'personal-info/gender', name: "gender", component: () => import('pages/edit/Gender.vue')},
      {path: 'personal-info/password', name: "password", component: () => import('pages/edit/Password.vue')},
      {path: 'personal-info/avatar', name: "avatar", component: () => import('pages/edit/Avatar.vue')},
      {path: 'personal-info/email', name: "email", component: () => import('pages/edit/Email.vue')},

      {path: 'clients/new', name: "new-client", component: () => import('pages/edit/NewClient.vue')},
      {path: 'client/:id', name: "client", component: () => import ('pages/edit/Client.vue')},

      {path: 'users/new', name: "new-user", component: () => import('pages/edit/NewUser.vue')},
      {path: 'user/:id', name: "user", component: () => import('pages/edit/User.vue')}
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
