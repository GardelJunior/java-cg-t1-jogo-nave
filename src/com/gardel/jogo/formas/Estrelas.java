package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import com.gardel.jogo.texture.Texture;

public class Estrelas {
	private final int SIZE = 100;
	private float [][] pontos = new float[SIZE][6]; //100 Estrelas
	private float velocity = 0;
	Random randomizer;
	public float width,height;
	
	public Estrelas(int width, int height) {
		this.width = width;
		this.height = height;
		randomizer = new Random();
		for(int i = 0 ; i < SIZE ; i++) {
			pontos[i] = new float[] {
					randomizer.nextFloat() * width, 
					randomizer.nextFloat() * height,
					1.0f ,
					1.0f ,
					1.0f ,
					.4f + randomizer.nextFloat()*1.5f
			};
		}
		System.gc();
	}
	
	public void update() {
		for(int i = 0 ; i < SIZE ; i++) {
			pontos[i][1] += pontos[i][2] + velocity;
			if(pontos[i][1] > height + velocity) {
				pontos[i][0] = randomizer.nextFloat() * width;
				pontos[i][1] = 0;
				pontos[i][5] = .4f + randomizer.nextFloat()*1.4f;
				if(velocity>25) {
					pontos[i][2] = (float) Math.random();
					pontos[i][3] = (float) Math.random();
					pontos[i][4] = (float) Math.random();
				}
			}
		}
	}
	
	public void render() {
		Texture.unbind();
		
		if(velocity>0) {
			glBegin(GL_LINES);
			for(int i = 0 ; i < SIZE ; i++) {
				glColor3f(pontos[i][2], pontos[i][3], pontos[i][4]);
				glVertex2f(pontos[i][0], pontos[i][1] - velocity);
				glVertex2f(pontos[i][0], pontos[i][1]);
			}
			glEnd();
		}else {
			glColor3f(1, 1, 1);
			glBegin(GL_POINTS);
				for(int i = 0 ; i < SIZE ; i++) {
					glVertex2f(pontos[i][0], pontos[i][1]);
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
}
