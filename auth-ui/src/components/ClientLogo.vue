<template>
  <div class="text-transform：uppercase">
    <q-avatar rounded v-if="error || client != null && client.cid != null && client.logo == null" color="grey"
              text-color="white"
              :size="(size?size:32) + 'px'">
      {{ firstChar }}
    </q-avatar>
    <q-avatar rounded v-else :size="(size?size:32) + 'px'">
      <q-skeleton square type="QAvatar" :size="(size?size:32) + 'px'" v-if="isLoading()"/>
      <q-img v-if="client && client.logo" :src="client.logo" @load="onLoad" @error="onError"/>
    </q-avatar>
  </div>
</template>

<script>
export default {
  name: 'ClientLogo',
  props: {
    size: Number | String,
    client: Object
  },
  data() {
    return {
      error: false,
      loading: true
    }
  },
  computed: {
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
