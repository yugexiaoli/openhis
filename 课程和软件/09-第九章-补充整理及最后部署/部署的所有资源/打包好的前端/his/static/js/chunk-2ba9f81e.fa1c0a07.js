(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2ba9f81e"],{"14c9":function(t,e,r){"use strict";r.d(e,"f",(function(){return a})),r.d(e,"e",(function(){return i})),r.d(e,"a",(function(){return s})),r.d(e,"b",(function(){return o})),r.d(e,"g",(function(){return l})),r.d(e,"c",(function(){return u})),r.d(e,"d",(function(){return c}));var n=r("b775");function a(t){return Object(n["a"])({url:"/doctor/registration/listDeptForScheduling",method:"get",params:t})}function i(t){return Object(n["a"])({url:"/doctor/registration/getPatientByIdCard/"+t,method:"get"})}function s(t){return Object(n["a"])({url:"/doctor/registration/addRegistration",method:"post",data:t})}function o(t){return Object(n["a"])({url:"/doctor/registration/collectFee/"+t,method:"post"})}function l(t){return Object(n["a"])({url:"/doctor/registration/queryRegistrationForPage",method:"get",params:t})}function u(t){return Object(n["a"])({url:"/doctor/registration/doInvalid/"+t,method:"post"})}function c(t){return Object(n["a"])({url:"/doctor/registration/doReturn/"+t,method:"post"})}},4519:function(t,e,r){},"601a":function(t,e,r){"use strict";var n=r("4519"),a=r.n(n);a.a},b7d3:function(t,e,r){"use strict";r.r(e);var n=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"app-container"},[r("el-form",{ref:"queryForm",attrs:{model:t.queryParams,inline:!0,"label-width":"68px"}},[r("el-form-item",{attrs:{label:"所属部门",prop:"deptId"}},[r("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"请选择所属部门",clearable:"",size:"small"},model:{value:t.queryParams.deptId,callback:function(e){t.$set(t.queryParams,"deptId",e)},expression:"queryParams.deptId"}},t._l(t.deptList,(function(t){return r("el-option",{key:t.deptId,attrs:{label:t.deptName,value:t.deptId}})})),1)],1),t._v(" "),r("el-form-item",{attrs:{label:"患者名称",prop:"patientName"}},[r("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"请输入患者名称",clearable:"",size:"small"},model:{value:t.queryParams.patientName,callback:function(e){t.$set(t.queryParams,"patientName",e)},expression:"queryParams.patientName"}})],1),t._v(" "),r("el-form-item",{attrs:{label:"挂号类型",prop:"schedulingType"}},[r("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"可用状态",clearable:"",size:"small"},model:{value:t.queryParams.schedulingType,callback:function(e){t.$set(t.queryParams,"schedulingType",e)},expression:"queryParams.schedulingType"}},t._l(t.schedulingTypeOptions,(function(t){return r("el-option",{key:t.dictValue,attrs:{label:t.dictLabel,value:t.dictValue}})})),1)],1),t._v(" "),r("el-form-item",{attrs:{label:"挂号时段",prop:"subsectionType"}},[r("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"挂号时段",clearable:"",size:"small"},model:{value:t.queryParams.subsectionType,callback:function(e){t.$set(t.queryParams,"subsectionType",e)},expression:"queryParams.subsectionType"}},t._l(t.subsectionTypeOptions,(function(t){return r("el-option",{key:t.dictValue,attrs:{label:t.dictLabel,value:t.dictValue}})})),1)],1),t._v(" "),r("el-form-item",{attrs:{label:"挂号状态",prop:"regStatus"}},[r("el-select",{staticStyle:{width:"200px"},attrs:{placeholder:"挂号状态",clearable:"",size:"small"},model:{value:t.queryParams.regStatus,callback:function(e){t.$set(t.queryParams,"regStatus",e)},expression:"queryParams.regStatus"}},t._l(t.regStatusOptions,(function(t){return r("el-option",{key:t.dictValue,attrs:{label:t.dictLabel,value:t.dictValue}})})),1)],1),t._v(" "),r("el-form-item",{attrs:{label:"挂号时间",prop:"queryDate"}},[r("el-date-picker",{staticStyle:{width:"200px"},attrs:{size:"small","value-format":"yyyy-MM-dd",type:"date"},model:{value:t.queryParams.queryDate,callback:function(e){t.$set(t.queryParams,"queryDate",e)},expression:"queryParams.queryDate"}})],1),t._v(" "),r("el-form-item",[r("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:t.handleQuery}},[t._v("搜索")]),t._v(" "),r("el-button",{attrs:{type:"normal",icon:"el-icon-refresh",size:"mini"},on:{click:t.resetQuery}},[t._v("重置")])],1)],1),t._v(" "),r("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.loading,expression:"loading"}],attrs:{border:"",data:t.regTableList}},[r("el-table-column",{attrs:{type:"expand"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("el-form",{staticClass:"demo-table-expand",attrs:{"label-position":"right",inline:""}},[r("el-form-item",{attrs:{label:"挂号ID:"}},[r("span",[t._v(t._s(e.row.regId))])]),t._v(" "),r("el-form-item",{attrs:{label:"挂号员:"}},[r("span",[t._v(t._s(e.row.createBy))])]),t._v(" "),r("el-form-item",{attrs:{label:"创建时间:"}},[r("span",[t._v(t._s(e.row.createTime))])])],1)]}}])}),t._v(" "),r("el-table-column",{attrs:{label:"患者姓名",align:"center",prop:"patientName"}}),t._v(" "),r("el-table-column",{attrs:{label:"挂号科室",align:"center",prop:"deptId",formatter:t.deptIdFormatter}}),t._v(" "),r("el-table-column",{attrs:{label:"接诊医生",align:"center",prop:"doctorName"}}),t._v(" "),r("el-table-column",{attrs:{label:"挂号费用",align:"center",prop:"regItemAmount"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("span",[t._v(t._s(t._f("rounding")(e.row.regItemAmount)))])]}}])}),t._v(" "),r("el-table-column",{attrs:{label:"流水编号",align:"center",prop:"regNumber"}}),t._v(" "),r("el-table-column",{attrs:{label:"状态",align:"center",prop:"regStatus",formatter:t.regStatusFormatter}}),t._v(" "),r("el-table-column",{attrs:{label:"就诊日期",align:"center",prop:"visitDate"}}),t._v(" "),r("el-table-column",{attrs:{label:"挂号类型",align:"center",prop:"schedulingType",formatter:t.schedulingTypeFormatter}}),t._v(" "),r("el-table-column",{attrs:{label:"挂号时段",align:"center",prop:"subsectionType",formatter:t.subsectionTypeFormatter}}),t._v(" "),r("el-table-column",{attrs:{label:"操作",align:"center",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return["0"==e.row.regStatus?r("el-button",{attrs:{type:"success",icon:"el-icon-check",size:"mini"},on:{click:function(r){return t.handleCollect(e.row)}}},[t._v("收费")]):t._e(),t._v(" "),"1"==e.row.regStatus?r("el-button",{attrs:{type:"danger",icon:"el-icon-close",size:"mini"},on:{click:function(r){return t.handleReturn(e.row)}}},[t._v("退号")]):t._e(),t._v(" "),"0"==e.row.regStatus?r("el-button",{attrs:{type:"danger",icon:"el-icon-check",size:"mini"},on:{click:function(r){return t.handleInvalid(e.row)}}},[t._v("作废")]):t._e()]}}])})],1),t._v(" "),r("el-pagination",{directives:[{name:"show",rawName:"v-show",value:t.total>0,expression:"total>0"}],attrs:{"current-page":t.queryParams.pageNum,"page-sizes":[5,10,20,30],"page-size":t.queryParams.pageSize,layout:"total,sizes,prev,pager,next,jumper",total:t.total},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}})],1)},a=[],i=r("fcb7"),s=r("c0c7"),o=r("14c9"),l={filters:{rounding:function(t){return t.toFixed(2)}},data:function(){return{loading:!1,total:0,regTableList:[],deptList:[],userList:[],regStatusOptions:[],schedulingTypeOptions:[],subsectionTypeOptions:[],queryParams:{pageNum:1,pageSize:10,patientName:void 0,deptId:void 0,schedulingType:void 0,subsectionType:void 0,visitDate:void 0,queryDate:new Date}}},created:function(){var t=this;this.getDataByType("his_scheduling_type").then((function(e){t.schedulingTypeOptions=e.data})),this.getDataByType("his_subsection_type").then((function(e){t.subsectionTypeOptions=e.data})),this.getDataByType("his_registration_status").then((function(e){t.regStatusOptions=e.data})),Object(i["e"])().then((function(e){t.deptList=e.data})),Object(s["f"])().then((function(e){t.userList=e.data})),this.listRegistration()},methods:{listRegistration:function(){var t=this;this.loading=!0,this.queryParams.visitDate=this.moment(this.queryParams.queryDate).format("YYYY-MM-DD"),Object(o["g"])(this.queryParams).then((function(e){t.regTableList=e.data,t.total=e.total,t.loading=!1})).catch((function(){t.loading=!1}))},handleSizeChange:function(t){this.queryParams.pageSize=t,this.listRegistration()},handleCurrentChange:function(t){this.queryParams.pageNum=t,this.listRegistration()},handleQuery:function(){this.listRegistration()},resetQuery:function(){this.resetForm("queryForm"),this.listRegistration()},deptIdFormatter:function(t,e){var r;return this.deptList.filter((function(e){e.deptId===t.deptId&&(r=e.deptName)})),r},schedulingTypeFormatter:function(t,e){return this.selectDictLabel(this.schedulingTypeOptions,t.schedulingType)},subsectionTypeFormatter:function(t,e){return this.selectDictLabel(this.subsectionTypeOptions,t.subsectionType)},regStatusFormatter:function(t,e){return this.selectDictLabel(this.regStatusOptions,t.regStatus)},handleCollect:function(t){var e=this;this.loading=!0,Object(o["b"])(t.regId).then((function(t){e.loading=!1,e.msgSuccess("收费成功"),e.listRegistration()})).catch((function(){e.msgError("收费失败")}))},handleInvalid:function(t){var e=this;this.loading=!0,Object(o["c"])(t.regId).then((function(t){e.loading=!1,e.msgSuccess("作废成功"),e.listRegistration()})).catch((function(){e.msgError("作废失败")}))},handleReturn:function(t){var e=this;this.loading=!0,Object(o["d"])(t.regId).then((function(t){e.loading=!1,e.msgSuccess("退号成功"),e.listRegistration()})).catch((function(){e.msgError("退号失败")}))}}},u=l,c=(r("601a"),r("2877")),d=Object(c["a"])(u,n,a,!1,null,"6ae85fae",null);e["default"]=d.exports},c0c7:function(t,e,r){"use strict";r.d(e,"d",(function(){return a})),r.d(e,"a",(function(){return i})),r.d(e,"g",(function(){return s})),r.d(e,"b",(function(){return o})),r.d(e,"c",(function(){return l})),r.d(e,"e",(function(){return u})),r.d(e,"f",(function(){return c}));var n=r("b775");function a(t){return Object(n["a"])({url:"/system/user/listUserForPage",method:"get",params:t})}function i(t){return Object(n["a"])({url:"/system/user/addUser",method:"post",params:t})}function s(t){return Object(n["a"])({url:"/system/user/updateUser",method:"put",params:t})}function o(t){return Object(n["a"])({url:"/system/user/deleteUserByIds/"+t,method:"delete"})}function l(t){return Object(n["a"])({url:"/system/user/getUserById/"+t,method:"get"})}function u(t){return Object(n["a"])({url:"/system/user/resetPwd/"+t,method:"post"})}function c(){return Object(n["a"])({url:"/system/user/selectAllUser",method:"get"})}},fcb7:function(t,e,r){"use strict";r.d(e,"d",(function(){return a})),r.d(e,"a",(function(){return i})),r.d(e,"f",(function(){return s})),r.d(e,"b",(function(){return o})),r.d(e,"c",(function(){return l})),r.d(e,"e",(function(){return u}));var n=r("b775");function a(t){return Object(n["a"])({url:"/system/dept/listDeptForPage",method:"get",params:t})}function i(t){return Object(n["a"])({url:"/system/dept/addDept",method:"post",params:t})}function s(t){return Object(n["a"])({url:"/system/dept/updateDept",method:"put",params:t})}function o(t){return Object(n["a"])({url:"/system/dept/deleteDeptByIds/"+t,method:"delete"})}function l(t){return Object(n["a"])({url:"/system/dept/getDeptById/"+t,method:"get"})}function u(){return Object(n["a"])({url:"/system/dept/selectAllDept",method:"get"})}}}]);