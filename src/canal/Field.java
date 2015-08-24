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
 * フィールドである。
 */
public class Field {
	/**
	 * 指定された位置の横成分がフィールドに含まれるかを返す。
	 *
	 * @param x 位置の横成分。
	 * @return フィールドに含まれる場合は true，それ以外の場合は false。
	 */
	public static boolean containsX(int x) {
		return (0 <= x && x < Configuration.FIELD_WIDTH);
	}

	/**
	 * 指定された位置の縦成分がフィールドに含まれるかを返す。
	 *
	 * @param y 位置の縦成分。
	 * @return フィールドに含まれる場合は true，それ以外の場合は false。
	 */
	public static boolean containsY(int y) {
		return (0 <= y && y < Configuration.FIELD_HEIGHT);
	}

	/**
	 * 指定された位置がフィールドに含まれるかを返す。
	 *
	 * @param x 位置の横成分。
	 * @param y 位置の縦成分。
	 * @return フィールドに含まれる場合は true，それ以外の場合は false。
	 */
	public static boolean contains(int x, int y) {
		return (containsX(x) && containsY(y));
	}

	/**
	 * 指定された位置がフィールドに含まれるかを返す。
	 *
	 * @param point 位置。
	 * @return フィールドに含まれる場合は true，それ以外の場合は false。
	 */
	public static boolean contains(Point point) {
		return contains(point.getX(), point.getY());
	}

	/**
	 * 指定された領域がフィールドに含まれるかを返す。
	 *
	 * @param region 領域。
	 * @return フィールドに領域が完全に含まれる場合は true，それ以外の場合は false。
	 */
	public static boolean contains(Region region) {
		return contains(region.getX(), region.getY())
				&& contains(region.getX() + region.getWidth() - 1, region.getY() + region.getHeight() - 1);
	}
}
