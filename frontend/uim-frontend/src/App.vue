<template>
  <div id="q-app">
    <router-view/>
  </div>
</template>

<script>

  //系统错误捕获
  import axios from "boot/axios";
  import Vue from "vue";

  const errorHandler = (e, vm) => {
    console.error(e);
    Vue.prototype.$q.notify({
      message: (e.message || '异常') + (e.code ? ", 错误码：" + e.code : ""),
      caption: (e.data ? e.data : e),
      icon: 'warning',
      color: 'negative'
    })
  }
  Vue.config.errorHandler = errorHandler;
  Vue.prototype.$throw = (error) => errorHandler(error, this);

  export default {
    name: 'App'
  }
</script>
