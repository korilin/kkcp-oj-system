
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

class CommonModuleApis {

  async questionDefineData() {
    const mock = { status: false }
    const result = {
      levels: mock,
      types: mock
    }
    const levelsPromise = HttpService.get(QUESTION_LEVELS_URI)
    result.types = await HttpService.get(QUESTION_TYPES_URI)
    result.levels = await levelsPromise;
    return result;
  }
}

const QUERY_QUESTION_LIST_URI = "/admin/question/query/all"
const QUERY_QUESTION_DETAIL_URI = "/admin/question/query/detail"
const QUESTION_UPDATE_URI = "/admin/question/update"
const QUESTION_DELETE_URI = "/admin/question/delete"

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
}

const QUERY_CONTEST_LIST_URL = "/admin/contest/query/all"

class ContestModuleApis {
  async queryAllContest() {
    return HttpService.get(QUERY_CONTEST_LIST_URL)
  }
}

const Apis = {
  LoginModule: new LoginModuleApis(),
  CommonModule: new CommonModuleApis(),
  QuestionModule: new QuestionModuleApis(),
  ContestModule: new ContestModuleApis()
}

export default Apis
