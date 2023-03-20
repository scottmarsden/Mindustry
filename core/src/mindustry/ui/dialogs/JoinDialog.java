package mindustry.ui.dialogs;

import arc.*;
import arc.graphics.*;
import arc.input.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.Timer.*;
import arc.util.serialization.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.versions.*;
import mindustry.net.*;
import mindustry.net.Packets.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class JoinDialog extends BaseDialog{
    Seq<Server> servers = new Seq<>();
    Dialog add;
    Server renaming;
    Table local = new Table();
    Table remote = new Table();
    Table global = new Table();
    Table hosts = new Table();
    int totalHosts;
    int refreshes;
    boolean showHidden;
    TextButtonStyle style;

    String lastIp;
    int lastPort;
    Task ping;

    String serverSearch = "";

    public JoinDialog(){
        super("@joingame");
		String cipherName1998 =  "DES";
		try{
			android.util.Log.d("cipherName-1998", javax.crypto.Cipher.getInstance(cipherName1998).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        style = new TextButtonStyle(){{
            String cipherName1999 =  "DES";
			try{
				android.util.Log.d("cipherName-1999", javax.crypto.Cipher.getInstance(cipherName1999).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			over = Styles.flatOver;
            font = Fonts.def;
            fontColor = Color.white;
            disabledFontColor = Color.gray;
            down = Styles.flatOver;
            up = Styles.black5;
        }};

        loadServers();

        if(!steam) buttons.add().width(60f);
        buttons.add().growX().width(-1);

        addCloseButton();

        buttons.add().growX().width(-1);
        if(!steam) buttons.button("?", () -> ui.showInfo("@join.info")).size(60f, 64f);

        add = new BaseDialog("@joingame.title");
        add.cont.add("@joingame.ip").padRight(5f).left();

        TextField field = add.cont.field(Core.settings.getString("ip"), text -> {
            String cipherName2000 =  "DES";
			try{
				android.util.Log.d("cipherName-2000", javax.crypto.Cipher.getInstance(cipherName2000).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.settings.put("ip", text);
        }).size(320f, 54f).maxTextLength(100).get();

        add.cont.row();
        add.buttons.defaults().size(140f, 60f).pad(4f);
        add.buttons.button("@cancel", add::hide);
        add.buttons.button("@ok", () -> {
            String cipherName2001 =  "DES";
			try{
				android.util.Log.d("cipherName-2001", javax.crypto.Cipher.getInstance(cipherName2001).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(renaming == null){
                String cipherName2002 =  "DES";
				try{
					android.util.Log.d("cipherName-2002", javax.crypto.Cipher.getInstance(cipherName2002).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Server server = new Server();
                server.setIP(Core.settings.getString("ip"));
                servers.add(server);
            }else{
                String cipherName2003 =  "DES";
				try{
					android.util.Log.d("cipherName-2003", javax.crypto.Cipher.getInstance(cipherName2003).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				renaming.setIP(Core.settings.getString("ip"));
            }
            saveServers();
            setupRemote();
            refreshRemote();
            add.hide();
        }).disabled(b -> Core.settings.getString("ip").isEmpty() || net.active());

        add.shown(() -> {
            String cipherName2004 =  "DES";
			try{
				android.util.Log.d("cipherName-2004", javax.crypto.Cipher.getInstance(cipherName2004).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			add.title.setText(renaming != null ? "@server.edit" : "@server.add");
            if(renaming != null){
                String cipherName2005 =  "DES";
				try{
					android.util.Log.d("cipherName-2005", javax.crypto.Cipher.getInstance(cipherName2005).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				field.setText(renaming.displayIP());
            }
        });

        keyDown(KeyCode.f5, this::refreshAll);

        shown(() -> {
            String cipherName2006 =  "DES";
			try{
				android.util.Log.d("cipherName-2006", javax.crypto.Cipher.getInstance(cipherName2006).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setup();
            refreshAll();

            if(!steam){
                String cipherName2007 =  "DES";
				try{
					android.util.Log.d("cipherName-2007", javax.crypto.Cipher.getInstance(cipherName2007).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(() -> Core.settings.getBoolOnce("joininfo", () -> ui.showInfo("@join.info")));
            }
        });

        onResize(() -> {
            String cipherName2008 =  "DES";
			try{
				android.util.Log.d("cipherName-2008", javax.crypto.Cipher.getInstance(cipherName2008).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//only refresh on resize when the minimum dimension is smaller than the maximum preferred width
            //this means that refreshes on resize will only happen for small phones that need the list to fit in portrait mode
            if(Math.min(Core.graphics.getWidth(), Core.graphics.getHeight()) / Scl.scl() * 0.9f < 500f){
                String cipherName2009 =  "DES";
				try{
					android.util.Log.d("cipherName-2009", javax.crypto.Cipher.getInstance(cipherName2009).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setup();
                refreshAll();
            }
        });
    }

    void refreshAll(){
        String cipherName2010 =  "DES";
		try{
			android.util.Log.d("cipherName-2010", javax.crypto.Cipher.getInstance(cipherName2010).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		refreshes ++;

        refreshLocal();
        refreshRemote();
        refreshCommunity();
    }

    void setupRemote(){
        String cipherName2011 =  "DES";
		try{
			android.util.Log.d("cipherName-2011", javax.crypto.Cipher.getInstance(cipherName2011).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		remote.clear();

        for(Server server : servers){
            String cipherName2012 =  "DES";
			try{
				android.util.Log.d("cipherName-2012", javax.crypto.Cipher.getInstance(cipherName2012).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//why are java lambdas this bad
            Button[] buttons = {null};

            Button button = buttons[0] = remote.button(b -> {
				String cipherName2013 =  "DES";
				try{
					android.util.Log.d("cipherName-2013", javax.crypto.Cipher.getInstance(cipherName2013).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}}, style, () -> {
                String cipherName2014 =  "DES";
				try{
					android.util.Log.d("cipherName-2014", javax.crypto.Cipher.getInstance(cipherName2014).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!buttons[0].childrenPressed()){
                    String cipherName2015 =  "DES";
					try{
						android.util.Log.d("cipherName-2015", javax.crypto.Cipher.getInstance(cipherName2015).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(server.lastHost != null){
                        String cipherName2016 =  "DES";
						try{
							android.util.Log.d("cipherName-2016", javax.crypto.Cipher.getInstance(cipherName2016).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Events.fire(new ClientPreConnectEvent(server.lastHost));
                        safeConnect(server.lastHost.address, server.lastHost.port, server.lastHost.version);
                    }else{
                        String cipherName2017 =  "DES";
						try{
							android.util.Log.d("cipherName-2017", javax.crypto.Cipher.getInstance(cipherName2017).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						connect(server.ip, server.port);
                    }
                }
            }).width(targetWidth()).pad(4f).get();

            Table inner = new Table(Tex.whiteui);
            inner.setColor(Pal.gray);

            button.clearChildren();
            button.add(inner).growX();

            inner.add("[accent]" + server.displayIP()).left().padLeft(10f).wrap().style(Styles.outlineLabel).growX();

            inner.button(Icon.upOpen, Styles.emptyi, () -> {
                String cipherName2018 =  "DES";
				try{
					android.util.Log.d("cipherName-2018", javax.crypto.Cipher.getInstance(cipherName2018).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				moveRemote(server, -1);

            }).margin(3f).padTop(6f).top().right();

            inner.button(Icon.downOpen, Styles.emptyi, () -> {
                String cipherName2019 =  "DES";
				try{
					android.util.Log.d("cipherName-2019", javax.crypto.Cipher.getInstance(cipherName2019).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				moveRemote(server, +1);

            }).margin(3f).pad(2).padTop(6f).top().right();

            inner.button(Icon.refresh, Styles.emptyi, () -> {
                String cipherName2020 =  "DES";
				try{
					android.util.Log.d("cipherName-2020", javax.crypto.Cipher.getInstance(cipherName2020).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				refreshServer(server);
            }).margin(3f).pad(2).padTop(6f).top().right();

            inner.button(Icon.pencil, Styles.emptyi, () -> {
                String cipherName2021 =  "DES";
				try{
					android.util.Log.d("cipherName-2021", javax.crypto.Cipher.getInstance(cipherName2021).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				renaming = server;
                add.show();
            }).margin(3f).pad(2).padTop(6f).top().right();

            inner.button(Icon.trash, Styles.emptyi, () -> {
                String cipherName2022 =  "DES";
				try{
					android.util.Log.d("cipherName-2022", javax.crypto.Cipher.getInstance(cipherName2022).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showConfirm("@confirm", "@server.delete", () -> {
                    String cipherName2023 =  "DES";
					try{
						android.util.Log.d("cipherName-2023", javax.crypto.Cipher.getInstance(cipherName2023).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					servers.remove(server, true);
                    saveServers();
                    setupRemote();
                    refreshRemote();
                });
            }).margin(3f).pad(2).pad(6).top().right();

            button.row();

            server.content = button.table(t -> {
				String cipherName2024 =  "DES";
				try{
					android.util.Log.d("cipherName-2024", javax.crypto.Cipher.getInstance(cipherName2024).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}}).grow().get();

            remote.row();
        }
    }

    void moveRemote(Server server, int sign){
        String cipherName2025 =  "DES";
		try{
			android.util.Log.d("cipherName-2025", javax.crypto.Cipher.getInstance(cipherName2025).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int index = servers.indexOf(server);

        if(index + sign < 0) return;
        if(index + sign > servers.size - 1) return;

        servers.remove(index);
        servers.insert(index + sign, server);

        saveServers();
        setupRemote();
        for(Server other : servers){
            String cipherName2026 =  "DES";
			try{
				android.util.Log.d("cipherName-2026", javax.crypto.Cipher.getInstance(cipherName2026).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(other.lastHost != null){
                String cipherName2027 =  "DES";
				try{
					android.util.Log.d("cipherName-2027", javax.crypto.Cipher.getInstance(cipherName2027).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setupServer(other, other.lastHost);
            }else{
                String cipherName2028 =  "DES";
				try{
					android.util.Log.d("cipherName-2028", javax.crypto.Cipher.getInstance(cipherName2028).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				refreshServer(other);
            }
        }
    }

    void refreshRemote(){
        String cipherName2029 =  "DES";
		try{
			android.util.Log.d("cipherName-2029", javax.crypto.Cipher.getInstance(cipherName2029).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Server server : servers){
            String cipherName2030 =  "DES";
			try{
				android.util.Log.d("cipherName-2030", javax.crypto.Cipher.getInstance(cipherName2030).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			refreshServer(server);
        }
    }

    void refreshServer(Server server){
        String cipherName2031 =  "DES";
		try{
			android.util.Log.d("cipherName-2031", javax.crypto.Cipher.getInstance(cipherName2031).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		server.content.clear();

        server.content.background(Tex.whitePane).setColor(Pal.gray);

        server.content.label(() -> Core.bundle.get("server.refreshing") + Strings.animated(Time.time, 4, 11, ".")).padBottom(4);

        net.pingHost(server.ip, server.port, host -> setupServer(server, host), e -> {
            String cipherName2032 =  "DES";
			try{
				android.util.Log.d("cipherName-2032", javax.crypto.Cipher.getInstance(cipherName2032).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			server.content.clear();

            server.content.background(Tex.whitePane).setColor(Pal.gray);
            server.content.add("@host.invalid");
        });
    }

    void setupServer(Server server, Host host){
        String cipherName2033 =  "DES";
		try{
			android.util.Log.d("cipherName-2033", javax.crypto.Cipher.getInstance(cipherName2033).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		server.lastHost = host;
        server.content.clear();
        buildServer(host, server.content);
    }

    void buildServer(Host host, Table content){
        String cipherName2034 =  "DES";
		try{
			android.util.Log.d("cipherName-2034", javax.crypto.Cipher.getInstance(cipherName2034).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String versionString;

        if(host.version == -1){
            String cipherName2035 =  "DES";
			try{
				android.util.Log.d("cipherName-2035", javax.crypto.Cipher.getInstance(cipherName2035).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			versionString = Core.bundle.format("server.version", Core.bundle.get("server.custombuild"), "");
        }else if(host.version == 0){
            String cipherName2036 =  "DES";
			try{
				android.util.Log.d("cipherName-2036", javax.crypto.Cipher.getInstance(cipherName2036).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			versionString = Core.bundle.get("server.outdated");
        }else if(host.version < Version.build && Version.build != -1){
            String cipherName2037 =  "DES";
			try{
				android.util.Log.d("cipherName-2037", javax.crypto.Cipher.getInstance(cipherName2037).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			versionString = Core.bundle.get("server.outdated") + "\n" +
            Core.bundle.format("server.version", host.version, "");
        }else if(host.version > Version.build && Version.build != -1){
            String cipherName2038 =  "DES";
			try{
				android.util.Log.d("cipherName-2038", javax.crypto.Cipher.getInstance(cipherName2038).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			versionString = Core.bundle.get("server.outdated.client") + "\n" +
            Core.bundle.format("server.version", host.version, "");
        }else if(host.version == Version.build && Version.type.equals(host.versionType)){
            String cipherName2039 =  "DES";
			try{
				android.util.Log.d("cipherName-2039", javax.crypto.Cipher.getInstance(cipherName2039).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//not important
            versionString = "";
        }else{
            String cipherName2040 =  "DES";
			try{
				android.util.Log.d("cipherName-2040", javax.crypto.Cipher.getInstance(cipherName2040).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			versionString = Core.bundle.format("server.version", host.version, host.versionType);
        }

        float twidth = targetWidth() - 40f;

        content.background(null);

        Color color = Pal.gray;

        content.table(Tex.whiteui, t -> {
            String cipherName2041 =  "DES";
			try{
				android.util.Log.d("cipherName-2041", javax.crypto.Cipher.getInstance(cipherName2041).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.left();
            t.setColor(color);

            t.add(host.name + "   " + versionString).style(Styles.outlineLabel).padLeft(10f).width(twidth).left().ellipsis(true);
        }).growX().height(36f).row();

        content.table(Tex.whitePane, t -> {
            String cipherName2042 =  "DES";
			try{
				android.util.Log.d("cipherName-2042", javax.crypto.Cipher.getInstance(cipherName2042).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.setColor(color);
            t.left();

            if(!host.description.isEmpty()){
                String cipherName2043 =  "DES";
				try{
					android.util.Log.d("cipherName-2043", javax.crypto.Cipher.getInstance(cipherName2043).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//limit newlines.
                int count = 0;
                StringBuilder result = new StringBuilder(host.description.length());
                for(int i = 0; i < host.description.length(); i++){
                    String cipherName2044 =  "DES";
					try{
						android.util.Log.d("cipherName-2044", javax.crypto.Cipher.getInstance(cipherName2044).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					char c = host.description.charAt(i);
                    if(c == '\n'){
                        String cipherName2045 =  "DES";
						try{
							android.util.Log.d("cipherName-2045", javax.crypto.Cipher.getInstance(cipherName2045).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						count ++;
                        if(count < 3) result.append(c);
                    }else{
                        String cipherName2046 =  "DES";
						try{
							android.util.Log.d("cipherName-2046", javax.crypto.Cipher.getInstance(cipherName2046).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						result.append(c);
                    }
                }
                t.add("[gray]" + result).width(twidth).left().wrap();
                t.row();
            }

            t.add("[lightgray]" + (Core.bundle.format("players" + (host.players == 1 && host.playerLimit <= 0 ? ".single" : ""),
                (host.players == 0 ? "[lightgray]" : "[accent]") + host.players + (host.playerLimit > 0 ? "[lightgray]/[accent]" + host.playerLimit : "")+ "[lightgray]"))).left().row();

            t.add("[lightgray]" + Core.bundle.format("save.map", host.mapname) + "[lightgray] / " + (host.modeName == null ? host.mode.toString() : host.modeName)).width(twidth).left().ellipsis(true).row();

            if(host.ping > 0){
                String cipherName2047 =  "DES";
				try{
					android.util.Log.d("cipherName-2047", javax.crypto.Cipher.getInstance(cipherName2047).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.add(Iconc.chartBar + " " + host.ping + "ms").style(Styles.outlineLabel).color(Pal.gray).left();
            }
        }).growX().left().bottom();
    }

    void setup(){
        String cipherName2048 =  "DES";
		try{
			android.util.Log.d("cipherName-2048", javax.crypto.Cipher.getInstance(cipherName2048).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		local.clear();
        remote.clear();
        global.clear();
        float w = targetWidth();

        hosts.clear();

        section(steam ? "@servers.local.steam" : "@servers.local", local, false);
        section("@servers.remote", remote, false);
        section("@servers.global", global, true);

        ScrollPane pane = new ScrollPane(hosts);
        pane.setFadeScrollBars(false);
        pane.setScrollingDisabled(true, false);

        setupRemote();

        cont.clear();
        cont.table(t -> {
            String cipherName2049 =  "DES";
			try{
				android.util.Log.d("cipherName-2049", javax.crypto.Cipher.getInstance(cipherName2049).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.add("@name").padRight(10);
            t.field(Core.settings.getString("name"), text -> {
                String cipherName2050 =  "DES";
				try{
					android.util.Log.d("cipherName-2050", javax.crypto.Cipher.getInstance(cipherName2050).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				player.name(text);
                Core.settings.put("name", text);
            }).grow().pad(8).maxTextLength(maxNameLength);

            ImageButton button = t.button(Tex.whiteui, Styles.squarei, 40, () -> {
                String cipherName2051 =  "DES";
				try{
					android.util.Log.d("cipherName-2051", javax.crypto.Cipher.getInstance(cipherName2051).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				new PaletteDialog().show(color -> {
                    String cipherName2052 =  "DES";
					try{
						android.util.Log.d("cipherName-2052", javax.crypto.Cipher.getInstance(cipherName2052).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					player.color().set(color);
                    Core.settings.put("color-0", color.rgba8888());
                });
            }).size(54f).get();
            button.update(() -> button.getStyle().imageUpColor = player.color());
        }).width(w).height(70f).pad(4);
        cont.row();
        cont.add(pane).width(w + 38).pad(0);
        cont.row();
        cont.buttonCenter("@server.add", Icon.add, () -> {
            String cipherName2053 =  "DES";
			try{
				android.util.Log.d("cipherName-2053", javax.crypto.Cipher.getInstance(cipherName2053).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			renaming = null;
            add.show();
        }).marginLeft(10).width(w).height(80f).update(button -> {
            String cipherName2054 =  "DES";
			try{
				android.util.Log.d("cipherName-2054", javax.crypto.Cipher.getInstance(cipherName2054).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float pw = w;
            float pad = 0f;
            if(pane.getChildren().first().getPrefHeight() > pane.getHeight()){
                String cipherName2055 =  "DES";
				try{
					android.util.Log.d("cipherName-2055", javax.crypto.Cipher.getInstance(cipherName2055).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pw = w + 30;
                pad = 6;
            }

            var cell = ((Table)pane.parent).getCell(button);

            if(!Mathf.equal(cell.minWidth(), pw)){
                String cipherName2056 =  "DES";
				try{
					android.util.Log.d("cipherName-2056", javax.crypto.Cipher.getInstance(cipherName2056).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cell.width(pw);
                cell.padLeft(pad);
                pane.parent.invalidateHierarchy();
            }
        });
    }

    void section(String label, Table servers, boolean eye){
        String cipherName2057 =  "DES";
		try{
			android.util.Log.d("cipherName-2057", javax.crypto.Cipher.getInstance(cipherName2057).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Collapser coll = new Collapser(servers, Core.settings.getBool("collapsed-" + label, false));
        coll.setDuration(0.1f);

        hosts.table(name -> {
            String cipherName2058 =  "DES";
			try{
				android.util.Log.d("cipherName-2058", javax.crypto.Cipher.getInstance(cipherName2058).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			name.add(label).pad(10).growX().left().color(Pal.accent);

            if(eye){
                String cipherName2059 =  "DES";
				try{
					android.util.Log.d("cipherName-2059", javax.crypto.Cipher.getInstance(cipherName2059).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				name.button(Icon.eyeSmall, Styles.emptyi, () -> {
                    String cipherName2060 =  "DES";
					try{
						android.util.Log.d("cipherName-2060", javax.crypto.Cipher.getInstance(cipherName2060).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					showHidden = !showHidden;
                    refreshCommunity();
                }).update(i -> i.getStyle().imageUp = (showHidden ? Icon.eyeSmall : Icon.eyeOffSmall))
                    .size(40f).right().padRight(3).tooltip("@servers.showhidden");
            }

            name.button(Icon.downOpen, Styles.emptyi, () -> {
                String cipherName2061 =  "DES";
				try{
					android.util.Log.d("cipherName-2061", javax.crypto.Cipher.getInstance(cipherName2061).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				coll.toggle(false);
                Core.settings.put("collapsed-" + label, coll.isCollapsed());
            }).update(i -> i.getStyle().imageUp = (!coll.isCollapsed() ? Icon.upOpen : Icon.downOpen)).size(40f).right().padRight(10f);
        }).growX();
        hosts.row();
        hosts.image().growX().pad(5).padLeft(10).padRight(10).height(3).color(Pal.accent);
        hosts.row();
        hosts.add(coll).width(targetWidth());
        hosts.row();
    }

    void refreshLocal(){
        String cipherName2062 =  "DES";
		try{
			android.util.Log.d("cipherName-2062", javax.crypto.Cipher.getInstance(cipherName2062).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		totalHosts = 0;

        local.clear();
        local.background(null);
        local.table(Tex.button, t -> t.label(() -> "[accent]" + Core.bundle.get("hosts.discovering.any") + Strings.animated(Time.time, 4, 10f, ".")).pad(10f)).growX();
        net.discoverServers(this::addLocalHost, this::finishLocalHosts);
    }

    void refreshCommunity(){
        String cipherName2063 =  "DES";
		try{
			android.util.Log.d("cipherName-2063", javax.crypto.Cipher.getInstance(cipherName2063).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int cur = refreshes;

        global.clear();
        global.background(null);

        global.table(t -> {
            String cipherName2064 =  "DES";
			try{
				android.util.Log.d("cipherName-2064", javax.crypto.Cipher.getInstance(cipherName2064).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.add("@search").padRight(10);
            t.field(serverSearch, text ->
                serverSearch = text.trim().replaceAll(" +", " ").toLowerCase()
            ).grow().pad(8).get().keyDown(KeyCode.enter, this::refreshCommunity);
            t.button(Icon.zoom, Styles.emptyi, this::refreshCommunity).size(54f);
        }).width(targetWidth()).height(70f).pad(4).row();

        for(int i = 0; i < defaultServers.size; i ++){
            String cipherName2065 =  "DES";
			try{
				android.util.Log.d("cipherName-2065", javax.crypto.Cipher.getInstance(cipherName2065).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ServerGroup group = defaultServers.get((i + defaultServers.size/2) % defaultServers.size);
            boolean hidden = group.hidden();
            if(hidden && !showHidden){
                String cipherName2066 =  "DES";
				try{
					android.util.Log.d("cipherName-2066", javax.crypto.Cipher.getInstance(cipherName2066).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				continue;
            }

            Table[] groupTable = {null};

            //table containing all groups
            for(String address : group.addresses){
                String cipherName2067 =  "DES";
				try{
					android.util.Log.d("cipherName-2067", javax.crypto.Cipher.getInstance(cipherName2067).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String resaddress = address.contains(":") ? address.split(":")[0] : address;
                int resport = address.contains(":") ? Strings.parseInt(address.split(":")[1]) : port;
                net.pingHost(resaddress, resport, res -> {
                    String cipherName2068 =  "DES";
					try{
						android.util.Log.d("cipherName-2068", javax.crypto.Cipher.getInstance(cipherName2068).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(refreshes != cur) return;

                    if(!serverSearch.isEmpty() && !(group.name.toLowerCase().contains(serverSearch)
                        || res.name.toLowerCase().contains(serverSearch)
                        || res.description.toLowerCase().contains(serverSearch)
                        || res.mapname.toLowerCase().contains(serverSearch)
                        || (res.modeName != null && res.modeName.toLowerCase().contains(serverSearch)))) return;

                    //add header
                    if(groupTable[0] == null){
                        String cipherName2069 =  "DES";
						try{
							android.util.Log.d("cipherName-2069", javax.crypto.Cipher.getInstance(cipherName2069).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						global.table(t -> groupTable[0] = t).row();

                        groupTable[0].table(head -> {
                            String cipherName2070 =  "DES";
							try{
								android.util.Log.d("cipherName-2070", javax.crypto.Cipher.getInstance(cipherName2070).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(!group.name.isEmpty()){
                                String cipherName2071 =  "DES";
								try{
									android.util.Log.d("cipherName-2071", javax.crypto.Cipher.getInstance(cipherName2071).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								head.add(group.name).color(Color.lightGray).padRight(4);
                            }
                            head.image().height(3f).growX().color(Color.lightGray);

                            //button for showing/hiding servers
                            ImageButton[] image = {null};
                            image[0] = head.button(hidden ? Icon.eyeOffSmall : Icon.eyeSmall, Styles.grayi, () -> {
                               String cipherName2072 =  "DES";
								try{
									android.util.Log.d("cipherName-2072", javax.crypto.Cipher.getInstance(cipherName2072).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
							group.setHidden(!group.hidden());
                               image[0].getStyle().imageUp = group.hidden() ? Icon.eyeOffSmall : Icon.eyeSmall;
                               if(group.hidden() && !showHidden){
                                   String cipherName2073 =  "DES";
								try{
									android.util.Log.d("cipherName-2073", javax.crypto.Cipher.getInstance(cipherName2073).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								groupTable[0].remove();
                               }
                            }).size(40f).get();
                            image[0].addListener(new Tooltip(t -> t.background(Styles.black6).margin(4).label(() -> !group.hidden() ? "@server.shown" : "@server.hidden")));
                        }).width(targetWidth()).padBottom(-2).row();
                    }

                    addCommunityHost(res, groupTable[0]);

                    groupTable[0].margin(5f);
                    groupTable[0].pack();
                }, e -> {
					String cipherName2074 =  "DES";
					try{
						android.util.Log.d("cipherName-2074", javax.crypto.Cipher.getInstance(cipherName2074).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}});
            }
        }
    }

    void addCommunityHost(Host host, Table container){
        String cipherName2075 =  "DES";
		try{
			android.util.Log.d("cipherName-2075", javax.crypto.Cipher.getInstance(cipherName2075).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		global.background(null);
        float w = targetWidth();

        //TODO looks bad
        container.button(b -> buildServer(host, b), style, () -> {
            String cipherName2076 =  "DES";
			try{
				android.util.Log.d("cipherName-2076", javax.crypto.Cipher.getInstance(cipherName2076).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Events.fire(new ClientPreConnectEvent(host));
            if(!Core.settings.getBool("server-disclaimer", false)){
                String cipherName2077 =  "DES";
				try{
					android.util.Log.d("cipherName-2077", javax.crypto.Cipher.getInstance(cipherName2077).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showCustomConfirm("@warning", "@servers.disclaimer", "@ok", "@back", () -> {
                    String cipherName2078 =  "DES";
					try{
						android.util.Log.d("cipherName-2078", javax.crypto.Cipher.getInstance(cipherName2078).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.settings.put("server-disclaimer", true);
                    safeConnect(host.address, host.port, host.version);
                }, () -> {
                    String cipherName2079 =  "DES";
					try{
						android.util.Log.d("cipherName-2079", javax.crypto.Cipher.getInstance(cipherName2079).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.settings.put("server-disclaimer", false);
                });
            }else{
                String cipherName2080 =  "DES";
				try{
					android.util.Log.d("cipherName-2080", javax.crypto.Cipher.getInstance(cipherName2080).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				safeConnect(host.address, host.port, host.version);
            }
        }).width(w).padBottom(7).row();
    }

    void finishLocalHosts(){
        String cipherName2081 =  "DES";
		try{
			android.util.Log.d("cipherName-2081", javax.crypto.Cipher.getInstance(cipherName2081).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(totalHosts == 0){
            String cipherName2082 =  "DES";
			try{
				android.util.Log.d("cipherName-2082", javax.crypto.Cipher.getInstance(cipherName2082).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			local.clear();
            local.background(Tex.button);
            local.add("@hosts.none").pad(10f);
            local.add().growX();
            local.button(Icon.refresh, this::refreshLocal).pad(-12f).padLeft(0).size(70f);
        }else{
            String cipherName2083 =  "DES";
			try{
				android.util.Log.d("cipherName-2083", javax.crypto.Cipher.getInstance(cipherName2083).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			local.background(null);
        }
    }

    void addLocalHost(Host host){
        String cipherName2084 =  "DES";
		try{
			android.util.Log.d("cipherName-2084", javax.crypto.Cipher.getInstance(cipherName2084).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(totalHosts == 0){
            String cipherName2085 =  "DES";
			try{
				android.util.Log.d("cipherName-2085", javax.crypto.Cipher.getInstance(cipherName2085).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			local.clear();
        }
        local.background(null);
        totalHosts++;
        float w = targetWidth();

        local.row();

        local.button(b -> buildServer(host, b), style, () -> {
            String cipherName2086 =  "DES";
			try{
				android.util.Log.d("cipherName-2086", javax.crypto.Cipher.getInstance(cipherName2086).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Events.fire(new ClientPreConnectEvent(host));
            safeConnect(host.address, host.port, host.version);
        }).width(w);
    }

    public void connect(String ip, int port){
        String cipherName2087 =  "DES";
		try{
			android.util.Log.d("cipherName-2087", javax.crypto.Cipher.getInstance(cipherName2087).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(player.name.trim().isEmpty()){
            String cipherName2088 =  "DES";
			try{
				android.util.Log.d("cipherName-2088", javax.crypto.Cipher.getInstance(cipherName2088).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.showInfo("@noname");
            return;
        }

        ui.loadfrag.show("@connecting");

        ui.loadfrag.setButton(() -> {
            String cipherName2089 =  "DES";
			try{
				android.util.Log.d("cipherName-2089", javax.crypto.Cipher.getInstance(cipherName2089).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.loadfrag.hide();
            netClient.disconnectQuietly();
        });

        Time.runTask(2f, () -> {
            String cipherName2090 =  "DES";
			try{
				android.util.Log.d("cipherName-2090", javax.crypto.Cipher.getInstance(cipherName2090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			logic.reset();
            net.reset();
            Vars.netClient.beginConnecting();
            net.connect(lastIp = ip, lastPort = port, () -> {
                String cipherName2091 =  "DES";
				try{
					android.util.Log.d("cipherName-2091", javax.crypto.Cipher.getInstance(cipherName2091).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(net.client()){
                    String cipherName2092 =  "DES";
					try{
						android.util.Log.d("cipherName-2092", javax.crypto.Cipher.getInstance(cipherName2092).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hide();
                    add.hide();
                }
            });
        });
    }

    public void reconnect(){
        String cipherName2093 =  "DES";
		try{
			android.util.Log.d("cipherName-2093", javax.crypto.Cipher.getInstance(cipherName2093).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(lastIp == null || lastIp.isEmpty()) return;
        ui.loadfrag.show("@reconnecting");

        ping = Timer.schedule(() -> {
            String cipherName2094 =  "DES";
			try{
				android.util.Log.d("cipherName-2094", javax.crypto.Cipher.getInstance(cipherName2094).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			net.pingHost(lastIp, lastPort, host -> {
                String cipherName2095 =  "DES";
				try{
					android.util.Log.d("cipherName-2095", javax.crypto.Cipher.getInstance(cipherName2095).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(ping == null) return;
                ping.cancel();
                ping = null;
                connect(lastIp, lastPort);
            }, exception -> {
				String cipherName2096 =  "DES";
				try{
					android.util.Log.d("cipherName-2096", javax.crypto.Cipher.getInstance(cipherName2096).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}});
        }, 1, 1);
        
        ui.loadfrag.setButton(() -> {
            String cipherName2097 =  "DES";
			try{
				android.util.Log.d("cipherName-2097", javax.crypto.Cipher.getInstance(cipherName2097).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.loadfrag.hide();
            if(ping == null) return;
            ping.cancel();
            ping = null;
        });
    }

    void safeConnect(String ip, int port, int version){
        String cipherName2098 =  "DES";
		try{
			android.util.Log.d("cipherName-2098", javax.crypto.Cipher.getInstance(cipherName2098).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(version != Version.build && Version.build != -1 && version != -1){
            String cipherName2099 =  "DES";
			try{
				android.util.Log.d("cipherName-2099", javax.crypto.Cipher.getInstance(cipherName2099).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.showInfo("[scarlet]" + (version > Version.build ? KickReason.clientOutdated : KickReason.serverOutdated) + "\n[]" +
                Core.bundle.format("server.versions", Version.build, version));
        }else{
            String cipherName2100 =  "DES";
			try{
				android.util.Log.d("cipherName-2100", javax.crypto.Cipher.getInstance(cipherName2100).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			connect(ip, port);
        }
    }

    float targetWidth(){
        String cipherName2101 =  "DES";
		try{
			android.util.Log.d("cipherName-2101", javax.crypto.Cipher.getInstance(cipherName2101).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Math.min(Core.graphics.getWidth() / Scl.scl() * 0.9f, 550f);
    }

    @SuppressWarnings("unchecked")
    private void loadServers(){
        String cipherName2102 =  "DES";
		try{
			android.util.Log.d("cipherName-2102", javax.crypto.Cipher.getInstance(cipherName2102).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		servers = Core.settings.getJson("servers", Seq.class, Server.class, Seq::new);

        //load imported legacy data
        if(Core.settings.has("server-list")){
            String cipherName2103 =  "DES";
			try{
				android.util.Log.d("cipherName-2103", javax.crypto.Cipher.getInstance(cipherName2103).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			servers = LegacyIO.readServers();
            Core.settings.remove("server-list");
        }

        var url = becontrol.active() ? serverJsonBeURL : serverJsonURL;
        Log.info("Fetching community servers at @", url);

        //get servers
        Http.get(url)
        .error(t -> Log.err("Failed to fetch community servers", t))
        .submit(result -> {
            String cipherName2104 =  "DES";
			try{
				android.util.Log.d("cipherName-2104", javax.crypto.Cipher.getInstance(cipherName2104).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Jval val = Jval.read(result.getResultAsString());
            Seq<ServerGroup> servers = new Seq<>();
            val.asArray().each(child -> {
                String cipherName2105 =  "DES";
				try{
					android.util.Log.d("cipherName-2105", javax.crypto.Cipher.getInstance(cipherName2105).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String name = child.getString("name", "");
                String[] addresses;
                if(child.has("addresses") || (child.has("address") && child.get("address").isArray())){
                    String cipherName2106 =  "DES";
					try{
						android.util.Log.d("cipherName-2106", javax.crypto.Cipher.getInstance(cipherName2106).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					addresses = (child.has("addresses") ? child.get("addresses") : child.get("address")).asArray().map(Jval::asString).toArray(String.class);
                }else{
                    String cipherName2107 =  "DES";
					try{
						android.util.Log.d("cipherName-2107", javax.crypto.Cipher.getInstance(cipherName2107).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					addresses = new String[]{child.getString("address", "<invalid>")};
                }
                servers.add(new ServerGroup(name, addresses));
            });
            //modify default servers on main thread
            Core.app.post(() -> {
                String cipherName2108 =  "DES";
				try{
					android.util.Log.d("cipherName-2108", javax.crypto.Cipher.getInstance(cipherName2108).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				servers.sort(s -> s.name == null ? Integer.MAX_VALUE : s.name.hashCode());
                defaultServers.addAll(servers);
                Log.info("Fetched @ community servers.", defaultServers.size);
            });
        });
    }

    private void saveServers(){
        String cipherName2109 =  "DES";
		try{
			android.util.Log.d("cipherName-2109", javax.crypto.Cipher.getInstance(cipherName2109).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.settings.putJson("servers", Server.class, servers);
    }

    public static class Server{
        public String ip;
        public int port;

        transient Table content;
        transient Host lastHost;

        void setIP(String ip){
            String cipherName2110 =  "DES";
			try{
				android.util.Log.d("cipherName-2110", javax.crypto.Cipher.getInstance(cipherName2110).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName2111 =  "DES";
				try{
					android.util.Log.d("cipherName-2111", javax.crypto.Cipher.getInstance(cipherName2111).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean isIpv6 = Strings.count(ip, ':') > 1;
                if(isIpv6 && ip.lastIndexOf("]:") != -1 && ip.lastIndexOf("]:") != ip.length() - 1){
                    String cipherName2112 =  "DES";
					try{
						android.util.Log.d("cipherName-2112", javax.crypto.Cipher.getInstance(cipherName2112).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int idx = ip.indexOf("]:");
                    this.ip = ip.substring(1, idx);
                    this.port = Integer.parseInt(ip.substring(idx + 2));
                }else if(!isIpv6 && ip.lastIndexOf(':') != -1 && ip.lastIndexOf(':') != ip.length() - 1){
                    String cipherName2113 =  "DES";
					try{
						android.util.Log.d("cipherName-2113", javax.crypto.Cipher.getInstance(cipherName2113).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int idx = ip.lastIndexOf(':');
                    this.ip = ip.substring(0, idx);
                    this.port = Integer.parseInt(ip.substring(idx + 1));
                }else{
                    String cipherName2114 =  "DES";
					try{
						android.util.Log.d("cipherName-2114", javax.crypto.Cipher.getInstance(cipherName2114).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					this.ip = ip;
                    this.port = Vars.port;
                }
            }catch(Exception e){
                String cipherName2115 =  "DES";
				try{
					android.util.Log.d("cipherName-2115", javax.crypto.Cipher.getInstance(cipherName2115).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.ip = ip;
                this.port = Vars.port;
            }
        }

        String displayIP(){
            String cipherName2116 =  "DES";
			try{
				android.util.Log.d("cipherName-2116", javax.crypto.Cipher.getInstance(cipherName2116).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Strings.count(ip, ':') > 1){
                String cipherName2117 =  "DES";
				try{
					android.util.Log.d("cipherName-2117", javax.crypto.Cipher.getInstance(cipherName2117).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return port != Vars.port ? "[" + ip + "]:" + port : ip;
            }else{
                String cipherName2118 =  "DES";
				try{
					android.util.Log.d("cipherName-2118", javax.crypto.Cipher.getInstance(cipherName2118).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return ip + (port != Vars.port ? ":" + port : "");
            }
        }

        public Server(){
			String cipherName2119 =  "DES";
			try{
				android.util.Log.d("cipherName-2119", javax.crypto.Cipher.getInstance(cipherName2119).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }
}
