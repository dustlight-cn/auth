<template>
  <q-dialog :persistent="busying" ref="dialog" @hide="onDialogHide">

    <q-card class="full-width">

      <q-card-section>
        <div class="text-h6">{{ role && role.rid ? $tt($options, "edit") : $tt($options, "new") }}</div>
      </q-card-section>
      <q-card-section>
        <q-form @submit="save">
          <div class="q-gutter-sm">
            <q-input :loading="busying" :disable="busying"
                     color="accent" filled
                     :rules="rules.name"
                     v-model="model.name"
                     :label="$tt($options,'roleName')"/>

            <q-input :loading="busying" :disable="busying"
                     color="accent" filled
                     :rules="rules.description"
                     v-model="model.description"
                     :label="$tt($options,'roleDescription')"/>
          </div>

          <div class="text-right q-gutter-sm q-pt-sm">
            <q-btn :label="$t('cancel')"
                   color="grey-7"
                   flat
                   :disable="busying"
                   v-close-popup/>
            <q-btn :label="$t('update')"
                   type="submit"
                   color="accent"
                   :disable="busying"
                   :loading="busying"/>
          </div>
        </q-form>
      </q-card-section>


    </q-card>
  </q-dialog>
</template>

<script>
export default {
  name: "EditRole",
  props: {
    client: Object,
    currentUser: Object,
    role: Object,
    onUpdated: Function,
    onInserted: Function
  },
  data() {
    return {
      busying: false,
      model: {
        name: this.role ? this.role.roleName : "",
        description: this.role ? this.role.roleDescription : "",
      },
      rules: {
        name: [val => val && val.trim().length > 0 || this.$tt(this, 'roleNameRule')],
        description: [val => val && val.trim().length > 0 || this.$tt(this, 'roleDescriptionRule')],
      }
    }
  },
  mounted() {

  },
  methods: {
    save() {
      if (this.busying)
        return
      this.busying = true
      this.$rolesApi.setUserClientRoles(this.currentUser.uid, this.client.cid, [{
        rid: this.role ? this.role.rid : null,
        roleName: this.model.name.trim(),
        roleDescription: this.model.description.trim()
      }])
        .then((res) => {
          if (this.role) {
            this.role.roleName = this.model.name.trim()
            this.role.roleDescription = this.model.description.trim()
            if (this.onUpdated)
              this.onUpdated(this.role)
          } else {
            if (this.onInserted)
              this.onInserted({
                roleName: this.model.name.trim(),
                roleDescription: this.model.description.trim()
              })
          }
          this.hide()
        })
        .finally(() => this.busying = false)
    },
    // 以下方法是必需的
    // (不要改变它的名称 --> "show")
    show() {
      this.$refs.dialog.show()
    },

    // 以下方法是必需的
    // (不要改变它的名称 --> "hide")
    hide() {
      this.$refs.dialog.hide()
    },

    onDialogHide() {
      // QDialog发出“hide”事件时
      // 需要发出
      this.$emit('hide')
    },

    onOKClick() {
      // 按OK，在隐藏QDialog之前
      // 发出“ok”事件（带有可选的有效负载）
      // 是必需的
      this.$emit('ok')
      // 或带有有效负载：this.$emit('ok', { ... })

      // 然后隐藏对话框
      this.hide()
    },

    onCancelClick() {
      // 我们只需要隐藏对话框
      this.hide()
    }
  }
}
</script>

<style scoped>

</style>
