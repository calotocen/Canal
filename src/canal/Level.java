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

/**
 * レベル (自機，領地，および敵の初期配置) である。
 */
public class Level {
	/** 自機 */
	private Player m_player;

	/** 領地 */
	private Territory m_territory;

	/** 敵 */
	private Collection<Enemy> m_enemies;

	/**
	 * レベルを生成する。
	 *
	 * @param territory 領地。
	 * @param player 自機。
	 * @param enemies 敵。
	 */
	public Level(Player player, Territory territory, Collection<Enemy> enemies) {
		m_player = player;
		m_territory = territory;
		m_enemies = enemies;
	}

	/**
	 * 自機を返す。
	 *
	 * @return 自機。
	 */
	public Player getPlayer() {
		return m_player;
	}

	/**
	 * 領地を返す。
	 *
	 * @return 領地。
	 */
	public Territory getTerritory() {
		return m_territory;
	}

	/**
	 * 敵を返す。
	 *
	 * @return 敵。
	 */
	public Collection<Enemy> getEnemies() {
		return m_enemies;
	}
}
