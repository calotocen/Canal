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
 * 領域である。
 */
public class Region {
	/** 基点の横座標 */
	private int m_x;

	/** 基点の縦座標 */
	private int m_y;

	/** 横幅 */
	private int m_width;

	/** 縦幅 */
	private int m_height;

	/**
	 * 領域を生成する。
	 *
	 * @param x 基点の横座標。
	 * @param y 基点の縦座標。
	 * @param width 横幅。
	 * @param height 縦幅。
	 */
	public Region(int x, int y, int width, int height) {
		m_x = x;
		m_y = y;
		m_width = width;
		m_height = height;
	}

	/**
	 * 基点の横座標を返す。
	 *
	 * @return 基点の横座標。
	 */
	public int getX() {
		return m_x;
	}

	/**
	 * 基点の縦座標を返す。
	 *
	 * @return 基点の縦座標。
	 */
	public int getY() {
		return m_y;
	}

	/**
	 * 基点の横幅を返す。
	 *
	 * @return 基点の横幅。
	 */
	public int getWidth() {
		return m_width;
	}

	/**
	 * 基点の縦幅を返す。
	 *
	 * @return 基点の縦幅。
	 */
	public int getHeight() {
		return m_height;
	}
}
