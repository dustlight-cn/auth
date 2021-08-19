<template>
  <q-dialog :persistent="updating" ref="dialog" @hide="onDialogHide">
    <q-card class="full-width">
      <q-card-section>
        <div class="text-h6">{{ $tt($options, "expiredAt") }}</div>
      </q-card-section>
      <q-card-section class="q-pa-none">
        <q-item v-if="role">
          <q-item-section avatar>
            <q-icon name="person"/>
          </q-item-section>
          <q-item-section>
            <q-item-label>
              {{ role.roleName }}
            </q-item-label>
            <q-item-label caption>
              {{ role.roleDescription }}
            </q-item-label>
          </q-item-section>
        </q-item>
      </q-card-section>
      <q-card-section>
        <q-tabs
          mobile-arrows
          outside-arrows
          no-caps
          dense
          v-model="expiredType"
        >
          <q-tab name="null" :label="$tt($options,'expiredNull')"/>
          <q-tab name="simple" :label="$tt($options,'expiredSimple')"/>
          <q-tab name="details" :label="$tt($options,'expiredDetails')"/>
        </q-tabs>
        <q-separator/>
        <q-tab-panels v-model="expiredType" animated>
          <q-tab-panel name="null" class="text-center q-pt-xl text-subtitle1 text-grey">
            {{ $tt($options, "expiredNullDesc") }}
          </q-tab-panel>
          <!-- 简单设置 -->
          <q-tab-panel name="simple" class="text-center q-pt-xl">
            <q-input v-model="expiredValue.simple" filled color="accent" dense type="number" step="1" min="0">
              <template v-slot:prepend>
                <q-icon name="timer"/>
              </template>
              <template v-slot:after>
                <div class="q-pa-sm text-subtitle1">
                  {{ $t("day") }}
                </div>
              </template>
            </q-input>
          </q-tab-panel>
          <!-- 详细设置 -->
          <q-tab-panel name="details" class="text-center q-pt-xl">
            <q-input readonly v-model="expiredValue.details.date"
                     filled color="accent"
                     dense>
              <template v-slot:prepend>
                <q-icon name="timer"/>
              </template>
              <template v-slot:after>
                <q-btn icon="edit" flat round>
                  <q-popup-proxy transition-show="scale" transition-hide="scale">
                    <q-date no-unset v-model="expiredValue.details.date" color="accent">
                      <div class="row items-center justify-end q-gutter-sm">
                        <q-btn :label="$t('done')" color="accent" v-close-popup/>
                      </div>
                    </q-date>
                  </q-popup-proxy>
                </q-btn>
              </template>
            </q-input>
            <q-input readonly v-model="expiredValue.details.time"
                     class="q-mt-sm"
                     filled color="accent"
                     dense>
              <template v-slot:prepend>
                <q-icon name="timer"/>
              </template>
              <template v-slot:after>
                <q-btn icon="edit" flat round>
                  <q-popup-proxy transition-show="scale" transition-hide="scale">
                    <q-time format24h no-unset v-model="expiredValue.details.time" color="accent">
                      <div class="row items-center justify-end q-gutter-sm">
                        <q-btn :label="$t('done')" color="accent" v-close-popup/>
                      </div>
                    </q-time>
                  </q-popup-proxy>
                </q-btn>
              </template>
            </q-input>
          </q-tab-panel>
        </q-tab-panels>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn :label="$t('cancel')"
               flat
               :disable="updating"
               v-close-popup/>
        <q-btn :label="$t('update')"
               color="accent"
               @click="updateUserRole"
               :disable="updating"
               :loading="updating"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
export default {
  name: "UserRoleSettings",
  props: {
    // ...你自定义的属性
    user: Object,
    client: Object,
    currentUser: Object,
    role: Object,
    onUpdated: Function
  },
  data() {
    return {
      updating: false,
      expiredType: null,
      expiredValue: {
        simple: 1,
        details: {
          date: null,
          time: null
        }
      }
    }
  },
  watch: {
    role() {
      this.onRoleChange()
    }
  },
  mounted() {
    this.onRoleChange()
  },
  methods: {
    onRoleChange() {
      if (this.role != null) {
        this.expiredType = this.role.expiredAt ? "details" : "null";
        this.expiredValue.simple = this.role.expiredAt ? Math.ceil((new Date(this.role.expiredAt) - new Date()) / 1000 / 86400) : 0;
        this.expiredValue.details.date = this.$util.dateFormat(this.role.expiredAt || new Date(), "YYYY/mm/dd");
        this.expiredValue.details.time = this.$util.dateFormat(this.role.expiredAt || new Date(), "HH:MM:SS");
      }
    },
    updateUserRole() {
      if (this.role == null || this.updating)
        return;
      let expiredAt = null;
      switch (this.expiredType) {
        case 'simple':
          expiredAt = new Date().getTime() + this.expiredValue.simple * 86400 * 1000;
          break;
        case 'details':
          expiredAt = new Date(this.expiredValue.details.date + " " + this.expiredValue.details.time);
          break;
        case 'null':
        default:
          expiredAt = null;
          break
      }
      if (this.role.expiredAt == null && expiredAt == null) {
        this.role = null;
        return;
      }
      let data = {
        rid: this.role.rid,
        expiredAt: expiredAt
      };

      this.updating = true;
      this.$rolesApi.setUserClientRoles(this.user.uid, this.client.cid, [data])
        .then(res => {
          this.role.expiredAt = expiredAt;
          this.$q.notify({
            message: this.$t("updateSuccess"),
            type: 'positive'
          })
          this.updating = false
          if(this.onUpdated)
            this.onUpdated(this.role)
          this.hide()
        })
        .catch(() => this.updating = false)
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
