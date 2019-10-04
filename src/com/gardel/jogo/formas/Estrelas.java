package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

public class Estrelas {
	private final int SIZE = 100;
	float [][] pontos = new float[SIZE][3]; //100 Estrelas
	Random randomizer;
	public float width,height;
	
	public Estrelas(int width, int height) {
		this.width = width;
		this.height = height;
		randomizer = new Random();
		for(int i = 0 ; i < SIZE ; i++) {
			pontos[i] = new float[] {randomizer.nextFloat() * width, randomizer.nextFloat() * height, .4f + randomizer.nextFloat()*1.4f};
		}
		System.gc();
	}
	
	public void update() {
		for(int i = 0 ; i < SIZE ; i++) {
			pontos[i][1] += pontos[i][2];
			if(pontos[i][1] > height) {
				pontos[i][0] = randomizer.nextFloat() * width;
				pontos[i][1] = 0;
				pontos[i][2] = .4f + randomizer.nextFloat()*1.4f;
			}
		}
	}
	
	public void render() {
		glBegin(GL_POINTS);
		glColor3f(1, 1, 1);
		
		for(int i = 0 ; i < SIZE ; i++) {
			glVertex2f(pontos[i][0], pontos[i][1]);
		}
		
		glEnd();
	}
}
