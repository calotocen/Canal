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
import java.util.Collections;
import java.util.LinkedList;

public class GameContext {
	public enum State {
		PLAYING,
		LEVEL_CLEAR,
		TIME_UP,
		GAME_OVER,
	}

	private static int st_levelNumber;
	private static State st_state;
	private static int st_lifeCount;
	private static long st_score;
	private static long st_remainingTime;
	private static Player st_player;
	private static Territory st_territory;
	private static ExpeditionLine st_expeditionLine;
	private static LinkedList<Enemy> st_enemies;

	public static void initialize() {
		st_levelNumber = 0;
		st_state = State.PLAYING;
		st_lifeCount = Configuration.INITIAL_LIFE_COUNT;
		st_score = 0;
		st_remainingTime = Configuration.INITIAL_REMAINING_TIME;
		st_player = null;
		st_territory = null;
		st_expeditionLine = null;
		st_enemies = new LinkedList<>();
	}

	public static int getLevelNumber() {
		return st_levelNumber;
	}

	public static void setLevelNumber(int levelNumber) {
		st_levelNumber = levelNumber;
	}

	public static State getState() {
		return st_state;
	}

	public static void setState(State state) {
		st_state = state;
	}

	public static int getLifeCount() {
		return st_lifeCount;
	}

	public static void setLifeCount(int lifeCount) {
		st_lifeCount = lifeCount;
	}

	public static void increaseLifeCount() {
		++st_lifeCount;
	}

	public static void decreaseLifeCount() {
		--st_lifeCount;
	}

	public static long getScore() {
		return st_score;
	}

	public static void setScore(long score) {
		st_score = score;
	}

	public static void addScore(long score) {
		st_score += score;
	}

	public static long getRemainingTime() {
		return st_remainingTime;
	}

	public static void setRemainingTime(long remainingTime) {
		st_remainingTime = remainingTime;
	}

	public static void subtractRemainingTime(long time) {
		st_remainingTime -= time;
		if (st_remainingTime < 0) {
			st_remainingTime = 0;
		}
	}

	public static Player getPlayer() {
		return st_player;
	}

	public static void setPlayer(Player player) {
		st_player = player;
	}

	public static Territory getTerritory() {
		return st_territory;
	}

	public static void setTerritory(Territory territory) {
		st_territory = territory;
	}

	public static ExpeditionLine getExpeditionLine() {
		return st_expeditionLine;
	}

	public static void setExpeditionLine(ExpeditionLine expeditionLine) {
		st_expeditionLine = expeditionLine;
	}

	public static Collection<Enemy> getEnemies() {
		return Collections.unmodifiableList(st_enemies);
	}

	public static void setEnemies(Collection<Enemy> enemies) {
		st_enemies.clear();
		st_enemies.addAll(enemies);
	}

	public static void removeEnemy(Enemy enemy) {
		st_enemies.remove(enemy);
	}
}
