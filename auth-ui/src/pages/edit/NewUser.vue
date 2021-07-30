<template>
  <div>
    <require-authorization>
      <template v-slot="{user}">
        {{ "", user_ == null || user != null && user_.uid != user.uid ? user_ = user : "" }}
        <edit-page v-slot="{wide}" v-if="hasCreateUserPermission">
          <q-form @submit="create">
            <q-card-section>
              <div class="text-h6">{{ $tt($options, "title") }}</div>
            </q-card-section>
            <q-card-section>
              <q-list>
                <!-- 用户名 -->
                <q-item class="q-pa-none">
                  <q-item-section>
                    <q-item-label header class="q-pa-none">
                      {{ $tt($options, 'username') }}
                    </q-item-label>
                    <q-item-label>
                      <q-input
                        :dense="!wide"
                        debounce="1000"
                        :disable="creating"
                        v-model="data.username"
                        :rules="rules.username"
                        :placeholder="$tt($options,'usernameHint')"
                        color="accent" filled>
                      </q-input>
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <!-- 密码 -->
                <q-item class="q-pa-none q-mt-sm">
                  <q-item-section>
                    <q-item-label header class="q-pa-none">
                      {{ $tt($options, 'password') }}
                    </q-item-label>
                    <q-item-label>
                      <q-input
                        type="password"
                        :dense="!wide"
                        :disable="creating"
                        v-model="data.password"
                        :rules="rules.password"
                        :placeholder="$tt($options,'passwordHint')"
                        color="accent" filled>
                      </q-input>
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <!-- 重复密码 -->
                <q-item class="q-pa-none q-mt-sm">
                  <q-item-section>
                    <q-item-label header class="q-pa-none">
                      {{ $tt($options, 'confirmPassword') }}
                    </q-item-label>
                    <q-item-label>
                      <q-input
                        type="password"
                        :dense="!wide"
                        :disable="creating"
                        v-model="data.confirmPassword"
                        :rules="rules.confirmPassword"
                        :placeholder="$tt($options,'confirmPasswordHint')"
                        color="accent" filled>
                      </q-input>
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <!-- 电子邮箱 -->
                <q-item class="q-pa-none q-mt-sm">
                  <q-item-section>
                    <q-item-label header class="q-pa-none">
                      {{ $tt($options, 'email') }}
                    </q-item-label>
                    <q-item-label>
                      <q-input
                        :dense="!wide"
                        debounce="1000"
                        :disable="creating"
                        v-model="data.email"
                        :rules="rules.email"
                        :placeholder="$tt($options,'emailHint')"
                        color="accent" filled>
                      </q-input>
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <!-- 手机号码 -->
                <q-item class="q-pa-none q-mt-sm">
                  <q-item-section>
                    <q-item-label header class="q-pa-none">
                      {{ $tt($options, 'phone') }}
                    </q-item-label>
                    <q-item-label>
                      <q-input
                        :dense="!wide"
                        debounce="1000"
                        :disable="creating"
                        v-model="data.phone"
                        :rules="rules.phone"
                        :placeholder="$tt($options,'phoneHint')"
                        color="accent" filled>
                      </q-input>
                    </q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-card-section>
            <div class="text-right q-mt-lg q-mb-md">
              <q-btn no-caps :loading="creating" type="submit" color="accent" :label="$t('create')"/>
            </div>
          </q-form>
        </edit-page>
        <!-- 权限不足页面 -->
        <q-page v-else class="text-center">
          <div class="q-pa-md">
            <div style="font-size: 10vh">
              <q-icon name="security"/>
            </div>
            <div class="text-h4" style="opacity:.4">
              {{ $t('needPermission') }}
            </div>
          </div>
        </q-page>
      </template>
    </require-authorization>
  </div>
</template>

<script>
import EditPage from "../../components/EditPage";
import RequireAuthorization from "../../components/RequireAuthorization";

export default {
  name: "NewUser",
  components: {RequireAuthorization, EditPage},
  data() {
    return {
      user_: null,
      creating: false,
      data: {
        username: "",
        password: "",
        confirmPassword: "",
        email: "",
        phone: ""
      },
      rules: {
        username: [
          val => val && val.match(this.$cfg.pattern.username) || this.$tt("SignUp", "step1.usernameRule"),
          val => this.querying.username ?
            this.querying.username :
            (this.querying.username = this.$userApi.isUsernameExists(val)
                .then((res) => !res.data || this.$tt("SignUp", "step1.usernameExists"))
                .finally(() => this.querying.username = null)
            )
        ],
        password: [val => val && val.match(this.$cfg.pattern.password) || this.$tt("SignUp", "step1.passwordRule")],
        confirmPassword: [val => val == this.data.password || this.$tt("SignUp", "step1.confirmPasswordRule")],
        email: [
          val => !val || val.match(this.$cfg.pattern.email) || this.$tt("SignUp", "step1.emailRule"),
          val => !val || this.querying.email ?
            this.querying.email :
            (this.querying.email = this.$userApi.isEmailExists(val)
                .then((res) => !res.data || this.$tt("SignUp", "step1.emailExists"))
                .finally(() => this.querying.email = null)
            )
        ],
        phone: [
          val => !val || val.match(this.$cfg.pattern.phone) || this.$tt("SignUp", "step1.phoneRule"),
          val => !val || this.querying.phone ?
            this.querying.phone :
            (this.querying.phone = this.$userApi.isPhoneExists(val)
                .then((res) => !res.data || this.$tt("SignUp", "step1.phoneExists"))
                .finally(() => this.querying.phone = null)
            )]
      },
      querying: {
        username: null,
        email: null,
        phone: null
      }
    }
  },
  methods: {
    hasPermission(authority) {
      return this.user_ && this.user_.authorities && this.user_.authorities.indexOf(authority) >= 0;
    },
    showCreateSuccessMessage() {
      this.$q.notify({
        message: this.$t("createSuccess"),
        type: 'positive'
      })
    },
    create() {
      if (this.creating)
        return;
      this.creating = true;
      this.$usersApi.createUser(this.data.username, this.data.password, this.data.email, this.data.phone)
        .then(res => {
          this.showCreateSuccessMessage();
          this.$router.push({name: "user", params: {id: res.data.uid}});
        })
        .finally(() => this.creating = false);
    }
  },
  computed: {
    hasCreateUserPermission() {
      return this.hasPermission("CREATE_USER");
    }
  }
}
</script>

<style scoped>

</style>
