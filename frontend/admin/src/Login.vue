<script setup>
import { MailOutlined, LockOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAccountStore } from './plugins/pinia';
import HttpService from './utils/axios-service';

const accountStore = useAccountStore();
const router = useRouter();

const loginState = reactive({
    email: "",
    code: "",
})

const sendCodeText = ref("Send Code")
const sendCodeDisabled = ref(false)
let count = 60;
let counter = null;

const loginLoading = ref(false)

function checkToken() {
    const token = window.sessionStorage.getItem(import.meta.env.VITE_ADMIN_TOKEN_KEY);
    if (token != null) {
        router.push({ name: "profile" })
    }
}

function saveIntoStorage(token, account) {
    window.sessionStorage.setItem(import.meta.env.VITE_ADMIN_TOKEN_KEY, token);
    window.sessionStorage.setItem(import.meta.env.VITE_ADMIN_ACCOUNT_KEY, JSON.stringify(account));
    accountStore.kkcpAdminToken = token;
    accountStore.account = account;
}

function stopCounter() {
    count = 60;
    sendCodeDisabled.value = false;
    sendCodeText.value = "Send Code";
    window.clearInterval(counter);
}

function startCounter() {
    sendCodeDisabled.value = true;
    sendCodeText.value = count;
    counter = setInterval(() => {
        count -= 1;
        if (count < 0) {
            stopCounter();
        } else {
            sendCodeText.value = count;
        }
    }, 1000);
}
function sendCode() {
    const url = "/admin/verify/sendCode?email=" + loginState.email;
    startCounter();
    HttpService.post(url).then((response) => {
        const body = response.data;
        if (body.status) {
            message.success(body.message);
        } else {
            stopCounter();
        }
    }).catch((error) => {
        stopCounter();
    })
}

function tryLogin() {
    const data = {
        email: loginState.email,
        code: loginState.code
    }
    loginLoading.value = true;
    const url = "/admin/verify/login";
    HttpService.post(url, data).then((response) => {
        const body = response.data;
        if (body.status) {
            saveIntoStorage(body.data.token, body.data.account)
            router.push({ name: "profile" });
        }
    }).finally(() => {
        loginLoading.value = false;
    })
}

checkToken();
</script>
<template>
    <div class="flex-wrapper">
        <div style="text-align: left;">
            <div>
                <h1>KKCP | Admin</h1>
                <h5
                    class="text-gray-7"
                    style="font-weight: 100;"
                >使用邮箱登录 Kotlin Knowledge Contest Platform 管理后台</h5>
            </div>
            <a-form :model="loginState" class="form login-form-global-style" @finish="tryLogin">
                <a-form-item>
                    <a-input
                        style="width: 400px;"
                        v-model:value="loginState.email"
                        type="email"
                        placeholder="Admin Email"
                    >
                        <template #prefix>
                            <MailOutlined style="color: rgba(0, 0, 0, 0.25)" />
                        </template>
                    </a-input>
                </a-form-item>
                <a-form-item class="vc-form-item">
                    <a-input
                        v-model:value="loginState.code"
                        type="text"
                        placeholder="Verification Code"
                    >
                        <template #prefix>
                            <LockOutlined style="color: rgba(0, 0, 0, 0.25)" />
                        </template>
                    </a-input>
                    <a-button
                        type="primary"
                        style="margin-left: 20px;"
                        @click="sendCode"
                        :disabled="sendCodeDisabled"
                    >{{ sendCodeText }}</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button
                        block
                        type="primary"
                        html-type="submit"
                        :loading="loginLoading"
                        :disabled="loginState.email === '' || loginState.code === ''"
                    >Log In</a-button>
                </a-form-item>
            </a-form>
        </div>
        <a-divider type="vertical" style="height: 500px;" />
        <img src="/kmm-hero.png" alt="hero" />
    </div>
</template>

<style scoped lang="scss">
.flex-wrapper {
    width: 1000px;
    margin: auto;
    align-items: center;
    min-height: 100vh;
    display: flex;
    justify-content: space-between;
    user-select: none;

    h1,
    h5 {
        margin-bottom: 15px;
    }
}

.form {
    width: 400px;
    margin-top: 50px;

    .ant-input-affix-wrapper {
        border-radius: 6px;
    }
}

img {
    width: 30%;
}
</style>

<style lang="scss">
.login-form-global-style {
    .ant-input {
        font-weight: 500;
        height: 35px;
    }

    .vc-form-item {
        .ant-form-item-control-input-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    }
}
</style>