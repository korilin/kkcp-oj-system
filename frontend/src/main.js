import { createApp } from "vue";
import App from "./App.vue";

import Antd from "ant-design-vue";
import "ant-design-vue/dist/antd.css";
// 覆盖 antd 样式
import "./muse-ant-vue/app.scss";

const app = createApp(App);

app.use(Antd);

app.mount("#app");
