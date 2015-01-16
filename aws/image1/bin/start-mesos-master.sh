#!/bin/bash -e

function show_help() {
  echo "Usage: start-mesos-master.sh [-h]"
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

~/mesos/bin/mesos-master.sh --ip=0.0.0.0 --work_dir=/var/lib/mesos > /tmp/mesos-master.log 2>&1 &

PRIVATE_IP=$(curl -s http://169.254.169.254/latest/meta-data/local-ipv4)
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)

echo "mesos starting on ip: 0.0.0.0"
echo "logs at: /tmp/mesos-master.log"
echo "master ip for mesos slaves: ${PRIVATE_IP}"
echo "web ui at: http://${PUBLIC_IP}:5050/"
