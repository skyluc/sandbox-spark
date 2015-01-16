#!/bin/bash -e

function show_help() {
  echo "Usage: stop-mesos-slave.sh [-h]"
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

MESOS_SLAVE_PID=$(ps aux | grep lt-mesos-slave | grep -v grep | awk '{print $2;}')

echo "Terminating mesos slave with pid: ${MESOS_SLAVE_PID}"

kill ${MESOS_SLAVE_PID}

