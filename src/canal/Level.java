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

import java.util.Collection;

public class Level {
	Player m_player;
	Territory m_territory;
	Collection<Enemy> m_enemies;

	public Level(Territory territory, Player player, Collection<Enemy> enemies) {
		m_player = player;
		m_territory = territory;
		m_enemies = enemies;
	}

	public Player getPlayer() {
		return m_player;
	}

	public Territory getTerritory() {
		return m_territory;
	}

	public Collection<Enemy> getEnemies() {
		return m_enemies;
	}
}
