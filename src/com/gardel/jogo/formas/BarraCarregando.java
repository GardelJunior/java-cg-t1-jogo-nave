package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.texture.Texture;

public class BarraCarregando implements IForma {

	private float x,y;
	
	private float max;
	private float current;
	
	private boolean show = false;
	
	private static final int SIZE_BORDER_X = 230;
	private static final int SIZE_BORDER_Y = 15;
	
	private static final int SIZE_INNER_X = SIZE_BORDER_X - 2;
	private static final int SIZE_INNER_Y = SIZE_BORDER_Y - 2;
	
	public BarraCarregando(float x, float y, float max, float current) {
		this.x = x;
		this.y = y;
		this.max = max;
		this.current = current;
	}

	@Override
	public IForma render() {
		if(!show) return this;
		float percent = 2 * (1 - current/max);
		glPushMatrix();
			Texture.unbind();
			glTranslatef(x, y, 0);
			glBegin(GL_QUADS);
			glColor3f(1, 1, 1);
				/* Bordas de fora */
				glVertex2f(-SIZE_BORDER_X, -SIZE_BORDER_Y);
				glVertex2f(-SIZE_BORDER_X,  SIZE_BORDER_Y);
				glVertex2f( SIZE_BORDER_X,  SIZE_BORDER_Y);
				glVertex2f( SIZE_BORDER_X, -SIZE_BORDER_Y);
				/* Preenchimento de dentro */
				glColor4f(0, 0, 0, 1);
				glVertex2f(-SIZE_INNER_X, -SIZE_INNER_Y);
				glVertex2f(-SIZE_INNER_X,  SIZE_INNER_Y);
				glVertex2f( SIZE_INNER_X,  SIZE_INNER_Y);
				glVertex2f( SIZE_INNER_X, -SIZE_INNER_Y);
				/* Preenchimento porcentagem */
				glColor3f(1, 1, 1);
				glVertex2f(-SIZE_INNER_X, -SIZE_INNER_Y);
				glVertex2f(-SIZE_INNER_X,  SIZE_INNER_Y);
				glVertex2f(SIZE_INNER_X - SIZE_INNER_X * percent,  SIZE_INNER_Y);
				glVertex2f(SIZE_INNER_X - SIZE_INNER_X * percent, -SIZE_INNER_Y);
			glEnd();
		glPopMatrix();
		return this;
	}

	@Override
	public IForma update() {
		return this;
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

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getCurrent() {
		return current;
	}

	public void setCurrent(float current) {
		this.current = current;
	}
	
	public boolean isVisible() {
		return show;
	}

	public void setVisible(boolean visible) {
		this.show = visible;
	}

	@Override
	public int getDepth() {
		return 3;
	}
}
