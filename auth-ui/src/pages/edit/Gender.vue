<template>
  <update-user
    :on-success="onSuccess"
    :on-cancel="onCancel"
    :submit="onSubmit"
    :title="$tt(this,'operator')"
    :user="user"
    v-slot="{user,token,loading,busy}">
    <q-card-section v-if="!loading">
      <q-select
        emit-value
        map-options
        filled
        :disable="busy"
        v-model="model.gender == null ? (model.gender = (model.user = user).gender) : model.gender"
        :options="options"
        color="accent"
        :label="$tt($options,'gender')"/>
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
  name: "Gender",
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
        gender: null
      }
    }
  },
  methods: {
    onSubmit() {
      if (this.model.user.gender == this.model.gender) {
        if (this.onCancel != null)
          this.onCancel();
        return;
      }
      return this.$usersApi.updateUserGender(this.model.user.uid, this.model.gender)
        .then(() => {
          this.model.user.gender = this.model.gender;
        }).catch((e) => {
          this.model.gender = this.model.user.gender;
          throw e;
        })
    }
  },
  computed: {
    options() {
      return [
        {label: this.$t("gender.0"), value: 0},
        {label: this.$t("gender.1"), value: 1},
        {label: this.$t("gender.2"), value: 2},
        {label: this.$t("gender.3"), value: 3}
      ]
    }
  }
}
</script>

<style scoped>

</style>
