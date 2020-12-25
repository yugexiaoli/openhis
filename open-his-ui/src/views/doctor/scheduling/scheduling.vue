<template>
  <div class="app-container">
    <!-- 搜索开始 -->
    <el-row :gutter="12">
      <el-col :span="24">
        <el-card style="margin-bottom:5px;">

          <el-form ref="queryParams" :model="queryParams" :inline="true" label-width="68px">
            <el-form-item label="所属科室" prop="deptId">
              <el-select v-model="queryParams.deptId" placeholder="请选择医生科室" clearable size="small" style="width:180px">
                <el-option
                  v-for="dept in deptOptions"
                  :key="dept.deptId"
                  :label="dept.deptName"
                  :value="dept.deptId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="医生名称" prop="userId">
              <el-select v-model="queryParams.userId" placeholder="请选择医生" clearable size="small" style="width:180px">
                <el-option
                  v-for="user in userOptions"
                  :key="user.userId"
                  :label="user.userName"
                  :value="user.userId"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
            <el-form-item style="float:right;">
              <el-button type="primary" icon="el-icon-plus" size="mini" @click="upweek()">上一周</el-button>
              <el-button type="success" icon="el-icon-s-operation" size="mini" @click="thisweek()">当前周</el-button>
              <el-button type="primary" icon="el-icon-check" size="mini" @click="nextweek()">下一周</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <!-- 搜索结束 -->
    <!-- 排班周期开始 -->
    <el-row :gutter="12">
      <el-col :span="24">
        <el-card style="margin-bottom:5px;">
          <div style="text-align:center">
            <span>
              <span>{{ schedulingDate.startDatethisweek }}</span>-<span>{{ schedulingDate.endDatethisweek }}</span>
            </span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 排班周期结束 -->
    <!-- 表格开始 -->
    <!-- 表格结束 -->
  </div>
</template>

<script>
import { selectAllDept } from '@/api/system/dept/dept'
import { getUsersNeedScheduling } from '@/api/system/user/user'
export default {
  data() {
    return {
      // 查询参数
      queryParams: {
        deptId: undefined,
        userId: undefined
      },
      // 科室数据
      deptOptions: [],
      // 需要排版的用户数据
      userOptions: [],
      // 排班周期数据
      schedulingDate: {
        startDatethisweek: '2020-12-28(周一)',
        endDatethisweek: '2021-1-3(周日)'
      }
    }
  }, created() {
    selectAllDept().then(res => {
      this.deptOptions = res.data
    })
    getUsersNeedScheduling().then(res => {
      this.userOptions = res.data
    })
  }, methods: {
    // 搜索方法
    handleQuery() {

    },
    // 重置查询
    resetQuery() {

    },
    // 上一周
    upweek() {

    },
    // 当前周
    thisweek() {

    },
    // 下一周
    nextweek() {

    }

  }
}
</script>

<style>

</style>
