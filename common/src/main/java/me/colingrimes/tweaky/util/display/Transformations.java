package me.colingrimes.tweaky.util.display;

import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;

public final class Transformations {

	/**
	 * Constructs a very basic {@link Transformation} with standard default values.
	 *
	 * @return the basic transformation
	 */
	@Nonnull
	public static Transformation basic() {
		return new Builder().build();
	}

	/**
	 * Constructs a {@link Builder} object.
	 *
	 * @return the transformation builder
	 */
	@Nonnull
	public static Builder create() {
		return new Builder();
	}

	/**
	 * Constructs a {@link Builder} object with a default translation.
	 *
	 * @param translation the translation
	 * @return the transformation builder
	 */
	@Nonnull
	public static Builder of(@Nonnull Vector3f translation) {
		return new Builder().translate(translation);
	}

	/**
	 * Constructs a {@link Builder} object with a default translation.
	 *
	 * @param x the x value
	 * @param y the y value
	 * @param z the z value
	 * @return the transformation builder
	 */
	@Nonnull
	public static Builder of(double x, double y, double z) {
		return new Builder().translate(x, y, z);
	}

	/**
	 * Builder used to simplify the creation of {@link Transformation} objects.
	 */
	public static class Builder {
		private Vector3f translation = new Vector3f(0, 0, 0);
		private Quaternionf leftRotation = new Quaternionf();
		private Vector3f scale = new Vector3f(1, 1, 1);
		private Quaternionf rightRotation = new Quaternionf();

		@Nonnull
		public Builder translate(@Nonnull Vector3f translation) {
			this.translation = translation;
			return this;
		}

		@Nonnull
		public Builder translate(double x, double y, double z) {
			return translate(new Vector3f((float) x, (float) y, (float) z));
		}

		@Nonnull
		public Builder left(@Nonnull Quaternionf leftRotation) {
			this.leftRotation = leftRotation;
			return this;
		}

		@Nonnull
		public Builder leftRotateX(double x) {
			this.leftRotation.rotateX((float) Math.toRadians(x));
			return this;
		}

		@Nonnull
		public Builder leftRotateY(double y) {
			this.leftRotation.rotateY((float) Math.toRadians(y));
			return this;
		}

		@Nonnull
		public Builder leftRotateZ(double z) {
			this.leftRotation.rotateZ((float) Math.toRadians(z));
			return this;
		}

		@Nonnull
		public Builder scale(@Nonnull Vector3f scale) {
			this.scale = scale;
			return this;
		}

		@Nonnull
		public Builder scale(double x, double y, double z) {
			this.scale = new Vector3f((float) x, (float) y, (float) z);
			return this;
		}

		@Nonnull
		public Builder right(@Nonnull Quaternionf rightRotation) {
			this.rightRotation = rightRotation;
			return this;
		}

		@Nonnull
		public Builder rightRotateX(double x) {
			this.rightRotation.rotateX((float) Math.toRadians(x));
			return this;
		}

		@Nonnull
		public Builder rightRotateY(double y) {
			this.rightRotation.rotateY((float) Math.toRadians(y));
			return this;
		}

		@Nonnull
		public Builder rightRotateZ(double z) {
			this.rightRotation.rotateZ((float) Math.toRadians(z));
			return this;
		}

		@Nonnull
		public Transformation build() {
			this.leftRotation.normalize();
			this.rightRotation.normalize();
			return new Transformation(
					translation,
					leftRotation,
					scale,
					rightRotation
			);
		}
	}

	private Transformations() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}