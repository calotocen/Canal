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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class BigSquareEnemy extends Enemy {
	private static final int LOOP_COUNT = 500;

	private int m_chaseLevel;
	private Point m_targetPosition;
	private PriorityQueue<Point> m_traversingPoints;
	private HashMap<Point, Point> m_routeToTarget;

	public BigSquareEnemy(Point position) {
		super(position, Direction.DOWN_RIGHT);
		setSpeed(2);
		m_chaseLevel = 0;
		m_targetPosition = null;
		m_traversingPoints = null;
		m_routeToTarget = null;
	}

	public int getChaseLevel() {
		return m_chaseLevel;
	}

	public void move() {
		m_chaseLevel = 0;

		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (expeditionLine != null) {
			Point curPosition = getPosition();
			Point playerPosition = GameContext.getPlayer().getPosition();

			boolean chaseDirectly = Stream.iterate(curPosition, position -> new Point(position, Direction.valueOf(position, playerPosition), 1))
					.limit(Math.max(Math.abs(curPosition.getX() - playerPosition.getX()), Math.abs(curPosition.getY() - playerPosition.getY())))
					.allMatch(position -> !GameContext.getTerritory().isTerritory(position));
			if (chaseDirectly) {
				setDirection(Direction.valueOf(curPosition, playerPosition));
				m_chaseLevel = 2;
			} else {
				if (m_targetPosition == null) {
					m_targetPosition = playerPosition;
					m_traversingPoints = new PriorityQueue<>(
							Configuration.FIELD_WIDTH * Configuration.FIELD_HEIGHT,
							(point1, point2) -> Double.compare(m_targetPosition.getDistance(point1), m_targetPosition.getDistance(point2)));
					m_routeToTarget = new HashMap<>();

					m_traversingPoints.add(playerPosition);
				}

				if (!m_routeToTarget.containsKey(curPosition)) {
					for (int i = 0; i < LOOP_COUNT && m_traversingPoints.size() > 0; i++) {
						Point targetPoint = m_traversingPoints.poll();
						if (targetPoint.equals(curPosition)) {
							break;
						}

						Stream.of(targetPoint.getPointsOnEightSides())
								.filter(point -> Field.contains(point))
								.filter(point -> !GameContext.getTerritory().isTerritory(point))
								.filter(point -> !m_routeToTarget.containsKey(point))
								.forEach(point -> {
									m_traversingPoints.add(point);
									m_routeToTarget.put(point, targetPoint);
								});
					}
				}
				if (!m_routeToTarget.containsKey(curPosition)) {
					setDirection(Direction.valueOf(curPosition, playerPosition));
					m_chaseLevel = 1;
				} else {
					setDirection(Direction.valueOf(curPosition, m_routeToTarget.get(curPosition)));
					m_chaseLevel = 2;
				}
			}
		} else {
			m_targetPosition = null;
			m_traversingPoints = null;
			m_routeToTarget = null;
		}

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
