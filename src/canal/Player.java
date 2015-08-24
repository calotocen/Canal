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

import java.util.Arrays;

/**
 * 自機である。
 */
public class Player extends Sprite {
	/**
	 * 自機を生成する。
	 *
	 * @param point 初期位置。
	 */
	public Player(Point point) {
		super(point, Direction.UP);
	}

	/**
	 * 移動する。
	 *
	 * @param direction 方向。
	 * @param expedition 遠征有無。
	 */
	public void move(Direction direction, boolean expedition) {
		Point curPosition = getPosition();

		// 方向を設定する。
		if (direction != Direction.NONE) {
			setDirection(direction);
		}

		// 移動先を生成する。
		// 自機の移動速度は，常に 2 である。
		Point newPosition1 = new Point(curPosition, direction, 1);
		Point newPosition2 = new Point(curPosition, direction, 2);

		// 移動先がフィールド内であるか確認する。
		if (!Field.contains(newPosition2)) {
			return;
		}

		Territory territory = GameContext.getTerritory();
		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (!expedition) {
			// 遠征有無が無しの場合，次のように処理する。
			//     - 遠征中の場合                         ... 自機は遠征中である。自機をバックする。
			//     - 上記以外で方向の指定がない場合       ... 自機は領地壁上にある。何もしない。
			//     - 上記以外で移動経路が領地壁でない場合 ... 自機は領地壁上にある。
			//                                                領地内部や荒野には移動できないので何もしない。
			//     - 上記以外                             ... 自機は領地壁上にある。
			//                                                指定された方向にある領地壁へ移動する。
			if (expeditionLine != null) {
				// 自機をバックする。
				expeditionLine.back();

				// 移動先を更新する。
				newPosition2 = expeditionLine.getEndPoint();

				// 領地に戻ってきた場合は，遠征線を削除する。
				if (territory.isWall(newPosition2)) {
					GameContext.setExpeditionLine(null);
				}
			} else if (direction == Direction.NONE) {
				// 何もしない。
				return;
			} else if (!territory.isWall(newPosition1) || !territory.isWall(newPosition2)) {
				// 何もしない。
				return;
			}
		} else {
			// 遠征有無が有りの場合，次のように処理する。
			//     - 方向の指定がない場合           ... 自機は領地壁上，または遠征中である。何もしない。
			//     - 上記以外で移動先が領地外の場合 ... 自機は領地壁上，または遠征中である。指定された方向に遠征する。
			//     - 上記以外で移動先が領地壁の場合 ... 自機は領地壁上，または遠征中である。指定された方向に移動する。
			//                                          遠征中であった場合は，遠征を終えて領地を確定する。
			//     - 上記以外の場合                 ... 自機は領地壁上にある。領地内部には移動できないので何もしない。
			if (direction == Direction.NONE) {
				// 何もしない。
				return;
			} else if (!territory.isTerritory(newPosition2)) {
				// 遠征中でなかった場合は，遠征線を生成する。
				if (expeditionLine == null) {
					expeditionLine = new ExpeditionLine(curPosition);
					GameContext.setExpeditionLine(expeditionLine);
				}

				// 遠征する。
				boolean forward = expeditionLine.forward(direction);
				if (!forward) {
					// 遠征線が交差するため，延伸できなかった。
					return;
				}
			} else if (territory.isWall(newPosition2)) {
				// 次のように処理する。
				//     - 遠征中の場合                         ... 遠征を終えて領地を確定する。
				//     - 上記以外で移動経路が領地壁以外の場合 ... 自機は領地壁上にある。荒野を挟んで隣の領地壁に移動する。
				//                                                必ず，遠征線で囲まれた領域が生じるため，領地を確定する。
				//     - 上記以外で移動先が領地壁以外の場合   ... 自機は領地壁上にある。
				//                                                領地内部には移動できないので何もしない。
				//     - 上記以外の場合                       ... 自機は領域壁上にある。
				//                                                指定された方向にある領地壁へ移動する。
				if (expeditionLine != null) {
					// 遠征する。
					// 遠征線が交差することはないため，必ず成功する。
					if (!expeditionLine.forward(direction)) {
						return;
					}

					// 遠征線の起点以外の場所が終点であった場合，
					// 遠征線で囲んだ領域を領地化する。
					if (expeditionLine.getPoints().size() > 1) {
						territory.territorise(expeditionLine.getPoints());
					}

					// 遠征線を削除する。
					GameContext.setExpeditionLine(null);
				} else if (!territory.isTerritory(newPosition1)) {
					// 領地戦で囲んだ領域を領地化する。
					territory.territorise(Arrays.asList(new Point[] { curPosition, newPosition1, newPosition2 }));
				} else if (!territory.isWall(newPosition1)) {
					// 何もしない。
					return;
				}
			} else {
				// 何もしない。
				return;
			}
		}

		// 移動する。
		setPosition(newPosition2);
	}
}
