node('mac') {
    stage('update jenkins script') {
        git branch: 'creator', url: 'git@github.com:wuzhiming/jenkins-script.git'
        //load script and init some config
        def fireball = load '../jenkins-script/config/fireball.groovy'
        def lite = load '../jenkins-script/config/cocos2d-x-lite.groovy'
        def list = new ArrayList()
        list.addAll(fireball.getParams())
        list.addAll(lite.getParams())

        properties([parameters(list)])
    }

    stage ('build cocos2d-x-lite') {
        build job: 'Creator_2D/cocos2d-x-lite/mac/cocos2d-x-lite'
    }

    stage ('build fireball') {
        build job: 'Creator_2D/fireball/mac/fireball'
    }
}