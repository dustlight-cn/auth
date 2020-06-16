const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Index.vue')}
    ]
  }, {
    path: '/login',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Login')}
    ]
  }, {
    path: '/register',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Register')}
    ]
  }
]

// Always leave this as last one
if (process.env.MODE !== 'ssr') {
  routes.push({
    path: '*',
    component: () => import('pages/Error404.vue')
  })
}

export default routes
