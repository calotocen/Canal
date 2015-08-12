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

public class SquareEnemy extends Enemy {
	private int m_chaseLevel;

	public SquareEnemy(Point position) {
		super(position, Direction.DOWN_RIGHT);
		m_chaseLevel = 0;
	}

	public int getChaseLevel() {
		return m_chaseLevel;
	}

	public void move() {
		m_chaseLevel = 0;

		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (expeditionLine != null) {
			Point position = getPosition();
			Point playerPosition = GameContext.getPlayer().getPosition();
			boolean chase = Stream.iterate(position, point -> new Point(point, Direction.valueOf(point, playerPosition), 1))
				.limit(Math.max(Math.abs(position.getX() - playerPosition.getX()), Math.abs(position.getY() - playerPosition.getY())))
				.allMatch(point -> !GameContext.getTerritory().isTerritory(point));
			if (chase) {
				setDirection(Direction.valueOf(getPosition(), GameContext.getPlayer().getPosition()));
				m_chaseLevel = 2;
			}
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
