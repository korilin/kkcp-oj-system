import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Contest from "../views/Contest.vue"
import Error from "../views/Error.vue";
import Record from "../views/Record.vue";

const routes = [
  { path: "/", component: Home },
  { path: "/error", component: Error },
  { path: "/contest", component: Contest },
  { path: "/record/:contestId", name: "record",  component: Record}
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
