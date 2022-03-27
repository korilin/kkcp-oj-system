
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

const Apis = {
  LoginModule: new LoginModuleApis()
}

export default Apis
