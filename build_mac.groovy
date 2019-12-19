//用来构建 -lite 和 fireball 两个 task 的脚本
node('mac') {
    stage('update jenkins script') {
        build job: 'Creator_2D/jenkins-script'
        def step = [
            booleanParam(name: 'BUILD_LITE', defaultValue: true, description: '是否构建 cocos2d-x-lite'),
            booleanParam(name: 'BUILD_FIREBALL', defaultValue: true, description: '是否构建 fireball'),
        ]
        //load script and init some config
        def fireball = load '../jenkins-script/mac/config/fireball.groovy'
        def lite = load '../jenkins-script/mac/config/cocos2d-x-lite.groovy'
        def list = new ArrayList()
        list.addAll(setp)
        list.addAll(fireball.getParams())
        list.addAll(lite.getParams())
        echo 'list length'+list.size
        properties([parameters(list)])
    }

    stage ('build cocos2d-x-lite') {
        build job: 'Creator_2D/cocos2d-x-lite/mac/cocos2d-x-lite'
    }

    stage ('build fireball') {
        build job: 'Creator_2D/fireball/mac/fireball'
    }
}