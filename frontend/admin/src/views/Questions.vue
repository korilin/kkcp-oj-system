<script setup>
import { useRouter } from "vue-router";
import { useQuestionsStore } from "../plugins/pinia"

const router = useRouter();
const questionsStore = useQuestionsStore();

const columns = [
    {
        title: "标题",
        dataIndex: "title",
        key: "title"
    },
    {
        title: "题目类型",
        dataIndex: "type",
        key: "type"
    },
    {
        title: "难度",
        key: "level",
    },
    {
        title: "ACTION",
        key: "action",
    },
];

const levels = {
    bg: [undefined, "green", "orange", "red"],
    text: [undefined, "easy", "medium", "hard"]
}

function goQuestion(questionId) {
    router.push({
        path: "/question/" + questionId
    })
}
</script>
<template>
    <a-table :columns="columns" :data-source="questionsStore.data" rowKey="questionId">
        <template #bodyCell="{ column, record }">
            <template v-if="column.key == 'level'">
                <a-tag :color="levels.bg[record.level]">{{ levels.text[record.level] }}</a-tag>
            </template>
            <template v-else-if="column.key == 'action'">
                <a-button type="link" size="small" @click="goQuestion(record.questionId)">more&edit</a-button>
                <a-divider type="vertical" />
                <a-button type="link" size="small" style="color: #ff7875;">del</a-button>
            </template>
        </template>
    </a-table>
</template>