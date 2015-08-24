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

import java.util.Arrays;
import java.util.LinkedList;

/**
 * レベルの集合である。
 */
public class Levels {
	/**
	 * レベル番号に対応するレベルを返す。
	 *
	 * @param level レベル番号。
	 * @return レベル。
	 */
	public static Level get(int level) {
		Player player = null;
		Territory territory = null;
		Enemy[] enemies = null;

		switch (level) {
		case 1:
			// レベル 1
			player = new Player(new Point(170, 80));
			territory = new Territory();
			territory.addTerritory(new Region(170, 80, 21, 21));
			territory.addTerritory(new Region(190, 80, 21, 101));
			territory.addTerritory(new Region(170, 180, 61, 21));
			enemies = new Enemy[] {
					new TriangleEnemy(new Point(300, 300)),
			};
			break;

		case 2:
			// レベル 2
			player = new Player(new Point(600, 100));
			territory = new Territory();
			territory.addTerritory(new Region(600, 100, 101, 21));
			territory.addTerritory(new Region(680, 100, 21, 61));
			territory.addTerritory(new Region(600, 160, 101, 21));
			territory.addTerritory(new Region(600, 160, 21, 61));
			territory.addTerritory(new Region(600, 220, 101, 21));
			enemies = new Enemy[] {
					new SquareEnemy(new Point(300, 100)),
			};
			break;

		case 3:
			// レベル 3
			player = new Player(new Point(200, 300));
			territory = new Territory();
			territory.addTerritory(new Region(200, 300, 101, 21));
			territory.addTerritory(new Region(280, 300, 21, 121));
			territory.addTerritory(new Region(200, 360, 101, 21));
			territory.addTerritory(new Region(200, 420, 101, 21));
			enemies = new Enemy[] {
					new BigTriangleEnemy(new Point(600, 300)),
					new TriangleEnemy(new Point(100, 100)),
					new TriangleEnemy(new Point(400, 400)),
			};
			break;

		case 4:
			// レベル 4
			player = new Player(new Point(600, 240));
			territory = new Territory();
			territory.addTerritory(new Region(600, 240, 21, 61));
			territory.addTerritory(new Region(600, 300, 101, 21));
			territory.addTerritory(new Region(650, 240, 21, 121));
			enemies = new Enemy[] {
					new BigSquareEnemy(new Point(600, 300)),
					new SquareEnemy(new Point(100, 100)),
					new SquareEnemy(new Point(400, 400)),
			};
			break;

		case 5:
			// レベル 5
			player = new Player(new Point(350, 170));
			territory = new Territory();
			territory.addTerritory(new Region(350, 170, 101, 21));
			territory.addTerritory(new Region(430, 230, 21, 61));
			territory.addTerritory(new Region(350, 230, 101, 21));
			territory.addTerritory(new Region(350, 170, 21, 61));
			territory.addTerritory(new Region(350, 290, 101, 21));
			enemies = new Enemy[] {
					new BigPentagonEnemy(new Point(349, 291)),
					new BigTriangleEnemy(new Point(100, 100)),
					new BigSquareEnemy(new Point(400, 400)),
			};
			break;
		}

		return new Level(territory, player, new LinkedList<Enemy>(Arrays.asList(enemies)));
	}

	/**
	 * レベルの個数を返す。
	 *
	 * @return レベルの個数。
	 */
	public static int size() {
		return 5;
	}
}
