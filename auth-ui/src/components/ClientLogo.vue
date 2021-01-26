<template>
  <div class="text-transform：uppercase">
    <q-avatar rounded v-if="error || client != null && client.cid != null && client.logo == null && src == null"
              color="grey"
              text-color="white"
              :size="computedSize">
      {{ firstChar }}
    </q-avatar>
    <q-avatar rounded v-else :size="computedSize">
      <q-skeleton square type="QAvatar" :size="computedSize" v-if="isLoading()"/>
      <q-img :width="computedSize" :height="computedSize" v-if="client && client.logo || src" :src="src || client.logo"
             @load="onLoad" @error="onError"/>
    </q-avatar>
  </div>
</template>

<script>
export default {
  name: 'ClientLogo',
  props: {
    size: Number | String,
    client: Object,
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
      return this.client.name.toUpperCase()[0];
    }
  },
  methods: {
    isLoading() {
      return this.client == null || this.client.cid == null || this.client.logo != null && this.loading;
    },
    onError() {
      this.error = true;
    },
    onLoad() {
      this.loading = false;
    }
  },
  watch: {
    src() {
      this.error = false
      this.loading = true
    }
  },
  mounted() {
    this.$root.$on('client_logo_update', (clientId, t) => {
      if (this.client == null || clientId != this.client.clientId)
        return;
      this.error = false;
      this.loading = true;
    })
  }
}
</script>
