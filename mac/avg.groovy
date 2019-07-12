properties([parameters([
  string(name: 'EDITOR_BUILD_BRANCH', defaultValue: 'dev', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'EDITOR_SETUP_ENV', defaultValue: false, description: '是否初始化环境')
])])

node('mac') {
    stage ('checkout code'){
        git branch: "${EDITOR_BUILD_BRANCH}", url: 'git@github.com:cocos-creator/avg.git'
    }

    stage ('setup environment') {
        if (Boolean.parseBoolean(env.EDITOR_SETUP_ENV)) {
            sh 'npm install'
            dir('avg-electron') {
                sh 'npm install'
                sh 'npm install cocos-creator/creator-asar'
            }
        } else {
            echo 'skip setup-environment stage'
        }
    }

    stage ('publish editor') {
        dir('avg-electron') {
            sh 'gulp publish'
        }
    }
}