<script setup>
import { useRoute } from "vue-router";
import { ref } from "vue";
import HttpService from "../utils/axios-service";
import { goHome } from "../utils/router-helper";

const route = useRoute();

const contestId = route.params.contestId;

const data = ref({});
const setup = ref(0); // -1 0 1

function initData() {
  HttpService.get(`/bisiness/query/contest/record/detail?contestId=${contestId}`).then(
    (body) => {
      if (body.status) {
        data.value = body.data;
        setup.value = -1;
      } else {
        setup.value = -1;
      }
    }
  );
}

initData()
</script>

<template>
  <div
    v-if="setup != 1"
    style="
      display: flex;
      align-items: center;
      justify-content: center;
      height: 800px;
    "
  >
    <a-spin size="large" v-if="setup == 0" />
    <a-result
      v-if="setup == -1"
      status="404"
      title="404"
      sub-title="Sorry, the page you visited does not exist."
    >
      <template #extra>
        <a-button type="primary" @click="goHome">Back Home</a-button>
      </template>
    </a-result>
  </div>
  <div v-if="setup == 1"></div>
  <div></div>
</template>
