<template>
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="所属部门" prop="deptId">
        <el-select
          v-model="queryParams.deptId"
          placeholder="请选择所属部门"
          clearable
          size="small"
          style="width:200px"
        >
          <el-option
            v-for="d in deptList"
            :key="d.deptId"
            :label="d.deptName"
            :value="d.deptId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="患者名称" prop="patientName">
        <el-input
          v-model="queryParams.patientName"
          placeholder="请输入患者名称"
          clearable
          size="small"
          style="width:200px"
        />
      </el-form-item>
      <el-form-item label="挂号类型" prop="schedulingType">
        <el-select
          v-model="queryParams.schedulingType"
          placeholder="可用状态"
          clearable
          size="small"
          style="width:200px"
        >
          <el-option
            v-for="dict in schedulingTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="挂号时段" prop="subsectionType">
        <el-select
          v-model="queryParams.subsectionType"
          placeholder="挂号时段"
          clearable
          size="small"
          style="width:200px"
        >
          <el-option
            v-for="dict in subsectionTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="挂号状态" prop="regStatus">
        <el-select
          v-model="queryParams.regStatus"
          placeholder="挂号状态"
          clearable
          size="small"
          style="width:200px"
        >
          <el-option
            v-for="dict in regStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="挂号时间" prop="queryDate">
        <el-date-picker
          v-model="queryParams.queryDate"
          size="small"
          style="width:200px"
          value-format="yyyy-MM-dd"
          type="date"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="normal" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询条件结束 -->
    <!-- 数据表格开始 -->
    <el-table v-loading="loading" border :data="regTableList">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="right" inline class="demo-table-expand">
            <el-form-item label="挂号ID:">
              <span>{{ props.row.regId }}</span>
            </el-form-item>
            <el-form-item label="挂号员:">
              <span>{{ props.row.createBy }}</span>
            </el-form-item>
            <el-form-item label="创建时间:">
              <span>{{ props.row.createTime }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column label="患者姓名" align="center" prop="patientName" />
      <el-table-column label="挂号科室" align="center" prop="deptId" :formatter="deptIdFormatter" />
      <el-table-column label="接诊医生" align="center" prop="doctorName" />
      <el-table-column label="挂号费用" align="center" prop="regItemAmount">
        <template slot-scope="scope">
          <span>{{ scope.row.regItemAmount|rounding }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流水编号" align="center" prop="regNumber" />
      <el-table-column label="状态" align="center" prop="regStatus" :formatter="regStatusFormatter" />
      <el-table-column label="就诊日期" align="center" prop="visitDate" />
      <el-table-column label="挂号类型" align="center" prop="schedulingType" :formatter="schedulingTypeFormatter" />
      <el-table-column label="挂号时段" align="center" prop="subsectionType" :formatter="subsectionTypeFormatter" />
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button v-if="scope.row.regStatus=='0'" type="success" icon="el-icon-check" size="mini" @click="handleCollect(scope.row)">收费</el-button>
          <el-button v-if="scope.row.regStatus=='1'" type="danger" icon="el-icon-close" size="mini" @click="handleReturn(scope.row)">退号</el-button>
          <el-button v-if="scope.row.regStatus=='0'" type="danger" icon="el-icon-check" size="mini" @click="handleInvalid(scope.row)">作废</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 数据表格结束 -->
    <!-- 分页控件开始 -->
    <el-pagination
      v-show="total>0"
      :current-page="queryParams.pageNum"
      :page-sizes="[5,10,20,30]"
      :page-size="queryParams.pageSize"
      layout="total,sizes,prev,pager,next,jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <!-- 分页控件结束 -->
  </div>
</template>

<script>
// 引入接口
import { selectAllDept } from '@/api/system/dept'
import { selectNeedSchedulingUsers } from '@/api/system/user'
import { queryRegistrationForPage, collectFee, doInvalid, doReturn } from '@/api/docter/registration'
export default {
  // 过滤器
  filters: {
    // 保留两位小数
    rounding(value) {
      return value.toFixed(2)
    }
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 总条数
      total: 0,
      // 挂号数据
      regTableList: [],
      // 部门数据
      deptList: [],
      // 用户数据
      userList: [],
      // 挂号单状态数据字典
      regStatusOptions: [],
      // 门诊急诊
      schedulingTypeOptions: [],
      // 上午  中午  晚上
      subsectionTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientName: undefined,
        deptId: undefined,
        schedulingType: undefined,
        subsectionType: undefined,
        visitDate: undefined,
        queryDate: new Date()
      }
    }
  },
  created() {
    // 加载查询条件排班类型1 门诊 2 急诊 字典表数据翻译
    this.getDataByType('his_scheduling_type').then(res => {
      this.schedulingTypeOptions = res.data
    })
    // 加载排班时段1上午  2下午 3晚上 字典表数据翻译
    this.getDataByType('his_subsection_type').then(res => {
      this.subsectionTypeOptions = res.data
    })
    // 加载挂号状态0未收费,1待就诊，2,就诊中，3，就诊完成，4，已退号，5 作废
    this.getDataByType('his_registration_status').then(res => {
      this.regStatusOptions = res.data
    })

    selectAllDept().then(res => {
      this.deptList = res.data
    })
    selectNeedSchedulingUsers().then(res => {
      this.userList = res.data
    })
    // 查询挂号信息
    this.listRegistration()
  },
  methods: {
    listRegistration() {
      this.loading = true
      this.queryParams.visitDate = this.moment(this.queryParams.queryDate).format('YYYY-MM-DD')
      queryRegistrationForPage(this.queryParams).then(res => {
        this.regTableList = res.data
        this.total = res.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 每页显示多少条的数据变化
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.listRegistration()
    },
    // 分页跳转
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.listRegistration()
    },
    // 搜索
    handleQuery() {
      this.listRegistration()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.listRegistration()
    },
    // 翻译部门
    deptIdFormatter(row, column) {
      let deptName
      this.deptList.filter(dept => {
        if (dept.deptId === row.deptId) {
          deptName = dept.deptName
        }
      })
      return deptName
    },
    // 翻译排班类型
    schedulingTypeFormatter(row, column) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType)
    },
    // 翻译排班时段
    subsectionTypeFormatter(row, column) {
      return this.selectDictLabel(this.subsectionTypeOptions, row.subsectionType)
    },
    // 翻译挂号单类型
    regStatusFormatter(row, column) {
      return this.selectDictLabel(this.regStatusOptions, row.regStatus)
    },
    // 收费
    handleCollect(row) {
      this.loading = true
      collectFee(row.regId).then(res => {
        this.loading = false
        this.msgSuccess('收费成功')
        this.listRegistration()
      }).catch(() => {
        this.msgError('收费失败')
      })
    },
    // 作废
    handleInvalid(row) {
      this.loading = true
      doInvalid(row.regId).then(res => {
        this.loading = false
        this.msgSuccess('作废成功')
        this.listRegistration()
      }).catch(() => {
        this.msgError('作废失败')
      })
    },
    // 退号
    handleReturn(row) {
      this.loading = true
      doReturn(row.regId).then(res => {
        this.loading = false
        this.msgSuccess('退号成功')
        this.listRegistration()
      }).catch(() => {
        this.msgError('退号失败')
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
