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

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * タイトル画面である。
 */
public class TitleScreen extends Screen {
	/**
	 * タイトル画面を生成する。
	 */
	public TitleScreen() {
		// スペースキー押下時にタイトル画面に切り替えるようにする。
		setFocusTraversable(true);

		// 画面に表示するテキストを生成する。
		Text titleText = createText("Canal", 15, Color.GREENYELLOW);

		// テキストをスタックペインに配置する。
		StackPane stackPane = new StackPane(titleText);
		stackPane.setPrefSize(Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT);
		stackPane.setStyle("-fx-background-color: black;");

		// スペースキー押下時にタイトル画面に切り替えるようにする。
		setOnKeyTyped(event -> {
			if (event.getCharacter().equals(" ")) {
				Main.startGame();
			}
		});

		// 画面にスタックペインを配置する。
		getChildren().add(stackPane);
	}
}
