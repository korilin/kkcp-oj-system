<script setup>
function getQueryVariables() {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    var params = {};
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        params[pair[0]] = pair[1];
    }
    return params;
}
</script>

<template>
    <a-result status="500" title="500" sub-title="Sorry, the server is wrong.">
        <template #extra>
            <a-button type="primary" @click="window.location.href='/'">Back Home</a-button>
        </template>
        <div class="desc">
            <div>{{ getQueryVariables() }}</div>
        </div>
    </a-result>
</template>