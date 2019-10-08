package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import com.gardel.jogo.texture.Texture;

public class Estrelas {
	private final int SIZE = 100;
	private Estrela[] pontos = new Estrela[SIZE]; //100 Estrelas
	private float velocity = 1;
	Random randomizer;
	public float width,height;
	
	public Estrelas(int width, int height) {
		this.width = width;
		this.height = height;
		randomizer = new Random();
		for(int i = 0 ; i < SIZE ; i++) {
			pontos[i] = new Estrela(
					randomizer.nextFloat() * width, 	//X
					randomizer.nextFloat() * height, 	//Y
					1, //R
					1, //G
					1, //B
					.4f + randomizer.nextFloat()*1.5f); //Size Y
		}
	}
	
	public void update() {
		for(int i = 0 ; i < SIZE ; i++) {
			pontos[i].y += pontos[i].yVel * velocity;
			boolean pA = pontos[i].y > height + velocity;
			boolean pB = pontos[i].y - velocity < 0;
			if( pA || pB ) {
				pontos[i].x = randomizer.nextFloat() * width;
				if(pA) {
					pontos[i].y = 0;
				}else {
					pontos[i].y = height;
				}
				pontos[i].yVel = (.4f + randomizer.nextFloat()*1.4f);
				if(velocity > 25f || velocity < -25f) {
					pontos[i].r = (float) Math.random();
					pontos[i].g = (float) Math.random();
					pontos[i].b = (float) Math.random();
				}
			}
		}
	}
	
	public void render() {
		Texture.unbind();
		if(velocity > 1f || velocity < -1f) {
			glBegin(GL_LINES);
			for(int i = 0 ; i < SIZE ; i++) {
				glColor3f(pontos[i].r, pontos[i].g, pontos[i].b);
				glVertex2f(pontos[i].x, pontos[i].y - velocity);
				glVertex2f(pontos[i].x, pontos[i].y);
			}
			glEnd();
		}else {
			glColor3f(1, 1, 1);
			glBegin(GL_POINTS);
				for(int i = 0 ; i < SIZE ; i++) {
					glVertex2f(pontos[i].x, pontos[i].y);
				}
			glEnd();
		}
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	
	private class Estrela {
		public float x,y;
		public float r,g,b;
		public float yVel;
		
		public Estrela(float x, float y, float r, float g, float b, float yVel) {
			this.x = x;
			this.y = y;
			this.r = r;
			this.g = g;
			this.b = b;
			this.yVel = yVel;
		}
	}
}
