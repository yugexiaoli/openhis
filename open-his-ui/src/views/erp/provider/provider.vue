<template>
  <!-- 供应商信息表页面 -->
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="100px">
      <el-form-item label="供应商名称" prop="providerName">
        <el-input v-model="queryParams.providerName" placeholder="请输入供应商名称" clearable size="small" style="width:180px" />
      </el-form-item>
      <el-form-item label="联系人" prop="contactName">
        <el-input v-model="queryParams.contactName" placeholder="请输入联系人名称" clearable size="small" style="width:180px" />
      </el-form-item>
      <el-form-item label="供应商电话" prop="contactTel">
        <el-input v-model="queryParams.contactTel" placeholder="请输入供应商电话" clearable size="small" style="width:180px" />
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

    </el-row>
    <!-- 表头按钮结束 -->

    <!-- 数据表格开始 -->
    <el-table v-loading="loading" border :data="ProviderTableList" @selection-change="handleSelectionChnage">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="providerId" label="供应商ID" align="center" width="88px" />
      <el-table-column prop="providerName" label="供应商名称" align="center" width="180px" />
      <el-table-column prop="contactName" label="联系人" align="center" />
      <el-table-column prop="contactMobile" label="联系人手机" align="center" />
      <el-table-column prop="contactTel" label="联系人电话" align="center" />
      <el-table-column prop="bankAccount" label="银行账号" align="center" width="180px" />
      <el-table-column prop="providerAddress" label="地址" align="center" />
      <el-table-column label="状态" prop="status" align="center" :formatter="statusFormatter" width="100px" />
      <el-table-column label="创建时间" prop="createTime" align="center" width="180" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" size="mini" @click="handleUpdate(scope.row)">修改</el-button>
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

    <!-- 添加和修改的弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="550px"
      center
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item prop="providerName" label="供应商名称">
          <el-input v-model="form.providerName" size="small" placeholder="请输入供应商名称" />
        </el-form-item>
        <el-form-item prop="contactName" label="联系人">
          <el-input v-model="form.contactName" size="small" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item prop="contactMobile" label="手机">
          <el-input v-model="form.contactMobile" size="small" placeholder="请输入手机" />
        </el-form-item>
        <el-form-item prop="contactTel" label="电话">
          <el-input v-model="form.contactTel" size="small" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item prop="bankAccount" label="银行账号">
          <el-input v-model="form.bankAccount" size="small" placeholder="请输入银行账号" />
        </el-form-item>
        <el-form-item prop="providerAddress" label="地址">
          <el-input v-model="form.providerAddress" size="small" placeholder="请输入地址" />
        </el-form-item>
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
import { listProviderForPage, addProvider, updateProvider, getProviderById, deleteProviderByIds } from '@/api/erp/provider/provider'
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
      ProviderTableList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        providerName: undefined,
        contactName: undefined,
        contactTel: undefined,
        status: undefined
      },
      // 表单数据
      form: {
        providerId: undefined,
        providerName: undefined,
        contactName: undefined,
        contactMobile: undefined,
        contactTel: undefined,
        bankAccount: undefined,
        providerAddress: undefined,
        status: '0'

      },
      // 表单校验
      rules: {
        providerName: [
          { required: true, message: '供应商名称不能为空', trigger: 'blur' }
        ],
        contactName: [
          { required: true, message: '联系人不能为空', trigger: 'blur' }
        ],
        contactMobile: [
          { required: true, message: '手机号不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getProviderList()
    // 使用全局的根据字典类型查询字典数据的方法查询字典数据
    this.getDataByType('sys_normal_disable').then(res => {
      this.statusOptions = res.data
    })
  },
  methods: {
    // 查询字典数据
    getProviderList() {
      this.loading = true
      listProviderForPage(this.queryParams).then(res => {
        this.loading = false
        this.ProviderTableList = res.data
        this.total = res.total
      })
    },
    // 条件查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getProviderList()
    },
    // 重置查询条件
    resetQuery() {
      this.resetForm('queryForm')
      this.getProviderList()
    },
    // 数据表格的多选择框选择时触发
    handleSelectionChnage(selection) {
      this.ids = selection.map(item => item.providerId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      // 重新查询
      this.getProviderList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      // 重新查询
      this.getProviderList()
    },
    // 翻译状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 重置表单
    reset() {
      this.form = {
        providerId: undefined,
        providerName: undefined,
        contactName: undefined,
        contactMobile: undefined,
        contactTel: undefined,
        bankAccount: undefined,
        providerAddress: undefined,
        status: '0'
      }
      this.resetForm('form')
    },
    // 打开添加的弹出层
    handleAdd() {
      this.title = '添加供应商'
      this.open = true
      this.reset()
    },
    // 打开修改的弹出层
    handleUpdate(row) {
      this.title = '修改供应商'
      this.open = true
      this.reset()
      const providerId = row.providerId || this.ids
      // 根据字典数据ID查询字典数据
      getProviderById(providerId).then(res => {
        this.form = res.data
      })
    },
    // 进行删除
    handleDelete(row) {
      const providerIds = row.providerId || this.ids
      this.$confirm('此操作将永久删除ID为【' + providerIds + '】的供应商数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteProviderByIds(providerIds).then(res => {
          this.loading = false
          this.msgSuccess('删除成功')
          this.getProviderList()// 全查询
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
          if (this.form.providerId === undefined) {
            addProvider(this.form).then(res => {
              this.msgSuccess('保存成功')
              this.loading = false
              this.getProviderList()// 列表重新查询
              this.open = false// 关闭弹出层
            }).catch(() => {
              this.loading = false
            })
          } else { // 做修改
            updateProvider(this.form).then(res => {
              this.msgSuccess('修改成功')
              this.loading = false
              this.getProviderList()// 列表重新查询
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
    }
  }
}
</script>
