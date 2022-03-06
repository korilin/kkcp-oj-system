<script setup>
import { reactive, ref } from 'vue';
import { useCommonStore } from '../plugins/pinia';
import InstantUploadBox from '../components/InstantUploadBox.vue';
import { resolveMarkdownAsHtml } from '../utils/tool-fun';

const commonStore = useCommonStore()

const formRef = ref();
const collapseKey = ref("1");

const formState = reactive({
    title: "",
    type: 0,
    level: 0,
    description: "",
    codeTemplate: "",
    testDataJson: ""
})

let titleRole = async (_rule, value) => {
    if (value === '') {
        return Promise.reject('Please input the title');
    } else {
        return Promise.resolve();
    }
};

let selectRole = async (_rule, value) => {
    if (value == 0) {
        return Promise.reject('Please select the field');
    } else {
        return Promise.resolve();
    }
};

let descPromise = null;
let codePromise = null;
let dataPromise = null;

let descriptionRole = async (_rule, value) => {
    if (descPromise == null || await descPromise == "") {
        return Promise.reject('Please upload the corresponding file');
    } else {
        return Promise.resolve();
    }
};

let codeTemplateRole = async (_rule, value) => {
    if (codePromise == null || await descPromise == "") {
        return Promise.reject('Please upload the corresponding file');
    } else {
        return Promise.resolve();
    }
};

let testDataJsonRole = async (_rule, value) => {
    if (dataPromise == null || await descPromise == "") {
        return Promise.reject('Please upload the corresponding file');
    } else {
        return Promise.resolve();
    }
};

const rules = {
    title: [{ required: true, validator: titleRole, trigger: 'change' }],
    type: [{ required: true, validator: selectRole, trigger: 'change' }],
    level: [{ required: true, validator: selectRole, trigger: 'change' }],
    description: [{ required: true, validator: descriptionRole, trigger: 'change' }],
    codeTemplate: [{ required: true, validator: codeTemplateRole, trigger: 'change' }],
    testDataJson: [{ required: true, validator: testDataJsonRole, trigger: 'change' }],
};

// 解析 Markdown 文件
const handleDescriptionChange = (file) => {
    // spinning.value = true;
    descPromise = new Promise((resolve) => {
        const reader = new FileReader();
        reader.onload = function fileReadCompleted() {
            // 当读取完成时，内容只在`reader.result`中
            formState.description = resolveMarkdownAsHtml(reader.result);
            // spinning.value = false;
            resolve();
        };
        reader.readAsText(file);
    });
};

const handleFinish = values => {
    console.log(values, formState);
};

const handleFinishFailed = errors => {
    console.log(errors);
};
</script>
<template>
    <a-form
        ref="formRef"
        :model="formState"
        :label-col="{ style: { width: '150px' } }"
        :wrapper-col="{ span: 14 }"
        :rules="rules"
        @finish="handleFinish"
        @finishFailed="handleFinishFailed"
    >
        <a-form-item label="Title" has-feedback name="title">
            <a-input v-model:value="formState.title" />
        </a-form-item>
        <a-form-item label="Type" has-feedback name="type">
            <a-radio-group v-model:value="formState.type">
                <a-radio
                    v-for="qType in commonStore.questionTypes"
                    :value="qType.id"
                >{{ qType.text }}</a-radio>
            </a-radio-group>
        </a-form-item>
        <a-form-item label="Level" has-feedback name="level">
            <a-radio-group v-model:value="formState.level">
                <a-radio
                    v-for="level in commonStore.questionLevels"
                    :value="level.id"
                >{{ level.text }}</a-radio>
            </a-radio-group>
        </a-form-item>
        <a-form-item label="Description" has-feedback name="description">
            <InstantUploadBox
                text="上传 Markdown 进行解析"
                hint="解析的 Markdown 将会被转换为 HTML 字符串存入数据库"
                :handleChange="handleDescriptionChange"
            />
            <a-collapse v-model:activeKey="collapseKey" ghost>
                <a-collapse-panel key="1" header="题目描述">
                    <div class="markdown-html" v-html="formState.description" v-highlight></div>
                </a-collapse-panel>
            </a-collapse>
        </a-form-item>
        <a-form-item label="Code Template" has-feedback name="codeTemplate">
            <InstantUploadBox
                text="上传 Kotlin 文件"
                hint="该 Kotlin 模板将用于执行用户代码与测试数据来校验答案"
                :handleChange="handleDescriptionChange"
            />
        </a-form-item>
        <a-form-item label="Test Data" has-feedback name="testDataJson">
            <InstantUploadBox
                text="上传 JSON 文件"
                hint="用于校验用户答案，格式请遵循平台说明文档"
                :handleChange="handleDescriptionChange"
            />
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
            <a-button type="primary" html-type="submit">Create</a-button>
            <a-button style="margin-left: 10px">Cancel</a-button>
        </a-form-item>
    </a-form>
</template>