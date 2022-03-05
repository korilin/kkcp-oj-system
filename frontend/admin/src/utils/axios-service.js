import axios from "axios";
import { message } from "ant-design-vue";

const instance = axios.create({
    baseURL: "/api",
    timeout: 1000,
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
        message.error("(＃°Д°)~网络出小差啦");
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
