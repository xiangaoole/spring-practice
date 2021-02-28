# build
mvn clean package -U

# deploy
java -jar user-web/target/user-web-v1-SNAPSHOT-war-exec.jar
