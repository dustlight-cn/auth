<template>
  <q-page>
    {{user}}
  </q-page>
</template>

<script>
  export default {
    name: "LoginCode",
    data() {
      return {
        client: {
          id: this.$route.path.substring('/login/code/'.length),
          code: this.$route.query.code,
          state: this.$route.query.state
        },
        user: {}
      }
    },
    methods: {
      authorize() {
        this.$uclient.authorization(this.client.id, this.client.code, this.client.state)
          .then(r => {
            this.user = r

            let redirect_uri = this.$q.sessionStorage.getItem("login_redirect_uri") || "/"
            this.$q.sessionStorage.remove("login_redirect_uri")
            window.location.href = redirect_uri
          })
      }
    },
    mounted() {
      this.authorize()
    }
  }
</script>

<style scoped>

</style>
