package mindustry.ai;

import arc.*;
import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class WaveSpawner{
    private static final float margin = 0f, coreMargin = tilesize * 2f, maxSteps = 30;

    private int tmpCount;
    private Seq<Tile> spawns = new Seq<>();
    private boolean spawning = false;
    private boolean any = false;
    private Tile firstSpawn = null;

    public WaveSpawner(){
        String cipherName13691 =  "DES";
		try{
			android.util.Log.d("cipherName-13691", javax.crypto.Cipher.getInstance(cipherName13691).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Events.on(WorldLoadEvent.class, e -> reset());
    }

    @Nullable
    public Tile getFirstSpawn(){
        String cipherName13692 =  "DES";
		try{
			android.util.Log.d("cipherName-13692", javax.crypto.Cipher.getInstance(cipherName13692).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		firstSpawn = null;
        eachGroundSpawn((cx, cy) -> {
            String cipherName13693 =  "DES";
			try{
				android.util.Log.d("cipherName-13693", javax.crypto.Cipher.getInstance(cipherName13693).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			firstSpawn = world.tile(cx, cy);
        });
        return firstSpawn;
    }

    public int countSpawns(){
        String cipherName13694 =  "DES";
		try{
			android.util.Log.d("cipherName-13694", javax.crypto.Cipher.getInstance(cipherName13694).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return spawns.size;
    }

    public Seq<Tile> getSpawns(){
        String cipherName13695 =  "DES";
		try{
			android.util.Log.d("cipherName-13695", javax.crypto.Cipher.getInstance(cipherName13695).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return spawns;
    }

    /** @return true if the player is near a ground spawn point. */
    public boolean playerNear(){
        String cipherName13696 =  "DES";
		try{
			android.util.Log.d("cipherName-13696", javax.crypto.Cipher.getInstance(cipherName13696).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.hasSpawns() && !player.dead() && spawns.contains(g -> Mathf.dst(g.x * tilesize, g.y * tilesize, player.x, player.y) < state.rules.dropZoneRadius && player.team() != state.rules.waveTeam);
    }

    public void spawnEnemies(){
        String cipherName13697 =  "DES";
		try{
			android.util.Log.d("cipherName-13697", javax.crypto.Cipher.getInstance(cipherName13697).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		spawning = true;

        eachGroundSpawn(-1, (spawnX, spawnY, doShockwave) -> {
            String cipherName13698 =  "DES";
			try{
				android.util.Log.d("cipherName-13698", javax.crypto.Cipher.getInstance(cipherName13698).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(doShockwave){
                String cipherName13699 =  "DES";
				try{
					android.util.Log.d("cipherName-13699", javax.crypto.Cipher.getInstance(cipherName13699).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doShockwave(spawnX, spawnY);
            }
        });

        for(SpawnGroup group : state.rules.spawns){
            String cipherName13700 =  "DES";
			try{
				android.util.Log.d("cipherName-13700", javax.crypto.Cipher.getInstance(cipherName13700).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(group.type == null) continue;

            int spawned = group.getSpawned(state.wave - 1);

            if(group.type.flying){
                String cipherName13701 =  "DES";
				try{
					android.util.Log.d("cipherName-13701", javax.crypto.Cipher.getInstance(cipherName13701).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float spread = margin / 1.5f;

                eachFlyerSpawn(group.spawn, (spawnX, spawnY) -> {
                    String cipherName13702 =  "DES";
					try{
						android.util.Log.d("cipherName-13702", javax.crypto.Cipher.getInstance(cipherName13702).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int i = 0; i < spawned; i++){
                        String cipherName13703 =  "DES";
						try{
							android.util.Log.d("cipherName-13703", javax.crypto.Cipher.getInstance(cipherName13703).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Unit unit = group.createUnit(state.rules.waveTeam, state.wave - 1);
                        unit.set(spawnX + Mathf.range(spread), spawnY + Mathf.range(spread));
                        spawnEffect(unit);
                    }
                });
            }else{
                String cipherName13704 =  "DES";
				try{
					android.util.Log.d("cipherName-13704", javax.crypto.Cipher.getInstance(cipherName13704).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float spread = tilesize * 2;

                eachGroundSpawn(group.spawn, (spawnX, spawnY, doShockwave) -> {

                    String cipherName13705 =  "DES";
					try{
						android.util.Log.d("cipherName-13705", javax.crypto.Cipher.getInstance(cipherName13705).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int i = 0; i < spawned; i++){
                        String cipherName13706 =  "DES";
						try{
							android.util.Log.d("cipherName-13706", javax.crypto.Cipher.getInstance(cipherName13706).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tmp.v1.rnd(spread);

                        Unit unit = group.createUnit(state.rules.waveTeam, state.wave - 1);
                        unit.set(spawnX + Tmp.v1.x, spawnY + Tmp.v1.y);
                        spawnEffect(unit);
                    }
                });
            }
        }

        Time.run(121f, () -> spawning = false);
    }

    public void doShockwave(float x, float y){
        String cipherName13707 =  "DES";
		try{
			android.util.Log.d("cipherName-13707", javax.crypto.Cipher.getInstance(cipherName13707).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fx.spawnShockwave.at(x, y, state.rules.dropZoneRadius);
        Damage.damage(state.rules.waveTeam, x, y, state.rules.dropZoneRadius, 99999999f, true);
    }

    public void eachGroundSpawn(Intc2 cons){
        String cipherName13708 =  "DES";
		try{
			android.util.Log.d("cipherName-13708", javax.crypto.Cipher.getInstance(cipherName13708).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		eachGroundSpawn(-1, (x, y, shock) -> cons.get(World.toTile(x), World.toTile(y)));
    }

    private void eachGroundSpawn(int filterPos, SpawnConsumer cons){
        String cipherName13709 =  "DES";
		try{
			android.util.Log.d("cipherName-13709", javax.crypto.Cipher.getInstance(cipherName13709).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state.hasSpawns()){
            String cipherName13710 =  "DES";
			try{
				android.util.Log.d("cipherName-13710", javax.crypto.Cipher.getInstance(cipherName13710).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Tile spawn : spawns){
                String cipherName13711 =  "DES";
				try{
					android.util.Log.d("cipherName-13711", javax.crypto.Cipher.getInstance(cipherName13711).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(filterPos != -1 && filterPos != spawn.pos()) continue;

                cons.accept(spawn.worldx(), spawn.worldy(), true);
            }
        }

        if(state.rules.attackMode && state.teams.isActive(state.rules.waveTeam) && !state.teams.playerCores().isEmpty()){
            String cipherName13712 =  "DES";
			try{
				android.util.Log.d("cipherName-13712", javax.crypto.Cipher.getInstance(cipherName13712).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building firstCore = state.teams.playerCores().first();
            for(Building core : state.rules.waveTeam.cores()){
                String cipherName13713 =  "DES";
				try{
					android.util.Log.d("cipherName-13713", javax.crypto.Cipher.getInstance(cipherName13713).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(filterPos != -1 && filterPos != core.pos()) continue;

                Tmp.v1.set(firstCore).sub(core).limit(coreMargin + core.block.size * tilesize /2f * Mathf.sqrt2);

                boolean valid = false;
                int steps = 0;

                //keep moving forward until the max step amount is reached
                while(steps++ < maxSteps){
                    String cipherName13714 =  "DES";
					try{
						android.util.Log.d("cipherName-13714", javax.crypto.Cipher.getInstance(cipherName13714).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int tx = World.toTile(core.x + Tmp.v1.x), ty = World.toTile(core.y + Tmp.v1.y);
                    any = false;
                    Geometry.circle(tx, ty, world.width(), world.height(), 3, (x, y) -> {
                        String cipherName13715 =  "DES";
						try{
							android.util.Log.d("cipherName-13715", javax.crypto.Cipher.getInstance(cipherName13715).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(world.solid(x, y)){
                            String cipherName13716 =  "DES";
							try{
								android.util.Log.d("cipherName-13716", javax.crypto.Cipher.getInstance(cipherName13716).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							any = true;
                        }
                    });

                    //nothing is in the way, spawn it
                    if(!any){
                        String cipherName13717 =  "DES";
						try{
							android.util.Log.d("cipherName-13717", javax.crypto.Cipher.getInstance(cipherName13717).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						valid = true;
                        break;
                    }else{
                        String cipherName13718 =  "DES";
						try{
							android.util.Log.d("cipherName-13718", javax.crypto.Cipher.getInstance(cipherName13718).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//make the vector longer
                        Tmp.v1.setLength(Tmp.v1.len() + tilesize*1.1f);
                    }
                }

                if(valid){
                    String cipherName13719 =  "DES";
					try{
						android.util.Log.d("cipherName-13719", javax.crypto.Cipher.getInstance(cipherName13719).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cons.accept(core.x + Tmp.v1.x, core.y + Tmp.v1.y, false);
                }
            }
        }
    }

    private void eachFlyerSpawn(int filterPos, Floatc2 cons){
        String cipherName13720 =  "DES";
		try{
			android.util.Log.d("cipherName-13720", javax.crypto.Cipher.getInstance(cipherName13720).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : spawns){
            String cipherName13721 =  "DES";
			try{
				android.util.Log.d("cipherName-13721", javax.crypto.Cipher.getInstance(cipherName13721).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(filterPos != -1 && filterPos != tile.pos()) continue;

            float angle = Angles.angle(world.width() / 2f, world.height() / 2f, tile.x, tile.y);
            float trns = Math.max(world.width(), world.height()) * Mathf.sqrt2 * tilesize;
            float spawnX = Mathf.clamp(world.width() * tilesize / 2f + Angles.trnsx(angle, trns), -margin, world.width() * tilesize + margin);
            float spawnY = Mathf.clamp(world.height() * tilesize / 2f + Angles.trnsy(angle, trns), -margin, world.height() * tilesize + margin);
            cons.get(spawnX, spawnY);
        }

        if(state.rules.attackMode && state.teams.isActive(state.rules.waveTeam)){
            String cipherName13722 =  "DES";
			try{
				android.util.Log.d("cipherName-13722", javax.crypto.Cipher.getInstance(cipherName13722).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Building core : state.rules.waveTeam.data().cores){
                String cipherName13723 =  "DES";
				try{
					android.util.Log.d("cipherName-13723", javax.crypto.Cipher.getInstance(cipherName13723).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(filterPos != -1 && filterPos != core.pos()) continue;

                cons.get(core.x, core.y);
            }
        }
    }

    public int countGroundSpawns(){
        String cipherName13724 =  "DES";
		try{
			android.util.Log.d("cipherName-13724", javax.crypto.Cipher.getInstance(cipherName13724).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tmpCount = 0;
        eachGroundSpawn((x, y) -> tmpCount ++);
        return tmpCount;
    }

    public int countFlyerSpawns(){
        String cipherName13725 =  "DES";
		try{
			android.util.Log.d("cipherName-13725", javax.crypto.Cipher.getInstance(cipherName13725).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tmpCount = 0;
        eachFlyerSpawn(-1, (x, y) -> tmpCount ++);
        return tmpCount;
    }

    public boolean isSpawning(){
        String cipherName13726 =  "DES";
		try{
			android.util.Log.d("cipherName-13726", javax.crypto.Cipher.getInstance(cipherName13726).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return spawning && !net.client();
    }

    public void reset(){
        String cipherName13727 =  "DES";
		try{
			android.util.Log.d("cipherName-13727", javax.crypto.Cipher.getInstance(cipherName13727).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		spawning = false;
        spawns.clear();

        for(Tile tile : world.tiles){
            String cipherName13728 =  "DES";
			try{
				android.util.Log.d("cipherName-13728", javax.crypto.Cipher.getInstance(cipherName13728).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.overlay() == Blocks.spawn){
                String cipherName13729 =  "DES";
				try{
					android.util.Log.d("cipherName-13729", javax.crypto.Cipher.getInstance(cipherName13729).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				spawns.add(tile);
            }
        }
    }

    /** Applies the standard wave spawn effects to a unit - invincibility, unmoving. */
    public void spawnEffect(Unit unit){
        String cipherName13730 =  "DES";
		try{
			android.util.Log.d("cipherName-13730", javax.crypto.Cipher.getInstance(cipherName13730).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		spawnEffect(unit, unit.angleTo(world.width()/2f * tilesize, world.height()/2f * tilesize));
    }

    /** Applies the standard wave spawn effects to a unit - invincibility, unmoving. */
    public void spawnEffect(Unit unit, float rotation){
        String cipherName13731 =  "DES";
		try{
			android.util.Log.d("cipherName-13731", javax.crypto.Cipher.getInstance(cipherName13731).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		unit.rotation = rotation;
        unit.apply(StatusEffects.unmoving, 30f);
        unit.apply(StatusEffects.invincible, 60f);
        unit.add();
        unit.unloaded();

        Events.fire(new UnitSpawnEvent(unit));
        Call.spawnEffect(unit.x, unit.y, unit.rotation, unit.type);
    }

    private interface SpawnConsumer{
        void accept(float x, float y, boolean shockwave);
    }

    @Remote(called = Loc.server, unreliable = true)
    public static void spawnEffect(float x, float y, float rotation, UnitType u){

        String cipherName13732 =  "DES";
		try{
			android.util.Log.d("cipherName-13732", javax.crypto.Cipher.getInstance(cipherName13732).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fx.unitSpawn.at(x, y, rotation, u);

        Time.run(30f, () -> Fx.spawn.at(x, y));
    }
}
