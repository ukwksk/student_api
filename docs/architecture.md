# アーキテクチャについて

今回の仕様およびドメインは非常にシンプルなため、
アーキテクチャもシンプルなものでレイヤードアーキテクチャを採用しました。

## 説明

各レイヤーとパッケージの対応は下記のようになっています。

```
src/
|- controller (プレゼンテーション層)
|- usecase （アプリケーション層）
|- domain （ドメイン層）
  |- service （Domain Logic）
  |- model （Domain Model / Factory）
  |- value （Value Object）
  |- repository （Repository Interface）
|- repository （リポジトリ層: Repository Imprementation）
  |- db （DB Schema）
```

各レイヤーの依存関係を適切に保つため、
Ktor への依存はプレゼンテーション層のみ、
Exposed への依存はリポジトリ層のみとなるようにしています。

例外として、アプリケーション層にトランザクション境界を持たせるため、
`org.jetbrains.exposed.sql.transactions.transaction` のみ
アプリケーション層 `usecase` から参照しています。

## UT について
### プレゼンテーション層
リクエストに対し、バリデーション・データ変換が期待通りに行われていることを確認しています。

### ユースケース層
モックライブラリ（Mockito）を利用し、各 Domain Service からの戻り値に応じて、期待した挙動になっていることを確認しています。

### ドメイン層
実際に DI・DB接続 を行い、ドメイン単位で期待通りのDB操作（今回は参照のみ）ができていることを確認しています。
