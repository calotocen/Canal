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

/**
 * 大五角敵である。
 */
public class BigPentagonEnemy extends Enemy {
	/** 移動位置履歴 */
	private Point m_prePosition[];

	/**
	 * 大五角敵を生成する。
	 *
	 * @param position 初期位置。
	 */
	public BigPentagonEnemy(Point position) {
		// 初期位置に右向きに配置する。
		super(position, Direction.RIGHT);

		// 速度を 1 に設定する。
		setSpeed(1);

		// 移動位置履歴を初期化する。
		m_prePosition = new Point[3];
		m_prePosition[0] = position;
		m_prePosition[1] = position;
		m_prePosition[2] = position;
	}

	/**
	 * 移動する。
	 * 移動は，領地の周囲を沿うように行う。
	 */
	public void move() {
		Point curPosition = getPosition();
		Territory territory = GameContext.getTerritory();

		for (int i = 0; i < 3; i++) {
			for (Point point : curPosition.getPointsOnEightSides()) {
				// 移動先候補が移動位置履歴にある場合，そこへは移動しない。
				// これにより，同じ場所の往復を防ぐことができる。
				// 移動履歴のサイズを 3 とするとうまく動作する (経験則)。
				if (point.equals(m_prePosition[0])) {
					continue;
				}
				if (point.equals(m_prePosition[1])) {
					continue;
				}
				if (point.equals(m_prePosition[2])) {
					continue;
				}

				// 移動先候補が領地内である場合，そこへは移動しない。
				if (territory.isTerritory(point)) {
					continue;
				}

				// 移動先候補が領地に接している場合は，そこを移動先とする。
				boolean flag = false;
				for (Point p : point.getPointsOnEightSides()) {
					if (territory.isTerritory(p)) {
						flag = true;
						break;
					}
				}
				if (flag) {
					m_prePosition[2] = m_prePosition[1];
					m_prePosition[1] = m_prePosition[0];
					m_prePosition[0] = curPosition;
					curPosition = point;
					break;
				}
			}

			// 移動先へ移動する。
			// 移動先へは確実に移動できるので，移動できたかの確認は行わない。
			move(Direction.valueOf(m_prePosition[0], curPosition));
		}
	}
}
