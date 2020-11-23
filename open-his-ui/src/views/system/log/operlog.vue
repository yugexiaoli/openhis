<template>
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="系统模块" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入系统模块"
          clearable
          size="small"
          style="width:240px"
        />
      </el-form-item>
      <el-form-item label="操作人员" prop="operName">
        <el-input
          v-model="queryParams.operName"
          placeholder="请输入操作人员"
          clearable
          size="small"
          style="width:240px"
        />
      </el-form-item>
      <el-form-item label="操作类型" prop="businessType">
        <el-select
          v-model="queryParams.businessType"
          placeholder="请选择操作类型"
          clearable
          size="small"
          style="width:240px"
        >
          <el-option
            v-for="type in businessTypeOptions"
            :key="type.dictValue"
            :label="type.dictLabel"
            :value="type.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请求状态"
          clearable
          size="small"
          style="width:240px"
        >
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
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleClear">清空</el-button>
      </el-col>

    </el-row>
    <!-- 表头按钮结束 -->

    <!-- 数据表格开始 -->
    <el-table v-loading="loading" border :data="LogDataTableList" @selection-change="handleSelectionChnage">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="expand" label="详情" width="55">
        <template slot-scope="props">
          <el-form label-position="left" inline class="LogDataTableList_table_expand">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-form-item label="操作模块">
                  <span>{{ props.row.title }}</span>
                </el-form-item><br>
                <el-form-item label="请求地址">
                  <span>{{ props.row.operUrl }}</span>
                </el-form-item><br>
                <el-form-item label="请求参数">
                  <span>{{ props.row.operParam }}</span>
                </el-form-item><br>
                <el-form-item label="操作状态">
                  <span>{{ props.row.status == 0 ? '成功':'异常' }}</span>
                </el-form-item><br>
                <el-form-item label="异常信息">
                  <span>{{ props.row.errorMsg }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="登录信息">
                  <span>{{ props.row.operName + ' // ' + props.row.operIp + ' // ' + props.row.operLocation }}</span>
                </el-form-item><br>
                <el-form-item label="操作方法">
                  <span>{{ props.row.method }}</span>
                </el-form-item><br>
                <el-form-item label="返回参数">
                  <span>{{ props.row.jsonResult }}</span>
                </el-form-item><br>
                <el-form-item label="操作时间">
                  <span>{{ props.row.operTime }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column label="日志ID" prop="operId" align="center" />
      <el-table-column label="系统模块" prop="title" align="center" />
      <el-table-column label="操作类型" prop="businessType" :formatter="operTypeFormatter" align="center" />
      <el-table-column label="请求方式" prop="requestMethod" align="center" />
      <el-table-column label="操作人员" prop="operName" align="center" />
      <el-table-column label="主机" prop="operIp" align="center" :show-overflow-tooltip="true" />
      <el-table-column label="操作地点" prop="operLocation" align="center" :show-overflow-tooltip="true" />
      <el-table-column label="操作状态" prop="status" align="center" :formatter="statusFormatter" :show-overflow-tooltip="true" />
      <el-table-column label="操作时间" prop="operTime" align="center" width="180" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row)">删除</el-button>
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

  </div>
</template>
<script>
import { listForPage, deleteOperLogByIds, clearAllOperLog } from '@/api/system/log/operlog'
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
      LogDataTableList: [],
      // 状态数据字典
      statusOptions: [],
      // 操作类型字典
      businessTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined
      }

    }
  },
  created() {
    this.getDictDataList()
    // 使用全局的根据字典类型查询字典数据的方法查询字典数据
    this.getDataByType('sys_common_status').then(res => {
      this.statusOptions = res.data
    })
    // 使用全局的根据字典类型查询字典数据的方法查询字典数据
    this.getDataByType('sys_oper_type').then(res => {
      this.businessTypeOptions = res.data
    })
  },
  methods: {
    // 查询操作日志数据
    getDictDataList() {
      this.loading = true
      listForPage(this.queryParams).then(res => {
        this.loading = false
        this.LogDataTableList = res.data
        this.total = res.total
      })
    },
    // 条件查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getDictDataList()
    },
    // 重置查询条件
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.dictType = this.defaultDictType
      this.getDictDataList()
    },
    // 数据表格的多选择框选择时触发
    handleSelectionChnage(selection) {
      this.ids = selection.map(item => item.operId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      // 重新查询
      this.getDictDataList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      // 重新查询
      this.getDictDataList()
    },
    // 翻译状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 翻译操作类型
    operTypeFormatter(row) {
      return this.selectDictLabel(this.businessTypeOptions, row.businessType)
    },
    // 重置表单
    reset() {
      this.form = {
        operId: undefined,
        dictLable: undefined,
        dictValue: undefined,
        dictType: undefined,
        status: '0',
        dictSort: 0,
        remark: undefined
      }
      this.resetForm('form')
    },
    // 进行删除
    handleDelete(row) {
      const operIds = row.operId || this.ids
      this.$confirm('此操作将永久删除该字典数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteOperLogByIds(operIds).then(res => {
          this.loading = false
          this.msgSuccess('删除成功')
          this.getDictDataList()// 全查询
        })
      }).catch(() => {
        this.msgError('删除已取消')
        this.loading = false
      })
    },
    // 清空日志
    handleClear() {
      this.loading = true
      clearAllOperLog().then(res => {
        this.msgSuccess('清空日志成功')
        this.loading = false
        this.getDictDataList()
      })
    }

  }
}
</script>
<style scoped>
.LogDataTableList_table_expand el-col{
    padding-right: 8px;
}
</style>
