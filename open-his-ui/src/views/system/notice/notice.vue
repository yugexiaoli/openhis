<template>
  <div class="app-container">
    <!-- 查询条件开始 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="68px">
      <el-form-item label="公告标题" prop="noticeTitle">
        <el-input
          v-model="queryParams.noticeTitle"
          placeholder="请输入公告标题"
          clearable
          size="small"
          style="width:240px"
        />
      </el-form-item>
      <el-form-item label="操作人员" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入操作人员"
          clearable
          size="small"
          style="width:240px"
        />
      </el-form-item>
      <el-form-item label="类型" prop="noticeType">
        <el-select
          v-model="queryParams.noticeType"
          placeholder="公告类型"
          clearable
          size="small"
          style="width:240px"
        >
          <el-option
            v-for="dict in noticetypeOptions"
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
    <el-table v-loading="loading" border :data="noticeTableList" @selection-change="handleSelectionChnage">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="公告ID" prop="noticeId" align="center" width="80" />
      <el-table-column label="公告标题" prop="noticeTitle" align="center" />
      <el-table-column label="公告类型" prop="noticeType" align="center" :formatter="noticetypeFormatter" />
      <el-table-column label="状态" prop="status" align="center" :formatter="statusFormatter" width="180" />
      <el-table-column label="创建者" prop="createBy" align="center" />
      <el-table-column label="创建时间" prop="createTime" align="center" width="180" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" size="mini" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button type="text" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="text" icon="el-icon-view" size="mini" @click="handleView(scope.row)">查看内容</el-button>
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
      width="900px"
      center
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="10">
            <el-form-item label="公告名称" prop="noticeTitle">
              <el-input v-model="form.noticeTitle" placeholder="请输入公告标题" size="small" />
            </el-form-item>
          </el-col>
          <el-col :span="8" :offset="2">
            <el-form-item label="公告类型" prop="noticeType">
              <el-radio-group v-model="form.noticeType">
                <el-radio
                  v-for="dict in noticetypeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                  :value="dict.dictValue"
                >{{ dict.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="15" />
          <el-col :span="8" :offset="12">
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
        </el-row>
        <el-row>
          <el-form-item label="内容" prop="noticeContent">
            <markdown-editor
              ref="noticeContent"
              v-model="form.noticeContent"
              :mode="editmode"
              height="300px"
              :options="{hideModeSwitch:true,previewStyle:'tab'}"
              language="zh"
            />
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              :rows="2"
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 添加和修改的弹出层结束 -->
    <!-- 查看内容弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="viewopen"
      width="900px"
      center
      append-to-body
    >
      <markdown-editor ref="noticeContent" v-model="noticeContent" language="zh" :aria-disabled="true" :options="{hideModeSwitch:true,previewStyle:'tab'}" />
    </el-dialog>
    <!-- 查看内容弹出层结束 -->
  </div>
</template>
<script>
import MarkdownEditor from '@/components/MarkdownEditor/index'
import { listNoticeForPage, addNotice, updateNotice, getNoticeById, deleteNoticeByIds } from '@/api/system/notice/notice'
export default {
  components: {
    'markdown-editor': MarkdownEditor
  },
  data() {
    return {
      // 查看内容
      noticeContent: undefined,
      // 查看内容弹出层显示隐藏
      viewopen: false,
      // 编辑器类型
      editmode: 'markdown',
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
      noticeTableList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 类型数据字典
      noticetypeOptions: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 5,
        noticeTitle: undefined,
        createBy: undefined,
        noticeType: undefined
      },
      // 表单数据
      form: {},
      // 表单校验
      rules: {
        noticeTitle: [
          { required: true, message: '公告标题不能为空', trigger: 'blur' }
        ],
        noticeContent: [
          { required: true, message: '公告内容不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getnoticeList()
    // 获取类型字典数据
    this.getDataByType('sys_notice_type').then(res => {
      this.noticetypeOptions = res.data
    })
    // 获取状态子典数据
    this.getDataByType('sys_normal_disable').then(res => {
      this.statusOptions = res.data
    })
  },
  methods: {
    // 查询字典数据
    getnoticeList() {
      this.loading = true
      listNoticeForPage(this.queryParams).then(res => {
        this.loading = false
        this.noticeTableList = res.data
        this.total = res.total
      })
    },
    // 条件查询
    handleQuery() {
      this.getnoticeList()
    },
    // 重置查询条件
    resetQuery() {
      this.resetForm('queryForm')
      this.getnoticeList()
    },
    // 数据表格的多选择框选择时触发
    handleSelectionChnage(selection) {
      this.ids = selection.map(item => item.noticeId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 分页pageSize变化时触发
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      // 重新查询
      this.getnoticeList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      // 重新查询
      this.getnoticeList()
    },
    // 翻译状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 翻译类型
    noticetypeFormatter(row) {
      return this.selectDictLabel(this.noticetypeOptions, row.noticeType)
    },
    // 重置表单
    reset() {
      this.form = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: '1',
        status: '0',
        noticeContent: undefined,
        remark: undefined
      }
      this.resetForm('form')
    },
    // 打开添加的弹出层
    handleAdd() {
      this.title = '添加公告信息'
      this.open = true
      this.reset()
    },
    // 打开修改的弹出层
    handleUpdate(row) {
      this.open = true
      this.reset()
      const noticeId = row.noticeId || this.ids
      // 根据字典数据ID查询字典数据
      getNoticeById(noticeId).then(res => {
        this.form = res.data
      })
    },
    // 进行删除
    handleDelete(row) {
      const noticeIds = row.noticeId || this.ids
      const noticeTitle = row.noticeTitle || '....'
      this.$confirm('此操作将永久删除标题为【' + noticeTitle + '】的公告通知, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteNoticeByIds(noticeIds).then(res => {
          this.loading = false
          this.msgSuccess('删除成功')
          this.getnoticeList()// 全查询
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
          if (this.form.noticeId === undefined) {
            addNotice(this.form).then(res => {
              this.msgSuccess('保存成功')
              this.loading = false
              this.getnoticeList()// 列表重新查询
              this.open = false// 关闭弹出层
            }).catch(() => {
              this.loading = false
            })
          } else { // 做修改
            updateNotice(this.form).then(res => {
              this.msgSuccess('修改成功')
              this.loading = false
              this.getnoticeList()// 列表重新查询
              this.open = false// 关闭弹出层
            }).catch(() => {
              this.loading = false
            })
          }
        }
      })
    },
    // 查看内容
    handleView(row) {
      this.viewopen = true
      this.title = row.noticeTitle
      this.noticeContent = row.noticeContent
    },
    // 取消
    cancel() {
      this.open = false
    }
  }
}
</script>
