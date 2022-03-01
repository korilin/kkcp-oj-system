<script setup>
import { reactive, ref } from "vue";
import { useRoute, } from "vue-router";
import { InboxOutlined } from "@ant-design/icons-vue";
import { resolveMarkdownAsHtml } from "../utils/tool-fun";

const route = useRoute();

const questionId = route.params.questionId
const readMode = ref(true)
const spinning = ref(false)
const collapseKey = ref("1")

// 新文件列表，需要保证列表只有一个 file
const fileList = ref([]);

// 拦截上传
const beforeUpload = file => {
    fileList.value[0] = file;
    return false;
};

// 文件变化
const handleChange = ({
    file,
    fileList,
}) => {
    const reader = new FileReader();
    reader.onload = function fileReadCompleted() {
        // 当读取完成时，内容只在`reader.result`中
        question.description.mdText = reader.result;
    };
    reader.readAsText(file);
};


const question = reactive({
    questionId: questionId,
    title: "模拟题目",
    type: 1,
    level: 1,
    tags: "Vue|Kotlin|More",
    description: {
        lastUpdateTime: "2022/2/20 23:20:00",
        mdText: "hi"
    },
    useContests: [],
    submiteTimes: 1000,
    passTimes: 800,
});

</script>
<template>
    <a-descriptions class="global-question-desc-style">
        <template #extra>
            <a-button v-if="readMode" @click="readMode = false">Edit</a-button>
            <span v-else>
                <a-button @click="readMode = true" type="primary">Done</a-button>
                <a-button @click="readMode = true" style="margin-left: 20px;">Cancel</a-button>
            </span>
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
                <a-select-option :value="1">实用技巧</a-select-option>
                <a-select-option :value="2">算法解题</a-select-option>
                <a-select-option :value="3">源码模拟</a-select-option>
            </a-select>
        </a-descriptions-item>
        <a-descriptions-item label="难度">{{ question.level }}</a-descriptions-item>
        <a-descriptions-item label="标签">{{ question.tags }}</a-descriptions-item>
        <a-descriptions-item label="通过/提交（次数）" :span="2">
            <a-statistic value="/">
                <template #prefix>
                    <span class="text-success">{{ question.passTimes }}</span>
                </template>
                <template #suffix>
                    <span>{{ question.submiteTimes }}</span>
                </template>
            </a-statistic>
        </a-descriptions-item>
        <a-descriptions-item :span="3" class="contests">
            <a-divider>引用场次</a-divider>
            <a-list
                size="small"
                :data-source="question.useContests"
                :locale="{ emptyText: '没有引用本题目的竞赛场次' }"
            >
                <template #renderItem="{ item }">
                    <a-list-item>
                        <a-button type="link">{{ item.title }}</a-button>
                    </a-list-item>
                </template>
            </a-list>
        </a-descriptions-item>
        <a-descriptions-item :span="3" class="files">
            <a-divider>题目文件</a-divider>
        </a-descriptions-item>
    </a-descriptions>
    <a-spin tip="解析中" :spinning="spinning">
        <a-collapse v-model:activeKey="collapseKey">
            <a-collapse-panel key="1" header="题目描述">
                <div class="file-space" v-if="!readMode">
                    <a-upload-dragger
                        name="file"
                        @change="handleChange"
                        :before-upload="beforeUpload"
                        v-model:file-list="fileList"
                    >
                        <div style="padding: 30px;">
                            <p class="ant-upload-drag-icon">
                                <inbox-outlined></inbox-outlined>
                            </p>
                            <p class="ant-upload-text">上传 Markdown 文件进行解析</p>
                            <p
                                class="ant-upload-hint"
                            >最后上传时间: {{ question.description.lastUpdateTime }}</p>
                        </div>
                    </a-upload-dragger>
                </div>
                <div
                    class="desc-md-space markdown-h tml"
                    v-html="resolveMarkdownAsHtml(question.description.mdText)"
                    v-highlight
                ></div>
            </a-collapse-panel>
        </a-collapse>
    </a-spin>
</template>

<style scoped lang="scss">
.desc-md-space,
.file-space {
    margin-top: 30px;
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