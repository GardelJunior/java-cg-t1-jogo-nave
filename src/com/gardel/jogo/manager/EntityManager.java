package com.gardel.jogo.manager;

import java.util.HashMap;
import java.util.Observable;
import java.util.Vector;
import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import com.gardel.Jogo;
import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.events.EventoAdicionaForma;
import com.gardel.jogo.events.EventoRemoveForma;
import com.gardel.jogo.events.IKeyListener;
import com.gardel.jogo.formas.BarraVida;
import com.gardel.jogo.formas.EndGameDerrota;
import com.gardel.jogo.formas.EndGameVitoria;
import com.gardel.jogo.formas.EndGameVitoria2;
import com.gardel.jogo.formas.Estrelas;
import com.gardel.jogo.formas.IForma;
import com.gardel.jogo.formas.Planeta;
import com.gardel.jogo.formas.entidades.Asteroide;
import com.gardel.jogo.formas.entidades.ChefaoCorpo;
import com.gardel.jogo.formas.entidades.Explosao;
import com.gardel.jogo.formas.entidades.Jogador;
import com.gardel.jogo.formas.entidades.MissilChefao;
import com.gardel.jogo.formas.entidades.NaveInimiga;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.renderer.Layer;
import com.gardel.jogo.sound.SoundManager;
import com.gardel.jogo.texture.Texture;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

@SuppressWarnings("unused")
public class EntityManager extends Observable implements IKeyListener {

	private static EntityManager instance;

	public static EntityManager getInstance() {
		if (instance == null) {
			instance = new EntityManager();
		}
		return instance;
	}

	private Estrelas estrelas; // Campo de estrelas do jogo
	private Jogador jogador; // Jogador
	private Planeta planeta; //Planeta
	
	private RenderManager renderer;

	private Vector<Collidable> collidables = new Vector<Collidable>();

	private EntityManager() {
		super();
		renderer = RenderManager.getInstance();
		estrelas = new Estrelas(Jogo.WIDTH, Jogo.HEIGHT); // Inicia o campo de estrelas
		jogador = new Jogador(Jogo.WIDTH / 2, Jogo.HEIGHT * 0.9f);
	}

	public void add(IForma forma) {
		
		if (forma instanceof Collidable)
			collidables.add((Collidable) forma);
		renderer.add(forma);
		
		setChanged();
		notifyObservers(new EventoAdicionaForma(forma));
	}

	public void clear() {
		super.deleteObservers();
		renderer.clear();
		collidables.clear();
		add(estrelas);
	}
	
	public boolean remove(IForma forma) {
		if (forma instanceof Collidable)
			collidables.remove((Collidable) forma);
		
		setChanged();
		notifyObservers(new EventoRemoveForma(forma));
		
		return renderer.remove(forma);
	}

	public void update() {
		checkCollison();
	}

	public Jogador getJogador() {
		return jogador;
	}
	
	public Estrelas getEstrelas() {
		return estrelas;
	}

	public void onKeyEvent(int key, int action) {
		jogador.onKeyEvent(key, action);
	}

	public void checkCollison() {
		for (int i = 0; i < collidables.size(); i++) {
			for (int j = 0; j < collidables.size(); j++) {
				if (i == j)
					continue;
				int size = collidables.size();
				if (size <= i || size <= j)
					continue; // Confirmar que não dê exceção
				if (collidables.get(i).isColliding(collidables.get(j))) {
					collidables.get(i).onCollideWith(collidables.get(j));
				}
			}
		}
	}
}
