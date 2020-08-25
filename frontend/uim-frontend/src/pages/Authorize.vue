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
            <ClientImage :client="client" size="128"/>
            <div class="flex flex-center text-subtitle2">
              <div>{{clientName}}</div>
            </div>
            <div class="flex flex-center text-caption">
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
            <q-item dense v-for="(scope,i) in scopes" :key="i" tag="label" v-ripple>
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
                <q-btn label="授权" type="submit" color="primary"/>
              </q-item-section>
            </q-item>
<!--            <div style="min-height: 80px"/>-->
            <div class="q-pb-md text-caption text-grey text-center">
              授权完成后将跳转到
              <div><b>{{query.redirect_uri}}</b></div>
            </div>
          </q-list>
        </q-form>

        <q-list class="q-ma-sm q-mt-lg text-grey" bordered>
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
                {{dateFormat(createdAt,"YYYY-mm-dd")}}
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
        currentUser: null
      }
    },
    computed: {
      provider() {
        return this.nickname && this.nickname.trim() ? this.nickname : this.username
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
      },
      dateFormat(date, fmt) {
        let ret;
        const opt = {
          "Y+": date.getFullYear().toString(),        // 年
          "m+": (date.getMonth() + 1).toString(),     // 月
          "d+": date.getDate().toString(),            // 日
          "H+": date.getHours().toString(),           // 时
          "M+": date.getMinutes().toString(),         // 分
          "S+": date.getSeconds().toString()          // 秒
        };
        for (let k in opt) {
          ret = new RegExp("(" + k + ")").exec(fmt);
          if (ret) {
            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
          }
          ;
        }
        ;
        return fmt;
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
