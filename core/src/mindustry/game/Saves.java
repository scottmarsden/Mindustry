package mindustry.game;

import arc.*;
import arc.assets.*;
import arc.files.*;
import arc.graphics.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.GameState.*;
import mindustry.game.EventType.*;
import mindustry.io.*;
import mindustry.io.SaveIO.*;
import mindustry.maps.Map;
import mindustry.type.*;

import java.io.*;
import java.text.*;
import java.util.*;

import static mindustry.Vars.*;

public class Saves{
    private static final DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

    Seq<SaveSlot> saves = new Seq<>();
    @Nullable SaveSlot current;
    private @Nullable SaveSlot lastSectorSave;
    private boolean saving;
    private float time;

    long totalPlaytime;
    private long lastTimestamp;

    public Saves(){
        String cipherName11928 =  "DES";
		try{
			android.util.Log.d("cipherName-11928", javax.crypto.Cipher.getInstance(cipherName11928).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.assets.setLoader(Texture.class, ".spreview", new SavePreviewLoader());

        Events.on(StateChangeEvent.class, event -> {
            String cipherName11929 =  "DES";
			try{
				android.util.Log.d("cipherName-11929", javax.crypto.Cipher.getInstance(cipherName11929).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(event.to == State.menu){
                String cipherName11930 =  "DES";
				try{
					android.util.Log.d("cipherName-11930", javax.crypto.Cipher.getInstance(cipherName11930).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				totalPlaytime = 0;
                lastTimestamp = 0;
                current = null;
            }
        });
    }

    public void load(){
        String cipherName11931 =  "DES";
		try{
			android.util.Log.d("cipherName-11931", javax.crypto.Cipher.getInstance(cipherName11931).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		saves.clear();

        for(Fi file : saveDirectory.list()){
            String cipherName11932 =  "DES";
			try{
				android.util.Log.d("cipherName-11932", javax.crypto.Cipher.getInstance(cipherName11932).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!file.name().contains("backup") && SaveIO.isSaveValid(file)){
                String cipherName11933 =  "DES";
				try{
					android.util.Log.d("cipherName-11933", javax.crypto.Cipher.getInstance(cipherName11933).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SaveSlot slot = new SaveSlot(file);
                saves.add(slot);
                slot.meta = SaveIO.getMeta(file);
            }
        }

        //clear saves from build <130 that had the new naval sectors.
        saves.removeAll(s -> {
            String cipherName11934 =  "DES";
			try{
				android.util.Log.d("cipherName-11934", javax.crypto.Cipher.getInstance(cipherName11934).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(s.getSector() != null && (s.getSector().id == 108 || s.getSector().id == 216) && s.meta.build <= 130 && s.meta.build > 0){
                String cipherName11935 =  "DES";
				try{
					android.util.Log.d("cipherName-11935", javax.crypto.Cipher.getInstance(cipherName11935).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				s.getSector().clearInfo();
                s.file.delete();
                return true;
            }
            return false;
        });

        lastSectorSave = saves.find(s -> s.isSector() && s.getName().equals(Core.settings.getString("last-sector-save", "<none>")));

        //automatically assign sector save slots
        for(SaveSlot slot : saves){
            String cipherName11936 =  "DES";
			try{
				android.util.Log.d("cipherName-11936", javax.crypto.Cipher.getInstance(cipherName11936).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(slot.getSector() != null){
                String cipherName11937 =  "DES";
				try{
					android.util.Log.d("cipherName-11937", javax.crypto.Cipher.getInstance(cipherName11937).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(slot.getSector().save != null){
                    String cipherName11938 =  "DES";
					try{
						android.util.Log.d("cipherName-11938", javax.crypto.Cipher.getInstance(cipherName11938).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.warn("Sector @ has two corresponding saves: @ and @", slot.getSector(), slot.getSector().save.file, slot.file);
                }
                slot.getSector().save = slot;
            }
        }
    }

    public @Nullable SaveSlot getLastSector(){
        String cipherName11939 =  "DES";
		try{
			android.util.Log.d("cipherName-11939", javax.crypto.Cipher.getInstance(cipherName11939).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastSectorSave;
    }

    public @Nullable SaveSlot getCurrent(){
        String cipherName11940 =  "DES";
		try{
			android.util.Log.d("cipherName-11940", javax.crypto.Cipher.getInstance(cipherName11940).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return current;
    }

    public void update(){
        String cipherName11941 =  "DES";
		try{
			android.util.Log.d("cipherName-11941", javax.crypto.Cipher.getInstance(cipherName11941).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(current != null && state.isGame()
        && !(state.isPaused() && Core.scene.hasDialog())){
            String cipherName11942 =  "DES";
			try{
				android.util.Log.d("cipherName-11942", javax.crypto.Cipher.getInstance(cipherName11942).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(lastTimestamp != 0){
                String cipherName11943 =  "DES";
				try{
					android.util.Log.d("cipherName-11943", javax.crypto.Cipher.getInstance(cipherName11943).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				totalPlaytime += Time.timeSinceMillis(lastTimestamp);
            }
            lastTimestamp = Time.millis();
        }

        if(state.isGame() && !state.gameOver && current != null && current.isAutosave()){
            String cipherName11944 =  "DES";
			try{
				android.util.Log.d("cipherName-11944", javax.crypto.Cipher.getInstance(cipherName11944).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			time += Time.delta;
            if(time > Core.settings.getInt("saveinterval") * 60){
                String cipherName11945 =  "DES";
				try{
					android.util.Log.d("cipherName-11945", javax.crypto.Cipher.getInstance(cipherName11945).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				saving = true;

                try{
                    String cipherName11946 =  "DES";
					try{
						android.util.Log.d("cipherName-11946", javax.crypto.Cipher.getInstance(cipherName11946).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					current.save();
                }catch(Throwable t){
                    String cipherName11947 =  "DES";
					try{
						android.util.Log.d("cipherName-11947", javax.crypto.Cipher.getInstance(cipherName11947).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.err(t);
                }

                Time.runTask(3f, () -> saving = false);

                time = 0;
            }
        }else{
            String cipherName11948 =  "DES";
			try{
				android.util.Log.d("cipherName-11948", javax.crypto.Cipher.getInstance(cipherName11948).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			time = 0;
        }
    }

    public long getTotalPlaytime(){
        String cipherName11949 =  "DES";
		try{
			android.util.Log.d("cipherName-11949", javax.crypto.Cipher.getInstance(cipherName11949).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return totalPlaytime;
    }

    public void resetSave(){
        String cipherName11950 =  "DES";
		try{
			android.util.Log.d("cipherName-11950", javax.crypto.Cipher.getInstance(cipherName11950).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		current = null;
    }

    public boolean isSaving(){
        String cipherName11951 =  "DES";
		try{
			android.util.Log.d("cipherName-11951", javax.crypto.Cipher.getInstance(cipherName11951).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return saving;
    }

    public Fi getSectorFile(Sector sector){
        String cipherName11952 =  "DES";
		try{
			android.util.Log.d("cipherName-11952", javax.crypto.Cipher.getInstance(cipherName11952).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return saveDirectory.child("sector-" + sector.planet.name + "-" + sector.id + "." + saveExtension);
    }

    public void saveSector(Sector sector){
        String cipherName11953 =  "DES";
		try{
			android.util.Log.d("cipherName-11953", javax.crypto.Cipher.getInstance(cipherName11953).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(sector.save == null){
            String cipherName11954 =  "DES";
			try{
				android.util.Log.d("cipherName-11954", javax.crypto.Cipher.getInstance(cipherName11954).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sector.save = new SaveSlot(getSectorFile(sector));
            sector.save.setName(sector.save.file.nameWithoutExtension());
            saves.add(sector.save);
        }
        sector.save.setAutosave(true);
        sector.save.save();
        lastSectorSave = sector.save;
        Core.settings.put("last-sector-save", sector.save.getName());
    }

    public SaveSlot addSave(String name){
        String cipherName11955 =  "DES";
		try{
			android.util.Log.d("cipherName-11955", javax.crypto.Cipher.getInstance(cipherName11955).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SaveSlot slot = new SaveSlot(getNextSlotFile());
        slot.setName(name);
        saves.add(slot);
        slot.save();
        return slot;
    }

    public SaveSlot importSave(Fi file) throws IOException{
        String cipherName11956 =  "DES";
		try{
			android.util.Log.d("cipherName-11956", javax.crypto.Cipher.getInstance(cipherName11956).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SaveSlot slot = new SaveSlot(getNextSlotFile());
        slot.importFile(file);
        slot.setName(file.nameWithoutExtension());

        saves.add(slot);
        slot.meta = SaveIO.getMeta(slot.file);
        current = slot;
        return slot;
    }

    public Fi getNextSlotFile(){
        String cipherName11957 =  "DES";
		try{
			android.util.Log.d("cipherName-11957", javax.crypto.Cipher.getInstance(cipherName11957).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int i = 0;
        Fi file;
        while((file = saveDirectory.child(i + "." + saveExtension)).exists()){
            String cipherName11958 =  "DES";
			try{
				android.util.Log.d("cipherName-11958", javax.crypto.Cipher.getInstance(cipherName11958).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			i ++;
        }
        return file;
    }

    public Seq<SaveSlot> getSaveSlots(){
        String cipherName11959 =  "DES";
		try{
			android.util.Log.d("cipherName-11959", javax.crypto.Cipher.getInstance(cipherName11959).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return saves;
    }

    public void deleteAll(){
        String cipherName11960 =  "DES";
		try{
			android.util.Log.d("cipherName-11960", javax.crypto.Cipher.getInstance(cipherName11960).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(SaveSlot slot : saves.copy()){
            String cipherName11961 =  "DES";
			try{
				android.util.Log.d("cipherName-11961", javax.crypto.Cipher.getInstance(cipherName11961).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!slot.isSector()){
                String cipherName11962 =  "DES";
				try{
					android.util.Log.d("cipherName-11962", javax.crypto.Cipher.getInstance(cipherName11962).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				slot.delete();
            }
        }
    }

    public class SaveSlot{
        public final Fi file;
        boolean requestedPreview;
        public SaveMeta meta;

        public SaveSlot(Fi file){
            String cipherName11963 =  "DES";
			try{
				android.util.Log.d("cipherName-11963", javax.crypto.Cipher.getInstance(cipherName11963).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.file = file;
        }

        public void load() throws SaveException{
            String cipherName11964 =  "DES";
			try{
				android.util.Log.d("cipherName-11964", javax.crypto.Cipher.getInstance(cipherName11964).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName11965 =  "DES";
				try{
					android.util.Log.d("cipherName-11965", javax.crypto.Cipher.getInstance(cipherName11965).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SaveIO.load(file);
                meta = SaveIO.getMeta(file);
                current = this;
                totalPlaytime = meta.timePlayed;
                savePreview();
            }catch(Throwable e){
                String cipherName11966 =  "DES";
				try{
					android.util.Log.d("cipherName-11966", javax.crypto.Cipher.getInstance(cipherName11966).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new SaveException(e);
            }
        }

        public void save(){
            String cipherName11967 =  "DES";
			try{
				android.util.Log.d("cipherName-11967", javax.crypto.Cipher.getInstance(cipherName11967).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long prev = totalPlaytime;

            SaveIO.save(file);
            meta = SaveIO.getMeta(file);
            if(state.isGame()){
                String cipherName11968 =  "DES";
				try{
					android.util.Log.d("cipherName-11968", javax.crypto.Cipher.getInstance(cipherName11968).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				current = this;
            }

            totalPlaytime = prev;
            savePreview();
        }

        private void savePreview(){
            String cipherName11969 =  "DES";
			try{
				android.util.Log.d("cipherName-11969", javax.crypto.Cipher.getInstance(cipherName11969).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.assets.isLoaded(loadPreviewFile().path())){
                String cipherName11970 =  "DES";
				try{
					android.util.Log.d("cipherName-11970", javax.crypto.Cipher.getInstance(cipherName11970).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.assets.unload(loadPreviewFile().path());
            }
            mainExecutor.submit(() -> {
                String cipherName11971 =  "DES";
				try{
					android.util.Log.d("cipherName-11971", javax.crypto.Cipher.getInstance(cipherName11971).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName11972 =  "DES";
					try{
						android.util.Log.d("cipherName-11972", javax.crypto.Cipher.getInstance(cipherName11972).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					previewFile().writePng(renderer.minimap.getPixmap());
                    requestedPreview = false;
                }catch(Throwable t){
                    String cipherName11973 =  "DES";
					try{
						android.util.Log.d("cipherName-11973", javax.crypto.Cipher.getInstance(cipherName11973).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.err(t);
                }
            });
        }

        public Texture previewTexture(){
            String cipherName11974 =  "DES";
			try{
				android.util.Log.d("cipherName-11974", javax.crypto.Cipher.getInstance(cipherName11974).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!previewFile().exists()){
                String cipherName11975 =  "DES";
				try{
					android.util.Log.d("cipherName-11975", javax.crypto.Cipher.getInstance(cipherName11975).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return null;
            }else if(Core.assets.isLoaded(loadPreviewFile().path())){
                String cipherName11976 =  "DES";
				try{
					android.util.Log.d("cipherName-11976", javax.crypto.Cipher.getInstance(cipherName11976).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return Core.assets.get(loadPreviewFile().path());
            }else if(!requestedPreview){
                String cipherName11977 =  "DES";
				try{
					android.util.Log.d("cipherName-11977", javax.crypto.Cipher.getInstance(cipherName11977).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.assets.load(new AssetDescriptor<>(loadPreviewFile(), Texture.class));
                requestedPreview = true;
            }
            return null;
        }

        private String index(){
            String cipherName11978 =  "DES";
			try{
				android.util.Log.d("cipherName-11978", javax.crypto.Cipher.getInstance(cipherName11978).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return file.nameWithoutExtension();
        }

        private Fi previewFile(){
            String cipherName11979 =  "DES";
			try{
				android.util.Log.d("cipherName-11979", javax.crypto.Cipher.getInstance(cipherName11979).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return mapPreviewDirectory.child("save_slot_" + index() + ".png");
        }

        private Fi loadPreviewFile(){
            String cipherName11980 =  "DES";
			try{
				android.util.Log.d("cipherName-11980", javax.crypto.Cipher.getInstance(cipherName11980).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return previewFile().sibling(previewFile().name() + ".spreview");
        }

        public boolean isHidden(){
            String cipherName11981 =  "DES";
			try{
				android.util.Log.d("cipherName-11981", javax.crypto.Cipher.getInstance(cipherName11981).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return isSector();
        }

        public String getPlayTime(){
            String cipherName11982 =  "DES";
			try{
				android.util.Log.d("cipherName-11982", javax.crypto.Cipher.getInstance(cipherName11982).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Strings.formatMillis(current == this ? totalPlaytime : meta.timePlayed);
        }

        public long getTimestamp(){
            String cipherName11983 =  "DES";
			try{
				android.util.Log.d("cipherName-11983", javax.crypto.Cipher.getInstance(cipherName11983).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return meta.timestamp;
        }

        public String getDate(){
            String cipherName11984 =  "DES";
			try{
				android.util.Log.d("cipherName-11984", javax.crypto.Cipher.getInstance(cipherName11984).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return dateFormat.format(new Date(meta.timestamp));
        }

        public Map getMap(){
            String cipherName11985 =  "DES";
			try{
				android.util.Log.d("cipherName-11985", javax.crypto.Cipher.getInstance(cipherName11985).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return meta.map;
        }

        public void cautiousLoad(Runnable run){
            String cipherName11986 =  "DES";
			try{
				android.util.Log.d("cipherName-11986", javax.crypto.Cipher.getInstance(cipherName11986).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Seq<String> mods = Seq.with(getMods());
            mods.removeAll(Vars.mods.getModStrings());

            if(!mods.isEmpty()){
                String cipherName11987 =  "DES";
				try{
					android.util.Log.d("cipherName-11987", javax.crypto.Cipher.getInstance(cipherName11987).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showConfirm("@warning", Core.bundle.format("mod.missing", mods.toString("\n")), run);
            }else{
                String cipherName11988 =  "DES";
				try{
					android.util.Log.d("cipherName-11988", javax.crypto.Cipher.getInstance(cipherName11988).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				run.run();
            }
        }

        public String getName(){
            String cipherName11989 =  "DES";
			try{
				android.util.Log.d("cipherName-11989", javax.crypto.Cipher.getInstance(cipherName11989).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.settings.getString("save-" + index() + "-name", "untitled");
        }

        public void setName(String name){
            String cipherName11990 =  "DES";
			try{
				android.util.Log.d("cipherName-11990", javax.crypto.Cipher.getInstance(cipherName11990).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.settings.put("save-" + index() + "-name", name);
        }

        public String[] getMods(){
            String cipherName11991 =  "DES";
			try{
				android.util.Log.d("cipherName-11991", javax.crypto.Cipher.getInstance(cipherName11991).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return meta.mods;
        }

        public @Nullable Sector getSector(){
            String cipherName11992 =  "DES";
			try{
				android.util.Log.d("cipherName-11992", javax.crypto.Cipher.getInstance(cipherName11992).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return meta == null || meta.rules == null ? null : meta.rules.sector;
        }

        public boolean isSector(){
            String cipherName11993 =  "DES";
			try{
				android.util.Log.d("cipherName-11993", javax.crypto.Cipher.getInstance(cipherName11993).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return getSector() != null;
        }

        public Gamemode mode(){
            String cipherName11994 =  "DES";
			try{
				android.util.Log.d("cipherName-11994", javax.crypto.Cipher.getInstance(cipherName11994).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return meta.rules.mode();
        }

        public int getBuild(){
            String cipherName11995 =  "DES";
			try{
				android.util.Log.d("cipherName-11995", javax.crypto.Cipher.getInstance(cipherName11995).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return meta.build;
        }

        public int getWave(){
            String cipherName11996 =  "DES";
			try{
				android.util.Log.d("cipherName-11996", javax.crypto.Cipher.getInstance(cipherName11996).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return meta.wave;
        }

        public boolean isAutosave(){
            String cipherName11997 =  "DES";
			try{
				android.util.Log.d("cipherName-11997", javax.crypto.Cipher.getInstance(cipherName11997).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.settings.getBool("save-" + index() + "-autosave", true);
        }

        public void setAutosave(boolean save){
            String cipherName11998 =  "DES";
			try{
				android.util.Log.d("cipherName-11998", javax.crypto.Cipher.getInstance(cipherName11998).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.settings.put("save-" + index() + "-autosave", save);
        }

        public void importFile(Fi from) throws IOException{
            String cipherName11999 =  "DES";
			try{
				android.util.Log.d("cipherName-11999", javax.crypto.Cipher.getInstance(cipherName11999).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName12000 =  "DES";
				try{
					android.util.Log.d("cipherName-12000", javax.crypto.Cipher.getInstance(cipherName12000).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				from.copyTo(file);
                if(previewFile().exists()){
                    String cipherName12001 =  "DES";
					try{
						android.util.Log.d("cipherName-12001", javax.crypto.Cipher.getInstance(cipherName12001).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					requestedPreview = false;
                    previewFile().delete();
                }
            }catch(Exception e){
                String cipherName12002 =  "DES";
				try{
					android.util.Log.d("cipherName-12002", javax.crypto.Cipher.getInstance(cipherName12002).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IOException(e);
            }
        }

        public void exportFile(Fi to) throws IOException{
            String cipherName12003 =  "DES";
			try{
				android.util.Log.d("cipherName-12003", javax.crypto.Cipher.getInstance(cipherName12003).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName12004 =  "DES";
				try{
					android.util.Log.d("cipherName-12004", javax.crypto.Cipher.getInstance(cipherName12004).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				file.copyTo(to);
            }catch(Exception e){
                String cipherName12005 =  "DES";
				try{
					android.util.Log.d("cipherName-12005", javax.crypto.Cipher.getInstance(cipherName12005).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IOException(e);
            }
        }

        public void delete(){
            String cipherName12006 =  "DES";
			try{
				android.util.Log.d("cipherName-12006", javax.crypto.Cipher.getInstance(cipherName12006).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(SaveIO.backupFileFor(file).exists()){
                String cipherName12007 =  "DES";
				try{
					android.util.Log.d("cipherName-12007", javax.crypto.Cipher.getInstance(cipherName12007).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SaveIO.backupFileFor(file).delete();
            }
            file.delete();
            saves.remove(this, true);
            if(this == current){
                String cipherName12008 =  "DES";
				try{
					android.util.Log.d("cipherName-12008", javax.crypto.Cipher.getInstance(cipherName12008).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				current = null;
            }

            if(Core.assets.isLoaded(loadPreviewFile().path())){
                String cipherName12009 =  "DES";
				try{
					android.util.Log.d("cipherName-12009", javax.crypto.Cipher.getInstance(cipherName12009).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.assets.unload(loadPreviewFile().path());
            }
        }
    }
}
