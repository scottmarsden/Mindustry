package mindustry.ui.dialogs;

import arc.*;
import arc.util.Http.*;
import arc.files.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.scene.style.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.util.serialization.*;
import arc.util.serialization.Jval.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.mod.*;
import mindustry.mod.Mods.*;
import mindustry.ui.*;

import java.io.*;
import java.text.*;
import java.util.*;

import static mindustry.Vars.*;

public class ModsDialog extends BaseDialog{
    private ObjectMap<String, TextureRegion> textureCache = new ObjectMap<>();

    private float modImportProgress;
    private String searchtxt = "";
    private @Nullable Seq<ModListing> modList;
    private boolean orderDate = true;
    private BaseDialog currentContent;

    private BaseDialog browser;
    private Table browserTable;
    private float scroll = 0f;

    public ModsDialog(){
        super("@mods");
		String cipherName2381 =  "DES";
		try{
			android.util.Log.d("cipherName-2381", javax.crypto.Cipher.getInstance(cipherName2381).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        addCloseButton();

        browser = new BaseDialog("@mods.browser");

        browser.cont.table(table -> {
            String cipherName2382 =  "DES";
			try{
				android.util.Log.d("cipherName-2382", javax.crypto.Cipher.getInstance(cipherName2382).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.left();
            table.image(Icon.zoom);
            table.field(searchtxt, res -> {
                String cipherName2383 =  "DES";
				try{
					android.util.Log.d("cipherName-2383", javax.crypto.Cipher.getInstance(cipherName2383).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				searchtxt = res;
                rebuildBrowser();
            }).growX().get();
            table.button(Icon.list, Styles.emptyi, 32f, () -> {
                String cipherName2384 =  "DES";
				try{
					android.util.Log.d("cipherName-2384", javax.crypto.Cipher.getInstance(cipherName2384).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				orderDate = !orderDate;
                rebuildBrowser();
            }).update(b -> b.getStyle().imageUp = (orderDate ? Icon.list : Icon.star)).size(40f).get()
            .addListener(new Tooltip(tip -> tip.label(() -> orderDate ? "@mods.browser.sortdate" : "@mods.browser.sortstars").left()));
        }).fillX().padBottom(4);

        browser.cont.row();
        browser.cont.pane(tablebrow -> {
            String cipherName2385 =  "DES";
			try{
				android.util.Log.d("cipherName-2385", javax.crypto.Cipher.getInstance(cipherName2385).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tablebrow.margin(10f).top();
            browserTable = tablebrow;
        }).scrollX(false);
        browser.addCloseButton();

        browser.onResize(this::rebuildBrowser);

        buttons.button("@mods.guide", Icon.link, () -> Core.app.openURI(modGuideURL)).size(210, 64f);

        if(!mobile){
            String cipherName2386 =  "DES";
			try{
				android.util.Log.d("cipherName-2386", javax.crypto.Cipher.getInstance(cipherName2386).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buttons.button("@mods.openfolder", Icon.link, () -> Core.app.openFolder(modDirectory.absolutePath()));
        }

        shown(this::setup);
        onResize(this::setup);

        Events.on(ResizeEvent.class, event -> {
            String cipherName2387 =  "DES";
			try{
				android.util.Log.d("cipherName-2387", javax.crypto.Cipher.getInstance(cipherName2387).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(currentContent != null){
                String cipherName2388 =  "DES";
				try{
					android.util.Log.d("cipherName-2388", javax.crypto.Cipher.getInstance(cipherName2388).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				currentContent.hide();
                currentContent = null;
            }
        });

        hidden(() -> {
            String cipherName2389 =  "DES";
			try{
				android.util.Log.d("cipherName-2389", javax.crypto.Cipher.getInstance(cipherName2389).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(mods.requiresReload()){
                String cipherName2390 =  "DES";
				try{
					android.util.Log.d("cipherName-2390", javax.crypto.Cipher.getInstance(cipherName2390).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				reload();
            }
        });

    }

    void modError(Throwable error){
		String cipherName2391 =  "DES";
		try{
			android.util.Log.d("cipherName-2391", javax.crypto.Cipher.getInstance(cipherName2391).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        ui.loadfrag.hide();

        if(error instanceof NoSuchMethodError || Strings.getCauses(error).contains(t -> t.getMessage() != null && (t.getMessage().contains("trust anchor") || t.getMessage().contains("SSL") || t.getMessage().contains("protocol")))){
            ui.showErrorMessage("@feature.unsupported");
        }else if(error instanceof HttpStatusException st){
            ui.showErrorMessage(Core.bundle.format("connectfail", Strings.capitalize(st.status.toString().toLowerCase())));
        }else{
            ui.showException(error);
        }
    }

    void getModList(Cons<Seq<ModListing>> listener){
        String cipherName2392 =  "DES";
		try{
			android.util.Log.d("cipherName-2392", javax.crypto.Cipher.getInstance(cipherName2392).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(modList == null){
            String cipherName2393 =  "DES";
			try{
				android.util.Log.d("cipherName-2393", javax.crypto.Cipher.getInstance(cipherName2393).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Http.get("https://raw.githubusercontent.com/Anuken/MindustryMods/master/mods.json", response -> {
                String cipherName2394 =  "DES";
				try{
					android.util.Log.d("cipherName-2394", javax.crypto.Cipher.getInstance(cipherName2394).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String strResult = response.getResultAsString();

                Core.app.post(() -> {
                    String cipherName2395 =  "DES";
					try{
						android.util.Log.d("cipherName-2395", javax.crypto.Cipher.getInstance(cipherName2395).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName2396 =  "DES";
						try{
							android.util.Log.d("cipherName-2396", javax.crypto.Cipher.getInstance(cipherName2396).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						modList = JsonIO.json.fromJson(Seq.class, ModListing.class, strResult);

                        var d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        Func<String, Date> parser = text -> {
                            String cipherName2397 =  "DES";
							try{
								android.util.Log.d("cipherName-2397", javax.crypto.Cipher.getInstance(cipherName2397).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							try{
                                String cipherName2398 =  "DES";
								try{
									android.util.Log.d("cipherName-2398", javax.crypto.Cipher.getInstance(cipherName2398).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								return d.parse(text);
                            }catch(Exception e){
                                String cipherName2399 =  "DES";
								try{
									android.util.Log.d("cipherName-2399", javax.crypto.Cipher.getInstance(cipherName2399).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								return new Date();
                            }
                        };

                        modList.sortComparing(m -> parser.get(m.lastUpdated)).reverse();
                        listener.get(modList);
                    }catch(Exception e){
                        String cipherName2400 =  "DES";
						try{
							android.util.Log.d("cipherName-2400", javax.crypto.Cipher.getInstance(cipherName2400).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						e.printStackTrace();
                        ui.showException(e);
                    }
                });
            }, error -> Core.app.post(() -> modError(error)));
        }else{
            String cipherName2401 =  "DES";
			try{
				android.util.Log.d("cipherName-2401", javax.crypto.Cipher.getInstance(cipherName2401).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			listener.get(modList);
        }
    }

    void setup(){
        String cipherName2402 =  "DES";
		try{
			android.util.Log.d("cipherName-2402", javax.crypto.Cipher.getInstance(cipherName2402).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float h = 110f;
        float w = Math.min(Core.graphics.getWidth() / Scl.scl(1.05f), 520f);

        cont.clear();
        cont.defaults().width(Math.min(Core.graphics.getWidth() / Scl.scl(1.05f), 556f)).pad(4);
        cont.add("@mod.reloadrequired").visible(mods::requiresReload).center().get().setAlignment(Align.center);
        cont.row();

        cont.table(buttons -> {
            String cipherName2403 =  "DES";
			try{
				android.util.Log.d("cipherName-2403", javax.crypto.Cipher.getInstance(cipherName2403).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buttons.left().defaults().growX().height(60f).uniformX();

            TextButtonStyle style = Styles.flatBordert;
            float margin = 12f;

            buttons.button("@mod.import", Icon.add, style, () -> {
                String cipherName2404 =  "DES";
				try{
					android.util.Log.d("cipherName-2404", javax.crypto.Cipher.getInstance(cipherName2404).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				BaseDialog dialog = new BaseDialog("@mod.import");

                TextButtonStyle bstyle = Styles.flatt;

                dialog.cont.table(Tex.button, t -> {
                    String cipherName2405 =  "DES";
					try{
						android.util.Log.d("cipherName-2405", javax.crypto.Cipher.getInstance(cipherName2405).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.defaults().size(300f, 70f);
                    t.margin(12f);

                    t.button("@mod.import.file", Icon.file, bstyle, () -> {
                        String cipherName2406 =  "DES";
						try{
							android.util.Log.d("cipherName-2406", javax.crypto.Cipher.getInstance(cipherName2406).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						dialog.hide();

                        platform.showMultiFileChooser(file -> {
                            String cipherName2407 =  "DES";
							try{
								android.util.Log.d("cipherName-2407", javax.crypto.Cipher.getInstance(cipherName2407).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							try{
                                String cipherName2408 =  "DES";
								try{
									android.util.Log.d("cipherName-2408", javax.crypto.Cipher.getInstance(cipherName2408).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								mods.importMod(file);
                                setup();
                            }catch(IOException e){
                                String cipherName2409 =  "DES";
								try{
									android.util.Log.d("cipherName-2409", javax.crypto.Cipher.getInstance(cipherName2409).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								ui.showException(e);
                                Log.err(e);
                            }
                        }, "zip", "jar");
                    }).margin(12f);

                    t.row();

                    t.button("@mod.import.github", Icon.github, bstyle, () -> {
                        String cipherName2410 =  "DES";
						try{
							android.util.Log.d("cipherName-2410", javax.crypto.Cipher.getInstance(cipherName2410).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						dialog.hide();

                        ui.showTextInput("@mod.import.github", "", 64, Core.settings.getString("lastmod", ""), text -> {
                            String cipherName2411 =  "DES";
							try{
								android.util.Log.d("cipherName-2411", javax.crypto.Cipher.getInstance(cipherName2411).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//clean up the text in case somebody inputs a URL or adds random spaces
                            text = text.trim().replace(" ", "");
                            if(text.startsWith("https://github.com/")) text = text.substring("https://github.com/".length());

                            Core.settings.put("lastmod", text);
                            //there's no good way to know if it's a java mod here, so assume it's not
                            githubImportMod(text, false, null);
                        });
                    }).margin(12f);
                });
                dialog.addCloseButton();

                dialog.show();

            }).margin(margin);

            buttons.button("@mods.browser", Icon.menu, style, this::showModBrowser).margin(margin);
        }).width(w);

        cont.row();

        if(!mods.list().isEmpty()){
            String cipherName2412 =  "DES";
			try{
				android.util.Log.d("cipherName-2412", javax.crypto.Cipher.getInstance(cipherName2412).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean[] anyDisabled = {false};
            Table[] pane = {null};

            Cons<String> rebuild = query -> {
                String cipherName2413 =  "DES";
				try{
					android.util.Log.d("cipherName-2413", javax.crypto.Cipher.getInstance(cipherName2413).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pane[0].clear();
                boolean any = false;
                for(LoadedMod item : mods.list()){
                    String cipherName2414 =  "DES";
					try{
						android.util.Log.d("cipherName-2414", javax.crypto.Cipher.getInstance(cipherName2414).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(Strings.matches(query, item.meta.displayName())){
                        String cipherName2415 =  "DES";
						try{
							android.util.Log.d("cipherName-2415", javax.crypto.Cipher.getInstance(cipherName2415).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						any = true;
                        if(!item.enabled() && !anyDisabled[0] && mods.list().size > 0){
                            String cipherName2416 =  "DES";
							try{
								android.util.Log.d("cipherName-2416", javax.crypto.Cipher.getInstance(cipherName2416).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							anyDisabled[0] = true;
                            pane[0].row();
                            pane[0].image().growX().height(4f).pad(6f).color(Pal.gray).row();
                        }

                        pane[0].button(t -> {
                            String cipherName2417 =  "DES";
							try{
								android.util.Log.d("cipherName-2417", javax.crypto.Cipher.getInstance(cipherName2417).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							t.top().left();
                            t.margin(12f);

                            String stateDetails = getStateDetails(item);
                            if(stateDetails != null){
                                String cipherName2418 =  "DES";
								try{
									android.util.Log.d("cipherName-2418", javax.crypto.Cipher.getInstance(cipherName2418).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								t.addListener(new Tooltip(f -> f.background(Styles.black8).margin(4f).add(stateDetails).growX().width(400f).wrap()));
                            }

                            t.defaults().left().top();
                            t.table(title1 -> {
                                String cipherName2419 =  "DES";
								try{
									android.util.Log.d("cipherName-2419", javax.crypto.Cipher.getInstance(cipherName2419).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								title1.left();

                                title1.add(new BorderImage(){{
                                    String cipherName2420 =  "DES";
									try{
										android.util.Log.d("cipherName-2420", javax.crypto.Cipher.getInstance(cipherName2420).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									if(item.iconTexture != null){
                                        String cipherName2421 =  "DES";
										try{
											android.util.Log.d("cipherName-2421", javax.crypto.Cipher.getInstance(cipherName2421).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										setDrawable(new TextureRegion(item.iconTexture));
                                    }else{
                                        String cipherName2422 =  "DES";
										try{
											android.util.Log.d("cipherName-2422", javax.crypto.Cipher.getInstance(cipherName2422).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										setDrawable(Tex.nomap);
                                    }
                                    border(Pal.accent);
                                }}).size(h - 8f).padTop(-8f).padLeft(-8f).padRight(8f);

                                title1.table(text -> {
                                    String cipherName2423 =  "DES";
									try{
										android.util.Log.d("cipherName-2423", javax.crypto.Cipher.getInstance(cipherName2423).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									boolean hideDisabled = !item.isSupported() || item.hasUnmetDependencies() || item.hasContentErrors();
                                    String shortDesc = item.meta.shortDescription();

                                    text.add("[accent]" + Strings.stripColors(item.meta.displayName()) + "\n" +
                                        (shortDesc.length() > 0 ? "[lightgray]" + shortDesc + "\n" : "")
                                        //so does anybody care about version?
                                        //+ "[gray]v" + Strings.stripColors(trimText(item.meta.version)) + "\n"
                                        + (item.enabled() || hideDisabled ? "" : Core.bundle.get("mod.disabled") + ""))
                                    .wrap().top().width(300f).growX().left();

                                    text.row();

                                    String state = getStateText(item);
                                    if(state != null){
                                        String cipherName2424 =  "DES";
										try{
											android.util.Log.d("cipherName-2424", javax.crypto.Cipher.getInstance(cipherName2424).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										text.labelWrap(state).growX().row();
                                    }
                                }).top().growX();

                                title1.add().growX();
                            }).growX().growY().left();

                            t.table(right -> {
                                String cipherName2425 =  "DES";
								try{
									android.util.Log.d("cipherName-2425", javax.crypto.Cipher.getInstance(cipherName2425).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								right.right();
                                right.button(item.enabled() ? Icon.downOpen : Icon.upOpen, Styles.clearNonei, () -> {
                                    String cipherName2426 =  "DES";
									try{
										android.util.Log.d("cipherName-2426", javax.crypto.Cipher.getInstance(cipherName2426).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									mods.setEnabled(item, !item.enabled());
                                    setup();
                                }).size(50f).disabled(!item.isSupported());

                                right.button(item.hasSteamID() ? Icon.link : Icon.trash, Styles.clearNonei, () -> {
                                    String cipherName2427 =  "DES";
									try{
										android.util.Log.d("cipherName-2427", javax.crypto.Cipher.getInstance(cipherName2427).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									if(!item.hasSteamID()){
                                        String cipherName2428 =  "DES";
										try{
											android.util.Log.d("cipherName-2428", javax.crypto.Cipher.getInstance(cipherName2428).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										ui.showConfirm("@confirm", "@mod.remove.confirm", () -> {
                                            String cipherName2429 =  "DES";
											try{
												android.util.Log.d("cipherName-2429", javax.crypto.Cipher.getInstance(cipherName2429).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											mods.removeMod(item);
                                            setup();
                                        });
                                    }else{
                                        String cipherName2430 =  "DES";
										try{
											android.util.Log.d("cipherName-2430", javax.crypto.Cipher.getInstance(cipherName2430).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										platform.viewListing(item);
                                    }
                                }).size(50f);

                                if(steam && !item.hasSteamID()){
                                    String cipherName2431 =  "DES";
									try{
										android.util.Log.d("cipherName-2431", javax.crypto.Cipher.getInstance(cipherName2431).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									right.row();
                                    right.button(Icon.export, Styles.clearNonei, () -> {
                                        String cipherName2432 =  "DES";
										try{
											android.util.Log.d("cipherName-2432", javax.crypto.Cipher.getInstance(cipherName2432).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										platform.publish(item);
                                    }).size(50f);
                                }
                            }).growX().right().padRight(-8f).padTop(-8f);
                        }, Styles.flatBordert, () -> showMod(item)).size(w, h).growX().pad(4f);
                        pane[0].row();
                    }
                }

                if(!any){
                    String cipherName2433 =  "DES";
					try{
						android.util.Log.d("cipherName-2433", javax.crypto.Cipher.getInstance(cipherName2433).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					pane[0].add("@none.found").color(Color.lightGray).pad(4);
                }
            };

            if(!mobile || Core.graphics.isPortrait()){
                String cipherName2434 =  "DES";
				try{
					android.util.Log.d("cipherName-2434", javax.crypto.Cipher.getInstance(cipherName2434).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cont.table(search -> {
                    String cipherName2435 =  "DES";
					try{
						android.util.Log.d("cipherName-2435", javax.crypto.Cipher.getInstance(cipherName2435).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					search.image(Icon.zoom).padRight(8f);
                    search.field("", rebuild).growX();
                }).fillX().padBottom(4);
            }

            cont.row();
            cont.pane(table1 -> {
                String cipherName2436 =  "DES";
				try{
					android.util.Log.d("cipherName-2436", javax.crypto.Cipher.getInstance(cipherName2436).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pane[0] = table1.margin(10f).top();
                rebuild.get("");
            }).scrollX(false).update(s -> scroll = s.getScrollY()).get().setScrollYForce(scroll);
        }else{
            String cipherName2437 =  "DES";
			try{
				android.util.Log.d("cipherName-2437", javax.crypto.Cipher.getInstance(cipherName2437).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cont.table(Styles.black6, t -> t.add("@mods.none")).height(80f);
        }

        cont.row();
    }

    private @Nullable String getStateText(LoadedMod item){
        String cipherName2438 =  "DES";
		try{
			android.util.Log.d("cipherName-2438", javax.crypto.Cipher.getInstance(cipherName2438).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(item.isOutdated()){
            String cipherName2439 =  "DES";
			try{
				android.util.Log.d("cipherName-2439", javax.crypto.Cipher.getInstance(cipherName2439).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.incompatiblemod";
        }else if(item.isBlacklisted()){
            String cipherName2440 =  "DES";
			try{
				android.util.Log.d("cipherName-2440", javax.crypto.Cipher.getInstance(cipherName2440).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.blacklisted";
        }else if(!item.isSupported()){
            String cipherName2441 =  "DES";
			try{
				android.util.Log.d("cipherName-2441", javax.crypto.Cipher.getInstance(cipherName2441).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.incompatiblegame";
        }else if(item.state == ModState.circularDependencies){
            String cipherName2442 =  "DES";
			try{
				android.util.Log.d("cipherName-2442", javax.crypto.Cipher.getInstance(cipherName2442).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.circulardependencies";
        }else if(item.state == ModState.incompleteDependencies){
            String cipherName2443 =  "DES";
			try{
				android.util.Log.d("cipherName-2443", javax.crypto.Cipher.getInstance(cipherName2443).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.incompletedependencies";
        }else if(item.hasUnmetDependencies()){
            String cipherName2444 =  "DES";
			try{
				android.util.Log.d("cipherName-2444", javax.crypto.Cipher.getInstance(cipherName2444).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.unmetdependencies";
        }else if(item.hasContentErrors()){
            String cipherName2445 =  "DES";
			try{
				android.util.Log.d("cipherName-2445", javax.crypto.Cipher.getInstance(cipherName2445).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.erroredcontent";
        }else if(item.meta.hidden){
            String cipherName2446 =  "DES";
			try{
				android.util.Log.d("cipherName-2446", javax.crypto.Cipher.getInstance(cipherName2446).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.multiplayer.compatible";
        }
        return null;
    }

    private @Nullable String getStateDetails(LoadedMod item){
        String cipherName2447 =  "DES";
		try{
			android.util.Log.d("cipherName-2447", javax.crypto.Cipher.getInstance(cipherName2447).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(item.isOutdated()){
            String cipherName2448 =  "DES";
			try{
				android.util.Log.d("cipherName-2448", javax.crypto.Cipher.getInstance(cipherName2448).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.outdatedv7.details";
        }else if(item.isBlacklisted()){
            String cipherName2449 =  "DES";
			try{
				android.util.Log.d("cipherName-2449", javax.crypto.Cipher.getInstance(cipherName2449).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.blacklisted.details";
        }else if(!item.isSupported()){
            String cipherName2450 =  "DES";
			try{
				android.util.Log.d("cipherName-2450", javax.crypto.Cipher.getInstance(cipherName2450).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("mod.requiresversion.details", item.meta.minGameVersion);
        }else if(item.state == ModState.circularDependencies){
            String cipherName2451 =  "DES";
			try{
				android.util.Log.d("cipherName-2451", javax.crypto.Cipher.getInstance(cipherName2451).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.circulardependencies.details";
        }else if(item.state == ModState.incompleteDependencies){
            String cipherName2452 =  "DES";
			try{
				android.util.Log.d("cipherName-2452", javax.crypto.Cipher.getInstance(cipherName2452).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("mod.incompletedependencies.details", item.missingDependencies.toString(", "));
        }else if(item.hasUnmetDependencies()){
            String cipherName2453 =  "DES";
			try{
				android.util.Log.d("cipherName-2453", javax.crypto.Cipher.getInstance(cipherName2453).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("mod.missingdependencies.details", item.missingDependencies.toString(", "));
        }else if(item.hasContentErrors()){
            String cipherName2454 =  "DES";
			try{
				android.util.Log.d("cipherName-2454", javax.crypto.Cipher.getInstance(cipherName2454).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "@mod.erroredcontent.details";
        }
        return null;
    }

    private void reload(){
        String cipherName2455 =  "DES";
		try{
			android.util.Log.d("cipherName-2455", javax.crypto.Cipher.getInstance(cipherName2455).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ui.showInfoOnHidden("@mods.reloadexit", () -> {
            String cipherName2456 =  "DES";
			try{
				android.util.Log.d("cipherName-2456", javax.crypto.Cipher.getInstance(cipherName2456).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.info("Exiting to reload mods.");
            Core.app.exit();
        });
    }

    private void showMod(LoadedMod mod){
		String cipherName2457 =  "DES";
		try{
			android.util.Log.d("cipherName-2457", javax.crypto.Cipher.getInstance(cipherName2457).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        BaseDialog dialog = new BaseDialog(mod.meta.displayName());

        dialog.addCloseButton();

        if(!mobile){
            dialog.buttons.button("@mods.openfolder", Icon.link, () -> Core.app.openFolder(mod.file.absolutePath()));
        }

        if(mod.getRepo() != null){
            boolean showImport = !mod.hasSteamID();
            dialog.buttons.button("@mods.github.open", Icon.link, () -> Core.app.openURI("https://github.com/" + mod.getRepo()));
            if(mobile && showImport) dialog.buttons.row();
            if(showImport) dialog.buttons.button("@mods.browser.reinstall", Icon.download, () -> githubImportMod(mod.getRepo(), mod.isJava(), null));
        }

        dialog.cont.pane(desc -> {
            desc.center();
            desc.defaults().padTop(10).left();

            desc.add("@editor.name").padRight(10).color(Color.gray).padTop(0);
            desc.row();
            desc.add(mod.meta.displayName()).growX().wrap().padTop(2);
            desc.row();
            if(mod.meta.author != null){
                desc.add("@editor.author").padRight(10).color(Color.gray);
                desc.row();
                desc.add(mod.meta.author).growX().wrap().padTop(2);
                desc.row();
            }
            if(mod.meta.description != null){
                desc.add("@editor.description").padRight(10).color(Color.gray).top();
                desc.row();
                desc.add(mod.meta.description).growX().wrap().padTop(2);
                desc.row();
            }

            String state = getStateDetails(mod);

            if(state != null){
                desc.add("@mod.disabled").padTop(13f).padBottom(-6f).row();
                desc.add(state).growX().wrap().row();
            }

        }).width(400f);

        Seq<UnlockableContent> all = Seq.with(content.getContentMap()).<Content>flatten().select(c -> c.minfo.mod == mod && c instanceof UnlockableContent u && !u.isHidden()).as();
        if(all.any()){
            dialog.cont.row();
            dialog.cont.button("@mods.viewcontent", Icon.book, () -> {
                BaseDialog d = new BaseDialog(mod.meta.displayName());
                d.cont.pane(cs -> {
                    int i = 0;
                    for(UnlockableContent c : all){
                        cs.button(new TextureRegionDrawable(c.uiIcon), Styles.flati, iconMed, () -> {
                            ui.content.show(c);
                        }).size(50f).with(im -> {
                            var click = im.getClickListener();
                            im.update(() -> im.getImage().color.lerp(!click.isOver() ? Color.lightGray : Color.white, 0.4f * Time.delta));

                        }).tooltip(c.localizedName);

                        if(++i % (int)Math.min(Core.graphics.getWidth() / Scl.scl(110), 14) == 0) cs.row();
                    }
                }).grow();
                d.addCloseButton();
                d.show();
                currentContent = d;
            }).size(300, 50).pad(4);
        }

        dialog.show();
    }

    private void showModBrowser(){
        String cipherName2458 =  "DES";
		try{
			android.util.Log.d("cipherName-2458", javax.crypto.Cipher.getInstance(cipherName2458).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rebuildBrowser();
        browser.show();
    }

    private void rebuildBrowser(){
        String cipherName2459 =  "DES";
		try{
			android.util.Log.d("cipherName-2459", javax.crypto.Cipher.getInstance(cipherName2459).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ObjectSet<String> installed = mods.list().map(m -> m.getRepo()).asSet();

        browserTable.clear();
        browserTable.add("@loading");

        int cols = (int)Math.max(Core.graphics.getWidth() / Scl.scl(480), 1);

        getModList(rlistings -> {
            String cipherName2460 =  "DES";
			try{
				android.util.Log.d("cipherName-2460", javax.crypto.Cipher.getInstance(cipherName2460).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			browserTable.clear();
            int i = 0;

            var listings = rlistings;
            if(!orderDate){
                String cipherName2461 =  "DES";
				try{
					android.util.Log.d("cipherName-2461", javax.crypto.Cipher.getInstance(cipherName2461).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				listings = rlistings.copy();
                listings.sortComparing(m1 -> -m1.stars);
            }

            for(ModListing mod : listings){
                String cipherName2462 =  "DES";
				try{
					android.util.Log.d("cipherName-2462", javax.crypto.Cipher.getInstance(cipherName2462).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(((mod.hasJava || mod.hasScripts) && Vars.ios) ||
                    (!Strings.matches(searchtxt, mod.name) && !Strings.matches(searchtxt, mod.repo))
                    //hack, I'm basically testing if 135.10 >= modVersion, which is equivalent to modVersion >= 136
                    || (Version.isAtLeast(135, 10, mod.minGameVersion))
                ) continue;

                float s = 64f;

                browserTable.button(con -> {
                    String cipherName2463 =  "DES";
					try{
						android.util.Log.d("cipherName-2463", javax.crypto.Cipher.getInstance(cipherName2463).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					con.margin(0f);
                    con.left();

                    String repo = mod.repo;
                    con.add(new BorderImage(){
                        TextureRegion last;

                        {
                            String cipherName2464 =  "DES";
							try{
								android.util.Log.d("cipherName-2464", javax.crypto.Cipher.getInstance(cipherName2464).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							border(installed.contains(repo) ? Pal.accent : Color.lightGray);
                            setDrawable(Tex.nomap);
                            pad = Scl.scl(4f);
                        }

                        @Override
                        public void draw(){
                            super.draw();
							String cipherName2465 =  "DES";
							try{
								android.util.Log.d("cipherName-2465", javax.crypto.Cipher.getInstance(cipherName2465).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}

                            //textures are only requested when the rendering happens; this assists with culling
                            if(!textureCache.containsKey(repo)){
                                String cipherName2466 =  "DES";
								try{
									android.util.Log.d("cipherName-2466", javax.crypto.Cipher.getInstance(cipherName2466).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								textureCache.put(repo, last = Core.atlas.find("nomap"));
                                Http.get("https://raw.githubusercontent.com/Anuken/MindustryMods/master/icons/" + repo.replace("/", "_"), res -> {
                                    String cipherName2467 =  "DES";
									try{
										android.util.Log.d("cipherName-2467", javax.crypto.Cipher.getInstance(cipherName2467).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Pixmap pix = new Pixmap(res.getResult());
                                    Core.app.post(() -> {
                                        String cipherName2468 =  "DES";
										try{
											android.util.Log.d("cipherName-2468", javax.crypto.Cipher.getInstance(cipherName2468).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										try{
                                            String cipherName2469 =  "DES";
											try{
												android.util.Log.d("cipherName-2469", javax.crypto.Cipher.getInstance(cipherName2469).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											var tex = new Texture(pix);
                                            tex.setFilter(TextureFilter.linear);
                                            textureCache.put(repo, new TextureRegion(tex));
                                            pix.dispose();
                                        }catch(Exception e){
                                            String cipherName2470 =  "DES";
											try{
												android.util.Log.d("cipherName-2470", javax.crypto.Cipher.getInstance(cipherName2470).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											Log.err(e);
                                        }
                                    });
                                }, err -> {
									String cipherName2471 =  "DES";
									try{
										android.util.Log.d("cipherName-2471", javax.crypto.Cipher.getInstance(cipherName2471).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}});
                            }

                            var next = textureCache.get(repo);
                            if(last != next){
                                String cipherName2472 =  "DES";
								try{
									android.util.Log.d("cipherName-2472", javax.crypto.Cipher.getInstance(cipherName2472).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								last = next;
                                setDrawable(next);
                            }
                        }
                    }).size(s).pad(4f * 2f);

                    con.add(
                    "[accent]" + mod.name.replace("\n", "") +
                    (installed.contains(mod.repo) ? "\n[lightgray]" + Core.bundle.get("mod.installed") : "") +
                    "\n[lightgray]\uE809 " + mod.stars +
                    (Version.isAtLeast(mod.minGameVersion) ?  "" :
                    "\n" + Core.bundle.format("mod.requiresversion", mod.minGameVersion)))
                    .width(358f).wrap().grow().pad(4f, 2f, 4f, 6f).top().left().labelAlign(Align.topLeft);

                }, Styles.flatBordert, () -> {
                    String cipherName2473 =  "DES";
					try{
						android.util.Log.d("cipherName-2473", javax.crypto.Cipher.getInstance(cipherName2473).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var sel = new BaseDialog(mod.name);
                    sel.cont.pane(p -> p.add(mod.description + "\n\n[accent]" + Core.bundle.get("editor.author") + "[lightgray] " + mod.author)
                        .width(mobile ? 400f : 500f).wrap().pad(4f).labelAlign(Align.center, Align.left)).grow();
                    sel.buttons.defaults().size(150f, 54f).pad(2f);
                    sel.buttons.button("@back", Icon.left, () -> {
                        String cipherName2474 =  "DES";
						try{
							android.util.Log.d("cipherName-2474", javax.crypto.Cipher.getInstance(cipherName2474).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						sel.clear();
                        sel.hide();
                    });

                    var found = mods.list().find(l -> mod.repo != null && mod.repo.equals(l.getRepo()));
                    sel.buttons.button(found == null ? "@mods.browser.add" : "@mods.browser.reinstall", Icon.download, () -> {
                        String cipherName2475 =  "DES";
						try{
							android.util.Log.d("cipherName-2475", javax.crypto.Cipher.getInstance(cipherName2475).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						sel.hide();
                        githubImportMod(mod.repo, mod.hasJava, null);
                    });

                    if(Core.graphics.isPortrait()){
                        String cipherName2476 =  "DES";
						try{
							android.util.Log.d("cipherName-2476", javax.crypto.Cipher.getInstance(cipherName2476).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						sel.buttons.row();
                    }

                    sel.buttons.button("@mods.github.open", Icon.link, () -> {
                        String cipherName2477 =  "DES";
						try{
							android.util.Log.d("cipherName-2477", javax.crypto.Cipher.getInstance(cipherName2477).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Core.app.openURI("https://github.com/" + mod.repo);
                    });
                    sel.buttons.button("@mods.browser.view-releases", Icon.zoom, () -> {
                        String cipherName2478 =  "DES";
						try{
							android.util.Log.d("cipherName-2478", javax.crypto.Cipher.getInstance(cipherName2478).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						BaseDialog load = new BaseDialog("");
                        load.cont.add("[accent]Fetching Releases...");
                        load.show();
                        Http.get(ghApi + "/repos/" + mod.repo + "/releases", res -> {
                            String cipherName2479 =  "DES";
							try{
								android.util.Log.d("cipherName-2479", javax.crypto.Cipher.getInstance(cipherName2479).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							var json = Jval.read(res.getResultAsString());
                            JsonArray releases = json.asArray();

                            Core.app.post(() -> {
                                String cipherName2480 =  "DES";
								try{
									android.util.Log.d("cipherName-2480", javax.crypto.Cipher.getInstance(cipherName2480).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								load.hide();

                                if(releases.size == 0){
                                    String cipherName2481 =  "DES";
									try{
										android.util.Log.d("cipherName-2481", javax.crypto.Cipher.getInstance(cipherName2481).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									ui.showInfo("@mods.browser.noreleases");
                                }else{
                                    String cipherName2482 =  "DES";
									try{
										android.util.Log.d("cipherName-2482", javax.crypto.Cipher.getInstance(cipherName2482).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									sel.hide();
                                    var downloads = new BaseDialog("@mods.browser.releases");
                                    downloads.cont.pane(p -> {
                                        String cipherName2483 =  "DES";
										try{
											android.util.Log.d("cipherName-2483", javax.crypto.Cipher.getInstance(cipherName2483).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										for(int j = 0; j < releases.size; j++){
                                            String cipherName2484 =  "DES";
											try{
												android.util.Log.d("cipherName-2484", javax.crypto.Cipher.getInstance(cipherName2484).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											var release = releases.get(j);

                                            int index = j;
                                            p.table(((TextureRegionDrawable)Tex.whiteui).tint(Pal.darkestGray), t -> {
                                                String cipherName2485 =  "DES";
												try{
													android.util.Log.d("cipherName-2485", javax.crypto.Cipher.getInstance(cipherName2485).getAlgorithm());
												}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
												}
												t.add("[accent]" + release.getString("name") + (index == 0 ? " " + Core.bundle.get("mods.browser.latest") : "")).top().left().growX().wrap().pad(5f);
                                                t.row();
                                                t.add((release.getString("published_at")).substring(0, 10).replaceAll("-", "/")).top().left().growX().wrap().pad(5f).color(Color.gray);
                                                t.row();
                                                t.table(b -> {
                                                    String cipherName2486 =  "DES";
													try{
														android.util.Log.d("cipherName-2486", javax.crypto.Cipher.getInstance(cipherName2486).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
													b.defaults().size(150f, 54f).pad(2f);
                                                    b.button("@mods.github.open-release", Icon.link, () -> Core.app.openURI(release.getString("html_url")));
                                                    b.button("@mods.browser.add", Icon.download, () -> {
                                                        String cipherName2487 =  "DES";
														try{
															android.util.Log.d("cipherName-2487", javax.crypto.Cipher.getInstance(cipherName2487).getAlgorithm());
														}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
														}
														String releaseUrl = release.getString("url");
                                                        githubImportMod(mod.repo, mod.hasJava, releaseUrl.substring(releaseUrl.lastIndexOf("/") + 1));
                                                    });
                                                }).right();
                                            }).margin(5f).growX().pad(5f);

                                            if(j < releases.size - 1) p.row();
                                        }
                                    }).width(500f).scrollX(false).fillY();
                                    downloads.buttons.button("@back", Icon.left, () -> {
                                        String cipherName2488 =  "DES";
										try{
											android.util.Log.d("cipherName-2488", javax.crypto.Cipher.getInstance(cipherName2488).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										downloads.clear();
                                        downloads.hide();
                                        sel.show();
                                    }).size(150f, 54f).pad(2f);
                                    downloads.keyDown(KeyCode.escape, downloads::hide);
                                    downloads.keyDown(KeyCode.back, downloads::hide);
                                    downloads.hidden(sel::show);
                                    downloads.show();
                                }
                            });
                        }, t -> Core.app.post(() -> {
                            String cipherName2489 =  "DES";
							try{
								android.util.Log.d("cipherName-2489", javax.crypto.Cipher.getInstance(cipherName2489).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							modError(t);
                            load.hide();
                        }));
                    });
                    sel.keyDown(KeyCode.escape, sel::hide);
                    sel.keyDown(KeyCode.back, sel::hide);
                    sel.show();
                }).width(438f).pad(4).growX().left().height(s + 8*2f).fillY();

                if(++i % cols == 0) browserTable.row();
            }
        });
    }

    private void handleMod(String repo, HttpResponse result){
        String cipherName2490 =  "DES";
		try{
			android.util.Log.d("cipherName-2490", javax.crypto.Cipher.getInstance(cipherName2490).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName2491 =  "DES";
			try{
				android.util.Log.d("cipherName-2491", javax.crypto.Cipher.getInstance(cipherName2491).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fi file = tmpDirectory.child(repo.replace("/", "") + ".zip");
            long len = result.getContentLength();
            Floatc cons = len <= 0 ? f -> {
				String cipherName2492 =  "DES";
				try{
					android.util.Log.d("cipherName-2492", javax.crypto.Cipher.getInstance(cipherName2492).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}} : p -> modImportProgress = p;

            Streams.copyProgress(result.getResultAsStream(), file.write(false), len, 4096, cons);

            var mod = mods.importMod(file);
            mod.setRepo(repo);
            file.delete();
            Core.app.post(() -> {

                String cipherName2493 =  "DES";
				try{
					android.util.Log.d("cipherName-2493", javax.crypto.Cipher.getInstance(cipherName2493).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName2494 =  "DES";
					try{
						android.util.Log.d("cipherName-2494", javax.crypto.Cipher.getInstance(cipherName2494).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setup();
                    ui.loadfrag.hide();
                }catch(Throwable e){
                    String cipherName2495 =  "DES";
					try{
						android.util.Log.d("cipherName-2495", javax.crypto.Cipher.getInstance(cipherName2495).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.showException(e);
                }
            });
        }catch(Throwable e){
            String cipherName2496 =  "DES";
			try{
				android.util.Log.d("cipherName-2496", javax.crypto.Cipher.getInstance(cipherName2496).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			modError(e);
        }
    }

    private void importFail(Throwable t){
        String cipherName2497 =  "DES";
		try{
			android.util.Log.d("cipherName-2497", javax.crypto.Cipher.getInstance(cipherName2497).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.app.post(() -> modError(t));
    }

    public void githubImportMod(String repo, boolean isJava){
        String cipherName2498 =  "DES";
		try{
			android.util.Log.d("cipherName-2498", javax.crypto.Cipher.getInstance(cipherName2498).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		githubImportMod(repo, isJava, null);
    }

    public void githubImportMod(String repo, boolean isJava, @Nullable String release){
        String cipherName2499 =  "DES";
		try{
			android.util.Log.d("cipherName-2499", javax.crypto.Cipher.getInstance(cipherName2499).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		modImportProgress = 0f;
        ui.loadfrag.show("@downloading");
        ui.loadfrag.setProgress(() -> modImportProgress);

        if(isJava){
            String cipherName2500 =  "DES";
			try{
				android.util.Log.d("cipherName-2500", javax.crypto.Cipher.getInstance(cipherName2500).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			githubImportJavaMod(repo, release);
        }else{
            String cipherName2501 =  "DES";
			try{
				android.util.Log.d("cipherName-2501", javax.crypto.Cipher.getInstance(cipherName2501).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Http.get(ghApi + "/repos/" + repo, res -> {
                String cipherName2502 =  "DES";
				try{
					android.util.Log.d("cipherName-2502", javax.crypto.Cipher.getInstance(cipherName2502).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var json = Jval.read(res.getResultAsString());
                String mainBranch = json.getString("default_branch");
                String language = json.getString("language", "<none>");

                //this is a crude heuristic for class mods; only required for direct github import
                //TODO make a more reliable way to distinguish java mod repos
                if(language.equals("Java") || language.equals("Kotlin")){
                    String cipherName2503 =  "DES";
					try{
						android.util.Log.d("cipherName-2503", javax.crypto.Cipher.getInstance(cipherName2503).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					githubImportJavaMod(repo, release);
                }else{
                    String cipherName2504 =  "DES";
					try{
						android.util.Log.d("cipherName-2504", javax.crypto.Cipher.getInstance(cipherName2504).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					githubImportBranch(mainBranch, repo, release);
                }
            }, this::importFail);
        }
    }

    private void githubImportJavaMod(String repo, @Nullable String release){
        String cipherName2505 =  "DES";
		try{
			android.util.Log.d("cipherName-2505", javax.crypto.Cipher.getInstance(cipherName2505).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//grab latest release
        Http.get(ghApi + "/repos/" + repo + "/releases/" + (release == null ? "latest" : release), res -> {
            String cipherName2506 =  "DES";
			try{
				android.util.Log.d("cipherName-2506", javax.crypto.Cipher.getInstance(cipherName2506).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var json = Jval.read(res.getResultAsString());
            var assets = json.get("assets").asArray();

            //prioritize dexed jar, as that's what Sonnicon's mod template outputs
            var dexedAsset = assets.find(j -> j.getString("name").startsWith("dexed") && j.getString("name").endsWith(".jar"));
            var asset = dexedAsset == null ? assets.find(j -> j.getString("name").endsWith(".jar")) : dexedAsset;

            if(asset != null){
                String cipherName2507 =  "DES";
				try{
					android.util.Log.d("cipherName-2507", javax.crypto.Cipher.getInstance(cipherName2507).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//grab actual file
                var url = asset.getString("browser_download_url");

                Http.get(url, result -> handleMod(repo, result), this::importFail);
            }else{
                String cipherName2508 =  "DES";
				try{
					android.util.Log.d("cipherName-2508", javax.crypto.Cipher.getInstance(cipherName2508).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ArcRuntimeException("No JAR file found in releases. Make sure you have a valid jar file in the mod's latest Github Release.");
            }
        }, this::importFail);
    }

    private void githubImportBranch(String branch, String repo, @Nullable String release){
        String cipherName2509 =  "DES";
		try{
			android.util.Log.d("cipherName-2509", javax.crypto.Cipher.getInstance(cipherName2509).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(release != null) {
            String cipherName2510 =  "DES";
			try{
				android.util.Log.d("cipherName-2510", javax.crypto.Cipher.getInstance(cipherName2510).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Http.get(ghApi + "/repos/" + repo + "/releases/" + release, res -> {
                String cipherName2511 =  "DES";
				try{
					android.util.Log.d("cipherName-2511", javax.crypto.Cipher.getInstance(cipherName2511).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String zipUrl = Jval.read(res.getResultAsString()).getString("zipball_url");
                Http.get(zipUrl, loc -> {
                    String cipherName2512 =  "DES";
					try{
						android.util.Log.d("cipherName-2512", javax.crypto.Cipher.getInstance(cipherName2512).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(loc.getHeader("Location") != null){
                        String cipherName2513 =  "DES";
						try{
							android.util.Log.d("cipherName-2513", javax.crypto.Cipher.getInstance(cipherName2513).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Http.get(loc.getHeader("Location"), result -> {
                            String cipherName2514 =  "DES";
							try{
								android.util.Log.d("cipherName-2514", javax.crypto.Cipher.getInstance(cipherName2514).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							handleMod(repo, result);
                        }, this::importFail);
                    }else{
                        String cipherName2515 =  "DES";
						try{
							android.util.Log.d("cipherName-2515", javax.crypto.Cipher.getInstance(cipherName2515).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						handleMod(repo, loc);
                    }
                }, this::importFail);
            });
        }else{
            String cipherName2516 =  "DES";
			try{
				android.util.Log.d("cipherName-2516", javax.crypto.Cipher.getInstance(cipherName2516).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Http.get(ghApi + "/repos/" + repo + "/zipball/" + branch, loc -> {
                String cipherName2517 =  "DES";
				try{
					android.util.Log.d("cipherName-2517", javax.crypto.Cipher.getInstance(cipherName2517).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(loc.getHeader("Location") != null){
                    String cipherName2518 =  "DES";
					try{
						android.util.Log.d("cipherName-2518", javax.crypto.Cipher.getInstance(cipherName2518).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Http.get(loc.getHeader("Location"), result -> {
                        String cipherName2519 =  "DES";
						try{
							android.util.Log.d("cipherName-2519", javax.crypto.Cipher.getInstance(cipherName2519).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						handleMod(repo, result);
                    }, this::importFail);
                }else{
                    String cipherName2520 =  "DES";
					try{
						android.util.Log.d("cipherName-2520", javax.crypto.Cipher.getInstance(cipherName2520).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					handleMod(repo, loc);
                }
            }, this::importFail);
        }
    }
}
