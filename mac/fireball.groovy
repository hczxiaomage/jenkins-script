import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException

node('mac') {
    def utils = load '../../../jenkins-script/utils/utils.groovy'
    try {
        boolean isCancel = false
        stage('update jenkins script') {
            //load script and init some config
            //加载同一份打包脚本，在 Creator_2D 目录下
            def conf = load '../../../jenkins-script/config/fireball.groovy'
            def list = [
                    booleanParam(name: 'FIREBALL_AUTO_TEST_IOS', defaultValue: true, description: '是否自动测试 iOS'),
                    booleanParam(name: 'FIREBALL_AUTO_TEST_IOS_WEIXIN', defaultValue: true, description: '是否自动测试iOS微信'),
            ]
            def originList = utils.getParams();
            originList.addAll(list);

            properties([parameters(originList)])

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
            echo 'checkout branch ---' + env.FIREBALL_BUILD_BRANCH
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
                utils.execGulp('update-fireball');
            } else {
                echo 'skip update-fireball stage'
            }
        }
    
        stage ('update builtin') {
            if (Boolean.parseBoolean(env.FIREBALL_UPDATE_BUILTIN)) {
                 utils.execGulp('prune-builtin');
                 utils.execGulp('clone-builtin');
            } else {
                echo 'skip update-builtin stage'
            }
        }
    
        stage ('checkout setting branch') {
            if (Boolean.parseBoolean(env.FIREBALL_CHECKOUT_SETTING_BRANCH)) {
                utils.execGulp('checkout-setting-branch');
            } else {
                echo 'skip checkout-setting-branch stage'
            }
        }
    
        stage ('update hosts') {
            if (Boolean.parseBoolean(env.FIREBALL_UPDATE_HOSTS)) {
                utils.execGulp('update-hosts');
            } else {
                echo 'skip update stage'
            }
        }
        
        stage ('update builtin') {
            utils.execGulp('clone-update-builtin');
        }
        
        stage ('clean engine dev') {
            utils.execGulp('clean-engine-dev');
        }
        
        stage ('build engine') {
            utils.execGulp('build-engine');
        }
        
        stage ('make tsd') {
            utils.execGulp('make-tsd');
        }
    
        stage ('clean cache') {
            if (Boolean.parseBoolean(env.FIREBALL_CLEAN_CACHE)) {
                utils.execGulp('clean-cache');
            } else {
                echo 'skip clean-cache stage'
            }
        }
    
        stage ('sync engine version') {
            if (Boolean.parseBoolean(env.FIREBALL_SYNC_ENGINE_VERSION)) {
                utils.execGulp('sync-engine-version');
            } else {
                echo 'skip sync-engine-version stage'
            }
        }
    
        stage ('update externs') {
            if (Boolean.parseBoolean(env.FIREBALL_UPDATE_EXTERNS)) {
                utils.execGulp('update-externs');
            } else {
                echo 'skip update-externs stage'
            }
        }
    
        stage ('update templates') {
            if (Boolean.parseBoolean(env.FIREBALL_UPDATE_TEMPLATES)) {
                utils.execGulp('update-templates');
            } else {
                echo 'skip update-templates stage'
            }
        }
        stage ('push tag') {
            if (Boolean.parseBoolean(env.FIREBALL_PUSH_TAG)) {
                utils.execGulp('push-tag');
            } else {
                echo 'skip push-tag stage'
            }
        }
    
        stage ('make dist and deploy') {
            utils.execGulp('make-dist-and-deploy');
        }

        if(params.AUTO_TEST)  {
            stage ('auto test iOS') {
                if (params.FIREBALL_AUTO_TEST_IOS) {
                    println 'CocosCreator_Path' + pwd()+'\\'+'dist'
                    build job: 'AutoTest_Creator_iOS/iOS', parameters:[string(name: 'CocosCreator_Path',value: pwd() + 'dist/CocosCreator.app/Contents/MacOS/CocosCreator')]
                } else {
                    echo 'auto test android'
                }
            }

            stage ('auto test wechat') {
                if (params.FIREBALL_AUTO_TEST_WEIXIN) {
                    build job: 'AutoTest_Creator_iOS_weixin/weixin_MiniProgram/', parameters:[string(name: 'CocosCreator_Path',value: pwd() + 'dist/CocosCreator.app/Contents/MacOS/CocosCreator')]
                } else {
                    echo 'auto test web'
                }
            }
        }
    } catch (e) {
        e.printStackTrace()
        if (Boolean.parseBoolean(env.FIREBALL_PUSH_TAG) && !(e instanceof FlowInterruptedException)) {
                  echo '构建失败发邮件'
                  def platform = 'Windows'
                  if(isUnix()){
                      platform = 'Mac'
                  }
                  utils.sendMail(env.FIREBALL_MAIL_TO,env.FIREBALL_MAIL_CC,'构建' + platform + '失败' ,'构建版本 '+ env.FIREBALL_BUILD_BRANCH +' 失败, 时间：' + new Date())
              }
    }
}
