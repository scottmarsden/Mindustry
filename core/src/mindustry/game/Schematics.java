package mindustry.game;

import arc.*;
import arc.assets.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.util.io.Streams.*;
import arc.util.pooling.*;
import arc.util.serialization.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.entities.units.*;
import mindustry.game.EventType.*;
import mindustry.game.Schematic.*;
import mindustry.gen.*;
import mindustry.input.*;
import mindustry.input.Placement.*;
import mindustry.io.*;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.legacy.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.sandbox.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.storage.CoreBlock.*;
import mindustry.world.meta.*;

import java.io.*;
import java.util.zip.*;

import static mindustry.Vars.*;

/** Handles schematics.*/
public class Schematics implements Loadable{
    private static final Schematic tmpSchem = new Schematic(new Seq<>(), new StringMap(), 0, 0);
    private static final Schematic tmpSchem2 = new Schematic(new Seq<>(), new StringMap(), 0, 0);

    private static final byte[] header = {'m', 's', 'c', 'h'};
    private static final byte version = 1;

    private static final int padding = 2;
    private static final int maxPreviewsMobile = 32;
    private static final int resolution = 32;

    private OptimizedByteArrayOutputStream out = new OptimizedByteArrayOutputStream(1024);
    private Seq<Schematic> all = new Seq<>();
    private OrderedMap<Schematic, FrameBuffer> previews = new OrderedMap<>();
    private ObjectSet<Schematic> errored = new ObjectSet<>();
    private ObjectMap<CoreBlock, Seq<Schematic>> loadouts = new ObjectMap<>();
    private ObjectMap<CoreBlock, Schematic> defaultLoadouts = new ObjectMap<>();
    private FrameBuffer shadowBuffer;
    private Texture errorTexture;
    private long lastClearTime;

    public Schematics(){

        String cipherName12270 =  "DES";
		try{
			android.util.Log.d("cipherName-12270", javax.crypto.Cipher.getInstance(cipherName12270).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Events.on(ClientLoadEvent.class, event -> {
            String cipherName12271 =  "DES";
			try{
				android.util.Log.d("cipherName-12271", javax.crypto.Cipher.getInstance(cipherName12271).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			errorTexture = new Texture("sprites/error.png");
        });
    }

    @Override
    public void loadSync(){
        String cipherName12272 =  "DES";
		try{
			android.util.Log.d("cipherName-12272", javax.crypto.Cipher.getInstance(cipherName12272).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		load();
    }

    /** Load all schematics in the folder immediately.*/
    public void load(){
        String cipherName12273 =  "DES";
		try{
			android.util.Log.d("cipherName-12273", javax.crypto.Cipher.getInstance(cipherName12273).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		all.clear();

        loadLoadouts();

        for(Fi file : schematicDirectory.list()){
            String cipherName12274 =  "DES";
			try{
				android.util.Log.d("cipherName-12274", javax.crypto.Cipher.getInstance(cipherName12274).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			loadFile(file);
        }

        platform.getWorkshopContent(Schematic.class).each(this::loadFile);

        //mod-specific schematics, cannot be removed
        mods.listFiles("schematics", (mod, file) -> {
            String cipherName12275 =  "DES";
			try{
				android.util.Log.d("cipherName-12275", javax.crypto.Cipher.getInstance(cipherName12275).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Schematic s = loadFile(file);
            if(s != null){
                String cipherName12276 =  "DES";
				try{
					android.util.Log.d("cipherName-12276", javax.crypto.Cipher.getInstance(cipherName12276).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				s.mod = mod;
            }
        });

        all.sort();

        if(shadowBuffer == null){
            String cipherName12277 =  "DES";
			try{
				android.util.Log.d("cipherName-12277", javax.crypto.Cipher.getInstance(cipherName12277).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.app.post(() -> shadowBuffer = new FrameBuffer(maxSchematicSize + padding + 8, maxSchematicSize + padding + 8));
        }
    }

    private void loadLoadouts(){
        String cipherName12278 =  "DES";
		try{
			android.util.Log.d("cipherName-12278", javax.crypto.Cipher.getInstance(cipherName12278).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq.with(Loadouts.basicShard, Loadouts.basicFoundation, Loadouts.basicNucleus, Loadouts.basicBastion).each(s -> checkLoadout(s, false));
    }

    public void overwrite(Schematic target, Schematic newSchematic){
        String cipherName12279 =  "DES";
		try{
			android.util.Log.d("cipherName-12279", javax.crypto.Cipher.getInstance(cipherName12279).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(previews.containsKey(target)){
            String cipherName12280 =  "DES";
			try{
				android.util.Log.d("cipherName-12280", javax.crypto.Cipher.getInstance(cipherName12280).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			previews.get(target).dispose();
            previews.remove(target);
        }

        target.tiles.clear();
        target.tiles.addAll(newSchematic.tiles);
        target.width = newSchematic.width;
        target.height = newSchematic.height;
        newSchematic.labels = target.labels;
        newSchematic.tags.putAll(target.tags);
        newSchematic.file = target.file;

        loadouts.each((block, list) -> list.remove(target));
        checkLoadout(target, true);

        try{
            String cipherName12281 =  "DES";
			try{
				android.util.Log.d("cipherName-12281", javax.crypto.Cipher.getInstance(cipherName12281).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write(newSchematic, target.file);
        }catch(Exception e){
            String cipherName12282 =  "DES";
			try{
				android.util.Log.d("cipherName-12282", javax.crypto.Cipher.getInstance(cipherName12282).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err("Failed to overwrite schematic '@' (@)", newSchematic.name(), target.file);
            Log.err(e);
            ui.showException(e);
        }
    }

    private @Nullable Schematic loadFile(Fi file){
        String cipherName12283 =  "DES";
		try{
			android.util.Log.d("cipherName-12283", javax.crypto.Cipher.getInstance(cipherName12283).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!file.extension().equals(schematicExtension)) return null;

        try{
            String cipherName12284 =  "DES";
			try{
				android.util.Log.d("cipherName-12284", javax.crypto.Cipher.getInstance(cipherName12284).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Schematic s = read(file);
            all.add(s);
            checkLoadout(s, true);

            //external file from workshop
            if(!s.file.parent().equals(schematicDirectory)){
                String cipherName12285 =  "DES";
				try{
					android.util.Log.d("cipherName-12285", javax.crypto.Cipher.getInstance(cipherName12285).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				s.tags.put("steamid", s.file.parent().name());
            }

            return s;
        }catch(Throwable e){
            String cipherName12286 =  "DES";
			try{
				android.util.Log.d("cipherName-12286", javax.crypto.Cipher.getInstance(cipherName12286).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err("Failed to read schematic from file '@'", file);
            Log.err(e);
        }
        return null;
    }

    public Seq<Schematic> all(){
        String cipherName12287 =  "DES";
		try{
			android.util.Log.d("cipherName-12287", javax.crypto.Cipher.getInstance(cipherName12287).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return all;
    }

    public void saveChanges(Schematic s){
        String cipherName12288 =  "DES";
		try{
			android.util.Log.d("cipherName-12288", javax.crypto.Cipher.getInstance(cipherName12288).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(s.file != null){
            String cipherName12289 =  "DES";
			try{
				android.util.Log.d("cipherName-12289", javax.crypto.Cipher.getInstance(cipherName12289).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName12290 =  "DES";
				try{
					android.util.Log.d("cipherName-12290", javax.crypto.Cipher.getInstance(cipherName12290).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write(s, s.file);
            }catch(Exception e){
                String cipherName12291 =  "DES";
				try{
					android.util.Log.d("cipherName-12291", javax.crypto.Cipher.getInstance(cipherName12291).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showException(e);
            }
        }
        all.sort();
    }

    public void savePreview(Schematic schematic, Fi file){
        String cipherName12292 =  "DES";
		try{
			android.util.Log.d("cipherName-12292", javax.crypto.Cipher.getInstance(cipherName12292).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		FrameBuffer buffer = getBuffer(schematic);
        Draw.flush();
        buffer.begin();
        Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, buffer.getWidth(), buffer.getHeight());
        file.writePng(pixmap);
        buffer.end();
    }

    public Texture getPreview(Schematic schematic){
        String cipherName12293 =  "DES";
		try{
			android.util.Log.d("cipherName-12293", javax.crypto.Cipher.getInstance(cipherName12293).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(errored.contains(schematic)) return errorTexture;

        try{
            String cipherName12294 =  "DES";
			try{
				android.util.Log.d("cipherName-12294", javax.crypto.Cipher.getInstance(cipherName12294).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return getBuffer(schematic).getTexture();
        }catch(Throwable t){
            String cipherName12295 =  "DES";
			try{
				android.util.Log.d("cipherName-12295", javax.crypto.Cipher.getInstance(cipherName12295).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err("Failed to get preview for schematic '@' (@)", schematic.name(), schematic.file);
            Log.err(t);
            errored.add(schematic);
            return errorTexture;
        }
    }

    public boolean hasPreview(Schematic schematic){
        String cipherName12296 =  "DES";
		try{
			android.util.Log.d("cipherName-12296", javax.crypto.Cipher.getInstance(cipherName12296).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return previews.containsKey(schematic);
    }

    public FrameBuffer getBuffer(Schematic schematic){
        String cipherName12297 =  "DES";
		try{
			android.util.Log.d("cipherName-12297", javax.crypto.Cipher.getInstance(cipherName12297).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//dispose unneeded previews to prevent memory outage errors.
        //only runs every 2 seconds
        if(mobile && Time.timeSinceMillis(lastClearTime) > 1000 * 2 && previews.size > maxPreviewsMobile){
            String cipherName12298 =  "DES";
			try{
				android.util.Log.d("cipherName-12298", javax.crypto.Cipher.getInstance(cipherName12298).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Seq<Schematic> keys = previews.orderedKeys().copy();
            for(int i = 0; i < previews.size - maxPreviewsMobile; i++){
                String cipherName12299 =  "DES";
				try{
					android.util.Log.d("cipherName-12299", javax.crypto.Cipher.getInstance(cipherName12299).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//dispose and remove unneeded previews
                previews.get(keys.get(i)).dispose();
                previews.remove(keys.get(i));
            }
            //update last clear time
            lastClearTime = Time.millis();
        }

        if(!previews.containsKey(schematic)){
            String cipherName12300 =  "DES";
			try{
				android.util.Log.d("cipherName-12300", javax.crypto.Cipher.getInstance(cipherName12300).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.blend();
            Draw.reset();
            Tmp.m1.set(Draw.proj());
            Tmp.m2.set(Draw.trans());
            FrameBuffer buffer = new FrameBuffer((schematic.width + padding) * resolution, (schematic.height + padding) * resolution);

            shadowBuffer.begin(Color.clear);

            Draw.trans().idt();
            Draw.proj().setOrtho(0, 0, shadowBuffer.getWidth(), shadowBuffer.getHeight());

            Draw.color();
            schematic.tiles.each(t -> {
                String cipherName12301 =  "DES";
				try{
					android.util.Log.d("cipherName-12301", javax.crypto.Cipher.getInstance(cipherName12301).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int size = t.block.size;
                int offsetx = -(size - 1) / 2;
                int offsety = -(size - 1) / 2;
                for(int dx = 0; dx < size; dx++){
                    String cipherName12302 =  "DES";
					try{
						android.util.Log.d("cipherName-12302", javax.crypto.Cipher.getInstance(cipherName12302).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int dy = 0; dy < size; dy++){
                        String cipherName12303 =  "DES";
						try{
							android.util.Log.d("cipherName-12303", javax.crypto.Cipher.getInstance(cipherName12303).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int wx = t.x + dx + offsetx;
                        int wy = t.y + dy + offsety;
                        Fill.square(padding/2f + wx + 0.5f, padding/2f + wy + 0.5f, 0.5f);
                    }
                }
            });

            shadowBuffer.end();

            buffer.begin(Color.clear);

            Draw.proj().setOrtho(0, buffer.getHeight(), buffer.getWidth(), -buffer.getHeight());

            Tmp.tr1.set(shadowBuffer.getTexture(), 0, 0, schematic.width + padding, schematic.height + padding);
            Draw.color(0f, 0f, 0f, 1f);
            Draw.rect(Tmp.tr1, buffer.getWidth()/2f, buffer.getHeight()/2f, buffer.getWidth(), -buffer.getHeight());
            Draw.color();

            Seq<BuildPlan> plans = schematic.tiles.map(t -> new BuildPlan(t.x, t.y, t.rotation, t.block, t.config));

            Draw.flush();
            //scale each plan to fit schematic
            Draw.trans().scale(resolution / tilesize, resolution / tilesize).translate(tilesize*1.5f, tilesize*1.5f);

            //draw plans
            plans.each(req -> {
                String cipherName12304 =  "DES";
				try{
					android.util.Log.d("cipherName-12304", javax.crypto.Cipher.getInstance(cipherName12304).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				req.animScale = 1f;
                req.worldContext = false;
                req.block.drawPlanRegion(req, plans);
            });

            plans.each(req -> req.block.drawPlanConfigTop(req, plans));

            Draw.flush();
            Draw.trans().idt();

            buffer.end();

            Draw.proj(Tmp.m1);
            Draw.trans(Tmp.m2);

            previews.put(schematic, buffer);
        }

        return previews.get(schematic);
    }

    /** Creates an array of build plans from a schematic's data, centered on the provided x+y coordinates. */
    public Seq<BuildPlan> toPlans(Schematic schem, int x, int y){
        String cipherName12305 =  "DES";
		try{
			android.util.Log.d("cipherName-12305", javax.crypto.Cipher.getInstance(cipherName12305).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return schem.tiles.map(t -> new BuildPlan(t.x + x - schem.width/2, t.y + y - schem.height/2, t.rotation, t.block, t.config).original(t.x, t.y, schem.width, schem.height))
            .removeAll(s -> (!s.block.isVisible() && !(s.block instanceof CoreBlock)) || !s.block.unlockedNow()).sort(Structs.comparingInt(s -> -s.block.schematicPriority));
    }

    /** @return all the valid loadouts for a specific core type. */
    public Seq<Schematic> getLoadouts(CoreBlock block){
        String cipherName12306 =  "DES";
		try{
			android.util.Log.d("cipherName-12306", javax.crypto.Cipher.getInstance(cipherName12306).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return loadouts.get(block, Seq::new);
    }

    public ObjectMap<CoreBlock, Seq<Schematic>> getLoadouts(){
        String cipherName12307 =  "DES";
		try{
			android.util.Log.d("cipherName-12307", javax.crypto.Cipher.getInstance(cipherName12307).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return loadouts;
    }

    public @Nullable Schematic getDefaultLoadout(CoreBlock block){
        String cipherName12308 =  "DES";
		try{
			android.util.Log.d("cipherName-12308", javax.crypto.Cipher.getInstance(cipherName12308).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return defaultLoadouts.get(block);
    }

    public boolean isDefaultLoadout(Schematic schem){
        String cipherName12309 =  "DES";
		try{
			android.util.Log.d("cipherName-12309", javax.crypto.Cipher.getInstance(cipherName12309).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return defaultLoadouts.containsValue(schem, true);
    }

    /** Checks a schematic for deployment validity and adds it to the cache. */
    private void checkLoadout(Schematic s, boolean customSchem){
        String cipherName12310 =  "DES";
		try{
			android.util.Log.d("cipherName-12310", javax.crypto.Cipher.getInstance(cipherName12310).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Stile core = s.tiles.find(t -> t.block instanceof CoreBlock);
        if(core == null) return;
        int cores = s.tiles.count(t -> t.block instanceof CoreBlock);
        int maxSize = getMaxLaunchSize(core.block);

        //make sure a core exists, and that the schematic is small enough.
        if((customSchem && (s.width > maxSize || s.height > maxSize
            || s.tiles.contains(t -> t.block.buildVisibility == BuildVisibility.sandboxOnly || !t.block.unlocked()) || cores > 1))) return;

        //place in the cache
        loadouts.get((CoreBlock)core.block, Seq::new).add(s);

        //save non-custom loadout
        if(!customSchem){
            String cipherName12311 =  "DES";
			try{
				android.util.Log.d("cipherName-12311", javax.crypto.Cipher.getInstance(cipherName12311).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			defaultLoadouts.put((CoreBlock)core.block, s);
        }
    }

    public int getMaxLaunchSize(Block block){
        String cipherName12312 =  "DES";
		try{
			android.util.Log.d("cipherName-12312", javax.crypto.Cipher.getInstance(cipherName12312).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return block.size + maxLoadoutSchematicPad*2;
    }

    /** Adds a schematic to the list, also copying it into the files.*/
    public void add(Schematic schematic){
        String cipherName12313 =  "DES";
		try{
			android.util.Log.d("cipherName-12313", javax.crypto.Cipher.getInstance(cipherName12313).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		all.add(schematic);
        try{
            String cipherName12314 =  "DES";
			try{
				android.util.Log.d("cipherName-12314", javax.crypto.Cipher.getInstance(cipherName12314).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fi file = schematicDirectory.child(Time.millis() + "." + schematicExtension);
            write(schematic, file);
            schematic.file = file;
        }catch(Exception e){
            String cipherName12315 =  "DES";
			try{
				android.util.Log.d("cipherName-12315", javax.crypto.Cipher.getInstance(cipherName12315).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.showException(e);
            Log.err(e);
        }

        checkLoadout(schematic, true);
        all.sort();
    }

    public void remove(Schematic s){
        String cipherName12316 =  "DES";
		try{
			android.util.Log.d("cipherName-12316", javax.crypto.Cipher.getInstance(cipherName12316).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		all.remove(s);
        loadouts.each((block, seq) -> seq.remove(s));
        if(s.file != null){
            String cipherName12317 =  "DES";
			try{
				android.util.Log.d("cipherName-12317", javax.crypto.Cipher.getInstance(cipherName12317).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			s.file.delete();
        }

        if(previews.containsKey(s)){
            String cipherName12318 =  "DES";
			try{
				android.util.Log.d("cipherName-12318", javax.crypto.Cipher.getInstance(cipherName12318).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			previews.get(s).dispose();
            previews.remove(s);
        }
        all.sort();
    }

    /** Creates a schematic from a world selection. */
    public Schematic create(int x, int y, int x2, int y2){
		String cipherName12319 =  "DES";
		try{
			android.util.Log.d("cipherName-12319", javax.crypto.Cipher.getInstance(cipherName12319).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Team team = headless ? null : Vars.player.team();
        NormalizeResult result = Placement.normalizeArea(x, y, x2, y2, 0, false, maxSchematicSize);
        x = result.x;
        y = result.y;
        x2 = result.x2;
        y2 = result.y2;

        int ox = x, oy = y, ox2 = x2, oy2 = y2;

        Seq<Stile> tiles = new Seq<>();

        int minx = x2, miny = y2, maxx = x, maxy = y;
        boolean found = false;
        for(int cx = x; cx <= x2; cx++){
            for(int cy = y; cy <= y2; cy++){
                Building linked = world.build(cx, cy);
                if(linked != null && (!linked.isDiscovered(team) || !linked.wasVisible)) continue;

                Block realBlock = linked == null ? null : linked instanceof ConstructBuild cons ? cons.current : linked.block;

                if(linked != null && realBlock != null && (realBlock.isVisible() || realBlock instanceof CoreBlock)){
                    int top = realBlock.size/2;
                    int bot = realBlock.size % 2 == 1 ? -realBlock.size/2 : -(realBlock.size - 1)/2;
                    minx = Math.min(linked.tileX() + bot, minx);
                    miny = Math.min(linked.tileY() + bot, miny);
                    maxx = Math.max(linked.tileX() + top, maxx);
                    maxy = Math.max(linked.tileY() + top, maxy);
                    found = true;
                }
            }
        }

        if(found){
            x = minx;
            y = miny;
            x2 = maxx;
            y2 = maxy;
        }else{
            return new Schematic(new Seq<>(), new StringMap(), 1, 1);
        }

        int width = x2 - x + 1, height = y2 - y + 1;
        int offsetX = -x, offsetY = -y;
        IntSet counted = new IntSet();
        for(int cx = ox; cx <= ox2; cx++){
            for(int cy = oy; cy <= oy2; cy++){
                Building tile = world.build(cx, cy);
                if(tile != null && (!tile.isDiscovered(team) || !tile.wasVisible)) continue;
                Block realBlock = tile == null ? null : tile instanceof ConstructBuild cons ? cons.current : tile.block;

                if(tile != null && !counted.contains(tile.pos()) && realBlock != null
                    && (realBlock.isVisible() || realBlock instanceof CoreBlock)){
                    Object config = tile instanceof ConstructBuild cons ? cons.lastConfig : tile.config();

                    tiles.add(new Stile(realBlock, tile.tileX() + offsetX, tile.tileY() + offsetY, config, (byte)tile.rotation));
                    counted.add(tile.pos());
                }
            }
        }

        return new Schematic(tiles, new StringMap(), width, height);
    }

    /** Converts a schematic to base64. Note that the result of this will always start with 'bXNjaAB'.*/
    public String writeBase64(Schematic schematic){
        String cipherName12320 =  "DES";
		try{
			android.util.Log.d("cipherName-12320", javax.crypto.Cipher.getInstance(cipherName12320).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName12321 =  "DES";
			try{
				android.util.Log.d("cipherName-12321", javax.crypto.Cipher.getInstance(cipherName12321).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out.reset();
            write(schematic, out);
            return new String(Base64Coder.encode(out.getBuffer(), out.size()));
        }catch(IOException e){
            String cipherName12322 =  "DES";
			try{
				android.util.Log.d("cipherName-12322", javax.crypto.Cipher.getInstance(cipherName12322).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(e);
        }
    }

    /** Places the last launch loadout at the coordinates and fills it with the launch resources. */
    public static void placeLaunchLoadout(int x, int y){
        String cipherName12323 =  "DES";
		try{
			android.util.Log.d("cipherName-12323", javax.crypto.Cipher.getInstance(cipherName12323).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		placeLoadout(universe.getLastLoadout(), x, y, state.rules.defaultTeam);
        if(world.tile(x, y).build == null) throw new RuntimeException("No core at loadout coordinates!");
        world.tile(x, y).build.items.add(universe.getLaunchResources());
    }

    public static void placeLoadout(Schematic schem, int x, int y){
        String cipherName12324 =  "DES";
		try{
			android.util.Log.d("cipherName-12324", javax.crypto.Cipher.getInstance(cipherName12324).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		placeLoadout(schem, x, y, state.rules.defaultTeam);
    }

    public static void placeLoadout(Schematic schem, int x, int y, Team team){
        String cipherName12325 =  "DES";
		try{
			android.util.Log.d("cipherName-12325", javax.crypto.Cipher.getInstance(cipherName12325).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		placeLoadout(schem, x, y, team, true);
    }

    public static void placeLoadout(Schematic schem, int x, int y, Team team, boolean check){
		String cipherName12326 =  "DES";
		try{
			android.util.Log.d("cipherName-12326", javax.crypto.Cipher.getInstance(cipherName12326).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Stile coreTile = schem.tiles.find(s -> s.block instanceof CoreBlock);
        Seq<Tile> seq = new Seq<>();
        if(coreTile == null) throw new IllegalArgumentException("Loadout schematic has no core tile!");
        int ox = x - coreTile.x, oy = y - coreTile.y;
        schem.tiles.copy().sort(s -> -s.block.schematicPriority).each(st -> {
            Tile tile = world.tile(st.x + ox, st.y + oy);
            if(tile == null) return;

            //check for blocks that are in the way.
            if(check && !(st.block instanceof CoreBlock)){
                seq.clear();
                tile.getLinkedTilesAs(st.block, seq);
                //remove env blocks, or not?
                //if(seq.contains(t -> !t.block().alwaysReplace && !t.synthetic())){
                //    return;
                //}
                for(var t : seq){
                    if(t.block() != Blocks.air){
                        t.remove();
                    }
                }
            }

            tile.setBlock(st.block, team, st.rotation);

            Object config = st.config;
            if(tile.build != null){
                tile.build.configureAny(config);
            }

            if(tile.build instanceof CoreBuild cb){
                state.teams.registerCore(cb);
            }
        });
    }

    public static void place(Schematic schem, int x, int y, Team team){
        String cipherName12327 =  "DES";
		try{
			android.util.Log.d("cipherName-12327", javax.crypto.Cipher.getInstance(cipherName12327).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int ox = x - schem.width/2, oy = y - schem.height/2;
        schem.tiles.each(st -> {
            String cipherName12328 =  "DES";
			try{
				android.util.Log.d("cipherName-12328", javax.crypto.Cipher.getInstance(cipherName12328).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = world.tile(st.x + ox, st.y + oy);
            if(tile == null) return;

            tile.setBlock(st.block, team, st.rotation);

            Object config = st.config;
            if(tile.build != null){
                String cipherName12329 =  "DES";
				try{
					android.util.Log.d("cipherName-12329", javax.crypto.Cipher.getInstance(cipherName12329).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.build.configureAny(config);
            }
        });
    }

    //region IO methods

    /** Loads a schematic from base64. May throw an exception. */
    public static Schematic readBase64(String schematic){
        String cipherName12330 =  "DES";
		try{
			android.util.Log.d("cipherName-12330", javax.crypto.Cipher.getInstance(cipherName12330).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName12331 =  "DES";
			try{
				android.util.Log.d("cipherName-12331", javax.crypto.Cipher.getInstance(cipherName12331).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return read(new ByteArrayInputStream(Base64Coder.decode(schematic.trim())));
        }catch(IOException e){
            String cipherName12332 =  "DES";
			try{
				android.util.Log.d("cipherName-12332", javax.crypto.Cipher.getInstance(cipherName12332).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(e);
        }
    }

    public static Schematic read(Fi file) throws IOException{
        String cipherName12333 =  "DES";
		try{
			android.util.Log.d("cipherName-12333", javax.crypto.Cipher.getInstance(cipherName12333).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Schematic s = read(new DataInputStream(file.read(1024)));
        if(!s.tags.containsKey("name")){
            String cipherName12334 =  "DES";
			try{
				android.util.Log.d("cipherName-12334", javax.crypto.Cipher.getInstance(cipherName12334).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			s.tags.put("name", file.nameWithoutExtension());
        }
        s.file = file;
        return s;
    }

    public static Schematic read(InputStream input) throws IOException{
        String cipherName12335 =  "DES";
		try{
			android.util.Log.d("cipherName-12335", javax.crypto.Cipher.getInstance(cipherName12335).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(byte b : header){
            String cipherName12336 =  "DES";
			try{
				android.util.Log.d("cipherName-12336", javax.crypto.Cipher.getInstance(cipherName12336).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(input.read() != b){
                String cipherName12337 =  "DES";
				try{
					android.util.Log.d("cipherName-12337", javax.crypto.Cipher.getInstance(cipherName12337).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IOException("Not a schematic file (missing header).");
            }
        }

        int ver = input.read();

        try(DataInputStream stream = new DataInputStream(new InflaterInputStream(input))){
            String cipherName12338 =  "DES";
			try{
				android.util.Log.d("cipherName-12338", javax.crypto.Cipher.getInstance(cipherName12338).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			short width = stream.readShort(), height = stream.readShort();

            if(width > 128 || height > 128) throw new IOException("Invalid schematic: Too large (max possible size is 128x128)");

            StringMap map = new StringMap();
            int tags = stream.readUnsignedByte();
            for(int i = 0; i < tags; i++){
                String cipherName12339 =  "DES";
				try{
					android.util.Log.d("cipherName-12339", javax.crypto.Cipher.getInstance(cipherName12339).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				map.put(stream.readUTF(), stream.readUTF());
            }

            String[] labels = null;

            //try to read the categories, but skip if it fails
            try{
                String cipherName12340 =  "DES";
				try{
					android.util.Log.d("cipherName-12340", javax.crypto.Cipher.getInstance(cipherName12340).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				labels = JsonIO.read(String[].class, map.get("labels", "[]"));
            }catch(Exception ignored){
				String cipherName12341 =  "DES";
				try{
					android.util.Log.d("cipherName-12341", javax.crypto.Cipher.getInstance(cipherName12341).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }

            IntMap<Block> blocks = new IntMap<>();
            byte length = stream.readByte();
            for(int i = 0; i < length; i++){
                String cipherName12342 =  "DES";
				try{
					android.util.Log.d("cipherName-12342", javax.crypto.Cipher.getInstance(cipherName12342).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String name = stream.readUTF();
                Block block = Vars.content.getByName(ContentType.block, SaveFileReader.fallback.get(name, name));
                blocks.put(i, block == null || block instanceof LegacyBlock ? Blocks.air : block);
            }

            int total = stream.readInt();

            if(total > 128 * 128) throw new IOException("Invalid schematic: Too many blocks.");

            Seq<Stile> tiles = new Seq<>(total);
            for(int i = 0; i < total; i++){
                String cipherName12343 =  "DES";
				try{
					android.util.Log.d("cipherName-12343", javax.crypto.Cipher.getInstance(cipherName12343).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Block block = blocks.get(stream.readByte());
                int position = stream.readInt();
                Object config = ver == 0 ? mapConfig(block, stream.readInt(), position) : TypeIO.readObject(Reads.get(stream));
                byte rotation = stream.readByte();
                if(block != Blocks.air){
                    String cipherName12344 =  "DES";
					try{
						android.util.Log.d("cipherName-12344", javax.crypto.Cipher.getInstance(cipherName12344).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tiles.add(new Stile(block, Point2.x(position), Point2.y(position), config, rotation));
                }
            }

            Schematic out = new Schematic(tiles, map, width, height);
            if(labels != null) out.labels.addAll(labels);
            return out;
        }
    }

    public static void write(Schematic schematic, Fi file) throws IOException{
        String cipherName12345 =  "DES";
		try{
			android.util.Log.d("cipherName-12345", javax.crypto.Cipher.getInstance(cipherName12345).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write(schematic, file.write(false, 1024));
    }

    public static void write(Schematic schematic, OutputStream output) throws IOException{
        String cipherName12346 =  "DES";
		try{
			android.util.Log.d("cipherName-12346", javax.crypto.Cipher.getInstance(cipherName12346).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		output.write(header);
        output.write(version);

        try(DataOutputStream stream = new DataOutputStream(new DeflaterOutputStream(output))){

            String cipherName12347 =  "DES";
			try{
				android.util.Log.d("cipherName-12347", javax.crypto.Cipher.getInstance(cipherName12347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stream.writeShort(schematic.width);
            stream.writeShort(schematic.height);

            schematic.tags.put("labels", JsonIO.write(schematic.labels.toArray(String.class)));

            stream.writeByte(schematic.tags.size);
            for(var e : schematic.tags.entries()){
                String cipherName12348 =  "DES";
				try{
					android.util.Log.d("cipherName-12348", javax.crypto.Cipher.getInstance(cipherName12348).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stream.writeUTF(e.key);
                stream.writeUTF(e.value);
            }

            OrderedSet<Block> blocks = new OrderedSet<>();
            schematic.tiles.each(t -> blocks.add(t.block));

            //create dictionary
            stream.writeByte(blocks.size);
            for(int i = 0; i < blocks.size; i++){
                String cipherName12349 =  "DES";
				try{
					android.util.Log.d("cipherName-12349", javax.crypto.Cipher.getInstance(cipherName12349).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stream.writeUTF(blocks.orderedItems().get(i).name);
            }

            stream.writeInt(schematic.tiles.size);
            //write each tile
            for(Stile tile : schematic.tiles){
                String cipherName12350 =  "DES";
				try{
					android.util.Log.d("cipherName-12350", javax.crypto.Cipher.getInstance(cipherName12350).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stream.writeByte(blocks.orderedItems().indexOf(tile.block));
                stream.writeInt(Point2.pack(tile.x, tile.y));
                TypeIO.writeObject(Writes.get(stream), tile.config);
                stream.writeByte(tile.rotation);
            }
        }
    }

    /** Maps legacy int configs to new config objects. */
    private static Object mapConfig(Block block, int value, int position){
        String cipherName12351 =  "DES";
		try{
			android.util.Log.d("cipherName-12351", javax.crypto.Cipher.getInstance(cipherName12351).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(block instanceof Sorter || block instanceof Unloader || block instanceof ItemSource) return content.item(value);
        if(block instanceof LiquidSource) return content.liquid(value);
        if(block instanceof MassDriver || block instanceof ItemBridge) return Point2.unpack(value).sub(Point2.x(position), Point2.y(position));
        if(block instanceof LightBlock) return value;

        return null;
    }

    //endregion
    //region misc utility

    /** @return a temporary schematic representing the input rotated 90 degrees counterclockwise N times. */
    public static Schematic rotate(Schematic input, int times){
        String cipherName12352 =  "DES";
		try{
			android.util.Log.d("cipherName-12352", javax.crypto.Cipher.getInstance(cipherName12352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(times == 0) return input;

        boolean sign = times > 0;
        for(int i = 0; i < Math.abs(times); i++){
            String cipherName12353 =  "DES";
			try{
				android.util.Log.d("cipherName-12353", javax.crypto.Cipher.getInstance(cipherName12353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			input = rotated(input, sign);
        }
        return input;
    }

    private static Schematic rotated(Schematic input, boolean counter){
        String cipherName12354 =  "DES";
		try{
			android.util.Log.d("cipherName-12354", javax.crypto.Cipher.getInstance(cipherName12354).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int direction = Mathf.sign(counter);
        Schematic schem = input == tmpSchem ? tmpSchem2 : tmpSchem2;
        schem.width = input.width;
        schem.height = input.height;
        Pools.freeAll(schem.tiles);
        schem.tiles.clear();
        for(Stile tile : input.tiles){
            String cipherName12355 =  "DES";
			try{
				android.util.Log.d("cipherName-12355", javax.crypto.Cipher.getInstance(cipherName12355).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			schem.tiles.add(Pools.obtain(Stile.class, Stile::new).set(tile));
        }

        int ox = schem.width/2, oy = schem.height/2;

        schem.tiles.each(req -> {
            String cipherName12356 =  "DES";
			try{
				android.util.Log.d("cipherName-12356", javax.crypto.Cipher.getInstance(cipherName12356).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			req.config = BuildPlan.pointConfig(req.block, req.config, p -> {
                String cipherName12357 =  "DES";
				try{
					android.util.Log.d("cipherName-12357", javax.crypto.Cipher.getInstance(cipherName12357).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int cx = p.x, cy = p.y;
                int lx = cx;

                if(direction >= 0){
                    String cipherName12358 =  "DES";
					try{
						android.util.Log.d("cipherName-12358", javax.crypto.Cipher.getInstance(cipherName12358).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cx = -cy;
                    cy = lx;
                }else{
                    String cipherName12359 =  "DES";
					try{
						android.util.Log.d("cipherName-12359", javax.crypto.Cipher.getInstance(cipherName12359).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cx = cy;
                    cy = -lx;
                }
                p.set(cx, cy);
            });

            //rotate actual plan, centered on its multiblock position
            float wx = (req.x - ox) * tilesize + req.block.offset, wy = (req.y - oy) * tilesize + req.block.offset;
            float x = wx;
            if(direction >= 0){
                String cipherName12360 =  "DES";
				try{
					android.util.Log.d("cipherName-12360", javax.crypto.Cipher.getInstance(cipherName12360).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wx = -wy;
                wy = x;
            }else{
                String cipherName12361 =  "DES";
				try{
					android.util.Log.d("cipherName-12361", javax.crypto.Cipher.getInstance(cipherName12361).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wx = wy;
                wy = -x;
            }
            req.x = (short)(World.toTile(wx - req.block.offset) + ox);
            req.y = (short)(World.toTile(wy - req.block.offset) + oy);
            req.rotation = (byte)Mathf.mod(req.rotation + direction, 4);
        });

        //assign flipped values, since it's rotated
        schem.width = input.height;
        schem.height = input.width;

        return schem;
    }

    //endregion
}
