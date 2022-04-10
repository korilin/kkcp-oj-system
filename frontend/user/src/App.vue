<script setup>
import Header from "./components/Header.vue"
import { useCommonStore } from "./plugins/pinia";

const commonStore = useCommonStore()
commonStore.initData()

</script>

<template>
  <a-layout class="layout-default" id="layout-default">
    <a-layout-header>
      <Header />
    </a-layout-header>
    <a-layout-content>
      <router-view />
    </a-layout-content>
    <a-layout-footer>
    </a-layout-footer>
  </a-layout>
</template>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
.ant-layout-content {
  padding-top: 0 !important;
}
</style>
