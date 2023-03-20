package mindustry.core;

import arc.*;
import arc.files.*;
import arc.func.*;
import arc.graphics.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import mindustry.game.EventType.*;
import mindustry.io.*;
import mindustry.mod.Mods.*;
import mindustry.type.*;
import mindustry.world.*;

import static arc.Core.*;
import static mindustry.Vars.*;

/**
 * Loads all game content.
 * Call load() before doing anything with content.
 */
@SuppressWarnings("unchecked")
public class ContentLoader{
    private ObjectMap<String, MappableContent>[] contentNameMap = new ObjectMap[ContentType.all.length];
    private Seq<Content>[] contentMap = new Seq[ContentType.all.length];
    private MappableContent[][] temporaryMapper;
    private @Nullable LoadedMod currentMod;
    private @Nullable Content lastAdded;
    private ObjectSet<Cons<Content>> initialization = new ObjectSet<>();

    public ContentLoader(){
        String cipherName4084 =  "DES";
		try{
			android.util.Log.d("cipherName-4084", javax.crypto.Cipher.getInstance(cipherName4084).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(ContentType type : ContentType.all){
            String cipherName4085 =  "DES";
			try{
				android.util.Log.d("cipherName-4085", javax.crypto.Cipher.getInstance(cipherName4085).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			contentMap[type.ordinal()] = new Seq<>();
            contentNameMap[type.ordinal()] = new ObjectMap<>();
        }
    }

    /** Creates all base types. */
    public void createBaseContent(){
        String cipherName4086 =  "DES";
		try{
			android.util.Log.d("cipherName-4086", javax.crypto.Cipher.getInstance(cipherName4086).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TeamEntries.load();
        Items.load();
        StatusEffects.load();
        Liquids.load();
        Bullets.load();
        UnitTypes.load();
        Blocks.load();
        Loadouts.load();
        Weathers.load();
        Planets.load();
        SectorPresets.load();
        SerpuloTechTree.load();
        ErekirTechTree.load();
    }

    /** Creates mod content, if applicable. */
    public void createModContent(){
        String cipherName4087 =  "DES";
		try{
			android.util.Log.d("cipherName-4087", javax.crypto.Cipher.getInstance(cipherName4087).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(mods != null){
            String cipherName4088 =  "DES";
			try{
				android.util.Log.d("cipherName-4088", javax.crypto.Cipher.getInstance(cipherName4088).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mods.loadContent();
        }
    }

    /** Logs content statistics. */
    public void logContent(){
        String cipherName4089 =  "DES";
		try{
			android.util.Log.d("cipherName-4089", javax.crypto.Cipher.getInstance(cipherName4089).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//check up ID mapping, make sure it's linear (debug only)
        for(Seq<Content> arr : contentMap){
            String cipherName4090 =  "DES";
			try{
				android.util.Log.d("cipherName-4090", javax.crypto.Cipher.getInstance(cipherName4090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = 0; i < arr.size; i++){
                String cipherName4091 =  "DES";
				try{
					android.util.Log.d("cipherName-4091", javax.crypto.Cipher.getInstance(cipherName4091).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int id = arr.get(i).id;
                if(id != i){
                    String cipherName4092 =  "DES";
					try{
						android.util.Log.d("cipherName-4092", javax.crypto.Cipher.getInstance(cipherName4092).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IllegalArgumentException("Out-of-order IDs for content '" + arr.get(i) + "' (expected " + i + " but got " + id + ")");
                }
            }
        }

        Log.debug("--- CONTENT INFO ---");
        for(int k = 0; k < contentMap.length; k++){
            String cipherName4093 =  "DES";
			try{
				android.util.Log.d("cipherName-4093", javax.crypto.Cipher.getInstance(cipherName4093).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.debug("[@]: loaded @", ContentType.all[k].name(), contentMap[k].size);
        }
        Log.debug("Total content loaded: @", Seq.with(ContentType.all).mapInt(c -> contentMap[c.ordinal()].size).sum());
        Log.debug("-------------------");
    }

    /** Calls Content#init() on everything. Use only after all modules have been created. */
    public void init(){
        String cipherName4094 =  "DES";
		try{
			android.util.Log.d("cipherName-4094", javax.crypto.Cipher.getInstance(cipherName4094).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		initialize(Content::init);
        if(logicVars != null) logicVars.init();
        Events.fire(new ContentInitEvent());
    }

    /** Calls Content#loadIcon() and Content#load() on everything. Use only after all modules have been created on the client. */
    public void load(){
        String cipherName4095 =  "DES";
		try{
			android.util.Log.d("cipherName-4095", javax.crypto.Cipher.getInstance(cipherName4095).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		initialize(Content::loadIcon);
        initialize(Content::load);
    }

    /** Initializes all content with the specified function. */
    private void initialize(Cons<Content> callable){
        String cipherName4096 =  "DES";
		try{
			android.util.Log.d("cipherName-4096", javax.crypto.Cipher.getInstance(cipherName4096).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(initialization.contains(callable)) return;

        for(ContentType type : ContentType.all){
            String cipherName4097 =  "DES";
			try{
				android.util.Log.d("cipherName-4097", javax.crypto.Cipher.getInstance(cipherName4097).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Content content : contentMap[type.ordinal()]){
                String cipherName4098 =  "DES";
				try{
					android.util.Log.d("cipherName-4098", javax.crypto.Cipher.getInstance(cipherName4098).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName4099 =  "DES";
					try{
						android.util.Log.d("cipherName-4099", javax.crypto.Cipher.getInstance(cipherName4099).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					callable.get(content);
                }catch(Throwable e){
                    String cipherName4100 =  "DES";
					try{
						android.util.Log.d("cipherName-4100", javax.crypto.Cipher.getInstance(cipherName4100).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(content.minfo.mod != null){
                        String cipherName4101 =  "DES";
						try{
							android.util.Log.d("cipherName-4101", javax.crypto.Cipher.getInstance(cipherName4101).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Log.err(e);
                        mods.handleContentError(content, e);
                    }else{
                        String cipherName4102 =  "DES";
						try{
							android.util.Log.d("cipherName-4102", javax.crypto.Cipher.getInstance(cipherName4102).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new RuntimeException(e);
                    }
                }
            }
        }

        initialization.add(callable);
    }

    /** Loads block colors. */
    public void loadColors(){
        String cipherName4103 =  "DES";
		try{
			android.util.Log.d("cipherName-4103", javax.crypto.Cipher.getInstance(cipherName4103).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Pixmap pixmap = new Pixmap(files.internal("sprites/block_colors.png"));
        for(int i = 0; i < pixmap.width; i++){
            String cipherName4104 =  "DES";
			try{
				android.util.Log.d("cipherName-4104", javax.crypto.Cipher.getInstance(cipherName4104).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(blocks().size > i){
                String cipherName4105 =  "DES";
				try{
					android.util.Log.d("cipherName-4105", javax.crypto.Cipher.getInstance(cipherName4105).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int color = pixmap.get(i, 0);

                if(color == 0 || color == 255) continue;

                Block block = block(i);
                block.mapColor.rgba8888(color);
                //partial alpha colors indicate a square sprite
                block.squareSprite = block.mapColor.a > 0.5f;
                block.mapColor.a = 1f;
                block.hasColor = true;
            }
        }
        pixmap.dispose();
        ColorMapper.load();
    }

    /** Get last piece of content created for error-handling purposes. */
    public @Nullable Content getLastAdded(){
        String cipherName4106 =  "DES";
		try{
			android.util.Log.d("cipherName-4106", javax.crypto.Cipher.getInstance(cipherName4106).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastAdded;
    }

    /** Remove last content added in case of an exception. */
    public void removeLast(){
		String cipherName4107 =  "DES";
		try{
			android.util.Log.d("cipherName-4107", javax.crypto.Cipher.getInstance(cipherName4107).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(lastAdded != null && contentMap[lastAdded.getContentType().ordinal()].peek() == lastAdded){
            contentMap[lastAdded.getContentType().ordinal()].pop();
            if(lastAdded instanceof MappableContent c){
                contentNameMap[lastAdded.getContentType().ordinal()].remove(c.name);
            }
        }
    }

    public void handleContent(Content content){
        String cipherName4108 =  "DES";
		try{
			android.util.Log.d("cipherName-4108", javax.crypto.Cipher.getInstance(cipherName4108).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.lastAdded = content;
        contentMap[content.getContentType().ordinal()].add(content);
    }

    public void setCurrentMod(@Nullable LoadedMod mod){
        String cipherName4109 =  "DES";
		try{
			android.util.Log.d("cipherName-4109", javax.crypto.Cipher.getInstance(cipherName4109).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.currentMod = mod;
    }

    public String transformName(String name){
        String cipherName4110 =  "DES";
		try{
			android.util.Log.d("cipherName-4110", javax.crypto.Cipher.getInstance(cipherName4110).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return currentMod == null ? name : currentMod.name + "-" + name;
    }

    public void handleMappableContent(MappableContent content){
        String cipherName4111 =  "DES";
		try{
			android.util.Log.d("cipherName-4111", javax.crypto.Cipher.getInstance(cipherName4111).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(contentNameMap[content.getContentType().ordinal()].containsKey(content.name)){
            String cipherName4112 =  "DES";
			try{
				android.util.Log.d("cipherName-4112", javax.crypto.Cipher.getInstance(cipherName4112).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Two content objects cannot have the same name! (issue: '" + content.name + "')");
        }
        if(currentMod != null){
            String cipherName4113 =  "DES";
			try{
				android.util.Log.d("cipherName-4113", javax.crypto.Cipher.getInstance(cipherName4113).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			content.minfo.mod = currentMod;
            if(content.minfo.sourceFile == null){
                String cipherName4114 =  "DES";
				try{
					android.util.Log.d("cipherName-4114", javax.crypto.Cipher.getInstance(cipherName4114).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				content.minfo.sourceFile = new Fi(content.name);
            }
        }
        contentNameMap[content.getContentType().ordinal()].put(content.name, content);
    }

    public void setTemporaryMapper(MappableContent[][] temporaryMapper){
        String cipherName4115 =  "DES";
		try{
			android.util.Log.d("cipherName-4115", javax.crypto.Cipher.getInstance(cipherName4115).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.temporaryMapper = temporaryMapper;
    }

    public Seq<Content>[] getContentMap(){
        String cipherName4116 =  "DES";
		try{
			android.util.Log.d("cipherName-4116", javax.crypto.Cipher.getInstance(cipherName4116).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return contentMap;
    }

    public void each(Cons<Content> cons){
        String cipherName4117 =  "DES";
		try{
			android.util.Log.d("cipherName-4117", javax.crypto.Cipher.getInstance(cipherName4117).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Seq<Content> seq : contentMap){
            String cipherName4118 =  "DES";
			try{
				android.util.Log.d("cipherName-4118", javax.crypto.Cipher.getInstance(cipherName4118).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			seq.each(cons);
        }
    }

    public <T extends MappableContent> T getByName(ContentType type, String name){
        String cipherName4119 =  "DES";
		try{
			android.util.Log.d("cipherName-4119", javax.crypto.Cipher.getInstance(cipherName4119).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var map = contentNameMap[type.ordinal()];

        if(map == null) return null;

        //load fallbacks
        if(type == ContentType.block){
            String cipherName4120 =  "DES";
			try{
				android.util.Log.d("cipherName-4120", javax.crypto.Cipher.getInstance(cipherName4120).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			name = SaveVersion.modContentNameMap.get(name, name);
        }

        return (T)map.get(name);
    }

    public <T extends Content> T getByID(ContentType type, int id){

        String cipherName4121 =  "DES";
		try{
			android.util.Log.d("cipherName-4121", javax.crypto.Cipher.getInstance(cipherName4121).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(temporaryMapper != null && temporaryMapper[type.ordinal()] != null && temporaryMapper[type.ordinal()].length != 0){
            String cipherName4122 =  "DES";
			try{
				android.util.Log.d("cipherName-4122", javax.crypto.Cipher.getInstance(cipherName4122).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//-1 = invalid content
            if(id < 0){
                String cipherName4123 =  "DES";
				try{
					android.util.Log.d("cipherName-4123", javax.crypto.Cipher.getInstance(cipherName4123).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return null;
            }
            if(temporaryMapper[type.ordinal()].length <= id || temporaryMapper[type.ordinal()][id] == null){
                String cipherName4124 =  "DES";
				try{
					android.util.Log.d("cipherName-4124", javax.crypto.Cipher.getInstance(cipherName4124).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return (T)contentMap[type.ordinal()].get(0); //default value is always ID 0
            }
            return (T)temporaryMapper[type.ordinal()][id];
        }

        if(id >= contentMap[type.ordinal()].size || id < 0){
            String cipherName4125 =  "DES";
			try{
				android.util.Log.d("cipherName-4125", javax.crypto.Cipher.getInstance(cipherName4125).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        return (T)contentMap[type.ordinal()].get(id);
    }

    public <T extends Content> Seq<T> getBy(ContentType type){
        String cipherName4126 =  "DES";
		try{
			android.util.Log.d("cipherName-4126", javax.crypto.Cipher.getInstance(cipherName4126).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (Seq<T>)contentMap[type.ordinal()];
    }

    //utility methods, just makes things a bit shorter

    public Seq<Block> blocks(){
        String cipherName4127 =  "DES";
		try{
			android.util.Log.d("cipherName-4127", javax.crypto.Cipher.getInstance(cipherName4127).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getBy(ContentType.block);
    }

    public Block block(int id){
        String cipherName4128 =  "DES";
		try{
			android.util.Log.d("cipherName-4128", javax.crypto.Cipher.getInstance(cipherName4128).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByID(ContentType.block, id);
    }

    public Block block(String name){
        String cipherName4129 =  "DES";
		try{
			android.util.Log.d("cipherName-4129", javax.crypto.Cipher.getInstance(cipherName4129).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByName(ContentType.block, name);
    }

    public Seq<Item> items(){
        String cipherName4130 =  "DES";
		try{
			android.util.Log.d("cipherName-4130", javax.crypto.Cipher.getInstance(cipherName4130).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getBy(ContentType.item);
    }

    public Item item(int id){
        String cipherName4131 =  "DES";
		try{
			android.util.Log.d("cipherName-4131", javax.crypto.Cipher.getInstance(cipherName4131).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByID(ContentType.item, id);
    }

    public Item item(String name){
        String cipherName4132 =  "DES";
		try{
			android.util.Log.d("cipherName-4132", javax.crypto.Cipher.getInstance(cipherName4132).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByName(ContentType.item, name);
    }

    public Seq<Liquid> liquids(){
        String cipherName4133 =  "DES";
		try{
			android.util.Log.d("cipherName-4133", javax.crypto.Cipher.getInstance(cipherName4133).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getBy(ContentType.liquid);
    }

    public Liquid liquid(int id){
        String cipherName4134 =  "DES";
		try{
			android.util.Log.d("cipherName-4134", javax.crypto.Cipher.getInstance(cipherName4134).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByID(ContentType.liquid, id);
    }

    public Liquid liquid(String name){
        String cipherName4135 =  "DES";
		try{
			android.util.Log.d("cipherName-4135", javax.crypto.Cipher.getInstance(cipherName4135).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByName(ContentType.liquid, name);
    }

    public Seq<BulletType> bullets(){
        String cipherName4136 =  "DES";
		try{
			android.util.Log.d("cipherName-4136", javax.crypto.Cipher.getInstance(cipherName4136).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getBy(ContentType.bullet);
    }

    public BulletType bullet(int id){
        String cipherName4137 =  "DES";
		try{
			android.util.Log.d("cipherName-4137", javax.crypto.Cipher.getInstance(cipherName4137).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByID(ContentType.bullet, id);
    }

    public Seq<StatusEffect> statusEffects(){
        String cipherName4138 =  "DES";
		try{
			android.util.Log.d("cipherName-4138", javax.crypto.Cipher.getInstance(cipherName4138).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getBy(ContentType.status);
    }

    public StatusEffect statusEffect(String name){
        String cipherName4139 =  "DES";
		try{
			android.util.Log.d("cipherName-4139", javax.crypto.Cipher.getInstance(cipherName4139).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByName(ContentType.status, name);
    }

    public Seq<SectorPreset> sectors(){
        String cipherName4140 =  "DES";
		try{
			android.util.Log.d("cipherName-4140", javax.crypto.Cipher.getInstance(cipherName4140).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getBy(ContentType.sector);
    }

    public SectorPreset sector(String name){
        String cipherName4141 =  "DES";
		try{
			android.util.Log.d("cipherName-4141", javax.crypto.Cipher.getInstance(cipherName4141).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByName(ContentType.sector, name);
    }

    public Seq<UnitType> units(){
        String cipherName4142 =  "DES";
		try{
			android.util.Log.d("cipherName-4142", javax.crypto.Cipher.getInstance(cipherName4142).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getBy(ContentType.unit);
    }

    public UnitType unit(int id){
        String cipherName4143 =  "DES";
		try{
			android.util.Log.d("cipherName-4143", javax.crypto.Cipher.getInstance(cipherName4143).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByID(ContentType.unit, id);
    }

    public UnitType unit(String name){
        String cipherName4144 =  "DES";
		try{
			android.util.Log.d("cipherName-4144", javax.crypto.Cipher.getInstance(cipherName4144).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByName(ContentType.unit, name);
    }

    public Seq<Planet> planets(){
        String cipherName4145 =  "DES";
		try{
			android.util.Log.d("cipherName-4145", javax.crypto.Cipher.getInstance(cipherName4145).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getBy(ContentType.planet);
    }

    public Planet planet(String name){
        String cipherName4146 =  "DES";
		try{
			android.util.Log.d("cipherName-4146", javax.crypto.Cipher.getInstance(cipherName4146).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getByName(ContentType.planet, name);
    }
}
