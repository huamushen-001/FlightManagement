<template>
  <div class="statistics">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>工作时长统计</span>
              <el-button type="primary" @click="exportWorkHours">导出报表</el-button>
            </div>
          </template>
          <el-table :data="workHoursData" style="width: 100%">
            <el-table-column prop="name" label="机组成员" width="120" />
            <el-table-column prop="position" label="岗位" width="100" />
            <el-table-column prop="totalHours" label="总工作时长" width="120">
              <template #default="scope">
                {{ scope.row.totalHours }}小时
              </template>
            </el-table-column>
            <el-table-column prop="flightCount" label="航班次数" width="100" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>航班统计</span>
              <el-button type="primary" @click="exportFlightStats">导出报表</el-button>
            </div>
          </template>
          <el-table :data="flightStatsData" style="width: 100%">
            <el-table-column prop="route" label="航线" width="150" />
            <el-table-column prop="flightCount" label="航班次数" width="100" />
            <el-table-column prop="totalDuration" label="总飞行时长" width="120">
              <template #default="scope">
                {{ scope.row.totalDuration }}分钟
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>排班统计</span>
              <div>
                <el-date-picker
                  v-model="dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  @change="handleDateChange"
                  style="margin-right: 10px;"
                />
                <el-button type="primary" @click="exportSchedule">导出排班表</el-button>
              </div>
            </div>
          </template>
          <el-table :data="scheduleStatsData" style="width: 100%">
            <el-table-column prop="date" label="日期" width="120" />
            <el-table-column prop="flightCount" label="航班数" width="100" />
            <el-table-column prop="crewCount" label="参与机组数" width="120" />
            <el-table-column prop="totalWorkHours" label="总工作时长" width="120">
              <template #default="scope">
                {{ scope.row.totalWorkHours }}小时
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>综合统计</span>
              <el-button type="primary" @click="exportAllStats">导出报表</el-button>
            </div>
          </template>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-item">
                <h3>{{ totalCrewMembers }}</h3>
                <p>机组成员总数</p>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <h3>{{ totalFlights }}</h3>
                <p>航班总数</p>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <h3>{{ totalSchedules }}</h3>
                <p>排班计划总数</p>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <h3>{{ totalWorkHours }}</h3>
                <p>总工作时长(小时)</p>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { excelApi } from '../api'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

export default {
  name: 'Statistics',
  data() {
    return {
      dateRange: [],
      workHoursData: [],
      flightStatsData: [],
      scheduleStatsData: []
    }
  },
  computed: {
    ...mapGetters(['crewMembers', 'flights', 'schedules']),
    totalCrewMembers() {
      return this.crewMembers.length
    },
    totalFlights() {
      return this.flights.length
    },
    totalSchedules() {
      return this.schedules.length
    },
    totalWorkHours() {
      return this.schedules.reduce((total, schedule) => total + (schedule.workHours || 0), 0)
    }
  },
  mounted() {
    this.loadStatistics()
  },
  methods: {
    loadStatistics() {
      this.calculateWorkHoursStats()
      this.calculateFlightStats()
      this.calculateScheduleStats()
    },
    calculateWorkHoursStats() {
      const stats = {}
      this.schedules.forEach(schedule => {
        const memberId = schedule.crewMemberId
        const member = this.crewMembers.find(cm => cm.id === memberId)
        if (member) {
          if (!stats[memberId]) {
            stats[memberId] = {
              name: member.name,
              position: member.position,
              totalHours: 0,
              flightCount: 0
            }
          }
          stats[memberId].totalHours += schedule.workHours || 0
          stats[memberId].flightCount += 1
        }
      })
      this.workHoursData = Object.values(stats)
    },
    calculateFlightStats() {
      const stats = {}
      this.flights.forEach(flight => {
        const route = `${flight.departureAirport}-${flight.arrivalAirport}`
        if (!stats[route]) {
          stats[route] = {
            route,
            flightCount: 0,
            totalDuration: 0
          }
        }
        stats[route].flightCount += 1
        stats[route].totalDuration += flight.flightDuration || 0
      })
      this.flightStatsData = Object.values(stats)
    },
    calculateScheduleStats() {
      const stats = {}
      this.schedules.forEach(schedule => {
        if (schedule.flight) {
          const date = dayjs(schedule.flight.departureTime).format('YYYY-MM-DD')
          if (!stats[date]) {
            stats[date] = {
              date,
              flightCount: 0,
              crewCount: 0,
              totalWorkHours: 0,
              crewMembers: new Set()
            }
          }
          stats[date].crewMembers.add(schedule.crewMemberId)
          stats[date].totalWorkHours += schedule.workHours || 0
        }
      })
      
      // 计算每日航班数
      this.flights.forEach(flight => {
        const date = dayjs(flight.departureTime).format('YYYY-MM-DD')
        if (stats[date]) {
          stats[date].flightCount += 1
        }
      })
      
      this.scheduleStatsData = Object.values(stats).map(stat => ({
        ...stat,
        crewCount: stat.crewMembers.size,
        crewMembers: undefined
      }))
    },
    handleDateChange() {
      this.loadStatistics()
    },
    async exportWorkHours() {
      try {
        const response = await excelApi.exportCrewMembers()
        this.downloadFile(response.data, '工作时长统计.xlsx')
        ElMessage.success('导出成功')
      } catch (error) {
        ElMessage.error('导出失败')
      }
    },
    async exportFlightStats() {
      try {
        const response = await excelApi.exportFlights()
        this.downloadFile(response.data, '航班统计.xlsx')
        ElMessage.success('导出成功')
      } catch (error) {
        ElMessage.error('导出失败')
      }
    },
    async exportSchedule() {
      try {
        const startDate = this.dateRange[0] ? dayjs(this.dateRange[0]).format('YYYY-MM-DD HH:mm:ss') : null
        const endDate = this.dateRange[1] ? dayjs(this.dateRange[1]).format('YYYY-MM-DD HH:mm:ss') : null
        const response = await excelApi.exportSchedule(startDate, endDate)
        this.downloadFile(response.data, '排班表.xlsx')
        ElMessage.success('导出成功')
      } catch (error) {
        ElMessage.error('导出失败')
      }
    },
    async exportAllStats() {
      try {
        const startDate = this.dateRange[0] ? dayjs(this.dateRange[0]).format('YYYY-MM-DD HH:mm:ss') : null
        const endDate = this.dateRange[1] ? dayjs(this.dateRange[1]).format('YYYY-MM-DD HH:mm:ss') : null
        const response = await excelApi.exportStatistics(startDate, endDate)
        this.downloadFile(response.data, '综合统计报表.xlsx')
        ElMessage.success('导出成功')
      } catch (error) {
        ElMessage.error('导出失败')
      }
    },
    downloadFile(data, filename) {
      const blob = new Blob([data], { type: 'application/vnd.ms-excel' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = filename
      link.click()
      window.URL.revokeObjectURL(url)
    }
  }
}
</script>

<style scoped>
.statistics {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-item {
  text-align: center;
  padding: 20px;
}

.stat-item h3 {
  font-size: 2em;
  color: #409EFF;
  margin: 0 0 10px 0;
}

.stat-item p {
  margin: 0;
  color: #666;
}
</style> 