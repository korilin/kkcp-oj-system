
import HttpService from "./axios-service";

const SEND_CODE_URI = "/admin/verify/sendCode"
const LOGIN_URI = "/admin/verify/login"

class LoginModuleApis {

  async sendCode(email) {
    const url = `${SEND_CODE_URI}?email=${email}`
    return HttpService.post(url);
  }

  async doLogin(data) {
    return HttpService.post(LOGIN_URI, data)
  }
}

const QUESTION_LEVELS_URI = "/common/question/levels"
const QUESTION_TYPES_URI = "/common/question/types"

const CONTEST_TYPES_URI = "/common/contest/types"
const CONTEST_STATUSES_URI = "/common/contest/statuses"

class CommonModuleApis {

  async questionDefineData() {
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

  async contestDefineData() {
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
}

const QUERY_QUESTION_LIST_URI = "/admin/question/query/all"
const QUERY_QUESTION_DETAIL_URI = "/admin/question/query/detail"
const QUESTION_UPDATE_URI = "/admin/question/update"
const QUESTION_DELETE_URI = "/admin/question/delete"
const CREATE_QUESTION_URI = "/admin/question/new"

class QuestionModuleApis {

  async queryQuestions() {
    return HttpService.get(QUERY_QUESTION_LIST_URI)
  }

  async queryQuestionDetail(questionId) {
    const url = `${QUERY_QUESTION_DETAIL_URI}?questionId=${questionId}`
    return HttpService.get(url)
  }

  async updateQuestion(questionId, data) {
    const url = `${QUESTION_UPDATE_URI}?questionId=${questionId}`
    return HttpService.put(url, data)
  }

  async deleteQuestion(questionId) {
    const url = `${QUESTION_DELETE_URI}?questionId=${questionId}`
    return HttpService.delete(url)
  }

  async newQuestion(form) {
    return HttpService.post(CREATE_QUESTION_URI, form)
  }
}

const QUERY_CONTEST_LIST_URI = "/admin/contest/query/all"
const CREATE_CONTEST_URI = "/admin/contest/new"

class ContestModuleApis {
  async queryAllContest() {
    return HttpService.get(QUERY_CONTEST_LIST_URI)
  }

  async newContest(form) {
    return HttpService.post(CREATE_CONTEST_URI, form)
  }
}

const Apis = {
  LoginModule: new LoginModuleApis(),
  CommonModule: new CommonModuleApis(),
  QuestionModule: new QuestionModuleApis(),
  ContestModule: new ContestModuleApis()
}

export default Apis
