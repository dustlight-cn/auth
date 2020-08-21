<template>
  <div class="q-pa-md">
    <q-list separator>
      <q-item>
        <q-item-section>
          <q-item-label overline>权限列表 ({{authorities.length}})</q-item-label>
        </q-item-section>
      </q-item>
      <q-item v-if="authorities == null || authorities.length == 0" class="text-center">
        <q-item-section>
          <q-item-label overline>空空如也</q-item-label>
        </q-item-section>
      </q-item>
      <q-item clickable v-ripple v-for="authority in authorities">
        <q-item-section>
          <q-item-label v-if="isProtectedAuthority(authority.name)" caption>{{authority.name}}</q-item-label>
          <q-item-label v-else>
            {{authority.name}}
            <q-popup-edit v-if="hasAuthority('MANAGE_AUTHORITY')"
                          @save="(val,initVal) => updateAuthority(val,initVal,authority,true)" v-model="authority.name"
                          buttons
                          label-set="保存"
                          label-cancel="取消">
              <q-input label="权限名" hint="权限名不可重复" v-model="authority.name" dense autofocus counter/>
            </q-popup-edit>
          </q-item-label>
          <q-item-label caption>
            {{authority.description}}
            <q-popup-edit v-if="hasAuthority('MANAGE_AUTHORITY')"
                          @save="(val,initVal) => updateAuthority(val,initVal,authority,false)"
                          v-model="authority.description"
                          buttons label-set="保存"
                          label-cancel="取消">
              <q-input label="权限描述" v-model="authority.description" dense autofocus counter/>
            </q-popup-edit>
          </q-item-label>
        </q-item-section>
        <q-item-section>
          <q-item-label class="text-center">
            <q-btn v-if="hasAuthority('MANAGE_AUTHORITY') && !isProtectedAuthority(authority.name)"
                   @click="()=>deleteAuthority(authority)" flat round dense
                   icon="delete" color="negative"/>
            <q-btn v-else
                   disable
                   @click="()=>deleteAuthority(authority)" flat round dense
                   icon="delete" color="grey"/>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-btn @click="createAuthority" v-if="hasAuthority('MANAGE_AUTHORITY')" color="primary" icon="add" round/>
        <q-btn v-else icon="security" flat color="primary" label="需要权限"/>
      </q-page-sticky>
    </q-list>
    <q-inner-loading :showing="loading">
      <q-spinner-gears size="50px" color="primary"/>
    </q-inner-loading>
  </div>
</template>

<script>
  export default {
    name: "Authorities",
    inject: ["hasAuthority"],
    data() {
      return {
        loading: false,
        authorities: [],
        protectedAuthority: ["MANAGE_AUTHORITY", "MANAGE_SCOPE", "MANAGE_ROLE"]
      }
    }, methods: {
      load() {
        if (this.loading)
          return
        this.loading = true
        this.$uim.client.getAllAuthorities()
          .then(res => this.authorities = res)
          .finally(() => this.loading = false)
      },
      isProtectedAuthority(authority) {
        return this.protectedAuthority.indexOf(authority) >= 0
      },
      updateAuthority(val, initVal, authority, flag) {
        this.$q.loading.show()
        this.$uim.client.updateAuthority(authority.id, authority.name, authority.description)
          .catch(e => {
            if (flag)
              authority.name = initVal
            else
              authority.description = initVal
          })
          .finally(() => this.$q.loading.hide())

      },
      createAuthority() {
        this.$q.dialog({
          title: '创建权限',
          message: '请输入权限名',
          prompt: {
            model: '',
            type: 'text' // optional
          },
          cancel: true,
          ok: {label: "创建", flat: true},
          cancel: {label: "取消", flat: true}
        }).onOk(data => {
          if (data == null || data.trim().length == 0)
            return
          this.$q.loading.show()
          this.$uim.client.createAuthority(data, data)
            .then(res => this.load())
            .finally(() => this.$q.loading.hide())
        })
      },
      deleteAuthority(authority) {
        this.$q.dialog({
          title: '是否删除权限',
          message: '删除操作不可撤销，是否要删除权限"' + authority.name + '"？',
          cancel: true,
          ok: {
            label: "确定",
            color: "negative",
            flat: true
          },
          cancel: {label: "取消", color: "primary", flat: true}
        }).onOk(() => {
          this.$q.loading.show()
          this.$uim.client.deleteAuthority(authority.id)
            .then(res => this.load())
            .finally(() => this.$q.loading.hide())
        })
      }
    }, mounted() {
      this.load()
    }
  }
</script>

<style scoped>

</style>
