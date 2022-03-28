<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useCommonStore, useQuestionsStore } from "../plugins/pinia"
import Apis from "../utils/apis";
import { goNewQuestion, goQuestionItem } from "../utils/router-helper";

const router = useRouter();
const questionsStore = useQuestionsStore();
const commonStore = useCommonStore();

const columns = [
  {
    title: "标题",
    dataIndex: "title",
    key: "title"
  },
  {
    title: "题目类型",
    dataIndex: "type",
    key: "type"
  },
  {
    title: "难度",
    key: "level",
  },
  {
    title: "ACTION",
    key: "action",
  },
];

const levelsColor = [undefined, "green", "orange", "red"];

const loading = ref(false);

function initQuestionsData() {
  loading.value = true;
  Apis.QuestionModule.queryQuestions().then((body) => {
    if (body.status) {
      questionsStore.data = body.data;
      loading.value = false;
    }
  })
}

if (!questionsStore.init) {
  initQuestionsData();
  questionsStore.init = true;
}
</script>
<template>
  <div class="bar">
    <h3 class="text-dark">Question Pool</h3>
    <a-button type="primary" @click="goNewQuestion">Add</a-button>
  </div>
  <a-table
    :columns="columns"
    :data-source="questionsStore.data"
    rowKey="questionId"
    :loading="loading"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key == 'type'">
        <a-tag color="blue">{{ commonStore.getTypeById([record.type]).text }}</a-tag>
      </template>
      <template v-else-if="column.key == 'level'">
        <a-tag
          :color="levelsColor[record.level]"
        >{{ commonStore.getLevelById([record.level]).text }}</a-tag>
      </template>
      <template v-else-if="column.key == 'action'">
        <a-button type="link" size="small" @click="goQuestionItem(record.questionId)">More&Edit</a-button>
        <a-divider type="vertical" />
        <a-button type="link" size="small" style="color: #ff7875;">Del</a-button>
      </template>
    </template>
  </a-table>
</template>

<style scoped lang="scss">
.bar {
  display: flex;
  justify-content: space-between;
}
</style>
