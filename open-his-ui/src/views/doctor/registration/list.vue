<template>
  <!-- 挂号列表页面 -->
  <div class="app-container">
    <!--查询条件开始  -->
    <div>
      <el-form ref="regQueryParams" :model="regQueryParams" :inline="true" label-width="68px">
        <!-- 第一行 -->
        <el-row>
          <el-col :span="24">
            <el-form-item label="所属科室" prop="deptId">
              <el-select v-model="regQueryParams.deptId" placeholder="请选择科室" size="small" style="width:150px">
                <el-option
                  v-for="d in deptOptions"
                  :key="d.deptId"
                  :label="d.deptName"
                  :value="d.deptId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="患者姓名" prop="patientName">
              <el-input
                v-model="regQueryParams.patientName"
                size="small"
                placeholder="患者姓名"
                clearable
                style="width:180px"
              />
            </el-form-item>
            <el-form-item label="挂号类型" prop="schedulingType">
              <el-select v-model="regQueryParams.schedulingType" placeholder="请选择挂号类型" size="small" style="width:150px">
                <el-option
                  v-for="d in schedulingOptions"
                  :key="d.dictValue"
                  :label="d.dictLabel"
                  :value="d.dictValue"
                />

              </el-select>
            </el-form-item>
            <el-form-item label="挂号时段" prop="subsectionType">
              <el-select v-model="regQueryParams.subsectionType" placeholder="请选择挂号时段" size="small" style="width:140px">
                <el-option
                  v-for="d in subsectionOptions"
                  :key="d.dictValue"
                  :label="d.dictLabel"
                  :value="d.dictValue"
                />

              </el-select>
            </el-form-item>
            <el-form-item label="挂号状态" prop="registrationStatus">
              <el-select v-model="regQueryParams.registrationStatus" placeholder="请选择挂号状态" size="small" style="width:140px;">
                <el-option
                  v-for="d in regstatusOptions"
                  :key="d.dictValue"
                  :label="d.dictLabel"
                  :value="d.dictValue"
                />

              </el-select>
            </el-form-item>
            <!-- 第二行 -->
            <el-row>
              <el-col :span="24">
                <el-form-item label="就诊日期" prop="visitDate">
                  <el-date-picker
                    v-model="regQueryParams.visitDate"
                    size="small"
                    type="daterange"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" size="mini" @click="handleRegQuery">搜索</el-button>
                  <el-button icon="el-icon-refresh" size="mini" @click="resetRegQuery">重置</el-button>
                </el-form-item>
              </el-col>
            </el-row>

          </el-col>
        </el-row>

      </el-form>
    </div>
    <!--查询条件结束  -->
    <!-- {{ regItemOptions }} -->
    <!-- 数据表格开始 -->
    <el-table v-loading="loading" border :data="RegDataTableList">
      <el-table-column type="expand" label="详情" width="55">
        <template slot-scope="props">
          <el-form label-position="left" inline class="RegTableList_table_expand">
            <el-row :gutter="10">
              <el-col :span="12">
                <!-- 展开项 -->
                <el-form-item label="挂号流水">
                  <span>{{ props.row.registrationId }}</span>
                </el-form-item><br>
                <el-form-item label="挂号项目类型">
                  <span>{{ props.row.registrationItemId | regItemIdFormatter }}</span>
                </el-form-item><br>
              </el-col>
              <el-col :span="12">
                <el-form-item label="创建时间">
                  <span>{{ props.row.createTime }}</span>
                </el-form-item><br>
                <el-form-item label="挂号员">
                  <span>{{ props.row.createBy }}</span>
                </el-form-item><br>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column label="患者姓名" prop="patientName" align="center" />
      <el-table-column label="挂号科室" prop="deptId" align="center" :formatter="deptFormatter" />
      <el-table-column label="接诊医生" width="90px" prop="doctorName" align="center" />
      <el-table-column label="挂号费用" width="100px" prop="registrationAmount" align="center" :formatter="regmoneyFormatter" />
      <el-table-column label="流水编号" width="85px" prop="registrationNumber" align="center" />
      <el-table-column label="状态" prop="registrationStatus" align="center" :formatter="RegstatusFormatter" />
      <el-table-column label="就诊日期" prop="visitDate" align="center" />
      <el-table-column label="挂号类型" width="85px" prop="schedulingType" align="center" :formatter="scheTypeFormatter" />
      <el-table-column label="挂号时段" width="85px" prop="subsectionType" align="center" :formatter="subTypeFormatter" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-delete" size="mini" @click="handleCollectFee(scope.row)">收费</el-button>
          <el-button type="text" icon="el-icon-delete" size="mini" @click="handleInvalid(scope.row)">作废</el-button>
          <el-button type="text" icon="el-icon-delete" size="mini" @click="handleReturn(scope.row)">退号</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 数据表格结束 -->

    <!-- 分页组件开始 -->
    <el-pagination
      v-show="total>0"
      :current-page="regQueryParams.pageNum"
      :page-sizes="[5, 10, 20, 30]"
      :page-size="regQueryParams.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <!-- 分页组件结束 -->

  </div>
</template>

<script>
let that
import { selectAllDept } from '@/api/system/dept/dept'
import { getDataByType } from '@/api/system/dict/data'
import { queryRegistrationForPage, doInvalid, doReturn, collectFee } from '@/api/doctor/registration/registration'
import { selectAllRegisteredItem } from '@/api/system/registeredItem/registeredItem'
export default {
  filters: {
    regItemIdFormatter(value) {
      return that.regItemOptions.filter(item => {
        if (item.regItemId === value) {
          return item.regItemName
        }
      })[0].regItemName
    }
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 科室数据
      deptOptions: [],
      // 所有挂号项目
      regItemOptions: [],
      // 挂号类型和时段
      schedulingOptions: [],
      subsectionOptions: [],
      // 挂号状态
      regstatusOptions: [],
      // 挂号列表查询条件
      regQueryParams: {
        deptId: '',
        patientName: '',
        schedulingType: '',
        subsectionType: '',
        regStatus: '',
        visitDate: '',
        beginTime: '',
        endTime: '',
        pageNum: 1,
        pageSize: 5
      },
      // 挂号单数据表格
      RegDataTableList: []
    }
  },
  beforeCreate() {
    that = this
  },
  created() {
    // 查询所有挂号项目
    selectAllRegisteredItem().then(res => {
      this.regItemOptions = res.data
    })
    // 查询所有部门
    selectAllDept().then(res => {
      this.deptOptions = res.data
    })
    // 查询挂号类型和时段
    getDataByType('his_scheduling_type').then(res => {
      this.schedulingOptions = res.data
    })
    getDataByType('his_subsection_type').then(res => {
      this.subsectionOptions = res.data
    })
    // 挂号状态
    getDataByType('his_registration_status').then(res => {
      this.regstatusOptions = res.data
    })
    // 查询挂号数据
    this.getRegData()
  },
  methods: {
    // 总查询
    getRegData() {
      queryRegistrationForPage(this.regQueryParams).then(res => {
        this.RegDataTableList = res.data
        this.total = res.total
        this.loading = false
      })
    },
    // 查询挂号搜索
    handleRegQuery() {
      if (this.regQueryParams.visitDate !== '') {
        this.loading = true
        if (this.regQueryParams.visitDate[0] === this.regQueryParams.visitDate[1]) {
          this.regQueryParams.beginTime = ''
          this.regQueryParams.endTime = ''
          this.regQueryParams.visitDate = this.regQueryParams.visitDate[0].toString()
        } else {
          this.regQueryParams.beginTime = this.regQueryParams.visitDate[0]
          this.regQueryParams.endTime = this.regQueryParams.visitDate[1]
          this.regQueryParams.visitDate = ''
        }
        this.getRegData()
      } else {
        this.loading = true
        this.getRegData()
      }
    },
    // 重置查询条件
    resetRegQuery() {
      this.loading = true
      this.regQueryParams = {
        deptId: '',
        patientName: '',
        schedulingType: '',
        subsectionType: '',
        regStatus: '',
        visitDate: '',
        beginTime: '',
        endTime: '',
        pageNum: 1,
        pageSize: 5
      }

      this.getRegData()
      this.loading = false
    },
    // 收费
    handleCollectFee(row) {
      const tx = this
      const registrationId = row.registrationId
      this.$confirm('是否要对挂号单【' + registrationId + '】进行收费？', '现金收费', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        type: 'waring',
        center: true
      }).then(() => {
        collectFee(registrationId).then(res => {
          tx.msgSuccess('现金收费成功')
          this.resetRegQuery()
        }).catch(() => {
          tx.msgError('现金收费失败')
        })
      }).catch(() => {
        tx.msgError('取消收费')
      })
    },
    // 作废
    handleInvalid(row) {
      const registrationId = row.registrationId
      this.$confirm('确定要把挂号流水为【' + registrationId + '】的挂号单作废吗', '挂号单作废', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        type: 'waring',
        center: true

      }).then(() => {
        doInvalid(registrationId).then(() => {
          this.msgSuccess('作废成功')
          this.resetRegQuery()
        })
      }).catch(() => {
        this.msgError('取消作废')
      })
    },
    // 退号
    handleReturn(row) {
      const registrationId = row.registrationId
      this.$confirm('确定要把挂号流水为【' + registrationId + '】的挂号单退号吗', '挂号单退号', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        type: 'waring',
        center: true

      }).then(() => {
        doReturn(registrationId).then(() => {
          this.msgSuccess('退号成功')
          this.resetRegQuery()
        })
      }).catch(() => {
        this.msgError('取消退号')
      })
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.regQueryParams.pageSize = val
      // 重新查询
      this.getRegData()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.regQueryParams.pageNum = val
      // 重新查询
      this.getRegData()
    },
    // 部门翻译
    deptFormatter(row) {
      return this.selectDeptLabel(this.deptOptions, row.deptId)
    },
    // 挂号费用格式
    regmoneyFormatter(row) {
      return '￥' + row.registrationAmount + '元'
    },
    // 状态翻译
    RegstatusFormatter(row) {
      return this.selectDictLabel(this.regstatusOptions, row.registrationStatus)
    },
    // 挂号类型翻译
    scheTypeFormatter(row) {
      return this.selectDictLabel(this.schedulingOptions, row.schedulingType)
    },
    // 挂号时段翻译
    subTypeFormatter(row) {
      return this.selectDictLabel(this.subsectionOptions, row.subsectionType)
    }

  }
}
</script>

<style lang="scss" scoped>
.RegTableList_table_expand el-col{
    padding-right: 8px;
}
</style>
