<script setup>
import Header from "./components/Header.vue"
import { useCommonStore, useUserStore } from "./plugins/pinia";
import HttpService from "./utils/axios-service";

const commonStore = useCommonStore()
const userStore = useUserStore()

async function initUser(uid) {
  const body = await HttpService.post(`/auth/token?uid=${uid}`)
  // 可能还是为 null
  if (body.status && body.data != null) {
    window.localStorage.setItem(import.meta.env.VITE_USER_TOKEN_KEY, body.data)
    userStore.token = body.data
    userStore.initProfile()
  }
}

function initApp() {
  const uid = window.localStorage.getItem(import.meta.env.VITE_UID_KEY);
  const token = window.localStorage.getItem(import.meta.env.VITE_USER_TOKEN_KEY);

  if (uid == null || uid == undefined) {
    window.localStorage.removeItem(import.meta.env.VITE_USER_TOKEN_KEY)
    HttpService.get("/auth/random").then(body => {
      if (body.status) {
        window.localStorage.setItem(import.meta.env.VITE_UID_KEY, body.data)
      }
    })
  } else if (token == null || token == undefined) {
    initUser(uid)
  } else {
    userStore.token = token
    userStore.initProfile()
  }
}

commonStore.initData()
initApp()
</script>

<template>
  <a-layout class="layout-default" id="layout-default">
    <a-layout-header v-if="commonStore.showHeader">
      <Header />
    </a-layout-header>
    <a-layout-content>
      <router-view v-if="commonStore.init" />
      <div v-else style="text-align: center; margin-top: 50px;">
        <a-spin />
      </div>
    </a-layout-content>
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

.avatar {
  border: #e9e9e980 2px solid;
  padding: 5px !important;
  width: 45px !important;
  height: 45px !important;
}
</style>
