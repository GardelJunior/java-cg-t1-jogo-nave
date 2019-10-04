package com.gardel.jogo.collision;

public abstract class Collidable {
	
	protected float raio;
	
	protected float x, y;

	public boolean isColliding(Collidable collidable) {
		final float dx = (x - collidable.getX());
		final float dy = (y - collidable.getY());
		return Math.sqrt((dx * dx) + (dy * dy)) <= (raio + collidable.getRaio());
	}
	
	public void onCollideWith(Collidable c) {
		
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

	public float getRaio() {
		return raio;
	}

	public void setRaio(float raio) {
		this.raio = raio;
	}
}
