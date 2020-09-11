<template>
  <div id="q-app">
    <router-view/>
  </div>
</template>
<script>
  import Vue from "vue";

  const errorHandler = (e, vm) => {
    console.error(e);
    if (e.response) {
      Vue.prototype.$q.notify({
        message: e.response.statusText + " " + e.response.status,
        caption: e.response.data,
        icon: 'warning',
        color: 'negative'
      })
    } else {
      Vue.prototype.$q.notify({
        message: "Error",
        caption: e.message,
        icon: 'warning',
        color: 'negative'
      })
    }

  }
  Vue.config.errorHandler = errorHandler;
  Vue.prototype.$throw = (error) => errorHandler(error, this);
  export default {
    name: 'App'
  }
</script>
