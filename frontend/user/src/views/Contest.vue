<script setup>
import { ref, reactive } from "vue";
import { useCommonStore, useUserStore } from "../plugins/pinia";
import HttpService from "../utils/axios-service";
import { goHome } from "../utils/router-helper";
import KotlinEditor from "../components/KotlinEditor.vue";
import QuestionCard from "../components/QuestionCard.vue";

const commonStore = useCommonStore();
const userStore = useUserStore();

/**
 * 0. 加载中
 * 1. 未登陆
 * 2. 数据完成
 * -1. 失败
 */
const setup = ref(0);
const errMes = ref("Error");
const data = ref(reactive({}));

const current = ref(0);

commonStore.showHeader = true;

if (userStore.token == null) {
  setup.value = 1;
} else if (userStore.profile == null) {
  userStore.$subscribe((mutations, state) => {
    if (state.profile != null) {
      const uid = state.profile.id;
      initData(uid);
    }
  });
} else {
  initData(userStore.profile.id);
}

function initData(uid) {
  HttpService.get("/business/query/contest?userId=" + uid).then((body) => {
    if (body.status) {
      setup.value = 2;
      commonStore.showHeader = false;
      data.value = reactive(body.data);
    } else {
      setup.value = -1;
      errMes.value = body.message;
    }
  });
}
</script>
<template>
  <a-layout class="layout-contest" id="layout-contest">
    <a-layout-header v-if="setup == 2">
      <a-pagination v-model:current="current" simple :total="data.questions.lenght" />
    </a-layout-header>

    <a-layout-content style="background-color: none">
      <div v-if="setup == 0" style="text-align: center; margin-top: 50px">
        <a-spin />
      </div>
      <div v-if="setup == 1" style="text-align: center; margin-top: 50px">
        <a-result status="403" title="未登陆" sub-title="请先进行登陆"> </a-result>
      </div>
      <div v-if="setup == -1" style="text-align: center; margin-top: 50px">
        <a-result status="error" title="发生错误" :sub-title="errMes">
          <template #extra>
            <a-button key="console" type="primary" @click="goHome">返回主页</a-button>
          </template>
        </a-result>
      </div>
      <div v-if="setup == 2" class="contest-space">
        <QuestionCard class="question" :question="data.questions[current].question" />
        <KotlinEditor class="editor" />
      </div>
    </a-layout-content>
  </a-layout>
</template>

<style lang="scss" scoped>
.contest-space {
  height: calc(100vh - 104px);
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 20px 0;

  .question {
    width: 45%;
    height: 100%;
  }

  .editor {
    height: 100%;
    width: 50%;
  }
}
</style>
