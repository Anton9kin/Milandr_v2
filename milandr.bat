@ECHO OFF
REM setup your valid JRE path here
SET "JRE_HOME=C:\Program Files (x86)\Java\jre1.8.0_121"
SET "JAVA_HOME=%JRE_HOME%\bin"
SET "JFX_PATH=%JRE_HOME%\lib"
java -cp "%JFX_PATH%" -jar MilandrFx_v2.jar