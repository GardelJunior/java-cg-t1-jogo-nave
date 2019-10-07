package com.gardel.jogo.formas;

import static org.lwjgl.opengl.GL11.*;

import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.texture.Texture;

public class BarraVida implements IForma {

	private float x,y;
	
	private float max;
	private float current;
	private float current_to;
	
	private boolean show = true;
	
	private static final int SIZE_BORDER_X = 230;
	private static final int SIZE_BORDER_Y = 15;
	
	private static final int SIZE_INNER_X = SIZE_BORDER_X - 2;
	private static final int SIZE_INNER_Y = SIZE_BORDER_Y - 2;
	
	public BarraVida(float x, float y, float max, float current) {
		this.x = x;
		this.y = y;
		this.max = max;
		this.current = current;
		this.current_to = current;
	}

	@Override
	public IForma render() {
		if(!show) return this;
		float percent_g = 2 * (1 - current_to/max);
		float percent_r = 2 * (1 - current/max);
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
				/* Preenchimento sombra vida */
				glColor3f(1, 0, 0);
				glVertex2f(-SIZE_INNER_X, -SIZE_INNER_Y);
				glVertex2f(-SIZE_INNER_X,  SIZE_INNER_Y);
				glVertex2f(SIZE_INNER_X - SIZE_INNER_X * percent_r,  SIZE_INNER_Y);
				glVertex2f(SIZE_INNER_X - SIZE_INNER_X * percent_r, -SIZE_INNER_Y);
				/* Preenchimento vida */
				glColor3f(0, 1, 0);
				glVertex2f(-SIZE_INNER_X, -SIZE_INNER_Y);
				glVertex2f(-SIZE_INNER_X,  SIZE_INNER_Y);
				glVertex2f(SIZE_INNER_X - SIZE_INNER_X * percent_g,  SIZE_INNER_Y);
				glVertex2f(SIZE_INNER_X - SIZE_INNER_X * percent_g, -SIZE_INNER_Y);
			glEnd();
		glPopMatrix();
		return this;
	}

	@Override
	public IForma update() {
		current = Mathf.lerp(current, current_to, 0.1f);
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
		return current_to;
	}

	public void setCurrent(float current) {
		this.current_to = current;
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
