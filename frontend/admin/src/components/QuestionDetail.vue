<script setup>
import { onMounted, ref } from "vue";
import InstantUploadBox from "../components/InstantUploadBox.vue";
import { useCommonStore } from "../plugins/pinia";
import * as echarts from 'echarts';
import { goContestItem } from "../utils/router-helper";

const props = defineProps({
  question: Object,
  submits: Object,
  contests: Object,
  handleCodeTemplateChange: Function,
  handleTestDataJsonChange: Function,
  handleDescriptionChange: Function,
  editAction: Function,
  deleteAction: Function,
  saveAction: Function,
  cancelAction: Function
})

const readMode = ref(true)
const commonStore = useCommonStore();

const loadSubmitCountEchart = (submiteTimes, passTimes) => {
  const option = {
    tooltip: {
      trigger: 'item'
    },
    title: {
      text: submiteTimes,
      left: 'center',
      top: 'center',
    },
    series: [
      {
        type: 'pie',
        data: [
          {
            value: submiteTimes - passTimes,
            name: '未通过次数'
          },
          {
            value: passTimes,
            name: '通过次数'
          },
        ],
        radius: ['50%', '70%']
      }
    ]
  };

  const submitCountEchart = echarts.init(document.getElementById('submitCountEchart'));
  submitCountEchart.setOption(option)
}

onMounted(() => {
  loadSubmitCountEchart(props.submits.count, props.submits.pass);
})

const doneLoading = ref(false)
const deleteLoading = ref(false)

async function doEdit() {
  props.editAction?.();
  readMode.value = false;
}

async function doDelete() {
  deleteLoading.value = true
  await props.deleteAction?.();
  deleteLoading.value = false
}

async function doSave() {
  doneLoading.value = true
  await props.saveAction?.();
  readMode.value = true;
  doneLoading.value = false
}

async function doCancel() {
  props.cancelAction?.();
  readMode.value = true;
}

const colors = [
  'pink',
  'purple',
  'blue',
  'geekblue',
  'green',
];
</script>
<template>
  <a-descriptions class="global-question-desc-style">
    <template #extra>
      <span v-if="readMode">
        <a-button @click="doEdit">Edit</a-button>
        <a-button
          @click="doDelete"
          danger
          type="primary"
          style="margin-left: 20px;"
          :loading="deleteLoading"
        >Delete</a-button>
      </span>
      <span v-else>
        <a-button @click="doSave" type="primary" :loading="doneLoading">Done</a-button>
        <a-button @click="doCancel" style="margin-left: 20px;">Cancel</a-button>
      </span>
    </template>
    <a-descriptions-item label="题目">
      <a-input v-model:value="question.title" :bordered="false" :disabled="readMode" />
    </a-descriptions-item>
    <a-descriptions-item label="类型">
      <a-select
        ref="typeSelect"
        v-model:value="question.type"
        style="width: 120px"
        :disabled="readMode"
        :bordered="false"
      >
        <a-select-option
          v-for="qType in commonStore.questionTypes"
          :value="qType.id"
        >{{ qType.text }}</a-select-option>
      </a-select>
    </a-descriptions-item>
    <a-descriptions-item label="难度">
      <a-select
        ref="levelSelect"
        v-model:value="question.level"
        style="width: 120px"
        :disabled="readMode"
        :bordered="false"
      >
        <a-select-option
          v-for="level in commonStore.questionLevels"
          :value="level.id"
        >{{ level.text }}</a-select-option>
      </a-select>
    </a-descriptions-item>
    <a-descriptions-item label="答案提交次数" :span="3" style="text-align: center;">
      <div id="submitCountEchart" :style="{ width: '500px', height: '250px', margin: 'auto' }"></div>
    </a-descriptions-item>
    <a-descriptions-item :span="3" class="contests">
      <a-divider>引用场次</a-divider>
      <a-list size="small" :data-source="contests" :locale="{ emptyText: '没有引用本题目的竞赛场次' }">
        <template #renderItem="{ item }">
          <a-list-item style="justify-content: flex-start;">
            <a-button type="link" @click="goContestItem(item.contestId)">{{ item.title }}</a-button>
            <a-tag
              :color="colors[item.status]"
            >{{ commonStore.getContestStatusById([item.status]).text }}</a-tag>
          </a-list-item>
        </template>
      </a-list>
    </a-descriptions-item>
    <a-descriptions-item :span="3" class="files" v-if="!readMode">
      <a-divider>题目数据/文件</a-divider>
      <div class="file-space">
        <InstantUploadBox
          class="upload-box"
          text="代码模版: 上传 Kotlin 文件进行更新"
          :hint="'最后更新时间: ' + question.codeTemplateLastUpdateTime"
          :handleChange="handleCodeTemplateChange"
          kkcpIcon="icon-kotlin-file"
        />
        <InstantUploadBox
          class="upload-box"
          :text="'测试数据: 上传 JSON 文件进行更新'"
          :hint="'最后更新时间: ' + question.testDataJsonLastUpdateTime"
          :handleChange="handleTestDataJsonChange"
          kkcpIcon="icon-JSON"
        />
        <InstantUploadBox
          class="upload-box"
          style="width: 80%;"
          :text="'题目描述: 上传 Markdown 文件进行解析'"
          :hint="'最后更新时间: ' + question.descriptionLastUpdateTime"
          :handleChange="handleDescriptionChange"
          kkcp-icon="icon-markdown"
        />
      </div>
    </a-descriptions-item>
  </a-descriptions>
</template>

<style lang="scss" scoped>
.file-space {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  padding: 20px;

  .upload-box {
    width: 45%;
    margin-top: 20px;
  }
}
</style>

<style lang="scss">
.global-question-desc-style {
  .ant-descriptions-item-label,
  .ant-descriptions-item-content {
    line-height: 3;
    font-size: 15px;
    font-weight: 400;
  }

  .ant-descriptions-item-content {
    width: 100%;
    display: inline-block;
  }

  .ant-input {
    border: 0;
    font-weight: 400;
  }

  .files,
  .contests {
    .ant-descriptions-item-container {
      .ant-descriptions-item-label {
        display: none;
      }

      flex-direction: column;
    }
    .ant-divider-inner-text {
      color: #8c8c8c;
      font-weight: 500;
      font-size: 15px;
    }
  }
}
</style>
