#!/bin/bash -e

function show_help() {
  echo "Usage: stop-mesos-master.sh [-h]"
  echo "  -h: this help"
}

while getopts "h" opt; do
    case "$opt" in
    h)
        show_help
        exit 0
        ;;
    esac
done

MESOS_MASTER_PID=$(ps aux | grep lt-mesos-master | grep -v grep | awk '{print $2;}')

echo "Terminating mesos master with pid: ${MESOS_MASTER_PID}"

kill ${MESOS_MASTER_PID}

