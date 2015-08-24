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

import javafx.util.Duration;

/**
 * アプリケーション設定である。
 */
public class Configuration {
	/** ゲーム情報ペインの横幅 */
	public static final int STATUS_BAR_WIDTH = 801;

	/** ゲーム情報ペインの縦幅 */
	public static final int STATUS_BAR_HEIGHT = 45;

	/** 区切りペインの横幅 */
	public static final int PARTITION_WIDTH = 801;

	/** 区切りペインの縦幅 */
	public static final int PARTITION_HEIGHT = 2;

	/** フィールドペインの横幅 */
	public static final int FIELD_WIDTH = 801;

	/** フィールドペインの縦幅 */
	public static final int FIELD_HEIGHT = 501;

	/** 画面の横幅 */
	public static final int SCREEN_WIDTH = FIELD_WIDTH;

	/** 画面の縦幅 */
	public static final int SCREEN_HEIGHT = STATUS_BAR_HEIGHT + PARTITION_HEIGHT + FIELD_HEIGHT;

	/**
	 * ウィンドウボーダーの横幅。
	 * プラットフォーム固有の値であり，サイズ 3 は Windows 8.1 でのものである。
	 */
	public static final int WINDOW_BORDER_WIDTH = 3;

	/**
	 * ウィンドウボーダーの縦幅。
	 * プラットフォーム固有の値であり，サイズ 3 は Windows 8.1 でのものである。
	 */
	public static final int WINDOW_BORDER_HEIGHT = 3;

	/**
	 * タイトルバーの縦幅。
	 * プラットフォーム固有の値であり，サイズ 23 は Windows 8.1 でのものである。
	 */
	public static final int TITLE_BAR_HEIGHT = 23;

	/** フレームレート */
	public static final Duration FRAME_PER_SECOND = new Duration(1_000 / 60);

	/** ライフ数初期値 */
	public static final int INITIAL_LIFE_COUNT = 2;

	/** 残り時間初期値 */
	public static final int INITIAL_REMAINING_TIME = 300 * 1_000;

	/** ライフ 1 つあたりのスコア */
	public static final long SCORE_PER_LIFE = 1_000_000;

	/** 領地 1 つあたりのスコア */
	public static final long SCORE_PER_DOT = 1;

	/** 残り時間 1 ミリ秒あたりのスコア */
	public static final long SCORE_PER_MILLISECOND = 1;

	/** 三角敵の撃破スコア */
	public static final long SCORE_OF_TRIANGLE_ENEMY = 100_000;

	/** 四角敵の撃破スコア */
	public static final long SCORE_OF_SQUARE_ENEMY = 200_000;

	/** 大三角敵の撃破スコア */
	public static final long SCORE_OF_BIG_TRIANGLE_ENEMY = 500_000;

	/** 大四角敵の撃破スコア */
	public static final long SCORE_OF_BIG_SQUARE_ENEMY = 1_000_000;

	/** 大五角敵の撃破スコア */
	public static final long SCORE_OF_BIG_PENTAGON_ENEMY = 2_000_000;

	/** スコアランク S の下限値 */
	public static final long SCORE_BORDER_OF_RANK_S = 14_000_000;

	/** スコアランク A の下限値 */
	public static final long SCORE_BORDER_OF_RANK_A = 12_000_000;

	/** スコアランク B の下限値 */
	public static final long SCORE_BORDER_OF_RANK_B = 10_000_000;

	/** スコラランク C の下限値 */
	public static final long SCORE_BORDER_OF_RANK_C = 8_000_000;

	/** スコアランク D の下限値 */
	public static final long SCORE_BORDER_OF_RANK_D = 6_000_000;
}
