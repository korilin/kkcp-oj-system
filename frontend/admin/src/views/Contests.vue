<script setup>
import { ref } from "vue";
import { useContestStore } from "../plugins/pinia"
import Apis from "../utils/apis";
import { goContestItem, goNewContest } from "../utils/router-helper";

const contestStore = useContestStore();

const loading = ref(false)

if (!contestStore.init) {
  loading.value = true
  Apis.ContestModule.queryAllContest().then(body => {
    if (body.status) {
      contestStore.init = true
      contestStore.data = body.data
    }
  }).finally(() => {
    loading.value = false
  })
}

const columns = [
  {
    title: "标题",
    dataIndex: ["contest", "title"],
    key: "title"
  },
  {
    title: "开始时间",
    dataIndex: ["contest", "startTime"],
    key: "startTime"
  },
  {
    title: "题数",
    key: "questionCount",
  },
  {
    title: "时长",
    dataIndex: ["contest", "duration"],
    key: "duration"
  },
  {
    title: "状态",
    dataIndex: ["contest", "status"],
    key: "status",
  },
  {
    title: "ACTION",
    key: "action",
  },
]

</script>
<template>
  <div style="text-align: right; margin-bottom: 20px; padding-right: 50px;">
    <a-button type="primary" @click="goNewContest">New Contest</a-button>
  </div>
  <a-table
    :columns="columns"
    :data-source="contestStore.data"
    rowKey="contestId"
    :loading="loading"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key == 'questionCount'">{{ record.questions.length }}</template>
      <template v-if="column.key == 'status'">
        <a-tag v-if="record.contest.status == 0" color="purple">进行中</a-tag>
        <a-tag v-else color="green">已完成</a-tag>
      </template>
      <template v-if="column.key == 'action'">
        <a-button
          type="link"
          size="small"
          @click="goContestItem(record.contest.contestId)"
        >more&edit</a-button>
        <a-divider type="vertical" />
        <a-button type="link" size="small" style="color: #ff7875;">del</a-button>
      </template>
    </template>
  </a-table>
</template>
