<template>
  <!-- 单据审核页面 -->
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="供应商" prop="providerId">
        <el-select v-model="queryParams.providerId" placeholder="请选择供应商" clearable size="small" style="width:180px">
          <el-option
            v-for="provider in providerOptions"
            :key="provider.providerId"
            :label="provider.providerName"
            :value="provider.providerId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="申请人" prop="applyUserName">
        <el-input v-model="queryParams.applyUserName" placeholder="请输入申请人" clearable size="small" style="width:240px" />
      </el-form-item>
      <el-form-item label="单据状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="可用状态" clearable size="small" style="width:240px">
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
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleAuditPass">审核通过</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleAuditNoPass">审核不通过</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handlePurchaseItem">查看详情</el-button>
      </el-col>
    </el-row>
    <!-- 表头按钮结束 -->

    <!-- 数据表格开始 -->
    <el-table v-loading="loading" border :data="purchaseTableList" @selection-change="handleSelectionChnage">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="purchaseId" label="单据ID" align="center">
        <template slot-scope="scope">
          <!-- 去到修改单据页面，和新增单据页面相似 -->
          <router-link :to="'/stock/editpurchase/'+scope.row.purchaseId" class="link-type">
            <span>{{ scope.row.purchaseId }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="providerId" label="供应商" align="center" :formatter="providerDormatter" />
      <el-table-column prop="purchaseTradeTotalAmount" label="采购批发总额" align="center" :formatter="TotalAmountFormatter" width="130px" />
      <el-table-column label="状态" prop="status" align="center" :formatter="statusFormatter" width="130px" />
      <el-table-column prop="applyUserName" label="申请人" align="center" width="130px" />
      <el-table-column prop="storageOptUser" label="入库操作人" align="center" width="130px" />
      <el-table-column prop="storageOptTime" label="入库时间" align="center" />
      <el-table-column label="创建时间" prop="createTime" align="center" width="180" />
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
    <!-- 查看单据详情弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1068px"
    >
      <el-table
        v-loading="taleloading"
        :data="purchseItemData"
        border
        empty-text="数据加载中。。"
      >
        <el-table-column property="itemId" label="详情ID" width="180px" />
        <el-table-column property="purchaseId" label="采购单据ID" width="200px" />
        <el-table-column property="medicinesName" label="药品名称" width="auto" />
        <el-table-column property="purchaseNumber" label="采购数量" />
        <el-table-column property="tradePrice" label="批发价" />
        <el-table-column property="tradeTotalAmount" label="批发额" />
        <el-table-column property="batchNumber" label="批次号" />
        <el-table-column property="conversion" label="换算量" />
        <el-table-column property="unit" label="单位" />
        <el-table-column property="remark" label="备注" />
      </el-table>
    </el-dialog>
    <!-- 查看单据详情弹出层结束 -->
  </div>
</template>
<script>
import { listPurchasePendingForPage, auditPass, auditNoPass, getPurchaseItemById } from '@/api/erp/purchase/purchase'
import { selectAllProvider } from '@/api/erp/provider/provider'
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
      // 弹出层是否开启
      open: false,
      // 弹出层标题
      title: undefined,
      // 表格加载
      taleloading: false,
      // 单据详情数据
      purchseItemData: [],
      // 字典数据表格数据
      purchaseTableList: [],
      // 状态数据字典
      statusOptions: [],
      // 供应商数据
      providerOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        providerId: undefined,
        status: '2',
        applyUserName: undefined
      }

    }
  },
  created() {
    this.getStockPurchaseList()
    // 单据状态下拉框
    this.getDataByType('his_order_status').then(res => {
      this.statusOptions = res.data
    })
    // 加载供应商下拉框数据
    selectAllProvider().then(res => {
      this.providerOptions = res.data
    })
  },
  methods: {
    // 查询字典数据
    getStockPurchaseList() {
      this.loading = true
      listPurchasePendingForPage(this.queryParams).then(res => {
        this.loading = false
        this.purchaseTableList = res.data
        this.total = res.total
      })
    },
    // 条件查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getStockPurchaseList()
    },
    // 重置查询条件
    resetQuery() {
      this.resetForm('queryForm')
      this.getStockPurchaseList()
    },
    // 数据表格的多选择框选择时触发
    handleSelectionChnage(selection) {
      this.ids = selection.map(item => item.purchaseId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      // 重新查询
      this.getStockPurchaseList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      // 重新查询
      this.getStockPurchaseList()
    },
    // 翻译状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 翻译供应商名称
    providerDormatter(row) {
      return this.selectProviderLabel(this.providerOptions, row.providerId)
    },
    // 采购批发总额格式化
    TotalAmountFormatter(row) {
      return row.purchaseTradeTotalAmount.toFixed(2)
    },
    // 审核通过
    handleAuditPass() {
      const purchaseId = this.ids[0]
      this.$confirm('是否批准ID为【' + purchaseId + '】的采购单据?', '同意审核', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        auditPass(purchaseId).then(() => {
          this.msgSuccess('批准单据成功!')
          this.getStockPurchaseList()
        }).catch(() => {
          this.msgError('批准单据失败!')
        })
      }).catch(() => {
        this.msgInfo('取消批准单据!')
      })
    },
    // 审核不通过
    handleAuditNoPass() {
      const purchaseId = this.ids[0]
      this.$prompt('请输入不通过理由', '回绝单据', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        auditNoPass(purchaseId, value).then(res => {
          this.getStockPurchaseList()
          this.$message({
            type: 'success',
            message: '单据已打回'
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消回绝'
        })
      })
    },
    // 查看详情
    handlePurchaseItem() {
      this.taleloading = true
      this.open = true
      const purchaseId = this.ids[0]
      this.title = '单据ID为【' + purchaseId + '】的详情信息'
      getPurchaseItemById(purchaseId).then(res => {
        this.purchseItemData = res.data
        this.taleloading = false
      })
    }
  }
}
</script>
