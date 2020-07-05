<template>
  <q-layout view="hHh Lpr lff" class="shadow-2 rounded-borders">
    <q-header class="bg-blue-grey-6 text-white">
      <q-toolbar>
        <q-toolbar-title>
          <q-btn @click="$router.push({path:'/'})" flat label="统一身份管理系统"/>
        </q-toolbar-title>
        <q-btn flat round>
          <q-avatar size="32px">
            <img src="https://cdn.quasar.dev/img/boy-avatar.png">
          </q-avatar>
          <q-menu>
            <q-list style="min-width: 100px">
              <q-item>
                <q-item-section>
                  <q-chip>
                    <q-avatar size="32px">
                      <img src="https://cdn.quasar.dev/img/boy-avatar.png">
                    </q-avatar>
                    {{nickname}}
                  </q-chip>
                </q-item-section>
              </q-item>
              <q-separator/>
              <q-item clickable v-close-popup @click="$router.push({path:'/manage'})">
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
