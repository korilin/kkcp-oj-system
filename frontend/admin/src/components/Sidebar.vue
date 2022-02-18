<script setup>
import { ref } from 'vue';
import { HomeFilled } from "@ant-design/icons-vue";
const selectedKeys = ref(["profile"]);

const menus = [
    {
        key: "profile",
        icon: HomeFilled,
        text: "Profile",
        name: "profile"
    },
    {
        key: "dashboard",
        icon: HomeFilled,
        text: "Dashboard",
        name: "dashboard"
    },
    {
        key: "contests",
        icon: HomeFilled,
        text: "Contests",
        name: "contests"
    },
    {
        key: "questions",
        icon: HomeFilled,
        text: "Question Pool",
        name: "questions"
    },
]
</script>
<template>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        :collapsedWidth="0"
        class="text-gray-12"
    >
        <template v-for="menu in menus" :key="menu.key">
            <div>
                <a-menu-item :key="menu.key">
                    <component v-bind:is="menu.icon" />
                    <span class="text">{{ menu.text }}</span>
                </a-menu-item>
            </div>
        </template>
    </a-menu>
</template>

<style scoped lang="scss">
.ant-menu-item {
    transition: all 0.3s;
    .anticon {
        padding: 8px 10px;
        box-shadow: 0 4px 6px rgb(0 0 0 / 12%);
        border-radius: 6px;
        color: #d9d9d9;
        background-color: white;
        transition: all 0.8s;
    }
    .text {
        user-select: none;
        color: #1f1f1f;
    }
    &:hover {
        .anticon {
            box-shadow: 0 2px 4px rgb(0 0 0 / 12%);
        }
    }
}

.ant-menu-item-selected {
    transition: all 0.1s;
    .anticon {
        color: white;
        background-color: #1890ff;
        transition: all 0.3s;
    }
    .text {
        font-weight: 600;
    }
}
</style>