#!/bin/bash -e

# loads environment if directly invoked by ssh
if [ -z "${SSH_TTY}" ]
then
  . ~/bin/ssh_env.sh
fi

function show_help() {
  echo "Usage: start-mesos-master.sh [-h] <mesos-master-ip>"
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

MESOS_MASTER_IP=$1

if [ -z "${MESOS_MASTER_IP}" ]
then
  echo "Missing mesos master ip" >&2
  show_help
  exit 1
fi

export MESOS_NATIVE_LIBRARY=/home/ubuntu/opt/mesos/src/.libs/libmesos.so
export SPARK_EXECUTOR_URI="hdfs://${MESOS_MASTER_IP}:9000/opt/spark/spark-1.2.0-bin-hadoop2.4.tgz"

PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)

echo "spark web UI will be available at: http://${PUBLIC_IP}:4040/"

~/opt/spark/bin/spark-shell --master "mesos://${MESOS_MASTER_IP}:5050"
