<template>
  <!-- 挂号费用页面 -->
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="100px">
      <el-form-item label="挂号费名称" prop="regItemName">
        <el-input v-model="queryParams.regItemName" placeholder="请输入挂号项目名称" clearable size="small" style="width:240px" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
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
    <el-table v-loading="loading" border :data="sysRegisteredItemTableList" @selection-change="handleSelectionChnage">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="regItemId" label="挂号费ID" align="center" />
      <el-table-column prop="regItemName" label="挂号费名称" align="center" />
      <el-table-column prop="regItemFee" label="挂号费" align="center" :formatter="FeeFormatter" />
      <el-table-column label="状态" prop="status" align="center" :formatter="statusFormatter" width="180" />
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
      width="600px"
      center
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item prop="regItemName" label="挂号费名称">
          <el-input v-model="form.regItemName" placeholder="请输入挂号费名称" size="small" />
        </el-form-item>
        <el-form-item prop="regItemFee" label="金额">
          <el-input-number v-model="form.regItemFee" :precision="2" :step="10" size="small" />
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
import { listRegisteredItemForPage, addRegisteredItem, updateRegisteredItem, getRegisteredItemById, deleteRegisteredItemByIds } from '@/api/system/registeredItem/registeredItem'
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
      sysRegisteredItemTableList: [],
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
        regItemName: undefined,
        status: undefined
      },
      // 表单数据
      form: {
        regItemId: undefined,
        regItemName: undefined,
        regItemFee: '100.00',
        status: '0'
      },
      // 表单校验
      rules: {
        regItemName: [
          { required: true, message: '挂号费名称不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getSysRegisteredItemList()
    // 使用全局的根据字典类型查询字典数据的方法查询字典数据
    this.getDataByType('sys_normal_disable').then(res => {
      this.statusOptions = res.data
    })
  },
  methods: {
    // 查询字典数据
    getSysRegisteredItemList() {
      this.loading = true
      listRegisteredItemForPage(this.queryParams).then(res => {
        this.loading = false
        this.sysRegisteredItemTableList = res.data
        this.total = res.total
      })
    },
    // 条件查询
    handleQuery() {
      this.getSysRegisteredItemList()
    },
    // 重置查询条件
    resetQuery() {
      this.resetForm('queryForm')
      this.getSysRegisteredItemList()
    },
    // 数据表格的多选择框选择时触发
    handleSelectionChnage(selection) {
      this.ids = selection.map(item => item.regItemId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      // 重新查询
      this.getSysRegisteredItemList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      // 重新查询
      this.getSysRegisteredItemList()
    },
    // 翻译状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 挂号费用金额格式化
    FeeFormatter(row) {
      return row.regItemFee.toFixed(2)
    },
    // 重置表单
    reset() {
      this.form = {
        regItemId: undefined,
        regItemName: undefined,
        regItemFee: '100.00',
        status: '0'
      }
      this.resetForm('form')
    },
    // 打开添加的弹出层
    handleAdd() {
      this.title = '新增挂号费用'
      this.open = true
      this.reset()
    },
    // 打开修改的弹出层
    handleUpdate(row) {
      this.title = '修改挂号费用'
      this.open = true
      this.reset()
      const regItemId = row.regItemId || this.ids
      // 根据字典数据ID查询字典数据
      getRegisteredItemById(regItemId).then(res => {
        this.form = res.data
      })
    },
    // 进行删除
    handleDelete(row) {
      const regItemIds = row.regItemId || this.ids
      this.$confirm('此操作将永久删除ID为【' + regItemIds + '】的挂号费, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteRegisteredItemByIds(regItemIds).then(res => {
          this.loading = false
          this.msgSuccess('删除成功')
          this.getSysRegisteredItemList()// 全查询
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
          if (this.form.regItemId === undefined) {
            addRegisteredItem(this.form).then(res => {
              this.msgSuccess('保存成功')
              this.loading = false
              this.getSysRegisteredItemList()// 列表重新查询
              this.open = false// 关闭弹出层
            }).catch(() => {
              this.loading = false
            })
          } else { // 做修改
            updateRegisteredItem(this.form).then(res => {
              this.msgSuccess('修改成功')
              this.loading = false
              this.getSysRegisteredItemList()// 列表重新查询
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
