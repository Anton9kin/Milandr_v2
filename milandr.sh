#!/usr/bin/sh
#cd target;
# setup your valid JRE path here
export JRE_HOME="/opt/jdk1.8.0_121/jre";
export JFX_PATH="${JAVA_HOME}/lib/"
${JRE_HOME}/bin/java -cp "${JFX_PATH}" -jar MilandrFx_v2.jar
#cd ..