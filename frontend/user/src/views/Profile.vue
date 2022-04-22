<script setup>
import { useCommonStore, useUserStore } from "../plugins/pinia";
import { GithubFilled } from "@ant-design/icons-vue";
import { goRecord } from "../utils/router-helper";

const userStore = useUserStore();
const commonStore = useCommonStore();

const columns = [
  {
    title: "比赛",
    key: "title",
  },
  {
    title: "类型",
    key: "type",
  },
  {
    title: "排名",
    dataIndex: "rank",
    key: "rank",
  },
];

function openGitHub() {
  window.open(userStore.profile.htmlUrl, "target");
}
</script>

<template>
  <div class="wrap">
    <a-spin size="large" v-if="userStore.profile == null" />
    <template v-else>
      <div class="profile">
        <div style="width: 80px; margin-right: 20px">
          <a-avatar :src="userStore.profile.avatarUrl" class="avatar" shape="circle" />
        </div>
        <GithubFilled class="github-icon" @click="openGitHub" />
        <a-descriptions
          :column="{ xxl: 1, xl: 1, lg: 1, md: 1, sm: 1, xs: 1 }"
          :labelStyle="{ color: 'white' }"
          :contentStyle="{ color: 'white' }"
        >
          <a-descriptions-item label="GitHub">
            <a
              :href="userStore.profile.htmlUrl"
              target="_blank"
              rel="noopener noreferrer"
            >
              {{ userStore.profile.login }}
            </a>
          </a-descriptions-item>
          <a-descriptions-item label="Nick Name">{{
            userStore.profile.name
          }}</a-descriptions-item>
          <a-descriptions-item label="Email">{{
            userStore.profile.email
          }}</a-descriptions-item>
          <a-descriptions-item label="Bio">{{
            userStore.profile.bio
          }}</a-descriptions-item>
        </a-descriptions>
      </div>
      <a-divider />
      <a-table :dataSource="userStore.contests" :columns="columns">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'title'">
            <a-button type="link" @click="goRecord(record.contest.contestId)">
              {{ record.contest.title }}
            </a-button>
          </template>
          <template v-if="column.key === 'type'">
            <a-tag color="purple">{{
              commonStore.getContestTypeById(record.contest.type).text
            }}</a-tag>
          </template>
        </template>
      </a-table>
    </template>
  </div>
</template>
<style lang="scss" scoped>
.wrap {
  width: 800px;
  padding: 40px;
  margin: auto;
  min-height: calc(100vh - 84px);
}

.profile {
  background-color: #22272e;
  padding: 40px 50px 30px 30px;
  border-radius: 10px;
  position: relative;
  display: flex;
}

.avatar {
  padding: 0px !important;
  width: 60px !important;
  height: 60px !important;
}

.github-icon {
  color: white;
  position: absolute;
  bottom: 20px;
  right: 20px;
  width: 20px;
  height: 20px;
  border: 0;
  padding: 0;
}
</style>
