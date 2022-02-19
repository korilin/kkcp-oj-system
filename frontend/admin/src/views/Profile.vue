<script setup >
import { ref } from 'vue';
import { useAccountStore } from '../plugins/pinia';

const accountStore = useAccountStore();
const account = ref(accountStore.account);

// TODO check account is null?
// TODO check token is vaild?

</script>

<template>
    <a-descriptions
        title="管理员信息"
        class="global-style-profile-desc"
        :column="{ xxl: 1, xl: 1, lg: 1, md: 1, sm: 1, xs: 1 }"
        size="middle"
    >
        <a-descriptions-item label="账户名">{{ account.name }}</a-descriptions-item>
        <a-descriptions-item label="邮箱">{{ account.email }}</a-descriptions-item>
        <a-descriptions-item label="权限级别">{{ account.level }}</a-descriptions-item>
    </a-descriptions>
    <a-alert style="margin-top: 30px" message="更改账户权限/密码请联系 Super Admin" type="info" />
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