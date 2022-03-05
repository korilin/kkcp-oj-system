import axios from "axios";
import { message } from "ant-design-vue";

function getToken() {
    var token = window.sessionStorage.getItem(
        import.meta.env.VITE_ADMIN_TOKEN_KEY
    );
    if (token == null || token == undefined) {
        token = "No Login";
    }
    return token;
}

export function doGet(url, params, callback) {
    axios
        .request({
            method: "get",
            params: params,
            url: "/api" + url,
            headers: {
                "Admin-Token": getToken(),
            },
        })
        .then((response) => {
            const rData = response.data;
            if (rData.status) {
                callback(rData);
            } else {
                message.error(rData);
            }
        })
        .catch((e) => {
            console.error(e);
            message.error("!网络出小差啦!");
        });
}

export function doPost(url, params, data, callback) {
    axios
        .request({
            method: "post",
            params: params,
            data: data,
            url: "/api" + url,
            headers: {
                "Admin-Token": getToken(),
            },
        })
        .then((response) => {
            const rData = response.data;
            if (rData.status) {
                callback(rData);
            } else {
                message.error(rData);
            }
        })
        .catch((e) => {
            console.error(e);
            message.error("!网络出小差啦!");
        });
}
