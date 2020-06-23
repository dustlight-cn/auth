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
  }, {
    path: '/authorize',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Authorize')}
    ]
  }, {
    path: '/templates/list',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Templaets.vue')}
    ]
  }, {
    path: '/templates/edit',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/EditTemplate.vue')}
    ]
  }, {
    path: '/client/authorities',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/EditAuthorities.vue')}
    ]
  }, {
    path: '/error/403',
    component: () => import('pages/Error403.vue')
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
