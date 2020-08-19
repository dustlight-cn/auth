<template>
  <div>
    <q-avatar v-if="error" color="grey" text-color="white" :size="size + 'px'">
      {{user.nickname[0]}}
    </q-avatar>
    <q-avatar v-else :size="size + 'px'">
      <q-skeleton type="QAvatar" :size="size + 'px'" v-if="isLoading()"/>
      <q-img v-if="user.avatar" :src="url" @load="onLoad" @error="onError"/>
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
        loading: true
      }
    },
    computed: {
      url() {
        return this.user.avatar(this.size)
      }
    },
    methods: {
      isLoading() {
        return this.user == null || this.loading
      },
      onError() {
        this.error = true
      },
      onLoad() {
        this.loading = false
      }
    },
    mounted() {
      this.$root.$on('avatar_update', () => {
        this.error = false
        this.loading = true
      })
    }
  }
</script>
