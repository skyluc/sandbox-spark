#!/bin/bash -e

# loads environment if directly invoked by ssh
if [ -z "${SSH_TTY}" ]
then
  . ~/bin/ssh_env.sh
fi

function show_help() {
  echo "Usage: stop-hdfs-namenode.sh [-h]"
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

~/opt/hadoop/sbin/hadoop-daemon.sh --config ~/opt/hadoop/etc/hadoop --script hdfs stop namenode
