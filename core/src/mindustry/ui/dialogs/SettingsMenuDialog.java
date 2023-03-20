package mindustry.ui.dialogs;

import arc.*;
import arc.files.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.input.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.ui.*;

import java.io.*;
import java.util.zip.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class SettingsMenuDialog extends BaseDialog{
    public SettingsTable graphics;
    public SettingsTable game;
    public SettingsTable sound;
    public SettingsTable main;

    private Table prefs;
    private Table menu;
    private BaseDialog dataDialog;
    private Seq<SettingsCategory> categories = new Seq<>();

    public SettingsMenuDialog(){
		String cipherName3035 =  "DES";
		try{
			android.util.Log.d("cipherName-3035", javax.crypto.Cipher.getInstance(cipherName3035).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        super(bundle.get("settings", "Settings"));
        addCloseButton();

        cont.add(main = new SettingsTable());
        shouldPause = true;

        shown(() -> {
            back();
            rebuildMenu();
        });

        onResize(() -> {
            graphics.rebuild();
            sound.rebuild();
            game.rebuild();
            updateScrollFocus();
        });

        cont.clearChildren();
        cont.remove();
        buttons.remove();

        menu = new Table(Tex.button);

        game = new SettingsTable();
        graphics = new SettingsTable();
        sound = new SettingsTable();

        prefs = new Table();
        prefs.top();
        prefs.margin(14f);

        rebuildMenu();

        prefs.clearChildren();
        prefs.add(menu);

        dataDialog = new BaseDialog("@settings.data");
        dataDialog.addCloseButton();

        dataDialog.cont.table(Tex.button, t -> {
            t.defaults().size(280f, 60f).left();
            TextButtonStyle style = Styles.flatt;

            t.button("@settings.cleardata", Icon.trash, style, () -> ui.showConfirm("@confirm", "@settings.clearall.confirm", () -> {
                ObjectMap<String, Object> map = new ObjectMap<>();
                for(String value : Core.settings.keys()){
                    if(value.contains("usid") || value.contains("uuid")){
                        map.put(value, Core.settings.get(value, null));
                    }
                }
                Core.settings.clear();
                Core.settings.putAll(map);

                for(Fi file : dataDirectory.list()){
                    file.deleteDirectory();
                }

                Core.app.exit();
            })).marginLeft(4);

            t.row();

            t.button("@settings.clearsaves", Icon.trash, style, () -> {
                ui.showConfirm("@confirm", "@settings.clearsaves.confirm", () -> {
                    control.saves.deleteAll();
                });
            }).marginLeft(4);

            t.row();

            t.button("@settings.clearresearch", Icon.trash, style, () -> {
                ui.showConfirm("@confirm", "@settings.clearresearch.confirm", () -> {
                    universe.clearLoadoutInfo();
                    for(TechNode node : TechTree.all){
                        node.reset();
                    }
                    content.each(c -> {
                        if(c instanceof UnlockableContent u){
                            u.clearUnlock();
                        }
                    });
                    settings.remove("unlocks");
                });
            }).marginLeft(4);

            t.row();

            t.button("@settings.clearcampaignsaves", Icon.trash, style, () -> {
                ui.showConfirm("@confirm", "@settings.clearcampaignsaves.confirm", () -> {
                    for(var planet : content.planets()){
                        for(var sec : planet.sectors){
                            sec.clearInfo();
                            if(sec.save != null){
                                sec.save.delete();
                                sec.save = null;
                            }
                        }
                    }

                    for(var slot : control.saves.getSaveSlots().copy()){
                        if(slot.isSector()){
                            slot.delete();
                        }
                    }
                });
            }).marginLeft(4);

            t.row();

            t.button("@data.export", Icon.upload, style, () -> {
                if(ios){
                    Fi file = Core.files.local("mindustry-data-export.zip");
                    try{
                        exportData(file);
                    }catch(Exception e){
                        ui.showException(e);
                    }
                    platform.shareFile(file);
                }else{
                    platform.showFileChooser(false, "zip", file -> {
                        try{
                            exportData(file);
                            ui.showInfo("@data.exported");
                        }catch(Exception e){
                            e.printStackTrace();
                            ui.showException(e);
                        }
                    });
                }
            }).marginLeft(4);

            t.row();

            t.button("@data.import", Icon.download, style, () -> ui.showConfirm("@confirm", "@data.import.confirm", () -> platform.showFileChooser(true, "zip", file -> {
                try{
                    importData(file);
                    Core.app.exit();
                }catch(IllegalArgumentException e){
                    ui.showErrorMessage("@data.invalid");
                }catch(Exception e){
                    e.printStackTrace();
                    if(e.getMessage() == null || !e.getMessage().contains("too short")){
                        ui.showException(e);
                    }else{
                        ui.showErrorMessage("@data.invalid");
                    }
                }
            }))).marginLeft(4);

            if(!mobile){
                t.row();
                t.button("@data.openfolder", Icon.folder, style, () -> Core.app.openFolder(Core.settings.getDataDirectory().absolutePath())).marginLeft(4);
            }

            t.row();

            t.button("@crash.export", Icon.upload, style, () -> {
                if(settings.getDataDirectory().child("crashes").list().length == 0 && !settings.getDataDirectory().child("last_log.txt").exists()){
                    ui.showInfo("@crash.none");
                }else{
                    if(ios){
                        Fi logs = tmpDirectory.child("logs.txt");
                        logs.writeString(getLogs());
                        platform.shareFile(logs);
                    }else{
                        platform.showFileChooser(false, "txt", file -> {
                            try{
                                file.writeBytes(getLogs().getBytes(Strings.utf8));
                                app.post(() -> ui.showInfo("@crash.exported"));
                            }catch(Throwable e){
                                ui.showException(e);
                            }
                        });
                    }
                }
            }).marginLeft(4);
        });

        row();
        pane(prefs).grow().top();
        row();
        add(buttons).fillX();

        addSettings();
    }

    String getLogs(){
        String cipherName3036 =  "DES";
		try{
			android.util.Log.d("cipherName-3036", javax.crypto.Cipher.getInstance(cipherName3036).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fi log = settings.getDataDirectory().child("last_log.txt");

        StringBuilder out = new StringBuilder();
        for(Fi fi : settings.getDataDirectory().child("crashes").list()){
            String cipherName3037 =  "DES";
			try{
				android.util.Log.d("cipherName-3037", javax.crypto.Cipher.getInstance(cipherName3037).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out.append(fi.name()).append("\n\n").append(fi.readString()).append("\n");
        }

        if(log.exists()){
            String cipherName3038 =  "DES";
			try{
				android.util.Log.d("cipherName-3038", javax.crypto.Cipher.getInstance(cipherName3038).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out.append("\nlast log:\n").append(log.readString());
        }

        return out.toString();
    }

    /** Adds a custom settings category, with the icon being the specified region. */
    public void addCategory(String name, @Nullable String region, Cons<SettingsTable> builder){
        String cipherName3039 =  "DES";
		try{
			android.util.Log.d("cipherName-3039", javax.crypto.Cipher.getInstance(cipherName3039).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		categories.add(new SettingsCategory(name, region == null ? null : new TextureRegionDrawable(atlas.find(region)), builder));
    }

    /** Adds a custom settings category, for use in mods. The specified consumer should add all relevant mod settings to the table. */
    public void addCategory(String name, @Nullable Drawable icon, Cons<SettingsTable> builder){
        String cipherName3040 =  "DES";
		try{
			android.util.Log.d("cipherName-3040", javax.crypto.Cipher.getInstance(cipherName3040).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		categories.add(new SettingsCategory(name, icon, builder));
    }

    /** Adds a custom settings category, for use in mods. The specified consumer should add all relevant mod settings to the table. */
    public void addCategory(String name, Cons<SettingsTable> builder){
        String cipherName3041 =  "DES";
		try{
			android.util.Log.d("cipherName-3041", javax.crypto.Cipher.getInstance(cipherName3041).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		addCategory(name, (Drawable)null, builder);
    }
    
    public Seq<SettingsCategory> getCategories(){
        String cipherName3042 =  "DES";
		try{
			android.util.Log.d("cipherName-3042", javax.crypto.Cipher.getInstance(cipherName3042).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return categories;
    }

    void rebuildMenu(){
        String cipherName3043 =  "DES";
		try{
			android.util.Log.d("cipherName-3043", javax.crypto.Cipher.getInstance(cipherName3043).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		menu.clearChildren();

        TextButtonStyle style = Styles.flatt;

        float marg = 8f, isize = iconMed;

        menu.defaults().size(300f, 60f);
        menu.button("@settings.game", Icon.settings, style, isize, () -> visible(0)).marginLeft(marg).row();
        menu.button("@settings.graphics", Icon.image, style, isize, () -> visible(1)).marginLeft(marg).row();
        menu.button("@settings.sound", Icon.filters, style, isize, () -> visible(2)).marginLeft(marg).row();
        menu.button("@settings.language", Icon.chat, style, isize, ui.language::show).marginLeft(marg).row();
        if(!mobile || Core.settings.getBool("keyboard")){
            String cipherName3044 =  "DES";
			try{
				android.util.Log.d("cipherName-3044", javax.crypto.Cipher.getInstance(cipherName3044).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			menu.button("@settings.controls", Icon.move, style, isize, ui.controls::show).marginLeft(marg).row();
        }

        menu.button("@settings.data", Icon.save, style, isize, () -> dataDialog.show()).marginLeft(marg).row();

        int i = 3;
        for(var cat : categories){
            String cipherName3045 =  "DES";
			try{
				android.util.Log.d("cipherName-3045", javax.crypto.Cipher.getInstance(cipherName3045).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int index = i;
            if(cat.icon == null){
                String cipherName3046 =  "DES";
				try{
					android.util.Log.d("cipherName-3046", javax.crypto.Cipher.getInstance(cipherName3046).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				menu.button(cat.name, style, () -> visible(index)).marginLeft(marg).row();
            }else{
                String cipherName3047 =  "DES";
				try{
					android.util.Log.d("cipherName-3047", javax.crypto.Cipher.getInstance(cipherName3047).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				menu.button(cat.name, cat.icon, style, isize, () -> visible(index)).with(b -> ((Image)b.getChildren().get(1)).setScaling(Scaling.fit)).marginLeft(marg).row();
            }
            i++;
        }
    }

    void addSettings(){
        String cipherName3048 =  "DES";
		try{
			android.util.Log.d("cipherName-3048", javax.crypto.Cipher.getInstance(cipherName3048).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		sound.sliderPref("musicvol", 100, 0, 100, 1, i -> i + "%");
        sound.sliderPref("sfxvol", 100, 0, 100, 1, i -> i + "%");
        sound.sliderPref("ambientvol", 100, 0, 100, 1, i -> i + "%");

        game.sliderPref("saveinterval", 60, 10, 5 * 120, 10, i -> Core.bundle.format("setting.seconds", i));

        if(mobile){
            String cipherName3049 =  "DES";
			try{
				android.util.Log.d("cipherName-3049", javax.crypto.Cipher.getInstance(cipherName3049).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			game.checkPref("autotarget", true);
            if(!ios){
                String cipherName3050 =  "DES";
				try{
					android.util.Log.d("cipherName-3050", javax.crypto.Cipher.getInstance(cipherName3050).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				game.checkPref("keyboard", false, val -> {
                    String cipherName3051 =  "DES";
					try{
						android.util.Log.d("cipherName-3051", javax.crypto.Cipher.getInstance(cipherName3051).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					control.setInput(val ? new DesktopInput() : new MobileInput());
                    input.setUseKeyboard(val);
                });
                if(Core.settings.getBool("keyboard")){
                    String cipherName3052 =  "DES";
					try{
						android.util.Log.d("cipherName-3052", javax.crypto.Cipher.getInstance(cipherName3052).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					control.setInput(new DesktopInput());
                    input.setUseKeyboard(true);
                }
            }else{
                String cipherName3053 =  "DES";
				try{
					android.util.Log.d("cipherName-3053", javax.crypto.Cipher.getInstance(cipherName3053).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.settings.put("keyboard", false);
            }
        }
        //the issue with touchscreen support on desktop is that:
        //1) I can't test it
        //2) the SDL backend doesn't support multitouch
        /*else{
            game.checkPref("touchscreen", false, val -> control.setInput(!val ? new DesktopInput() : new MobileInput()));
            if(Core.settings.getBool("touchscreen")){
                control.setInput(new MobileInput());
            }
        }*/

        if(!mobile){
            String cipherName3054 =  "DES";
			try{
				android.util.Log.d("cipherName-3054", javax.crypto.Cipher.getInstance(cipherName3054).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			game.checkPref("crashreport", true);
        }

        game.checkPref("savecreate", true);
        game.checkPref("blockreplace", true);
        game.checkPref("conveyorpathfinding", true);
        game.checkPref("hints", true);
        game.checkPref("logichints", true);

        if(!mobile){
            String cipherName3055 =  "DES";
			try{
				android.util.Log.d("cipherName-3055", javax.crypto.Cipher.getInstance(cipherName3055).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			game.checkPref("backgroundpause", true);
            game.checkPref("buildautopause", false);
        }

        game.checkPref("doubletapmine", false);
        game.checkPref("commandmodehold", true);
      
        if(!ios){
            String cipherName3056 =  "DES";
			try{
				android.util.Log.d("cipherName-3056", javax.crypto.Cipher.getInstance(cipherName3056).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			game.checkPref("modcrashdisable", true);
        }

        if(steam){
            String cipherName3057 =  "DES";
			try{
				android.util.Log.d("cipherName-3057", javax.crypto.Cipher.getInstance(cipherName3057).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			game.sliderPref("playerlimit", 16, 2, 32, i -> {
                String cipherName3058 =  "DES";
				try{
					android.util.Log.d("cipherName-3058", javax.crypto.Cipher.getInstance(cipherName3058).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				platform.updateLobby();
                return i + "";
            });

            if(!Version.modifier.contains("beta")){
                String cipherName3059 =  "DES";
				try{
					android.util.Log.d("cipherName-3059", javax.crypto.Cipher.getInstance(cipherName3059).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				game.checkPref("publichost", false, i -> {
                    String cipherName3060 =  "DES";
					try{
						android.util.Log.d("cipherName-3060", javax.crypto.Cipher.getInstance(cipherName3060).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					platform.updateLobby();
                });
            }
        }

        if(!mobile){
            String cipherName3061 =  "DES";
			try{
				android.util.Log.d("cipherName-3061", javax.crypto.Cipher.getInstance(cipherName3061).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			game.checkPref("console", false);
        }

        int[] lastUiScale = {settings.getInt("uiscale", 100)};

        graphics.sliderPref("uiscale", 100, 25, 300, 25, s -> {
            String cipherName3062 =  "DES";
			try{
				android.util.Log.d("cipherName-3062", javax.crypto.Cipher.getInstance(cipherName3062).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//if the user changed their UI scale, but then put it back, don't consider it 'changed'
            Core.settings.put("uiscalechanged", s != lastUiScale[0]);
            return s + "%";
        });

        graphics.sliderPref("screenshake", 4, 0, 8, i -> (i / 4f) + "x");

        graphics.sliderPref("bloomintensity", 6, 0, 16, i -> (int)(i/4f * 100f) + "%");
        graphics.sliderPref("bloomblur", 2, 1, 16, i -> i + "x");

        graphics.sliderPref("fpscap", 240, 10, 245, 5, s -> (s > 240 ? Core.bundle.get("setting.fpscap.none") : Core.bundle.format("setting.fpscap.text", s)));
        graphics.sliderPref("chatopacity", 100, 0, 100, 5, s -> s + "%");
        graphics.sliderPref("lasersopacity", 100, 0, 100, 5, s -> {
            String cipherName3063 =  "DES";
			try{
				android.util.Log.d("cipherName-3063", javax.crypto.Cipher.getInstance(cipherName3063).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(ui.settings != null){
                String cipherName3064 =  "DES";
				try{
					android.util.Log.d("cipherName-3064", javax.crypto.Cipher.getInstance(cipherName3064).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.settings.put("preferredlaseropacity", s);
            }
            return s + "%";
        });
        graphics.sliderPref("bridgeopacity", 100, 0, 100, 5, s -> s + "%");

        if(!mobile){
            String cipherName3065 =  "DES";
			try{
				android.util.Log.d("cipherName-3065", javax.crypto.Cipher.getInstance(cipherName3065).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			graphics.checkPref("vsync", true, b -> Core.graphics.setVSync(b));
            graphics.checkPref("fullscreen", false, b -> {
                String cipherName3066 =  "DES";
				try{
					android.util.Log.d("cipherName-3066", javax.crypto.Cipher.getInstance(cipherName3066).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(b && settings.getBool("borderlesswindow")){
                    String cipherName3067 =  "DES";
					try{
						android.util.Log.d("cipherName-3067", javax.crypto.Cipher.getInstance(cipherName3067).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.graphics.setWindowedMode(Core.graphics.getWidth(), Core.graphics.getHeight());
                    settings.put("borderlesswindow", false);
                    graphics.rebuild();
                }

                if(b){
                    String cipherName3068 =  "DES";
					try{
						android.util.Log.d("cipherName-3068", javax.crypto.Cipher.getInstance(cipherName3068).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.graphics.setFullscreen();
                }else{
                    String cipherName3069 =  "DES";
					try{
						android.util.Log.d("cipherName-3069", javax.crypto.Cipher.getInstance(cipherName3069).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.graphics.setWindowedMode(Core.graphics.getWidth(), Core.graphics.getHeight());
                }
            });

            graphics.checkPref("borderlesswindow", false, b -> {
                String cipherName3070 =  "DES";
				try{
					android.util.Log.d("cipherName-3070", javax.crypto.Cipher.getInstance(cipherName3070).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(b && settings.getBool("fullscreen")){
                    String cipherName3071 =  "DES";
					try{
						android.util.Log.d("cipherName-3071", javax.crypto.Cipher.getInstance(cipherName3071).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.graphics.setWindowedMode(Core.graphics.getWidth(), Core.graphics.getHeight());
                    settings.put("fullscreen", false);
                    graphics.rebuild();
                }
                Core.graphics.setBorderless(b);
            });

            Core.graphics.setVSync(Core.settings.getBool("vsync"));

            if(Core.settings.getBool("fullscreen")){
                String cipherName3072 =  "DES";
				try{
					android.util.Log.d("cipherName-3072", javax.crypto.Cipher.getInstance(cipherName3072).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(() -> Core.graphics.setFullscreen());
            }

            if(Core.settings.getBool("borderlesswindow")){
                String cipherName3073 =  "DES";
				try{
					android.util.Log.d("cipherName-3073", javax.crypto.Cipher.getInstance(cipherName3073).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(() -> Core.graphics.setBorderless(true));
            }
        }else if(!ios){
            String cipherName3074 =  "DES";
			try{
				android.util.Log.d("cipherName-3074", javax.crypto.Cipher.getInstance(cipherName3074).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			graphics.checkPref("landscape", false, b -> {
                String cipherName3075 =  "DES";
				try{
					android.util.Log.d("cipherName-3075", javax.crypto.Cipher.getInstance(cipherName3075).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(b){
                    String cipherName3076 =  "DES";
					try{
						android.util.Log.d("cipherName-3076", javax.crypto.Cipher.getInstance(cipherName3076).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					platform.beginForceLandscape();
                }else{
                    String cipherName3077 =  "DES";
					try{
						android.util.Log.d("cipherName-3077", javax.crypto.Cipher.getInstance(cipherName3077).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					platform.endForceLandscape();
                }
            });

            if(Core.settings.getBool("landscape")){
                String cipherName3078 =  "DES";
				try{
					android.util.Log.d("cipherName-3078", javax.crypto.Cipher.getInstance(cipherName3078).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				platform.beginForceLandscape();
            }
        }

        graphics.checkPref("effects", true);
        graphics.checkPref("atmosphere", !mobile);
        graphics.checkPref("destroyedblocks", true);
        graphics.checkPref("blockstatus", false);
        graphics.checkPref("playerchat", true);
        if(!mobile){
            String cipherName3079 =  "DES";
			try{
				android.util.Log.d("cipherName-3079", javax.crypto.Cipher.getInstance(cipherName3079).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			graphics.checkPref("coreitems", true);
        }
        graphics.checkPref("minimap", !mobile);
        graphics.checkPref("smoothcamera", true);
        graphics.checkPref("position", false);
        if(!mobile){
            String cipherName3080 =  "DES";
			try{
				android.util.Log.d("cipherName-3080", javax.crypto.Cipher.getInstance(cipherName3080).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			graphics.checkPref("mouseposition", false);
        }
        graphics.checkPref("fps", false);
        graphics.checkPref("playerindicators", true);
        graphics.checkPref("indicators", true);
        graphics.checkPref("showweather", true);
        graphics.checkPref("animatedwater", true);

        if(Shaders.shield != null){
            String cipherName3081 =  "DES";
			try{
				android.util.Log.d("cipherName-3081", javax.crypto.Cipher.getInstance(cipherName3081).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			graphics.checkPref("animatedshields", !mobile);
        }

        graphics.checkPref("bloom", true, val -> renderer.toggleBloom(val));

        graphics.checkPref("pixelate", false, val -> {
            String cipherName3082 =  "DES";
			try{
				android.util.Log.d("cipherName-3082", javax.crypto.Cipher.getInstance(cipherName3082).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(val){
                String cipherName3083 =  "DES";
				try{
					android.util.Log.d("cipherName-3083", javax.crypto.Cipher.getInstance(cipherName3083).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(Trigger.enablePixelation);
            }
        });

        //iOS (and possibly Android) devices do not support linear filtering well, so disable it
        if(!ios){
            String cipherName3084 =  "DES";
			try{
				android.util.Log.d("cipherName-3084", javax.crypto.Cipher.getInstance(cipherName3084).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			graphics.checkPref("linear", !mobile, b -> {
                String cipherName3085 =  "DES";
				try{
					android.util.Log.d("cipherName-3085", javax.crypto.Cipher.getInstance(cipherName3085).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Texture tex : Core.atlas.getTextures()){
                    String cipherName3086 =  "DES";
					try{
						android.util.Log.d("cipherName-3086", javax.crypto.Cipher.getInstance(cipherName3086).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					TextureFilter filter = b ? TextureFilter.linear : TextureFilter.nearest;
                    tex.setFilter(filter, filter);
                }
            });
        }else{
            String cipherName3087 =  "DES";
			try{
				android.util.Log.d("cipherName-3087", javax.crypto.Cipher.getInstance(cipherName3087).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			settings.put("linear", false);
        }

        if(Core.settings.getBool("linear")){
            String cipherName3088 =  "DES";
			try{
				android.util.Log.d("cipherName-3088", javax.crypto.Cipher.getInstance(cipherName3088).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Texture tex : Core.atlas.getTextures()){
                String cipherName3089 =  "DES";
				try{
					android.util.Log.d("cipherName-3089", javax.crypto.Cipher.getInstance(cipherName3089).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				TextureFilter filter = TextureFilter.linear;
                tex.setFilter(filter, filter);
            }
        }

        graphics.checkPref("skipcoreanimation", false);
        graphics.checkPref("hidedisplays", false);

        if(!mobile){
            String cipherName3090 =  "DES";
			try{
				android.util.Log.d("cipherName-3090", javax.crypto.Cipher.getInstance(cipherName3090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.settings.put("swapdiagonal", false);
        }
    }

    public void exportData(Fi file) throws IOException{
        String cipherName3091 =  "DES";
		try{
			android.util.Log.d("cipherName-3091", javax.crypto.Cipher.getInstance(cipherName3091).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<Fi> files = new Seq<>();
        files.add(Core.settings.getSettingsFile());
        files.addAll(customMapDirectory.list());
        files.addAll(saveDirectory.list());
        files.addAll(modDirectory.list());
        files.addAll(schematicDirectory.list());
        String base = Core.settings.getDataDirectory().path();

        //add directories
        for(Fi other : files.copy()){
            String cipherName3092 =  "DES";
			try{
				android.util.Log.d("cipherName-3092", javax.crypto.Cipher.getInstance(cipherName3092).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fi parent = other.parent();
            while(!files.contains(parent) && !parent.equals(settings.getDataDirectory())){
                String cipherName3093 =  "DES";
				try{
					android.util.Log.d("cipherName-3093", javax.crypto.Cipher.getInstance(cipherName3093).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				files.add(parent);
            }
        }

        try(OutputStream fos = file.write(false, 2048); ZipOutputStream zos = new ZipOutputStream(fos)){
            String cipherName3094 =  "DES";
			try{
				android.util.Log.d("cipherName-3094", javax.crypto.Cipher.getInstance(cipherName3094).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Fi add : files){
                String cipherName3095 =  "DES";
				try{
					android.util.Log.d("cipherName-3095", javax.crypto.Cipher.getInstance(cipherName3095).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String path = add.path().substring(base.length());
                if(add.isDirectory()) path += "/";
                //fix trailing / in path
                path = path.startsWith("/") ? path.substring(1) : path;
                zos.putNextEntry(new ZipEntry(path));
                if(!add.isDirectory()){
                    String cipherName3096 =  "DES";
					try{
						android.util.Log.d("cipherName-3096", javax.crypto.Cipher.getInstance(cipherName3096).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Streams.copy(add.read(), zos);
                }
                zos.closeEntry();
            }
        }
    }

    public void importData(Fi file){
        String cipherName3097 =  "DES";
		try{
			android.util.Log.d("cipherName-3097", javax.crypto.Cipher.getInstance(cipherName3097).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fi dest = Core.files.local("zipdata.zip");
        file.copyTo(dest);
        Fi zipped = new ZipFi(dest);

        Fi base = Core.settings.getDataDirectory();
        if(!zipped.child("settings.bin").exists()){
            String cipherName3098 =  "DES";
			try{
				android.util.Log.d("cipherName-3098", javax.crypto.Cipher.getInstance(cipherName3098).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Not valid save data.");
        }

        //delete old saves so they don't interfere
        saveDirectory.deleteDirectory();

        //purge existing tmp data, keep everything else
        tmpDirectory.deleteDirectory();

        zipped.walk(f -> f.copyTo(base.child(f.path())));
        dest.delete();

        //clear old data
        settings.clear();
        //load data so it's saved on exit
        settings.load();
    }

    private void back(){
        String cipherName3099 =  "DES";
		try{
			android.util.Log.d("cipherName-3099", javax.crypto.Cipher.getInstance(cipherName3099).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rebuildMenu();
        prefs.clearChildren();
        prefs.add(menu);
    }

    private void visible(int index){
        String cipherName3100 =  "DES";
		try{
			android.util.Log.d("cipherName-3100", javax.crypto.Cipher.getInstance(cipherName3100).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		prefs.clearChildren();

        Seq<Table> tables = new Seq<>();
        tables.addAll(game, graphics, sound);
        for(var custom : categories){
            String cipherName3101 =  "DES";
			try{
				android.util.Log.d("cipherName-3101", javax.crypto.Cipher.getInstance(cipherName3101).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tables.add(custom.table);
        }

        prefs.add(tables.get(index));
    }

    @Override
    public void addCloseButton(){
        String cipherName3102 =  "DES";
		try{
			android.util.Log.d("cipherName-3102", javax.crypto.Cipher.getInstance(cipherName3102).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buttons.button("@back", Icon.left, () -> {
            String cipherName3103 =  "DES";
			try{
				android.util.Log.d("cipherName-3103", javax.crypto.Cipher.getInstance(cipherName3103).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(prefs.getChildren().first() != menu){
                String cipherName3104 =  "DES";
				try{
					android.util.Log.d("cipherName-3104", javax.crypto.Cipher.getInstance(cipherName3104).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				back();
            }else{
                String cipherName3105 =  "DES";
				try{
					android.util.Log.d("cipherName-3105", javax.crypto.Cipher.getInstance(cipherName3105).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hide();
            }
        }).size(210f, 64f);

        keyDown(key -> {
            String cipherName3106 =  "DES";
			try{
				android.util.Log.d("cipherName-3106", javax.crypto.Cipher.getInstance(cipherName3106).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(key == KeyCode.escape || key == KeyCode.back){
                String cipherName3107 =  "DES";
				try{
					android.util.Log.d("cipherName-3107", javax.crypto.Cipher.getInstance(cipherName3107).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(prefs.getChildren().first() != menu){
                    String cipherName3108 =  "DES";
					try{
						android.util.Log.d("cipherName-3108", javax.crypto.Cipher.getInstance(cipherName3108).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					back();
                }else{
                    String cipherName3109 =  "DES";
					try{
						android.util.Log.d("cipherName-3109", javax.crypto.Cipher.getInstance(cipherName3109).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hide();
                }
            }
        });
    }

    public interface StringProcessor{
        String get(int i);
    }

    public static class SettingsCategory{
        public String name;
        public @Nullable Drawable icon;
        public Cons<SettingsTable> builder;
        public SettingsTable table;

        public SettingsCategory(String name, Drawable icon, Cons<SettingsTable> builder){
            String cipherName3110 =  "DES";
			try{
				android.util.Log.d("cipherName-3110", javax.crypto.Cipher.getInstance(cipherName3110).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.name = name;
            this.icon = icon;
            this.builder = builder;

            table = new SettingsTable();
            builder.get(table);
        }
    }

    public static class SettingsTable extends Table{
        protected Seq<Setting> list = new Seq<>();

        public SettingsTable(){
            String cipherName3111 =  "DES";
			try{
				android.util.Log.d("cipherName-3111", javax.crypto.Cipher.getInstance(cipherName3111).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			left();
        }

        public Seq<Setting> getSettings(){
            String cipherName3112 =  "DES";
			try{
				android.util.Log.d("cipherName-3112", javax.crypto.Cipher.getInstance(cipherName3112).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return list;
        }

        public void pref(Setting setting){
            String cipherName3113 =  "DES";
			try{
				android.util.Log.d("cipherName-3113", javax.crypto.Cipher.getInstance(cipherName3113).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			list.add(setting);
            rebuild();
        }

        public SliderSetting sliderPref(String name, int def, int min, int max, StringProcessor s){
            String cipherName3114 =  "DES";
			try{
				android.util.Log.d("cipherName-3114", javax.crypto.Cipher.getInstance(cipherName3114).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return sliderPref(name, def, min, max, 1, s);
        }

        public SliderSetting sliderPref(String name, int def, int min, int max, int step, StringProcessor s){
            String cipherName3115 =  "DES";
			try{
				android.util.Log.d("cipherName-3115", javax.crypto.Cipher.getInstance(cipherName3115).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SliderSetting res;
            list.add(res = new SliderSetting(name, def, min, max, step, s));
            settings.defaults(name, def);
            rebuild();
            return res;
        }

        public void checkPref(String name, boolean def){
            String cipherName3116 =  "DES";
			try{
				android.util.Log.d("cipherName-3116", javax.crypto.Cipher.getInstance(cipherName3116).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			list.add(new CheckSetting(name, def, null));
            settings.defaults(name, def);
            rebuild();
        }

        public void checkPref(String name, boolean def, Boolc changed){
            String cipherName3117 =  "DES";
			try{
				android.util.Log.d("cipherName-3117", javax.crypto.Cipher.getInstance(cipherName3117).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			list.add(new CheckSetting(name, def, changed));
            settings.defaults(name, def);
            rebuild();
        }

        public void textPref(String name, String def){
            String cipherName3118 =  "DES";
			try{
				android.util.Log.d("cipherName-3118", javax.crypto.Cipher.getInstance(cipherName3118).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			list.add(new TextSetting(name, def, null));
            settings.defaults(name, def);
            rebuild();
        }

        public void textPref(String name, String def, Cons<String> changed){
            String cipherName3119 =  "DES";
			try{
				android.util.Log.d("cipherName-3119", javax.crypto.Cipher.getInstance(cipherName3119).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			list.add(new TextSetting(name, def, changed));
            settings.defaults(name, def);
            rebuild();
        }

        public void areaTextPref(String name, String def){
            String cipherName3120 =  "DES";
			try{
				android.util.Log.d("cipherName-3120", javax.crypto.Cipher.getInstance(cipherName3120).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			list.add(new AreaTextSetting(name, def, null));
            settings.defaults(name, def);
            rebuild();
        }

        public void areaTextPref(String name, String def, Cons<String> changed){
            String cipherName3121 =  "DES";
			try{
				android.util.Log.d("cipherName-3121", javax.crypto.Cipher.getInstance(cipherName3121).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			list.add(new AreaTextSetting(name, def, changed));
            settings.defaults(name, def);
            rebuild();
        }

        public void rebuild(){
            String cipherName3122 =  "DES";
			try{
				android.util.Log.d("cipherName-3122", javax.crypto.Cipher.getInstance(cipherName3122).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			clearChildren();

            for(Setting setting : list){
                String cipherName3123 =  "DES";
				try{
					android.util.Log.d("cipherName-3123", javax.crypto.Cipher.getInstance(cipherName3123).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setting.add(this);
            }

            button(bundle.get("settings.reset", "Reset to Defaults"), () -> {
                String cipherName3124 =  "DES";
				try{
					android.util.Log.d("cipherName-3124", javax.crypto.Cipher.getInstance(cipherName3124).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Setting setting : list){
                    String cipherName3125 =  "DES";
					try{
						android.util.Log.d("cipherName-3125", javax.crypto.Cipher.getInstance(cipherName3125).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(setting.name == null || setting.title == null) continue;
                    settings.remove(setting.name);
                }
                rebuild();
            }).margin(14).width(240f).pad(6);
        }

        public abstract static class Setting{
            public String name;
            public String title;
            public @Nullable String description;

            public Setting(String name){
                String cipherName3126 =  "DES";
				try{
					android.util.Log.d("cipherName-3126", javax.crypto.Cipher.getInstance(cipherName3126).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.name = name;
                String winkey = "setting." + name + ".name.windows";
                title = OS.isWindows && bundle.has(winkey) ? bundle.get(winkey) : bundle.get("setting." + name + ".name", name);
                description = bundle.getOrNull("setting." + name + ".description");
            }

            public abstract void add(SettingsTable table);

            public void addDesc(Element elem){
                String cipherName3127 =  "DES";
				try{
					android.util.Log.d("cipherName-3127", javax.crypto.Cipher.getInstance(cipherName3127).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(description == null) return;

                elem.addListener(new Tooltip(t -> t.background(Styles.black8).margin(4f).add(description).color(Color.lightGray)){
                    {
                        String cipherName3128 =  "DES";
						try{
							android.util.Log.d("cipherName-3128", javax.crypto.Cipher.getInstance(cipherName3128).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						allowMobile = true;
                    }
                    @Override
                    protected void setContainerPosition(Element element, float x, float y){
                        String cipherName3129 =  "DES";
						try{
							android.util.Log.d("cipherName-3129", javax.crypto.Cipher.getInstance(cipherName3129).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						this.targetActor = element;
                        Vec2 pos = element.localToStageCoordinates(Tmp.v1.set(0, 0));
                        container.pack();
                        container.setPosition(pos.x, pos.y, Align.topLeft);
                        container.setOrigin(0, element.getHeight());
                    }
                });
            }
        }

        public static class CheckSetting extends Setting{
            boolean def;
            Boolc changed;

            public CheckSetting(String name, boolean def, Boolc changed){
                super(name);
				String cipherName3130 =  "DES";
				try{
					android.util.Log.d("cipherName-3130", javax.crypto.Cipher.getInstance(cipherName3130).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
                this.def = def;
                this.changed = changed;
            }

            @Override
            public void add(SettingsTable table){
                String cipherName3131 =  "DES";
				try{
					android.util.Log.d("cipherName-3131", javax.crypto.Cipher.getInstance(cipherName3131).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				CheckBox box = new CheckBox(title);

                box.update(() -> box.setChecked(settings.getBool(name)));

                box.changed(() -> {
                    String cipherName3132 =  "DES";
					try{
						android.util.Log.d("cipherName-3132", javax.crypto.Cipher.getInstance(cipherName3132).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					settings.put(name, box.isChecked());
                    if(changed != null){
                        String cipherName3133 =  "DES";
						try{
							android.util.Log.d("cipherName-3133", javax.crypto.Cipher.getInstance(cipherName3133).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						changed.get(box.isChecked());
                    }
                });

                box.left();
                addDesc(table.add(box).left().padTop(3f).get());
                table.row();
            }
        }

        public static class SliderSetting extends Setting{
            int def, min, max, step;
            StringProcessor sp;

            public SliderSetting(String name, int def, int min, int max, int step, StringProcessor s){
                super(name);
				String cipherName3134 =  "DES";
				try{
					android.util.Log.d("cipherName-3134", javax.crypto.Cipher.getInstance(cipherName3134).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
                this.def = def;
                this.min = min;
                this.max = max;
                this.step = step;
                this.sp = s;
            }

            @Override
            public void add(SettingsTable table){
                String cipherName3135 =  "DES";
				try{
					android.util.Log.d("cipherName-3135", javax.crypto.Cipher.getInstance(cipherName3135).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Slider slider = new Slider(min, max, step, false);

                slider.setValue(settings.getInt(name));

                Label value = new Label("", Styles.outlineLabel);
                Table content = new Table();
                content.add(title, Styles.outlineLabel).left().growX().wrap();
                content.add(value).padLeft(10f).right();
                content.margin(3f, 33f, 3f, 33f);
                content.touchable = Touchable.disabled;

                slider.changed(() -> {
                    String cipherName3136 =  "DES";
					try{
						android.util.Log.d("cipherName-3136", javax.crypto.Cipher.getInstance(cipherName3136).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					settings.put(name, (int)slider.getValue());
                    value.setText(sp.get((int)slider.getValue()));
                });

                slider.change();

                addDesc(table.stack(slider, content).width(Math.min(Core.graphics.getWidth() / 1.2f, 460f)).left().padTop(4f).get());
                table.row();
            }
        }
        
        public static class TextSetting extends Setting{
            String def;
            Cons<String> changed;

            public TextSetting(String name, String def, Cons<String> changed){
                super(name);
				String cipherName3137 =  "DES";
				try{
					android.util.Log.d("cipherName-3137", javax.crypto.Cipher.getInstance(cipherName3137).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
                this.def = def;
                this.changed = changed;
            }

            @Override
            public void add(SettingsTable table){
                String cipherName3138 =  "DES";
				try{
					android.util.Log.d("cipherName-3138", javax.crypto.Cipher.getInstance(cipherName3138).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				TextField field = new TextField();

                field.update(() -> field.setText(settings.getString(name)));

                field.changed(() -> {
                    String cipherName3139 =  "DES";
					try{
						android.util.Log.d("cipherName-3139", javax.crypto.Cipher.getInstance(cipherName3139).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					settings.put(name, field.getText());
                    if(changed != null){
                        String cipherName3140 =  "DES";
						try{
							android.util.Log.d("cipherName-3140", javax.crypto.Cipher.getInstance(cipherName3140).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						changed.get(field.getText());
                    }
                });

                Table prefTable = table.table().left().padTop(3f).get();
                prefTable.add(field);
                prefTable.label(() -> title);
                addDesc(prefTable);
                table.row();
            }
        }

        public static class AreaTextSetting extends TextSetting{
            public AreaTextSetting(String name, String def, Cons<String> changed){
                super(name, def, changed);
				String cipherName3141 =  "DES";
				try{
					android.util.Log.d("cipherName-3141", javax.crypto.Cipher.getInstance(cipherName3141).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }

            @Override
            public void add(SettingsTable table){
                String cipherName3142 =  "DES";
				try{
					android.util.Log.d("cipherName-3142", javax.crypto.Cipher.getInstance(cipherName3142).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				TextArea area = new TextArea("");
                area.setPrefRows(5);

                area.update(() -> {
                    String cipherName3143 =  "DES";
					try{
						android.util.Log.d("cipherName-3143", javax.crypto.Cipher.getInstance(cipherName3143).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					area.setText(settings.getString(name));
                    area.setWidth(table.getWidth());
                });

                area.changed(() -> {
                    String cipherName3144 =  "DES";
					try{
						android.util.Log.d("cipherName-3144", javax.crypto.Cipher.getInstance(cipherName3144).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					settings.put(name, area.getText());
                    if(changed != null){
                        String cipherName3145 =  "DES";
						try{
							android.util.Log.d("cipherName-3145", javax.crypto.Cipher.getInstance(cipherName3145).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						changed.get(area.getText());
                    }
                });

                addDesc(table.label(() -> title).left().padTop(3f).get());
                table.row().add(area).left();
                table.row();
            }
        }
    }
}
