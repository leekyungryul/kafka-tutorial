# Kafka Binaries 설치

1. macos
   - Java JDK version 11 설치
    sdkman으로 설치
    ```
    $ sdk install java 11.0.21-amzn
    $ sdk use java 11.0.21-amzn
    ```
   - Kafka 2.13-3.6.1 다운로드 From https://downloads.apache.org/kafka/3.6.1/kafka_2.13-3.6.1.tgz
   - 압축 해제
    ```
    $ tar -xzf kafka_2.13-3.6.1.tgz
    ```
    - 환경변수 설정
     ```
    vi ~lkr/.zshrc
    아래 코드 추가
    PATH="$PATH:/Users/lkr/kafka_2.13-3.6.1/bin"
    ```
    - Kafka 실행
    ```
    $ zookeeper-server-start.sh ~/kafka_2.13-3.6.1/config/zookeeper.properties
    $ kafka-server-start.sh ~/kafka_2.13-3.6.1/config/server.properties
    ```
   
2. linux(centos)
    - sdkman 설치
     ```
     $ yum install zip
     $ yum install unzip
     $ curl -s "https://get.sdkman.io" | bash
     $ source "$HOME/.sdkman/bin/sdkman-init.sh"
     ```
    - Java JDK version 11 설치
     sdkman으로 설치
     ```
     $ sdk install java 11.0.11-amzn
     $ sdk use java 11.0.21-amzn
     ```
   - update-alternatives --config javac
   
   - Kafka 2.13-3.6.1 다운로드 
    ```
    $ yum install wget
    $ wget https://downloads.apache.org/kafka/3.6.1/kafka_2.13-3.6.1.tgz
    $ tar -xzf kafka_2.13-3.6.1.tgz
   ```
   - 환경변수 설정
   ```
   vi /etc/profile
   아래 코드 추가
   export KAFKA_HOME=/home/sw/kafka
   export PATH=$KAFKA_HOME/bin:$PATH
   ```
   - Kafka 실행
   ```
   root 계정으로 실행
   $ zookeeper-server-start.sh ~/kafka_2.13-3.6.1/config/zookeeper.properties
   $ kafka-server-start.sh ~/kafka_2.13-3.6.1/config/server.properties
   ```