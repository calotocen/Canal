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


public class BigPentagonEnemy extends Enemy {
	Point m_prePosition[];

	public BigPentagonEnemy(Point position) {
		super(position, Direction.RIGHT);
		setSpeed(1);
		m_prePosition = new Point[3];
		m_prePosition[0] = position;
		m_prePosition[1] = position;
		m_prePosition[2] = position;
	}

	public void move() {
		Point curPosition = getPosition();
		Territory territory = GameContext.getTerritory();

		for (int i = 0; i < 3; i++) {
			for (Point point : curPosition.getPointsOnEightSides()) {
				if (point.equals(m_prePosition[0])) {
					continue;
				}

				if (point.equals(m_prePosition[1])) {
					continue;
				}

				if (point.equals(m_prePosition[2])) {
					continue;
				}

				if (territory.isTerritory(point)) {
					continue;
				}

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

			move(Direction.valueOf(m_prePosition[0], curPosition));
		}
	}
}
