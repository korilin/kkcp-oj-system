<script setup>
import { ref, watch } from 'vue';
import Sidebar from "./components/Sidebar.vue"
import KotlinSVGVue from './components/KotlinSVG.vue';
import { useRoute, useRouter } from 'vue-router';
import { useAccountStore } from "./plugins/pinia";
const router = useRouter();
const route = useRoute();
const accountStore = useAccountStore();

const collapsed = ref(false);

accountStore.kkcpAdminToken = window.sessionStorage.getItem(import.meta.env.VITE_ADMIN_TOKEN_KEY);

if (accountStore.kkcpAdminToken == null) {
  router.push({ name: "login" });
}
</script>

<template>
  <a-layout
    class="layout-default"
    id="layout-default"
    v-if="route.name != 'login' && route.name != 'error'"
  >
    <a-layout-sider
      theme="light"
      v-model:collapsed="collapsed"
      collapsible
      breakpoint="lg"
      collapsed-width="0"
      width="250px"
    >
      <KotlinSVGVue style="padding: 50px 50px 40px 40px;" />
      <Sidebar />
    </a-layout-sider>
    <a-layout class="main-layout">
      <a-layout-content>
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
  <template v-else-if="route.name == 'login'">
    <router-view />
  </template>
  <template v-else>
    <router-view />
  </template>
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
.main-layout {
  padding: 50px;
  .ant-layout-content {
    padding: 50px;
    border-radius: 10px;
    background-color: white;
  }
}
.ant-layout-sider-zero-width-trigger {
  border-radius: 0px 10px 10px 0px !important;
}
</style>
