<template>
  <div>
    <h4>权限编辑</h4>
  </div>
</template>

<script>
  import axios from 'axios'
  import qs from 'qs'

  export default {
    name: 'EditAuthorities',
    data() {
      return {
        authorities: []
      }
    },
    methods: {},
    mounted() {
      this.$q.loading.show()
      axios.get('/api/client/authorities')
        .then(res => {
          this.authorities = res;
          console.log(res)
        }).catch(e => {
        console.error(e)
      }).finally(() => {
        this.$q.loading.hide()
      })
    }
  }
</script>
