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

public class Territory {
	public enum State {
		WILDERNESS,
		INSIDE,
		WALL,
		ENCLOSURE,
		OUTSIDE,
	}

	private State[][] m_status;
	private int m_size;

	public Territory() {
		m_status = new State[Configuration.FIELD_WIDTH][Configuration.FIELD_HEIGHT];
		each((point, state) -> setState(point, State.WILDERNESS));
	}

	private State getState(int x, int y) {
		return Field.contains(x, y) ? m_status[x][y] : State.OUTSIDE;
	}

	private State getState(Point point) {
		return getState(point.getX(), point.getY());
	}

	private void setState(int x, int y, State state) {
		if (Field.contains(x, y)) {
			m_status[x][y] = state;
		}
	}

	private void setState(Point point, State state) {
		setState(point.getX(), point.getY(), state);
	}

	private boolean isWilderness(int x, int y) {
		return getState(x, y) == State.WILDERNESS;
	}

	private boolean isWilderness(Point point) {
		return isWilderness(point.getX(), point.getY());
	}

	private boolean isInside(int x, int y) {
		return getState(x, y) == State.INSIDE;
	}

	@SuppressWarnings("unused")
	private boolean isInside(Point point) {
		return isInside(point.getX(), point.getY());
	}

	public boolean isWall(int x, int y) {
		return getState(x, y) == State.WALL;
	}

	public boolean isWall(Point point) {
		return isWall(point.getX(), point.getY());
	}

	private boolean isEnclosure(int x, int y) {
		return getState(x, y) == State.ENCLOSURE;
	}

	@SuppressWarnings("unused")
	private boolean isEnclosure(Point point) {
		return isEnclosure(point.getX(), point.getY());
	}

	public boolean isTerritory(int x, int y) {
		return isInside(x, y) || isWall(x, y) || isEnclosure(x, y);
	}

	public boolean isTerritory(Point point) {
		return isTerritory(point.getX(), point.getY());
	}

	private boolean isOutside(int x, int y) {
		return getState(x, y) == State.OUTSIDE;
	}

	@SuppressWarnings("unused")
	private boolean isOutside(Point point) {
		return isOutside(point.getX(), point.getY());
	}

	private boolean isVertex(int x, int y) {
		long count = Stream.of((new Point(x, y)).getPointsOnEightSides())
				.filter(point -> !isTerritory(point))
				.count();
		return (count == 1 || count == 5);
	}

	@SuppressWarnings("unused")
	private boolean isVertex(Point point) {
		return isVertex(point.getX(), point.getY());
	}

	private void each(BiConsumer<Point, State> consumer) {
		IntStream.range(0, Configuration.FIELD_HEIGHT).forEach(y -> {
			IntStream.range(0, Configuration.FIELD_WIDTH).forEach(x -> {
				consumer.accept(new Point(x, y), m_status[x][y]);
			});
		});
	}

	@SuppressWarnings("unused")
	private void each(Point[] points, BiConsumer<Point, State> consumer) {
		Stream.of(points).forEach(point -> consumer.accept(point, m_status[point.getX()][point.getY()]));
	}

	private boolean checkAll(Point[] points, Predicate<State> predicate) {
		return Stream.of(points).allMatch(point -> predicate.test(getState(point)));
	}

	private boolean checkAny(Point[] points, Predicate<State> predicate) {
		return Stream.of(points).anyMatch(point -> predicate.test(getState(point)));
	}

	private void updateState() {
		m_size = 0;
		each((point, state) -> {
			switch (state) {
			case INSIDE:
				if (checkAny(point.getPointsOnEightSides(), s -> (s == State.WILDERNESS || s == State.OUTSIDE))) {
					setState(point, State.WALL);
				}
				m_size++;
				break;
			case WALL:
				if (checkAll(point.getPointsOnEightSides(), s -> (s != State.WILDERNESS && s != State.OUTSIDE))) {
					setState(point, State.INSIDE);
				}
				m_size++;
				break;
			case ENCLOSURE:
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

	public void territorise(Collection<Point> points) {
		if (points.size() == 0) {
			return;
		}

		// 遠征路に柵を設定する。
		points.forEach(point -> setState(point, State.ENCLOSURE));

		// 一時的に壁を柵に変更する。
		Optional<Point> optionalTraversingPoint = points.stream().findFirst();
		while (optionalTraversingPoint.isPresent()) {
			setState(optionalTraversingPoint.get(), State.ENCLOSURE);
			optionalTraversingPoint = Stream.of(optionalTraversingPoint.get().getPointsOnFourSides())
					.filter(point -> isWall(point))
					.findFirst();
		}

		// 壁の内側にある座標を見つける。
		Optional<Point> optionalInnerPoint = Stream.of(points.stream().findFirst().get().getPointsOnSlantingFourSides())
			.filter(point -> isWilderness(point))
			.filter(point -> {
				long count = IntStream.range(point.getX(), Configuration.FIELD_WIDTH)
						.filter(x -> isEnclosure(x, point.getY()))
						.count();
				return count % 2 != 0;
			})
			.findFirst();

		// 壁の内側を自陣で塗りつぶす。
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

	public int getSize() {
		return m_size;
	}
}
