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
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameScreen extends Screen {
	private long m_lastTime;
	private int m_lastTerritorySize;
	private long m_lastScore;
	private Text m_scoreText;
	private Text m_lifeCountText;
	private Text m_remainingTimeText;
	private Text m_notificationText1;
	private Text m_notificationText2;
	private Canvas m_canvas;
	private HashMap<Sprite, SpriteDrawer> m_spriteDrawers;
	private TerritoryDrawer m_territoryDrawer;
	private ExpeditionLineDrawer m_expeditionLineDrawer;
	private int[] m_argbs;
	private HashSet<KeyCode> m_pressedKeys;
	private Timeline m_timeline;

	public GameScreen() {
		m_lastTime = System.currentTimeMillis();
		m_lastTerritorySize = GameContext.getTerritory().getSize();
		m_lastScore = GameContext.getScore();

		initializeScreen();
		initializeDrawers();
		initializeTimeline();
	}

	private void initializeScreen() {
		getChildren().add(new VBox(createInformationPane(), createPartitionPane(), createFieldPane()));
	}

	private Node createInformationPane() {
		final Text[][] texts = {
				{ createText("SCORE"), createText(Long.toString(GameContext.getScore())) },
				{ createText("LIFE"), createText(Integer.toString(GameContext.getLifeCount())) },
				{ createText("LEVEL"), createText(Integer.toString(GameContext.getLevelNumber())) },
				{ createText("TIME"), createText(Long.toString(GameContext.getRemainingTime())) }
		};
		final int width = texts.length;
		final int height = texts[0].length;

		GridPane gridPane = new GridPane();
		gridPane.setPrefSize(Configuration.STATUS_BAR_WIDTH, Configuration.STATUS_BAR_HEIGHT);
		gridPane.setStyle("-fx-background-color: navy;");
		IntStream.range(0, height).forEach(y -> {
			IntStream.range(0, width).forEach(x -> {
				gridPane.add(texts[x][y], x, y);
				GridPane.setHalignment(texts[x][y], HPos.CENTER);
			});
		});
		IntStream.range(0, width).forEach(x -> {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth((double) 100 / width);
			gridPane.getColumnConstraints().add(columnConstraints);
		});
		IntStream.range(0, height).forEach(y -> {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight((double) 100 / height);
			gridPane.getRowConstraints().add(rowConstraints);
		});

		m_scoreText = texts[0][1];
		m_lifeCountText = texts[1][1];
		m_remainingTimeText = texts[3][1];

		return new Group(gridPane);
	}

	private Node createPartitionPane() {
		Pane pane = new Pane();
		pane.setPrefSize(Configuration.PARTITION_WIDTH, Configuration.PARTITION_HEIGHT);
		pane.setStyle("-fx-background-color: slateblue;");

		return new Group(pane);
	}

	private Node createFieldPane() {
		m_canvas = new Canvas(Configuration.FIELD_WIDTH, Configuration.FIELD_HEIGHT);

		m_notificationText1 = createText("", 100);
		m_notificationText2 = createText("", 30);

		VBox vBox = new VBox(m_notificationText1, m_notificationText2);
		vBox.setAlignment(Pos.CENTER);

		return new Group(new StackPane(m_canvas, new Group(vBox)));
	}

	private void initializeDrawers() {
		m_spriteDrawers = new HashMap<>();
		m_spriteDrawers.put(GameContext.getPlayer(), new PlayerDrawer(GameContext.getPlayer()));
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
		m_territoryDrawer = new TerritoryDrawer(GameContext.getTerritory());
		m_expeditionLineDrawer = null;

		m_argbs = new int[Configuration.FIELD_WIDTH * Configuration.FIELD_HEIGHT];
	}

	private void moveSprites() {
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

		GameContext.getEnemies().forEach(enemy -> enemy.move());
	}

	private void killEnemiesOnTerritory() {
		// remove 操作を行うので，filter と forEach の間に collect を挟む。
		GameContext.getEnemies().stream()
				.filter(enemy -> GameContext.getTerritory().isTerritory(enemy.getPosition()))
				.collect(Collectors.toList())
				.forEach(enemy -> {
					enemy.setLive(false);
					GameContext.removeEnemy(enemy);
					m_spriteDrawers.remove(enemy);

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

	private void killIsolatedPlayer() {
		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (expeditionLine != null) {
			Optional<Enemy> optionalEnemy = GameContext.getEnemies().stream()
					.filter(e -> expeditionLine.getPoints().contains(e.getPosition()))
					.findFirst();
			optionalEnemy.ifPresent(enemy -> {
				GameContext.getPlayer().setLive(false);
				expeditionLine.setLive(false);
			});
		}
	}

	private void updateInformation() {
		long currentTime = System.currentTimeMillis();
		if (m_lastTime < currentTime) {
			GameContext.subtractRemainingTime(currentTime - m_lastTime);
			m_lastTime = currentTime;
		}

		int territorySize = GameContext.getTerritory().getSize();
		if (m_lastTerritorySize < territorySize) {
			GameContext.addScore((territorySize - m_lastTerritorySize) * Configuration.SCORE_PER_DOT);
			m_lastTerritorySize = territorySize;
		}

		long score = GameContext.getScore();
		if (m_lastScore < score) {
			if ((m_lastScore / Configuration.SCORE_PER_LIFE) != (score / Configuration.SCORE_PER_LIFE)) {
				GameContext.increaseLifeCount();
			}
			m_lastScore = score;
		}
	}

	private void initializeTimeline() {
		m_pressedKeys = new HashSet<>();
		setOnKeyPressed(event -> {
			m_pressedKeys.add(event.getCode());
		});
		setOnKeyReleased(event -> {
			m_pressedKeys.remove(event.getCode());
		});
		setFocusTraversable(true);

		KeyFrame keyFrame = new KeyFrame(Configuration.FRAME_PER_SECOND, event -> {
			switch (GameContext.getState()) {
			case PLAYING:
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
				updateInformation();
				if (GameContext.getState() != GAME_OVER && GameContext.getRemainingTime() <= 0) {
					GameContext.setState(TIME_UP);
				}
				break;
			case LEVEL_CLEAR:
				if (m_pressedKeys.contains(KeyCode.SPACE)) {
					Main.changeScreen(GameContext.getLevelNumber() + 1);
				}
				m_pressedKeys.clear();
				break;
			case TIME_UP:
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
				if (m_pressedKeys.contains(KeyCode.SPACE)) {
					Main.changeScreen(0);
				}
				m_pressedKeys.clear();
				break;
			}

			updateScreen();
		});
		m_timeline = new Timeline();
		m_timeline.setCycleCount(Timeline.INDEFINITE);
		m_timeline.getKeyFrames().add(keyFrame);
	}

	public void start() {
		m_timeline.play();
	}

	public void stop() {
		m_timeline.stop();
	}

	public void updateScreen() {
		final int width = Configuration.FIELD_WIDTH;
		final int height = Configuration.FIELD_HEIGHT;

		m_scoreText.setText(Long.toString(GameContext.getScore()));
		m_lifeCountText.setText(Integer.toString(GameContext.getLifeCount()));
		m_remainingTimeText.setText(Long.toString(GameContext.getRemainingTime() / 1_000));

		// 0xff000000 : Color.BLACK
		Arrays.fill(m_argbs, 0xff000000);

		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (expeditionLine != null) {
			if (m_expeditionLineDrawer == null) {
				m_expeditionLineDrawer = new ExpeditionLineDrawer(expeditionLine);
			}
			m_expeditionLineDrawer.draw(m_argbs, width, height);
		} else {
			m_expeditionLineDrawer = null;
		}

		m_territoryDrawer.draw(m_argbs, width, height);

		GraphicsContext gc = m_canvas.getGraphicsContext2D();
		gc.getPixelWriter().setPixels(0, 0, width, height, WritablePixelFormat.getIntArgbPreInstance(), m_argbs, 0, width);

		m_spriteDrawers.values().forEach(spriteDrawer -> spriteDrawer.draw(gc));

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
