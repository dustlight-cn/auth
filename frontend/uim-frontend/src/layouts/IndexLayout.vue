<template>
  <q-layout view="hHh Lpr lff" class="">
    <q-header class="bg-white text-black text-white shadow-2">
      <q-toolbar>
        <q-btn to="/" flat round dense icon="home"/>
        <q-toolbar-title>
          统一身份管理系统
        </q-toolbar-title>
        <q-skeleton v-if="user.username == null" size="32px" type="QAvatar"/>
        <q-btn v-else flat round>
          <q-avatar size="32px">
            <img :src="user.avatar(32)"  onerror="javascript:this.src='/statics/img/avatar.jpg'">
          </q-avatar>
          <q-menu>
            <q-list style="min-width: 100px">
              <q-item>
                <q-item-section>
                  <q-chip>
                    <q-avatar size="32px">
                      <img :src="user.avatar(32)"  onerror="javascript:this.src='/statics/img/avatar.jpg'">
                    </q-avatar>
                    {{nickname}}
                  </q-chip>
                </q-item-section>
              </q-item>
              <q-separator/>
              <q-item to="/manage/info" clickable v-close-popup>
                <q-item-section>管理</q-item-section>
              </q-item>
              <q-item @click="logout" clickable v-close-popup>
                <q-item-section>注销</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>

      </q-toolbar>

    </q-header>


    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script>
  import axios from "axios";

  export default {
    name: "IndexLayout",
    data() {
      return {
        user: {}
      }
    },
    computed: {
      nickname() {
        return this.user.nickname ? this.user.nickname : this.user.username
      },
      email() {
        return this.user.email
      }
    },
    mounted() {
      axios.get("/api/user/details")
        .then(res => {
          this.user = res;
          this.user.avatar = (size) => {
            return "/api/user/avatar/" + res.uid + (size == null ? "" : "?size=" + size)
          }
        }).finally(() => {
        this.loading = false
      })
    },
    methods: {
      logout() {
        axios.get("/api/user/logout").finally(() => {
          this.$router.go(0);
        })
      }
    }
  }
</script>

<style scoped>

</style>
