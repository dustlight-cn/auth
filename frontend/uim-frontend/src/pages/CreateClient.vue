<template>
  <div class="q-pa-md" style="margin: 0 auto;max-width: 400px">
    <q-form
      class="q-gutter-md"
      @submit="onSubmit"
    >
      <q-input
        outlined
        v-model="name"
        label="应用名称"
        hint="新建应用的名称"
        lazy-rules
        :rules="[ val => val && val.length > 0 || '应用名称不能为空']"
      />
      <q-input
        outlined
        type="textarea"
        maxlength="4096"
        style="resize: none;"
        v-model="description"
        label="应用描述"
        hint="该应用的描述"
        lazy-rules
        :rules="[ val => val && val.length > 0 || '应用描述不能为空']"
      />

      <q-input
        outlined
        type="textarea"
        maxlength="4096"
        style="resize: none;"
        v-model="redirectUri"
        label="回调地址"
        hint="提供多个回调地址时以','分割"
        lazy-rules
        :rules="[ val => val && val.length > 0 || '回调地址不能为空']"
      />
      <q-expansion-item
        default-opened
        expand-separator
        icon="lock"
        label="授权作用域"
      >
        <q-option-group
          v-model="scopes"
          :options="scopeOptions"
          color="primary"
          type="checkbox"
        />
      </q-expansion-item>

      <q-expansion-item
        default-opened
        expand-separator
        icon="link"
        label="授权模式"
      >
        <q-option-group
          v-model="grantTypes"
          :options="grantTypeOptions"
          color="primary"
          type="checkbox"
        />
      </q-expansion-item>
      <div>
        <q-btn label="创建" type="submit" color="primary"/>
      </div>

      <q-inner-loading :showing="scopesLoading || grantTypesLoading">
        <q-spinner-gears size="50px" color="primary"/>
      </q-inner-loading>
    </q-form>

    <q-dialog persistent v-model="showAppCard">
      <q-card class="my-card">
        <q-card-section>
          <div class="col text-h6 ellipsis">
            创建成功
          </div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            AppKey
          </div>
          <div class="text-caption text-grey">
            {{appKey}}
          </div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <div class="text-subtitle1">
            AppSecret
          </div>
          <div class="text-caption text-grey">
            {{appSecret}}
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <div class="text-caption text-grey">
            请自行保管AppSecret，若丢失可通过重置AppSecret获得。
          </div>
        </q-card-section>
        <q-separator/>

        <q-card-actions align="right">
          <q-btn to="." v-close-popup flat color="primary" label="确定"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
  export default {
    name: "CreateClient",
    data() {
      return {
        name: "",
        description: "",
        redirectUri: "",
        accept: false,
        age: 0,
        scopes: [],
        scopeOptions: [],
        grantTypes: [],
        grantTypeOptions: [],
        showAppCard: false,
        appKey: "null",
        appSecret: "null",
        scopesLoading: false,
        grantTypesLoading: false
      }
    },
    methods: {
      loadGrantTypes() {
        this.grantTypesLoading = true
        this.$uim.client.getAllGrantTypes()
          .then(res => {
            this.grantTypes = []
            res.forEach(s => {
              this.grantTypeOptions.push({
                label: s.name,
                value: s.id
              })
            })
          })
          .finally(() => this.grantTypesLoading = false)
      },
      loadScopes() {
        this.scopesLoading = true
        this.$uim.client.getAllScopes()
          .then(res => {
            this.scopeOptions = []
            res.forEach(s => {
              this.scopeOptions.push({
                label: s.name + " (" + s.description + ")",
                value: s.id
              })
            })
          })
          .finally(() => this.scopesLoading = false)
      },
      onSubmit() {
        this.$q.loading.show()
        this.$uim.client.createClient(this.name, this.description, this.redirectUri, this.scopes, this.grantTypes)
          .then(res => {
            this.appKey = res.appKey
            this.appSecret = res.appSecret
            this.showAppCard = true
          })
          .finally(() => this.$q.loading.hide())
      }
    }, mounted() {
      this.loadGrantTypes()
      this.loadScopes()
    }
  }
</script>

<style scoped>

</style>
