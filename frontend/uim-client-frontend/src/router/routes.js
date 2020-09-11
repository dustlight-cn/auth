const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Index.vue')}
    ]
  },
  {
    path: '/login/',
    component: () => import('layouts/ClearLayout'),
    children: [
      {path: '', component: () => import('pages/Login')},
      {path: 'code/*', component: () => import('pages/LoginCode')}
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
