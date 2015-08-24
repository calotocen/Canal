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

/**
 * 方向である。
 */
public enum Direction {
	/** 方向なし */
	NONE(0, 0),

	/** 上方向 */
	UP(0, -1),

	/** 下方向 */
	DOWN(0, 1),

	/** 左方向 */
	LEFT(-1, 0),

	/** 右方向 */
	RIGHT(1, 0),

	/** 左上方向 */
	UP_LEFT(-1, -1),

	/** 右上方向 */
	UP_RIGHT(1, -1),

	/** 左下方向 */
	DOWN_LEFT(-1, 1),

	/** 右下方向 */
	DOWN_RIGHT(1, 1);

	/** 横成分 */
	private int m_x;

	/** 縦成分 */
	private int m_y;

	/**
	 * 引数に対応する方向を返す。
	 *
	 * @param x 横成分。左の場合は -1，右の場合は 1，方向なしの場合は 0 を指定する。
	 * @param y 縦成分。上の場合は -1，下の場合は 1，方向なしの場合は 0 を指定する。
	 * @return 引数に対応する方向。
	 */
	public static Direction valueOf(int x, int y) {
		return Stream.of(values())
				.filter(d -> (x == d.m_x && y == d.m_y))
				.findFirst()
				.get();
	}

	/**
	 * 基点から対象への方向を返す。
	 *
	 * @param basePoint 基点となる位置。
	 * @param targetPoint 対象位置。
	 * @return 基点から対象に向かうための方向。
	 */
	public static Direction valueOf(Point basePoint, Point targetPoint) {
		// 基点と対象の位置関係を計算する。
		// 斜め 45 度以外の斜め方向にあった場合は，方向を丸める。
		int x = (int) Math.signum(targetPoint.getX() - basePoint.getX());
		int y = (int) Math.signum(targetPoint.getY() - basePoint.getY());
		return valueOf(x, y);
	}

	/**
	 * 上，右，下，および左の方向を返す。
	 *
	 * @return 縦横四方向の配列。
	 */
	public static Direction[] valuesOfFourDirection() {
		return new Direction[] {
				UP,
				RIGHT,
				DOWN,
				LEFT,
		};
	}

	/**
	 * 右上，右下，左下，および左上の方向を返す。
	 *
	 * @return 斜め四方向の配列。
	 */
	public static Direction[] valuesOfSlantingFourDirection() {
		return new Direction[] {
				UP_RIGHT,
				DOWN_RIGHT,
				DOWN_LEFT,
				UP_LEFT,
		};
	}

	/**
	 * 上，右上，右，右下，下，左下，左，および左上の方向を返す。
	 *
	 * @return 縦横斜め八方向の配列。
	 */
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

	/**
	 * 方向を生成する。
	 *
	 * @param x 横成分。左の場合は -1，右の場合は 1，方向なしの場合は 0 を指定する。
	 * @param y 縦成分。上の場合は -1，下の場合は 1，方向なしの場合は 0 を指定する。
	 */
	private Direction(int x, int y) {
		m_x = x;
		m_y = y;
	}

	/**
	 * 横成分を返す。
	 *
	 * @return 左の場合は -1，右の場合は 1，方向なしの場合は 0。
	 */
	public int getX() {
		return m_x;
	}

	/**
	 * 縦成分を返す。
	 *
	 * @return 上の場合は -1，下の場合は 1，方向なしの場合は 0 を指定する。
	 */
	public int getY() {
		return m_y;
	}

	/**
	 * 横成分を逆にした方向を返す。
	 *
	 * @return 横成分を逆にした方向。
	 */
	public Direction getRetrorseX() {
		return valueOf(-m_x, m_y);
	}

	/**
	 * 縦成分を逆にした方向を返す。
	 *
	 * @return 縦成分を逆にした方向。
	 */
	public Direction getRetrorseY() {
		return valueOf(m_x, -m_y);
	}

	/**
	 * 逆の方向を返す。
	 *
	 * @return 逆の方向。
	 */
	public Direction getRetrorse() {
		return valueOf(-m_x, -m_y);
	}

	/**
	 * 方向の角度をラジアンで返す。
	 * 角度は，上方向を基点とした時計回りで表現する。
	 *
	 * @return 角度 (ラジアン)。
	 */
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
