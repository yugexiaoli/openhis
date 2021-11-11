<template>
  <!-- 门诊挂号页面 -->
  <div class="app-container">
    <!-- 患者信息查询 -->
    <el-card class="box-card">
      <el-form ref="queryForm" :model="patientParams" label-width="70px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input
                v-model="patientParams.idCard"
                placeholder="请输入患者身份证号码"
                clearable
                size="small"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item>
              <el-button type="primary" size="small" icon="el-icon-search" @click="HandleIdCardQuery">加载身份证号</el-button>
              <el-button type="warning" size="small" icon="el-icon-search" @click="HandleIdCardQuery">加载患者信息</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <!-- 患者基本信息表单开始 -->
    <el-card class="box-card">
      <el-form ref="form" :model="patientParams" :rules="rules" :inline="true" label-width="88px">
        <el-form-item label="身份证号" prop="idCard">
          <el-input
            v-model="patientParams.idCard"
            placeholder="请输身份证号"
            clearable
            size="small"
            style="width:200px"
          />
        </el-form-item>
        <el-form-item label="患者姓名" prop="name">
          <el-input
            v-model="patientParams.name"
            placeholder="请输入患者姓名"
            clearable
            size="small"
            style="width:200px"
          />
        </el-form-item>
        <el-form-item label="患者电话" prop="phone">
          <el-input
            v-model="patientParams.phone"
            placeholder="请输入患者电话"
            clearable
            size="small"
            style="width:200px"
          />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="patientParams.sex">
            <el-radio
              v-for="d in sexOptions"
              :key="d.dictValue"
              :label="d.dictValue"
              :value="d.dictValue"
            >{{ d.dictLabel }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker
            v-model="patientParams.birthday"
            size="small"
            style="width:200px"
            value-format="yyyy-MM-dd"
            type="date"
          />
        </el-form-item>
        <el-form-item label="患者年龄" prop="age">
          <el-input
            v-model="patientParams.age"
            :readonly="true"
            placeholder="请输入患者年龄"
            clearable
            size="small"
            style="width:200px"
          />
        </el-form-item>
        <el-form-item label="地址信息" prop="address">
          <el-input
            v-model="patientParams.address"
            placeholder="请输入地址信息"
            clearable
            style="width:500px"
            size="small"
          />
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 患者基本信息表单结束 -->
    <!-- 挂号项目工具栏开始 -->
    <el-card class="box-card">
      <el-row>
        <el-col :span="12">
          <el-radio-group v-model="deptQueryParam.regItemId" @change="HanldeRegChange">
            <el-radio-button
              v-for="r in regItemOptions"
              :key="r.regItemId"
              :value="r.regItemId"
              :label="r.regItemId"
            >
              {{ r.regItemName }}
            </el-radio-button>
          </el-radio-group>
        </el-col>
        <el-col :span="12">
          <div style="float:right">
            <span style="margin-right:20px">挂号费:￥{{ deptQueryParam.regItemAmount }}</span>
            <el-button type="danger" icon="el-icon-search" :disabled="single" size="small" @click="handleRegistration">挂号收费</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 挂号项目工具栏结束-->
    <!-- 科室查询开始 -->
    <el-card>
      <el-form ref="queryDeptForm" :model="deptQueryParam" :inline="true" label-width="68px">
        <el-form-item label="所属科室" prop="deptId">
          <el-select
            v-model="deptQueryParam.deptId"
            placeholder="请选择所属科室"
            clearable
            size="small"
            style="width:200px"
          >
            <el-option
              v-for="d in deptOptions"
              :key="d.deptId"
              :label="d.deptName"
              :value="d.deptId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="挂号类型" prop="schedulingType">
          <el-select
            v-model="deptQueryParam.schedulingType"
            placeholder="请选择挂号类型"

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
            v-model="deptQueryParam.subsectionType"
            placeholder="请选择挂号时段"

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
        <el-form-item label="挂号时间" prop="schedulingDay">
          <el-date-picker
            v-model="deptQueryParam.schedulingDay"
            size="small"
            style="width:240px"
            value-format="yyyy-MM-dd"
            type="date"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleDeptQuery">搜索</el-button>
          <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetDeptQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <!-- 部门数据表格开始 -->
      <el-table v-loading="loading" border :data="deptTableList" highlight-current-row @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="科室ID" align="center" prop="deptId" />
        <el-table-column label="科室名称" align="center" prop="deptName" />
        <el-table-column label="当前号数" align="center" prop="regNumber" />
      </el-table>
      <!-- 部门数据表格结束 -->
    </el-card>
    <!-- {{ deptQueryParam }} -->
    <!-- {{ deptQueryParam.schedulingDay }} -->
  </div>
</template>

<script>
import { selectAllDept } from '@/api/system/dept/dept'
import { getDataByType } from '@/api/system/dict/data'
import { selectAllRegisteredItem } from '@/api/system/registeredItem/registeredItem'
import { listDeptForScheduling, getPatientByIdCard, addRegistration, collectFee } from '@/api/doctor/registration/registration'
export default {
  data() {
    return {
      // 遮罩层
      loading: false,
      // 患者查询参数
      patientParams: {
      },
      // 选中部门Ids
      ids: [],
      // 非单个禁用
      single: true,
      // 科室下拉列表数据
      deptOptions: [],
      // 性别数据字典
      sexOptions: [],
      // 挂号类型
      schedulingTypeOptions: [],
      // 排班时段
      subsectionTypeOptions: [],

      // 所有挂号项目
      regItemOptions: [],
      // 科室查询参数
      deptQueryParam: {
        deptId: undefined,
        schedulingType: '1',
        subsectionType: '2',
        schedulingDay: '',

        // 挂号费用
        regItemAmount: undefined,
        // 挂号项目id
        regItemId: undefined

      },
      // 部门表格数据
      deptTableList: [],

      // 表单校验
      rules: {
        name: [
          { required: true, message: '患者姓名不能为空', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          { pattern: /^1[3|4|5|7|8|9][0-9]\d{8}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: '身份证号不能为空', trigger: 'blur' }
        ]
      }

    }
  },
  watch: {
    patientParams: {
      handler: function() {
        if (this.patientParams.birthday !== undefined) {
          this.patientParams.age = this.getAge(this.patientParams.birthday.substring(0, 10))
        }
      },
      deep: true
    }
  },
  created() {
    // 默认设置挂号日期
    this.deptQueryParam.schedulingDay = this.moment(new Date()).format('YYYY-MM-DD')

    // 默认显示挂号时段上午下午晚上
    const subsectionType = this.getCurrentTimeType()
    if (subsectionType !== undefined) {
      this.deptQueryParam.subsectionType = subsectionType
    }

    this.patientParams.sex = '2'
    getDataByType('sys_user_sex').then(res => {
      this.sexOptions = res.data
    })
    getDataByType('his_scheduling_type').then(res => {
      this.schedulingTypeOptions = res.data
    })
    getDataByType('his_subsection_type').then(res => {
      this.subsectionTypeOptions = res.data
    })
    selectAllRegisteredItem().then(res => {
      this.regItemOptions = res.data
      this.deptQueryParam.regItemId = res.data[0].regItemId
      this.deptQueryParam.regItemAmount = res.data[0].regItemFee
    })
    selectAllDept().then(res => {
      this.deptOptions = res.data
    })

    // 加载有号的科室
    this.handleDeptQuery()
  },
  methods: {
    // 查询患者信息
    HandleIdCardQuery() {
      this.loading = true
      if (this.patientParams.idCard === undefined) {
        this.msgError('请输入身份证号')
        this.loading = false
        return
      }
      getPatientByIdCard(this.patientParams.idCard).then(res => {
        this.loading = false
        this.patientParams = res.data
        this.patientParams.age = this.getAge(res.data.birthday)
      }).catch(() => {
        this.loading = false
        this.patientParams = { sex: '2' }
      })
    },
    // 挂号收费
    handleRegistration() {
      const tx = this
      tx.$refs['form'].validate(vaid => {
        if (vaid) {
          tx.$confirm('确定要给患者【' + tx.patientParams.name + '】进行挂号吗', '挂号', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            closeOnClickModal: false,
            type: 'waring',
            center: true

          }).then(() => {
            // 组装添加挂号参数

            const data = { 'patientDto': tx.patientParams, 'registrationDto': {
              'deptId': tx.ids[0],
              'regItemId': tx.deptQueryParam.regItemId,
              'regItemAmount': tx.deptQueryParam.regItemAmount,
              'visitDate': tx.deptQueryParam.schedulingDay,
              'schedulingType': tx.deptQueryParam.schedulingType,
              'subsectionType': tx.deptQueryParam.subsectionType

            }}
            // 后台添加挂号
            addRegistration(data).then(res => {
              const regId = res.data
              tx.msgSuccess('挂号成功,挂号单为【' + regId + '】')
              this.resetDeptQuery()
              this.handleDeptQuery()
              this.patientParams = { sex: '2' }
              // 现金收费
              tx.$confirm('是否要对挂号单【' + regId + '】进行收费？', '现金收费', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                closeOnClickModal: false,
                type: 'waring',
                center: true
              }).then(() => {
                collectFee(regId).then(res => {
                  tx.msgSuccess('现金收费成功')
                  this.resetDeptQuery()
                  this.handleDeptQuery()
                }).catch(() => {
                  tx.msgError('现金收费失败')
                })
              }).catch(() => {
                tx.msgError('取消收费')
              })
            }).catch(() => {
              tx.msgError('挂号失败')
            })
          }).catch(() => {
            tx.msgError('已取消挂号')
          })
        }
      })
    },
    // 挂号项目变化，费用变化
    HanldeRegChange(regItemId) {
      // 绑定挂号费用
      this.regItemOptions.filter(item => {
        if (item.regItemId === regItemId) {
          this.deptQueryParam.regItemId = item.regItemId
          this.deptQueryParam.regItemAmount = item.regItemFee
        }
      })
      // 更换挂号类型
      if (regItemId === 1 || regItemId === 2) {
        this.deptQueryParam.schedulingType = '1'
      } else {
        this.deptQueryParam.schedulingType = '2'
      }
      // 重新查询部门数据
      this.handleDeptQuery()
    },
    // 搜索科室
    handleDeptQuery() {
      this.loading = true
      if (this.deptQueryParam.schedulingDay === null) {
        this.msgError('请先选中挂号日期')
        this.loading = false
      }
      listDeptForScheduling(this.deptQueryParam).then(res => {
        this.deptTableList = res.data
        this.loading = false
      })
    },
    // 重置科室
    resetDeptQuery() {
      this.deptQueryParam.deptId = undefined
      this.deptTableList = []
    },
    // 科室表格选中事件
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.deptId)
      this.single = selection.length !== 1
    }
  }

}
</script>

<style lang="scss" scoped>
.box-card{
  padding: 0px;
  margin: 0px;
  margin-bottom: 8px;

}
</style>
