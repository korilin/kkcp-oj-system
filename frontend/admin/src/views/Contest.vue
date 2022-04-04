<script setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import { useContestStore, useCommonStore } from '../plugins/pinia';

const route = useRoute()
const contestStore = useContestStore()
const commonStore = useCommonStore()

const contestId = route.params.contestId

const contestInfo = ref({})
const questions = ref([])
const readMode = ref(true)

function getDataFromStore() {
  const contest = contestStore.getContestById(contestId)
  contestInfo.value = contest.contest
  questions.value = contest.questions
}

if (!contestStore.init) {
  contestStore.initData().then(() => {
    getDataFromStore()
  })
} else {
  getDataFromStore()
}

function doEdit() {
  if (readMode.value) {
    readMode.value = false
  } else {
    readMode.value = true
    // TODO 提交修改
  }
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
</script>
<template>
  <div v-if="!contestStore.init" style="text-align: center;">
    <a-spin />
  </div>
  <a-descriptions v-else :title="'Contest ID: ' + contestId" class="global-contest-style">
    <template #extra>
      <a-button type="primary" @click="doEdit">Edit</a-button>
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
    <a-descriptions-item label="Start Time" :span="3"></a-descriptions-item>
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
  <a-divider></a-divider>
  <a-list bordered :data-source="data">
    <template #renderItem="{ item }">
      <a-list-item>{{ item }}</a-list-item>
    </template>
    <template #header>
      <div style="display: flex; justify-content: space-between;">
        <div style="line-height: 34px; font-weight: bold;">Included Questions</div>
        <a-button size="small" type="primary">Add Question</a-button>
      </div>
    </template>
    <template #footer>
      <div>Number of questions included: {{ questions.length }}</div>
    </template>
  </a-list>
</template>

<style lang="scss">
.global-contest-style {
  .ant-input {
    border: 0;
  }

  .ant-input[disabled] {
    color: #8c8c8c !important;
    border-color: #d9d9d9;
    background-color: none !important;
    box-shadow: none;
    cursor: not-allowed;
    opacity: 1;
  }

  .ant-badge-status-text {
    color: #8c8c8c !important;
  }

  .ant-descriptions-item-content {
    width: 100%;
    display: inline-block;
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
