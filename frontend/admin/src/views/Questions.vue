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
        slots: { customRender: "level" }
    },
    {
        title: "标签",
        key: "tags",
        slots: { customRender: "tags" }
    },
    {
        title: "ACTION",
        key: "action",
        slots: { customRender: "action" }
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
        <template #tags="{ record }">
            <template v-for="tag in record.tags.split('|')">
                <a-tag color="blue">{{ tag }}</a-tag>
            </template>
        </template>
        <template #level="{ record }">
            <a-tag :color="levels.bg[record.level]">{{ levels.text[record.level] }}</a-tag>
        </template>
        <template #action="{ record }">
            <a-button type="link" size="small" @click="goQuestion(record.questionId)">more&edit</a-button>
            <a-divider type="vertical" />
            <a-button type="link" size="small" style="color: #ff7875;">del</a-button>
        </template>
    </a-table>
</template>