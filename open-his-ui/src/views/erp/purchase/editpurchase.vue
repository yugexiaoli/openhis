<template>
  <div>
    <!-- 操作栏卡片开始 -->
    <el-card class="mycard">
      <el-row :gutter="10">
        <el-col :span="8" :offset="17">
          <el-button size="small" type="primary" icon="el-icon-plus" :disabled="isSubmit" @click="handleAddMedicines">添加药品</el-button>
          <el-button size="small" type="success" icon="el-icon-s-operation" :disabled="isSubmit" @click="handleSetting">批量设置</el-button>
          <el-button size="small" type="warning" icon="el-icon-check" :disabled="isSubmit" @click="handleAddPurchase">暂存</el-button>
          <el-button size="small" type="danger" icon="el-icon-finished" :disabled="isSubmit" @click="handleAddPurchaseToAudit">提交审核</el-button>
        </el-col>
      </el-row>
    </el-card>
    <!-- 操作栏卡片结束-->
    <!-- 主表单卡片开始 -->
    <el-card class="mycard">
      <el-form
        ref="form"
        :rules="rules"
        :model="form"
        label-width="90"
        inline
      >
        <el-form-item label="单据号" prop="purchaseId">
          <el-input v-model="form.purchaseId" style="width:220px;" disabled size="small" />
        </el-form-item>

        <el-form-item label="供应商" prop="providerId">
          <el-select
            v-model="form.providerId"
            size="small"
            placeholder="请选择供应商"
          >
            <el-option
              v-for="provider in providerOptions"
              :key="provider.providerId"
              :label="provider.providerName"
              :value="provider.providerId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="总批发额" prop="purchaseTradeTotalAmount">
          <el-input v-model="form.purchaseTradeTotalAmount" disabled size="small" />
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 主表单卡片结束 -->
    <!-- 单据详情表格卡片开始 -->
    <el-card class="mycard">
      <el-table
        :data="purchaseItemDataList"
        border
      >
        <el-table-column prop="medicinesId" label="药品ID" align="center" width="80" />
        <el-table-column prop="medicinesName" label="药品名称" align="center" width="180" />
        <el-table-column prop="conversion" label="规格" align="center" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.conversion }}{{ scope.row.unit }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="producterId" label="生产厂家" width="220" :formatter="producterFormatter" />
        <el-table-column align="center" prop="purchaseNumber" label="数量" width="160">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.purchaseNumber"
              :step="1"
              :min="1"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column align="center" prop="unit" label="单位" width="80" />
        <el-table-column align="center" prop="tradePrice" label="批发价" width="160">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.tradePrice"
              :step="0.01"
              :min="0.01"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column align="center" prop="tradeTotalAmount" label="批发额" width="120" :formatter="AmountFormatter" />
        <el-table-column align="center" prop="batchNumber" label="批次号" width="220">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.batchNumber"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column align="center" prop="remark" label="备注" width="220">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.remark"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="auto">
          <template slot-scope="scope">
            <el-button type="danger" :disabled="isSubmit" @click="handleDeleteItem(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 单据详情表格卡片结束-->
    <!-- 添加药品弹出层开始 -->
    <el-dialog
      :visible.sync="open"
      :title="title"
      append-to-body
      width="1200px"
    >
      <!-- 药品查询条件开始 -->
      <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">

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

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <!-- 药品查询条件结束 -->
      <!-- 药品数据表格开始 -->
      <el-table ref="stockMedicinesTableList" v-loading="loading" empty-text="数据加载中。。。" border :data="stockMedicinesTableList" @selection-change="handleSelectionChnage">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="medicinesId" label="药品ID" align="center" width="80px" />
        <el-table-column prop="medicinesName" label="药品名称" align="center" />
        <el-table-column prop="medicinesNumber" label="药品编码" align="center" />
        <el-table-column prop="producterId" label="生产厂家" align="center" width="200px" :formatter="producterFormatter" />
        <el-table-column prop="medicinesType" label="药品类型" align="center" :formatter="medicinesTypeFormatter" />
        <el-table-column prop="prescriptionType" label="处方类型" align="center" :formatter="prescriptionTypeFormatter" />
        <el-table-column prop="keywords" label="关键字" align="center" width="80px" />
      </el-table>
      <!-- 药品数据表格结束 -->
      <!-- 药品分页组件开始 -->
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
      <!-- 药品分页组件结束 -->
    </el-dialog>
    <!-- 添加药品弹出层结束 -->
    <!-- 批量设置弹出层开始 -->
    <el-dialog
      :visible.sync="bitchsetopen"
      :title="title"
      append-to-body
      width="550px"
      center
    >
      <el-form
        ref="batchsetForm"
        :model="batchsetForm"

        size="small"
        label-width="80px"
      >
        <el-form-item label="数量" prop="purchaseNumber">
          <el-input-number v-model="batchsetForm.purchaseNumber" :step="1" :min="1" />
        </el-form-item>
        <el-form-item label="批发价格" prop="tradePrice">
          <el-input-number v-model="batchsetForm.tradePrice" :precision="2" :step="0.01" :min="0" />
        </el-form-item>
        <el-form-item label="批次号" prop="batchNumber">
          <el-input v-model="batchsetForm.batchNumber" placeholder="请输入批次号" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="batchsetForm.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleBatchSetSubmit">确 定</el-button>
        <el-button @click="cancelBatchSet">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 批量设置弹出层结束 -->
    <!-- {{ purchaseItemDataList }} -->
  </div>
</template>

<script>
import { selectAllProducter } from '@/api/erp/producter/producter'
import { addPurchase, addPurchaseToAudit, queryPurchaseAndItemByPurchaseId } from '@/api/erp/purchase/purchase'
import { selectAllProvider } from '@/api/erp/provider/provider'
import { listMedicinesForPage } from '@/api/erp/medicines/medicines'
export default {
  data() {
    return {
      // 是否提交审核，用于禁用工具栏按钮
      isSubmit: true,
      // 表单对象
      form: {
        purchaseId: undefined,
        providerId: undefined,
        purchaseTradeTotalAmount: 0
      },
      // 表单校验
      rules: {
        providerId: [
          { required: true, message: '供应商不能为空', trigger: 'blur' }
        ]
      },
      // 供应商数据
      providerOptions: [],
      // 厂家数据
      producterOptions: [],
      // 单据详情表格数据
      purchaseItemDataList: [],
      // 添加药品弹出层显示
      open: false,
      // 添加药品弹出层标题
      title: '',
      // =====药品弹出层相关数据开始
      // 遮罩层
      loading: false,
      // 总条数
      total: 0,
      // 药品数据表格数据
      stockMedicinesTableList: [],
      // 药品类型
      medicinesTypeOptions: [],
      // 处方类型
      prescriptionTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        medicinesType: undefined,
        prescriptionType: undefined,
        keywords: undefined
      },
      // 选中的药品
      selectMedicines: [],
      // =====药品弹出层相关数据结束

      // 批量设置弹出层显示
      bitchsetopen: false,
      // 批量设置弹出层表单对象
      batchsetForm: {
        purchaseNumber: 0,
        tradePrice: 0.00,
        batchNumber: '',
        remark: ''
      }
    }
  },
  // 监听详情数据，改变总批发额
  watch: {
    purchaseItemDataList: {
      handler: function() {
        this.form.purchaseTradeTotalAmount = 0.00
        this.purchaseItemDataList.filter(item => {
          item.tradeTotalAmount = item.purchaseNumber * item.tradePrice
          this.form.purchaseTradeTotalAmount += item.tradeTotalAmount
        })
      },
      deep: true

    }

  },
  created() {
    // 加载供应商下拉框数据
    selectAllProvider().then(res => {
      this.providerOptions = res.data
    })
    // 加载生产厂家数据
    selectAllProducter().then(res => {
      this.producterOptions = res.data
    })
    // 药品类型
    this.getDataByType('his_medicines_type').then(res => {
      this.medicinesTypeOptions = res.data
    })
    // 处方类型
    this.getDataByType('his_prescription_type').then(res => {
      this.prescriptionTypeOptions = res.data
    })
    // 加载单据表单数据和详情数据
    const purchaseId = this.$route.params.purchaseId
    queryPurchaseAndItemByPurchaseId(purchaseId).then(res => {
      this.form = res.data.purchase
      this.purchaseItemDataList = res.data.items
      this.form.providerId = parseInt(this.form.providerId)
      if (res.data.purchase.status === '1' || res.data.purchase.status === '4') {
        this.isSubmit = false
      }
    })
  },
  methods: {
    // 翻译生产厂家
    producterFormatter(row) {
      return this.selectProducterLabel(this.producterOptions, row.producterId)
    },
    // 批发额格式化保留小数点2
    AmountFormatter(row) {
      return row.tradeTotalAmount.toFixed(2)
    },
    // 打开添加药品弹出层
    handleAddMedicines() {
      this.open = true
      this.title = '选择要添加的药品'
      this.getStockMedicinesList()
    },

    // ======添加药品弹出层方法开始
    // 查询药品数据
    getStockMedicinesList() {
      this.loading = true
      listMedicinesForPage(this.queryParams).then(res => {
        this.loading = false
        this.stockMedicinesTableList = res.data
        this.total = res.total
        this.$nextTick(() => {
          // 反选之前有的
          this.stockMedicinesTableList.filter(r1 => {
            this.purchaseItemDataList.filter(r2 => {
              if (parseInt(r1.medicinesId) === parseInt(r2.medicinesId)) {
                this.$refs.stockMedicinesTableList.toggleRowSelection(r1, true)
              }
            })
          })
        })
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
      this.selectMedicines = selection
      this.selectMedicines.filter(m1 => {
        let flag = false
        // 判断purchaseItemDataList里面有没有已选择的ID
        this.purchaseItemDataList.filter(purch => {
          if (parseInt(m1.medicinesId) === parseInt(purch.medicinesId)) {
            flag = true
          }
        })
        // 如果没有，就加入purchaseItemDataList
        if (!flag) {
          // 放到purchaseItemDataList对象是selection深度clone
          this.purchaseItemDataList.push(JSON.parse(JSON.stringify(m1)))
        }
      })
      // 剔除里面不用的属性，添加需要的属性
      this.purchaseItemDataList.filter(m => {
        this.$delete(m, 'status')
        this.$delete(m, 'medicinesStockNum')
        this.$delete(m, 'medicinesStockDangerNum')
        this.$delete(m, 'createTime')
        this.$delete(m, 'updateTime')
        this.$delete(m, 'createBy')
        this.$delete(m, 'updateBy')

        this.$set(m, 'purchaseNumber', m.purchaseNumber ? m.purchaseNumber : 1)
        this.$set(m, 'tradePrice', m.tradePrice ? m.tradePrice : 0.00)
        this.$set(m, 'tradeTotalAmount', m.tradeTotalAmount ? m.tradeTotalAmount : 0.00)
        this.$set(m, 'batchNumber', m.batchNumber ? m.batchNumber : '')
        this.$set(m, 'remark', m.remark ? m.remark : '')
      })
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
    // 翻译药品类型
    medicinesTypeFormatter(row) {
      return this.selectDictLabel(this.medicinesTypeOptions, row.medicinesType)
    },
    // 处方类型
    prescriptionTypeFormatter(row) {
      return this.selectDictLabel(this.prescriptionTypeOptions, row.prescriptionType)
    },
    // ======添加药品弹出层方法结束

    // 批量设置
    handleSetting() {
      this.bitchsetopen = true
      this.title = '批量设置'
    },
    // 批量设置提交
    handleBatchSetSubmit() {
      this.purchaseItemDataList.filter(item => {
        item.purchaseNumber = this.batchsetForm.purchaseNumber
        item.tradePrice = this.batchsetForm.tradePrice
        item.batchNumber = this.batchsetForm.batchNumber
        item.remark = this.batchsetForm.remark
      })
      this.bitchsetopen = false
    },
    // 批量设置取消
    cancelBatchSet() {
      this.bitchsetopen = false
    },
    // 暂存
    handleAddPurchase() {
      if (this.purchaseItemDataList.length > 0) {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            // 构造后台对象
            const purchaseObj = { 'purchaseDto': this.form, 'purchaseItemDtos': this.purchaseItemDataList }
            addPurchase(purchaseObj).then(res => {
              this.msgSuccess('暂存成功')
            }).catch(() => {
              this.msgError('暂存失败')
            })
          }
        })
      } else {
        this.msgError('药品不能为空')
      }
    },
    // 提交审核
    handleAddPurchaseToAudit() {
      if (this.purchaseItemDataList.length > 0) {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            // 构造后台对象
            const purchaseObj = { 'purchaseDto': this.form, 'purchaseItemDtos': this.purchaseItemDataList }
            addPurchaseToAudit(purchaseObj).then(res => {
              this.msgSuccess('提交审核成功')
              this.isSubmit = true
            }).catch(() => {
              this.msgError('提交审核失败')
            })
          }
        })
      } else {
        this.msgError('药品不能为空')
      }
    },
    // 删除一个单据详情
    handleDeleteItem(index) {
      this.purchaseItemDataList.splice(index, 1)
    }
  }
}
</script>

<style>
.mycard{
  margin: 10px;
  padding: 0;
  margin-bottom: 10px;
}
</style>
