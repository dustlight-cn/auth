<template>
  <q-layout view="hHh Lpr lff" class="bg-white text-black">
    <q-header elevated class="bg-white text-black">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="ion-logo-google"
          color="primary"
          aria-label="Home Page"
          to="/"
        />
        <q-separator dark vertical inset/>
        <q-toolbar-title>账号</q-toolbar-title>
        <q-space/>
        <q-btn flat round dense icon="more_vert"/>

        <q-btn flat round>
          <Avatar size="32" :user="user"/>
          <q-menu v-if="!loading">
            <q-list style="min-width: 100px">
              <q-item>
                <q-item-section>
                  <q-chip>
                    <Avatar size="32" :user="user"/>
                    {{user.nickname}}
                  </q-chip>
                </q-item-section>
              </q-item>
              <q-separator/>
              <q-item to="/info" clickable v-close-popup>
                <q-item-section>管理</q-item-section>
              </q-item>
              <q-item @click="user.logout" clickable v-close-popup>
                <q-item-section>退出登录</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>
      </q-toolbar>
      <q-tabs
        v-model="tab"
        inline-label
        align="justify"

        active-color="primary"
        indicator-color="primary"
      >
        <q-route-tab v-for="menu in menus"
                     v-if="hasAuthorities(menu.authorities)"
                     :name="menu.label"
                     :icon="menu.icon"
                     :label="menu.label"
                     :to="menu.link"/>
      </q-tabs>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>
    <q-footer>
      <div class="bg-white text-caption text-grey q-pa-sm">
        <span class="q-pa-sm">粤ICP备17010183号-1</span>
        <span class="q-pa-sm">© Dustlight</span>
      </div>
    </q-footer>
  </q-layout>
</template>

<script>
  import MainMenu from 'components/MainMenu'
  import axios from "axios"
  import MenuConfig from 'components/MenuConfig'
  import Config from 'components/Config'

  import Avatar from "components/Avatar";

  export default {
    name: 'MainLayout',
    components: {
      MainMenu, Avatar
    },
    data() {
      return {
        leftDrawerOpen: false,
        user: {},
        authorities: [],
        loading: true,
        tab: "mails",
        menus: MenuConfig,
        config: Config
      }
    },
    provide() {
      return {
        hasAuthority: this.hasAuthority,
        loading: this.isLoading,
        nickname: this.getNickname,
        email: this.getEmail,
        user: this.getUser,
        roleName: this.getRoleName
      }
    },
    methods: {
      /**
       * 检查当前用户是否拥有权限（数组）
       * @param authorities 权限（数组）
       * @returns {boolean}
       */
      hasAuthorities(authorities) {
        if (authorities != null) {
          for (let i in authorities) {
            let authority = authorities[i]
            if (!this.hasAuthority(authority))
              return false
          }
        }
        return true
      },
      /**
       * 检查当前用户是否拥有权限
       * @param authority 权限
       * @returns {boolean}
       */
      hasAuthority(authority) {
        return this.authorities.indexOf(authority) >= 0;
      },
      getRoleName() {
        return this.user.roleName
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
            if (res) { // 处理用户数据
              this.authorities = [];
              res.authoritiesString.forEach(a => {
                this.authorities.push(a);
              })
              this.user.avatar = (size) => {
                return "/api/user/avatar/" + res.uid + (size == null ? "" : "?size=" + size)
              }
              if (this.user.nickname == null || this.user.nickname.length == 0)
                this.user.nickname = this.user.username
              this.user.logout = () => {
                axios.get("/api/user/logout").finally(() => {
                  this.$router.go(0);
                })
              }
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
