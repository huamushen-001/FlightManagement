<template>
  <div class="schedules">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>排班计划管理</span>
          <el-button type="primary" @click="showAddDialog">添加排班</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="航班号">
          <el-input v-model="searchForm.flightNumber" placeholder="请输入航班号" clearable />
        </el-form-item>
        <el-form-item label="机组成员">
          <el-input v-model="searchForm.crewMemberName" placeholder="请输入机组成员姓名" clearable />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="searchForm.startDate"
            type="datetime"
            placeholder="开始时间"
            clearable
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="searchForm.endDate"
            type="datetime"
            placeholder="结束时间"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="flight.flightNumber" label="航班号" width="120" />
        <el-table-column prop="flight.departureAirport" label="出发机场" width="120" />
        <el-table-column prop="flight.arrivalAirport" label="到达机场" width="120" />
        <el-table-column prop="flight.departureTime" label="出发时间" width="150">
          <template #default="scope">
            {{ formatDateTime(scope.row.flight?.departureTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="flight.arrivalTime" label="到达时间" width="150">
          <template #default="scope">
            {{ formatDateTime(scope.row.flight?.arrivalTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="crewMember.name" label="机组成员" width="120" />
        <el-table-column prop="role" label="角色" width="100" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="workHours" label="工作时长" width="100">
          <template #default="scope">
            {{ scope.row.workHours }}小时
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="航班" prop="flightId">
          <el-select 
            v-model="form.flightId" 
            placeholder="请选择航班" 
            style="width: 100%"
            @change="handleFlightChange"
          >
            <el-option
              v-for="flight in flightOptions"
              :key="flight.id"
              :label="`${flight.flightNumber} (${flight.departureAirport}-${flight.arrivalAirport})`"
              :value="flight.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="机组成员" prop="crewMemberId">
          <el-select 
            v-model="form.crewMemberId" 
            placeholder="请选择机组成员" 
            style="width: 100%"
          >
            <el-option
              v-for="member in crewMemberOptions"
              :key="member.id"
              :label="`${member.name} (${member.position})`"
              :value="member.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="机长" value="机长" />
            <el-option label="副驾驶" value="副驾驶" />
            <el-option label="乘务长" value="乘务长" />
            <el-option label="乘务员" value="乘务员" />
            <el-option label="维修工程师" value="维修工程师" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="已分配" value="已分配" />
            <el-option label="已确认" value="已确认" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
        <el-form-item label="工作时长" prop="workHours">
          <el-input-number v-model="form.workHours" :min="0" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { scheduleApi, flightApi, crewMemberApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

export default {
  name: 'Schedules',
  data() {
    return {
      loading: false,
      searchForm: {
        flightNumber: '',
        crewMemberName: '',
        startDate: '',
        endDate: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '添加排班',
      form: {
        flightId: '',
        crewMemberId: '',
        role: '',
        status: '已分配',
        workHours: 0
      },
      rules: {
        flightId: [{ required: true, message: '请选择航班', trigger: 'change' }],
        crewMemberId: [{ required: true, message: '请选择机组成员', trigger: 'change' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      },
      isEdit: false,
      editId: null,
      flightOptions: [],
      crewMemberOptions: []
    }
  },
  mounted() {
    this.loadData()
    this.loadFlightOptions()
    this.loadCrewMemberOptions()
  },
  methods: {
    formatDateTime(dateTime) {
      return dayjs(dateTime).format('YYYY-MM-DD HH:mm')
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        console.log('发送请求参数:', params)
        const response = await scheduleApi.getPage(params)
        console.log('API响应:', response)
        console.log('响应数据结构:', response.data)
        
        // 检查响应数据结构
        if (response.data && response.data.code === 200) {
          if (response.data.data && response.data.data.records) {
            this.tableData = response.data.data.records
            this.pagination.total = response.data.data.total
            console.log('成功加载数据:', this.tableData.length, '条记录')
          } else {
            console.error('响应数据格式不正确:', response.data)
            ElMessage.error('数据格式错误')
          }
        } else {
          console.error('API返回错误:', response.data)
          ElMessage.error(response.data?.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        console.error('错误详情:', error.response?.data)
        ElMessage.error('加载数据失败: ' + (error.response?.data?.message || error.message))
      } finally {
        this.loading = false
      }
    },
    async loadFlightOptions() {
      try {
        console.log('开始加载航班选项...')
        const response = await flightApi.getPage({ current: 1, size: 1000 })
        console.log('航班API响应:', response)
        if (response.data && response.data.code === 200 && response.data.data && response.data.data.records) {
          this.flightOptions = response.data.data.records
          console.log('成功加载航班选项:', this.flightOptions.length, '条记录')
        } else {
          console.error('航班数据格式错误:', response.data)
        }
      } catch (error) {
        console.error('加载航班选项失败:', error)
      }
    },
    async loadCrewMemberOptions() {
      try {
        console.log('开始加载机组成员选项...')
        const response = await crewMemberApi.getPage({ current: 1, size: 1000 })
        console.log('机组成员API响应:', response)
        if (response.data && response.data.code === 200 && response.data.data && response.data.data.records) {
          this.crewMemberOptions = response.data.data.records
          console.log('成功加载机组成员选项:', this.crewMemberOptions.length, '条记录')
        } else {
          console.error('机组成员数据格式错误:', response.data)
        }
      } catch (error) {
        console.error('加载机组成员选项失败:', error)
      }
    },
    handleFlightChange(flightId) {
      const flight = this.flightOptions.find(f => f.id === flightId)
      if (flight) {
        // 自动计算工作时长（小时）
        const departureTime = dayjs(flight.departureTime)
        const arrivalTime = dayjs(flight.arrivalTime)
        const duration = arrivalTime.diff(departureTime, 'hour', true)
        this.form.workHours = Math.round(duration * 10) / 10
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        flightNumber: '',
        crewMemberName: '',
        startDate: '',
        endDate: ''
      }
      this.handleSearch()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },
    showAddDialog() {
      this.isEdit = false
      this.dialogTitle = '添加排班'
      this.form = {
        flightId: '',
        crewMemberId: '',
        role: '',
        status: '已分配',
        workHours: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.editId = row.id
      this.dialogTitle = '编辑排班'
      this.form = { 
        flightId: row.flightId,
        crewMemberId: row.crewMemberId,
        role: row.role,
        status: row.status,
        workHours: row.workHours
      }
      this.dialogVisible = true
    },
    async handleDelete(row) {
      try {
        await ElMessageBox.confirm('确定要删除该排班计划吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await scheduleApi.delete(row.id)
        ElMessage.success('删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.formRef.validate()
        
        console.log('提交的表单数据:', this.form)
        
        if (this.isEdit) {
          console.log('更新排班数据:', { ...this.form, id: this.editId })
          await scheduleApi.update({ ...this.form, id: this.editId })
          ElMessage.success('更新成功')
        } else {
          console.log('添加排班数据:', this.form)
          await scheduleApi.add(this.form)
          ElMessage.success('添加成功')
        }
        
        this.dialogVisible = false
        this.loadData()
      } catch (error) {
        console.error('提交失败:', error)
        console.error('错误详情:', error.response?.data)
        // 优先展示后端返回的 message
        ElMessage.error(error.response?.data?.message || (this.isEdit ? '更新失败' : '添加失败'))
      }
    },
    handleDialogClose() {
      this.$refs.formRef?.resetFields()
    }
  }
}
</script>

<style scoped>
.schedules {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style> 