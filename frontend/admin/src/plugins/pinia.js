import { defineStore } from "pinia";

// useStore could be anything like useUser, useCart
// the first argument is a unique id of the store across your application
export const useAccountStore = defineStore("account", {
    state: () => {
        return {
            loginToken: null,
            account: null,
        };
    },
});

const contests = [];

for (let index = 0; index < 20; index++) {
    contests[index] = {
        contestId: index,
        title: "测试数据" + index,
        description: "这可能是一个非常长的描述",
        duration: 3600,
        startTime: "2022/2/20 02:25:00",
        status: index > 10 ? 1 : 0,
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
