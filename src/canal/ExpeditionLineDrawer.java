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
 * 遠征線を描画するクラスである。
 */
public class ExpeditionLineDrawer {
	/** 遠征線 */
	private ExpeditionLine m_expeditionLine;

	/**
	 * 遠征線描画者を生成する。
	 *
	 * @param expeditionLine 遠征線。
	 */
	public ExpeditionLineDrawer(ExpeditionLine expeditionLine) {
		m_expeditionLine = expeditionLine;
	}

	/**
	 * 遠征線を描画する。
	 *
	 * @param argbs キャンバス用ビットマップ。
	 * @param width キャンバスの横幅。
	 * @param height キャンバスの縦幅。
	 */
	public void draw(int[] argbs, int width, int height) {
		int argb;

		// 遠征線の色を選択する。
		if (m_expeditionLine.isLive()) {
			// 0xffffff00 : Color.YELLOW
			argb = 0xffffff00;
		} else {
			// 0xffff0000 : Color.RED
			argb = 0xffff0000;
		}

		// 遠征線をキャンバス用ビットマップに描き込む。
		m_expeditionLine.getPoints().forEach(point -> argbs[point.getX() + point.getY() * width] = argb);
	}
}
