import axios from "axios";
import { message } from "ant-design-vue";

const instance = axios.create({
  baseURL: "/api",
  timeout: 5000,
});

instance.interceptors.request.use((config) => {
  config.headers["Admin-Token"] = getToken();
  return config;
});

instance.interceptors.response.use(
  (response) => {
    const body = response.data;
    if (!body.status) {
      message.error(body.message);
    }
    return body;
  },
  (error) => {
    console.error(error);
    if (error.request.status == 401) {
      message.error("Unauthorized Or Permission Denied");
    } else {
      message.error("(＃°Д°)~网络出小差啦");
    }
    return { status: false }
  }
);

function getToken() {
  var token = window.sessionStorage.getItem(
    import.meta.env.VITE_ADMIN_TOKEN_KEY
  );
  if (token == null || token == undefined) {
    token = "No Login";
  }
  return token;
}

const HttpService = instance;

export default HttpService;
