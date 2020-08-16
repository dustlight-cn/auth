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
      <q-item clickable v-ripple>
        <q-item-section>
          <q-item-label overline>性别</q-item-label>
        </q-item-section>
        <q-item-section>
          <q-item-label v-if="(user().gender != null) && gender[user().gender]"
                        :class="'text-'+gender[user().gender].color">
            <q-icon :name="gender[user().gender].icon"/>
            {{gender[user().gender].name}}
          </q-item-label>
        </q-item-section>
        <q-popup-edit @save="resetGedner" v-model="user().gender" buttons label-set="保存" label-cancel="取消">
          <q-select emit-value map-options option-label="name" option-value="id" v-model="user().gender"
                    :options="gender" label="性别">
            <template v-slot:option="scope">
              <q-item
                v-bind="scope.itemProps"
                v-on="scope.itemEvents"
              >
                <q-item-section avatar>
                  <q-icon :name="scope.opt.icon"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label v-html="scope.opt.name"/>
                </q-item-section>
              </q-item>
            </template>
          </q-select>
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
      return {
        gender: [
          {name: "未知", icon: "ion-help", color: "", id: 0},
          {name: "男", icon: "ion-male", color: "blue", id: 1},
          {name: "女", icon: "ion-female", color: "pink", id: 2},
          {name: "其他", icon: "ion-transgender", color: "grey", id: 3}
        ]
      }
    },
    computed: {
      options() {
        let result = []
        this.gender.forEach(v => {
          result.push(v.name)
        })
        return result
      }
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
      },
      resetGedner(value, initValue) {
        this.$q.loading.show()
        axios.post("/api/user/reset/gender", qs.stringify({gender: value}))
          .catch(e => {
            this.user().gender = initialValue
          }).finally(() => {
          this.$q.loading.hide()
        })
      }
    }
  }
</script>

<style scoped>

</style>
