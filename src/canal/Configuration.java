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

public class Configuration {
	public static final int STATUS_BAR_WIDTH = 801;
	public static final int STATUS_BAR_HEIGHT = 45;

	public static final int PARTITION_WIDTH = 801;
	public static final int PARTITION_HEIGHT = 2;

	public static final int FIELD_WIDTH = 801;
	public static final int FIELD_HEIGHT = 501;

	public static final int SCREEN_WIDTH = FIELD_WIDTH;
	public static final int SCREEN_HEIGHT = STATUS_BAR_HEIGHT + PARTITION_HEIGHT + FIELD_HEIGHT;

	public static final int WINDOW_BORDER_WIDTH = 3;
	public static final int WINDOW_BORDER_HEIGHT = 3;

	public static final int TITLE_BAR_HEIGHT = 23;

	public static final Duration FRAME_PER_SECOND = new Duration(1_000 / 60);

	public static final int INITIAL_LIFE_COUNT = 2;

	public static final int INITIAL_REMAINING_TIME = 300 * 1_000;

	public static final long SCORE_PER_LIFE = 1_000_000;

	public static final long SCORE_PER_DOT = 1;

	public static final long SCORE_PER_MILLISECOND = 1;

	public static final long SCORE_OF_TRIANGLE_ENEMY = 100_000;
	public static final long SCORE_OF_SQUARE_ENEMY = 200_000;
	public static final long SCORE_OF_BIG_TRIANGLE_ENEMY = 500_000;
	public static final long SCORE_OF_BIG_SQUARE_ENEMY = 1_000_000;
	public static final long SCORE_OF_BIG_PENTAGON_ENEMY = 2_000_000;

	public static final long SCORE_BORDER_OF_RANK_S = 14_000_000;
	public static final long SCORE_BORDER_OF_RANK_A = 12_000_000;
	public static final long SCORE_BORDER_OF_RANK_B = 10_000_000;
	public static final long SCORE_BORDER_OF_RANK_C = 8_000_000;
	public static final long SCORE_BORDER_OF_RANK_D = 6_000_000;
}
