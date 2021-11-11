<template>
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="98px">
      <el-form-item label="患者姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入患者姓名"
          clearable
          size="small"
          style="width:180px"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
          size="small"
          style="width:180px"
        />
      </el-form-item>
      <el-form-item label="身份证号" prop="idCard">
        <el-input
          v-model="queryParams.idCard"
          placeholder="请输身份证号"
          clearable
          size="small"
          style="width:180px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询条件结束 -->
    <!-- 表格工具按钮开始 -->
    <el-row :gutter="10" style="margin-bottom: 8px;">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" :disabled="single" @click="handleViewFile">查看档案</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleViewCareHistory">查看就诊病例</el-button>
      </el-col>
    </el-row>
    <!-- 表格工具按钮结束 -->

    <!-- 数据表格开始 -->
    <el-table v-loading="loading" border :data="patientTableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="电话" align="center" prop="phone" />
      <el-table-column label="身份证号" align="center" prop="idCard" />
      <el-table-column label="出生年月" align="center" prop="birthday">
        <template slot-scope="scope">
          <span>{{ scope.row.birthday.substring(0,10) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="年龄" align="center" prop="phone" :formatter="calculateAgeFormatter" />
      <el-table-column label="性别" prop="sex" align="center" :formatter="sexFormatter" />
      <el-table-column label="信息状态" prop="isFinal" align="center" :formatter="isFinalFormatter" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
    </el-table>
    <!-- 数据表格结束 -->
    <!-- 分页控件开始 -->
    <el-pagination
      v-show="total>0"
      :current-page="queryParams.pageNum"
      :page-sizes="[5, 10, 20, 30]"
      :page-size="queryParams.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <!-- 分页控件结束 -->

    <!-- 查看患者档案弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="fileOpen"
      width="1000px"
      center
      append-to-body
    >
      <el-form label-position="left" label-width="120px" inline class="demo-table-expand">
        <el-form-item label="ID:">
          <span v-text="patientObj.patientId" />
        </el-form-item>
        <el-form-item label="姓名:">
          <span v-text="patientObj.name" />
        </el-form-item>
        <el-form-item label="身份证号:">
          <span v-text="patientObj.idCard" />
        </el-form-item>
        <el-form-item label="电话:">
          <span v-text="patientObj.phone" />
        </el-form-item>
        <el-form-item label="患者性别:">
          <span v-text="patientObj.sex" />
        </el-form-item>
        <el-form-item label="出生年月:">
          <span v-text="patientObj.birthday" />
        </el-form-item>
        <el-form-item label="地址信息:">
          <span v-text="patientObj.address" />
        </el-form-item>
        <el-form-item label="过敏信息:">
          <span v-text="patientObj.allergyInfo" />
        </el-form-item>
        <el-form-item label="是否完善信息:">
          <span v-text="patientObj.isFinal" />
        </el-form-item>
        <el-form-item label="最后登录ip:">
          <span v-text="patientObj.lastLoginIp" />
        </el-form-item>
        <el-form-item label="最后登录时间:">
          <span v-text="patientObj.lastLoginTime" />
        </el-form-item>
        <el-form-item label="创建时间:">
          <span v-text="patientObj.createTime" />
        </el-form-item>
        <el-form-item label="更新时间:">
          <span v-text="patientObj.updateTime" />
        </el-form-item>
        <el-form-item label="紧急联系人:">
          <span v-text="patientFileObj.emergencyContactName" />
        </el-form-item>
        <el-form-item label="紧急联系人电话:">
          <span v-text="patientFileObj.emergencyContactPhone" />
        </el-form-item>
        <el-form-item label="关系:">
          <span v-text="patientFileObj.emergencyContactRelation" />
        </el-form-item>
        <el-form-item label="左耳听力:">
          <span v-text="patientFileObj.leftEarHearing" />
        </el-form-item>
        <el-form-item label="右耳听力:">
          <span v-text="patientFileObj.rightEarHearing" />
        </el-form-item>
        <el-form-item label="左眼视力:">
          <span v-text="patientFileObj.leftVision" />
        </el-form-item>
        <el-form-item label="右眼视力:">
          <span v-text="patientFileObj.rightVision" />
        </el-form-item>
        <el-form-item label="身高:">
          <span v-text="patientFileObj.height" />
        </el-form-item>
        <el-form-item label="体重:">
          <span v-text="patientFileObj.weight" />
        </el-form-item>
        <el-form-item label="血型:">
          <span v-text="patientFileObj.bloodType" />
        </el-form-item>
        <el-form-item label="个人史:">
          <span v-text="patientFileObj.personalInfo" />
        </el-form-item>
        <el-form-item label="家族史:">
          <span v-text="patientFileObj.familyInfo" />
        </el-form-item>
        <el-form-item label="档案创建时间:">
          <span v-text="patientFileObj.createTime" />
        </el-form-item>
        <el-form-item label="档案更新时间:">
          <span v-text="patientFileObj.updateTime" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelFile">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 查看患者档案弹出层结束 -->
    <!-- 查看历史病例弹出层开始 -->
    <!-- 查看患者档案弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="careHistoryOpen"
      width="1000px"
      center
      append-to-body
    >
      <div v-if="careHistoryList.length === 0" style="text-align:center;color:red">此患者暂无历史病例</div>
      <div v-else style="text-align:center">
        <el-collapse accordion>
          <el-collapse-item v-for="ch in careHistoryList" :key="ch.Id" style="margin-left:50px">
            <template slot="title">
              科室：<span style="margin-right:15px;" v-text=" '【'+ch.deptName+'】'" />  就诊时间：<span v-text="ch.careTime" />
            </template>
            <div style="text-align:left;margin-left:25px;">
              <div>医生姓名：{{ ch.userName }}</div>
              <div>接诊类型：{{ ch.receiveType | receiveTypeFormater }}</div>
              <div>是否传染：{{ ch.isContagious |isContagiousFormatter }}</div>
              <div>发病日期：{{ ch.caseDate }}</div>
              <div>主诉：{{ ch.caseTitle }}</div>
              <div>诊断信息：{{ ch.caseResult }}</div>
              <div>医生建议：{{ ch.doctorTips }}</div>
              <div>备注：{{ ch.remark }}</div>
              <div>挂号单号：{{ ch.regId }}</div>
              <div>病例单号：{{ ch.chId }}</div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-dialog>
    <!-- 查看历史病例弹出层结束 -->
    <!-- {{ careHistoryList }} -->
  </div>
</template>
<script>
let that
// 引入api
import { getDataByType } from '@/api/system/dict/data'
import { getPatientAllMessageByPatientId } from '@/api/doctor/care/care'
import { listPatientForPage, getPatientById, getPatientFileById } from '@/api/doctor/patient/patient'
export default {
  filters: {

    // 接诊类型翻译
    receiveTypeFormater(receiveType) {
      return that.receiveTypeOptions.filter((item) => {
        if (item.dictValue === receiveType) {
          return item.dictLabel
        }
      })[0].dictLabel
    },
    // 是否传染翻译
    isContagiousFormatter(isContagious) {
      return that.isContagiousOptions.filter((item) => {
        if (item.dictValue === isContagious) {
          return item.dictLabel
        }
      })[0].dictLabel
    }
  },
  // 定义页面数据
  data() {
    return {
      // 接诊类型数据
      receiveTypeOptions: [],
      // 是否传染
      isContagiousOptions: [],
      // 是否启用遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 分页数据总条数
      total: 0,
      // 字典表格数据
      patientTableList: [],
      // 弹出层标题
      title: '',
      // 是否显示档案弹出层
      fileOpen: false,
      // 是否显示病例弹出层
      careHistoryOpen: false,
      // 性别数据字典
      sexOptions: [],
      // 完善信息状态
      isFinalOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        idCard: undefined,
        phone: undefined
      },
      // 患者信息
      patientObj: {},
      // 档案信息
      patientFileObj: {},
      // 历史病例
      careHistoryList: []
    }
  },
  beforeCreate() {
    that = this
  },
  // 勾子
  created() {
    // 加载性别
    this.getDataByType('sys_user_sex').then(res => {
      this.sexOptions = res.data
    })
    // 加载性别
    this.getDataByType('his_patient_msg_final').then(res => {
      this.isFinalOptions = res.data
    })
    // 查询接诊类型
    getDataByType('his_receive_type').then(res => {
      this.receiveTypeOptions = res.data
    })
    // 查询是否传染
    getDataByType('his_contagious_status').then(res => {
      this.isContagiousOptions = res.data
    })
    // 查询表格数据
    this.getPatientList()
  },
  // 方法
  methods: {
    // 查询表格数据
    getPatientList() {
      this.loading = true // 打开遮罩
      listPatientForPage(this.addDateRange(this.queryParams, this.dateRange)).then(res => {
        this.patientTableList = res.data
        this.total = res.total
        this.loading = false// 关闭遮罩
      })
    },
    // 条件查询
    handleQuery() {
      this.getPatientList()
    },
    // 重置查询条件
    resetQuery() {
      this.resetForm('queryForm')
      this.getPatientList()
    },
    // 数据表格的多选择框选择时触发
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.patientId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      // 重新查询
      this.getPatientList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      // 重新查询
      this.getPatientList()
    },
    // 翻译性别
    sexFormatter(row) {
      return this.selectDictLabel(this.sexOptions, row.sex)
    },
    // 翻译信息状态
    isFinalFormatter(row) {
      return this.selectDictLabel(this.isFinalOptions, row.isFinal)
    },
    // 根据出生年月计算年龄
    calculateAgeFormatter(row) {
      return this.getAge(row.birthday.substring(0, 10))
    },
    // 查询患者档案
    handleViewFile() {
      const patientId = this.ids[0]
      this.fileOpen = true
      this.title = '查询患者档案信息'
      this.patientFileObj = {}
      this.patientObj = {}
      // 查询患者
      getPatientById(patientId).then(res => {
        this.patientObj = res.data
        const sex = this.patientObj.sex
        const isFinal = this.patientObj.isFinal
        this.patientObj.sex = (sex === '0' ? '男' : sex === '1' ? '女' : '未知')
        this.patientObj.isFinal = isFinal === '0' ? '未完善' : '已完善'
      })
      // 查询档案
      getPatientFileById(patientId).then(res => {
        if (res.data !== null) {
          this.patientFileObj = res.data
        }
      })

      //
    },
    // 取消
    cancelFile() {
      this.fileOpen = false
      this.title = ''
    },
    // 查询患者就诊病例信息
    handleViewCareHistory() {
      const patientId = this.ids[0]

      this.title = '患者编号为【' + patientId + '】的历史病例'
      this.careHistoryOpen = true
      getPatientAllMessageByPatientId(patientId).then(res => {
        // 历史病例集合
        this.careHistoryList = res.data.careHistoryList
      })
    }

  }
}
</script>
<style scoped>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
