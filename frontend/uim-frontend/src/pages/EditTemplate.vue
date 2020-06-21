<template>
  <q-form
    style="padding: 16px"
    @submit="OnSubmit">
    <h4>{{template.name}}</h4>
    <q-editor
      filled
      type="textarea"

      v-model="template.text"/>

    <q-input
      filled
      type="textarea"
      v-model="template.text"/>
    <q-btn type="submit" label="Submit"/>
  </q-form>
</template>

<script>
  import axios from 'axios'
  import qs from 'qs'

  export default {
    name: 'EditTemplate',
    data() {
      return {
        template: {
          name: "",
          text: ""
        }
      }
    },
    methods: {
      OnSubmit() {

        this.$q.loading.show()
        axios.post('/api/template/text/' + encodeURIComponent(this.$route.query.name)
          , qs.stringify({text: this.template.text}))
          .then(res => {
          }).catch(e => {
          console.error(e);
        }).finally(() => {
          this.$q.loading.hide()
        })
      }
    },
    mounted() {
      if (this.$route.query.name) {
        this.template.name = this.$route.query.name
        this.$q.loading.show()
        axios.get('/api/template/text/' + encodeURIComponent(this.$route.query.name))
          .then(res => {
            this.template.text = res;
          }).catch(e => {
          console.error(e)
        }).finally(() => {
          this.$q.loading.hide()
        })
      }

    }
  }
</script>
