<template>
  <div class="q-pa-md vertical-middle" style="margin: 0 auto;max-width: 400px">

    <h4>授权</h4>
    <q-form
      class="q-gutter-md"
      @submit="onSubmit"
    >
      <input type="hidden" hidden name="user_oauth_approval" value="true"/>
      <q-list>
        <q-item-label header>应用 "{{clientId}}" 申请以下权限：</q-item-label>
        <q-item v-for="(scope,i) in scopes" tag="label" v-ripple>
          <q-item-section side top>
            <q-checkbox :name="'scope.'+scope" v-model="scopesValues[i]"/>
          </q-item-section>

          <q-item-section>
            <q-item-label>{{scope}}</q-item-label>
            <q-item-label caption>
              权限描述
            </q-item-label>
          </q-item-section>
        </q-item>
      </q-list>

      <div style="min-height: 100px"/>
      <div class="absolute-bottom-right">
        <q-btn label="授权" type="submit" color="primary"/>
      </div>
    </q-form>

  </div>
</template>

<script>
  import axios from 'axios'
  import qs from 'qs'

  export default {
    name: 'Authorize',
    data() {
      return {
        clientId: "unknow",
        scopes: [],
        scopesValues: []
      }
    },
    methods: {
      onSubmit() {
        let data = {
          user_oauth_approval: true
        }
        this.scopes.forEach((val, i) => {
          data['scope.' + val] = this.scopesValues[i];
        })
        console.log(data);
        axios.post('/oauth/authorize?' + qs.stringify(this.$route.query), qs.stringify(data))
          .then(res => {
            let r = res.data;
            console.log(r);
            if (r.code == 200) {
              location.href = r.data;
            } else {
              throw new Error(r.msg + "," + r.data)
            }
          }).catch(e => {
          console.error(e);
        })
      }
    },
    mounted() {
      let data = {
        response_type: this.$route.query.response_type,
        client_id: this.$route.query.client_id,
        redirect_uri: this.$route.query.redirect_uri,
        scope: this.$route.query.scope,
        state: this.$route.query.state
      }
      console.log(qs.stringify(data));
      axios.get('/oauth/authorize?' + qs.stringify(data))
        .then(res => {
          let r = res.data;

          console.log(r.data);
          if (r.code == 200) {
            if (r.data.redirect_uri) {
              location.href = r.data.redirect_uri
              return
            }
            this.clientId = r.data.clientId;
            this.scopes = r.data.scopes;
            this.scopesValues = new Array();
            this.scopes.forEach(val => {
              this.scopesValues.push(true)
            })
          } else if (r.code == 501) {
            this.$router.push({
              path: '/Login',
              query: {redirect_uri: location.href}
            })
          } else {
            throw new Error(r.msg);
          }
        }).catch(e => {
        console.error(e);
      })
    }
  }
</script>
