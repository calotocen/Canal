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

/**
 * ゲームコンテキストである。
 */
public class GameContext {
	/**
	 * ゲーム状態である。
	 */
	public enum State {
		/** プレイ中 */
		PLAYING,

		/** レベルクリア */
		LEVEL_CLEAR,

		/** 時間切れ */
		TIME_UP,

		/** ゲームオーバー */
		GAME_OVER,
	}

	/** レベル番号 */
	private static int st_levelNumber;

	/** ゲーム状態 */
	private static State st_state;

	/** ライフ数 */
	private static int st_lifeCount;

	/** スコア */
	private static long st_score;

	/** 残り時間 */
	private static long st_remainingTime;

	/** 自機 */
	private static Player st_player;

	/** 領地 */
	private static Territory st_territory;

	/** 遠征線 */
	private static ExpeditionLine st_expeditionLine;

	/** 敵 */
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

	/**
	 * レベル番号を返す。
	 *
	 * @return レベル番号。
	 */
	public static int getLevelNumber() {
		return st_levelNumber;
	}

	/**
	 * レベル番号を設定する。
	 *
	 * @param levelNumber レベル番号。
	 */
	public static void setLevelNumber(int levelNumber) {
		st_levelNumber = levelNumber;
	}

	/**
	 * ゲーム状態を返す。
	 *
	 * @return ゲーム状態。
	 */
	public static State getState() {
		return st_state;
	}

	/**
	 * ゲーム状態を設定する。
	 *
	 * @param state ゲーム状態。
	 */
	public static void setState(State state) {
		st_state = state;
	}

	/**
	 * ライフ数を返す。
	 *
	 * @return ライフ数。
	 */
	public static int getLifeCount() {
		return st_lifeCount;
	}

	/**
	 * ライフ数を設定する。
	 *
	 * @param lifeCount ライフ数。
	 */
	public static void setLifeCount(int lifeCount) {
		st_lifeCount = lifeCount;
	}

	/**
	 * ライフ数を 1 つ増やす。
	 */
	public static void increaseLifeCount() {
		++st_lifeCount;
	}

	/**
	 * ライフ数を 1 つ減らす。
	 */
	public static void decreaseLifeCount() {
		--st_lifeCount;
	}

	/**
	 * スコアを返す。
	 *
	 * @return スコア。
	 */
	public static long getScore() {
		return st_score;
	}

	/**
	 * スコアを設定する。
	 *
	 * @param score スコア。
	 */
	public static void setScore(long score) {
		st_score = score;
	}

	/**
	 * スコアを加える。
	 *
	 * @param score 加えるスコア。
	 */
	public static void addScore(long score) {
		st_score += score;
	}

	/**
	 * 残り時間を返す。
	 *
	 * @return 残り時間。
	 */
	public static long getRemainingTime() {
		return st_remainingTime;
	}

	/**
	 * 残り時間を設定する。
	 *
	 * @param remainingTime 残り時間。
	 */
	public static void setRemainingTime(long remainingTime) {
		st_remainingTime = remainingTime;
	}

	/**
	 * 残り時間を減らす。
	 *
	 * @param time 減じる時間。
	 */
	public static void subtractRemainingTime(long time) {
		st_remainingTime -= time;
		if (st_remainingTime < 0) {
			st_remainingTime = 0;
		}
	}

	/**
	 * 自機を返す。
	 *
	 * @return 自機。
	 */
	public static Player getPlayer() {
		return st_player;
	}

	/**
	 * 自機を設定する。
	 *
	 * @param player 自機。
	 */
	public static void setPlayer(Player player) {
		st_player = player;
	}

	/**
	 * 領地を返す。
	 *
	 * @return 領地。
	 */
	public static Territory getTerritory() {
		return st_territory;
	}

	/**
	 * 領地を設定する。
	 *
	 * @param territory 領地。
	 */
	public static void setTerritory(Territory territory) {
		st_territory = territory;
	}

	/**
	 * 遠征線を返す。
	 *
	 * @return 遠征線。
	 */
	public static ExpeditionLine getExpeditionLine() {
		return st_expeditionLine;
	}

	/**
	 * 遠征線を設定する。
	 *
	 * @param expeditionLine 遠征線。
	 */
	public static void setExpeditionLine(ExpeditionLine expeditionLine) {
		st_expeditionLine = expeditionLine;
	}

	/**
	 * 敵を返す。
	 *
	 * @return 敵。
	 */
	public static Collection<Enemy> getEnemies() {
		return Collections.unmodifiableList(st_enemies);
	}

	/**
	 * 敵を設定する。
	 *
	 * @param enemies 敵。
	 */
	public static void setEnemies(Collection<Enemy> enemies) {
		st_enemies.clear();
		st_enemies.addAll(enemies);
	}

	/**
	 * 敵を除外する。
	 *
	 * @param enemy 除外対象。
	 */
	public static void removeEnemy(Enemy enemy) {
		st_enemies.remove(enemy);
	}
}
