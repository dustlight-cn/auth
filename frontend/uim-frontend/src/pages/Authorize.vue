<template>
  <div style="margin: 0 auto;max-width: 600px">
    <transition
      appear
      enter-active-class="animated fadeIn"
      leave-active-class="animated fadeOut"
    >
      <div
        class="q-pt-lg"
        v-show="!loading">
        <div class="flex flex-center">
          <div>
            <ClientImage class="flex flex-center" :client="client" size="128"/>
            <div class="flex flex-center text-h6">
              <div>{{clientName}}</div>
            </div>
            <div class="flex flex-center text-caption text-grey">
              <div>{{clientDescription}}</div>
            </div>
          </div>
        </div>
        <q-form
          class="q-ma-sm"
          @submit="onSubmit"
        >
          <input type="hidden" hidden name="user_oauth_approval" value="true"/>
          <q-list bordered>
            <q-item>
              <q-item-section avatar>
                <Avatar :user="currentUser" size="36"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>
                  {{clientName}}
                </q-item-label>
                <q-item-label caption>
                  想要访问您的账号: <b>{{currentUser ? currentUser.nickname : ""}}</b>
                </q-item-label>
              </q-item-section>
            </q-item>

            <q-item dense v-for="(scope,i) in scopes" :key="scope.details.id" tag="label" v-ripple>
              <q-item-section>
                <q-item-label overline>{{scope.details.description}}</q-item-label>
                <q-item-label v-if="scope.approved" caption>已授权</q-item-label>
              </q-item-section>

              <q-item-section side top>
                <q-checkbox v-if="scope && scope.approved" disable :name="'scope.'+ scope.scope"
                            v-model="scope.value"/>
                <q-checkbox v-else :name="'scope.'+ scope.scope" v-model="scope.value"/>
              </q-item-section>
            </q-item>
            <q-separator/>
            <q-item class="q-pt-lg">
              <q-space/>
              <q-item-section side top>
                <q-btn v-if="!isApproved" label="授权" type="submit" color="primary"/>
                <q-btn disable v-else label="正在跳转" color="primary"/>
              </q-item-section>
            </q-item>
            <div class="q-pb-md text-caption text-grey text-center">
              授权完成后将跳转到
              <div><b>{{query.redirect_uri}}</b></div>
            </div>
          </q-list>
        </q-form>

        <q-list class="q-ma-sm q-mt-lg" bordered>
          <q-item>
            <q-item-section>
              <q-item-label overline>
                应用开发者
              </q-item-label>
              <q-item-label caption>
                {{provider}}
              </q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-if="createdAt">
            <q-item-section>
              <q-item-label overline>
                创建时间
              </q-item-label>
              <q-item-label caption>
                {{$util.dateFormat(createdAt,"YYYY-mm-dd")}}
              </q-item-label>
            </q-item-section>
          </q-item>
          <q-item v-if="createdAt">
            <q-item-section>
              <q-item-label overline>
                授权用户
              </q-item-label>
              <q-item-label caption>
                {{UserNumber}}
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </div>

    </transition>
    <q-inner-loading :showing="loading">
      <q-spinner-gears size="50px" color="primary"/>
    </q-inner-loading>
  </div>
</template>

<script>
  import ClientImage from "components/ClientImage";
  import Avatar from "components/Avatar";

  export default {
    name: 'Authorize',
    components: {Avatar, ClientImage},
    data() {
      return {
        loading: true,
        clientName: this.$route.query.client_id || "Unknown",
        clientDescription: "",
        scopes: [],
        query: {
          response_type: this.$route.query.response_type,
          client_id: this.$route.query.client_id,
          redirect_uri: this.$route.query.redirect_uri,
          scope: this.$route.query.scope,
          state: this.$route.query.state
        },
        username: "Unknown",
        nickname: "",
        createdAt: null,
        updatedAt: null,
        client: null,
        currentUser: null,
        userNumber: 0,
        isApproved: false
      }
    },
    computed: {
      provider() {
        return this.nickname && this.nickname.trim() ? this.nickname : this.username
      },
      UserNumber() {
        if (this.userNumber < 1000)
          return this.userNumber
        if (this.userNumber < 1000 * 100)
          return (this.userNumber / 1000).toFixed(1) + " k"
        return (this.userNumber / 1000).toFixed(0) + " k"
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
        this.$uim.authorize(this.query.response_type, this.query.client_id, this.query.redirect_uri, this.query.scope, this.query.state, data)
          .then(res => location.href = res)
          .finally(() => this.$q.loading.hide())
      }
    },
    mounted() {
      this.$uim.preAuthorize(this.query.response_type, this.query.client_id, this.query.redirect_uri, this.query.scope, this.query.state)
        .then(res => {
          this.client = res
          this.clientName = res.clientName;
          this.clientDescription = res.description;
          this.username = res.username
          this.nickname = res.nickname
          this.updatedAt = new Date(res.updatedAt)
          this.createdAt = new Date(res.createdAt)
          this.userNumber = res.userNumber
          this.isApproved = res.isApproved
          if (res.scopes) {
            for (var i in res.scopes)
              this.scopes.push({
                scope: i,
                approved: res.scopes[i].approved,
                details: res.scopes[i].details,
                value: true
              })
          }
          this.$uim.user.getCurrentUserDetails()
            .then((res) => this.currentUser = res)
            .finally(() => this.loading = false)
        })
    }
  }
</script>
