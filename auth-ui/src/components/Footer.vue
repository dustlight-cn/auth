<template>
  <div class="q-ma-md text-center text-grey">
    <div>
      Powered by <a class="text-grey text-bold" target="_blank"
                    href="https://dustlight-cn.github.io/auth">Auth</a>
    </div>
    <div>
      <q-btn @click="openURL('http://github.com/dustlight-cn/auth')" size="12px" dense no-caps flat
             icon="img:icons/github.png"
             label="View on Github"/>
    </div>
  </div>
</template>

<script>
import {openURL} from 'quasar';

export default {
  name: "Footer",
  data() {
    return {
      openURL: openURL
    }
  }
}
</script>

<style scoped>

</style>
