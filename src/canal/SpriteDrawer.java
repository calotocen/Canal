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

import java.util.EnumMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class SpriteDrawer {
	static EnumMap<Direction, double[]> st_triangleXs = new EnumMap<>(Direction.class);
	static EnumMap<Direction, double[]> st_triangleYs = new EnumMap<>(Direction.class);
	static EnumMap<Direction, double[]> st_squareXs = new EnumMap<>(Direction.class);
	static EnumMap<Direction, double[]> st_squareYs = new EnumMap<>(Direction.class);
	static EnumMap<Direction, double[]> st_pentagonXs = new EnumMap<>(Direction.class);
	static EnumMap<Direction, double[]> st_pentagonYs = new EnumMap<>(Direction.class);

	private Sprite m_sprite;
	private int m_size;
	private Paint m_fill;

	static {
		double[] baseTriangleXs = { 0, 0.8660254037844386467637, -0.8660254037844386467637 };
		double[] baseTriangleYs = { -1, 0.5, 0.5 };
		Stream.of(Direction.values()).forEach(direction -> {
			double angle = direction.getAngle();
			st_triangleXs.put(direction, getRotateXs(baseTriangleXs, baseTriangleYs, angle));
			st_triangleYs.put(direction, getRotateYs(baseTriangleXs, baseTriangleYs, angle));
		});

		double[] baseSquareXs = { 0, 1, 0, -1 };
		double[] baseSquareYs = { -1, 0, 1, 0 };
		Stream.of(Direction.values()).forEach(direction -> {
			double angle = direction.getAngle();
			st_squareXs.put(direction, getRotateXs(baseSquareXs, baseSquareYs, angle));
			st_squareYs.put(direction, getRotateYs(baseSquareXs, baseSquareYs, angle));
		});

		double[] basePentagonXs = { 0, 0.9510565162951535721164, 0.5877852522924731291687, -0.5877852522924731291687, -0.9510565162951535721164 };
		double[] basePentagonYs = { -1, -0.3090169943749474241023, 0.8090169943749474241023, 0.8090169943749474241023, -0.3090169943749474241023 };
		Stream.of(Direction.values()).forEach(direction -> {
			double angle = direction.getAngle();
			st_pentagonXs.put(direction, getRotateXs(basePentagonXs, basePentagonYs, angle));
			st_pentagonYs.put(direction, getRotateYs(basePentagonXs, basePentagonYs, angle));
		});
	}

	private static double[] getRotateXs(double[] xs, double[] ys, double angle) {
		return IntStream.range(0, xs.length).asDoubleStream()
				.map(i -> xs[(int) i] * Math.cos(angle) - ys[(int) i] * Math.sin(angle))
				.toArray();
	}

	private static double[] getRotateYs(double[] xs, double[] ys, double angle) {
		return IntStream.range(0, ys.length).asDoubleStream()
				.map(i -> xs[(int) i] * Math.sin(angle) + ys[(int) i] * Math.cos(angle))
				.toArray();
	}

	public SpriteDrawer(Sprite sprite) {
		m_sprite = sprite;
		m_size = 7;
		m_fill = Color.WHITE;
	}

	public Sprite getSprite() {
		return m_sprite;
	}

	public void setSprite(Sprite sprite) {
		m_sprite = sprite;
	}

	public int getSize() {
		return m_size;
	}

	public void setSize(int size) {
		m_size = size;
	}

	public Paint getFill() {
		return m_fill;
	}

	public void setFill(Color fill) {
		m_fill = fill;
	}

	protected void draw(GraphicsContext gc, double[] xs, double[] ys) {
		gc.setFill(m_fill);

		Point position = m_sprite.getPosition();
		double[] pointXs = IntStream.range(0, xs.length).asDoubleStream().map(i -> position.getX() + xs[(int) i] * m_size).toArray();
		double[] pointYs = IntStream.range(0, ys.length).asDoubleStream().map(i -> position.getY() + ys[(int) i] * m_size).toArray();
		gc.fillPolygon(pointXs, pointYs, xs.length);
	}

	protected void drawTriangle(GraphicsContext gc) {
		Direction direction = m_sprite.getDirection();
		draw(gc, st_triangleXs.get(direction), st_triangleYs.get(direction));
	}

	protected void drawSquare(GraphicsContext gc) {
		Direction direction = m_sprite.getDirection();
		draw(gc, st_squareXs.get(direction), st_squareYs.get(direction));
	}

	protected void drawPentagon(GraphicsContext gc) {
		Direction direction = m_sprite.getDirection();
		draw(gc, st_pentagonXs.get(direction), st_pentagonYs.get(direction));
	}

	protected void drawCircle(GraphicsContext gc) {
		Point point = m_sprite.getPosition();
		gc.setFill(m_fill);
		gc.fillOval(point.getX() - m_size / 2, point.getY() - m_size / 2, m_size, m_size);
	}

	public abstract void draw(GraphicsContext gc);
}
