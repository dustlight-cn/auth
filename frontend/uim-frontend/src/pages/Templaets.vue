<template>
  <q-list dense bordered padding class="rounded-borders">
    <q-item v-for="name in templates" clickable v-ripple
            @click="$router.push({path:'/edit_template',query:{name: name}})">
      <q-item-section>
        {{name}}
      </q-item-section>
    </q-item>
  </q-list>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'Templates',
    data() {
      return {
        templates: []
      }
    },
    mounted() {

      this.$q.loading.show()
      axios.get('/api/template/names')
        .then(res => {
          this.templates = res
        }).catch(e => {
        console.error(e);
      }).finally(() => {

        this.$q.loading.hide()
      })
    }
  }
</script>
