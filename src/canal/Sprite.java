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

public class Sprite {
	private Point m_position;
	private Direction m_direction;
	private boolean m_live;

	public Sprite(Point position, Direction direction) {
		m_position = position;
		m_direction = direction;
		m_live = true;
	}

	public Point getPosition() {
		return m_position;
	}

	public void setPosition(Point position) {
		m_position = position;
	}

	public Direction getDirection() {
		return m_direction;
	}

	public void setDirection(Direction direction) {
		m_direction = direction;
	}

	public boolean isLive() {
		return m_live;
	}

	public void setLive(boolean live) {
		m_live = live;
	}
}
