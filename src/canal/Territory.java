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
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 領地である。
 */
public class Territory {
	/**
	 * フィールドの状態である。
	 */
	public enum State {
		/** 荒野 */
		WILDERNESS,

		/** 領地内部 */
		INSIDE,

		/** 壁 */
		WALL,

		/** 柵 */
		ENCLOSURE,

		/** フィールド外 */
		OUTSIDE,
	}

	/** フィールドの状態 */
	private State[][] m_status;

	/** 領地のサイズ */
	private int m_size;

	/**
	 * 領地を生成する。
	 */
	public Territory() {
		m_status = new State[Configuration.FIELD_WIDTH][Configuration.FIELD_HEIGHT];
		each((point, state) -> setState(point, State.WILDERNESS));
	}

	/**
	 * フィールドの状態を返す。
	 *
	 * @param x 横座標。
	 * @param y 縦座標。
	 * @return フィールドの状態。
	 */
	private State getState(int x, int y) {
		return Field.contains(x, y) ? m_status[x][y] : State.OUTSIDE;
	}

	/**
	 * フィールドの状態を返す。
	 *
	 * @param point 座標。
	 * @return フィールドの状態。
	 */
	private State getState(Point point) {
		return getState(point.getX(), point.getY());
	}

	/**
	 * フィールドの状態を設定する。
	 *
	 * @param x 横座標。
	 * @param y 縦座標。
	 * @param state フィールドの状態。
	 */
	private void setState(int x, int y, State state) {
		if (Field.contains(x, y)) {
			m_status[x][y] = state;
		}
	}

	/**
	 * フィールドの状態を設定する。
	 *
	 * @param point 座標。
	 * @param state フィールドの状態。
	 */
	private void setState(Point point, State state) {
		setState(point.getX(), point.getY(), state);
	}

	/**
	 * フィールドの状態が『荒野』であるか調べる。
	 *
	 * @param x 横座標。
	 * @param y 縦座標。
	 * @return フィールドの状態が『荒野』の場合は true，それ以外の場合は false。
	 */
	private boolean isWilderness(int x, int y) {
		return getState(x, y) == State.WILDERNESS;
	}

	/**
	 * フィールドの状態が『荒野』であるか調べる。
	 *
	 * @param point 座標。
	 * @return フィールドの状態が『荒野』の場合は true，それ以外の場合は false。
	 */
	private boolean isWilderness(Point point) {
		return isWilderness(point.getX(), point.getY());
	}

	/**
	 * フィールドの状態が『領地内部』であるか調べる。
	 *
	 * @param x 横座標。
	 * @param y 縦座標。
	 * @return フィールドの状態が『領地内部』の場合は true，それ以外の場合は false。
	 */
	private boolean isInside(int x, int y) {
		return getState(x, y) == State.INSIDE;
	}

	/**
	 * フィールドの状態が『領地内部』であるか調べる。
	 *
	 * @param point 座標。
	 * @return フィールドの状態が『領地内部』の場合は true，それ以外の場合は false。
	 */
	@SuppressWarnings("unused")
	private boolean isInside(Point point) {
		return isInside(point.getX(), point.getY());
	}

	/**
	 * フィールドの状態が『壁』であるか調べる。
	 *
	 * @param x 横座標。
	 * @param y 縦座標。
	 * @return フィールドの状態が『壁』の場合は true，それ以外の場合は false。
	 */
	public boolean isWall(int x, int y) {
		return getState(x, y) == State.WALL;
	}

	/**
	 * フィールドの状態が『壁』であるか調べる。
	 *
	 * @param point 座標。
	 * @return フィールドの状態が『壁』の場合は true，それ以外の場合は false。
	 */
	public boolean isWall(Point point) {
		return isWall(point.getX(), point.getY());
	}

	/**
	 * フィールドの状態が『柵』であるか調べる。
	 *
	 * @param x 横座標。
	 * @param y 縦座標。
	 * @return フィールドの状態が『柵』の場合は true，それ以外の場合は false。
	 */
	private boolean isEnclosure(int x, int y) {
		return getState(x, y) == State.ENCLOSURE;
	}

	/**
	 * フィールドの状態が『柵』であるか調べる。
	 *
	 * @param point 座標。
	 * @return フィールドの状態が『柵』の場合は true，それ以外の場合は false。
	 */
	@SuppressWarnings("unused")
	private boolean isEnclosure(Point point) {
		return isEnclosure(point.getX(), point.getY());
	}

	/**
	 * 領地であるか調べる。
	 *
	 * @param x 横座標。
	 * @param y 縦座標。
	 * @return 領地である場合は true，それ以外の場合は false。
	 */
	public boolean isTerritory(int x, int y) {
		return isInside(x, y) || isWall(x, y) || isEnclosure(x, y);
	}

	/**
	 * 領地であるか調べる。
	 *
	 * @param point 座標。
	 * @return 領地である場合は true，それ以外の場合は false。
	 */
	public boolean isTerritory(Point point) {
		return isTerritory(point.getX(), point.getY());
	}

	/**
	 * フィールド外であるか調べる。
	 *
	 * @param x 横座標。
	 * @param y 縦座標。
	 * @return フィールド外である場合は true，それ以外の場合は false。
	 */
	private boolean isOutside(int x, int y) {
		return getState(x, y) == State.OUTSIDE;
	}

	/**
	 * フィールド外であるか調べる。
	 *
	 * @param point 座標。
	 * @return フィールド外である場合は true，それ以外の場合は false。
	 */
	@SuppressWarnings("unused")
	private boolean isOutside(Point point) {
		return isOutside(point.getX(), point.getY());
	}

	/**
	 * 頂点 (曲がり角) であるかを調べる。
	 * 凸点だけでなく，凹点も頂点とみなす。
	 *
	 * @param x 横座標。
	 * @param y 縦座標。
	 * @return 頂点である場合は true，それ以外の場合は false。
	 */
	private boolean isVertex(int x, int y) {
		long count = Stream.of((new Point(x, y)).getPointsOnEightSides())
				.filter(point -> !isTerritory(point))
				.count();
		return (count == 1 || count == 5);
	}

	/**
	 * 頂点 (曲がり角) であるかを調べる。
	 * 凸点だけでなく，凹点も頂点とみなす。
	 *
	 * @param point 座標。
	 * @return 頂点である場合は true，それ以外の場合は false。
	 */
	@SuppressWarnings("unused")
	private boolean isVertex(Point point) {
		return isVertex(point.getX(), point.getY());
	}

	/**
	 * 領地上のすべての点に対して操作を実行する。
	 *
	 * @param consumer 操作。
	 */
	private void each(BiConsumer<Point, State> consumer) {
		IntStream.range(0, Configuration.FIELD_HEIGHT).forEach(y -> {
			IntStream.range(0, Configuration.FIELD_WIDTH).forEach(x -> {
				consumer.accept(new Point(x, y), m_status[x][y]);
			});
		});
	}

	/**
	 * 点に対して操作を実行する。
	 *
	 * @param points 点。
	 * @param consumer 操作。
	 */
	@SuppressWarnings("unused")
	private void each(Point[] points, BiConsumer<Point, State> consumer) {
		Stream.of(points).forEach(point -> consumer.accept(point, m_status[point.getX()][point.getY()]));
	}

	/**
	 * すべての点がテストをパスするか調べる。
	 *
	 * @param points 点。
	 * @param predicate テスト。
	 * @return 点がすべてテストをパスする場合は true，それ以外の場合は false。
	 */
	private boolean checkAll(Point[] points, Predicate<State> predicate) {
		return Stream.of(points).allMatch(point -> predicate.test(getState(point)));
	}

	/**
	 * 点のうち少なくとも一つがテストをパスするか調べる。
	 *
	 * @param points 点。
	 * @param predicate テスト。
	 * @return 点のうち少なくとも一つがテストをパスする場合は true，それ以外の場合は false。
	 */
	private boolean checkAny(Point[] points, Predicate<State> predicate) {
		return Stream.of(points).anyMatch(point -> predicate.test(getState(point)));
	}

	/**
	 * フィールドの状態を更新する。
	 */
	private void updateState() {
		m_size = 0;
		each((point, state) -> {
			switch (state) {
			case INSIDE:
				// 領地外に接している場合は，状態を壁に変更する。
				if (checkAny(point.getPointsOnEightSides(), s -> (s == State.WILDERNESS || s == State.OUTSIDE))) {
					setState(point, State.WALL);
				}
				m_size++;
				break;
			case WALL:
				// 領地外に接していない場合は，状態を領地内に変更する。
				if (checkAll(point.getPointsOnEightSides(), s -> (s != State.WILDERNESS && s != State.OUTSIDE))) {
					setState(point, State.INSIDE);
				}
				m_size++;
				break;
			case ENCLOSURE:
				// 領地外に接している場合は状態を壁に，接していない場合は状態を領地内に変更する。
				if (checkAny(point.getPointsOnEightSides(), s -> (s == State.WILDERNESS || s == State.OUTSIDE))) {
					setState(point, State.WALL);
				} else {
					setState(point, State.INSIDE);
				}
				m_size++;
				break;
			default:
				// 何もしない。
			}
		});
	}

	/**
	 * 指定された領域を領地を追加する。
	 *
	 * @param region 領域。
	 */
	public void addTerritory(Region region) {
		if (!Field.contains(region)) {
			throw new IndexOutOfBoundsException();
		}

		IntStream.range(0, region.getHeight()).forEach(dy -> {
			IntStream.range(0, region.getWidth()).forEach(dx -> {
				setState(region.getX() + dx, region.getY() + dy, State.INSIDE);
			});
		});

		updateState();
	}

	/**
	 * 指定された点と領地に囲まれた領域を領地化する。
	 *
	 * @param points 点。
	 */
	public void territorise(Collection<Point> points) {
		if (points.size() == 0) {
			return;
		}

		// 遠征線上に柵を設定する。
		points.forEach(point -> setState(point, State.ENCLOSURE));

		// 新しい領地を囲む壁を柵に一時的に変更する。
		// この方法では既存の領地を柵内に含む可能性があるが，含んでも問題ない。
		Optional<Point> optionalTraversingPoint = points.stream().findFirst();
		while (optionalTraversingPoint.isPresent()) {
			setState(optionalTraversingPoint.get(), State.ENCLOSURE);
			optionalTraversingPoint = Stream.of(optionalTraversingPoint.get().getPointsOnFourSides())
					.filter(point -> isWall(point))
					.findFirst();
		}

		// 柵の内側にある座標を見つける。
		// 具体的には遠征線の起点の斜め 4 方向にある点について，
		// そこが『荒野』であれば，そこから右方向に伸ばした線分と柵の交点を数える。
		// 交点が奇数であれば，その点は柵の内側にある。
		Optional<Point> optionalInnerPoint = Stream.of(points.stream().findFirst().get().getPointsOnSlantingFourSides())
				.filter(point -> isWilderness(point))
				.filter(point -> {
					long count = IntStream.range(point.getX(), Configuration.FIELD_WIDTH)
							.filter(x -> isEnclosure(x, point.getY()))
							.count();
					return count % 2 != 0;
				})
				.findFirst();

		// 柵の内側を領地とする。
		optionalInnerPoint.ifPresent(innerPoint -> {
			LinkedList<Point> innerPoints = new LinkedList<>(Collections.singletonList(innerPoint));
			while (!innerPoints.isEmpty()) {
				Point point = innerPoints.removeFirst();
				if (isWilderness(point)) {
					setState(point, State.INSIDE);
					innerPoints.addAll(Stream.of(point.getPointsOnEightSides())
							.filter(p -> isWilderness(p))
							.collect(Collectors.toList()));
				}
			}
		});

		// フィールドを更新する。
		updateState();
	}

	/**
	 * 領地のサイズを返す。
	 *
	 * @return 領地のサイズ。
	 */
	public int getSize() {
		return m_size;
	}
}
