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

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 敵の基底クラスである。
 */
public abstract class Enemy extends Sprite {
	/** 速度 */
	private int m_speed;

	/**
	 * 敵を生成する。
	 *
	 * @param position 初期位置。
	 * @param direction 初期方向。
	 */
	public Enemy(Point position, Direction direction) {
		super(position, direction);
		m_speed = 1;
	}

	/**
	 * 速度を返す。
	 *
	 * @return 速度。
	 */
	protected int getSpeed() {
		return m_speed;
	}

	/**
	 * 速度を設定する。
	 *
	 * @param speed 速度。
	 */
	protected void setSpeed(int speed) {
		m_speed = speed;
	}

	/**
	 * 反射方向を返す。
	 *
	 * @return 反射方向。
	 */
	protected Direction getReflectionDirection() {
		Direction direction = getDirection();
		Point curPosition = getPosition();
		Point newPosition = new Point(curPosition, direction, 1);
		Territory territory = GameContext.getTerritory();

		// 反射方向の計算は，フィールド外にあるか，領地内にあるときのみ行う。
		if (Field.contains(newPosition) && !territory.isTerritory(newPosition)) {
			return Direction.NONE;
		}

		// 衝突方向を判定する。
		boolean horizontalCollision = false;
		boolean verticalCollision = false;
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			// 水平方向からの衝突である。
			horizontalCollision = true;
		} else if (direction == Direction.UP || direction == Direction.DOWN) {
			// 垂直方向からの衝突である。
			verticalCollision = true;
		} else {
			// 斜め方向の衝突である。
			// 水平方向と垂直方向について，それぞれ衝突の有無を判定する。
			if (!Field.containsX(newPosition.getX()) || territory.isTerritory(newPosition.getX(), curPosition.getY())) {
				horizontalCollision = true;
			}
			if (!Field.containsY(newPosition.getY()) || territory.isTerritory(curPosition.getX(), newPosition.getY())) {
				verticalCollision = true;
			}
		}

		// 衝突方向に応じた反射方向を返す。
		if (horizontalCollision && !verticalCollision) {
			return direction.getRetrorseX();
		} else if (!horizontalCollision && verticalCollision) {
			return direction.getRetrorseY();
		} else {
			return direction.getRetrorse();
		}
	}

	/**
	 * 移動する。
	 * 移動経路に遠征線があった場合は，遠征線上で止まる。
	 * 移動先がフィールド外や領地内であった場合は，移動失敗とする。
	 *
	 * @param direction 移動方向。
	 * @return 移動成否。
	 */
	protected boolean move(Direction direction) {
		Point curPosition = getPosition();
		Optional<Point> newPosition;

		// 進路上に遠征線がある場合は，遠征線まで進める。
		final ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		newPosition = Stream.iterate(curPosition, position -> new Point(position, direction, 1)).limit(m_speed)
				.filter(position -> expeditionLine != null && expeditionLine.getPoints().contains(position))
				.findFirst();
		if (newPosition.isPresent()) {
			setPosition(newPosition.get());
			return true;
		}

		// フィールド，または領地にぶつかるまで，進める。
		for (int i = 1; i <= m_speed; i++) {
			Point position = new Point(curPosition, direction, i);
			if (!Field.contains(position) || GameContext.getTerritory().isTerritory(position)) {
				break;
			}
			newPosition = Optional.of(position);
		}
		if (newPosition.isPresent()) {
			setPosition(newPosition.get());
			return true;
		}

		// 指定された方向には，一歩も進めなかった。
		return false;
	}

	/**
	 * 移動する。
	 */
	abstract public void move();
}
