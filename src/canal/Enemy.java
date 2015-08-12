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

import java.util.Optional;
import java.util.stream.Stream;

public abstract class Enemy extends Sprite {
	private int m_speed;

	public Enemy(Point position, Direction direction) {
		super(position, direction);
		m_speed = 1;
	}

	protected int getSpeed() {
		return m_speed;
	}

	protected void setSpeed(int speed) {
		m_speed = speed;
	}

	protected Direction getReflectionDirection() {
		Direction direction = getDirection();
		Point curPosition = getPosition();
		Point newPosition = new Point(curPosition, direction, 1);
		Territory territory = GameContext.getTerritory();

		if (Field.contains(newPosition) && !territory.isTerritory(newPosition)) {
			return Direction.NONE;
		}

		boolean horizontalCollision = false;
		boolean verticalCollision = false;
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			horizontalCollision = true;
		} else if (direction == Direction.UP || direction == Direction.DOWN) {
			verticalCollision = true;
		} else {
			if (!Field.containsX(newPosition.getX()) || territory.isTerritory(newPosition.getX(), curPosition.getY())) {
				horizontalCollision = true;
			}
			if (!Field.containsY(newPosition.getY()) || territory.isTerritory(curPosition.getX(), newPosition.getY())) {
				verticalCollision = true;
			}
		}
		if (horizontalCollision && !verticalCollision) {
			return direction.getRetrorseX();
		} else if (!horizontalCollision && verticalCollision) {
			return direction.getRetrorseY();
		} else {
			return direction.getRetrorse();
		}
	}

	protected boolean move(Direction direction) {
		Point curPosition = getPosition();
		Optional<Point> newPosition;

		final ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		newPosition = Stream.iterate(curPosition, position -> new Point(position, direction, 1)).limit(m_speed)
				.filter(position -> expeditionLine != null && expeditionLine.getPoints().contains(position))
				.findFirst();
		if (newPosition.isPresent()) {
			setPosition(newPosition.get());
			return true;
		}

		for (int i = 1; i <= m_speed; i++) {
			Point position = new Point(curPosition, direction, i);
			if (!Field.contains(position) || GameContext.getTerritory().isTerritory(position)) {
				break;
			}
			newPosition = Optional.of(position);
		}
		if (newPosition.isPresent()) {
			setPosition(newPosition.get());
			return true;
		}

		return false;
	}

	abstract public void move();
}
