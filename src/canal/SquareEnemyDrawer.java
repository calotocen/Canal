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
 * 四角敵を描画するクラスである。
 */
public class SquareEnemyDrawer extends SpriteDrawer {
	/**
	 * 四角敵描画者を生成する。
	 *
	 * @param squareEnemy 大四角敵
	 */
	public SquareEnemyDrawer(SquareEnemy squareEnemy) {
		super(squareEnemy);
	}

	/**
	 * 四角敵を描画する。
	 *
	 * @param gc 描画先。
	 */
	public void draw(GraphicsContext gc) {
		// 四角敵を取得する。
		SquareEnemy squareEnemy = (SquareEnemy) getSprite();

		// 追跡レベルに応じて，四角敵の色を変更する。
		switch (squareEnemy.getChaseLevel()) {
		case 0:
			setFill(Color.ROYALBLUE);
			break;
		case 1:
			setFill(Color.MEDIUMVIOLETRED);
			break;
		case 2:
			setFill(Color.RED);
			break;
		}

		// 四角敵を描画する。
		drawSquare(gc);
	}
}
