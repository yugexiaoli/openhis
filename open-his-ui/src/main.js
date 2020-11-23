import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './styles/element-variables.scss'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log
import '@/utils/global' // 自定义全局js

import * as filters from './filters' // global filters

import VueCodeMirror from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
Vue.use(VueCodeMirror)

// 引入自定义的
import { resetForm, addDateRange, selectDictLabel, handleTree, selectDeptLabel, selectProducterLabel } from '@/utils/his-utils'
import { getDataByType } from '@/api/system/dict/data'
// 全局方法挂载
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDeptLabel = selectDeptLabel
Vue.prototype.getDataByType = getDataByType // 加载全局的根据字典类型查询字典的方法[基本每个页面都要使用]
Vue.prototype.handleTree = handleTree // 构造树
Vue.prototype.selectProducterLabel = selectProducterLabel
// 挂载全局消息框
Vue.prototype.msgSuccess = function(msg) {
  this.$message({ showClose: true, message: msg, type: 'success' })
}
Vue.prototype.msgError = function(msg) {
  this.$message({ showClose: true, message: msg, type: 'error' })
}
Vue.prototype.msgInfo = function(msg) {
  this.$message.info(msg)
}

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // 如果使用中文，无需设置，请删除
})

// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
