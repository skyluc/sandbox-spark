#!/bin/bash -e

function show_help() {
  echo "Usage: start-hdfs-datanode.sh [-h] <hdfs-namenode-ip>"
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

HDFS_NAMENODE_IP=$1

if [ -z "${HDFS_NAMENODE_IP}" ]
then
  echo "Missing hdfs namenode ip" >&2
  show_help
  exit 1
fi

~/bin/utils/create-hdfs-config.sh ${HDFS_NAMENODE_IP}

~/hadoop/sbin/hadoop-daemon.sh --config ~/hadoop/etc/hadoop --script hdfs start datanode

PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)

echo "hdfs datanode started, connecting to master: ${HDFS_NAMENODE_IP}:9000"
echo "web ui at: http://${PUBLIC_IP}:50075/"
