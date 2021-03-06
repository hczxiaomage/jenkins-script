properties([parameters([
  string(name: 'FIREBALL_BUILD_BRANCH', defaultValue: 'v2.0-release', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'FIREBALL_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
  booleanParam(name: 'FIREBALL_LITE_PUBLISH', defaultValue: true, description:'是否重新构建上传-lite仓库模拟器'),
])])

node('mac') {
    stage ('checkout code'){
        git branch: "${FIREBALL_BUILD_BRANCH}", url: 'git@github.com:wuzhiming/cocos2d-x-lite.git'
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

    stage ('cocos2d-x-lite publish') {
            if (Boolean.parseBoolean(env.FIREBALL_LITE_PUBLISH)) {
                sh 'gulp publish'
            } else {
                echo 'skip cocos2d-x-lite publish stage'
            }
        }
}