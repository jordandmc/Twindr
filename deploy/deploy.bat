@echo off

echo Compiling distributable...
cd ..
start "Compile Twindr" "CMD /C sbt dist"
echo Continue when compiling is done.
pause
cd deploy
::timeout 20
goto comment
echo Uploading zip to the server machine...
pscp -i ./TwindrSSH.ppk ../target/universal/twindr-1.0.zip ec2-user@54.149.24.39:./Downloads
if %errorlevel% EQU 1 echo There was an error uploading zip! && pause && exit 1
echo Unziping the files on the server...
plink -ssh -i ./TwindrSSH.ppk ec2-user@54.149.24.39 unzip -o ./Downloads/twindr-1.0.zip
echo Restarting the web server...
plink -ssh -t -i ./TwindrSSH.ppk ec2-user@54.149.24.39 sudo kill -15 `sudo lsof -t -i:80` &
plink -ssh -t -i ./TwindrSSH.ppk ec2-user@54.149.24.39 sudo twindr-1.0/bin/twindr -Dhttp.port=80 &
:comment
echo Done deploying to server.
pause