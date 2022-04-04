<script setup>
import { useCommonStore, useQuestionsStore } from "../plugins/pinia"
import { goNewQuestion, goQuestionItem } from "../utils/router-helper";

const questionsStore = useQuestionsStore();
const commonStore = useCommonStore();

questionsStore.ensureInit();

const columns = [
  {
    title: "Title",
    dataIndex: "title",
    key: "title"
  },
  {
    title: "Type",
    dataIndex: "type",
    key: "type"
  },
  {
    title: "Level",
    key: "level",
  },
  {
    title: "Action",
    key: "action",
  },
];

const levelsColor = [undefined, "green", "orange", "red"];

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
    :loading="!questionsStore.init"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key == 'type'">
        <a-tag color="blue">{{ commonStore.getQuestionTypeById([record.type]).text }}</a-tag>
      </template>
      <template v-else-if="column.key == 'level'">
        <a-tag
          :color="levelsColor[record.level]"
        >{{ commonStore.getQuestionLevelById([record.level]).text }}</a-tag>
      </template>
      <template v-else-if="column.key == 'action'">
        <a-button
          type="link"
          size="small"
          @click="goQuestionItem(record.questionId)"
          style="padding: 0;"
        >More & Edit</a-button>
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
