package se.afsa.evolutionai.engine;


/**
 * @author Mattias Jönsson
 * A vector in R2 (x, y) with basic operations.
 */
public class Vector2D {
	private double
			x,
			y;
	
	/**
	 * Create a new 2D vector (0, 0).
	 */
	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * Copy the input vector.
	 * @param vector2d - input vector
	 */
	public Vector2D(Vector2D vector2d) {
		this.x = vector2d.getX();
		this.y = vector2d.getY();
	}
	
	/**
	 * Create new 2D vector (x, y).
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Set location of the existing vector to (x, y).
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @return this vector
	 */
	public Vector2D setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	/**
	 * Get x coordinate.
	 * @return x coordinate
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Get y coordinate.
	 * @return y coordinate
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Add a vector to this vector.
	 * @param vector2d - the vector to add
	 * @return this vector
	 */
	public Vector2D add(Vector2D vector2d) {
		x += vector2d.getX();
		y += vector2d.getY();
		return this;
	}
	
	/**
	 * Subtracts a vector from this vector.
	 * @param vector2d / the vector to subtract
	 * @return this vector
	 */
	public Vector2D subtract(Vector2D vector2d) {
		return add(vector2d.getInverted());
	}
	
	/**
	 * Multiply every element in the vector 
	 * Example: (x, y) weight c => (cx, cy)
	 * @param weight - the c value
	 * @return this vector
	 */
	public Vector2D weight(double weight) {
		x = x * weight;
		y = y * weight;
		return this;
	}
	
	/**
	 * Calculates the dot-product between this and another vector.
	 * @param vector2d - the other vector
	 * @return the dot product
	 */
	public double dot(Vector2D vector2d) {
		return x * vector2d.getX() + y * vector2d.getY();
	}
	
	/**
	 * Calculate the length of a vector.
	 * @return the length to the origin
	 */
	public double length() {
		return distanceTo(new Vector2D(0, 0));
	}
	
	/**
	 * Calculate the distance between this vector and another vector |u-v|.
	 * @param vector2d - the other vector
	 * @return the distance between the vectors.
	 */
	public double distanceTo(Vector2D vector2d) {
		return Math.sqrt(Math.pow(x - vector2d.getX(), 2) + Math.pow(y - vector2d.getY(), 2));
	}
	
	/**
	 * Get the vector from another vector to this.
	 * @param vector2d - the other vector
	 * @return the vector between them pointing to the other vector
	 */
	public Vector2D from(Vector2D vector2d) {
		return vector2d.copy().subtract(this);
	}
	
	/**
	 * Normalize the vector so the length is 1.
	 * @return this vector
	 */
	public Vector2D normalize() {
		double length = length();
		x = (length != 0) ? x/length : 0;
		y = (length != 0) ? y/length : 0;
		return this;
	}
	
	/**
	 * Invert the vector to (-x, -y).
	 * @return this vector
	 */
	public Vector2D invert() {
		x = -x;
		y = -y;
		return this;
	}
	
	/**
	 * Conjugate the vector to (x, -y).
	 * @return this vector
	 */
	public Vector2D conjugate() {
		y = -y;
		return this;
	}
	
	/**
	 * Get the inverse of this vector.
	 * @return a new vector that is the inverse of this
	 */
	public Vector2D getInverted() {
		return this.copy().invert();
	}
	
	/**
	 * Get the conjugate of this vector.
	 * @return a new vector that is the conjugate of this
	 */
	public Vector2D getConjugate() {
		return this.copy().conjugate();
	}
	
	/**
	 * Get the angle of the vector.
	 * @return the angle
	 */
	public double getAngle() {
		double xDir = (x < 0) ? Math.PI : 0;
		return (x == 0) ? Math.signum(y)*Math.PI/2 : Math.atan(y/x) + xDir;
	}
	
	/**
	 * Copy the vector.
	 * @return a copy of this
	 */
	public Vector2D copy() {
		return new Vector2D(this);
	}
	
	public Vector2D quantize() {
		double length = length();
		double angle = Math.PI * Math.round(4 * getAngle() / Math.PI) / 4;
		x = Math.cos(angle);
		y = Math.sin(angle);
		weight(length);
		return this;
	}
	
	@Override
	public String toString() {
		return "X: " + x + " Y: " + y;
	}
}
