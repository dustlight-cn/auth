<template>
  <div style="margin: 0 auto;max-width: 600px">
    <div class="q-pa-lg text-h4">授权</div>
    <transition
      appear
      enter-active-class="animated fadeIn"
      leave-active-class="animated fadeOut"
    >
      <q-form
        class="q-gutter-md q-ma-sm"
        @submit="onSubmit"
        v-show="!loading"
      >

        <input type="hidden" hidden name="user_oauth_approval" value="true"/>
        <q-list>
          <q-item-label header>应用 "{{clientName}}" 申请以下权限：</q-item-label>
          <q-item v-for="scope in scopes" tag="label" v-ripple>
            <q-item-section side top>
              <q-checkbox v-if="scope && scope.approved" disable :name="'scope.'+ scope.scope"
                          v-model="scope.value"/>
              <q-checkbox v-else :name="'scope.'+ scope.scope" v-model="scope.value"/>
            </q-item-section>

            <q-item-section>
              <q-item-label>{{scope.details.description}}</q-item-label>
              <q-item-label v-if="scope.approved" caption>已授权</q-item-label>
            </q-item-section>
          </q-item>

          <q-item-label caption>{{clientDescription}}</q-item-label>
        </q-list>

        <div style="min-height: 100px"/>
        <div class="absolute-bottom-right">
          <q-btn label="授权" type="submit" color="primary"/>
        </div>
      </q-form>
    </transition>
    <q-inner-loading :showing="loading">
      <q-spinner-gears size="50px" color="primary"/>
    </q-inner-loading>
  </div>
</template>

<script>
  import qs from 'qs'

  export default {
    name: 'Authorize',
    data() {
      return {
        loading: true,
        clientName: this.$route.query.client_id || "Unknown",
        clientDescription: "",
        scopes: []
      }
    },
    methods: {
      onSubmit() {
        let data = {
          user_oauth_approval: true
        }
        this.scopes.forEach(scope => {
          data['scope.' + scope.scope] = scope.value;
        })
        this.$q.loading.show()
        this.$uim.ax.post('/oauth/authorize?' + qs.stringify(this.$route.query), qs.stringify(data))
          .then(res => {

            location.href = res;
          }).catch(e => {
          console.error(e);
        }).finally(() => {
          this.$q.loading.hide()
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
      this.$uim.ax.get('/oauth/authorize?' + qs.stringify(data))
        .then(res => {
          this.clientName = res.clientName;
          this.clientDescription = res.description;
          if (res.scopes) {
            for (var i in res.scopes)
              this.scopes.push({
                scope: i,
                approved: res.scopes[i].approved,
                details: res.scopes[i].details,
                value: true
              })
          }
        }).catch(e => {
        console.error(e);
      }).finally(() => this.loading = false)
    }
  }
</script>
