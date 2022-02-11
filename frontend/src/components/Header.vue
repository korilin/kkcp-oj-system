<script setup>
</script>

<template>
    <div class="header-wrap">
        <span class="account">
            <a-button type="link">登录</a-button>
            <span class="text-gray-8">or</span>
            <a-button type="link">注册</a-button>
        </span>
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
</style>
