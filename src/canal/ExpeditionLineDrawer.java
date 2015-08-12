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


public class ExpeditionLineDrawer {
	ExpeditionLine m_expeditionLine;

	public ExpeditionLineDrawer(ExpeditionLine expeditionLine) {
		m_expeditionLine = expeditionLine;
	}

	public void draw(int[] argbs, int width, int height) {
		int argb;
		if (m_expeditionLine.isLive()) {
			// 0xffffff00 : Color.YELLOW
			argb = 0xffffff00;
		} else {
			// 0xffff0000 : Color.RED
			argb = 0xffff0000;
		}

		m_expeditionLine.getPoints().forEach(point -> argbs[point.getX() + point.getY() * width] = argb);
	}
}
