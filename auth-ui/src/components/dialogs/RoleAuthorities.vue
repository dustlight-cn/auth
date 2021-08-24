<template>
  <q-dialog :persistent="updatingAuthorities.length > 0" ref="dialog" @hide="onDialogHide">
    <q-card class="full-width">
      <q-card-section>
        <div class="text-h6">
          {{ $tt($options, "title") }}
        </div>
      </q-card-section>
      <q-card-section class="q-pt-none">
        <q-item class="q-pa-none">
          <q-item-section avatar style="min-width: 0px">
            <q-icon name="person"/>
          </q-item-section>
          <q-item-section>
            <q-item-label>{{ role.roleName }}</q-item-label>
            <q-item-label caption>{{ role.roleDescription }}</q-item-label>
          </q-item-section>
          <q-item-section side v-if="loading">
            <q-skeleton type="text" width="2em"/>
          </q-item-section>
          <q-item-section side v-else>
            {{ grantedAuthorities.length }} / {{ clientAuthorities.length }}
          </q-item-section>
        </q-item>
      </q-card-section>

      <q-card-section class="q-pt-none">
        <q-separator/>
      </q-card-section>

      <q-card-section class="q-pa-none">
        <div v-if="clientAuthorities && clientAuthorities.length > 0">
          <!-- 骨架 -->
          <q-list v-if="loading">
            <q-item clickable v-ripple
                    v-for="authority in clientAuthorities"
                    :key="authority.aid">
              <q-item-section avatar>
                <q-skeleton type="QAvatar" size="2em"/>
              </q-item-section>
              <q-item-section>
                <q-item-label>
                  <q-skeleton type="text" width="3em"/>
                </q-item-label>
                <q-item-label caption>
                  <q-skeleton type="text" width="10em"/>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-skeleton class="q-ml-xs q-mr-xs" type="QAvatar" size="2em"/>
              </q-item-section>
            </q-item>
          </q-list>
          <!-- 列表 -->
          <q-list v-else>
            <q-item clickable v-ripple
                    v-for="authority in clientAuthorities"
                    :key="authority.aid">
              <q-item-section avatar>
                <q-icon name="security" :color="isGranted(authority)?'accent':''"/>
              </q-item-section>
              <q-item-section>
                <q-item-label :class="isGranted(authority)?'text-accent':''">
                  {{ authority.authorityName }}
                </q-item-label>
                <q-item-label caption>
                  {{ authority.authorityDescription }}
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn flat round dense
                       :loading="updatingAuthorities.indexOf(authority.aid) >= 0"
                       :icon="isGranted(authority) ? 'remove' : 'add'"
                       @click.stop="()=>grantOrRevokeAuthority(authority)"/>
              </q-item-section>
            </q-item>
          </q-list>
        </div>
        <no-results v-else/>
      </q-card-section>

      <q-card-section class="text-right">
        <q-btn
          :loading="updatingAuthorities.length > 0"
          :label="$t('done')" color="accent" v-close-popup/>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import NoResults from "../common/NoResults";

export default {
  name: "RoleAuthorities",
  components: {NoResults},
  props: {
    client: Object,
    currentUser: Object,
    role: Object,
    authorities: Array | Function
  },
  watch: {
    "role.rid"() {
      this.loadRoleAuthorities()
    }
  },
  computed: {
    clientAuthorities() {
      if (this.authorities instanceof Array)
        return this.authorities
      else
        return this.authorities()
    }
  },
  data() {
    return {
      updatingAuthorities: [],
      grantedAuthorities: [],
      loading: false
    }
  },
  methods: {
    loadRoleAuthorities() {
      if (this.loading)
        return
      this.loading = true
      this.$authoritiesApi.getRoleAuthorities(this.role.rid)
        .then(res => {
          this.grantedAuthorities = res.data
        })
        .finally(() => this.loading = false)
    },
    isGranted(authority) {
      return this.grantedAuthorities.indexOf(authority.authorityName) >= 0
    },
    getAuthorityId(authorityName) {
      for (let i in this.clientAuthorities) {
        if (this.clientAuthorities[i].authorityName == authorityName)
          return this.clientAuthorities[i].aid
      }
      return null
    },
    grantOrRevokeAuthority(authority) {
      let aid = this.getAuthorityId(authority.authorityName)
      if (this.updatingAuthorities.indexOf(aid) >= 0)
        return
      let granted = this.isGranted(authority)
      this.updatingAuthorities.push(aid)
      let p = granted ? this.$authoritiesApi.deleteRoleAuthorities(this.role.rid, [aid], this.client.cid) :
        this.$authoritiesApi.setRoleAuthorities(this.role.rid, [aid], this.client.cid)
      let then = granted ? () => {
        this.grantedAuthorities.splice(this.grantedAuthorities.indexOf(authority.authorityName), 1)
      } : () => {
        this.grantedAuthorities.push(authority.authorityName)
      }
      let final = () => this.updatingAuthorities.splice(this.updatingAuthorities.indexOf(aid), 1)

      p.then(() => then())
        .finally(final)
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
    },
  },
  mounted() {
    if (this.role && this.role.rid)
      this.loadRoleAuthorities()
  }
}
</script>

<style scoped>

</style>
