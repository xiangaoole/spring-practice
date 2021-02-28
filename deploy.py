from os import system

# build
build = system("mvn clean package -U")

# deploy
if build == 0:
    system("java -jar user-web/target/user-web-v1-SNAPSHOT-war-exec.jar")