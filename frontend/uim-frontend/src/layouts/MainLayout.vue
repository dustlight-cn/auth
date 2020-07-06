<template>
  <q-layout view="hHh Lpr lff" class="shadow-2 rounded-borders">
    <q-header class="bg-primary">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="leftDrawerOpen = !leftDrawerOpen"
        />
        <q-btn to="/" flat round dense icon="home"/>
        <q-toolbar-title>
          统一身份管理系统
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      :width="200"
      :breakpoint="500"
      bordered
      show-if-above
      content-class="bg-grey-3"
    >
      <MainMenu/>
    </q-drawer>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script>
  import MainMenu from 'components/MainMenu'
  import axios from "axios";

  export default {
    name: 'MainLayout',
    components: {
      MainMenu
    },
    data() {
      return {
        leftDrawerOpen: false,
        user: {},
        authorities: [],
        loading: true
      }
    },
    provide() {
      return {
        hasAuthority: this.hasAuthority,
        loading: this.isLoading,
        nickname: this.getNickname,
        email: this.getEmail,
        user: this.getUser
      }
    },
    methods: {
      hasAuthority(authority) {
        return this.authorities.indexOf(authority) >= 0;
      },
      isLoading() {
        return this.loading
      },
      getNickname() {
        return this.user.nickname ? this.user.nickname : this.user.username
      },
      getEmail() {
        return this.user.email
      },
      getUser() {
        return this.user
      },
      loadUserDetails() {
        axios.get("/api/user/details")
          .then(res => {
            this.user = res;
            if (res) {
              this.authorities = [];
              res.authorities.forEach(a => {
                this.authorities.push(a.authority);
              })
            }
          }).finally(() => {
          this.loading = false
        })
      }
    },
    mounted() {
      this.loadUserDetails()
    }
  }
</script>
