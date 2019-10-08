package com.gardel.jogo.manager;

import java.util.HashMap;
import java.util.Observable;
import java.util.Vector;

import org.lwjgl.glfw.GLFW;

import com.gardel.Jogo;
import com.gardel.jogo.collision.Collidable;
import com.gardel.jogo.events.EventoAdicionaForma;
import com.gardel.jogo.events.EventoRemoveForma;
import com.gardel.jogo.events.IKeyListener;
import com.gardel.jogo.formas.Asteroide;
import com.gardel.jogo.formas.BarraVida;
import com.gardel.jogo.formas.ChefaoCorpo;
import com.gardel.jogo.formas.EndGameDerrota;
import com.gardel.jogo.formas.EndGameVitoria;
import com.gardel.jogo.formas.EndGameVitoria2;
import com.gardel.jogo.formas.Estrelas;
import com.gardel.jogo.formas.Explosao;
import com.gardel.jogo.formas.IForma;
import com.gardel.jogo.formas.Jogador;
import com.gardel.jogo.formas.MissilChefao;
import com.gardel.jogo.formas.NaveInimiga;
import com.gardel.jogo.formas.Planeta;
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

	private Vector<Layer> layers = new Vector<Layer>();
	private Vector<Integer> layers_z = new Vector<Integer>();
	private Vector<Collidable> collidables = new Vector<Collidable>();

	private boolean outro = false;
	private int outro_time = 220;
	private int game_time = 3200;
	private boolean first = false;
	private int spawn_interval = 90;
	private int enemy_on_screen = 0;
	private int playerDieInterval = 220;
	private boolean playerDied = false;
	private boolean shouldSpawn = true;

	private EntityManager() {
		super();
		estrelas = new Estrelas(Jogo.WIDTH, Jogo.HEIGHT); // Inicia o campo de estrelas
	}
	
	public void startLevel() {
		
		SoundManager.MUSIC_1.stop();
		SoundManager.MUSIC_2.stop();
		SoundManager.MUSIC_3.stop();
		
		clearEntities();
		first = false;
		outro = false;
		shouldSpawn = true;
		playerDied = false;
		game_time = 3200;
		outro_time = 220;
		playerDieInterval = 220;
		jogador = new Jogador(Jogo.WIDTH / 2, Jogo.HEIGHT * 0.9f);
		addObserver(jogador);
		add(jogador);
		
		SoundManager.MUSIC_1.play();
	}

	public void add(IForma forma) {
		
		if (!layers_z.contains(forma.getDepth())) {
			layers_z.add(forma.getDepth());
			layers.add(new Layer());
			zOrderLayers();
		}

		layers.get(layers_z.indexOf(forma.getDepth())).add(forma);
		if (forma instanceof Collidable)
			collidables.add((Collidable) forma);

		setChanged();
		notifyObservers(new EventoAdicionaForma(forma));
	}

	private void clearEntities() {
		super.deleteObservers();
		layers.forEach(layer -> layer.clear());
		collidables.clear();
		enemy_on_screen = 0;
	}
	
	public boolean remove(IForma forma) {
		onRemove(forma);
		if (layers_z.contains(forma.getDepth())) {
			int layer_z = layers_z.indexOf(forma.getDepth());
			if (forma instanceof Collidable)
				collidables.remove((Collidable) forma);
			return layers.get(layer_z).remove(forma);
		}
		return false;
	}

	private void zOrderLayers() {
		layers.sort((Layer l1, Layer l2) -> Integer.compare(l1.getZ_order(), l2.getZ_order()));
		layers_z.sort((Integer z1, Integer z2) -> Integer.compare(z1, z2));
	}

	public void update() {
		
		if(playerDied && playerDieInterval >= 0) {
			if(playerDieInterval == 220) {
				shouldSpawn = false;
			}else if(playerDieInterval == 180 ) {
				clearEntities();
			} else if(playerDieInterval == 120) {
				planeta = new Planeta(Jogo.WIDTH/2, Jogo.HEIGHT/2);
				add(planeta);
			} else if(playerDieInterval == 80) {
				add(new Explosao(Jogo.WIDTH/2, Jogo.HEIGHT/2,4));
				remove(planeta);
				planeta = null;
				SoundManager.SOUND_EXPLOSION_BIG.play();
			} else if(playerDieInterval == 30) {
				add(new EndGameDerrota(Jogo.WIDTH/2, Jogo.HEIGHT/2));
			}
			playerDieInterval--;
		}
		
		if (outro && outro_time > 0) {
			if(jogador.getAngle() == 180) {
				estrelas.setVelocity(Mathf.max(Mathf.abs(estrelas.getVelocity()) * -1.05f, -50));
				jogador.setyScale(Mathf.min(jogador.getyScale() * 1.001f, 3));
				outro_time--;
			}else {
				jogador.setAngle(jogador.getAngle()+2);
			}
			jogador.setX(Mathf.lerp(jogador.getX(), Jogo.WIDTH / 2, 0.05f));
		} else if (outro_time == 0) {
			if(jogador.getAngle() == 0) {
				outro_time = -1;
				add(new EndGameVitoria(Jogo.WIDTH / 2, Jogo.HEIGHT / 4));
				add(new EndGameVitoria2(Jogo.WIDTH / 2, Jogo.HEIGHT / 2));
			}else {
				jogador.setyScale(1);
				estrelas.setVelocity(1);
				jogador.setAngle(jogador.getAngle()-2);
				if(!first) {
					first = !first;
					add(new Planeta(Jogo.WIDTH / 2, Jogo.HEIGHT / 2));
				}
			}
		}
		estrelas.update(); // Atualiza o campo de estrelas
		if (game_time > 0) {
			if(shouldSpawn) {
				game_time--;
				if (spawn_interval == 0) {
					spawn_interval = 60;
					if (Mathf.randomInRangef(0, 100) < 50) {
						if (Mathf.randomInRangef(0, 100) < 50) {
							add(new NaveInimiga(Mathf.randomInRangef(50, Jogo.WIDTH - 50), -20));
						} else {
							add(new Asteroide(Mathf.randomInRangef(50, Jogo.WIDTH - 50), -20));
						}
						enemy_on_screen++;
					}
				} else {
					spawn_interval--;
				}
			}
		} else if (game_time == 0 && shouldSpawn) {
			if (enemy_on_screen == 0) {
				game_time = -1;
				SoundManager.MUSIC_1.stop();
				SoundManager.MUSIC_2.play();
				add(new ChefaoCorpo(Jogo.WIDTH / 2, -50));
			}
		}

		checkCollison();
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void render() {
		estrelas.render(); // Desenha o camp de estrelas

		for (int i = 0; i < layers.size(); i++) {
			layers.get(i).render();
		}
	}

	public void onKeyEvent(int key, int action) {
		jogador.onKeyEvent(key, action);
		if(playerDied && playerDieInterval == -1) {
			if(key == GLFW.GLFW_KEY_SPACE && action > 0) {
				startLevel();
			}
		}
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
	
	private void onRemove(IForma forma) {
		
		if (forma instanceof ChefaoCorpo) {
			estrelas.setVelocity(1);
			outro = true;
		}
		
		if(forma instanceof Jogador) {
			//Jogador foi removido
			playerDied = true;
		}

		if (forma instanceof Asteroide || forma instanceof NaveInimiga) {
			enemy_on_screen--;
		}
		
		setChanged();
		notifyObservers(new EventoRemoveForma(forma));
	}
}
