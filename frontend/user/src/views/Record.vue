<script setup>
import { useRoute } from "vue-router";
import { ref } from "vue";
import HttpService from "../utils/axios-service";
import { goHome } from "../utils/router-helper";
import { getDurationTime } from "../utils/utils";
import { useCommonStore } from "../plugins/pinia";
import TrophySVG from "../components/TrophySVG.vue";

const route = useRoute();
const commonStore = useCommonStore();
const contestId = route.params.contestId;

const data = ref({});
const setup = ref(0); // -1 0 1

const showQuestion = ref(0);

function initData() {
  HttpService.get(`/visitor/query/contest/record/detail?contestId=${contestId}`).then(
    (body) => {
      if (body.status) {
        data.value = body.data;
        setup.value = 1;
      } else {
        setup.value = -1;
      }
    }
  );
}

initData();
</script>

<template>
  <div
    v-if="setup != 1"
    style="display: flex; align-items: center; justify-content: center; height: 800px"
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
  <div v-if="setup == 1" class="wrap">
    <a-card :bordered="true" class="widget-1">
      <div>
        <h5>{{ data.contest.title }}</h5>
        <div>
          <a-tag>开始时间：{{ data.contest.startTime }}</a-tag>
          <a-tag color="blue">时长：{{ getDurationTime(data.contest.duration) }}</a-tag>
          <a-tag color="purple">
            {{ commonStore.getContestTypeById(data.contest.type).text }}
          </a-tag>
        </div>
        <div style="margin-top: 20px">{{ data.contest.description }}</div>
      </div>
    </a-card>
    <a-divider style="margin: 40px 0; width: 100%">问题集</a-divider>
    <a-collapse
      v-model:activeKey="showQuestion"
      style="width: 100%; background: white; padding: 20px; border-radius: 10px"
      ghost
    >
      <a-collapse-panel
        v-for="item in data.questions"
        :key="item.questionId"
        :header="item.title"
      >
        <div class="markdown-html" v-html="item.description" v-highlight></div>
      </a-collapse-panel>
    </a-collapse>
    <a-divider style="margin: 40px 0; width: 100%">
      <TrophySVG style="width: 30px; height: 30px" />
      <div>前10名</div>
    </a-divider>
    <a-list
      item-layout="horizontal"
      :data-source="data.rank"
      style="background-color: white; padding: 20px; border-radius: 10px"
    >
      <template #renderItem="{ item, index }">
        <a-list-item>
          <a-list-item-meta :description="item.user.bio">
            <template #title>
              <a :href="item.user.htmlUrl" target="_blank">{{ item.user.name }}</a>
            </template>
            <template #avatar>
              <a-avatar :src="item.user.avatarUrl" />
            </template>
          </a-list-item-meta>
        </a-list-item>
      </template>
    </a-list>
  </div>
</template>

<style lang="scss">
.wrap {
  width: 800px;
  padding: 40px;
  margin: auto;
  min-height: calc(100vh - 84px);
}

.widget-1 {
  width: 100%;
  padding: 20px;
  min-height: 150px;
}
</style>
