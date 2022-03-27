import { defineStore } from "pinia";
import HttpService from "../utils/axios-service";

// useStore could be anything like useUser, useCart
// the first argument is a unique id of the store across your application
export const useAccountStore = defineStore("account", {
  state: () => {
    return {
      kkcpAdminToken: null,
    };
  },
});

export const useCommonStore = defineStore("common", {
  state: () => {
    return {
      questionTypes: [],
      questionLevels: [],
    };
  },
  getters: {
    getTypeById: (state) => {
      return (typeId) =>
        state.questionTypes.find((type) => type.id == typeId);
    },
    getLevelById: (state) => {
      return (levelId) =>
        state.questionLevels.find((level) => level.id == levelId);
    },
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

export const useQuestionsStore = defineStore("questions", {
  state: () => ({
    init: false,
    data: [],
  }),
  actions: {
    reQueryData() {
      this.init = false
      this.data = []
    }
  }
});
