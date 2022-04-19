import axios from "axios";
import { message } from "ant-design-vue";

const instance = axios.create({
  baseURL: "/api",
  timeout: 30000,
});

instance.interceptors.request.use((config) => {
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


const HttpService = instance;

export default HttpService;
