<script setup>
import { reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
const route = useRoute();

const questionId = route.params.questionId

// TODO request question info in here

// TODO remove mock question

const question = reactive({
    questionId: questionId,
    title: "模拟题目",
    type: 1,
    level: 1,
    tags: "Vue|Kotlin|More",
    description: "md 描述",
    useContests: [
        {
            contestId: 0,
            title: "contest 1",
        },
        {
            contestId: 1,
            title: "contest 2",
        }
    ],
    submiteTimes: 1000,
    passTimes: 800,
})

const readMode = ref(true)
</script>
<template>
    <a-descriptions class="global-question-desc-style">
        <template #extra>
            <a-button v-if="readMode" @click="readMode = false">Edit</a-button>
            <a-button v-else @click="readMode = true" type="primary">Done</a-button>
        </template>
        <a-descriptions-item label="题目">
            <a-input v-model:value="question.title" :bordered="false" :disabled="readMode" />
        </a-descriptions-item>
        <a-descriptions-item label="类型">
            <a-select
                ref="select"
                v-model:value="question.type"
                style="width: 120px"
                :disabled="readMode"
            >
                <a-select-option value="1">hi</a-select-option>
            </a-select>
        </a-descriptions-item>
        <a-descriptions-item label="难度">{{ question.level }}</a-descriptions-item>
        <a-descriptions-item label="标签">{{ question.tags }}</a-descriptions-item>
        <a-descriptions-item label="提交次数">{{ question.submiteTimes }}</a-descriptions-item>
        <a-descriptions-item label="通过次数">{{ question.passTimes }}</a-descriptions-item>
        <a-descriptions-item label="引用场次" :span="3">
            <a-list size="small" :data-source="question.useContests">
                <template #renderItem="{ item }">
                    <a-list-item>
                        <a-button type="link">{{ item.title }}</a-button>
                    </a-list-item>
                </template>
            </a-list>
        </a-descriptions-item>
        <a-descriptions-item label="题目描述" :span="3">
            <div style="padding-left:30px" v-html="question.description"></div>
        </a-descriptions-item>
    </a-descriptions>
</template>

<style lang="scss">
.global-question-desc-style {
    .ant-descriptions-item-label,
    .ant-descriptions-item-content {
        font-weight: 400;
        line-height: 3;
        font-size: 14px;
    }

    .ant-descriptions-item-content {
        width: 100%;
        display: inline-block;
    }

    .ant-input {
        border: 0;
    }
}
</style>