# 最热商品列表取得用API 

GET

/api/hottest

### 输入参数

### 结果Json

[hottest.json](../specs/hottest.json)

### TODO
### sample

 | 物理名 | 論理名 | 対応 RDB カラム | 説明 |
 | --- | --- | --- | --- |
 | id | ID | event.id | イベントID |
 | name | 名称 | event.name | イベントの名称 |
 | stadiumId | スタジアムID | event.stadium_id | |
 | eventDate | イベント開催日 | event.event_date | [標準 DateFormat](README.md#apiで利用するフォーマット) |
 | eventMenus.id | 紐付けID | event_menu.id | eventMenus以下は配列 |
 | eventMenus.menu.id | メニューID | event_menu.menu_id | menu以下はオブジェクト |
 | eventMenus.menu.name | メニュー名称 | menu.name | |
 | eventMenus.menu.description | 説明 | menu.description | メニューの説明 |
 | eventMenus.menu.logoFileUrl | ロゴファイルのファイルURL | menu.logo_file_path | menu.logo_file_pathをもとに表示可能なURLにしたもの |
 | eventMenus.menu.price | メニュー定価 | menu.price | Long | |
 | eventMenus.menu.createdAt | 作成日時 | menu.created_at | [標準 DateTimeFormat](README.md#apiで利用するフォーマット) | |
 | eventMenus.menu.updatedAt | 更新日時 | menu.updated_at| [標準 DateTimeFormat](README.md#apiで利用するフォーマット) | |
 | eventMenus.menu.versionNo | バージョン | menu.version_no | Long | |
 | createdAt | 作成日時 | event.created_at | [標準 DateTimeFormat](README.md#apiで利用するフォーマット) |
 | updatedAt | 更新日時 | event.updated_at|[標準 DateTimeFormat](README.md#apiで利用するフォーマット) |
 | versionNo | バージョン | event.version_no | |



