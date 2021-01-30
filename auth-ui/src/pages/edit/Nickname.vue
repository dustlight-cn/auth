<template>
  <update-user
    :on-success="onSuccess"
    :on-cancel="onCancel"
    :submit="onSubmit"
    :title="$tt(this,'operator')"
    :user="user"
    v-slot="{user,token,loading,busy}">
    <q-card-section v-if="!loading">
      <q-input
        :label="$tt($options,'nickname')"
        :hint="$tt($options,'nicknameHint')"
        v-model="model.nickname == null ? (model.nickname = (model.user = user).nickname) : model.nickname"
        autocomplete="name"
        color="accent"
        filled
        :rules="rules.nickname"
        :disable="busy"
        class="full-width"/>
    </q-card-section>
    <q-card-section v-else>
      <q-skeleton type="QInput"/>
    </q-card-section>
  </update-user>
</template>

<script>
import RequireAuthorization from "../../components/RequireAuthorization";
import UpdateUser from "../../components/UpdateUser";

export default {
  name: "Nickname",
  components: {UpdateUser, RequireAuthorization},
  props: {
    user: Object,
    onSuccess: Function,
    onCancel: Function
  },
  data() {
    return {
      model: {
        user: null,
        nickname: null
      },
      rules: {
        nickname: [val => val && val.length > 0 || this.$tt(this, "nicknameRule")]
      }
    }
  },
  methods: {
    onSubmit() {
      if (this.model.user.nickname == this.model.nickname) {
        if (this.onCancel != null)
          this.onCancel();
        return;
      }
      return this.$usersApi.updateUserNickname(this.model.user.uid, this.model.nickname)
        .then(() => {
          this.model.user.nickname = this.model.nickname;
        }).catch((e) => {
          this.model.nickname = this.model.user.nickname;
          throw e;
        })
    }
  }
}
</script>

<style scoped>

</style>
