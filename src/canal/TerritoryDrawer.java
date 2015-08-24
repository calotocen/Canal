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

import java.util.stream.IntStream;

/**
 * 領地を描画するクラスである。
 */
public class TerritoryDrawer {
	/** 領地 */
	private Territory m_territory;

	/**
	 * 領地描画者を生成する。
	 *
	 * @param territory 大四角敵
	 */
	public TerritoryDrawer(Territory territory) {
		m_territory = territory;
	}

	/**
	 * 領地を描画する。
	 *
	 * @param argbs 描画先。
	 * @param width 描画先の横幅。
	 * @param height 描画先の縦幅。
	 */
	public void draw(int[] argbs, int width, int height) {
		IntStream.range(0, height).forEach(y -> {
			IntStream.range(0, width).forEach(x -> {
				if (m_territory.isWall(x, y)) {
					// 壁を描画する (0xff90EE90: Color.LIGHTGREEN)。
					argbs[x + y * width] = 0xff90EE90;
				} else if (m_territory.isTerritory(x, y)) {
					// 領地を描画する (0xff008000: Color.GREEN)。
					argbs[x + y * width] = 0xff008000;
				}
			});
		});
	}
}
