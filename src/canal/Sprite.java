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
 * スプライトの基底クラスである。
 */
public class Sprite {
	/** 位置 */
	private Point m_position;

	/** 向き */
	private Direction m_direction;

	/**
	 * 生存状態。
	 * true ならば『生存』，false ならば『撃墜』を表す。
	 */
	private boolean m_live;

	/**
	 * スプライトを生成する。
	 * 生成したスプライトの生存状態は『生存』である。
	 *
	 * @param position 位置。
	 * @param direction 向き。
	 */
	public Sprite(Point position, Direction direction) {
		m_position = position;
		m_direction = direction;
		m_live = true;
	}

	/**
	 * 位置を返す。
	 *
	 * @return 位置。
	 */
	public Point getPosition() {
		return m_position;
	}

	/**
	 * 位置を設定する。
	 *
	 * @param position 位置。
	 */
	public void setPosition(Point position) {
		m_position = position;
	}

	/**
	 * 向きを返す。
	 *
	 * @return 向き。
	 */
	public Direction getDirection() {
		return m_direction;
	}

	/**
	 * 向きを設定する。
	 *
	 * @param direction 向き。
	 */
	public void setDirection(Direction direction) {
		m_direction = direction;
	}

	/**
	 * 生存状態を返す。
	 *
	 * @return 生存状態。『生存』ならば true，『撃墜』ならば false。
	 */
	public boolean isLive() {
		return m_live;
	}

	/**
	 * 生存状態を設定する。
	 *
	 * @param live 生存状態。
	 *             『生存』ならば true，『撃墜』ならば false を指定する。
	 */
	public void setLive(boolean live) {
		m_live = live;
	}
}
