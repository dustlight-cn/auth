<template>
  <div class="fixed-center text-center">
    <div>
      <img
        src="~assets/sad.svg"
        style="width:30vw;max-width:150px;"
      >
    </div>
    <div class="text-h5">
      没有访问权限
    </div>
    <div class="text-caption">
      403 Forbidden
    </div>
    <q-btn
      color="primary"
      flat
      @click="login"
      label="登录或切换账号"
    />
  </div>
</template>

<script>
  export default {
    name: 'Error403',
    methods: {
      login() {
        this.$router.push({
          path: "/Login",
          query: {
            redirect_uri: this.$route.query.redirect_uri
          }
        })
      }
    }
  }
</script>
