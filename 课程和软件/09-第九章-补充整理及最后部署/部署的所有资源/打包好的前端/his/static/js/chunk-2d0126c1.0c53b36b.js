(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0126c1"],{"783c":function(e,t,r){"use strict";r.d(t,"d",(function(){return l})),r.d(t,"a",(function(){return o})),r.d(t,"f",(function(){return n})),r.d(t,"b",(function(){return s})),r.d(t,"c",(function(){return i})),r.d(t,"e",(function(){return c}));var a=r("b775");function l(e){return Object(a["a"])({url:"/erp/producter/listProducterForPage",method:"get",params:e})}function o(e){return Object(a["a"])({url:"/erp/producter/addProducter",method:"post",params:e})}function n(e){return Object(a["a"])({url:"/erp/producter/updateProducter",method:"put",params:e})}function s(e){return Object(a["a"])({url:"/erp/producter/deleteProducterByIds/"+e,method:"delete"})}function i(e){return Object(a["a"])({url:"/erp/producter/getProducterById/"+e,method:"get"})}function c(){return Object(a["a"])({url:"/erp/producter/selectAllProducter",method:"get"})}},fe60:function(e,t,r){"use strict";r.r(t);var a=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"app-container"},[r("el-form",{ref:"queryForm",attrs:{model:e.queryParams,inline:!0,"label-width":"98px"}},[r("el-form-item",{attrs:{label:"厂家名称",prop:"producterName"}},[r("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"请输入厂家名称",clearable:"",size:"small"},model:{value:e.queryParams.producterName,callback:function(t){e.$set(e.queryParams,"producterName",t)},expression:"queryParams.producterName"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"关键字",prop:"keywords"}},[r("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"请输入关键字",clearable:"",size:"small"},model:{value:e.queryParams.keywords,callback:function(t){e.$set(e.queryParams,"keywords",t)},expression:"queryParams.keywords"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"厂家电话",prop:"producterTel"}},[r("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"请输入厂家电话",clearable:"",size:"small"},model:{value:e.queryParams.producterTel,callback:function(t){e.$set(e.queryParams,"producterTel",t)},expression:"queryParams.producterTel"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"状态",prop:"status"}},[r("el-select",{staticStyle:{width:"180px"},attrs:{placeholder:"可用状态",clearable:"",size:"small"},model:{value:e.queryParams.status,callback:function(t){e.$set(e.queryParams,"status",t)},expression:"queryParams.status"}},e._l(e.statusOptions,(function(e){return r("el-option",{key:e.dictValue,attrs:{label:e.dictLabel,value:e.dictValue}})})),1)],1),e._v(" "),r("el-form-item",{attrs:{label:"创建时间"}},[r("el-date-picker",{staticStyle:{width:"240px"},attrs:{size:"small","value-format":"yyyy-MM-dd",type:"daterange","range-separator":"-","start-placeholde":"开始日期","end-placeholde":"结束日期"},model:{value:e.dateRange,callback:function(t){e.dateRange=t},expression:"dateRange"}})],1),e._v(" "),r("el-form-item",[r("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:e.handleQuery}},[e._v("搜索")]),e._v(" "),r("el-button",{attrs:{type:"primary",icon:"el-icon-refresh",size:"mini"},on:{click:e.resetQuery}},[e._v("重置")])],1)],1),e._v(" "),r("el-row",{staticStyle:{"margin-bottom":"8px"},attrs:{gutter:10}},[r("el-col",{attrs:{span:1.5}},[r("el-button",{attrs:{type:"primary",icon:"el-icon-plus",size:"mini"},on:{click:e.handleAdd}},[e._v("新增")])],1),e._v(" "),r("el-col",{attrs:{span:1.5}},[r("el-button",{attrs:{type:"success",icon:"el-icon-edit",size:"mini",disabled:e.single},on:{click:e.handleUpdate}},[e._v("修改")])],1),e._v(" "),r("el-col",{attrs:{span:1.5}},[r("el-button",{attrs:{type:"danger",icon:"el-icon-delete",size:"mini",disabled:e.multiple},on:{click:e.handleDelete}},[e._v("删除")])],1)],1),e._v(" "),r("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{border:"",data:e.producterTableList},on:{"selection-change":e.handleSelectionChnage}},[r("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),e._v(" "),r("el-table-column",{attrs:{label:"厂家ID",align:"center",width:"120",prop:"producterId"}}),e._v(" "),r("el-table-column",{attrs:{label:"厂家名称",width:"280",prop:"producterName"}}),e._v(" "),r("el-table-column",{attrs:{label:"厂家编码",align:"center",prop:"producterCode"}}),e._v(" "),r("el-table-column",{attrs:{label:"联系人",align:"center",prop:"producterPerson"}}),e._v(" "),r("el-table-column",{attrs:{label:"电话",align:"center",prop:"producterTel"}}),e._v(" "),r("el-table-column",{attrs:{label:"关键字",align:"center",prop:"keywords"}}),e._v(" "),r("el-table-column",{attrs:{label:"状态",prop:"status",align:"center",formatter:e.statusFormatter}}),e._v(" "),r("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime"}}),e._v(" "),r("el-table-column",{attrs:{label:"操作",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-button",{attrs:{type:"text",icon:"el-icon-edit",size:"mini"},on:{click:function(r){return e.handleUpdate(t.row)}}},[e._v("修改")]),e._v(" "),r("el-button",{attrs:{type:"text",icon:"el-icon-delete",size:"mini"},on:{click:function(r){return e.handleDelete(t.row)}}},[e._v("删除")])]}}])})],1),e._v(" "),r("el-pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{"current-page":e.queryParams.pageNum,"page-sizes":[5,10,20,30],"page-size":e.queryParams.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}}),e._v(" "),r("el-dialog",{attrs:{title:e.title,visible:e.open,width:"600px",center:"","append-to-body":""},on:{"update:visible":function(t){e.open=t}}},[r("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"120px"}},[r("el-form-item",{attrs:{"el-form-item":"",label:"厂家名称",prop:"producterName"}},[r("el-input",{attrs:{placeholder:"请输入厂家名称",clearable:"",size:"small"},model:{value:e.form.producterName,callback:function(t){e.$set(e.form,"producterName",t)},expression:"form.producterName"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"厂家编码",prop:"producterCode"}},[r("el-input",{attrs:{placeholder:"请输入厂家编码",clearable:"",size:"small"},model:{value:e.form.producterCode,callback:function(t){e.$set(e.form,"producterCode",t)},expression:"form.producterCode"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"联系人",prop:"producterPerson"}},[r("el-input",{attrs:{placeholder:"请输入联系人",clearable:"",size:"small"},model:{value:e.form.producterPerson,callback:function(t){e.$set(e.form,"producterPerson",t)},expression:"form.producterPerson"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"电话",prop:"producterTel"}},[r("el-input",{attrs:{placeholder:"请输入电话",clearable:"",size:"small"},model:{value:e.form.producterTel,callback:function(t){e.$set(e.form,"producterTel",t)},expression:"form.producterTel"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"关键字",prop:"keywords"}},[r("el-input",{attrs:{placeholder:"请输入关键字",clearable:"",size:"small"},model:{value:e.form.keywords,callback:function(t){e.$set(e.form,"keywords",t)},expression:"form.keywords"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"地址",prop:"producterAddress"}},[r("el-input",{attrs:{placeholder:"请输入地址",clearable:"",size:"small"},model:{value:e.form.producterAddress,callback:function(t){e.$set(e.form,"producterAddress",t)},expression:"form.producterAddress"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"状态",prop:"status"}},[r("el-radio-group",{model:{value:e.form.status,callback:function(t){e.$set(e.form,"status",t)},expression:"form.status"}},e._l(e.statusOptions,(function(t){return r("el-radio",{key:t.dictValue,attrs:{label:t.dictValue,value:t.dictValue}},[e._v(e._s(t.dictLabel))])})),1)],1)],1),e._v(" "),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{attrs:{type:"primary"},on:{click:e.handleSubmit}},[e._v("确 定")]),e._v(" "),r("el-button",{on:{click:e.cancel}},[e._v("取 消")])],1)],1)],1)},l=[],o=r("783c"),n={data:function(){return{loading:!1,ids:[],single:!0,multiple:!0,total:0,producterTableList:[],title:"",open:!1,statusOptions:[],typeOptions:[],dateRange:[],queryParams:{pageNum:1,pageSize:10,producterName:void 0,producterCode:void 0,producterTel:void 0,status:void 0,keywords:void 0},form:{},rules:{producterName:[{required:!0,message:"厂家名称不能为空",trigger:"blur"}],producterTel:[{required:!0,message:"联系电话不能为空",trigger:"blur"},{pattern:/^1[3|4|5|7|8|9][0-9]\d{8}$/,message:"请输入正确的手机号",trigger:"blur"}],keywords:[{required:!0,message:"查询关键字不能为空",trigger:"blur"}]}}},created:function(){var e=this;this.getDataByType("sys_normal_disable").then((function(t){e.statusOptions=t.data})),this.getProducterList()},methods:{getProducterList:function(){var e=this;this.loading=!0,Object(o["d"])(this.addDateRange(this.queryParams,this.dateRange)).then((function(t){e.producterTableList=t.data,e.total=t.total,e.loading=!1}))},handleQuery:function(){this.getProducterList()},resetQuery:function(){this.resetForm("queryForm"),this.dateRange=[],this.getProducterList()},handleSelectionChnage:function(e){this.ids=e.map((function(e){return e.producterId})),this.single=1!==e.length,this.multiple=!e.length},handleSizeChange:function(e){this.queryParams.pageSize=e,this.getProducterList()},handleCurrentChange:function(e){this.queryParams.pageNum=e,this.getProducterList()},statusFormatter:function(e){return this.selectDictLabel(this.statusOptions,e.status)},handleAdd:function(){this.open=!0,this.reset(),this.title="添加生产厂家信息"},handleUpdate:function(e){var t=this;this.title="修改生产厂家信息";var r=e.producterId||this.ids;this.open=!0,this.reset(),this.loading=!0,Object(o["c"])(r).then((function(e){t.form=e.data,t.loading=!1}))},handleDelete:function(e){var t=e.producterId||this.ids,r=this;r.$confirm("此操作将永久删除该生产厂家数据, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){r.loading=!0,Object(o["b"])(t).then((function(e){r.loading=!1,r.msgSuccess("删除成功"),r.getProducterList()})).catch((function(){r.msgError("删除失败"),r.loading=!1}))})).catch((function(){r.msgError("删除已取消"),r.loading=!1}))},handleSubmit:function(){var e=this;this.$refs["form"].validate((function(t){if(t){e.loading=!0;var r=e;void 0===r.form.producterId?Object(o["a"])(r.form).then((function(e){r.msgSuccess("保存成功"),r.loading=!1,r.getProducterList(),r.open=!1})).catch((function(){r.loading=!1})):Object(o["f"])(r.form).then((function(e){r.msgSuccess("修改成功"),r.loading=!1,r.getProducterList(),r.open=!1})).catch((function(){r.loading=!1}))}}))},cancel:function(){this.open=!1,this.title=""},reset:function(){this.resetForm("form"),this.form={producterId:void 0,producterName:void 0,producterCode:void 0,producterTel:void 0,keywords:void 0,status:"0"}}}},s=n,i=r("2877"),c=Object(i["a"])(s,a,l,!1,null,null,null);t["default"]=c.exports}}]);