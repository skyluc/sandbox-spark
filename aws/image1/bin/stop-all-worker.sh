#!/bin/bash -e

function show_help() {
  echo "Usage: stop-all-worker.sh [-h]"
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

~/bin/stop-mesos-slave.sh
~/bin/stop-hdfs-datanode.sh

