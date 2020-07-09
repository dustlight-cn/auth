<template>
  <div class="q-pa-md">
    <q-list separator>
      <q-item>
        <q-item-section>
          <q-item-label overline>角色列表 ({{roles.length}})</q-item-label>
        </q-item-section>
      </q-item>
      <q-item v-if="roles == null || roles.length == 0" class="text-center">
        <q-item-section>
          <q-item-label overline>空空如也</q-item-label>
        </q-item-section>
      </q-item>
      <q-item clickable v-ripple v-for="role in roles">
        <q-item-section>
          <div>
            <q-item-label v-if="roleName() == role.name" caption>{{role.name}}</q-item-label>
            <q-item-label v-else> {{role.name}}</q-item-label>
            <q-popup-edit v-if="hasAuthority('MANAGE_ROLE')"
                          @save="(val,initVal) => updateRole(val,initVal,role,true)" v-model="role.name"
                          buttons
                          label-set="保存"
                          label-cancel="取消">
              <q-input label="权限名" hint="权限名不可重复" v-model="role.name" dense autofocus counter/>
            </q-popup-edit>
          </div>
          <q-item-label caption>
            {{role.description}}
            <q-popup-edit v-if="hasAuthority('MANAGE_ROLE')"
                          @save="(val,initVal) => updateRole(val,initVal,role,false)"
                          v-model="role.description"
                          buttons label-set="保存"
                          label-cancel="取消">
              <q-input label="权限描述" v-model="role.description" dense autofocus counter/>
            </q-popup-edit>
          </q-item-label>
        </q-item-section>
        <q-item-section>
          <q-item-label v-if="hasAuthority('MANAGE_ROLE')" class="text-center">
            <q-btn
              @click="()=>editRole(role)" flat round dense
              icon="edit"/>
            <q-btn v-if="roleName() != role.name"
                   @click="()=>deleteRole(role)" flat round dense
                   icon="delete" color="negative"/>
          </q-item-label>
        </q-item-section>
      </q-item>

      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <div v-if="hasAuthority('MANAGE_ROLE')">
          <q-btn @click="createRole" color="primary" icon="add" round/>
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
              {{selectedRole.name}}
            </div>
          </div>

          <div class="text-subtitle1 text-caption text-grey">
            {{selectedRole.description}}
          </div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-list>
            <q-item>
              <q-item-section style="min-width: 100px">
                <q-item-label overline>
                  权限列表 ({{selectedRoleAuthorities.length}})
                </q-item-label>
              </q-item-section>
              <q-item-section class="text-right">
                <q-item-label>
                  <q-btn @click="()=>selectAuthority()" round dense icon="add"
                         flat/>
                </q-item-label>
              </q-item-section>
            </q-item>
            <q-item dense v-for="authority in selectedRoleAuthorities">
              <q-item-section>
                <q-item-label>
                  <q-chip v-if="roleName() == selectedRole.name && authority.name=='MANAGE_ROLE'"
                          icon="security"
                          :label="authority.name + '(' +authority.description + ')'"/>
                  <q-chip v-else removable
                          @remove="removeAuthority(authority,selectedRole)"
                          icon="security"
                          :label="authority.name + '(' +authority.description + ')'"/>
                </q-item-label>
              </q-item-section>
            </q-item>
            <q-item class="text-center"
                    v-if="selectedRoleAuthorities == null || selectedRoleAuthorities.length == 0">
              <q-item-section>
                <q-item-label caption>
                  无任何权限
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-list>


        </q-card-section>

        <q-inner-loading :showing="loadingRoleAuthorities">
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
                    选择权限 {{selectedRoleAuthorities.length}}/{{authorities.length}}
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
                    <q-btn @click="()=>addAuthority(authority,selectedRole)" v-else flat round dense icon="add"/>
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
    name: "Roles",
    inject: ["hasAuthority", "roleName"],
    data() {
      return {
        loading: false,
        roles: [],
        editting: false,
        selectedRole: {},
        selectedRoleAuthorities: [],
        loadingRoleAuthorities: false,
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
        axios.get("/api/client/roles")
          .then((res) => {
            this.roles = res
          })
          .finally(() => {
            this.loading = false
          })
      },
      updateRole(val, initVal, role, flag) {
        this.$q.loading.show()
        axios.post("/api/client/role/" + role.id, qs.stringify({
          name: role.name,
          description: role.description
        })).catch(e => {
          if (flag)
            role.name = initVal
          else
            role.description = initVal
        }).finally(() => {
          this.$q.loading.hide()
        })

      },
      createRole() {
        this.$q.dialog({
          title: '创建角色',
          message: '请输入角色名',
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
          axios.post("/api/client/role", qs.stringify({name: data, description: data}))
            .then(res => {
              this.load();
            })
            .finally(() => {
              this.$q.loading.hide();
            })
        })
      },
      deleteRole(role) {
        this.$q.dialog({
          title: '是否删除角色',
          message: '删除操作不可撤销，是否要删除角色"' + role.name + '"？',
          cancel: true,
          ok: {
            label: "确定",
            color: "negative",
            flat: true
          },
          cancel: {label: "取消", color: "primary", flat: true}
        }).onOk(() => {
          this.$q.loading.show()
          axios.delete("/api/client/role/" + role.id).then(res => {
            this.load()
          }).finally(() => {
            this.$q.loading.hide()
          })
        })
      },
      editRole(role) {
        this.selectedRole = role
        this.editting = true
        this.loadingRoleAuthorities = true
        axios.get("/api/client/authorities/role/" + role.id)
          .then(res => {
            this.selectedRoleAuthorities = res
          }).finally(() => {
          this.loadingRoleAuthorities = false
        })
      },
      removeAuthority(authority, role) {
        this.$q.loading.show()
        let data = {
          roleId: role.id,
          authorityId: authority.id
        }
        axios.delete("/api/client/role_authority?" + qs.stringify(data))
          .then((res) => {
            this.editRole(role)
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
            this.selectedRoleAuthorities.forEach(a => {
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
      }, addAuthority(authority, role) {
        this.$q.loading.show()
        let data = {
          roleId: role.id,
          authorityId: authority.id
        }
        axios.post("/api/client/role_authority?" + qs.stringify(data))
          .then(res => {
            authority.flag = true
            this.selectedRoleAuthorities.push(authority)
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
