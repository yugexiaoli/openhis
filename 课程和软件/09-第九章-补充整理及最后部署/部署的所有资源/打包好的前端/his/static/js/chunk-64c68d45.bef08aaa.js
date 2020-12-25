(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-64c68d45"],{"12c9":function(e,t,a){"use strict";a.d(t,"d",(function(){return l})),a.d(t,"a",(function(){return o})),a.d(t,"f",(function(){return i})),a.d(t,"b",(function(){return n})),a.d(t,"c",(function(){return s})),a.d(t,"e",(function(){return c}));var r=a("b775");function l(e){return Object(r["a"])({url:"/erp/provider/listProviderForPage",method:"get",params:e})}function o(e){return Object(r["a"])({url:"/erp/provider/addProvider",method:"post",params:e})}function i(e){return Object(r["a"])({url:"/erp/provider/updateProvider",method:"put",params:e})}function n(e){return Object(r["a"])({url:"/erp/provider/deleteProviderByIds/"+e,method:"delete"})}function s(e){return Object(r["a"])({url:"/erp/provider/getProviderById/"+e,method:"get"})}function c(){return Object(r["a"])({url:"/erp/provider/selectAllProvider",method:"get"})}},9004:function(e,t,a){"use strict";a.r(t);var r=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container"},[a("el-form",{ref:"queryForm",attrs:{model:e.queryParams,inline:!0,"label-width":"98px"}},[a("el-form-item",{attrs:{label:"供应商名称",prop:"providerName"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"请输入供应商名称",clearable:"",size:"small"},model:{value:e.queryParams.providerName,callback:function(t){e.$set(e.queryParams,"providerName",t)},expression:"queryParams.providerName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"联系人",prop:"contactName"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"请输入联系人",clearable:"",size:"small"},model:{value:e.queryParams.contactName,callback:function(t){e.$set(e.queryParams,"contactName",t)},expression:"queryParams.contactName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"供应商电话",prop:"contactTel"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"请输入供应商电话",clearable:"",size:"small"},model:{value:e.queryParams.contactTel,callback:function(t){e.$set(e.queryParams,"contactTel",t)},expression:"queryParams.contactTel"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"状态",prop:"status"}},[a("el-select",{staticStyle:{width:"180px"},attrs:{placeholder:"可用状态",clearable:"",size:"small"},model:{value:e.queryParams.status,callback:function(t){e.$set(e.queryParams,"status",t)},expression:"queryParams.status"}},e._l(e.statusOptions,(function(e){return a("el-option",{key:e.dictValue,attrs:{label:e.dictLabel,value:e.dictValue}})})),1)],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:e.handleQuery}},[e._v("搜索")]),e._v(" "),a("el-button",{attrs:{type:"primary",icon:"el-icon-refresh",size:"mini"},on:{click:e.resetQuery}},[e._v("重置")])],1)],1),e._v(" "),a("el-row",{staticStyle:{"margin-bottom":"8px"},attrs:{gutter:10}},[a("el-col",{attrs:{span:1.5}},[a("el-button",{attrs:{type:"primary",icon:"el-icon-plus",size:"mini"},on:{click:e.handleAdd}},[e._v("新增")])],1),e._v(" "),a("el-col",{attrs:{span:1.5}},[a("el-button",{attrs:{type:"success",icon:"el-icon-edit",size:"mini",disabled:e.single},on:{click:e.handleUpdate}},[e._v("修改")])],1),e._v(" "),a("el-col",{attrs:{span:1.5}},[a("el-button",{attrs:{type:"danger",icon:"el-icon-delete",size:"mini",disabled:e.multiple},on:{click:e.handleDelete}},[e._v("删除")])],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{border:"",data:e.providerTableList},on:{"selection-change":e.handleSelectionChnage}},[a("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),e._v(" "),a("el-table-column",{attrs:{label:"供应商ID",align:"center",width:"120",prop:"providerId"}}),e._v(" "),a("el-table-column",{attrs:{label:"供应商名称",width:"220",prop:"providerName"}}),e._v(" "),a("el-table-column",{attrs:{label:"联系人",align:"center",prop:"contactName"}}),e._v(" "),a("el-table-column",{attrs:{label:"联系人手机",align:"center",prop:"contactMobile"}}),e._v(" "),a("el-table-column",{attrs:{label:"联系人电话",align:"center",prop:"contactTel"}}),e._v(" "),a("el-table-column",{attrs:{label:"银行账号",width:"200",align:"center",prop:"bankAccount"}}),e._v(" "),a("el-table-column",{attrs:{label:"供应商地址",align:"center",prop:"providerAddress"}}),e._v(" "),a("el-table-column",{attrs:{label:"状态",prop:"status",align:"center",formatter:e.statusFormatter}}),e._v(" "),a("el-table-column",{attrs:{label:"创建时间",width:"180",align:"center",prop:"createTime"}}),e._v(" "),a("el-table-column",{attrs:{label:"操作",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",icon:"el-icon-edit",size:"mini"},on:{click:function(a){return e.handleUpdate(t.row)}}},[e._v("修改")]),e._v(" "),a("el-button",{attrs:{type:"text",icon:"el-icon-delete",size:"mini"},on:{click:function(a){return e.handleDelete(t.row)}}},[e._v("删除")])]}}])})],1),e._v(" "),a("el-pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{"current-page":e.queryParams.pageNum,"page-sizes":[5,10,20,30],"page-size":e.queryParams.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}}),e._v(" "),a("el-dialog",{attrs:{title:e.title,visible:e.open,width:"600px",center:"","append-to-body":""},on:{"update:visible":function(t){e.open=t}}},[a("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"120px"}},[a("el-form-item",{attrs:{"el-form-item":"",label:"供应商名称",prop:"providerName"}},[a("el-input",{attrs:{placeholder:"请输入供应商名称",clearable:"",size:"small"},model:{value:e.form.providerName,callback:function(t){e.$set(e.form,"providerName",t)},expression:"form.providerName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"联系人",prop:"contactName"}},[a("el-input",{attrs:{placeholder:"请输入联系人",clearable:"",size:"small"},model:{value:e.form.contactName,callback:function(t){e.$set(e.form,"contactName",t)},expression:"form.contactName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"手机",prop:"contactMobile"}},[a("el-input",{attrs:{placeholder:"请输入手机",clearable:"",size:"small"},model:{value:e.form.contactMobile,callback:function(t){e.$set(e.form,"contactMobile",t)},expression:"form.contactMobile"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"电话",prop:"contactTel"}},[a("el-input",{attrs:{placeholder:"请输入电话",clearable:"",size:"small"},model:{value:e.form.contactTel,callback:function(t){e.$set(e.form,"contactTel",t)},expression:"form.contactTel"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"银行账号",prop:"bankAccount"}},[a("el-input",{attrs:{placeholder:"请输入银行账号",clearable:"",size:"small"},model:{value:e.form.bankAccount,callback:function(t){e.$set(e.form,"bankAccount",t)},expression:"form.bankAccount"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"地址",prop:"providerAddress"}},[a("el-input",{attrs:{placeholder:"请输入地址",clearable:"",size:"small"},model:{value:e.form.providerAddress,callback:function(t){e.$set(e.form,"providerAddress",t)},expression:"form.providerAddress"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"状态",prop:"status"}},[a("el-radio-group",{model:{value:e.form.status,callback:function(t){e.$set(e.form,"status",t)},expression:"form.status"}},e._l(e.statusOptions,(function(t){return a("el-radio",{key:t.dictValue,attrs:{label:t.dictValue,value:t.dictValue}},[e._v(e._s(t.dictLabel))])})),1)],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{type:"primary"},on:{click:e.handleSubmit}},[e._v("确 定")]),e._v(" "),a("el-button",{on:{click:e.cancel}},[e._v("取 消")])],1)],1)],1)},l=[],o=a("12c9"),i={data:function(){return{loading:!1,ids:[],single:!0,multiple:!0,total:0,providerTableList:[],title:"",open:!1,statusOptions:[],queryParams:{pageNum:1,pageSize:10,providerName:void 0,contactName:void 0,contactTel:void 0,status:void 0},form:{},rules:{providerName:[{required:!0,message:"供应商名称不能为空",trigger:"blur"}],contactName:[{required:!0,message:"联系人不能为空",trigger:"blur"}],contactMobile:[{required:!0,message:"手机不能为空",trigger:"blur"},{pattern:/^1[3|4|5|7|8|9][0-9]\d{8}$/,message:"请输入正确的手机号",trigger:"blur"}],bankAccount:[{required:!0,message:"银行账号不能为空",trigger:"blur"}]}}},created:function(){var e=this;this.getDataByType("sys_normal_disable").then((function(t){e.statusOptions=t.data})),this.getProviderList()},methods:{getProviderList:function(){var e=this;this.loading=!0,Object(o["d"])(this.queryParams).then((function(t){e.providerTableList=t.data,e.total=t.total,e.loading=!1}))},handleQuery:function(){this.getProviderList()},resetQuery:function(){this.resetForm("queryForm"),this.dateRange=[],this.getProviderList()},handleSelectionChnage:function(e){this.ids=e.map((function(e){return e.providerId})),this.single=1!==e.length,this.multiple=!e.length},handleSizeChange:function(e){this.queryParams.pageSize=e,this.getProviderList()},handleCurrentChange:function(e){this.queryParams.pageNum=e,this.getProviderList()},statusFormatter:function(e){return this.selectDictLabel(this.statusOptions,e.status)},handleAdd:function(){this.open=!0,this.reset(),this.title="添加生产供应商信息"},handleUpdate:function(e){var t=this;this.title="修改生产供应商信息";var a=e.providerId||this.ids;this.open=!0,this.reset(),this.loading=!0,Object(o["c"])(a).then((function(e){t.form=e.data,t.loading=!1}))},handleDelete:function(e){var t=e.providerId||this.ids,a=this;a.$confirm("此操作将永久删除该生产供应商数据, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){a.loading=!0,Object(o["b"])(t).then((function(e){a.loading=!1,a.msgSuccess("删除成功"),a.getProviderList()})).catch((function(){a.msgError("删除失败"),a.loading=!1}))})).catch((function(){a.msgError("删除已取消"),a.loading=!1}))},handleSubmit:function(){var e=this;this.$refs["form"].validate((function(t){if(t){e.loading=!0;var a=e;void 0===a.form.providerId?Object(o["a"])(a.form).then((function(e){a.msgSuccess("保存成功"),a.loading=!1,a.getProviderList(),a.open=!1})).catch((function(){a.loading=!1})):Object(o["f"])(a.form).then((function(e){a.msgSuccess("修改成功"),a.loading=!1,a.getProviderList(),a.open=!1})).catch((function(){a.loading=!1}))}}))},cancel:function(){this.open=!1,this.title=""},reset:function(){this.resetForm("form"),this.form={providerId:void 0,providerName:void 0,contactName:void 0,contactMobile:void 0,contactTel:void 0,bankAccount:void 0,providerAddress:void 0,status:"0"}}}},n=i,s=a("2877"),c=Object(s["a"])(n,r,l,!1,null,null,null);t["default"]=c.exports}}]);