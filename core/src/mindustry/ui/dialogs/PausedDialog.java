package mindustry.ui.dialogs;

import arc.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

public class PausedDialog extends BaseDialog{
    private SaveDialog save = new SaveDialog();
    private LoadDialog load = new LoadDialog();
    private boolean wasClient = false;

    public PausedDialog(){
        super("@menu");
		String cipherName1909 =  "DES";
		try{
			android.util.Log.d("cipherName-1909", javax.crypto.Cipher.getInstance(cipherName1909).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        shouldPause = true;

        shown(this::rebuild);

        addCloseListener();
    }

    void rebuild(){
        String cipherName1910 =  "DES";
		try{
			android.util.Log.d("cipherName-1910", javax.crypto.Cipher.getInstance(cipherName1910).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cont.clear();

        update(() -> {
            String cipherName1911 =  "DES";
			try{
				android.util.Log.d("cipherName-1911", javax.crypto.Cipher.getInstance(cipherName1911).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.isMenu() && isShown()){
                String cipherName1912 =  "DES";
				try{
					android.util.Log.d("cipherName-1912", javax.crypto.Cipher.getInstance(cipherName1912).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hide();
            }
        });

        if(!mobile){
            String cipherName1913 =  "DES";
			try{
				android.util.Log.d("cipherName-1913", javax.crypto.Cipher.getInstance(cipherName1913).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float dw = 220f;
            cont.defaults().width(dw).height(55).pad(5f);

            cont.button("@objective", Icon.info, () -> ui.fullText.show("@objective", state.rules.sector.preset.description))
            .visible(() -> state.rules.sector != null && state.rules.sector.preset != null && state.rules.sector.preset.description != null).padTop(-60f);

            cont.button("@abandon", Icon.cancel, () -> ui.planet.abandonSectorConfirm(state.rules.sector, this::hide)).padTop(-60f)
            .disabled(b -> net.client()).visible(() -> state.rules.sector != null).row();

            cont.button("@back", Icon.left, this::hide).name("back");
            cont.button("@settings", Icon.settings, ui.settings::show).name("settings");

            if(!state.isCampaign() && !state.isEditor()){
                String cipherName1914 =  "DES";
				try{
					android.util.Log.d("cipherName-1914", javax.crypto.Cipher.getInstance(cipherName1914).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cont.row();
                cont.button("@savegame", Icon.save, save::show);
                cont.button("@loadgame", Icon.upload, load::show).disabled(b -> net.active());
            }

            cont.row();

            cont.button("@hostserver", Icon.host, () -> {
                String cipherName1915 =  "DES";
				try{
					android.util.Log.d("cipherName-1915", javax.crypto.Cipher.getInstance(cipherName1915).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(net.server() && steam){
                    String cipherName1916 =  "DES";
					try{
						android.util.Log.d("cipherName-1916", javax.crypto.Cipher.getInstance(cipherName1916).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					platform.inviteFriends();
                }else{
                    String cipherName1917 =  "DES";
					try{
						android.util.Log.d("cipherName-1917", javax.crypto.Cipher.getInstance(cipherName1917).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(steam){
                        String cipherName1918 =  "DES";
						try{
							android.util.Log.d("cipherName-1918", javax.crypto.Cipher.getInstance(cipherName1918).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.host.runHost();
                    }else{
                        String cipherName1919 =  "DES";
						try{
							android.util.Log.d("cipherName-1919", javax.crypto.Cipher.getInstance(cipherName1919).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.host.show();
                    }
                }
            }).disabled(b -> !((steam && net.server()) || !net.active())).colspan(2).width(dw * 2 + 10f).update(e -> e.setText(net.server() && steam ? "@invitefriends" : "@hostserver"));

            cont.row();

            cont.button("@quit", Icon.exit, this::showQuitConfirm).colspan(2).width(dw + 10f).update(s -> s.setText(control.saves.getCurrent() != null && control.saves.getCurrent().isAutosave() ? "@save.quit" : "@quit"));

        }else{
            String cipherName1920 =  "DES";
			try{
				android.util.Log.d("cipherName-1920", javax.crypto.Cipher.getInstance(cipherName1920).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cont.defaults().size(130f).pad(5);
            cont.buttonRow("@back", Icon.play, this::hide);
            cont.buttonRow("@settings", Icon.settings, ui.settings::show);

            if(!state.isCampaign() && !state.isEditor()){
                String cipherName1921 =  "DES";
				try{
					android.util.Log.d("cipherName-1921", javax.crypto.Cipher.getInstance(cipherName1921).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cont.buttonRow("@save", Icon.save, save::show);

                cont.row();

                cont.buttonRow("@load", Icon.download, load::show).disabled(b -> net.active());
            }else if(state.isCampaign()){
                String cipherName1922 =  "DES";
				try{
					android.util.Log.d("cipherName-1922", javax.crypto.Cipher.getInstance(cipherName1922).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cont.buttonRow("@research", Icon.tree, ui.research::show);

                cont.row();

                cont.buttonRow("@planetmap", Icon.map, () -> {
                    String cipherName1923 =  "DES";
					try{
						android.util.Log.d("cipherName-1923", javax.crypto.Cipher.getInstance(cipherName1923).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hide();
                    ui.planet.show();
                });
            }else{
                String cipherName1924 =  "DES";
				try{
					android.util.Log.d("cipherName-1924", javax.crypto.Cipher.getInstance(cipherName1924).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cont.row();
            }

            cont.buttonRow("@hostserver.mobile", Icon.host, ui.host::show).disabled(b -> net.active());

            cont.buttonRow("@quit", Icon.exit, this::showQuitConfirm).update(s -> {
                String cipherName1925 =  "DES";
				try{
					android.util.Log.d("cipherName-1925", javax.crypto.Cipher.getInstance(cipherName1925).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				s.setText(control.saves.getCurrent() != null && control.saves.getCurrent().isAutosave() ? "@save.quit" : "@quit");
                s.getLabelCell().growX().wrap();
            });
        }
    }

    void showQuitConfirm(){
        String cipherName1926 =  "DES";
		try{
			android.util.Log.d("cipherName-1926", javax.crypto.Cipher.getInstance(cipherName1926).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Runnable quit = () -> {
            String cipherName1927 =  "DES";
			try{
				android.util.Log.d("cipherName-1927", javax.crypto.Cipher.getInstance(cipherName1927).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			runExitSave();
            hide();
        };

        if(confirmExit){
            String cipherName1928 =  "DES";
			try{
				android.util.Log.d("cipherName-1928", javax.crypto.Cipher.getInstance(cipherName1928).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.showConfirm("@confirm", "@quit.confirm", quit);
        }else{
            String cipherName1929 =  "DES";
			try{
				android.util.Log.d("cipherName-1929", javax.crypto.Cipher.getInstance(cipherName1929).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			quit.run();
        }
    }

    public boolean checkPlaytest(){
        String cipherName1930 =  "DES";
		try{
			android.util.Log.d("cipherName-1930", javax.crypto.Cipher.getInstance(cipherName1930).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state.playtestingMap != null){
            String cipherName1931 =  "DES";
			try{
				android.util.Log.d("cipherName-1931", javax.crypto.Cipher.getInstance(cipherName1931).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//no exit save here
            var testing = state.playtestingMap;
            logic.reset();
            ui.editor.resumeAfterPlaytest(testing);
            return true;
        }
        return false;
    }

    public void runExitSave(){
        String cipherName1932 =  "DES";
		try{
			android.util.Log.d("cipherName-1932", javax.crypto.Cipher.getInstance(cipherName1932).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		wasClient = net.client();
        if(net.client()) netClient.disconnectQuietly();

        if(state.isEditor() && !wasClient){
            String cipherName1933 =  "DES";
			try{
				android.util.Log.d("cipherName-1933", javax.crypto.Cipher.getInstance(cipherName1933).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.editor.resumeEditing();
            return;
        }else if(checkPlaytest()){
            String cipherName1934 =  "DES";
			try{
				android.util.Log.d("cipherName-1934", javax.crypto.Cipher.getInstance(cipherName1934).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        if(control.saves.getCurrent() == null || !control.saves.getCurrent().isAutosave() || wasClient || state.gameOver){
            String cipherName1935 =  "DES";
			try{
				android.util.Log.d("cipherName-1935", javax.crypto.Cipher.getInstance(cipherName1935).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			logic.reset();
            return;
        }

        ui.loadAnd("@saving", () -> {
            String cipherName1936 =  "DES";
			try{
				android.util.Log.d("cipherName-1936", javax.crypto.Cipher.getInstance(cipherName1936).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName1937 =  "DES";
				try{
					android.util.Log.d("cipherName-1937", javax.crypto.Cipher.getInstance(cipherName1937).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				control.saves.getCurrent().save();
            }catch(Throwable e){
                String cipherName1938 =  "DES";
				try{
					android.util.Log.d("cipherName-1938", javax.crypto.Cipher.getInstance(cipherName1938).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				e.printStackTrace();
                ui.showException("[accent]" + Core.bundle.get("savefail"), e);
            }
            logic.reset();
        });
    }
}
