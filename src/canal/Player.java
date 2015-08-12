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

public class Player extends Sprite {
	public Player(Point point) {
		super(point, Direction.UP);
	}

	public void move(Direction direction, boolean expedition) {
		Point curPosition = getPosition();

		if (direction != Direction.NONE) {
			setDirection(direction);
		}

		Point newPosition1 = new Point(curPosition, direction, 1);
		Point newPosition2 = new Point(curPosition, direction, 2);
		if (!Field.contains(newPosition2)) {
			return;
		}

		Territory territory = GameContext.getTerritory();
		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (!expedition) {
			if (expeditionLine != null) {
				expeditionLine.back();

				newPosition2 = expeditionLine.getEndPoint();
				if (territory.isWall(newPosition2)) {
					GameContext.setExpeditionLine(null);
				}
			} else if (direction == Direction.NONE) {
				return;
			} else if (!territory.isWall(newPosition1) || !territory.isWall(newPosition2)) {
				return;
			}
		} else {
			if (direction == Direction.NONE) {
				return;
			} else if (!territory.isTerritory(newPosition2)) {
				if (expeditionLine == null) {
					expeditionLine = new ExpeditionLine(curPosition);
					GameContext.setExpeditionLine(expeditionLine);
				}

				boolean forward = expeditionLine.forward(direction);
				if (!forward) {
					return;
				}
			} else if (territory.isWall(newPosition2)) {
				if (expeditionLine != null) {
					if (!expeditionLine.forward(direction)) {
						return;
					}
					if (expeditionLine.getPoints().size() > 1) {
						territory.territorise(expeditionLine.getPoints());
					}

					GameContext.setExpeditionLine(null);
				} else if (!territory.isTerritory(newPosition1)) {
					territory.territorise(Arrays.asList(new Point[] { curPosition, newPosition1, newPosition2 }));
				} else if (!territory.isWall(newPosition1)) {
					return;
				}
			} else {
				return;
			}
		}

		setPosition(newPosition2);
	}
}
