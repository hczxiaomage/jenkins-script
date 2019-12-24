//用来构建 -lite 和 fireball 两个 task 的脚本
node('windows') {
    def utils = load '../jenkins-script/utils/utils.groovy'
    def list = [
            booleanParam(name: 'BUILD_LITE', defaultValue: true, description: '是否构建 cocos2d-x-lite'),
            booleanParam(name: 'BUILD_FIREBALL', defaultValue: true, description: '是否构建 fireball'),
        ]
    stage('update jenkins script') {
        bat ' chcp 65001'
        build job: 'Creator_2D/jenkins-script', parameters: [booleanParam(name: 'IS_WINDOWS', value: true)]
        //load script and init some config
        def fireball = load '../jenkins-script/config/fireball.groovy'
        def lite = load '../jenkins-script/config/cocos2d-x-lite.groovy'
        list.addAll(fireball.getParams())
        list.addAll(lite.getParams())
        echo 'list length' + list.size()
        properties([parameters(list)])
    }

    stage ('build cocos2d-x-lite') {
        if (Boolean.parseBoolean(env.BUILD_LITE)) {
            build job: 'Creator_2D/cocos2d-x-lite/windows/cocos2d-x-lite', parameters:utils.genParams(list)
        } else {
            echo 'skip build cocos2d-x-lite'
        }
    }

    stage ('build fireball') {
        if (Boolean.parseBoolean(env.BUILD_LITE)) {
            build job: 'Creator_2D/fireball/windows/fireball', parameters:utils.genParams(list)
        } else {
            echo 'skip build fireball'
        }
    }
}