package com.gardel.jogo.math;

import java.util.Random;

/**
 * 
 * @author Gardel Júnior
 *
 */

public class Mathf {
	public static final float PI = 3.14159265359f;
	public static final float HALF_PI = 3.14159265359f/2;
	public static final float Deg2Rad = PI / 180.0f;
	public static final float Rad2Deg = 180.0f / PI;
	
	private static Random randomic = new Random(System.currentTimeMillis());
	
	/**
	 * 
	 * @param distance
	 * @param angle
	 * @return
	 */
	public static float lengthdir_x(float distance, float angle) {
		return (float) (distance * Math.cos(angle * Deg2Rad));
	}
	/**
	 * 
	 * @param val
	 * @return
	 */
	public static int abs(int val) {
		if(val < 0) return -val;
		else return val;
	}
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static float max(float a, float b) {
		if(a>b) return a;
		return b;
	}
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static float min(float a, float b) {
		if(a<b) return a;
		return b;
	}
	/**
	 * 
	 * @param distance
	 * @param angle
	 * @return
	 */
	public static float lengthdir_y(float distance, float angle) {
		return (float) (distance * -Math.sin(angle * Deg2Rad));
	}
	
	/**
	 * 
	 * @param val
	 * @return
	 */
	public static int sign(float val) {
		if(val < 0) return -1;
		else return 1;
	}
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static int angle_between_points(float x1, float y1, float x2, float y2) {
		float theta = (float)( Math.atan2(x2 - x1, y2 - y1) - HALF_PI );
		return (int)(theta * Rad2Deg) + (theta > 0 ? 0 : 360);
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static float randomInRangef(float min, float max) {
		return min + (randomic.nextFloat()*(max - min));
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInRangei(int min, int max) {
		return min + (randomic.nextInt(max - min));
	}
	
	/**
	 * 
	 * @param val
	 * @param to
	 * @param percent
	 * @return
	 */
	public static float lerp(float val, float to, float percent) {
		return (val * (1-percent)) + (to * percent);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static float sqrt(float value) {
		return (float) Math.sqrt(value);
	}
	
	/**
	 * Calcula a distância Euclidiana entre dois pontos.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	
	public static float distance_between_points(float x1, float y1, float x2, float y2) {
		return sqrt(( x2 - x1 ) * ( x2 - x1 ) + ( y2 - y1 ) * ( y2 - y1 ));
	}
	
	/**
	 * Retorna o valor absoluto do parâmetro de entrada
	 * @param val
	 * @return
	 */
	public static float abs(float val) {
		return (val<0)? -val : val;
	}
	
	/**
	 * 
	 * @param val
	 * @param valMin
	 * @param valMax
	 * @return
	 */
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
