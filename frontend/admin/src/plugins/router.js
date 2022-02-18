import { createRouter, createWebHashHistory } from "vue-router";
import Profile from "../views/Profile.vue";
import Error from "../views/Error.vue";

const routes = [
    { path: "/", component: Profile, name: "profile" },
    { path: "/error", component: Error },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

export default router;
