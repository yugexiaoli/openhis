<template>
  <div class="app-container">
    <!-- 搜索条件开始 -->
    <el-form ref="MysearchForm" :inline="true" :model="searchForm" class="search-form-inline">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="searchForm.roleName" size="small" placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item label="权限字符" prop="roleCode">
        <el-input v-model="searchForm.roleCode" size="small" placeholder="请输入权限字符" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="searchForm.status" size="small" placeholder="角色状态">
          <el-option
            v-for="status in statusOptions"
            :key="status.dictCode"
            :label="status.dictLabel"
            :value="status.dictValue"
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
        <el-button type="primary" size="mini" icon="el-icon-search" @click="dosearch">搜索</el-button>
        <el-button type="primary" size="mini" icon="el-icon-refresh" @click="resetSearchForm">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 搜索条件结束 -->
    <!-- 工具栏开始 -->
    <el-row :gutter="10" style="margin-bottom: 8px;">
      <el-col :span="1.5">
        <el-button type="primary" size="mini" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" size="mini" icon="el-icon-edit" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" size="mini" icon="el-icon-delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" size="mini" icon="el-icon-delete" :disabled="single" @click="handleSelectMenu">分配权限</el-button>
      </el-col>
    </el-row>
    <!-- 工具栏结束 -->
    <!-- 表格开始 -->
    <el-table
      ref="multipleTable"
      v-loading="loading"
      border
      height="600px"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 98%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="roleId" label="角色ID" width="200" align="center" />
      <el-table-column prop="roleName" label="角色名称" width="200" align="center" />
      <el-table-column prop="roleCode" label="权限编码" align="center" width="200" />
      <el-table-column prop="roleSort" label="显示顺序" align="center" width="200" />
      <el-table-column prop="status" label="状态" width="200" align="center" :formatter="statusFormatter" />
      <el-table-column prop="remark" label="备注" width="200" align="center" />
      <el-table-column prop="createTime" label="创建时间" width="auto" align="center" />
      <el-table-column fixed="right" label="操作" width="200px" align="center">
        <template slot-scope="scope">
          <el-button icon="el-icon-edit" type="text" size="small" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button icon="el-icon-delete" type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
          <el-button icon="el-icon-thumb" type="text" size="small" @click="handleSelectMenu(scope.row)">分配权限</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 表格结束 -->
    <!-- 分页开始 -->
    <div class="block">
      <el-pagination
        v-show="datatatal>0"
        :current-page="searchForm.pageNum"
        :page-sizes="[5, 10, 20, 25]"
        :page-size="searchForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="datatatal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 分页结束 -->
    <!-- 添加修改弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      center
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" clearable size="small" />
        </el-form-item>
        <el-form-item label="权限编码" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="请输入权限编码" clearable size="small" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="roleSort">
          <el-input-number v-model="form.roleSort" placeholder="显示顺序" clearable size="small" />
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
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入角色备注" clearable size="small" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 添加修改弹出层结束 -->
    <!-- 分配权限弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="menuOpen"
      width="500px"
      center
      append-to-body
    >
      <el-tree
        ref="menu"
        :data="selectMenuData"
        show-checkbox
        empty-text="加载中"
        node-key="menuId"
        highlight-current
        :props="{id:'menuId', children: 'children', label: 'menuName'}"
      />
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="selectmenuSubmit">确 定</el-button>
        <el-button @click="selectmenucancel">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 分配权限弹出层结束 -->
  </div>
</template>

<script>
import { listRoleForPage, addRole, getRoleById, updateRole, deleteRoleByIds, saveRoleMenu } from '@/api/system/role/role'
import { selectMenuTree, getMenuIdsByRoleId } from '@/api/system/menu/menu'
export default {
  data() {
    return {

      // 是否启用遮盖层
      loading: false,
      // 表格数据
      tableData: [],
      // 选中数组
      multipleSelection: [],
      // 选中的ids数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 状态数据角色
      statusOptions: [],
      // 表单数据
      form: {},
      // 表单校验
      rules: {
        roleName: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ]
      },
      // 搜索表单以及分页参数 roletypedto
      searchForm: {
        pageNum: 1,
        pageSize: 5,
        roleName: undefined,
        roleCode: undefined,
        status: undefined
      },
      // 开始日期-结束日期
      beginAndEndTime: [],
      // 数据总条数
      datatatal: 100,
      // 权限弹出层
      menuOpen: false,
      // 权限树数据
      selectMenuData: [],
      // 当前角色id
      currentRoleid: undefined
    }
  },
  mounted() {
    // 初始化表格数据
    this.initTable()
    // 使用全局的根据角色类型查询角色数据的方法查询角色数据
    this.getDataByType('sys_normal_disable').then(res => {
      this.statusOptions = res.data
    })
  },
  methods: {

    // 初始化表格数据
    initTable() {
      this.loading = true
      listRoleForPage(this.addDateRange(this.searchForm, this.beginAndEndTime)).then(res => {
        const { code, data, total } = res
        if (code === 200) {
          this.tableData = data
          this.datatatal = total
        }
        this.loading = false
      })
    },
    // 查询
    dosearch() {
      this.initTable()
    },
    // 重置表单
    resetSearchForm() {
      this.resetForm('MysearchForm')
      this.beginAndEndTime = []
      this.initTable()
    },
    // 表格选中改变事件
    handleSelectionChange(val) {
      this.ids = val.map(item => item.roleId)
      this.single = val.length !== 1
      this.multiple = !val.length
    },
    // 翻译状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },

    // 工具栏增删改按钮事件
    // 打开添加的弹出层
    handleAdd() {
      this.reset()
      this.form = {
        roleName: undefined,
        roleCode: undefined,
        roleSort: '0',
        status: '0',
        remark: undefined
      }
      this.open = true
    },
    // 打开修改的弹出层
    handleUpdate(row) {
      const roleId = row.roleId || this.ids
      // const roleId = row.roleId === undefined ? this.ids[0] : row.roleId

      this.open = true
      this.reset()
      // 根据roleId查询一个角色信息
      getRoleById(roleId).then(res => {
        this.form = res.data
      })
    },
    // 打开分配角色权限弹出层
    handleSelectMenu(row) {
      this.currentRoleid = row.roleId || this.ids[0]
      // 加载权限树
      selectMenuTree().then(res => {
        this.selectMenuData = this.handleTree(res.data, 'menuId')
      })
      // 树绑定角色所含的权限
      getMenuIdsByRoleId(this.currentRoleid).then(res => {
        this.$refs.menu.setCheckedKeys(res.data)
      })
      this.menuOpen = true
      this.title = '分配菜单权限'
    },
    // 执行删除
    handleDelete(row) {
      const roleIds = row.roleId || this.ids
      this.$confirm('此操作将永久删除该角色数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteRoleByIds(roleIds).then(res => {
          this.loading = false
          this.msgSuccess('删除成功')
          this.initTable()// 全查询
        })
      }).catch(() => {
        this.msgError('删除已取消')
        this.loading = false
      })
    },
    // 权限弹出层确定保存权限信息
    selectmenuSubmit() {
      const checkedkeys = this.$refs.menu.getCheckedKeys()
      const halfcheckkeys = this.$refs.menu.getHalfCheckedKeys()
      const allckecked = checkedkeys.concat(halfcheckkeys)
      saveRoleMenu(this.currentRoleid, allckecked).then(res => {
        this.msgSuccess('分配成功')
        this.menuOpen = false
      }).catch(() => {
        this.msgError('分配失败')
        this.menuOpen = false
      })
    },
    // 权限弹出层取消
    selectmenucancel() {
      this.menuOpen = false
      this.selectMenuData = []
    },
    // 保存
    handleSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          // 做添加
          this.loading = true
          if (this.form.roleId === undefined) {
            addRole(this.form).then(res => {
              this.msgSuccess('保存成功')
              this.loading = false
              this.open = false// 关闭弹出层
              this.initTable()// 列表重新查询
            }).catch(() => {
              this.loading = false
            })
          } else { // 做修改
            updateRole(this.form).then(res => {
              this.msgSuccess('修改成功')
              this.loading = false
              this.open = false// 关闭弹出层
              this.initTable()// 列表重新查询
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
    // 重置表单
    reset() {
      this.form = {
        roleName: undefined,
        roleCode: undefined,
        roleSort: undefined,
        status: '0',
        remark: undefined
      }
      this.resetForm('form')
    },
    // 分页时间
    handleSizeChange(val) {
      this.searchForm.pageSize = val
      this.initTable()
    },
    handleCurrentChange(val) {
      this.searchForm.pageNum = val
      this.initTable()
    }
  }
}
</script>

<style>
.el-date-editor.el-input, .el-date-editor.el-input__inner {
    width: 220px;
}
</style>
