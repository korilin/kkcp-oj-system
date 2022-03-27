<script setup>
import { onMounted, reactive, ref } from "vue";
import { useRoute, } from "vue-router";
import { resolveMarkdownAsHtml } from "../utils/tool-fun";
import HttpService from "../utils/axios-service";
import QuestionDescriptions from "../components/QuestionDescriptions.vue";
import QuestionCollapse from "../components/QuestionCollapse.vue";

const route = useRoute();

const questionId = route.params.questionId

const inited = ref(false)

/**
 * 编辑时备份原有的数据，数据不能为空
 * @see convertData 唯一备份点
 */
const backup = {
    question: {},
    commits: {},
    contests: []
};

// 深度备份
function backupData() {
    Object.assign(backup.question, question.value);
    Object.assign(backup.commits, commits.value);
    Object.assign(backup.contests, contests.value);
}

// 深度恢复
function recoverData() {
    Object.assign(question.value, backup.question);
    Object.assign(commits.value, backup.commits);
    Object.assign(contests.value, backup.contests);
}

const question = ref(backup.question);
const commits = ref(backup.commits);
const contests = ref(backup.contests)

function convertData(data) {
    // 保证数据不为 null
    question.value = data.question ?? {};
    commits.value = data.commits ?? {};
    contests.value = data.contests ?? [];
    backupData();
}

const getQuestionDetail = async () => {
    const url = "/admin/question/query/detail?questionId=" + questionId;
    return HttpService.get(url).then((body) => {
        if (body.status) {
            const data = body.data;
            convertData(data)
            inited.value = true;
        }
    })
}

// ========================= 文件数据更新 =================================

const descriptionSpinning = ref(false)
const handleDescriptionChange = (file) => {
    descriptionSpinning.value = true;
    const reader = new FileReader();
    reader.onload = function fileReadCompleted() {
        question.description = resolveMarkdownAsHtml(reader.result);
        descriptionSpinning.value = false;
    };
    reader.readAsText(file);
};

const codeTemplateSpinning = ref(false)
const handleCodeTemplateChange = (file) => {
    codeTemplateSpinning.value = true;
    const reader = new FileReader();
    reader.onload = function fileReadCompleted() {
        question.codeTemplate = reader.result;
        codeTemplateSpinning.value = false;
    };
    reader.readAsText(file);
}

const testDataJsonSpinning = ref(false)
const handleTestDataJsonChange = (file) => {
    testDataJsonSpinning.value = true;
    const reader = new FileReader();
    reader.onload = function fileReadCompleted() {
        question.testDataJson = reader.result;
        testDataJsonSpinning.value = false;
    };
    reader.readAsText(file);
}

// 发起数据请求
getQuestionDetail();

// 按钮事件
function cancelAction() {
    recoverData()
}
</script>
<template>
    <div v-if="inited">
        <QuestionDescriptions
            :question="question"
            :commits="commits"
            :contests="contests"
            :handleDescriptionChange="handleDescriptionChange"
            :handleCodeTemplateChange="handleCodeTemplateChange"
            :handleTestDataJsonChange="handleTestDataJsonChange"
            :cancelAction="cancelAction"
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
    <div v-else style="text-align: center;">
        <a-spin />
    </div>
</template>
