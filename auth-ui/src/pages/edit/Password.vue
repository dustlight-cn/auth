<template>
  <update-user
    :submit="onSubmit"
    :title="$tt(this,'operator')"
    :user="user"
    v-slot="{user,token,loading,busy}">
    <div v-if="!loading">
      <q-card-section>
        <q-input
          :label="$tt($options,'oldPassword')"
          :hint="$tt($options,'oldPasswordHint')"
          v-model="model.oldPassword"
          type="password"
          autocomplete="current-password"
          color="accent"
          filled
          :rules="rules.password"
          :disable="busy"
          class="full-width"/>
      </q-card-section>

      <q-card-section>
        <q-input
          :label="$tt($options,'newPassword')"
          :hint="$tt($options,'newPasswordHint')"
          v-model="model.newPassword"
          type="password"
          autocomplete="new-password"
          color="accent"
          filled
          :rules="rules.password"
          :disable="busy"
          class="full-width"/>
      </q-card-section>

      <q-card-section>
        <q-input
          :label="$tt($options,'confirmPassword')"
          :hint="$tt($options,'confirmPasswordHint')"
          v-model="model.confirmPassword"
          type="password"
          autocomplete="new-password"
          color="accent"
          filled
          :rules="rules.confirmPassword"
          :disable="busy"
          class="full-width"/>
      </q-card-section>
    </div>
    <div v-else>
      <q-card-section>
        <q-skeleton type="QInput"/>
      </q-card-section>
      <q-card-section>
        <q-skeleton type="QInput"/>
      </q-card-section>
      <q-card-section>
        <q-skeleton type="QInput"/>
      </q-card-section>
    </div>
  </update-user>
</template>

<script>
import RequireAuthorization from "../../components/RequireAuthorization";
import UpdateUser from "../../components/UpdateUser";

export default {
  name: "Password",
  components: {UpdateUser, RequireAuthorization},
  props: {
    user: Object
  },
  data() {
    return {
      model: {
        user: null,
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      },
      rules: {
        password: [val => val && val.match(this.$cfg.pattern.password) || this.$tt(this, "passwordRule"),
          val => this.model.newPassword != this.model.oldPassword || this.$tt(this, 'passwordRuleOriginal')],
        confirmPassword: [val => val == this.model.newPassword || this.$tt(this, "confirmPasswordRule")],
      }
    }
  },
  methods: {
    onSubmit() {
      return this.$userApi.resetPassword(this.model.oldPassword, this.model.newPassword);
    }
  }
}
</script>

<style scoped>

</style>
