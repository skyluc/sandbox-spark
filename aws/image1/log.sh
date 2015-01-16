# Setup of AMI for ec2 with Mesos 0.20.1, hdfs (hadoop) 2.5.2, Spark 1.2.0


# base folders
mkdir -p opt/archive dev bin

# install dependencies needed by mesos
sudo apt-get update
sudo apt-get upgrade
sudo apt-get install build-essential
# doc was saying jdk6, but jdk7 is less old ..
sudo apt-get install openjdk-7-jdk
sudo apt-get install python-dev python-boto
sudo apt-get install libcurl4-nss-dev
sudo apt-get install libsasl2-dev
sudo apt-get install maven

# build mesos
cd dev/
wget http://www.apache.org/dist/mesos/0.20.1/mesos-0.20.1.tar.gz
mv mesos-0.20.1.tar.gz ~/opt/archive
tar -xf mesos-0.20.1.tar.gz
cd mesos-0.20.1/
mkdir build
cd build
../configure

# 'install' mesos
cd ~/opt
ln -s ~/dev/mesos-0.20.1/build mesos
sudo mkdir -p /var/lib/mesos
sudo hown ubuntu:ubuntu /var/lib/mesos

# install hadoop
cd ~/opt
wget ftp://mirror.switch.ch/mirror/apache/dist/hadoop/common/hadoop-2.5.2/hadoop-2.5.2.tar.gz
tar xf hadoop-2.5.2.tar.gz
mv hadoop-2.5.2.tar.gz archive/
ln -s hadoop-2.5.2 hadoop

# install spark
cd ~/opt
wget http://d3kbcqa49mib13.cloudfront.net/spark-1.2.0-bin-hadoop2.4.tgz
tar xf spark-1.2.0-bin-hadoop2.4.tgz 
mv spark-1.2.0-bin-hadoop2.4.tgz archive
ln -s spark-1.2.0-bin-hadoop2.4 spark

# install dependency needed by spark
# for MLlib
sudo apt-get install libgfortran3

# link provided files
ln -s ~/dev/spark/sandbox-spark/aws/image1/.bash_aliases ~/.bash_aliases
ln -s ~/dev/spark/sandbox-spark/aws/image1/bin ~/bin
