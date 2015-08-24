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

import java.util.stream.IntStream;

import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * リザルト画面である。
 */
public class ResultScreen extends Screen {
	/**
	 * リザルト画面を生成する。
	 */
	public ResultScreen() {
		// スペースキー押下時にタイトル画面に切り替えるようにする。
		setOnKeyTyped(event -> {
			// KeyTyped イベントの場合は KeyCode を得られないので，Character で判定する。
			if (event.getCharacter().equals(" ")) {
				Main.changeScreen(0);
			}
		});
		setFocusTraversable(true);

		// ゲーム情報の最終値を取得する。
		long score = GameContext.getScore();
		long lifeBonus = GameContext.getLifeCount() * Configuration.SCORE_PER_LIFE;
		GameContext.addScore(lifeBonus);
		long totalScore = GameContext.getScore();

		// ランクを計算する。
		String rank;
		if (totalScore >= Configuration.SCORE_BORDER_OF_RANK_S) {
			rank = "S";
		} else if (totalScore >= Configuration.SCORE_BORDER_OF_RANK_A) {
			rank = "A";
		} else if (totalScore >= Configuration.SCORE_BORDER_OF_RANK_B) {
			rank = "B";
		} else if (totalScore >= Configuration.SCORE_BORDER_OF_RANK_C) {
			rank = "C";
		} else if (totalScore >= Configuration.SCORE_BORDER_OF_RANK_D) {
			rank = "D";
		} else {
			rank = "E";
		}

		// 画面に表示するテキストを生成する。
		Text resultCaptionText = createText("CONGRATULATION!!", 100, Color.GREENYELLOW);
		Text scoreCaptionText = createText("SCORE", 50, Color.GREENYELLOW);
		Text scoreText = createText(Long.toString(score), "monospace", 50, Color.GREENYELLOW);
		Text lifeBonusCaptionText = createText("LIFE BONUS", 50, Color.GREENYELLOW);
		Text lifeBonusText = createText(Long.toString(lifeBonus), "monospace", 50, Color.GREENYELLOW);
		Text totalScoreCaptionText = createText("TOTAL SCORE", 50, Color.GREENYELLOW);
		Text totalScoreText = createText(Long.toString(totalScore), "monospace", 50, Color.GREENYELLOW);
		Text rankCaptionText = createText("RANK", 50, Color.GREENYELLOW);
		Text rankText = createText(rank, 50, Color.GREENYELLOW);

		// 区切り線を生成する。
		VBox[] partitionPanes = new VBox[3];
		IntStream.range(0, partitionPanes.length).forEach(i -> {
			partitionPanes[i] = new VBox();
			partitionPanes[i].setPrefHeight(Configuration.PARTITION_HEIGHT);
			partitionPanes[i].setStyle("-fx-background-color: greenyellow;");
		});

		// テキスト，および区切り線をグリッドペインに配置する。
		GridPane gridPane = new GridPane();
		GridPane.setHalignment(resultCaptionText, HPos.CENTER);
		GridPane.setHalignment(scoreText, HPos.RIGHT);
		GridPane.setHalignment(lifeBonusText, HPos.RIGHT);
		GridPane.setHalignment(totalScoreText, HPos.RIGHT);
		GridPane.setHalignment(rankText, HPos.CENTER);
		GridPane.setHgrow(scoreCaptionText, Priority.ALWAYS);
		gridPane.add(resultCaptionText, 0, 0, 2, 1);
		gridPane.add(partitionPanes[0], 0, 1, 2, 1);
		gridPane.add(scoreCaptionText, 0, 2);
		gridPane.add(scoreText, 1, 2);
		gridPane.add(lifeBonusCaptionText, 0, 3);
		gridPane.add(lifeBonusText, 1, 3);
		gridPane.add(partitionPanes[1], 0, 4, 2, 1);
		gridPane.add(totalScoreCaptionText, 0, 5);
		gridPane.add(totalScoreText, 1, 5);
		gridPane.add(rankCaptionText, 0, 6);
		gridPane.add(rankText, 1, 6);
		gridPane.add(partitionPanes[2], 0, 7, 2, 1);

		// グリッドペインをスタックペインに配置する。
		// こうすることで，グリッドペインが画面の中央に配置される。
		StackPane stackPane = new StackPane(new Group(gridPane));
		stackPane.setPrefSize(Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT);
		stackPane.setStyle("-fx-background-color: black;");

		// 画面にスタックペインを配置する。
		getChildren().add(stackPane);
	}

}
