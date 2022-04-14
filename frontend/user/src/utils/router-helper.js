import router from '../plugins/router';

export const goHome = function () {
  router.push('/')
}

export const goContest = function (contestId) {
  router.push("contest")
}
