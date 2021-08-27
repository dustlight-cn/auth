<template>
  <div>
    <require-authorization>
      <template v-slot="{user}">
        {{ "", user_ == null || user != null && user_.uid != user.uid ? user_ = user : "" }}
        <q-file
          style="width: 0px;height: 0px;"
          ref="logoPicker"
          @input="onLogoPicked"
          accept="image/*"
          hide-hint
          hide-bottom-space
          borderless
        >
        </q-file>

        <div v-if="!deleted">
          <edit-page v-if="error == null">
            <template v-slot="{wide}">
              <!-- 骨架 -->
              <div v-if="loading || !client">
                <div class="q-mb-sm">
                  <div class="text-h4 q-mb-sm">
                    <q-skeleton type="text" width="40%"/>
                  </div>
                  <q-separator class="q-ma-none"/>
                  <q-item class="q-pa-none q-mt-sm">
                    <q-item-section avatar class="q-pa-none q-mr-md" style="min-width: 40px;">
                      <q-skeleton type="QAvatar"/>
                    </q-item-section>
                    <q-item-section>
                      <q-skeleton type="text" width="60%"/>
                    </q-item-section>
                  </q-item>
                </div>
                <q-separator class="q-ma-none"/>
                <q-list class="q-mb-md">
                  <q-item class="q-pa-none q-mt-md" v-for="index in 8" :key="index">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">
                        <q-skeleton type="text" width="32%"/>
                      </q-item-label>
                      <q-item-label>
                        <q-skeleton type="text" width="52%"/>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </div>
              <!-- 页面 -->
              <div v-else>
                <div class="q-mb-sm">
                  <div class="text-h4 q-mb-sm">{{ client.name }}</div>
                  <q-separator/>
                  <q-item class="q-pa-none q-mt-sm">
                    <q-item-section avatar class="q-pa-none" style="min-width: 40px;">
                      <q-btn v-if="owner && owner.uid"
                             color="accent"
                             rounded dense flat no-caps :to="{name:'user',params:{id:owner.uid}}">
                        <avatar :size="36" :user="owner"/>
                        <span class="q-pl-sm q-pr-xs">{{ ownerName }}</span>
                      </q-btn>
                    </q-item-section>
                    <q-item-section v-if="owner">
                      <q-item-label>
                        {{ $tt($options, "createdAt") }}
                        <span class="text-caption">{{ $util.dateFormat(client.createdAt) }}</span>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section v-else>
                      <q-skeleton type="text" width="60%"/>
                    </q-item-section>

                  </q-item>
                </div>
                <q-separator/>
                <q-list class="q-mb-md">
                  <!-- 应用 ID, Client ID-->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientId") }}</q-item-label>
                      <q-item-label class="code">{{ client.cid }}</q-item-label>
                    </q-item-section>
                  </q-item>

                  <!-- 应用密钥, Client Secret -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientSecret") }}</q-item-label>
                      <q-item-label class="code">{{ client.secret || "******" }}</q-item-label>
                    </q-item-section>
                    <q-item-section side>
                      <q-btn v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient" :disable="secretRegenerating"
                             :loading="secretRegenerating"
                             dense no-caps
                             color="negative" :label="$tt($options,'regenerateSecret')"
                             @click="regenerateSecret"
                             icon="vpn_key"/>
                    </q-item-section>
                  </q-item>

                  <!-- 应用图标, Client Logo -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientLogo") }}</q-item-label>
                      <q-item-label>
                        <q-btn flat dense @click="changeLogo"
                               v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient">
                          <client-logo :src="logo" :size="118" :client="client"/>
                        </q-btn>
                        <client-logo v-else :src="logo" :size="118" :client="client"/>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side>
                      <q-btn v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient"
                             :disable="logoUploading" :loading="logoUploading"
                             @click="changeLogo" dense no-caps
                             :label="$tt($options,'upload')" icon="upload"/>
                    </q-item-section>
                  </q-item>

                  <!-- 应用名称, Client Name -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientName") }}</q-item-label>
                      <q-item-label v-if="edit.name==null" class="content">
                        {{ client.name || "-" }}
                      </q-item-label>
                      <q-item-label v-else class="text-right">
                        <q-form @submit="updateName">
                          <q-input :placeholder="$tt($options, 'clientName')"
                                   :disable="updating.name"
                                   :loading="updating.name"
                                   dense filled
                                   class="q-mb-sm"
                                   maxlength="64"
                                   :rules="rules.name"
                                   color="accent"
                                   v-model="edit.name"/>
                          <q-btn :disable="updating.name" no-caps flat :label="$t('cancel')" color="grey-7"
                                 class="q-mr-sm"
                                 @click="()=>edit.name=null"/>
                          <q-btn :loading="updating.name" type="submit" no-caps :label="$t('update')" color="accent"/>
                        </q-form>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side top v-if="edit.name==null">
                      <q-btn @click="updateName" v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient" flat round
                             size="12px"
                             no-caps
                             icon="edit"/>
                    </q-item-section>
                  </q-item>

                  <!-- 应用描述, Client Description -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientDescription") }}</q-item-label>
                      <q-item-label v-if="edit.description==null" class="content">
                        {{ client.description || "-" }}
                      </q-item-label>
                      <q-item-label class="text-right" v-else>
                        <q-form @submit="updateDescription">
                          <q-input :placeholder="$tt($options, 'clientDescription')"
                                   :disable="updating.description"
                                   :loading="updating.description"
                                   dense filled
                                   class="q-mb-sm"
                                   type="textarea"
                                   maxlength="256"
                                   :rules="rules.description"
                                   color="accent"
                                   v-model="edit.description"/>
                          <q-btn :disable="updating.description" no-caps flat :label="$t('cancel')" color="grey-7"
                                 class="q-mr-sm"
                                 @click="()=>edit.description=null"/>
                          <q-btn :loading="updating.description" type="submit" no-caps :label="$t('update')"
                                 color="accent"/>
                        </q-form>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side top v-if="edit.description==null">
                      <q-btn @click="updateDescription" v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient" flat
                             round
                             size="12px"
                             no-caps
                             icon="edit"/>
                    </q-item-section>
                  </q-item>

                  <!-- 应用回调地址, Client Redirect Uri -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientRedirectUri") }}</q-item-label>
                      <q-item-label>
                        <q-list>
                          <q-item v-for="(uri,index) in client.redirectUri" :key="index">
                            <q-item-section avatar>
                              <q-icon name="link"/>
                            </q-item-section>
                            <q-item-section>
                              <q-item-label style="word-break: break-all;">{{ uri }}</q-item-label>
                            </q-item-section>
                          </q-item>
                        </q-list>
                        <no-results v-if="client.redirectUri == null || client.redirectUri.length == 0"/>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side top>
                      <q-btn @click="updateRedirectUri" v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient" flat
                             round
                             size="12px"
                             no-caps
                             icon="edit"/>
                    </q-item-section>
                  </q-item>

                  <!-- 应用授权作用域, Client Scopes -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientScopes") }}</q-item-label>
                      <q-item-label>
                        <q-list>
                          <q-item class="" v-for="(scope,index) in client.scopes" :key="scope.sid">
                            <q-item-section avatar>
                              <q-icon name="policy"/>
                            </q-item-section>
                            <q-item-section>
                              <q-item-label overline>{{ scope.name }}</q-item-label>
                              <q-item-label>{{ scope.subtitle }}</q-item-label>
                              <q-item-label caption>{{ scope.description }}</q-item-label>
                            </q-item-section>
                          </q-item>
                        </q-list>
                        <no-results v-if="client.scopes == null || client.scopes.length == 0"/>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side top>
                      <q-btn @click="updateScopes" v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient" flat round
                             size="12px"
                             no-caps
                             icon="edit"/>
                    </q-item-section>
                  </q-item>

                  <!-- 应用权限, Client Authorities -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientAuthorities") }}</q-item-label>
                      <q-item-label>
                        <q-chip icon="security" :size="wide?'14px':'12px'" class=""
                                v-for="(authority,index) in client.authorities"
                                :key="authority.aid">
                          {{ authority }}
                        </q-chip>
                        <no-results v-if="client.authorities == null || client.authorities.length == 0"/>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side top>
                      <q-btn @click="updateAuthorities" v-if="hasGrantClientPermission" flat round size="12px" no-caps
                             icon="edit"/>
                    </q-item-section>
                  </q-item>

                  <!-- 应用授权模式, Client Grant Types -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientGrantTypes") }}</q-item-label>
                      <q-item-label>
                        <q-list>
                          <q-item dense v-for="(type,index) in client.grantTypes" :key="index">
                            <q-item-section avatar>
                              <q-icon name="electrical_services"/>
                            </q-item-section>
                            <q-item-section>
                              <q-item-label>{{ type }}</q-item-label>
                            </q-item-section>
                          </q-item>
                        </q-list>
                        <no-results v-if="client.grantTypes == null || client.grantTypes.length == 0"/>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side top>
                      <q-btn @click="updateGrantTypes" v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient" flat
                             round size="12px"
                             no-caps
                             icon="edit"/>
                    </q-item-section>
                  </q-item>

                  <!-- 应用成员, Client Members -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "clientMembers") }}</q-item-label>
                      <q-item-label v-if="membersPromise">
                        <q-chip v-for="i in 3" :key="i">
                          <q-avatar size="30px">
                            <q-skeleton size="30px" type="QAvatar"/>
                          </q-avatar>
                          <div style="width: 2em"/>
                        </q-chip>
                      </q-item-label>
                      <div v-else>
                        <q-item-label v-if="members == null || members.length == 0">
                          <no-results/>
                        </q-item-label>
                        <q-item-label v-else>
                          <q-chip loading v-for="(member,index) in members" :key="member.uid" clickable
                                  @click="()=> $router.push({name:'user',params:{id:member.uid}})">
                            <avatar :user="member"/>
                            {{
                              member.nickname && member.nickname.trim() ? member.nickname.trim() : member.username
                            }}
                          </q-chip>
                        </q-item-label>
                      </div>
                    </q-item-section>
                    <q-item-section side top>
                      <q-btn @click="updateMembers" v-if="hasWriteClientPermissionOrOwnClient" flat
                             round size="12px"
                             no-caps
                             icon="edit"/>
                    </q-item-section>
                  </q-item>

                  <!-- Access Token 有效期 -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "accessTokenValidity") }}</q-item-label>
                      <q-item-label v-if="edit.accessTokenValidity==null" class="content">
                        <q-item dense>
                          <q-item-section style="min-width: 0px;" avatar>
                            <q-icon name="timer"/>
                          </q-item-section>
                          <q-item-section>
                            <q-item-label>{{ client.accessTokenValidity || "0" }}
                              <span>{{ $t('seconds') }}</span>
                            </q-item-label>
                          </q-item-section>
                        </q-item>
                      </q-item-label>
                      <q-item-label v-else class="text-right">
                        <q-form @submit="updateAccessTokenValidity">
                          <q-input :placeholder="$tt($options, 'accessTokenValidityHint')"
                                   :disable="updating.accessTokenValidity"
                                   :loading="updating.accessTokenValidity"
                                   dense filled
                                   class="q-mb-sm"
                                   type="number"
                                   min="0"
                                   step="1"
                                   color="accent"
                                   v-model="edit.accessTokenValidity">
                            <template v-slot:prepend>
                              <q-icon name="timer"/>
                            </template>
                            <template v-slot:after>
                              <div class="q-pa-sm text-subtitle1">
                                {{ $t('seconds') }}
                              </div>
                            </template>
                          </q-input>
                          <q-btn :disable="updating.accessTokenValidity" no-caps flat :label="$t('cancel')"
                                 color="grey-7"
                                 class="q-mr-sm"
                                 @click="()=>edit.accessTokenValidity=null"/>
                          <q-btn :loading="updating.accessTokenValidity" type="submit" no-caps :label="$t('update')"
                                 color="accent"/>
                        </q-form>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side top v-if="edit.accessTokenValidity==null">
                      <q-btn @click="updateAccessTokenValidity" v-if="hasWriteClientPermission" flat round size="12px"
                             no-caps
                             icon="edit"/>
                    </q-item-section>
                  </q-item>

                  <!-- Refresh Token 有效期 -->
                  <q-item class="q-pa-none q-mt-md">
                    <q-item-section>
                      <q-item-label header class="q-pl-none">{{ $tt($options, "refreshTokenValidity") }}</q-item-label>
                      <q-item-label v-if="edit.refreshTokenValidity==null" class="content">
                        <q-item dense>
                          <q-item-section style="min-width: 0px;" avatar>
                            <q-icon name="timer"/>
                          </q-item-section>
                          <q-item-section>
                            <q-item-label>{{ client.refreshTokenValidity || "0" }}
                              <span>{{ $t('seconds') }}</span>
                            </q-item-label>
                          </q-item-section>
                        </q-item>
                      </q-item-label>
                      <q-item-label v-else class="text-right">
                        <q-form @submit="updateRefreshTokenValidity">
                          <q-input :placeholder="$tt($options, 'refreshTokenValidity')"
                                   :disable="updating.refreshTokenValidity"
                                   :loading="updating.refreshTokenValidity"
                                   dense filled
                                   class="q-mb-sm"
                                   type="number"
                                   min="0"
                                   step="1"
                                   color="accent"
                                   v-model="edit.refreshTokenValidity">
                            <template v-slot:prepend>
                              <q-icon name="timer"/>
                            </template>
                            <template v-slot:after>
                              <div class="q-pa-sm text-subtitle1">
                                {{ $t('seconds') }}
                              </div>
                            </template>
                          </q-input>
                          <q-btn :disable="updating.refreshTokenValidity" no-caps flat :label="$t('cancel')"
                                 color="grey-7"
                                 class="q-mr-sm"
                                 @click="()=>edit.refreshTokenValidity=null"/>
                          <q-btn :loading="updating.refreshTokenValidity" type="submit" no-caps :label="$t('update')"
                                 color="accent"/>
                        </q-form>
                      </q-item-label>
                    </q-item-section>
                    <q-item-section side top v-if="edit.refreshTokenValidity==null">
                      <q-btn @click="updateRefreshTokenValidity" v-if="hasWriteClientPermission" flat round size="12px"
                             no-caps
                             icon="edit"/>
                    </q-item-section>
                  </q-item>

                  <!-- 操作按钮 -->
                  <q-separator v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient" class="q-mt-md"/>
                  <div v-if="hasWriteClientPermissionOrOwnClientOrMemberOfClient"
                       class="text-right q-gutter-sm q-pt-lg">

                    <!-- 高级设置 -->
                    <q-btn
                      :to="{name:'client-advanced-settings',params:{id:clientId,client: client,owner: owner},query: uid ? {uid:uid}:null}"
                      color="grey-7"
                      flat icon="settings" :label="$tt($options,'AdvancedSettings')" no-caps/>

                    <!-- 删除应用 -->
                    <q-btn v-if="hasWriteClientPermissionOrOwnClient"
                           :loading="deleting" @click="deleteClient" no-caps color="negative" icon="delete"
                           :label="$tt($options,'deleteClient')"/>
                  </div>
                </q-list>
              </div>
            </template>
          </edit-page>
          <!-- 错误页面 -->
          <q-page v-else class="text-center">
            <div class="q-pa-md">
              <div style="font-size: 10vh">
                {{ error.details ? error.message : error.name }}
              </div>
              <div class="text-h4" style="opacity:.4">
                {{ error.details || error.message }}
              </div>
            </div>
          </q-page>
        </div>

        <!-- 删除后页面 -->
        <q-page v-else class="text-center">
          <div class="q-pa-md">
            <div style="font-size: 10vh">
              <q-icon name="delete"/>
            </div>
            <div class="text-h4" style="opacity:.4">
              {{ $tt($options, "deleted") }}
            </div>
          </div>
        </q-page>

      </template>
    </require-authorization>
    <!-- 回调地址 -->
    <q-dialog :persistent="updating.redirectUri" style="max-width: 400px;" :value="edit.redirectUri != null"
              @input="val=>{if(!val)edit.redirectUri=null}">
      <q-card class="full-width">
        <q-card-section>
          <div class="text-h6">{{ $tt($options, 'clientRedirectUri') }}</div>
        </q-card-section>
        <q-card-section v-if="edit.redirectUri" class="q-pa-none">
          <q-list>
            <no-results class="q-ma-sm" v-if="edit.redirectUri.length==0"/>
            <transition
              v-for="(uri,index) in edit.redirectUri"
              :key="index"
              appear
              enter-active-class="animated fadeIn"
              leave-active-class="animated fadeOut"
            >
              <q-item>
                <q-item-section>
                  <q-item-label>
                    <q-input :disable="updating.redirectUri" color="accent" filled dense
                             placeholder="URI"
                             v-model="edit.redirectUri[index]">
                      <template v-slot:prepend>
                        <q-icon name="link"/>
                      </template>
                      <template v-slot:after>
                        <q-btn :disable="updating.redirectUri" @click="edit.redirectUri.splice(index,1)" round flat
                               icon="remove"/>
                      </template>
                    </q-input>
                  </q-item-label>
                </q-item-section>
              </q-item>
            </transition>
            <q-item>
              <q-item-section>
                <q-item-label>
                  <q-form>
                    <q-input
                      v-model="edit.newRedirectUri" placeholder="URI" color="accent" filled dense>
                      <template v-slot:prepend>
                        <q-icon name="link"/>
                      </template>
                      <template v-slot:after>
                        <q-btn type="submit" :disable="updating.redirectUri || edit.newRedirectUri.trim().length==0"
                               @click="()=>{edit.redirectUri.push(edit.newRedirectUri.trim());edit.newRedirectUri=''}"
                               round
                               flat
                               icon="add"/>
                      </template>
                    </q-input>
                  </q-form>
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-list>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat :label="$t('cancel')" color="grey-7" :disable="updating.redirectUri" v-close-popup/>
          <q-btn :label="$t('update')" color="accent"
                 :disable="updating.redirectUri"
                 :loading="updating.redirectUri"
                 @click="updateRedirectUri"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <!-- 授权作用域 -->
    <q-dialog :persistent="updating.scopes.length>0" style="max-width: 400px;" v-model="edit.scopes">
      <q-card class="full-width">
        <q-card-section>
          <div class="row items-center no-wrap">
            <div class="text-h6 col">{{ $tt($options, 'clientScopes') }}</div>
            <div class="col-auto text-caption text-grey" v-if="scopes">
              {{ (client && client.scopes ? client.scopes.length : 0) + " / " + scopes.length }}
            </div>
          </div>
        </q-card-section>
        <q-card-section v-if="!scopesLoading" class="q-pa-none">
          <q-list v-if="scopes && scopes.length>0">
            <transition
              v-for="(scope,index) in scopes"
              :key="scope.sid"
              appear
              enter-active-class="animated fadeIn"
              leave-active-class="animated fadeOut"
            >
              <q-item clickable v-ripple>
                <q-item-section avatar style="min-width: 0px;">
                  <q-icon :color="getClientScopeIndex(scope.sid) > -1?'accent':''" name="policy"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label v-if="getClientScopeIndex(scope.sid) > -1" class="text-accent" overline>
                    {{ scope.name }}
                  </q-item-label>
                  <q-item-label v-else overline>
                    {{ scope.name }}
                  </q-item-label>
                  <q-item-label>
                    {{ scope.subtitle }}
                  </q-item-label>
                  <q-item-label caption>
                    {{ scope.description }}
                  </q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-btn
                    :disable="updating.scopes.indexOf(scope.sid)>-1"
                    :loading="updating.scopes.indexOf(scope.sid)>-1"
                    @click="()=>addOrRemoveScope(scope)"
                    flat round :icon="getClientScopeIndex(scope.sid) > -1?'remove':'add'"/>
                </q-item-section>
              </q-item>
            </transition>
          </q-list>
          <no-results class="q-ma-sm" v-else/>
        </q-card-section>
        <q-card-section v-else style="height: 80px;">
          <q-inner-loading :showing="scopesLoading">
            <q-spinner-gears size="50px" color="accent"/>
          </q-inner-loading>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn :label="$t('done')" color="accent"
                 :disable="updating.scopes.length>0"
                 :loading="updating.scopes.length>0"
                 v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <!-- 权限 -->
    <q-dialog :persistent="updating.authorities.length>0" style="max-width: 400px;" v-model="edit.authorities">
      <q-card class="full-width">
        <q-card-section>
          <div class="row items-center no-wrap">
            <div class="text-h6 col">{{ $tt($options, 'clientAuthorities') }}</div>
            <div class="col-auto text-caption text-grey" v-if="authorities">
              {{ (client && client.authorities ? client.authorities.length : 0) + " / " + authorities.length }}
            </div>
          </div>
        </q-card-section>
        <q-card-section v-if="!authoritiesLoading" class="q-pa-none">
          <q-list v-if="authorities && authorities.length>0">
            <transition
              v-for="(authority,index) in authorities"
              :key="authority.aid"
              appear
              enter-active-class="animated fadeIn"
              leave-active-class="animated fadeOut"
            >
              <q-item clickable v-ripple>
                <q-item-section avatar style="min-width: 0px;">
                  <q-icon
                    :color="client.authorities && client.authorities.indexOf(authority.authorityName) > -1?'accent':''"
                    name="policy"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label v-if="client.authorities && client.authorities.indexOf(authority.authorityName) > -1"
                                class="text-accent">
                    {{ authority.authorityName }}
                  </q-item-label>
                  <q-item-label v-else>
                    {{ authority.authorityName }}
                  </q-item-label>
                  <q-item-label caption>
                    {{ authority.authorityDescription }}
                  </q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-btn
                    :disable="updating.authorities.indexOf(authority.aid)>-1"
                    :loading="updating.authorities.indexOf(authority.aid)>-1"
                    @click="()=>addOrRemoveAuthorities(authority)"
                    flat round
                    :icon="client.authorities && client.authorities.indexOf(authority.authorityName) > -1?'remove':'add'"/>
                </q-item-section>
              </q-item>
            </transition>
          </q-list>
          <no-results class="q-ma-sm" v-else/>
        </q-card-section>
        <q-card-section v-else style="height: 80px;">
          <q-inner-loading :showing="authoritiesLoading">
            <q-spinner-gears size="50px" color="accent"/>
          </q-inner-loading>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn :label="$t('done')" color="accent"
                 :disable="updating.authorities.length>0"
                 :loading="updating.authorities.length>0"
                 v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <!-- 授权模式 -->
    <q-dialog :persistent="updating.grantTypes.length>0" style="max-width: 400px;" v-model="edit.grantTypes">
      <q-card class="full-width">
        <q-card-section>
          <div class="row items-center no-wrap">
            <div class="text-h6 col">{{ $tt($options, 'clientGrantTypes') }}</div>
            <div class="col-auto text-caption text-grey" v-if="grantTypes">
              {{ (client && client.grantTypes ? client.grantTypes.length : 0) + " / " + grantTypes.length }}
            </div>
          </div>
        </q-card-section>
        <q-card-section v-if="!grantTypesLoading" class="q-pa-none">
          <q-list v-if="grantTypes && grantTypes.length>0">
            <transition
              v-for="(type,index) in grantTypes"
              :key="type.tid"
              appear
              enter-active-class="animated fadeIn"
              leave-active-class="animated fadeOut"
            >
              <q-item clickable v-ripple>
                <q-item-section avatar style="min-width: 0px;">
                  <q-icon
                    :color="client.grantTypes && client.grantTypes.indexOf(type.name) > -1?'accent':''"
                    name="electrical_services"/>
                </q-item-section>
                <q-item-section>
                  <q-item-label v-if="client.grantTypes && client.grantTypes.indexOf(type.name) > -1"
                                class="text-accent">
                    {{ type.name }}
                  </q-item-label>
                  <q-item-label v-else>
                    {{ type.name }}
                  </q-item-label>
                  <q-item-label caption>
                    {{ type.description }}
                  </q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-btn
                    :disable="updating.grantTypes.indexOf(type.tid)>-1"
                    :loading="updating.grantTypes.indexOf(type.tid)>-1"
                    @click="()=>addOrRemoveGrantType(type)"
                    flat round
                    :icon="client.grantTypes && client.grantTypes.indexOf(type.name) > -1?'remove':'add'"/>
                </q-item-section>
              </q-item>
            </transition>
          </q-list>
          <no-results class="q-ma-sm" v-else/>
        </q-card-section>
        <q-card-section v-else style="height: 80px;">
          <q-inner-loading :showing="grantTypesLoading">
            <q-spinner-gears size="50px" color="accent"/>
          </q-inner-loading>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn :label="$t('done')" color="accent"
                 :disable="updating.grantTypes.length>0"
                 :loading="updating.grantTypes.length>0"
                 v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <!-- 成员 -->
    <q-dialog :persistent="updating.members.length>0" style="max-width: 400px;" v-model="edit.members">
      <q-card class="full-width">
        <q-card-section>
          <div class="text-h6">
            {{ $tt($options, 'clientMembers') }}
          </div>
        </q-card-section>

        <q-card-section v-if="edit.members">
          <div>
            <no-results class="q-ma-sm" v-if="members==null || members.length == 0"/>
            <transition
              v-for="(member,index) in members"
              :key="member.uid"
              appear
              enter-active-class="animated fadeIn"
              leave-active-class="animated fadeOut"
            >
              <q-chip :removable="updating.members.indexOf(member.uid) == -1" loading clickable
                      @remove="() => removeOrAddMember(member)">
                <avatar :user="member"/>
                {{
                  member.nickname && member.nickname.trim() ? member.nickname.trim() : member.username
                }}
                <q-spinner-tail
                  v-if="updating.members.indexOf(member.uid)>-1"
                  class="q-pl-sm"
                  color="grey"
                  size="1.5em"
                />
                <q-tooltip>
                  <q-item class="q-pa-none">
                    <q-item-section avatar style="min-width: 0px">
                      <avatar :user="member"/>
                    </q-item-section>
                    <q-item-section>
                      <q-item-label>
                        {{ member.username }}
                      </q-item-label>
                      <q-item-label v-if="member.email">
                        <q-icon name="email"/>
                        {{ member.email }}
                      </q-item-label>
                      <q-item-label v-if="member.phone">
                        <q-icon name="phone"/>
                        {{ member.phone }}
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </q-tooltip>
              </q-chip>
            </transition>
          </div>
        </q-card-section>

        <div class="q-pa-sm">
          <q-separator/>
        </div>

        <users-list>
          <template v-slot:side="{user}">
            <q-btn
              :disable="updating.members.indexOf(user.uid)>-1"
              :loading="updating.members.indexOf(user.uid)>-1"
              @click.stop="()=>removeOrAddMember(user)"
              flat round
              :icon="client && client.members && client.members.indexOf(user.uid) >= 0 ? 'remove' : 'add'"/>
          </template>
        </users-list>

        <q-card-actions align="right">
          <q-btn :disable="updating.members.length>0"
                 :loading="updating.members.length>0"
                 :label="$t('done')" color="accent" v-close-popup/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import EditPage from "../../components/common/EditPage";
import Avatar from "../../components/api/Avatar";
import RequireAuthorization from "../../components/common/RequireAuthorization";
import ClientLogo from "../../components/api/ClientLogo";
import NoResults from '../../components/common/NoResults'
import UsersList from "../../components/api/UsersList";

export default {
  name: "Client",
  components: {UsersList, ClientLogo, RequireAuthorization, Avatar, EditPage, NoResults},
  data() {
    return {
      user_: null,
      uid: this.$route.query.uid,
      clientId: this.$route.params.id,
      client: null,
      loading: false,
      error: null,
      owner: null,
      logo: null,
      logoUploading: false,
      reader: new FileReader(),
      secretRegenerating: false,
      edit: {
        name: null,
        description: null,
        redirectUri: null,
        accessTokenValidity: null,
        refreshTokenValidity: null,
        scopes: false,
        authorities: false,
        grantTypes: false,
        members: false,
        newRedirectUri: ""
      },
      updating: {
        name: false,
        description: false,
        redirectUri: false,
        accessTokenValidity: false,
        refreshTokenValidity: false,
        scopes: [],
        authorities: [],
        grantTypes: [],
        members: []
      },
      rules: {
        name: [val => val && val.length <= 64 && (val = val.trim()).length > 0 || this.$tt(this, "clientNameRule")],
        description: [val => val && val.length <= 256 && (val = val.trim()).length > 0 || this.$tt(this, "clientDescriptionRule")]
      },
      scopes: null,
      scopesLoading: false,
      authorities: null,
      authoritiesLoading: false,
      grantTypes: null,
      grantTypesLoading: false,
      members: null,
      membersPromise: null,
      deleting: false,
      deleted: false
    }
  },
  methods: {
    hasPermission(permission) {
      return this.user_ && this.user_.authorities && this.user_.authorities.indexOf(permission) >= 0;
    },
    loadClient() {
      if (this.loading)
        return;
      this.loading = true;
      (this.uid == null ?
          this.$clientApi.getClient(this.clientId) :
          this.$clientApi.getUserClient(this.uid, this.clientId)
      )
        .then((res) => {
          this.client = res.data
          let usr = this.user_;
          if (usr && usr.uid == this.client.uid)
            this.owner = usr;
          else
            this.$usersApi.getUser(this.client.uid).then((res) => this.owner = res.data);
        })
        .catch(e => this.error = e)
        .finally(() => this.loading = false)
    },
    showUpdateSuccessMessage() {
      this.$q.notify({
        message: this.$t("updateSuccess"),
        type: 'positive'
      })
    },
    showDeleteSuccessMessage() {
      this.$q.notify({
        message: this.$t("deleteSuccess"),
        type: 'positive'
      })
    },
    changeLogo(p) {
      if (this.logoUploading)
        return;
      this.$refs.logoPicker.pickFiles(p);
    },
    onLogoPicked(file) {
      if (file == null || this.logoUploading)
        return;
      this.logoUploading = true;
      (this.uid == null ?
          this.$clientApi.updateClientLogo(this.clientId, file, {
            transformRequest: (data, header) => {
              header["Content-Type"] = "" // 阿里云OSS生成签名的URL限制了Content-TYpe
              return data;
            }
          }) :
          this.$clientApi.updateUserClientLogo(this.uid, this.clientId, file, {
            transformRequest: (data, header) => {
              header["Content-Type"] = "" // 阿里云OSS生成签名的URL限制了Content-TYpe
              return data;
            }
          })
      ).then(res => {
        this.reader.readAsDataURL(file);
        let cb = (r) => this.logo = r;
        this.reader.onload = function () {
          cb(this.result)
        }
        this.showUpdateSuccessMessage();
      }).finally(() => this.logoUploading = false)
    },
    regenerateSecret() {
      if (this.secretRegenerating)
        return;
      this.$q.dialog({
        title: this.$tt(this, "regenerateSecret"),
        message: this.$tt(this, "regenerateSecretMsg"),
        color: "negative",
        ok: {},
        cancel: {
          flat: true
        }
      }).onOk(() => {
        if (this.secretRegenerating)
          return;
        this.secretRegenerating = true;
        (this.uid == null ?
            this.$clientApi.updateClientSecret(this.clientId) :
            this.$clientApi.updateUserClientSecret(this.uid, this.clientId)
        )
          .then(res => {
            this.client.secret = res.data;
            this.showUpdateSuccessMessage();
          })
          .finally(() => this.secretRegenerating = false)
      })
    },
    updateName() {
      if (this.edit.name == null)
        this.edit.name = this.client.name;
      else {
        if (this.updating.name)
          return;
        if (this.edit.name == this.client.name) {
          this.edit.name = null;
          return;
        }
        this.updating.name = true;
        (this.uid == null ?
            this.$clientApi.updateClientName(this.clientId, this.edit.name) :
            this.$clientApi.updateUserClientName(this.uid, this.clientId, this.edit.name)
        ).then(() => {
          this.client.name = this.edit.name;
          this.edit.name = null;
          this.showUpdateSuccessMessage();
        }).finally(() => {
          this.updating.name = false;
        })
      }
    },
    updateAccessTokenValidity() {
      if (this.edit.accessTokenValidity == null)
        this.edit.accessTokenValidity = this.client.accessTokenValidity;
      else {
        if (this.updating.accessTokenValidity)
          return;
        if (this.edit.accessTokenValidity == this.client.accessTokenValidity) {
          this.edit.accessTokenValidity = null;
          return;
        }
        this.updating.accessTokenValidity = true;
        this.$clientApi.updateClientAccessTokenValidity(this.clientId, this.edit.accessTokenValidity)
          .then(() => {
            this.client.accessTokenValidity = this.edit.accessTokenValidity;
            this.edit.accessTokenValidity = null;
            this.showUpdateSuccessMessage();
          }).finally(() => {
          this.updating.accessTokenValidity = false;
        })
      }
    },
    updateRefreshTokenValidity() {
      if (this.edit.refreshTokenValidity == null)
        this.edit.refreshTokenValidity = this.client.refreshTokenValidity;
      else {
        if (this.updating.refreshTokenValidity)
          return;
        if (this.edit.refreshTokenValidity == this.client.refreshTokenValidity) {
          this.edit.refreshTokenValidity = null;
          return;
        }
        this.updating.refreshTokenValidity = true;
        this.$clientApi.updateClientRefreshTokenValidity(this.clientId, this.edit.refreshTokenValidity)
          .then(() => {
            this.client.refreshTokenValidity = this.edit.refreshTokenValidity;
            this.edit.refreshTokenValidity = null;
            this.showUpdateSuccessMessage();
          }).finally(() => {
          this.updating.refreshTokenValidity = false;
        })
      }
    },
    updateDescription() {
      if (this.edit.description == null)
        this.edit.description = this.client.description;
      else {
        if (this.updating.description)
          return;
        if (this.edit.description == this.client.description) {
          this.edit.description = null;
          return;
        }
        this.updating.description = true;
        (this.uid == null ?
            this.$clientApi.updateClientDescription(this.clientId, this.edit.description) :
            this.$clientApi.updateUserClientDescription(this.uid, this.clientId, this.edit.description)
        ).then(() => {
          this.client.description = this.edit.description;
          this.edit.description = null;
          this.showUpdateSuccessMessage();
        }).finally(() => {
          this.updating.description = false;
        })
      }
    },
    updateRedirectUri() {
      if (this.edit.redirectUri == null) {
        this.edit.redirectUri = [];
        if (this.client.redirectUri)
          this.client.redirectUri.forEach(uri => this.edit.redirectUri.push(uri));
      } else {
        if (this.updating.redirectUri)
          return;
        let r = "";
        let set = new Set();
        if (this.edit.redirectUri) {
          this.edit.redirectUri.forEach(uri => {
            if (uri == null || (uri = uri.trim()).length == 0)
              return;
            set.add(uri);
          });
          set.forEach((uri) => {
            if (r.length > 0)
              r += ",";
            r += uri;
          })
        }
        let r_ = "";
        if (this.client.redirectUri) { // 如果内容未更改，不进行更新。
          this.client.redirectUri.forEach(uri => {
            if (uri == null || (uri = uri.trim()).length == 0)
              return;
            if (r_.length > 0)
              r_ += ",";
            r_ += uri;
          })
        }
        if (r == r_) {
          this.edit.redirectUri = null;
          return;
        }
        this.updating.redirectUri = true;
        (this.uid == null ?
            this.$clientApi.updateClientRedirectUri(this.clientId, r) :
            this.$clientApi.updateUserClientRedirectUri(this.uid, this.clientId, r)
        ).then(() => {
          this.client.redirectUri = [];
          set.forEach(uri => this.client.redirectUri.push(uri));
          this.edit.redirectUri = null;
          this.showUpdateSuccessMessage();
        }).finally(() => {
          this.updating.redirectUri = false;
        })
      }
    },
    updateScopes() {
      if (this.edit.scopes)
        return;
      this.edit.scopes = true;
      if (this.scopes == null && !this.scopesLoading) {
        this.scopesLoading = true;
        this.$scopesApi.getScopes()
          .then(res => this.scopes = res.data)
          .finally(() => this.scopesLoading = false)
      }
    },
    getClientScopeIndex(sid) {
      if (this.client && this.client.scopes) {
        for (let index in this.client.scopes)
          if (this.client.scopes[index].sid == sid)
            return index;
        return -1;
      }
      return null;
    },
    addOrRemoveScope(scope) {
      if (this.updating.scopes.indexOf(scope.sid) >= 0)
        return;
      let contains = this.getClientScopeIndex(scope.sid) > -1;
      this.updating.scopes.push(scope.sid);
      let p = null;
      if (this.uid == null)
        p = contains ?
          this.$scopesApi.removeClientScopes(this.clientId, [scope.sid]) :
          this.$scopesApi.addClientScopes(this.clientId, [scope.sid]);
      else
        p = contains ?
          this.$scopesApi.removeUserClientScopes(this.uid, this.clientId, [scope.sid]) :
          this.$scopesApi.addUserClientScopes(this.uid, this.clientId, [scope.sid]);
      p.then(res => {
        if (contains)
          this.client.scopes.splice(this.getClientScopeIndex(scope.sid), 1);
        else
          this.client.scopes.push(scope);
      }).finally(() => {
        this.updating.scopes.splice(this.updating.scopes.indexOf(scope.sid), 1);
      })
    },
    updateAuthorities() {
      if (this.edit.authorities)
        return;
      this.edit.authorities = true;
      if (this.authorities == null && !this.authoritiesLoading) {
        this.authoritiesLoading = true;
        this.$authoritiesApi.getAuthorities()
          .then(res => this.authorities = res.data)
          .finally(() => this.authoritiesLoading = false)
      }
    },
    addOrRemoveAuthorities(authority) {
      if (this.updating.authorities.indexOf(authority.aid) >= 0)
        return;
      let contains = this.client.authorities && this.client.authorities.indexOf(authority.authorityName) > -1;
      this.updating.authorities.push(authority.aid);
      let p = contains ?
        this.$authoritiesApi.deleteClientAuthorities(this.clientId, [authority.aid]) :
        this.$authoritiesApi.setClientAuthorities(this.clientId, [authority.aid]);
      p.then(res => {
        if (contains)
          this.client.authorities.splice(this.client.authorities.indexOf(authority.authorityName), 1);
        else
          this.client.authorities.push(authority.authorityName);
      }).finally(() => {
        this.updating.authorities.splice(this.updating.authorities.indexOf(authority.aid), 1);
      })
    },
    updateGrantTypes() {
      if (this.edit.grantTypes)
        return;
      this.edit.grantTypes = true;
      if (this.grantTypes == null && !this.grantTypesLoading) {
        this.grantTypesLoading = true;
        this.$grantTypesApi.getGrantTypes()
          .then(res => this.grantTypes = res.data)
          .finally(() => this.grantTypesLoading = false)
      }
    },
    updateMembers() {
      if (this.edit.members)
        return;
      this.edit.members = true;
    },
    indexOfMember(member) {
      if (member == null || member.uid == null || this.members == null)
        return -1;
      for (let i in this.members)
        if (this.members[i].uid == member.uid)
          return i;
      return -1;
    },
    removeOrAddMember(member) {
      if (this.updating.members.indexOf(member.uid) >= 0)
        return;
      let contains = this.client.members && this.client.members.indexOf(member.uid) > -1;
      this.updating.members.push(member.uid);
      let p = null;
      if (this.uid == null)
        p = contains ?
          this.$clientApi.removeClientMembers(this.clientId, [member.uid]) :
          this.$clientApi.addClientMembers(this.clientId, [member.uid]);
      else
        p = contains ?
          this.$clientApi.removeUserClientMembers(this.uid, this.clientId, [member.uid]) :
          this.$clientApi.addUserClientMembers(this.uid, this.clientId, [member.uid]);
      p.then(res => {
        if (contains) {
          this.client.members.splice(this.client.members.indexOf(member.uid), 1)
          // this.members.splice(this.indexOfMember(member), 1)
        } else {
          this.client.members.push(member.uid)
          // this.members.push(member)
        }
      }).finally(() => {
        this.updating.members.splice(this.updating.members.indexOf(member.uid), 1);
      })

    },
    addOrRemoveGrantType(type) {
      if (this.updating.grantTypes.indexOf(type.tid) >= 0)
        return;
      let contains = this.client.grantTypes && this.client.grantTypes.indexOf(type.name) > -1;
      this.updating.grantTypes.push(type.tid);
      let p = null;
      if (this.uid == null)
        p = contains ?
          this.$grantTypesApi.deleteClientGrantTypes(this.clientId, [type.tid]) :
          this.$grantTypesApi.addClientGrantTypes(this.clientId, [type.tid]);
      else
        p = contains ?
          this.$grantTypesApi.deleteUserClientGrantTypes(this.uid, this.clientId, [type.tid]) :
          this.$grantTypesApi.addUserClientGrantTypes(this.uid, this.clientId, [type.tid]);
      p.then(res => {
        if (contains)
          this.client.grantTypes.splice(this.client.grantTypes.indexOf(type.name), 1);
        else
          this.client.grantTypes.push(type.name);
      }).finally(() => {
        this.updating.grantTypes.splice(this.updating.grantTypes.indexOf(type.tid), 1);
      })
    },
    deleteClient() {
      if (this.deleting || this.deleted)
        return;
      this.$q.dialog({
        type: "warning",
        color: "negative",
        message: this.$tt(this, "deleteClientMsg"),
        title: this.$tt(this, "deleteClientTitle"),
        ok: {
          label: this.$t("delete")
        },
        cancel: true
      }).onOk(() => {
        this.deleting = true;
        (this.uid == null ?
            this.$clientApi.removeClient(this.clientId) :
            this.$clientApi.removeUserClient(this.uid, this.clientId)
        ).then(() => {
          this.deleted = true;
          this.showDeleteSuccessMessage();
        }).finally(() => this.deleting = false);
      })
    }
  },
  computed: {
    ownerName() {
      if (this.owner == null)
        return null;
      let name = this.owner.nickname;
      if (name == null || name.trim().length == 0)
        name = this.owner.username;
      return name;
    },
    isOwner() {
      return this.owner && this.user_ && this.owner.uid && this.user_.uid && this.owner.uid == this.user_.uid;
    },
    isMember() {
      return this.client && this.user_ && this.user_.uid && this.client.members && this.client.members.indexOf(this.user_.uid) >= 0
    },
    hasWriteClientPermission() {
      return this.hasPermission("WRITE_CLIENT");
    },
    hasWriteClientPermissionOrOwnClient() {
      return this.isOwner || this.hasWriteClientPermission;
    },
    hasWriteClientPermissionOrOwnClientOrMemberOfClient() {
      return this.isOwner || this.isMember || this.hasWriteClientPermission;
    },
    hasGrantClientPermission() {
      return this.hasPermission("GRANT_CLIENT");
    }
  },
  watch: {
    user_() {
      if (this.user_ && this.user_.uid)
        this.loadClient();
    },
    "client.members"() {
      if (this.client.members == null || this.client.members.length === 0) {
        this.members = null;
        this.membersPromise = null;
      } else {
        let p = () => this.$usersApi.getUsers(this.client.members)
          .then(res => {
            this.members = res.data.data;
          })
          .catch(e => {
            this.members = null;
            return e;
          })
          .finally(() => this.membersPromise = null);
        if (this.membersPromise == null)
          this.membersPromise = p()
        else {
          this.membersPromise.then(() => p())
        }
      }

    }
  }
}
</script>

<style scoped>
.code {
  font-family: Consolas;
  font-size: 16px;
  word-break: break-all;
}

.content {
  font-family: Consolas;
  font-size: 16px;
}
</style>
