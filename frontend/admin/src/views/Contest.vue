<script setup>
import { message } from 'ant-design-vue';
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import { useContestStore, useCommonStore, useQuestionsStore } from '../plugins/pinia';
import Apis from '../utils/apis';
import QuesionInclusionList from '../components/QuesionInclusionList.vue';

const route = useRoute()
const contestStore = useContestStore()
const commonStore = useCommonStore()
const questionsStore = useQuestionsStore()

const contestId = route.params.contestId

const contestInfo = ref({})
const questions = ref([])
const readMode = ref(true)

let backup = null

function getDataFromStore() {
  const contest = contestStore.getContestById(contestId)
  contestInfo.value = contest.contest
  questions.value = contest.questions
}

contestStore.ensureInit().then(() => {
  getDataFromStore()
})

questionsStore.ensureInit()

function doEdit() {
  if (readMode.value) {
    Object.assign(backup, contestInfo.value)
    readMode.value = false
  } else {
    readMode.value = true
    // TODO 提交修改
  }
}

function doCancel() {
  contestInfo.value = backup
}

const colors = [
  'pink',
  'purple',
  'blue',
  'geekblue',
  'green',
];


const durationMark = {
  60: '1h',
  120: '2h',
  180: '3h',
  240: '4h',
  300: '5h',
}

function getDurationTime() {
  if (contestInfo.duration == null) return "0h 0min"
  let h = parseInt(state.duration / 60)
  let min = contestInfo.duration - h * 60
  return `${h}h ${min}min`
}

function updateQuestions(data) {
  const contest = contestStore.getContestById(contestId)
  contest.questions = data
  questions.value = contest.questions
}

function addQuestions(value) {
  Apis.ContestModule.addInclusion(contestId, value)
    .then(body => {
      if (body.status) {
        message.info(body.message)
        updateQuestions(body.data)
      }
    })
}

function removeQuestion(questionId) {
  Apis.ContestModule.removeInclusion(contestId, questionId).then(body => {
    if (body.status) {
      message.info("~remove success~")
      updateQuestions(body.data)
    }
  })
}
</script>
<template>
  <div v-if="!contestStore.init" style="text-align: center;">
    <a-spin />
  </div>
  <a-descriptions
    v-if="contestStore.init"
    :title="'Contest ID: ' + contestId"
    class="global-contest-style"
  >
    <template #extra>
      <a-button type="primary" @click="doEdit">{{ readMode ? "Edit" : "Save" }}</a-button>
      <a-button v-if="readMode" danger type="primary" style="margin-left: 20px;">Delete</a-button>
      <a-button v-else @click="doEdit" style="margin-left: 20px;">Cancel</a-button>
    </template>
    <a-descriptions-item label="Title">
      <a-input v-model:value="contestInfo.title" :bordered="false" :disabled="readMode" />
    </a-descriptions-item>
    <a-descriptions-item label="Type">
      <a-select
        ref="typeSelect"
        v-model:value="contestInfo.type"
        :disabled="readMode"
        :bordered="false"
      >
        <a-select-option
          v-for="cType in commonStore.contestTypes"
          :value="cType.id"
        >{{ cType.text }}</a-select-option>
      </a-select>
    </a-descriptions-item>
    <a-descriptions-item label="Status">
      <a-select
        ref="statusSelect"
        v-model:value="contestInfo.status"
        :disabled="readMode"
        :bordered="false"
      >
        <a-select-option v-for="status in commonStore.contestStatuses" :value="status.id">
          <a-badge :color="colors[status.id]" :text="status.text" />
        </a-select-option>
      </a-select>
    </a-descriptions-item>
    <a-descriptions-item label="Start Time" :span="3">
      <a-date-picker
        :disabled="readMode"
        :allowClear="false"
        :show-time="{ format: 'HH:mm' }"
        :format="'YYYY/MM/DD HH:mm'"
        v-model:value="contestInfo.startTime"
      />
    </a-descriptions-item>
    <a-descriptions-item label="Duration" :span="3">
      <a-row type="flex" justify="space-around" align="middle">
        <a-col :span="18">
          <a-slider
            v-model:value="contestInfo.duration"
            :marks="durationMark"
            :max="300"
            :disabled="readMode"
          />
        </a-col>
        <a-col :span="2">
          <a-tag color="cyan">{{ getDurationTime() }}</a-tag>
        </a-col>
      </a-row>
    </a-descriptions-item>
    <a-descriptions-item label="Description" :span="3">
      <a-textarea
        style="margin: 10px; padding: 20px;"
        v-model:value="contestInfo.description"
        :rows="8"
        :disabled="readMode"
      />
    </a-descriptions-item>
  </a-descriptions>
  <QuesionInclusionList
    v-if="contestStore.init"
    style="margin-top: 25px;"
    :questions="questions"
    :addQuestions="addQuestions"
    :removeQuestion="removeQuestion"
  />
</template>

<style lang="scss">
.global-contest-style {
  .ant-input,
  .ant-picker {
    border: 0;
  }

  .ant-picker-disabled {
    background: none !important;
  }

  .ant-input[disabled] {
    color: #8c8c8c !important;
    border-color: #d9d9d9;
    background-color: none !important;
    box-shadow: none;
    cursor: not-allowed;
    opacity: 1;
  }

  .ant-picker-input > input[disabled] {
    color: #8c8c8c !important;
  }

  .ant-badge-status-text {
    color: #8c8c8c !important;
  }

  .ant-slider-disabled {
    .ant-slider-track {
      background-color: #91d5ff !important;
    }
    .ant-slider-dot {
      border-color: #f0f0f0 !important;
    }

    .ant-slider-dot-active {
      border-color: #8cc8ff !important;
    }
  }

  .ant-descriptions-item-content {
    width: 100%;
  }
  .ant-select-disabled.ant-select:not(.ant-select-customize-input)
    .ant-select-selector {
    color: #8c8c8c !important;
  }
  textarea {
    background-color: #f5f5f5 !important;
  }
}
</style>
