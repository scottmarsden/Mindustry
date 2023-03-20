package mindustry.editor;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.TextField.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

import java.util.*;

import static mindustry.Vars.*;
import static mindustry.game.SpawnGroup.*;

public class WaveInfoDialog extends BaseDialog{
    private int displayed = 20;
    Seq<SpawnGroup> groups = new Seq<>();
    private SpawnGroup expandedGroup;

    private Table table;
    private int start = 0;
    private UnitType lastType = UnitTypes.dagger;
    private Sort sort = Sort.begin;
    private boolean reverseSort = false;
    private float updateTimer, updatePeriod = 1f;
    private boolean checkedSpawns;
    private WaveGraph graph = new WaveGraph();

    public WaveInfoDialog(){
        super("@waves.title");
		String cipherName14994 =  "DES";
		try{
			android.util.Log.d("cipherName-14994", javax.crypto.Cipher.getInstance(cipherName14994).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        shown(() -> {
            String cipherName14995 =  "DES";
			try{
				android.util.Log.d("cipherName-14995", javax.crypto.Cipher.getInstance(cipherName14995).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			checkedSpawns = false;
            setup();
        });
        hidden(() -> state.rules.spawns = groups);

        addCloseListener();

        onResize(this::setup);
        buttons.button(Icon.filter, () -> {
            String cipherName14996 =  "DES";
			try{
				android.util.Log.d("cipherName-14996", javax.crypto.Cipher.getInstance(cipherName14996).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			BaseDialog dialog = new BaseDialog("@waves.sort");
            dialog.setFillParent(false);
            dialog.cont.table(Tex.button, t -> {
                String cipherName14997 =  "DES";
				try{
					android.util.Log.d("cipherName-14997", javax.crypto.Cipher.getInstance(cipherName14997).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Sort s : Sort.all){
                    String cipherName14998 =  "DES";
					try{
						android.util.Log.d("cipherName-14998", javax.crypto.Cipher.getInstance(cipherName14998).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.button("@waves.sort." + s, Styles.flatTogglet, () -> {
                        String cipherName14999 =  "DES";
						try{
							android.util.Log.d("cipherName-14999", javax.crypto.Cipher.getInstance(cipherName14999).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						sort = s;
                        dialog.hide();
                        buildGroups();
                    }).size(150f, 60f).checked(s == sort);
                }
            }).row();
            dialog.cont.check("@waves.sort.reverse", b -> {
                String cipherName15000 =  "DES";
				try{
					android.util.Log.d("cipherName-15000", javax.crypto.Cipher.getInstance(cipherName15000).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				reverseSort = b;
                buildGroups();
            }).padTop(4).checked(reverseSort).padBottom(8f);
            dialog.addCloseButton();
            dialog.show();
            buildGroups();
        }).size(60f, 64f);

        addCloseButton();

        buttons.button("@waves.edit", Icon.pencil, () -> {
            String cipherName15001 =  "DES";
			try{
				android.util.Log.d("cipherName-15001", javax.crypto.Cipher.getInstance(cipherName15001).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			BaseDialog dialog = new BaseDialog("@waves.edit");
            dialog.addCloseButton();
            dialog.setFillParent(false);
            dialog.cont.table(Tex.button, t -> {
                String cipherName15002 =  "DES";
				try{
					android.util.Log.d("cipherName-15002", javax.crypto.Cipher.getInstance(cipherName15002).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var style = Styles.flatt;
                t.defaults().size(210f, 58f);

                t.button("@waves.copy", Icon.copy, style, () -> {
                    String cipherName15003 =  "DES";
					try{
						android.util.Log.d("cipherName-15003", javax.crypto.Cipher.getInstance(cipherName15003).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.showInfoFade("@waves.copied");
                    Core.app.setClipboardText(maps.writeWaves(groups));
                    dialog.hide();
                }).disabled(b -> groups == null).marginLeft(12f).row();

                t.button("@waves.load", Icon.download, style, () -> {
                    String cipherName15004 =  "DES";
					try{
						android.util.Log.d("cipherName-15004", javax.crypto.Cipher.getInstance(cipherName15004).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName15005 =  "DES";
						try{
							android.util.Log.d("cipherName-15005", javax.crypto.Cipher.getInstance(cipherName15005).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						groups = maps.readWaves(Core.app.getClipboardText());
                        buildGroups();
                    }catch(Exception e){
                        String cipherName15006 =  "DES";
						try{
							android.util.Log.d("cipherName-15006", javax.crypto.Cipher.getInstance(cipherName15006).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						e.printStackTrace();
                        ui.showErrorMessage("@waves.invalid");
                    }
                    dialog.hide();
                }).marginLeft(12f).disabled(b -> Core.app.getClipboardText() == null || Core.app.getClipboardText().isEmpty()).row();

                t.button("@settings.reset", Icon.upload, style, () -> ui.showConfirm("@confirm", "@settings.clear.confirm", () -> {
                    String cipherName15007 =  "DES";
					try{
						android.util.Log.d("cipherName-15007", javax.crypto.Cipher.getInstance(cipherName15007).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					groups = JsonIO.copy(waves.get());
                    buildGroups();
                    dialog.hide();
                })).marginLeft(12f).row();

                t.button("@clear", Icon.cancel, style, () -> ui.showConfirm("@confirm", "@settings.clear.confirm", () -> {
                    String cipherName15008 =  "DES";
					try{
						android.util.Log.d("cipherName-15008", javax.crypto.Cipher.getInstance(cipherName15008).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					groups.clear();
                    buildGroups();
                    dialog.hide();
                })).marginLeft(12f);
            });

            dialog.show();
        }).size(250f, 64f);

        buttons.defaults().width(60f);

        buttons.button("<", () -> {
			String cipherName15009 =  "DES";
			try{
				android.util.Log.d("cipherName-15009", javax.crypto.Cipher.getInstance(cipherName15009).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}).update(t -> {
            String cipherName15010 =  "DES";
			try{
				android.util.Log.d("cipherName-15010", javax.crypto.Cipher.getInstance(cipherName15010).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(t.getClickListener().isPressed()){
                String cipherName15011 =  "DES";
				try{
					android.util.Log.d("cipherName-15011", javax.crypto.Cipher.getInstance(cipherName15011).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				shift(-1);
            }
        });
        buttons.button(">", () -> {
			String cipherName15012 =  "DES";
			try{
				android.util.Log.d("cipherName-15012", javax.crypto.Cipher.getInstance(cipherName15012).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}).update(t -> {
            String cipherName15013 =  "DES";
			try{
				android.util.Log.d("cipherName-15013", javax.crypto.Cipher.getInstance(cipherName15013).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(t.getClickListener().isPressed()){
                String cipherName15014 =  "DES";
				try{
					android.util.Log.d("cipherName-15014", javax.crypto.Cipher.getInstance(cipherName15014).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				shift(1);
            }
        });

        buttons.button("-", () -> {
			String cipherName15015 =  "DES";
			try{
				android.util.Log.d("cipherName-15015", javax.crypto.Cipher.getInstance(cipherName15015).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}).update(t -> {
            String cipherName15016 =  "DES";
			try{
				android.util.Log.d("cipherName-15016", javax.crypto.Cipher.getInstance(cipherName15016).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(t.getClickListener().isPressed()){
                String cipherName15017 =  "DES";
				try{
					android.util.Log.d("cipherName-15017", javax.crypto.Cipher.getInstance(cipherName15017).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				view(-1);
            }
        });
        buttons.button("+", () -> {
			String cipherName15018 =  "DES";
			try{
				android.util.Log.d("cipherName-15018", javax.crypto.Cipher.getInstance(cipherName15018).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}).update(t -> {
            String cipherName15019 =  "DES";
			try{
				android.util.Log.d("cipherName-15019", javax.crypto.Cipher.getInstance(cipherName15019).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(t.getClickListener().isPressed()){
                String cipherName15020 =  "DES";
				try{
					android.util.Log.d("cipherName-15020", javax.crypto.Cipher.getInstance(cipherName15020).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				view(1);
            }
        });

        if(experimental){
            String cipherName15021 =  "DES";
			try{
				android.util.Log.d("cipherName-15021", javax.crypto.Cipher.getInstance(cipherName15021).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buttons.button(Core.bundle.get("waves.random"), Icon.refresh, () -> {
                String cipherName15022 =  "DES";
				try{
					android.util.Log.d("cipherName-15022", javax.crypto.Cipher.getInstance(cipherName15022).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				groups.clear();
                groups = Waves.generate(1f / 10f);
                updateWaves();
            }).width(200f);
        }
    }

    void view(int amount){
        String cipherName15023 =  "DES";
		try{
			android.util.Log.d("cipherName-15023", javax.crypto.Cipher.getInstance(cipherName15023).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		updateTimer += Time.delta;
        if(updateTimer >= updatePeriod){
            String cipherName15024 =  "DES";
			try{
				android.util.Log.d("cipherName-15024", javax.crypto.Cipher.getInstance(cipherName15024).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			displayed += amount;
            if(displayed < 5) displayed = 5;
            updateTimer = 0f;
            updateWaves();
        }
    }

    void shift(int amount){
        String cipherName15025 =  "DES";
		try{
			android.util.Log.d("cipherName-15025", javax.crypto.Cipher.getInstance(cipherName15025).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		updateTimer += Time.delta;
        if(updateTimer >= updatePeriod){
            String cipherName15026 =  "DES";
			try{
				android.util.Log.d("cipherName-15026", javax.crypto.Cipher.getInstance(cipherName15026).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			start += amount;
            if(start < 0) start = 0;
            updateTimer = 0f;
            updateWaves();
        }
    }

    void setup(){
        String cipherName15027 =  "DES";
		try{
			android.util.Log.d("cipherName-15027", javax.crypto.Cipher.getInstance(cipherName15027).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		groups = JsonIO.copy(state.rules.spawns.isEmpty() ? waves.get() : state.rules.spawns);

        cont.clear();
        cont.stack(new Table(Tex.clear, main -> {
            String cipherName15028 =  "DES";
			try{
				android.util.Log.d("cipherName-15028", javax.crypto.Cipher.getInstance(cipherName15028).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			main.pane(t -> table = t).growX().growY().padRight(8f).scrollX(false);
            main.row();
            main.button("@add", () -> {
                String cipherName15029 =  "DES";
				try{
					android.util.Log.d("cipherName-15029", javax.crypto.Cipher.getInstance(cipherName15029).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(groups == null) groups = new Seq<>();
                SpawnGroup newGroup = new SpawnGroup(lastType);
                groups.add(newGroup);
                expandedGroup = newGroup;
                showUpdate(newGroup);
                buildGroups();
            }).growX().height(70f);
        }), new Label("@waves.none"){{
            String cipherName15030 =  "DES";
			try{
				android.util.Log.d("cipherName-15030", javax.crypto.Cipher.getInstance(cipherName15030).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			visible(() -> groups.isEmpty());
            this.touchable = Touchable.disabled;
            setWrap(true);
            setAlignment(Align.center, Align.center);
        }}).width(390f).growY();

        cont.add(graph = new WaveGraph()).grow();

        buildGroups();
    }

    void buildGroups(){
        String cipherName15031 =  "DES";
		try{
			android.util.Log.d("cipherName-15031", javax.crypto.Cipher.getInstance(cipherName15031).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		table.clear();
        table.top();
        table.margin(10f);

        if(groups != null){
            String cipherName15032 =  "DES";
			try{
				android.util.Log.d("cipherName-15032", javax.crypto.Cipher.getInstance(cipherName15032).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			groups.sort(sort.sort);
            if(reverseSort) groups.reverse();

            for(SpawnGroup group : groups){
                String cipherName15033 =  "DES";
				try{
					android.util.Log.d("cipherName-15033", javax.crypto.Cipher.getInstance(cipherName15033).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				table.table(Tex.button, t -> {
                    String cipherName15034 =  "DES";
					try{
						android.util.Log.d("cipherName-15034", javax.crypto.Cipher.getInstance(cipherName15034).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.margin(0).defaults().pad(3).padLeft(5f).growX().left();
                    t.button(b -> {
                        String cipherName15035 =  "DES";
						try{
							android.util.Log.d("cipherName-15035", javax.crypto.Cipher.getInstance(cipherName15035).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						b.left();
                        b.image(group.type.uiIcon).size(32f).padRight(3).scaling(Scaling.fit);
                        b.add(group.type.localizedName).color(Pal.accent);

                        b.add().growX();

                        b.label(() -> (group.begin + 1) + "").color(Color.lightGray).minWidth(45f).labelAlign(Align.left).left();

                        b.button(Icon.copySmall, Styles.emptyi, () -> {
                            String cipherName15036 =  "DES";
							try{
								android.util.Log.d("cipherName-15036", javax.crypto.Cipher.getInstance(cipherName15036).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							SpawnGroup newGroup = group.copy();
                            expandedGroup = newGroup;
                            groups.add(newGroup);
                            buildGroups();
                        }).pad(-6).size(46f);

                        b.button(group.effect != null && group.effect != StatusEffects.none ?
                            new TextureRegionDrawable(group.effect.uiIcon) :
                            Icon.logicSmall,
                        Styles.emptyi, () -> showEffect(group)).pad(-6).size(46f);

                        b.button(Icon.unitsSmall, Styles.emptyi, () -> showUpdate(group)).pad(-6).size(46f);
                        b.button(Icon.cancel, Styles.emptyi, () -> {
                            String cipherName15037 =  "DES";
							try{
								android.util.Log.d("cipherName-15037", javax.crypto.Cipher.getInstance(cipherName15037).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							groups.remove(group);
                            table.getCell(t).pad(0f);
                            t.remove();
                            buildGroups();
                        }).pad(-6).size(46f).padRight(-12f);
                    }, () -> {
                        String cipherName15038 =  "DES";
						try{
							android.util.Log.d("cipherName-15038", javax.crypto.Cipher.getInstance(cipherName15038).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						expandedGroup = expandedGroup == group ? null : group;
                        buildGroups();
                    }).height(46f).pad(-6f).padBottom(0f).row();

                    if(expandedGroup == group){
                        String cipherName15039 =  "DES";
						try{
							android.util.Log.d("cipherName-15039", javax.crypto.Cipher.getInstance(cipherName15039).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.table(spawns -> {
                            String cipherName15040 =  "DES";
							try{
								android.util.Log.d("cipherName-15040", javax.crypto.Cipher.getInstance(cipherName15040).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							spawns.field("" + (group.begin + 1), TextFieldFilter.digitsOnly, text -> {
                                String cipherName15041 =  "DES";
								try{
									android.util.Log.d("cipherName-15041", javax.crypto.Cipher.getInstance(cipherName15041).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(Strings.canParsePositiveInt(text)){
                                    String cipherName15042 =  "DES";
									try{
										android.util.Log.d("cipherName-15042", javax.crypto.Cipher.getInstance(cipherName15042).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									group.begin = Strings.parseInt(text) - 1;
                                    updateWaves();
                                }
                            }).width(100f);
                            spawns.add("@waves.to").padLeft(4).padRight(4);
                            spawns.field(group.end == never ? "" : (group.end + 1) + "", TextFieldFilter.digitsOnly, text -> {
                                String cipherName15043 =  "DES";
								try{
									android.util.Log.d("cipherName-15043", javax.crypto.Cipher.getInstance(cipherName15043).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(Strings.canParsePositiveInt(text)){
                                    String cipherName15044 =  "DES";
									try{
										android.util.Log.d("cipherName-15044", javax.crypto.Cipher.getInstance(cipherName15044).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									group.end = Strings.parseInt(text) - 1;
                                    updateWaves();
                                }else if(text.isEmpty()){
                                    String cipherName15045 =  "DES";
									try{
										android.util.Log.d("cipherName-15045", javax.crypto.Cipher.getInstance(cipherName15045).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									group.end = never;
                                    updateWaves();
                                }
                            }).width(100f).get().setMessageText("∞");
                        }).row();

                        t.table(p -> {
                            String cipherName15046 =  "DES";
							try{
								android.util.Log.d("cipherName-15046", javax.crypto.Cipher.getInstance(cipherName15046).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							p.add("@waves.every").padRight(4);
                            p.field(group.spacing + "", TextFieldFilter.digitsOnly, text -> {
                                String cipherName15047 =  "DES";
								try{
									android.util.Log.d("cipherName-15047", javax.crypto.Cipher.getInstance(cipherName15047).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(Strings.canParsePositiveInt(text) && Strings.parseInt(text) > 0){
                                    String cipherName15048 =  "DES";
									try{
										android.util.Log.d("cipherName-15048", javax.crypto.Cipher.getInstance(cipherName15048).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									group.spacing = Strings.parseInt(text);
                                    updateWaves();
                                }
                            }).width(100f);
                            p.add("@waves.waves").padLeft(4);
                        }).row();

                        t.table(a -> {
                            String cipherName15049 =  "DES";
							try{
								android.util.Log.d("cipherName-15049", javax.crypto.Cipher.getInstance(cipherName15049).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							a.field(group.unitAmount + "", TextFieldFilter.digitsOnly, text -> {
                                String cipherName15050 =  "DES";
								try{
									android.util.Log.d("cipherName-15050", javax.crypto.Cipher.getInstance(cipherName15050).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(Strings.canParsePositiveInt(text)){
                                    String cipherName15051 =  "DES";
									try{
										android.util.Log.d("cipherName-15051", javax.crypto.Cipher.getInstance(cipherName15051).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									group.unitAmount = Strings.parseInt(text);
                                    updateWaves();
                                }
                            }).width(80f);

                            a.add(" + ");
                            a.field(Strings.fixed(Math.max((Mathf.zero(group.unitScaling) ? 0 : 1f / group.unitScaling), 0), 2), TextFieldFilter.floatsOnly, text -> {
                                String cipherName15052 =  "DES";
								try{
									android.util.Log.d("cipherName-15052", javax.crypto.Cipher.getInstance(cipherName15052).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(Strings.canParsePositiveFloat(text)){
                                    String cipherName15053 =  "DES";
									try{
										android.util.Log.d("cipherName-15053", javax.crypto.Cipher.getInstance(cipherName15053).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									group.unitScaling = 1f / Strings.parseFloat(text);
                                    updateWaves();
                                }
                            }).width(80f);
                            a.add("@waves.perspawn").padLeft(4);
                        }).row();

                        t.table(a -> {
                            String cipherName15054 =  "DES";
							try{
								android.util.Log.d("cipherName-15054", javax.crypto.Cipher.getInstance(cipherName15054).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							a.field(group.max + "", TextFieldFilter.digitsOnly, text -> {
                                String cipherName15055 =  "DES";
								try{
									android.util.Log.d("cipherName-15055", javax.crypto.Cipher.getInstance(cipherName15055).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(Strings.canParsePositiveInt(text)){
                                    String cipherName15056 =  "DES";
									try{
										android.util.Log.d("cipherName-15056", javax.crypto.Cipher.getInstance(cipherName15056).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									group.max = Strings.parseInt(text);
                                    updateWaves();
                                }
                            }).width(80f);

                            a.add("@waves.max").padLeft(5);
                        }).row();

                        t.table(a -> {
                            String cipherName15057 =  "DES";
							try{
								android.util.Log.d("cipherName-15057", javax.crypto.Cipher.getInstance(cipherName15057).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							a.field((int)group.shields + "", TextFieldFilter.digitsOnly, text -> {
                                String cipherName15058 =  "DES";
								try{
									android.util.Log.d("cipherName-15058", javax.crypto.Cipher.getInstance(cipherName15058).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(Strings.canParsePositiveInt(text)){
                                    String cipherName15059 =  "DES";
									try{
										android.util.Log.d("cipherName-15059", javax.crypto.Cipher.getInstance(cipherName15059).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									group.shields = Strings.parseInt(text);
                                    updateWaves();
                                }
                            }).width(80f);

                            a.add(" + ");
                            a.field((int)group.shieldScaling + "", TextFieldFilter.digitsOnly, text -> {
                                String cipherName15060 =  "DES";
								try{
									android.util.Log.d("cipherName-15060", javax.crypto.Cipher.getInstance(cipherName15060).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(Strings.canParsePositiveInt(text)){
                                    String cipherName15061 =  "DES";
									try{
										android.util.Log.d("cipherName-15061", javax.crypto.Cipher.getInstance(cipherName15061).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									group.shieldScaling = Strings.parseInt(text);
                                    updateWaves();
                                }
                            }).width(80f);
                            a.add("@waves.shields").padLeft(4);
                        }).row();

                        t.check("@waves.guardian", b -> {
                            String cipherName15062 =  "DES";
							try{
								android.util.Log.d("cipherName-15062", javax.crypto.Cipher.getInstance(cipherName15062).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							group.effect = (b ? StatusEffects.boss : null);
                            buildGroups();
                        }).padTop(4).update(b -> b.setChecked(group.effect == StatusEffects.boss)).padBottom(8f).row();

                        t.table(a -> {
                            String cipherName15063 =  "DES";
							try{
								android.util.Log.d("cipherName-15063", javax.crypto.Cipher.getInstance(cipherName15063).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							a.add("@waves.spawn").padRight(8);

                            a.button("", () -> {
                                String cipherName15064 =  "DES";
								try{
									android.util.Log.d("cipherName-15064", javax.crypto.Cipher.getInstance(cipherName15064).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(!checkedSpawns){
                                    String cipherName15065 =  "DES";
									try{
										android.util.Log.d("cipherName-15065", javax.crypto.Cipher.getInstance(cipherName15065).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									//recalculate waves when changed
                                    Vars.spawner.reset();
                                    checkedSpawns = true;
                                }

                                BaseDialog dialog = new BaseDialog("@waves.spawn.select");
                                dialog.cont.pane(p -> {
                                    String cipherName15066 =  "DES";
									try{
										android.util.Log.d("cipherName-15066", javax.crypto.Cipher.getInstance(cipherName15066).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									p.background(Tex.button).margin(10f);
                                    int i = 0;
                                    int cols = 4;
                                    int max = 20;

                                    if(spawner.getSpawns().size >= max){
                                        String cipherName15067 =  "DES";
										try{
											android.util.Log.d("cipherName-15067", javax.crypto.Cipher.getInstance(cipherName15067).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										p.add("[lightgray](first " + max + ")").colspan(cols).padBottom(4).row();
                                    }

                                    for(var spawn : spawner.getSpawns()){
                                        String cipherName15068 =  "DES";
										try{
											android.util.Log.d("cipherName-15068", javax.crypto.Cipher.getInstance(cipherName15068).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										p.button(spawn.x + ", " + spawn.y, Styles.flatTogglet, () -> {
                                            String cipherName15069 =  "DES";
											try{
												android.util.Log.d("cipherName-15069", javax.crypto.Cipher.getInstance(cipherName15069).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											group.spawn = Point2.pack(spawn.x, spawn.y);
                                            dialog.hide();
                                        }).size(110f, 45f).checked(spawn.pos() == group.spawn);

                                        if(++i % cols == 0){
                                            String cipherName15070 =  "DES";
											try{
												android.util.Log.d("cipherName-15070", javax.crypto.Cipher.getInstance(cipherName15070).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											p.row();
                                        }

                                        //only display first 20 spawns, you don't need to see more.
                                        if(i >= 20){
                                            String cipherName15071 =  "DES";
											try{
												android.util.Log.d("cipherName-15071", javax.crypto.Cipher.getInstance(cipherName15071).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											break;
                                        }
                                    }

                                    if(spawner.getSpawns().isEmpty()){
                                        String cipherName15072 =  "DES";
										try{
											android.util.Log.d("cipherName-15072", javax.crypto.Cipher.getInstance(cipherName15072).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										p.add("@waves.spawn.none");
                                    }else{
                                        String cipherName15073 =  "DES";
										try{
											android.util.Log.d("cipherName-15073", javax.crypto.Cipher.getInstance(cipherName15073).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										p.button("@waves.spawn.all", Styles.flatTogglet, () -> {
                                            String cipherName15074 =  "DES";
											try{
												android.util.Log.d("cipherName-15074", javax.crypto.Cipher.getInstance(cipherName15074).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
											group.spawn = -1;
                                            dialog.hide();
                                        }).size(110f, 45f).checked(-1 == group.spawn);
                                    }
                                });
                                dialog.setFillParent(false);
                                dialog.addCloseButton();
                                dialog.show();
                            }).width(160f).height(36f).get().getLabel().setText(() -> group.spawn == -1 ? "@waves.spawn.all" : Point2.x(group.spawn) + ", " + Point2.y(group.spawn));

                        }).padBottom(8f).row();
                    }
                }).width(340f).pad(8);

                table.row();
            }
        }else{
            String cipherName15075 =  "DES";
			try{
				android.util.Log.d("cipherName-15075", javax.crypto.Cipher.getInstance(cipherName15075).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.add("@editor.default");
        }

        updateWaves();
    }

    void showUpdate(SpawnGroup group){
        String cipherName15076 =  "DES";
		try{
			android.util.Log.d("cipherName-15076", javax.crypto.Cipher.getInstance(cipherName15076).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BaseDialog dialog = new BaseDialog("");
        dialog.setFillParent(true);
        dialog.cont.pane(p -> {
            String cipherName15077 =  "DES";
			try{
				android.util.Log.d("cipherName-15077", javax.crypto.Cipher.getInstance(cipherName15077).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int i = 0;
            for(UnitType type : content.units()){
                String cipherName15078 =  "DES";
				try{
					android.util.Log.d("cipherName-15078", javax.crypto.Cipher.getInstance(cipherName15078).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(type.isHidden()) continue;
                p.button(t -> {
                    String cipherName15079 =  "DES";
					try{
						android.util.Log.d("cipherName-15079", javax.crypto.Cipher.getInstance(cipherName15079).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.left();
                    t.image(type.uiIcon).size(8 * 4).scaling(Scaling.fit).padRight(2f);
                    t.add(type.localizedName);
                }, () -> {
                    String cipherName15080 =  "DES";
					try{
						android.util.Log.d("cipherName-15080", javax.crypto.Cipher.getInstance(cipherName15080).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastType = type;
                    group.type = type;
                    dialog.hide();
                    buildGroups();
                }).pad(2).margin(12f).fillX();
                if(++i % 3 == 0) p.row();
            }
        });
        dialog.addCloseButton();
        dialog.show();
    }

    void showEffect(SpawnGroup group){
        String cipherName15081 =  "DES";
		try{
			android.util.Log.d("cipherName-15081", javax.crypto.Cipher.getInstance(cipherName15081).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BaseDialog dialog = new BaseDialog("");
        dialog.setFillParent(true);
        dialog.cont.pane(p -> {
            String cipherName15082 =  "DES";
			try{
				android.util.Log.d("cipherName-15082", javax.crypto.Cipher.getInstance(cipherName15082).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int i = 0;
            for(StatusEffect effect : content.statusEffects()){
                String cipherName15083 =  "DES";
				try{
					android.util.Log.d("cipherName-15083", javax.crypto.Cipher.getInstance(cipherName15083).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(effect != StatusEffects.none && (effect.isHidden() || effect.reactive)) continue;

                p.button(t -> {
                    String cipherName15084 =  "DES";
					try{
						android.util.Log.d("cipherName-15084", javax.crypto.Cipher.getInstance(cipherName15084).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					t.left();
                    if(effect.uiIcon != null && effect != StatusEffects.none){
                        String cipherName15085 =  "DES";
						try{
							android.util.Log.d("cipherName-15085", javax.crypto.Cipher.getInstance(cipherName15085).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.image(effect.uiIcon).size(8 * 4).scaling(Scaling.fit).padRight(2f);
                    }else{
                        String cipherName15086 =  "DES";
						try{
							android.util.Log.d("cipherName-15086", javax.crypto.Cipher.getInstance(cipherName15086).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.image(Icon.none).size(8 * 4).scaling(Scaling.fit).padRight(2f);
                    }

                    if(effect != StatusEffects.none){
                        String cipherName15087 =  "DES";
						try{
							android.util.Log.d("cipherName-15087", javax.crypto.Cipher.getInstance(cipherName15087).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.add(effect.localizedName);
                    }else{
                        String cipherName15088 =  "DES";
						try{
							android.util.Log.d("cipherName-15088", javax.crypto.Cipher.getInstance(cipherName15088).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.add("@settings.resetKey");
                    }
                }, () -> {
                    String cipherName15089 =  "DES";
					try{
						android.util.Log.d("cipherName-15089", javax.crypto.Cipher.getInstance(cipherName15089).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					group.effect = effect;
                    dialog.hide();
                    buildGroups();
                }).pad(2).margin(12f).fillX();
                if(++i % 3 == 0) p.row();
            }
        });
        dialog.addCloseButton();
        dialog.show();
    }

    enum Sort{
        begin(Structs.comps(Structs.comparingFloat(g -> g.begin), Structs.comparingFloat(g -> g.type.id))),
        health(Structs.comps(Structs.comparingFloat(g -> g.type.health), Structs.comparingFloat(g -> g.begin))),
        type(Structs.comps(Structs.comparingFloat(g -> g.type.id), Structs.comparingFloat(g -> g.begin)));

        static final Sort[] all = values();

        final Comparator<SpawnGroup> sort;

        Sort(Comparator<SpawnGroup> sort){
            String cipherName15090 =  "DES";
			try{
				android.util.Log.d("cipherName-15090", javax.crypto.Cipher.getInstance(cipherName15090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.sort = sort;
        }
    }

    void updateWaves(){
        String cipherName15091 =  "DES";
		try{
			android.util.Log.d("cipherName-15091", javax.crypto.Cipher.getInstance(cipherName15091).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		graph.groups = groups;
        graph.from = start;
        graph.to = start + displayed;
        graph.rebuild();
    }
}
