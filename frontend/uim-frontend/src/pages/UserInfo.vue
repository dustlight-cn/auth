<template>
  <div class="q-pa-md">
    <q-list separator>
      <!--      clickable v-ripple-->
      <q-item>
        <q-item-section>
          <q-item-label overline>用户名</q-item-label>
        </q-item-section>
        <q-item-section>
          <q-item-label caption>{{user().username}}</q-item-label>
        </q-item-section>
      </q-item>
      <q-item>
        <q-item-section>
          <q-item-label overline>邮箱</q-item-label>
        </q-item-section>
        <q-item-section>
          <q-item-label caption>{{user().email}}</q-item-label>
        </q-item-section>
      </q-item>
      <q-item clickable v-ripple>
        <q-item-section>
          <q-item-label overline>昵称</q-item-label>
        </q-item-section>
        <q-item-section>
          <q-item-label>{{user().nickname}}</q-item-label>
        </q-item-section>
        <q-popup-edit @save="resetNickname" v-model="user().nickname" buttons label-set="保存" label-cancel="取消">
          <q-input label="昵称" v-model="user().nickname" dense autofocus counter/>
        </q-popup-edit>
      </q-item>
    </q-list>

    <q-inner-loading :showing="loading()">
      <q-spinner-gears size="50px" color="primary"/>
    </q-inner-loading>
  </div>
</template>

<script>
  import axios from 'axios'
  import qs from 'qs'

  export default {
    name: "UserInfo",
    inject: ["user", "loading"],
    data() {
      return {}
    },
    methods: {
      resetNickname(value, initialValue) {
        this.$q.loading.show()
        axios.post("/api/user/reset/nickname", qs.stringify({nickname: value}))
          .catch(e => {
            this.user().nickname = initialValue
          }).finally(() => {
          this.$q.loading.hide()
        })
      }
    }
  }
</script>

<style scoped>

</style>
