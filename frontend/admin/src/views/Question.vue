<script setup>
import { onMounted, reactive, ref } from "vue";
import { useRoute, } from "vue-router";
import { resolveMarkdownAsHtml } from "../utils/tool-fun";
import HttpService from "../utils/axios-service";
import QuestionDescriptions from "../components/QuestionDescriptions.vue";
import QuestionCollapse from "../components/QuestionCollapse.vue";

const route = useRoute();

const questionId = route.params.questionId

/**
 * 编辑时备份原有的数据
 */
const backup = {
    question: {},
    commits: {},
    contests: []
};

const question = ref(backup.question);
const commits = ref(backup.commits);
const contests = ref(backup.contests)

function convertData(data) {
    backup.question = data.question;
    backup.commits = data.commits;
    backup.contests = data.contests;
    // Object.assign(question, backup.question);
    // Object.assign(commits, backup.commits);
    // Object.assign(contests, backup.contests);
    question.value = backup.question;
    commits.value = backup.commits;
    contests.value = backup.contests;
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

getQuestionDetail();

const inited = ref(false)
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
        />

        <QuestionCollapse 
            :description="question.description"
            :descriptionSpinning="descriptionSpinning"
            :codeTemplate="question.codeTemplate"
            :codeTemplateSpinning="codeTemplateSpinning"
            :testDataJson="question.testDataJson"
            :testDataJsonSpinning="testDataJsonSpinning"
        />
    </div>
    <div v-else style="text-align: center;">
        <a-spin />
    </div>
</template>
