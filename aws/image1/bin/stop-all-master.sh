#!/bin/bash -e

function show_help() {
  echo "Usage: stop-all-master.sh [-h]"
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

~/bin/stop-mesos-master.sh
~/bin/stop-hdfs-namenode.sh

