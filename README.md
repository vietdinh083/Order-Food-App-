
## 🍱 Order Food App – プロジェクト紹介

### 🔹 概要（Overview）

**Order Food App** は、ユーザーがスマートフォンから簡単にレストランの料理を注文できる **フードデリバリーアプリ** です。
Kotlin と Firebase を使用して開発されており、**リアルタイム注文管理・メニュー閲覧・チャットボット連携** など、実際のサービスに近い機能を体験できます。

このアプリは、モバイルアプリ開発の学習を目的として作成され、**クリーンなUI設計とFirebase連携の実装力** を示すポートフォリオプロジェクトです。

---

### 🧩 主な機能（Main Features）

* 🍔 **レストランメニュー表示**：Firebaseからデータを取得してメニューを動的に表示
* 🛒 **カート機能**：注文した商品をカートに追加・削除
* 💳 **注文確認ページ**：注文内容の確認と送信
* 💬 **チャットボット機能**：Gemini API と連携したAIチャットサポート（注文サポートなど）
* 🔥 **Firebase連携**：Realtime Database / Authentication / Storage を活用
* 🎨 **Material Designに基づいたUI**

---

### 🛠️ 技術スタック（Tech Stack）

| カテゴリ    | 使用技術                                         |
| ------- | -------------------------------------------- |
| 言語      | Kotlin                                       |
| 開発環境    | Android Studio 2025.1.3 (Koala Feature Drop) |
| データベース  | Firebase Realtime ,Room Database                   |
| 認証      | Firebase Authentication                      |
| ストレージ   | Firebase Storage                           |
| AI連携    | Google Gemini API                            |


---

## ⚙️ セットアップ手順（Setup Guide）

### ✅ 必要環境

* Android Studio **2025.1.3 以降**
* Java SDK **17 以上**
* Android SDK **34 (Android 14)**
* Firebase アカウント

---

### 🪜 インストール手順

1. **GitHubからプロジェクトをクローン**

   ```bash
   git clone https://github.com/vietdinh083/Order-Food-App-.git
   ```

2. **Android Studioを開く**

   * 「Open an Existing Project」からクローンしたフォルダを選択

3. **Firebase設定ファイルを追加**

   * Firebase Consoleで新しいAndroidプロジェクトを作成
   * `google-services.json` をダウンロードして
     → `app/` フォルダ内に配置

4. **Gemini APIキーの設定（任意）**

   * `BuildConfig.kt` にAPIキーを設定
   * セキュリティのため、`.gitignore` に追加してGitHubに公開しないようにしてください

5. **依存関係の同期**

   * メニューの **「File > Sync Project with Gradle Files」** を実行

6. **アプリを実行**

   * エミュレーターまたは実機を接続
   * 「Run ▶」 ボタンを押して起動

---

### 📱 実行結果イメージ（Expected Behavior）

* 起動後、メニュー一覧画面が表示されます
* 商品を選択 → カートに追加 → 注文確認ページへ遷移
* Firebaseに注文データがリアルタイムで登録されます

---

### 📂 フォルダ構成（Main Folder Structure）

```
Order-Food-App-/
 ├── app/
 │   ├── src/
 │   │   ├── main/
 │   │   │   ├── java/com/example/orderfood/
 │   │   │   │   ├── ui/       # Activity / Fragment
 │   │   │   │   ├── model/    # データモデル
 │   │   │   │   ├── adapter/  # RecyclerViewアダプタ
 │   │   │   │   ├── ChatBotPackage/ # Gemini連携用コード
 │   │   │   ├── res/          # レイアウト・画像
 │   │   ├── AndroidManifest.xml
 │   ├── build.gradle
 ├── build.gradle
 └── README.md
```

---

### 💡 今後の改善案（Future Improvements）

* ユーザーアカウントの履歴管理
* 管理者用の注文確認パネル追加
* クーポンやレビュー機能の実装
* UIの多言語化対応（英語・日本語）

---

### 🧑‍💻 作者（Author）

**Dinh Quoc Viet**
モバイルアプリ開発者志望。
Android Studio と Kotlin を中心に、Firebase・AI連携アプリを開発中。

---

