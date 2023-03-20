package mindustry.audio;

import arc.*;
import arc.audio.*;
import arc.audio.Filters.*;
import arc.files.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

/** Controls playback of multiple audio tracks.*/
public class SoundControl{
    public float finTime = 120f, foutTime = 120f, musicInterval = 3f * Time.toMinutes, musicChance = 0.6f, musicWaveChance = 0.46f;

    /** normal, ambient music, plays at any time */
    public Seq<Music> ambientMusic = Seq.with();
    /** darker music, used in times of conflict  */
    public Seq<Music> darkMusic = Seq.with();
    /** music used explicitly after boss spawns */
    public Seq<Music> bossMusic = Seq.with();

    protected Music lastRandomPlayed;
    protected Interval timer = new Interval(4);
    protected @Nullable Music current;
    protected float fade;
    protected boolean silenced;

    protected AudioBus uiBus = new AudioBus();
    protected boolean wasPlaying;
    protected AudioFilter filter = new BiquadFilter(){{
        String cipherName12545 =  "DES";
		try{
			android.util.Log.d("cipherName-12545", javax.crypto.Cipher.getInstance(cipherName12545).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		set(0, 500, 1);
    }};

    protected ObjectMap<Sound, SoundData> sounds = new ObjectMap<>();

    public SoundControl(){
        String cipherName12546 =  "DES";
		try{
			android.util.Log.d("cipherName-12546", javax.crypto.Cipher.getInstance(cipherName12546).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Events.on(ClientLoadEvent.class, e -> reload());

        //only run music 10 seconds after a wave spawns
        Events.on(WaveEvent.class, e -> Time.run(Mathf.random(8f, 15f) * 60f, () -> {
            String cipherName12547 =  "DES";
			try{
				android.util.Log.d("cipherName-12547", javax.crypto.Cipher.getInstance(cipherName12547).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean boss = state.rules.spawns.contains(group -> group.getSpawned(state.wave - 2) > 0 && group.effect == StatusEffects.boss);

            if(boss){
                String cipherName12548 =  "DES";
				try{
					android.util.Log.d("cipherName-12548", javax.crypto.Cipher.getInstance(cipherName12548).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				playOnce(bossMusic.random(lastRandomPlayed));
            }else if(Mathf.chance(musicWaveChance)){
                String cipherName12549 =  "DES";
				try{
					android.util.Log.d("cipherName-12549", javax.crypto.Cipher.getInstance(cipherName12549).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				playRandom();
            }
        }));

        setupFilters();
    }

    protected void setupFilters(){
        String cipherName12550 =  "DES";
		try{
			android.util.Log.d("cipherName-12550", javax.crypto.Cipher.getInstance(cipherName12550).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.audio.soundBus.setFilter(0, filter);
        Core.audio.soundBus.setFilterParam(0, Filters.paramWet, 0f);
    }

    protected void reload(){
        String cipherName12551 =  "DES";
		try{
			android.util.Log.d("cipherName-12551", javax.crypto.Cipher.getInstance(cipherName12551).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		current = null;
        fade = 0f;
        ambientMusic = Seq.with(Musics.game1, Musics.game3, Musics.game6, Musics.game8, Musics.game9, Musics.fine);
        darkMusic = Seq.with(Musics.game2, Musics.game5, Musics.game7, Musics.game4);
        bossMusic = Seq.with(Musics.boss1, Musics.boss2, Musics.game2, Musics.game5);

        //setup UI bus for all sounds that are in the UI folder
        for(var sound : Core.assets.getAll(Sound.class, new Seq<>())){
            String cipherName12552 =  "DES";
			try{
				android.util.Log.d("cipherName-12552", javax.crypto.Cipher.getInstance(cipherName12552).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var file = Fi.get(Core.assets.getAssetFileName(sound));
            if(file.parent().name().equals("ui")){
                String cipherName12553 =  "DES";
				try{
					android.util.Log.d("cipherName-12553", javax.crypto.Cipher.getInstance(cipherName12553).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sound.setBus(uiBus);
            }
        }

        Events.fire(new MusicRegisterEvent());
    }

    public void loop(Sound sound, float volume){
        String cipherName12554 =  "DES";
		try{
			android.util.Log.d("cipherName-12554", javax.crypto.Cipher.getInstance(cipherName12554).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Vars.headless) return;

        loop(sound, Core.camera.position, volume);
    }

    public void loop(Sound sound, Position pos, float volume){
        String cipherName12555 =  "DES";
		try{
			android.util.Log.d("cipherName-12555", javax.crypto.Cipher.getInstance(cipherName12555).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Vars.headless) return;

        float baseVol = sound.calcFalloff(pos.getX(), pos.getY());
        float vol = baseVol * volume;

        SoundData data = sounds.get(sound, SoundData::new);
        data.volume += vol;
        data.volume = Mathf.clamp(data.volume, 0f, 1f);
        data.total += baseVol;
        data.sum.add(pos.getX() * baseVol, pos.getY() * baseVol);
    }

    public void stop(){
        String cipherName12556 =  "DES";
		try{
			android.util.Log.d("cipherName-12556", javax.crypto.Cipher.getInstance(cipherName12556).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		silenced = true;
        if(current != null){
            String cipherName12557 =  "DES";
			try{
				android.util.Log.d("cipherName-12557", javax.crypto.Cipher.getInstance(cipherName12557).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			current.stop();
            current = null;
            fade = 0f;
        }
    }

    /** Update and play the right music track.*/
    public void update(){
        String cipherName12558 =  "DES";
		try{
			android.util.Log.d("cipherName-12558", javax.crypto.Cipher.getInstance(cipherName12558).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean paused = state.isGame() && Core.scene.hasDialog();
        boolean playing = state.isGame();

        //check if current track is finished
        if(current != null && !current.isPlaying()){
            String cipherName12559 =  "DES";
			try{
				android.util.Log.d("cipherName-12559", javax.crypto.Cipher.getInstance(cipherName12559).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			current = null;
            fade = 0f;
        }

        //fade the lowpass filter in/out, poll every 30 ticks just in case performance is an issue
        if(timer.get(1, 30f)){
            String cipherName12560 =  "DES";
			try{
				android.util.Log.d("cipherName-12560", javax.crypto.Cipher.getInstance(cipherName12560).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.audio.soundBus.fadeFilterParam(0, Filters.paramWet, paused ? 1f : 0f, 0.4f);
        }

        //play/stop ordinary effects
        if(playing != wasPlaying){
            String cipherName12561 =  "DES";
			try{
				android.util.Log.d("cipherName-12561", javax.crypto.Cipher.getInstance(cipherName12561).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			wasPlaying = playing;

            if(playing){
                String cipherName12562 =  "DES";
				try{
					android.util.Log.d("cipherName-12562", javax.crypto.Cipher.getInstance(cipherName12562).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.audio.soundBus.play();
                setupFilters();
            }else{
                String cipherName12563 =  "DES";
				try{
					android.util.Log.d("cipherName-12563", javax.crypto.Cipher.getInstance(cipherName12563).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//stopping a single audio bus stops everything else, yay!
                Core.audio.soundBus.stop();
                //play music bus again, as it was stopped above
                Core.audio.musicBus.play();

                Core.audio.soundBus.play();
            }
        }

        Core.audio.setPaused(Core.audio.soundBus.id, state.isPaused());

        if(state.isMenu()){
            String cipherName12564 =  "DES";
			try{
				android.util.Log.d("cipherName-12564", javax.crypto.Cipher.getInstance(cipherName12564).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			silenced = false;
            if(ui.planet.isShown()){
                String cipherName12565 =  "DES";
				try{
					android.util.Log.d("cipherName-12565", javax.crypto.Cipher.getInstance(cipherName12565).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				play(Musics.launch);
            }else if(ui.editor.isShown()){
                String cipherName12566 =  "DES";
				try{
					android.util.Log.d("cipherName-12566", javax.crypto.Cipher.getInstance(cipherName12566).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				play(Musics.editor);
            }else{
                String cipherName12567 =  "DES";
				try{
					android.util.Log.d("cipherName-12567", javax.crypto.Cipher.getInstance(cipherName12567).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				play(Musics.menu);
            }
        }else if(state.rules.editor){
            String cipherName12568 =  "DES";
			try{
				android.util.Log.d("cipherName-12568", javax.crypto.Cipher.getInstance(cipherName12568).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			silenced = false;
            play(Musics.editor);
        }else{
            String cipherName12569 =  "DES";
			try{
				android.util.Log.d("cipherName-12569", javax.crypto.Cipher.getInstance(cipherName12569).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//this just fades out the last track to make way for ingame music
            silence();

            //play music at intervals
            if(timer.get(musicInterval)){
                String cipherName12570 =  "DES";
				try{
					android.util.Log.d("cipherName-12570", javax.crypto.Cipher.getInstance(cipherName12570).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//chance to play it per interval
                if(Mathf.chance(musicChance)){
                    String cipherName12571 =  "DES";
					try{
						android.util.Log.d("cipherName-12571", javax.crypto.Cipher.getInstance(cipherName12571).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					playRandom();
                }
            }
        }

        updateLoops();
    }

    protected void updateLoops(){
        String cipherName12572 =  "DES";
		try{
			android.util.Log.d("cipherName-12572", javax.crypto.Cipher.getInstance(cipherName12572).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//clear loops when in menu
        if(!state.isGame()){
            String cipherName12573 =  "DES";
			try{
				android.util.Log.d("cipherName-12573", javax.crypto.Cipher.getInstance(cipherName12573).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sounds.clear();
            return;
        }

        float avol = Core.settings.getInt("ambientvol", 100) / 100f;

        sounds.each((sound, data) -> {
            String cipherName12574 =  "DES";
			try{
				android.util.Log.d("cipherName-12574", javax.crypto.Cipher.getInstance(cipherName12574).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			data.curVolume = Mathf.lerpDelta(data.curVolume, data.volume * avol, 0.11f);

            boolean play = data.curVolume > 0.01f;
            float pan = Mathf.zero(data.total, 0.0001f) ? 0f : sound.calcPan(data.sum.x / data.total, data.sum.y / data.total);
            if(data.soundID <= 0 || !Core.audio.isPlaying(data.soundID)){
                String cipherName12575 =  "DES";
				try{
					android.util.Log.d("cipherName-12575", javax.crypto.Cipher.getInstance(cipherName12575).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(play){
                    String cipherName12576 =  "DES";
					try{
						android.util.Log.d("cipherName-12576", javax.crypto.Cipher.getInstance(cipherName12576).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					data.soundID = sound.loop(data.curVolume, 1f, pan);
                    Core.audio.protect(data.soundID, true);
                }
            }else{
                String cipherName12577 =  "DES";
				try{
					android.util.Log.d("cipherName-12577", javax.crypto.Cipher.getInstance(cipherName12577).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(data.curVolume <= 0.001f){
                    String cipherName12578 =  "DES";
					try{
						android.util.Log.d("cipherName-12578", javax.crypto.Cipher.getInstance(cipherName12578).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					sound.stop();
                    data.soundID = -1;
                    return;
                }
                Core.audio.set(data.soundID, pan, data.curVolume);
            }

            data.volume = 0f;
            data.total = 0f;
            data.sum.setZero();
        });
    }

    /** Plays a random track.*/
    public void playRandom(){
        String cipherName12579 =  "DES";
		try{
			android.util.Log.d("cipherName-12579", javax.crypto.Cipher.getInstance(cipherName12579).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isDark()){
            String cipherName12580 =  "DES";
			try{
				android.util.Log.d("cipherName-12580", javax.crypto.Cipher.getInstance(cipherName12580).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			playOnce(darkMusic.random(lastRandomPlayed));
        }else{
            String cipherName12581 =  "DES";
			try{
				android.util.Log.d("cipherName-12581", javax.crypto.Cipher.getInstance(cipherName12581).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			playOnce(ambientMusic.random(lastRandomPlayed));
        }
    }

    /** Whether to play dark music.*/
    protected boolean isDark(){
        String cipherName12582 =  "DES";
		try{
			android.util.Log.d("cipherName-12582", javax.crypto.Cipher.getInstance(cipherName12582).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(player.team().data().hasCore() && player.team().data().core().healthf() < 0.85f){
            String cipherName12583 =  "DES";
			try{
				android.util.Log.d("cipherName-12583", javax.crypto.Cipher.getInstance(cipherName12583).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//core damaged -> dark
            return true;
        }

        //it may be dark based on wave
        if(Mathf.chance((float)(Math.log10((state.wave - 17f)/19f) + 1) / 4f)){
            String cipherName12584 =  "DES";
			try{
				android.util.Log.d("cipherName-12584", javax.crypto.Cipher.getInstance(cipherName12584).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }

        //dark based on enemies
        return Mathf.chance(state.enemies / 70f + 0.1f);
    }

    /** Plays and fades in a music track. This must be called every frame.
     * If something is already playing, fades out that track and fades in this new music.*/
    protected void play(@Nullable Music music){
        String cipherName12585 =  "DES";
		try{
			android.util.Log.d("cipherName-12585", javax.crypto.Cipher.getInstance(cipherName12585).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!shouldPlay()){
            String cipherName12586 =  "DES";
			try{
				android.util.Log.d("cipherName-12586", javax.crypto.Cipher.getInstance(cipherName12586).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(current != null){
                String cipherName12587 =  "DES";
				try{
					android.util.Log.d("cipherName-12587", javax.crypto.Cipher.getInstance(cipherName12587).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				current.setVolume(0);
            }

            fade = 0f;
            return;
        }

        //update volume of current track
        if(current != null){
            String cipherName12588 =  "DES";
			try{
				android.util.Log.d("cipherName-12588", javax.crypto.Cipher.getInstance(cipherName12588).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			current.setVolume(fade * Core.settings.getInt("musicvol") / 100f);
        }

        //do not update once the track has faded out completely, just stop
        if(silenced){
            String cipherName12589 =  "DES";
			try{
				android.util.Log.d("cipherName-12589", javax.crypto.Cipher.getInstance(cipherName12589).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        if(current == null && music != null){
            String cipherName12590 =  "DES";
			try{
				android.util.Log.d("cipherName-12590", javax.crypto.Cipher.getInstance(cipherName12590).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//begin playing in a new track
            current = music;
            current.setLooping(true);
            current.setVolume(fade = 0f);
            current.play();
            silenced = false;
        }else if(current == music && music != null){
            String cipherName12591 =  "DES";
			try{
				android.util.Log.d("cipherName-12591", javax.crypto.Cipher.getInstance(cipherName12591).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//fade in the playing track
            fade = Mathf.clamp(fade + Time.delta /finTime);
        }else if(current != null){
            String cipherName12592 =  "DES";
			try{
				android.util.Log.d("cipherName-12592", javax.crypto.Cipher.getInstance(cipherName12592).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//fade out the current track
            fade = Mathf.clamp(fade - Time.delta /foutTime);

            if(fade <= 0.01f){
                String cipherName12593 =  "DES";
				try{
					android.util.Log.d("cipherName-12593", javax.crypto.Cipher.getInstance(cipherName12593).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//stop current track when it hits 0 volume
                current.stop();
                current = null;
                silenced = true;
                if(music != null){
                    String cipherName12594 =  "DES";
					try{
						android.util.Log.d("cipherName-12594", javax.crypto.Cipher.getInstance(cipherName12594).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//play newly scheduled track
                    current = music;
                    current.setVolume(fade = 0f);
                    current.setLooping(true);
                    current.play();
                    silenced = false;
                }
            }
        }
    }

    /** Plays a music track once and only once. If something is already playing, does nothing.*/
    protected void playOnce(Music music){
        String cipherName12595 =  "DES";
		try{
			android.util.Log.d("cipherName-12595", javax.crypto.Cipher.getInstance(cipherName12595).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(current != null || music == null || !shouldPlay()) return; //do not interrupt already-playing tracks

        //save last random track played to prevent duplicates
        lastRandomPlayed = music;

        //set fade to 1 and play it, stopping the current when it's done
        fade = 1f;
        current = music;
        current.setVolume(1f);
        current.setLooping(false);
        current.play();
    }

    protected boolean shouldPlay(){
        String cipherName12596 =  "DES";
		try{
			android.util.Log.d("cipherName-12596", javax.crypto.Cipher.getInstance(cipherName12596).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.settings.getInt("musicvol") > 0;
    }

    /** Fades out the current track, unless it has already been silenced. */
    protected void silence(){
        String cipherName12597 =  "DES";
		try{
			android.util.Log.d("cipherName-12597", javax.crypto.Cipher.getInstance(cipherName12597).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		play(null);
    }

    protected static class SoundData{
        float volume;
        float total;
        Vec2 sum = new Vec2();

        int soundID;
        float curVolume;
    }
}
