import router from '../plugins/router';

export const goHome = function () {
  router.push('/')
}

export const goQuestions = function () {
  router.push({ name: "questions" })
}
