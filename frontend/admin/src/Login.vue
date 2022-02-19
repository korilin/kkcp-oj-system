<script setup>
import { MailOutlined, LockOutlined } from '@ant-design/icons-vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAccountStore } from './plugins/pinia';

const accountStore = useAccountStore();
const router = useRouter();

const loginState = ref({
    email: "",
    code: "",
})

function checkToken() {
    const token = window.sessionStorage.getItem('admin_login_token');
    if (token != null) {
        router.push({ name: "profile" })
    }
}

function saveIntoStore(token, account) {
    accountStore.loginToken = token;
    accountStore.account = account;
    window.sessionStorage.setItem('admin_login_token', token);
    window.sessionStorage.setItem('admin_account_info', account);
}

function tryLogin() {
    // mock data
    const loginToken = "xxxxx";
    const account = {
        email: loginState.email,
        name: "mock_kori",
        level: 5
    };
    saveIntoStore(loginToken, account);
    router.push({ name: "profile" });
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
                    <a-button type="primary" style="margin-left: 20px;">Send Code</a-button>
                </a-form-item>
                <a-form-item>
                    <a-button
                        block
                        type="primary"
                        html-type="submit"
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