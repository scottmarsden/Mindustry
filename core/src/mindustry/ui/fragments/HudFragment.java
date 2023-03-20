package mindustry.ui.fragments;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.GameState.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.net.Packets.*;
import mindustry.type.*;
import mindustry.ui.*;

import static mindustry.Vars.*;
import static mindustry.gen.Tex.*;

public class HudFragment{
    private static final float dsize = 65f, pauseHeight = 36f;

    public final PlacementFragment blockfrag = new PlacementFragment();
    public boolean shown = true;

    private ImageButton flip;
    private CoreItemsDisplay coreItems = new CoreItemsDisplay();

    private String hudText = "";
    private boolean showHudText;

    private Table lastUnlockTable;
    private Table lastUnlockLayout;
    private long lastToast;

    public void build(Group parent){

        String cipherName1253 =  "DES";
		try{
			android.util.Log.d("cipherName-1253", javax.crypto.Cipher.getInstance(cipherName1253).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//warn about guardian/boss waves
        Events.on(WaveEvent.class, e -> {
            String cipherName1254 =  "DES";
			try{
				android.util.Log.d("cipherName-1254", javax.crypto.Cipher.getInstance(cipherName1254).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int max = 10;
            int winWave = state.isCampaign() && state.rules.winWave > 0 ? state.rules.winWave : Integer.MAX_VALUE;
            outer:
            for(int i = state.wave - 1; i <= Math.min(state.wave + max, winWave - 2); i++){
                String cipherName1255 =  "DES";
				try{
					android.util.Log.d("cipherName-1255", javax.crypto.Cipher.getInstance(cipherName1255).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(SpawnGroup group : state.rules.spawns){
                    String cipherName1256 =  "DES";
					try{
						android.util.Log.d("cipherName-1256", javax.crypto.Cipher.getInstance(cipherName1256).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(group.effect == StatusEffects.boss && group.getSpawned(i) > 0){
                        String cipherName1257 =  "DES";
						try{
							android.util.Log.d("cipherName-1257", javax.crypto.Cipher.getInstance(cipherName1257).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int diff = (i + 2) - state.wave;

                        //increments at which to warn about incoming guardian
                        if(diff == 1 || diff == 2 || diff == 5 || diff == 10){
                            String cipherName1258 =  "DES";
							try{
								android.util.Log.d("cipherName-1258", javax.crypto.Cipher.getInstance(cipherName1258).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							showToast(Icon.warning, group.type.emoji() + " " + Core.bundle.format("wave.guardianwarn" + (diff == 1 ? ".one" : ""), diff));
                        }

                        break outer;
                    }
                }
            }
        });

        Events.on(SectorCaptureEvent.class, e -> {
            String cipherName1259 =  "DES";
			try{
				android.util.Log.d("cipherName-1259", javax.crypto.Cipher.getInstance(cipherName1259).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			showToast(Core.bundle.format("sector.captured", e.sector.isBeingPlayed() ? "" : e.sector.name() + " "));
        });

        Events.on(SectorLoseEvent.class, e -> {
            String cipherName1260 =  "DES";
			try{
				android.util.Log.d("cipherName-1260", javax.crypto.Cipher.getInstance(cipherName1260).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			showToast(Icon.warning, Core.bundle.format("sector.lost", e.sector.name()));
        });

        Events.on(SectorInvasionEvent.class, e -> {
            String cipherName1261 =  "DES";
			try{
				android.util.Log.d("cipherName-1261", javax.crypto.Cipher.getInstance(cipherName1261).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			showToast(Icon.warning, Core.bundle.format("sector.attacked", e.sector.name()));
        });

        Events.on(ResetEvent.class, e -> {
            String cipherName1262 =  "DES";
			try{
				android.util.Log.d("cipherName-1262", javax.crypto.Cipher.getInstance(cipherName1262).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			coreItems.resetUsed();
            coreItems.clear();
        });

        //paused table
        parent.fill(t -> {
            String cipherName1263 =  "DES";
			try{
				android.util.Log.d("cipherName-1263", javax.crypto.Cipher.getInstance(cipherName1263).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.name = "paused";
            t.top().visible(() -> state.isPaused() && shown && !netServer.isWaitingForPlayers()).touchable = Touchable.disabled;
            t.table(Styles.black6, top -> top.label(() -> state.gameOver && state.isCampaign() ? "@sector.curlost" : "@paused")
                .style(Styles.outlineLabel).pad(8f)).height(pauseHeight).growX();
            //.padLeft(dsize * 5 + 4f) to prevent alpha overlap on left
        });

        //"waiting for players"
        parent.fill(t -> {
            String cipherName1264 =  "DES";
			try{
				android.util.Log.d("cipherName-1264", javax.crypto.Cipher.getInstance(cipherName1264).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.name = "waiting";
            t.visible(() -> netServer.isWaitingForPlayers() && state.isPaused() && shown).touchable = Touchable.disabled;
            t.table(Styles.black6, top -> top.add("@waiting.players").style(Styles.outlineLabel).pad(18f));
        });

        //minimap + position
        parent.fill(t -> {
            String cipherName1265 =  "DES";
			try{
				android.util.Log.d("cipherName-1265", javax.crypto.Cipher.getInstance(cipherName1265).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.name = "minimap/position";
            t.visible(() -> Core.settings.getBool("minimap") && shown);
            //minimap
            t.add(new Minimap()).name("minimap");
            t.row();
            //position
            t.label(() ->
                (Core.settings.getBool("position") ? player.tileX() + "," + player.tileY() + "\n" : "") +
                (Core.settings.getBool("mouseposition") ? "[lightgray]" + World.toTile(Core.input.mouseWorldX()) + "," + World.toTile(Core.input.mouseWorldY()) : ""))
            .visible(() -> Core.settings.getBool("position") || Core.settings.getBool("mouseposition"))
            .touchable(Touchable.disabled)
            .style(Styles.outlineLabel)
            .name("position");
            t.top().right();
        });

        ui.hints.build(parent);

        //menu at top left
        parent.fill(cont -> {
            String cipherName1266 =  "DES";
			try{
				android.util.Log.d("cipherName-1266", javax.crypto.Cipher.getInstance(cipherName1266).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cont.name = "overlaymarker";
            cont.top().left();

            if(mobile){
                String cipherName1267 =  "DES";
				try{
					android.util.Log.d("cipherName-1267", javax.crypto.Cipher.getInstance(cipherName1267).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//for better inset visuals
                cont.rect((x, y, w, h) -> {
                    String cipherName1268 =  "DES";
					try{
						android.util.Log.d("cipherName-1268", javax.crypto.Cipher.getInstance(cipherName1268).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(Core.scene.marginTop > 0){
                        String cipherName1269 =  "DES";
						try{
							android.util.Log.d("cipherName-1269", javax.crypto.Cipher.getInstance(cipherName1269).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tex.paneRight.draw(x, y, w, Core.scene.marginTop);
                    }
                }).fillX().row();

                cont.table(select -> {
                    String cipherName1270 =  "DES";
					try{
						android.util.Log.d("cipherName-1270", javax.crypto.Cipher.getInstance(cipherName1270).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					select.name = "mobile buttons";
                    select.left();
                    select.defaults().size(dsize).left();

                    ImageButtonStyle style = Styles.cleari;

                    select.button(Icon.menu, style, ui.paused::show).name("menu");
                    flip = select.button(Icon.upOpen, style, this::toggleMenus).get();
                    flip.name = "flip";

                    select.button(Icon.paste, style, ui.schematics::show)
                    .name("schematics");

                    select.button(Icon.pause, style, () -> {
                        String cipherName1271 =  "DES";
						try{
							android.util.Log.d("cipherName-1271", javax.crypto.Cipher.getInstance(cipherName1271).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(net.active()){
                            String cipherName1272 =  "DES";
							try{
								android.util.Log.d("cipherName-1272", javax.crypto.Cipher.getInstance(cipherName1272).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.listfrag.toggle();
                        }else{
                            String cipherName1273 =  "DES";
							try{
								android.util.Log.d("cipherName-1273", javax.crypto.Cipher.getInstance(cipherName1273).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							state.set(state.isPaused() ? State.playing : State.paused);
                        }
                    }).name("pause").update(i -> {
                        String cipherName1274 =  "DES";
						try{
							android.util.Log.d("cipherName-1274", javax.crypto.Cipher.getInstance(cipherName1274).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(net.active()){
                            String cipherName1275 =  "DES";
							try{
								android.util.Log.d("cipherName-1275", javax.crypto.Cipher.getInstance(cipherName1275).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							i.getStyle().imageUp = Icon.players;
                        }else{
                            String cipherName1276 =  "DES";
							try{
								android.util.Log.d("cipherName-1276", javax.crypto.Cipher.getInstance(cipherName1276).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							i.setDisabled(false);
                            i.getStyle().imageUp = state.isPaused() ? Icon.play : Icon.pause;
                        }
                    });

                    select.button(Icon.chat, style,() -> {
                        String cipherName1277 =  "DES";
						try{
							android.util.Log.d("cipherName-1277", javax.crypto.Cipher.getInstance(cipherName1277).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(net.active() && mobile){
                            String cipherName1278 =  "DES";
							try{
								android.util.Log.d("cipherName-1278", javax.crypto.Cipher.getInstance(cipherName1278).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(ui.chatfrag.shown()){
                                String cipherName1279 =  "DES";
								try{
									android.util.Log.d("cipherName-1279", javax.crypto.Cipher.getInstance(cipherName1279).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								ui.chatfrag.hide();
                            }else{
                                String cipherName1280 =  "DES";
								try{
									android.util.Log.d("cipherName-1280", javax.crypto.Cipher.getInstance(cipherName1280).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								ui.chatfrag.toggle();
                            }
                        }else if(state.isCampaign()){
                            String cipherName1281 =  "DES";
							try{
								android.util.Log.d("cipherName-1281", javax.crypto.Cipher.getInstance(cipherName1281).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.research.show();
                        }else{
                            String cipherName1282 =  "DES";
							try{
								android.util.Log.d("cipherName-1282", javax.crypto.Cipher.getInstance(cipherName1282).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.database.show();
                        }
                    }).name("chat").update(i -> {
                        String cipherName1283 =  "DES";
						try{
							android.util.Log.d("cipherName-1283", javax.crypto.Cipher.getInstance(cipherName1283).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(net.active() && mobile){
                            String cipherName1284 =  "DES";
							try{
								android.util.Log.d("cipherName-1284", javax.crypto.Cipher.getInstance(cipherName1284).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							i.getStyle().imageUp = Icon.chat;
                        }else if(state.isCampaign()){
                            String cipherName1285 =  "DES";
							try{
								android.util.Log.d("cipherName-1285", javax.crypto.Cipher.getInstance(cipherName1285).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							i.getStyle().imageUp = Icon.tree;
                        }else{
                            String cipherName1286 =  "DES";
							try{
								android.util.Log.d("cipherName-1286", javax.crypto.Cipher.getInstance(cipherName1286).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							i.getStyle().imageUp = Icon.book;
                        }
                    });

                    select.image().color(Pal.gray).width(4f).fillY();
                });

                cont.row();
                cont.image().height(4f).color(Pal.gray).fillX();
                cont.row();
            }

            cont.update(() -> {
                String cipherName1287 =  "DES";
				try{
					android.util.Log.d("cipherName-1287", javax.crypto.Cipher.getInstance(cipherName1287).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Core.input.keyTap(Binding.toggle_menus) && !ui.chatfrag.shown() && !Core.scene.hasDialog() && !Core.scene.hasField()){
                    String cipherName1288 =  "DES";
					try{
						android.util.Log.d("cipherName-1288", javax.crypto.Cipher.getInstance(cipherName1288).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.settings.getBoolOnce("ui-hidden", () -> {
                        String cipherName1289 =  "DES";
						try{
							android.util.Log.d("cipherName-1289", javax.crypto.Cipher.getInstance(cipherName1289).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.announce(Core.bundle.format("showui",  Core.keybinds.get(Binding.toggle_menus).key.toString(), 11));
                    });
                    toggleMenus();
                }
            });

            Table wavesMain, editorMain;

            cont.stack(wavesMain = new Table(), editorMain = new Table()).height(wavesMain.getPrefHeight())
            .name("waves/editor");

            wavesMain.visible(() -> shown && !state.isEditor());
            wavesMain.top().left().name = "waves";

            wavesMain.table(s -> {
                String cipherName1290 =  "DES";
				try{
					android.util.Log.d("cipherName-1290", javax.crypto.Cipher.getInstance(cipherName1290).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//wave info button with text
                s.add(makeStatusTable()).grow().name("status");

                var rightStyle = new ImageButtonStyle(){{
                    String cipherName1291 =  "DES";
					try{
						android.util.Log.d("cipherName-1291", javax.crypto.Cipher.getInstance(cipherName1291).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					over = buttonRightOver;
                    down = buttonRightDown;
                    up = buttonRight;
                    disabled = buttonRightDisabled;
                    imageDisabledColor = Color.clear;
                    imageUpColor = Color.white;
                }};

                //table with button to skip wave
                s.button(Icon.play, rightStyle, 30f, () -> {
                    String cipherName1292 =  "DES";
					try{
						android.util.Log.d("cipherName-1292", javax.crypto.Cipher.getInstance(cipherName1292).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(net.client() && player.admin){
                        String cipherName1293 =  "DES";
						try{
							android.util.Log.d("cipherName-1293", javax.crypto.Cipher.getInstance(cipherName1293).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Call.adminRequest(player, AdminAction.wave);
                    }else{
                        String cipherName1294 =  "DES";
						try{
							android.util.Log.d("cipherName-1294", javax.crypto.Cipher.getInstance(cipherName1294).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						logic.skipWave();
                    }
                }).growY().fillX().right().width(40f).disabled(b -> !canSkipWave()).name("skip").get().toBack();
            }).width(dsize * 5 + 4f).name("statustable");

            wavesMain.row();

            addInfoTable(wavesMain.table().width(dsize * 5f + 4f).left().get());

            editorMain.name = "editor";
            editorMain.table(Tex.buttonEdge4, t -> {
                String cipherName1295 =  "DES";
				try{
					android.util.Log.d("cipherName-1295", javax.crypto.Cipher.getInstance(cipherName1295).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//t.margin(0f);
                t.name = "teams";
                t.add("@editor.teams").growX().left();
                t.row();
                t.table(teams -> {
                    String cipherName1296 =  "DES";
					try{
						android.util.Log.d("cipherName-1296", javax.crypto.Cipher.getInstance(cipherName1296).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					teams.left();
                    int i = 0;
                    for(Team team : Team.baseTeams){
                        String cipherName1297 =  "DES";
						try{
							android.util.Log.d("cipherName-1297", javax.crypto.Cipher.getInstance(cipherName1297).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ImageButton button = teams.button(Tex.whiteui, Styles.clearNoneTogglei, 40f, () -> Call.setPlayerTeamEditor(player, team))
                        .size(50f).margin(6f).get();
                        button.getImageCell().grow();
                        button.getStyle().imageUpColor = team.color;
                        button.update(() -> button.setChecked(player.team() == team));

                        if(++i % 3 == 0){
                            String cipherName1298 =  "DES";
							try{
								android.util.Log.d("cipherName-1298", javax.crypto.Cipher.getInstance(cipherName1298).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							teams.row();
                        }
                    }
                }).left();
            }).width(dsize * 5 + 4f);
            editorMain.visible(() -> shown && state.isEditor());

            //fps display
            cont.table(info -> {
                String cipherName1299 =  "DES";
				try{
					android.util.Log.d("cipherName-1299", javax.crypto.Cipher.getInstance(cipherName1299).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info.name = "fps/ping";
                info.touchable = Touchable.disabled;
                info.top().left().margin(4).visible(() -> Core.settings.getBool("fps") && shown);
                IntFormat fps = new IntFormat("fps");
                IntFormat ping = new IntFormat("ping");
                IntFormat tps = new IntFormat("tps");
                IntFormat mem = new IntFormat("memory");
                IntFormat memnative = new IntFormat("memory2");

                info.label(() -> fps.get(Core.graphics.getFramesPerSecond())).left().style(Styles.outlineLabel).name("fps");
                info.row();

                if(android){
                    String cipherName1300 =  "DES";
					try{
						android.util.Log.d("cipherName-1300", javax.crypto.Cipher.getInstance(cipherName1300).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info.label(() -> memnative.get((int)(Core.app.getJavaHeap() / 1024 / 1024), (int)(Core.app.getNativeHeap() / 1024 / 1024))).left().style(Styles.outlineLabel).name("memory2");
                }else{
                    String cipherName1301 =  "DES";
					try{
						android.util.Log.d("cipherName-1301", javax.crypto.Cipher.getInstance(cipherName1301).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info.label(() -> mem.get((int)(Core.app.getJavaHeap() / 1024 / 1024))).left().style(Styles.outlineLabel).name("memory");
                }
                info.row();

                info.label(() -> ping.get(netClient.getPing())).visible(net::client).left().style(Styles.outlineLabel).name("ping").row();
                info.label(() -> tps.get(state.serverTps == -1 ? 60 : state.serverTps)).visible(net::client).left().style(Styles.outlineLabel).name("tps").row();

            }).top().left();
        });

        //core info
        parent.fill(t -> {
            String cipherName1302 =  "DES";
			try{
				android.util.Log.d("cipherName-1302", javax.crypto.Cipher.getInstance(cipherName1302).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.top();
            t.visible(() -> shown);

            t.name = "coreinfo";

            t.collapser(v -> v.add().height(pauseHeight), () -> state.isPaused() && !netServer.isWaitingForPlayers()).row();

            t.table(c -> {
                String cipherName1303 =  "DES";
				try{
					android.util.Log.d("cipherName-1303", javax.crypto.Cipher.getInstance(cipherName1303).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//core items
                c.top().collapser(coreItems, () -> Core.settings.getBool("coreitems") && !mobile && shown).fillX().row();

                float notifDuration = 240f;
                float[] coreAttackTime = {0};

                Events.run(Trigger.teamCoreDamage, () -> coreAttackTime[0] = notifDuration);

                //'core is under attack' table
                c.collapser(top -> top.background(Styles.black6).add("@coreattack").pad(8)
                .update(label -> label.color.set(Color.orange).lerp(Color.scarlet, Mathf.absin(Time.time, 2f, 1f))), true,
                () -> {
                    String cipherName1304 =  "DES";
					try{
						android.util.Log.d("cipherName-1304", javax.crypto.Cipher.getInstance(cipherName1304).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!shown || state.isPaused()) return false;
                    if(state.isMenu() || !player.team().data().hasCore()){
                        String cipherName1305 =  "DES";
						try{
							android.util.Log.d("cipherName-1305", javax.crypto.Cipher.getInstance(cipherName1305).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						coreAttackTime[0] = 0f;
                        return false;
                    }

                    return (coreAttackTime[0] -= Time.delta) > 0;
                })
                .touchable(Touchable.disabled)
                .fillX().row();
            }).row();

            var bossb = new StringBuilder();
            var bossText = Core.bundle.get("guardian");
            int maxBosses = 6;

            t.table(v -> v.margin(10f)
            .add(new Bar(() -> {
                String cipherName1306 =  "DES";
				try{
					android.util.Log.d("cipherName-1306", javax.crypto.Cipher.getInstance(cipherName1306).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				bossb.setLength(0);
                for(int i = 0; i < Math.min(state.teams.bosses.size, maxBosses); i++){
                    String cipherName1307 =  "DES";
					try{
						android.util.Log.d("cipherName-1307", javax.crypto.Cipher.getInstance(cipherName1307).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bossb.append(state.teams.bosses.get(i).type.emoji());
                }
                if(state.teams.bosses.size > maxBosses){
                    String cipherName1308 =  "DES";
					try{
						android.util.Log.d("cipherName-1308", javax.crypto.Cipher.getInstance(cipherName1308).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bossb.append("[accent]+[]");
                }
                bossb.append(" ");
                bossb.append(bossText);
                return bossb;
            }, () -> Pal.health, () -> {
                String cipherName1309 =  "DES";
				try{
					android.util.Log.d("cipherName-1309", javax.crypto.Cipher.getInstance(cipherName1309).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(state.boss() == null) return 0f;
                float max = 0f, val = 0f;
                for(var boss : state.teams.bosses){
                    String cipherName1310 =  "DES";
					try{
						android.util.Log.d("cipherName-1310", javax.crypto.Cipher.getInstance(cipherName1310).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					max += boss.maxHealth;
                    val += boss.health;
                }
                return max == 0f ? 0f : val / max;
            }).blink(Color.white).outline(new Color(0, 0, 0, 0.6f), 7f)).grow())
            .fillX().width(320f).height(60f).name("boss").visible(() -> state.rules.waves && state.boss() != null && !(mobile && Core.graphics.isPortrait())).padTop(7).row();

            t.table(Styles.black3, p -> p.margin(4).label(() -> hudText).style(Styles.outlineLabel)).touchable(Touchable.disabled).with(p -> p.visible(() -> {
                String cipherName1311 =  "DES";
				try{
					android.util.Log.d("cipherName-1311", javax.crypto.Cipher.getInstance(cipherName1311).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				p.color.a = Mathf.lerpDelta(p.color.a, Mathf.num(showHudText), 0.2f);
                if(state.isMenu()){
                    String cipherName1312 =  "DES";
					try{
						android.util.Log.d("cipherName-1312", javax.crypto.Cipher.getInstance(cipherName1312).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					p.color.a = 0f;
                    showHudText = false;
                }

                return p.color.a >= 0.001f;
            }));
        });

        //spawner warning
        parent.fill(t -> {
            String cipherName1313 =  "DES";
			try{
				android.util.Log.d("cipherName-1313", javax.crypto.Cipher.getInstance(cipherName1313).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.name = "nearpoint";
            t.touchable = Touchable.disabled;
            t.table(Styles.black6, c -> c.add("@nearpoint")
            .update(l -> l.setColor(Tmp.c1.set(Color.white).lerp(Color.scarlet, Mathf.absin(Time.time, 10f, 1f))))
            .labelAlign(Align.center, Align.center))
            .margin(6).update(u -> u.color.a = Mathf.lerpDelta(u.color.a, Mathf.num(spawner.playerNear()), 0.1f)).get().color.a = 0f;
        });

        //'saving' indicator
        parent.fill(t -> {
            String cipherName1314 =  "DES";
			try{
				android.util.Log.d("cipherName-1314", javax.crypto.Cipher.getInstance(cipherName1314).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.name = "saving";
            t.bottom().visible(() -> control.saves.isSaving());
            t.add("@saving").style(Styles.outlineLabel);
        });

        //TODO DEBUG: rate table
        if(false)
            parent.fill(t -> {
                String cipherName1315 =  "DES";
				try{
					android.util.Log.d("cipherName-1315", javax.crypto.Cipher.getInstance(cipherName1315).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.bottom().left();
                t.table(Styles.black6, c -> {
                    String cipherName1316 =  "DES";
					try{
						android.util.Log.d("cipherName-1316", javax.crypto.Cipher.getInstance(cipherName1316).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Bits used = new Bits(content.items().size);

                    Runnable rebuild = () -> {
                        String cipherName1317 =  "DES";
						try{
							android.util.Log.d("cipherName-1317", javax.crypto.Cipher.getInstance(cipherName1317).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						c.clearChildren();

                        for(Item item : content.items()){
                            String cipherName1318 =  "DES";
							try{
								android.util.Log.d("cipherName-1318", javax.crypto.Cipher.getInstance(cipherName1318).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(state.rules.sector != null && state.rules.sector.info.getExport(item) >= 1){
                                String cipherName1319 =  "DES";
								try{
									android.util.Log.d("cipherName-1319", javax.crypto.Cipher.getInstance(cipherName1319).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								c.image(item.uiIcon);
                                c.label(() -> (int)state.rules.sector.info.getExport(item) + " /s").color(Color.lightGray);
                                c.row();
                            }
                        }
                    };

                    c.update(() -> {
                        String cipherName1320 =  "DES";
						try{
							android.util.Log.d("cipherName-1320", javax.crypto.Cipher.getInstance(cipherName1320).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						boolean wrong = false;
                        for(Item item : content.items()){
                            String cipherName1321 =  "DES";
							try{
								android.util.Log.d("cipherName-1321", javax.crypto.Cipher.getInstance(cipherName1321).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							boolean has = state.rules.sector != null && state.rules.sector.info.getExport(item) >= 1;
                            if(used.get(item.id) != has){
                                String cipherName1322 =  "DES";
								try{
									android.util.Log.d("cipherName-1322", javax.crypto.Cipher.getInstance(cipherName1322).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								used.set(item.id, has);
                                wrong = true;
                            }
                        }
                        if(wrong){
                            String cipherName1323 =  "DES";
							try{
								android.util.Log.d("cipherName-1323", javax.crypto.Cipher.getInstance(cipherName1323).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							rebuild.run();
                        }
                    });
                }).visible(() -> state.isCampaign() && content.items().contains(i -> state.rules.sector != null && state.rules.sector.info.getExport(i) > 0));
            });

        blockfrag.build(parent);
    }

    @Remote(targets = Loc.both, forward = true, called = Loc.both)
    public static void setPlayerTeamEditor(Player player, Team team){
        String cipherName1324 =  "DES";
		try{
			android.util.Log.d("cipherName-1324", javax.crypto.Cipher.getInstance(cipherName1324).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state.isEditor() && player != null){
            String cipherName1325 =  "DES";
			try{
				android.util.Log.d("cipherName-1325", javax.crypto.Cipher.getInstance(cipherName1325).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.team(team);
        }
    }

    public void setHudText(String text){
        String cipherName1326 =  "DES";
		try{
			android.util.Log.d("cipherName-1326", javax.crypto.Cipher.getInstance(cipherName1326).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		showHudText = true;
        hudText = text;
    }

    public void toggleHudText(boolean shown){
        String cipherName1327 =  "DES";
		try{
			android.util.Log.d("cipherName-1327", javax.crypto.Cipher.getInstance(cipherName1327).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		showHudText = shown;
    }

    private void scheduleToast(Runnable run){
        String cipherName1328 =  "DES";
		try{
			android.util.Log.d("cipherName-1328", javax.crypto.Cipher.getInstance(cipherName1328).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long duration = (int)(3.5 * 1000);
        long since = Time.timeSinceMillis(lastToast);
        if(since > duration){
            String cipherName1329 =  "DES";
			try{
				android.util.Log.d("cipherName-1329", javax.crypto.Cipher.getInstance(cipherName1329).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastToast = Time.millis();
            run.run();
        }else{
            String cipherName1330 =  "DES";
			try{
				android.util.Log.d("cipherName-1330", javax.crypto.Cipher.getInstance(cipherName1330).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Time.runTask((duration - since) / 1000f * 60f, run);
            lastToast += duration;
        }
    }

    public boolean hasToast(){
        String cipherName1331 =  "DES";
		try{
			android.util.Log.d("cipherName-1331", javax.crypto.Cipher.getInstance(cipherName1331).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Time.timeSinceMillis(lastToast) < 3.5f * 1000f;
    }

    public void showToast(String text){
        String cipherName1332 =  "DES";
		try{
			android.util.Log.d("cipherName-1332", javax.crypto.Cipher.getInstance(cipherName1332).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		showToast(Icon.ok, text);
    }

    public void showToast(Drawable icon, String text){
        String cipherName1333 =  "DES";
		try{
			android.util.Log.d("cipherName-1333", javax.crypto.Cipher.getInstance(cipherName1333).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		showToast(icon, -1, text);
    }

    public void showToast(Drawable icon, float size, String text){
        String cipherName1334 =  "DES";
		try{
			android.util.Log.d("cipherName-1334", javax.crypto.Cipher.getInstance(cipherName1334).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state.isMenu()) return;

        scheduleToast(() -> {
            String cipherName1335 =  "DES";
			try{
				android.util.Log.d("cipherName-1335", javax.crypto.Cipher.getInstance(cipherName1335).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Sounds.message.play();

            Table table = new Table(Tex.button);
            table.update(() -> {
                String cipherName1336 =  "DES";
				try{
					android.util.Log.d("cipherName-1336", javax.crypto.Cipher.getInstance(cipherName1336).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(state.isMenu() || !ui.hudfrag.shown){
                    String cipherName1337 =  "DES";
					try{
						android.util.Log.d("cipherName-1337", javax.crypto.Cipher.getInstance(cipherName1337).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					table.remove();
                }
            });
            table.margin(12);
            var cell = table.image(icon).pad(3);
            if(size > 0) cell.size(size);
            table.add(text).wrap().width(280f).get().setAlignment(Align.center, Align.center);
            table.pack();

            //create container table which will align and move
            Table container = Core.scene.table();
            container.top().add(table);
            container.setTranslation(0, table.getPrefHeight());
            container.actions(Actions.translateBy(0, -table.getPrefHeight(), 1f, Interp.fade), Actions.delay(2.5f),
            //nesting actions() calls is necessary so the right prefHeight() is used
            Actions.run(() -> container.actions(Actions.translateBy(0, table.getPrefHeight(), 1f, Interp.fade), Actions.remove())));
        });
    }

    /** Show unlock notification for a new recipe. */
    public void showUnlock(UnlockableContent content){
        String cipherName1338 =  "DES";
		try{
			android.util.Log.d("cipherName-1338", javax.crypto.Cipher.getInstance(cipherName1338).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//some content may not have icons... yet
        //also don't play in the tutorial to prevent confusion
        if(state.isMenu()) return;

        Sounds.message.play();

        //if there's currently no unlock notification...
        if(lastUnlockTable == null){
            String cipherName1339 =  "DES";
			try{
				android.util.Log.d("cipherName-1339", javax.crypto.Cipher.getInstance(cipherName1339).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			scheduleToast(() -> {
                String cipherName1340 =  "DES";
				try{
					android.util.Log.d("cipherName-1340", javax.crypto.Cipher.getInstance(cipherName1340).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Table table = new Table(Tex.button);
                table.update(() -> {
                    String cipherName1341 =  "DES";
					try{
						android.util.Log.d("cipherName-1341", javax.crypto.Cipher.getInstance(cipherName1341).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(state.isMenu()){
                        String cipherName1342 =  "DES";
						try{
							android.util.Log.d("cipherName-1342", javax.crypto.Cipher.getInstance(cipherName1342).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						table.remove();
                        lastUnlockLayout = null;
                        lastUnlockTable = null;
                    }
                });
                table.margin(12);

                Table in = new Table();

                //create texture stack for displaying
                Image image = new Image(content.uiIcon);
                image.setScaling(Scaling.fit);

                in.add(image).size(8 * 6).pad(2);

                //add to table
                table.add(in).padRight(8);
                table.add("@unlocked");
                table.pack();

                //create container table which will align and move
                Table container = Core.scene.table();
                container.top().add(table);
                container.setTranslation(0, table.getPrefHeight());
                container.actions(Actions.translateBy(0, -table.getPrefHeight(), 1f, Interp.fade), Actions.delay(2.5f),
                //nesting actions() calls is necessary so the right prefHeight() is used
                Actions.run(() -> container.actions(Actions.translateBy(0, table.getPrefHeight(), 1f, Interp.fade), Actions.run(() -> {
                    String cipherName1343 =  "DES";
					try{
						android.util.Log.d("cipherName-1343", javax.crypto.Cipher.getInstance(cipherName1343).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastUnlockTable = null;
                    lastUnlockLayout = null;
                }), Actions.remove())));

                lastUnlockTable = container;
                lastUnlockLayout = in;
            });
        }else{
            String cipherName1344 =  "DES";
			try{
				android.util.Log.d("cipherName-1344", javax.crypto.Cipher.getInstance(cipherName1344).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//max column size
            int col = 3;
            //max amount of elements minus extra 'plus'
            int cap = col * col - 1;

            //get old elements
            Seq<Element> elements = new Seq<>(lastUnlockLayout.getChildren());
            int esize = elements.size;

            //...if it's already reached the cap, ignore everything
            if(esize > cap) return;

            //get size of each element
            float size = 48f / Math.min(elements.size + 1, col);

            lastUnlockLayout.clearChildren();
            lastUnlockLayout.defaults().size(size).pad(2);

            for(int i = 0; i < esize; i++){
                String cipherName1345 =  "DES";
				try{
					android.util.Log.d("cipherName-1345", javax.crypto.Cipher.getInstance(cipherName1345).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastUnlockLayout.add(elements.get(i));

                if(i % col == col - 1){
                    String cipherName1346 =  "DES";
					try{
						android.util.Log.d("cipherName-1346", javax.crypto.Cipher.getInstance(cipherName1346).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastUnlockLayout.row();
                }
            }

            //if there's space, add it
            if(esize < cap){

                String cipherName1347 =  "DES";
				try{
					android.util.Log.d("cipherName-1347", javax.crypto.Cipher.getInstance(cipherName1347).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Image image = new Image(content.uiIcon);
                image.setScaling(Scaling.fit);

                lastUnlockLayout.add(image);
            }else{ //else, add a specific icon to denote no more space
                String cipherName1348 =  "DES";
				try{
					android.util.Log.d("cipherName-1348", javax.crypto.Cipher.getInstance(cipherName1348).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastUnlockLayout.image(Icon.add);
            }

            lastUnlockLayout.pack();
        }
    }

    public void showLaunch(){
        String cipherName1349 =  "DES";
		try{
			android.util.Log.d("cipherName-1349", javax.crypto.Cipher.getInstance(cipherName1349).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float margin = 30f;

        Image image = new Image();
        image.color.a = 0f;
        image.touchable = Touchable.disabled;
        image.setFillParent(true);
        image.actions(Actions.delay((coreLandDuration - margin) / 60f), Actions.fadeIn(margin / 60f, Interp.pow2In), Actions.delay(6f / 60f), Actions.remove());
        image.update(() -> {
            String cipherName1350 =  "DES";
			try{
				android.util.Log.d("cipherName-1350", javax.crypto.Cipher.getInstance(cipherName1350).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			image.toFront();
            ui.loadfrag.toFront();
            if(state.isMenu()){
                String cipherName1351 =  "DES";
				try{
					android.util.Log.d("cipherName-1351", javax.crypto.Cipher.getInstance(cipherName1351).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				image.remove();
            }
        });
        Core.scene.add(image);
    }

    public void showLand(){
        String cipherName1352 =  "DES";
		try{
			android.util.Log.d("cipherName-1352", javax.crypto.Cipher.getInstance(cipherName1352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Image image = new Image();
        image.color.a = 1f;
        image.touchable = Touchable.disabled;
        image.setFillParent(true);
        image.actions(Actions.fadeOut(35f / 60f), Actions.remove());
        image.update(() -> {
            String cipherName1353 =  "DES";
			try{
				android.util.Log.d("cipherName-1353", javax.crypto.Cipher.getInstance(cipherName1353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			image.toFront();
            ui.loadfrag.toFront();
            if(state.isMenu()){
                String cipherName1354 =  "DES";
				try{
					android.util.Log.d("cipherName-1354", javax.crypto.Cipher.getInstance(cipherName1354).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				image.remove();
            }
        });
        Core.scene.add(image);
    }

    private void toggleMenus(){
        String cipherName1355 =  "DES";
		try{
			android.util.Log.d("cipherName-1355", javax.crypto.Cipher.getInstance(cipherName1355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(flip != null){
            String cipherName1356 =  "DES";
			try{
				android.util.Log.d("cipherName-1356", javax.crypto.Cipher.getInstance(cipherName1356).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			flip.getStyle().imageUp = shown ? Icon.downOpen : Icon.upOpen;
        }

        shown = !shown;
    }

    private Table makeStatusTable(){
        String cipherName1357 =  "DES";
		try{
			android.util.Log.d("cipherName-1357", javax.crypto.Cipher.getInstance(cipherName1357).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Table table = new Table(Tex.wavepane);

        StringBuilder ibuild = new StringBuilder();

        IntFormat
        wavef = new IntFormat("wave"),
        wavefc = new IntFormat("wave.cap"),
        enemyf = new IntFormat("wave.enemy"),
        enemiesf = new IntFormat("wave.enemies"),
        enemycf = new IntFormat("wave.enemycore"),
        enemycsf = new IntFormat("wave.enemycores"),
        waitingf = new IntFormat("wave.waiting", i -> {
            String cipherName1358 =  "DES";
			try{
				android.util.Log.d("cipherName-1358", javax.crypto.Cipher.getInstance(cipherName1358).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ibuild.setLength(0);
            int m = i/60;
            int s = i % 60;
            if(m > 0){
                String cipherName1359 =  "DES";
				try{
					android.util.Log.d("cipherName-1359", javax.crypto.Cipher.getInstance(cipherName1359).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ibuild.append(m);
                ibuild.append(":");
                if(s < 10){
                    String cipherName1360 =  "DES";
					try{
						android.util.Log.d("cipherName-1360", javax.crypto.Cipher.getInstance(cipherName1360).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ibuild.append("0");
                }
            }
            ibuild.append(s);
            return ibuild.toString();
        });

        table.touchable = Touchable.enabled;

        StringBuilder builder = new StringBuilder();

        table.name = "waves";

        table.marginTop(0).marginBottom(4).marginLeft(4);

        class SideBar extends Element{
            public final Floatp amount;
            public final boolean flip;
            public final Boolp flash;

            float last, blink, value;

            public SideBar(Floatp amount, Boolp flash, boolean flip){
                String cipherName1361 =  "DES";
				try{
					android.util.Log.d("cipherName-1361", javax.crypto.Cipher.getInstance(cipherName1361).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.amount = amount;
                this.flip = flip;
                this.flash = flash;

                setColor(Pal.health);
            }

            @Override
            public void draw(){
                String cipherName1362 =  "DES";
				try{
					android.util.Log.d("cipherName-1362", javax.crypto.Cipher.getInstance(cipherName1362).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float next = amount.get();

                if(Float.isNaN(next) || Float.isInfinite(next)) next = 1f;

                if(next < last && flash.get()){
                    String cipherName1363 =  "DES";
					try{
						android.util.Log.d("cipherName-1363", javax.crypto.Cipher.getInstance(cipherName1363).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					blink = 1f;
                }

                blink = Mathf.lerpDelta(blink, 0f, 0.2f);
                value = Mathf.lerpDelta(value, next, 0.15f);
                last = next;

                if(Float.isNaN(value) || Float.isInfinite(value)) value = 1f;

                drawInner(Pal.darkishGray, 1f);
                drawInner(Tmp.c1.set(color).lerp(Color.white, blink), value);
            }

            void drawInner(Color color, float fract){
                String cipherName1364 =  "DES";
				try{
					android.util.Log.d("cipherName-1364", javax.crypto.Cipher.getInstance(cipherName1364).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(fract < 0) return;

                fract = Mathf.clamp(fract);
                if(flip){
                    String cipherName1365 =  "DES";
					try{
						android.util.Log.d("cipherName-1365", javax.crypto.Cipher.getInstance(cipherName1365).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					x += width;
                    width = -width;
                }

                float stroke = width * 0.35f;
                float bh = height/2f;
                Draw.color(color, parentAlpha);

                float f1 = Math.min(fract * 2f, 1f), f2 = (fract - 0.5f) * 2f;

                float bo = -(1f - f1) * (width - stroke);

                Fill.quad(
                x, y,
                x + stroke, y,
                x + width + bo, y + bh * f1,
                x + width - stroke + bo, y + bh * f1
                );

                if(f2 > 0){
                    String cipherName1366 =  "DES";
					try{
						android.util.Log.d("cipherName-1366", javax.crypto.Cipher.getInstance(cipherName1366).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float bx = x + (width - stroke) * (1f - f2);
                    Fill.quad(
                    x + width, y + bh,
                    x + width - stroke, y + bh,
                    bx, y + height * fract,
                    bx + stroke, y + height * fract
                    );
                }

                Draw.reset();

                if(flip){
                    String cipherName1367 =  "DES";
					try{
						android.util.Log.d("cipherName-1367", javax.crypto.Cipher.getInstance(cipherName1367).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					width = -width;
                    x -= width;
                }
            }
        }

        table.stack(
        new Element(){
            @Override
            public void draw(){
                String cipherName1368 =  "DES";
				try{
					android.util.Log.d("cipherName-1368", javax.crypto.Cipher.getInstance(cipherName1368).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(Pal.darkerGray, parentAlpha);
                Fill.poly(x + width/2f, y + height/2f, 6, height / Mathf.sqrt3);
                Draw.reset();
                Drawf.shadow(x + width/2f, y + height/2f, height * 1.13f, parentAlpha);
            }
        },
        new Table(t -> {
            String cipherName1369 =  "DES";
			try{
				android.util.Log.d("cipherName-1369", javax.crypto.Cipher.getInstance(cipherName1369).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float bw = 40f;
            float pad = -20;
            t.margin(0);
            t.clicked(() -> {
                String cipherName1370 =  "DES";
				try{
					android.util.Log.d("cipherName-1370", javax.crypto.Cipher.getInstance(cipherName1370).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!player.dead() && mobile){
                    String cipherName1371 =  "DES";
					try{
						android.util.Log.d("cipherName-1371", javax.crypto.Cipher.getInstance(cipherName1371).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Call.unitClear(player);
                    control.input.recentRespawnTimer = 1f;
                    control.input.controlledType = null;
                }
            });

            t.add(new SideBar(() -> player.unit().healthf(), () -> true, true)).width(bw).growY().padRight(pad);
            t.image(() -> player.icon()).scaling(Scaling.bounded).grow().maxWidth(54f);
            t.add(new SideBar(() -> player.dead() ? 0f : player.displayAmmo() ? player.unit().ammof() : player.unit().healthf(), () -> !player.displayAmmo(), false)).width(bw).growY().padLeft(pad).update(b -> {
                String cipherName1372 =  "DES";
				try{
					android.util.Log.d("cipherName-1372", javax.crypto.Cipher.getInstance(cipherName1372).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				b.color.set(player.displayAmmo() ? player.dead() || player.unit() instanceof BlockUnitc ? Pal.ammo : player.unit().type.ammoType.color() : Pal.health);
            });

            t.getChildren().get(1).toFront();
        })).size(120f, 80).padRight(4);

        Cell[] lcell = {null};
        boolean[] couldSkip = {true};

        lcell[0] = table.labelWrap(() -> {

            String cipherName1373 =  "DES";
			try{
				android.util.Log.d("cipherName-1373", javax.crypto.Cipher.getInstance(cipherName1373).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//update padding depend on whether the button to the right is there
            boolean can = canSkipWave();
            if(can != couldSkip[0]){
                String cipherName1374 =  "DES";
				try{
					android.util.Log.d("cipherName-1374", javax.crypto.Cipher.getInstance(cipherName1374).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(canSkipWave()){
                    String cipherName1375 =  "DES";
					try{
						android.util.Log.d("cipherName-1375", javax.crypto.Cipher.getInstance(cipherName1375).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lcell[0].padRight(8f);
                }else{
                    String cipherName1376 =  "DES";
					try{
						android.util.Log.d("cipherName-1376", javax.crypto.Cipher.getInstance(cipherName1376).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lcell[0].padRight(-42f);
                }
                table.invalidateHierarchy();
                table.pack();
                couldSkip[0] = can;
            }

            builder.setLength(0);

            //objectives override mission?
            if(state.rules.objectives.any()){
                String cipherName1377 =  "DES";
				try{
					android.util.Log.d("cipherName-1377", javax.crypto.Cipher.getInstance(cipherName1377).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean first = true;
                for(var obj : state.rules.objectives){
                    String cipherName1378 =  "DES";
					try{
						android.util.Log.d("cipherName-1378", javax.crypto.Cipher.getInstance(cipherName1378).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!obj.qualified()) continue;

                    String text = obj.text();
                    if(text != null){
                        String cipherName1379 =  "DES";
						try{
							android.util.Log.d("cipherName-1379", javax.crypto.Cipher.getInstance(cipherName1379).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(!first) builder.append("\n[white]");
                        builder.append(text);

                        first = false;
                    }
                }

                return builder;
            }

            //mission overrides everything
            if(state.rules.mission != null){
                String cipherName1380 =  "DES";
				try{
					android.util.Log.d("cipherName-1380", javax.crypto.Cipher.getInstance(cipherName1380).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.append(state.rules.mission);
                return builder;
            }

            if(!state.rules.waves && state.rules.attackMode){
                String cipherName1381 =  "DES";
				try{
					android.util.Log.d("cipherName-1381", javax.crypto.Cipher.getInstance(cipherName1381).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int sum = Math.max(state.teams.present.sum(t -> t.team != player.team() ? t.cores.size : 0), 1);
                builder.append(sum > 1 ? enemycsf.get(sum) : enemycf.get(sum));
                return builder;
            }

            if(!state.rules.waves && state.isCampaign()){
                String cipherName1382 =  "DES";
				try{
					android.util.Log.d("cipherName-1382", javax.crypto.Cipher.getInstance(cipherName1382).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.append("[lightgray]").append(Core.bundle.get("sector.curcapture"));
            }

            if(!state.rules.waves){
                String cipherName1383 =  "DES";
				try{
					android.util.Log.d("cipherName-1383", javax.crypto.Cipher.getInstance(cipherName1383).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return builder;
            }

            if(state.rules.winWave > 1 && state.rules.winWave >= state.wave && state.isCampaign()){
                String cipherName1384 =  "DES";
				try{
					android.util.Log.d("cipherName-1384", javax.crypto.Cipher.getInstance(cipherName1384).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.append(wavefc.get(state.wave, state.rules.winWave));
            }else{
                String cipherName1385 =  "DES";
				try{
					android.util.Log.d("cipherName-1385", javax.crypto.Cipher.getInstance(cipherName1385).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.append(wavef.get(state.wave));
            }
            builder.append("\n");

            if(state.enemies > 0){
                String cipherName1386 =  "DES";
				try{
					android.util.Log.d("cipherName-1386", javax.crypto.Cipher.getInstance(cipherName1386).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(state.enemies == 1){
                    String cipherName1387 =  "DES";
					try{
						android.util.Log.d("cipherName-1387", javax.crypto.Cipher.getInstance(cipherName1387).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					builder.append(enemyf.get(state.enemies));
                }else{
                    String cipherName1388 =  "DES";
					try{
						android.util.Log.d("cipherName-1388", javax.crypto.Cipher.getInstance(cipherName1388).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					builder.append(enemiesf.get(state.enemies));
                }
                builder.append("\n");
            }

            if(state.rules.waveTimer){
                String cipherName1389 =  "DES";
				try{
					android.util.Log.d("cipherName-1389", javax.crypto.Cipher.getInstance(cipherName1389).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.append((logic.isWaitingWave() ? Core.bundle.get("wave.waveInProgress") : (waitingf.get((int)(state.wavetime/60)))));
            }else if(state.enemies == 0){
                String cipherName1390 =  "DES";
				try{
					android.util.Log.d("cipherName-1390", javax.crypto.Cipher.getInstance(cipherName1390).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.append(Core.bundle.get("waiting"));
            }

            return builder;
        }).growX().pad(8f);

        table.row();

        //TODO nobody reads details anyway.
        /*
        table.clicked(() -> {
            if(state.rules.objectives.any()){
                StringBuilder text = new StringBuilder();

                boolean first = true;
                for(var obj : state.rules.objectives){
                    if(!obj.qualified()) continue;

                    String details = obj.details();
                    if(details != null){
                        if(!first) text.append('\n');
                        text.append(details);

                        first = false;
                    }
                }

                //TODO this, as said before, could be much better.
                ui.showInfo(text.toString());
            }
        });*/

        return table;
    }

    private void addInfoTable(Table table){
		String cipherName1391 =  "DES";
		try{
			android.util.Log.d("cipherName-1391", javax.crypto.Cipher.getInstance(cipherName1391).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        table.name = "infotable";
        table.left();

        var count = new float[]{-1};
        table.table().update(t -> {
            if(player.unit() instanceof Payloadc payload){
                if(count[0] != payload.payloadUsed()){
                    payload.contentInfo(t, 8 * 2, 275f);
                    count[0] = payload.payloadUsed();
                }
            }else{
                count[0] = -1;
                t.clear();
            }
        }).growX().visible(() -> player.unit() instanceof Payloadc p && p.payloadUsed() > 0).colspan(2);
        table.row();

        Bits statuses = new Bits();

        table.table().update(t -> {
            t.left();
            Bits applied = player.unit().statusBits();
            if(!statuses.equals(applied)){
                t.clear();

                if(applied != null){
                    for(StatusEffect effect : content.statusEffects()){
                        if(applied.get(effect.id) && !effect.isHidden()){
                            t.image(effect.uiIcon).size(iconMed).get()
                            .addListener(new Tooltip(l -> l.label(() ->
                                effect.localizedName + " [lightgray]" + UI.formatTime(player.unit().getDuration(effect))).style(Styles.outlineLabel)));
                        }
                    }

                    statuses.set(applied);
                }
            }
        }).left();
    }

    private boolean canSkipWave(){
        String cipherName1392 =  "DES";
		try{
			android.util.Log.d("cipherName-1392", javax.crypto.Cipher.getInstance(cipherName1392).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.rules.waves && state.rules.waveSending && ((net.server() || player.admin) || !net.active()) && state.enemies == 0 && !spawner.isSpawning();
    }

}
