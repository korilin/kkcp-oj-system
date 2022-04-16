<script setup>
import KotlinSVG from "../components/KotlinSVG.vue"
import TrophySVG from "../components/TrophySVG.vue"
import { ref } from 'vue';
import { useContestStore, useUserStore } from "../plugins/pinia";
import { useCommonStore } from "../../../admin/src/plugins/pinia";
import { getDurationTime } from "../utils/utils";
import { LoginOutlined } from "@ant-design/icons-vue"
import { message } from "ant-design-vue";
import HttpService from "../utils/axios-service";
import { goContest } from "../utils/router-helper";

// 定义页面文本 & 默认状态属性
const heroTitle = "Kotlin Knowledge Contest";
const heroSubtitle = "Kotlin 编程知识竞赛";
const noActiveSeminar = "当前没有进行中的活动";
const loading = "loading...";

const contestStore = useContestStore()
const commonStore = useCommonStore()
const userStore = useUserStore()

commonStore.showHeader = true

const columns = [
  { title: "以往竞赛", dataIndex: "title", key: "title" },
  { title: "时长", dataIndex: "duration", key: "duration" },
  { title: "参加人数", dataIndex: "participants", key: "participants" },
];

const records = [];

const toolLinks = [
  { text: "帮助文档", link: "", },
  { text: "问题反馈", link: "", },
  { text: "社区合作", link: "", }
];

const releaseContest = ref(null)

contestStore.ensureInit().then(() => {
  releaseContest.value = contestStore.releaseContest
})

const releaseColor = '#1890FF'
const underWayColor = '#8C8C8C'

function getReleaseColor() {
  if (releaseContest.value.status == 3) {
    return underWayColor
  } else {
    return releaseColor
  }
}

function getReleaseNavButtonText() {
  if (releaseContest.value.status == 1) {
    return "报名比赛"
  } else if (releaseContest.value.status == 2) {
    return "进入比赛"
  } else {
    return "已结束"
  }
}

function applyContest() {
  if (releaseContest.value.status == 1) {
    if (userStore.profile == null) {
      message.info("请先登录")
    } else {
      const data = {
        contestId: releaseContest.value.contestId,
        userId: userStore.profile.id
      }
      HttpService.post("/business/register", data).then(body => {
        if (body.status) {
          if (body.data) {
            message.success("报名成功")
          } else {
            message.info(body.message)
          }
        }
      })
    }
  } else if (releaseContest.value.status == 2) {
    goContest()
  }
}

</script>

<template>
  <div class="home">
    <div class="hero-wrap">
      <div class="svg">
        <KotlinSVG class="kotlin-svg" />
        <TrophySVG class="trophy-svg" />
      </div>
      <div class="text">
        <h3 class="text-gray-2">{{ heroTitle }}</h3>
        <p class="text-gray-6">{{ heroSubtitle }}</p>
      </div>
    </div>
  </div>
  <div class="main-wrap">
    <div class="current-seminar">
      <a-card :bordered="false" class="widget-1">
        <a-empty v-if="releaseContest == null" :description="noActiveSeminar" />
        <div v-else style="padding: 10px; display: flex; align-items: center; justify-content: space-between;">
          <div>
            <h5>
              {{ releaseContest.title }}
              <a-tag v-if="releaseContest.status == 2" color="green">进行中</a-tag>
            </h5>
            <div>
              <a-tag>开始时间：{{ releaseContest.startTime }}</a-tag>
              <a-tag color="blue">时长：{{ getDurationTime(releaseContest.duration) }}</a-tag>
              <a-tag color="purple">{{ commonStore.getContestTypeById(releaseContest.type).text }}</a-tag>
            </div>
            <div style="margin-top: 20px;">{{ releaseContest.description }}</div>
          </div>
          <div style="text-align: center; cursor: pointer; padding: 10px; font-weight: bold;" @click="applyContest">
            <div>
              <LoginOutlined :style="{ fontSize: '35px', color: getReleaseColor() }" />
            </div>
            <div :style="{ marginTop: '10px', color: getReleaseColor(), fontSize: '14px' }">{{
              getReleaseNavButtonText()
            }}
            </div>
          </div>
        </div>
      </a-card>
    </div>
    <div class="records-wrap">
      <div class="seminar-redords card">
        <div class="header">
          <h4 class="text-gray-9">往期竞赛记录</h4>
        </div>
        <div class="records">
          <a-table :columns="columns" :data-source="records"></a-table>
        </div>
      </div>
      <div class="tools-box card">
        <h4 class="text-gray-8">Tool Box</h4>
        <a-list item-layout="horizontal" :data-source="toolLinks">
          <template #renderItem="{ item }">
            <a-list-item>
              <a style="width: 100%;">{{ item.text }}</a>
            </a-list-item>
          </template>
        </a-list>
      </div>
    </div>  </div>
</template>

<style scoped lang="scss">
.hero-wrap {
  height: 500px;
  // background-image: linear-gradient(150deg, #ed485a 0%, #963f8a 60%, #6c49e3 100%);
  background-color: #27282c;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  flex-direction: column;

  .svg {
    position: relative;
    top: -50px;
    right: 10px;

    .kotlin-svg {
      height: 180px;
      width: 180px;
    }

    .trophy-svg {
      position: absolute;
      height: 80px;
      width: 80px;
      transform: rotate(30deg);
      right: 5px;
      bottom: 5px;
    }
  }

  .text {
    text-align: center;
    position: relative;
    top: -30px;

    p {
      font-size: 16px;
      font-weight: 200;
    }
  }
}

.main-wrap {
  max-width: 800px;
  margin: auto;

  .current-seminar {
    position: relative;
    top: -60px;

    .widget-1 {
      min-height: 150px;
    }
  }

  .records-wrap {
    display: flex;
    justify-content: space-between;

    .card {
      box-shadow: 0 0 30px -10px rgb(0 0 0 / 15%);
      border-radius: 12px;
      background-color: white;
    }

    .seminar-redords {
      width: 70%;

      .header {
        padding: 30px 20px;
      }

      .records {
        padding-bottom: 30px;
      }
    }

    .tools-box {
      width: 25%;
      padding: 30px 20px 20px;
      text-align: center;
    }
  }
}
</style>
