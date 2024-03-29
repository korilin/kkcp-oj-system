<script setup>
import { ref, reactive, watch, h } from "vue";
import { useCommonStore, useUserStore } from "../plugins/pinia";
import HttpService from "../utils/axios-service";
import { goHome } from "../utils/router-helper";
import KotlinEditor from "../components/KotlinEditor.vue";
import QuestionCard from "../components/QuestionCard.vue";
import { message, Modal } from "ant-design-vue";

const commonStore = useCommonStore();
const userStore = useUserStore();

/**
 * 0. 加载中
 * 1. 未登陆
 * 2. 数据完成
 * -1. 失败
 */
const setup = ref(0);
const errMes = ref("Error");
const data = ref({});

const endTime = ref(Date.now());

const current = ref(0);
const question = ref({});
const answer = ref("");
const submits = ref([]);

const editor = ref(null);
const card = ref(null);

function getIndex() {
  return current.value - 1;
}

function startTimeCountDown() {
  const start = new Date(data.value.contest.startTime).getTime();
  const end = start + data.value.contest.duration * 60 * 1000;
  endTime.value = new Date(end);
}

function endTimeCountDown() {
  Modal.info({
    title: "时间到啦～",
    content: h("div", {}, [
      h("p", "比赛已结束，答题终止，感谢你的参与，"),
      h("p", "比赛结果将会在审核后展示在主页，点击按钮返回主页"),
    ]),
    onOk() {
      goHome();
    },
  });
}

watch(current, (newV, oldV) => {
  if (newV == data.value.questions.length + 1) {
    setup.value = 3;
    return newV;
  } else if (setup.value > 2) {
    setup.value = 2;
  }
  const v = data.value.questions[newV - 1];
  question.value = v.question;
  answer.value = v.answer;
  editor.value?.updateValue(v.answer);
  card.value?.resetIndex();
  submits.value = [];
  getSubmits();
  return newV;
});

watch(answer, (newV, oldV) => {
  data.value.questions[getIndex()].answer = newV;
  return newV;
});

commonStore.showHeader = true;

if (userStore.token == null) {
  setup.value = 1;
} else if (userStore.profile == null) {
  userStore.$subscribe((mutations, state) => {
    if (state.profile != null) {
      const uid = state.profile.id;
      initData(uid);
    }
  });
} else {
  initData(userStore.profile.id);
}

function initData(uid) {
  HttpService.get("/business/query/contest?userId=" + uid).then((body) => {
    if (body.status) {
      commonStore.showHeader = false;
      data.value = body.data;
      startTimeCountDown();
      current.value = 1;
      setup.value = 2;
    } else {
      setup.value = -1;
      errMes.value = body.message;
    }
  });
}

function saveAnswerRequest() {
  const url = "/business/answer/update";
  const answers = [];
  data.value.questions.forEach((element) => {
    const item = {
      questionId: element.question.questionId,
      answer: element.answer,
    };
    answers.push(item);
  });
  const params = {
    userId: userStore.profile.id,
    answers: answers,
  };
  return HttpService.put(url, params);
}

function saveAnswer() {
  saveAnswerRequest().then((body) => {
    if (body.status) {
      message.success("保存成功");
    } else {
      message.error("保存失败，请重试");
    }
  });
}

function resetAnswer() {
  const code = data.value.questions[getIndex()].question.codeTemplate;
  console.log(code);
  answer.value = code;
  editor.value?.updateValue(code);
}

async function postAnswer(url) {
  const params = {
    userId: userStore.profile.id,
    answers: [{ questionId: question.value.questionId, answer: answer.value }],
  };
  card.value?.updateResult(0, "运行中");
  return HttpService.post(url, params).catch((_) => {
    card.value?.updateResult(2, "Service Error Happen!");
  });
}

async function testAnswer() {
  postAnswer("/business/answer/test").then((body) => {
    if (body.status && body.data) {
      card.value?.updateResult(1, body.message);
    } else {
      card.value?.updateResult(2, body.message);
    }
  });
}

async function submitAnswer() {
  const job1 = postAnswer("/business/answer/submit");
  const body = await job1;
  await getSubmits();
  if (body.status && body.data) {
    card.value?.updateResult(1, body.message);
  } else {
    card.value?.updateResult(2, body.message);
  }
}

async function getSubmits() {
  const qid = question.value.questionId;
  const uid = userStore.profile.id;
  const url = `/business/query/submits?questionId=${qid}&userId=${uid}`;
  return HttpService.get(url).then((body) => {
    if (body.status) {
      submits.value = body.data;
    }
  });
}
</script>
<template>
  <a-layout class="layout-contest" id="layout-contest">
    <a-layout-header v-if="setup >= 2" class="contest-header">
      <h5>{{ data.contest?.title }}</h5>
      <a-statistic-countdown
        :value="endTime"
        style="margin-right: 50px"
        @finish="endTimeCountDown"
      />
      <a-pagination
        v-model:current="current"
        simple
        :total="data.questions.length + 1"
        :pageSize="1"
      />
    </a-layout-header>

    <a-layout-content style="background-color: none">
      <div
        v-if="setup == 0"
        style="display: flex; align-items: center; justify-content: center; height: 500px"
      >
        <a-spin />
      </div>
      <div v-if="setup == 1" style="text-align: center; margin-top: 50px">
        <a-result status="403" title="未登陆" sub-title="请先进行登陆"> </a-result>
      </div>
      <div v-if="setup == -1" style="text-align: center; margin-top: 50px">
        <a-result status="error" title="发生错误" :sub-title="errMes">
          <template #extra>
            <a-button key="console" type="primary" @click="goHome">返回主页</a-button>
          </template>
        </a-result>
      </div>
      <div v-if="setup == 2" class="contest-space">
        <QuestionCard
          class="question"
          ref="card"
          :question="question"
          :submits="submits"
        />
        <div class="editor-wrap">
          <KotlinEditor class="editor" ref="editor" v-model="answer" />
          <div class="opt-btn">
            <a-button size="smaill" @click="saveAnswer">保存</a-button>
            <a-button size="smaill" @click="resetAnswer">重置</a-button>
            <a-button type="primary" size="smaill" @click="testAnswer">测试</a-button>
            <a-button type="primary" size="smaill" @click="submitAnswer">提交</a-button>
          </div>
        </div>
      </div>
      <div v-if="setup == 3" class="contest-space">
        <div class="over">
          <a-result
            status="info"
            title="后面没有题目了，如果已经通过所有用例可返回主页等待结果"
          >
            <template #extra>
              <a-button key="console" type="primary" @click="goHome">返回主页</a-button>
            </template>
          </a-result>
        </div>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<style lang="scss" scoped>
.contest-space {
  height: calc(100vh - 104px);
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 20px 0;

  .question {
    width: 45%;
    height: 100%;
  }

  .editor-wrap {
    height: 100%;
    width: 50%;
  }

  .editor {
    height: calc(100% - 60px);
  }

  .opt-btn {
    text-align: right;
    margin-top: 20px;

    button {
      margin: 0 5px;
    }
  }
}

.contest-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  h5 {
    margin: 0;
  }
}
</style>
