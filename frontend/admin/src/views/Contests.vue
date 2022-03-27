<script setup>
import { useContestsStore } from "../plugins/pinia"

const contestsStore = useContestsStore();

const columns = [
  {
    title: "标题",
    dataIndex: "title",
    key: "title"
  },
  {
    title: "开始时间",
    dataIndex: "startTime",
    key: "startTime"
  },
  {
    title: "题数",
    key: "questionCount",
  },
  {
    title: "时长",
    dataIndex: "duration",
    key: "duration"
  },
  {
    title: "状态",
    dataIndex: "status",
    key: "status",
  },
  {
    title: "ACTION",
    key: "action",
  },
]
</script>
<template>
  <a-table :columns="columns" :data-source="contestsStore.data" rowKey="contestId">
    <template #bodyCell="{ column, record }">
      <template v-if="column.key == 'questionCount'">{{ record.questionCount }}</template>
      <template v-if="column.key == 'status'">
        <a-tag v-if="record.status == 0" color="purple">进行中</a-tag>
        <a-tag v-else color="green">已完成</a-tag>
      </template>
      <template v-if="column.key == 'action'">
        <a-button type="link" size="small">more&edit</a-button>
        <a-divider type="vertical" />
        <a-button type="link" size="small" style="color: #ff7875;">del</a-button>
      </template>
    </template>
  </a-table>
</template>
