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

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	private static Group st_root;
	private static Screen st_screen;

	@Override
	public void start(Stage stage) throws Exception {
		st_root = new Group();
		Scene scene = new Scene(st_root);

		changeScreen(0);

//		stage.setResizable(false);
//		stage.setWidth(Configuration.SCREEN_WIDTH + Configuration.WINDOW_BORDER_WIDTH * 2);
//		stage.setHeight(Configuration.SCREEN_HEIGHT + Configuration.WINDOW_BORDER_HEIGHT * 2 + Configuration.TITLE_BAR_HEIGHT);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void startGame() {
		changeScreen(1);
	}

	public static void changeScreen(int newScreenNumber) {
		if (st_screen != null) {
			st_screen.stop();
		}

		if (newScreenNumber == 0) {
			GameContext.initialize();
			st_screen = new TitleScreen();
		} else if (newScreenNumber <= Levels.size()) {
			Level level = Levels.get(newScreenNumber);
			GameContext.setLevelNumber(newScreenNumber);
			GameContext.setState(GameContext.State.PLAYING);
			GameContext.setRemainingTime(Configuration.INITIAL_REMAINING_TIME);
			GameContext.setPlayer(level.getPlayer());
			GameContext.setEnemies(level.getEnemies());
			GameContext.setTerritory(level.getTerritory());
			GameContext.setExpeditionLine(null);
			st_screen = new GameScreen();
		} else {
			st_screen = new ResultScreen();
		}

		st_root.getChildren().clear();
		st_root.getChildren().add(st_screen);
		st_screen.start();
	}
}
