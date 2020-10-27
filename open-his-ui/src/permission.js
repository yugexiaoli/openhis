import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
// 加载菜单和权限的js逻辑....

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // 设置标题
  document.title = getPageTitle(to.meta.title)

  // 获取token
  const hasToken = getToken()

  // 判断是否有token
  if (hasToken) {
    if (to.path === '/login') {
      // 如果有token，并且已经登录过，直接去后台
      next({ path: '/' })
      NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        next()
      } else {
        try {
          // 从store中user/getInfo获取用户角色
          const { roles } = await store.dispatch('user/getInfo')

          // 根据用户角色生成菜单路由
          const accessRoutes = await store.dispatch('permission/generateRoutes', roles)

          // 加载路由
          router.addRoutes(accessRoutes)

          // hack method to ensure that addRoutes is complete
          // set the replace: true, so the navigation will not leave a history record
          next({ ...to, replace: true })
        } catch (error) {
          // 如果有异常去到登录页面
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* 没token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // 去登录页
      next()
    } else {
      // 对其他页面没权限也去登录页
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
