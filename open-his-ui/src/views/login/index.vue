<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">赣县人民医院医疗系统</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="手机号"
          name="username"
          type="text"
          tabindex="1"
          autocomplete="on"
        />
      </el-form-item>

      <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            autocomplete="on"
            @keyup.native="checkCapslock"
            @blur="capsTooltip = false"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
      </el-tooltip>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>

      <div style="position:relative">
        <div class="tips">
          <span />
          <span />
        </div>
        <div class="tips">
          <span style="margin-right:18px;" />
          <span />
        </div>

        <el-button class="thirdparty-button" type="primary" @click="thirdLogin">
          第三方登录
        </el-button>
      </div>
    </el-form>

    <!--  第三方登录弹出层 -->
    <el-dialog class="thirdLogin" title="人脸识别登录" :visible.sync="showDialog">
      <div class="box">
        <div class="right">
          <div class="video">
            <video id="video" width="300px" height="300px" autoplay="autoplay" />
            <el-button ref="mybutton" style="margin-left:100px;margin-top:20px;" type="success" @click="openMedia">开始识别</el-button>
            <canvas id="canvas" hidden width="250px" height="250px" />
          </div>
        </div>
      </div>
      <!-- QQ微信登录 -->
      <social-sign class="socialLogin" />
    </el-dialog>
  </div>
</template>

<script>

</script>
<script src="http://www.jq22.com/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript" color="120,148,255" opacity='0.8' zIndex="1" count="100" src="https://files.cnblogs.com/files/lfri/canvas-nest.js"></script>
<script>
var mediaStreamTrack;
import SocialSign from './components/SocialSignin'
import { dictCacheAsync } from '@/api/system/dict/type'

export default {

  name: 'Login',
  components: { SocialSign },
  data() {
    // 验证密码规则
    const validatePassword = (rule, value, callback) => {
      if (value.length < 4) {
        callback(new Error('密码不能小于4位'))
      } else {
        callback()
      }
    }
    return {
      //第三方登录弹出层
      showDialog:false,
      loginForm: {
        username: '13888001001',
        password: '001001'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '手机号不能为空' }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {}
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {

  },
  mounted() {
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }

  },
  destroyed() {

  },
  //关闭摄像头
  beforeRouteLeave (to, from, next) {
      this.$router.go(0);  //刷新路由
  },
  methods: {
    //第三方登录
    thirdLogin(){
      this.showDialog=true
    },
    // 摄像头开启
    openMedia(){
      let constraints = {
              video: {width: 500, height: 500},
              audio: true
          };
          //获得video摄像头区域
          let video = document.getElementById("video");
          let promise = navigator.mediaDevices.getUserMedia(constraints);
          promise.then(function (MediaStream) {
              video.srcObject = MediaStream;
              video.play();
          });
        this.chatTimer = setInterval(() => {
           this.jiance();
        }, 1000);
    },
    //摄像头截图
    jiance() {
      //获得Canvas对象
      let video = document.getElementById("video");
      let canvas = document.getElementById("canvas");
      let ctx = canvas.getContext('2d');
      ctx.drawImage(video, 0, 0, 250, 250);
      //截取图片的base64编码
      const imgSrc =encodeURIComponent( document.getElementById("canvas").toDataURL("image/png").split("base64,")[1]);

        this.$store.dispatch('user/search',imgSrc)
        .then((user_id)=>{
           clearInterval(this.chatTimer);
          var userId=user_id
          //识别成功
          this.msgSuccess('识别成功')
          this.$store.dispatch('user/faceLogin',userId)
          .then(()=>{
             this.msgSuccess('登录成功,页面跳转中...')
              // 字典缓存
            dictCacheAsync()
            //  this.$router.push('/')
            this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
          })

        }).catch(()=>{
          console.log('识别失败');
        })
       //返回结果中有一个face_token要存浏览器中，以后每次发请求携带face_token

      //关闭对话框，不截图
      if(this.showDialog===false){
          clearInterval(this.chatTimer);
      }
    },
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    // 按钮登录操作
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        // 验证表单
        if (valid) {
          this.loading = true
          // 调用store中的user/login的登录方法 设置token
          this.$store.dispatch('user/login', this.loginForm)
            .then(() => {
              this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
              // 字典缓存
              dictCacheAsync()
              this.loading = false
            })
            .catch(() => {
              this.loading = false
              this.$message.error('用户名或密码错误')
            })
        } else {
          return false
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
    // afterQRScan() {
    //   if (e.key === 'x-admin-oauth-code') {
    //     const code = getQueryObject(e.newValue)
    //     const codeMap = {
    //       wechat: 'code',
    //       tencent: 'code'
    //     }
    //     const type = codeMap[this.auth_type]
    //     const codeName = code[type]
    //     if (codeName) {
    //       this.$store.dispatch('LoginByThirdparty', codeName).then(() => {
    //         this.$router.push({ path: this.redirect || '/' })
    //       })
    //     } else {
    //       alert('第三方登录失败')
    //     }
    //   }
    // }
  }
}
</script>

<style lang="scss">

/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }
      .thirdLogin{
      position: absolute;
      top: 20px;

    }
    .video{
      width:300px;
      height:300px;
      margin:auto;
      background:no-repeat url('../../assets/bgimg/rlsb.jpg');
      background-size: 100% 100%;
    }
    .box{

      width: 100%;
      height: 500px;
      background-size:100%, 100%;
    }
    .socialLogin{
        position: absolute;
        bottom: 5px;
        right: 20px;
    }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}
</style>
