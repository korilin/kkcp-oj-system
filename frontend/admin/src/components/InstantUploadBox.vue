
<script setup>
import { ref } from 'vue';
import { createFromIconfontCN, InboxOutlined } from "@ant-design/icons-vue";

const KKCPIcon = createFromIconfontCN({
    scriptUrl: "//at.alicdn.com/t/font_3225437_iqqela7vpnq.js"
});

const props = defineProps({
    text: String,
    hint: String,
    handleChange: Function,
    kkcpIcon: String,
})

// 新文件列表，需要保证列表只有一个 file
const fileList = ref([]);

// 拦截上传
const beforeUpload = file => {
    fileList.value[0] = file;
    return false;
};

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
                    <KKCPIcon :type="kkcpIcon" v-if="kkcpIcon != null" />
                    <InboxOutlined v-else />
                </p>
                <p class="ant-upload-text">{{ text }}</p>
                <p class="ant-upload-hint">{{ hint }}</p>
            </div>
        </a-upload-dragger>
    </div>
</template>