package com.gardel.jogo.manager;

import java.util.HashMap;

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
	
	public Texture getTexture(String name) {
		return textureMap.get(name);
	}
	
	public void loadTexture(String name, String filename) {
		textureMap.put(name, new Texture(filename));
	}
}
