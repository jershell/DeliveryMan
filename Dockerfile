FROM anapsix/alpine-java
EXPOSE 8888:8888

ENV SMTP_HOSTNAME smtp.yandex.ru
ENV SMTP_PORT 587
ENV SMTP_TLS REQUIRED
ENV SMTP_USERNAME asdf@asdf.ru
ENV SMTP_PASSWORD 12345678
ENV MAIL_LIST user1@example.com,user2@example.com
ENV MSG_TITLE title on message
ENV MSG_SITENAME you-site-name.com

RUN mkdir /app
COPY ./build/libs/app.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]