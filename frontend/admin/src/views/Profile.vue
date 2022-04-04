<script setup >
import { Modal } from 'ant-design-vue';
import { ref } from 'vue';
import { useAccountStore } from '../plugins/pinia';
import { goLogin } from '../utils/router-helper';

const accountStore = useAccountStore();
const account = ref(accountStore.account);

function logout() {
  Modal.confirm({
    title: "Do you want to logout?",
    okType: 'danger',
    onOk() {
      accountStore.clean()
      window.sessionStorage.removeItem(import.meta.env.VITE_ADMIN_TOKEN_KEY);
      window.sessionStorage.removeItem(import.meta.env.VITE_ADMIN_ACCOUNT_KEY);
      goLogin();
    },
  })
}
</script>

<template>
  <a-descriptions
    title="Admin Infomation"
    class="global-style-profile-desc"
    :column="{ xxl: 1, xl: 1, lg: 1, md: 1, sm: 1, xs: 1 }"
  >
    <a-descriptions-item label="Name">{{ account.name }}</a-descriptions-item>
    <a-descriptions-item label="Email">{{ account.email }}</a-descriptions-item>
    <a-descriptions-item label="Level">{{ account.level }}</a-descriptions-item>
  </a-descriptions>
  <a-alert
    style="margin-top: 30px"
    message="Contact Lv5 administrator to change your account infomation."
    type="info"
  />
  <div style="text-align: center;">
    <a-button type="primary" danger style="margin: 40px auto;" @click="logout">Logout</a-button>
  </div>
</template>

<style lang="scss">
.global-style-profile-desc {
  .ant-descriptions-title {
    font-size: 24px !important;
  }

  .ant-descriptions-item-label,
  .ant-descriptions-item-content {
    font-size: 16px !important;
    font-weight: 500;
    line-height: 2;
  }
}
</style>
