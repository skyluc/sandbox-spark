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

~/opt/mesos/bin/mesos-slave.sh --master=${MESOS_MASTER_IP}:5050 > /tmp/mesos-slave.log 2>&1 &

PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)

echo "mesos slave starting, connecting to master: ${MESOS_MASTER_IP}:5050"
echo "logs at: /tmp/mesos-slave.log"
