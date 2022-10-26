# ubuntu with xrdp installed
FROM linuxserver/rdesktop:ubuntu-xfce

WORKDIR /app

COPY . .

RUN sudo apt update &&\
    sudo apt install openjdk-8-jdk -y &&\
    find -name "*.java" > paths.txt &&\
    mkdir bin &&\
    javac -d "bin" @paths.txt -cp "src/"
WORKDIR /app/bin
#CMD ["java", "client.Client_Chat"] Application Could be run only after connecting to the virtual ubuntu.
