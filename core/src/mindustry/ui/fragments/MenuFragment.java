package mindustry.ui.fragments;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;

import static mindustry.Vars.*;
import static mindustry.gen.Tex.*;

public class MenuFragment{
    private Table container, submenu;
    private Button currentMenu;
    private MenuRenderer renderer;
    private Seq<MenuButton> customButtons = new Seq<>();

    public void build(Group parent){
        String cipherName1168 =  "DES";
		try{
			android.util.Log.d("cipherName-1168", javax.crypto.Cipher.getInstance(cipherName1168).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		renderer = new MenuRenderer();

        Group group = new WidgetGroup();
        group.setFillParent(true);
        group.visible(() -> !ui.editor.isShown());
        parent.addChild(group);

        parent = group;

        parent.fill((x, y, w, h) -> renderer.render());

        parent.fill(c -> {
            String cipherName1169 =  "DES";
			try{
				android.util.Log.d("cipherName-1169", javax.crypto.Cipher.getInstance(cipherName1169).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			c.pane(Styles.noBarPane, cont -> {
                String cipherName1170 =  "DES";
				try{
					android.util.Log.d("cipherName-1170", javax.crypto.Cipher.getInstance(cipherName1170).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				container = cont;
                cont.name = "menu container";

                if(!mobile){
                    String cipherName1171 =  "DES";
					try{
						android.util.Log.d("cipherName-1171", javax.crypto.Cipher.getInstance(cipherName1171).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					c.left();
                    buildDesktop();
                    Events.on(ResizeEvent.class, event -> buildDesktop());
                }else{
                    String cipherName1172 =  "DES";
					try{
						android.util.Log.d("cipherName-1172", javax.crypto.Cipher.getInstance(cipherName1172).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					buildMobile();
                    Events.on(ResizeEvent.class, event -> buildMobile());
                }
            }).with(pane -> {
                String cipherName1173 =  "DES";
				try{
					android.util.Log.d("cipherName-1173", javax.crypto.Cipher.getInstance(cipherName1173).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pane.setOverscroll(false, false);
            }).grow();
        });

        parent.fill(c -> c.bottom().right().button(Icon.discord, new ImageButtonStyle(){{
            String cipherName1174 =  "DES";
			try{
				android.util.Log.d("cipherName-1174", javax.crypto.Cipher.getInstance(cipherName1174).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			up = discordBanner;
        }}, ui.discord::show).marginTop(9f).marginLeft(10f).tooltip("@discord").size(84, 45).name("discord"));

        //info icon
        if(mobile){
            String cipherName1175 =  "DES";
			try{
				android.util.Log.d("cipherName-1175", javax.crypto.Cipher.getInstance(cipherName1175).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parent.fill(c -> c.bottom().left().button("", new TextButtonStyle(){{
                String cipherName1176 =  "DES";
				try{
					android.util.Log.d("cipherName-1176", javax.crypto.Cipher.getInstance(cipherName1176).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				font = Fonts.def;
                fontColor = Color.white;
                up = infoBanner;
            }}, ui.about::show).size(84, 45).name("info"));

            parent.fill((x, y, w, h) -> {
                String cipherName1177 =  "DES";
				try{
					android.util.Log.d("cipherName-1177", javax.crypto.Cipher.getInstance(cipherName1177).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Core.scene.marginBottom > 0){
                    String cipherName1178 =  "DES";
					try{
						android.util.Log.d("cipherName-1178", javax.crypto.Cipher.getInstance(cipherName1178).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tex.paneTop.draw(0, 0, Core.graphics.getWidth(), Core.scene.marginBottom);
                }
            });
        }else if(becontrol.active()){
            String cipherName1179 =  "DES";
			try{
				android.util.Log.d("cipherName-1179", javax.crypto.Cipher.getInstance(cipherName1179).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parent.fill(c -> c.bottom().right().button("@be.check", Icon.refresh, () -> {
                String cipherName1180 =  "DES";
				try{
					android.util.Log.d("cipherName-1180", javax.crypto.Cipher.getInstance(cipherName1180).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.loadfrag.show();
                becontrol.checkUpdate(result -> {
                    String cipherName1181 =  "DES";
					try{
						android.util.Log.d("cipherName-1181", javax.crypto.Cipher.getInstance(cipherName1181).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.loadfrag.hide();
                    if(!result){
                        String cipherName1182 =  "DES";
						try{
							android.util.Log.d("cipherName-1182", javax.crypto.Cipher.getInstance(cipherName1182).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.showInfo("@be.noupdates");
                    }
                });
            }).size(200, 60).name("becheck").update(t -> {
                String cipherName1183 =  "DES";
				try{
					android.util.Log.d("cipherName-1183", javax.crypto.Cipher.getInstance(cipherName1183).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.getLabel().setColor(becontrol.isUpdateAvailable() ? Tmp.c1.set(Color.white).lerp(Pal.accent, Mathf.absin(5f, 1f)) : Color.white);
            }));
        }

        String versionText = ((Version.build == -1) ? "[#fc8140aa]" : "[#ffffffba]") + Version.combined();
        parent.fill((x, y, w, h) -> {
            String cipherName1184 =  "DES";
			try{
				android.util.Log.d("cipherName-1184", javax.crypto.Cipher.getInstance(cipherName1184).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			TextureRegion logo = Core.atlas.find("logo");
            float width = Core.graphics.getWidth(), height = Core.graphics.getHeight() - Core.scene.marginTop;
            float logoscl = Scl.scl(1) * logo.scale;
            float logow = Math.min(logo.width * logoscl, Core.graphics.getWidth() - Scl.scl(20));
            float logoh = logow * (float)logo.height / logo.width;

            float fx = (int)(width / 2f);
            float fy = (int)(height - 6 - logoh) + logoh / 2 - (Core.graphics.isPortrait() ? Scl.scl(30f) : 0f);

            Draw.color();
            Draw.rect(logo, fx, fy, logow, logoh);

            Fonts.outline.setColor(Color.white);
            Fonts.outline.draw(versionText, fx, fy - logoh/2f - Scl.scl(2f), Align.center);
        }).touchable = Touchable.disabled;
    }

    private void buildMobile(){
        String cipherName1185 =  "DES";
		try{
			android.util.Log.d("cipherName-1185", javax.crypto.Cipher.getInstance(cipherName1185).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		container.clear();
        container.name = "buttons";
        container.setSize(Core.graphics.getWidth(), Core.graphics.getHeight());

        float size = 120f;
        container.defaults().size(size).pad(5).padTop(4f);

        MobileButton
            play = new MobileButton(Icon.play, "@campaign", () -> checkPlay(ui.planet::show)),
            custom = new MobileButton(Icon.rightOpenOut, "@customgame", () -> checkPlay(ui.custom::show)),
            maps = new MobileButton(Icon.download, "@loadgame", () -> checkPlay(ui.load::show)),
            join = new MobileButton(Icon.add, "@joingame", () -> checkPlay(ui.join::show)),
            editor = new MobileButton(Icon.terrain, "@editor", () -> checkPlay(ui.maps::show)),
            tools = new MobileButton(Icon.settings, "@settings", ui.settings::show),
            mods = new MobileButton(Icon.book, "@mods", ui.mods::show),
            exit = new MobileButton(Icon.exit, "@quit", () -> Core.app.exit()),
            about = new MobileButton(Icon.info, "@about.button", ui.about::show);

        Seq<MobileButton> customs = customButtons.map(b -> new MobileButton(b.icon, b.text, b.runnable == null ? () -> {
			String cipherName1186 =  "DES";
			try{
				android.util.Log.d("cipherName-1186", javax.crypto.Cipher.getInstance(cipherName1186).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}} : b.runnable));

        if(!Core.graphics.isPortrait()){
            String cipherName1187 =  "DES";
			try{
				android.util.Log.d("cipherName-1187", javax.crypto.Cipher.getInstance(cipherName1187).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			container.marginTop(60f);
            container.add(play);
            container.add(join);
            container.add(custom);
            container.add(maps);
            // add odd custom buttons
            for(int i = 1; i < customs.size; i += 2){
                String cipherName1188 =  "DES";
				try{
					android.util.Log.d("cipherName-1188", javax.crypto.Cipher.getInstance(cipherName1188).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				container.add(customs.get(i));
            }
            container.row();

            container.add(editor);
            container.add(tools);
            container.add(mods);
            // add even custom buttons (before the exit button)
            for(int i = 0; i < customs.size; i += 2){
                String cipherName1189 =  "DES";
				try{
					android.util.Log.d("cipherName-1189", javax.crypto.Cipher.getInstance(cipherName1189).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				container.add(customs.get(i));
            }
            container.add(ios ? about : exit);
        }else{
            String cipherName1190 =  "DES";
			try{
				android.util.Log.d("cipherName-1190", javax.crypto.Cipher.getInstance(cipherName1190).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			container.marginTop(0f);
            container.add(play);
            container.add(maps);
            container.row();
            container.add(custom);
            container.add(join);
            container.row();
            container.add(editor);
            container.add(tools);
            container.row();
            container.add(mods);
            // add custom buttons
            for(int i = 0; i < customs.size; i++){
                String cipherName1191 =  "DES";
				try{
					android.util.Log.d("cipherName-1191", javax.crypto.Cipher.getInstance(cipherName1191).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				container.add(customs.get(i));
                if(i % 2 == 0) container.row();
            }
            container.add(ios ? about : exit);
        }
    }

    private void buildDesktop(){
        String cipherName1192 =  "DES";
		try{
			android.util.Log.d("cipherName-1192", javax.crypto.Cipher.getInstance(cipherName1192).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		container.clear();
        container.setSize(Core.graphics.getWidth(), Core.graphics.getHeight());

        float width = 230f;
        Drawable background = Styles.black6;

        container.left();
        container.add().width(Core.graphics.getWidth()/10f);
        container.table(background, t -> {
            String cipherName1193 =  "DES";
			try{
				android.util.Log.d("cipherName-1193", javax.crypto.Cipher.getInstance(cipherName1193).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.defaults().width(width).height(70f);
            t.name = "buttons";

            buttons(t,
                new MenuButton("@play", Icon.play,
                    new MenuButton("@campaign", Icon.play, () -> checkPlay(ui.planet::show)),
                    new MenuButton("@joingame", Icon.add, () -> checkPlay(ui.join::show)),
                    new MenuButton("@customgame", Icon.terrain, () -> checkPlay(ui.custom::show)),
                    new MenuButton("@loadgame", Icon.download, () -> checkPlay(ui.load::show))
                ),
                new MenuButton("@database.button", Icon.menu,
                    new MenuButton("@schematics", Icon.paste, ui.schematics::show),
                    new MenuButton("@database", Icon.book, ui.database::show),
                    new MenuButton("@about.button", Icon.info, ui.about::show)
                ),
                new MenuButton("@editor", Icon.terrain, () -> checkPlay(ui.maps::show)), steam ? new MenuButton("@workshop", Icon.steam, platform::openWorkshop) : null,
                new MenuButton("@mods", Icon.book, ui.mods::show),
                new MenuButton("@settings", Icon.settings, ui.settings::show)
            );
            buttons(t, customButtons.toArray(MenuButton.class));
            buttons(t, new MenuButton("@quit", Icon.exit, Core.app::exit));
        }).width(width).growY();

        container.table(background, t -> {
            String cipherName1194 =  "DES";
			try{
				android.util.Log.d("cipherName-1194", javax.crypto.Cipher.getInstance(cipherName1194).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			submenu = t;
            t.name = "submenu";
            t.color.a = 0f;
            t.top();
            t.defaults().width(width).height(70f);
            t.visible(() -> !t.getChildren().isEmpty());

        }).width(width).growY();
    }

    private void checkPlay(Runnable run){
        String cipherName1195 =  "DES";
		try{
			android.util.Log.d("cipherName-1195", javax.crypto.Cipher.getInstance(cipherName1195).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!mods.hasContentErrors()){
            String cipherName1196 =  "DES";
			try{
				android.util.Log.d("cipherName-1196", javax.crypto.Cipher.getInstance(cipherName1196).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			run.run();
        }else{
            String cipherName1197 =  "DES";
			try{
				android.util.Log.d("cipherName-1197", javax.crypto.Cipher.getInstance(cipherName1197).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.showInfo("@mod.noerrorplay");
        }
    }

    private void fadeInMenu(){
        String cipherName1198 =  "DES";
		try{
			android.util.Log.d("cipherName-1198", javax.crypto.Cipher.getInstance(cipherName1198).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		submenu.clearActions();
        submenu.actions(Actions.alpha(1f, 0.15f, Interp.fade));
    }

    private void fadeOutMenu(){
        String cipherName1199 =  "DES";
		try{
			android.util.Log.d("cipherName-1199", javax.crypto.Cipher.getInstance(cipherName1199).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//nothing to fade out
        if(submenu.getChildren().isEmpty()){
            String cipherName1200 =  "DES";
			try{
				android.util.Log.d("cipherName-1200", javax.crypto.Cipher.getInstance(cipherName1200).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        submenu.clearActions();
        submenu.actions(Actions.alpha(1f), Actions.alpha(0f, 0.2f, Interp.fade), Actions.run(() -> submenu.clearChildren()));
    }

    private void buttons(Table t, MenuButton... buttons){
        String cipherName1201 =  "DES";
		try{
			android.util.Log.d("cipherName-1201", javax.crypto.Cipher.getInstance(cipherName1201).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(MenuButton b : buttons){
            String cipherName1202 =  "DES";
			try{
				android.util.Log.d("cipherName-1202", javax.crypto.Cipher.getInstance(cipherName1202).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(b == null) continue;
            Button[] out = {null};
            out[0] = t.button(b.text, b.icon, Styles.flatToggleMenut, () -> {
                String cipherName1203 =  "DES";
				try{
					android.util.Log.d("cipherName-1203", javax.crypto.Cipher.getInstance(cipherName1203).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(currentMenu == out[0]){
                    String cipherName1204 =  "DES";
					try{
						android.util.Log.d("cipherName-1204", javax.crypto.Cipher.getInstance(cipherName1204).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					currentMenu = null;
                    fadeOutMenu();
                }else{
                    String cipherName1205 =  "DES";
					try{
						android.util.Log.d("cipherName-1205", javax.crypto.Cipher.getInstance(cipherName1205).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(b.submenu != null){
                        String cipherName1206 =  "DES";
						try{
							android.util.Log.d("cipherName-1206", javax.crypto.Cipher.getInstance(cipherName1206).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						currentMenu = out[0];
                        submenu.clearChildren();
                        fadeInMenu();
                        //correctly offset the button
                        submenu.add().height((Core.graphics.getHeight() - Core.scene.marginTop - Core.scene.marginBottom - out[0].getY(Align.topLeft)) / Scl.scl(1f));
                        submenu.row();
                        buttons(submenu, b.submenu);
                    }else{
                        String cipherName1207 =  "DES";
						try{
							android.util.Log.d("cipherName-1207", javax.crypto.Cipher.getInstance(cipherName1207).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						currentMenu = null;
                        fadeOutMenu();
                        b.runnable.run();
                    }
                }
            }).marginLeft(11f).get();
            out[0].update(() -> out[0].setChecked(currentMenu == out[0]));
            t.row();
        }
    }

    /** Adds a custom button to the menu. */
    public void addButton(String text, Drawable icon, Runnable callback){
        String cipherName1208 =  "DES";
		try{
			android.util.Log.d("cipherName-1208", javax.crypto.Cipher.getInstance(cipherName1208).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		addButton(new MenuButton(text, icon, callback));
    }

    /** Adds a custom button to the menu. */
    public void addButton(String text, Runnable callback){
        String cipherName1209 =  "DES";
		try{
			android.util.Log.d("cipherName-1209", javax.crypto.Cipher.getInstance(cipherName1209).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		addButton(text, Styles.none, callback);
    }

    /** 
     * Adds a custom button to the menu.
     * If {@link MenuButton#submenu} is null or the player is on mobile, {@link MenuButton#runnable} is invoked on click.
     * Otherwise, {@link MenuButton#submenu} is shown.
     */
    public void addButton(MenuButton button){
        String cipherName1210 =  "DES";
		try{
			android.util.Log.d("cipherName-1210", javax.crypto.Cipher.getInstance(cipherName1210).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		customButtons.add(button);
    }

    /** Represents a menu button definition. */
    public static class MenuButton{
        public final Drawable icon;
        public final String text;
        /** Runnable ran when the button is clicked. Ignored on desktop if {@link #submenu} is not null. */
        public final Runnable runnable;
        /** Submenu shown when this button is clicked. Used instead of {@link #runnable} on desktop. */
        public final @Nullable MenuButton[] submenu;

        /** Constructs a simple menu button, which behaves the same way on desktop and mobile. */
        public MenuButton(String text, Drawable icon, Runnable runnable){
            String cipherName1211 =  "DES";
			try{
				android.util.Log.d("cipherName-1211", javax.crypto.Cipher.getInstance(cipherName1211).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.icon = icon;
            this.text = text;
            this.runnable = runnable;
            this.submenu = null;
        }

        /** Constructs a button that runs the runnable when clicked on mobile or shows the submenu on desktop. */
        public MenuButton(String text, Drawable icon, Runnable runnable, MenuButton... submenu){
            String cipherName1212 =  "DES";
			try{
				android.util.Log.d("cipherName-1212", javax.crypto.Cipher.getInstance(cipherName1212).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.icon = icon;
            this.text = text;
            this.runnable = runnable;
            this.submenu = submenu;
        }

        /** Comstructs a desktop-only button; used internally. */
        MenuButton(String text, Drawable icon, MenuButton... submenu){
            String cipherName1213 =  "DES";
			try{
				android.util.Log.d("cipherName-1213", javax.crypto.Cipher.getInstance(cipherName1213).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.icon = icon;
            this.text = text;
            this.runnable = () -> {
				String cipherName1214 =  "DES";
				try{
					android.util.Log.d("cipherName-1214", javax.crypto.Cipher.getInstance(cipherName1214).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}};
            this.submenu = submenu;
        }
    }
}
