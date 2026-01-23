# Java 2025 final assignment: 迷路自動生成・探索アルゴリズム比較可視化アプリケーション 仕様書

## 1. 概要

### 1.1 目的
クラスカル法で迷路を自動生成し、5つの探索アルゴリズム（DFS、BFS、A*、ダイクストラ法、貪欲法）を同時実行して比較可視化する。

### 1.2 技術要件
- **言語**: Java
- **GUI**: Swing (JFrame, JPanel)
- **アニメーション**: javax.swing.Timer
- **設計**: MVCモデル（最低3クラス、public/staticフィールド禁止）

---

## 2. システム構成

### 2.1 動作フロー
1. クラスカル法で迷路生成
2. 5つのアルゴリズムを同時に1ステップずつ実行
3. 各アルゴリズムの探索過程を並列表示
4. 完了後、統計情報を比較

---

## 3. Model層

### 3.1 Mazeクラス
**責務**: 迷路データの保持

**フィールド**:
- `int width, height`: 迷路サイズ
- `int[][] cells`: 迷路配列（0=通路、1=壁）
- `Point start, goal`: スタート/ゴール座標

**メソッド**:
- コンストラクタ、各種ゲッター/セッター
- `boolean isWall(int x, int y)`: 壁判定
- `boolean isInBounds(int x, int y)`: 範囲内判定

---

### 3.2 MazeGeneratorクラス
**責務**: クラスカル法による迷路生成

**アルゴリズム（クラスカル法）**:
1. 全セルを壁で初期化、各セルを独立した集合とする
2. 全ての壁（エッジ）のリストを作成
3. 壁リストをシャッフル
4. 各壁について:
    - 壁の両側のセルが異なる集合なら壁を削除し、集合を統合
    - 同じ集合なら何もしない
5. 全セルが1つの集合になるまで繰り返す
6. スタートとゴールをランダム配置

**フィールド**:
- `Maze maze`
- `Random random`
- `UnionFind unionFind`: 素集合データ構造（Union-Find木）

**メソッド**:
- `Maze generate(int width, int height)`: 迷路生成

**UnionFindクラス（内部クラスまたは別クラス）**:
- `int find(int x)`: 根を探す
- `void union(int x, int y)`: 集合を統合
- `boolean isSame(int x, int y)`: 同じ集合か判定

---

### 3.3 Searcherインターフェース
**共通メソッド**:
- `void initialize()`: 初期化
- `boolean step()`: 1ステップ実行（継続中=true、完了=false）
- `boolean isCompleted()`: 完了判定
- `List<Point> getVisitedCells()`: 訪問済みセル取得
- `Point getCurrentPosition()`: 現在位置取得
- `List<Point> getFinalPath()`: 最終経路取得
- `Statistics getStatistics()`: 統計情報取得

---

### 3.4 DFSSearcherクラス
**アルゴリズム**: 深さ優先探索

**フィールド**:
- `Maze maze`
- `Stack<Point> stack`
- `Set<Point> visited`
- `Map<Point, Point> parentMap`
- `Point current`
- `boolean completed`
- `List<Point> finalPath`
- `Statistics statistics`

**step()の動作**:
1. スタックからポップ → 現在位置
2. ゴール到達なら経路復元して終了
3. 未訪問の隣接セル（上右下左）をスタックにプッシュ

**特徴**: 行き止まりまで進む、最短保証なし

---

### 3.5 BFSSearcherクラス
**アルゴリズム**: 幅優先探索

**フィールド**:
- `Maze maze`
- `Queue<Point> queue`
- `Set<Point> visited`
- `Map<Point, Point> parentMap`
- その他DFSと同様

**step()の動作**:
1. キューからデキュー → 現在位置
2. ゴール到達なら経路復元して終了
3. 未訪問の隣接セルをキューにエンキュー

**特徴**: 波紋状に広がる、最短経路保証

---

### 3.6 AStarSearcherクラス
**アルゴリズム**: A*探索

**フィールド**:
- `Maze maze`
- `PriorityQueue<Node> openSet`: f値でソート
- `Set<Point> closedSet`
- `Map<Point, Integer> gScore`: スタートからの実コスト
- `Map<Point, Point> parentMap`
- その他

**内部クラス Node**:
- `Point position`
- `int f, g, h`: f=g+h

**step()の動作**:
1. openSetからf値最小のノードを取り出し
2. ゴール到達なら経路復元して終了
3. 隣接セルのg値、h値（マンハッタン距離）、f値を計算してopenSetに追加

**ヒューリスティック**: `h = |x1-x2| + |y1-y2|`

**特徴**: ゴールに向かって賢く探索、最短経路保証

---

### 3.7 DijkstraSearcherクラス
**アルゴリズム**: ダイクストラ法

**フィールド**:
- `Maze maze`
- `PriorityQueue<Node> queue`: コスト(g値)でソート
- `Set<Point> visited`
- `Map<Point, Integer> distance`: 各セルまでの最短距離
- `Map<Point, Point> parentMap`
- その他

**内部クラス Node**:
- `Point position`
- `int cost`: スタートからのコスト

**step()の動作**:
1. queueからコスト最小のノードを取り出し
2. ゴール到達なら経路復元して終了
3. 隣接セルの距離を更新（現在のコスト+1）、queueに追加

**特徴**: A*のヒューリスティックなし版、最短経路保証、全方位に均等に探索

---

### 3.8 GreedySearcherクラス
**アルゴリズム**: 貪欲法（Greedy Best-First Search）

**フィールド**:
- `Maze maze`
- `PriorityQueue<Node> openSet`: h値（ヒューリスティック）のみでソート
- `Set<Point> visited`
- `Map<Point, Point> parentMap`
- その他

**内部クラス Node**:
- `Point position`
- `int h`: ゴールまでのヒューリスティック推定値

**step()の動作**:
1. openSetからh値最小のノードを取り出し
2. ゴール到達なら経路復元して終了
3. 隣接セルのh値（マンハッタン距離）を計算してopenSetに追加

**ヒューリスティック**: `h = |x1-x2| + |y1-y2|`

**特徴**: ゴールに最も近いセルを優先、高速だが最短経路保証なし

---

### 3.9 Statisticsクラス
**責務**: 統計情報の保持

**フィールド**:
- `String algorithmName`
- `int stepCount`: ステップ数
- `int visitedCellCount`: 訪問セル数
- `int pathLength`: 経路長
- `boolean completed`: 完了フラグ

**メソッド**:
- コンストラクタ、各種ゲッター/セッター
- `void incrementStepCount()`
- `double getEfficiency()`: 効率計算（pathLength / visitedCellCount）

---

## 4. View層

### 4.1 MazePanelクラス
**責務**: 1つの迷路と探索過程を描画

**継承**: `extends JPanel`

**フィールド**:
- `Maze maze`
- `Searcher searcher`
- `int cellSize`: 1セルのサイズ（20px推奨）
- `String title`: アルゴリズム名

**メソッド**:
- コンストラクタ、セッター
- `@Override void paintComponent(Graphics g)`:
    1. タイトル描画
    2. 迷路の各セルを描画（壁=黒、通路=白）
    3. 訪問済みセル=薄い青
    4. 現在位置=黄色
    5. スタート=緑、ゴール=赤
    6. 最終経路=太い緑線
    7. 統計情報をテキスト表示

**色定数**:
- `WALL_COLOR = Color.BLACK`
- `VISITED_COLOR = new Color(200, 200, 255)`
- `CURRENT_COLOR = Color.YELLOW`
- `START_COLOR = Color.GREEN`
- `GOAL_COLOR = Color.RED`
- `FINAL_PATH_COLOR = new Color(0, 200, 0)`

---

### 4.2 ComparisonPanelクラス
**責務**: 5つのMazePanelを配置

**継承**: `extends JPanel`

**フィールド**:
- `List<MazePanel> mazePanels`

**メソッド**:
- コンストラクタ（GridLayout設定）
- `void addMazePanel(MazePanel panel)`
- `void updateAll()`: 全パネルrepaint

**レイアウト**: `GridLayout(1, 5)` または `GridLayout(5, 1)`

---

### 4.3 ControlPanelクラス
**責務**: 操作UI

**継承**: `extends JPanel`

**フィールド**:
- `JButton generateButton, startButton, pauseButton, resetButton`
- `JSlider speedSlider`
- `JLabel statusLabel`

**メソッド**:
- コンストラクタ（各コンポーネント配置）
- 各ゲッター
- `void setStatusText(String text)`

---

## 5. Controller層

### 5.1 MainFrameクラス
**責務**: 全体制御、タイマー管理

**継承**: `extends JFrame`

**フィールド**:
- `Maze maze`
- `MazeGenerator generator`
- `List<Searcher> searchers`: 5つのアルゴリズム
- `ComparisonPanel comparisonPanel`
- `ControlPanel controlPanel`
- `Timer timer`
- `int timerDelay`: 初期値100ms
- `boolean isPaused`

**メソッド**:
- `MainFrame()`: 初期化、レイアウト設定、イベント登録
- `void generateNewMaze()`: 迷路生成、Searcher初期化
- `void startSearch()`: 探索開始、タイマー開始
- `void pauseSearch()`: 一時停止
- `void resetSearch()`: リセット
- `void updateSpeed(int speed)`: 速度変更
- `void onTimerTick()`: 各Searcherのstep()呼び出し、repaint

**イベントリスナー**:
- generateButton → `generateNewMaze()` → `startSearch()`
- pauseButton → `pauseSearch()`
- resetButton → `resetSearch()`
- speedSlider → `updateSpeed()`
- timer → `onTimerTick()`

**mainメソッド**:
```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    });
}
```

---

## 6. 推奨パラメータ

- **迷路サイズ**: 25 x 25（デフォルト）
- **セルサイズ**: 20px
- **タイマー遅延**: 100ms（スライダーで10-500ms調整可）
- **フレームサイズ**: 横並び=2000x600、縦並び=500x2500

---

## 7. アルゴリズム比較表

| アルゴリズム | データ構造 | 最短保証 | 探索特性 | 訪問セル数 |
|------------|----------|---------|---------|-----------|
| DFS | Stack | × | 行き止まりまで進む | 多い |
| BFS | Queue | ○ | 波紋状に広がる | 中程度 |
| A* | PriorityQueue(f値) | ○ | ゴールに向かって賢く | 少ない |
| ダイクストラ | PriorityQueue(g値) | ○ | 全方位に均等 | 中程度 |
| 貪欲法 | PriorityQueue(h値) | × | ゴールに直進 | 最少 |

---

## 8. クラス一覧

### Model層
- `Maze.java`
- `MazeGenerator.java`
- `UnionFind.java`（MazeGenerator内部でも可）
- `Searcher.java`（インターフェース）
- `DFSSearcher.java`
- `BFSSearcher.java`
- `AStarSearcher.java`
- `DijkstraSearcher.java`
- `GreedySearcher.java`
- `Statistics.java`

### View層
- `MazePanel.java`
- `ComparisonPanel.java`
- `ControlPanel.java`

### Controller層
- `MainFrame.java`（mainメソッド含む）

---

## 9. 実装順序

1. Maze、MazeGenerator（クラスカル法）、UnionFind実装
2. MazePanel実装、迷路描画テスト
3. BFSSearcher実装、1アルゴリズムでアニメーション確認
4. 残り4つのSearcher実装
5. ComparisonPanel、MainFrame実装、5並列表示
6. ControlPanel実装、UI機能追加
7. 統計情報表示、調整

---

## 10. 重要ポイント

### クラスカル法のポイント
- Union-Find木で効率的に集合管理
- 壁をランダムに削除して迷路生成
- 必ず連結な迷路が生成される

### 各アルゴリズムの違い
- **DFS/BFS**: 基本的な探索、データ構造のみ異なる
- **A***: f=g+h でゴール方向を優先
- **ダイクストラ**: A*のh=0版、全方位探索
- **貪欲法**: A*のg=0版、ヒューリスティックのみ

### MVCの分離
- Model: 迷路データと探索ロジック（描画なし）
- View: 描画のみ（ビジネスロジックなし）
- Controller: タイマーでModelを更新、Viewを再描画

---

以上