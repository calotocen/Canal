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
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * 四角敵である。
 */
public class SquareEnemy extends Enemy {
	/**
	 * 追跡レベル。値の意味は次の通りである。
	 * <ul>
	 *     <li>0 ... 未追跡</li>
	 *     <li>1 ... 追跡 (経路未発見)</li>
	 *     <li>2 ... 追跡 (経路発見済)</li>
	 * </ul>
	 */
	private int m_chaseLevel;

	/**
	 * 四角敵を生成する。
	 *
	 * @param position 初期位置。
	 */
	public SquareEnemy(Point position) {
		super(position, Direction.DOWN_RIGHT);
		m_chaseLevel = 0;
	}

	/**
	 * 追跡レベルを返す。
	 *
	 * @return 追跡レベル。
	 */
	public int getChaseLevel() {
		return m_chaseLevel;
	}

	/**
	 * 移動する。
	 * 自機が遠征している場合，間に遮蔽物がなければ自機を追う。
	 * 自機を追っていない場合，進行方向へまっすぐ移動する。
	 * なお，障害物にぶつかったときは，ランダムに向きを変える。
	 */
	public void move() {
		m_chaseLevel = 0;

		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (expeditionLine != null) {
			Point position = getPosition();
			Point playerPosition = GameContext.getPlayer().getPosition();

			// 直接，自機を追える状態 (自機との間に領地がない状態) か調べる。
			// 具体的には，自機までの経路を洗い出し，その経路上に領地が含まれないことを確認する。
			boolean chase = Stream.iterate(position, point -> new Point(point, Direction.valueOf(point, playerPosition), 1))
				.limit(Math.max(Math.abs(position.getX() - playerPosition.getX()), Math.abs(position.getY() - playerPosition.getY())))
				.allMatch(point -> !GameContext.getTerritory().isTerritory(point));
			if (chase) {
				setDirection(Direction.valueOf(getPosition(), GameContext.getPlayer().getPosition()));
				m_chaseLevel = 2;
			}
		}

		// 移動する。
		// 移動に失敗した場合は，ランダムに向きを変えて移動可能な方向へ移動する。
		boolean moved = move(getDirection());
		if (!moved) {
			LinkedList<Direction> directions = new LinkedList<>(Arrays.asList(Direction.valuesOfSlantingFourDirection()));
			Collections.shuffle(directions);
			for (Direction direction : directions) {
				if (move(direction)) {
					setDirection(direction);
					break;
				}
			}
		}
	}
}
