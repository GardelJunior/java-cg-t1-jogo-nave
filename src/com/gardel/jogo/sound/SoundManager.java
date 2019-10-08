package com.gardel.jogo.sound;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import com.gardel.jogo.sound.ogg.OggData;
import com.gardel.jogo.sound.ogg.OggDecoder;

public class SoundManager {
	
	public static SoundSource SOUND_LASER;
	public static SoundSource SOUND_EXPLOSION_1;
	public static SoundSource SOUND_EXPLOSION_2;
	public static SoundSource SOUND_EXPLOSION_P;
	public static SoundSource SOUND_EXPLOSION_BIG;
	public static SoundSource SOUND_HIT;
	public static SoundSource SOUND_ATTACK;
	
	public static SoundSource MUSIC_1;
	public static SoundSource MUSIC_2;
	public static SoundSource MUSIC_3;
	
	private static long device;
	private static long context;
	
	private static List<Integer> sound_list = new ArrayList<Integer>();
	
	public static void init() {
		device = ALC10.alcOpenDevice((ByteBuffer)null);
		if(device == 0) {
			System.err.println("Falha ao abrir o driver de áudio");
			return;
		}
		
		ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
		
		context = ALC10.alcCreateContext(device, (IntBuffer) null);
		
		if(context == 0){
			System.err.println("Falha ao iniciar o contexto de áudio");
			return;
		}
		
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCapabilities);
	}
	
	public static int loadWaveSound(String file) {
		try {
			int buffer = AL10.alGenBuffers(); //Aloca um buffer de áudio e retorna o endr.
			AudioInputStream fis = AudioSystem.getAudioInputStream(new File("./resources/audio" + file));
			WaveData wave = WaveData.create(fis); //Lê o arquivo de audio
			AL10.alBufferData(buffer, wave.format, wave.data, wave.samplerate); //Coloca o audio no buffer.
			sound_list.add(buffer); //Adiciona o buffer na lista de áudios
			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int loadOggSound(String file) {
		try {
			int buffer = AL10.alGenBuffers(); //Aloca um buffer de áudio e retorna o endr.
			OggData data = new OggDecoder().getData(new FileInputStream(new File("./resources/audio" + file)));
			AL10.alBufferData(buffer, data.channels, data.data, data.rate); //Coloca o audio no buffer.
			sound_list.add(buffer); //Adiciona o buffer na lista de áudios
			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void setListenerConfiguration() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0); //Posição do ouvinte
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0); //Velocidade do ouvinte (Efeito doppler)
	}
	
	public static void terminate() {
		
		for(int sound : sound_list) {
			AL10.alDeleteBuffers(sound);
		}
		
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
}
