<script setup>
import { useCommonStore } from "../plugins/pinia";
import { ref, reactive } from "vue";
import { resolveMarkdownAsHtml } from "../utils/utils";

const props = defineProps({
  question: Object,
  submits: Array,
});

const commonStore = useCommonStore();

const levelsColor = [undefined, "green", "orange", "red"];

const tabList = [
  {
    key: "1",
    tab: "问题描述",
  },
  {
    key: "2",
    tab: "提交记录",
  },
  {
    key: "3",
    tab: "自测数据",
  },
];

const activeTabKey = ref("1");
const submitDetail = ref({});
const showModal = ref(false);

const result = reactive({
  status: -1,
  message: "",
});

function onTabChange(key, tag) {
  activeTabKey.value = key;
}

const testDataColumns = [
  {
    title: "输入",
    dataIndex: "input",
    key: "input",
  },
  {
    title: "输出",
    dataIndex: "output",
    key: "output",
  },
];

const submitColumns = [
  {
    title: "提交时间",
    dataIndex: "submitTime",
    key: "submitTime",
  },
  {
    title: "结果",
    key: "result",
  },
  {
    title: "Accepted",
    dataIndex: "pass",
    key: "pass",
  },
  {
    title: "Action",
    key: "action",
  },
];

/**
 *
 * @param {Number} status -1 隐藏 0 加载中 1 失败 2 成功
 * @param {String} message 内容
 */
function updateResult(status, message) {
  activeTabKey.value = "2";
  result.status = status;
  result.message = message;
}

function toCodeMdHtml(code, lang) {
  const prefix = "```" + lang;
  const suffix = "```";
  const md = `${prefix}
${code}
${suffix}`;
  return resolveMarkdownAsHtml(md);
}

function showDetail(item) {
  submitDetail.value = item;
  showModal.value = true;
}

function closeDetail() {
  showModal.value = true;
}

defineExpose({
  updateResult,
});
</script>
<template>
  <a-card
    :bordered="false"
    class="question"
    :tab-list="tabList"
    :active-tab-key="activeTabKey"
    @tabChange="(key) => onTabChange(key, 'key')"
  >
    <template #title>
      <h5>{{ question.title }}</h5>
      <div>
        <a-tag color="blue">{{
          commonStore.getQuestionTypeById(question.type).text
        }}</a-tag>
        <a-tag :color="levelsColor[question.level]">{{
          commonStore.getQuestionLevelById(question.level).text
        }}</a-tag>
      </div>
    </template>
    <div
      style="height: 100%; user-select: text"
      class="markdown-html"
      v-html="question.description"
      v-highlight
      v-show="activeTabKey == '1'"
    />
    <div v-show="activeTabKey == '2'">
      <a-card class="result-card" title="运行结果" v-if="result.status != -1">
        <a-spin :spinning="result.status == 0">
          <a-alert
            :message="result.message"
            :type="result.status == 1 ? 'success' : result.status == 2 ? 'error' : 'info'"
          />
        </a-spin>
      </a-card>
      <div style="margin-top: 20px">
        <a-table :dataSource="submits" :columns="submitColumns">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'pass'">{{ record.pass }}% </template>
            <template v-else-if="column.key === 'result'">
              <span>
                <a-tag :color="record.pass == 100 ? 'green' : 'orange'">
                  {{ record.pass == 100 ? "通过" : "未通过" }}
                </a-tag>
              </span>
            </template>
            <template v-if="column.key === 'action'">
              <a-button type="link" @click="showDetail(record)">详情</a-button>
            </template>
          </template>
        </a-table>
      </div>
      <a-modal v-model:visible="showModal" title="提交记录" :width="800">
        <template #footer>
          <a-button key="back" @click="closeDetail">关闭</a-button>
        </template>
        <a-descriptions>
          <a-descriptions-item label="提交时间">
            {{ submitDetail.submitTime }}
          </a-descriptions-item>
          <a-descriptions-item label="结果">
            <a-tag :color="submitDetail.pass == 100 ? 'green' : 'orange'">
              {{ submitDetail.pass == 100 ? "通过" : "未通过" }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="用例通过比例">
            {{ submitDetail.pass }}%
          </a-descriptions-item>
          <a-descriptions-item label="耗时">
            {{ submitDetail.elapsedTime }}ms
          </a-descriptions-item>
        </a-descriptions>
        <a-divider>Answer</a-divider>
        <div
          v-html="toCodeMdHtml(submitDetail.answer, 'Kotlin')"
          v-highlight
          class="markdown-html"
        ></div>
      </a-modal>
    </div>
    <div v-show="activeTabKey == '3'">
      <a-table :dataSource="question.testDataJson" :columns="testDataColumns" />
    </div>
  </a-card>
</template>

<style scoped lang="scss">
.question {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  user-select: none;
}

::-webkit-scrollbar {
  width: 8px;
  background-color: #f5f5f5;
}

/*定义滚动条轨道
 内阴影+圆角*/
::-webkit-scrollbar-track {
  border-radius: 10px;
  background-color: #f5f5f5;
}

/*定义滑块
 内阴影+圆角*/
::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: #bcd8f2;
}

.result-card {
  width: 100%;
  border-radius: 15px;
  padding: 0 10px;
}
</style>
