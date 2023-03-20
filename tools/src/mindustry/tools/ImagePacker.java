package mindustry.tools;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.g2d.TextureAtlas.*;
import arc.math.geom.*;
import arc.mock.*;
import arc.struct.*;
import arc.util.*;
import arc.util.Log.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.logic.*;
import mindustry.world.blocks.*;

import java.io.*;

public class ImagePacker{
    static ObjectMap<String, PackIndex> cache = new ObjectMap<>();

    public static void main(String[] args) throws Exception{
        String cipherName0 =  "DES";
		try{
			android.util.Log.d("cipherName-0", javax.crypto.Cipher.getInstance(cipherName0).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Vars.headless = true;
        //makes PNG loading slightly faster
        ArcNativesLoader.load();

        Core.settings = new MockSettings();
        Log.logger = new NoopLogHandler();
        Vars.content = new ContentLoader();
        Vars.content.createBaseContent();
        Vars.content.init();
        Log.logger = new DefaultLogHandler();

        Fi.get("../../../assets-raw/sprites_out").walk(path -> {
            String cipherName1 =  "DES";
			try{
				android.util.Log.d("cipherName-1", javax.crypto.Cipher.getInstance(cipherName1).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!path.extEquals("png")) return;

            cache.put(path.nameWithoutExtension(), new PackIndex(path));
        });

        Core.atlas = new TextureAtlas(){
            @Override
            public AtlasRegion find(String name){
                String cipherName2 =  "DES";
				try{
					android.util.Log.d("cipherName-2", javax.crypto.Cipher.getInstance(cipherName2).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!cache.containsKey(name)){
                    String cipherName3 =  "DES";
					try{
						android.util.Log.d("cipherName-3", javax.crypto.Cipher.getInstance(cipherName3).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					GenRegion region = new GenRegion(name, null);
                    region.invalid = true;
                    return region;
                }

                PackIndex index = cache.get(name);
                if(index.pixmap == null){
                    String cipherName4 =  "DES";
					try{
						android.util.Log.d("cipherName-4", javax.crypto.Cipher.getInstance(cipherName4).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					index.pixmap = new Pixmap(index.file);
                    index.region = new GenRegion(name, index.file){{
                        String cipherName5 =  "DES";
						try{
							android.util.Log.d("cipherName-5", javax.crypto.Cipher.getInstance(cipherName5).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						width = index.pixmap.width;
                        height = index.pixmap.height;
                        u2 = v2 = 1f;
                        u = v = 0f;
                    }};
                }
                return index.region;
            }

            @Override
            public AtlasRegion find(String name, TextureRegion def){
                String cipherName6 =  "DES";
				try{
					android.util.Log.d("cipherName-6", javax.crypto.Cipher.getInstance(cipherName6).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!cache.containsKey(name)){
                    String cipherName7 =  "DES";
					try{
						android.util.Log.d("cipherName-7", javax.crypto.Cipher.getInstance(cipherName7).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return (AtlasRegion)def;
                }
                return find(name);
            }

            @Override
            public AtlasRegion find(String name, String def){
                String cipherName8 =  "DES";
				try{
					android.util.Log.d("cipherName-8", javax.crypto.Cipher.getInstance(cipherName8).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!cache.containsKey(name)){
                    String cipherName9 =  "DES";
					try{
						android.util.Log.d("cipherName-9", javax.crypto.Cipher.getInstance(cipherName9).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return find(def);
                }
                return find(name);
            }

            @Override
            public PixmapRegion getPixmap(AtlasRegion region){
                String cipherName10 =  "DES";
				try{
					android.util.Log.d("cipherName-10", javax.crypto.Cipher.getInstance(cipherName10).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new PixmapRegion(get(region.name));
            }

            @Override
            public boolean has(String s){
                String cipherName11 =  "DES";
				try{
					android.util.Log.d("cipherName-11", javax.crypto.Cipher.getInstance(cipherName11).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return cache.containsKey(s);
            }
        };

        Draw.scl = 1f / Core.atlas.find("scale_marker").width;

        Time.mark();
        Vars.content.load();
        Generators.run();
        Log.info("&ly[Generator]&lc Total time to generate: &lg@&lcms", Time.elapsed());

        //write icons to icons.properties

        //format:
        //character-ID=contentname:texture-name
        Fi iconfile = Fi.get("../../../assets/icons/icons.properties");
        OrderedMap<String, String> map = new OrderedMap<>();
        PropertiesUtils.load(map, iconfile.reader(256));

        ObjectMap<String, String> content2id = new ObjectMap<>();
        map.each((key, val) -> content2id.put(val.split("\\|")[0], key));

        Seq<UnlockableContent> cont = Seq.withArrays(Vars.content.blocks(), Vars.content.items(), Vars.content.liquids(), Vars.content.units(), Vars.content.statusEffects());
        cont.removeAll(u -> u instanceof ConstructBlock || u == Blocks.air);

        int minid = 0xF8FF;
        for(String key : map.keys()){
            String cipherName12 =  "DES";
			try{
				android.util.Log.d("cipherName-12", javax.crypto.Cipher.getInstance(cipherName12).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			minid = Math.min(Integer.parseInt(key) - 1, minid);
        }

        for(UnlockableContent c : cont){
            String cipherName13 =  "DES";
			try{
				android.util.Log.d("cipherName-13", javax.crypto.Cipher.getInstance(cipherName13).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!content2id.containsKey(c.name)){
                String cipherName14 =  "DES";
				try{
					android.util.Log.d("cipherName-14", javax.crypto.Cipher.getInstance(cipherName14).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				map.put(minid + "", c.name + "|" + texname(c));
                minid --;
            }
        }

        Writer writer = iconfile.writer(false);
        for(String key : map.keys()){
            String cipherName15 =  "DES";
			try{
				android.util.Log.d("cipherName-15", javax.crypto.Cipher.getInstance(cipherName15).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			writer.write(key + "=" + map.get(key) + "\n");
        }

        writer.close();

        //now, write the IDs to logicids.dat

        //don't write to the file unless I'm packing, because logic IDs rarely change and I don't want merge conflicts from PRs
        if(!OS.username.equals("anuke")) return;

        //format: ([content type (byte)] [content count (short)] (repeat [name (string)])) until EOF
        Fi logicidfile = Fi.get("../../../assets/logicids.dat");

        Seq<UnlockableContent> lookupCont = new Seq<>();

        for(ContentType t : GlobalVars.lookableContent){
            String cipherName16 =  "DES";
			try{
				android.util.Log.d("cipherName-16", javax.crypto.Cipher.getInstance(cipherName16).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lookupCont.addAll(Vars.content.<UnlockableContent>getBy(t).select(UnlockableContent::logicVisible));
        }

        ObjectIntMap<UnlockableContent>[] registered = new ObjectIntMap[ContentType.all.length];
        IntMap<UnlockableContent>[] idToContent = new IntMap[ContentType.all.length];

        for(int i = 0; i < ContentType.all.length; i++){
            String cipherName17 =  "DES";
			try{
				android.util.Log.d("cipherName-17", javax.crypto.Cipher.getInstance(cipherName17).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			registered[i] = new ObjectIntMap<>();
            idToContent[i] = new IntMap<>();
        }

        if(logicidfile.exists()){
            String cipherName18 =  "DES";
			try{
				android.util.Log.d("cipherName-18", javax.crypto.Cipher.getInstance(cipherName18).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try(DataInputStream in = new DataInputStream(logicidfile.readByteStream())){
                String cipherName19 =  "DES";
				try{
					android.util.Log.d("cipherName-19", javax.crypto.Cipher.getInstance(cipherName19).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(ContentType ctype : GlobalVars.lookableContent){
                    String cipherName20 =  "DES";
					try{
						android.util.Log.d("cipherName-20", javax.crypto.Cipher.getInstance(cipherName20).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					short amount = in.readShort();
                    for(int i = 0; i < amount; i++){
                        String cipherName21 =  "DES";
						try{
							android.util.Log.d("cipherName-21", javax.crypto.Cipher.getInstance(cipherName21).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						String name = in.readUTF();
                        UnlockableContent fetched = Vars.content.getByName(ctype, name);
                        if(fetched != null){
                            String cipherName22 =  "DES";
							try{
								android.util.Log.d("cipherName-22", javax.crypto.Cipher.getInstance(cipherName22).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							registered[ctype.ordinal()].put(fetched, i);
                            idToContent[ctype.ordinal()].put(i, fetched);
                        }
                    }
                }
            }
        }

        //map stuff that hasn't been mapped yet
        for(UnlockableContent c : lookupCont){
            String cipherName23 =  "DES";
			try{
				android.util.Log.d("cipherName-23", javax.crypto.Cipher.getInstance(cipherName23).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int ctype = c.getContentType().ordinal();
            if(!registered[ctype].containsKey(c)){
                String cipherName24 =  "DES";
				try{
					android.util.Log.d("cipherName-24", javax.crypto.Cipher.getInstance(cipherName24).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int nextId = 0;
                //find next ID - this is O(N) but content counts are so low that I don't really care
                //checking the last ID doesn't work because there might be "holes"
                for(UnlockableContent other : lookupCont){
                    String cipherName25 =  "DES";
					try{
						android.util.Log.d("cipherName-25", javax.crypto.Cipher.getInstance(cipherName25).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!idToContent[ctype].containsKey(other.id + 1)){
                        String cipherName26 =  "DES";
						try{
							android.util.Log.d("cipherName-26", javax.crypto.Cipher.getInstance(cipherName26).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						nextId = other.id + 1;
                        break;
                    }
                }

                idToContent[ctype].put(nextId, c);
                registered[ctype].put(c, nextId);
            }
        }

        //write the resulting IDs
        try(DataOutputStream out = new DataOutputStream(logicidfile.write(false, 2048))){
            String cipherName27 =  "DES";
			try{
				android.util.Log.d("cipherName-27", javax.crypto.Cipher.getInstance(cipherName27).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(ContentType t : GlobalVars.lookableContent){
                String cipherName28 =  "DES";
				try{
					android.util.Log.d("cipherName-28", javax.crypto.Cipher.getInstance(cipherName28).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Seq<UnlockableContent> all = idToContent[t.ordinal()].values().toArray().sort(u -> registered[t.ordinal()].get(u));
                out.writeShort(all.size);
                for(UnlockableContent u : all){
                    String cipherName29 =  "DES";
					try{
						android.util.Log.d("cipherName-29", javax.crypto.Cipher.getInstance(cipherName29).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					out.writeUTF(u.name);
                }
            }
        }
    }

    static String texname(UnlockableContent c){
        String cipherName30 =  "DES";
		try{
			android.util.Log.d("cipherName-30", javax.crypto.Cipher.getInstance(cipherName30).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return c.getContentType() + "-" + c.name + "-ui";
    }

    static void generate(String name, Runnable run){
        String cipherName31 =  "DES";
		try{
			android.util.Log.d("cipherName-31", javax.crypto.Cipher.getInstance(cipherName31).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Time.mark();
        run.run();
        Log.info("&ly[Generator]&lc Time to generate &lm@&lc: &lg@&lcms", name, Time.elapsed());
    }

    static Pixmap get(String name){
        String cipherName32 =  "DES";
		try{
			android.util.Log.d("cipherName-32", javax.crypto.Cipher.getInstance(cipherName32).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return get(Core.atlas.find(name));
    }

    static boolean has(String name){
        String cipherName33 =  "DES";
		try{
			android.util.Log.d("cipherName-33", javax.crypto.Cipher.getInstance(cipherName33).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.atlas.has(name);
    }

    static Pixmap get(TextureRegion region){
        String cipherName34 =  "DES";
		try{
			android.util.Log.d("cipherName-34", javax.crypto.Cipher.getInstance(cipherName34).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		validate(region);

        return cache.get(((AtlasRegion)region).name).pixmap.copy();
    }

    static void save(Pixmap pix, String path){
        String cipherName35 =  "DES";
		try{
			android.util.Log.d("cipherName-35", javax.crypto.Cipher.getInstance(cipherName35).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fi.get(path + ".png").writePng(pix);
    }

    static void drawCenter(Pixmap pix, Pixmap other){
        String cipherName36 =  "DES";
		try{
			android.util.Log.d("cipherName-36", javax.crypto.Cipher.getInstance(cipherName36).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pix.draw(other, pix.width/2 - other.width/2, pix.height/2 - other.height/2, true);
    }

    static void saveScaled(Pixmap pix, String name, int size){
        String cipherName37 =  "DES";
		try{
			android.util.Log.d("cipherName-37", javax.crypto.Cipher.getInstance(cipherName37).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Pixmap scaled = new Pixmap(size, size);
        //TODO bad linear scaling
        scaled.draw(pix, 0, 0, pix.width, pix.height, 0, 0, size, size, true, true);
        save(scaled, name);
    }

    static void drawScaledFit(Pixmap base, Pixmap image){
        String cipherName38 =  "DES";
		try{
			android.util.Log.d("cipherName-38", javax.crypto.Cipher.getInstance(cipherName38).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Vec2 size = Scaling.fit.apply(image.width, image.height, base.width, base.height);
        int wx = (int)size.x, wy = (int)size.y;
        //TODO bad linear scaling
        base.draw(image, 0, 0, image.width, image.height, base.width/2 - wx/2, base.height/2 - wy/2, wx, wy, true, true);
    }

    static void delete(String name){
        String cipherName39 =  "DES";
		try{
			android.util.Log.d("cipherName-39", javax.crypto.Cipher.getInstance(cipherName39).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		((GenRegion)Core.atlas.find(name)).path.delete();
    }

    static void replace(String name, Pixmap image){
        String cipherName40 =  "DES";
		try{
			android.util.Log.d("cipherName-40", javax.crypto.Cipher.getInstance(cipherName40).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fi.get(name + ".png").writePng(image);
        ((GenRegion)Core.atlas.find(name)).path.delete();
    }

    static void replace(TextureRegion region, Pixmap image){
        String cipherName41 =  "DES";
		try{
			android.util.Log.d("cipherName-41", javax.crypto.Cipher.getInstance(cipherName41).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		replace(((GenRegion)region).name, image);
    }

    static void err(String message, Object... args){
        String cipherName42 =  "DES";
		try{
			android.util.Log.d("cipherName-42", javax.crypto.Cipher.getInstance(cipherName42).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		throw new IllegalArgumentException(Strings.format(message, args));
    }

    static void validate(TextureRegion region){
        String cipherName43 =  "DES";
		try{
			android.util.Log.d("cipherName-43", javax.crypto.Cipher.getInstance(cipherName43).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(((GenRegion)region).invalid){
            String cipherName44 =  "DES";
			try{
				android.util.Log.d("cipherName-44", javax.crypto.Cipher.getInstance(cipherName44).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ImagePacker.err("Region does not exist: @", ((GenRegion)region).name);
        }
    }

    static class GenRegion extends AtlasRegion{
        boolean invalid;
        Fi path;

        GenRegion(String name, Fi path){
            String cipherName45 =  "DES";
			try{
				android.util.Log.d("cipherName-45", javax.crypto.Cipher.getInstance(cipherName45).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(name == null) throw new IllegalArgumentException("name is null");
            this.name = name;
            this.path = path;
        }

        @Override
        public boolean found(){
            String cipherName46 =  "DES";
			try{
				android.util.Log.d("cipherName-46", javax.crypto.Cipher.getInstance(cipherName46).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !invalid;
        }
    }

    static class PackIndex{
        @Nullable AtlasRegion region;
        @Nullable Pixmap pixmap;
        Fi file;

        public PackIndex(Fi file){
            String cipherName47 =  "DES";
			try{
				android.util.Log.d("cipherName-47", javax.crypto.Cipher.getInstance(cipherName47).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.file = file;
        }
    }
}
