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

public class ExpeditionLine {
	private ArrayList<Point> m_points;
	private boolean m_live;

	public ExpeditionLine(Point startPoint) {
		m_points = new ArrayList<>(Configuration.FIELD_WIDTH * Configuration.FIELD_HEIGHT);
		m_points.add(startPoint);
		m_live = true;
	}

	public boolean isLive() {
		return m_live;
	}

	public void setLive(boolean live) {
		m_live = live;
	}

	public Point getStartPoint() {
		return m_points.get(0);
	}

	public Point getEndPoint() {
		return m_points.get(m_points.size() - 1);
	}

	public Collection<Point> getPoints() {
		return Collections.unmodifiableList(m_points);
	}

	public boolean forward(Direction direction) {
		Point currentPoint = m_points.get(m_points.size() - 1);
		Point newPoint1 = new Point(currentPoint, direction, 1);
		Point newPoint2 = new Point(currentPoint, direction, 2);

		if (!m_points.contains(newPoint2)) {
			m_points.add(newPoint1);
			m_points.add(newPoint2);
		} else if (newPoint2.equals(m_points.get(m_points.size() - 3))) {
			m_points.remove(m_points.size() - 1);
			m_points.remove(m_points.size() - 1);
		} else {
			return false;
		}

		return true;
	}

	public boolean back() {
		Point currentPoint = m_points.get(m_points.size() - 1);
		Point previousPoint = m_points.get(m_points.size() - 2);
		return forward(Direction.valueOf(currentPoint, previousPoint));
	}
}
