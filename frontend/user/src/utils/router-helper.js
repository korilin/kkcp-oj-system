import router from '../plugins/router';

export const goHome = function () {
  router.push('/')
}

export const goContest = function () {
  router.push("contest")
}

export const goRecord = function (contestId) {
  router.push({
    name: "record",
    params: {
      contestId: contestId
    }
  })
}
