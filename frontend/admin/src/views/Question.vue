<script setup>
import { createVNode, reactive, ref } from "vue";
import { useRoute } from "vue-router";
import { resolveMarkdownAsHtml } from "../utils/utils";
import QuestionDetail from "../components/QuestionDetail.vue";
import QuestionCollapse from "../components/QuestionCollapse.vue";
import { message, Modal } from "ant-design-vue";
import Apis from "../utils/apis";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { goQuestions } from "../utils/router-helper";
import { useQuestionStore } from "../plugins/pinia";

const route = useRoute();
const questionStore = useQuestionStore();

const questionId = route.params.questionId;

const inited = ref(false);

/**
 * 编辑时备份原有的数据，数据不能为空
 * @see convertData 唯一备份点
 */
const backup = {
  question: "",
  submits: "",
  contests: "",
};

// 数据备份
function backupData(data) {
  // 保证数据不为 null
  backup.question = JSON.stringify(data.question ?? {});
  backup.submits = JSON.stringify(data.submits ?? {});
  backup.contests = JSON.stringify(data.contests ?? []);
}

// 数据恢复
function recoverData() {
  Object.assign(question, JSON.parse(backup.question));
  Object.assign(submits, JSON.parse(backup.submits));
  Object.assign(contests, JSON.parse(backup.contests));
}

// 实际操作数据
const question = reactive({});
const submits = reactive({});
const contests = reactive([]);

function convertData(data) {
  data.question.testDataJson = JSON.stringify(data.question.testDataJson);
  backupData(data);
  recoverData();
}

// ========================= 接口请求 =========================

const getQuestionDetail = () => {
  Apis.QuestionModule.queryQuestionDetail(questionId).then((body) => {
    if (body.status) {
      const data = body.data;
      convertData(data);
      inited.value = true;
    }
  });
};

// ========================= 文件数据更新 =========================

const descriptionSpinning = ref(false);
const handleDescriptionChange = (file) => {
  descriptionSpinning.value = true;
  const reader = new FileReader();
  reader.onload = function fileReadCompleted() {
    question.description = resolveMarkdownAsHtml(reader.result);
    descriptionSpinning.value = false;
  };
  reader.readAsText(file);
};

const codeTemplateSpinning = ref(false);
const handleCodeTemplateChange = (file) => {
  codeTemplateSpinning.value = true;
  const reader = new FileReader();
  reader.onload = function fileReadCompleted() {
    question.codeTemplate = reader.result;
    codeTemplateSpinning.value = false;
  };
  reader.readAsText(file);
};

const testDataJsonSpinning = ref(false);
const handleTestDataJsonChange = (file) => {
  testDataJsonSpinning.value = true;
  const reader = new FileReader();
  reader.onload = function fileReadCompleted() {
    question.testDataJson = reader.result;
    testDataJsonSpinning.value = false;
  };
  reader.readAsText(file);
};

// 发起数据请求
getQuestionDetail();

// 按钮事件
function cancelAction() {
  recoverData();
}

async function saveAction() {
  const compare = backup.question;
  const data = {};
  // 取变化数据，减少请求体大小
  if (question.type != compare.type) data.type = question.type;
  if (question.level != compare.level) data.level = question.level;
  if (question.title != compare.title) data.title = question.title;
  if (question.codeTemplate != compare.codeTemplate)
    data.codeTemplate = question.codeTemplate;
  if (question.testDataJson != compare.testDataJson)
    data.testDataJson = question.testDataJson;
  if (question.description != compare.description)
    data.description = question.description;
  const result = await Apis.QuestionModule.updateQuestion(questionId, data);
  if (result.status) {
    message.success("Update Success");
    getQuestionDetail();
  } else {
    recoverData();
  }
}

function showDeleteConfirmModal() {
  Modal.confirm({
    title: "Do you want to delete this question?",
    icon: createVNode(ExclamationCircleOutlined),
    content:
      "After the problem is deleted, the data submitted by the user will be deleted. Are you sure to delete the problem?",
    okType: "danger",
    onOk() {
      return Apis.QuestionModule.deleteQuestion(questionId).then((body) => {
        if (body.status) {
          message.success("Delete Success");
          questionStore.refreshData();
          goQuestions();
        } else {
          message.error(body.message);
        }
      });
    },
    onCancel() {},
  });
}

function showNoDeleteModal() {
  Modal.error({
    title: `Could not delete Question[${questionId}]`,
    content: `This question is referenced by the ${contests.length} contest activities and cannot be removed`,
  });
}

async function deleteAction() {
  if (contests.length == 0) {
    showDeleteConfirmModal();
  } else {
    showNoDeleteModal();
  }
}
</script>
<template>
  <div v-if="inited">
    <QuestionDetail
      :question="question"
      :submits="submits"
      :contests="contests"
      :handleDescriptionChange="handleDescriptionChange"
      :handleCodeTemplateChange="handleCodeTemplateChange"
      :handleTestDataJsonChange="handleTestDataJsonChange"
      :cancelAction="cancelAction"
      :saveAction="saveAction"
      :deleteAction="deleteAction"
    />

    <QuestionCollapse
      :description="question.description"
      :descriptionSpinning="descriptionSpinning"
      :codeTemplate="question.codeTemplate"
      :codeTemplateSpinning="codeTemplateSpinning"
      :testDataJson="JSON.stringify(question.testDataJson)"
      :testDataJsonSpinning="testDataJsonSpinning"
    />
  </div>
  <div v-else style="text-align: center">
    <a-spin />
  </div>
</template>
