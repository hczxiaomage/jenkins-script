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

def genParams(lists){
    def ls = []
    for(item in lists){
        def key = item.toMap().get('name');
        ls.add(string(name:key,value:params[key]))
    }
    return ls
}
return this