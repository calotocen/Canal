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

public class Field {
	public static boolean containsX(int x) {
		return (0 <= x && x < Configuration.FIELD_WIDTH);
	}

	public static boolean containsY(int y) {
		return (0 <= y && y < Configuration.FIELD_HEIGHT);
	}

	public static boolean contains(int x, int y) {
		return (containsX(x) && containsY(y));
	}

	public static boolean contains(Point point) {
		return contains(point.getX(), point.getY());
	}

	public static boolean contains(Region region) {
		return contains(region.getX(), region.getY())
				&& contains(region.getX() + region.getWidth() - 1, region.getY() + region.getHeight() - 1);
	}
}
