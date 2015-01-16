#!/bin/bash -e

function show_help() {
  echo "Usage: stop-hdfs-datanode.sh [-h]"
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

~/opt/hadoop/sbin/hadoop-daemon.sh --config ~/hadoop/etc/hadoop --script hdfs stop datanode

