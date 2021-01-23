<template>
  <q-page padding>
    <require-authorization v-slot="{user,token}">
      <!-- 个人应用 -->
      <q-card bordered flat class="q-pa-md q-mt-md">
        <q-item>
          <q-item-section class="text-h6 q-pb-md">
            {{ $tt($options, "yourClients") }}
            <div class="text-caption">
              {{ $tt($options, "yourClientsDesc") }}
            </div>
          </q-item-section>
          <q-item-section side>
            <q-btn color="accent" :to="{name:'new-client'}" :label="$t('create')"/>
          </q-item-section>
        </q-item>

        <div class="q-pa-sm" align="center" style="max-width: 400px; margin: 0 auto">
          <q-input
            v-model="myClients.keywords"
            debounce="500"
            filled
            dense
            color="accent"
            :placeholder="$t('search')"
          >
            <template v-slot:append>
              <q-icon name="search"/>
            </template>
          </q-input>
        </div>
        {{ "", user_ != user && (user_ == null || user == null || user_.uid != user.uid) ? user_ = user : null }}
        <div v-if="myClients.query">
          <q-item
            v-for="id in myClients.data != null && myClients.data.length > 0?Math.min(myClients.data.length,myClients.limit):myClients.limit"
            :key="id">
            <q-item-section avatar>
              <q-skeleton type="QAvatar"/>
            </q-item-section>
            <q-item-section>
              <q-item-label>
                <q-skeleton type="text"/>
              </q-item-label>
              <q-item-label caption>
                <q-skeleton type="text"/>
              </q-item-label>
            </q-item-section>
          </q-item>
        </div>
        <div v-else>
          <q-list separator>
            <q-item clickable v-ripple v-for="client in myClients.data" :key="client.cid"
                    :to="{name:'client',params:{id:client.cid}}">
              <q-item-section avatar>
                <client-logo :size="45" :client="client"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>{{ client.name }}</q-item-label>
                <q-item-label caption>{{ client.description }}</q-item-label>
              </q-item-section>
              <q-item-section side>
                {{ $util.dateFormat(client.createdAt, "YYYY-mm-dd") }}
              </q-item-section>
            </q-item>
          </q-list>
          <q-card-actions
            align="center">
            <q-pagination
              v-if="myClients.count>0"
              color="accent"
              v-model="myClients.page"
              :max="Math.ceil(myClients.count / myClients.limit)"
              :input="true"/>
            <div class="text-caption text-grey" v-else>
              {{ $t("noSearchResults") }}
            </div>
          </q-card-actions>
        </div>
      </q-card>
    </require-authorization>
  </q-page>
</template>

<script>
import RequireAuthorization from "components/RequireAuthorization.vue";
import ClientLogo from "../../components/ClientLogo";

export default {
  name: "Clients",
  components: {ClientLogo, RequireAuthorization},
  data() {
    return {
      user_: null,
      myClients: {
        keywords: "",
        limit: 10,
        data: [],
        count: 0,
        query: null,
        page: 1
      }
    }
  },
  methods: {
    loadMyClients(uid, refresh) {
      if (refresh) {
        this.myClients.page = 1;
      }
      this.myClients.query = this.$clientApi.getUserClients(uid,
        this.myClients.keywords,
        "",
        (this.myClients.page - 1) * this.myClients.limit,
        this.myClients.limit
      ).then(res => {
        this.myClients.data = res.data.data
        this.myClients.count = res.data.count
        return res
      }).finally(() => {
        this.myClients.query = null
      })
    }
  },
  watch: {
    user_() {
      if (this.user_ && this.user_.uid)
        this.loadMyClients(this.user_.uid, true);
    },
    "myClients.page": function () {
      if (this.user_ && this.user_.uid)
        this.loadMyClients(this.user_.uid, false);
    },
    "myClients.keywords": function () {
      if (this.user_ && this.user_.uid)
        this.loadMyClients(this.user_.uid, true);
    }
  }
}
</script>

<style scoped>

</style>
