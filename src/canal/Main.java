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

/**
 * エントリーポイントを定義するクラスである。
 */
public class Main extends Application {
	/** ルートノード */
	private static Group st_root;

	/** アクティブな画面 */
	private static Screen st_screen;

	/**
	 * JavaFX アプリケーションのエントリポイントである。
	 *
	 * @param stage アプリケーションのプライマリステージ。
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// ルートペイン，およびシーンを生成する。
		st_root = new Group();
		Scene scene = new Scene(st_root);

		// 画面をタイトル画面に切り替える。
		changeScreen(0);

		// リサイズを禁止する場合は，次のコードを有効にする。
		// リサイズを禁止すると最適なウィンドウサイズが自動的に設定されないので，
		// 明示的にサイズを指定する必要がある。
		// stage.setResizable(false);
		// stage.setWidth(Configuration.SCREEN_WIDTH + Configuration.WINDOW_BORDER_WIDTH * 2);
		// stage.setHeight(Configuration.SCREEN_HEIGHT + Configuration.WINDOW_BORDER_HEIGHT * 2 + Configuration.TITLE_BAR_HEIGHT);

		// ステージにシーンを設定し，ステージを表示する。
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * アプリケーションのエントリポイントである。
	 *
	 * @param args コマンドライン引数。
	 */
	public static void main(String[] args) {
		// JavaFX アプリケーションを起動する
		launch(args);
	}

	/**
	 * ゲームを開始する。
	 */
	public static void startGame() {
		// 画面をゲーム画面 (１面) に切り替える。
		changeScreen(1);
	}

	/**
	 * 画面を切り替える。
	 *
	 * @param newScreenNumber 画面番号。
	 */
	public static void changeScreen(int newScreenNumber) {
		// 画面を停止する。
		if (st_screen != null) {
			st_screen.stop();
		}

		if (newScreenNumber == 0) {
			// 画面をタイトル画面に切り替える。
			GameContext.initialize();
			st_screen = new TitleScreen();
		} else if (newScreenNumber <= Levels.size()) {
			// ゲームコンテキストを更新後，
			// 画面をゲーム画面に切り替える。
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
			// 画面をリザルト画面に切り替える。
			st_screen = new ResultScreen();
		}

		// ルートペインに画面を設定する。
		st_root.getChildren().clear();
		st_root.getChildren().add(st_screen);

		// 画面を開始する。
		st_screen.start();
	}
}
