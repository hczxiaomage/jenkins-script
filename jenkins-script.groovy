//用于添加 jenkins-script 到对应节点的脚本
node ('mac') {
    git branch: 'creator', url: 'git@github.com:wuzhiming/jenkins-script.git'
}
node ('windows') {
    git branch: 'creator', url: 'git@github.com:wuzhiming/jenkins-script.git'
}