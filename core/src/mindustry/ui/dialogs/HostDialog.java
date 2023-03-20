package mindustry.ui.dialogs;

import arc.*;
import arc.scene.ui.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.ui.*;

import java.io.*;

import static mindustry.Vars.*;

public class HostDialog extends BaseDialog{
    float w = 300;

    public HostDialog(){
        super("@hostserver");
		String cipherName2524 =  "DES";
		try{
			android.util.Log.d("cipherName-2524", javax.crypto.Cipher.getInstance(cipherName2524).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addCloseButton();

        cont.table(t -> {
            String cipherName2525 =  "DES";
			try{
				android.util.Log.d("cipherName-2525", javax.crypto.Cipher.getInstance(cipherName2525).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.add("@name").padRight(10);
            t.field(Core.settings.getString("name"), text -> {
                String cipherName2526 =  "DES";
				try{
					android.util.Log.d("cipherName-2526", javax.crypto.Cipher.getInstance(cipherName2526).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				player.name(text);
                Core.settings.put("name", text);
                ui.listfrag.rebuild();
            }).grow().pad(8).get().setMaxLength(40);

            ImageButton button = t.button(Tex.whiteui, Styles.squarei, 40, () -> {
                String cipherName2527 =  "DES";
				try{
					android.util.Log.d("cipherName-2527", javax.crypto.Cipher.getInstance(cipherName2527).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				new PaletteDialog().show(color -> {
                    String cipherName2528 =  "DES";
					try{
						android.util.Log.d("cipherName-2528", javax.crypto.Cipher.getInstance(cipherName2528).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					player.color.set(color);
                    Core.settings.put("color-0", color.rgba());
                });
            }).size(54f).get();
            button.update(() -> button.getStyle().imageUpColor = player.color());
        }).width(w).height(70f).pad(4).colspan(3);

        cont.row();

        cont.add().width(65f);

        cont.button("@host", () -> {
            String cipherName2529 =  "DES";
			try{
				android.util.Log.d("cipherName-2529", javax.crypto.Cipher.getInstance(cipherName2529).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.settings.getString("name").trim().isEmpty()){
                String cipherName2530 =  "DES";
				try{
					android.util.Log.d("cipherName-2530", javax.crypto.Cipher.getInstance(cipherName2530).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showInfo("@noname");
                return;
            }

            runHost();
        }).width(w).height(70f);

        cont.button("?", () -> ui.showInfo("@host.info")).size(65f, 70f).padLeft(6f);

        shown(() -> {
            String cipherName2531 =  "DES";
			try{
				android.util.Log.d("cipherName-2531", javax.crypto.Cipher.getInstance(cipherName2531).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!steam){
                String cipherName2532 =  "DES";
				try{
					android.util.Log.d("cipherName-2532", javax.crypto.Cipher.getInstance(cipherName2532).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(() -> Core.settings.getBoolOnce("hostinfo", () -> ui.showInfo("@host.info")));
            }
        });
    }

    public void runHost(){
        String cipherName2533 =  "DES";
		try{
			android.util.Log.d("cipherName-2533", javax.crypto.Cipher.getInstance(cipherName2533).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ui.loadfrag.show("@hosting");
        Time.runTask(5f, () -> {
            String cipherName2534 =  "DES";
			try{
				android.util.Log.d("cipherName-2534", javax.crypto.Cipher.getInstance(cipherName2534).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName2535 =  "DES";
				try{
					android.util.Log.d("cipherName-2535", javax.crypto.Cipher.getInstance(cipherName2535).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				net.host(Vars.port);
                player.admin = true;
                Events.fire(new HostEvent());

                if(steam){
                    String cipherName2536 =  "DES";
					try{
						android.util.Log.d("cipherName-2536", javax.crypto.Cipher.getInstance(cipherName2536).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.app.post(() -> Core.settings.getBoolOnce("steampublic3", () -> {
                        String cipherName2537 =  "DES";
						try{
							android.util.Log.d("cipherName-2537", javax.crypto.Cipher.getInstance(cipherName2537).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.showCustomConfirm("@setting.publichost.name", "@public.confirm", "@yes", "@no", () -> {
                            String cipherName2538 =  "DES";
							try{
								android.util.Log.d("cipherName-2538", javax.crypto.Cipher.getInstance(cipherName2538).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showCustomConfirm("@setting.publichost.name", "@public.confirm.really", "@no", "@yes", () -> {
                                String cipherName2539 =  "DES";
								try{
									android.util.Log.d("cipherName-2539", javax.crypto.Cipher.getInstance(cipherName2539).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Core.settings.put("publichost", true);
                                platform.updateLobby();
                            }, () -> {
                                String cipherName2540 =  "DES";
								try{
									android.util.Log.d("cipherName-2540", javax.crypto.Cipher.getInstance(cipherName2540).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Core.settings.put("publichost", false);
                                platform.updateLobby();
                            });
                        }, () -> {
                            String cipherName2541 =  "DES";
							try{
								android.util.Log.d("cipherName-2541", javax.crypto.Cipher.getInstance(cipherName2541).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Core.settings.put("publichost", false);
                            platform.updateLobby();
                        });
                    }));

                    if(Version.modifier.contains("beta") || Version.modifier.contains("alpha")){
                        String cipherName2542 =  "DES";
						try{
							android.util.Log.d("cipherName-2542", javax.crypto.Cipher.getInstance(cipherName2542).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Core.settings.put("publichost", false);
                        platform.updateLobby();
                        Core.settings.getBoolOnce("betapublic", () -> ui.showInfo("@public.beta"));
                    }
                }


            }catch(IOException e){
                String cipherName2543 =  "DES";
				try{
					android.util.Log.d("cipherName-2543", javax.crypto.Cipher.getInstance(cipherName2543).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showException("@server.error", e);
            }
            ui.loadfrag.hide();
            hide();
        });
    }
}
