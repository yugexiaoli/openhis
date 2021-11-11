<template>
  <div v-loading="loading" class="app-container">
    <!-- 选择门诊和急诊的选项开始 -->
    <el-card style="margin-bottom:5px;text-align:center">
      <el-radio-group v-model="schedulingType" @change="schedulingTypeChange">
        <el-radio-button
          v-for="dict in schedulingTypeOptions"
          :key="dict.dictValue"
          :label="dict.dictValue"
          :value="dict.dictValue"
        >
          {{ dict.dictLabel }}
        </el-radio-button>
      </el-radio-group>
    </el-card>
    <!-- 选择门诊和急诊的选项结束 -->

    <!-- 下面的整体卡片开始 -->
    <el-row :gutter="5">
      <el-col :span="7">
        <!-- 左边的患者信息开始 -->
        <el-card>
          <el-form ref="form" :model="patientAllObj.patientObj" :inline="true" label-width="88px">
            <el-form-item label="患者姓名" prop="name">
              <el-input
                v-model="patientAllObj.patientObj.name"
                placeholder="请输入患者姓名"
                :disabled="true"
                style="width:100%"
                size="small"
              >
                <el-button slot="append" icon="el-icon-user-solid" @click="viewRegistration" />
              </el-input>
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard">
              <el-input
                v-model="patientAllObj.patientObj.idCard"
                placeholder="请输入身份证号"
                :disabled="true"
                style="width:100%"
                size="small"
              />
            </el-form-item>
            <el-form-item label="患者电话" prop="phone">
              <el-input
                v-model="patientAllObj.patientObj.phone"
                placeholder="请输入患者电话"
                :disabled="true"
                style="width:100%"
                size="small"
              />
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-radio-group v-model="patientAllObj.patientObj.sex">
                <el-radio
                  v-for="d in sexOptions"
                  :key="d.dictValue"
                  :label="d.dictValue"
                  :value="d.dictValue"
                >{{ d.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="出生日期" prop="phone">
              <el-input
                v-model="patientAllObj.patientObj.birthday"
                placeholder="请选择出生日期"
                :disabled="true"
                style="width:100%"
                size="small"
              />
            </el-form-item>
            <el-form-item label="患者年龄" prop="age">
              <el-input
                v-model="patientAllObj.patientObj.age"
                placeholder="请输入患者年龄"
                :disabled="true"
                style="width:100%"
                size="small"
              />
            </el-form-item>
            <el-form-item label="患者地址" prop="address">
              <el-input
                v-model="patientAllObj.patientObj.address"
                placeholder="请输入患者地址"
                :disabled="true"
                style="width:100%"
                size="small"
              />
            </el-form-item>
            <el-form-item label="过敏信息" prop="allergyInfo">
              <el-input
                v-model="patientAllObj.patientObj.allergyInfo"
                placeholder="请输入过敏信息"
                type="textarea"
                :autosize="{minRows:2,maxRows:4}"
                :disabled="true"
                maxlength="100"
                show-word-limit
                style="width:100%"
                size="small"
              />
            </el-form-item>
          </el-form>
          <el-tabs :stretch="true" type="card">
            <el-tab-pane label="患者档案">
              <div v-if="patientAllObj.patientObj.isFinal==='1'">
                <div class="item">
                  紧急联系人:<span v-text="patientAllObj.patientFileObj.emergencyContactName" />
                </div>
                <div class="item">
                  紧急联系人电话:<span v-text="patientAllObj.patientFileObj.emergencyContactPhone" />
                </div>
                <div class="item">
                  关系: <span v-text="patientAllObj.patientFileObj.emergencyContactRelation" />
                </div>
                <div class="item">
                  左耳听力:<span v-text="patientAllObj.patientFileObj.leftEarHearing" />
                </div>
                <div class="item">
                  右耳听力:<span v-text="patientAllObj.patientFileObj.rightEarHearing" />
                </div>
                <div class="item">
                  左眼视力:<span v-text="patientAllObj.patientFileObj.leftVision" />
                </div>
                <div class="item">
                  右眼视力:<span v-text="patientAllObj.patientFileObj.rightVision" />
                </div>
                <div class="item">
                  身高:<span v-text="patientAllObj.patientFileObj.height" />
                </div>
                <div class="item">
                  体重:<span v-text="patientAllObj.patientFileObj.weight" />
                </div>
                <div class="item">
                  血型:<span v-text="patientAllObj.patientFileObj.bloodType" />
                </div>
                <div class="item">
                  个人史:<span v-text="patientAllObj.patientFileObj.personalInfo" />
                </div>
                <div class="item">
                  家族史:<span v-text="patientAllObj.patientFileObj.familyInfo" />
                </div>
                <div class="item">
                  档案创建时间:<span v-text="patientAllObj.patientFileObj.createTime" />
                </div>
                <div class="item">
                  档案更新时间:<span v-text="patientAllObj.patientFileObj.updateTime" />
                </div>
              </div>
              <div v-else style="text-align:center">
                暂无患者档案信息
              </div>
            </el-tab-pane>
            <el-tab-pane label="历史病历">
              <div v-if="patientAllObj.careHistoryObjList.length>0">
                <el-collapse accordion>
                  <el-collapse-item v-for="(item,index) in patientAllObj.careHistoryObjList" :key="index">
                    <template slot="title">
                      就诊部门:【{{ item.deptName }}】 就诊时间:{{ item.careDate }}
                    </template>
                    <div class="item">
                      主诉:{{ item.caseTitle }}
                    </div>
                    <div class="item">
                      发病日期:{{ item.caseDate }}
                    </div>
                    <div class="item">
                      诊断信息:{{ item.caseResult }}
                    </div>
                    <div class="item">
                      医生建议:{{ item.doctorTips }}
                    </div>
                    <div class="item">
                      备注:{{ item.remark }}
                    </div>
                    <div class="item">
                      接诊类型:{{ item.receiveType==='0'?'初诊':'复诊' }}
                    </div>
                    <div class="item">
                      是否传染:{{ item.isContagious==='0'?'否':'是' }}
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
              <div v-else style="text-align:center">
                暂无患者病历信息
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>

        <!-- 左边的患者信息结束 -->
      </el-col>
      <el-col :span="17">
        <!-- 右边的病历，处方开始 -->
        <el-card>
          <el-row :gutter="5">
            <el-col :span="12">
              挂号单号:
              <span v-if="careHistory.regId===undefined" style="color:red">选择患者后显示</span>
              <span v-else style="color:red">{{ careHistory.regId }}</span>
              <br>
              病历编号:
              <span v-if="careHistory.chId===undefined" style="color:red">保存病例后显示</span>
              <span v-else style="color:blue">{{ careHistory.chId }}</span>
            </el-col>
            <el-col :span="12" style="text-align:right">
              <el-button type="primary" :disabled="careHistory.regId===undefined" icon="el-icon-check" @click="handleSaveCareHistory">保存病历</el-button>
              <el-button type="danger" :disabled="careHistory.regId===undefined" icon="el-icon-finished" @click="handleVisitComplete">完成就诊</el-button>
            </el-col>
          </el-row>
        </el-card>
        <el-card style="margin-top:3px">
          <el-tabs :v-model="careActiveName" type="card">
            <el-tab-pane label="病历">
              <!-- 病例表单开始 -->
              <el-card>
                <el-form ref="form" :model="careHistory" :inline="true" label-width="88px">
                  <el-form-item label="发病日期" prop="caseDateObj">
                    <el-date-picker
                      v-model="caseDateObj"
                      value-format="yyyy-MM-dd"
                      size="small"
                    />
                  </el-form-item>
                  <el-form-item label="接诊类型" prop="receiveType">
                    <el-select
                      v-model="careHistory.receiveType"
                      placeholder="接诊类型"
                      size="small"
                    >
                      <el-option
                        v-for="dict in receiveTypeOptions"
                        :key="dict.dictValue"
                        :label="dict.dictLabel"
                        :value="dict.dictValue"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="是否传染" prop="isContagious">
                    <el-select
                      v-model="careHistory.isContagious"
                      placeholder="是否传染"
                      size="small"
                    >
                      <el-option
                        v-for="dict in isContagiousOptions"
                        :key="dict.dictValue"
                        :label="dict.dictLabel"
                        :value="dict.dictValue"
                      />
                    </el-select>
                  </el-form-item>
                </el-form>
                <el-form ref="form" :model="careHistory" label-width="88px">
                  <el-form-item label="主诉" :inline="false" prop="caseTitle">
                    <el-input
                      v-model="careHistory.caseTitle"
                      type="textarea"
                      :autosize="{ minRows: 4, maxRows: 6}"
                      placeholder="请输入主诉"
                      style="width:100%"
                      maxlength="200"
                      show-word-limit
                      size="small"
                    />
                  </el-form-item>
                  <el-form-item label="诊断信息" :inline="false" prop="caseResult">
                    <el-input
                      v-model="careHistory.caseResult"
                      type="textarea"
                      :autosize="{ minRows: 4, maxRows: 6}"
                      placeholder="请输入诊断信息"
                      style="width:100%"
                      maxlength="200"
                      show-word-limit
                      size="small"
                    />
                  </el-form-item>
                  <el-form-item label="医生建议" :inline="false" prop="doctorTips">
                    <el-input
                      v-model="careHistory.doctorTips"
                      type="textarea"
                      :autosize="{ minRows: 4, maxRows: 6}"
                      placeholder="请输入医生建议"
                      style="width:100%"
                      maxlength="200"
                      show-word-limit
                      size="small"
                    />
                  </el-form-item>
                  <el-form-item label="备注" :inline="false" prop="remark">
                    <el-input
                      v-model="careHistory.remark"
                      type="textarea"
                      :autosize="{ minRows: 4, maxRows: 6}"
                      placeholder="请输入备注"
                      style="width:100%"
                      maxlength="200"
                      show-word-limit
                      size="small"
                    />
                  </el-form-item>
                </el-form>
              </el-card>
              <!-- 病例表单结束 -->
            </el-tab-pane>
            <el-tab-pane label="处方">
              <!-- 处方详情开始 -->
              <el-collapse v-if="careOrders.length>0" accordion>
                <el-collapse-item v-for="(item,index) in careOrders" :key="index">
                  <template slot="title">
                    【{{ item.careOrder.coType==='0'?'药用处方':'检查处方' }}】【{{ index+1 }}】【处方总额】:￥{{ item.careOrder.allAmount }}
                  </template>
                  <el-table
                    v-loading="loading"
                    border
                    :data="item.careOrderItems"
                  >
                    <el-table-column label="序号" align="center" type="index" width="50" />
                    <el-table-column :label="item.careOrder.coType==='0'?'药品名称':'项目名称'" align="center" prop="itemName" />
                    <el-table-column label="数量" align="center" prop="num" />
                    <el-table-column label="单价(元)" align="center" prop="price" />
                    <el-table-column label="金额(元)" align="center" prop="amount" />
                    <el-table-column label="备注" align="center" prop="remark" />
                    <el-table-column label="状态" align="center" prop="status" :formatter="orderDetailsStatusFormatter" />
                    <el-table-column label="操作" align="center">
                      <template slot-scope="scope">
                        <el-button v-show="scope.row.status==='0'" type="text" icon="el-icon-delete" size="mini" @click="handleCareOrderItemDeleteByItemId(scope.row)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-collapse-item>
              </el-collapse>
              <!-- 处方详情结束 -->
              <div style="margin:3px;text-align:left">
                <el-button type="success" icon="el-icon-plus" :disabled="careHistory.chId===undefined" @click="handelAddMedicinesOrder">添加药用处方</el-button>
                <el-button type="success" icon="el-icon-plus" :disabled="careHistory.chId===undefined" @click="handelAddCheckItemOrder">添加检查处方</el-button>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
        <!-- 右边的病历，处方结束 -->
      </el-col>
    </el-row>
    <!-- 下面的整体卡片结束 -->

    <!-- 弹出选择患者的层开始 -->
    <el-dialog
      title="请选择挂号患者"
      :visible.sync="openRegistration"
      width="1000px"
      center
      :close-on-click-modal="false"
      append-to-body
    >
      <el-tabs v-model="activeName" :stretch="true" type="card" @tab-click="handleRegistrationTabClick">
        <el-tab-pane label="待就诊列表" name="t1">
          <el-table v-loading="tableLoading" border :data="toBeSeenRegistration">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="right" inline class="demo-table-expand">
                  <el-form-item label="挂号ID">
                    <span>{{ scope.row.regId }}</span>
                  </el-form-item>
                  <el-form-item label="患者ID">
                    <span>{{ scope.row.patientId }}</span>
                  </el-form-item>
                  <el-form-item label="挂号员">
                    <span>{{ scope.row.createBy }}</span>
                  </el-form-item>
                  <el-form-item label="挂号时间">
                    <span>{{ scope.row.createTime }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="患者姓名" align="center" prop="patientName" />
            <el-table-column label="流水编号" align="center" prop="regNumber" />
            <el-table-column label="挂号类型" align="center" prop="schedulingType" :formatter="schedulingTypeFormatter" />
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button type="success" icon="el-icon-check" size="mini" @click="handleVisit(scope.row)">接诊</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="就诊中列表" name="t2">
          <el-table v-loading="tableLoading" border :data="visitingRegistration">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="right" inline class="demo-table-expand">
                  <el-form-item label="挂号ID">
                    <span>{{ scope.row.regId }}</span>
                  </el-form-item>
                  <el-form-item label="患者ID">
                    <span>{{ scope.row.patientId }}</span>
                  </el-form-item>
                  <el-form-item label="挂号员">
                    <span>{{ scope.row.createBy }}</span>
                  </el-form-item>
                  <el-form-item label="挂号时间">
                    <span>{{ scope.row.createTime }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="患者姓名" align="center" prop="patientName" />
            <el-table-column label="流水编号" align="center" prop="regNumber" />
            <el-table-column label="挂号类型" align="center" prop="schedulingType" :formatter="schedulingTypeFormatter" />
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button type="success" icon="el-icon-check" size="mini" @click="handleLoading(scope.row)">载入</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="就诊完成列表" name="t3">
          <el-table v-loading="tableLoading" border :data="visitCompletedRegistration">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="right" inline class="demo-table-expand">
                  <el-form-item label="挂号ID">
                    <span>{{ scope.row.regId }}</span>
                  </el-form-item>
                  <el-form-item label="患者ID">
                    <span>{{ scope.row.patientId }}</span>
                  </el-form-item>
                  <el-form-item label="挂号员">
                    <span>{{ scope.row.createBy }}</span>
                  </el-form-item>
                  <el-form-item label="挂号时间">
                    <span>{{ scope.row.createTime }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="患者姓名" align="center" prop="patientName" />
            <el-table-column label="流水编号" align="center" prop="regNumber" />
            <el-table-column label="挂号类型" align="center" prop="schedulingType" :formatter="schedulingTypeFormatter" />
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button type="success" icon="el-icon-check" size="mini" @click="handleLoading(scope.row)">载入</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <!-- 弹出选择患者的层结束 -->

    <!-- 添加药用处方和检查出方的弹出层开始 -->
    <el-dialog
      :title="title"
      :visible.sync="openAddOrderItem"
      width="1000px"
      center
      :close-on-click-modal="false"
      append-to-body
    >
      <div style="margin:3px;text-align:right">
        【{{ submitCareOrder.careOrder.coType==='0'?'药用':'检查' }}】处方总额:￥<span style="color:red">{{ submitCareOrder.careOrder.allAmount }}</span>
        <el-button style="margin-left:15px" type="success" icon="el-icon-plus" :disabled="submitCareOrder.careOrderItems.length===0" @click="handleSaveOrderItem">确定添加</el-button>
      </div>
      <el-table
        border
        :data="submitCareOrder.careOrderItems"
        :row-class-name="tableRowClassName"
      >
        <el-table-column label="序号" align="center" type="index" width="50" />
        <el-table-column :label="submitCareOrder.careOrder.coType==='0'?'药品名称':'项目名称'" align="center" prop="itemName" />
        <el-table-column label="数量" width="180px" align="center" prop="num">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.num"
              size="small"
              @change="handleCareOrderItemNumChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="单价(元)" align="center" prop="price" />
        <el-table-column label="金额(元)" align="center" prop="amount" />
        <el-table-column label="备注" align="center" prop="remark">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.remark"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-delete" size="mini" @click="handleCareOrderItemDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button type="primary" size="mini" style="width:100%;margin-top:2px" @click="handleOpenAddOrderItemDrawer">添加【{{ submitCareOrder.careOrder.coType==='0'?'药品':'检查' }}】项 </el-button>
    </el-dialog>
    <!-- 添加药用处方和检查处方的弹出层结束 -->

    <!-- 药品列表的抽屉开始 -->
    <el-drawer
      :visible.sync="openDrawerMedicines"
      direction="rtl"
      append-to-body
    >
      <h2 align="center">药品列表</h2>
      <el-form ref="queryItemForm" :model="queryItemFormParams" label-width="68px">
        <el-form-item label="关键字" prop="keywords">
          <el-input
            v-model="queryItemFormParams.keywords"
            placeholder="请输入关键字"
            clearable
            size="small"
            style="width:180px"
          />
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleMedicinesFormQuery">查询</el-button>
          <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetItemFormQuery">重置</el-button>
        </el-form-item>

        <el-table
          v-loading="drawerLoading"
          height="600px"
          border
          :data="tableItemList"
          @selection-change="handleMedicinesSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="药品ID" align="center" prop="medicinesId" />
          <el-table-column label="药品名称" align="center" prop="medicinesName" />
          <el-table-column label="关键字" align="center" prop="keywords" />
          <el-table-column label="库存量" align="center" prop="medicinesStockNum" />
          <el-table-column label="单位" align="center">
            <template slot-scope="scope">
              {{ scope.row.conversion }}/ {{ scope.row.unit }}
            </template>
          </el-table-column>
        </el-table>
        <!-- 数据表格结束 -->
        <!-- 分页控件开始 -->
        <el-pagination
          v-show="total>0"
          :current-page="queryItemFormParams.pageNum"
          :page-sizes="[5, 10, 20, 30]"
          :page-size="queryItemFormParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleMedicinesSizeChange"
          @current-change="handleMedicinesCurrentChange"
        />
        <el-form-item>
          <div style="text-align:center">
            <el-button type="primary" icon="el-icon-check" size="mini" @click="hanldeAddCareItem">添加并关闭</el-button>
          </div>
        </el-form-item>
      </el-form>
    <!-- 分页控件结束 -->
    </el-drawer>
    <!-- 药品列表的抽屉结束 -->

    <!-- 检查项目的抽屉开始 -->
    <el-drawer
      :visible.sync="openDrawerCheckItem"
      direction="rtl"
      append-to-body
    >
      <h2 align="center">检查项目列表</h2>
      <el-form ref="queryItemForm" :model="queryItemFormParams" label-width="68px">
        <el-form-item label="关键字" prop="keywords">
          <el-input
            v-model="queryItemFormParams.keywords"
            placeholder="请输入关键字"
            clearable
            size="small"
            style="width:180px"
          />
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleCheckItemFormQuery">查询</el-button>
          <el-button type="primary" icon="el-icon-refresh" size="mini" @click="resetItemFormQuery">重置</el-button>
        </el-form-item>

        <el-table
          v-loading="drawerLoading"
          height="600px"
          border
          :data="tableItemList"
          @selection-change="handleCheckItemSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="项目ID" align="center" prop="checkItemId" />
          <el-table-column label="项目名称" align="center" prop="checkItemName" />
          <el-table-column label="关键字" align="center" prop="keywords" />
          <el-table-column label="项目单价" align="center" prop="unitPrice" />
          <el-table-column label="单位" align="center" prop="unit" />
        </el-table>
        <!-- 数据表格结束 -->
        <!-- 分页控件开始 -->
        <el-pagination
          v-show="total>0"
          :current-page="queryItemFormParams.pageNum"
          :page-sizes="[5, 10, 20, 30]"
          :page-size="queryItemFormParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleCheckItemSizeChange"
          @current-change="handleCheckItemCurrentChange"
        />
        <el-form-item>
          <div style="text-align:center">
            <el-button type="primary" icon="el-icon-check" size="mini" @click="hanldeAddCareItem">添加并关闭</el-button>
          </div>
        </el-form-item>
      </el-form>

    </el-drawer>
    <!-- 检查项目的抽屉结束 -->
  </div>
</template>

<script>
import { queryToBeSeenRegistration, queryVisitingRegistration, queryVisitCompletedRegistration,
  receivePatient, getPatientAllMessageByPatientId,
  saveCareHistory, getCareHistoryByRegId, queryCareOrdersByChId,
  saveCareOrderItem, deleteCareOrderItemById, visitComplete
} from '@/api/docter/care'
import { listCheckItemForPage } from '@/api/system/checkItem'
import { listMedicinesForPage } from '@/api/erp/medicines'
export default {
  data() {
    return {
      // 总的遮罩层
      loading: false,
      // 表格的遮罩层
      tableLoading: false,
      // 门诊急诊的默认值
      schedulingType: '1',
      // 就诊类型的字典数据
      schedulingTypeOptions: [],
      // 排班类型的字典数据
      subsectionTypeOptions: [],
      // 性别数据字典
      sexOptions: [],
      // 接诊类型字典
      receiveTypeOptions: [],
      // 是否传染字典
      isContagiousOptions: [],
      // 处方详情的状态字典数据
      orderDetailsStatusOptions: [],
      // 是否打开选择挂号患者的弹层
      openRegistration: false,
      // 当前选中的挂号选项卡的
      activeName: 't1',
      // 右边的病例和处方的选项卡
      careActiveName: 'c1',
      // 待就诊数据
      toBeSeenRegistration: [],
      // 就诊中的数据
      visitingRegistration: [],
      // 就诊完成的数据
      visitCompletedRegistration: [],
      // 发病日期
      caseDateObj: new Date(),
      // 左边患者数据
      patientAllObj: {
        patientObj: {},
        patientFileObj: {},
        careHistoryObjList: []
      },
      // 提交到数据库的对象
      careHistory: {
        // 当前就诊中的挂号单ID
        regId: undefined,
        chId: undefined,
        caseDate: undefined,
        receiveType: '0',
        isContagious: '0',
        caseTitle: undefined,
        caseResult: undefined,
        doctorTips: undefined,
        remark: undefined,
        patientId: undefined,
        patientName: undefined
      },
      // 存放处方及详情数据
      careOrders: [],
      // 最后要提交到的处方及详情
      submitCareOrder: {
        careOrder: {
          allAmount: 0.00,
          patientId: undefined,
          patientName: undefined,
          coType: '0' // 默认为药用处方
        },
        careOrderItems: []
      },
      // 弹出层的标题
      title: '',
      // 是否打开药品和检查项的添加的弹出层
      openAddOrderItem: false,
      // 是否打开药品列表的抽屉
      openDrawerMedicines: false,
      // 是否打开检查项目列表的抽屉
      openDrawerCheckItem: false,
      // 药品和检查项目抽屉的查询条件
      queryItemFormParams: {
        pageNum: 1,
        pageSize: 10,
        keywords: undefined
      },
      // 药品或检查项目表格的数据
      tableItemList: [],
      // 抽屉里面数据的总条数
      total: 0,
      // 抽屉数据加载的遮罩
      drawerLoading: false,
      // 抽屉里面选中的数据
      selectItemList: []
    }
  },
  created() {
    // 加载排班类型 1 门诊  2急诊
    this.getDataByType('his_scheduling_type').then(res => {
      this.schedulingTypeOptions = res.data
    })
    // 排班时段 1 2 3 上午 下午 晚上
    this.getDataByType('his_subsection_type').then(res => {
      this.subsectionTypeOptions = res.data
    })
    // 加载性别
    this.getDataByType('sys_user_sex').then(res => {
      this.sexOptions = res.data
    })
    //  加载接诊类型字典
    this.getDataByType('his_receive_type').then(res => {
      this.receiveTypeOptions = res.data
    })
    //  加载是否传染字典
    this.getDataByType('his_contagious_status').then(res => {
      this.isContagiousOptions = res.data
    })
    //  加载处方详情的状态字典数据
    this.getDataByType('his_order_details_status').then(res => {
      this.orderDetailsStatusOptions = res.data
    })
  },
  methods: {
    // 门诊急诊切换事件
    schedulingTypeChange(value) {
      this.schedulingType = value
    },
    // 打开选择挂号患者的弹出层
    viewRegistration() {
      this.activeName = 't1'
      this.openRegistration = true
      this.queryToBeSeenRegistration()
    },
    // 挂号患者弹出层的选项卡change事件
    handleRegistrationTabClick(tab, event) {
      if (tab.name === 't1') {
        this.queryToBeSeenRegistration()
      } else if (tab.name === 't2') {
        this.queryVisitingRegistration()
      } else if (tab.name === 't3') {
        this.queryVisitCompletedRegistration()
      }
    },
    // 查询待就诊的数据
    queryToBeSeenRegistration() {
      this.tableLoading = true
      queryToBeSeenRegistration(this.schedulingType).then(res => {
        this.toBeSeenRegistration = res.data
        this.tableLoading = false
      }).catch(() => {
        this.msgError('查询失败')
        this.tableLoading = false
      })
    },
    // 查询就诊中的数据
    queryVisitingRegistration() {
      this.tableLoading = true
      queryVisitingRegistration(this.schedulingType).then(res => {
        this.visitingRegistration = res.data
        this.tableLoading = false
      }).catch(() => {
        this.msgError('查询失败')
        this.tableLoading = false
      })
    },
    // 查询就诊完成的数据
    queryVisitCompletedRegistration() {
      this.tableLoading = true
      queryVisitCompletedRegistration(this.schedulingType).then(res => {
        this.visitCompletedRegistration = res.data
        this.tableLoading = false
      }).catch(() => {
        this.msgError('查询失败')
        this.tableLoading = false
      })
    },

    // 翻译挂号类型
    schedulingTypeFormatter(row) {
      return this.selectDictLabel(this.schedulingTypeOptions, row.schedulingType)
    },
    // 翻译处方详情状态
    orderDetailsStatusFormatter(row) {
      return this.selectDictLabel(this.orderDetailsStatusOptions, row.status)
    },
    // 接诊
    handleVisit(row) {
      this.careHistory.regId = row.regId
      const patientId = row.patientId
      // 接诊，更新挂号的状态为就诊中并写入接诊的医生信息
      this.loading = true
      receivePatient(this.careHistory.regId).then(res => {
        this.loading = false
        this.msgSuccess('接诊成功')
        // 根据患者ID查询患者信息档案信息 病历信息
        getPatientAllMessageByPatientId(patientId).then(res => {
          this.patientAllObj.patientObj = res.data.patient
          this.patientAllObj.patientObj.age = this.getAge(res.data.patient.birthday)
          this.patientAllObj.patientFileObj = res.data.patientFile
          this.patientAllObj.careHistoryObjList = res.data.careHistoryList
          this.openRegistration = false
          this.loading = false
        }).catch(() => {
          this.msgError('查询患者信息失败')
          this.loading = false
        })
      }).catch(() => {
        this.msgError('接诊失败')
        this.loading = false
      })
    },
    // 载入
    handleLoading(row) {
      this.careHistory.regId = row.regId
      const patientId = row.patientId
      // 根据患者ID查询患者信息档案信息 病历信息
      getPatientAllMessageByPatientId(patientId).then(res => {
        this.patientAllObj.patientObj = res.data.patient
        this.patientAllObj.patientObj.age = this.getAge(res.data.patient.birthday)
        this.patientAllObj.patientFileObj = res.data.patientFile
        this.patientAllObj.careHistoryObjList = res.data.careHistoryList
        this.openRegistration = false
        this.loading = false
        // 查询当前挂号单之前对应的病例信息
      }).catch(() => {
        this.msgError('载入失败')
        this.loading = false
      })
      // 根据挂号单ID查询对应的病历信息
      this.getCareHistoryByRegId(row.regId)
    },
    // 根据挂号单ID查询对应的病历信息
    getCareHistoryByRegId(regId) {
      this.loading = true
      getCareHistoryByRegId(regId).then(res => {
        if (res.data != null) {
          this.careHistory = res.data
          this.caseDateObj = res.data.careDate
          // 如果有数据根据病历ID查询处方信息
          this.getCareOrdersByChId(res.data.chId)
        } else {
          this.resetCareHistoryAndCareOrders(regId)
        }
        this.loading = false
      }).catch(() => {
        this.msgError('查询病历失败')
        this.loading = false
      })
    },
    // 根据病历ID查询处方信息
    getCareOrdersByChId(chId) {
      this.loading = true
      queryCareOrdersByChId(chId).then(res => {
        this.careOrders = res.data
        this.loading = false
      }).catch(() => {
        this.msgError('查询详情失败')
        this.loading = false
      })
    },
    // 重置右边的病历和处方详情数据
    resetCareHistoryAndCareOrders(regId) {
      this.careHistory = {
        // 当前就诊中的挂号单ID
        regId: regId,
        chId: undefined,
        caseDate: undefined,
        caseDateObj: new Date(),
        receiveType: '0',
        isContagious: '0',
        caseTitle: undefined,
        caseResult: undefined,
        doctorTips: undefined,
        remark: undefined,
        patientId: undefined,
        patientName: undefined
      }
      this.careOrders = []
    },
    // 保存病历
    handleSaveCareHistory() {
      if (!this.careHistory.regId) {
        this.msgError('请选择挂号患者')
        return
      }
      // 封装数据
      this.careHistory.caseDate = this.moment(this.caseDateObj).format('YYYY-MM-DD')
      this.careHistory.patientId = this.patientAllObj.patientObj.patientId
      this.careHistory.patientName = this.patientAllObj.patientObj.name
      this.loading = true
      saveCareHistory(this.careHistory).then(res => {
        this.msgSuccess('保存病历成功')
        this.loading = false
        this.careHistory.chId = res.data.chId
      }).catch(() => {
        this.msgError('保存病历失败')
        this.loading = false
      })
    },

    // 打开添加药用处方的弹出层
    handelAddMedicinesOrder() {
      if (!this.careHistory.regId) {
        this.msgError('请选择挂号患者')
        return
      }
      if (!this.careHistory.chId) {
        this.msgError('请先保存病历')
        return
      }
      this.submitCareOrder.careOrder.coType = '0'
      this.title = '添加【药用】处方'
      this.openAddOrderItem = true
      this.submitCareOrder.careOrderItems = []
    },
    // 打开添加检查处方的弹出层
    handelAddCheckItemOrder() {
      if (!this.careHistory.regId) {
        this.msgError('请选择挂号患者')
        return
      }
      if (!this.careHistory.chId) {
        this.msgError('请先保存病历')
        return
      }
      this.submitCareOrder.careOrder.coType = '1'
      this.title = '添加【检查】处方'
      this.openAddOrderItem = true
      this.submitCareOrder.careOrderItems = []
    },
    // 打开药品或者检查项目的抽屉
    handleOpenAddOrderItemDrawer() {
      if (this.submitCareOrder.careOrder.coType === '0') {
        // 打开药口列表抽屉
        this.openDrawerMedicines = true
        this.resetItemFormQuery()
        this.getMedicinesList()
      } else {
        // 打开检查项目的抽屉
        this.openDrawerCheckItem = true
        this.resetItemFormQuery()
        this.getCheckItemList()
      }
    },

    // 加载药品数据
    getMedicinesList() {
      this.tableItemList = []
      this.drawerLoading = true
      listMedicinesForPage(this.queryItemFormParams).then(res => {
        this.tableItemList = res.data
        this.total = res.total
        this.drawerLoading = false
      }).catch(() => {
        this.drawerLoading = false
        this.msgError('查询药品失败')
      })
    },
    // 数据表格的多选择框选择时触发
    handleMedicinesSelectionChange(selection) {
      this.selectItemList = selection
    },
    // 分页pageSize变化时触发
    handleMedicinesSizeChange(val) {
      this.queryItemFormParams.pageSize = val
      // 重新查询
      this.getMedicinesList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleMedicinesCurrentChange(val) {
      this.queryItemFormParams.pageNum = val
      // 重新查询
      this.getMedicinesList()
    },
    // 搜索药品的方法
    handleMedicinesFormQuery() {
      this.queryItemFormParams.pageNum = 1
      this.getMedicinesList()
    },
    // 加载药品数据
    getCheckItemList() {
      this.tableItemList = []
      this.drawerLoading = true
      listCheckItemForPage(this.queryItemFormParams).then(res => {
        this.tableItemList = res.data
        this.total = res.total
        this.drawerLoading = false
      }).catch(() => {
        this.drawerLoading = false
        this.msgError('查询检查项目失败')
      })
    },
    // 数据表格的多选择框选择时触发
    handleCheckItemSelectionChange(selection) {
      this.selectItemList = selection
    },
    // 分页pageSize变化时触发
    handleCheckItemSizeChange(val) {
      this.queryItemFormParams.pageSize = val
      // 重新查询
      this.getCheckItemList()
    },
    // 点击上一页  下一页，跳转到哪一页面时触发
    handleCheckItemCurrentChange(val) {
      this.queryItemFormParams.pageNum = val
      // 重新查询
      this.getCheckItemList()
    },
    // 搜索检查项目的方法
    handleCheckItemFormQuery() {
      this.queryItemFormParams.pageNum = 1
      this.getCheckItemList()
    },
    // 重置抽屉的查询条件
    resetItemFormQuery() {
      this.queryItemFormParams = {
        pageNum: 1,
        pageSize: 10,
        keywords: undefined
      }
      if (this.submitCareOrder.careOrder.coType === '0') {
        this.getMedicinesList()
      } else {
        this.getCheckItemList()
      }
    },
    // 点击药品或检查项目抽屉页面的添加并关闭按钮
    hanldeAddCareItem() {
      const coType = this.submitCareOrder.careOrder.coType
      if (this.selectItemList.length === 0) {
        this.msgError('请选择【' + (coType === '0' ? '药品' : '检查项目') + '】')
        return
      }
      if (coType === '0') { // 药品
        this.selectItemList.filter(item => {
          const obj = {
            itemRefId: item.medicinesId,
            itemName: item.medicinesName,
            itemType: coType,
            num: 1,
            price: item.prescriptionPrice,
            amount: 1 * item.prescriptionPrice,
            remark: '请按说明服用'
          }
          let flag = false// 默认里面没有加
          this.submitCareOrder.careOrderItems.filter(i => {
            if (i.itemRefId === obj.itemRefId) {
              i.num = i.num + 1
              flag = true// 说明之前加过
            }
          })
          if (!flag) {
            this.submitCareOrder.careOrderItems.push(obj)
          }
          this.openDrawerMedicines = false
        })
      } else { // 检查项目
        this.selectItemList.filter(item => {
          const obj = {
            itemRefId: item.checkItemId,
            itemName: item.checkItemName,
            itemType: coType,
            num: 1,
            price: item.unitPrice,
            amount: 1 * item.unitPrice,
            remark: '按要求检查'
          }
          let flag = false// 默认里面没有加
          this.submitCareOrder.careOrderItems.filter(i => {
            if (i.itemRefId === obj.itemRefId) {
              i.num = i.num + 1
              flag = true// 说明之前加过
            }
          })
          if (!flag) {
            this.submitCareOrder.careOrderItems.push(obj)
          }
          this.openDrawerCheckItem = false
        })
      }
      // 计算总价
      this.computeOrderItemAllAmount()
    },
    // 删除弹出层里面的详情
    handleCareOrderItemDelete(row) {
      this.submitCareOrder.careOrderItems.splice(row.index, 1)
      this.computeOrderItemAllAmount()
    },
    // 把弹出层的表格的数据加上index
    tableRowClassName({ row, rowIndex }) {
      row.index = rowIndex
    },
    // 计算当前处方详情的总价
    computeOrderItemAllAmount() {
      this.submitCareOrder.careOrder.allAmount = 0.00
      this.submitCareOrder.careOrderItems.filter(item => {
        this.submitCareOrder.careOrder.allAmount +=
        (item.num * item.price)
      })
    },
    // 监听药品或检查项目弹出层的数量的变化
    handleCareOrderItemNumChange(row) {
      row.amount = row.num * row.price
      this.computeOrderItemAllAmount()
    },
    // 保存处方单信息
    handleSaveOrderItem() {
      if (this.submitCareOrder.careOrderItems.length === 0) {
        this.msgError('添加处方详情')
        return
      }
      this.submitCareOrder.careOrder.patientId = this.careHistory.patientId
      this.submitCareOrder.careOrder.patientName = this.careHistory.patientName
      this.submitCareOrder.careOrder.chId = this.careHistory.chId
      console.log(this.submitCareOrder)
      this.loading = true
      saveCareOrderItem(this.submitCareOrder).then(res => {
        this.loading = false
        this.msgSuccess('保存成功')
        // 刷新处方列表
        this.getCareOrdersByChId(this.careHistory.chId)
        // 关闭当前弹出层
        this.openAddOrderItem = false
      }).catch(() => {
        this.msgError('保存失败')
        this.loading = false
      })
    },
    // 根据详情ID删除数据库里面的详情【只能删除未支付的】
    handleCareOrderItemDeleteByItemId(row) {
      const itemId = row.itemId
      const itemName = row.itemName
      const tx = this
      tx.$confirm('是否确定删除【' + itemName + '】这条详情, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCareOrderItemById(itemId).then(res => {
          this.msgSuccess('删除成功')
          // 刷新处方列表
          this.getCareOrdersByChId(this.careHistory.chId)
        }).catch(() => {
          this.msgError('删除失败')
        })
      }).catch(() => {
        tx.msgError('删除已取消')
        tx.loading = false
      })
    },
    // 完成就诊
    handleVisitComplete() {
      const regId = this.careHistory.regId
      const patientName = this.careHistory.patientName
      const tx = this
      tx.$confirm('是否确定完成【' + patientName + '】的就诊, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        visitComplete(regId).then(res => {
          this.msgSuccess('操作成功')
          // 重置所有数据
          this.resetAllData()
        }).catch(() => {
          this.msgError('操作失败')
        })
      }).catch(() => {
        tx.msgError('操作已取消')
        tx.loading = false
      })
    },
    // 重置所有数据
    resetAllData() {
      this.patientAllObj = {
        patientObj: {},
        patientFileObj: {},
        careHistoryObjList: []
      }
      this.careHistory = {
        // 当前就诊中的挂号单ID
        regId: undefined,
        chId: undefined,
        caseDate: undefined,
        receiveType: '0',
        isContagious: '0',
        caseTitle: undefined,
        caseResult: undefined,
        doctorTips: undefined,
        remark: undefined,
        patientId: undefined,
        patientName: undefined
      }
      this.careOrders = []
      this.submitCareOrder = {
        careOrder: {
          allAmount: 0.00,
          patientId: undefined,
          patientName: undefined,
          coType: '0' // 默认为药用处方
        },
        careOrderItems: []
      }
    }
  }
}
</script>
<style scoped>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
  .item{
    font-size: 14px;
    margin-bottom: 8px;
  }
</style>
