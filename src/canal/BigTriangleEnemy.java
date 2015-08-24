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
 * 大三角敵である。
 */
public class BigTriangleEnemy extends Enemy {
	/**
	 * 大三角敵を生成する。
	 *
	 * @param position 初期位置。
	 */
	public BigTriangleEnemy(Point position) {
		// 初期位置に右下向きに配置する。
		super(position, Direction.DOWN_RIGHT);

		// 速度を 4 に設定する。
		setSpeed(4);
	}

	/**
	 * 移動する。
	 * 具体的には，進行方向へまっすぐ移動する。
	 * 障害物にぶつかったときは，反射するように方向転換する。
	 * ただし，たまに元方向へ戻ることがある。
	 */
	public void move() {
		boolean moved = move(getDirection());
		if (!moved) {
			if (Math.random() < 0.9) {
				setDirection(getReflectionDirection());
			} else {
				setDirection(getDirection().getRetrorse());
			}
			move(getDirection());
		}
	}
}
