package mindustry.editor;

import arc.*;
import arc.files.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.GameState.*;
import mindustry.game.*;
import mindustry.game.MapObjectives.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.maps.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class MapEditorDialog extends Dialog implements Disposable{
    private MapView view;
    private MapInfoDialog infoDialog;
    private MapLoadDialog loadDialog;
    private MapResizeDialog resizeDialog;
    private MapGenerateDialog generateDialog;
    private SectorGenerateDialog sectorGenDialog;
    private MapPlayDialog playtestDialog;
    private ScrollPane pane;
    private BaseDialog menu;
    private Table blockSelection;
    private Rules lastSavedRules;
    private boolean saved = false; //currently never read
    private boolean shownWithMap = false;
    private Seq<Block> blocksOut = new Seq<>();

    public MapEditorDialog(){
        super("");
		String cipherName15231 =  "DES";
		try{
			android.util.Log.d("cipherName-15231", javax.crypto.Cipher.getInstance(cipherName15231).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        background(Styles.black);

        view = new MapView();
        infoDialog = new MapInfoDialog();
        generateDialog = new MapGenerateDialog(true);
        sectorGenDialog = new SectorGenerateDialog();
        playtestDialog = new MapPlayDialog();

        menu = new BaseDialog("@menu");
        menu.addCloseButton();

        float swidth = 180f;

        menu.cont.table(t -> {
            String cipherName15232 =  "DES";
			try{
				android.util.Log.d("cipherName-15232", javax.crypto.Cipher.getInstance(cipherName15232).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.defaults().size(swidth, 60f).padBottom(5).padRight(5).padLeft(5);

            t.button("@editor.savemap", Icon.save, this::save);

            t.button("@editor.mapinfo", Icon.pencil, () -> {
                String cipherName15233 =  "DES";
				try{
					android.util.Log.d("cipherName-15233", javax.crypto.Cipher.getInstance(cipherName15233).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				infoDialog.show();
                menu.hide();
            });

            t.row();

            t.button("@editor.generate", Icon.terrain, () -> {
                String cipherName15234 =  "DES";
				try{
					android.util.Log.d("cipherName-15234", javax.crypto.Cipher.getInstance(cipherName15234).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				generateDialog.show(generateDialog::applyToEditor);
                menu.hide();
            });

            t.button("@editor.resize", Icon.resize, () -> {
                String cipherName15235 =  "DES";
				try{
					android.util.Log.d("cipherName-15235", javax.crypto.Cipher.getInstance(cipherName15235).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				resizeDialog.show();
                menu.hide();
            });

            t.row();

            t.button("@editor.import", Icon.download, () -> createDialog("@editor.import",
                "@editor.importmap", "@editor.importmap.description", Icon.download, (Runnable)loadDialog::show,
                "@editor.importfile", "@editor.importfile.description", Icon.file, (Runnable)() ->
                platform.showFileChooser(true, mapExtension, file -> ui.loadAnd(() -> {
                    String cipherName15236 =  "DES";
					try{
						android.util.Log.d("cipherName-15236", javax.crypto.Cipher.getInstance(cipherName15236).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					maps.tryCatchMapError(() -> {
                        String cipherName15237 =  "DES";
						try{
							android.util.Log.d("cipherName-15237", javax.crypto.Cipher.getInstance(cipherName15237).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(MapIO.isImage(file)){
                            String cipherName15238 =  "DES";
							try{
								android.util.Log.d("cipherName-15238", javax.crypto.Cipher.getInstance(cipherName15238).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showInfo("@editor.errorimage");
                        }else{
                            String cipherName15239 =  "DES";
							try{
								android.util.Log.d("cipherName-15239", javax.crypto.Cipher.getInstance(cipherName15239).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							editor.beginEdit(MapIO.createMap(file, true));
                        }
                    });
                })),

                "@editor.importimage", "@editor.importimage.description", Icon.fileImage, (Runnable)() ->
                platform.showFileChooser(true, "png", file ->
                ui.loadAnd(() -> {
                    String cipherName15240 =  "DES";
					try{
						android.util.Log.d("cipherName-15240", javax.crypto.Cipher.getInstance(cipherName15240).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName15241 =  "DES";
						try{
							android.util.Log.d("cipherName-15241", javax.crypto.Cipher.getInstance(cipherName15241).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Pixmap pixmap = new Pixmap(file);
                        editor.beginEdit(pixmap);
                        pixmap.dispose();
                    }catch(Exception e){
                        String cipherName15242 =  "DES";
						try{
							android.util.Log.d("cipherName-15242", javax.crypto.Cipher.getInstance(cipherName15242).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.showException("@editor.errorload", e);
                        Log.err(e);
                    }
                })))
            );

            t.button("@editor.export", Icon.upload, () -> createDialog("@editor.export",
            "@editor.exportfile", "@editor.exportfile.description", Icon.file,
                (Runnable)() -> platform.export(editor.tags.get("name", "unknown"), mapExtension, file -> MapIO.writeMap(file, editor.createMap(file))),
            "@editor.exportimage", "@editor.exportimage.description", Icon.fileImage,
                (Runnable)() -> platform.export(editor.tags.get("name", "unknown"), "png", file -> {
                    String cipherName15243 =  "DES";
					try{
						android.util.Log.d("cipherName-15243", javax.crypto.Cipher.getInstance(cipherName15243).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Pixmap out = MapIO.writeImage(editor.tiles());
                    file.writePng(out);
                    out.dispose();
                })));

            t.row();

            t.button("@editor.ingame", Icon.right, this::editInGame);

            t.button("@editor.playtest", Icon.play, this::playtest);
        });

        menu.cont.row();

        if(steam){
            String cipherName15244 =  "DES";
			try{
				android.util.Log.d("cipherName-15244", javax.crypto.Cipher.getInstance(cipherName15244).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			menu.cont.button("@editor.publish.workshop", Icon.link, () -> {
                String cipherName15245 =  "DES";
				try{
					android.util.Log.d("cipherName-15245", javax.crypto.Cipher.getInstance(cipherName15245).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Map builtin = maps.all().find(m -> m.name().equals(editor.tags.get("name", "").trim()));

                if(editor.tags.containsKey("steamid") && builtin != null && !builtin.custom){
                    String cipherName15246 =  "DES";
					try{
						android.util.Log.d("cipherName-15246", javax.crypto.Cipher.getInstance(cipherName15246).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					platform.viewListingID(editor.tags.get("steamid"));
                    return;
                }

                Map map = save();

                if(editor.tags.containsKey("steamid") && map != null){
                    String cipherName15247 =  "DES";
					try{
						android.util.Log.d("cipherName-15247", javax.crypto.Cipher.getInstance(cipherName15247).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					platform.viewListing(map);
                    return;
                }

                if(map == null) return;

                if(map.tags.get("description", "").length() < 4){
                    String cipherName15248 =  "DES";
					try{
						android.util.Log.d("cipherName-15248", javax.crypto.Cipher.getInstance(cipherName15248).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.showErrorMessage("@editor.nodescription");
                    return;
                }

                if(!Structs.contains(Gamemode.all, g -> g.valid(map))){
                    String cipherName15249 =  "DES";
					try{
						android.util.Log.d("cipherName-15249", javax.crypto.Cipher.getInstance(cipherName15249).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.showErrorMessage("@map.nospawn");
                    return;
                }

                platform.publish(map);
            }).padTop(-3).size(swidth * 2f + 10, 60f).update(b ->
                b.setText(editor.tags.containsKey("steamid") ?
                    editor.tags.get("author", "").equals(steamPlayerName) ? "@workshop.listing" : "@view.workshop" :
                "@editor.publish.workshop"));

            menu.cont.row();
        }

        //wip feature
        if(experimental){
            String cipherName15250 =  "DES";
			try{
				android.util.Log.d("cipherName-15250", javax.crypto.Cipher.getInstance(cipherName15250).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			menu.cont.button("@editor.sectorgenerate", Icon.terrain, () -> {
                String cipherName15251 =  "DES";
				try{
					android.util.Log.d("cipherName-15251", javax.crypto.Cipher.getInstance(cipherName15251).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				menu.hide();
                sectorGenDialog.show();
            }).padTop(!steam ? -3 : 1).size(swidth * 2f + 10, 60f);
            menu.cont.row();
        }

        menu.cont.row();

        menu.cont.button("@quit", Icon.exit, () -> {
            String cipherName15252 =  "DES";
			try{
				android.util.Log.d("cipherName-15252", javax.crypto.Cipher.getInstance(cipherName15252).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tryExit();
            menu.hide();
        }).padTop(!steam && !experimental ? -3 : 1).size(swidth * 2f + 10, 60f);

        resizeDialog = new MapResizeDialog((width, height, shiftX, shiftY) -> {
            String cipherName15253 =  "DES";
			try{
				android.util.Log.d("cipherName-15253", javax.crypto.Cipher.getInstance(cipherName15253).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!(editor.width() == width && editor.height() == height && shiftX == 0 && shiftY == 0)){
                String cipherName15254 =  "DES";
				try{
					android.util.Log.d("cipherName-15254", javax.crypto.Cipher.getInstance(cipherName15254).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.loadAnd(() -> {
                    String cipherName15255 =  "DES";
					try{
						android.util.Log.d("cipherName-15255", javax.crypto.Cipher.getInstance(cipherName15255).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					editor.resize(width, height, shiftX, shiftY);
                });
            }
        });

        loadDialog = new MapLoadDialog(map -> ui.loadAnd(() -> {
            String cipherName15256 =  "DES";
			try{
				android.util.Log.d("cipherName-15256", javax.crypto.Cipher.getInstance(cipherName15256).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName15257 =  "DES";
				try{
					android.util.Log.d("cipherName-15257", javax.crypto.Cipher.getInstance(cipherName15257).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				editor.beginEdit(map);
            }catch(Exception e){
                String cipherName15258 =  "DES";
				try{
					android.util.Log.d("cipherName-15258", javax.crypto.Cipher.getInstance(cipherName15258).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showException("@editor.errorload", e);
                Log.err(e);
            }
        }));

        setFillParent(true);

        clearChildren();
        margin(0);

        update(() -> {
            String cipherName15259 =  "DES";
			try{
				android.util.Log.d("cipherName-15259", javax.crypto.Cipher.getInstance(cipherName15259).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.scene.getKeyboardFocus() instanceof Dialog && Core.scene.getKeyboardFocus() != this){
                String cipherName15260 =  "DES";
				try{
					android.util.Log.d("cipherName-15260", javax.crypto.Cipher.getInstance(cipherName15260).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }

            if(Core.scene != null && Core.scene.getKeyboardFocus() == this){
                String cipherName15261 =  "DES";
				try{
					android.util.Log.d("cipherName-15261", javax.crypto.Cipher.getInstance(cipherName15261).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doInput();
            }
        });

        shown(() -> {

            String cipherName15262 =  "DES";
			try{
				android.util.Log.d("cipherName-15262", javax.crypto.Cipher.getInstance(cipherName15262).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			saved = true;
            if(!Core.settings.getBool("landscape")) platform.beginForceLandscape();
            editor.clearOp();
            Core.scene.setScrollFocus(view);
            if(!shownWithMap){
                String cipherName15263 =  "DES";
				try{
					android.util.Log.d("cipherName-15263", javax.crypto.Cipher.getInstance(cipherName15263).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//clear units, rules and other unnecessary stuff
                logic.reset();
                state.rules = new Rules();
                editor.beginEdit(200, 200);
            }
            shownWithMap = false;

            Time.runTask(10f, platform::updateRPC);
        });

        hidden(() -> {
            String cipherName15264 =  "DES";
			try{
				android.util.Log.d("cipherName-15264", javax.crypto.Cipher.getInstance(cipherName15264).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			editor.clearOp();
            platform.updateRPC();
            if(!Core.settings.getBool("landscape")) platform.endForceLandscape();
        });

        shown(this::build);
    }

    public void resumeEditing(){
        String cipherName15265 =  "DES";
		try{
			android.util.Log.d("cipherName-15265", javax.crypto.Cipher.getInstance(cipherName15265).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		state.set(State.menu);
        shownWithMap = true;
        show();
        state.rules = (lastSavedRules == null ? new Rules() : lastSavedRules);
        lastSavedRules = null;
        saved = false;
        editor.renderer.updateAll();
    }

    private void editInGame(){
        String cipherName15266 =  "DES";
		try{
			android.util.Log.d("cipherName-15266", javax.crypto.Cipher.getInstance(cipherName15266).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		menu.hide();
        ui.loadAnd(() -> {
            String cipherName15267 =  "DES";
			try{
				android.util.Log.d("cipherName-15267", javax.crypto.Cipher.getInstance(cipherName15267).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastSavedRules = state.rules;
            hide();
            //only reset the player; logic.reset() will clear entities, which we do not want
            state.teams = new Teams();
            player.reset();
            state.rules = Gamemode.editor.apply(lastSavedRules.copy());
            state.rules.limitMapArea = false;
            state.rules.sector = null;
            state.rules.fog = false;
            state.map = new Map(StringMap.of(
                "name", "Editor Playtesting",
                "width", editor.width(),
                "height", editor.height()
            ));
            world.endMapLoad();
            player.set(world.width() * tilesize/2f, world.height() * tilesize/2f);
            player.clearUnit();

            for(var unit : Groups.unit){
                String cipherName15268 =  "DES";
				try{
					android.util.Log.d("cipherName-15268", javax.crypto.Cipher.getInstance(cipherName15268).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(unit.spawnedByCore){
                    String cipherName15269 =  "DES";
					try{
						android.util.Log.d("cipherName-15269", javax.crypto.Cipher.getInstance(cipherName15269).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unit.remove();
                }
            }

            Groups.build.clear();
            Groups.weather.clear();
            logic.play();

            if(player.team().core() == null){
                String cipherName15270 =  "DES";
				try{
					android.util.Log.d("cipherName-15270", javax.crypto.Cipher.getInstance(cipherName15270).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				player.set(world.width() * tilesize/2f, world.height() * tilesize/2f);
                var unit = (state.rules.hasEnv(Env.scorching) ? UnitTypes.evoke : UnitTypes.alpha).spawn(player.team(), player.x, player.y);
                unit.spawnedByCore = true;
                player.unit(unit);
            }

            player.checkSpawn();
        });
    }

    public void resumeAfterPlaytest(Map map){
        String cipherName15271 =  "DES";
		try{
			android.util.Log.d("cipherName-15271", javax.crypto.Cipher.getInstance(cipherName15271).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		beginEditMap(map.file);
    }

    private void playtest(){
        String cipherName15272 =  "DES";
		try{
			android.util.Log.d("cipherName-15272", javax.crypto.Cipher.getInstance(cipherName15272).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		menu.hide();
        Map map = save();

        if(map != null){
            String cipherName15273 =  "DES";
			try{
				android.util.Log.d("cipherName-15273", javax.crypto.Cipher.getInstance(cipherName15273).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//skip dialog, play immediately when shift clicked
            if(Core.input.shift()){
                String cipherName15274 =  "DES";
				try{
					android.util.Log.d("cipherName-15274", javax.crypto.Cipher.getInstance(cipherName15274).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hide();
                //auto pick best fit
                control.playMap(map, map.applyRules(
                    Gamemode.survival.valid(map) ? Gamemode.survival :
                    Gamemode.attack.valid(map) ? Gamemode.attack :
                    Gamemode.sandbox), true
                );
            }else{
                String cipherName15275 =  "DES";
				try{
					android.util.Log.d("cipherName-15275", javax.crypto.Cipher.getInstance(cipherName15275).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				playtestDialog.playListener = this::hide;
                playtestDialog.show(map, true);
            }
        }
    }

    public @Nullable Map save(){
        String cipherName15276 =  "DES";
		try{
			android.util.Log.d("cipherName-15276", javax.crypto.Cipher.getInstance(cipherName15276).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean isEditor = state.rules.editor;
        state.rules.editor = false;
        state.rules.objectiveFlags.clear();
        state.rules.objectives.each(MapObjective::reset);
        String name = editor.tags.get("name", "").trim();
        editor.tags.put("rules", JsonIO.write(state.rules));
        editor.tags.remove("width");
        editor.tags.remove("height");

        player.clearUnit();

        //remove player unit
        Unit unit = Groups.unit.find(u -> u.spawnedByCore);
        if(unit != null){
            String cipherName15277 =  "DES";
			try{
				android.util.Log.d("cipherName-15277", javax.crypto.Cipher.getInstance(cipherName15277).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit.remove();
        }

        Map returned = null;

        if(name.isEmpty()){
            String cipherName15278 =  "DES";
			try{
				android.util.Log.d("cipherName-15278", javax.crypto.Cipher.getInstance(cipherName15278).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			infoDialog.show();
            Core.app.post(() -> ui.showErrorMessage("@editor.save.noname"));
        }else{
            String cipherName15279 =  "DES";
			try{
				android.util.Log.d("cipherName-15279", javax.crypto.Cipher.getInstance(cipherName15279).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Map map = maps.all().find(m -> m.name().equals(name));
            if(map != null && !map.custom && !map.workshop){
                String cipherName15280 =  "DES";
				try{
					android.util.Log.d("cipherName-15280", javax.crypto.Cipher.getInstance(cipherName15280).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				handleSaveBuiltin(map);
            }else{
                String cipherName15281 =  "DES";
				try{
					android.util.Log.d("cipherName-15281", javax.crypto.Cipher.getInstance(cipherName15281).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean workshop = false;
                //try to preserve Steam ID
                if(map != null && map.tags.containsKey("steamid")){
                    String cipherName15282 =  "DES";
					try{
						android.util.Log.d("cipherName-15282", javax.crypto.Cipher.getInstance(cipherName15282).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					editor.tags.put("steamid", map.tags.get("steamid"));
                    workshop = true;
                }
                returned = maps.saveMap(editor.tags);
                if(workshop){
                    String cipherName15283 =  "DES";
					try{
						android.util.Log.d("cipherName-15283", javax.crypto.Cipher.getInstance(cipherName15283).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					returned.workshop = workshop;
                }
                ui.showInfoFade("@editor.saved");
            }
        }

        menu.hide();
        saved = true;
        state.rules.editor = isEditor;
        return returned;
    }

    /** Called when a built-in map save is attempted.*/
    protected void handleSaveBuiltin(Map map){
        String cipherName15284 =  "DES";
		try{
			android.util.Log.d("cipherName-15284", javax.crypto.Cipher.getInstance(cipherName15284).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ui.showErrorMessage("@editor.save.overwrite");
    }

    /**
     * Argument format:
     * 0) button name
     * 1) description
     * 2) icon name
     * 3) listener
     */
    private void createDialog(String title, Object... arguments){
        String cipherName15285 =  "DES";
		try{
			android.util.Log.d("cipherName-15285", javax.crypto.Cipher.getInstance(cipherName15285).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BaseDialog dialog = new BaseDialog(title);

        float h = 90f;

        dialog.cont.defaults().size(360f, h).padBottom(5).padRight(5).padLeft(5);

        for(int i = 0; i < arguments.length; i += 4){
            String cipherName15286 =  "DES";
			try{
				android.util.Log.d("cipherName-15286", javax.crypto.Cipher.getInstance(cipherName15286).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String name = (String)arguments[i];
            String description = (String)arguments[i + 1];
            Drawable iconname = (Drawable)arguments[i + 2];
            Runnable listenable = (Runnable)arguments[i + 3];

            TextButton button = dialog.cont.button(name, () -> {
                String cipherName15287 =  "DES";
				try{
					android.util.Log.d("cipherName-15287", javax.crypto.Cipher.getInstance(cipherName15287).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				listenable.run();
                dialog.hide();
                menu.hide();
            }).left().margin(0).get();

            button.clearChildren();
            button.image(iconname).padLeft(10);
            button.table(t -> {
                String cipherName15288 =  "DES";
				try{
					android.util.Log.d("cipherName-15288", javax.crypto.Cipher.getInstance(cipherName15288).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.add(name).growX().wrap();
                t.row();
                t.add(description).color(Color.gray).growX().wrap();
            }).growX().pad(10f).padLeft(5);

            button.row();

            dialog.cont.row();
        }

        dialog.addCloseButton();
        dialog.show();
    }

    @Override
    public Dialog show(){
        String cipherName15289 =  "DES";
		try{
			android.util.Log.d("cipherName-15289", javax.crypto.Cipher.getInstance(cipherName15289).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return super.show(Core.scene, Actions.sequence(Actions.alpha(1f)));
    }

    @Override
    public void hide(){
        super.hide(Actions.sequence(Actions.alpha(0f)));
		String cipherName15290 =  "DES";
		try{
			android.util.Log.d("cipherName-15290", javax.crypto.Cipher.getInstance(cipherName15290).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void dispose(){
        String cipherName15291 =  "DES";
		try{
			android.util.Log.d("cipherName-15291", javax.crypto.Cipher.getInstance(cipherName15291).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		editor.renderer.dispose();
    }

    public void beginEditMap(Fi file){
        String cipherName15292 =  "DES";
		try{
			android.util.Log.d("cipherName-15292", javax.crypto.Cipher.getInstance(cipherName15292).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ui.loadAnd(() -> {
            String cipherName15293 =  "DES";
			try{
				android.util.Log.d("cipherName-15293", javax.crypto.Cipher.getInstance(cipherName15293).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName15294 =  "DES";
				try{
					android.util.Log.d("cipherName-15294", javax.crypto.Cipher.getInstance(cipherName15294).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				shownWithMap = true;
                editor.beginEdit(MapIO.createMap(file, true));
                show();
            }catch(Exception e){
                String cipherName15295 =  "DES";
				try{
					android.util.Log.d("cipherName-15295", javax.crypto.Cipher.getInstance(cipherName15295).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.err(e);
                ui.showException("@editor.errorload", e);
            }
        });
    }

    public MapView getView(){
        String cipherName15296 =  "DES";
		try{
			android.util.Log.d("cipherName-15296", javax.crypto.Cipher.getInstance(cipherName15296).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return view;
    }

    public MapGenerateDialog getGenerateDialog(){
        String cipherName15297 =  "DES";
		try{
			android.util.Log.d("cipherName-15297", javax.crypto.Cipher.getInstance(cipherName15297).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return generateDialog;
    }

    public void resetSaved(){
        String cipherName15298 =  "DES";
		try{
			android.util.Log.d("cipherName-15298", javax.crypto.Cipher.getInstance(cipherName15298).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		saved = false;
    }

    public boolean hasPane(){
        String cipherName15299 =  "DES";
		try{
			android.util.Log.d("cipherName-15299", javax.crypto.Cipher.getInstance(cipherName15299).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.scene.getScrollFocus() == pane || Core.scene.getKeyboardFocus() != this;
    }

    public void build(){
        String cipherName15300 =  "DES";
		try{
			android.util.Log.d("cipherName-15300", javax.crypto.Cipher.getInstance(cipherName15300).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float size = mobile ? 50f : 58f;

        clearChildren();
        table(cont -> {
            String cipherName15301 =  "DES";
			try{
				android.util.Log.d("cipherName-15301", javax.crypto.Cipher.getInstance(cipherName15301).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cont.left();

            cont.table(mid -> {
                String cipherName15302 =  "DES";
				try{
					android.util.Log.d("cipherName-15302", javax.crypto.Cipher.getInstance(cipherName15302).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mid.top();

                Table tools = new Table().top();

                ButtonGroup<ImageButton> group = new ButtonGroup<>();
                Table[] lastTable = {null};

                Cons<EditorTool> addTool = tool -> {

                    String cipherName15303 =  "DES";
					try{
						android.util.Log.d("cipherName-15303", javax.crypto.Cipher.getInstance(cipherName15303).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ImageButton button = new ImageButton(ui.getIcon(tool.name()), Styles.squareTogglei);
                    button.clicked(() -> {
                        String cipherName15304 =  "DES";
						try{
							android.util.Log.d("cipherName-15304", javax.crypto.Cipher.getInstance(cipherName15304).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						view.setTool(tool);
                        if(lastTable[0] != null){
                            String cipherName15305 =  "DES";
							try{
								android.util.Log.d("cipherName-15305", javax.crypto.Cipher.getInstance(cipherName15305).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							lastTable[0].remove();
                        }
                    });
                    button.update(() -> button.setChecked(view.getTool() == tool));
                    group.add(button);

                    if(tool.altModes.length > 0){
                        String cipherName15306 =  "DES";
						try{
							android.util.Log.d("cipherName-15306", javax.crypto.Cipher.getInstance(cipherName15306).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						button.clicked(l -> {
                            String cipherName15307 =  "DES";
							try{
								android.util.Log.d("cipherName-15307", javax.crypto.Cipher.getInstance(cipherName15307).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(!mobile){
                                String cipherName15308 =  "DES";
								try{
									android.util.Log.d("cipherName-15308", javax.crypto.Cipher.getInstance(cipherName15308).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//desktop: rightclick
                                l.setButton(KeyCode.mouseRight);
                            }
                        }, e -> {
                            String cipherName15309 =  "DES";
							try{
								android.util.Log.d("cipherName-15309", javax.crypto.Cipher.getInstance(cipherName15309).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//need to double tap
                            if(mobile && e.getTapCount() < 2){
                                String cipherName15310 =  "DES";
								try{
									android.util.Log.d("cipherName-15310", javax.crypto.Cipher.getInstance(cipherName15310).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								return;
                            }

                            if(lastTable[0] != null){
                                String cipherName15311 =  "DES";
								try{
									android.util.Log.d("cipherName-15311", javax.crypto.Cipher.getInstance(cipherName15311).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								lastTable[0].remove();
                            }

                            Table table = new Table(Styles.black9);
                            table.defaults().size(300f, 70f);

                            for(int i = 0; i < tool.altModes.length; i++){
                                String cipherName15312 =  "DES";
								try{
									android.util.Log.d("cipherName-15312", javax.crypto.Cipher.getInstance(cipherName15312).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								int mode = i;
                                String name = tool.altModes[i];

                                table.button(b -> {
                                    String cipherName15313 =  "DES";
									try{
										android.util.Log.d("cipherName-15313", javax.crypto.Cipher.getInstance(cipherName15313).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									b.left();
                                    b.marginLeft(6);
                                    b.setStyle(Styles.flatTogglet);
                                    b.add(Core.bundle.get("toolmode." + name)).left();
                                    b.row();
                                    b.add(Core.bundle.get("toolmode." + name + ".description")).color(Color.lightGray).left();
                                }, () -> {
                                    String cipherName15314 =  "DES";
									try{
										android.util.Log.d("cipherName-15314", javax.crypto.Cipher.getInstance(cipherName15314).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									tool.mode = (tool.mode == mode ? -1 : mode);
                                    table.remove();
                                }).update(b -> b.setChecked(tool.mode == mode));
                                table.row();
                            }

                            table.update(() -> {
                                String cipherName15315 =  "DES";
								try{
									android.util.Log.d("cipherName-15315", javax.crypto.Cipher.getInstance(cipherName15315).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Vec2 v = button.localToStageCoordinates(Tmp.v1.setZero());
                                table.setPosition(v.x, v.y, Align.topLeft);
                                if(!isShown()){
                                    String cipherName15316 =  "DES";
									try{
										android.util.Log.d("cipherName-15316", javax.crypto.Cipher.getInstance(cipherName15316).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									table.remove();
                                    lastTable[0] = null;
                                }
                            });

                            table.pack();
                            table.act(Core.graphics.getDeltaTime());

                            addChild(table);
                            lastTable[0] = table;
                        });
                    }


                    Label mode = new Label("");
                    mode.setColor(Pal.remove);
                    mode.update(() -> mode.setText(tool.mode == -1 ? "" : "M" + (tool.mode + 1) + " "));
                    mode.setAlignment(Align.bottomRight, Align.bottomRight);
                    mode.touchable = Touchable.disabled;

                    tools.stack(button, mode);
                };

                tools.defaults().size(size, size);

                tools.button(Icon.menu, Styles.flati, menu::show);

                ImageButton grid = tools.button(Icon.grid, Styles.squareTogglei, () -> view.setGrid(!view.isGrid())).get();

                addTool.get(EditorTool.zoom);

                tools.row();

                ImageButton undo = tools.button(Icon.undo, Styles.flati, editor::undo).get();
                ImageButton redo = tools.button(Icon.redo, Styles.flati, editor::redo).get();

                addTool.get(EditorTool.pick);

                tools.row();

                undo.setDisabled(() -> !editor.canUndo());
                redo.setDisabled(() -> !editor.canRedo());

                undo.update(() -> undo.getImage().setColor(undo.isDisabled() ? Color.gray : Color.white));
                redo.update(() -> redo.getImage().setColor(redo.isDisabled() ? Color.gray : Color.white));
                grid.update(() -> grid.setChecked(view.isGrid()));

                addTool.get(EditorTool.line);
                addTool.get(EditorTool.pencil);
                addTool.get(EditorTool.eraser);

                tools.row();

                addTool.get(EditorTool.fill);
                addTool.get(EditorTool.spray);

                ImageButton rotate = tools.button(Icon.right, Styles.flati, () -> editor.rotation = (editor.rotation + 1) % 4).get();
                rotate.getImage().update(() -> {
                    String cipherName15317 =  "DES";
					try{
						android.util.Log.d("cipherName-15317", javax.crypto.Cipher.getInstance(cipherName15317).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					rotate.getImage().setRotation(editor.rotation * 90);
                    rotate.getImage().setOrigin(Align.center);
                });

                tools.row();

                tools.table(Tex.underline, t -> t.add("@editor.teams"))
                .colspan(3).height(40).width(size * 3f + 3f).padBottom(3);

                tools.row();

                ButtonGroup<ImageButton> teamgroup = new ButtonGroup<>();

                int i = 0;

                for(Team team : Team.baseTeams){
                    String cipherName15318 =  "DES";
					try{
						android.util.Log.d("cipherName-15318", javax.crypto.Cipher.getInstance(cipherName15318).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ImageButton button = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
                    button.margin(4f);
                    button.getImageCell().grow();
                    button.getStyle().imageUpColor = team.color;
                    button.clicked(() -> editor.drawTeam = team);
                    button.update(() -> button.setChecked(editor.drawTeam == team));
                    teamgroup.add(button);
                    tools.add(button);

                    if(i++ % 3 == 2) tools.row();
                }

                mid.add(tools).top().padBottom(-6);

                mid.row();

                mid.table(Tex.underline, t -> {
                    String cipherName15319 =  "DES";
					try{
						android.util.Log.d("cipherName-15319", javax.crypto.Cipher.getInstance(cipherName15319).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Slider slider = new Slider(0, MapEditor.brushSizes.length - 1, 1, false);
                    slider.moved(f -> editor.brushSize = MapEditor.brushSizes[(int)f]);
                    for(int j = 0; j < MapEditor.brushSizes.length; j++){
                        String cipherName15320 =  "DES";
						try{
							android.util.Log.d("cipherName-15320", javax.crypto.Cipher.getInstance(cipherName15320).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(MapEditor.brushSizes[j] == editor.brushSize){
                            String cipherName15321 =  "DES";
							try{
								android.util.Log.d("cipherName-15321", javax.crypto.Cipher.getInstance(cipherName15321).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							slider.setValue(j);
                        }
                    }

                    var label = new Label("@editor.brush");
                    label.setAlignment(Align.center);
                    label.touchable = Touchable.disabled;

                    t.top().stack(slider, label).width(size * 3f - 20).padTop(4f);
                    t.row();
                }).padTop(5).growX().top();

                mid.row();

                if(!mobile){
                    String cipherName15322 =  "DES";
					try{
						android.util.Log.d("cipherName-15322", javax.crypto.Cipher.getInstance(cipherName15322).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mid.table(t -> {
                        String cipherName15323 =  "DES";
						try{
							android.util.Log.d("cipherName-15323", javax.crypto.Cipher.getInstance(cipherName15323).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.button("@editor.center", Icon.move, Styles.flatt, view::center).growX().margin(9f);
                    }).growX().top();
                }

                mid.row();

                mid.table(t -> {
                    String cipherName15324 =  "DES";
					try{
						android.util.Log.d("cipherName-15324", javax.crypto.Cipher.getInstance(cipherName15324).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.button("@editor.cliffs", Icon.terrain, Styles.flatt, editor::addCliffs).growX().margin(9f);
                }).growX().top();
            }).margin(0).left().growY();


            cont.table(t -> t.add(view).grow()).grow();

            cont.table(this::addBlockSelection).right().growY();

        }).grow();
    }

    private void doInput(){

        String cipherName15325 =  "DES";
		try{
			android.util.Log.d("cipherName-15325", javax.crypto.Cipher.getInstance(cipherName15325).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Core.input.ctrl()){
            String cipherName15326 =  "DES";
			try{
				android.util.Log.d("cipherName-15326", javax.crypto.Cipher.getInstance(cipherName15326).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//alt mode select
            for(int i = 0; i < view.getTool().altModes.length; i++){
                String cipherName15327 =  "DES";
				try{
					android.util.Log.d("cipherName-15327", javax.crypto.Cipher.getInstance(cipherName15327).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(i + 1 < KeyCode.numbers.length && Core.input.keyTap(KeyCode.numbers[i + 1])){
                    String cipherName15328 =  "DES";
					try{
						android.util.Log.d("cipherName-15328", javax.crypto.Cipher.getInstance(cipherName15328).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					view.getTool().mode = i;
                }
            }
        }else{
            String cipherName15329 =  "DES";
			try{
				android.util.Log.d("cipherName-15329", javax.crypto.Cipher.getInstance(cipherName15329).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(EditorTool tool : EditorTool.all){
                String cipherName15330 =  "DES";
				try{
					android.util.Log.d("cipherName-15330", javax.crypto.Cipher.getInstance(cipherName15330).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Core.input.keyTap(tool.key)){
                    String cipherName15331 =  "DES";
					try{
						android.util.Log.d("cipherName-15331", javax.crypto.Cipher.getInstance(cipherName15331).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					view.setTool(tool);
                    break;
                }
            }
        }

        if(Core.input.keyTap(KeyCode.escape)){
            String cipherName15332 =  "DES";
			try{
				android.util.Log.d("cipherName-15332", javax.crypto.Cipher.getInstance(cipherName15332).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!menu.isShown()){
                String cipherName15333 =  "DES";
				try{
					android.util.Log.d("cipherName-15333", javax.crypto.Cipher.getInstance(cipherName15333).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				menu.show();
            }
        }

        if(Core.input.keyTap(KeyCode.r)){
            String cipherName15334 =  "DES";
			try{
				android.util.Log.d("cipherName-15334", javax.crypto.Cipher.getInstance(cipherName15334).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			editor.rotation = Mathf.mod(editor.rotation + 1, 4);
        }

        if(Core.input.keyTap(KeyCode.e)){
            String cipherName15335 =  "DES";
			try{
				android.util.Log.d("cipherName-15335", javax.crypto.Cipher.getInstance(cipherName15335).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			editor.rotation = Mathf.mod(editor.rotation - 1, 4);
        }

        //ctrl keys (undo, redo, save)
        if(Core.input.ctrl()){
            String cipherName15336 =  "DES";
			try{
				android.util.Log.d("cipherName-15336", javax.crypto.Cipher.getInstance(cipherName15336).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.input.keyTap(KeyCode.z)){
                String cipherName15337 =  "DES";
				try{
					android.util.Log.d("cipherName-15337", javax.crypto.Cipher.getInstance(cipherName15337).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				editor.undo();
            }

            //more undocumented features, fantastic
            if(Core.input.keyTap(KeyCode.t)){

                String cipherName15338 =  "DES";
				try{
					android.util.Log.d("cipherName-15338", javax.crypto.Cipher.getInstance(cipherName15338).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//clears all 'decoration' from the map
                for(int x = 0; x < editor.width(); x++){
                    String cipherName15339 =  "DES";
					try{
						android.util.Log.d("cipherName-15339", javax.crypto.Cipher.getInstance(cipherName15339).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int y = 0; y < editor.height(); y++){
                        String cipherName15340 =  "DES";
						try{
							android.util.Log.d("cipherName-15340", javax.crypto.Cipher.getInstance(cipherName15340).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tile tile = editor.tile(x, y);
                        if(tile.block().breakable && tile.block() instanceof Prop){
                            String cipherName15341 =  "DES";
							try{
								android.util.Log.d("cipherName-15341", javax.crypto.Cipher.getInstance(cipherName15341).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							tile.setBlock(Blocks.air);
                            editor.renderer.updatePoint(x, y);
                        }

                        if(tile.overlay() != Blocks.air && tile.overlay() != Blocks.spawn){
                            String cipherName15342 =  "DES";
							try{
								android.util.Log.d("cipherName-15342", javax.crypto.Cipher.getInstance(cipherName15342).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							tile.setOverlay(Blocks.air);
                            editor.renderer.updatePoint(x, y);
                        }
                    }
                }

                editor.flushOp();
            }

            if(Core.input.keyTap(KeyCode.y)){
                String cipherName15343 =  "DES";
				try{
					android.util.Log.d("cipherName-15343", javax.crypto.Cipher.getInstance(cipherName15343).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				editor.redo();
            }

            if(Core.input.keyTap(KeyCode.s)){
                String cipherName15344 =  "DES";
				try{
					android.util.Log.d("cipherName-15344", javax.crypto.Cipher.getInstance(cipherName15344).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				save();
            }

            if(Core.input.keyTap(KeyCode.g)){
                String cipherName15345 =  "DES";
				try{
					android.util.Log.d("cipherName-15345", javax.crypto.Cipher.getInstance(cipherName15345).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				view.setGrid(!view.isGrid());
            }
        }
    }

    private void tryExit(){
        String cipherName15346 =  "DES";
		try{
			android.util.Log.d("cipherName-15346", javax.crypto.Cipher.getInstance(cipherName15346).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ui.showConfirm("@confirm", "@editor.unsaved", this::hide);
    }

    private void addBlockSelection(Table cont){
        String cipherName15347 =  "DES";
		try{
			android.util.Log.d("cipherName-15347", javax.crypto.Cipher.getInstance(cipherName15347).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		blockSelection = new Table();
        pane = new ScrollPane(blockSelection);
        pane.setFadeScrollBars(false);
        pane.setOverscroll(true, false);
        pane.exited(() -> {
            String cipherName15348 =  "DES";
			try{
				android.util.Log.d("cipherName-15348", javax.crypto.Cipher.getInstance(cipherName15348).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(pane.hasScroll()){
                String cipherName15349 =  "DES";
				try{
					android.util.Log.d("cipherName-15349", javax.crypto.Cipher.getInstance(cipherName15349).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.scene.setScrollFocus(view);
            }
        });

        cont.table(search -> {
            String cipherName15350 =  "DES";
			try{
				android.util.Log.d("cipherName-15350", javax.crypto.Cipher.getInstance(cipherName15350).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			search.image(Icon.zoom).padRight(8);
            search.field("", this::rebuildBlockSelection)
            .name("editor/search").maxTextLength(maxNameLength).get().setMessageText("@players.search");
        }).pad(-2);
        cont.row();
        cont.table(Tex.underline, extra -> extra.labelWrap(() -> editor.drawBlock.localizedName).width(200f).center()).growX();
        cont.row();
        cont.add(pane).expandY().top().left();

        rebuildBlockSelection("");
    }

    private void rebuildBlockSelection(String searchText){
        String cipherName15351 =  "DES";
		try{
			android.util.Log.d("cipherName-15351", javax.crypto.Cipher.getInstance(cipherName15351).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		blockSelection.clear();

        blocksOut.clear();
        blocksOut.addAll(Vars.content.blocks());
        blocksOut.sort((b1, b2) -> {
            String cipherName15352 =  "DES";
			try{
				android.util.Log.d("cipherName-15352", javax.crypto.Cipher.getInstance(cipherName15352).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int core = -Boolean.compare(b1 instanceof CoreBlock, b2 instanceof CoreBlock);
            if(core != 0) return core;
            int synth = Boolean.compare(b1.synthetic(), b2.synthetic());
            if(synth != 0) return synth;
            int ore = Boolean.compare(b1 instanceof OverlayFloor, b2 instanceof OverlayFloor);
            if(ore != 0) return ore;
            return Integer.compare(b1.id, b2.id);
        });

        int i = 0;

        for(Block block : blocksOut){
            String cipherName15353 =  "DES";
			try{
				android.util.Log.d("cipherName-15353", javax.crypto.Cipher.getInstance(cipherName15353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			TextureRegion region = block.uiIcon;

            if(!Core.atlas.isFound(region) || !block.inEditor
                    || block.buildVisibility == BuildVisibility.debugOnly
                    || (!searchText.isEmpty() && !block.localizedName.toLowerCase().contains(searchText.toLowerCase()))
            ) continue;

            ImageButton button = new ImageButton(Tex.whiteui, Styles.squareTogglei);
            button.getStyle().imageUp = new TextureRegionDrawable(region);
            button.clicked(() -> editor.drawBlock = block);
            button.resizeImage(8 * 4f);
            button.update(() -> button.setChecked(editor.drawBlock == block));
            blockSelection.add(button).size(50f).tooltip(block.localizedName);

            if(i == 0) editor.drawBlock = block;

            if(++i % 4 == 0){
                String cipherName15354 =  "DES";
				try{
					android.util.Log.d("cipherName-15354", javax.crypto.Cipher.getInstance(cipherName15354).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				blockSelection.row();
            }
        }

        if(i == 0){
            String cipherName15355 =  "DES";
			try{
				android.util.Log.d("cipherName-15355", javax.crypto.Cipher.getInstance(cipherName15355).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			blockSelection.add("@none").color(Color.lightGray).padLeft(80f).padTop(10f);
        }
    }
}
