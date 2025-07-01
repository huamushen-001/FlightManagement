<template>
  <div class="crew-members">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>机组人员管理</span>
          <el-button type="primary" @click="showAddDialog">添加机组成员</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="岗位">
          <el-select v-model="searchForm.position" placeholder="请选择岗位" clearable>
            <el-option label="飞行员" value="飞行员" />
            <el-option label="空乘" value="空乘" />
            <el-option label="维修工程师" value="维修工程师" />
          </el-select>
        </el-form-item>
        <el-form-item label="资质等级">
          <el-select v-model="searchForm.qualificationLevel" placeholder="请选择资质等级" clearable>
            <el-option label="高级" value="高级" />
            <el-option label="中级" value="中级" />
            <el-option label="初级" value="初级" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="employeeId" label="工号" width="120" />
        <el-table-column prop="position" label="岗位" width="120" />
        <el-table-column prop="qualificationLevel" label="资质等级" width="120" />
        <el-table-column prop="contact" label="联系方式" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="currentFlightHours" label="当前飞行小时" width="120" />
        <el-table-column prop="maxFlightHours" label="最大飞行小时" width="120" />
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
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="工号" prop="employeeId">
          <el-input v-model="form.employeeId" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="岗位" prop="position">
          <el-select v-model="form.position" placeholder="请选择岗位" style="width: 100%">
            <el-option label="飞行员" value="飞行员" />
            <el-option label="空乘" value="空乘" />
            <el-option label="维修工程师" value="维修工程师" />
          </el-select>
        </el-form-item>
        <el-form-item label="资质等级" prop="qualificationLevel">
          <el-select v-model="form.qualificationLevel" placeholder="请选择资质等级" style="width: 100%">
            <el-option label="高级" value="高级" />
            <el-option label="中级" value="中级" />
            <el-option label="初级" value="初级" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="入职日期" prop="hireDate">
          <el-date-picker
            v-model="form.hireDate"
            type="datetime"
            placeholder="请选择入职日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="最大飞行小时" prop="maxFlightHours">
          <el-input-number v-model="form.maxFlightHours" :min="0" style="width: 100%" />
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
import { crewMemberApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'CrewMembers',
  data() {
    return {
      loading: false,
      searchForm: {
        name: '',
        position: '',
        qualificationLevel: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '添加机组成员',
      form: {
        name: '',
        employeeId: '',
        position: '',
        qualificationLevel: '',
        contact: '',
        email: '',
        hireDate: '',
        maxFlightHours: 0
      },
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        employeeId: [{ required: true, message: '请输入工号', trigger: 'blur' }],
        position: [{ required: true, message: '请选择岗位', trigger: 'change' }],
        qualificationLevel: [{ required: true, message: '请选择资质等级', trigger: 'change' }],
        contact: [{ required: true, message: '请输入联系方式', trigger: 'blur' }]
      },
      isEdit: false,
      editId: null
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        const response = await crewMemberApi.getPage(params)
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
        name: '',
        position: '',
        qualificationLevel: ''
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
      this.dialogTitle = '添加机组成员'
      this.form = {
        name: '',
        employeeId: '',
        position: '',
        qualificationLevel: '',
        contact: '',
        email: '',
        hireDate: '',
        maxFlightHours: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.editId = row.id
      this.dialogTitle = '编辑机组成员'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleDelete(row) {
      try {
        await ElMessageBox.confirm('确定要删除该机组成员吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await crewMemberApi.delete(row.id)
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
          await crewMemberApi.update({ ...this.form, id: this.editId })
          ElMessage.success('更新成功')
        } else {
          await crewMemberApi.add(this.form)
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
.crew-members {
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