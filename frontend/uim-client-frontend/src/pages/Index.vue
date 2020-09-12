<template>
  <q-page class="q-pa-md">
    <q-form
      class="q-pa-sm"
      @submit="request"
    >
      <q-input
        list="apis"
        label="URL" hint="任意URL地址。（访问API时请确保已登录对应账户）" v-model="model.url">
      </q-input>

      <datalist id="apis">
        <option v-for="option in apis">{{option}}</option>
      </datalist>
      <q-select label="Type" hint="返回数据类型。（text、json、html）" :options="options" v-model="model.type"/>
      <q-btn :loading="loading" color="primary" class="q-ma-sm" type="submit" label="发送请求"/>
    </q-form>
    <q-card v-if="result" bordered class="q-ma-sm-md">
      <div class="text-bold">{{time}}ms</div>
      <div v-if="model.type == 'html'" v-html="result"/>
      <pre v-else-if="model.type == 'json'">{{formatJson}}</pre>
      <pre v-else>{{result}}</pre>
    </q-card>
  </q-page>
</template>

<script>
  import qs from 'qs'

  export default {
    name: 'PageIndex',
    data() {
      return {
        apis: ["https://api.github.com/", "https://api.github.com/user", "https://account.dustlight.cn/api/res/details", "https://www.googleapis.com/oauth2/v3/userinfo"],
        options: ['text', 'json', 'html'],
        model: {
          url: "",
          type: "text"
        },
        result: "",
        loading: false,
        time: 0
      }
    },
    computed: {
      formatJson() {
        return JSON.stringify(this.result, "  ", "    ")
      }
    },
    methods: {
      request() {
        this.loading = true;
        let start = new Date();
        try {
          this.$uclient.ax.get("/api/request?" + qs.stringify(this.model))
            .then(r => {
              this.result = r
            }).catch(e => {
            this.result = e.message
          }).finally(() => {
            this.loading = false
            this.time = new Date() - start;
          })
        } catch (e) {
          this.result = e.message
          this.loading = false
          this.time = new Date() - start;
        }

      }
    }
  }
</script>
