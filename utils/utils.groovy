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
return this