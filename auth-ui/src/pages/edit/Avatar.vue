<template>
  <update-user
    :on-success="onSuccess"
    :on-cancel="onCancel"
    :submit="onSubmit"
    :title="$tt(this,'operator')"
    :user="user"
    v-slot="{user,token,loading,busy}">
    <q-card-section class="flex flex-center">
      <q-file
        style="max-width: 128px;"
        @input="onSelected"
        accept="image/*"
        hide-hint
        hide-bottom-space
        borderless
      >
        <avatar-component style="cursor: pointer"
                          clickable
                          v-ripple
                          :src="img"
                          size="128"
                          :user="user_clone?user_clone:(user&&user.uid?(user_clone=user):(user))"/>
      </q-file>
    </q-card-section>
  </update-user>
</template>

<script>
import RequireAuthorization from "../../components/RequireAuthorization";
import UpdateUser from "../../components/UpdateUser";
import AvatarComponent from "../../components/Avatar"

export default {
  name: "Avatar",
  components: {UpdateUser, RequireAuthorization, AvatarComponent},
  props: {
    user: Object,
    onSuccess: Function,
    onCancel: Function
  },
  data() {
    return {
      user_clone: null,
      file: null,
      img: null,
      reader: new FileReader()
    }
  },
  methods: {
    onPreview(data) {
      this.img = data;
    },
    onSelected(file) {
      if (file == null)
        return;
      this.file = file;
      this.reader.readAsDataURL(file);
      let cb = this.onPreview;
      this.reader.onload = function () {
        cb(this.result);
      }
    },
    onSubmit() {
      if (this.file == null) {
        if (this.onCancel != null)
          this.onCancel();
        return;
      }
      return this.$usersApi.updateUserAvatar(this.user_clone.uid, this.file);
    }
  }
}
</script>

<style scoped>

</style>
