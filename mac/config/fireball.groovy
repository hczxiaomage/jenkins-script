def fireball = [
  string(name: 'FIREBALL_BUILD_BRANCH', defaultValue: 'v2.2.0-release', description: '构建的分支(对应GitHub上的branch)'),
  string(name: 'FIREBALL_PUBLISH_VERSION', defaultValue: '2.2.0', description: '用户实际看到的版本号'),
  string(name: 'FIREBALL_MAIL_TO', defaultValue: 'hao.wang@chukong-inc.com', description: '构建失败默认发送到谁的邮箱，多人用;隔开'),
  string(name: 'FIREBALL_MAIL_CC', defaultValue: 'zhiming.wu@chukong-inc.com', description: '构建失败默认发送到谁的邮箱，多人用;隔开'),
  booleanParam(name: 'FIREBALL_MAIL_SEND', defaultValue: true, description: '构建失败是否发邮件'),
  booleanParam(name: 'FIREBALL_HIDE_VERSION_CODE', defaultValue: false, description: '是否隐藏版本号'),
  booleanParam(name: 'FIREBALL_UPLOAD_WAN', defaultValue: false, description: '是否上传到外网'),
  booleanParam(name: 'FIREBALL_SKIP_NPM_REBUILD', defaultValue: true, description: '是否跳过 npm install 和 npm rebuild'),
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
]
def getFireball() {
    return fireball
}
return this