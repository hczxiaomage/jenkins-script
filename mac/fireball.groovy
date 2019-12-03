import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException
properties([parameters([
  string(name: 'FIREBALL_BUILD_BRANCH', defaultValue: 'v2.2.0-release', description: '构建的分支(对应GitHub上的branch)'),
  string(name: 'FIREBALL_PUBLISH_VERSION', defaultValue: '2.2.0', description: '用户实际看到的版本号'),
  booleanParam(name: 'FIREBALL_HIDE_VERSION_CODE', defaultValue: false, description: '是否隐藏版本号'),
  booleanParam(name: 'FIREBALL_UPLOAD_WAN', defaultValue: false, description: '是否上传到外网'),
  booleanParam(name: 'FIREBALL_SKIP_NPM_REBUILD', defaultValue: false, description: '是否跳过 npm install 和 npm rebuild'),
  booleanParam(name: 'FIREBALL_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
  booleanParam(name: 'FIREBALL_UPDATE_FIREBALL', defaultValue: true, description: 'update fireball'),
  booleanParam(name: 'FIREBALL_UPDATE_BUILTIN', defaultValue: true, description: '是否更新built-in'),
  booleanParam(name: 'FIREBALL_CHECKOUT_SETTING_BRANCH', defaultValue: true, description: '是否迁出对应版本的分支'),
  booleanParam(name: 'FIREBALL_UPDATE_HOSTS', defaultValue: true, description: '是否更新hosts'),
  booleanParam(name: 'FIREBALL_CLEAN_CACHE', defaultValue: true, description: '是否清除缓存'),
  booleanParam(name: 'FIREBALL_SYNC_ENGINE_VERSION', defaultValue: true, description: '是否同步引擎版本'),
  booleanParam(name: 'FIREBALL_UPDATE_EXTERNS', defaultValue: true, description: '是否更新externs'),
  booleanParam(name: 'FIREBALL_UPDATE_TEMPLATES', defaultValue: true, description: '是否更新新建工程的模板'),
  booleanParam(name: 'FIREBALL_PUSH_TAG', defaultValue: true, description: '是否添加tag'),
])])

def sendMail(sentTo,cc,title,body) {
    emailext to:sentTo, body:body,subject:title,cc:cc 
}

boolean isCancel = false

String paramStr = Boolean.parseBoolean(env.FIREBALL_HIDE_VERSION_CODE)? ' -B ':' -b ';
paramStr += env.FIREBALL_PUBLISH_VERSION;

if (Boolean.parseBoolean(env.FIREBALL_UPLOAD_WAN)) {
    paramStr += ' --fw'
}
if (Boolean.parseBoolean(env.FIREBALL_SKIP_NPM_REBUILD)) {
    paramStr += ' --sn'
}

env.PARAM_STRING = paramStr;

def execGulp(taskName) {
    String command = 'gulp ' + taskName + ' ' + env.PARAM_STRING;

    echo 'exec command ' + command;
    if (isUnix()) {
        sh command;
    } else {
        bat command;
    }
}

node('mac') {
try {
    stage ('checkout code') {
            git branch: "${FIREBALL_BUILD_BRANCH}", url: 'git@github.com:cocos-creator/fireball.git'
        }
    
        stage ('setup environment') {
            if (Boolean.parseBoolean(env.FIREBALL_SETUP_ENV)) {
                sh 'npm install --production'
                sh 'npm install cocos-creator/creator-asar'
                sh 'npm install rcedit@2.0.0 -g'
                sh 'npm run bootstrap'
            } else {
                echo 'skip setup-environment stage'
            }
        }
    
        stage ('update fireball') {
            if (Boolean.parseBoolean(env.FIREBALL_UPDATE_FIREBALL)) {
                execGulp('update-fireball');
            } else {
                echo 'skip update-fireball stage'
            }
        }
    
        stage ('update builtin') {
            if (Boolean.parseBoolean(env.FIREBALL_UPDATE_BUILTIN)) {
                 execGulp('prune-builtin');
                 execGulp('clone-builtin');
            } else {
                echo 'skip update-builtin stage'
            }
        }
    
        stage ('checkout setting branch') {
            if (Boolean.parseBoolean(env.FIREBALL_CHECKOUT_SETTING_BRANCH)) {
                execGulp('checkout-setting-branch');
            } else {
                echo 'skip checkout-setting-branch stage'
            }
        }
    
        stage ('update hosts') {
            if (Boolean.parseBoolean(env.FIREBALL_UPDATE_HOSTS)) {
                execGulp('update-hosts');
            } else {
                echo 'skip update stage'
            }
        }
        
        stage ('update builtin') {
            execGulp('clone-update-builtin');
        }
        
        stage ('clean engine dev') {
            execGulp('clean-engine-dev');
        }
        
        stage ('build engine') {
            execGulp('build-engine');
        }
        
        stage ('make tsd') {
            execGulp('make-tsd');
        }
    
        stage ('clean cache') {
            if (Boolean.parseBoolean(env.FIREBALL_CLEAN_CACHE)) {
                execGulp('clean-cache');
            } else {
                echo 'skip clean-cache stage'
            }
        }
    
        stage ('sync engine version') {
            if (Boolean.parseBoolean(env.FIREBALL_SYNC_ENGINE_VERSION)) {
                execGulp('sync-engine-version');
            } else {
                echo 'skip sync-engine-version stage'
            }
        }
    
        stage ('update externs') {
            if (Boolean.parseBoolean(env.FIREBALL_UPDATE_EXTERNS)) {
                execGulp('update-externs');
            } else {
                echo 'skip update-externs stage'
            }
        }
    
        stage ('update templates') {
            if (Boolean.parseBoolean(env.FIREBALL_UPDATE_TEMPLATES)) {
                execGulp('update-templates');
            } else {
                echo 'skip update-templates stage'
            }
        }
        stage ('push tag') {
            if (Boolean.parseBoolean(env.FIREBALL_PUSH_TAG)) {
                execGulp('push-tag');
            } else {
                echo 'skip push-tag stage'
            }
        }
    
        stage ('make dist and deploy') {
            execGulp('make-dist-and-deploy');
        }
    } catch (FlowInterruptedException interruptEx) {
        isCancel = true
    } finally {
        echo '构建失败发邮件'
        sendMail('154179667@qq.com','zhiming.wu@chukong-inc.com','构建结果','构建${env.FIREBALL_BUILD_BRANCH} 失败')
        if (!isCancel) {
        }
    }
   
}
