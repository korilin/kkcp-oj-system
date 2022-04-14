<script setup>
import KotlinEditorVue from '../components/KotlinEditor.vue';
import { ref } from 'vue'

const code = ref("")
function onChange(v) {
  code.value = v
}

</script>
<template>
  <div>
    <KotlinEditorVue style="height: 80vh;" :code="code" :onChange="onChange" />
  </div>
</template>


