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

      <div class="absolute-bottom-right q-mr-lg">
        <q-btn label="注册" color="primary" flat class="q-ml-sm"
               :to="{path:'register',query: {redirect_uri: $route.fullPath}}"/>
        <q-btn label="登录" type="submit" color="primary"/>
      </div>
      <div class="absolute-bottom-left">
        <q-btn label="忘记密码？" color="primary" flat class="q-ml-sm"
               :to="{path: 'password',query:{redirect_uri: $route.fullPath}}"/>
      </div>
    </q-form>

  </q-page>
</template>

<script>
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
        this.$q.loading.show()
        this.$uim.user.login(this.account, this.password)
          .then(res => location.href = this.$route.query.redirect_uri ? this.$route.query.redirect_uri : location.href = '.')
          .finally(() => this.$q.loading.hide())
      }
    }
  }
</script>
