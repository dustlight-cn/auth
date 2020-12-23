<template>
  <div class="text-transform：uppercase">
    <q-avatar v-if="error || user != null && user.uid != null && user.avatar == null" color="grey" text-color="white"
              :size="size?size:32 + 'px'">
      {{ firstChar }}
    </q-avatar>
    <q-avatar v-else :size="size?size:32 + 'px'">
      <q-skeleton type="QAvatar" :size="size?size:32 + 'px'" v-if="isLoading()"/>
      <q-img v-if="user && user.avatar" :src="user.avatar" @load="onLoad" @error="onError"/>
    </q-avatar>
  </div>
</template>

<script>
export default {
  name: 'Avatar',
  props: {
    size: Number | String,
    user: Object
  },
  data() {
    return {
      error: false,
      loading: true
    }
  },
  computed: {
    firstChar() {
      return this.user.nickname ? this.user.nickname.toUpperCase()[0] : this.user.username.toUpperCase()[0]
    }
  },
  methods: {
    isLoading() {
      return this.user == null || this.user.uid == null || this.user.avatar != null && this.loading
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
    })
  }
}
</script>
