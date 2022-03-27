<script setup>
import { ref } from 'vue';
import { resolveMarkdownAsHtml } from '../utils/tool-fun';

/**
 * Question的3个特殊数据：HTML描述，Kotlin代码模板，测试数据JSON
 */
const props = defineProps({
  description: String,
  descriptionSpinning: Boolean,
  codeTemplate: String,
  codeTemplateSpinning: Boolean,
  testDataJson: String,
  testDataJsonSpinning: Boolean
});

const activeKey = ref("0")

const toCodeMdHtml = (code, lang) => {
  const prefix = "```" + lang
  const suffix = "```"
  const md = `${prefix}
${code}
${suffix}`;
  return resolveMarkdownAsHtml(md)
}
</script>


<template>
  <a-collapse class="desc-md-space markdown-html" v-model:activeKey="activeKey" ghost>
    <a-collapse-panel key="1" header="题目描述">
      <a-spin tip="解析中" :spinning="descriptionSpinning">
        <div v-html="description" v-highlight></div>
      </a-spin>
    </a-collapse-panel>
    <a-collapse-panel key="2" header="Kotlin 代码模板">
      <a-spin tip="解析中" :spinning="codeTemplateSpinning">
        <div v-html="toCodeMdHtml(codeTemplate, 'Kotlin')" v-highlight></div>
      </a-spin>
    </a-collapse-panel>
    <a-collapse-panel key="3" header="测试数据（JSON）">
      <a-spin tip="解析中" :spinning="testDataJsonSpinning">
        <div v-html="toCodeMdHtml(testDataJson, 'JSON')" v-highlight></div>
      </a-spin>
    </a-collapse-panel>
  </a-collapse>
</template>
