const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Index.vue')},
      {path: 'personal-info', component: () => import('pages/PersonalInfo')}
    ]
  },
  {
    path: '/',
    component: () => import('layouts/SimpleLayout'),
    children: [
      {path: 'login', component: () => import('pages/connect/Login.vue')},
      {path: 'join', component: () => import('pages/connect/Register.vue')},
      {path: 'authorize', component: () => import('pages/connect/Authorize.vue')},
    ]
  },
  {
    path: '/',
    component: () => import('layouts/ChildLayout.vue'),
    children: [
      // {path: '', component: () => import('pages/children/')}
    ]
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '*',
    component: () => import('pages/Error404.vue')
  }
]

export default routes
