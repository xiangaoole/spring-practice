# build
mvn clean package -U
if [ "$?" -ne "0" ]; then
  exit $?
fi

# deploy
java -jar user-web/target/user-web-v1-SNAPSHOT-war-exec.jar