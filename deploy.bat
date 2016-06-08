@echo off

if "%TOMCAT_HOME%"=="" (echo TOMCAT_HOME is not defined) else (goto deploy)

:deploy
call gradle war
move /Y build\libs\hangman-1.0-SNAPSHOT.war %TOMCAT_HOME%\webapps\hangman.war
echo "Deployment successful!"
pause