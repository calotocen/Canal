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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.stream.Stream;

/**
 * 大四角敵である。
 */
public class BigSquareEnemy extends Enemy {
	/**
	 * 自機探索ループ回数の上限値。
	 * この値を増やせば自機探索速度が上がり，減じれば処理速度が向上する。
	 */
	private static final int LOOP_COUNT = 500;

	/**
	 * 追跡中フラグ。
	 * 追跡中の場合は true，それ以外の場合は false。
	 */
	private boolean m_chasing;

	/**
	 * 追跡レベル。値の意味は次の通りである。
	 * <ul>
	 *     <li>0 ... 未追跡</li>
	 *     <li>1 ... 追跡 (経路未発見)</li>
	 *     <li>2 ... 追跡 (経路発見済)</li>
	 * </ul>
	 */
	private int m_chaseLevel;

	/** 走査対象 */
	private PriorityQueue<Point> m_traversingPoints;

	/**
	 * 遠征線起点への経路。
	 * キーは対象位置，値は遠征線起点方向にある近接点である。
	 */
	private HashMap<Point, Point> m_routeToTarget;

	/**
	 * 大四角敵を生成する。
	 *
	 * @param position 初期位置。
	 */
	public BigSquareEnemy(Point position) {
		// 初期位置に右下向きに配置する。
		super(position, Direction.DOWN_RIGHT);

		// 速度を 2 に設定する。
		setSpeed(2);

		// 追跡情報を初期化する。
		m_chasing = false;
		m_chaseLevel = 0;
		m_traversingPoints = null;
		m_routeToTarget = null;
	}

	/**
	 * 追跡レベルを返す。
	 *
	 * @return 追跡レベル。
	 */
	public int getChaseLevel() {
		return m_chaseLevel;
	}

	/**
	 * 移動する。
	 * 自機が遠征している場合，自機を追う。
	 * 自機が領地内にいる場合，進行方向へまっすぐ移動する。
	 * なお，障害物にぶつかったときは，ランダムに向きを変える。
	 */
	public void move() {
		m_chaseLevel = 0;

		ExpeditionLine expeditionLine = GameContext.getExpeditionLine();
		if (expeditionLine != null) {
			Point curPosition = getPosition();
			Point playerPosition = GameContext.getPlayer().getPosition();

			// 直接，自機を追える状態 (自機との間に領地がない状態) か調べる。
			// 具体的には，自機までの経路を洗い出し，その経路上に領地が含まれないことを確認する。
			boolean chaseDirectly = Stream.iterate(curPosition, position -> new Point(position, Direction.valueOf(position, playerPosition), 1))
					.limit(Math.max(Math.abs(curPosition.getX() - playerPosition.getX()), Math.abs(curPosition.getY() - playerPosition.getY())))
					.allMatch(position -> !GameContext.getTerritory().isTerritory(position));

			// 自機との間に領地がない場合は，直接，自機を追う。
			// 領地がある場合は，迂回路を走査する。
			if (chaseDirectly) {
				// 移動方向を自機がいる方向に変更し，追跡レベルを『追跡 (経路発見済)』に変更する。
				setDirection(Direction.valueOf(curPosition, playerPosition));
				m_chaseLevel = 2;
			} else {
				// 初回走査時は，追跡情報を初期化する。
				if (!m_chasing) {
					// 走査対象を初期化する。
					m_traversingPoints = new PriorityQueue<>(
							Configuration.FIELD_WIDTH * Configuration.FIELD_HEIGHT,
							(point1, point2) -> Double.compare(playerPosition.getDistance(point1), playerPosition.getDistance(point2)));
					m_traversingPoints.add(playerPosition);

					// 遠征線起点への経路を初期化する。
					m_routeToTarget = new HashMap<>();

					// 追跡中フラグを『追跡中』に変更する。
					m_chasing = true;
				}

				if (!m_routeToTarget.containsKey(curPosition)) {
					// 大四角敵から遠征線起点への経路を探索する。
					// 具体的には，遠征線起点を起点として大四角敵の位置を走査する。
					// 大四角敵を走査の起点としないのは，本関数呼び出しごとにその位置が変わるためである。
					for (int i = 0; i < LOOP_COUNT && m_traversingPoints.size() > 0; i++) {
						// 走査対象から位置を取り出す。
						Point targetPoint = m_traversingPoints.poll();
						if (targetPoint.equals(curPosition)) {
							break;
						}

						// 走査対象の近接八点のうち，
						// フィールド上にあり，領地内でなく，すでに探索済みでない点を，
						// 走査対象に加え，遠征線起点への経路を記録する。
						Stream.of(targetPoint.getPointsOnEightSides())
								.filter(point -> Field.contains(point))
								.filter(point -> !GameContext.getTerritory().isTerritory(point))
								.filter(point -> !m_routeToTarget.containsKey(point))
								.forEach(point -> {
									m_traversingPoints.add(point);
									m_routeToTarget.put(point, targetPoint);
								});
					}
				}
				if (!m_routeToTarget.containsKey(curPosition)) {
					// 遠征線起点への経路が見つからなかったので，
					// とりあえず，自機に近づく方向へ移動する。
					setDirection(Direction.valueOf(curPosition, playerPosition));
					m_chaseLevel = 1;
				} else {
					// 遠征線起点への経路が見つかったので，その方向へ移動する。
					setDirection(Direction.valueOf(curPosition, m_routeToTarget.get(curPosition)));
					m_chaseLevel = 2;
				}
			}
		} else if (m_chasing) {
			// 自機は領地内にいるので追うのを止め，追跡情報を初期化する。
			m_traversingPoints = null;
			m_routeToTarget = null;
			m_chasing = false;
		}

		// 移動する。
		// 移動に失敗した場合は，ランダムに向きを変えて移動可能な方向へ移動する。
		boolean moved = move(getDirection());
		if (!moved) {
			LinkedList<Direction> directions = new LinkedList<>(Arrays.asList(Direction.valuesOfSlantingFourDirection()));
			Collections.shuffle(directions);
			for (Direction direction : directions) {
				if (move(direction)) {
					setDirection(direction);
					break;
				}
			}
		}
	}
}
