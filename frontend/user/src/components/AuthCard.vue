<script setup>
import { GithubFilled } from "@ant-design/icons-vue";

const url = "https://github.com/login/oauth/authorize";
const clientId = "ec5b9941d159dac959aa";
const redirectUri = "http://localhost:2021/api/auth/callback"

function githubAuth() {
    const state = window.localStorage.getItem(import.meta.env.VITE_UID_KEY);
    const scope = "read:user user:email"
    window.open(url + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope + "&state=" + state, "_self");
}

</script>
<template>
    <a-card :bordered="false" class="widget-1 auth-card">
        <h5>登录/注册</h5>
        <div class="go-auth">
            <div>
                本平台用户采用
                <strong>GitHub</strong> 账户授权
            </div>
            <p>点击下面按钮前往授权页面</p>
            <GithubFilled class="github-icon" @click="githubAuth" />
        </div>
        <a-divider />
        <p style="text-align: left;">
            同意授权代表同意本平台的
            <a>《用户协议》</a> 和
            <a>《隐私协议》</a>。
        </p>
    </a-card>
</template>

<style lang="scss">
.auth-card {
    width: 300px;
    text-align: center;
    padding: 20px !important;
    padding-bottom: 0 !important;
    user-select: none;
}

.github-icon {
    font-size: 30px;
    padding: 8px;
    border-radius: 50%;
    border: 1px #d9d9d9 solid;
    margin-top: 10px;
    cursor: pointer;
}
</style>
