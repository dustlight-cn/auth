<template>
  <q-page style="margin:0 auto;max-width: 800px">
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
  </q-page>
</template>

<script>
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
        this.$q.loading.show()
        this.$uim.user.sendEmailRegisterCode(this.email)
          .then(res => this.step = 2)
          .finally(() => this.$q.loading.hide())
      },
      verifyEmail() {
        this.$q.loading.show()
        this.$uim.user.verifyEmailCode(this.email, this.code)
          .then(res => this.step = 3)
          .finally(() => this.$q.loading.hide())
      },
      changeEmail() {
        this.$q.loading.show()
        this.$uim.user.updateEmail(this.email)
          .then(res => {
            if (this.$route.query.redirect_uri)
              location.href = this.$route.query.redirect_uri
            else
              location.href = '..'
          })
          .finally(() => this.$q.loading.hide())
      }
    }
  }
</script>
