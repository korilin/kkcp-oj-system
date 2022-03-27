
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

const Apis = {
  LoginModule: new LoginModuleApis(),
  CommonModule: new CommonModuleApis()
}

export default Apis
