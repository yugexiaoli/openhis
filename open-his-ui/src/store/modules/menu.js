import { getMenus } from '@/api/user'
import { constantRoutes, asyncRoutes, lastRoute } from '@/router/index'
// 匹配动态路由名字和后台菜单的路径
const state = {
  routes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.routes = routes
  }
}
// 调用方法改变pushRouter动态路由 pushRouter写死的路由 serMenu后台传的路由
// 动态菜单还是定义在前端，后台只会返回有权限的菜单列表，通过遍历服务端的菜单数据，没有的将对于菜单进行隐藏
// 这样的好处是服务端无需返回前端菜单相关结构，并且菜单显示又可以通过服务端来控制，进行菜单的动态控制
// 前端新增页面也无需先通过服务端进行菜单添加，遵循了前后端分离原则
function generationMenu(pushRouters, serMenus) {
  for (const pushRouter of pushRouters) {
    var isshow = false
    for (const serMenu of serMenus) {
      if (pushRouter.name !== undefined && pushRouter.name === serMenu.serPath && serMenu.show === true) {
        // 写死的菜单路由有这个数据库返回的菜单
        isshow = true
        pushRouter['hidden'] = false
      }
      if (isshow === false) {
        pushRouter['hidden'] = true
      }
      if (pushRouter['children'] !== undefined && pushRouter['children'].length > 0) {
        // 有子菜单，再判断子菜单显示不显示 再递归一次这个generationMenu方法
        generationMenu(pushRouter['children'], serMenus)
      }
    }
  }
}

const actions = {
  // 查询后端菜单数据
  getMenus({ commit, state }) {
    return new Promise((resolve, reject) => {
      getMenus(state.token).then(response => {
        // 后端返回的所有菜单
        const menuTreeVos = response.menuTreeVos
        // 动态路由
        const pushRouter = asyncRoutes

        // 调用方法改变pushRouter动态路由的隐藏显示hidden属性
        generationMenu(pushRouter, menuTreeVos)

        const routerArr = []
        // 合并数组用。。。
        routerArr.push(...constantRoutes)
        routerArr.push(...pushRouter)
        routerArr.push(...lastRoute)

        commit('SET_ROUTES', routerArr)
        resolve(routerArr)
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
