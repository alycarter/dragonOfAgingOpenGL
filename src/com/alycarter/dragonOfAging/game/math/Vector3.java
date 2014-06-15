package com.alycarter.dragonOfAging.game.math;

public class Vector3 {

	private float x;
	private float y;
	private float z;
	
	public Vector3(){
		setX(0);
		setY(0);
		setZ(0);
	} 
	
	public Vector3(float x, float y, float z) {
		setX(x);
		setY(y);
		setZ(z);
	}
	
	public Vector3(Vector3 vector) {
		set(vector);
	}
	
	public void set(Vector3 vector){
		setX(vector.x);
		setY(vector.y);
		setZ(vector.z);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public void add(Vector3 vector){
		x += vector.x;
		y += vector.y;
		z += vector.z;
	}
	
	public void subtract(Vector3 vector){
		x -= vector.x;
		y -= vector.y;
		z -= vector.z;
	}
	
	public void normalize(){
		float length = getLength();
		if(length != 0){
			x /= length;
			y /= length;
			z /= length;
		}
	}
	
	public Vector3 getNormalized(){
		Vector3 vector = new Vector3(this);
		vector.normalize();
		return vector;
	}
	
	public float getLength(){
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)+ Math.pow(z, 2));
	}
	
	public void scale(float scalar){
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}

	public float distanceTo(Vector3 vector){
		Vector3 distance = new Vector3(this);
		distance.subtract(vector);
		return distance.getLength();
	}
	
	public float dot(Vector3 vector){
		return (x*vector.x)+(y*vector.y)+(z*vector.z);
	}
}
