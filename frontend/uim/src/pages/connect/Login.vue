<template>
  <q-page padding>
    <!-- 宽窗口 -->
    <q-page class="flex flex-center gt-xs">
      <q-form
        @submit="login" class="full-width shadow-2 q-pa-md rounded-borders"
        style="max-width: 599px">
        <div class="q-gutter-md">
          <div class="text-h4">{{$tt(this,"title")}}</div>
          <q-input
            color="accent"
            v-model="model.account"
            :label="$tt(this,'account')"
            :hint="$tt(this,'accountHint')"
            lazy-rules
            :rules="rule.account"
          />
          <q-input
            color="accent"
            v-model="model.password"
            :label="$tt(this,'password')"
            :hint="$tt(this,'passwordHint')"
            type="password"
            lazy-rules
            :rules="rule.password"
          />
          <div class="q-mb-md flex">
            <q-btn :label="$tt(this,'signUp')" @click="signUp" flat color="accent" class="q-ml-sm"/>
            <q-space/>
            <q-btn :label="$tt(this,'forgotPassword')" @click="forgotPassword" flat color="accent" class="q-ml-sm"/>
            <q-btn :label="$tt(this,'login')" type="submit" color="accent" class="q-ml-sm"/>
          </div>
        </div>
      </q-form>
    </q-page>

    <!-- 窄窗口 -->
    <q-page class="lt-sm">
      <q-form
        @submit="login" class="full-width q-pt-lg">
        <div class="q-gutter-md">
          <div class="text-h4 q-pb-lg">{{$tt(this,"title")}}</div>
          <q-input
            filled
            v-model="model.account"
            color="accent"
            :label="$tt(this,'account')"
            :hint="$tt(this,'accountHint')"
            lazy-rules
            :rules="rule.account"
          />
          <q-input
            filled
            v-model="model.password"
            color="accent"
            :label="$tt(this,'password')"
            :hint="$tt(this,'passwordHint')"
            type="password"
            lazy-rules
            :rules="rule.password"
          />
          <div class="flex q-gutter-sm">
            <q-btn :label="$tt(this,'signUp')" @click="signUp" flat color="accent" class="q-ml-sm"/>
            <q-space/>
            <q-btn :label="$tt(this,'forgotPassword')" @click="forgotPassword" flat color="accent" class="q-ml-sm"/>
            <q-btn :label="$tt(this,'login')" type="submit" color="accent" class="q-ml-sm"/>
          </div>
        </div>
      </q-form>
    </q-page>
  </q-page>
</template>

<script>
  export default {
    name: "Login",
    data() {
      return {
        model: {
          account: "",
          password: ""
        },
        rule: {
          account: [val => val && val.length > 0 || 'Please type something'],
          password: []
        }
      }
    },
    methods: {
      login() {
        this.$api.createSession(this.model.account, this.model.password)
          .then((user) => {
            console.log(user)
          }).catch((e) => {
          console.error(e)
        })
      },
      signUp() {

      },
      forgotPassword() {

      }
    }, mounted() {

    }
  }
</script>

<style scoped>

</style>
