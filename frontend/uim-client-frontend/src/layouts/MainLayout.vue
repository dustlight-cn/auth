<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="home"
          aria-label="Menu"
          to="/"
        />

        <q-toolbar-title>
          Client Template
        </q-toolbar-title>
        <div>
          <q-btn v-if="loading || isLogin" rounded flat dense>
            <Avatar :user="user" size="32"/>
            <q-menu v-if="!loading">
              <div class="text-center q-pa-md">
                <Avatar class="q-ma-sm" size="50" :user="user"/>
                <div class="text-bold">{{user.nickname || user.username}}</div>
                <div class="text-caption">{{user.email}}</div>
              </div>
              <q-list style="min-width: 150px;">
                <q-separator/>
                <q-item v-ripple clickable v-close-popup to="/login">
                  <q-item-section>
                    <q-item-label>
                      切换登录
                    </q-item-label>
                  </q-item-section>
                </q-item>
                <q-item @click="logout" v-ripple clickable v-close-popup>
                  <q-item-section>
                    <q-item-label>退出登录</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
          <q-btn v-else flat label="登录" dense to="/login"/>
        </div>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script>
  import Avatar from "components/Avatar";

  export default {
    name: 'MainLayout',
    components: {Avatar},
    data() {
      return {
        user: null,
        loading: false
      }
    },
    computed: {
      isLogin() {
        return this.user && this.user.uid
      }
    },
    mounted() {
      this.$root.$on('user_update', (user) => {
        this.user = user
      })
      this.loading = true
      this.$uclient.getUserInfo()
        .then(r => this.user = r)
        .finally(() => this.loading = false)
    },
    methods: {
      logout() {
        if (!this.user || !this.user.logout)
          return
        this.user.logout()
          .then(r => {
            this.$root.$emit("user_update", null)
          })
      }
    }
  }
</script>
