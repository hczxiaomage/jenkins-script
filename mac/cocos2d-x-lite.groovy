properties([parameters([
  string(name: 'COCOS2DX_BUILD_BRANCH', defaultValue: 'v2.0-release', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'FIREBALL_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
  booleanParam(name: 'FIREBALL_MAKE_COCOS2DX', defaultValue: false, description: '是否打包上传 cocos2dx 代码'),
  booleanParam(name: 'FIREBALL_LITE_PUBLISH_MAC', defaultValue: true, description:'是否重新构建上传-lite仓库模拟器'),
])])

node('mac') {
    stage ('checkout code'){
        git branch: "${COCOS2DX_BUILD_BRANCH}", url: 'git@github.com:cocos-creator/cocos2d-x-lite.git'
    }

    stage ('setup environment') {
        if (Boolean.parseBoolean(env.FIREBALL_SETUP_ENV)) {
            sh 'python download-deps.py -r no'
            sh 'git submodule update --init'
            sh 'npm install'
        } else {
            echo 'skip setup-environment stage'
        }
    }

    stage ('make cocos2d-x') {
        if (Boolean.parseBoolean(env.FIREBALL_LITE_PUBLISH)) {
            sh 'gulp make-cocos2d-x -b ${version}'
        } else {
            echo 'skip make cocos2d-x stage'
        }
    }

    stage ('cocos2d-x-lite publish') {
        String version = env.COCOS2DX_BUILD_BRANCH.substring(1,COCOS2DX_BUILD_BRANCH.length());
        if (Boolean.parseBoolean(env.FIREBALL_LITE_PUBLISH_MAC)) {
            sh 'gulp publish -b ' + version
        } else {
            echo 'skip cocos2d-x-lite publish stage'
        }
    }
}