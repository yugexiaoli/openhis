<template>
  <!-- 检查费用表页面 -->
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="项目名称" prop="checkItemName">
        <el-input v-model="queryParams.checkItemName" placeholder="请输入项目名称" clearable size="small" style="width:240px" />
      </el-form-item>
      <el-form-item label="关键字" prop="keywords">
        <el-input v-model="queryParams.keywords" placeholder="请输入关键字" clearable size="small" style="width:240px" />
      </el-form-item>
      <el-form-item label="项目类别" prop="typeId">
        <el-select v-model="queryParams.typeId" placeholder="项目类别" clearable size="small" style="width:240px">
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
    <el-table v-loading="loading" border :data="sysCheckItemTableList" @selection-change="handleSelectionChnage">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="checkItemId" label="项目费用ID" align="center" />
      <el-table-column prop="checkItemName" label="项目名称" align="center" />
      <el-table-column prop="keywords" label="关键字" align="center" />
      <el-table-column prop="unitPrice" label="项目单价" align="center" :formatter="unitPriceFormatter" />
      <el-table-column prop="cost" label="项目成本" align="center" :formatter="costFormatter" />
      <el-table-column prop="unit" label="单位" align="center" />
      <el-table-column prop="typeId" label="项目类别" align="center" :formatter="typeFormatter" />
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
      width="500px"
      center
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item prop="typeId" label="项目类别">
          <el-select v-model="form.typeId" placeholder="项目类别" clearable size="small" style="width:240px">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="checkItemName" label="项目名称">
          <el-input v-model="form.checkItemName" placeholder="请输入项目名称" size="small" />
        </el-form-item>
        <el-form-item prop="keywords" label="关键字">
          <el-input v-model="form.keywords" placeholder="请输入关键字" size="small" />
        </el-form-item>
        <el-form-item prop="unitPrice" label="项目价格">
          <el-input v-model="form.unitPrice" placeholder="请输入项目价格" size="small" />
        </el-form-item>
        <el-form-item prop="cost" label="项目成本">
          <el-input v-model="form.cost" placeholder="请输入项目成本" size="small" />
        </el-form-item>
        <el-form-item prop="unit" label="项目单位">
          <el-input v-model="form.unit" placeholder="请输入项目单位" size="small" />
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
import { listCheckItemForPage, addCheckItem, updateCheckItem, getCheckItemById, deleteCheckItemByIds } from '@/api/system/checkItem/checkItem'
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
      sysCheckItemTableList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 项目类型字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        checkItemName: undefined,
        keywords: undefined,
        typeId: undefined,
        status: undefined
      },
      // 表单数据
      form: {
        checkItemId: undefined,
        checkItemName: undefined,
        keywords: undefined,
        unitPrice: undefined,
        cost: undefined,
        unit: undefined,
        typeId: '1',
        status: '0'
      },
      // 表单校验
      rules: {

        checkItemName: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        unitPrice: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        cost: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        typeId: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]

      }
    }
  },
  created() {
    this.getSysCheckItemList()
    // 初始化状态字典
    this.getDataByType('sys_normal_disable').then(res => {
      this.statusOptions = res.data
    })
    // 初始化类型字典
    this.getDataByType('his_inspection_type').then(res => {
      this.typeOptions = res.data
    })
  },
  methods: {
    // 查询字典数据
    getSysCheckItemList() {
      this.loading = true
      listCheckItemForPage(this.queryParams).then(res => {
        this.loading = false
        this.sysCheckItemTableList = res.data
        this.total = res.total
      })
    },
    // 条件查询
    handleQuery() {
      this.getSysCheckItemList()
    },
    // 重置查询条件
    resetQuery() {
      this.resetForm('queryForm')
      this.getSysCheckItemList()
    },
    // 数据表格的多选择框选择时触发
    handleSelectionChnage(selection) {
      this.ids = selection.map(item => item.checkItemId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      // 重新查询
      this.getSysCheckItemList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      // 重新查询
      this.getSysCheckItemList()
    },
    // 翻译状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 检查类型翻译
    typeFormatter(row) {
      return this.selectDictLabel(this.typeOptions, row.typeId)
    },
    // 单价格式化
    unitPriceFormatter(row) {
      return row.unitPrice.toFixed(2)
    },
    // 成本 格式化
    costFormatter(row) {
      return row.cost.toFixed(2)
    },
    // 重置表单
    reset() {
      this.form = {
        checkItemId: undefined,
        checkItemName: undefined,
        keywords: undefined,
        unitPrice: undefined,
        cost: undefined,
        unit: undefined,
        typeId: '1',
        status: '0'
      }
      this.resetForm('form')
    },
    // 打开添加的弹出层
    handleAdd() {
      this.title = '新增检查项目'
      this.open = true
      this.reset()
    },
    // 打开修改的弹出层
    handleUpdate(row) {
      this.title = '修改检查项目'
      this.open = true
      this.reset()
      const checkItemId = row.checkItemId || this.ids
      // 根据字典数据ID查询字典数据
      getCheckItemById(checkItemId).then(res => {
        this.form = res.data
      })
    },
    // 进行删除
    handleDelete(row) {
      const checkItemIds = row.checkItemId || this.ids
      this.$confirm('此操作将永久删除ID为【' + checkItemIds + '】的检查项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteCheckItemByIds(checkItemIds).then(res => {
          this.loading = false
          this.msgSuccess('删除成功')
          this.getSysCheckItemList()// 全查询
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
          if (this.form.checkItemId === undefined) {
            addCheckItem(this.form).then(res => {
              this.msgSuccess('保存成功')
              this.loading = false
              this.getSysCheckItemList()// 列表重新查询
              this.open = false// 关闭弹出层
            }).catch(() => {
              this.loading = false
            })
          } else { // 做修改
            updateCheckItem(this.form).then(res => {
              this.msgSuccess('修改成功')
              this.loading = false
              this.getSysCheckItemList()// 列表重新查询
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
