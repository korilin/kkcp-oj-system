<script setup>
import { defineComponent, onMounted, reactive, ref } from "vue";
import { useRoute, } from "vue-router";
import { resolveMarkdownAsHtml } from "../utils/tool-fun";
import * as echarts from 'echarts';
import InstantUploadBox from "../components/InstantUploadBox.vue";

const route = useRoute();

const questionId = route.params.questionId
const readMode = ref(true)
const spinning = ref(false)
const collapseKey = ref("1")

const question = reactive({
    questionId: questionId,
    title: "模拟题目",
    type: 1,
    level: 1,
    description: {
        lastUpdateTime: "2022/2/20 23:20:00",
        htmlText: "hi"
    },
    useContests: [],
    submiteTimes: 1000,
    passTimes: 800,
});

// 文件变化
const handleChange = (file) => {
    spinning.value = true;
    const reader = new FileReader();
    reader.onload = function fileReadCompleted() {
        // 当读取完成时，内容只在`reader.result`中
        question.description.htmlText = resolveMarkdownAsHtml(reader.result);
        spinning.value = false;
    };
    reader.readAsText(file);
};

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
    loadSubmitCountEchart(question.submiteTimes, question.passTimes)
})
</script>
<template>
    <a-descriptions class="global-question-desc-style">
        <template #extra>
            <span v-if="readMode">
                <a-button @click="readMode = false">Edit</a-button>
                <a-button @click="readMode = false" type="danger" style="margin-left: 20px;">Delete</a-button>
            </span>
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
                :bordered="false"
            >
                <a-select-option v-for="(type, index) in questionTypes" :value="index">{{ type }}</a-select-option>
            </a-select>
        </a-descriptions-item>
        <a-descriptions-item label="难度">{{ question.level }}</a-descriptions-item>
        <a-descriptions-item label="答案提交次数" :span="3" style="text-align: center;">
            <div id="submitCountEchart" :style="{ width: '500px', height: '250px', }"></div>
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
            <a-divider>题目数据/文件</a-divider>
            <div class="file-space">
                <InstantUploadBox
                    class="upload-box"
                    text="代码模版：上传 Kotlin 文件进行更新"
                    :hint="'最后更新时间：' + question.description.lastUpdateTime"
                    :handleChange="handleChange"
                    kkcpIcon="icon-kotlin-file"
                />
                <InstantUploadBox
                    class="upload-box"
                    :text="'测试数据：上传 JSON 文件进行更新'"
                    :hint="'最后更新时间：' + question.description.lastUpdateTime"
                    :handleChange="handleChange"
                    kkcpIcon="icon-JSON"
                />
                <InstantUploadBox
                    class="upload-box"
                    style="width: 80%;"
                    :text="'题目描述：上传 Markdown 文件进行解析'"
                    :hint="'最后更新时间：' + question.description.lastUpdateTime"
                    :handleChange="handleChange"
                    kkcp-icon="icon-markdown"
                />
            </div>
        </a-descriptions-item>
    </a-descriptions>
    <a-spin tip="解析中" :spinning="spinning">
        <a-collapse v-model:activeKey="collapseKey" ghost>
            <a-collapse-panel key="1" header="题目描述">
                <div
                    class="desc-md-space markdown-html"
                    v-html="question.description.htmlText"
                    v-highlight
                ></div>
            </a-collapse-panel>
        </a-collapse>
    </a-spin>
</template>

<style lang="scss">
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