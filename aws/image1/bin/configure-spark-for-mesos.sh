#!/bin/bash -e

function show_help() {
  echo "Usage: configure-spark-for-mesos.sh [-h]"
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

HDFS_URI="hdfs://${PRIVATE_IP}:9000"

SPARK_DIST="spark-1.2.0-bin-hadoop2.4.tgz"

DFS_CMD="${HOME}/hadoop/bin/hdfs dfs"

echo "remove previous version of the spark distribution (if any) from hdfs"

${DFS_CMD} -rm -f ${HDFS_URI}/opt/spark/${SPARK_DIST}

echo "create the /opt/spark folder in hdfs"

${DFS_CMD} -mkdir -p ${HDFS_URI}/opt/spark

echo "upload the spark distribution in /opt/spark in hdfs"

${DFS_CMD} -put ~/opt/archive/${SPARK_DIST} ${HDFS_URI}/opt/spark

~/bin/utils/create-hdfs-config.sh ${PRIVATE_IP}

echo "spark distribution available at: ${HDFS_URI}/opt/spark/${SPARK_DIST}"
