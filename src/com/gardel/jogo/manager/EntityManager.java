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
import com.gardel.jogo.formas.Chefao;
import com.gardel.jogo.formas.Estrelas;
import com.gardel.jogo.formas.IForma;
import com.gardel.jogo.formas.Jogador;
import com.gardel.jogo.formas.MissilChefao;
import com.gardel.jogo.formas.NaveInimiga;
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
	
	private Texture[] textures = new Texture[2];
	
	private Estrelas estrelas; //Campo de estrelas do jogo
	private Jogador jogador;
	private Chefao chefao;
	
	private HashMap<Integer,Vector<IForma>> formas = new HashMap<Integer,Vector<IForma>>();
	private Vector<Collidable> collidables = new Vector<>();
	
	private EntityManager() { 
		super();
		
		formas.put(0, new Vector<IForma>());
		formas.put(1, new Vector<IForma>());
		
		textures[0] = TextureManager.getInstance().getTexture("sprite_sheet");
		textures[1] = TextureManager.getInstance().getTexture("boss_sheet");
		
		estrelas = new Estrelas(Jogo.WIDTH,Jogo.HEIGHT); //Inicia o campo de estrelas
		jogador = new Jogador(Jogo.WIDTH/2,Jogo.HEIGHT * 0.9f);
		
		addObserver(jogador);
		
		add(jogador);
		add(new NaveInimiga(Jogo.WIDTH/4, Jogo.HEIGHT/2));
		add(new Asteroide(Jogo.WIDTH/2, Jogo.HEIGHT/2));
		add(new MissilChefao(Jogo.WIDTH/2 + Jogo.WIDTH/4, Jogo.HEIGHT/2));
		
		add(new Chefao(Jogo.WIDTH/2, Jogo.HEIGHT/5));
		
	}
	
	public void add(IForma forma) {
		formas.get(forma.getTextureSheet()).add(forma);
		if(forma instanceof Collidable) collidables.add((Collidable)forma);
		setChanged();
		notifyObservers(new EventoAdicionaForma(forma));
	}
	
	public boolean remove(IForma forma) {
		if(forma instanceof Collidable) collidables.remove((Collidable)forma);
		setChanged();
		notifyObservers(new EventoRemoveForma(forma));
		return formas.get(forma.getTextureSheet()).remove(forma);
	}
	
	public void update() {
		checkCollison();
	}
	
	public void render() {
		estrelas.update(); //Atualiza o campo de estrelas
		estrelas.render(); //Desenha o camp de estrelas
		
		for(int t = 0 ; t < textures.length ; t++) {
			textures[t].bind();
			Vector<IForma> forms = formas.get(t);
			for(int i = 0 ; i < forms.size() ; i++) { //Percorre todas as formas
				forms.get(i).update().render();
			}
		}
		textures[0].unbind();
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
