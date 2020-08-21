const config = {
  api: {
    user: {
      sendCode: {
        email: {
          register: 'api/user/code/email/register',
          resetPassword: 'api/user/code/email/resetPwd'
        }
      },
      verify: {
        email: {
          register: 'api/user/verify/email/register',
          resetPassword: 'api/user/verify/email/resetPwd'
        }
      },
      exists: {
        username: 'api/user/exists/username',
        email: 'api/user/exists/email',
        phone: 'api/user/exists/phone',
      },
      login: 'api/user/login',
      register: 'api/user/register',
      logout: 'api/user/logout',
      changeEmail: 'api/user/reset/email',
      resetPasswordByEmail: 'api/user/reset/email/password',
      resetNickname: 'api/user/reset/nickname',
      resetGender: 'api/user/reset/gender',
      resetAvatar: 'api/user/reset/avatar',
      userDetails: 'api/user/details/',
      usersDetails: 'api/user/details',
      userAvatar: 'api/user/avatar/',
      currentUserDetails: 'api/user/details',
      applyForDeveloper: 'api/user/applyForDeveloper'
    },
    client: {
      allScopes: 'api/client/scopes',
      allGrantTypes: 'api/client/grant_types',
      allAuthorities: 'api/client/authorities',
      allRoles: 'api/client/roles',

      currentUserClients: 'api/client/clients',
      resetClientSecret: 'api/client/app_secret/',
      createClient: 'api/client/app',
      deleteClient: 'api/client/app/',
      updateClientRedirectUri: 'api/client/app_redirect_uri/',
      updateClientName: 'api/client/app_name/',
      updateClientDescription: 'api/client/app_description/',
      addClientScopes: 'api/client/app_scopes/',
      removeClientScopes: 'api/client/app_scopes/',
      addClientGrantTypes: 'api/client/app_grant_types/',
      removeClientGrantTypes: 'api/client/app_grant_types/',


      allTemplates: 'api/template/list',
      updateTemplateName: 'api/template/name/',
      getTemplateText: 'api/template/text/',
      updateTemplateText: 'api/template/text/',
      deleteTemplate: 'api/template/delete',

      updateAuthority: 'api/client/authority/',
      createAuthority: 'api/client/authority',
      deleteAuthority:'api/client/authority/'
    }
  }
}

export default config
