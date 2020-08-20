<template>
  <div
    style="margin:0 auto;max-width: 800px"
    class="q-pa-md">
    <h4>更换邮箱</h4>
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
        title="更换邮箱"
        icon="settings"
        :done="step > 1"
      >
        <q-form
          @submit="sendEmailCode"
          class="q-gutter-y-md column"
          style="margin:0 auto;max-width: 500px">
          <q-input
            label="请输入新的电子邮箱"
            hint="该邮箱用于登录和通知" filled v-model="email" type="email">
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
        title="更换邮箱"
        icon="person"
      >
        <q-form
          @submit="changeEmail"
          class="q-gutter-md"
          style="margin:0 auto;max-width: 500px"
        >
          <div>邮箱 "{{email}}" 已通过验证，是否将账户绑定的电子邮箱更换？</div>
          <div style="height: 100px"/>
          <q-stepper-navigation class="absolute-bottom-right">
            <q-btn type="submit" color="primary" label="确认更换"/>
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
    name: 'ChangeEmail',
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
        // this.step = 2;
        // return;
        let data = {email: this.email}
        this.$q.loading.show()
        this.$uim.ax.post("user/code/email/register", qs.stringify(data))
          .then(res => {
            this.step = 2;
          }).catch(e => {
          console.error(e);
        }).finally(() => {
          this.$q.loading.hide()
        })
      },
      verifyEmail() {
        // this.step = 3;
        // return;
        let data = {email: this.email, code: this.code}
        this.$q.loading.show()
        this.$uim.ax.post("user/verify/email/register", qs.stringify(data))
          .then(res => {
            this.step = 3;
          }).catch(e => {
          console.error(e);
        }).finally(() => {
          this.$q.loading.hide()
        })
      },
      changeEmail() {
        let data = {email: this.email}
        this.$q.loading.show()
        this.$uim.ax.post("user/reset/email", qs.stringify(data))
          .then(res => {
            if (this.$route.query.redirect_uri)
              location.href = this.$route.query.redirect_uri
            else
              location.href = '/manage/info'
          }).catch(e => {
          console.error(e);
        }).finally(() => {
          this.$q.loading.hide()
        })
      }
    }
  }
</script>
