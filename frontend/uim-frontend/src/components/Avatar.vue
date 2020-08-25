<template>
  <div class="text-transform：uppercase">
    <q-avatar v-if="error" color="grey" text-color="white" :size="size + 'px'">
      {{firstChar}}
    </q-avatar>
    <q-avatar v-else :size="size + 'px'">
      <q-skeleton type="QAvatar" :size="size + 'px'" v-if="isLoading()"/>
      <q-img v-if="user && user.avatar" :src="url" @load="onLoad" @error="onError"/>
    </q-avatar>
  </div>
</template>

<script>
  export default {
    name: 'Avatar',
    props: ['user', 'size'],
    data() {
      return {
        error: false,
        loading: true,
        avatar_timestamp: this.$uim.avatar_updatedAt
      }
    },
    computed: {
      url() {
        return this.user.avatar(this.size, {t: this.avatar_timestamp})
      },
      firstChar() {
        return this.user.nickname ? this.user.nickname.toUpperCase()[0] : this.user.username.toUpperCase()[0]
      }
    },
    methods: {
      isLoading() {
        return this.user == null || this.user.avatar == null || this.loading
      },
      onError() {
        this.error = true
      },
      onLoad() {
        this.loading = false
      }
    },
    mounted() {
      this.$root.$on('avatar_update', (t) => {
        this.error = false
        this.loading = true
        this.avatar_timestamp = t
      })
    }
  }
</script>
