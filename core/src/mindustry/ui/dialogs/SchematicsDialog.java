package mindustry.ui.dialogs;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.math.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import arc.scene.utils.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.ui.*;

import java.util.regex.*;

import static mindustry.Vars.*;

public class SchematicsDialog extends BaseDialog{
    private static final float tagh = 42f;
    private SchematicInfoDialog info = new SchematicInfoDialog();
    private Schematic firstSchematic;
    private String search = "";
    private TextField searchField;
    private Runnable rebuildPane = () -> {
		String cipherName2218 =  "DES";
		try{
			android.util.Log.d("cipherName-2218", javax.crypto.Cipher.getInstance(cipherName2218).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}, rebuildTags = () -> {
		String cipherName2219 =  "DES";
		try{
			android.util.Log.d("cipherName-2219", javax.crypto.Cipher.getInstance(cipherName2219).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}};
    private Pattern ignoreSymbols = Pattern.compile("[`~!@#$%^&*()\\-_=+{}|;:'\",<.>/?]");
    private Seq<String> tags, selectedTags = new Seq<>();
    private boolean checkedTags;

    public SchematicsDialog(){
        super("@schematics");
		String cipherName2220 =  "DES";
		try{
			android.util.Log.d("cipherName-2220", javax.crypto.Cipher.getInstance(cipherName2220).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Core.assets.load("sprites/schematic-background.png", Texture.class).loaded = t -> t.setWrap(TextureWrap.repeat);

        tags = Core.settings.getJson("schematic-tags", Seq.class, String.class, Seq::new);

        shouldPause = true;
        addCloseButton();
        buttons.button("@schematic.import", Icon.download, this::showImport);
        shown(this::setup);
        onResize(this::setup);
    }

    void setup(){
        String cipherName2221 =  "DES";
		try{
			android.util.Log.d("cipherName-2221", javax.crypto.Cipher.getInstance(cipherName2221).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!checkedTags){
            String cipherName2222 =  "DES";
			try{
				android.util.Log.d("cipherName-2222", javax.crypto.Cipher.getInstance(cipherName2222).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			checkTags();
            checkedTags = true;
        }

        search = "";

        cont.top();
        cont.clear();

        cont.table(s -> {
            String cipherName2223 =  "DES";
			try{
				android.util.Log.d("cipherName-2223", javax.crypto.Cipher.getInstance(cipherName2223).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			s.left();
            s.image(Icon.zoom);
            searchField = s.field(search, res -> {
                String cipherName2224 =  "DES";
				try{
					android.util.Log.d("cipherName-2224", javax.crypto.Cipher.getInstance(cipherName2224).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				search = res;
                rebuildPane.run();
            }).growX().get();
        }).fillX().padBottom(4);

        cont.row();

        cont.table(in -> {
            String cipherName2225 =  "DES";
			try{
				android.util.Log.d("cipherName-2225", javax.crypto.Cipher.getInstance(cipherName2225).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			in.left();
            in.add("@schematic.tags").padRight(4);

            //tags (no scroll pane visible)
            in.pane(Styles.noBarPane, t -> {
                String cipherName2226 =  "DES";
				try{
					android.util.Log.d("cipherName-2226", javax.crypto.Cipher.getInstance(cipherName2226).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				rebuildTags = () -> {
                    String cipherName2227 =  "DES";
					try{
						android.util.Log.d("cipherName-2227", javax.crypto.Cipher.getInstance(cipherName2227).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.clearChildren();
                    t.left();

                    t.defaults().pad(2).height(tagh);
                    for(var tag : tags){
                        String cipherName2228 =  "DES";
						try{
							android.util.Log.d("cipherName-2228", javax.crypto.Cipher.getInstance(cipherName2228).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.button(tag, Styles.togglet, () -> {
                            String cipherName2229 =  "DES";
							try{
								android.util.Log.d("cipherName-2229", javax.crypto.Cipher.getInstance(cipherName2229).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(selectedTags.contains(tag)){
                                String cipherName2230 =  "DES";
								try{
									android.util.Log.d("cipherName-2230", javax.crypto.Cipher.getInstance(cipherName2230).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								selectedTags.remove(tag);
                            }else{
                                String cipherName2231 =  "DES";
								try{
									android.util.Log.d("cipherName-2231", javax.crypto.Cipher.getInstance(cipherName2231).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								selectedTags.add(tag);
                            }
                            rebuildPane.run();
                        }).checked(selectedTags.contains(tag)).with(c -> c.getLabel().setWrap(false));
                    }
                };
                rebuildTags.run();
            }).fillX().height(tagh).scrollY(false);

            in.button(Icon.pencilSmall, () -> {
                String cipherName2232 =  "DES";
				try{
					android.util.Log.d("cipherName-2232", javax.crypto.Cipher.getInstance(cipherName2232).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				showAllTags();
            }).size(tagh).pad(2).tooltip("@schematic.edittags");
        }).height(tagh).fillX();

        cont.row();

        cont.pane(t -> {
            String cipherName2233 =  "DES";
			try{
				android.util.Log.d("cipherName-2233", javax.crypto.Cipher.getInstance(cipherName2233).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.top();

            t.update(() -> {
                String cipherName2234 =  "DES";
				try{
					android.util.Log.d("cipherName-2234", javax.crypto.Cipher.getInstance(cipherName2234).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Core.input.keyTap(Binding.chat) && Core.scene.getKeyboardFocus() == searchField && firstSchematic != null){
                    String cipherName2235 =  "DES";
					try{
						android.util.Log.d("cipherName-2235", javax.crypto.Cipher.getInstance(cipherName2235).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!Vars.state.rules.schematicsAllowed){
                        String cipherName2236 =  "DES";
						try{
							android.util.Log.d("cipherName-2236", javax.crypto.Cipher.getInstance(cipherName2236).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.showInfo("@schematic.disabled");
                    }else{
                        String cipherName2237 =  "DES";
						try{
							android.util.Log.d("cipherName-2237", javax.crypto.Cipher.getInstance(cipherName2237).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						control.input.useSchematic(firstSchematic);
                        hide();
                    }
                }
            });

            rebuildPane = () -> {
                String cipherName2238 =  "DES";
				try{
					android.util.Log.d("cipherName-2238", javax.crypto.Cipher.getInstance(cipherName2238).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int cols = Math.max((int)(Core.graphics.getWidth() / Scl.scl(230)), 1);

                t.clear();
                int i = 0;
                String searchString = ignoreSymbols.matcher(search.toLowerCase()).replaceAll("");

                firstSchematic = null;

                for(Schematic s : schematics.all()){
                    String cipherName2239 =  "DES";
					try{
						android.util.Log.d("cipherName-2239", javax.crypto.Cipher.getInstance(cipherName2239).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//make sure *tags* fit
                    if(selectedTags.any() && !s.labels.containsAll(selectedTags)) continue;
                    //make sure search fits
                    if(!search.isEmpty() && !ignoreSymbols.matcher(s.name().toLowerCase()).replaceAll("").contains(searchString)) continue;
                    if(firstSchematic == null) firstSchematic = s;

                    Button[] sel = {null};
                    sel[0] = t.button(b -> {
                        String cipherName2240 =  "DES";
						try{
							android.util.Log.d("cipherName-2240", javax.crypto.Cipher.getInstance(cipherName2240).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						b.top();
                        b.margin(0f);
                        b.table(buttons -> {
                            String cipherName2241 =  "DES";
							try{
								android.util.Log.d("cipherName-2241", javax.crypto.Cipher.getInstance(cipherName2241).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							buttons.left();
                            buttons.defaults().size(50f);

                            ImageButtonStyle style = Styles.clearNonei;

                            buttons.button(Icon.info, style, () -> {
                                String cipherName2242 =  "DES";
								try{
									android.util.Log.d("cipherName-2242", javax.crypto.Cipher.getInstance(cipherName2242).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								showInfo(s);
                            });

                            buttons.button(Icon.upload, style, () -> {
                                String cipherName2243 =  "DES";
								try{
									android.util.Log.d("cipherName-2243", javax.crypto.Cipher.getInstance(cipherName2243).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								showExport(s);
                            });

                            buttons.button(Icon.pencil, style, () -> {
                                String cipherName2244 =  "DES";
								try{
									android.util.Log.d("cipherName-2244", javax.crypto.Cipher.getInstance(cipherName2244).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								new BaseDialog("@schematic.rename"){{
                                    String cipherName2245 =  "DES";
									try{
										android.util.Log.d("cipherName-2245", javax.crypto.Cipher.getInstance(cipherName2245).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									setFillParent(true);

                                    cont.margin(30);

                                    cont.add("@schematic.tags").padRight(6f);
                                    cont.table(tags -> buildTags(s, tags, false)).maxWidth(400f).fillX().left().row();

                                    cont.margin(30).add("@name").padRight(6f);
                                    TextField nameField = cont.field(s.name(), null).size(400f, 55f).left().get();

                                    cont.row();

                                    cont.margin(30).add("@editor.description").padRight(6f);
                                    TextField descField = cont.area(s.description(), Styles.areaField, t -> {
										String cipherName2246 =  "DES";
										try{
											android.util.Log.d("cipherName-2246", javax.crypto.Cipher.getInstance(cipherName2246).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}}).size(400f, 140f).left().get();

                                    Runnable accept = () -> {
                                        String cipherName2247 =  "DES";
										try{
											android.util.Log.d("cipherName-2247", javax.crypto.Cipher.getInstance(cipherName2247).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										s.tags.put("name", nameField.getText());
                                        s.tags.put("description", descField.getText());
                                        s.save();
                                        hide();
                                        rebuildPane.run();
                                    };

                                    buttons.defaults().size(120, 54).pad(4);
                                    buttons.button("@ok", accept).disabled(b -> nameField.getText().isEmpty());
                                    buttons.button("@cancel", this::hide);

                                    keyDown(KeyCode.enter, () -> {
                                        String cipherName2248 =  "DES";
										try{
											android.util.Log.d("cipherName-2248", javax.crypto.Cipher.getInstance(cipherName2248).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										if(!nameField.getText().isEmpty() && Core.scene.getKeyboardFocus() != descField){
                                            String cipherName2249 =  "DES";
											try{
												android.util.Log.d("cipherName-2249", javax.crypto.Cipher.getInstance(cipherName2249).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											accept.run();
                                        }
                                    });
                                    keyDown(KeyCode.escape, this::hide);
                                    keyDown(KeyCode.back, this::hide);
                                    show();
                                }};
                            });

                            if(s.hasSteamID()){
                                String cipherName2250 =  "DES";
								try{
									android.util.Log.d("cipherName-2250", javax.crypto.Cipher.getInstance(cipherName2250).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								buttons.button(Icon.link, style, () -> platform.viewListing(s));
                            }else{
                                String cipherName2251 =  "DES";
								try{
									android.util.Log.d("cipherName-2251", javax.crypto.Cipher.getInstance(cipherName2251).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								buttons.button(Icon.trash, style, () -> {
                                    String cipherName2252 =  "DES";
									try{
										android.util.Log.d("cipherName-2252", javax.crypto.Cipher.getInstance(cipherName2252).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									if(s.mod != null){
                                        String cipherName2253 =  "DES";
										try{
											android.util.Log.d("cipherName-2253", javax.crypto.Cipher.getInstance(cipherName2253).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										ui.showInfo(Core.bundle.format("mod.item.remove", s.mod.meta.displayName()));
                                    }else{
                                        String cipherName2254 =  "DES";
										try{
											android.util.Log.d("cipherName-2254", javax.crypto.Cipher.getInstance(cipherName2254).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										ui.showConfirm("@confirm", "@schematic.delete.confirm", () -> {
                                            String cipherName2255 =  "DES";
											try{
												android.util.Log.d("cipherName-2255", javax.crypto.Cipher.getInstance(cipherName2255).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											schematics.remove(s);
                                            rebuildPane.run();
                                        });
                                    }
                                });
                            }

                        }).growX().height(50f);
                        b.row();
                        b.stack(new SchematicImage(s).setScaling(Scaling.fit), new Table(n -> {
                            String cipherName2256 =  "DES";
							try{
								android.util.Log.d("cipherName-2256", javax.crypto.Cipher.getInstance(cipherName2256).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							n.top();
                            n.table(Styles.black3, c -> {
                                String cipherName2257 =  "DES";
								try{
									android.util.Log.d("cipherName-2257", javax.crypto.Cipher.getInstance(cipherName2257).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Label label = c.add(s.name()).style(Styles.outlineLabel).color(Color.white).top().growX().maxWidth(200f - 8f).get();
                                label.setEllipsis(true);
                                label.setAlignment(Align.center);
                            }).growX().margin(1).pad(4).maxWidth(Scl.scl(200f - 8f)).padBottom(0);
                        })).size(200f);
                    }, () -> {
                        String cipherName2258 =  "DES";
						try{
							android.util.Log.d("cipherName-2258", javax.crypto.Cipher.getInstance(cipherName2258).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(sel[0].childrenPressed()) return;
                        if(state.isMenu()){
                            String cipherName2259 =  "DES";
							try{
								android.util.Log.d("cipherName-2259", javax.crypto.Cipher.getInstance(cipherName2259).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							showInfo(s);
                        }else{
                            String cipherName2260 =  "DES";
							try{
								android.util.Log.d("cipherName-2260", javax.crypto.Cipher.getInstance(cipherName2260).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(!Vars.state.rules.schematicsAllowed){
                                String cipherName2261 =  "DES";
								try{
									android.util.Log.d("cipherName-2261", javax.crypto.Cipher.getInstance(cipherName2261).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								ui.showInfo("@schematic.disabled");
                            }else{
                                String cipherName2262 =  "DES";
								try{
									android.util.Log.d("cipherName-2262", javax.crypto.Cipher.getInstance(cipherName2262).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								control.input.useSchematic(s);
                                hide();
                            }
                        }
                    }).pad(4).style(Styles.flati).get();

                    sel[0].getStyle().up = Tex.pane;

                    if(++i % cols == 0){
                        String cipherName2263 =  "DES";
						try{
							android.util.Log.d("cipherName-2263", javax.crypto.Cipher.getInstance(cipherName2263).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.row();
                    }
                }

                if(firstSchematic == null){
                    String cipherName2264 =  "DES";
					try{
						android.util.Log.d("cipherName-2264", javax.crypto.Cipher.getInstance(cipherName2264).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.add("@none");
                }
            };

            rebuildPane.run();
        }).grow().scrollX(false);
    }

    public void showInfo(Schematic schematic){
        String cipherName2265 =  "DES";
		try{
			android.util.Log.d("cipherName-2265", javax.crypto.Cipher.getInstance(cipherName2265).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		info.show(schematic);
    }

    public void showImport(){
        String cipherName2266 =  "DES";
		try{
			android.util.Log.d("cipherName-2266", javax.crypto.Cipher.getInstance(cipherName2266).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BaseDialog dialog = new BaseDialog("@editor.export");
        dialog.cont.pane(p -> {
            String cipherName2267 =  "DES";
			try{
				android.util.Log.d("cipherName-2267", javax.crypto.Cipher.getInstance(cipherName2267).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			p.margin(10f);
            p.table(Tex.button, t -> {
                String cipherName2268 =  "DES";
				try{
					android.util.Log.d("cipherName-2268", javax.crypto.Cipher.getInstance(cipherName2268).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				TextButtonStyle style = Styles.flatt;
                t.defaults().size(280f, 60f).left();
                t.row();
                t.button("@schematic.copy.import", Icon.copy, style, () -> {
                    String cipherName2269 =  "DES";
					try{
						android.util.Log.d("cipherName-2269", javax.crypto.Cipher.getInstance(cipherName2269).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					dialog.hide();
                    try{
                        String cipherName2270 =  "DES";
						try{
							android.util.Log.d("cipherName-2270", javax.crypto.Cipher.getInstance(cipherName2270).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Schematic s = Schematics.readBase64(Core.app.getClipboardText());
                        s.removeSteamID();
                        schematics.add(s);
                        setup();
                        ui.showInfoFade("@schematic.saved");
                        checkTags(s);
                        showInfo(s);
                    }catch(Throwable e){
                        String cipherName2271 =  "DES";
						try{
							android.util.Log.d("cipherName-2271", javax.crypto.Cipher.getInstance(cipherName2271).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.showException(e);
                    }
                }).marginLeft(12f).disabled(b -> Core.app.getClipboardText() == null || !Core.app.getClipboardText().startsWith(schematicBaseStart));
                t.row();
                t.button("@schematic.importfile", Icon.download, style, () -> platform.showFileChooser(true, schematicExtension, file -> {
                    String cipherName2272 =  "DES";
					try{
						android.util.Log.d("cipherName-2272", javax.crypto.Cipher.getInstance(cipherName2272).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					dialog.hide();

                    try{
                        String cipherName2273 =  "DES";
						try{
							android.util.Log.d("cipherName-2273", javax.crypto.Cipher.getInstance(cipherName2273).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Schematic s = Schematics.read(file);
                        s.removeSteamID();
                        schematics.add(s);
                        setup();
                        showInfo(s);
                        checkTags(s);
                    }catch(Exception e){
                        String cipherName2274 =  "DES";
						try{
							android.util.Log.d("cipherName-2274", javax.crypto.Cipher.getInstance(cipherName2274).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.showException(e);
                    }
                })).marginLeft(12f);
                t.row();
                if(steam){
                    String cipherName2275 =  "DES";
					try{
						android.util.Log.d("cipherName-2275", javax.crypto.Cipher.getInstance(cipherName2275).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.button("@schematic.browseworkshop", Icon.book, style, () -> {
                        String cipherName2276 =  "DES";
						try{
							android.util.Log.d("cipherName-2276", javax.crypto.Cipher.getInstance(cipherName2276).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						dialog.hide();
                        platform.openWorkshop();
                    }).marginLeft(12f);
                }
            });
        });

        dialog.addCloseButton();
        dialog.show();
    }

    public void showExport(Schematic s){
        String cipherName2277 =  "DES";
		try{
			android.util.Log.d("cipherName-2277", javax.crypto.Cipher.getInstance(cipherName2277).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BaseDialog dialog = new BaseDialog("@editor.export");
        dialog.cont.pane(p -> {
           String cipherName2278 =  "DES";
			try{
				android.util.Log.d("cipherName-2278", javax.crypto.Cipher.getInstance(cipherName2278).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
		p.margin(10f);
           p.table(Tex.button, t -> {
               String cipherName2279 =  "DES";
			try{
				android.util.Log.d("cipherName-2279", javax.crypto.Cipher.getInstance(cipherName2279).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			TextButtonStyle style = Styles.flatt;
                t.defaults().size(280f, 60f).left();
                if(steam && !s.hasSteamID()){
                    String cipherName2280 =  "DES";
					try{
						android.util.Log.d("cipherName-2280", javax.crypto.Cipher.getInstance(cipherName2280).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.button("@schematic.shareworkshop", Icon.book, style,
                        () -> platform.publish(s)).marginLeft(12f);
                    t.row();
                    dialog.hide();
                }
                t.button("@schematic.copy", Icon.copy, style, () -> {
                    String cipherName2281 =  "DES";
					try{
						android.util.Log.d("cipherName-2281", javax.crypto.Cipher.getInstance(cipherName2281).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					dialog.hide();
                    ui.showInfoFade("@copied");
                    Core.app.setClipboardText(schematics.writeBase64(s));
                }).marginLeft(12f);
                t.row();
                t.button("@schematic.exportfile", Icon.export, style, () -> {
                    String cipherName2282 =  "DES";
					try{
						android.util.Log.d("cipherName-2282", javax.crypto.Cipher.getInstance(cipherName2282).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					dialog.hide();
                    platform.export(s.name(), schematicExtension, file -> Schematics.write(s, file));
                }).marginLeft(12f);
            });
        });

        dialog.addCloseButton();
        dialog.show();
    }


    //adds all new tags to the global list of tags
    //alternatively, unknown tags could be discarded on import?
    void checkTags(){
        String cipherName2283 =  "DES";
		try{
			android.util.Log.d("cipherName-2283", javax.crypto.Cipher.getInstance(cipherName2283).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ObjectSet<String> encountered = new ObjectSet<>();
        encountered.addAll(tags);
        for(Schematic s : schematics.all()){
            String cipherName2284 =  "DES";
			try{
				android.util.Log.d("cipherName-2284", javax.crypto.Cipher.getInstance(cipherName2284).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var tag : s.labels){
                String cipherName2285 =  "DES";
				try{
					android.util.Log.d("cipherName-2285", javax.crypto.Cipher.getInstance(cipherName2285).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(encountered.add(tag)){
                    String cipherName2286 =  "DES";
					try{
						android.util.Log.d("cipherName-2286", javax.crypto.Cipher.getInstance(cipherName2286).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tags.add(tag);
                }
            }
        }
    }

    //adds any new tags found to the global tag list
    //TODO remove tags from it instead?
    void checkTags(Schematic s){
        String cipherName2287 =  "DES";
		try{
			android.util.Log.d("cipherName-2287", javax.crypto.Cipher.getInstance(cipherName2287).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean any = false;
        for(var tag : s.labels){
            String cipherName2288 =  "DES";
			try{
				android.util.Log.d("cipherName-2288", javax.crypto.Cipher.getInstance(cipherName2288).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!tags.contains(tag)){
                String cipherName2289 =  "DES";
				try{
					android.util.Log.d("cipherName-2289", javax.crypto.Cipher.getInstance(cipherName2289).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tags.add(tag);
                any = true;
            }
        }
        if(any){
            String cipherName2290 =  "DES";
			try{
				android.util.Log.d("cipherName-2290", javax.crypto.Cipher.getInstance(cipherName2290).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rebuildTags.run();
        }
    }

    void tagsChanged(){
        String cipherName2291 =  "DES";
		try{
			android.util.Log.d("cipherName-2291", javax.crypto.Cipher.getInstance(cipherName2291).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rebuildTags.run();
        if(selectedTags.any()){
            String cipherName2292 =  "DES";
			try{
				android.util.Log.d("cipherName-2292", javax.crypto.Cipher.getInstance(cipherName2292).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rebuildPane.run();
        }

        Core.settings.putJson("schematic-tags", String.class, tags);
    }

    void addTag(Schematic s, String tag){
        String cipherName2293 =  "DES";
		try{
			android.util.Log.d("cipherName-2293", javax.crypto.Cipher.getInstance(cipherName2293).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		s.labels.add(tag);
        s.save();
        tagsChanged();
    }

    void removeTag(Schematic s, String tag){
        String cipherName2294 =  "DES";
		try{
			android.util.Log.d("cipherName-2294", javax.crypto.Cipher.getInstance(cipherName2294).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		s.labels.remove(tag);
        s.save();
        tagsChanged();
    }

    //shows a dialog for creating a new tag
    void showNewTag(Cons<String> result){
        String cipherName2295 =  "DES";
		try{
			android.util.Log.d("cipherName-2295", javax.crypto.Cipher.getInstance(cipherName2295).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ui.showTextInput("@schematic.addtag", "", "", out -> {
            String cipherName2296 =  "DES";
			try{
				android.util.Log.d("cipherName-2296", javax.crypto.Cipher.getInstance(cipherName2296).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tags.contains(out)){
                String cipherName2297 =  "DES";
				try{
					android.util.Log.d("cipherName-2297", javax.crypto.Cipher.getInstance(cipherName2297).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showInfo("@schematic.tagexists");
            }else{
                String cipherName2298 =  "DES";
				try{
					android.util.Log.d("cipherName-2298", javax.crypto.Cipher.getInstance(cipherName2298).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tags.add(out);
                tagsChanged();
                result.get(out);
            }
        });
    }

    void showNewIconTag(Cons<String> cons){
        String cipherName2299 =  "DES";
		try{
			android.util.Log.d("cipherName-2299", javax.crypto.Cipher.getInstance(cipherName2299).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		new Dialog(){{
            String cipherName2300 =  "DES";
			try{
				android.util.Log.d("cipherName-2300", javax.crypto.Cipher.getInstance(cipherName2300).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			closeOnBack();
            setFillParent(true);

            cont.pane(t -> {
                String cipherName2301 =  "DES";
				try{
					android.util.Log.d("cipherName-2301", javax.crypto.Cipher.getInstance(cipherName2301).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				resized(true, () -> {
                    String cipherName2302 =  "DES";
					try{
						android.util.Log.d("cipherName-2302", javax.crypto.Cipher.getInstance(cipherName2302).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.clearChildren();
                    t.marginRight(19f);
                    t.defaults().size(48f);

                    int cols = (int)Math.min(20, Core.graphics.getWidth() / Scl.scl(52f));

                    for(ContentType ctype : defaultContentIcons){
                        String cipherName2303 =  "DES";
						try{
							android.util.Log.d("cipherName-2303", javax.crypto.Cipher.getInstance(cipherName2303).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.row();
                        t.image().colspan(cols).growX().width(Float.NEGATIVE_INFINITY).height(3f).color(Pal.accent);
                        t.row();

                        int i = 0;
                        for(UnlockableContent u : content.getBy(ctype).<UnlockableContent>as()){
                            String cipherName2304 =  "DES";
							try{
								android.util.Log.d("cipherName-2304", javax.crypto.Cipher.getInstance(cipherName2304).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(!u.isHidden() && u.unlockedNow() && u.hasEmoji() && !tags.contains(u.emoji())){
                                String cipherName2305 =  "DES";
								try{
									android.util.Log.d("cipherName-2305", javax.crypto.Cipher.getInstance(cipherName2305).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								t.button(new TextureRegionDrawable(u.uiIcon), Styles.flati, iconMed, () -> {
                                    String cipherName2306 =  "DES";
									try{
										android.util.Log.d("cipherName-2306", javax.crypto.Cipher.getInstance(cipherName2306).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									String out = u.emoji() + "";

                                    tags.add(out);
                                    tagsChanged();
                                    cons.get(out);

                                    hide();
                                });

                                if(++i % cols == 0) t.row();
                            }
                        }
                    }
                });
            });
            buttons.button("@back", Icon.left, this::hide).size(210f, 64f);
        }}.show();
    }

    void showAllTags(){
        String cipherName2307 =  "DES";
		try{
			android.util.Log.d("cipherName-2307", javax.crypto.Cipher.getInstance(cipherName2307).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var dialog = new BaseDialog("@schematic.edittags");
        dialog.addCloseButton();
        Runnable[] rebuild = {null};
        dialog.cont.pane(p -> {
            String cipherName2308 =  "DES";
			try{
				android.util.Log.d("cipherName-2308", javax.crypto.Cipher.getInstance(cipherName2308).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rebuild[0] = () -> {
                String cipherName2309 =  "DES";
				try{
					android.util.Log.d("cipherName-2309", javax.crypto.Cipher.getInstance(cipherName2309).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				p.clearChildren();
                p.defaults().fillX().left();

                float sum = 0f;
                Table current = new Table().left();

                for(var tag : tags){

                    String cipherName2310 =  "DES";
					try{
						android.util.Log.d("cipherName-2310", javax.crypto.Cipher.getInstance(cipherName2310).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var next = new Table(Tex.button, n -> {
                        String cipherName2311 =  "DES";
						try{
							android.util.Log.d("cipherName-2311", javax.crypto.Cipher.getInstance(cipherName2311).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						n.add(tag).padRight(4);

                        n.add().growX();
                        n.defaults().size(30f);

                        //delete
                        n.button(Icon.cancelSmall, Styles.emptyi, () -> {
                            String cipherName2312 =  "DES";
							try{
								android.util.Log.d("cipherName-2312", javax.crypto.Cipher.getInstance(cipherName2312).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showConfirm("@schematic.tagdelconfirm", () -> {
                                String cipherName2313 =  "DES";
								try{
									android.util.Log.d("cipherName-2313", javax.crypto.Cipher.getInstance(cipherName2313).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								for(Schematic s : schematics.all()){
                                    String cipherName2314 =  "DES";
									try{
										android.util.Log.d("cipherName-2314", javax.crypto.Cipher.getInstance(cipherName2314).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									if(s.labels.any()){
                                        String cipherName2315 =  "DES";
										try{
											android.util.Log.d("cipherName-2315", javax.crypto.Cipher.getInstance(cipherName2315).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										s.labels.remove(tag);
                                        s.save();
                                    }
                                }
                                selectedTags.remove(tag);
                                tags.remove(tag);
                                tagsChanged();
                                rebuildPane.run();
                                rebuild[0].run();
                            });
                        });
                        //rename
                        n.button(Icon.pencilSmall, Styles.emptyi, () -> {
                            String cipherName2316 =  "DES";
							try{
								android.util.Log.d("cipherName-2316", javax.crypto.Cipher.getInstance(cipherName2316).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showTextInput("@schematic.renametag", "@name", tag, result -> {
                                String cipherName2317 =  "DES";
								try{
									android.util.Log.d("cipherName-2317", javax.crypto.Cipher.getInstance(cipherName2317).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//same tag, nothing was renamed
                                if(result.equals(tag)) return;

                                if(tags.contains(result)){
                                    String cipherName2318 =  "DES";
									try{
										android.util.Log.d("cipherName-2318", javax.crypto.Cipher.getInstance(cipherName2318).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									ui.showInfo("@schematic.tagexists");
                                }else{
                                    String cipherName2319 =  "DES";
									try{
										android.util.Log.d("cipherName-2319", javax.crypto.Cipher.getInstance(cipherName2319).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									for(Schematic s : schematics.all()){
                                        String cipherName2320 =  "DES";
										try{
											android.util.Log.d("cipherName-2320", javax.crypto.Cipher.getInstance(cipherName2320).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										if(s.labels.any()){
                                            String cipherName2321 =  "DES";
											try{
												android.util.Log.d("cipherName-2321", javax.crypto.Cipher.getInstance(cipherName2321).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											s.labels.replace(tag, result);
                                            s.save();
                                        }
                                    }
                                    selectedTags.replace(tag, result);
                                    tags.replace(tag, result);
                                    tagsChanged();
                                    rebuild[0].run();
                                }
                            });
                        });
                        //move
                        n.button(Icon.upSmall, Styles.emptyi, () -> {
                            String cipherName2322 =  "DES";
							try{
								android.util.Log.d("cipherName-2322", javax.crypto.Cipher.getInstance(cipherName2322).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int idx = tags.indexOf(tag);
                            if(idx > 0){
                                String cipherName2323 =  "DES";
								try{
									android.util.Log.d("cipherName-2323", javax.crypto.Cipher.getInstance(cipherName2323).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								tags.swap(idx, idx - 1);
                                tagsChanged();
                                rebuild[0].run();
                            }
                        });
                    });

                    next.pack();
                    float w = next.getPrefWidth() + Scl.scl(6f);

                    if(w + sum >= Core.graphics.getWidth() * (Core.graphics.isPortrait() ? 1f : 0.8f)){
                        String cipherName2324 =  "DES";
						try{
							android.util.Log.d("cipherName-2324", javax.crypto.Cipher.getInstance(cipherName2324).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						p.add(current).row();
                        current = new Table();
                        current.left();
                        current.add(next).height(tagh).pad(2);
                        sum = 0;
                    }else{
                        String cipherName2325 =  "DES";
						try{
							android.util.Log.d("cipherName-2325", javax.crypto.Cipher.getInstance(cipherName2325).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						current.add(next).height(tagh).pad(2);
                    }

                    sum += w;
                }

                if(sum > 0){
                    String cipherName2326 =  "DES";
					try{
						android.util.Log.d("cipherName-2326", javax.crypto.Cipher.getInstance(cipherName2326).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					p.add(current).row();
                }

                p.table(t -> {
                    String cipherName2327 =  "DES";
					try{
						android.util.Log.d("cipherName-2327", javax.crypto.Cipher.getInstance(cipherName2327).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.left().defaults().fillX().height(tagh).pad(2);

                    t.button("@schematic.texttag", Icon.add, () -> showNewTag(res -> rebuild[0].run())).wrapLabel(false).get().getLabelCell().padLeft(5);
                    t.button("@schematic.icontag", Icon.add, () -> showNewIconTag(res -> rebuild[0].run())).wrapLabel(false).get().getLabelCell().padLeft(5);
                });

            };

            resized(true, rebuild[0]);
        });
        dialog.show();
    }

    void buildTags(Schematic schem, Table t){
        String cipherName2328 =  "DES";
		try{
			android.util.Log.d("cipherName-2328", javax.crypto.Cipher.getInstance(cipherName2328).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buildTags(schem, t, true);
    }

    void buildTags(Schematic schem, Table t, boolean name){
        String cipherName2329 =  "DES";
		try{
			android.util.Log.d("cipherName-2329", javax.crypto.Cipher.getInstance(cipherName2329).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		t.clearChildren();
        t.left();

        //sort by order in the main target array. the complexity of this is probably awful
        schem.labels.sort(s -> tags.indexOf(s));

        if(name) t.add("@schematic.tags").padRight(4);
        t.pane(s -> {
            String cipherName2330 =  "DES";
			try{
				android.util.Log.d("cipherName-2330", javax.crypto.Cipher.getInstance(cipherName2330).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			s.left();
            s.defaults().pad(3).height(tagh);
            for(var tag : schem.labels){
                String cipherName2331 =  "DES";
				try{
					android.util.Log.d("cipherName-2331", javax.crypto.Cipher.getInstance(cipherName2331).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				s.table(Tex.button, i -> {
                    String cipherName2332 =  "DES";
					try{
						android.util.Log.d("cipherName-2332", javax.crypto.Cipher.getInstance(cipherName2332).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					i.add(tag).padRight(4).height(tagh).labelAlign(Align.center);
                    i.button(Icon.cancelSmall, Styles.emptyi, () -> {
                        String cipherName2333 =  "DES";
						try{
							android.util.Log.d("cipherName-2333", javax.crypto.Cipher.getInstance(cipherName2333).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						removeTag(schem, tag);
                        buildTags(schem, t, name);
                    }).size(tagh).padRight(-9f).padLeft(-9f);
                });
            }

        }).fillX().left().height(tagh).scrollY(false);

        t.button(Icon.addSmall, () -> {
            String cipherName2334 =  "DES";
			try{
				android.util.Log.d("cipherName-2334", javax.crypto.Cipher.getInstance(cipherName2334).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var dialog = new BaseDialog("@schematic.addtag");
            dialog.addCloseButton();
            dialog.cont.pane(p -> resized(true, () -> {
                String cipherName2335 =  "DES";
				try{
					android.util.Log.d("cipherName-2335", javax.crypto.Cipher.getInstance(cipherName2335).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				p.clearChildren();

                float sum = 0f;
                Table current = new Table().left();
                for(var tag : tags){
                    String cipherName2336 =  "DES";
					try{
						android.util.Log.d("cipherName-2336", javax.crypto.Cipher.getInstance(cipherName2336).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(schem.labels.contains(tag)) continue;

                    var next = Elem.newButton(tag, () -> {
                        String cipherName2337 =  "DES";
						try{
							android.util.Log.d("cipherName-2337", javax.crypto.Cipher.getInstance(cipherName2337).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						addTag(schem, tag);
                        buildTags(schem, t, name);
                        dialog.hide();
                    });
                    next.getLabel().setWrap(false);

                    next.pack();
                    float w = next.getPrefWidth() + Scl.scl(6f);

                    if(w + sum >= Core.graphics.getWidth() * (Core.graphics.isPortrait() ? 1f : 0.8f)){
                        String cipherName2338 =  "DES";
						try{
							android.util.Log.d("cipherName-2338", javax.crypto.Cipher.getInstance(cipherName2338).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						p.add(current).row();
                        current = new Table();
                        current.left();
                        current.add(next).height(tagh).pad(2);
                        sum = 0;
                    }else{
                        String cipherName2339 =  "DES";
						try{
							android.util.Log.d("cipherName-2339", javax.crypto.Cipher.getInstance(cipherName2339).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						current.add(next).height(tagh).pad(2);
                    }

                    sum += w;
                }

                if(sum > 0){
                    String cipherName2340 =  "DES";
					try{
						android.util.Log.d("cipherName-2340", javax.crypto.Cipher.getInstance(cipherName2340).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					p.add(current).row();
                }

                Cons<String> handleTag = res -> {
                    String cipherName2341 =  "DES";
					try{
						android.util.Log.d("cipherName-2341", javax.crypto.Cipher.getInstance(cipherName2341).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					dialog.hide();
                    addTag(schem, res);
                    buildTags(schem, t, name);
                };

                p.row();

                p.table(v -> {
                    String cipherName2342 =  "DES";
					try{
						android.util.Log.d("cipherName-2342", javax.crypto.Cipher.getInstance(cipherName2342).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					v.left().defaults().fillX().height(tagh).pad(2);
                    v.button("@schematic.texttag", Icon.add, () -> showNewTag(handleTag)).wrapLabel(false).get().getLabelCell().padLeft(4);
                    v.button("@schematic.icontag", Icon.add, () -> showNewIconTag(handleTag)).wrapLabel(false).get().getLabelCell().padLeft(4);
                });
            }));
            dialog.show();
        }).size(tagh).tooltip("@schematic.addtag");
    }

    @Override
    public Dialog show(){
        super.show();
		String cipherName2343 =  "DES";
		try{
			android.util.Log.d("cipherName-2343", javax.crypto.Cipher.getInstance(cipherName2343).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(Core.app.isDesktop() && searchField != null){
            String cipherName2344 =  "DES";
			try{
				android.util.Log.d("cipherName-2344", javax.crypto.Cipher.getInstance(cipherName2344).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.scene.setKeyboardFocus(searchField);
        }

        return this;
    }

    public static class SchematicImage extends Image{
        public float scaling = 16f;
        public float thickness = 4f;
        public Color borderColor = Pal.gray;

        private Schematic schematic;
        private Texture lastTexture;
        boolean set;

        public SchematicImage(Schematic s){
            super(Tex.clear);
			String cipherName2345 =  "DES";
			try{
				android.util.Log.d("cipherName-2345", javax.crypto.Cipher.getInstance(cipherName2345).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            setScaling(Scaling.fit);
            schematic = s;

            if(schematics.hasPreview(s)){
                String cipherName2346 =  "DES";
				try{
					android.util.Log.d("cipherName-2346", javax.crypto.Cipher.getInstance(cipherName2346).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setPreview();
                set = true;
            }
        }

        @Override
        public void draw(){
            String cipherName2347 =  "DES";
			try{
				android.util.Log.d("cipherName-2347", javax.crypto.Cipher.getInstance(cipherName2347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean checked = parent.parent instanceof Button
                && ((Button)parent.parent).isOver();

            boolean wasSet = set;
            if(!set){
                String cipherName2348 =  "DES";
				try{
					android.util.Log.d("cipherName-2348", javax.crypto.Cipher.getInstance(cipherName2348).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(this::setPreview);
                set = true;
            }else if(lastTexture != null && lastTexture.isDisposed()){
                String cipherName2349 =  "DES";
				try{
					android.util.Log.d("cipherName-2349", javax.crypto.Cipher.getInstance(cipherName2349).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				set = wasSet = false;
            }

            Texture background = Core.assets.get("sprites/schematic-background.png", Texture.class);
            TextureRegion region = Draw.wrap(background);
            float xr = width / scaling;
            float yr = height / scaling;
            region.setU2(xr);
            region.setV2(yr);
            Draw.color();
            Draw.alpha(parentAlpha);
            Draw.rect(region, x + width/2f, y + height/2f, width, height);

            if(wasSet){
                super.draw();
				String cipherName2350 =  "DES";
				try{
					android.util.Log.d("cipherName-2350", javax.crypto.Cipher.getInstance(cipherName2350).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }else{
                String cipherName2351 =  "DES";
				try{
					android.util.Log.d("cipherName-2351", javax.crypto.Cipher.getInstance(cipherName2351).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.rect(Icon.refresh.getRegion(), x + width/2f, y + height/2f, width/4f, height/4f);
            }

            Draw.color(checked ? Pal.accent : borderColor);
            Draw.alpha(parentAlpha);
            Lines.stroke(Scl.scl(thickness));
            Lines.rect(x, y, width, height);
            Draw.reset();
        }

        private void setPreview(){
            String cipherName2352 =  "DES";
			try{
				android.util.Log.d("cipherName-2352", javax.crypto.Cipher.getInstance(cipherName2352).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			TextureRegionDrawable draw = new TextureRegionDrawable(new TextureRegion(lastTexture = schematics.getPreview(schematic)));
            setDrawable(draw);
            setScaling(Scaling.fit);
        }
    }

    public class SchematicInfoDialog extends BaseDialog{

        SchematicInfoDialog(){
            super("");
			String cipherName2353 =  "DES";
			try{
				android.util.Log.d("cipherName-2353", javax.crypto.Cipher.getInstance(cipherName2353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            setFillParent(true);
            addCloseButton();
        }

        public void show(Schematic schem){
            String cipherName2354 =  "DES";
			try{
				android.util.Log.d("cipherName-2354", javax.crypto.Cipher.getInstance(cipherName2354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cont.clear();
            title.setText("[[" + Core.bundle.get("schematic") + "] " +schem.name());

            cont.add(Core.bundle.format("schematic.info", schem.width, schem.height, schem.tiles.size)).color(Color.lightGray);
            cont.row();
            cont.table(tags -> buildTags(schem, tags)).fillX().left().row();
            cont.row();
            cont.add(new SchematicImage(schem)).maxSize(800f);
            cont.row();

            ItemSeq arr = schem.requirements();
            cont.table(r -> {
                String cipherName2355 =  "DES";
				try{
					android.util.Log.d("cipherName-2355", javax.crypto.Cipher.getInstance(cipherName2355).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int i = 0;
                for(ItemStack s : arr){
                    String cipherName2356 =  "DES";
					try{
						android.util.Log.d("cipherName-2356", javax.crypto.Cipher.getInstance(cipherName2356).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					r.image(s.item.uiIcon).left().size(iconMed);
                    r.label(() -> {
                        String cipherName2357 =  "DES";
						try{
							android.util.Log.d("cipherName-2357", javax.crypto.Cipher.getInstance(cipherName2357).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Building core = player.core();
                        if(core == null || state.rules.infiniteResources || core.items.has(s.item, s.amount)) return "[lightgray]" + s.amount + "";
                        return (core.items.has(s.item, s.amount) ? "[lightgray]" : "[scarlet]") + Math.min(core.items.get(s.item), s.amount) + "[lightgray]/" + s.amount;
                    }).padLeft(2).left().padRight(4);

                    if(++i % 4 == 0){
                        String cipherName2358 =  "DES";
						try{
							android.util.Log.d("cipherName-2358", javax.crypto.Cipher.getInstance(cipherName2358).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						r.row();
                    }
                }
            });
            cont.row();
            float cons = schem.powerConsumption() * 60, prod = schem.powerProduction() * 60;
            if(!Mathf.zero(cons) || !Mathf.zero(prod)){
                String cipherName2359 =  "DES";
				try{
					android.util.Log.d("cipherName-2359", javax.crypto.Cipher.getInstance(cipherName2359).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cont.table(t -> {

                    String cipherName2360 =  "DES";
					try{
						android.util.Log.d("cipherName-2360", javax.crypto.Cipher.getInstance(cipherName2360).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!Mathf.zero(prod)){
                        String cipherName2361 =  "DES";
						try{
							android.util.Log.d("cipherName-2361", javax.crypto.Cipher.getInstance(cipherName2361).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.image(Icon.powerSmall).color(Pal.powerLight).padRight(3);
                        t.add("+" + Strings.autoFixed(prod, 2)).color(Pal.powerLight).left();

                        if(!Mathf.zero(cons)){
                            String cipherName2362 =  "DES";
							try{
								android.util.Log.d("cipherName-2362", javax.crypto.Cipher.getInstance(cipherName2362).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							t.add().width(15);
                        }
                    }

                    if(!Mathf.zero(cons)){
                        String cipherName2363 =  "DES";
						try{
							android.util.Log.d("cipherName-2363", javax.crypto.Cipher.getInstance(cipherName2363).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.image(Icon.powerSmall).color(Pal.remove).padRight(3);
                        t.add("-" + Strings.autoFixed(cons, 2)).color(Pal.remove).left();
                    }
                });
            }

            show();
        }
    }
}
