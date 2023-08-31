nohup \
/root/openlogic-openjdk-8u372-b07-linux-x64/bin/java \
-jar /root/TestPapaerGen-WebApp/TestPapaerGen-Backend/target/test-paper-generation-3.0.0.jar \
--server.port=18081 \
> /root/TestPapaerGen-WebApp/log/app.log 2>&1 &