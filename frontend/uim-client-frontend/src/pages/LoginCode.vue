<template>
  <q-page>

    <q-inner-loading :showing="loading">
      <q-spinner-gears size="50px" color="primary"/>
    </q-inner-loading>
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
        user: {},
        loading: false
      }
    },
    methods: {
      authorize() {
        this.loading = true
        this.$uclient.authorization(this.client.id, this.client.code, this.client.state)
          .then(r => {
            this.user = r

            let redirect_uri = this.$q.sessionStorage.getItem("login_redirect_uri") || "/"
            this.$q.sessionStorage.remove("login_redirect_uri")
            window.location.href = redirect_uri
          })
          .finally(() => this.loading = false)
      }
    },
    mounted() {
      this.authorize()
    }
  }
</script>

<style scoped>

</style>
