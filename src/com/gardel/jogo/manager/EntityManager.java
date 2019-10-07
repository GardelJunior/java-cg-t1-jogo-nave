package com.gardel.jogo.manager;

import java.util.HashMap;
import java.util.Observable;
import java.util.Vector;

import com.gardel.Jogo;
import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.events.EventoAdicionaForma;
import com.gardel.jogo.events.EventoRemoveForma;
import com.gardel.jogo.events.IKeyListener;
import com.gardel.jogo.formas.Asteroide;
import com.gardel.jogo.formas.BarraVida;
import com.gardel.jogo.formas.Chefao;
import com.gardel.jogo.formas.EndGameVictory;
import com.gardel.jogo.formas.Estrelas;
import com.gardel.jogo.formas.IForma;
import com.gardel.jogo.formas.Jogador;
import com.gardel.jogo.formas.MissilChefao;
import com.gardel.jogo.formas.NaveInimiga;
import com.gardel.jogo.math.Mathf;
import com.gardel.jogo.renderer.Layer;
import com.gardel.jogo.texture.Texture;

@SuppressWarnings("unused")
public class EntityManager extends Observable implements IKeyListener{
	
	private static EntityManager instance;
	public static EntityManager getInstance() {
		if(instance == null) {
			instance = new EntityManager();
		}
		return instance;
	}
	
	private Estrelas estrelas; //Campo de estrelas do jogo
	private Jogador jogador;
	private Chefao chefao;
	private BarraVida vida_chefao;
	
	private Vector<Layer> layers = new Vector<Layer>();
	private Vector<Integer> layers_z = new Vector<Integer>();
	private Vector<Collidable> collidables = new Vector<Collidable>();
	
	private boolean outro = false;
	private int outro_time = 220;
	
	private EntityManager() { 
		super();
		
		estrelas = new Estrelas(Jogo.WIDTH,Jogo.HEIGHT); //Inicia o campo de estrelas
		jogador = new Jogador(Jogo.WIDTH/2,Jogo.HEIGHT * 0.9f);
		
		addObserver(jogador);
		
		this.vida_chefao = new BarraVida(Jogo.WIDTH/2, 30, 30, 30);
		this.chefao = new Chefao(Jogo.WIDTH/2, Jogo.HEIGHT/5,this.vida_chefao);
		
		add(jogador);
		add(chefao);
		add(vida_chefao);
	}
	
	public void add(IForma forma) {
		if(!layers_z.contains(forma.getDepth())) {
			layers_z.add(forma.getDepth());
			layers.add(new Layer());
			zOrderLayers();
		}
		
		layers.get(layers_z.indexOf(forma.getDepth())).add(forma);
		if(forma instanceof Collidable) collidables.add((Collidable)forma);
		
		setChanged();
		notifyObservers(new EventoAdicionaForma(forma));
	}
	
	public boolean remove(IForma forma) {
		
		if(forma instanceof Chefao) {
			estrelas.setVelocity(1);
			outro = true;
		}
		
		if(layers_z.contains(forma.getDepth())) {
			int layer_z = layers_z.indexOf(forma.getDepth());
			if(forma instanceof Collidable) collidables.remove((Collidable) forma);
			setChanged();
			notifyObservers(new EventoRemoveForma(forma));
			return layers.get(layer_z).remove(forma);
		}
		
		return false;
	}
	
	private void zOrderLayers() {
		layers.sort((Layer l1, Layer l2) -> Integer.compare(l1.getZ_order(), l2.getZ_order()));
		layers_z.sort((Integer z1, Integer z2) -> Integer.compare(z1, z2));
	}
	
	public void update() {
		checkCollison();
	}
	
	public Jogador getJogador() {
		return jogador;
	}
	
	public void render() {
		if(outro && outro_time > 0) {
			estrelas.setVelocity(Mathf.min(estrelas.getVelocity() * 1.05f, 50));
			jogador.setyScale(Mathf.min(jogador.getyScale() * 1.001f, 3));
			jogador.setX(Mathf.lerp(jogador.getX(), Jogo.WIDTH/2, 0.1f));
			outro_time--;
		}else if(outro_time == 0) {
			outro_time = -1;
			jogador.setyScale(1);
			estrelas.setVelocity(0);
			add(new EndGameVictory(Jogo.WIDTH/2, Jogo.HEIGHT/2));
		}
		
		estrelas.update(); //Atualiza o campo de estrelas
		estrelas.render(); //Desenha o camp de estrelas
		
		for(int i = 0 ; i < layers.size() ; i++) {
			layers.get(i).render();
		}
	}

	public void onKeyEvent(int key, int action) {
		jogador.onKeyEvent(key, action);
	}
	
	public void checkCollison() {
		for(int i = 0 ; i < collidables.size() ; i++) {
			for(int j = 0 ; j < collidables.size() ; j++) {
				if(i == j) continue;
				int size = collidables.size();
				if(size <= i || size <= j) continue; //Confirmar que não dê exceção
				if(collidables.get(i).isColliding(collidables.get(j))) {
					collidables.get(i).onCollideWith(collidables.get(j));
				}
			}
		}
	}
}
