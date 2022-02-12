<script setup>
import { ref } from 'vue';
import AuthCard from './AuthCard.vue';
import { CloseOutlined } from '@ant-design/icons-vue';

const isAuthCardShow = ref(false)
</script>

<template>
    <div class="header-wrap">
        <span class="account">
            <a-button @click="isAuthCardShow = true">登录/注册</a-button>
        </span>
    </div>
    <div class="auth-card-wrapper" v-show="isAuthCardShow">
        <div style="position: relative;">
            <AuthCard />
            <CloseOutlined class="auth-card-close-btn" @click="isAuthCardShow = false" />
        </div>
    </div>
</template>

<style scoped lang="scss">
.header-wrap {
    width: 100%;
    display: flex;
    justify-content: flex-end;
}

.ant-btn {
    height: 30px;
    padding: 0 10px;
    font-size: 12px;
    font-weight: 500;
}

.auth-card-wrapper {
    position: fixed;
    z-index: 100;
    width: 100%;
    height: 100%;
    left: 0;
    background-color: #00000080;
    display: flex;
    justify-content: center;
    align-items: center;

    .auth-card-close-btn {
        position: absolute;
        right: 20px;
        top: 20px;
        color: #8c8c8c;
        cursor: pointer;
        &:hover {
            color: #141414;
        }
    }
}
</style>
