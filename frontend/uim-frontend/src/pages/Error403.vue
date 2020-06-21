<template>
  <div class="fixed-center text-center">
    <p>
      <img
        src="~assets/sad.svg"
        style="width:30vw;max-width:150px;"
      >
    </p>
    <p class="text-faded">
      没有访问权限<strong>(403)</strong>
    </p>
    <q-btn
      color="secondary"
      style="width:200px;"
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
