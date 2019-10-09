package com.gardel;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.gardel.jogo.manager.EntityManager;
import com.gardel.jogo.manager.RenderManager;
import com.gardel.jogo.manager.StateManager;
import com.gardel.jogo.manager.TextureManager;
import com.gardel.jogo.sound.SoundManager;
import com.gardel.jogo.sound.SoundSource;
import com.gardel.jogo.states.EstadoChefao;
import com.gardel.jogo.states.EstadoVitoria;
import com.gardel.jogo.states.EstadoEarlyGame;
import com.gardel.jogo.states.EstadoDerrota;

public class Jogo {
	
	public static final int WIDTH = 600,HEIGHT = 800;
	
	public static long janela;
	private EntityManager entityManager;
	private RenderManager renderManager;
	private StateManager stateManager;
	
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
		
		SoundManager.SOUND_EXPLOSION_1 	= new SoundSource(SoundManager.loadOggSound("/explosion.ogg"));
		SoundManager.SOUND_EXPLOSION_2 	= new SoundSource(SoundManager.loadOggSound("/explosion2.ogg"));
		SoundManager.SOUND_EXPLOSION_P 	= new SoundSource(SoundManager.loadOggSound("/explosion_player.ogg"));
		SoundManager.SOUND_EXPLOSION_BIG 	= new SoundSource(SoundManager.loadOggSound("/explosion_big.ogg"));
		SoundManager.SOUND_HIT				= new SoundSource(SoundManager.loadOggSound("/hit.ogg"));
		SoundManager.SOUND_LASER			= new SoundSource(SoundManager.loadOggSound("/laser.ogg"));
		SoundManager.SOUND_ATTACK			= new SoundSource(SoundManager.loadOggSound("/attack.ogg"));
		SoundManager.MUSIC_1				= new SoundSource(SoundManager.loadOggSound("/soundtrack1.ogg"));
		SoundManager.MUSIC_2				= new SoundSource(SoundManager.loadOggSound("/soundtrack2.ogg"));
		SoundManager.MUSIC_3				= new SoundSource(SoundManager.loadOggSound("/soundtrack3.ogg"));
		
		entityManager = EntityManager.getInstance();
		renderManager = RenderManager.getInstance();
		stateManager = StateManager.getInstance();
		
		stateManager.registerState("inicial", new EstadoEarlyGame());
		stateManager.registerState("chefao", new EstadoChefao());
		stateManager.registerState("vitoria", new EstadoVitoria());
		stateManager.registerState("derrota", new EstadoDerrota());
		
		stateManager.setState("inicial");
		
		SoundManager.MUSIC_1.setVolume(0.8f);
		SoundManager.MUSIC_2.setVolume(0.85f);
		SoundManager.MUSIC_3.setVolume(0.8f);
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
		entityManager.update();
		stateManager.update();
		renderManager.updateAndRender();
	}
	
	public void keyEvent(int key, int action) {
		if(action > 1) action = 1;
		entityManager.onKeyEvent(key, action); //Avisa ao jogador que uma tecla foi pressionada/solta
		stateManager.onKeyEvent(key, action);
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
		
		SoundManager.init();
		SoundManager.setListenerConfiguration();
		
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
		
		SoundManager.terminate();
		glfwTerminate();
	}
}
