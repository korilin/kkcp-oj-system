import router from '../plugins/router';

export const goHome = function () {
  router.push('/')
}

export const goQuestions = function () {
  router.push({ name: "questions" })
}

export const goQuestionItem = function (questionId) {
  router.push({
    name: "question-item",
    params: {
      questionId: questionId
    }
  })
}

export const goNewQuestion = function () {
  router.push({ name: "question-new" })
}

export const goNewContest = function () {
  router.push({ name: "contest-new" })
}
