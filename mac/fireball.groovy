import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException

def sendMail(sentTo,cc,title,body) {
    mail body:body,subject:title,to:sentTo,cc:cc
}

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
        stage('update jenkins script') {
            git branch: 'creator', url: 'git@github.com:wuzhiming/jenkins-script.git'
            //load script and init some config
            def conf = load '../jenkins-script/config/fireball.groovy'
            properties([parameters(conf.getParams())])

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
        }

        stage ('checkout code') {
            git branch: "${FIREBALL_BUILD_BRANCH}", url: 'git@github.com:cocos-creator/fireball.git'
        }
    
        stage ('setup environment') {
            if (Boolean.parseBoolean(env.FIREBALL_SETUP_ENV)) {
                sh 'npm install'
                sh 'npm install cocos-creator/creator-asar'
                sh 'npm install appdmg -g'
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
    } catch (e) {
        if (Boolean.parseBoolean(env.FIREBALL_PUSH_TAG) && !(e instanceof FlowInterruptedException)) {
                  echo '构建失败发邮件'
                  def platform = 'Windows'
                  if(isUnix()){
                      platform = 'Mac'
                  }
                  sendMail(env.FIREBALL_MAIL_TO,env.FIREBALL_MAIL_CC,'构建' + platform + '失败' ,'构建版本 '+ env.FIREBALL_BUILD_BRANCH +' 失败, 时间：' + new Date())
              }
    }
}
