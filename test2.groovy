def test2Param = [
  string(name: 'COCOS2DX_BUILD_BRANCH', defaultValue: 'v2.0.10-release', description: '构建的分支(对应GitHub上的branch)'),
  booleanParam(name: 'FIREBALL_SETUP_ENV', defaultValue: false, description: '是否初始化环境'),
  booleanParam(name: 'FIREBALL_MAKE_COCOS2DX', defaultValue: false, description: '是否打包上传 cocos2dx 代码'),
  booleanParam(name: 'FIREBALL_LITE_PUBLISH_MAC', defaultValue: true, description:'是否重新构建上传-lite仓库模拟器'),
];
properties([parameters(test2Param)])

for(int i = 0;i<test2Param.size();i++){
    echo '2 COCOS2DX_BUILD_BRANCH'+env.COCOS2DX_BUILD_BRANCH;
    echo '2 FIREBALL_MAKE_COCOS2DX'+env.FIREBALL_MAKE_COCOS2DX;
    echo '2 FIREBALL_MAKE_COCOS2DX'+env.FIREBALL_MAKE_COCOS2DX;
    echo '2 FIREBALL_LITE_PUBLISH_MAC'+env.FIREBALL_LITE_PUBLISH_MAC;
}

node('mac') {
    stage('test') {
        
    }
}

return this;

