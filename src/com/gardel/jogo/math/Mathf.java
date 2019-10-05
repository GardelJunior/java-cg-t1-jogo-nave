package com.gardel.jogo.math;

import java.util.Random;

public class Mathf {
	public static final float PI = 3.14159265359f;
	public static final float HALF_PI = 3.14159265359f/2;
	public static final float Deg2Rad = PI / 180.0f;
	public static final float Rad2Deg = 180.0f / PI;
	
	private static Random randomic = new Random(System.currentTimeMillis());
	
	public static float lengthdir_x(float distance, float angle) {
		return (float) (distance * Math.cos(angle * Deg2Rad));
	}
	
	public static int abs(int val) {
		if(val < 0) return -val;
		else return val;
	}
	
	public static float max(float a, float b) {
		if(a>b) return a;
		return b;
	}
	
	public static float min(float a, float b) {
		if(a<b) return a;
		return b;
	}
	
	public static float lengthdir_y(float distance, float angle) {
		return (float) (distance * -Math.sin(angle * Deg2Rad));
	}
	
	public static int sign(float val) {
		if(val < 0) return -1;
		else return 1;
	}
	
	public static int angle_between_points(float x1, float y1, float x2, float y2) {
		float theta = (float)( Math.atan2(x2 - x1, y2 - y1) - HALF_PI );
		return (int)(theta * Rad2Deg) + (theta > 0 ? 0 : 360);
	}
	
	public static float randomInRangef(float min, float max) {
		return min + (randomic.nextFloat()*(max - min));
	}
	
	public static int randomInRangei(int min, int max) {
		return min + (randomic.nextInt(max - min));
	}
	
	public static float lerp(float val, float to, float percent) {
		return (val * (1-percent)) + (to * percent);
	}
	
	public static float sqrt(float value) {
		return (float) Math.sqrt(value);
	}
	
	public static float distance_between_points(float x1, float y1, float x2, float y2) {
		return sqrt(( x2 - x1 ) * ( x2 - x1 ) + ( y2 - y1 ) * ( y2 - y1 ));
	}
	
	public static float abs(float val) {
		return (val<0)? -val : val;
	}
	
	public static float clamp(float val, float valMin, float valMax) {
		if(val <= valMin) {
			return valMin;
		}else if(val >= valMax) {
			return valMax;
		}else {
			return val;
		}
	}
}
