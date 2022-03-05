import { defineStore } from "pinia";

// useStore could be anything like useUser, useCart
// the first argument is a unique id of the store across your application
export const useAccountStore = defineStore("account", {
    state: () => {
        return {
            kkcpAdminToken: null,
        };
    },
});

const contests = [];

for (let index = 0; index < 15; index++) {
    contests[index] = {
        contestId: index,
        title: "测试数据" + index,
        description: "这可能是一个非常长的描述",
        duration: 3600,
        startTime: "2022/2/20 02:25:00",
        status: index > 5 ? 1 : 0,
        questionCount: 10,
    };
}

export const useContestsStore = defineStore("contests", {
    state: () => {
        return {
            data: contests,
        };
    },
});

const questions = [];

for (let index = 0; index < 20; index++) {
    questions[index] = {
        questionId: index,
        type: 0,
        title: "模拟问题" + index,
        description: "很长的问题描述，可能是一个 html 片段",
        level: (index % 3) + 1,
    };
}

export const useQuestionsStore = defineStore("questions", {
    state: () => {
        return {
            data: questions,
        };
    },
});
