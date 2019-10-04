package com.gardel;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.manager.TextureManager;

public class Jogo {
	
	public static final int WIDTH = 600,HEIGHT = 800;
	
	public static long janela;
	private EntityManager em;
	
	public Jogo() {}
	
	public void init() {
		glfwSwapInterval(1); //Ativa a sincronia vertical, renderiza 60 quadros por segundo
		glClearColor(0, 0, 0, 1); //Seta a cor de fundo de preto
		glMatrixMode(GL_PROJECTION); //Escolhe a matriz de projeção
		glLoadIdentity(); //Reseta ela com a identidade
		glOrtho(0, 600, 800, 0, -1, 1); //Remapeia as coordenadas
		glMatrixMode(GL_MODELVIEW); //Escolhe a matriz de visão
		glLoadIdentity(); //Reseta ela com a identidade
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		TextureManager.getInstance().loadTexture("sprite_sheet", "/sprite_sheet.png");
		TextureManager.getInstance().loadTexture("boss_sheet", "/boss_sheet.png");
		em = EntityManager.getInstance();
	}
	
	public void reshape(int width, int height) {
		glMatrixMode(GL_PROJECTION); //Escolhe a matriz de projeção
		glLoadIdentity(); //Reseta ela com a identidade
		glOrtho(0, width, height, 0, -1, 1); //Remapeia as coordenadas
		glMatrixMode(GL_MODELVIEW); //Escolhe a matriz de visão
		glLoadIdentity(); //Reseta ela com a identidade
		glViewport(0, 0, width, height); //Remapeia o tamanho da tela renderizável
	}
	
	public void display() {
		em.update();
		em.render();
	}
	
	public void keyEvent(int key, int action) {
		if(action > 1) action = 1;
		em.onKeyEvent(key, action); //Avisa ao jogador que uma tecla foi pressionada/solta
	}
	
	public void gameloop() throws Exception {
		while(!glfwWindowShouldClose(janela)) { //Enquanto a janela estiver aberta, execute
			glfwPollEvents(); //Processa todos os eventos do ciclo, teclas, mouses...
			glClear(GL_COLOR_BUFFER_BIT);
			
			display(); //Realiza a chamada dos métodos de renderização
			
			glfwSwapBuffers(janela); //Troca os buffers, o que está sendo desenhado é mostrado
			Thread.sleep(1);
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		Jogo jogo = new Jogo();
		
		glfwInit();
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		Jogo.janela = glfwCreateWindow(600, 800, "Jogo de Naves", 0, 0);
		GLFWVidMode info = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(Jogo.janela, (info.width() - 600)/2, (info.height() - 800)/2);
		glfwMakeContextCurrent(Jogo.janela);
		GL.createCapabilities();
		glfwSetWindowSizeCallback(Jogo.janela, (long w, int width, int height) -> jogo.reshape(width, height));
		glfwSetKeyCallback(Jogo.janela, (long w, int k, int s, int a, int m) -> jogo.keyEvent(k, a));
		
		jogo.init();
		jogo.gameloop();
		
		glfwTerminate();
	}
}
