/*
 * Copyright 2015 calotocen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package canal;

import static canal.GameContext.State.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * ゲーム画面である。
 */
public class GameScreen extends Screen {
	/** 前回のゲーム情報更新時の時間 */
	private long m_lastTime;

	/** 前回のゲーム情報更新時の領地サイズ */
	private int m_lastTerritorySize;

	/** 前回のゲーム情報更新時のスコア */
	private long m_lastScore;

	/** スコア表示用テキストノード */
	private Text m_scoreText;

	/** ライフ数表示用テキストノード */
	private Text m_lifeCountText;

	/** 残り時間表示用テキストノード */
	private Text m_remainingTimeText;

	/** メッセージ表示用テキストノード */
	private Text m_notificationText1;

	/** メッセージ表示用テキストノード */
	private Text m_notificationText2;

	/** キャンバスノード */
	private Canvas m_canvas;

	/** スプライト描画者用マップ */
	private HashMap<Sprite, SpriteDrawer> m_spriteDrawers;

	/** 領地描画者 */
	private TerritoryDrawer m_territoryDrawer;

	/** 遠征線描画者 */
	private ExpeditionLineDrawer m_expeditionLineDrawer;

	/** キャンバス用ビットマップ */
	private int[] m_argbs;

	/** キャンバス用ビットマップ */
	private byte[] m_pixels;

	/** 押下キーマップ */
	private HashSet<KeyCode> m_pressedKeys;

	/** タイムライン */
	private Timeline m_timeline;

	/**
	 * ゲーム画面を生成する。
	 */
	public GameScreen() {
		m_lastTime = System.currentTimeMillis();
		m_lastTerritorySize = GameContext.getTerritory().getSize();
		m_lastScore = GameContext.getScore();

		initializeScreen();
		initializeDrawers();
		initializeTimeline();
	}

	/**
	 * ゲーム画面を初期化する。
	 */
	private void initializeScreen() {
		// 上段にゲーム情報ペイン，下段にフィールドペインを配置し，その間を区切りペインで区切る。
		getChildren().add(new VBox(createInformationPane(), createPartitionPane(), createFieldPane()));
	}

	/**
	 * ゲーム情報ペインを生成する。
	 *
	 * @return ゲーム情報ペイン。
	 */
	private Node createInformationPane() {
		final Text[][] texts = {
				{ createText("SCORE"), createText(Long.toString(GameContext.getScore())) },
				{ createText("LIFE"), createText(Integer.toString(GameContext.getLifeCount())) },
				{ createText("LEVEL"), createText(Integer.toString(GameContext.getLevelNumber())) },
				{ createText("TIME"), createText(Long.toString(GameContext.getRemainingTime())) }
		};
		final int width = texts.length;
		final int height = texts[0].length;

		// グリッドペインを生成し，推奨サイズ，およびスタイルを設定する。
		GridPane gridPane = new GridPane();
		gridPane.setPrefSize(Configuration.STATUS_BAR_WIDTH, Configuration.STATUS_BAR_HEIGHT);
		gridPane.setStyle("-fx-background-color: navy;");

		// グリッドペインにテキストノードを配置する。
		IntStream.range(0, height).forEach(y -> {
			IntStream.range(0, width).forEach(x -> {
				gridPane.add(texts[x][y], x, y);
				GridPane.setHalignment(texts[x][y], HPos.CENTER);
			});
		});

		// グリッドペインの列制約 (横幅) を設定する。
		IntStream.range(0, width).forEach(x -> {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth((double) 100 / width);
			gridPane.getColumnConstraints().add(columnConstraints);
		});

		// グリッドペインの行制約 (縦幅) を設定する。
		IntStream.range(0, height).forEach(y -> {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight((double) 100 / height);
			gridPane.getRowConstraints().add(rowConstraints);
		});

		// 更新する必要のあるテキストノードを記憶する。
		m_scoreText = texts[0][1];
		m_lifeCountText = texts[1][1];
		m_remainingTimeText = texts[3][1];

		// 生成したグリッドペインを返す。
		return new Group(gridPane);
	}

	/**
	 * 区切りペインを生成する。
	 *
	 * @return 区切りペイン。
	 */
	private Node createPartitionPane() {
		Pane pane = new Pane();
		pane.setPrefSize(Configuration.PARTITION_WIDTH, Configuration.PARTITION_HEIGHT);
		pane.setStyle("-fx-background-color: slateblue;");

		return new Group(pane);
	}

	/**
	 * フィールドペインを生成する。
	 *
	 * @return フィールドペイン。
	 */
	private Node createFieldPane() {
		// キャンバスを生成する。
		m_canvas = new Canvas(Configuration.FIELD_WIDTH, Configuration.FIELD_HEIGHT);

		// メッセージ表示用テキストを生成する。
		m_notificationText1 = createText("", 100);
		m_notificationText2 = createText("", 30);

		// メッセージ表示用テキストを縦に並べる。
		VBox vBox = new VBox(m_notificationText1, m_notificationText2);
		vBox.setAlignment(Pos.CENTER);

		// 生成したフィールドペインを返す。
		return new Group(new StackPane(m_canvas, new Group(vBox)));
	}

	/**
	 * スプライト描画者を初期化する。
	 */
	private void initializeDrawers() {
		// スプライト描画者用マップを生成する。
		m_spriteDrawers = new HashMap<>();

		// 自機描画者を設定する。
		m_spriteDrawers.put(GameContext.getPlayer(), new PlayerDrawer(GameContext.getPlayer()));

		// 敵描画者を設定する。
		GameContext.getEnemies().forEach(enemy -> {
			if (enemy instanceof TriangleEnemy) {
				m_spriteDrawers.put(enemy, new TriangleEnemyDrawer((TriangleEnemy) enemy));
			} else if (enemy instanceof SquareEnemy) {
				m_spriteDrawers.put(enemy, new SquareEnemyDrawer((SquareEnemy) enemy));
			} else if (enemy instanceof BigTriangleEnemy) {
				m_spriteDrawers.put(enemy, new BigTriangleEnemyDrawer((BigTriangleEnemy) enemy));
			} else if (enemy instanceof BigSquareEnemy) {
				m_spriteDrawers.put(enemy, new BigSquareEnemyDrawer((BigSquareEnemy) enemy));
			} else if (enemy instanceof BigPentagonEnemy) {
				m_spriteDrawers.put(enemy, new BigPentagonEnemyDrawer((BigPentagonEnemy) enemy));
			}
		});

		// 領地描画者を設定する。
		m_territoryDrawer = new TerritoryDrawer(GameContext.getTerritory());

		// 遠征線描画者は遠征線がある場合のみ設定するので，ここでは null を設定する。
		m_expeditionLineDrawer = null;

		// キャンバス用ビットマップを初期化する。
		m_argbs = new int[Configuration.FIELD_WIDTH * Configuration.FIELD_HEIGHT];
		m_pixels = new byte[Configuration.FIELD_WIDTH * Configuration.FIELD_HEIGHT * 4];
	}

	/**
	 * スプライトを移動する。
	 */
	private void moveSprites() {
		// 自機を移動する。
		Direction direction = Direction.NONE;
		boolean expedition = false;
		if (m_pressedKeys.contains(KeyCode.LEFT)) {
			direction = Direction.LEFT;
		} else if (m_pressedKeys.contains(KeyCode.RIGHT)) {
			direction = Direction.RIGHT;
		} else if (m_pressedKeys.contains(KeyCode.UP)) {
			direction = Direction.UP;
		} else if (m_pressedKeys.contains(KeyCode.DOWN)) {
			direction = Direction.DOWN;
		}
		if (m_pressedKeys.contains(KeyCode.SHIFT)) {
			expedition = true;
		}
		GameContext.getPlayer().move(direction, expedition);

		// 敵を移動する。
		GameContext.getEnemies().forEach(enemy -> enemy.move());
	}

	/**
	 * 領地上の敵を除去する。
	 */
	private void killEnemiesOnTerritory() {
		GameContext.getEnemies().stream()
				// すべての敵から領地上にある敵を探す。
				.filter(enemy -> GameContext.getTerritory().isTerritory(enemy.getPosition()))

				// 次の forEach で remove 操作を行うので，一旦 collect する。
				.collect(Collectors.toList())

				// 領地上にある敵を除去し，対応するスコアを加算する。
				.forEach(enemy -> {
					// 領地上にある敵を除去する。
					// 敵の状態を『撃墜』に変更 (Enemy.setLive(false)) しても効果はないが，作法として変更しておく。
						enemy.setLive(false);
						GameContext.removeEnemy(enemy);
						m_spriteDrawers.remove(enemy);

						// 除去した敵に対応するスコアを加算する。
						if (enemy instanceof TriangleEnemy) {
							GameContext.addScore(Configuration.SCORE_OF_TRIANGLE_ENEMY);
						} else if (enemy instanceof SquareEnemy) {
							GameContext.addScore(Configuration.SCORE_OF_SQUARE_ENEMY);
						} else if (enemy instanceof BigTriangleEnemy) {
							GameContext.addScore(Configuration.SCORE_OF_BIG_TRIANGLE_ENEMY);
						} else if (enemy instanceof BigSquareEnemy) {
							GameContext.addScore(Configuration.SCORE_OF_BIG_SQUARE_ENEMY);
						} else if (enemy instanceof BigPentagonEnemy) {
							GameContext.addScore(Configuration.SCORE_OF_BIG_PENTAGON_ENEMY);
						}
					});
	}

	/**
	 * 敵に遠征線を切断されていた場合，自機の状態を『撃墜』に変更する。
	 */
	private void killIsolatedPlayer() {
		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (expeditionLine != null) {
			// 遠征線上にある敵を探す。
			Optional<Enemy> optionalEnemy = GameContext.getEnemies().stream()
					.filter(enemy -> expeditionLine.getPoints().contains(enemy.getPosition()))
					.findFirst();

			// 遠征線上に敵があった場合は，自機，および遠征線の状態を『撃墜』に変更する。
			optionalEnemy.ifPresent(enemy -> {
				GameContext.getPlayer().setLive(false);
				expeditionLine.setLive(false);
			});
		}
	}

	/**
	 * ゲーム情報を変更する。
	 */
	private void updateInformation() {
		// 残り時間を更新する。
		long currentTime = System.currentTimeMillis();
		if (m_lastTime < currentTime) {
			GameContext.subtractRemainingTime(currentTime - m_lastTime);
			m_lastTime = currentTime;
		}

		// 領地の拡大をスコアに反映する。
		int territorySize = GameContext.getTerritory().getSize();
		if (m_lastTerritorySize < territorySize) {
			GameContext.addScore((territorySize - m_lastTerritorySize) * Configuration.SCORE_PER_DOT);
			m_lastTerritorySize = territorySize;
		}

		// スコアが一定値に達している場合は，ライフを一つ増やす。
		long score = GameContext.getScore();
		if (m_lastScore < score) {
			if ((m_lastScore / Configuration.SCORE_PER_LIFE) != (score / Configuration.SCORE_PER_LIFE)) {
				GameContext.increaseLifeCount();
			}
			m_lastScore = score;
		}
	}

	/**
	 * タイムラインを初期化する。
	 */
	private void initializeTimeline() {
		// キーイベントを記録するためのマップを生成する。
		m_pressedKeys = new HashSet<>();

		// キーイベントリスナーを登録する。
		setOnKeyPressed(event -> {
			m_pressedKeys.add(event.getCode());
		});
		setOnKeyReleased(event -> {
			m_pressedKeys.remove(event.getCode());
		});

		// フォーカス走査サイクルにこのゲーム画面を登録する。
		// これを登録しないと画面に対するキーイベントが発生しない。
		setFocusTraversable(true);

		// キーフレームを生成する。
		// このキーフレームに登録したイベントが一定時間ごとに実行される。
		KeyFrame keyFrame = new KeyFrame(Configuration.FRAME_PER_SECOND, event -> {
			switch (GameContext.getState()) {
			case PLAYING:
				// 自機の状態が『生存』の場合
				//     - スプライトを移動する。
				//     - 領地上の敵を除外する。
				//     - 敵がいない場合は，ゲーム状態を LEVEL_CLEAR に変更し，残り時間に応じたスコアを加える。
				//     - 遠征線を切断されていた場合，自機の状態を『撃墜』に変更する。
				//
				// 自機の状態が『撃墜』の場合
				//     - 自機に遠征線をさかのぼらせる。
				//     - 自機が領地にたどり着いた場合は，ライフ数に応じて次の処理を行う。
				//         - ライフ数が 0 より大きい場合は，
				//           ライフ数を一つ減らし，自機の状態を『生存』に変更する。
				//         - 上記以外の場合は，ゲーム状態を『ゲームオーバー』に変更する。
				Player player = GameContext.getPlayer();
				if (player.isLive()) {
					moveSprites();
					killEnemiesOnTerritory();
					if (GameContext.getEnemies().isEmpty()) {
						GameContext.setState(LEVEL_CLEAR);
						GameContext.addScore(GameContext.getRemainingTime() * Configuration.SCORE_PER_MILLISECOND);
					}
					killIsolatedPlayer();
				} else {
					player.move(Direction.NONE, false);
					ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
					if (expeditionLine == null) {
						if (GameContext.getLifeCount() > 0) {
							GameContext.decreaseLifeCount();
							player.setLive(true);
						} else {
							GameContext.setState(GAME_OVER);
							m_spriteDrawers.remove(player);
						}
					}
				}

				// ゲーム情報を更新する。
				updateInformation();

				// タイムアップしている場合は，ゲーム状態を『TIME_UP』に変更する。
				if (GameContext.getState() != GAME_OVER && GameContext.getRemainingTime() <= 0) {
					GameContext.setState(TIME_UP);
				}
				break;

			case LEVEL_CLEAR:
				// スペースキーが押下されたら，次のレベルに切り替える。
				// 次のレベルがない場合は，リザルト画面を表示する。
				if (m_pressedKeys.contains(KeyCode.SPACE)) {
					Main.changeScreen(GameContext.getLevelNumber() + 1);
				}
				m_pressedKeys.clear();
				break;

			case TIME_UP:
				// スペースキーが押下されたら，ライフ数に応じて次の処理を行う。
				//     - ライフ数が 0 より大きい場合は，
				//       ライフ数を一つ減らし，レベルをやり直す。
				//     - 上記以外の場合は，ゲーム状態を『ゲームオーバー』に変更する。
				if (m_pressedKeys.contains(KeyCode.SPACE)) {
					if (GameContext.getLifeCount() > 0) {
						GameContext.decreaseLifeCount();
						Main.changeScreen(GameContext.getLevelNumber());
					} else {
						GameContext.setState(GAME_OVER);
					}
				}
				m_pressedKeys.clear();
				break;

			case GAME_OVER:
				// スペースキーが押下されたら，タイトルを表示する。
				if (m_pressedKeys.contains(KeyCode.SPACE)) {
					Main.changeScreen(0);
				}
				m_pressedKeys.clear();
				break;
			}

			// ゲーム画面の表示を更新する。
			updateScreen();
		});

		// タイムラインを生成する。
		m_timeline = new Timeline();
		m_timeline.setCycleCount(Timeline.INDEFINITE);
		m_timeline.getKeyFrames().add(keyFrame);
	}

	/**
	 * ゲーム画面を開始する。
	 */
	public void start() {
		m_timeline.play();
	}

	/**
	 * ゲーム画面を停止する。
	 */
	public void stop() {
		m_timeline.stop();
	}

	/**
	 * ゲーム画面を更新する。
	 */
	public void updateScreen() {
		final int width = Configuration.FIELD_WIDTH;
		final int height = Configuration.FIELD_HEIGHT;

		// ゲーム情報の表示を更新する。
		m_scoreText.setText(Long.toString(GameContext.getScore()));
		m_lifeCountText.setText(Integer.toString(GameContext.getLifeCount()));
		m_remainingTimeText.setText(Long.toString(GameContext.getRemainingTime() / 1_000));

		// キャンバス用ビットマップを黒 (0xff000000: Color.BLACK) で埋める。
		Arrays.fill(m_argbs, 0xff000000);

		// 遠征線がある場合は，遠征線を描画する。
		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (expeditionLine != null) {
			if (m_expeditionLineDrawer == null) {
				m_expeditionLineDrawer = new ExpeditionLineDrawer(expeditionLine);
			}
			m_expeditionLineDrawer.draw(m_argbs, width, height);
		} else {
			m_expeditionLineDrawer = null;
		}

		// 領地を描画する。
		m_territoryDrawer.draw(m_argbs, width, height);

		// キャンバスのグラフィックスコンテキストを取得する。
		GraphicsContext gc = m_canvas.getGraphicsContext2D();

		// キャンバス用ビットマップをキャンバスに反映する。
		// 本来であれば，下記のように PixelWriter.setPixels メソッドを実行すればよい。
		// ---
		//     gc.getPixelWriter().setPixels(0, 0, width, height, WritablePixelFormat.getIntArgbPreInstance(), m_argbs, 0, width);
		// ---
		//
		// しかし，setPixels メソッドは内部で width * height * 4 の大きさを持つ byte[] を new する。
		// このため，本処理のような頻繁に実行されるメソッド内で使用すると，メモリ使用量過多となる。
		// (ガベージコレクションによるメモリ領域の回収が間に合わず，フル GC が実行されてしまう)。
		// 上記問題を解決するため，private メソッドである PixelWriter.writePixelBuffer メソッドを直接実行するようにする。
		try {
			for (int i = 0, j = 0; i < width * height; i++) {
				m_pixels[j++] = (byte) (m_argbs[i]);
				m_pixels[j++] = (byte) (m_argbs[i] >> 8);
				m_pixels[j++] = (byte) (m_argbs[i] >> 16);
				m_pixels[j++] = (byte) (m_argbs[i] >> 24);
			}

			PixelWriter pixelWriter = gc.getPixelWriter();
			Method writePixelBufferMethod = pixelWriter.getClass().getDeclaredMethod("writePixelBuffer", int.class, int.class, int.class, int.class, byte[].class);
			writePixelBufferMethod.setAccessible(true);
			writePixelBufferMethod.invoke(pixelWriter, 0, 0, width, height, m_pixels);
		} catch (NoSuchMethodException exception) {
			// 通常，発生しない。
			return;
		} catch (IllegalAccessException exception) {
			// 通常，発生しない。
			return;
		} catch (IllegalArgumentException exception) {
			// 通常，発生しない。
			return;
		} catch (InvocationTargetException exception) {
			// 通常，発生しない。
			return;
		}

		// スプライトを描画する。
		m_spriteDrawers.values().forEach(spriteDrawer -> spriteDrawer.draw(gc));

		// ゲーム状態に応じてテキストを表示する。
		switch (GameContext.getState()) {
		case GAME_OVER:
			m_notificationText1.setText("GAME OVER");
			m_notificationText1.setFill(Color.RED);
			break;

		case TIME_UP:
			m_notificationText1.setText("TIME UP");
			m_notificationText1.setFill(Color.RED);
			break;

		case LEVEL_CLEAR:
			m_notificationText1.setText("LEVEL CLEAR");
			m_notificationText1.setFill(Color.BLUE);
			m_notificationText2.setText("TIME BONUS ... " + GameContext.getRemainingTime() * Configuration.SCORE_PER_MILLISECOND);
			m_notificationText2.setFill(Color.BLUE);
			break;

		default:
			// 何もしない。
		}
	}
}
