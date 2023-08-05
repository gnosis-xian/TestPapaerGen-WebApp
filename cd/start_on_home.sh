cd /root/TestPapaerGen-WebApp/ && ls | grep -v deploy.tar | grep -v cd | xargs rm -rf
cd /root/TestPapaerGen-WebApp/ && tar -xf deploy.tar
echo "当前工作空间文件"
ll /root/TestPapaerGen-WebApp/
sh /root/TestPapaerGen-WebApp/cd/shutdown.sh
sh /root/TestPapaerGen-WebApp/cd/startup.sh