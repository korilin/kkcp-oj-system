<script setup>
import { ref } from "vue";
import { resolveMarkdownAsHtml } from "../utils/utils";

const prop = defineProps({
  users: Array,
});

const columns = [
  {
    title: "Name",
    dataIndex: "name",
    key: "name",
  },
  {
    title: "Email",
    dataIndex: ["profile", "email"],
    key: "email",
  },
  {
    title: "Time",
    dataIndex: "time",
    key: "time",
  },
  {
    title: "Action",
    key: "action",
  },
];

const toCodeMdHtml = (code, lang) => {
  const prefix = "```" + lang;
  const suffix = "```";
  const md = `${prefix}
${code}
${suffix}`;
  return resolveMarkdownAsHtml(md);
};

const activeKey = ref(0);
const isShowDetail = ref(false);
const detail = ref({ profile: {}, time: "", answers: [] });

function seeDetail(item) {
  console.log(item);
  detail.value = item;
  isShowDetail.value = true;
}
</script>
<template>
  <a-table :dataSource="users" :columns="columns">
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'name'">
        <a :href="record.profile.htmlUrl" target="_blank">
          {{ record.profile.name }}
        </a>
      </template>
      <template v-if="column.key === 'action'">
        <a-button type="link" @click="seeDetail(record)">detail</a-button>
      </template>
    </template>
  </a-table>
  <a-drawer width="640" v-model:visible="isShowDetail" placement="right">
    <a-descriptions :column="{ xxl: 1, xl: 1, lg: 1, md: 1, sm: 1, xs: 1 }">
      <a-descriptions-item label="GitHub">
        <a :href="detail.profile.htmlUrl" target="_blank">
          {{ detail.profile.login }}
        </a>
      </a-descriptions-item>
      <a-descriptions-item label="Nick Name">{{
        detail.profile.name
      }}</a-descriptions-item>
      <a-descriptions-item label="Email">{{ detail.profile.email }}</a-descriptions-item>
      <a-descriptions-item label="Registration Time">{{
        detail.time
      }}</a-descriptions-item>
      <a-descriptions-item label="Bio">{{ detail.profile.bio }}</a-descriptions-item>
    </a-descriptions>
    <a-divider>Answers</a-divider>
    <a-collapse class="desc-md-space markdown-html" v-model:activeKey="activeKey" ghost>
      <a-collapse-panel
        v-for="answer in detail.answers"
        :key="answer.question.questionId"
      >
        <template #header>
          {{ answer.question.title }}
          <a-tag
            style="margin-left: 10px"
            :color="answer.pass == 100 ? 'green' : 'orange'"
          >
            {{ answer.pass }}
          </a-tag>
        </template>
        <div v-html="toCodeMdHtml(answer.answer, 'Kotlin')" v-highlight></div>
      </a-collapse-panel>
    </a-collapse>
  </a-drawer>
</template>
