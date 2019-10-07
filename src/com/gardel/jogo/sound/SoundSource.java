package com.gardel.jogo.sound;

import org.lwjgl.openal.AL10;

public class SoundSource {
	
	private int sourceID;
	
	public SoundSource() {
		sourceID = AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_GAIN, 1);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
		AL10.alSource3f(sourceID, AL10.AL_POSITION, 0, 0, 0);
	}
	
	public SoundSource(int soundid) {
		sourceID = AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_GAIN, 1);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, 1);
		AL10.alSource3f(sourceID, AL10.AL_POSITION, 0, 0, 0);
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, soundid);
	}
	
	public void setVolume(float volume) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
	}
	
	public void play() {
		AL10.alSourcePlay(sourceID);
	}
	
	public void play(int soundid) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, soundid);
		AL10.alSourcePlay(sourceID);
	}
	
	public void delete() {
		AL10.alDeleteSources(sourceID);
	}
}
