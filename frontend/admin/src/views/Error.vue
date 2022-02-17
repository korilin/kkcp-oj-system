<script setup>
import { goHome } from "../utils/router-helper";

function getErrorInfo() {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == "info") {
            return pair[1];
        }
    }
    return "发生了未知的错误";
}
</script>

<template>
    <a-result status="error" title="发送错误" :sub-title="getErrorInfo()">
        <template #extra>
            <a-button type="primary" @click="goHome">Back Home</a-button>
        </template>
    </a-result>
</template>