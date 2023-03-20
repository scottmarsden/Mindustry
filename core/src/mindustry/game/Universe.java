package mindustry.game;

import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.game.SectorInfo.*;
import mindustry.gen.*;
import mindustry.maps.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.*;

/** Updates and handles state of the campaign universe. Has no relevance to other gamemodes. */
public class Universe{
    private int seconds;
    private int netSeconds;
    private float secondCounter;
    private int turn;
    private float turnCounter;

    private @Nullable Schematic lastLoadout;
    private ItemSeq lastLaunchResources = new ItemSeq();

    public Universe(){
        String cipherName12459 =  "DES";
		try{
			android.util.Log.d("cipherName-12459", javax.crypto.Cipher.getInstance(cipherName12459).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		load();

        //update base coverage on capture
        Events.on(SectorCaptureEvent.class, e -> {
            String cipherName12460 =  "DES";
			try{
				android.util.Log.d("cipherName-12460", javax.crypto.Cipher.getInstance(cipherName12460).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!net.client() && state.isCampaign()){
                String cipherName12461 =  "DES";
				try{
					android.util.Log.d("cipherName-12461", javax.crypto.Cipher.getInstance(cipherName12461).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.getSector().planet.updateBaseCoverage();
            }
        });
    }

    /** Update regardless of whether the player is in the campaign. */
    public void updateGlobal(){
        String cipherName12462 =  "DES";
		try{
			android.util.Log.d("cipherName-12462", javax.crypto.Cipher.getInstance(cipherName12462).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//currently only updates one solar system
        updatePlanet(Planets.sun);
    }

    public int turn(){
        String cipherName12463 =  "DES";
		try{
			android.util.Log.d("cipherName-12463", javax.crypto.Cipher.getInstance(cipherName12463).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return turn;
    }

    private void updatePlanet(Planet planet){
        String cipherName12464 =  "DES";
		try{
			android.util.Log.d("cipherName-12464", javax.crypto.Cipher.getInstance(cipherName12464).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		planet.position.setZero();
        planet.addParentOffset(planet.position);
        if(planet.parent != null){
            String cipherName12465 =  "DES";
			try{
				android.util.Log.d("cipherName-12465", javax.crypto.Cipher.getInstance(cipherName12465).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			planet.position.add(planet.parent.position);
        }
        for(Planet child : planet.children){
            String cipherName12466 =  "DES";
			try{
				android.util.Log.d("cipherName-12466", javax.crypto.Cipher.getInstance(cipherName12466).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updatePlanet(child);
        }
    }

    /** Update planet rotations, global time and relevant state. */
    public void update(){

        String cipherName12467 =  "DES";
		try{
			android.util.Log.d("cipherName-12467", javax.crypto.Cipher.getInstance(cipherName12467).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//only update time when not in multiplayer
        if(!net.client()){
            String cipherName12468 =  "DES";
			try{
				android.util.Log.d("cipherName-12468", javax.crypto.Cipher.getInstance(cipherName12468).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			secondCounter += Time.delta / 60f;
            turnCounter += Time.delta;

            //auto-run turns
            if(turnCounter >= turnDuration){
                String cipherName12469 =  "DES";
				try{
					android.util.Log.d("cipherName-12469", javax.crypto.Cipher.getInstance(cipherName12469).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				turnCounter = 0;
                runTurn();
            }

            if(secondCounter >= 1){
                String cipherName12470 =  "DES";
				try{
					android.util.Log.d("cipherName-12470", javax.crypto.Cipher.getInstance(cipherName12470).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				seconds += (int)secondCounter;
                secondCounter %= 1f;

                //save every few seconds
                if(seconds % 10 == 1){
                    String cipherName12471 =  "DES";
					try{
						android.util.Log.d("cipherName-12471", javax.crypto.Cipher.getInstance(cipherName12471).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					save();
                }
            }
        }

        if(state.hasSector() && state.getSector().planet.updateLighting){
            String cipherName12472 =  "DES";
			try{
				android.util.Log.d("cipherName-12472", javax.crypto.Cipher.getInstance(cipherName12472).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean disable = state.getSector().preset != null && state.getSector().preset.noLighting;
            var planet = state.getSector().planet;
            //update sector light
            float light = state.getSector().getLight();
            float alpha = disable ? 1f : Mathf.clamp(Mathf.map(light, planet.lightSrcFrom, planet.lightSrcTo, planet.lightDstFrom, planet.lightDstTo));

            //assign and map so darkness is not 100% dark
            state.rules.ambientLight.a = 1f - alpha;
            state.rules.lighting = !Mathf.equal(alpha, 1f);
        }
    }

    public void clearLoadoutInfo(){
        String cipherName12473 =  "DES";
		try{
			android.util.Log.d("cipherName-12473", javax.crypto.Cipher.getInstance(cipherName12473).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		lastLoadout = null;
        lastLaunchResources = new ItemSeq();
        Core.settings.remove("launch-resources-seq");
        Core.settings.remove("lastloadout-core-shard");
        Core.settings.remove("lastloadout-core-nucleus");
        Core.settings.remove("lastloadout-core-foundation");
    }

    public ItemSeq getLaunchResources(){
        String cipherName12474 =  "DES";
		try{
			android.util.Log.d("cipherName-12474", javax.crypto.Cipher.getInstance(cipherName12474).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		lastLaunchResources = Core.settings.getJson("launch-resources-seq", ItemSeq.class, ItemSeq::new);
        return lastLaunchResources;
    }

    public void updateLaunchResources(ItemSeq stacks){
        String cipherName12475 =  "DES";
		try{
			android.util.Log.d("cipherName-12475", javax.crypto.Cipher.getInstance(cipherName12475).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.lastLaunchResources = stacks;
        Core.settings.putJson("launch-resources-seq", lastLaunchResources);
    }

    /** Updates selected loadout for future deployment. */
    public void updateLoadout(CoreBlock block, Schematic schem){
        String cipherName12476 =  "DES";
		try{
			android.util.Log.d("cipherName-12476", javax.crypto.Cipher.getInstance(cipherName12476).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.settings.put("lastloadout-" + block.name, schem.file == null ? "" : schem.file.nameWithoutExtension());
        lastLoadout = schem;
    }

    public Schematic getLastLoadout(){
        String cipherName12477 =  "DES";
		try{
			android.util.Log.d("cipherName-12477", javax.crypto.Cipher.getInstance(cipherName12477).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(lastLoadout == null) lastLoadout = state.rules.sector == null || state.rules.sector.planet.generator == null ? Loadouts.basicShard : state.rules.sector.planet.generator.defaultLoadout;
        return lastLoadout;
    }

    /** @return the last selected loadout for this specific core type. */
    @Nullable
    public Schematic getLoadout(CoreBlock core){
        String cipherName12478 =  "DES";
		try{
			android.util.Log.d("cipherName-12478", javax.crypto.Cipher.getInstance(cipherName12478).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//for tools - schem
        if(schematics == null) return Loadouts.basicShard;

        //find last used loadout file name
        String file = Core.settings.getString("lastloadout-" + core.name, "");

        //use default (first) schematic if not found
        Seq<Schematic> all = schematics.getLoadouts(core);
        Schematic schem = all.find(s -> s.file != null && s.file.nameWithoutExtension().equals(file));

        return schem == null ? all.any() ? all.first() : null : schem;
    }

    /** Runs possible events. Resets event counter. */
    public void runTurn(){
        String cipherName12479 =  "DES";
		try{
			android.util.Log.d("cipherName-12479", javax.crypto.Cipher.getInstance(cipherName12479).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		turn++;

        int newSecondsPassed = (int)(turnDuration / 60);
        Planet current = state.getPlanet();

        //update relevant sectors
        for(Planet planet : content.planets()){

            String cipherName12480 =  "DES";
			try{
				android.util.Log.d("cipherName-12480", javax.crypto.Cipher.getInstance(cipherName12480).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//planets with different wave simulation status are not updated
            if(current != null && current.allowWaveSimulation != planet.allowWaveSimulation){
                String cipherName12481 =  "DES";
				try{
					android.util.Log.d("cipherName-12481", javax.crypto.Cipher.getInstance(cipherName12481).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				continue;
            }

            //first pass: clear import stats
            for(Sector sector : planet.sectors){
                String cipherName12482 =  "DES";
				try{
					android.util.Log.d("cipherName-12482", javax.crypto.Cipher.getInstance(cipherName12482).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(sector.hasBase() && !sector.isBeingPlayed()){
                    String cipherName12483 =  "DES";
					try{
						android.util.Log.d("cipherName-12483", javax.crypto.Cipher.getInstance(cipherName12483).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					sector.info.lastImported.clear();
                }
            }

            //second pass: update export & import statistics
            for(Sector sector : planet.sectors){
                String cipherName12484 =  "DES";
				try{
					android.util.Log.d("cipherName-12484", javax.crypto.Cipher.getInstance(cipherName12484).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(sector.hasBase() && !sector.isBeingPlayed()){

                    String cipherName12485 =  "DES";
					try{
						android.util.Log.d("cipherName-12485", javax.crypto.Cipher.getInstance(cipherName12485).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//export to another sector
                    if(sector.info.destination != null){
                        String cipherName12486 =  "DES";
						try{
							android.util.Log.d("cipherName-12486", javax.crypto.Cipher.getInstance(cipherName12486).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Sector to = sector.info.destination;
                        if(to.hasBase() && to.planet == planet){
                            String cipherName12487 =  "DES";
							try{
								android.util.Log.d("cipherName-12487", javax.crypto.Cipher.getInstance(cipherName12487).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ItemSeq items = new ItemSeq();
                            //calculated exported items to this sector
                            sector.info.export.each((item, stat) -> items.add(item, (int)(stat.mean * newSecondsPassed * sector.getProductionScale())));
                            to.addItems(items);
                            to.info.lastImported.add(items);
                        }
                    }
                }
            }

            //third pass: everything else
            for(Sector sector : planet.sectors){
                String cipherName12488 =  "DES";
				try{
					android.util.Log.d("cipherName-12488", javax.crypto.Cipher.getInstance(cipherName12488).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(sector.hasBase()){

                    String cipherName12489 =  "DES";
					try{
						android.util.Log.d("cipherName-12489", javax.crypto.Cipher.getInstance(cipherName12489).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//if it is being attacked, capture time is 0; otherwise, increment the timer
                    if(sector.isAttacked()){
                        String cipherName12490 =  "DES";
						try{
							android.util.Log.d("cipherName-12490", javax.crypto.Cipher.getInstance(cipherName12490).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						sector.info.minutesCaptured = 0;
                    }else{
                        String cipherName12491 =  "DES";
						try{
							android.util.Log.d("cipherName-12491", javax.crypto.Cipher.getInstance(cipherName12491).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						sector.info.minutesCaptured += turnDuration / 60 / 60;
                    }

                    //increment seconds passed for this sector by the time that just passed with this turn
                    if(!sector.isBeingPlayed()){

                        String cipherName12492 =  "DES";
						try{
							android.util.Log.d("cipherName-12492", javax.crypto.Cipher.getInstance(cipherName12492).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//increment time if attacked
                        if(sector.isAttacked()){
                            String cipherName12493 =  "DES";
							try{
								android.util.Log.d("cipherName-12493", javax.crypto.Cipher.getInstance(cipherName12493).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							sector.info.secondsPassed += turnDuration/60f;
                        }

                        int wavesPassed = (int)(sector.info.secondsPassed*60f / sector.info.waveSpacing);
                        boolean attacked = sector.info.waves && sector.planet.allowWaveSimulation;

                        if(attacked){
                            String cipherName12494 =  "DES";
							try{
								android.util.Log.d("cipherName-12494", javax.crypto.Cipher.getInstance(cipherName12494).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							sector.info.wavesPassed = wavesPassed;
                        }

                        float damage = attacked ? SectorDamage.getDamage(sector.info) : 0f;

                        //damage never goes down until the player visits the sector, so use max
                        sector.info.damage = Math.max(sector.info.damage, damage);

                        //check if the sector has been attacked too many times...
                        if(attacked && damage >= 0.999f){
                            String cipherName12495 =  "DES";
							try{
								android.util.Log.d("cipherName-12495", javax.crypto.Cipher.getInstance(cipherName12495).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//fire event for losing the sector
                            Events.fire(new SectorLoseEvent(sector));

                            //sector is dead.
                            sector.info.items.clear();
                            sector.info.damage = 1f;
                            sector.info.hasCore = false;
                            sector.info.production.clear();
                        }else if(attacked && wavesPassed > 0 && sector.info.winWave > 1 && sector.info.wave + wavesPassed >= sector.info.winWave && !sector.hasEnemyBase()){
                            String cipherName12496 =  "DES";
							try{
								android.util.Log.d("cipherName-12496", javax.crypto.Cipher.getInstance(cipherName12496).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//autocapture the sector
                            sector.info.waves = false;
                            boolean was = sector.info.wasCaptured;
                            sector.info.wasCaptured = true;

                            //fire the event
                            Events.fire(new SectorCaptureEvent(sector, !was));
                        }

                        float scl = sector.getProductionScale();

                        //add production, making sure that it's capped
                        sector.info.production.each((item, stat) -> sector.info.items.add(item, Math.min((int)(stat.mean * newSecondsPassed * scl), sector.info.storageCapacity - sector.info.items.get(item))));

                        sector.info.export.each((item, stat) -> {
                            String cipherName12497 =  "DES";
							try{
								android.util.Log.d("cipherName-12497", javax.crypto.Cipher.getInstance(cipherName12497).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(sector.info.items.get(item) <= 0 && sector.info.production.get(item, ExportStat::new).mean < 0 && stat.mean > 0){
                                String cipherName12498 =  "DES";
								try{
									android.util.Log.d("cipherName-12498", javax.crypto.Cipher.getInstance(cipherName12498).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//cap export by import when production is negative.
                                stat.mean = Math.min(sector.info.lastImported.get(item) / (float)newSecondsPassed, stat.mean);
                            }
                        });

                        //prevent negative values with unloaders
                        sector.info.items.checkNegative();

                        sector.saveInfo();
                    }

                    //queue random invasions
                    if(!sector.isAttacked() && sector.planet.allowSectorInvasion && sector.info.minutesCaptured > invasionGracePeriod && sector.info.hasSpawns){
                        String cipherName12499 =  "DES";
						try{
							android.util.Log.d("cipherName-12499", javax.crypto.Cipher.getInstance(cipherName12499).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int count = sector.near().count(Sector::hasEnemyBase);

                        //invasion chance depends on # of nearby bases
                        if(count > 0 && Mathf.chance(baseInvasionChance * (0.8f + (count - 1) * 0.3f))){
                            String cipherName12500 =  "DES";
							try{
								android.util.Log.d("cipherName-12500", javax.crypto.Cipher.getInstance(cipherName12500).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int waveMax = Math.max(sector.info.winWave, sector.isBeingPlayed() ? state.wave : sector.info.wave + sector.info.wavesPassed) + Mathf.random(2, 4) * 5;

                            //assign invasion-related things
                            if(sector.isBeingPlayed()){
                                String cipherName12501 =  "DES";
								try{
									android.util.Log.d("cipherName-12501", javax.crypto.Cipher.getInstance(cipherName12501).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								state.rules.winWave = waveMax;
                                state.rules.waves = true;
                                state.rules.attackMode = false;
                                //update rules in multiplayer
                                if(net.server()){
                                    String cipherName12502 =  "DES";
									try{
										android.util.Log.d("cipherName-12502", javax.crypto.Cipher.getInstance(cipherName12502).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Call.setRules(state.rules);
                                }
                            }else{
                                String cipherName12503 =  "DES";
								try{
									android.util.Log.d("cipherName-12503", javax.crypto.Cipher.getInstance(cipherName12503).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								sector.info.winWave = waveMax;
                                sector.info.waves = true;
                                sector.info.attack = false;
                                sector.saveInfo();
                            }

                            Events.fire(new SectorInvasionEvent(sector));
                        }
                    }
                }
            }
        }

        Events.fire(new TurnEvent());

        save();
    }

    public void updateNetSeconds(int value){
        String cipherName12504 =  "DES";
		try{
			android.util.Log.d("cipherName-12504", javax.crypto.Cipher.getInstance(cipherName12504).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		netSeconds = value;
    }

    public float secondsMod(float mod, float scale){
        String cipherName12505 =  "DES";
		try{
			android.util.Log.d("cipherName-12505", javax.crypto.Cipher.getInstance(cipherName12505).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (seconds() / scale) % mod;
    }

    public int seconds(){
        String cipherName12506 =  "DES";
		try{
			android.util.Log.d("cipherName-12506", javax.crypto.Cipher.getInstance(cipherName12506).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//use networked seconds when playing as client
        return net.client() ? netSeconds : seconds;
    }

    public float secondsf(){
        String cipherName12507 =  "DES";
		try{
			android.util.Log.d("cipherName-12507", javax.crypto.Cipher.getInstance(cipherName12507).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return seconds() + secondCounter;
    }

    private void save(){
        String cipherName12508 =  "DES";
		try{
			android.util.Log.d("cipherName-12508", javax.crypto.Cipher.getInstance(cipherName12508).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.settings.put("utimei", seconds);
        Core.settings.put("turn", turn);
    }

    private void load(){
        String cipherName12509 =  "DES";
		try{
			android.util.Log.d("cipherName-12509", javax.crypto.Cipher.getInstance(cipherName12509).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		seconds = Core.settings.getInt("utimei");
        turn = Core.settings.getInt("turn");
    }

}
