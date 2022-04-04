<script setup>import { ref } from 'vue';
import { useCommonStore, useQuestionsStore } from '../plugins/pinia';


const props = defineProps({
  questions: Array,
  addQuestions: Function,
  removeQuestion: Function
})

const commonStore = useCommonStore()
const questionsStore = useQuestionsStore()

const levelsColor = [undefined, "green", "orange", "red"];
const opentAddQuestionModal = ref(false)
const addQuestionLoading = ref(false)
const selectedQuestions = ref([])
const onSelectChange = selectedRowKeys => {
  selectedQuestions.value = selectedRowKeys;
};

function checkQuestion(questionId) {
  for (let index in props.questions) {
    const question = props.questions[index]
    if (questionId == question.questionId) {
      return true
    }
  }
  return false
}

async function modalOk() {
  addQuestionLoading.value = true
  await props.addQuestions(selectedQuestions.value)
  addQuestionLoading.value = false
  opentAddQuestionModal.value = false
  selectedQuestions.value = []
}

const getCheckboxProps = record => ({
  disabled: checkQuestion(record.questionId)
});

const questionColumns = [
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
  }]
</script>
<template>
  <a-list :data-source="questions">
    <template #renderItem="{ item }">
      <a-list-item style="display: flex;justify-content: flex-start; padding: 10px 50px;">
        <div style="width: 40%;">{{ item.title }}</div>
        <a-divider type="vertical" />
        <div style="width: 20%; text-align: center;">
          <a-tag>{{ commonStore.getQuestionTypeById(item.type).text }}</a-tag>
        </div>
        <a-divider type="vertical" />
        <div style="width: 20%; text-align: center;">
          <a-tag
            :color="levelsColor[item.level]"
          >{{ commonStore.getQuestionLevelById(item.level).text }}</a-tag>
        </div>
        <div style="width: 20%; text-align: center;">
          <a-button type="link" danger size="small" @click="removeQuestion(item.questionId)">Remove</a-button>
        </div>
      </a-list-item>
    </template>
    <template #header>
      <div style="display: flex; justify-content: space-between;">
        <div style="line-height: 34px; font-weight: bold;">Included Questions</div>
        <a-button size="small" type="link" @click="opentAddQuestionModal = true">Add Question</a-button>
      </div>

      <a-modal
        v-model:visible="opentAddQuestionModal"
        width="800px"
        title="Select Question"
        okText="Add To This Contest"
        :confirm-loading="addQuestionLoading"
        @ok="modalOk"
      >
        <a-table
          :row-selection="{ selectedRowKeys: selectedQuestions, onChange: onSelectChange, getCheckboxProps: getCheckboxProps }"
          rowKey="questionId"
          :loading="!questionsStore.init"
          :columns="questionColumns"
          :data-source="questionsStore.data"
        />
      </a-modal>
    </template>
    <template #footer>
      <div class="text-primary">Number of questions included: {{ questions.length }}</div>
    </template>
  </a-list>
</template>
