node('windows') {
    stage('update jenkins script') {
        //load script and init some config
        def conf = load '../../../jenkins-script/config/cocos2d-x-lite.groovy'
        properties([parameters(conf.getParams())])
    }
    stage ('checkout code'){
        echo 'checkout branch111 ---' + params.get('COCOS2DX_BUILD_BRANCH')
        echo 'checkout branch ---' + env.COCOS2DX_BUILD_BRANCH
        git branch: "${COCOS2DX_BUILD_BRANCH}", url: 'git@github.com:cocos-creator/cocos2d-x-lite.git'
    }

    stage ('setup environment') {
        if (Boolean.parseBoolean(env.FIREBALL_SETUP_ENV)) {
            bat 'python download-deps.py -r no'
            bat 'git submodule update --init'
            bat 'npm install'
        } else {
            echo 'skip setup-environment stage'
        }
    }

    stage ('make cocos2d-x') {
        if (Boolean.parseBoolean(env.FIREBALL_LITE_PUBLISH)) {
            bat 'gulp make-cocos2d-x -b ${version}'
        } else {
            echo 'skip make cocos2d-x stage'
        }
    }

    stage ('cocos2d-x-lite publish') {
        if (Boolean.parseBoolean(env.FIREBALL_LITE_PUBLISH_MAC)) {
            String version = env.COCOS2DX_BUILD_BRANCH.substring(1, COCOS2DX_BUILD_BRANCH.length());
            if (version.indexOf('-') != -1) {
                version = version.substring(0, version.indexOf('-'));
            }
            bat 'gulp publish -b ' + version
        } else {
            echo 'skip cocos2d-x-lite publish stage'
        }
    }
}