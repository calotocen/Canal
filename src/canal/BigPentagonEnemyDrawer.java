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
 * 大五角敵を描画するクラスである。
 */
public class BigPentagonEnemyDrawer extends SpriteDrawer {
	/**
	 * 大五角敵描画者を生成する。
	 *
	 * @param bigPentagonEnemy 大五角敵
	 */
	public BigPentagonEnemyDrawer(BigPentagonEnemy bigPentagonEnemy) {
		super(bigPentagonEnemy);
		setFill(Color.BLUEVIOLET);
		setSize(getSize() * 2 + 1);
	}

	/**
	 * 大五角敵を描画する。
	 *
	 * @param gc 描画先。
	 */
	public void draw(GraphicsContext gc) {
		// 大五角敵を描画する。
		drawPentagon(gc);
	}
}
