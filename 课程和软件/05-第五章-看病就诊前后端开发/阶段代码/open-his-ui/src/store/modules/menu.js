import { asyncRoutes, constantRoutes, lastRoute } from '@/router/index'
import { getMenus } from '@/api/user'
const state = {
  routes: []
}
const mutations = {
  SET_ROUTES: (state, routes) => {
    state.routes = routes
  }
}

// 动态菜单还是定义在前端，后台只会返回有权限的菜单列表，通过遍历服务端的菜单数据，没有的将对于菜单进行隐藏
// 这样的好处是服务端无需返回前端菜单相关结构，并且菜单显示又可以通过服务端来控制，进行菜单的动态控制
// 前端新增页面也无需先通过服务端进行菜单添加，遵循了前后端分离原则
function generateRoutes(routes, srvMenus) {
  for (let i = 0; i < routes.length; i++) {
    const routeItem = routes[i]
    var showItem = false
    for (let j = 0; j < srvMenus.length; j++) {
      const srvItem = srvMenus[j]

      // 前后端数据通过 serPath 属性来匹配
      if (routeItem.name !== undefined && routeItem.name === srvItem.serPath && srvItem.show === true) {
        showItem = true // 此时当前的路由要显示
        routes[i]['hidden'] = false
        break
      }
    }
    if (showItem === false) {
      routes[i]['hidden'] = true
    }

    if (routeItem['children'] !== undefined && routeItem['children'].length > 0) {
      generateRoutes(routes[i]['children'], srvMenus)
    }
  }
}

const actions = {
  getMenus({ commit }, roles) {
    return new Promise(resolve => {
      // 查询所端的菜单数据
      getMenus(state.token).then(async function(res) {
        const srvMenus = res.data
        var pushRouter = asyncRoutes
        generateRoutes(pushRouter, srvMenus)
        const routeArr = []
        routeArr.push(...constantRoutes)
        routeArr.push(...pushRouter)
        routeArr.push(...lastRoute)
        commit('SET_ROUTES', routeArr)// 放到vuex里面
        resolve(routeArr)
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
