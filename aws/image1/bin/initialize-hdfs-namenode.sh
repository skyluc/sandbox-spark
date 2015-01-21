#!/bin/bash -e

# loads environment if directly invoked by ssh
if [ -z "${SSH_TTY}" ]
then
  . ~/bin/ssh_env.sh
fi

function show_help() {
  echo "Usage: initialize-hdfs-namenode.sh [-h]"
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

PRIVATE_IP=$(curl -s http://169.254.169.254/latest/meta-data/local-ipv4)

~/bin/utils/create-hdfs-config.sh ${PRIVATE_IP}

echo "remove any previous namenode info"

rm -rf /tmp/hadoop-ubuntu/dfs/name

echo "logs at: /tmp/hdfs-format.log"

~/opt/hadoop/bin/hdfs namenode -format > /tmp/hdfs-format.log 2>&1

echo "HDFS namenode initialized"
