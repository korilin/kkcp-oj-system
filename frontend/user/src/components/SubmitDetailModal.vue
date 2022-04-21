<script setup>
import { resolveMarkdownAsHtml } from "../utils/utils";

const props = defineProps({
  submitDetail: Object,
});

function toCodeMdHtml(code, lang) {
  const prefix = "```" + lang;
  const suffix = "```";
  const md = `${prefix}
${code}
${suffix}`;
  return resolveMarkdownAsHtml(md);
}
</script>

<template>
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
  <a-alert
    style="margin-top: 20px"
    :message="submitDetail.message"
    :type="submitDetail.pass == 100 ? 'success' : 'error'"
  />
</template>
