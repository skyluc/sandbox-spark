#!/bin/bash -e

# $1 - hdfs namenode hostname

cat > ~/opt/hadoop/etc/hadoop/core-site.xml << END
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://$1:9000</value>
    </property>
</configuration>
END

