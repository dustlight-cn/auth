const config = {
  api: {
    user: {
      sendCode: {
        email: {
          register: 'user/code/email/register',
          resetPassword: 'user/code/email/resetPwd'
        }
      },
      verify: {
        email: {
          register: 'user/verify/email/register',
          resetPassword: 'user/verify/email/resetPwd'
        }
      },
      exists: {
        username: 'user/exists/username',
        email: 'user/exists/email',
        phone: 'user/exists/phone',
      },
      login: 'user/login',
      register: 'user/register',
      logout: 'user/logout',
      changeEmail: 'user/reset/email',
      resetPasswordByEmail: 'user/reset/email/password',
      resetNickname: 'user/reset/nickname',
      resetGender: 'user/reset/gender',
      resetAvatar: 'user/reset/avatar',
      userDetails: 'user/details/',
      usersDetails: 'user/details',
      userAvatar: 'user/avatar/',
      currentUserDetails: 'user/details',
      applyForDeveloper: 'user/applyForDeveloper'
    }
  }
}

export default config
