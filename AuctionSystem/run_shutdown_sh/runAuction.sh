#!/bin/sh
### ==========================================================================###
##                                                                             ##
##   Mircoservice Bootstrap Script for Linux based OS's ##
##                                                                             ##
### ==========================================================================###


DIRNAME=`dirname $0`
PROGNAME=`basename $0`
GREP="grep"



REDHATVER=`cat /etc/redhat-release | awk '{print$7}'`

#if [ 1 -eq `echo "${REDHATVER} > 6.0" | bc` ]; then
#	echo "Going to write file edgeLB.conf in /etc/init/ folder"
#	grep_output_for_rhel6=`ls /etc/init/ | grep edgeLB.conf`
#	if [ "$grep_output_for_rhel6" != "" ]; then
#		grep_output_firstline=`cat /etc/init/edgeLB.conf | grep "start on runlevel \[2345\]"`
#		if [ "$grep_output_firstline" != "" ]; then
#			grep_output_execline=`cat /etc/init/edgeLB.conf | grep $(pwd)/runEdgeLB.sh`
#			if [ "$grep_output_execline" != "" ]; then
#				echo "Entry already exists in /etc/init/edgeLB.conf file"
#			else
#				echo "exec $(pwd)/runEdgeLB.sh" >> /etc/init/edgeLB.conf
#				echo "File written"
#			fi
#		else
#	                echo "start on runlevel [2345]" > /etc/init/edgeLB.conf
#			echo "exec $(pwd)/runEdgeLB.sh" >> /etc/init/edgeLB.conf
#			echo "File written"
#		fi
#	else
#		echo "start on runlevel [2345]" >> /etc/init/edgeLB.conf
#		echo "exec $(pwd)/runEdgeLB.sh" >> /etc/init/edgeLB.conf
#		echo "File written"
#	fi
#else
#	echo "Writing to /etc/rc.d/rc.local file"
#	grep_output_for_rhel5=`cat /etc/rc.d/rc.local | grep $(pwd)/runEdgeLB.sh`
#	if [ "$grep_output_for_rhel5" != "" ]; then		
#		echo "Entry already exists in /etc/rc.d/rc.local file."
#	else
#		echo "$(pwd)/runEdgeLB.sh" >> /etc/rc.d/rc.local
#		echo "Inserted this entry =  $(pwd)/runEdgeLB.sh"
#	fi
#fi

# Use the maximum available, or set MAX_FD != -1 to use that
MAX_FD="maximum"

#
# Helper to complain.
#
warn() {
    echo "${PROGNAME}: $*"
}

#
# Helper to puke.
#
die() {
    warn $*
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false;
darwin=false;
linux=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;

    Darwin*)
        darwin=true
        ;;
        
    Linux)
        linux=true
        ;;
esac

# Force IPv4 on Linux systems since IPv6 doesn't work correctly with jdk5 and lower
# For PC installation the values of Xms and Xmx in JAVA_OPTS can be kept as 512
# For load testing the values of Xms and Xmx in JAVA_OPTS can be kept according to the memory available

if [ "$linux" = "true" ]; then
 JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true -Xms4096m -Xmx4096m -server -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+UseTLAB -XX:+UseParNewGC -XX:NewSize=256m -XX:MaxNewSize=512m -XX:MaxTenuringThreshold=1 -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=90 -XX:+UseFastAccessorMethods -XX:MaxPermSize=256m -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
#JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true -Xms1024m -Xmx1024m -server -XX:+UseNUMA -XX:+UseCompressedStrings -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false
 
# Java opts used in chassis testing
 

   
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
    [ -n "$JAVAC_JAR" ] &&
        JAVAC_JAR=`cygpath --unix "$JAVAC_JAR"`
fi

# Increase the maximum file descriptors if we can
if [ "$cygwin" = "false" ]; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ]; then
	if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ]; then
	    # use the system max
	    MAX_FD="$MAX_FD_LIMIT"
	fi

	ulimit -n $MAX_FD
	if [ $? -ne 0 ]; then
	    warn "Could not set maximum file descriptor limit: $MAX_FD"
	fi
    else
	warn "Could not query system maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
fi

# Setup the JVM
if [ "x$JAVA" = "x" ]; then
    if [ "x$JAVA_HOME" != "x" ]; then
	JAVA="$JAVA_HOME/bin/java"
    else
	JAVA="java"
    fi
fi

# Setup the classpath
rtEdgeLBjar="./rtAuction.jar"
if [ ! -f "$rtEdgeLBjar" ]; then
    die "Missing required file: $rtAuctionjar"
fi


AS_BOOT_CLASSPATH="$rtEdgeLBjar"

# Only include tools.jar if someone wants to use the JDK instead.
# compatible distribution which JAVA_HOME points to
if [ "x$JAVAC_JAR" = "x" ]; then
    JAVAC_JAR_FILE="$JAVA_HOME/lib/tools.jar"
else
    JAVAC_JAR_FILE="$JAVAC_JAR"
fi

if [ ! -f "$JAVAC_JAR_FILE" ]; then
   # MacOSX does not have a seperate tools.jar
   if [ "$darwin" != "true" -a "x$JAVAC_JAR" != "x" ]; then
      warn "Missing file: JAVAC_JAR=$JAVAC_JAR"
      warn "Unexpected results may occur."
   fi
   JAVAC_JAR_FILE=
fi

if [ "x$AS_CLASSPATH" = "x" ]; then
    AS_CLASSPATH="$AS_BOOT_CLASSPATH"
else
    AS_CLASSPATH="$AS_CLASSPATH:$AS_BOOT_CLASSPATH"
fi
if [ "x$JAVAC_JAR_FILE" != "x" ]; then
    AS_CLASSPATH="$AS_CLASSPATH:$JAVAC_JAR_FILE"
fi

# If -server not set in JAVA_OPTS, set it, if supported
SERVER_SET=`echo $JAVA_OPTS | $GREP "\-server"`
if [ "x$SERVER_SET" = "x" ]; then

    # Check for SUN(tm) JVM w/ HotSpot support
    if [ "x$HAS_HOTSPOT" = "x" ]; then
	HAS_HOTSPOT=`"$JAVA" -version 2>&1 | $GREP -i HotSpot`
    fi

    # Enable -server if we have Hotspot, unless we can't
    if [ "x$HAS_HOTSPOT" != "x" ]; then
	# MacOS does not support -server flag
	if [ "$darwin" != "true" ]; then
	    JAVA_OPTS="-server $JAVA_OPTS"
	fi
    fi
fi

# Setup JAVA specific properties
JAVA_OPTS="-Dprogram.name=$PROGNAME $JAVA_OPTS"

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  AS_CLASSPATH=`cygpath --path --windows "$AS_CLASSPATH"`
fi
AS_CLASSPATH="$AS_CLASSPATH:../lib/*"
#------------------------- MAIN STARTUP --------------------------
LAUNCH_AS_IN_BACKGROUND=yes
RESTART_FLAG=true
while true; do
   if [ "x$LAUNCH_AS_IN_BACKGROUND" = "x" ]; then
      # Execute the JVM in the foreground
      "$JAVA"  $JAVA_OPTS \
         -classpath "$AS_CLASSPATH" \
         com.auction.bootstraper.AuctionBootsraper"$@"
      AS_STATUS=$?
   else
      # Execute the JVM in the background
      "$JAVA"  $JAVA_OPTS \
         -classpath "$AS_CLASSPATH" \
         com.auction.bootstraper.AuctionBootsraper"$@" &
      AS_PID=$!
      # Trap common signals and relay them to EdgeLB Microservice AS process
      trap "kill -HUP  $AS_PID" HUP
      trap "kill -TERM $AS_PID" INT
      trap "kill -QUIT $AS_PID" QUIT
      trap "kill -PIPE $AS_PID" PIPE
      trap "kill -TERM $AS_PID" TERM
      # Wait until the background process exits
      WAIT_STATUS=128
      SIGNAL=0
      while [ "$WAIT_STATUS" -ge 128 ]; do
         wait $AS_PID 2>/dev/null
         WAIT_STATUS=$?
         if [ "${WAIT_STATUS}" -gt 128 ]; then
            SIGNAL=`expr ${WAIT_STATUS} - 128`
            SIGNAL_NAME=`kill -l ${SIGNAL}`
            echo "*** Edge Load Balancer  Microservice process (${AS_PID}) received ${SIGNAL_NAME} signal (${SIGNAL}) ***"
         fi
      done
      if [ "${WAIT_STATUS}" -lt 127 ]; then
         AS_STATUS=$WAIT_STATUS
      else
         AS_STATUS=0
      fi
        if [ "${RESTART_FLAG}" = 'true' -a "${SIGNAL}" -ne 0 ]; then

                current_date=$(date)
                echo "$current_date : Received ${SIGNAL_NAME} signal. Going to restart the application."
                echo "$current_date : Received ${SIGNAL_NAME} signal. Going to restart the application in 2 seconds." >> ./logs/rtWorkflow.log
 			sleep 2
                ./runWorkflow.sh &
        else
                echo "Not going to restart the application."
        fi

   fi
   
   if [ "$AS_STATUS" -eq 10 ]; then
      echo "Restarting Edge Load Balancer  Microservice."
   else
      exit $AS_STATUS
   fi
done
