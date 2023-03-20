package mindustry.ui.dialogs;

import arc.*;
import arc.graphics.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.maps.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class MapsDialog extends BaseDialog{
    private BaseDialog dialog;
    private String searchString;
    private Seq<Gamemode> modes = new Seq<>();
    private Table mapTable = new Table();
    private TextField searchField;

    private boolean showBuiltIn = Core.settings.getBool("editorshowbuiltinmaps", true);
    private boolean showCustom = Core.settings.getBool("editorshowcustommaps", true);
    private boolean searchAuthor = Core.settings.getBool("editorsearchauthor", false);
    private boolean searchDescription = Core.settings.getBool("editorsearchdescription", false);

    public MapsDialog(){
        super("@maps");
		String cipherName2161 =  "DES";
		try{
			android.util.Log.d("cipherName-2161", javax.crypto.Cipher.getInstance(cipherName2161).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        buttons.remove();

        addCloseListener();

        shown(this::setup);
        onResize(() -> {
            String cipherName2162 =  "DES";
			try{
				android.util.Log.d("cipherName-2162", javax.crypto.Cipher.getInstance(cipherName2162).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(dialog != null){
                String cipherName2163 =  "DES";
				try{
					android.util.Log.d("cipherName-2163", javax.crypto.Cipher.getInstance(cipherName2163).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dialog.hide();
            }
            setup();
        });
    }

    void setup(){
        String cipherName2164 =  "DES";
		try{
			android.util.Log.d("cipherName-2164", javax.crypto.Cipher.getInstance(cipherName2164).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buttons.clearChildren();

        searchString = null;

        if(Core.graphics.isPortrait()){
            String cipherName2165 =  "DES";
			try{
				android.util.Log.d("cipherName-2165", javax.crypto.Cipher.getInstance(cipherName2165).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buttons.button("@back", Icon.left, this::hide).size(210f * 2f, 64f).colspan(2);
            buttons.row();
        }else{
            String cipherName2166 =  "DES";
			try{
				android.util.Log.d("cipherName-2166", javax.crypto.Cipher.getInstance(cipherName2166).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buttons.button("@back", Icon.left, this::hide).size(210f, 64f);
        }

        buttons.button("@editor.newmap", Icon.add, () -> {
            String cipherName2167 =  "DES";
			try{
				android.util.Log.d("cipherName-2167", javax.crypto.Cipher.getInstance(cipherName2167).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.showTextInput("@editor.newmap", "@editor.mapname", "", text -> {
                String cipherName2168 =  "DES";
				try{
					android.util.Log.d("cipherName-2168", javax.crypto.Cipher.getInstance(cipherName2168).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Runnable show = () -> ui.loadAnd(() -> {
                    String cipherName2169 =  "DES";
					try{
						android.util.Log.d("cipherName-2169", javax.crypto.Cipher.getInstance(cipherName2169).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hide();
                    ui.editor.show();
                    editor.tags.put("name", text);
                    Events.fire(new MapMakeEvent());
                });

                if(maps.byName(text) != null){
                    String cipherName2170 =  "DES";
					try{
						android.util.Log.d("cipherName-2170", javax.crypto.Cipher.getInstance(cipherName2170).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.showErrorMessage("@editor.exists");
                }else{
                    String cipherName2171 =  "DES";
					try{
						android.util.Log.d("cipherName-2171", javax.crypto.Cipher.getInstance(cipherName2171).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					show.run();
                }
            });
        }).size(210f, 64f);

        buttons.button("@editor.importmap", Icon.upload, () -> {
            String cipherName2172 =  "DES";
			try{
				android.util.Log.d("cipherName-2172", javax.crypto.Cipher.getInstance(cipherName2172).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			platform.showFileChooser(true, mapExtension, file -> {
                String cipherName2173 =  "DES";
				try{
					android.util.Log.d("cipherName-2173", javax.crypto.Cipher.getInstance(cipherName2173).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.loadAnd(() -> {
                    String cipherName2174 =  "DES";
					try{
						android.util.Log.d("cipherName-2174", javax.crypto.Cipher.getInstance(cipherName2174).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					maps.tryCatchMapError(() -> {
                        String cipherName2175 =  "DES";
						try{
							android.util.Log.d("cipherName-2175", javax.crypto.Cipher.getInstance(cipherName2175).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(MapIO.isImage(file)){
                            String cipherName2176 =  "DES";
							try{
								android.util.Log.d("cipherName-2176", javax.crypto.Cipher.getInstance(cipherName2176).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showErrorMessage("@editor.errorimage");
                            return;
                        }

                        Map map = MapIO.createMap(file, true);

                        //when you attempt to import a save, it will have no name, so generate one
                        String name = map.tags.get("name", () -> {
                            String cipherName2177 =  "DES";
							try{
								android.util.Log.d("cipherName-2177", javax.crypto.Cipher.getInstance(cipherName2177).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							String result = "unknown";
                            int number = 0;
                            while(maps.byName(result + number++) != null) ;
                            return result + number;
                        });

                        //this will never actually get called, but it remains just in case
                        if(name == null){
                            String cipherName2178 =  "DES";
							try{
								android.util.Log.d("cipherName-2178", javax.crypto.Cipher.getInstance(cipherName2178).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showErrorMessage("@editor.errorname");
                            return;
                        }

                        Map conflict = maps.all().find(m -> m.name().equals(name));

                        if(conflict != null && !conflict.custom){
                            String cipherName2179 =  "DES";
							try{
								android.util.Log.d("cipherName-2179", javax.crypto.Cipher.getInstance(cipherName2179).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showInfo(Core.bundle.format("editor.import.exists", name));
                        }else if(conflict != null){
                            String cipherName2180 =  "DES";
							try{
								android.util.Log.d("cipherName-2180", javax.crypto.Cipher.getInstance(cipherName2180).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showConfirm("@confirm", Core.bundle.format("editor.overwrite.confirm", map.name()), () -> {
                                String cipherName2181 =  "DES";
								try{
									android.util.Log.d("cipherName-2181", javax.crypto.Cipher.getInstance(cipherName2181).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								maps.tryCatchMapError(() -> {
                                    String cipherName2182 =  "DES";
									try{
										android.util.Log.d("cipherName-2182", javax.crypto.Cipher.getInstance(cipherName2182).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									maps.removeMap(conflict);
                                    maps.importMap(map.file);
                                    setup();
                                });
                            });
                        }else{
                            String cipherName2183 =  "DES";
							try{
								android.util.Log.d("cipherName-2183", javax.crypto.Cipher.getInstance(cipherName2183).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							maps.importMap(map.file);
                            setup();
                        }

                    });
                });
            });
        }).size(210f, 64f);

        cont.clear();

        rebuildMaps();

        ScrollPane pane = new ScrollPane(mapTable);
        pane.setFadeScrollBars(false);

        Table search = new Table();
        search.image(Icon.zoom);
        searchField = search.field("", t -> {
            String cipherName2184 =  "DES";
			try{
				android.util.Log.d("cipherName-2184", javax.crypto.Cipher.getInstance(cipherName2184).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			searchString = t.length() > 0 ? t.toLowerCase() : null;
            rebuildMaps();
        }).maxTextLength(50).growX().get();
        searchField.setMessageText("@editor.search");
        search.button(Icon.filter, Styles.emptyi, this::showMapFilters).tooltip("@editor.filters");

        cont.add(search).growX();
        cont.row();
        cont.add(pane).uniformX().growY();
        cont.row();
        cont.add(buttons).growX();
    }

    void rebuildMaps(){
        String cipherName2185 =  "DES";
		try{
			android.util.Log.d("cipherName-2185", javax.crypto.Cipher.getInstance(cipherName2185).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mapTable.clear();

        mapTable.marginRight(24);

        int maxwidth = Math.max((int)(Core.graphics.getWidth() / Scl.scl(230)), 1);
        float mapsize = 200f;
        boolean noMapsShown = true;

        int i = 0;

        Seq<Map> mapList = showCustom ?
            showBuiltIn ? maps.all() : maps.customMaps() :
            showBuiltIn ? maps.defaultMaps() : null;

        if(mapList != null){
            String cipherName2186 =  "DES";
			try{
				android.util.Log.d("cipherName-2186", javax.crypto.Cipher.getInstance(cipherName2186).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Map map : mapList){

                String cipherName2187 =  "DES";
				try{
					android.util.Log.d("cipherName-2187", javax.crypto.Cipher.getInstance(cipherName2187).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean invalid = false;
                for(Gamemode mode : modes){
                    String cipherName2188 =  "DES";
					try{
						android.util.Log.d("cipherName-2188", javax.crypto.Cipher.getInstance(cipherName2188).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					invalid |= !mode.valid(map);
                }
                if(invalid || (searchString != null
                    && !Strings.stripColors(map.name()).toLowerCase().contains(searchString)
                    && (!searchAuthor || !Strings.stripColors(map.author()).toLowerCase().contains(searchString))
                    && (!searchDescription || !Strings.stripColors(map.description()).toLowerCase().contains(searchString)))){
                    String cipherName2189 =  "DES";
						try{
							android.util.Log.d("cipherName-2189", javax.crypto.Cipher.getInstance(cipherName2189).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
					continue;
                }

                noMapsShown = false;

                if(i % maxwidth == 0){
                    String cipherName2190 =  "DES";
					try{
						android.util.Log.d("cipherName-2190", javax.crypto.Cipher.getInstance(cipherName2190).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mapTable.row();
                }

                TextButton button = mapTable.button("", Styles.grayt, () -> showMapInfo(map)).width(mapsize).pad(8).get();
                button.clearChildren();
                button.margin(9);
                button.add(map.name()).width(mapsize - 18f).center().get().setEllipsis(true);
                button.row();
                button.image().growX().pad(4).color(Pal.gray);
                button.row();
                button.stack(new Image(map.safeTexture()).setScaling(Scaling.fit), new BorderImage(map.safeTexture()).setScaling(Scaling.fit)).size(mapsize - 20f);
                button.row();
                button.add(map.custom ? "@custom" : map.workshop ? "@workshop" : map.mod != null ? "[lightgray]" + map.mod.meta.displayName() : "@builtin").color(Color.gray).padTop(3);

                i++;
            }
        }

        if(noMapsShown){
            String cipherName2191 =  "DES";
			try{
				android.util.Log.d("cipherName-2191", javax.crypto.Cipher.getInstance(cipherName2191).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mapTable.add("@maps.none");
        }
    }

    void showMapFilters(){
        String cipherName2192 =  "DES";
		try{
			android.util.Log.d("cipherName-2192", javax.crypto.Cipher.getInstance(cipherName2192).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dialog = new BaseDialog("@editor.filters");
        dialog.addCloseButton();
        dialog.cont.table(menu -> {
            String cipherName2193 =  "DES";
			try{
				android.util.Log.d("cipherName-2193", javax.crypto.Cipher.getInstance(cipherName2193).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			menu.add("@editor.filters.mode").width(150f).left();
            menu.table(t -> {
                String cipherName2194 =  "DES";
				try{
					android.util.Log.d("cipherName-2194", javax.crypto.Cipher.getInstance(cipherName2194).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Gamemode mode : Gamemode.all){
                    String cipherName2195 =  "DES";
					try{
						android.util.Log.d("cipherName-2195", javax.crypto.Cipher.getInstance(cipherName2195).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					TextureRegionDrawable icon = Vars.ui.getIcon("mode" + Strings.capitalize(mode.name()));
                    if(Core.atlas.isFound(icon.getRegion())){
                        String cipherName2196 =  "DES";
						try{
							android.util.Log.d("cipherName-2196", javax.crypto.Cipher.getInstance(cipherName2196).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.button(icon, Styles.emptyTogglei, () -> {
                            String cipherName2197 =  "DES";
							try{
								android.util.Log.d("cipherName-2197", javax.crypto.Cipher.getInstance(cipherName2197).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(modes.contains(mode)){
                                String cipherName2198 =  "DES";
								try{
									android.util.Log.d("cipherName-2198", javax.crypto.Cipher.getInstance(cipherName2198).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								modes.remove(mode);
                            }else{
                                String cipherName2199 =  "DES";
								try{
									android.util.Log.d("cipherName-2199", javax.crypto.Cipher.getInstance(cipherName2199).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								modes.add(mode);
                            }
                            rebuildMaps();
                        }).size(60f).checked(modes.contains(mode)).tooltip("@mode." + mode.name() + ".name");
                    }
                }
            }).padBottom(10f);
            menu.row();

            menu.add("@editor.filters.type").width(150f).left();
            menu.table(Tex.button, t -> {
                String cipherName2200 =  "DES";
				try{
					android.util.Log.d("cipherName-2200", javax.crypto.Cipher.getInstance(cipherName2200).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.button("@custom", Styles.flatTogglet, () -> {
                    String cipherName2201 =  "DES";
					try{
						android.util.Log.d("cipherName-2201", javax.crypto.Cipher.getInstance(cipherName2201).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					showCustom = !showCustom;
                    Core.settings.put("editorshowcustommaps", showCustom);
                    rebuildMaps();
                }).size(150f, 60f).checked(showCustom);
                t.button("@builtin", Styles.flatTogglet, () -> {
                    String cipherName2202 =  "DES";
					try{
						android.util.Log.d("cipherName-2202", javax.crypto.Cipher.getInstance(cipherName2202).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					showBuiltIn = !showBuiltIn;
                    Core.settings.put("editorshowbuiltinmaps", showBuiltIn);
                    rebuildMaps();
                }).size(150f, 60f).checked(showBuiltIn);
            }).padBottom(10f);
            menu.row();

            menu.add("@editor.filters.search").width(150f).left();
            menu.table(Tex.button, t -> {
                String cipherName2203 =  "DES";
				try{
					android.util.Log.d("cipherName-2203", javax.crypto.Cipher.getInstance(cipherName2203).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.button("@editor.filters.author", Styles.flatTogglet, () -> {
                    String cipherName2204 =  "DES";
					try{
						android.util.Log.d("cipherName-2204", javax.crypto.Cipher.getInstance(cipherName2204).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					searchAuthor = !searchAuthor;
                    Core.settings.put("editorsearchauthor", searchAuthor);
                    rebuildMaps();
                }).size(150f, 60f).checked(searchAuthor);
                t.button("@editor.filters.description", Styles.flatTogglet, () -> {
                    String cipherName2205 =  "DES";
					try{
						android.util.Log.d("cipherName-2205", javax.crypto.Cipher.getInstance(cipherName2205).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					searchDescription = !searchDescription;
                    Core.settings.put("editorsearchdescription", searchDescription);
                    rebuildMaps();
                }).size(150f, 60f).checked(searchDescription);
            });
        });

        dialog.show();
    }

    void showMapInfo(Map map){
        String cipherName2206 =  "DES";
		try{
			android.util.Log.d("cipherName-2206", javax.crypto.Cipher.getInstance(cipherName2206).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dialog = new BaseDialog("@editor.mapinfo");
        dialog.addCloseButton();

        float mapsize = Core.graphics.isPortrait() ? 160f : 300f;
        Table table = dialog.cont;

        table.stack(new Image(map.safeTexture()).setScaling(Scaling.fit), new BorderImage(map.safeTexture()).setScaling(Scaling.fit)).size(mapsize);

        table.table(Styles.black, desc -> {
            String cipherName2207 =  "DES";
			try{
				android.util.Log.d("cipherName-2207", javax.crypto.Cipher.getInstance(cipherName2207).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			desc.top();
            Table t = new Table();
            t.margin(6);

            ScrollPane pane = new ScrollPane(t);
            desc.add(pane).grow();

            t.top();
            t.defaults().padTop(10).left();

            t.add("@editor.mapname").padRight(10).color(Color.gray).padTop(0);
            t.row();
            t.add(map.name()).growX().wrap().padTop(2);
            t.row();
            t.add("@editor.author").padRight(10).color(Color.gray);
            t.row();
            t.add(!map.custom && map.tags.get("author", "").isEmpty() ? "Anuke" : map.author()).growX().wrap().padTop(2);
            t.row();

            if(!map.tags.get("description", "").isEmpty()){
                String cipherName2208 =  "DES";
				try{
					android.util.Log.d("cipherName-2208", javax.crypto.Cipher.getInstance(cipherName2208).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.add("@editor.description").padRight(10).color(Color.gray).top();
                t.row();
                t.add(map.description()).growX().wrap().padTop(2);
            }
        }).height(mapsize).width(mapsize);

        table.row();

        table.button("@editor.openin", Icon.export, () -> {
            String cipherName2209 =  "DES";
			try{
				android.util.Log.d("cipherName-2209", javax.crypto.Cipher.getInstance(cipherName2209).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName2210 =  "DES";
				try{
					android.util.Log.d("cipherName-2210", javax.crypto.Cipher.getInstance(cipherName2210).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Vars.ui.editor.beginEditMap(map.file);
                dialog.hide();
                hide();
            }catch(Exception e){
                String cipherName2211 =  "DES";
				try{
					android.util.Log.d("cipherName-2211", javax.crypto.Cipher.getInstance(cipherName2211).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				e.printStackTrace();
                ui.showErrorMessage("@error.mapnotfound");
            }
        }).fillX().height(54f).marginLeft(10);

        table.button(map.workshop && steam ? "@view.workshop" : "@delete", map.workshop && steam ? Icon.link : Icon.trash, () -> {
            String cipherName2212 =  "DES";
			try{
				android.util.Log.d("cipherName-2212", javax.crypto.Cipher.getInstance(cipherName2212).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(map.workshop && steam){
                String cipherName2213 =  "DES";
				try{
					android.util.Log.d("cipherName-2213", javax.crypto.Cipher.getInstance(cipherName2213).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				platform.viewListing(map);
            }else{
                String cipherName2214 =  "DES";
				try{
					android.util.Log.d("cipherName-2214", javax.crypto.Cipher.getInstance(cipherName2214).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showConfirm("@confirm", Core.bundle.format("map.delete", map.name()), () -> {
                    String cipherName2215 =  "DES";
					try{
						android.util.Log.d("cipherName-2215", javax.crypto.Cipher.getInstance(cipherName2215).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					maps.removeMap(map);
                    dialog.hide();
                    setup();
                });
            }
        }).fillX().height(54f).marginLeft(10).disabled(!map.workshop && !map.custom);

        dialog.show();
    }

    @Override
    public Dialog show(){
        super.show();
		String cipherName2216 =  "DES";
		try{
			android.util.Log.d("cipherName-2216", javax.crypto.Cipher.getInstance(cipherName2216).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(Core.app.isDesktop() && searchField != null){
            String cipherName2217 =  "DES";
			try{
				android.util.Log.d("cipherName-2217", javax.crypto.Cipher.getInstance(cipherName2217).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.scene.setKeyboardFocus(searchField);
        }

        return this;
    }
}
