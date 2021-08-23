<template>
  <q-page padding>
    <require-authorization>
      <template v-slot="{user}">
        {{ "", user_ != user && (user_ == null || user == null || user_.uid != user.uid) ? user_ = user : null }}
        <q-card bordered flat class="q-pa-md q-mt-md">
          <div>
            <q-item>
              <q-item-section class="text-h6 q-pb-md">
                {{ $tt($options, "userList") }}
                <div class="text-caption">
                  {{ $tt($options, "userListDesc") }}
                </div>
              </q-item-section>
              <q-item-section v-if="hasCreateUserPermission" side>
                <q-btn color="accent" :to="{name:'new-user'}" :label="$t('create')"/>
              </q-item-section>
            </q-item>
          </div>
          <div class="q-pa-sm" align="center" style="max-width: 400px; margin: 0 auto">
            <q-input
              v-model="search.keywords"
              debounce="500"
              filled
              clearable
              dense
              color="accent"
              :loading="searching"
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
                v-for="user in data" :key="user.uid"
                appear
                enter-active-class="animated fadeIn"
                leave-active-class="animated fadeOut">
                <q-item clickable v-ripple
                        :to="{name:'user',params:{id:user.uid}}">
                  <q-item-section class="q-pr-sm" style="min-width: 0px;" avatar>
                    <avatar :user="user"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label>
                      {{ user.nickname && user.nickname.trim() ? user.nickname.trim() : user.username }}
                    </q-item-label>
                    <q-item-label v-if="user.email" caption style="word-break: break-all;">
                      <q-icon name="email"/>
                      <span> {{ user.email }}</span>
                    </q-item-label>
                    <q-item-label v-if="user.phone" caption style="word-break: break-all;">
                      <q-icon name="phone"/>
                      <span> {{ user.phone }}</span>
                    </q-item-label>
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
                v-if="count>0"
                color="accent"
                v-model="search.page"
                :max="Math.ceil(count / limit)"
                :input="true"/>
              <div class="text-caption text-grey" v-else>
                {{ $t("noSearchResults") }}
              </div>
            </q-card-actions>
          </div>
        </q-card>
      </template>
    </require-authorization>
  </q-page>
</template>

<script>
import RequireAuthorization from "../../components/common/RequireAuthorization";
import Avatar from "../../components/api/Avatar";

export default {
  name: "Users",
  components: {Avatar, RequireAuthorization},
  data() {
    return {
      user_: null,
      search: this.loadSearch() || {
        keywords: "",
        page: 1
      },
      data: [],
      searching: false,
      limit: 10,
      count: 0
    }
  },
  watch: {
    user_() {
      if (this.user_ && this.user_.uid) {
        if (this.search.keywords == null)
          this.search.keywords = "";
        this.doSearchUser(false);
      }
    },
    "search.keywords"() {
      if (this.search.keywords == null)
        this.search.keywords = "";
      this.saveSearch();
      this.doSearchUser(true);
    },
    "search.page"() {
      this.saveSearch();
      this.doSearchUser(false);
    }
  },
  computed: {
    hasCreateUserPermission() {
      return this.hasPermission("CREATE_USER");
    }
  },
  methods: {
    hasPermission(authority) {
      return this.user_ && this.user_.authorities && this.user_.authorities.indexOf(authority) >= 0;
    },
    saveSearch() {
      this.$q.sessionStorage.set("users_search", this.search);
    },
    loadSearch() {
      return this.$q.sessionStorage.getItem("users_search");
    },
    doSearchUser(refresh) {
      if (this.searching)
        return;
      if (refresh)
        this.search.page = 1;
      this.searching = true;
      let k = this.search.keywords;
      this.$usersApi.getUsers(null,
        k,
        this.limit * (this.search.page - 1),
        this.limit,
        []
      ).then(res => {
        this.count = res.data.count;
        this.data = res.data.data;
      }).finally(() => {
        this.searching = false;
        if (k != this.search.keywords)
          this.doSearchUser(refresh);
      })
    }
  }
}
</script>

<style scoped>

</style>
