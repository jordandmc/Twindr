echo Restarting the web server...
plink -ssh -t -i ./TwindrSSH.ppk ec2-user@54.149.24.39 sudo kill -15 `sudo lsof -t -i:80` &
plink -ssh -t -i ./TwindrSSH.ppk ec2-user@54.149.24.39 sudo twindr-1.0/bin/twindr -Dconfig.resource=production.conf -Dhttp.port=80 &
echo Done deploying to server.
pause