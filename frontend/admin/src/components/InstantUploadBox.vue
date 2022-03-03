
<script setup>
import { ref } from 'vue';
import { InboxOutlined } from "@ant-design/icons-vue";

// 拦截上传
const beforeUpload = file => {
    fileList.value[0] = file;
    return false;
};

const props = defineProps({
    text: String,
    lastUpdateTime: String,
    handleChange: Function,
})
// 新文件列表，需要保证列表只有一个 file
const fileList = ref([]);

const doChange = async ({
    file,
    fileList,
}) => {
    props.handleChange(file);
    fileList.pop(0);
}
</script>

<template>
    <div>
        <a-upload-dragger
            name="file"
            @change="doChange"
            :before-upload="beforeUpload"
            v-model:file-list="fileList"
        >
            <div style="padding: 30px;">
                <p class="ant-upload-drag-icon">
                    <inbox-outlined></inbox-outlined>
                </p>
                <p class="ant-upload-text">{{ text }}</p>
                <p class="ant-upload-hint">最后上传时间: {{ lastUpdateTime }}</p>
            </div>
        </a-upload-dragger>
    </div>
</template>