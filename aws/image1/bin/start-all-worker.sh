#!/bin/bash -e

function show_help() {
  echo "Usage: start-all-worker.sh [-h] <master-ip>"
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

shift $((OPTIND-1))

MASTER_IP=$1

if [ -z "${MASTER_IP}" ]
then
  echo "Missing master ip" >&2
  show_help
  exit 1
fi

~/bin/start-mesos-slave.sh ${MASTER_IP}
~/bin/start-hdfs-datanode.sh ${MASTER_IP}

