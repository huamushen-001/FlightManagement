<template>
  <div id="app">
    <el-container>
      <el-aside width="200px">
        <el-menu
          :default-active="$route.path"
          router
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b">
          <el-menu-item index="/">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/crew-members">
            <!-- 路由器路径负责跳转-->
            <el-icon><User /></el-icon>
            <span>机组人员管理</span>
          </el-menu-item>
          <el-menu-item index="/flights">
            <el-icon><Switch /></el-icon>
            <span>航班信息管理</span>
          </el-menu-item>
          <el-menu-item index="/schedules">
            <el-icon><Calendar /></el-icon>
            <span>排班计划管理</span>
          </el-menu-item>
          <el-menu-item index="/statistics">
            <el-icon><DataAnalysis /></el-icon>
            <span>统计与报表</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <h2>航空机组排班系统</h2>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
}

.el-container {
  height: 100%;
}

.el-aside {
  background-color: #545c64;
}

.el-header {
  background-color: #409EFF;
  color: white;
  line-height: 60px;
  text-align: center;
}

.el-main {
  background-color: #f5f5f5;
  padding: 20px;
}
</style> 