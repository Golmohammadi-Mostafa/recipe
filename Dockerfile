FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.13_8
VOLUME /tmp
ADD target/recipe.jar recipe.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","recipe.jar"]