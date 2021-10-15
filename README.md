# Bloginner
Bloginnerは基本的な機能を持つヘッドレスなコンテンツ管理システム(Headless CMS)です。  
Spring Bootで提供されるREST API(本リポジトリ)とAngularで提供される管理画面([bloginner-admin](https://github.com/kthksgy/bloginner-admin))から成り立ちます。  
- 記事の投稿／閲覧／編集／削除
- コメント機能
- マルチユーザー

## 環境
- Java 11
- MySQL 8

## ビルド
ビルドには以下のビルド済みSPAを`src/main/resources/static`に配置して、本プロジェクトのビルドを行いWARファイルを生成します。

- 管理画面 [bloginner-admin](https://github.com/kthksgy/bloginner-admin)
- デフォルトビュー [bloginner-view](https://github.com/kthksgy/bloginner-view)

## 使用方法

### データベースのセットアップ
デフォルトでは以下の情報のデータベースを使用します。

- `spring.datasource.url=jdbc:mysql://localhost/bloginner`
- `spring.datasource.username=bloginner`
- `spring.datasource.password=bloginnerpassword`

以下のコマンドで作成可能です。

```
CREATE DATABASE bloginner;
CREATE USER 'bloginner'@'localhost' IDENTIFIED BY 'bloginnerpassword';
GRANT ALL ON bloginner.* TO 'bloginner'@'localhost';
```

### 起動
以下のコマンドで起動可能です。デフォルトではポート`80`番で起動します。

```
$ java -jar bloginner.war
```

#### デフォルトユーザー
システムには`admin`という管理権限を持ったユーザーが存在します。
初期パスワードは`admin`です。

ログイン後はパスワードの変更を推奨します。

**`admin`ユーザーを削除すると次回起動時に初期パスワード`admin`で再生成されるのでご注意ください。**

### 設定
`-D`から始まるコマンドライン引数を設定するとBloginnerの動作を一部変更できます。

#### 【重要】ブログパスフレーズ設定
本システムはJWT(JSON Web Token)による認証を行うため、改竄検知用のパスフレーズの設定が必須です。<br>
以下のようにコマンドライン引数を設定して起動する事を強く推奨します。
デフォルトでは以下の設定が自動で読み込まれます。

- パスフレーズ: `itssecretstring`
`-Dbloginner.passphrase=itssecretstring`

```
# 設定例
$ java -jar -Dbloginner.passphrase=itssecretstring bloginner-1.0.0.war
```

#### ポート設定
本システムを公開するポートを設定します。

```
# 設定例
$ java -jar -Dserver.port=80 bloginner-1.0.0.war
```

#### データベース設定
以下のようにコマンドライン引数を設定するとDB接続設定を変更できます。
指定しない場合は以下の設定が自動で読み込まれます。

- 接続先: `localhost`
- データベース名: `bloginner`
`-Dspring.datasource.url=jdbc:mysql://localhost/bloginner`
- 接続ユーザー名: `bloginner`
`-Dspring.datasource.username=bloginner`
- 接続パスワード: `bloginner`
`-Dspring.datasource.password=bloginnerpassword`

```
# 設定例
$ java -jar -Dspring.datasource.url=jdbc:mysql://localhost/bloginner -Dspring.datasource.username=bloginner -Dspring.datasource.password=bloginnerpassword bloginner-1.0.0.war
```

#### ブログタイトル設定
以下のようにコマンドライン引数を設定するとブログタイトルを設定できます。

- タイトル: `Bloginner`
`-Dbloginner.title=Bloginner`

```
# 設定例
$ java -jar -Dbloginner.title=Bloginner bloginner-1.0.0.war
```

#### デフォルトユーザーのパスワード初期化
以下のようにコマンドライン引数を設定するとデフォルトユーザー`admin`のパスワードを初期化(初期値: `admin`)できます。
パスワードを忘れた場合にご利用ください。

```
# 設定例
$ java -jar -Dbloginner.reset-admin-password=true bloginner-1.0.0.war
```

#### 自分で作成したビューの導入
`bloginner.war`と同じ階層に`html`フォルダを作り、中に`index.html`と関連ファイルを入れてください。
`html/index.html`が存在する場合、Bloginnerは自動的に配置したファイルでビューを上書きします。

```
|--bloginner.war
|--html
   |--index.html
   |--(style.css等)
```

#### 【Windows非推奨】Dockerによる自動セットアップ
この方法にはDocker及びDocker Composeの導入が必要です。
Dockerで自動セットアップを行うには、`docker/bloginner`フォルダに`bloginner.war`を配置して、
`docker`フォルダで`docker-compose up`コマンドを行ってください。

```
$ mv /path/to/bloginner.war docker/bloginner/
$ cd docker/bloginner
$ docker-compose up
```

## テストデータについて
以下のコマンドライン引数を設定して起動するとテストデータの記事とコメントが追加されます。
**新たにデータが追加されるので、引数の設定を消し忘れて2回目の起動を行わないようにご注意ください。**

```
# 設定例
$ java -jar -Dbloginner.add-dummy-data=true bloginner-1.0.0.war
```