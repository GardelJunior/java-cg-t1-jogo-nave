package com.gardel.jogo.formas;

import static com.gardel.jogo.texture.Texture.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.Observable;
import java.util.Observer;

import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.events.EventoRemoveForma;
import com.gardel.jogo.events.IKeyListener;
import com.gardel.jogo.manager.EntityManager;

public class Jogador extends Collidable implements IForma,IKeyListener,Observer{
	
	private int left,right,space;
	private int tiros = 3;
	private float delay = 0;
	
	private static final int SIZE = 40;
	
	public Jogador(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public IForma render() {
		glColor3f(1, 1, 1);
		glPushMatrix();
			glTranslatef(x, y, 0);
			glBegin(GL_QUADS);
				glTexCoord2f(0, SIZE_32);
				glVertex2f(-SIZE,  SIZE);
				glTexCoord2f(SIZE_32, SIZE_32);
				glVertex2f( SIZE,  SIZE);
				glTexCoord2f(SIZE_32, 0);
				glVertex2f( SIZE, -SIZE);
				glTexCoord2f(0, 0);
				glVertex2f(-SIZE, -SIZE);
			glEnd();
		glPopMatrix();
		
		return this;
	}

	@Override
	public IForma update() {
		
		x += (right - left) * 4;
		if(delay > 0) {
			delay -= 2;
		}else if(space > 0 && delay <= 0f && tiros > 0) {
			tiros--;
			delay = 60;
			EntityManager.getInstance().add(new LaserPlayer(x, y - 20));
		}
		return this;
	}

	@Override
	public void onKeyEvent(int key, int action) {
		switch(key) {
			case GLFW_KEY_A:
			case GLFW_KEY_LEFT: left = action; break;
			case GLFW_KEY_D:
			case GLFW_KEY_RIGHT: right = action; break;
			case GLFW_KEY_SPACE: space = action; break;
		}
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

	@Override
	public void update(Observable o, Object update) {
		// Sistema de eventos, ao adicionar um tiro ele remove do máximo de tiros que pode dar
		
		if(update instanceof EventoRemoveForma) {
			if(((EventoRemoveForma)update).getForma() instanceof LaserPlayer) {
				tiros++;
			} 
		}
	}
}
