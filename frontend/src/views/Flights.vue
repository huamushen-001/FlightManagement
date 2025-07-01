<template>
  <div class="flights">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>航班信息管理</span>
          <el-button type="primary" @click="showAddDialog">添加航班</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="航班号">
          <el-input v-model="searchForm.flightNumber" placeholder="请输入航班号" clearable />
        </el-form-item>
        <el-form-item label="出发机场">
          <el-input v-model="searchForm.departureAirport" placeholder="请输入出发机场" clearable />
        </el-form-item>
        <el-form-item label="到达机场">
          <el-input v-model="searchForm.arrivalAirport" placeholder="请输入到达机场" clearable />
        </el-form-item>
        <el-form-item label="出发时间">
          <el-date-picker
            v-model="searchForm.startDate"
            type="datetime"
            placeholder="开始时间"
            clearable
          />
        </el-form-item>
        <el-form-item label="到达时间">
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
        <el-table-column prop="flightNumber" label="航班号" width="120" />
        <el-table-column prop="departureAirport" label="出发机场" width="150" />
        <el-table-column prop="arrivalAirport" label="到达机场" width="150" />
        <el-table-column prop="departureTime" label="出发时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.departureTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="arrivalTime" label="到达时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.arrivalTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="flightDuration" label="飞行时长" width="120">
          <template #default="scope">
            {{ scope.row.flightDuration }}分钟
          </template>
        </el-table-column>
        <el-table-column prop="aircraftType" label="机型" width="120" />
        <el-table-column prop="status" label="状态" width="100" />
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
        <el-form-item label="航班号" prop="flightNumber">
          <el-input v-model="form.flightNumber" placeholder="请输入航班号" />
        </el-form-item>
        <el-form-item label="出发机场" prop="departureAirport">
          <el-input v-model="form.departureAirport" placeholder="请输入出发机场" />
        </el-form-item>
        <el-form-item label="到达机场" prop="arrivalAirport">
          <el-input v-model="form.arrivalAirport" placeholder="请输入到达机场" />
        </el-form-item>
        <el-form-item label="出发时间" prop="departureTime">
          <el-date-picker
            v-model="form.departureTime"
            type="datetime"
            placeholder="请选择出发时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="到达时间" prop="arrivalTime">
          <el-date-picker
            v-model="form.arrivalTime"
            type="datetime"
            placeholder="请选择到达时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="机型" prop="aircraftType">
          <el-input v-model="form.aircraftType" placeholder="请输入机型" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="计划" value="计划" />
            <el-option label="执行中" value="执行中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="取消" value="取消" />
          </el-select>
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
import { flightApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

export default {
  name: 'Flights',
  data() {
    return {
      loading: false,
      searchForm: {
        flightNumber: '',
        departureAirport: '',
        arrivalAirport: '',
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
      dialogTitle: '添加航班',
      form: {
        flightNumber: '',
        departureAirport: '',
        arrivalAirport: '',
        departureTime: '',
        arrivalTime: '',
        aircraftType: '',
        status: '计划'
      },
      rules: {
        flightNumber: [{ required: true, message: '请输入航班号', trigger: 'blur' }],
        departureAirport: [{ required: true, message: '请输入出发机场', trigger: 'blur' }],
        arrivalAirport: [{ required: true, message: '请输入到达机场', trigger: 'blur' }],
        departureTime: [{ required: true, message: '请选择出发时间', trigger: 'change' }],
        arrivalTime: [{ required: true, message: '请选择到达时间', trigger: 'change' }]
      },
      isEdit: false,
      editId: null
    }
  },
  mounted() {
    this.loadData()
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
        const response = await flightApi.getPage(params)
        this.tableData = response.data.data.records
        this.pagination.total = response.data.data.total
      } catch (error) {
        ElMessage.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        flightNumber: '',
        departureAirport: '',
        arrivalAirport: '',
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
      this.dialogTitle = '添加航班'
      this.form = {
        flightNumber: '',
        departureAirport: '',
        arrivalAirport: '',
        departureTime: '',
        arrivalTime: '',
        aircraftType: '',
        status: '计划'
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.editId = row.id
      this.dialogTitle = '编辑航班'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleDelete(row) {
      try {
        await ElMessageBox.confirm('确定要删除该航班吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await flightApi.delete(row.id)
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
        
        if (this.isEdit) {
          await flightApi.update({ ...this.form, id: this.editId })
          ElMessage.success('更新成功')
        } else {
          await flightApi.add(this.form)
          ElMessage.success('添加成功')
        }
        
        this.dialogVisible = false
        this.loadData()
      } catch (error) {
        ElMessage.error(this.isEdit ? '更新失败' : '添加失败')
      }
    },
    handleDialogClose() {
      this.$refs.formRef?.resetFields()
    }
  }
}
</script>

<style scoped>
.flights {
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