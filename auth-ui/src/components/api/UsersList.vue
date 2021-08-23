<template>
  <div>
    <div class="q-pa-sm">
      <q-input
        :placeholder="$t('search')"
        v-model="searchUser.key"
        :loading="searchUser.searching"
        debounce="500"
        filled
        clearable
        type="search"
        color="accent" dense>
        <template v-slot:prepend>
          <q-icon name="search"/>
        </template>
      </q-input>
    </div>

    <div style="min-height: 150px;">
      <q-list separator v-if="searchUser.data && searchUser.data.length > 0">
        <transition
          v-for="u in searchUser.data" :key="u.uid"
          appear
          enter-active-class="animated fadeIn"
          leave-active-class="animated fadeOut">
          <q-item style="word-break: break-all;" clickable v-ripple
                  @click="()=>onUserSelected ? onUserSelected(u) : null">
            <q-item-section avatar>
              <slot name="avatar" :user="u">
                <avatar :user="u"/>
              </slot>
            </q-item-section>
            <q-item-section>
              <slot :user="u">
                <q-item-label>{{ u.nickname && u.nickname.trim() ? u.nickname.trim() : u.username }}</q-item-label>
                <q-item-label v-if="u.email" caption>
                  <q-icon name="email"/>
                  <span> {{ u.email }}</span>
                </q-item-label>
                <q-item-label v-if="u.phone" caption>
                  <q-icon name="phone"/>
                  <span> {{ u.phone }}</span>
                </q-item-label>
              </slot>
            </q-item-section>
            <q-item-section side>
              <slot :user="u" name="side"></slot>
            </q-item-section>
          </q-item>
        </transition>
      </q-list>
      <div class="text-caption text-grey q-pa-lg text-center" v-else>
        {{ $t("noSearchResults") }}
      </div>
    </div>
    <q-card-actions align="center">
      <q-pagination
        style="margin: 0 auto;"
        v-if="searchUser.count>0"
        color="accent"
        v-model="searchUser.page"
        :max="Math.ceil(searchUser.count / searchUser.limit)"
        :input="true"/>
    </q-card-actions>
  </div>
</template>

<script>
import Avatar from "./Avatar";

export default {
  name: "UsersList",
  components: {Avatar},
  props: {
    onUserSelected: Function
  },
  data() {
    return {
      searchUser: {
        search: false,
        searching: false,
        key: "",
        page: 1,
        limit: 5,
        data: [],
        count: 0,
        neverSearch: true
      }
    }
  },
  watch: {
    "searchUser.key"() {
      if (this.searchUser.key == null)
        this.searchUser.key = "";
      this.doSearchUser(true);
    },
    "searchUser.page"() {
      this.doSearchUser(false);
    }
  },
  methods: {
    onSearchUser() {
      this.searchUser.search = true;
      if (this.searchUser.neverSearch) {
        this.searchUser.neverSearch = false;
        this.doSearchUser(true);
      }
    },
    doSearchUser(refresh) {
      if (this.searchUser.searching)
        return;
      if (refresh) {
        this.searchUser.page = 1;
      }
      let k = this.searchUser.key;
      this.searchUser.searching = true;
      this.$usersApi.getUsers(null,
        k,
        this.searchUser.limit * (this.searchUser.page - 1),
        this.searchUser.limit,
        [])
        .then(res => {
          this.searchUser.count = res.data.count;
          this.searchUser.data = res.data.data;
          return res;
        })
        .finally(() => {
          this.searchUser.searching = false;
          if (k != this.searchUser.key)
            this.searchUser(refresh);
        });
    }
  },
  mounted() {
    this.onSearchUser()
  }
}
</script>

<style scoped>

</style>
