<template>
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-row :gutter="12" style="margin-bottom:5px">
      <el-col :span="24">
        <el-card>
          <el-form :inline="true" label-width="68px">
            <el-form-item style="float:right">
              <el-button type="primary" icon="el-icon-plus" size="small" @click="upWeek()">上一周</el-button>
              <el-button type="success" icon="el-icon-s-operation" size="small" @click="currentWeek()">当前周</el-button>
              <el-button type="primary" icon="el-icon-plus" size="small" @click="nextWeek()">下一周</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <!-- 查询条件结束 -->
    <!-- 时间范围开始 -->
    <el-row :gutter="12" style="margin-bottom:5px">
      <el-col :span="24">
        <el-card>
          <div style="text-align:center">
            <span>{{ schedulingData.startTimeThisWeek }}(周一)</span>-<span>{{ schedulingData.endTimeThisWeek }}(周日)</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 时间范围结束 -->

    <!-- 排班表格开始 -->
    <el-table v-loading="loading" :data="tableData" border :span-method="spanMethod">
      <el-table-column prop="userId" label="医生" align="center" :formatter="userFormatter" />
      <el-table-column prop="deptId" label="科室" align="center" :formatter="deptFormatter" />
      <el-table-column prop="subsectionType" label="时间/日期" align="center" :formatter="subsectionTypeFormatter" />
      <el-table-column prop="schedulingType[0]" :label="labelNames[0]" align="center" :formatter="schedulingTypeDay1Formatter" />
      <el-table-column prop="schedulingType[1]" :label="labelNames[1]" align="center" :formatter="schedulingTypeDay2Formatter" />
      <el-table-column prop="schedulingType[2]" :label="labelNames[2]" align="center" :formatter="schedulingTypeDay3Formatter" />
      <el-table-column prop="schedulingType[3]" :label="labelNames[3]" align="center" :formatter="schedulingTypeDay4Formatter" />
      <el-table-column prop="schedulingType[4]" :label="labelNames[4]" align="center" :formatter="schedulingTypeDay5Formatter" />
      <el-table-column prop="schedulingType[5]" :label="labelNames[5]" align="center" :formatter="schedulingTypeDay6Formatter" />
      <el-table-column prop="schedulingType[6]" :label="labelNames[6]" align="center" :formatter="schedulingTypeDay7Formatter" />
    </el-table>
    <!-- 排班表格结束 -->

  </div>
</template>
<script>
import { queryUsersNeedScheduling, queryMyScheduling } from '@/api/docter/scheduling'
import { selectAllDept } from '@/api/system/dept'

export default {
  data() {
    return {
      // 遮罩层
      loading: false,
      // 科室数据
      deptOptions: [],
      // 用户数据
      userOptions: [],
      // 排班时间段字典
      subsectionTypeOptions: [],
      // 排班类型字典
      schedulingTypeOptions: [],
      queryParams: {
        queryDate: undefined
      },
      // 查询条件
      schedulingData: {
        startTimeThisWeek: undefined,
        endTimeThisWeek: undefined
      },
      // 排班表的数据   应该从后台查询
      tableData: [],
      // 表头数据
      labelNames: ['', '', '', '', '', '', '']
    }
  },
  computed: {
    groupNum() { // 获取医生列表数组
      return new Set(this.tableData.map(o => o.userId))
    }
  },
  created() {
    // 查询科室
    selectAllDept().then(res => {
      this.deptOptions = res.data
    })
    // 查询要排班的医生
    queryUsersNeedScheduling().then(res => {
      this.userOptions = res.data
    })
    // 查询班类型
    this.getDataByType('his_scheduling_type').then(res => {
      this.schedulingTypeOptions = res.data
    })
    // 查询排班时间段
    this.getDataByType('his_subsection_type').then(res => {
      this.subsectionTypeOptions = res.data
    })
    // 查询排班数据  返回值要填充schedulingData tableData labelNames
    this.listScheduling()
  },
  methods: {
    listScheduling() {
      this.loading = true
      // 调用API
      queryMyScheduling(this.queryParams).then(res => {
        this.labelNames = res.data.labelNames
        this.tableData = res.data.tableData
        this.schedulingData = res.data.schedulingData
        this.loading = false
      })
    },
    // 查询
    handleQuery() {
      this.listScheduling()
    },

    // 重置
    resetQuery() {
      this.resetForm('queryForm')
      this.listScheduling()
    },
    // 上一周
    upWeek() {
      this.queryParams.queryDate = this.schedulingData.startTimeThisWeek
      this.listScheduling()
    },
    // 当前周
    currentWeek() {
      this.queryParams.queryDate = undefined
      this.listScheduling()
    },
    // 下一周
    nextWeek() {
      this.queryParams.queryDate = this.schedulingData.endTimeThisWeek
      this.listScheduling()
    },
    // 翻译用户
    userFormatter(row) {
      let name = ''
      this.userOptions.filter(item => {
        if (row.userId === item.userId) {
          name = item.userName
        }
      })
      return name
    },
    // 翻译科室
    deptFormatter(row) {
      let name = ''
      this.deptOptions.filter(item => {
        if (row.deptId === item.deptId) {
          name = item.deptName
        }
      })
      return name
    },
    // 翻译排班时段
    subsectionTypeFormatter(row) {
      return this.selectDictLabel(this.subsectionTypeOptions, row.subsectionType)
    },
    // 翻译周一排班类型
    schedulingTypeDay1Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[0])
    },
    // 翻译周一排班类型
    schedulingTypeDay2Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[1])
    },
    // 翻译周二排班类型
    schedulingTypeDay3Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[2])
    },
    // 翻译周三排班类型
    schedulingTypeDay4Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[3])
    },
    // 翻译周四排班类型
    schedulingTypeDay5Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[4])
    },
    // 翻译周五排班类型
    schedulingTypeDay6Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[5])
    },
    // 翻译周六排班类型
    schedulingTypeDay7Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[6])
    },
    // 合并的代码
    spanMethod(data) { // 对于表格数据进行分组合并操作，UI组件回调函数
      const { row, rowIndex, columnIndex } = data
      if (columnIndex < 2 || columnIndex > 9) { // 医生合并展示区
        const len = this.userGroup(row.userId)
        const lenName = this.userIdLen(row.userId)
        if (rowIndex === lenName) { // 某医生首位部门行
          return {
            rowspan: len,
            colspan: 1
          }
        } else {
          return { // 某医生非首位部门行
            rowspan: 0,
            colspan: 0
          }
        }
      } else { // 排班信息展示区
        return {
          rowspan: 1,
          colspan: 1
        }
      }
    },
    userGroup(name) { // 根据医生名称获取医生数量
      return this.tableData.filter(o => o.userId === name).length
    },
    userIdLen(name) { // 根据医生名称获取该医生第一个部门在全量部门中的偏移位置
      const tmp = Array.from(this.groupNum)
      const index = tmp.indexOf(name) // 某医生在全医生中的偏移位置
      let len = 0
      for (let i = 0; i < index; i++) {
        len += this.userGroup(tmp[i])
      }
      return len
    }

  }
}
</script>
