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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 遠征線である。
 */
public class ExpeditionLine {
	/** 遠征線を構成する点 */
	private ArrayList<Point> m_points;

	/**
	 * 生存状態。
	 * true ならば『生存』，false ならば『撃墜』を表す。
	 */
	private boolean m_live;

	/**
	 * 遠征線を生成する。
	 *
	 * @param startPoint 遠征線の起点。
	 */
	public ExpeditionLine(Point startPoint) {
		m_points = new ArrayList<>(Configuration.FIELD_WIDTH * Configuration.FIELD_HEIGHT);
		m_points.add(startPoint);
		m_live = true;
	}

	/**
	 * 生存状態を返す。
	 *
	 * @return 生存状態。true ならば『生存』，false ならば『撃墜』。
	 */
	public boolean isLive() {
		return m_live;
	}

	/**
	 * 生存状態を設定する。
	 *
	 * @param live 生存状態。true ならば『生存』，false ならば『撃墜』。
	 */
	public void setLive(boolean live) {
		m_live = live;
	}

	/**
	 * 遠征線の起点を返す。
	 *
	 * @return 遠征線の起点。
	 */
	public Point getStartPoint() {
		return m_points.get(0);
	}

	/**
	 * 遠征線の終点を返す。
	 *
	 * @return 遠征線の終点。
	 */
	public Point getEndPoint() {
		return m_points.get(m_points.size() - 1);
	}

	/**
	 * 遠征線を構成する点を返す。
	 *
	 * @return 遠征線を構成する点。
	 */
	public Collection<Point> getPoints() {
		return Collections.unmodifiableList(m_points);
	}

	/**
	 * 遠征線を延伸，または短縮する。
	 *
	 * @param direction 延伸する方向。
	 * @return 延伸成否。
	 */
	public boolean forward(Direction direction) {
		Point currentPoint = m_points.get(m_points.size() - 1);
		Point newPoint1 = new Point(currentPoint, direction, 1);
		Point newPoint2 = new Point(currentPoint, direction, 2);

		if (!m_points.contains(newPoint2)) {
			// 延伸する方向に遠征線がない場合は，遠征線を延伸する。
			m_points.add(newPoint1);
			m_points.add(newPoint2);
		} else if (newPoint2.equals(m_points.get(m_points.size() - 3))) {
			// 延伸する方向が直前の終点である場合，遠征線を短縮する。
			m_points.remove(m_points.size() - 1);
			m_points.remove(m_points.size() - 1);
		} else {
			// 延伸すると遠征線が交差する場合，延伸失敗とする。
			return false;
		}

		return true;
	}

	/**
	 * 遠征線を短縮する。
	 *
	 * @return 短縮成否。
	 */
	public boolean back() {
		Point currentPoint = m_points.get(m_points.size() - 1);
		Point previousPoint = m_points.get(m_points.size() - 2);
		return forward(Direction.valueOf(currentPoint, previousPoint));
	}
}
