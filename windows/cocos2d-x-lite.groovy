properties([parameters([
  string(name: 'FIREBALL_BUILD_BRANCH', defaultValue: 'v2.0-release', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'FIREBALL_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
  booleanParam(name: 'FIREBALL_LITE_PUBLISH_WINDOWS', defaultValue: true, description:'是否重新构建上传-lite仓库模拟器'),
])])

node('windows') {
    stage ('checkout code'){
        git branch: "${FIREBALL_BUILD_BRANCH}", url: 'git@github.com:cocos-creator/cocos2d-x-lite.git'
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

    stage ('cocos2d-x-lite publish') {
            if (Boolean.parseBoolean(env.FIREBALL_LITE_PUBLISH_WINDOWS)) {
                bat 'gulp publish -b ${FIREBALL_BUILD_BRANCH}'
            } else {
                echo 'skip cocos2d-x-lite publish stage'
            }
        }
}