#!/bin/bash -e

function show_help() {
  echo "Usage: start-all-master.sh [-h]"
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

~/bin/start-mesos-master.sh
~/bin/start-hdfs-namenode.sh

