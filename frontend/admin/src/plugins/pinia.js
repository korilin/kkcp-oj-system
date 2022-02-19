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

export const useContestsStore = defineStore("contests", {
    state: () => {
        return {
            data: [
                {
                    contestId: 1,
                    title: "测试数据1",
                    description: "这可能是一个非常长的描述",
                    duration: 3600,
                    startTime: "2022/2/20 02:25:00",
                    status: 0,
                },
                {
                    contestId: 2,
                    title: "测试数据2",
                    description: "这可能是一个非常长的描述",
                    duration: 3600,
                    startTime: "2022/2/20 02:25:00",
                    status: 0,
                },
                {
                    contestId: 3,
                    title: "测试数据3",
                    description: "这可能是一个非常长的描述",
                    duration: 3600,
                    startTime: "2022/2/20 02:25:00",
                    status: 1,
                },
            ],
        };
    },
});
