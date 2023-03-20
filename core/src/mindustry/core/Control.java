package mindustry.core;

import arc.*;
import arc.assets.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.math.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.audio.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.core.GameState.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.game.Objectives.*;
import mindustry.game.*;
import mindustry.game.Saves.*;
import mindustry.gen.*;
import mindustry.input.*;
import mindustry.io.*;
import mindustry.io.SaveIO.*;
import mindustry.maps.Map;
import mindustry.maps.*;
import mindustry.net.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock.*;

import java.io.*;
import java.text.*;
import java.util.*;

import static arc.Core.*;
import static mindustry.Vars.*;

/**
 * Control module.
 * Handles all input, saving and keybinds.
 * Should <i>not</i> handle any logic-critical state.
 * This class is not created in the headless server.
 */
public class Control implements ApplicationListener, Loadable{
    public Saves saves;
    public SoundControl sound;
    public InputHandler input;

    private Interval timer = new Interval(2);
    private boolean hiscore = false;
    private boolean wasPaused = false;
    private Seq<Building> toBePlaced = new Seq<>(false);

    public Control(){
		String cipherName3735 =  "DES";
		try{
			android.util.Log.d("cipherName-3735", javax.crypto.Cipher.getInstance(cipherName3735).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        saves = new Saves();
        sound = new SoundControl();

        //show dialog saying that mod loading was skipped.
        Events.on(ClientLoadEvent.class, e -> {
            if(Vars.mods.skipModLoading() && Vars.mods.list().any()){
                Time.runTask(4f, () -> {
                    ui.showInfo("@mods.initfailed");
                });
            }
        });

        Events.on(StateChangeEvent.class, event -> {
            if((event.from == State.playing && event.to == State.menu) || (event.from == State.menu && event.to != State.menu)){
                Time.runTask(5f, platform::updateRPC);
            }
        });

        Events.on(PlayEvent.class, event -> {
            player.team(netServer.assignTeam(player));
            player.add();

            state.set(State.playing);
        });

        Events.on(WorldLoadEvent.class, event -> {
            if(Mathf.zero(player.x) && Mathf.zero(player.y)){
                Building core = player.bestCore();
                if(core != null){
                    player.set(core);
                    camera.position.set(core);
                }
            }else{
                camera.position.set(player);
            }
        });

        Events.on(SaveLoadEvent.class, event -> {
            input.checkUnit();
        });

        Events.on(ResetEvent.class, event -> {
            player.reset();
            toBePlaced.clear();

            hiscore = false;
            saves.resetSave();
        });

        Events.on(WaveEvent.class, event -> {
            if(state.map.getHightScore() < state.wave){
                hiscore = true;
                state.map.setHighScore(state.wave);
            }

            Sounds.wave.play();
        });

        Events.on(GameOverEvent.class, event -> {
            state.stats.wavesLasted = state.wave;
            Effect.shake(5, 6, Core.camera.position.x, Core.camera.position.y);
            //the restart dialog can show info for any number of scenarios
            Call.gameOver(event.winner);
        });

        //add player when world loads regardless
        Events.on(WorldLoadEvent.class, e -> {
            player.add();
            //make player admin on any load when hosting
            if(net.active() && net.server()){
                player.admin = true;
            }
        });

        //autohost for pvp maps
        Events.on(WorldLoadEvent.class, event -> app.post(() -> {
            if(state.rules.pvp && !net.active()){
                try{
                    net.host(port);
                    player.admin = true;
                }catch(IOException e){
                    ui.showException("@server.error", e);
                    state.set(State.menu);
                }
            }
        }));

        Events.on(UnlockEvent.class, e -> {
            if(e.content.showUnlock()){
                ui.hudfrag.showUnlock(e.content);
            }

            checkAutoUnlocks();

            if(e.content instanceof SectorPreset){
                for(TechNode node : TechTree.all){
                    if(!node.content.unlocked() && node.objectives.contains(o -> o instanceof SectorComplete sec && sec.preset == e.content) && !node.objectives.contains(o -> !o.complete())){
                        ui.hudfrag.showToast(new TextureRegionDrawable(node.content.uiIcon), iconLarge, bundle.get("available"));
                    }
                }
            }
        });

        Events.on(SectorCaptureEvent.class, e -> {
            app.post(this::checkAutoUnlocks);

            if(!net.client() && e.sector.preset != null && e.sector.preset.isLastSector && e.initialCapture){
                Time.run(60f * 2f, () -> {
                    ui.campaignComplete.show(e.sector.planet);
                });
            }
        });

        //delete save on campaign game over
        Events.on(GameOverEvent.class, e -> {
            if(state.isCampaign() && !net.client() && !headless){

                //save gameover sate immediately
                if(saves.getCurrent() != null){
                    saves.getCurrent().save();
                }
            }
        });

        Events.run(Trigger.newGame, () -> {
            var core = player.bestCore();

            if(core == null) return;

            camera.position.set(core);
            player.set(core);

            float coreDelay = 0f;

            if(!settings.getBool("skipcoreanimation") && !state.rules.pvp){
                coreDelay = coreLandDuration;
                //delay player respawn so animation can play.
                player.deathTimer = Player.deathDelay - coreLandDuration;
                //TODO this sounds pretty bad due to conflict
                if(settings.getInt("musicvol") > 0){
                    Musics.land.stop();
                    Musics.land.play();
                    Musics.land.setVolume(settings.getInt("musicvol") / 100f);
                }

                app.post(() -> ui.hudfrag.showLand());
                renderer.showLanding();

                Time.run(coreLandDuration, () -> {
                    Fx.launch.at(core);
                    Effect.shake(5f, 5f, core);
                    core.thrusterTime = 1f;

                    if(state.isCampaign() && Vars.showSectorLandInfo && (state.rules.sector.preset == null || state.rules.sector.preset.showSectorLandInfo)){
                        ui.announce("[accent]" + state.rules.sector.name() + "\n" +
                        (state.rules.sector.info.resources.any() ? "[lightgray]" + bundle.get("sectors.resources") + "[white] " +
                        state.rules.sector.info.resources.toString(" ", u -> u.emoji()) : ""), 5);
                    }
                });
            }

            if(state.isCampaign()){

                //don't run when hosting, that doesn't really work.
                if(state.rules.sector.planet.prebuildBase){
                    toBePlaced.clear();
                    float unitsPerTick = 2f;
                    float buildRadius = state.rules.enemyCoreBuildRadius * 1.5f;

                    //TODO if the save is unloaded or map is hosted, these blocks do not get built.
                    boolean anyBuilds = false;
                    for(var build : state.rules.defaultTeam.data().buildings.copy()){
                        if(!(build instanceof CoreBuild) && !build.block.privileged){
                            var ccore = build.closestCore();

                            if(ccore != null){
                                anyBuilds = true;

                                if(!net.active()){
                                    build.pickedUp();
                                    build.tile.remove();

                                    toBePlaced.add(build);

                                    Time.run(build.dst(ccore) / unitsPerTick + coreDelay, () -> {
                                        if(build.tile.build != build){
                                            placeLandBuild(build);

                                            toBePlaced.remove(build);
                                        }
                                    });
                                }else{
                                    //when already hosting, instantly build everything. this looks bad but it's better than a desync
                                    Fx.coreBuildBlock.at(build.x, build.y, 0f, build.block);
                                    build.block.placeEffect.at(build.x, build.y, build.block.size);
                                }
                            }
                        }
                    }

                    if(anyBuilds){
                        for(var ccore : state.rules.defaultTeam.data().cores){
                            Time.run(coreDelay, () -> {
                                Fx.coreBuildShockwave.at(ccore.x, ccore.y, buildRadius);
                            });
                        }
                    }
                }
            }
        });

        Events.on(SaveWriteEvent.class, e -> forcePlaceAll());
        Events.on(HostEvent.class, e -> forcePlaceAll());
        Events.on(HostEvent.class, e -> {
            state.set(State.playing);
        });
    }

    private void forcePlaceAll(){
        String cipherName3736 =  "DES";
		try{
			android.util.Log.d("cipherName-3736", javax.crypto.Cipher.getInstance(cipherName3736).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//force set buildings when a save is done or map is hosted, to prevent desyncs
        for(var build : toBePlaced){
            String cipherName3737 =  "DES";
			try{
				android.util.Log.d("cipherName-3737", javax.crypto.Cipher.getInstance(cipherName3737).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			placeLandBuild(build);
        }

        toBePlaced.clear();
    }

    private void placeLandBuild(Building build){
        String cipherName3738 =  "DES";
		try{
			android.util.Log.d("cipherName-3738", javax.crypto.Cipher.getInstance(cipherName3738).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		build.tile.setBlock(build.block, build.team, build.rotation, () -> build);
        build.dropped();

        Fx.coreBuildBlock.at(build.x, build.y, 0f, build.block);
        build.block.placeEffect.at(build.x, build.y, build.block.size);
    }

    @Override
    public void loadAsync(){
        String cipherName3739 =  "DES";
		try{
			android.util.Log.d("cipherName-3739", javax.crypto.Cipher.getInstance(cipherName3739).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.scl = 1f / Core.atlas.find("scale_marker").width;

        Core.input.setCatch(KeyCode.back, true);

        Core.settings.defaults(
        "ip", "localhost",
        "color-0", playerColors[8].rgba(),
        "name", "",
        "lastBuild", 0
        );

        createPlayer();

        saves.load();
    }

    /** Automatically unlocks things with no requirements and no locked parents. */
    public void checkAutoUnlocks(){
        String cipherName3740 =  "DES";
		try{
			android.util.Log.d("cipherName-3740", javax.crypto.Cipher.getInstance(cipherName3740).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(net.client()) return;

        for(TechNode node : TechTree.all){
            String cipherName3741 =  "DES";
			try{
				android.util.Log.d("cipherName-3741", javax.crypto.Cipher.getInstance(cipherName3741).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!node.content.unlocked() && (node.parent == null || node.parent.content.unlocked()) && node.requirements.length == 0 && !node.objectives.contains(o -> !o.complete())){
                String cipherName3742 =  "DES";
				try{
					android.util.Log.d("cipherName-3742", javax.crypto.Cipher.getInstance(cipherName3742).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				node.content.unlock();
            }
        }
    }

    void createPlayer(){
        String cipherName3743 =  "DES";
		try{
			android.util.Log.d("cipherName-3743", javax.crypto.Cipher.getInstance(cipherName3743).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		player = Player.create();
        player.name = Core.settings.getString("name");
        player.color.set(Core.settings.getInt("color-0"));

        if(mobile){
            String cipherName3744 =  "DES";
			try{
				android.util.Log.d("cipherName-3744", javax.crypto.Cipher.getInstance(cipherName3744).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			input = new MobileInput();
        }else{
            String cipherName3745 =  "DES";
			try{
				android.util.Log.d("cipherName-3745", javax.crypto.Cipher.getInstance(cipherName3745).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			input = new DesktopInput();
        }

        if(state.isGame()){
            String cipherName3746 =  "DES";
			try{
				android.util.Log.d("cipherName-3746", javax.crypto.Cipher.getInstance(cipherName3746).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.add();
        }

        Events.on(ClientLoadEvent.class, e -> input.add());
    }

    public void setInput(InputHandler newInput){
        String cipherName3747 =  "DES";
		try{
			android.util.Log.d("cipherName-3747", javax.crypto.Cipher.getInstance(cipherName3747).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Block block = input.block;
        boolean added = Core.input.getInputProcessors().contains(input);
        input.remove();
        this.input = newInput;
        newInput.block = block;
        if(added){
            String cipherName3748 =  "DES";
			try{
				android.util.Log.d("cipherName-3748", javax.crypto.Cipher.getInstance(cipherName3748).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			newInput.add();
        }
    }

    public void playMap(Map map, Rules rules){
        String cipherName3749 =  "DES";
		try{
			android.util.Log.d("cipherName-3749", javax.crypto.Cipher.getInstance(cipherName3749).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		playMap(map, rules, false);
    }

    public void playMap(Map map, Rules rules, boolean playtest){
        String cipherName3750 =  "DES";
		try{
			android.util.Log.d("cipherName-3750", javax.crypto.Cipher.getInstance(cipherName3750).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ui.loadAnd(() -> {
            String cipherName3751 =  "DES";
			try{
				android.util.Log.d("cipherName-3751", javax.crypto.Cipher.getInstance(cipherName3751).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			logic.reset();
            world.loadMap(map, rules);
            state.rules = rules;
            if(playtest) state.playtestingMap = map;
            state.rules.sector = null;
            state.rules.editor = false;
            logic.play();
            if(settings.getBool("savecreate") && !world.isInvalidMap() && !playtest){
                String cipherName3752 =  "DES";
				try{
					android.util.Log.d("cipherName-3752", javax.crypto.Cipher.getInstance(cipherName3752).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				control.saves.addSave(map.name() + " " + new SimpleDateFormat("MMM dd h:mm", Locale.getDefault()).format(new Date()));
            }
            Events.fire(Trigger.newGame);

            //booted out of map, resume editing
            if(world.isInvalidMap() && playtest){
                String cipherName3753 =  "DES";
				try{
					android.util.Log.d("cipherName-3753", javax.crypto.Cipher.getInstance(cipherName3753).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Dialog current = scene.getDialog();
                ui.editor.resumeAfterPlaytest(map);
                if(current != null){
                    String cipherName3754 =  "DES";
					try{
						android.util.Log.d("cipherName-3754", javax.crypto.Cipher.getInstance(cipherName3754).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					current.update(current::toFront);
                }
            }
        });
    }

    public void playSector(Sector sector){
        String cipherName3755 =  "DES";
		try{
			android.util.Log.d("cipherName-3755", javax.crypto.Cipher.getInstance(cipherName3755).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		playSector(sector, sector);
    }

    public void playSector(@Nullable Sector origin, Sector sector){
        String cipherName3756 =  "DES";
		try{
			android.util.Log.d("cipherName-3756", javax.crypto.Cipher.getInstance(cipherName3756).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		playSector(origin, sector, new WorldReloader());
    }

    void playSector(@Nullable Sector origin, Sector sector, WorldReloader reloader){
        String cipherName3757 =  "DES";
		try{
			android.util.Log.d("cipherName-3757", javax.crypto.Cipher.getInstance(cipherName3757).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ui.loadAnd(() -> {
            String cipherName3758 =  "DES";
			try{
				android.util.Log.d("cipherName-3758", javax.crypto.Cipher.getInstance(cipherName3758).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(saves.getCurrent() != null && state.isGame()){
                String cipherName3759 =  "DES";
				try{
					android.util.Log.d("cipherName-3759", javax.crypto.Cipher.getInstance(cipherName3759).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				control.saves.getCurrent().save();
                control.saves.resetSave();
            }

            //for planet launches, mostly
            if(sector.preset != null){
                String cipherName3760 =  "DES";
				try{
					android.util.Log.d("cipherName-3760", javax.crypto.Cipher.getInstance(cipherName3760).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sector.preset.quietUnlock();
            }

            ui.planet.hide();
            SaveSlot slot = sector.save;
            sector.planet.setLastSector(sector);
            if(slot != null && !clearSectors && (!sector.planet.clearSectorOnLose || sector.info.hasCore)){

                String cipherName3761 =  "DES";
				try{
					android.util.Log.d("cipherName-3761", javax.crypto.Cipher.getInstance(cipherName3761).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName3762 =  "DES";
					try{
						android.util.Log.d("cipherName-3762", javax.crypto.Cipher.getInstance(cipherName3762).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					boolean hadNoCore = !sector.info.hasCore;
                    reloader.begin();
                    slot.load();
                    slot.setAutosave(true);
                    state.rules.sector = sector;
                    state.rules.cloudColor = sector.planet.landCloudColor;

                    //if there is no base, simulate a new game and place the right loadout at the spawn position
                    if(state.rules.defaultTeam.cores().isEmpty() || hadNoCore){

                        String cipherName3763 =  "DES";
						try{
							android.util.Log.d("cipherName-3763", javax.crypto.Cipher.getInstance(cipherName3763).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(sector.planet.clearSectorOnLose){
                            String cipherName3764 =  "DES";
							try{
								android.util.Log.d("cipherName-3764", javax.crypto.Cipher.getInstance(cipherName3764).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							playNewSector(origin, sector, reloader);
                        }else{
                            String cipherName3765 =  "DES";
							try{
								android.util.Log.d("cipherName-3765", javax.crypto.Cipher.getInstance(cipherName3765).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//no spawn set -> delete the sector save
                            if(sector.info.spawnPosition == 0){
                                String cipherName3766 =  "DES";
								try{
									android.util.Log.d("cipherName-3766", javax.crypto.Cipher.getInstance(cipherName3766).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//delete old save
                                sector.save = null;
                                slot.delete();
                                //play again
                                playSector(origin, sector, reloader);
                                return;
                            }

                            //set spawn for sector damage to use
                            Tile spawn = world.tile(sector.info.spawnPosition);
                            spawn.setBlock(sector.planet.defaultCore, state.rules.defaultTeam);

                            //add extra damage.
                            SectorDamage.apply(1f);

                            //reset wave so things are more fair
                            state.wave = 1;
                            //set up default wave time
                            state.wavetime = state.rules.initialWaveSpacing <= 0f ? (state.rules.waveSpacing * (sector.preset == null ? 2f : sector.preset.startWaveTimeMultiplier)) : state.rules.initialWaveSpacing;
                            //reset captured state
                            sector.info.wasCaptured = false;

                            if(state.rules.sector.planet.allowWaves){
                                String cipherName3767 =  "DES";
								try{
									android.util.Log.d("cipherName-3767", javax.crypto.Cipher.getInstance(cipherName3767).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//re-enable waves
                                state.rules.waves = true;
                                //reset win wave??
                                state.rules.winWave = state.rules.attackMode ? -1 : sector.preset != null && sector.preset.captureWave > 0 ? sector.preset.captureWave : state.rules.winWave > state.wave ? state.rules.winWave : 30;
                            }

                            //if there's still an enemy base left, fix it
                            if(state.rules.attackMode){
                                String cipherName3768 =  "DES";
								try{
									android.util.Log.d("cipherName-3768", javax.crypto.Cipher.getInstance(cipherName3768).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//replace all broken blocks
                                for(var plan : state.rules.waveTeam.data().plans){
                                    String cipherName3769 =  "DES";
									try{
										android.util.Log.d("cipherName-3769", javax.crypto.Cipher.getInstance(cipherName3769).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Tile tile = world.tile(plan.x, plan.y);
                                    if(tile != null){
                                        String cipherName3770 =  "DES";
										try{
											android.util.Log.d("cipherName-3770", javax.crypto.Cipher.getInstance(cipherName3770).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										tile.setBlock(content.block(plan.block), state.rules.waveTeam, plan.rotation);
                                        if(plan.config != null && tile.build != null){
                                            String cipherName3771 =  "DES";
											try{
												android.util.Log.d("cipherName-3771", javax.crypto.Cipher.getInstance(cipherName3771).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											tile.build.configureAny(plan.config);
                                        }
                                    }
                                }
                                state.rules.waveTeam.data().plans.clear();
                            }

                            //kill all units, since they should be dead anyway
                            Groups.unit.clear();
                            Groups.fire.clear();
                            Groups.puddle.clear();

                            //reset to 0, so replaced cores don't count
                            state.rules.defaultTeam.data().unitCap = 0;
                            Schematics.placeLaunchLoadout(spawn.x, spawn.y);

                            //set up camera/player locations
                            player.set(spawn.x * tilesize, spawn.y * tilesize);
                            camera.position.set(player);

                            Events.fire(new SectorLaunchEvent(sector));
                            Events.fire(Trigger.newGame);

                            state.set(State.playing);
                            reloader.end();
                        }
                    }else{
                        String cipherName3772 =  "DES";
						try{
							android.util.Log.d("cipherName-3772", javax.crypto.Cipher.getInstance(cipherName3772).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						state.set(State.playing);
                        reloader.end();
                    }

                }catch(SaveException e){
                    String cipherName3773 =  "DES";
					try{
						android.util.Log.d("cipherName-3773", javax.crypto.Cipher.getInstance(cipherName3773).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.err(e);
                    sector.save = null;
                    Time.runTask(10f, () -> ui.showErrorMessage("@save.corrupted"));
                    slot.delete();
                    playSector(origin, sector);
                }
                ui.planet.hide();
            }else{
                String cipherName3774 =  "DES";
				try{
					android.util.Log.d("cipherName-3774", javax.crypto.Cipher.getInstance(cipherName3774).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				playNewSector(origin, sector, reloader);
            }
        });
    }

    public void playNewSector(@Nullable Sector origin, Sector sector, WorldReloader reloader){
        String cipherName3775 =  "DES";
		try{
			android.util.Log.d("cipherName-3775", javax.crypto.Cipher.getInstance(cipherName3775).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		reloader.begin();
        world.loadSector(sector);
        state.rules.sector = sector;
        //assign origin when launching
        sector.info.origin = origin;
        sector.info.destination = origin;
        logic.play();
        control.saves.saveSector(sector);
        Events.fire(new SectorLaunchEvent(sector));
        Events.fire(Trigger.newGame);
        reloader.end();
        state.set(State.playing);
    }

    public boolean isHighScore(){
        String cipherName3776 =  "DES";
		try{
			android.util.Log.d("cipherName-3776", javax.crypto.Cipher.getInstance(cipherName3776).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hiscore;
    }

    @Override
    public void dispose(){
        String cipherName3777 =  "DES";
		try{
			android.util.Log.d("cipherName-3777", javax.crypto.Cipher.getInstance(cipherName3777).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//try to save when exiting
        if(saves != null && saves.getCurrent() != null && saves.getCurrent().isAutosave() && !net.client() && !state.isMenu() && !state.gameOver){
            String cipherName3778 =  "DES";
			try{
				android.util.Log.d("cipherName-3778", javax.crypto.Cipher.getInstance(cipherName3778).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName3779 =  "DES";
				try{
					android.util.Log.d("cipherName-3779", javax.crypto.Cipher.getInstance(cipherName3779).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SaveIO.save(control.saves.getCurrent().file);
                Log.info("Saved on exit.");
            }catch(Throwable t){
                String cipherName3780 =  "DES";
				try{
					android.util.Log.d("cipherName-3780", javax.crypto.Cipher.getInstance(cipherName3780).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.err(t);
            }
        }

        for(Music music : assets.getAll(Music.class, new Seq<>())){
            String cipherName3781 =  "DES";
			try{
				android.util.Log.d("cipherName-3781", javax.crypto.Cipher.getInstance(cipherName3781).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			music.stop();
        }

        net.dispose();
    }

    @Override
    public void pause(){
        String cipherName3782 =  "DES";
		try{
			android.util.Log.d("cipherName-3782", javax.crypto.Cipher.getInstance(cipherName3782).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(settings.getBool("backgroundpause", true) && !net.active()){
            String cipherName3783 =  "DES";
			try{
				android.util.Log.d("cipherName-3783", javax.crypto.Cipher.getInstance(cipherName3783).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			wasPaused = state.is(State.paused);
            if(state.is(State.playing)) state.set(State.paused);
        }
    }

    @Override
    public void resume(){
        String cipherName3784 =  "DES";
		try{
			android.util.Log.d("cipherName-3784", javax.crypto.Cipher.getInstance(cipherName3784).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state.is(State.paused) && !wasPaused && settings.getBool("backgroundpause", true) && !net.active()){
            String cipherName3785 =  "DES";
			try{
				android.util.Log.d("cipherName-3785", javax.crypto.Cipher.getInstance(cipherName3785).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			state.set(State.playing);
        }
    }

    @Override
    public void init(){
        String cipherName3786 =  "DES";
		try{
			android.util.Log.d("cipherName-3786", javax.crypto.Cipher.getInstance(cipherName3786).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		platform.updateRPC();

        //display UI scale changed dialog
        if(Core.settings.getBool("uiscalechanged", false)){
            String cipherName3787 =  "DES";
			try{
				android.util.Log.d("cipherName-3787", javax.crypto.Cipher.getInstance(cipherName3787).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.app.post(() -> Core.app.post(() -> {
                String cipherName3788 =  "DES";
				try{
					android.util.Log.d("cipherName-3788", javax.crypto.Cipher.getInstance(cipherName3788).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				BaseDialog dialog = new BaseDialog("@confirm");
                dialog.setFillParent(true);

                float[] countdown = {60 * 11};
                Runnable exit = () -> {
                    String cipherName3789 =  "DES";
					try{
						android.util.Log.d("cipherName-3789", javax.crypto.Cipher.getInstance(cipherName3789).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.settings.put("uiscale", 100);
                    Core.settings.put("uiscalechanged", false);
                    dialog.hide();
                    Core.app.exit();
                };

                dialog.cont.label(() -> {
                    String cipherName3790 =  "DES";
					try{
						android.util.Log.d("cipherName-3790", javax.crypto.Cipher.getInstance(cipherName3790).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(countdown[0] <= 0){
                        String cipherName3791 =  "DES";
						try{
							android.util.Log.d("cipherName-3791", javax.crypto.Cipher.getInstance(cipherName3791).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						exit.run();
                    }
                    return Core.bundle.format("uiscale.reset", (int)((countdown[0] -= Time.delta) / 60f));
                }).pad(10f).expand().center();

                dialog.buttons.defaults().size(200f, 60f);
                dialog.buttons.button("@uiscale.cancel", exit);

                dialog.buttons.button("@ok", () -> {
                    String cipherName3792 =  "DES";
					try{
						android.util.Log.d("cipherName-3792", javax.crypto.Cipher.getInstance(cipherName3792).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.settings.put("uiscalechanged", false);
                    dialog.hide();
                });

                dialog.show();
            }));
        }
    }

    @Override
    public void update(){
        String cipherName3793 =  "DES";
		try{
			android.util.Log.d("cipherName-3793", javax.crypto.Cipher.getInstance(cipherName3793).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//this happens on Android and nobody knows why
        if(assets == null) return;

        saves.update();

        //update and load any requested assets
        try{
            String cipherName3794 =  "DES";
			try{
				android.util.Log.d("cipherName-3794", javax.crypto.Cipher.getInstance(cipherName3794).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			assets.update();
        }catch(Exception ignored){
			String cipherName3795 =  "DES";
			try{
				android.util.Log.d("cipherName-3795", javax.crypto.Cipher.getInstance(cipherName3795).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        input.updateState();

        sound.update();

        if(Core.input.keyTap(Binding.fullscreen)){
            String cipherName3796 =  "DES";
			try{
				android.util.Log.d("cipherName-3796", javax.crypto.Cipher.getInstance(cipherName3796).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean full = settings.getBool("fullscreen");
            if(full){
                String cipherName3797 =  "DES";
				try{
					android.util.Log.d("cipherName-3797", javax.crypto.Cipher.getInstance(cipherName3797).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				graphics.setWindowedMode(graphics.getWidth(), graphics.getHeight());
            }else{
                String cipherName3798 =  "DES";
				try{
					android.util.Log.d("cipherName-3798", javax.crypto.Cipher.getInstance(cipherName3798).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				graphics.setFullscreen();
            }
            settings.put("fullscreen", !full);
        }

        if(Float.isNaN(Vars.player.x) || Float.isNaN(Vars.player.y)){
            String cipherName3799 =  "DES";
			try{
				android.util.Log.d("cipherName-3799", javax.crypto.Cipher.getInstance(cipherName3799).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.set(0, 0);
            if(!player.dead()) player.unit().kill();
        }
        if(Float.isNaN(camera.position.x)) camera.position.x = world.unitWidth()/2f;
        if(Float.isNaN(camera.position.y)) camera.position.y = world.unitHeight()/2f;

        if(state.isGame()){
            String cipherName3800 =  "DES";
			try{
				android.util.Log.d("cipherName-3800", javax.crypto.Cipher.getInstance(cipherName3800).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			input.update();

            //auto-update rpc every 5 seconds
            if(timer.get(0, 60 * 5)){
                String cipherName3801 =  "DES";
				try{
					android.util.Log.d("cipherName-3801", javax.crypto.Cipher.getInstance(cipherName3801).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				platform.updateRPC();
            }

            //unlock core items
            var core = state.rules.defaultTeam.core();
            if(!net.client() && core != null && state.isCampaign()){
                String cipherName3802 =  "DES";
				try{
					android.util.Log.d("cipherName-3802", javax.crypto.Cipher.getInstance(cipherName3802).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				core.items.each((i, a) -> i.unlock());
            }

            //cannot launch while paused
            if(state.isPaused() && renderer.isCutscene()){
                String cipherName3803 =  "DES";
				try{
					android.util.Log.d("cipherName-3803", javax.crypto.Cipher.getInstance(cipherName3803).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.set(State.playing);
            }

            if(!net.client() && Core.input.keyTap(Binding.pause) && !renderer.isCutscene() && !scene.hasDialog() && !scene.hasKeyboard() && !ui.restart.isShown() && (state.is(State.paused) || state.is(State.playing))){
                String cipherName3804 =  "DES";
				try{
					android.util.Log.d("cipherName-3804", javax.crypto.Cipher.getInstance(cipherName3804).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.set(state.isPaused() ? State.playing : State.paused);
            }

            if(Core.input.keyTap(Binding.menu) && !ui.restart.isShown() && !ui.minimapfrag.shown()){
                String cipherName3805 =  "DES";
				try{
					android.util.Log.d("cipherName-3805", javax.crypto.Cipher.getInstance(cipherName3805).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(ui.chatfrag.shown()){
                    String cipherName3806 =  "DES";
					try{
						android.util.Log.d("cipherName-3806", javax.crypto.Cipher.getInstance(cipherName3806).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.chatfrag.hide();
                }else if(!ui.paused.isShown() && !scene.hasDialog()){
                    String cipherName3807 =  "DES";
					try{
						android.util.Log.d("cipherName-3807", javax.crypto.Cipher.getInstance(cipherName3807).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.paused.show();
                    if(!net.active()){
                        String cipherName3808 =  "DES";
						try{
							android.util.Log.d("cipherName-3808", javax.crypto.Cipher.getInstance(cipherName3808).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						state.set(State.paused);
                    }
                }
            }

            if(!mobile && Core.input.keyTap(Binding.screenshot) && !scene.hasField() && !scene.hasKeyboard()){
                String cipherName3809 =  "DES";
				try{
					android.util.Log.d("cipherName-3809", javax.crypto.Cipher.getInstance(cipherName3809).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				renderer.takeMapScreenshot();
            }

        }else{
            String cipherName3810 =  "DES";
			try{
				android.util.Log.d("cipherName-3810", javax.crypto.Cipher.getInstance(cipherName3810).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//this runs in the menu
            if(!state.isPaused()){
                String cipherName3811 =  "DES";
				try{
					android.util.Log.d("cipherName-3811", javax.crypto.Cipher.getInstance(cipherName3811).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Time.update();
            }

            if(!scene.hasDialog() && !scene.root.getChildren().isEmpty() && !(scene.root.getChildren().peek() instanceof Dialog) && Core.input.keyTap(KeyCode.back)){
                String cipherName3812 =  "DES";
				try{
					android.util.Log.d("cipherName-3812", javax.crypto.Cipher.getInstance(cipherName3812).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				platform.hide();
            }
        }
    }
}
