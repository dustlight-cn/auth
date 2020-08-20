<template>
  <q-page style="margin: 0 auto;max-width: 400px">
    <div class="q-pa-lg text-h4">登录</div>
    <q-form
      @submit="onSubmit"

      class="q-gutter-md q-pa-lg"
    >
      <q-input
        filled
        v-model="account"
        label="请输入账号"
        hint="您的用户名或邮箱"
        lazy-rules
        :rules="[ val => account && (account = account.trim()).length > 0 || '请输入账号']"
      />

      <q-input
        filled
        type="password"
        v-model="password"
        label="请输入密码"
        hint="您的密码"
        lazy-rules
        :rules="[
          val => val && val !== '' || '请输入密码'
        ]"
      />

      <div style="min-height: 100px"/>

      <div class="absolute-bottom-right">
        <q-btn label="注册" @click="register" color="primary" flat class="q-ml-sm"/>
        <q-btn label="登录" type="submit" color="primary"/>
      </div>
      <div class="absolute-bottom-left">
        <q-btn label="忘记密码？" @click="forgot" color="primary" flat class="q-ml-sm"/>
      </div>
    </q-form>

  </q-page>
</template>

<script>
  import qs from 'qs'

  export default {
    name: 'Login',
    data() {
      return {
        account: "",
        password: ""
      }
    },
    methods: {

      onSubmit() {
        let userdata = {username: this.account, password: this.password};

        this.$q.loading.show()
        this.$uim.ax.post("api/user/login", qs.stringify(userdata))
          .then(res => {
            if (this.$route.query.redirect_uri)
              location.href = this.$route.query.redirect_uri
            else
              location.href = '/'
          }).catch(e => {
          console.log(e);
        }).finally(() => {
          this.$q.loading.hide();
        })
      },
      register() {
        this.$router.push({
          path: '/register',
          query: {redirect_uri: location.href}
        })
      },
      forgot() {
        this.$router.push({
          path: '/password',
          query: {redirect_uri: location.href}
        })
      }
    }
  }
</script>
