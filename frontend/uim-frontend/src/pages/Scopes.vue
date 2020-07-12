<template>
  <div class="q-pa-md">
    <q-list separator>
      <q-item>
        <q-item-section>
          <q-item-label overline>授权作用域 ({{scopes.length}})</q-item-label>
        </q-item-section>
      </q-item>
      <q-item v-if="scopes == null || scopes.length == 0" class="text-center">
        <q-item-section>
          <q-item-label overline>空空如也</q-item-label>
        </q-item-section>
      </q-item>
      <q-item clickable v-ripple v-for="scope in scopes">
        <q-item-section>
          <div>
            <q-item-label> {{scope.name}}</q-item-label>
            <q-popup-edit v-if="hasAuthority('MANAGE_SCOPE')"
                          @save="(val,initVal) => updateScope(val,initVal,scope,true)" v-model="scope.name"
                          buttons
                          label-set="保存"
                          label-cancel="取消">
              <q-input label="授权作用域名" hint="作用域名不可重复" v-model="scope.name" dense autofocus counter/>
            </q-popup-edit>
          </div>
          <q-item-label caption>
            {{scope.description}}
            <q-popup-edit v-if="hasAuthority('MANAGE_SCOPE')"
                          @save="(val,initVal) => updateScope(val,initVal,scope,false)"
                          v-model="scope.description"
                          buttons label-set="保存"
                          label-cancel="取消">
              <q-input label="授权作用域描述" v-model="scope.description" dense autofocus counter/>
            </q-popup-edit>
          </q-item-label>
        </q-item-section>
        <q-item-section>
          <q-item-label v-if="hasAuthority('MANAGE_SCOPE')" class="text-center">
            <q-btn
              @click="()=>editScope(scope)" flat round dense
              icon="edit"/>
            <q-btn
              @click="()=>deleteScope(scope)" flat round dense
              icon="delete" color="negative"/>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <div v-if="hasAuthority('MANAGE_SCOPE')">
          <q-btn @click="createScope" color="primary" icon="add" round/>
        </div>
        <q-btn v-else icon="security" flat color="primary" label="需要权限"/>
      </q-page-sticky>
    </q-list>

    <q-dialog v-model="editting">
      <q-card class="my-card">
        <q-card-section>
          <div class=" no-wrap items-center">
            <!--            ellipsis-->
            <div class="col text-h6">
              {{selectedScope.name}}
            </div>
          </div>

          <div class="text-subtitle1 text-caption text-grey">
            {{selectedScope.description}}
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-list>
            <q-item>
              <q-item-section style="min-width: 100px">
                <q-item-label overline>
                  权限列表 ({{selectedScopeAuthorities.length}})
                </q-item-label>
              </q-item-section>
              <q-item-section class="text-right">
                <q-item-label>
                  <q-btn @click="()=>selectAuthority()" round dense icon="add"
                         flat/>
                </q-item-label>
              </q-item-section>
            </q-item>
            <q-item dense v-for="authority in selectedScopeAuthorities">
              <q-item-section>
                <q-item-label>
                  <q-chip removable
                          @remove="removeAuthority(authority,selectedScope)"
                          icon="security"
                          :label="authority.name + '(' + authority.description + ')'"/>
                </q-item-label>
              </q-item-section>
            </q-item>
            <q-item class="text-center"
                    v-if="selectedScopeAuthorities == null || selectedScopeAuthorities.length == 0">
              <q-item-section>
                <q-item-label caption>
                  无任何权限
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-list>


        </q-card-section>

        <q-inner-loading :showing="loadingScopeAuthorities">
          <q-spinner-gears size="50px" color="primary"/>
        </q-inner-loading>
        <q-separator/>
        <q-card-actions align="right">
          <q-btn v-close-popup flat color="primary" label="返回"/>
        </q-card-actions>
      </q-card>

      <q-dialog v-model="selectingAuthority">
        <q-card class="my-card">
          <q-card-section>
            <q-list separator>
              <q-item>
                <q-item-section>
                  <q-item-label overline>
                    选择权限 {{selectedScopeAuthorities.length}}/{{authorities.length}}
                  </q-item-label>
                </q-item-section>
              </q-item>
              <q-separator/>
              <q-item v-for="authority in authorities">
                <q-item-section>
                  <q-item-label>{{authority.name}}</q-item-label>
                  <q-item-label caption>{{authority.description}}</q-item-label>
                </q-item-section>
                <q-item-section class="text-right">
                  <q-item-label>
                    <q-icon v-if="authority.flag" name="check"/>
                    <q-btn @click="()=>addAuthority(authority,selectedScope)" v-else flat round dense icon="add"/>
                  </q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
          <q-inner-loading :showing="loadingAuthorities">
            <q-spinner-gears size="50px" color="primary"/>
          </q-inner-loading>

          <q-card-actions align="right">
            <q-btn v-close-popup flat color="primary" label="返回"/>
          </q-card-actions>

        </q-card>
      </q-dialog>
    </q-dialog>


    <q-inner-loading :showing="loading">
      <q-spinner-gears size="50px" color="primary"/>
    </q-inner-loading>
  </div>
</template>

<script>
  import axios from 'axios'
  import qs from 'qs'

  export default {
    name: "Scopes",
    inject: ["hasAuthority", "roleName"],
    data() {
      return {
        loading: false,
        scopes: [],
        editting: false,
        selectedScope: {},
        selectedScopeAuthorities: [],
        loadingScopeAuthorities: false,
        selectingAuthority: false,
        authorities: [],
        loadingAuthorities: false
      }
    },
    methods: {
      load() {
        if (this.loading)
          return
        this.loading = true
        axios.get("/api/client/scopes")
          .then((res) => {
            this.scopes = res
          })
          .finally(() => {
            this.loading = false
          })
      },
      updateScope(val, initVal, scope, flag) {
        this.$q.loading.show()
        axios.post("/api/client/scope/" + scope.id, qs.stringify({
          name: scope.name,
          description: scope.description
        })).catch(e => {
          if (flag)
            scope.name = initVal
          else
            scope.description = initVal
        }).finally(() => {
          this.$q.loading.hide()
        })

      },
      createScope() {
        this.$q.dialog({
          title: '创建授权作用域',
          message: '请输入作用域名',
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
          axios.post("/api/client/scope", qs.stringify({name: data, description: data}))
            .then(res => {
              this.load();
            })
            .finally(() => {
              this.$q.loading.hide();
            })
        })
      },
      deleteScope(scope) {
        this.$q.dialog({
          title: '是否删除授权作用域',
          message: '删除操作不可撤销，是否要删除作用域"' + scope.name + '"？',
          cancel: true,
          ok: {
            label: "确定",
            color: "negative",
            flat: true
          },
          cancel: {label: "取消", color: "primary", flat: true}
        }).onOk(() => {
          this.$q.loading.show()
          axios.delete("/api/client/scope/" + scope.id).then(res => {
            this.load()
          }).finally(() => {
            this.$q.loading.hide()
          })
        })
      },
      editScope(scope) {
        this.selectedScope = scope
        this.editting = true
        this.loadingScopeAuthorities = true
        axios.get("/api/client/authorities/scope/" + scope.id)
          .then(res => {
            this.selectedScopeAuthorities = res
          }).finally(() => {
          this.loadingScopeAuthorities = false
        })
      },
      removeAuthority(authority, scope) {
        this.$q.loading.show()
        let data = {
          scopeId: scope.id,
          authorityId: authority.id
        }
        axios.delete("/api/client/scope_authority?" + qs.stringify(data))
          .then((res) => {
            this.editScope(scope)
          }).finally(() => {
          this.$q.loading.hide()
        })
      },
      selectAuthority() {
        this.loadingAuthorities = true
        this.selectingAuthority = true
        axios.get("/api/client/authorities")
          .then((res) => {
            this.authorities = res
            let arr = []
            this.selectedScopeAuthorities.forEach(a => {
              arr.push(a.id)
            })
            this.authorities.forEach(a => {
              if (arr.indexOf(a.id) >= 0)
                a.flag = true
              else
                a.flag = false
            })
          }).finally(() => {
          this.loadingAuthorities = false
        })
      }, addAuthority(authority, scope) {
        this.$q.loading.show()
        let data = {
          scopeId: scope.id,
          authorityId: authority.id
        }
        axios.post("/api/client/scope_authority?" + qs.stringify(data))
          .then(res => {
            authority.flag = true
            this.selectedScopeAuthorities.push(authority)
          }).finally(() => {
          this.$q.loading.hide()
        })
      }
    }, mounted() {
      this.load()
    }
  }
</script>

<style scoped>

</style>
