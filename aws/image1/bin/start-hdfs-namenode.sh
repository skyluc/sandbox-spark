#!/bin/bash -e

# loads environment if directly invoked by ssh
if [ -z "${SSH_TTY}" ]
then
  . ~/bin/ssh_env.sh
fi

function show_help() {
  echo "Usage: start-hdfs-namenode.sh [-h]"
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
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)

~/bin/utils/create-hdfs-config.sh ${PRIVATE_IP}

~/opt/hadoop/sbin/hadoop-daemon.sh --config ~/opt/hadoop/etc/hadoop --script hdfs start namenode

echo "HDFS namenode started at : ${PRIVATE_IP}"
echo "namenode hostname for HDFS namenodes: ${PRIVATE_IP}"
echo "web ui at: http://${PUBLIC_IP}:50070/"
