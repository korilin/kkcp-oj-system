<script setup>
import { message, Modal } from "ant-design-vue";
import { ref } from "vue";
import { useRoute } from "vue-router";
import { useContestStore, useCommonStore, useQuestionStore } from "../plugins/pinia";
import Apis from "../utils/apis";
import QuesionInclusionList from "../components/QuesionInclusionList.vue";
import dayjs from "dayjs";
import { goContests } from "../utils/router-helper";
import { getDurationTime } from "../utils/utils";
import ParticipantsList from "../components/ParticipantsList.vue";

const route = useRoute();
const contestStore = useContestStore();
const commonStore = useCommonStore();
const questionStore = useQuestionStore();

const contestId = route.params.contestId;

const contestInfo = ref({});
const questions = ref([]);
const readMode = ref(true);

let backup = {};

function getDataFromStore() {
  const contest = contestStore.getContestById(contestId);
  contestInfo.value = contest.contest;
  questions.value = contest.questions;
  backup = JSON.stringify(contestInfo.value ?? {});
}

contestStore.ensureInit().then(() => {
  getDataFromStore();
});

questionStore.ensureInit();

const editLoading = ref(false);

function doEdit() {
  if (readMode.value) {
    backup = JSON.stringify(contestInfo.value ?? {});
    readMode.value = false;
  } else {
    editLoading.value = true;
    const contests = contestInfo.value;
    const form = {
      title: contests.title,
      type: contests.type,
      description: contests.description,
      duration: contests.duration,
      startTime: contests.startTime.format("YYYY-MM-DD HH:mm:ss"),
    };
    Apis.ContestModule.updateContestInfo(contestId, form)
      .then((body) => {
        if (body.status) {
          backup = JSON.stringify(contestInfo.value ?? {});
          const contest = contestStore.getContestById(contestId);
          contest.contest = contestInfo.value;
          message.success("~ updated ~");
          readMode.value = true;
        }
      })
      .finally(() => {
        editLoading.value = false;
      });
  }
}

function doCancel() {
  readMode.value = true;
  const temp = JSON.parse(backup);
  temp.startTime = dayjs(temp.startTime);
  temp.status = contestInfo.value.status;
  contestInfo.value = temp;
}

const colors = ["pink", "purple", "blue", "geekblue", "green"];

const durationMark = {
  60: "1h",
  120: "2h",
  180: "3h",
  240: "4h",
  300: "5h",
};

function updateQuestions(data) {
  const contest = contestStore.getContestById(contestId);
  contest.questions = data;
  questions.value = contest.questions;
}

function addQuestions(value) {
  Apis.ContestModule.addInclusion(contestId, value).then((body) => {
    if (body.status) {
      message.info(body.message);
      updateQuestions(body.data);
    }
  });
}

function removeQuestion(questionId) {
  Apis.ContestModule.removeInclusion(contestId, questionId).then((body) => {
    if (body.status) {
      message.info("~remove success~");
      updateQuestions(body.data);
    }
  });
}

function updateSort(questionId, offset) {
  Apis.ContestModule.updateInclusionSort(contestId, questionId, offset).then((body) => {
    if (body.status) {
      updateQuestions(body.data);
    }
  });
}

function deleteContest() {
  Modal.confirm({
    title: "Do you want to delete this contest?",
    content: "User registration and race data will be erased",
    onOk() {
      return Apis.ContestModule.deleteContest(contestId).then((body) => {
        if (body.status) {
          message.success("~ delete success ~");
          contestStore.refreshData();
          goContests();
        }
      });
    },
  });
}

function onStatusChange(value) {
  const status = commonStore.getContestStatusById(value);
  Modal.info({
    title: `Status will be update to ${status.text}`,
    content: status.updateDesc,
    onOk() {
      Apis.ContestModule.updateStatus(contestId, value).then((body) => {
        if (body.status) {
          contestInfo.value.status = body.data;
          message.success("Update Success!");
        } else if (body.status == false) {
          contestInfo.value.status = body.data;
        }
      });
    },
  });
}

const registrations = ref([]);

function getContestRegistrations() {
  Apis.ContestModule.getRegistrations(contestId).then((body) => {
    if (body.status) {
      registrations.value = body.data;
    }
  });
}

getContestRegistrations();
</script>
<template>
  <div v-if="!contestStore.init" style="text-align: center">
    <a-spin />
  </div>
  <a-descriptions
    v-if="contestStore.init"
    :title="'Contest ID: ' + contestId"
    class="global-contest-style"
  >
    <template #extra>
      <a-button type="primary" @click="doEdit" :loading="editLoading">{{
        readMode ? "Edit" : "Save"
      }}</a-button>
      <a-button
        v-if="readMode"
        danger
        type="primary"
        style="margin-left: 20px"
        @click="deleteContest"
        >Delete</a-button
      >
      <a-button v-else @click="doCancel" style="margin-left: 20px">Cancel</a-button>
    </template>
    <a-descriptions-item label="Status">
      <a-select
        ref="statusSelect"
        v-model:value="contestInfo.status"
        :bordered="false"
        @change="onStatusChange"
      >
        <a-select-option v-for="status in commonStore.contestStatuses" :value="status.id">
          <a-badge :color="colors[status.id]" :text="status.text" />
        </a-select-option>
      </a-select>
    </a-descriptions-item>
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
        <a-select-option v-for="cType in commonStore.contestTypes" :value="cType.id">{{
          cType.text
        }}</a-select-option>
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
          <a-tag color="cyan">{{ getDurationTime(contestInfo.duration) }}</a-tag>
        </a-col>
      </a-row>
    </a-descriptions-item>
    <a-descriptions-item label="Description" :span="3">
      <a-textarea
        style="margin: 10px; padding: 20px"
        v-model:value="contestInfo.description"
        :rows="8"
        :disabled="readMode"
      />
    </a-descriptions-item>
  </a-descriptions>
  <QuesionInclusionList
    v-if="contestStore.init"
    style="margin-top: 25px"
    :questions="questions"
    :addQuestions="addQuestions"
    :removeQuestion="removeQuestion"
    :updateSort="updateSort"
  />

  <a-divider>Registration (Sort By Rank)</a-divider>
  <ParticipantsList :users="registrations" />
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
  .ant-select-disabled.ant-select:not(.ant-select-customize-input) .ant-select-selector {
    color: #8c8c8c !important;
  }
  textarea {
    background-color: #f5f5f5 !important;
  }
}
</style>
