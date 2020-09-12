<template>
  <div>
    <q-avatar v-if="error || (user && !user.avatarUrl)" color="grey" text-color="white" :size="size + 'px'">
      {{firstChar}}
    </q-avatar>
    <q-avatar v-else :size="size + 'px'">
      <q-skeleton type="QAvatar" :size="size + 'px'" v-if="isLoading"/>
      <q-img v-if="user && user.avatarUrl" :src="url" @load="onLoad" @error="onError"/>
    </q-avatar>
  </div>
</template>

<script>
  export default {
    name: "Avatar",
    props: ['user', 'size'],
    data() {
      return {
        error: false,
        loading: true
      }
    },
    computed: {
      url() {
        if (this.user == null)
          return null
        return this.user.avatarUrl
      },
      firstChar() {
        if (this.user == null)
          return "?"
        let name = this.user.nickname && this.user.nickname.length > 0 ? this.user.nickname : this.user.username
        if (name == null || name.length == 0)
          return "?"
        return name.toUpperCase()[0]
      },
      isLoading() {
        return this.user == null || this.loading
      }
    },
    methods: {
      onError() {
        this.error = true
      },
      onLoad() {
        this.loading = false
      }
    },
    mounted() {
      // this.$root.$on('user_update', (user) => {
      //   this.error = false
      //   this.loading = true
      //   this.user = user
      // })
    }
  }
</script>

<style scoped>

</style>
