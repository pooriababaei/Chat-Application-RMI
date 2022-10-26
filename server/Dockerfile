#openjdk maintained by Amazon
FROM amazoncorretto:19-alpine

WORKDIR /app

COPY . .
RUN find -name "*.java" > paths.txt &&\
    javac -d "bin" @paths.txt -cp "src/"
WORKDIR /app/bin
#Run java server.Server_Chat

CMD ["java", "server.Server_Chat"]