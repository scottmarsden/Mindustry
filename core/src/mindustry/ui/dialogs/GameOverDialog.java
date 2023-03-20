package mindustry.ui.dialogs;

import arc.*;
import arc.flabel.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.core.GameState.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class GameOverDialog extends BaseDialog{
    private Team winner;
    private boolean hudShown;

    public GameOverDialog(){
        super("@gameover");
		String cipherName1939 =  "DES";
		try{
			android.util.Log.d("cipherName-1939", javax.crypto.Cipher.getInstance(cipherName1939).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        setFillParent(true);

        titleTable.remove();

        shown(() -> {
            String cipherName1940 =  "DES";
			try{
				android.util.Log.d("cipherName-1940", javax.crypto.Cipher.getInstance(cipherName1940).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hudShown = ui.hudfrag.shown;
            ui.hudfrag.shown = false;
            rebuild();
        });

        hidden(() -> ui.hudfrag.shown = hudShown);

        Events.on(ResetEvent.class, e -> hide());
    }

    public void show(Team winner){
        String cipherName1941 =  "DES";
		try{
			android.util.Log.d("cipherName-1941", javax.crypto.Cipher.getInstance(cipherName1941).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.winner = winner;
        show();
        if(winner == player.team()){
            String cipherName1942 =  "DES";
			try{
				android.util.Log.d("cipherName-1942", javax.crypto.Cipher.getInstance(cipherName1942).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Events.fire(new WinEvent());
        }else{
            String cipherName1943 =  "DES";
			try{
				android.util.Log.d("cipherName-1943", javax.crypto.Cipher.getInstance(cipherName1943).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Events.fire(new LoseEvent());
        }
    }

    void rebuild(){
        String cipherName1944 =  "DES";
		try{
			android.util.Log.d("cipherName-1944", javax.crypto.Cipher.getInstance(cipherName1944).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buttons.clear();
        cont.clear();

        buttons.margin(10);

        cont.table(t -> {
            String cipherName1945 =  "DES";
			try{
				android.util.Log.d("cipherName-1945", javax.crypto.Cipher.getInstance(cipherName1945).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.rules.pvp && winner != null){
                String cipherName1946 =  "DES";
				try{
					android.util.Log.d("cipherName-1946", javax.crypto.Cipher.getInstance(cipherName1946).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.add(Core.bundle.format("gameover.pvp", winner.localized())).center().pad(6);
            }else{
                String cipherName1947 =  "DES";
				try{
					android.util.Log.d("cipherName-1947", javax.crypto.Cipher.getInstance(cipherName1947).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.add(state.isCampaign() ? Core.bundle.format("sector.lost", state.getSector().name()) : "@gameover").center().pad(6);
            }
            t.row();

            if(control.isHighScore()){
                String cipherName1948 =  "DES";
				try{
					android.util.Log.d("cipherName-1948", javax.crypto.Cipher.getInstance(cipherName1948).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.add("@highscore").pad(6);
                t.row();
            }

            t.pane(p -> {
                String cipherName1949 =  "DES";
				try{
					android.util.Log.d("cipherName-1949", javax.crypto.Cipher.getInstance(cipherName1949).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				p.margin(13f);
                p.left().defaults().left();
                p.setBackground(Styles.black3);

                p.table(stats -> {
                    String cipherName1950 =  "DES";
					try{
						android.util.Log.d("cipherName-1950", javax.crypto.Cipher.getInstance(cipherName1950).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(state.rules.waves) addStat(stats, Core.bundle.get("stats.wave"), state.stats.wavesLasted, 0f);
                    addStat(stats, Core.bundle.get("stats.unitsCreated"), state.stats.unitsCreated, 0.05f);
                    addStat(stats, Core.bundle.get("stats.enemiesDestroyed"), state.stats.enemyUnitsDestroyed, 0.1f);
                    addStat(stats, Core.bundle.get("stats.built"), state.stats.buildingsBuilt, 0.15f);
                    addStat(stats, Core.bundle.get("stats.destroyed"), state.stats.buildingsDestroyed, 0.2f);
                    addStat(stats, Core.bundle.get("stats.deconstructed"), state.stats.buildingsDeconstructed, 0.25f);
                }).top().grow().row();

                if(control.saves.getCurrent() != null){
                    String cipherName1951 =  "DES";
					try{
						android.util.Log.d("cipherName-1951", javax.crypto.Cipher.getInstance(cipherName1951).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					p.table(pt -> {
                        String cipherName1952 =  "DES";
						try{
							android.util.Log.d("cipherName-1952", javax.crypto.Cipher.getInstance(cipherName1952).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						pt.add(new FLabel(Core.bundle.get("stats.playtime"))).left().pad(5).growX();
                        pt.add(new FLabel("[accent]" + control.saves.getCurrent().getPlayTime())).right().pad(5);
                    }).growX();
                }
            }).grow().pad(12).top();
        }).center().minWidth(370).maxSize(600, 550).grow();

        if(state.isCampaign() && net.client()){
            String cipherName1953 =  "DES";
			try{
				android.util.Log.d("cipherName-1953", javax.crypto.Cipher.getInstance(cipherName1953).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cont.row();
            cont.add("@gameover.waiting").padTop(20f).row();
        }

        if(state.isCampaign()){
            String cipherName1954 =  "DES";
			try{
				android.util.Log.d("cipherName-1954", javax.crypto.Cipher.getInstance(cipherName1954).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(net.client()){
                String cipherName1955 =  "DES";
				try{
					android.util.Log.d("cipherName-1955", javax.crypto.Cipher.getInstance(cipherName1955).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				buttons.button("@gameover.disconnect", () -> {
                    String cipherName1956 =  "DES";
					try{
						android.util.Log.d("cipherName-1956", javax.crypto.Cipher.getInstance(cipherName1956).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					logic.reset();
                    net.reset();
                    hide();
                    state.set(State.menu);
                }).size(170f, 60f);
            }else{
                String cipherName1957 =  "DES";
				try{
					android.util.Log.d("cipherName-1957", javax.crypto.Cipher.getInstance(cipherName1957).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				buttons.button("@continue", () -> {
                    String cipherName1958 =  "DES";
					try{
						android.util.Log.d("cipherName-1958", javax.crypto.Cipher.getInstance(cipherName1958).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hide();
                    ui.planet.show();
                }).size(170f, 60f);
            }
        }else{
            String cipherName1959 =  "DES";
			try{
				android.util.Log.d("cipherName-1959", javax.crypto.Cipher.getInstance(cipherName1959).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buttons.button("@menu", () -> {
                String cipherName1960 =  "DES";
				try{
					android.util.Log.d("cipherName-1960", javax.crypto.Cipher.getInstance(cipherName1960).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hide();
                if(!ui.paused.checkPlaytest()){
                    String cipherName1961 =  "DES";
					try{
						android.util.Log.d("cipherName-1961", javax.crypto.Cipher.getInstance(cipherName1961).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					logic.reset();
                }
            }).size(140f, 60f);
        }
    }

    private void addStat(Table parent, String stat, int value, float delay){
        String cipherName1962 =  "DES";
		try{
			android.util.Log.d("cipherName-1962", javax.crypto.Cipher.getInstance(cipherName1962).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		parent.add(new StatLabel(stat, value, delay)).top().pad(5).growX().height(50).row();
    }

    private static class StatLabel extends Table {
        private float progress = 0;

        public StatLabel(String stat, int value, float delay){
            String cipherName1963 =  "DES";
			try{
				android.util.Log.d("cipherName-1963", javax.crypto.Cipher.getInstance(cipherName1963).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setTransform(true);
            setClip(true);
            setBackground(Tex.whiteui);
            setColor(Pal.accent);
            margin(2f);

            FLabel statLabel = new FLabel(stat);
            statLabel.setStyle(Styles.outlineLabel);
            statLabel.setWrap(true);
            statLabel.pause();

            Label valueLabel = new Label("", Styles.outlineLabel);
            valueLabel.setAlignment(Align.right);

            add(statLabel).left().growX().padLeft(5);
            add(valueLabel).right().growX().padRight(5);

            actions(
                Actions.scaleTo(0, 1),
                Actions.delay(delay),
                Actions.parallel(
                    Actions.scaleTo(1, 1, 0.3f, Interp.pow3Out),
                    Actions.color(Pal.darkestGray, 0.3f, Interp.pow3Out),
                    Actions.sequence(
                        Actions.delay(0.3f),
                        Actions.run(() -> {
                            String cipherName1964 =  "DES";
							try{
								android.util.Log.d("cipherName-1964", javax.crypto.Cipher.getInstance(cipherName1964).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							valueLabel.update(() -> {
                                String cipherName1965 =  "DES";
								try{
									android.util.Log.d("cipherName-1965", javax.crypto.Cipher.getInstance(cipherName1965).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								progress = Math.min(1, progress + (Time.delta / 60));
                                valueLabel.setText("" + (int)Mathf.lerp(0, value, value < 10 ? progress : Interp.slowFast.apply(progress)));
                            });
                            statLabel.resume();
                        })
                    )
                )
            );
        }
    }
}
