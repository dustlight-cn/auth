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
  Vue.prototype.$util = {
    dateFormat(date, fmt) {
      if(typeof(date) != "object")
        date = new Date(date)
      let ret;
      if(fmt == null)
        fmt = "YYYY-mm-dd"
      const opt = {
        "Y+": date.getFullYear().toString(),        // 年
        "m+": (date.getMonth() + 1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "H+": date.getHours().toString(),           // 时
        "M+": date.getMinutes().toString(),         // 分
        "S+": date.getSeconds().toString()          // 秒
      };
      for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
          fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        }
        ;
      }
      ;
      return fmt;
    }
  }
  export default {
    name: 'App'
  }
</script>
