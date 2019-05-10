node {
	try{
		stage('检出代码'){//从git仓库中检出代码
	    	git branch: "${FIREBALL_BUILD_BRANCH}", url: 'git@github.com:wuzhiming/jenkins.git'
	       	loadProjectConfig();
	  	}
	   	stage('编译'){
	   		//这里是构建，你可以调用job入参或者项目配置的参数，比如：
	   		echo "项目名字 ${APP_CHINESE_NAME}"
	   		//可以判断
	   		if (Boolean.valueOf("${IS_USE_CODE_CHECK}")) {
	   			echo "需要静态代码检查"
	   		} else {
	   			echo "不需要静态代码检查"
	   		}

	   	}
	   	stage('存档'){//这个演示的Android的项目，实际使用中，请根据自己的产物确定
	       	def apk = getShEchoResult ("find ./lineup/build/outputs/apk -name '*.apk'")
	       	def artifactsDir="artifacts"//存放产物的文件夹
	        sh "mkdir ${artifactsDir}"
	       	sh "mv ${apk} ${artifactsDir}"
	       	archiveArtifacts "${artifactsDir}/*"
	   	}
	   	stage('通知负责人'){
	   		emailext body: "构建项目:${BUILD_URL}\r\n构建完成", subject: '构建结果通知【成功】', to: "${EMAIL}"
	   	}
	} catch (e) {
		emailext body: "构建项目:${BUILD_URL}\r\n构建失败，\r\n错误消息：${e.toString()}", subject: '构建结果通知【失败】', to: "${EMAIL}"
	} finally{
		// 清空工作空间
        cleanWs notFailBuild: true
	}


}

//加载项目里面的配置文件
def loadProjectConfig(){
    def jenkinsConfigFile="./jenkins.groovy"
    if (fileExists("${jenkinsConfigFile}")) {
        load "${jenkinsConfigFile}"
        echo "找到打包参数文件${jenkinsConfigFile}，加载成功"
    } else {
        echo "${jenkinsConfigFile}不存在,请在项目${jenkinsConfigFile}里面配置打包参数"
        sh "exit 1"
    }
}