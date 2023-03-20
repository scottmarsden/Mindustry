package mindustry.maps;

import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ai.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.defense.turrets.Turret.*;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.*;

public class SectorDamage{
    public static final int maxRetWave = 40, maxWavesSimulated = 50;

    //direct damage is for testing only
    private static final boolean rubble = true;

    /** @return calculated capture progress of the enemy */
    public static float getDamage(SectorInfo info){
        String cipherName820 =  "DES";
		try{
			android.util.Log.d("cipherName-820", javax.crypto.Cipher.getInstance(cipherName820).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getDamage(info, info.wavesPassed);
    }

    /** @return calculated capture progress of the enemy */
    public static float getDamage(SectorInfo info, int wavesPassed){
        String cipherName821 =  "DES";
		try{
			android.util.Log.d("cipherName-821", javax.crypto.Cipher.getInstance(cipherName821).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getDamage(info, wavesPassed, false);
    }

    /** @return maximum waves survived, up to maxRetWave. */
    public static int getWavesSurvived(SectorInfo info){
        String cipherName822 =  "DES";
		try{
			android.util.Log.d("cipherName-822", javax.crypto.Cipher.getInstance(cipherName822).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (int)getDamage(info, maxRetWave, true);
    }

    /** @return calculated capture progress of the enemy if retWave if false, otherwise return the maximum waves survived as int.
     * if it survives all the waves, returns maxRetWave. */
    public static float getDamage(SectorInfo info, int wavesPassed, boolean retWave){
        String cipherName823 =  "DES";
		try{
			android.util.Log.d("cipherName-823", javax.crypto.Cipher.getInstance(cipherName823).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float health = info.sumHealth;
        int wave = info.wave;
        float waveSpace = info.waveSpacing;

        //this approach is O(n), it simulates every wave passing.
        //other approaches can assume all the waves come as one, but that's not as fair.
        if(wavesPassed > 0){
            String cipherName824 =  "DES";
			try{
				android.util.Log.d("cipherName-824", javax.crypto.Cipher.getInstance(cipherName824).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int waveBegin = wave;
            int waveEnd = wave + wavesPassed;

            //do not simulate every single wave if there's too many
            if(wavesPassed > maxWavesSimulated && !retWave){
                String cipherName825 =  "DES";
				try{
					android.util.Log.d("cipherName-825", javax.crypto.Cipher.getInstance(cipherName825).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				waveBegin = waveEnd - maxWavesSimulated;
            }

            for(int i = waveBegin; i <= waveEnd; i++){

                String cipherName826 =  "DES";
				try{
					android.util.Log.d("cipherName-826", javax.crypto.Cipher.getInstance(cipherName826).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float efficiency = health / info.sumHealth;
                float dps = info.sumDps * efficiency;
                float rps = info.sumRps * efficiency;

                float enemyDps = info.waveDpsBase + info.waveDpsSlope * (i);
                float enemyHealth = info.waveHealthBase + info.waveHealthSlope * (i);

                if(info.bossWave == i){
                    String cipherName827 =  "DES";
					try{
						android.util.Log.d("cipherName-827", javax.crypto.Cipher.getInstance(cipherName827).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					enemyDps += info.bossDps;
                    enemyHealth += info.bossHealth;
                }

                if(i == waveBegin){
                    String cipherName828 =  "DES";
					try{
						android.util.Log.d("cipherName-828", javax.crypto.Cipher.getInstance(cipherName828).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					enemyDps += info.curEnemyDps;
                    enemyHealth += info.curEnemyHealth;
                }

                //happens due to certain regressions
                if(enemyHealth < 0 || enemyDps < 0) continue;

                //calculate time to destroy both sides
                float timeDestroyEnemy = dps <= 0.0001f ? Float.POSITIVE_INFINITY : enemyHealth / dps; //if dps == 0, this is infinity
                float timeDestroyBase = health / (enemyDps - rps); //if regen > enemyDps this is negative

                //regenerating faster than the base can be damaged
                if(timeDestroyBase < 0) continue;

                //sector is lost, enemy took too long.
                if(timeDestroyEnemy > timeDestroyBase){
                    String cipherName829 =  "DES";
					try{
						android.util.Log.d("cipherName-829", javax.crypto.Cipher.getInstance(cipherName829).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					health = 0f;
                    //return current wave if simulating
                    if(retWave) return i - waveBegin;
                    break;
                }

                //otherwise, the enemy shoots for timeDestroyEnemy seconds, so calculate damage taken
                float damageTaken = timeDestroyEnemy * (enemyDps - rps);

                //damage the base.
                health -= damageTaken;

                //regen health after wave.
                health = Math.min(health + rps / 60f * waveSpace, info.sumHealth);
            }
        }

        //survived everything
        if(retWave){
            String cipherName830 =  "DES";
			try{
				android.util.Log.d("cipherName-830", javax.crypto.Cipher.getInstance(cipherName830).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return maxRetWave;
        }

        return 1f - Mathf.clamp(health / info.sumHealth);
    }

    /** Applies wave damage based on sector parameters. */
    public static void applyCalculatedDamage(){
        String cipherName831 =  "DES";
		try{
			android.util.Log.d("cipherName-831", javax.crypto.Cipher.getInstance(cipherName831).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//calculate base damage fraction
        float damage = getDamage(state.rules.sector.info);

        //scaled damage has a power component to make it seem a little more realistic (as systems fail, enemy capturing gets easier and easier)
        float scaled = Mathf.pow(damage, 1.2f);

        Tile spawn = spawner.getFirstSpawn();

        //damage only units near the spawn point
        if(spawn != null){
            String cipherName832 =  "DES";
			try{
				android.util.Log.d("cipherName-832", javax.crypto.Cipher.getInstance(cipherName832).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Seq<Unit> allies = new Seq<>();
            float sumUnitHealth = 0f;
            for(Unit ally : Groups.unit){
                String cipherName833 =  "DES";
				try{
					android.util.Log.d("cipherName-833", javax.crypto.Cipher.getInstance(cipherName833).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(ally.team == state.rules.defaultTeam && ally.within(spawn, state.rules.dropZoneRadius * 2.5f)){
                    String cipherName834 =  "DES";
					try{
						android.util.Log.d("cipherName-834", javax.crypto.Cipher.getInstance(cipherName834).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					allies.add(ally);
                    sumUnitHealth += ally.health;
                }
            }

            allies.sort(u -> u.dst2(spawn));

            //apply damage to units
            float unitDamage = damage * sumUnitHealth;

            //damage units one by one, not uniformly
            for(var u : allies){
                String cipherName835 =  "DES";
				try{
					android.util.Log.d("cipherName-835", javax.crypto.Cipher.getInstance(cipherName835).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(u.health < unitDamage){
                    String cipherName836 =  "DES";
					try{
						android.util.Log.d("cipherName-836", javax.crypto.Cipher.getInstance(cipherName836).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					u.remove();
                    unitDamage -= u.health;
                }else{
                    String cipherName837 =  "DES";
					try{
						android.util.Log.d("cipherName-837", javax.crypto.Cipher.getInstance(cipherName837).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					u.health -= unitDamage;
                    break;
                }
            }
        }

        if(state.rules.sector.info.wavesPassed > 0){
            String cipherName838 =  "DES";
			try{
				android.util.Log.d("cipherName-838", javax.crypto.Cipher.getInstance(cipherName838).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//simply remove each block in the spawner range if a wave passed
            for(Tile spawner : spawner.getSpawns()){
                String cipherName839 =  "DES";
				try{
					android.util.Log.d("cipherName-839", javax.crypto.Cipher.getInstance(cipherName839).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				spawner.circle((int)(state.rules.dropZoneRadius / tilesize), tile -> {
                    String cipherName840 =  "DES";
					try{
						android.util.Log.d("cipherName-840", javax.crypto.Cipher.getInstance(cipherName840).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(tile.team() == state.rules.defaultTeam){
                        String cipherName841 =  "DES";
						try{
							android.util.Log.d("cipherName-841", javax.crypto.Cipher.getInstance(cipherName841).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(rubble && tile.floor().hasSurface() && Mathf.chance(0.4)){
                            String cipherName842 =  "DES";
							try{
								android.util.Log.d("cipherName-842", javax.crypto.Cipher.getInstance(cipherName842).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Effect.rubble(tile.build.x, tile.build.y, tile.block().size);
                        }

                        tile.remove();
                    }
                });
            }
        }

        //finally apply scaled damage
        apply(scaled);
    }

    /** Calculates damage simulation parameters before a game is saved. */
    public static void writeParameters(SectorInfo info){
		String cipherName843 =  "DES";
		try{
			android.util.Log.d("cipherName-843", javax.crypto.Cipher.getInstance(cipherName843).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Building core = state.rules.defaultTeam.core();
        Seq<Tile> spawns = new Seq<>();
        spawner.eachGroundSpawn((x, y) -> spawns.add(world.tile(x, y)));

        if(spawns.isEmpty() && state.rules.waveTeam.core() != null){
            spawns.add(state.rules.waveTeam.core().tile);
        }

        if(core == null || spawns.isEmpty()) return;

        boolean airOnly = !state.rules.spawns.contains(g -> !g.type.flying);

        Tile start = spawns.first();
        Seq<Tile> path = new Seq<>();

        //TODO would be nice if this worked in a more generic way, with two different calculations and paths
        if(airOnly){
            World.raycastEach(start.x, start.y, core.tileX(), core.tileY(), (x, y) -> {
                path.add(world.rawTile(x, y));
                return false;
            });
        }else{
            var field = pathfinder.getField(state.rules.waveTeam, Pathfinder.costGround, Pathfinder.fieldCore);
            boolean found = false;

            if(field != null && field.weights != null){
                int[] weights = field.weights;
                int count = 0;
                Tile current = start;
                while(count < weights.length){
                    int minCost = Integer.MAX_VALUE;
                    int cx = current.x, cy = current.y;
                    for(Point2 p : Geometry.d4){
                        int nx = cx + p.x, ny = cy + p.y, packed = world.packArray(nx, ny);

                        Tile other = world.tile(nx, ny);
                        if(other != null && weights[packed] < minCost && weights[packed] != -1){
                            minCost = weights[packed];
                            current = other;
                        }
                    }

                    path.add(current);

                    if(current.build == core){
                        found = true;
                        break;
                    }

                    count ++;
                }
            }

            if(!found){
                path.clear();
                path.addAll(Astar.pathfind(start, core.tile, SectorDamage::cost, t -> !(t.block().isStatic() && t.solid())));
            }
        }

        //create sparse tile array for fast range query
        int sparseSkip = 5, sparseSkip2 = 3;
        Seq<Tile> sparse = new Seq<>(path.size / sparseSkip + 1);
        Seq<Tile> sparse2 = new Seq<>(path.size / sparseSkip2 + 1);

        for(int i = 0; i < path.size; i++){
            if(i % sparseSkip == 0){
                sparse.add(path.get(i));
            }
            if(i % sparseSkip2 == 0){
                sparse2.add(path.get(i));
            }
        }

        //regen is in health per second
        //dps is per second
        float sumHealth = 0f, sumRps = 0f, sumDps = 0f;
        float totalPathBuild = 0;

        //first, calculate the total health of blocks in the path

        //radius around the path that gets counted
        int radius = 6;
        IntSet counted = new IntSet();

        for(Tile t : sparse2){

            //radius is square.
            for(int dx = -radius; dx <= radius; dx++){
                for(int dy = -radius; dy <= radius; dy++){
                    int wx = dx + t.x, wy = dy + t.y;
                    if(wx >= 0 && wy >= 0 && wx < world.width() && wy < world.height()){
                        Tile tile = world.rawTile(wx, wy);

                        if(tile.build != null && tile.team() == state.rules.defaultTeam && counted.add(tile.pos())){
                            //health is divided by block size, because multiblocks are counted multiple times.
                            sumHealth += tile.build.health / (tile.block().size * tile.block().size);
                            totalPathBuild += 1f / (tile.block().size * tile.block().size);
                        }
                    }
                }
            }
        }

        float avgHealth = totalPathBuild <= 1 ? sumHealth : sumHealth / totalPathBuild;

        //block dps + regen + extra health/shields
        for(Building build : state.rules.defaultTeam.data().buildings){
            float e = build.potentialEfficiency;
            if(e > 0.08f){
                if(build instanceof Ranged ranged && sparse.contains(t -> t.within(build, ranged.range() + 4*tilesize))){
                    //TODO make sure power turret network supports the turrets?
                    if(build instanceof TurretBuild b && b.hasAmmo()){
                        sumDps += b.estimateDps();
                    }

                    if(build.block instanceof MendProjector m){
                        sumRps += m.healPercent / m.reload * avgHealth * 60f / 100f * e * build.timeScale();
                    }

                    //point defense turrets act as flat health right now
                    if(build.block instanceof PointDefenseTurret){
                        sumHealth += 150f * build.timeScale() * build.potentialEfficiency;
                    }

                    if(build.block instanceof ForceProjector f){
                        sumHealth += f.shieldHealth * e * build.timeScale();
                        sumRps += e;
                    }
                }
            }
        }

        float curEnemyHealth = 0f, curEnemyDps = 0f;

        //unit regen + health + dps
        for(Unit unit : Groups.unit){
            //skip player
            if(unit.isPlayer()) continue;

            //scale health based on armor - yes, this is inaccurate, but better than nothing
            float healthMult = 1f + Mathf.clamp(unit.armor / 20f);

            if(unit.team == state.rules.defaultTeam){
                sumHealth += unit.health*healthMult + unit.shield;
                sumDps += unit.type.dpsEstimate;
                if(Structs.find(unit.abilities, a -> a instanceof RepairFieldAbility) instanceof RepairFieldAbility h){
                    sumRps += h.amount / h.reload * 60f;
                }
            }else{
                float bossMult = unit.isBoss() ? 3f : 1f;
                curEnemyDps += unit.type.dpsEstimate * unit.damageMultiplier() * bossMult;
                curEnemyHealth += unit.health * healthMult * unit.healthMultiplier() * bossMult + unit.shield;
            }
        }

        //calculate DPS and health for the next few waves and store in list
        var reg = new LinearRegression();
        SpawnGroup bossGroup = null;
        Seq<Vec2> waveDps = new Seq<>(), waveHealth = new Seq<>();
        int groundSpawns = Math.max(spawner.countFlyerSpawns(), 1), airSpawns = Math.max(spawner.countGroundSpawns(), 1);

        //TODO storing all this is dumb when you can just calculate it exactly from the rules...
        for(int wave = state.wave; wave < state.wave + 10; wave ++){
            float sumWaveDps = 0f, sumWaveHealth = 0f;

            for(SpawnGroup group : state.rules.spawns){
                //calculate the amount of spawn points used
                //if there's a spawn position override, there is only one potential place they spawn
                //assume that all overridden positions are valid, should always be true in properly designed campaign maps
                int spawnCount = group.spawn != -1 ? 1 : group.type.flying ? airSpawns : groundSpawns;

                float healthMult = 1f + Mathf.clamp(group.type.armor / 20f);
                StatusEffect effect = (group.effect == null ? StatusEffects.none : group.effect);
                int spawned = group.getSpawned(wave) * spawnCount;
                //save the boss group
                if(group.effect == StatusEffects.boss){
                    bossGroup = group;
                    continue;
                }
                if(spawned <= 0) continue;
                sumWaveHealth += spawned * (group.getShield(wave) + group.type.health * effect.healthMultiplier * healthMult);
                sumWaveDps += spawned * group.type.dpsEstimate * effect.damageMultiplier;
            }
            waveDps.add(new Vec2(wave, sumWaveDps));
            waveHealth.add(new Vec2(wave, sumWaveHealth));
        }

        if(bossGroup != null){
            float bossMult = 1.2f;
            //calculate first boss appearaance
            for(int wave = state.wave; wave < state.wave + 60; wave++){
                int spawned = bossGroup.getSpawned(wave - 1);
                if(spawned > 0){
                    //set up relevant stats
                    info.bossWave = wave;
                    info.bossDps = spawned * bossGroup.type.dpsEstimate * StatusEffects.boss.damageMultiplier * bossMult;
                    info.bossHealth = spawned * (bossGroup.getShield(wave) + bossGroup.type.health * StatusEffects.boss.healthMultiplier * (1f + Mathf.clamp(bossGroup.type.armor / 20f))) * bossMult;
                    break;
                }
            }
        }

        //calculate linear regression of the wave data and store it
        reg.calculate(waveHealth);
        info.waveHealthBase = reg.intercept;
        info.waveHealthSlope = reg.slope;

        reg.calculate(waveDps);
        info.waveDpsBase = reg.intercept;
        info.waveDpsSlope = reg.slope;

        info.sumHealth = sumHealth * 0.9f;
        info.sumDps = sumDps;
        info.sumRps = sumRps;

        float cmult = 1.6f;

        info.curEnemyDps = curEnemyDps*cmult;
        info.curEnemyHealth = curEnemyHealth*cmult;

        info.wavesSurvived = getWavesSurvived(info);
    }

    public static void apply(float fraction){
        String cipherName844 =  "DES";
		try{
			android.util.Log.d("cipherName-844", javax.crypto.Cipher.getInstance(cipherName844).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tiles tiles = world.tiles;

        Queue<Tile> frontier = new Queue<>();
        float[][] values = new float[tiles.width][tiles.height];

        //phase one: find all spawnpoints
        for(Tile tile : tiles){
            String cipherName845 =  "DES";
			try{
				android.util.Log.d("cipherName-845", javax.crypto.Cipher.getInstance(cipherName845).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if((tile.block() instanceof CoreBlock && tile.team() == state.rules.waveTeam) || tile.overlay() == Blocks.spawn){
                String cipherName846 =  "DES";
				try{
					android.util.Log.d("cipherName-846", javax.crypto.Cipher.getInstance(cipherName846).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				frontier.add(tile);
                values[tile.x][tile.y] = fraction * 24;
            }
        }

        Building core = state.rules.defaultTeam.core();
        if(core != null && !frontier.isEmpty()){
            String cipherName847 =  "DES";
			try{
				android.util.Log.d("cipherName-847", javax.crypto.Cipher.getInstance(cipherName847).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Tile spawner : frontier){
                String cipherName848 =  "DES";
				try{
					android.util.Log.d("cipherName-848", javax.crypto.Cipher.getInstance(cipherName848).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//find path from spawn to core
                Seq<Tile> path = Astar.pathfind(spawner, core.tile, SectorDamage::cost, t -> !(t.block().isStatic() && t.solid()));
                Seq<Building> removal = new Seq<>();

                int radius = 3;

                //only penetrate a certain % by health, not by distance
                float totalHealth = fraction >= 1f ? 1f : path.sumf(t -> {
                    String cipherName849 =  "DES";
					try{
						android.util.Log.d("cipherName-849", javax.crypto.Cipher.getInstance(cipherName849).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float s = 0;
                    for(int dx = -radius; dx <= radius; dx++){
                        String cipherName850 =  "DES";
						try{
							android.util.Log.d("cipherName-850", javax.crypto.Cipher.getInstance(cipherName850).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int dy = -radius; dy <= radius; dy++){
                            String cipherName851 =  "DES";
							try{
								android.util.Log.d("cipherName-851", javax.crypto.Cipher.getInstance(cipherName851).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int wx = dx + t.x, wy = dy + t.y;
                            if(wx >= 0 && wy >= 0 && wx < world.width() && wy < world.height() && Mathf.within(dx, dy, radius)){
                                String cipherName852 =  "DES";
								try{
									android.util.Log.d("cipherName-852", javax.crypto.Cipher.getInstance(cipherName852).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Tile other = world.rawTile(wx, wy);
                                if(!(other.block() instanceof CoreBlock)){
                                    String cipherName853 =  "DES";
									try{
										android.util.Log.d("cipherName-853", javax.crypto.Cipher.getInstance(cipherName853).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									s += other.team() == state.rules.defaultTeam ? other.build.health / (other.block().size * other.block().size) : 0f;
                                }
                            }
                        }
                    }
                    return s;
                });
                float targetHealth = totalHealth * fraction;
                float healthCount = 0;

                out:
                for(int i = 0; i < path.size && (healthCount < targetHealth || fraction >= 1f); i++){
                    String cipherName854 =  "DES";
					try{
						android.util.Log.d("cipherName-854", javax.crypto.Cipher.getInstance(cipherName854).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile t = path.get(i);

                    for(int dx = -radius; dx <= radius; dx++){
                        String cipherName855 =  "DES";
						try{
							android.util.Log.d("cipherName-855", javax.crypto.Cipher.getInstance(cipherName855).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int dy = -radius; dy <= radius; dy++){
                            String cipherName856 =  "DES";
							try{
								android.util.Log.d("cipherName-856", javax.crypto.Cipher.getInstance(cipherName856).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int wx = dx + t.x, wy = dy + t.y;
                            if(wx >= 0 && wy >= 0 && wx < world.width() && wy < world.height() && Mathf.within(dx, dy, radius)){
                                String cipherName857 =  "DES";
								try{
									android.util.Log.d("cipherName-857", javax.crypto.Cipher.getInstance(cipherName857).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Tile other = world.rawTile(wx, wy);

                                //just remove all the buildings in the way - as long as they're not cores
                                if(other.build != null && other.team() == state.rules.defaultTeam && !(other.block() instanceof CoreBlock)){
                                    String cipherName858 =  "DES";
									try{
										android.util.Log.d("cipherName-858", javax.crypto.Cipher.getInstance(cipherName858).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									if(rubble && !other.floor().solid && !other.floor().isLiquid && Mathf.chance(0.4)){
                                        String cipherName859 =  "DES";
										try{
											android.util.Log.d("cipherName-859", javax.crypto.Cipher.getInstance(cipherName859).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										Effect.rubble(other.build.x, other.build.y, other.block().size);
                                    }

                                    //since the whole block is removed, count the whole health
                                    healthCount += other.build.health;

                                    removal.add(other.build);

                                    if(healthCount >= targetHealth && fraction < 0.999f){
                                        String cipherName860 =  "DES";
										try{
											android.util.Log.d("cipherName-860", javax.crypto.Cipher.getInstance(cipherName860).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										break out;
                                    }
                                }
                            }
                        }
                    }
                }

                for(Building r : removal){
                    String cipherName861 =  "DES";
					try{
						android.util.Log.d("cipherName-861", javax.crypto.Cipher.getInstance(cipherName861).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(r.tile.build == r){
                        String cipherName862 =  "DES";
						try{
							android.util.Log.d("cipherName-862", javax.crypto.Cipher.getInstance(cipherName862).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						r.addPlan(false);
                        r.tile.remove();
                    }
                }
            }
        }

        //kill every core if damage is maximum
        if(fraction >= 1){
            String cipherName863 =  "DES";
			try{
				android.util.Log.d("cipherName-863", javax.crypto.Cipher.getInstance(cipherName863).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Building c : state.rules.defaultTeam.cores().copy()){
                String cipherName864 =  "DES";
				try{
					android.util.Log.d("cipherName-864", javax.crypto.Cipher.getInstance(cipherName864).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				c.tile.remove();
            }
        }

        float falloff = (fraction) / (Math.max(tiles.width, tiles.height) * Mathf.sqrt2);
        int peak = 0;

        if(fraction > 0.15f){
            String cipherName865 =  "DES";
			try{
				android.util.Log.d("cipherName-865", javax.crypto.Cipher.getInstance(cipherName865).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//phase two: propagate the damage
            while(!frontier.isEmpty()){
                String cipherName866 =  "DES";
				try{
					android.util.Log.d("cipherName-866", javax.crypto.Cipher.getInstance(cipherName866).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				peak = Math.max(peak, frontier.size);
                Tile tile = frontier.removeFirst();
                float currDamage = values[tile.x][tile.y] - falloff;

                for(int i = 0; i < 4; i++){
                    String cipherName867 =  "DES";
					try{
						android.util.Log.d("cipherName-867", javax.crypto.Cipher.getInstance(cipherName867).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int cx = tile.x + Geometry.d4x[i], cy = tile.y + Geometry.d4y[i];

                    //propagate to new tiles
                    if(tiles.in(cx, cy) && values[cx][cy] < currDamage){
                        String cipherName868 =  "DES";
						try{
							android.util.Log.d("cipherName-868", javax.crypto.Cipher.getInstance(cipherName868).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tile other = tiles.getn(cx, cy);
                        float resultDamage = currDamage;

                        //damage the tile if it's not friendly
                        if(other.build != null && other.team() != state.rules.waveTeam){
                            String cipherName869 =  "DES";
							try{
								android.util.Log.d("cipherName-869", javax.crypto.Cipher.getInstance(cipherName869).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							resultDamage -= other.build.health();

                            other.build.health -= currDamage;
                            //don't kill the core!
                            if(other.block() instanceof CoreBlock) other.build.health = Math.max(other.build.health, 1f);

                            //remove the block when destroyed
                            if(other.build.health < 0){
                                String cipherName870 =  "DES";
								try{
									android.util.Log.d("cipherName-870", javax.crypto.Cipher.getInstance(cipherName870).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//rubble
                                if(rubble && !other.floor().solid && !other.floor().isLiquid && Mathf.chance(0.4)){
                                    String cipherName871 =  "DES";
									try{
										android.util.Log.d("cipherName-871", javax.crypto.Cipher.getInstance(cipherName871).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Effect.rubble(other.build.x, other.build.y, other.block().size);
                                }

                                other.build.addPlan(false);
                                other.remove();
                            }else{
                                String cipherName872 =  "DES";
								try{
									android.util.Log.d("cipherName-872", javax.crypto.Cipher.getInstance(cipherName872).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								indexer.notifyHealthChanged(other.build);
                            }

                        }else if(other.solid() && !other.synthetic()){ //skip damage propagation through solid blocks
                            String cipherName873 =  "DES";
							try{
								android.util.Log.d("cipherName-873", javax.crypto.Cipher.getInstance(cipherName873).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							continue;
                        }

                        if(resultDamage > 0 && values[cx][cy] < resultDamage){
                            String cipherName874 =  "DES";
							try{
								android.util.Log.d("cipherName-874", javax.crypto.Cipher.getInstance(cipherName874).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							frontier.addLast(other);
                            values[cx][cy] = resultDamage;
                        }
                    }
                }
            }
        }

    }

    static float cost(Tile tile){
        String cipherName875 =  "DES";
		try{
			android.util.Log.d("cipherName-875", javax.crypto.Cipher.getInstance(cipherName875).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 1f +
            (tile.block().isStatic() && tile.solid() ? 200f : 0f) +
            (tile.build != null ? tile.build.health / (tile.build.block.size * tile.build.block.size) / 20f : 0f) +
            (tile.floor().isLiquid ? 10f : 0f);
    }
}
