package com.alycarter.dragonOfAging.game.graphics;

import java.awt.Color;

public class FloatColor {

	private float r;
	private float g;
	private float b;
	private float a;

	public static FloatColor WHITE = new FloatColor(1.0f, 1.0f, 1.0f, 1.0f);
	public static FloatColor BLACK = new FloatColor(0.0f, 0.0f, 0.0f, 1.0f);
	public static FloatColor RED = new FloatColor(1.0f, 0.0f, 0.0f, 1.0f);
	public static FloatColor GREEN = new FloatColor(0.0f, 1.0f, 0.0f, 1.0f);
	public static FloatColor BLUE = new FloatColor(0.0f, 0.0f, 1.0f, 1.0f);
	public static FloatColor YELLOW = new FloatColor(1.0f, 1.0f, 0.0f, 1.0f);
	public static FloatColor PINK = new FloatColor(1.0f, 0.0f, 1.0f, 1.0f);
	public static FloatColor CYAN = new FloatColor(0.0f, 1.0f, 1.0f, 1.0f);
	public static FloatColor GREY = new FloatColor(0.5f, 0.5f, 0.5f, 1.0f);
	
	
	public FloatColor(float r, float g, float b, float a) {
		setR(r);
		setG(g);
		setB(b);
		setA(a);
	}
	
	public FloatColor(int r, int g, int b, int a) {
		setR((float)r/255f);
		setG((float)g/255f);
		setB((float)b/255f);
		setA((float)a/255f);
	}
	
	public FloatColor(Color color) {
		setR((float)color.getRed()/255f);
		setG((float)color.getGreen()/255f);
		setB((float)color.getBlue()/255f);
		setA((float)color.getAlpha()/255f);
	}
	
	public float getR() {
		return r;
	}

	public void setR(float r) {
		if(r < 0){
			r = 0;
		}else{
			if(r>1){
				r=1;
			}
		}
		this.r = r;
	}

	public float getG() {
		return g;
	}

	public void setG(float g) {
		if(g < 0){
			g = 0;
		}else{
			if(g>1){
				g=1;
			}
		}
		this.g = g;
	}
	public float getB() {
		return b;
	}

	public void setB(float b) {
		if(b < 0){
			b = 0;
		}else{
			if(b>1){
				b=1;
			}
		}
		this.b = b;
	}
	public float getA() {
		return a;
	}

	public void setA(float a) {
		if(a < 0){
			a = 0;
		}else{
			if(a>1){
				a=1;
			}
		}
		this.a = a;
	}
}
