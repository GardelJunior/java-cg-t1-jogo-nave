package com.gardel.jogo.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gardel.jogo.texture.Texture;

public class TextureManager {
	private static TextureManager instance;
	
	public static TextureManager getInstance() {
		if (instance == null) {
			instance = new TextureManager();
		}
		return instance;
	}
	
	private HashMap<String, Texture> textureMap = new HashMap<>();
	private List<Texture> texture_list = new ArrayList<Texture>();
	
	public Texture getTexture(String name) {
		return textureMap.get(name);
	}
	
	public Texture getTexture(int id) {
		return texture_list.get(id);
	}
	
	public void loadTexture(String name, String filename) {
		Texture t = new Texture(filename);
		textureMap.put(name, t);
		texture_list.add(t);
	}
}
