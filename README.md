# student-api

## Required
* docker-compose

### for dev
* Java openjdk 14

## 起動方法
```shell
$ docker-compose up
```

### 説明
* `student-api`
  * gradle 公式 Docker Image を用いて build しています。
  * 初回の build 時は gradle の解決のため、2~3分程度の時間を要します。
* `student-db`
  * PostgreSQL 公式 Docker Image を用いて DB を起動しています。
  * 初回起動時にサンプルデータの投入を行っています。

## 動作確認方法
### [Swagger Editor](https://editor.swagger.io/) を使用する場合
1. https://editor.swagger.io/ にアクセスし、[OpenAPI](./docs/openapi.yml) の内容を Copy & Paste する
2. `/students` の「Try it out」から、各パラメータを入力し「Execute」で実行

Swagger Editor を使用すると不正なパラメータを送信できないため、例外系の動作を確認する場合は下記「[curl を使用する場合](#curl-を使用する場合)」

### curl を使用する場合
```shell
curl -v localhost:8081/students --get \
  --data-urlencode 'teacherId=1' \
  --data-urlencode 'page=1' \
  --data-urlencode 'limit=1' \
  --data-urlencode 'sort=name' \
  --data-urlencode 'order=asc' \
  --data-urlencode 'name_like=生徒2' \
  --data-urlencode 'loginId_like=student_2'
```

## 説明資料
* [アーキテクチャについて](./docs/architecture.md)

### 補足資料
* [仕様について](./docs/specification.md)
* [サンプルデータ](./docs/sample_data.md)
* [OpenAPI](./docs/openapi.yml)
