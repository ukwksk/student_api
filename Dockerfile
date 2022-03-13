FROM gradle:6.6.1-jdk14 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ls /home/gradle/src
RUN gradle build -x test

FROM openjdk:14-alpine

COPY --from=build /home/gradle/src/build/libs/student-api-0.0.1-all.jar app/

CMD java -jar app/student-api-0.0.1-all.jar
