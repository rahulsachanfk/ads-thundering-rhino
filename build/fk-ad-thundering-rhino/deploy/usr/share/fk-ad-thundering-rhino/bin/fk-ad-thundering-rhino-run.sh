#!/bin/bash

exec 2>&1

PAC=_PACKAGE_

[ -x /etc/default/${PAC} ] && . /etc/default/${PAC}

exec setuidgid ${USER} $JAVA_BIN -cp ${CLASSPATH} $JMX_OPT $MEM_OPT $JFR_OPT $DEBUG_OPT $MAIN server $CFG_FILE

