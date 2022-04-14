<script setup>

const prop = defineProps({
  data: Array
})

</script>
<template>
  <a-list :data-source="data">
    <template #renderItem="{ item }"></template>
    <template #header>
      <div style="line-height: 34px; font-weight: bold;">Participants</div>
    </template>
    <template #loadMore>
      <div
        v-if="!initLoading && !loading"
        :style="{ textAlign: 'center', marginTop: '12px', height: '32px', lineHeight: '32px' }"
      >
        <a-button @click="onLoadMore">loading more</a-button>
      </div>
    </template>
  </a-list>
</template>
