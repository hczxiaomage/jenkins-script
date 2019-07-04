properties([parameters([
  string(name: 'EDITOR_BUILD_BRANCH', defaultValue: 'master', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'EDITOR_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
])])

node('windows') {
    stage ('checkout code'){
        git branch: "${EDITOR_BUILD_BRANCH}", url: 'git@github.com:wuzhiming/avg.git'
    }

    stage ('setup environment') {
        if (Boolean.parseBoolean(env.EDITOR_SETUP_ENV)) {
            dir('avg-electron') {
                bat 'npm install'
                bat 'npm install cocos-creator/creator-asar'
            }
        } else {
            echo 'skip setup-environment stage'
        }
    }

    stage ('publish editor') {
        dir('avg-electron') {
           bat 'gulp publish'
        }
    }
}