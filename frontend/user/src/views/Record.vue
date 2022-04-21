<script setup>
import { useRoute } from "vue-router";

const route = useRoute();

const contestId = route.params.contestId;


</script>

<template>
  <div>
    {{ contestId }}
  </div>
</template>
