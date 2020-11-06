<template>
  <div class="dashboard_editor_container">
    <h1>赣县区人民医院医疗办公系统</h1>
    <img src="@/assets/bgimg/indexbg.jpg" alt="">
  </div>
</template>

<script>
// import GithubCorner from '@/components/GithubCorner'
// import PanelGroup from './components/PanelGroup'
// import LineChart from './components/LineChart'
// import RaddarChart from './components/RaddarChart'
// import PieChart from './components/PieChart'
// import BarChart from './components/BarChart'
// import TransactionTable from './components/TransactionTable'
// import TodoList from './components/TodoList'
// import BoxCard from './components/BoxCard'

// const lineChartData = {
//   newVisitis: {
//     expectedData: [100, 120, 161, 134, 105, 160, 165],
//     actualData: [120, 82, 91, 154, 162, 140, 145]
//   },
//   messages: {
//     expectedData: [200, 192, 120, 144, 160, 130, 140],
//     actualData: [180, 160, 151, 106, 145, 150, 130]
//   },
//   purchases: {
//     expectedData: [80, 100, 121, 104, 105, 90, 100],
//     actualData: [120, 90, 100, 138, 142, 130, 130]
//   },
//   shoppings: {
//     expectedData: [130, 140, 141, 142, 145, 150, 160],
//     actualData: [120, 82, 91, 154, 162, 140, 130]
//   }
// }

export default {
  name: 'DashboardAdmin',
  components: {
    // GithubCorner,
    // PanelGroup,
    // LineChart,
    // RaddarChart,
    // PieChart,
    // BarChart,
    // TransactionTable,
    // TodoList,
    // BoxCard
  },
  data() {
    return {
      // lineChartData: lineChartData.newVisitis
    }
  },
  methods: {
    // handleSetLineChartData(type) {
    //   this.lineChartData = lineChartData[type]
    // }
  }
}
</script>

<style lang="scss" scoped>
.dashboard_editor_container {
  height: 100%;
  width: 100%;
  margin-top: 5px;
  padding: 29%;
  position: relative;
  z-index: 30;
  h1{
    position: absolute;
    font-family:Arial, Helvetica, sans-serif;
    font-size: 50px;
    letter-spacing: 10px;
    top: 30px;
    left: 80px;
    z-index: 20;
  }
  img{
    display: block;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100%;
    height: 100%;
  }

  // .github-corner {
  //   position: absolute;
  //   top: 0px;
  //   border: 0;
  //   right: 0;
  // }

  // .chart-wrapper {
  //   background: #fff;
  //   padding: 16px 16px 0;
  //   margin-bottom: 32px;
  // }
}

@media (max-width:500px) {
  // .chart-wrapper {
  //   padding: 8px;
  // }

  .dashboard_editor_container {
      background:url('../../../assets/bgimg/phoneindex.jpg') 100% 100%  no-repeat;
      background-size: 140%;
      overflow: hidden;
      height: 100%;
      width: 100%;
      padding: 80%;
      h1{
          position: absolute;
          font-family:Arial, Helvetica, sans-serif;
          font-size: 25px;
          color: black;
          letter-spacing: 5px;
          top: 5px;
          left: 10px;
          z-index: 20;
        }
      img{
        display: none;
      }
  }

}
</style>
