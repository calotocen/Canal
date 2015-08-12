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

public class TitleScreen extends Screen {
	public TitleScreen() {
		setFocusTraversable(true);

		Text titleText = createText("Canal", 15, Color.GREENYELLOW);

		StackPane stackPane = new StackPane(titleText);
		stackPane.setPrefSize(Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT);
		stackPane.setStyle("-fx-background-color: black;");

		setOnKeyTyped(event -> {
			if (event.getCharacter().equals(" ")) {
				Main.startGame();
			}
		});

		getChildren().add(stackPane);
	}
}
