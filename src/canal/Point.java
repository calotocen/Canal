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

import java.util.Objects;

public class Point {
	private int m_x;
	private int m_y;

	public Point() {
		this(0, 0);
	}

	public Point(int x, int y) {
		this.m_x = x;
		this.m_y = y;
	}

	public Point(Point basePoint, Direction direction, int distance) {
		this(basePoint.m_x + direction.getX() * distance, basePoint.m_y + direction.getY() * distance);
	}

	public int getX() {
		return m_x;
	}

	public int getY() {
		return m_y;
	}

	public Point[] getPointsOnFourSides() {
		return new Point[] { new Point(m_x - 1, m_y), new Point(m_x + 1, m_y), new Point(m_x, m_y - 1), new Point(m_x, m_y + 1) };
	}

	public Point[] getPointsOnSlantingFourSides() {
		return new Point[] { new Point(m_x - 1, m_y - 1), new Point(m_x - 1, m_y + 1), new Point(m_x + 1, m_y - 1), new Point(m_x + 1, m_y + 1) };
	}

	public Point[] getPointsOnEightSides() {
		return new Point[] { new Point(m_x - 1, m_y - 1), new Point(m_x, m_y - 1), new Point(m_x + 1, m_y - 1), new Point(m_x - 1, m_y), new Point(m_x + 1, m_y), new Point(m_x - 1, m_y + 1), new Point(m_x, m_y + 1), new Point(m_x + 1, m_y + 1) };
	}

	public double getDistance(Point point) {
		double x = m_x - point.m_x;
		double y = m_y - point.m_y;
		return Math.sqrt(x * x + y * y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(m_x, m_y);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || !(object instanceof Point)) {
			return false;
		}

		Point other = (Point) object;
		return m_x == other.m_x && m_y == other.m_y;
	}
}
