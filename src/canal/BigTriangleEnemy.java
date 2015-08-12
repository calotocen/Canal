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


public class BigTriangleEnemy extends Enemy {
	public BigTriangleEnemy(Point position) {
		super(position, Direction.DOWN_RIGHT);
		setSpeed(4);
	}

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
