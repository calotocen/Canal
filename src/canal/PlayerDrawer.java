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

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 自機を描画するクラスである。
 */
public class PlayerDrawer extends SpriteDrawer {
	/**
	 * 自機描画者を生成する。
	 *
	 * @param sprite 自機
	 */
	public PlayerDrawer(Sprite sprite) {
		super(sprite);
	}

	/**
	 * 自機を描画する。
	 *
	 * @param gc 描画先。
	 */
	public void draw(GraphicsContext gc) {
		// 自機を取得する。
		Sprite sprite = getSprite();

		// 自機の生存状態に応じて，自機の色を変更する。
		setFill(sprite.isLive() ? Color.YELLOW : Color.RED);

		// 自機を描画する。
		drawCircle(gc);
	}
}
