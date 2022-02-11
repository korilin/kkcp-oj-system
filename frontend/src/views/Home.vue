<script setup>
import KotlinSVG from "../components/KotlinSVG.vue"
import TrophySVG from "../components/TrophySVG.vue"
import { Empty } from 'ant-design-vue';
const emptyImage = Empty.PRESENTED_IMAGE_SIMPLE;

// 定义页面文本 & 默认状态属性
const noActiveSeminar = "当前没有进行中的活动";
const emptySeminarRecord = "没有记录"

const columns = [
    { title: "以往竞赛", dataIndex: "title", key: "title" },
    { title: "时长", dataIndex: "duration", key: "duration" },
    { title: "参加人数", dataIndex: "participants", key: "participants" },
];
const records = []
</script>

<template>
    <div class="home">
        <div class="hero-wrap">
            <div class="svg">
                <KotlinSVG class="kotlin-svg" />
                <TrophySVG class="trophy-svg" />
            </div>
            <div class="text">
                <h3 class="text-gray-2">Kotlin Knowledge Contest</h3>
                <p class="text-gray-6">Kotlin 编程知识竞赛</p>
            </div>
        </div>
    </div>
    <div class="main-wrap">
        <div class="current-seminar">
            <a-card :bordered="false" class="widget-1">
                <a-empty :description="noActiveSeminar" />
            </a-card>
        </div>
        <div class="records-wrap">
            <div class="seminar-redords card">
                <div class="header">
                    <h4 class="text-gray-9">往期竞赛记录</h4>
                    <div class="records">
                        <a-table :columns="columns" :data-source="records"></a-table>
                    </div>
                </div>
            </div>
            <div class="tools-box card"></div>
        </div>
    </div>
</template>

<style scoped lang="scss">
.hero-wrap {
    height: 500px;
    // background-image: linear-gradient(150deg, #ed485a 0%, #963f8a 60%, #6c49e3 100%);
    background-color: #27282c;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    flex-direction: column;
    .svg {
        position: relative;
        top: -50px;
        right: 10px;

        .kotlin-svg {
            height: 150px;
            width: 150px;
        }

        .trophy-svg {
            position: absolute;
            height: 70px;
            width: 70px;
            transform: rotate(30deg);
            right: 5px;
            bottom: 5px;
        }
    }

    .text {
        text-align: center;
        position: relative;
        top: -30px;
        p {
            font-size: 16px;
            font-weight: 200;
        }
    }
}

.main-wrap {
    max-width: 800px;
    margin: auto;

    .current-seminar {
        position: relative;
        top: -50px;

        .widget-1 {
            min-height: 100px;
        }
    }

    .records-wrap {
        display: flex;
        justify-content: space-between;

        .card {
            box-shadow: 0 0 50px 0 rgb(0 0 0 / 15%);
            border-radius: 20px;
        }
        .seminar-redords {
            width: 70%;

            .header {
                padding: 30px 20px;
            }
        }

        .tools-box {
            width: 25%;
        }
    }
}
</style>