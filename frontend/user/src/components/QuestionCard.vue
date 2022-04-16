<script setup>
import { useCommonStore } from "../plugins/pinia";
import { ref } from "vue";

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
    tab: "个人测试数据",
  },
];

const activeTabKey = ref("1");

function onTabChange(key, tag) {
  activeTabKey.value = key;
}

console.log(props.question);
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
      style="height: 100%; user-select: text;"
      class="markdown-html"
      v-html="question.description"
      v-highlight
      v-show="activeTabKey == '1'"
    />
    <div></div>
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
</style>
