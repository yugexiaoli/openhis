<template>
  <div class="app-container">
    <!-- 搜索开始 -->
    <el-row :gutter="12">
      <el-col :span="24">
        <el-card style="margin-bottom:5px;">

          <el-form ref="queryParams" :model="queryParams" :inline="true" label-width="68px">
            <el-form-item label="所属科室" prop="deptId">
              <el-select v-model="queryParams.deptId" placeholder="请选择医生科室" clearable size="small" style="width:180px">
                <el-option
                  v-for="dept in deptOptions"
                  :key="dept.deptId"
                  :label="dept.deptName"
                  :value="dept.deptId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="医生名称" prop="userId">
              <el-select v-model="queryParams.userId" placeholder="请选择医生" clearable size="small" style="width:180px">
                <el-option
                  v-for="user in userOptions"
                  :key="user.userId"
                  :label="user.userName"
                  :value="user.userId"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
            <el-form-item style="float:right;">
              <el-button type="primary" icon="el-icon-plus" size="mini" @click="upweek()">上一周</el-button>
              <el-button type="success" icon="el-icon-s-operation" size="mini" @click="thisweek()">当前周</el-button>
              <el-button type="primary" icon="el-icon-check" size="mini" @click="nextweek()">下一周</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <!-- 搜索结束 -->
    <!-- 排班周期开始 -->
    <el-row :gutter="12">
      <el-col :span="24">
        <el-card style="margin-bottom:5px;">
          <div style="text-align:center">
            <span>
              <span>{{ schedulingDate.startDatethisweek }}(周一)</span>-<span>{{ schedulingDate.endDatethisweek }}(周日)</span>
            </span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 排班周期结束 -->
    <!-- 表格开始 -->
    <el-table v-loading="loading" :data="tableData" border :span-method="spanMethod">
      <el-table-column prop="userId" label="医生" align="center" :formatter="userFormatter" />
      <el-table-column prop="deptId" label="科室" align="center" :formatter="deptFormatter" />
      <el-table-column prop="subsectionType" label="时间/(上下晚)" align="center" :formatter="subsectionTypeFormatter" />
      <el-table-column prop="schedulingType[0]" :label="labelNames[0]" align="center" :formatter="schedulingTypeDay1Formatter" />
      <el-table-column prop="schedulingType[1]" :label="labelNames[1]" align="center" :formatter="schedulingTypeDay2Formatter" />
      <el-table-column prop="schedulingType[2]" :label="labelNames[2]" align="center" :formatter="schedulingTypeDay3Formatter" />
      <el-table-column prop="schedulingType[3]" :label="labelNames[3]" align="center" :formatter="schedulingTypeDay4Formatter" />
      <el-table-column prop="schedulingType[4]" :label="labelNames[4]" align="center" :formatter="schedulingTypeDay5Formatter" />
      <el-table-column prop="schedulingType[5]" :label="labelNames[5]" align="center" :formatter="schedulingTypeDay6Formatter" />
      <el-table-column prop="schedulingType[6]" :label="labelNames[6]" align="center" :formatter="schedulingTypeDay7Formatter" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" size="mini" @click="editScheduling(scope.row.userId)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 表格结束 -->
    <!-- 编辑弹出层开始 -->
    <el-dialog
      width="1200px"
      :title="title"
      :visible.sync="open"
      append-to-body
      center
    >
      <el-table :data="editData" border>
        <el-table-column label="排班时段（上下晚）" align="center" prop="subsectionType" :formatter="subsectionTypeFormatter" />
        <el-table-column prop="schedulingType[0]" :label="labelNames[0]" align="center">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.schedulingType[0]"
              clearable
            >
              <el-option v-for="t in schedulingTypeOptions" :key="t.dictValue" :label="t.dictLabel" :value="t.dictValue" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="schedulingType[1]" :label="labelNames[1]" align="center">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.schedulingType[1]"
              clearable
            >
              <el-option v-for="t in schedulingTypeOptions" :key="t.dictValue" :label="t.dictLabel" :value="t.dictValue" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="schedulingType[2]" :label="labelNames[2]" align="center">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.schedulingType[2]"
              clearable
            >
              <el-option v-for="t in schedulingTypeOptions" :key="t.dictValue" :label="t.dictLabel" :value="t.dictValue" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="schedulingType[3]" :label="labelNames[3]" align="center">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.schedulingType[3]"
              clearable
            >
              <el-option v-for="t in schedulingTypeOptions" :key="t.dictValue" :label="t.dictLabel" :value="t.dictValue" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="schedulingType[4]" :label="labelNames[4]" align="center">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.schedulingType[4]"
              clearable
            >
              <el-option v-for="t in schedulingTypeOptions" :key="t.dictValue" :label="t.dictLabel" :value="t.dictValue" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="schedulingType[5]" :label="labelNames[5]" align="center">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.schedulingType[5]"
              clearable
            >
              <el-option v-for="t in schedulingTypeOptions" :key="t.dictValue" :label="t.dictLabel" :value="t.dictValue" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="schedulingType[6]" :label="labelNames[6]" align="center">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.schedulingType[6]"
              clearable
            >
              <el-option v-for="t in schedulingTypeOptions" :key="t.dictValue" :label="t.dictLabel" :value="t.dictValue" />
            </el-select>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 编辑弹出层结束 -->
  </div>
</template>

<script>
import { saveScheduling, queryScheduling, queryUsersNeedScheduling } from '@/api/doctor/scheduling/scheduling'
import { selectAllDept } from '@/api/system/dept/dept'
export default {
  data() {
    return {
      // 修改弹出层标题
      title: '',
      // 弹出层显示
      open: false,
      // 修改弹出层数据表格
      editData: [],
      // 表格加载层
      loading: false,
      // 查询参数
      queryParams: {
        deptId: undefined,
        userId: undefined,
        queryDate: undefined
      },
      // 科室数据
      deptOptions: [],
      // 需要排版的用户数据
      userOptions: [],
      // 排班时段字典
      subsectionTypeOptions: [],
      // 排班类型字典
      schedulingTypeOptions: [],
      // 排班周期数据
      schedulingDate: {
        startDatethisweek: '',
        endDatethisweek: ''
      },
      // 表格数据
      // 医生和科室信息mock数据，该数据假设已经按着部门信息进行排序
      tableData: [],
      // 表格一周时间
      labelNames: ['', '', '', '', '', '', '']

    }
  },
  computed: {
    groupNum() { // 获取医生列表数组
      return new Set(this.tableData.map(o => o.userId))
    }
  },
  created() {
    selectAllDept().then(res => {
      this.deptOptions = res.data
    })
    queryUsersNeedScheduling().then(res => {
      this.userOptions = res.data
    })
    // 查询排班类型和时段字典数据
    this.getDataByType('his_subsection_type').then(res => {
      this.subsectionTypeOptions = res.data
    })
    this.getDataByType('his_scheduling_type').then(res => {
      this.schedulingTypeOptions = res.data
    })
    this.listScheduling()
  }, methods: {
    // 查询方法
    listScheduling() {
      this.loading = true
      queryScheduling(this.queryParams).then(res => {
        const { data } = res
        this.labelNames = data.labelNames
        this.schedulingDate = data.schedulingDate
        this.tableData = data.tableData
        this.loading = false
      })
      this.loading = false
    },
    // 医生翻译
    userFormatter(row) {
      let name = ''
      this.userOptions.filter(item => {
        if (row.userId === item.userId) {
          name = item.userName
        }
      })
      return name
    },
    // 科室翻译
    deptFormatter(row) {
      let name = ''
      this.deptOptions.filter(item => {
        if (row.deptId === item.deptId) {
          name = item.deptName
        }
      })
      return name
    },
    // 排班时间(上下晚班)翻译
    subsectionTypeFormatter(row) {
      return this.selectDictLabel(this.subsectionTypeOptions, row.subsectionType)
    },
    // 排班类型（周一）翻译
    schedulingTypeDay1Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[0])
    },
    // 排班类型（周二）翻译
    schedulingTypeDay2Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[1])
    },
    // 排班类型（周三）翻译
    schedulingTypeDay3Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[2])
    },
    // 排班类型（周四）翻译
    schedulingTypeDay4Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[3])
    },
    // 排班类型（周五）翻译
    schedulingTypeDay5Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[4])
    },
    // 排班类型（周六）翻译
    schedulingTypeDay6Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[5])
    },
    // 排班类型（周天）翻译
    schedulingTypeDay7Formatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType[6])
    },
    // 搜索方法
    handleQuery() {
      this.listScheduling()
    },
    // 重置查询
    resetQuery() {
      this.resetForm('queryParams')
      this.loading = true
      this.listScheduling()
      this.loading = false
    },
    // 上一周
    upweek() {
      this.queryParams.queryDate = this.schedulingDate.startDatethisweek
      this.loading = true
      this.listScheduling()
      this.loading = false
    },
    // 当前周
    thisweek() {
      this.queryParams.queryDate = undefined
      this.loading = true
      this.listScheduling()
      this.loading = false
    },
    // 下一周
    nextweek() {
      this.queryParams.queryDate = this.schedulingDate.endDatethisweek
      this.listScheduling()
    },
    // 编辑排班
    editScheduling(userId) {
      this.editData = []
      let name = ''
      this.userOptions.filter(item => {
        if (item.userId === userId) {
          name = item.userName
        }
      })
      this.title = '修改' + name + '的排班'
      this.open = true

      this.tableData.filter(item => {
        if (item.userId === userId) {
          this.editData.push(item)
        }
      })
    },
    // 弹出层确定 保存排班信息
    handleSubmit() {
      this.loading = true
      const form = { data: this.editData, startDate: this.schedulingDate.startDatethisweek }
      saveScheduling(form).then(res => {
        this.listScheduling()
        this.loading = false
        this.open = false
        this.msgSuccess('保存成功')
      }).catch(() => {
        this.loading = false
        this.open = false
        this.msgError('保存失败')
      })
    },
    // 弹出层取消
    cancel() {
      this.open = false
    },
    // 表格合并代码
    spanMethod(data) { // 对于表格数据进行分组合并操作，UI组件回调函数
      // const { row, column, rowIndex, columnIndex } = data
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

<style>

</style>
