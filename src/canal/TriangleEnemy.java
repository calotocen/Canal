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
 * 三角敵である。
 */
public class TriangleEnemy extends Enemy {
	/**
	 * 三角敵を生成する。
	 *
	 * @param position 初期位置。
	 */
	public TriangleEnemy(Point position) {
		super(position, Direction.DOWN_RIGHT);
	}

	/**
	 * 移動する。
	 * 具体的には，進行方向へまっすぐ移動する。
	 * 障害物にぶつかったときは，反射するように方向転換する。
	 */
	public void move() {
		boolean moved = move(getDirection());
		if (!moved) {
			setDirection(getReflectionDirection());
			move(getDirection());
		}
	}
}
