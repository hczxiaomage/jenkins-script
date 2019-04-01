properties([parameters([
  string(name: 'FIREBALL_BUILD_BRANCH', defaultValue: 'v2.0-release', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'FIREBALL_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
  booleanParam(name: 'FIREBALL_UPDATE_FIREBALL', defaultValue: true, description: 'update fireball'),
  booleanParam(name: 'FIREBALL_UPDATE_BUILTIN', defaultValue: true, description: '是否更新built-in'),
  booleanParam(name: 'FIREBALL_CHECKOUT_SETTING_BRANCH', defaultValue: true, description: '是否迁出对应版本的分支'),
  booleanParam(name: 'FIREBALL_UPDATE', defaultValue: true, description: '是否update'),
  booleanParam(name: 'FIREBALL_CLEAN_CACHE', defaultValue: true, description: '是否清除缓存'),
  booleanParam(name: 'FIREBALL_SYNC_ENGINE_VERSION', defaultValue: true, description: '是否同步引擎版本'),
  booleanParam(name: 'FIREBALL_UPDATE_EXTERNS', defaultValue: true, description: '是否更新externs'),
  booleanParam(name: 'FIREBALL_UPDATE_TEMPLATES', defaultValue: true, description: '是否更新新建工程的模板'),
  booleanParam(name: 'FIREBALL_PUSH_TAG', defaultValue: true, description: '是否添加tag'),
])])

node('windows') {
    stage ('checkout code'){
            git branch: "${FIREBALL_BUILD_BRANCH}", url: 'git@github.com:wuzhiming/fireball.git'
    }

    stage ('setup environment') {
        if (Boolean.parseBoolean(env.FIREBALL_SETUP_ENV)) {
            bat 'npm install'
        } else {
            echo 'skip setup-environment stage'
        }
    }

    stage ('update fireball') {
        if (Boolean.parseBoolean(env.FIREBALL_UPDATE_FIREBALL)) {
            // bat 'git fetch origin'
            // bat 'git checkout '+ env.FIREBALL_BUILD_BRANCH
            bat 'gulp update-fireball'
        } else {
            echo 'skip update-fireball stage'
        }
    }

    stage ('update builtin') {
        if (Boolean.parseBoolean(env.FIREBALL_UPDATE_BUILTIN)) {
            bat 'gulp update-builtin'
        } else {
            echo 'skip update-builtin stage'
        }
    }

    stage ('checkout setting branch') {
        if (Boolean.parseBoolean(env.FIREBALL_CHECKOUT_SETTING_BRANCH)) {
            bat 'gulp checkout-setting-branch'
        } else {
            echo 'skip checkout-setting-branch stage'
        }
    }

    stage ('update') {
        if (Boolean.parseBoolean(env.FIREBALL_UPDATE)) {
            bat 'gulp update'
        } else {
            echo 'skip update stage'
        }
    }

    stage ('clean cache') {
        if (Boolean.parseBoolean(env.FIREBALL_CLEAN_CACHE)) {
            bat 'gulp clean-cache'
        } else {
            echo 'skip clean-cache stage'
        }
    }

    stage ('sync engine version') {
        if (Boolean.parseBoolean(env.FIREBALL_SYNC_ENGINE_VERSION)) {
            bat 'gulp sync-engine-version'
        } else {
            echo 'skip sync-engine-version stage'
        }
    }

    stage ('update externs') {
        if (Boolean.parseBoolean(env.FIREBALL_UPDATE_EXTERNS)) {
            bat 'gulp update-externs'
        } else {
            echo 'skip update-externs stage'
        }
    }

    stage ('update templates') {
        if (Boolean.parseBoolean(env.FIREBALL_UPDATE_TEMPLATES)) {
            bat 'gulp update-templates'
        } else {
            echo 'skip update-templates stage'
        }
    }
    stage ('push tag') {
        if (Boolean.parseBoolean(env.FIREBALL_PUSH_TAG)) {
            bat 'gulp push-tag'
        } else {
            echo 'skip push-tag stage'
        }
    }

    stage ('make dist') {
        bat 'npm install cocos-creator/creator-asar rcedit'
        bat 'gulp make-dist'
    }

    stage ('update templates') {
        bat 'gulp deploy'
    }
}