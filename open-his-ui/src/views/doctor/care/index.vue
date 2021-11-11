<template>
  <div class="app-container">
    <!-- 门珍急诊选项卡开始-->
    <el-row :gutter="5" class="toprow">
      <el-col :span="24">
        <el-card style="margin-top:-10px;height:80px;">
          <el-tabs v-model="schedulingType" :stretch="true" type="card" @tab-click="handleScheTypeChange">
            <el-tab-pane
              v-for="d in schedulingTypeOptions"
              :key="d.dictValue"
              :label="d.dictLabel"
              :name="d.dictValue"
            />
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
    <!-- 门珍急诊选项卡结束-->
    <el-row :gutter="5">
      <el-col :span="7">
        <!-- 左边患者所有信息（基本信息，档案，病例）卡片 -->
        <el-card style="margin-top:8px;font-size:14px;">
          <!-- 患者基本信息 -->
          <el-form ref="patientForm" style="text-align:left;" :model="patientAllObj.patientObj" label-width="70px">
            <el-form-item label="患者姓名">
              <el-input v-model="patientAllObj.patientObj.name" size="mini" :disabled="true" placeholder="请输入患者姓名">
                <el-button slot="append" icon="el-icon-user-solid" @click="showRegistrationDialog" />
              </el-input>
            </el-form-item>
            <el-form-item label="身份证号">
              <el-input v-model="patientAllObj.patientObj.idCard" style="width:180px;" size="mini" :disabled="true" placeholder="请输入身份证号" />
            </el-form-item>
            <el-form-item label="患者电话">
              <el-input v-model="patientAllObj.patientObj.phone" style="width:180px;" size="mini" :disabled="true" placeholder="请输入患者电话" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="patientAllObj.patientObj.sex">
                <el-radio
                  v-for="d in sexOptions"
                  :key="d.dictValue"
                  :value="d.dictValue"
                  :label="d.dictValue"
                >{{ d.dictLabel }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="出生日期">
              <el-date-picker
                v-model="patientAllObj.patientObj.birthday"
                type="date"
                placeholder="选择日期"
                size="mini"
                @change="birthdayChange"
              />
            </el-form-item>
            <el-form-item label="患者年龄">
              <el-input v-model="patientAllObj.patientObj.age" size="mini" t style="width:180px;" placeholder="请输入年龄" />
            </el-form-item>
            <el-form-item label="地址信息">
              <el-input
                v-model="patientAllObj.patientObj.address"
                minlength="4"
                maxlength="30"
                show-word-limit
                type="textarea"
                size="small"
                placeholder="请输入地址"
              />
            </el-form-item>
            <el-form-item label="过敏史">
              <el-input
                v-model="patientAllObj.patientObj.allergyInfo"
                minlength="5"
                maxlength="100"
                show-word-limit
                type="textarea"
                size="small"
                placeholder="请输入过敏史"
              />
            </el-form-item>
          </el-form>
          <!-- 患者档案和历史病例选项卡 -->
          <el-tabs v-model="activeLeftName" :stretch="true" type="card" style="width:100%;" @tab-click="handleleftTabChange">
            <el-tab-pane label="患者档案" name="t1">
              <div v-if="patientAllObj.patientObj.isFinal==='0'" style="text-align:center">暂无患者档案</div>
              <div v-else class="patien-File-styl">
                <div>紧急联系人: <span v-text="patientAllObj.patientFileObj.emergencyContactName" /></div>
                <div>紧急联系人电话: <span v-text="patientAllObj.patientFileObj.emergencyContactPhone" /></div>
                <div>关系: <span v-text="patientAllObj.patientFileObj.emergencyContactRelation" /></div>
                <div>左耳听力: <span v-text="patientAllObj.patientFileObj.leftEarHearing" /></div>
                <div>右耳听力: <span v-text="patientAllObj.patientFileObj.rightEarHearing" /></div>
                <div>左眼视力: <span v-text="patientAllObj.patientFileObj.leftVision" /></div>
                <div>右眼视力: <span v-text="patientAllObj.patientFileObj.rightVision" /></div>
                <div>身高: <span v-text="patientAllObj.patientFileObj.height" /></div>
                <div>体重: <span v-text="patientAllObj.patientFileObj.weight" /></div>
                <div>血型:<span v-text="patientAllObj.patientFileObj.bloodType" /></div>
                <div>个人史: <span v-text="patientAllObj.patientFileObj.personalInfo" /></div>
                <div>家族史: <span v-text="patientAllObj.patientFileObj.familyInfo" /></div>
                <div>档案创建时间: <span v-text="patientAllObj.patientFileObj.createTime" /></div>
                <div>档案更新时间: <span v-text="patientAllObj.patientFileObj.updateTime" /></div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="历史病例" name="t2">
              <div v-if="patientAllObj.careHistoryList.length===0" style="text-align:center">暂无历史病例</div>
              <div v-else style="text-align:center;padding-left:50px">
                <el-collapse v-model="activeLeftCareName" accordion @change="handleLeftCareChange">
                  <el-collapse-item v-for="(ch,index) in patientAllObj.careHistoryList" :key="ch.Id" :name="index">
                    <template slot="title" @click="handleLeftCareChange(index)">
                      科室：<span style="margin-right:15px;" v-text=" '【'+ch.deptName+'】'" />  就诊时间：<span v-text="ch.careTime" />
                    </template>
                    <div style="text-align:left;margin-left:25px;">
                      <div>医生姓名：{{ ch.userName }}</div>
                      <div>接诊类型：{{ ch.receiveType | receiveTypeFormater }}</div>
                      <div>是否传染：{{ ch.isContagious |isContagiousFormatter }}</div>
                      <div>发病日期：{{ ch.caseDate }}</div>
                      <div>主诉：{{ ch.caseTitle }}</div>
                      <div>诊断信息：{{ ch.caseResult }}</div>
                      <div>医生建议：{{ ch.doctorTips }}</div>
                      <div>备注：{{ ch.remark }}</div>
                      <div>挂号单号：{{ ch.regId }}</div>
                      <div>病例单号：{{ ch.chId }}</div>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
      <el-col :span="17">
        <!-- 右边卡片 -->
        <!-- 右上面的卡片 -->
        <el-card style="margin-top:8px;">
          <el-row :gutter="5">
            <el-col :span="12" style="font-size:14px;">
              挂号单ID:
              <span v-if="careHistory.regId===undefined" style="color:red;">选择患者后显示</span>
              <span v-else style="color:blue;">{{ careHistory.regId }}</span><br>
              病历编号:
              <span v-if="careHistory.chId===undefined" style="color:red;">保存病例后生成</span>
              <span v-else style="color:blue;">{{ careHistory.chId }}</span><br>
            </el-col>
            <el-col :span="12" style="text-align:right">
              <el-button type="primary" size="small" icon="el-icon-check" :disabled="careHistory.regId===undefined" @click="HandlesaveHistory">保存病例</el-button>
              <el-button type="danger" size="small" icon="el-icon-finished" :disabled="careHistory.regId===undefined" @click="HandleCompleteCare">就诊完成</el-button>
            </el-col>
          </el-row>
        </el-card>
        <!-- 右下面的卡片 -->
        <el-card style="margin-top:3px;">
          <el-tabs v-model="activeRightName" type="card" @tab-click="handleRightTabChange">
            <el-tab-pane label="病例" name="t1">
              <el-card style="padding-left:20px;">
                <!-- 病例选项卡内容 -->
                <el-form ref="careHistoryForm" :model="careHistory" :rules="careHistoryRules" label-width="150px" :inline="true">
                  <el-form-item label="发病日期" prop="caseDate">
                    <el-date-picker
                      v-model="careHistory.caseDate"
                      style="width:180px"
                      value-format="yyyy-MM-dd"
                      size="mini"
                      type="date"
                      placeholder="选择发病日期"
                    />
                  </el-form-item>
                  <el-form-item label="接诊类型" prop="receiveType">
                    <el-select v-model="careHistory.receiveType" style="width:180px" size="mini" placeholder="请选择接诊类型">
                      <el-option
                        v-for="d in receiveTypeOptions"
                        :key="d.dictValue"
                        :label="d.dictLabel"
                        :value="d.dictValue"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="是否传染" prop="isContagious">
                    <el-select v-model="careHistory.isContagious" size="mini" placeholder="是否感染">
                      <el-option
                        v-for="d in isContagiousOptions"
                        :key="d.dictValue"
                        :label="d.dictLabel"
                        :value="d.dictValue"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="主诉" prop="caseTitle">
                    <el-input
                      v-model="careHistory.caseTitle"
                      style="width:700px;"
                      minlength="0"
                      maxlength="200"
                      :rows="4"
                      show-word-limit
                      type="textarea"
                      resize="vertical"
                      size="small"
                      placeholder="请输入主诉"
                    />
                  </el-form-item>
                  <el-form-item label="诊断信息" prop="caseResult">
                    <el-input
                      v-model="careHistory.caseResult"
                      style="width:700px"
                      minlength="0"
                      maxlength="200"
                      show-word-limit
                      :rows="4"
                      type="textarea"
                      resize="vertical"
                      size="small"
                      placeholder="请输入诊断信息"
                    />
                  </el-form-item>
                  <el-form-item label="医生建议" prop="doctorTips">
                    <el-input
                      v-model="careHistory.doctorTips"
                      style="width:700px"
                      minlength="0"
                      maxlength="200"
                      :rows="4"
                      show-word-limit
                      type="textarea"
                      resize="vertical"
                      size="small"
                      placeholder="请输入医生建议"
                    />
                  </el-form-item>
                  <el-form-item label="备注" prop="remark">
                    <el-input
                      v-model="careHistory.remark"
                      style="width:700px"
                      minlength="0"
                      maxlength="200"
                      show-word-limit
                      :rows="4"
                      type="textarea"
                      resize="vertical"
                      size="small"
                      placeholder="请输入备注"
                    />
                  </el-form-item>
                </el-form>
                <!-- {{ careHistory }} -->
              </el-card>
            </el-tab-pane>
            <el-tab-pane label="处方" name="t2">
              <div v-if="careOrderList.length===0" style="color:red">医生暂未开处方</div>
              <div v-else>
                <el-collapse v-model="activeRightCareOrderName" accordion @change="handleRightCareOrderChange">
                  <el-collapse-item v-for="(coAndItem,index) in careOrderList" :key="index" :name="index">
                    <template slot="title">
                      <span style="">{{ '【'+(coAndItem.careOrder.coType=="0"?'药用处方':'检查处方')+'】' }}</span>
                      <span v-text="'【'+(index+1)+'】'" />
                      <span>【处方金额】:</span>
                      ￥{{ coAndItem.careOrder.allAmount }}
                    </template>
                    <div style="text-align:left;margin-left:25px;">
                      <!-- 处方详情表格 -->
                      <el-table border :data="coAndItem.careOrderItems" style="margin-top:2px">
                        <el-table-column width="50px" type="index" label="序号" align="center" />
                        <el-table-column width="100px" label="项目名称" prop="itemName" align="center" />
                        <el-table-column label="单位" width="50px" prop="itemType" align="center" :formatter="CoItemUnitFormatter" />
                        <el-table-column label="单价（元）" width="100px" prop="price" align="center" />
                        <el-table-column label="处方次数" width="100px" prop="num" align="center" />
                        <el-table-column label="金额（元）" prop="amount" align="center" />
                        <el-table-column label="备注" prop="remark" align="center" />
                        <el-table-column label="状态" prop="status" align="center" :formatter="coItemStatusFormatter" />
                        <el-table-column width="100px" label="操作" align="center">
                          <template slot-scope="scope">
                            <el-button v-show="scope.row.status==0" type="text" icon="el-icon-delete" size="mini" @click="handleDeleteCoItem(scope.row)">删除</el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
              <div style="text-align:left">
                <!-- 处方选项卡按钮 -->
                <el-button :disabled="careHistory.chId===undefined" icon="el-icon-plus" size="small" type="success" @click="handleAddMedicineOrder">添加药用处方</el-button>
                <el-button :disabled="careHistory.chId===undefined" icon="el-icon-plus" size="small" type="success" @click="handleAddCheckOrder">添加检查处方</el-button>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
    <!-- 选择挂号患者的对话框dialog开始 -->
    <el-dialog
      title="选择挂号患者"
      :visible.sync="RegistrationVisible"
      :close-on-click-modal="false"
      width="1000px"
      center
      @close="dialogClose"
    >
      <!-- 挂号单状态选项卡 -->
      <el-tabs v-model="activeDialogName" :stretch="true" type="card" @tab-click="handleDialogTabChange">
        <el-tab-pane label="待就诊列表" name="t1">
          <!-- 待就诊列表数据表格 -->
          <el-table v-loading="dialogloading" border :data="ToBeSeenRegistration" style="margin-top:2px">
            <el-table-column type="expand" label="详情" width="55">
              <template slot-scope="props">
                <el-form label-position="left" inline class="RegTableList_table_expand">
                  <el-row :gutter="10">
                    <el-col :span="12">
                      <!-- 展开项 -->
                      <el-form-item label="挂号流水">
                        <span>{{ props.row.registrationId }}</span>
                      </el-form-item><br>
                      <el-form-item label="科室">
                        <span>{{ props.row.deptId | deptFormatter }}</span>
                      </el-form-item><br>
                      <el-form-item label="挂号员">
                        <span>{{ props.row.createBy }}</span>
                      </el-form-item><br>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="就诊日期">
                        <span>{{ props.row.visitDate }}</span>
                      </el-form-item><br>
                      <el-form-item label="创建时间">
                        <span>{{ props.row.createTime }}</span>
                      </el-form-item><br>
                      <el-form-item label="挂号时段">
                        <span>{{ props.row.subsectionType | subsectionTypeFormater }}</span>
                      </el-form-item><br>
                    </el-col>
                  </el-row>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column width="200px" label="患者姓名" prop="patientName" align="center" />
            <el-table-column label="流水编号" width="200px" prop="registrationNumber" align="center" />
            <el-table-column label="挂号类型" width="200px" prop="schedulingType" align="center" :formatter="scheTypeFormatter" />
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button type="success" icon="el-icon-check" size="mini" @click="handleReceive(scope.row)">接诊</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="就诊中列表" name="t2">
          <!-- 就诊中列表数据表格 -->
          <el-table v-loading="dialogloading" border :data="VisitingRegistration" style="margin-top:2px">
            <el-table-column type="expand" label="详情" width="55">
              <template slot-scope="props">
                <el-form label-position="left" inline class="RegTableList_table_expand">
                  <el-row :gutter="10">
                    <el-col :span="12">
                      <!-- 展开项 -->
                      <el-form-item label="挂号流水">
                        <span>{{ props.row.registrationId }}</span>
                      </el-form-item><br>
                      <el-form-item label="接诊医生">
                        <span>{{ props.row.doctorName || '暂未接诊' }} </span>
                      </el-form-item><br>
                      <el-form-item label="科室">
                        <span>{{ props.row.deptId | deptFormatter }}</span>
                      </el-form-item><br>
                      <el-form-item label="挂号员">
                        <span>{{ props.row.createBy }}</span>
                      </el-form-item><br>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="就诊日期">
                        <span>{{ props.row.visitDate }}</span>
                      </el-form-item><br>
                      <el-form-item label="创建时间">
                        <span>{{ props.row.createTime }}</span>
                      </el-form-item><br>
                      <el-form-item label="挂号时段">
                        <span>{{ props.row.subsectionType | subsectionTypeFormater }}</span>
                      </el-form-item><br>
                    </el-col>
                  </el-row>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column width="200px" label="患者姓名" prop="patientName" align="center" />
            <el-table-column label="流水编号" width="200px" prop="registrationNumber" align="center" />
            <el-table-column label="挂号类型" width="200px" prop="schedulingType" align="center" :formatter="scheTypeFormatter" />
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button type="success" icon="el-icon-loading" size="mini" @click="handleLoading(scope.row)">载入</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="就诊完成列表" name="t3">
          <!-- 就诊完成列表数据表格-->
          <el-table v-loading="dialogloading" border :data="VisitCompletedRegistration" style="margin-top:2px">
            <el-table-column type="expand" label="详情" width="55">
              <template slot-scope="props">
                <el-form label-position="left" inline class="RegTableList_table_expand">
                  <el-row :gutter="10">
                    <el-col :span="12">
                      <!-- 展开项 -->
                      <el-form-item label="挂号流水">
                        <span>{{ props.row.registrationId }}</span>
                      </el-form-item><br>
                      <el-form-item label="接诊医生">
                        <span>{{ props.row.doctorName || '暂未接诊' }} </span>
                      </el-form-item><br>
                      <el-form-item label="科室">
                        <span>{{ props.row.deptId | deptFormatter }}</span>
                      </el-form-item><br>
                      <el-form-item label="挂号员">
                        <span>{{ props.row.createBy }}</span>
                      </el-form-item><br>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="就诊日期">
                        <span>{{ props.row.visitDate }}</span>
                      </el-form-item><br>
                      <el-form-item label="创建时间">
                        <span>{{ props.row.createTime }}</span>
                      </el-form-item><br>
                      <el-form-item label="挂号时段">
                        <span>{{ props.row.subsectionType | subsectionTypeFormater }}</span>
                      </el-form-item><br>
                    </el-col>
                  </el-row>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column width="200px" label="患者姓名" prop="patientName" align="center" />
            <el-table-column label="流水编号" width="200px" prop="registrationNumber" align="center" />
            <el-table-column label="挂号类型" width="200px" prop="schedulingType" align="center" :formatter="scheTypeFormatter" />
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button type="success" icon="el-icon-loading" size="mini" @click="handleLoading(scope.row)">载入</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <!-- 选择挂号患者的对话框dialog结束 -->

    <!-- 添加药用和检查处方弹出层开始 -->
    <el-dialog
      :title="addOrderTitle"
      :visible.sync="addOrderVisible"
      :close-on-click-modal="false"
      width="1000px"
      center
      append-to-body
      @close="addOrderDialogClose"
    >
      <el-row :gutter="5" style="margin-bottom:10px">
        <el-col :span="12">
          <el-button type="success" size="small" icon="el-icon-plus" @click="handleAddOrder">确定添加</el-button>
        </el-col>
        <el-col :span="12" style="text-align:right;padding-top:15px;">
          药品处方总金额: ￥ <span style="color:red">{{ submitCareOrder.careOrder.allAmount }} </span>
        </el-col>
      </el-row>
      <!-- 勾选的处方详情表格 -->
      <el-table empty-text="暂未选择处方" border :data="submitCareOrder.careOrderItems">
        <el-table-column width="80px" type="index" label="序号" align="center" />
        <el-table-column width="150px" label="药品名称" prop="itemName" align="center" />
        <el-table-column label="数量" width="150px" prop="num" align="center">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.num" style="width:120px;" size="small" :min="1" @change="handleOrderNumChange" />
          </template>
        </el-table-column>
        <el-table-column label="单价（元）" width="100px" prop="price" align="center" />
        <el-table-column label="金额（元）" prop="amount" align="center" />
        <el-table-column label="服用备注" prop="remark" align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.remark" clearable placeholder="请输入服用备注" />
          </template>
        </el-table-column>
        <el-table-column width="100px" label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              icon="el-icon-delete"
              size="mini"
              @click.native.prevent="deleteItemofRow(scope.$index, submitCareOrder.careOrderItems)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button
        type="primary"
        style="width:100%;"
        size="mini"
        @click="openAddOrderDialog"
      >添加药品项</el-button>
    </el-dialog>
    <!-- 添加药用和检查处方弹出层结束 -->

    <!-- 选择药品处方列表蒙蔽开始 -->
    <el-drawer
      title=""
      :wrapper-closable="false"
      :visible.sync="orderListdrawer"
    >
      <div style="margin-top:40px;text-align:center;font-Size:20px;font-weight: bold;">药品列表</div>
      <!-- 处方列表搜索关键字 -->
      <el-form ref="searchOrderForm" :rules="orderListsearchRules" style="margin-left:10px;margin-top:20px;text-align:left;" :inline="true" :model="searchOrderForm">
        <el-form-item prop="search" label="关键字">
          <el-input v-model="searchOrderForm.search" size="mini" placeholder="请输入关键字" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" icon="el-icon-search" @click="orderListSearch">搜索</el-button>
          <el-button type="primary" size="mini" icon="el-icon-refresh" @click="orderListReset">重置</el-button>
        </el-form-item>
      </el-form>
      <!-- 处方列表表格 -->
      <el-table
        ref="OrderListTable"
        :data="OrderListTable"
        height="70%"
        tooltip-effect="light"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column align="center" prop="medicinesId" label="药品ID" width="80" />
        <el-table-column align="center" prop="medicinesName" label="药品名称" width="100" />
        <el-table-column align="center" prop="medicinesStockNum" label="库存" width="100" />
        <el-table-column align="center" prop="unit" label="单位" width="80" />
        <el-table-column align="center" prop="conversion" label="换算量" width="80" />
      </el-table>
      <div style="text-align:center">
        <el-button type="primary" icon="el-icon-check" size="mini" @click="addOrderAndClose">添加并关闭</el-button>
      </div>

    </el-drawer>
    <!-- 选择药品处方列表蒙蔽结束 -->

  </div>
</template>

<script>
let that
import { selectAllMedicines, searchMedicinesByName } from '@/api/erp/medicines/medicines'
import { selectAllDept } from '@/api/system/dept/dept'
import { getDataByType } from '@/api/system/dict/data'
import { queryToBeSeenRegistration, queryVisitingRegistration, queryVisitCompletedRegistration, receivePatient, getPatientAllMessageByPatientId, saveCareHistory, getCareHistoryByRegId, queryCareOrdersByChId, saveCareOrderItem, deleteCareOrderItemById, visitComplete } from '@/api/doctor/care/care'

export default {
  filters: {
    // 翻译部门
    deptFormatter(deptId) {
      return that.selectDeptLabel(that.deptOptions, deptId)
    },
    // 翻译挂号时段
    subsectionTypeFormater(subsectionType) {
      return that.subsectionOptions.filter((item) => {
        if (item.dictValue === subsectionType) {
          return item.dictLabel
        }
      })[0].dictLabel
    },
    // 接诊类型翻译
    receiveTypeFormater(receiveType) {
      return that.receiveTypeOptions.filter((item) => {
        if (item.dictValue === receiveType) {
          return item.dictLabel
        }
      })[0].dictLabel
    },
    // 是否传染翻译
    isContagiousFormatter(isContagious) {
      return that.isContagiousOptions.filter((item) => {
        if (item.dictValue === isContagious) {
          return item.dictLabel
        }
      })[0].dictLabel
    }

  },
  data() {
    return {
      // 科室数据
      deptOptions: [],
      // 门诊急诊选项卡
      schedulingType: '1',
      // 挂号类型门诊急诊数据
      schedulingTypeOptions: [],
      // 性别数据
      sexOptions: [],
      // 左边患者所有信息的数据
      patientAllObj: {
        // 患者信息
        patientObj: {
          sex: '2',
          address: '江西赣州',
          allergyInfo: '无过敏信息',
          age: '18',
          isFinal: '0'
        },
        // 患者档案信息
        patientFileObj: {

        },
        // 历史病例集合
        careHistoryList: []
      },
      // 左边患者档案和历史病例选项卡
      activeLeftName: 't1',
      // 右边病例处方选项卡
      activeRightName: 't1',
      // 挂号弹出层是否打开
      RegistrationVisible: false,
      // 弹出层中选项卡
      activeDialogName: 't1',
      // 待就诊数据
      ToBeSeenRegistration: [],
      // 就诊中数据
      VisitingRegistration: [],
      // 就诊完成数据
      VisitCompletedRegistration: [],
      // 对话框数据表格遮罩
      dialogloading: false,
      // 挂号类型和时段
      schedulingOptions: [],
      subsectionOptions: [],
      // 右边病例表单对象数据
      careHistory: {

      },
      // 接诊类型数据
      receiveTypeOptions: [],
      // 是否传染
      isContagiousOptions: [],
      // 左边历史病例选中
      activeLeftCareName: '',
      // 最新挂号的挂号单
      nowRegId: undefined,
      // 病例表单验证规则
      careHistoryRules: {
        receiveType: [
          { required: true, message: '接诊类型必选 ', trigger: 'change' }
        ],
        isContagious: [
          { required: true, message: '是否传染必选', trigger: 'change' }
        ],
        caseDate: [
          { required: true, message: '发病日期必填', trigger: 'change' }
        ],
        caseTitle: [
          { required: true, message: '主诉必填', trigger: 'change' }
        ],
        caseResult: [
          { required: true, message: '诊断信息必填', trigger: 'change' }
        ]
      },
      // 右边处方列表数据
      careOrderList: [],
      // 右边处方手风琴
      activeRightCareOrderName: '',
      // 处方详情状态字典
      orderItemStatus: [],
      // 添加处方弹出层标题
      addOrderTitle: '添加药用处方',
      // 添加处方弹出层显示
      addOrderVisible: false,
      // 添加处方的数据
      submitCareOrder: {
        careOrder: {
          coType: '0',
          paatientId: undefined,
          patientName: undefined,
          chId: undefined,
          allAmount: 0

        },
        careOrderItems: []
      },
      // 药品处方列表蒙蔽显示
      orderListdrawer: false,
      // 蒙蔽中处方搜索表单
      searchOrderForm: {
        // 搜索关键字
        search: undefined
      },
      // 处方列表搜索表单验证
      orderListsearchRules: {
        search: [
          { required: true, message: '请输入关键词', trigger: 'change' }
        ]
      },
      // 处方列表表格数据
      OrderListTable: [],
      // 选中的处方列表数据
      multipleSelection: [],
      // 保存选中行，用于反选
      SelectionRow: []

    }
  },
  beforeCreate() {
    that = this
  }, created() {
    // 查询所有部门
    selectAllDept().then(res => {
      this.deptOptions = res.data
    })
    // 查询挂号类型和时段
    getDataByType('his_scheduling_type').then(res => {
      this.schedulingOptions = res.data
    })
    getDataByType('his_subsection_type').then(res => {
      this.subsectionOptions = res.data
    })
    // 查询门诊急诊字典数据
    getDataByType('his_scheduling_type').then(res => {
      this.schedulingTypeOptions = res.data
    })
    // 查询性别数据
    getDataByType('sys_user_sex').then(res => {
      this.sexOptions = res.data
    })
    // 查询接诊类型
    getDataByType('his_receive_type').then(res => {
      this.receiveTypeOptions = res.data
    })
    // 查询是否传染
    getDataByType('his_contagious_status').then(res => {
      this.isContagiousOptions = res.data
    })
    // 查询处方详情状态字典数据
    getDataByType('his_order_details_status').then(res => {
      this.orderItemStatus = res.data
    })
    // 查询处方列表数据
    selectAllMedicines().then(res => {
      this.OrderListTable = res.data
    })
  }, methods: {
    // 门诊急诊选项卡改变事件
    handleScheTypeChange(tab, event) {
      this.schedulingType = tab.paneName
      // console.log(tab)
    },
    // 点击患者姓名图标，打开选择挂号单对话框
    showRegistrationDialog() {
      this.RegistrationVisible = true
      // 默认查询待就诊的数据
      this.queryToBeSeenReg()
    },
    // 左边患者档案和历史病例选项卡改变
    handleleftTabChange(tab, event) {

    },
    // 右边病例处方选项卡改变事件
    handleRightTabChange(tab, event) {
      // 如果有病例编号加载右边处方信息和处方详情信息
      if (this.careHistory.chId !== undefined) {
        queryCareOrdersByChId(this.careHistory.chId).then(res => {
          this.careOrderList = res.data
        })
      }
    },
    // 弹出层选项卡的改变事件
    handleDialogTabChange(tab, event) {
      this.dialogloading = true
      if (this.activeDialogName === 't1') {
        // 加载待就诊数据
        this.queryToBeSeenReg()
        this.dialogloading = false
      } else if (this.activeDialogName === 't2') {
        // 加载就诊中数据
        queryVisitingRegistration(this.schedulingType).then(res => {
          this.VisitingRegistration = res.data
          this.dialogloading = false
        }).catch(() => {
          this.msgError('查询就诊中数据失败')
          this.dialogloading = false
        })
      } else if (this.activeDialogName === 't3') {
        // 加载就诊完成数据
        queryVisitCompletedRegistration(this.schedulingType).then(res => {
          this.VisitCompletedRegistration = res.data
          this.dialogloading = false
        }).catch(() => {
          this.msgError('查询就诊完成数据失败')
          this.dialogloading = false
        })
      }
    },
    // 查询待就诊数据的方法
    queryToBeSeenReg() {
      queryToBeSeenRegistration(this.schedulingType).then(res => {
        this.ToBeSeenRegistration = res.data
      }).catch(() => {
        this.msgError('查询待就诊数据失败')
      })
    },
    // 挂号类型翻译
    scheTypeFormatter(row) {
      return this.selectDictLabel(this.schedulingOptions, row.schedulingType)
    },
    // 接诊
    handleReceive(row) {
      this.careHistory = {}
      this.dialogloading = true
      this.careHistory.regId = row.registrationId
      this.nowRegId = row.registrationId
      receivePatient(row.registrationId).then(res => {
        this.msgSuccess('接诊成功')
        this.dialogloading = false
        this.RegistrationVisible = false
        this.getPatient(row.patientId)
      }).catch(() => {
        this.msgError('接诊失败')
        this.dialogloading = false
      })
    },
    // 载入
    handleLoading(row) {
      this.careHistory = {}
      this.dialogloading = true
      this.careHistory.regId = row.registrationId
      this.nowRegId = row.registrationId
      this.RegistrationVisible = false
      this.getPatient(row.patientId)
    },
    // 加载患者信息
    getPatient(patientId) {
      // 加载患者所有信息
      getPatientAllMessageByPatientId(patientId).then(res => {
        this.patientAllObj.patientObj = res.data.patient
        this.patientAllObj.patientFileObj = res.data.patientFile
        this.patientAllObj.careHistoryList = res.data.careHistoryList
        // 计算年龄
        this.patientAllObj.patientObj.age = this.getAge(this.patientAllObj.patientObj.birthday)
        // console.log(res.data)
        this.dialogloading = false
        this.careHistory.careTime = this.moment(Date.now()).format('YYYY-MM-DD HH:mm:ss')

        // 如果挂号单有病例，就将病例显示出来
        getCareHistoryByRegId(this.careHistory.regId).then(res => {
          if (res.data !== null) {
            this.careHistory = res.data
          }
        })
      }).catch(() => {
        this.msgError('数据加载失败')
        this.dialogloading = false
      })
    },

    // 生日改变事件
    birthdayChange(birthday) {
      this.patientAllObj.patientObj.birthday = this.moment(birthday).format('YYYY-MM-DD')
      // 计算患者年龄
      this.patientAllObj.patientObj.age = this.getAge(this.patientAllObj.patientObj.birthday)
    },
    // 对话框关闭事件
    dialogClose() {
      // 待就诊数据
      this.ToBeSeenRegistration = []
      // 就诊中数据
      this.VisitingRegistration = []
      // 就诊完成数据
      this.VisitCompletedRegistration = []
      this.activeDialogName = 't1'
    },
    // 保存病例
    HandlesaveHistory() {
      this.$refs['careHistoryForm'].validate(valid => {
        if (valid) {
          this.careHistory.patientId = this.patientAllObj.patientObj.patientId
          this.careHistory.patientName = this.patientAllObj.patientObj.name

          saveCareHistory(this.careHistory).then(res => {
            this.msgSuccess('保存病例成功')
            this.careHistory = res.data
            this.getPatient(this.patientAllObj.patientObj.patientId)
          }).catch(() => {
            this.msgError('保存病例失败')
          })
        }
      })
    },
    // 就诊完成
    HandleCompleteCare() {
      visitComplete(this.careHistory.regId).then(res => {
        this.msgSuccess('已就诊完成，您辛苦了!!')
        // 加载就诊完成数据
        queryVisitCompletedRegistration(this.schedulingType).then(res => {
          this.VisitCompletedRegistration = res.data
          this.dialogloading = false
        }).catch(() => {
          this.msgError('查询就诊完成数据失败')
          this.dialogloading = false
        })
      }).catch(() => {
        this.msgError('完成就诊失败')
      })
    },
    // 添加药用处方
    handleAddMedicineOrder() {
      this.addOrderTitle = '添加【药用】处方'
      this.addOrderVisible = true
    },
    // 添加检查处方
    handleAddCheckOrder() {

    },
    // 右边处方手风琴改变事件
    handleRightCareOrderChange(activeRightCareOrderName) {

    },

    // 后台删除处方详情
    handleDeleteCoItem(row) {
      this.$confirm('确定删除名为【' + row.itemName + '】的处方详情吗？', '删除详情', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        type: 'waring',
        center: true
      }).then(() => {
        deleteCareOrderItemById(row.itemId).then(res => {
          this.msgSuccess('删除成功')
          // 如果有病例编号加载右边处方信息和处方详情信息
          if (this.careHistory.chId !== undefined) {
            queryCareOrdersByChId(this.careHistory.chId).then(res => {
              this.careOrderList = res.data
            })
          }
        }).catch(() => {
          this.msgError('删除失败')
        })
      }).catch(() => {
        this.msgError('取消删除')
      })
    },
    // 添加处方弹出层页面中删除处方（静态）
    deleteItemofRow(index, rows) {
      rows.splice(index, 1)
      this.resetmoeny()
    },
    // 确认后台进行添加处方
    handleAddOrder() {
      this.submitCareOrder.careOrder.patientId = this.patientAllObj.patientObj.patientId
      this.submitCareOrder.careOrder.patientName = this.patientAllObj.patientObj.name
      this.submitCareOrder.careOrder.chId = this.careHistory.chId
      if (this.submitCareOrder.careOrderItems.length === 0) {
        this.msgError('未选择处方')
        return
      }
      saveCareOrderItem(this.submitCareOrder).then(res => {
        this.msgSuccess('添加处方成功')
        // 如果有病例编号加载右边处方信息和处方详情信息
        if (this.careHistory.chId !== undefined) {
          queryCareOrdersByChId(this.careHistory.chId).then(res => {
            this.careOrderList = res.data
          })
        }
        this.addOrderVisible = false
      }).catch(() => {
        this.msgError('添加处方失败')
        this.addOrderVisible = false
      })
    },
    // 打开右侧选择处方药品列表蒙版
    openAddOrderDialog() {
      if (this.SelectionRow) {
        this.SelectionRow.forEach(row => {
          this.$refs.OrderListTable.toggleRowSelection(row)
        })
      } else {
        this.$refs.OrderListTable.clearSelection()
      }

      this.orderListdrawer = true
    },
    // 选择处方蒙蔽中搜索
    orderListSearch() {
      this.$refs['searchOrderForm'].validate(valid => {
        if (valid) {
          searchMedicinesByName(this.searchOrderForm.search).then(res => {
            this.OrderListTable = res.data
          })
        }
      })
    },
    // 选择处方蒙蔽中重置
    orderListReset() {
      this.searchOrderForm.search = undefined
      // 查询处方列表数据
      selectAllMedicines().then(res => {
        this.OrderListTable = res.data
      })
    },
    // 选择处方蒙蔽选中事件
    handleSelectionChange(val) {
      this.SelectionRow = []
      this.multipleSelection = val
    },
    // 添加处方对话框关闭事件
    addOrderDialogClose() {
      this.submitCareOrder.careOrderItems = []
      this.submitCareOrder.careOrder = {
        coType: '0',
        paatientId: undefined,
        patientName: undefined,
        chId: undefined,
        allAmount: 0

      }
    },
    // 蒙版中添加并关闭
    addOrderAndClose() {
      this.SelectionRow = this.multipleSelection
      this.SelectionRow.map((m) => {
        this.submitCareOrder.careOrderItems.push({
          'itemRefId': m.medicinesId.toString(),
          'itemName': m.medicinesName,
          'price': this.toDecimal(m.prescriptionPrice),
          'num': 1,
          'amount': 0.00,
          'remark': undefined,
          'itemType': '0'
        })
      })
      this.resetmoeny()
      this.orderListdrawer = false
    },
    // 处方数量变化时间
    handleOrderNumChange() {
      this.resetmoeny()
    },
    // 重新计算金额
    resetmoeny() {
      this.submitCareOrder.careOrderItems.map(i => {
        i.amount = this.toDecimal((i.price * i.num))
      })
      this.submitCareOrder.careOrder.allAmount = 0
      this.submitCareOrder.careOrderItems.map(i => {
        this.submitCareOrder.careOrder.allAmount += this.toDecimal(i.amount)
      })
      this.submitCareOrder.careOrder.allAmount = this.toDecimal(this.submitCareOrder.careOrder.allAmount)
    },
    // 保留两位小数
    toDecimal(x) {
      var f = parseFloat(x)
      if (isNaN(f)) {
        return
      }
      f = Math.round(x * 100) / 100
      return f
    },
    // 处方详情单位格式化
    CoItemUnitFormatter(row) {
      return row.itemType === '0' ? '剂' : '次'
    },
    // 处方详情状态格式翻译
    coItemStatusFormatter(row) {
      return this.orderItemStatus.filter(item => {
        if (item.dictValue === row.status) {
          return item.dictLabel
        }
      })[0].dictLabel
    },
    // 左边历史病例点击事件
    handleLeftCareChange(activeLeftCareName) {
      // 让右边的选项卡变成病例选项卡
      this.activeRightName = 't1'
      if (activeLeftCareName === '') {
        // 没选中病例
        this.careHistory.regId = this.nowRegId
        this.careHistory.receiveType = '0'
        this.careHistory.isContagious = '0'
        this.careHistory.caseDate = undefined
        this.careHistory.caseTitle = undefined
        this.careHistory.caseResult = undefined
        this.careHistory.doctorTips = undefined
        this.careHistory.careTime = this.moment(Date.now()).format('YYYY-MM-DD HH:mm:ss')
        this.careHistory.remark = ''
        if (this.careHistory.chId === undefined) {
          this.careHistory.chId = undefined
        }
        // 如果挂号单有病例，就将病例显示出来
        getCareHistoryByRegId(this.careHistory.regId).then(res => {
          if (res.data == null) {
            this.careHistory.userId = undefined
            this.careHistory.userName = undefined
            this.careHistory.patientId = undefined
            this.careHistory.patientName = undefined
            this.careHistory.deptId = undefined
            this.careHistory.deptName = undefined
            this.careHistory.chId = undefined
            this.careHistory.receiveType = '0'
            this.careHistory.isContagious = '0'
            this.careHistory.caseDate = undefined
            this.careHistory.caseTitle = undefined
            this.careHistory.caseResult = undefined
            this.careHistory.doctorTips = undefined
            this.careHistory.careTime = this.moment(Date.now()).format('YYYY-MM-DD HH:mm:ss')
            this.careHistory.remark = ''
          }
        })
        this.getPatient(this.patientAllObj.patientObj.patientId)
      } else {
        // 加载选中的病例信息到右边
        this.getPatient(this.patientAllObj.patientObj.patientId)

        this.careHistory = this.patientAllObj.careHistoryList[activeLeftCareName ]
      }
    }
  }
}
</script>

<style lang="scss"  scoped>
.activeTab{
  background: dimgrey;
}
.toprow{
  padding-top: 0px;
}
.RegTableList_table_expand el-col{
    padding-right: 8px;
}
.patien-File-styl{
  text-align:left;
  margin-left:50px;
  div{
    margin-top: 10px;
    span{
      color: indianred;

    }
  }
}
</style>
