const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {
        path: '',
        component: () => import('pages/Index.vue')
      }, {
        path: 'info',
        component: () => import('pages/UserInfo.vue')
      }, {
        path: 'info/change_email',
        component: () => import('pages/ChangeEmail.vue')
      }, {
        path: 'templates',
        component: () => import('pages/Templaets.vue')
      }, {
        path: 'clients',
        component: () => import('pages/Clients.vue')
      }, {
        path: 'clients/create',
        component: () => import('pages/CreateClient.vue')
      }, {
        path: 'authorities',
        component: () => import('pages/Authorities.vue')
      }, {
        path: 'roles',
        component: () => import('pages/Roles.vue')
      }, {
        path: 'scopes',
        component: () => import('pages/Scopes.vue')
      }
    ]
  }, {
    path: '/login',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Login')}
    ]
  }, {
    path: '/password',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/ResetPassword')}
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
    path: '/error/403',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Error403.vue')}
    ]
  }
]

// Always leave this as last one
if (process.env.MODE !== 'ssr') {
  routes.push({
    path: '*',
    component: () => import('layouts/ClearLayout.vue'),
    children: [
      {path: '', component: () => import('pages/Error404.vue')}
    ]
  })
}

export default routes
