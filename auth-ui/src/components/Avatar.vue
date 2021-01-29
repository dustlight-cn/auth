<template>
  <div>
    <q-avatar v-if="error || user != null && user.uid != null && user.avatar == null && src==null" color="grey"
              text-color="white"
              :size="computedSize">
      {{ firstChar }}
    </q-avatar>
    <q-avatar class="user-avatar" v-else :size="computedSize">
      <q-skeleton type="QAvatar" :size="computedSize" v-if="isLoading()"/>
      <q-img :width="computedSize" :height="computedSize" v-if="user && user.avatar || src" :src="src || user.avatar"
             @load="onLoad"
             @error="onError"/>
    </q-avatar>
  </div>
</template>

<script>
export default {
  name: 'Avatar',
  props: {
    size: Number | String,
    user: Object,
    src: String
  },
  data() {
    return {
      error: false,
      loading: true
    }
  },
  computed: {
    computedSize() {
      return (this.size ? this.size : 32) + 'px'
    },
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
  watch: {
    src() {
      this.error = false
      this.loading = true
    },
    "user.avatar"() {
      this.error = false
      this.loading = true
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
