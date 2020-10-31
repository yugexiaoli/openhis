import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()
  // 设置页面标题
  document.title = getPageTitle(to.meta.title)
  // 从vuex里面得到用户登陆的token
  const hasToken = getToken()
  // 判断是否有token
  if (hasToken) {
    if (to.path === '/login') {
      // 如果已登陆了，步转到首页
      next({ path: '/' })
      NProgress.done()
    } else {
      // 确定用户是否已通过getInfo获得其用户
      const hasName = store.getters.name !== ''

      if (hasName) {
        next()
      } else {
        try {
          // 如果没有得到权限则再去请求后台得到用户信息及权限信息
          await store.dispatch('user/getInfo')
          // 绑定动态路由【后面我们要修改】
          await store.dispatch('menu/getMenus').then(accessRoutes => {
            // 添加动态路由到主路由
            router.addRoutes(accessRoutes)
          })

          next({ ...to, replace: true })
        } catch (error) {
          // 如果出现异常，请求后台重置用户的token 并跳转到登陆页
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    // 如果没有token就跳转到登陆页
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
