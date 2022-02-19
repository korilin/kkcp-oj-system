import { createRouter, createWebHashHistory } from "vue-router";
import Profile from "../views/Profile.vue";
import Error from "../views/Error.vue";
import Contests from "../views/Contests.vue";
import Login from "../Login.vue";

const routes = [
    { path: "/login", component: Login, name: "login" },
    {
        path: "/",
        redirect: (to) => {
            // 方法接收目标路由作为参数
            // return 重定向的字符串路径/路径对象
            return { path: "/profile" };
        },
    },
    { path: "/profile", component: Profile, name: "profile" },
    { path: "/dashboard", component: Profile, name: "dashboard" },
    { path: "/contests", component: Contests, name: "contests" },
    { path: "/questions", component: Profile, name: "questions" },
    { path: "/error", component: Error },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

export default router;
