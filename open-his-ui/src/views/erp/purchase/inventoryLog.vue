<template>
  <!-- 药品信息页面 -->
  <div class="app-container">

    <el-tabs v-model="activeName" :stretch="true" @tab-click="handleClick">
      <el-tab-pane label="药品总库存" name="medicinesTab">
        <!-- 查询条件开始 -->
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
          <el-form-item label="药品名称" prop="medicinesName">
            <el-input v-model="queryParams.medicinesName" placeholder="请输入药品名称" clearable size="small" style="width:180px" />
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

          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 查询条件结束 -->

        <!-- 数据表格开始 -->
        <el-table v-loading="loading" empty-text="数据加载中。。。" border :data="stockMedicinesTableList">
          <el-table-column prop="medicinesId" label="药品ID" align="center" width="80px" />
          <el-table-column prop="medicinesName" label="药品名称" align="center" />
          <el-table-column prop="medicinesStockDangerNum" label="预警值" align="center" width="80px" />
          <el-table-column prop="producterId" label="生产厂家" align="center" width="200px" :formatter="producterFormatter" />
          <el-table-column prop="medicinesType" label="药品类型" align="center" :formatter="medicinesTypeFormatter" />
          <el-table-column prop="prescriptionType" label="处方类型" align="center" :formatter="prescriptionTypeFormatter" />
          <el-table-column prop="unit" label="单位" align="center" width="50px" />
          <el-table-column prop="conversion" label="换算量" align="center" width="80px" />
        </el-table>
        <!-- 数据表格结束 -->
        <!-- 分页组件开始 -->
        <el-pagination
          v-show="medicinetotal>0"
          :current-page="queryParams.pageNum"
          :page-sizes="[5, 10, 20, 30]"
          :page-size="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="medicinetotal"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
        <!-- 分页组件结束 -->

      </el-tab-pane>
      <!-- 库存tab -->
      <el-tab-pane label="批次库存及价格" name="inventoryLogTab">
        <!-- 查询条件开始 -->
        <el-form ref="queryParamsforInv" :model="queryParamsforInv" :inline="true" label-width="88px">
          <el-form-item label="采购单据号" prop="purchaseId">
            <el-input v-model="queryParamsforInv.purchaseId" placeholder="请输入采购单据号" clearable size="small" style="width:240px" />
          </el-form-item>
          <el-form-item label="药品名称" prop="medicinesName">
            <el-input v-model="queryParamsforInv.medicinesName" placeholder="请输入药品名称" clearable size="small" style="width:240px" />
          </el-form-item>
          <el-form-item label="药品类型" prop="medicinesType">
            <el-select v-model="queryParamsforInv.medicinesType" placeholder="药品类型" clearable size="small" style="width:180px">
              <el-option
                v-for="dict in medicinesTypeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="生产厂家" prop="producterId">
            <el-select v-model="queryParamsforInv.producterId" placeholder="生产厂家" clearable size="small" style="width:180px">
              <el-option
                v-for="producter in producterOptions"
                :key="producter.producterId"
                :label="producter.producterName"
                :value="producter.producterId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="处方类型" prop="prescriptionType">
            <el-select v-model="queryParamsforInv.prescriptionType" placeholder="处方类型" clearable size="small" style="width:180px">
              <el-option
                v-for="dict in prescriptionTypeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间" prop="beginAndEndTime">
            <el-date-picker
              v-model="beginAndEndTime"
              prefix-icon="el-icon-date"
              size="small"
              style="width:240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholde="开始日期"
              end-placeholde="结束日期"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 查询条件结束 -->
        <!-- 数据表格开始 -->
        <el-table v-loading="loading" border :data="stockInventoryLogTableList">

          <el-table-column prop="medicinesId" label="药品ID" align="center" />
          <el-table-column prop="medicinesName" label="药品名称" align="center" />
          <el-table-column prop="inventoryLogNum" label="采购量" align="center" />
          <el-table-column prop="tradePrice" label="批发价" align="center" />
          <el-table-column prop="tradeTotalAmount" label="批发额" align="center" />
          <el-table-column prop="batchNumber" label="批次号" align="center" />
          <el-table-column prop="producterId" label="生产厂家" align="center" width="200px" :formatter="producterFormatter" />
          <el-table-column prop="medicinesType" label="药品类型" align="center" :formatter="medicinesTypeFormatter" />
          <el-table-column prop="prescriptionType" label="处方类型" align="center" :formatter="prescriptionTypeFormatter" />
          <el-table-column label="创建时间" prop="createTime" align="center" width="180px" />
        </el-table>
        <!-- 数据表格结束 -->
        <!-- 分页组件开始 -->
        <el-pagination
          v-show="inventoryTotal>0"
          :current-page="queryParams.pageNum"
          :page-sizes="[5, 10, 20, 30]"
          :page-size="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="inventoryTotal"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
        <!-- 分页组件结束 -->
      </el-tab-pane>
    </el-tabs>

  </div>
</template>
<script>
import { listInventoryLogForPage } from '@/api/erp/inventory/inventory'
import { listMedicinesForPage } from '@/api/erp/medicines/medicines'
import { selectAllProducter } from '@/api/erp/producter/producter'
export default {
  data() {
    return {
      // 当前选中选项卡
      activeName: 'medicinesTab',
      // 遮罩层
      loading: false,
      // 药品总条数
      medicinetotal: 0,
      // 库存总条数
      inventoryTotal: 0,
      // 字典数据表格数据
      stockMedicinesTableList: [],
      // 库存表格数据
      stockInventoryLogTableList: [],
      // 药品类型
      medicinesTypeOptions: [],
      // 生产厂家数据
      producterOptions: [],
      // 处方类型
      prescriptionTypeOptions: [],
      // 药品查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        medicinesName: undefined,
        medicinesType: undefined,
        prescriptionType: undefined,
        producterId: undefined
      },
      // 库存查询参数
      queryParamsforInv: {
        pageNum: 1,
        pageSize: 10,
        purchaseId: undefined,
        medicinesName: undefined,
        medicinesType: undefined,
        prescriptionType: undefined,
        producterId: undefined
      },
      // 库存创建时间条件时间范围
      beginAndEndTime: []
    }
  },
  created() {
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
    this.getStockMedicinesList()
  },
  methods: {
    // 查询药品数据
    getStockMedicinesList() {
      this.loading = true
      listMedicinesForPage(this.queryParams).then(res => {
        this.loading = false
        this.stockMedicinesTableList = res.data
        this.medicinetotal = res.total
      })
    },
    // 查询库存数据
    getStockInventoryLogList() {
      this.loading = true
      listInventoryLogForPage(this.addDateRange(this.queryParamsforInv, this.beginAndEndTime)).then(res => {
        this.loading = false
        this.stockInventoryLogTableList = res.data
        this.inventoryTotal = res.total
      })
    },
    // 条件查询
    handleQuery() {
      if (this.activeName === 'medicinesTab') {
        this.queryParams.pageNum = 1
        this.getStockMedicinesList()
      } else if (this.activeName === 'inventoryLogTab') {
        this.queryParams.pageNum = 1
        this.getStockInventoryLogList()
      }
    },
    // 重置查询条件
    resetQuery() {
      if (this.activeName === 'medicinesTab') {
        this.resetForm('queryParams')
        this.getStockMedicinesList()
      } else if (this.activeName === 'inventoryLogTab') {
        this.resetForm('queryParamsforInv')
        this.beginAndEndTime = []
        this.getStockInventoryLogList()
      }
    },

    // 分页pageSize变化时触发
    handleSizeChange(val) {
      if (this.activeName === 'medicinesTab') {
        this.queryParams.pageSize = val
        this.getStockMedicinesList()
      } else if (this.activeName === 'inventoryLogTab') {
        this.queryParamsforInv.pageSize = val
        this.getStockInventoryLogList()
      }
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      if (this.activeName === 'medicinesTab') {
        this.queryParams.pageNum = val
        this.getStockMedicinesList()
      } else if (this.activeName === 'inventoryLogTab') {
        this.queryParamsforInv.pageNum = val
        this.getStockInventoryLogList()
      }
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
    // 选项卡点击事件
    handleClick(tab, event) {
      if (this.activeName === 'medicinesTab') {
        this.getStockMedicinesList()
      } else if (this.activeName === 'inventoryLogTab') {
        this.getStockInventoryLogList()
      }
    }

  }
}
</script>
