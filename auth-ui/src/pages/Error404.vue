<template>
  <div class="fullscreen text-center flex flex-center">
    <div class="q-pa-md">
      <div style="font-size: 25vh">
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
        :label="$t('goBack')"
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
