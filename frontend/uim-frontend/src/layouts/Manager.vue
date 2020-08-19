<template>
  <q-layout view="hHh Lpr lff" class="bg-white text-black">

    <!-- 头部 -->
    <q-header class="header text-black shadow-2">

      <!-- 工具栏 -->
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

        <!-- 其他菜单 -->
        <q-btn flat round dense icon="more_vert">
          <q-menu>
            <q-list style="min-width: 150px">
              <q-item v-for="menu in menus.other" :to="menu.link" v-ripple clickable v-close-popup>
                <q-item-section>
                  <q-item-label>
                    <q-icon v-if="menu.icon" :name="menu.icon" size="22px"/>
                    {{menu.label}}
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>

        <!-- 头像菜单 -->
        <q-btn flat round>
          <Avatar size="32" :user="user"/>
          <q-menu v-if="!loading">
            <div class="text-center q-pa-md">
              <Avatar class="q-ma-sm" size="50" :user="user"/>
              <div class="text-bold">{{user.nickname}}</div>
              <div class="text-caption">{{user.email}}</div>
            </div>
            <q-list style="min-width: 150px;">
              <q-separator/>
              <q-item v-for="menu in menus.avatar" :to="menu.link" v-ripple clickable v-close-popup>
                <q-item-section>
                  <q-item-label>
                    <q-icon v-if="menu.icon" size="22px" :name="menu.icon"/>
                    {{menu.label}}
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-item @click="user.logout" v-ripple clickable v-close-popup>
                <q-item-section>退出</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>
      </q-toolbar>


      <!-- 工具栏菜单 -->
      <q-tabs
        v-model="tab"
        inline-label
        align="justify"
        active-color="primary"
        indicator-color="primary"
        v-if="!leftDrawerOpen"
      >
        <q-route-tab v-for="menu in menus.main"
                     v-if="hasAuthorities(menu.authorities)"
                     :name="menu.label"
                     :icon="menu.icon"
                     :label="menu.label"
                     :to="menu.link"/>
      </q-tabs>
    </q-header>

    <!-- 侧边菜单 -->
    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      no-swipe-close
      no-swipe-open
      no-swipe-backdrop
      persistent
      :breakpoint="1023"
      :width="250"
    >
      <q-scroll-area class="fit">
        <q-tabs
          v-model="tab"
          active-color="primary"
          indicator-color="primary"
          vertical
        >
          <q-route-tab v-for="menu in menus.main"
                       v-if="hasAuthorities(menu.authorities)"
                       :name="menu.label"
                       :icon="menu.icon"
                       :label="menu.label"
                       :to="menu.link"/>
        </q-tabs>
      </q-scroll-area>
    </q-drawer>

    <!-- 内容 -->
    <q-page-container>
      <router-view/>
    </q-page-container>

    <!-- 尾部 -->
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
  import qs from 'qs'

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
        tab: "",
        menus: MenuConfig,
        config: Config,
        splitterModel: 20,
        avatar_updatedAt: 0
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
                let p = {
                  size: size,
                  t: this.avatar_updatedAt
                }
                return "/api/user/avatar/" + res.uid + "?" + qs.stringify(p)
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
      this.$root.$on('avatar_update', () => {
        this.avatar_updatedAt = new Date().getTime()
      })
      this.loadUserDetails()
    }
  }
</script>
