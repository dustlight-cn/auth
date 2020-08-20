<template>
  <div
    style="margin:0 auto;max-width: 800px"
    class="q-pa-md">
    <h4>密码重置</h4>
    <q-stepper
      v-model="step"
      ref="stepper"
      color="primary"
      flat
      vertical
      animated
    >
      <q-step
        :name="1"
        title="电子邮箱"
        icon="settings"
        :done="step > 1"
      >
        <q-form
          @submit="sendEmailCode"
          class="q-gutter-y-md column"
          style="margin:0 auto;max-width: 500px">
          <q-input
            label="请输入您的电子邮箱"
            hint="该邮箱为您注册时绑定的邮箱" filled v-model="email" type="email">
            <template v-slot:before>
              <q-icon name="mail"/>
            </template>
          </q-input>

          <div style="height: 100px"/>
          <q-stepper-navigation class="absolute-bottom-right">
            <q-btn type="submit" color="primary" label="发送验证码"/>
          </q-stepper-navigation>
        </q-form>
      </q-step>

      <q-step
        :name="2"
        title="邮箱验证"
        icon="email"
        :done="step > 2"
      >
        <q-form
          @submit="verifyEmail"
          class="q-gutter-y-md column"
          style="margin:0 auto;max-width: 500px">
          <q-input
            label="请输入验证码"
            hint="验证码已通过邮箱发送" filled v-model="code">
          </q-input>

          <div style="height: 100px"/>
          <q-stepper-navigation class="absolute-bottom-right">
            <q-btn type="submit" color="primary" label="邮箱验证"/>
            <q-btn flat @click="step = 1" color="primary" label="返回" class="q-ml-sm"/>
          </q-stepper-navigation>
        </q-form>
      </q-step>

      <q-step
        :name="3"
        title="设置新密码"
        icon="person"
      >
        <q-form
          @submit="resetPwd"
          class="q-gutter-md"
          style="margin:0 auto;max-width: 500px"
        >
          <q-input
            type="password"
            label="请输入新密码"
            hint="密码长度至少为6" filled v-model="password"
            lazy-rules
            :rules="[ val => val && val.length >= 6 || '密码长度至少为6']">

          </q-input>
          <q-input
            type="password"
            label="请确认密码"
            hint="两次输入的密码必须一致"
            filled v-model="rePassword"
            reactive-rules
            :rules="[
            val => password == rePassword || '两次输入的密码不一致']">
          </q-input>

          <div style="height: 100px"/>
          <q-stepper-navigation class="absolute-bottom-right">
            <q-btn type="submit" color="primary" label="重置密码"/>
            <q-btn flat @click="step = 2" color="primary" label="返回" class="q-ml-sm"/>
          </q-stepper-navigation>
        </q-form>
      </q-step>

    </q-stepper>
  </div>
</template>

<script>
  import qs from 'qs'

  export default {
    name: 'ResetPassword',
    data() {
      return {
        step: 1,
        email: "",
        code: "",
        username: "",
        password: "",
        rePassword: ""
      }
    },
    methods: {
      sendEmailCode() {
        let data = {email: this.email}
        this.$q.loading.show()
        this.$uim.ax.post("api/user/code/email/resetPwd", qs.stringify(data))
          .then(res => {
            this.step = 2;
          }).catch(e => {
          console.error(e);
        }).finally(() => {
          this.$q.loading.hide()
        })
      },
      verifyEmail() {
        let data = {email: this.email, code: this.code}
        this.$q.loading.show()
        this.$uim.ax.post("api/user/verify/email/resetPwd", qs.stringify(data))
          .then(res => {
            this.step = 3;
          }).catch(e => {
          console.error(e);
        }).finally(() => {
          this.$q.loading.hide()
        })
      },
      resetPwd() {
        if (this.rePassword != this.password) {
          console.error("password error");
          return;
        }
        let data = {email: this.email, password: this.password}
        this.$q.loading.show()
        this.$uim.ax.post("api/user/reset/email/password", qs.stringify(data))
          .then(res => {
            if (this.$route.query.redirect_uri)
              location.href = this.$route.query.redirect_uri
            else
              location.href = '/Login'
          }).catch(e => {
          console.error(e);
        }).finally(() => {
          this.$q.loading.hide()
        })
      }
    }
  }
</script>
