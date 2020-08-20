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
    client:{
      currentUserClients: 'api/client/clients'
    }
  }
}

export default config
