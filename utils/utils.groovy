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
        def value = params[key]
        if(value instanceof Boolean){
            ls.add(booleanParam(name:key,value:value))
        }else {
            ls.add(string(name:key,value:value))
        }
    }
    return ls
}
return this