<template>
  <q-page class="q-pa-sm">
    <q-card>
      <q-card-section>
        <div class="text-h6">个人资料</div>
        <div class="text-caption">您的基本信息</div>
      </q-card-section>
      <q-card-section>
        <q-list separator>
          <q-item @click="uploadAvatar" clickable v-ripple>
            <q-item-section>
              <q-item-label caption>头像</q-item-label>
            </q-item-section>
            <q-item-label>
              <Avatar size="50" :user="user()"/>
            </q-item-label>
          </q-item>
          <q-item clickable v-ripple>
            <q-item-section>
              <q-item-label caption>昵称</q-item-label>
              <q-item-label>{{user().nickname}}</q-item-label>
            </q-item-section>
            <q-item-label>
              <q-btn dense rounded flat icon="keyboard_arrow_right"/>
            </q-item-label>
            <q-popup-edit @save="resetNickname" v-model="user().nickname" buttons label-set="保存" label-cancel="取消">
              <q-input label="昵称" v-model="user().nickname" dense autofocus counter/>
            </q-popup-edit>
          </q-item>
          <q-item clickable v-ripple>
            <q-item-section>
              <q-item-label caption>性别</q-item-label>
              <q-item-label v-if="(user().gender != null) && gender[user().gender]">
                {{gender[user().gender].name}}
                <q-icon size="12px" :color="gender[user().gender].color" :name="gender[user().gender].icon"/>
              </q-item-label>
            </q-item-section>
            <q-item-label>
              <q-btn dense rounded flat icon="keyboard_arrow_right"/>
            </q-item-label>
            <q-popup-edit @save="resetGender" v-model="user().gender" buttons label-set="保存" label-cancel="取消">
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
          <q-item clickable v-ripple :to="resetPwd">
            <q-item-section>
              <q-item-label caption>密码</q-item-label>
              <q-item-label>******</q-item-label>
            </q-item-section>
            <q-item-label>
              <q-btn dense rounded flat icon="keyboard_arrow_right"/>
            </q-item-label>
          </q-item>
        </q-list>
      </q-card-section>
    </q-card>

    <q-card class="q-mt-sm">
      <q-card-section>
        <div class="text-h6">联系方式</div>
        <div class="text-caption">您的联系方式</div>
      </q-card-section>
      <q-card-section>
        <q-list separator>
          <q-item clickable v-ripple to="info/change_email">
            <q-item-section>
              <q-item-label caption>邮箱</q-item-label>
              <q-item-label>{{user().email}}</q-item-label>
            </q-item-section>
            <q-item-label>
              <q-btn dense rounded flat icon="keyboard_arrow_right"/>
            </q-item-label>
          </q-item>
        </q-list>
      </q-card-section>
    </q-card>

    <q-card class="q-mt-sm">
      <q-card-section>
        <div class="text-h6">其他</div>
        <div class="text-caption"></div>
      </q-card-section>
      <q-card-section>
        <q-list separator>
          <q-item>
            <q-item-section>
              <q-item-label caption>用户名</q-item-label>
              <q-item-label overline>{{user().username}}</q-item-label>
            </q-item-section>
          </q-item>
          <q-item>
            <q-item-section>
              <q-item-label caption>角色</q-item-label>
              <q-item-label overline>{{user().roleDescription}}</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-card-section>
    </q-card>

    <q-dialog v-model="uploadAvatarFlag">
      <q-uploader
        :url=uploadAvatarURL
        label="上传头像"
        method="PUT"
        accept=".jpg, image/*"
        style="max-width: 300px"
        auto-upload
        hide-upload-btn
        send-raw=""
        @uploaded="uploadAvatarSuccess"
      >

      </q-uploader>
    </q-dialog>
    <q-inner-loading :showing="loading()">
      <q-spinner-gears size="50px" color="primary"/>
    </q-inner-loading>
  </q-page>
</template>

<script>
  import Avatar from "components/Avatar";

  export default {
    name: "UserInfo",
    components: {Avatar},
    inject: ["user", "loading"],
    data() {
      return {
        gender: [
          {name: "未知", icon: "ion-help", color: "", id: 0},
          {name: "男", icon: "ion-male", color: "light-blue", id: 1},
          {name: "女", icon: "ion-female", color: "pink", id: 2},
          {name: "其他", icon: "ion-transgender", color: "grey", id: 3}
        ],
        uploadAvatarFlag: false,
        uploadAvatarWaiting: false,
        uploadAvatarURL: null
      }
    },
    computed: {
      options() {
        let result = []
        this.gender.forEach(v => {
          result.push(v.name)
        })
        return result
      },
      resetPwd() {
        return '/password?redirect_uri=' + encodeURIComponent(location.href)
      }
    },
    methods: {
      resetNickname(value, initialValue) {
        this.$q.loading.show()
        this.$uim.user.updateNickname(value)
          .catch(e => this.user().nickname = initialValue)
          .finally(() => this.$q.loading.hide())
      },
      resetGender(value, initValue) {
        this.$q.loading.show()
        this.$uim.user.updateGender(value)
          .catch(e => this.user().gender = initValue)
          .finally(() => this.$q.loading.hide())
      },
      uploadAvatar() {
        this.uploadAvatarWaiting = true
        this.uploadAvatarFlag = true
        this.$uim.user.generateUploadAvatarUrl()
          .then(res => this.uploadAvatarURL = res)
          .catch(e => this.uploadAvatarFlag = false)
          .finally(() => this.uploadAvatarWaiting = false)
      },
      uploadAvatarSuccess() {
        let t = new Date().getTime()
        this.$uim.user.notifyAvatarUpdate(t)
        this.$root.$emit("avatar_update", t)
        this.uploadAvatarFlag = false
      }
    }
  }
</script>

<style scoped>

</style>
