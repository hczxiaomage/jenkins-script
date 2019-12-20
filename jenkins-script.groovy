//用于添加 jenkins-script 到对应节点的脚本
properties([parameters([
    booleanParam(name: 'IS_WINDOWS', defaultValue: false, description: '是否是 windows 节点'),
])])
def nodeName = 'mac'
if(Boolean.parseBoolean(env.IS_WINDOWS)) {
    nodeName = 'windows'
}
node (nodeName) {
    git branch: 'creator', url: 'git@github.com:wuzhiming/jenkins-script.git'
}