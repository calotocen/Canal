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

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 画面の基底クラスである。
 */
public class Screen extends Pane {
	/**
	 * 画面を生成する。
	 */
	public Screen() {
		// 何もしない。
	}

	/**
	 * 画面を開始する。
	 */
	public void start() {
		// 何もしない。
	}

	/**
	 * 画面を停止する。
	 */
	public void stop() {
		// 何もしない。
	}

	/**
	 * テキストノードを生成する。
	 *
	 * @param text テキスト。
	 * @return テキストノード。
	 */
	public Text createText(String text) {
		return createText(text, 15);
	}

	/**
	 * テキストノードを生成する。
	 *
	 * @param text テキスト。
	 * @param fontSize フォントサイズ。
	 * @return テキストノード。
	 */
	public Text createText(String text, int fontSize) {
		return createText(text, fontSize, Color.WHITE);
	}

	/**
	 * テキストノードを生成する。
	 *
	 * @param text テキスト。
	 * @param fontSize フォントサイズ。
	 * @param color フォントカラー。
	 * @return テキストノード。
	 */
	public Text createText(String text, int fontSize, Color color) {
		return createText(text, "bold-gothic", fontSize, color);
	}

	/**
	 * テキストノードを生成する。
	 *
	 * @param text テキスト。
	 * @param fontFamily フォントファミリー。
	 * @param fontSize フォントサイズ。
	 * @param color フォントカラー。
	 * @return テキストノード。
	 */
	public Text createText(String text, String fontFamily, int fontSize, Color color) {
		Text textNode = new Text(text);
		switch (fontFamily) {
		case "bold-gothic":
			textNode.setFont(new Font("Impact", fontSize));
			break;

		default:
			textNode.setStyle("-fx-font-family: " + fontFamily + "; -fx-font-size: " + fontSize + ";");
			break;
		}
		textNode.setFill(color);

		return textNode;
	}
}
