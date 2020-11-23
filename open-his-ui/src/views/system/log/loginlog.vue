<template>
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          size="small"
          style="width:180px"
        />
      </el-form-item>
      <el-form-item label="用户账户" prop="loginAccount">
        <el-input
          v-model="queryParams.loginAccount"
          placeholder="请输入用户账户"
          clearable
          size="small"
          style="width:180px"
        />
      </el-form-item>
      <el-form-item label="IP地址" prop="ipAddr">
        <el-input
          v-model="queryParams.ipAddr"
          placeholder="请输入IP地址"
          clearable
          size="small"
          style="width:180px"
        />
      </el-form-item>
      <el-form-item label="登录状态" prop="loginStatus">
        <el-select
          v-model="queryParams.loginStatus"
          placeholder="请选择登录状态"
          clearable
          size="small"
          style="width:180px"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="登录类型" prop="loginType">
        <el-select
          v-model="queryParams.loginType"
          placeholder="请选择登录类型"
          clearable
          size="small"
          style="width:180px"
        >
          <el-option
            v-for="type in LoginTypeOptions"
            :key="type.dictValue"
            :label="type.dictLabel"
            :value="type.dictValue"
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
      <el-table-column label="日志ID" prop="infoId" align="center" />
      <el-table-column label="用户名称" prop="userName" align="center" />
      <el-table-column label="登陆账号" prop="loginAccount" />
      <el-table-column label="IP" prop="ipAddr" align="center" />
      <el-table-column label="登录地址" prop="loginLocation" align="center" />
      <el-table-column label="浏览器" prop="browser" align="center" :show-overflow-tooltip="true" />
      <el-table-column label="操作系统" prop="os" align="center" :show-overflow-tooltip="true" />
      <el-table-column label="登录状态" prop="loginStatus" align="center" :formatter="statusFormatter" :show-overflow-tooltip="true" />
      <el-table-column label="用户类型" prop="loginType" align="center" :formatter="UserTypeFormatter" width="180" />
      <el-table-column label="登录时间" prop="loginTime" align="center" width="180" />
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
import { listForPage, deleteLoginInfoByIds, clearLoginInfo } from '@/api/system/log/loginlog'
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
      // 登录状态数据字典
      statusOptions: [],
      // 登录类型字典
      LoginTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        loginAccount: undefined,
        ipAddr: undefined,
        loginStatus: undefined,
        loginType: undefined
      },
      beginAndEndTime: []

    }
  },
  created() {
    this.getDictDataList()
    // 使用全局的根据字典类型查询字典数据的方法查询字典数据
    this.getDataByType('sys_common_status').then(res => {
      this.statusOptions = res.data
    })
    // 使用全局的根据字典类型查询字典数据的方法查询字典数据
    this.getDataByType('sys_user_type').then(res => {
      this.LoginTypeOptions = res.data
    })
  },
  methods: {
    // 查询操作日志数据
    getDictDataList() {
      this.loading = true
      listForPage(this.addDateRange(this.queryParams, this.beginAndEndTime)).then(res => {
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
      this.beginAndEndTime = []
      this.getDictDataList()
    },
    // 数据表格的多选择框选择时触发
    handleSelectionChnage(selection) {
      this.ids = selection.map(item => item.infoId)
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
      return this.selectDictLabel(this.statusOptions, row.loginStatus)
    },
    // 翻译操作类型
    UserTypeFormatter(row) {
      return this.selectDictLabel(this.LoginTypeOptions, row.loginType)
    },
    // 重置表单
    reset() {
      this.form = {
        infoId: undefined,
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
      const infoIds = row.infoId || this.ids
      this.$confirm('此操作将永久删除该字典数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteLoginInfoByIds(infoIds).then(res => {
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
      clearLoginInfo().then(res => {
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
