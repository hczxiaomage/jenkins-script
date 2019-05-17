properties([parameters([
  string(name: 'EDITOR_BUILD_BRANCH', defaultValue: 'master', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'EDITOR_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
  booleanParam(name: 'EDITOR_CLEAR', defaultValue: true, description: '是否执行clear'),
  booleanParam(name: 'EDITOR_REBUILD', defaultValue: true, description: '是否重新构建所有task'),
])])

node('mac') {
    stage ('checkout code'){
        git branch: "${EDITOR_BUILD_BRANCH}", url: 'git@github.com:cocos-creator/editor-3d.git'
    }

    stage ('setup environment') {
        if (Boolean.parseBoolean(env.EDITOR_SETUP_ENV)) {
            bat 'npm install'
            bat 'npm install cocos-creator/creator-asar'
        } else {
            echo 'skip setup-environment stage'
        }
    }

    stage ('num run clear') {
        if (Boolean.parseBoolean(env.EDITOR_CLEAR)) {
            bat 'npm run clear'
        } else {
            echo 'skip num run clear stage'
        }
    }

    stage ('rebuild task') {
        if (Boolean.parseBoolean(env.EDITOR_REBUILD)) {
            bat 'npm install'
        } else {
            echo 'skip update-builtin stage'
        }
    }

    stage ('update templates') {
        bat 'npm run publish'
    }
}