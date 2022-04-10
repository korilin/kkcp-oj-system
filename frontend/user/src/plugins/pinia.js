import { defineStore } from "pinia";
import HttpService from "../utils/axios-service";

const QUESTION_LEVELS_URI = "/common/question/levels"
const QUESTION_TYPES_URI = "/common/question/types"

const CONTEST_TYPES_URI = "/common/contest/types"
const CONTEST_STATUSES_URI = "/common/contest/statuses"

async function questionDefineData() {
  const mock = { status: false }
  const result = {
    levels: mock,
    types: mock
  }
  const levelsPromise = HttpService.get(QUESTION_LEVELS_URI)
  const typesPromis = HttpService.get(QUESTION_TYPES_URI)
  result.types = await typesPromis;
  result.levels = await levelsPromise;
  return result;
}

async function contestDefineData() {
  const mock = { status: false }
  const result = {
    types: mock,
    statuses: mock,
  }
  const typesPromis = HttpService.get(CONTEST_TYPES_URI)
  const statusesPromis = HttpService.get(CONTEST_STATUSES_URI)
  result.types = await typesPromis
  result.statuses = await statusesPromis
  return result
}

export const useCommonStore = defineStore("common", {
  state: () => {
    return {
      questionTypes: [],
      questionLevels: [],
      contestTypes: [],
      contestStatuses: [],
    };
  },
  getters: {
    getQuestionTypeById: (state) => {
      return (typeId) =>
        state.questionTypes.find((type) => type.id == typeId);
    },
    getQuestionLevelById: (state) => {
      return (levelId) =>
        state.questionLevels.find((level) => level.id == levelId);
    },
    getContestTypeById: (state) => {
      return (typeId) => state.contestTypes.find((type) => type.id == typeId)
    },
    getContestStatusById: (state) => {
      return (statusId) => state.contestStatuses.find((status) => status.id == statusId)
    }
  },
  actions: {
    async initData() {
      questionDefineData().then((result) => {
        if (result.levels.status) {
          this.questionLevels = result.levels.data;
        }
        if (result.types.status) {
          this.questionTypes = result.types.data;
        }
      })
      contestDefineData().then(result => {
        if (result.types.status) {
          this.contestTypes = result.types.data
        }
        if (result.statuses.status) {
          this.contestStatuses = result.statuses.data
        }
      })
    }
  }
});

