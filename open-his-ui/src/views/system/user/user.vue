<template>
  <div class="app-container">
    <!-- 搜索条件开始 -->
    <el-form ref="MysearchForm" :inline="true" :model="searchForm" class="search-form-inline">
      <el-form-item label="所属部门" prop="deptId">
        <el-select v-model="searchForm.deptId" size="small" placeholder="请选择所属部门">
          <el-option
            v-for="dept in deptOptions"
            :key="dept.deptId"
            :label="dept.deptName"
            :value="dept.deptId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input v-model="searchForm.userName" size="small" placeholder="请输入用户名称" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="searchForm.phone" size="small" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="searchForm.status" size="small" placeholder="用户状态">
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
        <el-button size="mini" icon="el-icon-refresh" @click="resetSearchForm">重置</el-button>
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
        <el-button type="warning" size="mini" icon="el-icon-delete" :disabled="single" @click="handleSelectRole">分配角色</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" size="mini" icon="el-icon-refresh" :disabled="multiple" @click="handleResetPwd">重置密码</el-button>
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
      <el-table-column prop="userId" label="用户ID" width="70" align="center" />
      <el-table-column prop="userName" label="用户姓名" width="100" align="center" />
      <el-table-column prop="deptId" label="部门" align="center" width="100" :formatter="deptFomatter" />
      <el-table-column prop="phone" label="手机号码【登录身份】" align="center" width="170" />
      <el-table-column prop="sex" label="性别" align="center" width="100" :formatter="sexFormatter" />
      <el-table-column prop="age" label="年龄" align="center" width="100" />
      <el-table-column prop="schedulingFlag" label="是否排班" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.schedulingFlag==0 ? 'success' : 'danger' ">
            {{ scope.row.schedulingFlag==0 ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="userRank" label="级别" align="center" width="100" :formatter="userRankFormatter" />
      <el-table-column prop="background" label="背景" align="center" width="100" :formatter="backgroundFormatter" />
      <el-table-column prop="status" label="状态" width="100" align="center" :formatter="statusFormatter" />
      <el-table-column prop="createTime" label="创建时间" width="auto" align="center" />
      <el-table-column fixed="right" label="操作" width="200px" align="center">
        <template slot-scope="scope">
          <el-button icon="el-icon-edit" type="text" size="small" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button icon="el-icon-delete" type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
          <el-button icon="el-icon-thumb" type="text" size="small" @click="handleSelectRole(scope.row)">分配角色</el-button>
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
      width="800px"
      center
      append-to-body
    >

      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="10">
            <el-form-item label="姓名" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名称" clearable size="small" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号,作为登录凭证" clearable size="small" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="所属科室" prop="deptId">
              <el-select v-model="form.deptId" size="small" placeholder="所属部门">
                <el-option
                  v-for="dept in deptOptions"
                  :key="dept.deptId"
                  :label="dept.deptName"
                  :value="dept.deptId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="级别" prop="userRank">
              <el-select v-model="form.userRank" size="small" placeholder="用户级别">
                <el-option
                  v-for="userLevel in userRankOptions"
                  :key="userLevel.dictValue"
                  :label="userLevel.dictLabel"
                  :value="userLevel.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="form.age" clearable size="small" />
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="背景" prop="background">
              <el-select v-model="form.background" size="small" placeholder="学历">
                <el-option
                  v-for="background in backgroundOptions"
                  :key="background.dictValue"
                  :label="background.dictLabel"
                  :value="background.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="性别" prop="sex">
              <el-radio-group v-model="form.sex">
                <el-radio
                  v-for="sex in sexOptions"
                  :key="sex.dictValue"
                  :label="sex.dictValue"
                  :value="sex.dictValue"
                >{{ sex.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
        <el-form-item label="是否参与排班" prop="schedulingFlag" label-width="100px">
          <el-radio-group v-model="form.schedulingFlag">
            <el-radio label="0">是</el-radio>
            <el-radio label="1">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 添加修改弹出层结束 -->
    <!-- 分配角色弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="roleOpen"
      width="800px"
      center
      append-to-body
      @close="selectRoleClose"
    >
      <el-table
        ref="selectRoleTable"
        :data="selectRoleTable"
        empty-text="角色加载中"
        tooltip-effect="dark"
        style="width: 100%"
        border
        @selection-change="handleSelectionRoleChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="roleId" label="ID" width="55" />
        <el-table-column prop="roleName" label="角色名称" width="auto" />
        <el-table-column prop="roleCode" label="权限编码" width="120" />
        <el-table-column prop="remark" label="备注" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="auto" />
      </el-table>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="selectroleSubmit">确 定</el-button>
        <el-button @click="selectRolecancel">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 分配角色弹出层结束 -->
  </div>
</template>

<script>
import { listUserForPage, addUser, getUserById, updateUser, deleteUserByIds, resetPwd, getRoleIdsByUserId, saveUserRole } from '@/api/system/user/user'
import { selectAllDept } from '@/api/system/dept/dept'
import { selectAllRole } from '@/api/system/role/role'
export default {
  data() {
    // 验证手机号
    var checkPhone = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('手机号不能为空'))
      } else {
        const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
        console.log(reg.test(value))
        if (reg.test(value)) {
          callback()
        } else {
          return callback(new Error('请输入正确的手机号'))
        }
      }
    }
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
      // 性别数据
      sexOptions: [],
      // 级别数据
      userRankOptions: [],
      // 背景数据
      backgroundOptions: [],
      // 是否字典数据
      yesnoOptions: [],
      // 表单数据
      form: {},
      // 表单校验
      rules: {
        userName: [
          { required: true, message: '用户名称不能为空', trigger: 'blur' }
        ],
        phone: [
          { validator: checkPhone, trigger: 'blur' }
        ],
        deptId: [
          { required: true, message: '未选择科室', trigger: 'blur' }
        ],
        userRank: [
          { required: true, message: '未选择用户级别', trigger: 'blur' }
        ],
        background: [
          { required: true, message: '未选择背景', trigger: 'blur' }
        ]
      },
      // 搜索表单以及分页参数 roletypedto
      searchForm: {
        pageNum: 1,
        pageSize: 10,
        deptId: undefined,
        userName: undefined,
        phone: undefined,
        status: undefined
      },
      // 开始日期-结束日期
      beginAndEndTime: [],
      // 数据总条数
      datatatal: 100,
      // 权限弹出层
      roleOpen: false,
      // 权限树数据
      selectMenuData: [],
      // 当前角色id
      currentRoleid: undefined,
      // 部门数据
      deptOptions: [],
      // 分配角色弹出层表格数据
      selectRoleTable: [],
      // 分配角色表格选中的id
      selectRoleIds: [],
      // 当前分配角色的用户
      currentUserId: 0
    }
  },
  mounted() {
    // 初始化表格数据
    this.initTable()
    // 初始化部门下拉框
    this.initDept()
    // 使用全局的根据角色类型查询角色数据的方法查询角色数据
    this.getDataByType('sys_normal_disable').then(res => {
      this.statusOptions = res.data
    })
    // 性别字典
    this.getDataByType('sys_user_sex').then(res => {
      this.sexOptions = res.data
    })
    // 级别字典
    this.getDataByType('sys_user_level').then(res => {
      this.userRankOptions = res.data
    })
    // 学历字典
    this.getDataByType('sys_user_background').then(res => {
      this.backgroundOptions = res.data
    })
    // 是否字典
    this.getDataByType('sys_yes_no').then(res => {
      this.yesnoOptions = res.data
    })
  },
  methods: {
    // 初始化表格数据
    initTable() {
      this.loading = true
      listUserForPage(this.addDateRange(this.searchForm, this.beginAndEndTime)).then(res => {
        const { code, data, total } = res
        if (code === 200) {
          this.tableData = data
          this.datatatal = total
        }
        this.loading = false
      })
    },
    // 初始化部门下拉框
    initDept() {
      selectAllDept().then(res => {
        this.deptOptions = res.data
      })
    },
    // 查询
    dosearch() {
      this.searchForm.pageNum = 1
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
      this.ids = val.map(item => item.userId)
      this.single = val.length !== 1
      this.multiple = !val.length
    },
    // 分配角色表格选中事件
    handleSelectionRoleChange(val) {
      this.selectRoleIds = val.map(item => item.roleId)
    },
    // 翻译状态
    statusFormatter(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 翻译性别
    sexFormatter(row) {
      return this.selectDictLabel(this.sexOptions, row.sex)
    },
    // 翻译级别
    userRankFormatter(row) {
      return this.selectDictLabel(this.userRankOptions, row.userRank)
    },
    // 翻译背景
    backgroundFormatter(row) {
      return this.selectDictLabel(this.backgroundOptions, row.background)
    },
    // 翻译部门
    deptFomatter(row) {
      return this.selectDeptLabel(this.deptOptions, row.deptId)
    },
    // 重置密码
    handleResetPwd() {
      const userIds = this.ids
      this.$confirm('确定重置用户密码, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        resetPwd(userIds).then(res => {
          this.loading = false
          this.msgSuccess('重置成功')
          this.initTable()// 全查询
        }).catch(() => {
          this.loading = false
          this.msgSuccess('重置失败')
          this.initTable()// 全查询
        })
      }).catch(() => {
        this.msgError('重置已取消')
        this.loading = false
      })
    },
    // 工具栏增删改按钮事件
    // 打开添加的弹出层
    handleAdd() {
      this.reset()
      this.title = '添加用户信息'
      this.open = true
    },
    // 打开修改的弹出层
    handleUpdate(row) {
      this.title = '修改用户信息'
      const userId = row.userId || this.ids[0]
      // const roleId = row.roleId === undefined ? this.ids[0] : row.roleId

      this.open = true
      this.reset()
      // 根据roleId查询一个角色信息
      getUserById(userId).then(res => {
        this.form = res.data
      })
    },
    // 打开分配角色权限弹出层
    async handleSelectRole(row) {
      this.currentUserId = row.userId || this.ids[0]
      const tx = this
      await selectAllRole().then(res => {
        tx.selectRoleTable = res.data
        this.$nextTick(() => {
          // 根据当前用户查找之前拥有的角色IDS
          getRoleIdsByUserId(tx.currentUserId).then(res2 => {
            res2.data.filter(r1 => {
              tx.selectRoleTable.filter(r2 => {
                if (r1 === r2.roleId) {
                  // 选中表格checkbox
                  tx.$refs.selectRoleTable.toggleRowSelection(r2, true)
                }
              })
            })
          })
        })
      })

      this.title = '分配角色'

      this.roleOpen = true
    },
    // 执行删除
    handleDelete(row) {
      const userIds = row.userId || this.ids
      this.$confirm('此操作将永久删除该用户数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        deleteUserByIds(userIds).then(res => {
          this.loading = false
          this.msgSuccess('删除成功')
          this.initTable()// 全查询
        })
      }).catch(() => {
        this.msgError('删除已取消')
        this.loading = false
      })
    },
    // 分配角色弹出层关闭
    selectRoleClose() {
      // this.$refs.selectRoleTable.clearSelection()
    },
    // 权限弹出层确定保存角色信息
    selectroleSubmit() {
      saveUserRole(this.currentUserId, this.selectRoleIds).then(res => {
        this.msgSuccess('分配成功')
        this.roleOpen = false
      }).catch(() => {
        this.msgError('分配失败')
        this.roleOpen = false
      })
    },
    // 角色弹出层取消
    selectRolecancel() {
      this.roleOpen = false
      this.selectRoleTable = []
      // this.$refs.selectRoleTable.clearSelection()
    },
    // 保存
    handleSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          // 做添加
          this.loading = true
          if (this.form.userId === undefined) {
            addUser(this.form).then(res => {
              this.msgSuccess('保存成功')
              this.loading = false
              this.open = false// 关闭弹出层
              this.initTable()// 列表重新查询
            }).catch(() => {
              this.loading = false
            })
          } else { // 做修改
            updateUser(this.form).then(res => {
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
        userId: undefined,
        userName: undefined,
        phone: undefined,
        deptId: undefined,
        userRank: undefined,
        age: '20',
        background: undefined,
        sex: '0',
        status: '0',
        schedulingFlag: '1'
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
