<template>
  <div>
    <q-scroll-area style="height:300px; margin-top: 150px; border-right: 1px solid #ddd">
      <q-list padding>
        <q-item clickable v-ripple>
          <q-item-section avatar>
            <q-icon name="person"/>
          </q-item-section>
          <q-item-section>
            账号
          </q-item-section>
        </q-item>

        <q-item v-if="hasAuthority('READ_TEMPLATE')" clickable v-ripple
                @click="$router.push({path:'/manage/templates'})">
          <q-item-section avatar>
            <q-icon name="mail"/>
          </q-item-section>
          <q-item-section>
            邮箱模板
          </q-item-section>
        </q-item>

      </q-list>
    </q-scroll-area>

    <q-img class="absolute-top" src="https://cdn.quasar.dev/img/material.png" style="height: 150px">
      <div class="absolute-bottom bg-transparent">
        <q-avatar size="56px" class="q-mb-sm">
          <q-skeleton v-if="loading" type="QAvatar"/>
          <img v-else src="https://cdn.quasar.dev/img/boy-avatar.png">
        </q-avatar>
        <q-skeleton v-if="loading" type="text"/>
        <div v-else class="text-weight-bold">{{nickname}}</div>
        <q-skeleton v-if="loading" type="text"/>
        <div v-else>{{email}}</div>
      </div>
    </q-img>
  </div>
</template>

<script>
  import axios from 'axios'

  export default {
    name: 'MainMenu',
    data() {
      return {
        user: {},
        authorities: [],
        loading: true
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
          if (res) {
            this.authorities = [];
            res.authorities.forEach(a => {
              this.authorities.push(a.authority);
            })
          }
        }).finally(() => {
        this.loading = false
      })
    },
    methods: {
      hasAuthority(authority) {
        return this.authorities.indexOf(authority) >= 0;
      }
    }
  }
</script>
