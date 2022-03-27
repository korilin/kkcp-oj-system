import { createApp } from "vue";
import App from "./App.vue";

import Antd from "ant-design-vue";
import "ant-design-vue/dist/antd.css";
// 覆盖 antd 样式
import "../../muse-ant-vue/app.scss";
import hljs from "highlight.js";
import "highlight.js/styles/github.css";

import router from "./plugins/router";
import { createPinia } from "pinia";

import "./style/markdown-html.scss";

const app = createApp(App);
const pinia = createPinia();

app.directive("highlight", function (el) {
    hljs.configure({ useBR: true, ignoreUnescapedHTML: true });
    let blocks = el.querySelectorAll("pre");
    blocks.forEach((block) => {
        hljs.highlightElement(block);
    });
});

app.use(Antd);
app.use(router);
app.use(pinia);

app.mount("#app");
