import axios from "axios";
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
      showHeader: true,
      questionTypes: [],
      questionLevels: [],
      contestTypes: [],
      contestStatuses: [],
      init: false
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
      axios.all([questionDefineData(), contestDefineData()])
        .then(axios.spread((r1, r2) => {
          if (r1.levels.status) {
            this.questionLevels = r1.levels.data;
          }
          if (r1.types.status) {
            this.questionTypes = r1.types.data;
          }
          if (r2.types.status) {
            this.contestTypes = r2.types.data
          }
          if (r2.statuses.status) {
            this.contestStatuses = r2.statuses.data
          }
          this.init = true
        }))
    }
  }
});

export const useContestStore = defineStore("contest", {
  state: () => ({
    releaseContest: null,
    contestRecord: [],
    init: false
  }),
  getters: {

  },
  actions: {
    async clean() {
      this.init = false
      this.releaseContest = null
      this.contestRecord = []
    },
    async refreshData() {
      this.clean()
      return this.initData()
    },
    async initData() {
      const job1 = HttpService.get("/visitor/query/contest/records").then(body => {
        if (body.status) {
          this.contestRecord = body.data
          console.log(body.data);
        }
      })
      const job2 = HttpService.get("/visitor/query/contest/release").then(body => {
        if (body.status) {
          this.releaseContest = body.data
        }
      })
      await job1;
      await job2;
      this.init = true
    },
    async ensureInit() {
      if (!this.init) {
        return this.initData();
      }
    }
  }
})

async function updateProfile(profile) {
  await HttpService.put("/user/update", profile)
}

export const useUserStore = defineStore("user", {
  state: () => ({
    token: null,
    profile: null,
  }),
  actions: {
    async initProfile() {
      const config = {
        headers: {
          Authorization: `token ${this.token}`
        }
      }
      const response = await axios.get("https://api.github.com/user", config)
      this.profile = {
        id: response.data.id,
        login: response.data.login,
        name: response.data.name,
        email: response.data.email,
        avatarUrl: response.data.avatar_url,
        bio: response.data.bio,
        htmlUrl: response.data.html_url
      }
      updateProfile(this.profile)
    }
  }
})
