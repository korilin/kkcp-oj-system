<script setup>
import { ref } from 'vue';
import Sidebar from "./components/Sidebar.vue"
import KotlinSVGVue from './components/KotlinSVG.vue';

const collapsed = ref(false)
</script>

<template>
  <a-layout class="layout-default" id="layout-default">
    <a-layout-sider
      theme="light"
      v-model:collapsed="collapsed"
      collapsible
      breakpoint="lg"
      collapsed-width="0"
      width="250px"
    >
      <KotlinSVGVue style="padding: 30px 50px 15px 30px;" />
      <Sidebar />
    </a-layout-sider>
    <a-layout class="main-layout">
      <a-layout-content>
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<style lang="scss">
html,
body {
  height: 100%;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#layout-default {
  min-height: 100vh;
}
.ant-layout-content {
  padding-top: 0 !important;
}
.main-layout {
  padding-left: 50px;
}
</style>
