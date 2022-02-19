import { createRouter, createWebHashHistory } from "vue-router";
import Profile from "../views/Profile.vue";
import Error from "../views/Error.vue";
import Contests from "../views/Contests.vue";
import Questions from "../views/Questions.vue";
import Question from "../views/Question.vue";
import Login from "../Login.vue";

const routes = [
    { path: "/login", component: Login, name: "login" },
    {
        path: "/",
        redirect: (to) => {
            return { path: "/profile" };
        },
    },
    { path: "/profile", component: Profile, name: "profile" },
    { path: "/dashboard", component: Profile, name: "dashboard" },
    { path: "/contests", component: Contests, name: "contests" },
    { path: "/questions", component: Questions, name: "questions" },
    { path: "/question/:questionId", component: Question, name: "question" },
    { path: "/error", component: Error },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

export default router;
