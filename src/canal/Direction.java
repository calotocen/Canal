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

import java.util.stream.Stream;

public enum Direction {
	NONE(0, 0),
	UP(0, -1),
	DOWN(0, 1),
	LEFT(-1, 0),
	RIGHT(1, 0),
	UP_LEFT(-1, -1),
	UP_RIGHT(1, -1),
	DOWN_LEFT(-1, 1),
	DOWN_RIGHT(1, 1);

	private int m_x;
	private int m_y;

	public static Direction valueOf(int x, int y) {
		return Stream.of(values())
				.filter(d -> (x == d.m_x && y == d.m_y))
				.findFirst()
				.get();
	}

	public static Direction valueOf(Point curPoint, Point newPoint) {
		int x = (int) Math.signum(newPoint.getX() - curPoint.getX());
		int y = (int) Math.signum(newPoint.getY() - curPoint.getY());
		return valueOf(x, y);
	}

	public static Direction[] valuesOfFourDirection() {
		return new Direction[] {
				UP,
				RIGHT,
				DOWN,
				LEFT,
		};
	}

	public static Direction[] valuesOfSlantingFourDirection() {
		return new Direction[] {
				UP_RIGHT,
				DOWN_RIGHT,
				DOWN_LEFT,
				UP_LEFT,
		};
	}

	public static Direction[] valuesOfEightDirection() {
		return new Direction[] {
				UP,
				UP_RIGHT,
				RIGHT,
				DOWN_RIGHT,
				DOWN,
				DOWN_LEFT,
				LEFT,
				UP_LEFT,
		};
	}

	private Direction(int x, int y) {
		m_x = x;
		m_y = y;
	}

	public int getX() {
		return m_x;
	}

	public int getY() {
		return m_y;
	}

	public Direction getRetrorseX() {
		return valueOf(-m_x, m_y);
	}

	public Direction getRetrorseY() {
		return valueOf(m_x, -m_y);
	}

	public Direction getRetrorse() {
		return valueOf(-m_x, -m_y);
	}

	public Direction getTransverseComponent() {
		return valueOf(m_x, 0);
	}

	public Direction getLongitudinalComponent() {
		return valueOf(0, m_y);
	}

	public double getAngle() {
		switch (this) {
		case UP:
			return 0.25 * 0 * Math.PI;
		case UP_RIGHT:
			return 0.25 * 1 * Math.PI;
		case RIGHT:
			return 0.25 * 2 * Math.PI;
		case DOWN_RIGHT:
			return 0.25 * 3 * Math.PI;
		case DOWN:
			return 0.25 * 4 * Math.PI;
		case DOWN_LEFT:
			return 0.25 * 5 * Math.PI;
		case LEFT:
			return 0.25 * 6 * Math.PI;
		case UP_LEFT:
			return 0.25 * 7 * Math.PI;
		default:
			return 0.25 * 0 * Math.PI;
		}
	}
}
