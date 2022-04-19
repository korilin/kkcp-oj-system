<script setup>
import { useCommonStore } from "../plugins/pinia";
import { ref, reactive } from "vue";

const props = defineProps({
  question: Object,
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

// TODO
console.log(props.question);

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
      <div style="margin-top: 20px">commits</div>
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
