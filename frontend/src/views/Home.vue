<template>
   <div class="home">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>机组成员总数</span>
            </div>
          </template>
          <div class="card-content">
            <h2>{{ crewMemberCount }}</h2>
            <p>在职机组成员</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>航班总数</span>
            </div>
          </template>
          <div class="card-content">
            <h2>{{ flightCount }}</h2>
            <p>计划航班</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>排班计划</span>
            </div>
          </template>
          <div class="card-content">
            <h2>{{ scheduleCount }}</h2>
            <p>已分配排班</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>系统状态</span>
            </div>
          </template>
          <div class="card-content">
            <h2 style="color: #67C23A;">正常</h2>
            <p>系统运行正常</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近排班</span>
            </div>
          </template>
          <el-table :data="recentSchedules" style="width: 100%">
            <el-table-column prop="flight.flightNumber" label="航班号" width="120" />
            <el-table-column prop="crewMember.name" label="机组成员" width="120" />
            <el-table-column prop="role" label="角色" width="100" />
            <el-table-column prop="status" label="状态" width="100" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>系统功能</span>
            </div>
          </template>
          <div class="feature-list">
            <el-button type="primary" @click="$router.push('/crew-members')" style="margin: 5px;">
              机组人员管理
            </el-button>
            <el-button type="success" @click="$router.push('/flights')" style="margin: 5px;">
              航班信息管理
            </el-button>
            <el-button type="warning" @click="$router.push('/schedules')" style="margin: 5px;">
              排班计划管理
            </el-button>
            <el-button type="info" @click="$router.push('/statistics')" style="margin: 5px;">
              统计与报表
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Home',
  computed: {
    ...mapGetters(['crewMembers', 'flights', 'schedules']),
    crewMemberCount() {
      return this.crewMembers.length
    },
    flightCount() {
      return this.flights.length
    },
    scheduleCount() {
      return this.schedules.length
    },
    recentSchedules() {
      return this.schedules.slice(0, 5)
    }
  },
  mounted() {
    this.$store.dispatch('fetchCrewMembers')
    this.$store.dispatch('fetchFlights')
    this.$store.dispatch('fetchSchedules')
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  text-align: center;
}

.card-content h2 {
  font-size: 2em;
  margin: 10px 0;
  color: #409EFF;
}

.feature-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style> 