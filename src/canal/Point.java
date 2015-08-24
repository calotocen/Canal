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

/**
 * 点である。
 */
public class Point {
	/** 点の横座標 */
	private int m_x;

	/** 点の縦座標 */
	private int m_y;

	/**
	 * 点 (0, 0) を生成する。
	 */
	public Point() {
		this(0, 0);
	}

	/**
	 * 点 (x, y) を生成する。
	 *
	 * @param x 点の横座標。
	 * @param y 点の縦座標。
	 */
	public Point(int x, int y) {
		this.m_x = x;
		this.m_y = y;
	}

	/**
	 * 基点から指定された方向へ距離だけ離れた点を生成する。
	 *
	 * @param basePoint 基点。
	 * @param direction 方向。
	 * @param distance 距離。
	 */
	public Point(Point basePoint, Direction direction, int distance) {
		this(basePoint.m_x + direction.getX() * distance, basePoint.m_y + direction.getY() * distance);
	}

	/**
	 * 横座標を返す。
	 *
	 * @return 横座標。
	 */
	public int getX() {
		return m_x;
	}

	/**
	 * 縦座標を返す。
	 *
	 * @return 縦座標。
	 */
	public int getY() {
		return m_y;
	}

	/**
	 * 点の左右上下にある 4 つの点を返す。
	 *
	 * @return 左右上下にある点。
	 */
	public Point[] getPointsOnFourSides() {
		return new Point[] {
				new Point(m_x - 1, m_y),
				new Point(m_x + 1, m_y),
				new Point(m_x, m_y - 1),
				new Point(m_x, m_y + 1)
		};
	}

	/**
	 * 点の左上，左下，右上，右下にある 4 つの点を返す。
	 *
	 * @return 左上，左下，右上，右下にある点。
	 */
	public Point[] getPointsOnSlantingFourSides() {
		return new Point[] {
				new Point(m_x - 1, m_y - 1),
				new Point(m_x - 1, m_y + 1),
				new Point(m_x + 1, m_y - 1),
				new Point(m_x + 1, m_y + 1)
		};
	}

	/**
	 * 点の周りにある 8 つの点を返す。
	 * 順序は，左上，上，右上，左，右，左下，下，右下である。
	 *
	 * @return 周りにある点。
	 */
	public Point[] getPointsOnEightSides() {
		return new Point[] {
				new Point(m_x - 1, m_y - 1),
				new Point(m_x, m_y - 1),
				new Point(m_x + 1, m_y - 1),
				new Point(m_x - 1, m_y),
				new Point(m_x + 1, m_y),
				new Point(m_x - 1, m_y + 1),
				new Point(m_x, m_y + 1),
				new Point(m_x + 1, m_y + 1)
		};
	}

	/**
	 * 対象点までの距離を返す。
	 *
	 * @param point 対象点
	 * @return 対象点までの距離。
	 */
	public double getDistance(Point point) {
		double x = m_x - point.m_x;
		double y = m_y - point.m_y;
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * ハッシュコードを返す。
	 *
	 * @return ハッシュコード。
	 */
	@Override
	public int hashCode() {
		return Objects.hash(m_x, m_y);
	}

	/**
	 * 同じ点かを返す。
	 *
	 * @return 同じ点の場合は true，それ以外は false。
	 */
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
