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
    <div class="auth-card-shade" :class="{ 'auth-card-shade-show': isAuthCardShow }">
        <div class="auth-card-wrapper">
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

.auth-card-shade {
    position: fixed;
    z-index: 100;
    width: 100%;
    height: 100%;
    left: 0;
    background-color: #00000000;
    display: flex;
    justify-content: center;
    align-items: center;
    visibility: hidden;
    transition: all 0.3s;

    .auth-card-wrapper {
        position: relative;
        top: 50px;
        opacity: 0;
        transition: all 0.3s;
    }

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
.auth-card-shade-show {
    visibility: visible;
    background-color: #00000080;
    .auth-card-wrapper {
        top: 0px;
        opacity: 1;
    }
}
</style>
