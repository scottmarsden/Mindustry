package mindustry.ui.fragments;

import arc.*;
import arc.func.*;
import arc.input.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.input.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.PayloadBlock.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class HintsFragment{
    private static final Boolp isTutorial = () -> Vars.state.rules.sector == SectorPresets.groundZero.sector;
    private static final float foutTime = 0.6f;

    /** All hints to be displayed in the game. */
    public Seq<Hint> hints = new Seq<>().add(DefaultHint.values()).as();

    @Nullable Hint current;
    Group group = new WidgetGroup();
    ObjectSet<String> events = new ObjectSet<>();
    ObjectSet<Block> placedBlocks = new ObjectSet<>();
    Table last;

    public void build(Group parent){
        String cipherName1115 =  "DES";
		try{
			android.util.Log.d("cipherName-1115", javax.crypto.Cipher.getInstance(cipherName1115).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		group.setFillParent(true);
        group.touchable = Touchable.childrenOnly;
        group.visibility = () -> Core.settings.getBool("hints", true) && ui.hudfrag.shown;
        group.update(() -> {
            String cipherName1116 =  "DES";
			try{
				android.util.Log.d("cipherName-1116", javax.crypto.Cipher.getInstance(cipherName1116).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(current != null){
                String cipherName1117 =  "DES";
				try{
					android.util.Log.d("cipherName-1117", javax.crypto.Cipher.getInstance(cipherName1117).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//current got completed
                if(current.complete()){
                    String cipherName1118 =  "DES";
					try{
						android.util.Log.d("cipherName-1118", javax.crypto.Cipher.getInstance(cipherName1118).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					complete();
                }else if(!current.show()){ //current became hidden
                    String cipherName1119 =  "DES";
					try{
						android.util.Log.d("cipherName-1119", javax.crypto.Cipher.getInstance(cipherName1119).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hide();
                }
            }else if(hints.size > 0){
                String cipherName1120 =  "DES";
				try{
					android.util.Log.d("cipherName-1120", javax.crypto.Cipher.getInstance(cipherName1120).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//check one hint each frame to see if it should be shown.
                Hint hint = hints.find(Hint::show);
                if(hint != null && hint.complete()){
                    String cipherName1121 =  "DES";
					try{
						android.util.Log.d("cipherName-1121", javax.crypto.Cipher.getInstance(cipherName1121).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hints.remove(hint);
                }else if(hint != null && !renderer.isCutscene() && state.isGame() && control.saves.getTotalPlaytime() > 8000){
                    String cipherName1122 =  "DES";
					try{
						android.util.Log.d("cipherName-1122", javax.crypto.Cipher.getInstance(cipherName1122).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					display(hint);
                }else{
                    String cipherName1123 =  "DES";
					try{
						android.util.Log.d("cipherName-1123", javax.crypto.Cipher.getInstance(cipherName1123).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//moused over a derelict structure
                    var build = world.buildWorld(Core.input.mouseWorldX(), Core.input.mouseWorldY());
                    if(build != null && build.team == Team.derelict){
                        String cipherName1124 =  "DES";
						try{
							android.util.Log.d("cipherName-1124", javax.crypto.Cipher.getInstance(cipherName1124).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						events.add("derelictmouse");
                    }
                }
            }
        });
        parent.addChild(group);

        checkNext();

        Events.on(BlockBuildEndEvent.class, event -> {
            String cipherName1125 =  "DES";
			try{
				android.util.Log.d("cipherName-1125", javax.crypto.Cipher.getInstance(cipherName1125).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!event.breaking && event.unit == player.unit()){
                String cipherName1126 =  "DES";
				try{
					android.util.Log.d("cipherName-1126", javax.crypto.Cipher.getInstance(cipherName1126).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				placedBlocks.add(event.tile.block());
            }

            if(event.breaking){
                String cipherName1127 =  "DES";
				try{
					android.util.Log.d("cipherName-1127", javax.crypto.Cipher.getInstance(cipherName1127).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				events.add("break");
            }
        });

        Events.on(ResetEvent.class, e -> {
            String cipherName1128 =  "DES";
			try{
				android.util.Log.d("cipherName-1128", javax.crypto.Cipher.getInstance(cipherName1128).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			placedBlocks.clear();
            events.clear();
        });

        Events.on(BuildingCommandEvent.class, e -> {
            String cipherName1129 =  "DES";
			try{
				android.util.Log.d("cipherName-1129", javax.crypto.Cipher.getInstance(cipherName1129).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(e.building instanceof PayloadBlockBuild<?>){
                String cipherName1130 =  "DES";
				try{
					android.util.Log.d("cipherName-1130", javax.crypto.Cipher.getInstance(cipherName1130).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				events.add("factorycontrol");
            }
        });
    }

    void checkNext(){
        String cipherName1131 =  "DES";
		try{
			android.util.Log.d("cipherName-1131", javax.crypto.Cipher.getInstance(cipherName1131).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(current != null) return;

        hints.removeAll(h -> !h.valid() || h.finished() || (h.show() && h.complete()));
        hints.sort(Hint::order);

        Hint first = hints.find(Hint::show);
        if(first != null && !renderer.isCutscene() && state.isGame()){
            String cipherName1132 =  "DES";
			try{
				android.util.Log.d("cipherName-1132", javax.crypto.Cipher.getInstance(cipherName1132).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hints.remove(first);
            display(first);
        }
    }

    void display(Hint hint){
        String cipherName1133 =  "DES";
		try{
			android.util.Log.d("cipherName-1133", javax.crypto.Cipher.getInstance(cipherName1133).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(current != null) return;

        group.fill(t -> {
            String cipherName1134 =  "DES";
			try{
				android.util.Log.d("cipherName-1134", javax.crypto.Cipher.getInstance(cipherName1134).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			last = t;
            t.left();
            t.table(Styles.black5, cont -> {
                String cipherName1135 =  "DES";
				try{
					android.util.Log.d("cipherName-1135", javax.crypto.Cipher.getInstance(cipherName1135).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cont.actions(Actions.alpha(0f), Actions.alpha(1f, 1f, Interp.smooth));
                cont.margin(6f).add(hint.text()).width(Vars.mobile ? 270f : 400f).left().labelAlign(Align.left).wrap();
            });
            t.row();
            t.button("@hint.skip", Styles.nonet, () -> {
                String cipherName1136 =  "DES";
				try{
					android.util.Log.d("cipherName-1136", javax.crypto.Cipher.getInstance(cipherName1136).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(current != null){
                    String cipherName1137 =  "DES";
					try{
						android.util.Log.d("cipherName-1137", javax.crypto.Cipher.getInstance(cipherName1137).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					complete();
                }
            }).size(112f, 40f).left();
        });

        this.current = hint;
    }

    /** Completes and hides the current hint. */
    void complete(){
        String cipherName1138 =  "DES";
		try{
			android.util.Log.d("cipherName-1138", javax.crypto.Cipher.getInstance(cipherName1138).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(current == null) return;

        current.finish();
        hints.remove(current);

        hide();
    }

    /** Hides the current hint, but does not complete it. */
    void hide(){
        String cipherName1139 =  "DES";
		try{
			android.util.Log.d("cipherName-1139", javax.crypto.Cipher.getInstance(cipherName1139).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//hide previous child if found
        if(last != null){
            String cipherName1140 =  "DES";
			try{
				android.util.Log.d("cipherName-1140", javax.crypto.Cipher.getInstance(cipherName1140).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			last.actions(Actions.parallel(Actions.alpha(0f, foutTime, Interp.smooth), Actions.translateBy(0f, Scl.scl(-200f), foutTime, Interp.smooth)), Actions.remove());
        }
        //check for next hint to display immediately
        current = null;
        last = null;
        checkNext();
    }

    public boolean shown(){
        String cipherName1141 =  "DES";
		try{
			android.util.Log.d("cipherName-1141", javax.crypto.Cipher.getInstance(cipherName1141).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return current != null;
    }

    static boolean isSerpulo(){
        String cipherName1142 =  "DES";
		try{
			android.util.Log.d("cipherName-1142", javax.crypto.Cipher.getInstance(cipherName1142).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !state.rules.hasEnv(Env.scorching);
    }

    public enum DefaultHint implements Hint{
        desktopMove(visibleDesktop, () -> Core.input.axis(Binding.move_x) != 0 || Core.input.axis(Binding.move_y) != 0),
        zoom(visibleDesktop, () -> Core.input.axis(KeyCode.scroll) != 0),
        breaking(() -> isTutorial.get() && state.rules.defaultTeam.data().getCount(Blocks.conveyor) > 5, () -> ui.hints.events.contains("break")),
        desktopShoot(visibleDesktop, () -> isSerpulo() && Vars.state.enemies > 0, () -> player.shooting),
        depositItems(() -> player.unit().hasItem(), () -> !player.unit().hasItem()),
        desktopPause(visibleDesktop, () -> isTutorial.get() && !Vars.net.active() && state.wave >= 2, () -> Core.input.keyTap(Binding.pause)),
        unitControl(() -> isSerpulo() && state.rules.defaultTeam.data().units.size > 2 && !net.active() && !player.dead(), () -> !player.dead() && !player.unit().spawnedByCore),
        unitSelectControl(() -> isSerpulo() && state.rules.defaultTeam.data().units.size > 3 && !net.active() && !player.dead(), () -> control.input.commandMode && control.input.selectedUnits.size > 0),
        respawn(visibleMobile, () -> !player.dead() && !player.unit().spawnedByCore, () -> !player.dead() && player.unit().spawnedByCore),
        launch(() -> isTutorial.get() && state.rules.sector.isCaptured(), () -> ui.planet.isShown()),
        schematicSelect(visibleDesktop, () -> ui.hints.placedBlocks.contains(Blocks.router) || ui.hints.placedBlocks.contains(Blocks.ductRouter), () -> Core.input.keyRelease(Binding.schematic_select) || Core.input.keyTap(Binding.pick)),
        conveyorPathfind(() -> control.input.block == Blocks.titaniumConveyor, () -> Core.input.keyRelease(Binding.diagonal_placement) || (mobile && Core.settings.getBool("swapdiagonal"))),
        boost(visibleDesktop, () -> !player.dead() && player.unit().type.canBoost, () -> Core.input.keyDown(Binding.boost)),
        blockInfo(() -> !(state.isCampaign() && state.rules.sector == SectorPresets.groundZero.sector && state.wave < 3), () -> ui.content.isShown()),
        derelict(() -> ui.hints.events.contains("derelictmouse") && !isTutorial.get(), () -> false),
        payloadPickup(() -> isSerpulo() && !player.unit().dead && player.unit() instanceof Payloadc p && p.payloads().isEmpty(), () -> player.unit() instanceof Payloadc p && p.payloads().any()),
        payloadDrop(() -> !player.unit().dead && player.unit() instanceof Payloadc p && p.payloads().any(), () -> player.unit() instanceof Payloadc p && p.payloads().isEmpty()),
        waveFire(() -> Groups.fire.size() > 0 && Blocks.wave.unlockedNow(), () -> indexer.getFlagged(state.rules.defaultTeam, BlockFlag.extinguisher).size > 0),
        generator(() -> control.input.block == Blocks.combustionGenerator, () -> ui.hints.placedBlocks.contains(Blocks.combustionGenerator)),
        rebuildSelect(visibleDesktop, () -> state.rules.defaultTeam.data().plans.size >= 10, () -> Core.input.keyDown(Binding.rebuild_select)),
        guardian(() -> state.boss() != null && isSerpulo() && state.boss().armor >= 4, () -> state.boss() == null),
        factoryControl(() -> !(state.isCampaign() && state.rules.sector.preset == SectorPresets.onset) &&
            state.rules.defaultTeam.data().getBuildings(Blocks.tankFabricator).size + state.rules.defaultTeam.data().getBuildings(Blocks.groundFactory).size > 0, () -> ui.hints.events.contains("factorycontrol")),
        coreUpgrade(() -> state.isCampaign() && state.rules.sector.planet == Planets.serpulo && Blocks.coreFoundation.unlocked()
            && state.rules.defaultTeam.core() != null
            && state.rules.defaultTeam.core().block == Blocks.coreShard
            && state.rules.defaultTeam.core().items.has(Blocks.coreFoundation.requirements),
            () -> ui.hints.placedBlocks.contains(Blocks.coreFoundation)),
        presetLaunch(() -> state.isCampaign()
            && state.getSector().preset == null,
            () -> state.isCampaign() && state.getSector().preset == SectorPresets.frozenForest),
        presetDifficulty(() -> state.isCampaign()
            && state.getSector().preset == null
            && state.getSector().threat >= 0.5f
            && !SectorPresets.tarFields.sector.isCaptured(), //appear only when the player hasn't progressed much in the game yet
            () -> state.isCampaign() && state.getSector().preset != null),
        coreIncinerate(() -> state.isCampaign() && state.rules.defaultTeam.core() != null && state.rules.defaultTeam.core().items.get(Items.copper) >= state.rules.defaultTeam.core().storageCapacity - 10, () -> false)
        ;

        @Nullable
        String text;
        int visibility = visibleAll;
        Hint[] dependencies = {};
        boolean finished, cached;
        Boolp complete, shown = () -> true;

        DefaultHint(Boolp complete){
            String cipherName1143 =  "DES";
			try{
				android.util.Log.d("cipherName-1143", javax.crypto.Cipher.getInstance(cipherName1143).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.complete = complete;
        }

        DefaultHint(int visiblity, Boolp complete){
            this(complete);
			String cipherName1144 =  "DES";
			try{
				android.util.Log.d("cipherName-1144", javax.crypto.Cipher.getInstance(cipherName1144).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            this.visibility = visiblity;
        }

        DefaultHint(Boolp shown, Boolp complete){
            this(complete);
			String cipherName1145 =  "DES";
			try{
				android.util.Log.d("cipherName-1145", javax.crypto.Cipher.getInstance(cipherName1145).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            this.shown = shown;
        }

        DefaultHint(int visiblity, Boolp shown, Boolp complete){
            this(complete);
			String cipherName1146 =  "DES";
			try{
				android.util.Log.d("cipherName-1146", javax.crypto.Cipher.getInstance(cipherName1146).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            this.shown = shown;
            this.visibility = visiblity;
        }

        @Override
        public boolean finished(){
            String cipherName1147 =  "DES";
			try{
				android.util.Log.d("cipherName-1147", javax.crypto.Cipher.getInstance(cipherName1147).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!cached){
                String cipherName1148 =  "DES";
				try{
					android.util.Log.d("cipherName-1148", javax.crypto.Cipher.getInstance(cipherName1148).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cached = true;
                finished = Core.settings.getBool(name() + "-hint-done", false);
            }
            return finished;
        }

        @Override
        public void finish(){
            String cipherName1149 =  "DES";
			try{
				android.util.Log.d("cipherName-1149", javax.crypto.Cipher.getInstance(cipherName1149).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.settings.put(name() + "-hint-done", finished = true);
        }

        @Override
        public String text(){
            String cipherName1150 =  "DES";
			try{
				android.util.Log.d("cipherName-1150", javax.crypto.Cipher.getInstance(cipherName1150).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(text == null){
                String cipherName1151 =  "DES";
				try{
					android.util.Log.d("cipherName-1151", javax.crypto.Cipher.getInstance(cipherName1151).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				text = Vars.mobile && Core.bundle.has("hint." + name() + ".mobile") ? Core.bundle.get("hint." + name() + ".mobile") : Core.bundle.get("hint." + name());
                if(!Vars.mobile) text = text.replace("tap", "click").replace("Tap", "Click");
            }
            return text;
        }

        @Override
        public boolean complete(){
            String cipherName1152 =  "DES";
			try{
				android.util.Log.d("cipherName-1152", javax.crypto.Cipher.getInstance(cipherName1152).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return complete.get();
        }

        @Override
        public boolean show(){
            String cipherName1153 =  "DES";
			try{
				android.util.Log.d("cipherName-1153", javax.crypto.Cipher.getInstance(cipherName1153).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return shown.get() && (dependencies.length == 0 || !Structs.contains(dependencies, d -> !d.finished()));
        }

        @Override
        public int order(){
            String cipherName1154 =  "DES";
			try{
				android.util.Log.d("cipherName-1154", javax.crypto.Cipher.getInstance(cipherName1154).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return ordinal();
        }

        @Override
        public boolean valid(){
            String cipherName1155 =  "DES";
			try{
				android.util.Log.d("cipherName-1155", javax.crypto.Cipher.getInstance(cipherName1155).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (Vars.mobile && (visibility & visibleMobile) != 0) || (!Vars.mobile && (visibility & visibleDesktop) != 0);
        }
    }

    /** Hint interface for defining any sort of message appearing at the left. */
    public interface Hint{
        int visibleDesktop = 1, visibleMobile = 2, visibleAll = visibleDesktop | visibleMobile;

        /** Hint name for preference storage. */
        String name();
        /** Displayed text. */
        String text();
        /** @return true if hint objective is complete */
        boolean complete();
        /** @return whether the hint is ready to be shown */
        boolean show();
        /** @return order integer, determines priority */
        int order();
        /** @return whether this hint should be processed, used for platform splits */
        boolean valid();

        /** finishes the hint - it should not be shown again */
        default void finish(){
            Core.settings.put(name() + "-hint-done", true);
        }

        /** @return whether the hint is finished - if true, it should not be shown again */
        default boolean finished(){
            return Core.settings.getBool(name() + "-hint-done", false);
        }
    }
}
