properties([parameters([
  string(name: 'FIREBALL_BUILD_BRANCH', defaultValue: 'v2.0-release', description: '构建的分支(对应GitHub上的branch)'),
])])

node('mac') {
    stage ('setup environment') {
        echo 'begin checkout branch'
        git branch: "${FIREBALL_BUILD_BRANCH}", url: 'git@github.com:wuzhiming/jenkins.git'
        sh 'node index.js'
    }
}