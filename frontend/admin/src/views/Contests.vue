<script setup>
import { useContestsStore } from "../plugins/pinia"

const contestsStore = useContestsStore();

const columns = [
    {
        title: "标题",
        dataIndex: "title",
        key: "title"
    },
    {
        title: "开始时间",
        dataIndex: "startTime",
        key: "startTime"
    },
    {
        title: "时长",
        dataIndex: "duration",
        key: "duration"
    },
    {
        title: "状态",
        dataIndex: "status",
        key: "status"
    },
    {
        title: "Action",
        key: "action"
    },
]
</script>
<template>
    <a-table :columns="columns" :data-source="contestsStore.data" rowKey="contestId">
        <template #bodyCell="{ column, record }">
            <template v-if="column.key == 'status'">{{ record.status }} ?</template>
            <template v-else-if="column.key == 'action'">
                <span>
                    <a class="ant-dropdown-link">More actions</a>
                </span>
            </template>
        </template>
    </a-table>
</template>