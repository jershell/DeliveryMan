# DeliveryMan

Simple handler for form on your site

Required environment variables
```
SMTP_HOSTNAME=smtp.yandex.ru
SMTP_PORT=587
SMTP_TLS=DISABLED | OPTIONAL | REQUIRED
SMTP_USERNAME=bla_bla@bla.bla
SMTP_PASSWORD=asdf123###123asdf

MAIL_LIST=user1@example.com,user2@example.com
MSG_TITLE=message from form
MSG_SITENAME=you.company.com
```
## API

**POST** ```/form``` JSON { email, message, phone, name }