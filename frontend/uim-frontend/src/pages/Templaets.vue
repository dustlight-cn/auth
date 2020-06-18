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
      axios.get('/api/template/names')
        .then(res => {
          let data = res.data;
          if (data.code == 200)
            this.templates = data.data;
          else
            throw new Error(data.msg + "," + data.data);
        }).catch(e => {
        console.error(e);
      })
    }
  }
</script>
