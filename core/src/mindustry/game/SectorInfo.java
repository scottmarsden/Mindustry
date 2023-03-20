package mindustry.game;

import arc.func.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.maps.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock.*;
import mindustry.world.meta.*;
import mindustry.world.modules.*;

import java.util.*;

import static mindustry.Vars.*;

public class SectorInfo{
    /** average window size in samples */
    private static final int valueWindow = 60;
    /** refresh period of export in ticks */
    private static final float refreshPeriod = 60;
    private static float returnf;

    /** Core input statistics. */
    public ObjectMap<Item, ExportStat> production = new ObjectMap<>();
    /** Raw item production statistics. */
    public ObjectMap<Item, ExportStat> rawProduction = new ObjectMap<>();
    /** Export statistics. */
    public ObjectMap<Item, ExportStat> export = new ObjectMap<>();
    /** Items stored in all cores. */
    public ItemSeq items = new ItemSeq();
    /** The best available core type. */
    public Block bestCoreType = Blocks.coreShard;
    /** Max storage capacity. */
    public int storageCapacity = 0;
    /** Whether a core is available here. */
    public boolean hasCore = true;
    /** Whether this sector was ever fully captured. */
    public boolean wasCaptured = false;
    /** Sector that was launched from. */
    public @Nullable Sector origin;
    /** Launch destination. */
    public @Nullable Sector destination;
    /** Resources known to occur at this sector. */
    public Seq<UnlockableContent> resources = new Seq<>();
    /** Whether waves are enabled here. */
    public boolean waves = true;
    /** Whether attack mode is enabled here. */
    public boolean attack = false;
    /** Whether this sector has any enemy spawns. */
    public boolean hasSpawns = true;
    /** Wave # from state */
    public int wave = 1, winWave = -1;
    /** Waves this sector can survive if under attack. Based on wave in info. <0 means uncalculated. */
    public int wavesSurvived = -1;
    /** Time between waves. */
    public float waveSpacing = 2 * Time.toMinutes;
    /** Damage dealt to sector. */
    public float damage;
    /** How many waves have passed while the player was away. */
    public int wavesPassed;
    /** Packed core spawn position. */
    public int spawnPosition;
    /** How long the player has been playing elsewhere. */
    public float secondsPassed;
    /** How many minutes this sector has been captured. */
    public float minutesCaptured;
    /** Display name. */
    public @Nullable String name;
    /** Displayed icon. */
    public @Nullable String icon;
    /** Displayed icon, as content. */
    public @Nullable UnlockableContent contentIcon;
    /** Version of generated waves. When it doesn't match, new waves are generated. */
    public int waveVersion = -1;
    /** Whether this sector was indicated to the player or not. */
    public boolean shown = false;
    /** Temporary seq for last imported items. Do not use. */
    public transient ItemSeq lastImported = new ItemSeq();

    /** Special variables for simulation. */
    public float sumHealth, sumRps, sumDps, waveHealthBase, waveHealthSlope, waveDpsBase, waveDpsSlope, bossHealth, bossDps, curEnemyHealth, curEnemyDps;
    /** Wave where first boss shows up. */
    public int bossWave = -1;

    /** Counter refresh state. */
    private transient Interval time = new Interval();
    /** Core item storage input/output deltas. */
    private @Nullable transient int[] coreDeltas;
    /** Core item storage input/output deltas. */
    private @Nullable transient int[] productionDeltas;

    /** Handles core item changes. */
    public void handleCoreItem(Item item, int amount){
        String cipherName12510 =  "DES";
		try{
			android.util.Log.d("cipherName-12510", javax.crypto.Cipher.getInstance(cipherName12510).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(coreDeltas == null) coreDeltas = new int[content.items().size];
        coreDeltas[item.id] += amount;
    }

    /** Handles raw production stats. */
    public void handleProduction(Item item, int amount){
        String cipherName12511 =  "DES";
		try{
			android.util.Log.d("cipherName-12511", javax.crypto.Cipher.getInstance(cipherName12511).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(productionDeltas == null) productionDeltas = new int[content.items().size];
        productionDeltas[item.id] += amount;
    }

    /** @return the real location items go when launched on this sector */
    public Sector getRealDestination(){
        String cipherName12512 =  "DES";
		try{
			android.util.Log.d("cipherName-12512", javax.crypto.Cipher.getInstance(cipherName12512).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//on multiplayer the destination is, by default, the first captured sector (basically random)
        return !net.client() || destination != null ? destination : state.rules.sector.planet.sectors.find(Sector::hasBase);
    }

    /** Updates export statistics. */
    public void handleItemExport(ItemStack stack){
        String cipherName12513 =  "DES";
		try{
			android.util.Log.d("cipherName-12513", javax.crypto.Cipher.getInstance(cipherName12513).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		handleItemExport(stack.item, stack.amount);
    }

    /** Updates export statistics. */
    public void handleItemExport(Item item, int amount){
        String cipherName12514 =  "DES";
		try{
			android.util.Log.d("cipherName-12514", javax.crypto.Cipher.getInstance(cipherName12514).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		export.get(item, ExportStat::new).counter += amount;
    }

    public float getExport(Item item){
        String cipherName12515 =  "DES";
		try{
			android.util.Log.d("cipherName-12515", javax.crypto.Cipher.getInstance(cipherName12515).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return export.get(item, ExportStat::new).mean;
    }

    /** Write contents of meta into main storage. */
    public void write(){
        String cipherName12516 =  "DES";
		try{
			android.util.Log.d("cipherName-12516", javax.crypto.Cipher.getInstance(cipherName12516).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//enable attack mode when there's a core.
        if(state.rules.waveTeam.core() != null){
            String cipherName12517 =  "DES";
			try{
				android.util.Log.d("cipherName-12517", javax.crypto.Cipher.getInstance(cipherName12517).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			attack = true;
            if(!state.rules.sector.planet.allowWaves){
                String cipherName12518 =  "DES";
				try{
					android.util.Log.d("cipherName-12518", javax.crypto.Cipher.getInstance(cipherName12518).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				winWave = 0;
            }
        }

        //if there are infinite waves and no win wave, add a win wave.
        if(winWave <= 0 && !attack && state.rules.sector.planet.allowWaves){
            String cipherName12519 =  "DES";
			try{
				android.util.Log.d("cipherName-12519", javax.crypto.Cipher.getInstance(cipherName12519).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			winWave = 30;
        }

        if(state.rules.sector != null && state.rules.sector.preset != null && state.rules.sector.preset.captureWave > 0 && !state.rules.sector.planet.allowWaves){
            String cipherName12520 =  "DES";
			try{
				android.util.Log.d("cipherName-12520", javax.crypto.Cipher.getInstance(cipherName12520).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			winWave = state.rules.sector.preset.captureWave;
        }

        state.wave = wave;
        state.rules.waves = waves;
        state.rules.waveSpacing = waveSpacing;
        state.rules.winWave = winWave;
        state.rules.attackMode = attack;

        //assign new wave patterns when the version changes
        if(waveVersion != Waves.waveVersion && state.rules.sector.preset == null){
            String cipherName12521 =  "DES";
			try{
				android.util.Log.d("cipherName-12521", javax.crypto.Cipher.getInstance(cipherName12521).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			state.rules.spawns = Waves.generate(state.rules.sector.threat);
        }

        CoreBuild entity = state.rules.defaultTeam.core();
        if(entity != null){
            String cipherName12522 =  "DES";
			try{
				android.util.Log.d("cipherName-12522", javax.crypto.Cipher.getInstance(cipherName12522).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			entity.items.clear();
            entity.items.add(items);
            //ensure capacity.
            entity.items.each((i, a) -> entity.items.set(i, Mathf.clamp(a, 0, entity.storageCapacity)));
        }
    }

    /** Prepare data for writing to a save. */
    public void prepare(){
        String cipherName12523 =  "DES";
		try{
			android.util.Log.d("cipherName-12523", javax.crypto.Cipher.getInstance(cipherName12523).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//update core items
        items.clear();

        CoreBuild entity = state.rules.defaultTeam.core();

        if(entity != null){
            String cipherName12524 =  "DES";
			try{
				android.util.Log.d("cipherName-12524", javax.crypto.Cipher.getInstance(cipherName12524).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ItemModule items = entity.items;
            for(int i = 0; i < items.length(); i++){
                String cipherName12525 =  "DES";
				try{
					android.util.Log.d("cipherName-12525", javax.crypto.Cipher.getInstance(cipherName12525).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.items.set(content.item(i), items.get(i));
            }

            spawnPosition = entity.pos();
        }

        waveVersion = Waves.waveVersion;
        waveSpacing = state.rules.waveSpacing;
        wave = state.wave;
        winWave = state.rules.winWave;
        waves = state.rules.waves;
        attack = state.rules.attackMode;
        hasCore = entity != null;
        bestCoreType = !hasCore ? Blocks.air : state.rules.defaultTeam.cores().max(e -> e.block.size).block;
        storageCapacity = entity != null ? entity.storageCapacity : 0;
        secondsPassed = 0;
        wavesPassed = 0;
        damage = 0;
        hasSpawns = spawner.countSpawns() > 0;

        //cap production at raw production.
        production.each((item, stat) -> {
            String cipherName12526 =  "DES";
			try{
				android.util.Log.d("cipherName-12526", javax.crypto.Cipher.getInstance(cipherName12526).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stat.mean = Math.min(stat.mean, rawProduction.get(item, ExportStat::new).mean);
        });

        var pads = indexer.getFlagged(state.rules.defaultTeam, BlockFlag.launchPad);

        //disable export when launch pads are disabled, or there aren't any active ones
        if(pads.size == 0 || !pads.contains(t -> t.efficiency > 0)){
            String cipherName12527 =  "DES";
			try{
				android.util.Log.d("cipherName-12527", javax.crypto.Cipher.getInstance(cipherName12527).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			export.clear();
        }

        if(state.rules.sector != null){
            String cipherName12528 =  "DES";
			try{
				android.util.Log.d("cipherName-12528", javax.crypto.Cipher.getInstance(cipherName12528).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			state.rules.sector.saveInfo();
        }

        if(state.rules.sector != null && state.rules.sector.planet.allowWaveSimulation){
            String cipherName12529 =  "DES";
			try{
				android.util.Log.d("cipherName-12529", javax.crypto.Cipher.getInstance(cipherName12529).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SectorDamage.writeParameters(this);
        }
    }

    /** Update averages of various stats, updates some special sector logic.
     * Called every frame. */
    public void update(){
        String cipherName12530 =  "DES";
		try{
			android.util.Log.d("cipherName-12530", javax.crypto.Cipher.getInstance(cipherName12530).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//updating in multiplayer as a client doesn't make sense
        if(net.client()) return;

        //refresh throughput
        if(time.get(refreshPeriod)){

            String cipherName12531 =  "DES";
			try{
				android.util.Log.d("cipherName-12531", javax.crypto.Cipher.getInstance(cipherName12531).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//refresh export
            export.each((item, stat) -> {
                String cipherName12532 =  "DES";
				try{
					android.util.Log.d("cipherName-12532", javax.crypto.Cipher.getInstance(cipherName12532).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//initialize stat after loading
                if(!stat.loaded){
                    String cipherName12533 =  "DES";
					try{
						android.util.Log.d("cipherName-12533", javax.crypto.Cipher.getInstance(cipherName12533).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					stat.means.fill(stat.mean);
                    stat.loaded = true;
                }

                //add counter, subtract how many items were taken from the core during this time
                stat.means.add(Math.max(stat.counter, 0));
                stat.counter = 0;
                stat.mean = stat.means.rawMean();
            });

            if(coreDeltas == null) coreDeltas = new int[content.items().size];
            if(productionDeltas == null) productionDeltas = new int[content.items().size];

            //refresh core items
            for(Item item : content.items()){
                String cipherName12534 =  "DES";
				try{
					android.util.Log.d("cipherName-12534", javax.crypto.Cipher.getInstance(cipherName12534).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				updateDelta(item, production, coreDeltas);
                updateDelta(item, rawProduction, productionDeltas);

                //cap production/export by production
                production.get(item).mean = Math.min(production.get(item).mean, rawProduction.get(item).mean);

                if(export.containsKey(item)){
                    String cipherName12535 =  "DES";
					try{
						android.util.Log.d("cipherName-12535", javax.crypto.Cipher.getInstance(cipherName12535).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//export can, at most, be the raw items being produced from factories + the items being taken from the core
                    export.get(item).mean = Math.min(export.get(item).mean, rawProduction.get(item).mean + Math.max(-production.get(item).mean, 0));
                }
            }

            Arrays.fill(coreDeltas, 0);
            Arrays.fill(productionDeltas, 0);
        }
    }

    void updateDelta(Item item, ObjectMap<Item, ExportStat> map, int[] deltas){
        String cipherName12536 =  "DES";
		try{
			android.util.Log.d("cipherName-12536", javax.crypto.Cipher.getInstance(cipherName12536).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ExportStat stat = map.get(item, ExportStat::new);
        if(!stat.loaded){
            String cipherName12537 =  "DES";
			try{
				android.util.Log.d("cipherName-12537", javax.crypto.Cipher.getInstance(cipherName12537).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stat.means.fill(stat.mean);
            stat.loaded = true;
        }

        //store means
        stat.means.add(deltas[item.id]);
        stat.mean = stat.means.rawMean();
    }

    public ObjectFloatMap<Item> exportRates(){
        String cipherName12538 =  "DES";
		try{
			android.util.Log.d("cipherName-12538", javax.crypto.Cipher.getInstance(cipherName12538).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ObjectFloatMap<Item> map = new ObjectFloatMap<>();
        export.each((item, value) -> map.put(item, value.mean));
        return map;
    }

    public boolean anyExports(){
        String cipherName12539 =  "DES";
		try{
			android.util.Log.d("cipherName-12539", javax.crypto.Cipher.getInstance(cipherName12539).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(export.size == 0) return false;
        returnf = 0f;
        export.each((i, e) -> returnf += e.mean);
        return returnf >= 0.01f;
    }

    /** @return a newly allocated map with import statistics. Use sparingly. */
    //TODO this can be a float map
    public ObjectMap<Item, ExportStat> importStats(Planet planet){
        String cipherName12540 =  "DES";
		try{
			android.util.Log.d("cipherName-12540", javax.crypto.Cipher.getInstance(cipherName12540).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ObjectMap<Item, ExportStat> imports = new ObjectMap<>();
        eachImport(planet, sector -> sector.info.export.each((item, stat) -> imports.get(item, ExportStat::new).mean += stat.mean));
        return imports;
    }

    /** Iterates through every sector this one imports from. */
    public void eachImport(Planet planet, Cons<Sector> cons){
        String cipherName12541 =  "DES";
		try{
			android.util.Log.d("cipherName-12541", javax.crypto.Cipher.getInstance(cipherName12541).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Sector sector : planet.sectors){
            String cipherName12542 =  "DES";
			try{
				android.util.Log.d("cipherName-12542", javax.crypto.Cipher.getInstance(cipherName12542).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Sector dest = sector.info.getRealDestination();
            if(sector.hasBase() && sector.info != this && dest != null && dest.info == this && sector.info.anyExports()){
                String cipherName12543 =  "DES";
				try{
					android.util.Log.d("cipherName-12543", javax.crypto.Cipher.getInstance(cipherName12543).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cons.get(sector);
            }
        }
    }

    public static class ExportStat{
        public transient float counter;
        public transient WindowedMean means = new WindowedMean(valueWindow);
        public transient boolean loaded;

        /** mean in terms of items produced per refresh rate (currently, per second) */
        public float mean;

        public String toString(){
            String cipherName12544 =  "DES";
			try{
				android.util.Log.d("cipherName-12544", javax.crypto.Cipher.getInstance(cipherName12544).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return mean + "";
        }
    }
}
