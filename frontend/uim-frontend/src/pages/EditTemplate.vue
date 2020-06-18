<template>
  <q-form
    style="padding: 16px"
    @submit="OnSubmit">
    <h4>{{template.name}}</h4>
    <q-input
      filled
      type="textarea" v-model="template.text"/>
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
        axios.post('/api/template/text/' + encodeURIComponent(this.$route.query.name)
          , qs.stringify({text: this.template.text}))
          .then(res => {
            let data = res.data;
            if (data.code == 200) {

            } else {
              throw new Error(data.msg + "," + data.data)
            }
          }).catch(e => {
          console.error(e);
        })
      }
    },
    mounted() {
      if (this.$route.query.name) {
        this.template.name = this.$route.query.name
        axios.get('/api/template/text/' + encodeURIComponent(this.$route.query.name))
          .then(res => {
            let data = res.data;
            if (data.code == 200)
              this.template.text = data.data;
            else
              throw new Error(data.msg + "," + data.data);
          }).catch(e => {
          console.error(e);
        })
      }

    }
  }
</script>
