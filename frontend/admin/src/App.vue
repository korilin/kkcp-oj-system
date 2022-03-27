<script setup>
import { ref, watch } from 'vue';
import Sidebar from "./components/Sidebar.vue"
import KotlinSVGVue from './components/KotlinSVG.vue';
import { useRoute, useRouter } from 'vue-router';
import { useAccountStore, useCommonStore } from "./plugins/pinia";
import HttpService from './utils/axios-service';
import Apis from './utils/apis';
const router = useRouter();
const route = useRoute();
const accountStore = useAccountStore();
const commonStore = useCommonStore();

const collapsed = ref(false);

/**
 * 请求平台基本数据，包括：
 * - 问题类型列表
 * - 问题难度列表
 */
function doCommonDataInit() {
  Apis.CommonModule.questionDefineData().then((result) => {
    if (result.levels.status) {
      commonStore.questionLevels = result.levels.data;
    }
    if (result.types.status) {
      commonStore.questionTypes = result.types.data;
    }
  })
}

/**
 * 初始化用户，当用户登录状态不可用时，移除所有登录信息，跳转到登录页面重新登录。
 * 当登录状态可时，将 token 和用户信息存储到 pinia 中
 */
function doAccountInit() {
  const kkcpAdminToken = window.sessionStorage.getItem(import.meta.env.VITE_ADMIN_TOKEN_KEY);
  const accountJson = window.sessionStorage.getItem(import.meta.env.VITE_ADMIN_ACCOUNT_KEY);
  const account = JSON.parse(accountJson);
  if (kkcpAdminToken == null ||
    kkcpAdminToken == "undefined" ||
    account == null ||
    account == "undefined") {
    router.push({ name: "login" });
  } else {
    accountStore.kkcpAdminToken = kkcpAdminToken;
    accountStore.account = account;
  }
}

doAccountInit();
doCommonDataInit();
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
        <router-view v-slot="{ Component, route }">
          <transition name="fade" mode="out-in">
            <div :key="route.name">
              <component :is="Component"></component>
            </div>
          </transition>
        </router-view>
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

<style scoped lang="scss">
.fade-enter-active,
.fade-leave-active {
  transition: all 0.1s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateX(20px);
}
</style>

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
