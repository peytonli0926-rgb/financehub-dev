import Cookies from 'js-cookie'

const TokenKey = 'lotsoUserToken'

const ExpiresInKey = 'Admin-Expires-In'

export function getToken() {
  let token = Cookies.get(TokenKey)
  if (window.location.href.includes('?token')) {
    const value = getParam(window.location.href, 'token')
    // debugger
    if (value) {
      token = value
      setToken(token)
    }
  }
  return token
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getExpiresIn() {
  return Cookies.get(ExpiresInKey) || -1
}

export function setExpiresIn(time) {
  return Cookies.set(ExpiresInKey, time)
}

export function removeExpiresIn() {
  return Cookies.remove(ExpiresInKey)
}
function getParam(jspath, parm) {
  const urlparse = jspath.split('?')
  const parms = urlparse[1].split('&')
  for (let i = 0; i < parms.length; i++) {
    const pr = parms[i].split('=')
    if (pr[0] === parm) return pr[1]
  }
  return ''
}
