<template>
  <div class="text-transform：uppercase">
    <q-avatar rounded v-if="error" color="grey" text-color="white" :size="size + 'px'">
      {{firstChar}}
    </q-avatar>
    <q-avatar rounded v-else :size="size + 'px'">
      <q-skeleton square type="QAvatar" :size="size + 'px'" v-if="isLoading()"/>
      <q-img v-if="client && client.clientId" :src="url" @load="onLoad" @error="onError"/>
    </q-avatar>
  </div>
</template>

<script>
  export default {
    name: 'ClientImage',
    props: ['client', 'size'],
    data() {
      return {
        error: false,
        loading: true,
        img_timestamp: this.client ? this.$uim.client.clientImageUpdateAt[this.client.clientId] : null
      }
    },
    computed: {
      url() {
        return this.$uim.client.getClientImageUrl(this.client.clientId, this.size, {t: this.img_timestamp})
      },
      firstChar() {
        return this.client.clientName ? this.client.clientName.toUpperCase()[0] : this.client.clientId.toUpperCase()[0]
      }
    },
    methods: {
      isLoading() {
        return this.client == null || this.client.clientId == null || this.loading
      },
      onError() {
        this.error = true
      },
      onLoad() {
        this.loading = false
      }
    },
    mounted() {
      this.$root.$on('client_image_update', (clientId, t) => {
        if (clientId != this.client.clientId)
          return
        this.error = false
        this.loading = true
        this.img_timestamp = t
      })
    }
  }
</script>
