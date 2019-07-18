properties([parameters([
  string(name: 'EDITOR_BUILD_BRANCH', defaultValue: 'dev', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'EDITOR_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
  booleanParam(name: 'EDITOR_GREEN', defaultValue: true, description: '是否是构建绿色版本'),
  booleanParam(name: 'EDITOR_INSTALLER', defaultValue: false, description: '是否是构建安装版本版本'),
  booleanParam(name: 'EDITOR_UPLOAD_LAN', defaultValue: true, description: '是否上传到ftp'),
])])

node('windows') {
    stage ('checkout code'){
        git branch: "${EDITOR_BUILD_BRANCH}", url: 'git@github.com:wuzhiming/avg.git'
    }

    stage ('setup environment') {
        if (Boolean.parseBoolean(env.EDITOR_SETUP_ENV)) {
            bat 'npm install'
            bat 'npm install cocos-creator/creator-asar'
            dir('avg-electron') {
                bat 'npm install'
            }
        } else {
            echo 'skip setup-environment stage'
        }
    }

    stage ('publish editor') {
       bat 'gulp publish'
    }
}