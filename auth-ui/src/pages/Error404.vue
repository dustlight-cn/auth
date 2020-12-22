<template>
  <div class="fullscreen text-center q-pa-md flex flex-center">
    <div>
      <div style="font-size: 30vh">
        404
      </div>

      <div class="text-h2" style="opacity:.4">
        {{ $tt(this, "notFound") }}
      </div>

      <q-btn
        class="q-mt-xl"
        color="black"
        text-color="white"
        unelevated
        icon="arrow_back"
        @click="$router.back()"
        :label="$tt(this,'goBack')"
        no-caps
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Error404'
}
</script>
