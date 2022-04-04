<script setup>
import { useCommonStore, useContestStore } from "../plugins/pinia"
import { goContestItem, goNewContest } from "../utils/router-helper";

const contestStore = useContestStore();
const commonStore = useCommonStore();

contestStore.ensureInit()

const columns = [
  {
    title: "Title",
    dataIndex: ["contest", "title"],
    key: "title"
  },
  {
    title: "Start Time",
    key: "startTime"
  },
  {
    title: "Question Count",
    key: "questionCount",
  },
  {
    title: "Duration",
    dataIndex: ["contest", "duration"],
    key: "duration"
  },
  {
    title: "Status",
    dataIndex: ["contest", "status"],
    key: "status",
  },
  {
    title: "Action",
    key: "action",
  },
]

const colors = [
  'pink',
  'purple',
  'blue',
  'geekblue',
  'green',
];
</script>
<template>
  <div style="text-align: right; margin-bottom: 20px; padding-right: 50px;">
    <a-button type="primary" @click="goNewContest">New Contest</a-button>
  </div>
  <a-table
    :columns="columns"
    :data-source="contestStore.data"
    rowKey="contestId"
    :loading="!contestStore.init"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key == 'questionCount'">{{ record.questions.length }}</template>
      <template v-if="column.key == 'startTime'">
        {{ record.contest.startTime.format('YYYY/MM/DD HH:mm:ss') }}
      </template>
      <template v-if="column.key == 'status'">
        <a-tag
          :color="colors[record.contest.status]"
        >{{ commonStore.getContestStatusById([record.contest.status]).text }}</a-tag>
      </template>
      <template v-if="column.key == 'action'">
        <a-button
          type="link"
          size="small"
          style="padding: 0;"
          @click="goContestItem(record.contest.contestId)"
        >More & Edit</a-button>
      </template>
    </template>
  </a-table>
</template>
