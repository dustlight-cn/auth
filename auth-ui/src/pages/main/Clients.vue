<template>
  <q-page padding>
    <require-authorization v-slot="{user,token}">
      {{ "", user_ != user && (user_ == null || user == null || user_.uid != user.uid) ? user_ = user : null }}
      <!-- 个人应用 -->
      <q-card bordered flat class="q-pa-md q-mt-md">
        <q-item>
          <q-item-section class="text-h6 q-pb-md">
            {{ $tt($options, "yourClients") }}
            <div class="text-caption">
              {{ $tt($options, "yourClientsDesc") }}
            </div>
          </q-item-section>
          <q-item-section v-if="hasCreateClientPermission" side>
            <q-btn color="accent" :to="{name:'new-client'}" :label="$t('create')"/>
          </q-item-section>
        </q-item>
        <div class="q-pa-sm" align="center" style="max-width: 400px; margin: 0 auto">
          <q-input
            v-model="myClients.keywords"
            debounce="500"
            filled
            clearable
            dense
            color="accent"
            :loading="myClients.query!=null"
            :placeholder="$t('search')"
          >
            <template v-slot:prepend>
              <q-icon name="search"/>
            </template>
          </q-input>
        </div>
        <div>
          <q-list separator>
            <transition
              v-for="client in myClients.data" :key="client.cid"
              appear
              enter-active-class="animated fadeIn"
              leave-active-class="animated fadeOut">
              <q-item clickable v-ripple :to="{name:'client',params:{id:client.cid},query:{uid:user.uid}}">
                <q-item-section avatar>
                  <client-logo :size="45" :client="client"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label>{{ client.name }}</q-item-label>
                  <q-item-label caption>{{ client.description }}</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-icon name="keyboard_arrow_right"/>
                </q-item-section>
              </q-item>
            </transition>
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

      <!-- 所有应用 -->
      <q-card v-if="hasPermission" bordered flat class="q-pa-md q-mt-md">
        <q-item>
          <q-item-section class="text-h6 q-pb-md">
            {{ $tt($options, "allClients") }}
            <div class="text-caption">
              {{ $tt($options, "allClientsDesc") }}
            </div>
          </q-item-section>
        </q-item>

        <div class="q-pa-sm" align="center" style="max-width: 400px; margin: 0 auto">
          <q-input
            v-model="allClients.keywords"
            debounce="500"
            filled
            clearable
            dense
            color="accent"
            :loading="allClients.query!=null"
            :placeholder="$t('search')"
          >
            <template v-slot:prepend>
              <q-icon name="search"/>
            </template>
          </q-input>
        </div>
        <div>
          <q-list separator>
            <transition
              v-for="client in allClients.data" :key="client.cid"
              appear
              enter-active-class="animated fadeIn"
              leave-active-class="animated fadeOut">
              <q-item clickable v-ripple
                      :to="{name:'client',params:{id:client.cid}}">
                <q-item-section avatar>
                  <client-logo :size="45" :client="client"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label>{{ client.name }}</q-item-label>
                  <q-item-label caption>{{ client.description }}</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-icon name="keyboard_arrow_right"/>
                </q-item-section>
              </q-item>
            </transition>
          </q-list>
          <q-card-actions
            align="center">
            <q-pagination
              v-if="allClients.count>0"
              color="accent"
              v-model="allClients.page"
              :max="Math.ceil(allClients.count / allClients.limit)"
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
import RequireAuthorization from "../../components/common/RequireAuthorization";
import ClientLogo from "../../components/api/ClientLogo";

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
      },
      allClients: {
        keywords: "",
        limit: 10,
        data: [],
        count: 0,
        query: null,
        page: 1
      }
    }
  },
  computed: {
    hasPermission() {
      return this.user_ && this.user_.uid && this.user_.authorities && this.user_.authorities.indexOf('READ_CLIENT') >= 0;
    },
    hasCreateClientPermission() {
      return this.user_ && this.user_.uid && this.user_.authorities && this.user_.authorities.indexOf('CREATE_CLIENT') >= 0;
    }
  },
  methods: {
    loadMyClients(uid, refresh) {
      if (this.myClients.query != null)
        return;
      if (refresh) {
        this.myClients.page = 1;
      }
      let k = this.myClients.keywords;
      this.myClients.query = this.$clientApi.getUserClients(uid,
        k,
        "",
        (this.myClients.page - 1) * this.myClients.limit,
        this.myClients.limit
      ).then(res => {
        this.myClients.data = res.data.data
        this.myClients.count = res.data.count
        return res
      }).finally(() => {
        this.myClients.query = null;
        if (k != this.myClients.keywords)
          this.loadMyClients(uid, refresh);
      })
    },
    loadClients(refresh) {
      if (this.allClients.query != null)
        return;
      if (refresh) {
        this.allClients.page = 1;
      }
      let k = this.allClients.keywords;
      this.allClients.query = this.$clientApi.getClients(
        this.allClients.keywords,
        "",
        (this.allClients.page - 1) * this.allClients.limit,
        this.allClients.limit
      ).then(res => {
        this.allClients.data = res.data.data
        this.allClients.count = res.data.count
        return res
      }).finally(() => {
        this.allClients.query = null;
        if (k != this.allClients.keywords)
          this.loadClients(refresh);
      })
    }
  },
  watch: {
    user_() {
      if (this.user_ && this.user_.uid) {
        this.loadMyClients(this.user_.uid, true);
        if (this.hasPermission)
          this.loadClients(true);
      }
    },
    "myClients.page": function () {
      if (this.user_ && this.user_.uid)
        this.loadMyClients(this.user_.uid, false);
    },
    "myClients.keywords": function () {
      if (this.myClients.keywords == null)
        this.myClients.keywords = "";
      if (this.user_ && this.user_.uid)
        this.loadMyClients(this.user_.uid, true);
    },
    "allClients.page": function () {
      this.loadClients(false);
    },
    "allClients.keywords": function () {
      if (this.allClients.keywords == null)
        this.allClients.keywords = "";
      this.loadClients(true);
    }
  }
}
</script>

<style scoped>

</style>
