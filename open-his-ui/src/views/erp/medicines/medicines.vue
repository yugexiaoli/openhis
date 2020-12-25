<template>
  <!-- 药品信息页面 -->
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="药品名称" prop="medicinesName">
        <el-input v-model="queryParams.medicinesName" placeholder="请输入药品名称" clearable size="small" style="width:180px" />
      </el-form-item>
      <el-form-item label="关键字" prop="keywords">
        <el-input v-model="queryParams.keywords" placeholder="请输入关键字" clearable size="small" style="width:180px" />
      </el-form-item>
      <el-form-item label="药品类型" prop="medicinesType">
        <el-select v-model="queryParams.medicinesType" placeholder="药品类型" clearable size="small" style="width:180px">
          <el-option
            v-for="dict in medicinesTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生产厂家" prop="producterId">
        <el-select v-model="queryParams.producterId" placeholder="生产厂家" clearable size="small" style="width:180px">
          <el-option
            v-for="producter in producterOptions"
            :key="producter.producterId"
            :label="producter.producterName"
            :value="producter.producterId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="处方类型" prop="prescriptionType">
        <el-select v-model="queryParams.prescriptionType" placeholder="处方类型" clearable size="small" style="width:180px">
          <el-option
            v-for="dict in prescriptionTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="可用状态" clearable size="small" style="width:180px">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询条件结束 -->

    <!-- 表头按钮开始 -->
    <el-row :gutter="10" style="margin-bottom: 8px;">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" @click="handleImportDeleted">导入删除药品</el-button>
      </el-col>

    </el-row>
    <!-- 表头按钮结束 -->

    <!-- 数据表格开始 -->
    <el-table v-loading="loading" empty-text="数据加载中。。。" border :data="stockMedicinesTableList" @selection-change="handleSelectionChnage">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="medicinesId" label="药品ID" align="center" width="80px" />
      <el-table-column prop="medicinesName" label="药品名称" align="center" />
      <el-table-column prop="medicinesNumber" label="药品编码" align="center" />
      <el-table-column prop="producterId" label="生产厂家" align="center" width="200px" :formatter="producterFormatter" />
      <el-table-column prop="medicinesType" label="药品类型" align="center" :formatter="medicinesTypeFormatter" />
      <el-table-column prop="prescriptionType" label="处方类型" align="center" :formatter="prescriptionTypeFormatter" />
      <el-table-column prop="unit" label="单位" align="center" width="50px" />
      <el-table-column prop="prescriptionPrice" label="处方价格" align="center" width="80px" />
      <el-table-column prop="medicinesStockNum" label="库存量" align="center" width="80px" />
      <el-table-column prop="medicinesStockDangerNum" label="预警值" align="center" width="80px" />
      <el-table-column prop="conversion" label="换算量" align="center" width="80px" />
      <el-table-column label="状态" prop="status" align="center" :formatter="statusFormatter" width="80px" />
      <el-table-column label="操作" align="center" width="200px">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" size="mini" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button type="text" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="text" icon="el-icon-plus" size="mini" @click="handleUpdateStorage(scope.row)">调整库存</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 数据表格结束 -->
    <!-- 分页组件开始 -->
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
    <!-- 分页组件结束 -->

    <!-- 添加和修改的弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="700px"
      center
      append-to-body
    >

      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item prop="medicinesName" label="药品名称">
              <el-input v-model="form.medicinesName" size="small" placeholder="请输入药品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="medicinesNumber" label="药品编码">
              <el-input v-model="form.medicinesNumber" size="small" placeholder="请输入药品编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="keywords" label="关键字">
              <el-input v-model="form.keywords" size="small" placeholder="请输入关键字" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="unit" label="单位">
              <el-input v-model="form.unit" size="small" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">

            <el-form-item prop="conversion" label="换算量">
              <el-input v-model="form.conversion" size="small" placeholder="请输入换算量" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="prescriptionPrice" label="处方价格">
              <el-input-number v-model="form.prescriptionPrice" size="small" placeholder="请输入处方价格" />
            </el-form-item>

          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="medicinesStockNum" label="库存量">
              <el-input v-model="form.medicinesStockNum" size="small" placeholder="请输入库存量" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="medicinesStockDangerNum" label="预警值">
              <el-input-number v-model="form.medicinesStockDangerNum" size="small" placeholder="请输入预警值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">

            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                  :value="dict.dictValue"
                >{{ dict.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="药品类型" prop="medicinesType">
              <el-select v-model="form.medicinesType" placeholder="药品类型" clearable size="small" style="width:180px">
                <el-option

                  v-for="dict in medicinesTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="生产厂家" prop="producterId">
              <el-select v-model="form.producterId" placeholder="生产厂家" clearable size="small" style="width:180px">
                <el-option
                  v-for="producter in producterOptions"
                  :key="producter.producterId"
                  :label="producter.producterName"
                  :value="producter.producterId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">

            <el-form-item label="处方类型" prop="prescriptionType">
              <el-select v-model="form.prescriptionType" placeholder="处方类型" clearable size="small" style="width:180px">
                <el-option
                  v-for="dict in prescriptionTypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 添加和修改的弹出层结束 -->
  </div>
</template>
<script>
import { listMedicinesForPage, addMedicines, updateMedicines, getMedicinesById, deleteMedicinesByIds, updateMedicinesStorage } from '@/api/erp/medicines/medicines'
import { selectAllProducter } from '@/api/erp/producter/producter'
export default {
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
      // 字典数据表格数据
      stockMedicinesTableList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 药品类型
      medicinesTypeOptions: [],
      // 生产厂家数据
      producterOptions: [],
      // 处方类型
      prescriptionTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        medicinesName: undefined,
        medicinesType: undefined,
        prescriptionType: undefined,
        keywords: undefined,
        producterId: undefined,
        status: undefined
      },
      // 表单数据
      form: {
        medicinesId: undefined,
        medicinesNumber: undefined,
        medicinesName: undefined,
        medicinesType: undefined,
        prescriptionType: undefined,
        prescriptionPrice: '200',
        unit: undefined,
        conversion: undefined,
        keywords: undefined,
        producterId: undefined,
        status: '0',
        medicinesStockNum: undefined,
        medicinesStockDangerNum: '100'
      },
      // 表单校验
      rules: {
        medicinesName: [
          { required: true, message: '药品名称不能为空', trigger: 'blur' }
        ],
        keywords: [
          { required: true, message: '关键词不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    // 状态
    this.getDataByType('sys_normal_disable').then(res => {
      this.statusOptions = res.data
    })
    // 药品类型
    this.getDataByType('his_medicines_type').then(res => {
      this.medicinesTypeOptions = res.data
    })
    // 生产厂家数据
    selectAllProducter().then(res => {
      this.producterOptions = res.data
      this.getStockMedicinesList()
    })
    // 处方类型
    this.getDataByType('his_prescription_type').then(res => {
      this.prescriptionTypeOptions = res.data
    })
  },
  methods: {
    // 查询字典数据
    getStockMedicinesList() {
      this.loading = true
      listMedicinesForPage(this.queryParams).then(res => {
        this.loading = false
        this.stockMedicinesTableList = res.data
        this.total = res.total
      })
    },
    // 条件查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getStockMedicinesList()
    },
    // 重置查询条件
    resetQuery() {
      this.resetForm('queryForm')
      this.getStockMedicinesList()
    },
    // 数据表格的多选择框选择时触发
    handleSelectionChnage(selection) {
      this.ids = selection.map(item => item.medicinesId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      // 重新查询
      this.getStockMedicinesList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      // 重新查询
      this.getStockMedicinesList()
    },
    // 翻译状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 翻译厂家
    producterFormatter(row) {
      return this.selectProducterLabel(this.producterOptions, row.producterId)
    },
    // 翻译药品类型
    medicinesTypeFormatter(row) {
      return this.selectDictLabel(this.medicinesTypeOptions, row.medicinesType)
    },
    // 处方类型
    prescriptionTypeFormatter(row) {
      return this.selectDictLabel(this.prescriptionTypeOptions, row.prescriptionType)
    },
    // 重置表单
    reset() {
      this.form = {
        medicinesId: undefined,
        medicinesNumber: undefined,
        medicinesName: undefined,
        medicinesType: undefined,
        prescriptionType: undefined,
        prescriptionPrice: '200',
        unit: undefined,
        conversion: undefined,
        keywords: undefined,
        producterId: undefined,
        status: '0',
        medicinesStockNum: undefined,
        medicinesStockDangerNum: '100'
      }
      this.resetForm('form')
    },
    // 打开添加的弹出层
    handleAdd() {
      this.title = '添加药品'
      this.open = true
      this.reset()
    },
    // 打开修改的弹出层
    handleUpdate(row) {
      this.title = '修改药品'
      this.open = true
      this.reset()
      const medicinesId = row.medicinesId || this.ids
      // 根据字典数据ID查询字典数据
      getMedicinesById(medicinesId).then(res => {
        this.form = res.data
      })
    },
    // 进行删除
    handleDelete(row) {
      const medicinesIds = row.medicinesId || this.ids
      this.$confirm('此操作将永久删除ID为【' + medicinesIds + '】药品数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteMedicinesByIds(medicinesIds).then(res => {
          this.loading = false
          this.msgSuccess('删除成功')
          this.getStockMedicinesList()// 全查询
        })
      }).catch(() => {
        this.msgError('删除已取消')
        this.loading = false
      })
    },
    // 保存
    handleSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          // 做添加
          this.loading = true
          if (this.form.medicinesId === undefined) {
            addMedicines(this.form).then(res => {
              this.msgSuccess('保存成功')
              this.loading = false
              this.getStockMedicinesList()// 列表重新查询
              this.open = false// 关闭弹出层
            }).catch(() => {
              this.loading = false
            })
          } else { // 做修改
            updateMedicines(this.form).then(res => {
              this.msgSuccess('修改成功')
              this.loading = false
              this.getStockMedicinesList()// 列表重新查询
              this.open = false// 关闭弹出层
            }).catch(() => {
              this.loading = false
            })
          }
        }
      })
    },
    // 取消
    cancel() {
      this.open = false
    },
    // 导入删除药品
    handleImportDeleted() {

    },
    // 调整库存
    handleUpdateStorage(row) {
      this.$prompt('请输入' + row.medicinesName + '的库存量', '修改库存', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: row.medicinesStockNum,
        inputPattern: /^[1-9]\d*$/,
        inputErrorMessage: '库存量格式不正确'
      }).then(({ value }) => {
        updateMedicinesStorage(row.medicinesId, value).then(res => {
          this.getStockMedicinesList()
          this.$message({
            type: 'success',
            message: '修改成功,库存量是: ' + value
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消修改'
        })
      })
    }
  }
}
</script>
