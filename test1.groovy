def test1Param = [
  string(name: 'COCOS2DX_BUILD_BRANCH', defaultValue: 'v2.0.10-release', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'FIREBALL_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
  booleanParam(name: 'FIREBALL_MAKE_COCOS2DX', defaultValue: false, description: '是否打包上传 cocos2dx 代码'),
  booleanParam(name: 'FIREBALL_LITE_PUBLISH_MAC', defaultValue: true, description:'是否重新构建上传-lite仓库模拟器'),
];
properties([parameters(test1Param)])

for(int i = 0;i<test1Param.size();i++){
    echo 'item is ' + test1Param[i];
}

node('mac') {
    stage('test') {
        
    }
}

return this;