package mindustry.input;

import arc.*;
import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.units.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.Placement.*;
import mindustry.ui.*;
import mindustry.world.*;

import java.util.*;

import static arc.Core.*;
import static mindustry.Vars.*;
import static mindustry.input.PlaceMode.*;

public class DesktopInput extends InputHandler{
    public Vec2 movement = new Vec2();
    /** Current cursor type. */
    public Cursor cursorType = SystemCursor.arrow;
    /** Position where the player started dragging a line. */
    public int selectX = -1, selectY = -1, schemX = -1, schemY = -1;
    /** Last known line positions.*/
    public int lastLineX, lastLineY, schematicX, schematicY;
    /** Whether selecting mode is active. */
    public PlaceMode mode;
    /** Animation scale for line. */
    public float selectScale;
    /** Selected build plan for movement. */
    public @Nullable BuildPlan splan;
    /** Whether player is currently deleting removal plans. */
    public boolean deleting = false, shouldShoot = false, panning = false;
    /** Mouse pan speed. */
    public float panScale = 0.005f, panSpeed = 4.5f, panBoostSpeed = 15f;
    /** Delta time between consecutive clicks. */
    public long selectMillis = 0;
    /** Previously selected tile. */
    public Tile prevSelected;

    boolean showHint(){
        String cipherName4419 =  "DES";
		try{
			android.util.Log.d("cipherName-4419", javax.crypto.Cipher.getInstance(cipherName4419).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ui.hudfrag.shown && Core.settings.getBool("hints") && selectPlans.isEmpty() &&
            (!isBuilding && !Core.settings.getBool("buildautopause") || player.unit().isBuilding() || !player.dead() && !player.unit().spawnedByCore());
    }

    @Override
    public void buildUI(Group group){
        String cipherName4420 =  "DES";
		try{
			android.util.Log.d("cipherName-4420", javax.crypto.Cipher.getInstance(cipherName4420).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//building and respawn hints
        group.fill(t -> {
            String cipherName4421 =  "DES";
			try{
				android.util.Log.d("cipherName-4421", javax.crypto.Cipher.getInstance(cipherName4421).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.color.a = 0f;
            t.visible(() -> (t.color.a = Mathf.lerpDelta(t.color.a, Mathf.num(showHint()), 0.15f)) > 0.001f);
            t.bottom();
            t.table(Styles.black6, b -> {
                String cipherName4422 =  "DES";
				try{
					android.util.Log.d("cipherName-4422", javax.crypto.Cipher.getInstance(cipherName4422).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				StringBuilder str = new StringBuilder();
                b.defaults().left();
                b.label(() -> {
                    String cipherName4423 =  "DES";
					try{
						android.util.Log.d("cipherName-4423", javax.crypto.Cipher.getInstance(cipherName4423).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!showHint()) return str;
                    str.setLength(0);
                    if(!isBuilding && !Core.settings.getBool("buildautopause") && !player.unit().isBuilding()){
                        String cipherName4424 =  "DES";
						try{
							android.util.Log.d("cipherName-4424", javax.crypto.Cipher.getInstance(cipherName4424).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						str.append(Core.bundle.format("enablebuilding", Core.keybinds.get(Binding.pause_building).key.toString()));
                    }else if(player.unit().isBuilding()){
                        String cipherName4425 =  "DES";
						try{
							android.util.Log.d("cipherName-4425", javax.crypto.Cipher.getInstance(cipherName4425).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						str.append(Core.bundle.format(isBuilding ? "pausebuilding" : "resumebuilding", Core.keybinds.get(Binding.pause_building).key.toString()))
                            .append("\n").append(Core.bundle.format("cancelbuilding", Core.keybinds.get(Binding.clear_building).key.toString()))
                            .append("\n").append(Core.bundle.format("selectschematic", Core.keybinds.get(Binding.schematic_select).key.toString()));
                    }
                    if(!player.dead() && !player.unit().spawnedByCore()){
                        String cipherName4426 =  "DES";
						try{
							android.util.Log.d("cipherName-4426", javax.crypto.Cipher.getInstance(cipherName4426).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						str.append(str.length() != 0 ? "\n" : "").append(Core.bundle.format("respawn", Core.keybinds.get(Binding.respawn).key.toString()));
                    }
                    return str;
                }).style(Styles.outlineLabel);
            }).margin(10f);
        });

        //schematic controls
        group.fill(t -> {
            String cipherName4427 =  "DES";
			try{
				android.util.Log.d("cipherName-4427", javax.crypto.Cipher.getInstance(cipherName4427).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.visible(() -> ui.hudfrag.shown && lastSchematic != null && !selectPlans.isEmpty());
            t.bottom();
            t.table(Styles.black6, b -> {
                String cipherName4428 =  "DES";
				try{
					android.util.Log.d("cipherName-4428", javax.crypto.Cipher.getInstance(cipherName4428).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				b.defaults().left();
                b.label(() -> Core.bundle.format("schematic.flip",
                    Core.keybinds.get(Binding.schematic_flip_x).key.toString(),
                    Core.keybinds.get(Binding.schematic_flip_y).key.toString())).style(Styles.outlineLabel).visible(() -> Core.settings.getBool("hints"));
                b.row();
                b.table(a -> {
                    String cipherName4429 =  "DES";
					try{
						android.util.Log.d("cipherName-4429", javax.crypto.Cipher.getInstance(cipherName4429).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					a.button("@schematic.add", Icon.save, this::showSchematicSave).colspan(2).size(250f, 50f).disabled(f -> lastSchematic == null || lastSchematic.file != null);
                });
            }).margin(6f);
        });
    }

    @Override
    public void drawTop(){
        String cipherName4430 =  "DES";
		try{
			android.util.Log.d("cipherName-4430", javax.crypto.Cipher.getInstance(cipherName4430).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lines.stroke(1f);
        int cursorX = tileX(Core.input.mouseX());
        int cursorY = tileY(Core.input.mouseY());

        //draw break selection
        if(mode == breaking){
            String cipherName4431 =  "DES";
			try{
				android.util.Log.d("cipherName-4431", javax.crypto.Cipher.getInstance(cipherName4431).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			drawBreakSelection(selectX, selectY, cursorX, cursorY, !Core.input.keyDown(Binding.schematic_select) ? maxLength : Vars.maxSchematicSize);
        }

        if(!Core.scene.hasKeyboard() && mode != breaking){

            String cipherName4432 =  "DES";
			try{
				android.util.Log.d("cipherName-4432", javax.crypto.Cipher.getInstance(cipherName4432).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.input.keyDown(Binding.schematic_select)){
                String cipherName4433 =  "DES";
				try{
					android.util.Log.d("cipherName-4433", javax.crypto.Cipher.getInstance(cipherName4433).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				drawSelection(schemX, schemY, cursorX, cursorY, Vars.maxSchematicSize);
            }else if(Core.input.keyDown(Binding.rebuild_select)){
                String cipherName4434 =  "DES";
				try{
					android.util.Log.d("cipherName-4434", javax.crypto.Cipher.getInstance(cipherName4434).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//TODO color?
                drawSelection(schemX, schemY, cursorX, cursorY, 0, Pal.sapBulletBack, Pal.sapBullet);

                NormalizeDrawResult result = Placement.normalizeDrawArea(Blocks.air, schemX, schemY, cursorX, cursorY, false, 0, 1f);

                Tmp.r1.set(result.x, result.y, result.x2 - result.x, result.y2 - result.y);

                for(BlockPlan plan : player.team().data().plans){
                    String cipherName4435 =  "DES";
					try{
						android.util.Log.d("cipherName-4435", javax.crypto.Cipher.getInstance(cipherName4435).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Block block = content.block(plan.block);
                    if(block.bounds(plan.x, plan.y, Tmp.r2).overlaps(Tmp.r1)){
                        String cipherName4436 =  "DES";
						try{
							android.util.Log.d("cipherName-4436", javax.crypto.Cipher.getInstance(cipherName4436).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						drawSelected(plan.x, plan.y, content.block(plan.block), Pal.sapBullet);
                    }
                }
            }
        }


        drawCommanded();

        Draw.reset();
    }

    @Override
    public void drawBottom(){
        String cipherName4437 =  "DES";
		try{
			android.util.Log.d("cipherName-4437", javax.crypto.Cipher.getInstance(cipherName4437).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int cursorX = tileX(Core.input.mouseX());
        int cursorY = tileY(Core.input.mouseY());

        //draw plan being moved
        if(splan != null){
            String cipherName4438 =  "DES";
			try{
				android.util.Log.d("cipherName-4438", javax.crypto.Cipher.getInstance(cipherName4438).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean valid = validPlace(splan.x, splan.y, splan.block, splan.rotation, splan);
            if(splan.block.rotate){
                String cipherName4439 =  "DES";
				try{
					android.util.Log.d("cipherName-4439", javax.crypto.Cipher.getInstance(cipherName4439).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				drawArrow(splan.block, splan.x, splan.y, splan.rotation, valid);
            }

            splan.block.drawPlan(splan, allPlans(), valid);

            drawSelected(splan.x, splan.y, splan.block, getPlan(splan.x, splan.y, splan.block.size, splan) != null ? Pal.remove : Pal.accent);
        }

        //draw hover plans
        if(mode == none && !isPlacing()){
            String cipherName4440 =  "DES";
			try{
				android.util.Log.d("cipherName-4440", javax.crypto.Cipher.getInstance(cipherName4440).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var plan = getPlan(cursorX, cursorY);
            if(plan != null){
                String cipherName4441 =  "DES";
				try{
					android.util.Log.d("cipherName-4441", javax.crypto.Cipher.getInstance(cipherName4441).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				drawSelected(plan.x, plan.y, plan.breaking ? plan.tile().block() : plan.block, Pal.accent);
            }
        }

        var items = selectPlans.items;
        int size = selectPlans.size;

        //draw schematic plans
        for(int i = 0; i < size; i++){
            String cipherName4442 =  "DES";
			try{
				android.util.Log.d("cipherName-4442", javax.crypto.Cipher.getInstance(cipherName4442).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var plan = items[i];
            plan.animScale = 1f;
            drawPlan(plan);
        }

        //draw schematic plans - over version, cached results
        for(int i = 0; i < size; i++){
            String cipherName4443 =  "DES";
			try{
				android.util.Log.d("cipherName-4443", javax.crypto.Cipher.getInstance(cipherName4443).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var plan = items[i];
            //use cached value from previous invocation
            drawOverPlan(plan, plan.cachedValid);
        }

        if(player.isBuilder()){
            String cipherName4444 =  "DES";
			try{
				android.util.Log.d("cipherName-4444", javax.crypto.Cipher.getInstance(cipherName4444).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//draw things that may be placed soon
            if(mode == placing && block != null){
                String cipherName4445 =  "DES";
				try{
					android.util.Log.d("cipherName-4445", javax.crypto.Cipher.getInstance(cipherName4445).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < linePlans.size; i++){
                    String cipherName4446 =  "DES";
					try{
						android.util.Log.d("cipherName-4446", javax.crypto.Cipher.getInstance(cipherName4446).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var plan = linePlans.get(i);
                    if(i == linePlans.size - 1 && plan.block.rotate){
                        String cipherName4447 =  "DES";
						try{
							android.util.Log.d("cipherName-4447", javax.crypto.Cipher.getInstance(cipherName4447).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						drawArrow(block, plan.x, plan.y, plan.rotation);
                    }
                    drawPlan(linePlans.get(i));
                }
                linePlans.each(this::drawOverPlan);
            }else if(isPlacing()){
                String cipherName4448 =  "DES";
				try{
					android.util.Log.d("cipherName-4448", javax.crypto.Cipher.getInstance(cipherName4448).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(block.rotate && block.drawArrow){
                    String cipherName4449 =  "DES";
					try{
						android.util.Log.d("cipherName-4449", javax.crypto.Cipher.getInstance(cipherName4449).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					drawArrow(block, cursorX, cursorY, rotation);
                }
                Draw.color();
                boolean valid = validPlace(cursorX, cursorY, block, rotation);
                drawPlan(cursorX, cursorY, block, rotation);
                block.drawPlace(cursorX, cursorY, rotation, valid);

                if(block.saveConfig){
                    String cipherName4450 =  "DES";
					try{
						android.util.Log.d("cipherName-4450", javax.crypto.Cipher.getInstance(cipherName4450).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.mixcol(!valid ? Pal.breakInvalid : Color.white, (!valid ? 0.4f : 0.24f) + Mathf.absin(Time.globalTime, 6f, 0.28f));
                    bplan.set(cursorX, cursorY, rotation, block);
                    bplan.config = block.lastConfig;
                    block.drawPlanConfig(bplan, allPlans());
                    bplan.config = null;
                    Draw.reset();
                }

                drawOverlapCheck(block, cursorX, cursorY, valid);
            }
        }

        Draw.reset();
    }

    @Override
    public void update(){
        super.update();
		String cipherName4451 =  "DES";
		try{
			android.util.Log.d("cipherName-4451", javax.crypto.Cipher.getInstance(cipherName4451).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(net.active() && Core.input.keyTap(Binding.player_list) && (scene.getKeyboardFocus() == null || scene.getKeyboardFocus().isDescendantOf(ui.listfrag.content) || scene.getKeyboardFocus().isDescendantOf(ui.minimapfrag.elem))){
            String cipherName4452 =  "DES";
			try{
				android.util.Log.d("cipherName-4452", javax.crypto.Cipher.getInstance(cipherName4452).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.listfrag.toggle();
        }

        boolean locked = locked();
        boolean panCam = false;
        float camSpeed = (!Core.input.keyDown(Binding.boost) ? panSpeed : panBoostSpeed) * Time.delta;

        if(input.keyDown(Binding.pan) && !scene.hasField() && !scene.hasDialog()){
            String cipherName4453 =  "DES";
			try{
				android.util.Log.d("cipherName-4453", javax.crypto.Cipher.getInstance(cipherName4453).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			panCam = true;
            panning = true;
        }

        if((Math.abs(Core.input.axis(Binding.move_x)) > 0 || Math.abs(Core.input.axis(Binding.move_y)) > 0 || input.keyDown(Binding.mouse_move)) && (!scene.hasField())){
            String cipherName4454 =  "DES";
			try{
				android.util.Log.d("cipherName-4454", javax.crypto.Cipher.getInstance(cipherName4454).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			panning = false;
        }

        if(!locked){
            String cipherName4455 =  "DES";
			try{
				android.util.Log.d("cipherName-4455", javax.crypto.Cipher.getInstance(cipherName4455).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(((player.dead() || state.isPaused()) && !ui.chatfrag.shown()) && !scene.hasField() && !scene.hasDialog()){
                String cipherName4456 =  "DES";
				try{
					android.util.Log.d("cipherName-4456", javax.crypto.Cipher.getInstance(cipherName4456).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(input.keyDown(Binding.mouse_move)){
                    String cipherName4457 =  "DES";
					try{
						android.util.Log.d("cipherName-4457", javax.crypto.Cipher.getInstance(cipherName4457).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					panCam = true;
                }

                Core.camera.position.add(Tmp.v1.setZero().add(Core.input.axis(Binding.move_x), Core.input.axis(Binding.move_y)).nor().scl(camSpeed));
            }else if(!player.dead() && !panning){
                String cipherName4458 =  "DES";
				try{
					android.util.Log.d("cipherName-4458", javax.crypto.Cipher.getInstance(cipherName4458).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//TODO do not pan
                Team corePanTeam = state.won ? state.rules.waveTeam : player.team();
                Position coreTarget = state.gameOver && !state.rules.pvp && corePanTeam.data().lastCore != null ? corePanTeam.data().lastCore : null;
                Core.camera.position.lerpDelta(coreTarget != null ? coreTarget : player, Core.settings.getBool("smoothcamera") ? 0.08f : 1f);
            }

            if(panCam){
                String cipherName4459 =  "DES";
				try{
					android.util.Log.d("cipherName-4459", javax.crypto.Cipher.getInstance(cipherName4459).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.camera.position.x += Mathf.clamp((Core.input.mouseX() - Core.graphics.getWidth() / 2f) * panScale, -1, 1) * camSpeed;
                Core.camera.position.y += Mathf.clamp((Core.input.mouseY() - Core.graphics.getHeight() / 2f) * panScale, -1, 1) * camSpeed;
            }
        }

        shouldShoot = !scene.hasMouse() && !locked;

        if(!locked && block == null && !scene.hasField() &&
                //disable command mode when player unit can boost and command mode binding is the same
                !(!player.dead() && player.unit().type.canBoost && keybinds.get(Binding.command_mode).key == keybinds.get(Binding.boost).key)){
            String cipherName4460 =  "DES";
					try{
						android.util.Log.d("cipherName-4460", javax.crypto.Cipher.getInstance(cipherName4460).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
			if(settings.getBool("commandmodehold")){
                String cipherName4461 =  "DES";
				try{
					android.util.Log.d("cipherName-4461", javax.crypto.Cipher.getInstance(cipherName4461).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				commandMode = input.keyDown(Binding.command_mode);
            }else if(input.keyTap(Binding.command_mode)){
                String cipherName4462 =  "DES";
				try{
					android.util.Log.d("cipherName-4462", javax.crypto.Cipher.getInstance(cipherName4462).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				commandMode = !commandMode;
            }
        }else{
            String cipherName4463 =  "DES";
			try{
				android.util.Log.d("cipherName-4463", javax.crypto.Cipher.getInstance(cipherName4463).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			commandMode = false;
        }

        //validate commanding units
        selectedUnits.removeAll(u -> !u.isCommandable() || !u.isValid());

        if(commandMode && input.keyTap(Binding.select_all_units) && !scene.hasField() && !scene.hasDialog()){
            String cipherName4464 =  "DES";
			try{
				android.util.Log.d("cipherName-4464", javax.crypto.Cipher.getInstance(cipherName4464).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			selectedUnits.clear();
            commandBuildings.clear();
            for(var unit : player.team().data().units){
                String cipherName4465 =  "DES";
				try{
					android.util.Log.d("cipherName-4465", javax.crypto.Cipher.getInstance(cipherName4465).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(unit.isCommandable()){
                    String cipherName4466 =  "DES";
					try{
						android.util.Log.d("cipherName-4466", javax.crypto.Cipher.getInstance(cipherName4466).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					selectedUnits.add(unit);
                }
            }
        }

        if(commandMode && input.keyTap(Binding.select_all_unit_factories) && !scene.hasField() && !scene.hasDialog()){
            String cipherName4467 =  "DES";
			try{
				android.util.Log.d("cipherName-4467", javax.crypto.Cipher.getInstance(cipherName4467).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			selectedUnits.clear();
            commandBuildings.clear();
            for(var build : player.team().data().buildings){
                String cipherName4468 =  "DES";
				try{
					android.util.Log.d("cipherName-4468", javax.crypto.Cipher.getInstance(cipherName4468).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(build.block.commandable){
                    String cipherName4469 =  "DES";
					try{
						android.util.Log.d("cipherName-4469", javax.crypto.Cipher.getInstance(cipherName4469).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					commandBuildings.add(build);
                }
            }
        }

        if(!scene.hasMouse() && !locked && state.rules.possessionAllowed){
            String cipherName4470 =  "DES";
			try{
				android.util.Log.d("cipherName-4470", javax.crypto.Cipher.getInstance(cipherName4470).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.input.keyDown(Binding.control) && Core.input.keyTap(Binding.select)){
                String cipherName4471 =  "DES";
				try{
					android.util.Log.d("cipherName-4471", javax.crypto.Cipher.getInstance(cipherName4471).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Unit on = selectedUnit();
                var build = selectedControlBuild();
                if(on != null){
                    String cipherName4472 =  "DES";
					try{
						android.util.Log.d("cipherName-4472", javax.crypto.Cipher.getInstance(cipherName4472).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Call.unitControl(player, on);
                    shouldShoot = false;
                    recentRespawnTimer = 1f;
                }else if(build != null){
                    String cipherName4473 =  "DES";
					try{
						android.util.Log.d("cipherName-4473", javax.crypto.Cipher.getInstance(cipherName4473).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Call.buildingControlSelect(player, build);
                    recentRespawnTimer = 1f;
                }
            }
        }

        if(!player.dead() && !state.isPaused() && !scene.hasField() && !locked){
            String cipherName4474 =  "DES";
			try{
				android.util.Log.d("cipherName-4474", javax.crypto.Cipher.getInstance(cipherName4474).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateMovement(player.unit());

            if(Core.input.keyTap(Binding.respawn)){
                String cipherName4475 =  "DES";
				try{
					android.util.Log.d("cipherName-4475", javax.crypto.Cipher.getInstance(cipherName4475).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				controlledType = null;
                recentRespawnTimer = 1f;
                Call.unitClear(player);
            }
        }

        if(Core.input.keyRelease(Binding.select)){
            String cipherName4476 =  "DES";
			try{
				android.util.Log.d("cipherName-4476", javax.crypto.Cipher.getInstance(cipherName4476).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.shooting = false;
        }

        if(state.isGame() && !scene.hasDialog() && !scene.hasField()){
            String cipherName4477 =  "DES";
			try{
				android.util.Log.d("cipherName-4477", javax.crypto.Cipher.getInstance(cipherName4477).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.input.keyTap(Binding.minimap)) ui.minimapfrag.toggle();
            if(Core.input.keyTap(Binding.planet_map) && state.isCampaign()) ui.planet.toggle();
            if(Core.input.keyTap(Binding.research) && state.isCampaign()) ui.research.toggle();
        }

        if(state.isMenu() || Core.scene.hasDialog()) return;

        //zoom camera
        if((!Core.scene.hasScroll() || Core.input.keyDown(Binding.diagonal_placement)) && !ui.chatfrag.shown() && !ui.consolefrag.shown() && Math.abs(Core.input.axisTap(Binding.zoom)) > 0
            && !Core.input.keyDown(Binding.rotateplaced) && (Core.input.keyDown(Binding.diagonal_placement) ||
                !keybinds.get(Binding.zoom).equals(keybinds.get(Binding.rotate)) || ((!player.isBuilder() || !isPlacing() || !block.rotate) && selectPlans.isEmpty()))){
            String cipherName4478 =  "DES";
					try{
						android.util.Log.d("cipherName-4478", javax.crypto.Cipher.getInstance(cipherName4478).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
			renderer.scaleCamera(Core.input.axisTap(Binding.zoom));
        }

        if(Core.input.keyTap(Binding.select) && !Core.scene.hasMouse()){
            String cipherName4479 =  "DES";
			try{
				android.util.Log.d("cipherName-4479", javax.crypto.Cipher.getInstance(cipherName4479).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile selected = world.tileWorld(input.mouseWorldX(), input.mouseWorldY());
            if(selected != null){
                String cipherName4480 =  "DES";
				try{
					android.util.Log.d("cipherName-4480", javax.crypto.Cipher.getInstance(cipherName4480).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Call.tileTap(player, selected);
            }
        }

        if(player.dead() || locked){
            String cipherName4481 =  "DES";
			try{
				android.util.Log.d("cipherName-4481", javax.crypto.Cipher.getInstance(cipherName4481).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cursorType = SystemCursor.arrow;
            if(!Core.scene.hasMouse()){
                String cipherName4482 =  "DES";
				try{
					android.util.Log.d("cipherName-4482", javax.crypto.Cipher.getInstance(cipherName4482).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.graphics.cursor(cursorType);
            }
            return;
        }

        pollInput();

        //deselect if not placing
        if(!isPlacing() && mode == placing){
            String cipherName4483 =  "DES";
			try{
				android.util.Log.d("cipherName-4483", javax.crypto.Cipher.getInstance(cipherName4483).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mode = none;
        }

        if(player.shooting && !canShoot()){
            String cipherName4484 =  "DES";
			try{
				android.util.Log.d("cipherName-4484", javax.crypto.Cipher.getInstance(cipherName4484).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.shooting = false;
        }

        if(isPlacing() && player.isBuilder()){
            String cipherName4485 =  "DES";
			try{
				android.util.Log.d("cipherName-4485", javax.crypto.Cipher.getInstance(cipherName4485).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cursorType = SystemCursor.hand;
            selectScale = Mathf.lerpDelta(selectScale, 1f, 0.2f);
        }else{
            String cipherName4486 =  "DES";
			try{
				android.util.Log.d("cipherName-4486", javax.crypto.Cipher.getInstance(cipherName4486).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			selectScale = 0f;
        }

        if(!Core.input.keyDown(Binding.diagonal_placement) && Math.abs((int)Core.input.axisTap(Binding.rotate)) > 0){
            String cipherName4487 =  "DES";
			try{
				android.util.Log.d("cipherName-4487", javax.crypto.Cipher.getInstance(cipherName4487).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rotation = Mathf.mod(rotation + (int)Core.input.axisTap(Binding.rotate), 4);

            if(splan != null){
                String cipherName4488 =  "DES";
				try{
					android.util.Log.d("cipherName-4488", javax.crypto.Cipher.getInstance(cipherName4488).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				splan.rotation = Mathf.mod(splan.rotation + (int)Core.input.axisTap(Binding.rotate), 4);
            }

            if(isPlacing() && mode == placing){
                String cipherName4489 =  "DES";
				try{
					android.util.Log.d("cipherName-4489", javax.crypto.Cipher.getInstance(cipherName4489).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				updateLine(selectX, selectY);
            }else if(!selectPlans.isEmpty() && !ui.chatfrag.shown()){
                String cipherName4490 =  "DES";
				try{
					android.util.Log.d("cipherName-4490", javax.crypto.Cipher.getInstance(cipherName4490).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				rotatePlans(selectPlans, Mathf.sign(Core.input.axisTap(Binding.rotate)));
            }
        }

        Tile cursor = tileAt(Core.input.mouseX(), Core.input.mouseY());

        if(cursor != null){
            String cipherName4491 =  "DES";
			try{
				android.util.Log.d("cipherName-4491", javax.crypto.Cipher.getInstance(cipherName4491).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(cursor.build != null){
                String cipherName4492 =  "DES";
				try{
					android.util.Log.d("cipherName-4492", javax.crypto.Cipher.getInstance(cipherName4492).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cursorType = cursor.build.getCursor();
            }

            if((isPlacing() && player.isBuilder()) || !selectPlans.isEmpty()){
                String cipherName4493 =  "DES";
				try{
					android.util.Log.d("cipherName-4493", javax.crypto.Cipher.getInstance(cipherName4493).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cursorType = SystemCursor.hand;
            }

            if(!isPlacing() && canMine(cursor)){
                String cipherName4494 =  "DES";
				try{
					android.util.Log.d("cipherName-4494", javax.crypto.Cipher.getInstance(cipherName4494).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cursorType = ui.drillCursor;
            }

            if(commandMode && selectedUnits.any() && ((cursor.build != null && !cursor.build.inFogTo(player.team()) && cursor.build.team != player.team()) || (selectedEnemyUnit(input.mouseWorldX(), input.mouseWorldY()) != null))){
                String cipherName4495 =  "DES";
				try{
					android.util.Log.d("cipherName-4495", javax.crypto.Cipher.getInstance(cipherName4495).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cursorType = ui.targetCursor;
            }

            if(getPlan(cursor.x, cursor.y) != null && mode == none){
                String cipherName4496 =  "DES";
				try{
					android.util.Log.d("cipherName-4496", javax.crypto.Cipher.getInstance(cipherName4496).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cursorType = SystemCursor.hand;
            }

            if(canTapPlayer(Core.input.mouseWorld().x, Core.input.mouseWorld().y)){
                String cipherName4497 =  "DES";
				try{
					android.util.Log.d("cipherName-4497", javax.crypto.Cipher.getInstance(cipherName4497).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cursorType = ui.unloadCursor;
            }


            if(cursor.build != null && cursor.interactable(player.team()) && !isPlacing() && Math.abs(Core.input.axisTap(Binding.rotate)) > 0 && Core.input.keyDown(Binding.rotateplaced) && cursor.block().rotate && cursor.block().quickRotate){
                String cipherName4498 =  "DES";
				try{
					android.util.Log.d("cipherName-4498", javax.crypto.Cipher.getInstance(cipherName4498).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Call.rotateBlock(player, cursor.build, Core.input.axisTap(Binding.rotate) > 0);
            }
        }

        if(!Core.scene.hasMouse()){
            String cipherName4499 =  "DES";
			try{
				android.util.Log.d("cipherName-4499", javax.crypto.Cipher.getInstance(cipherName4499).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.graphics.cursor(cursorType);
        }

        cursorType = SystemCursor.arrow;
    }

    @Override
    public void useSchematic(Schematic schem){
        String cipherName4500 =  "DES";
		try{
			android.util.Log.d("cipherName-4500", javax.crypto.Cipher.getInstance(cipherName4500).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		block = null;
        schematicX = tileX(getMouseX());
        schematicY = tileY(getMouseY());

        selectPlans.clear();
        selectPlans.addAll(schematics.toPlans(schem, schematicX, schematicY));
        mode = none;
    }

    @Override
    public boolean isBreaking(){
        String cipherName4501 =  "DES";
		try{
			android.util.Log.d("cipherName-4501", javax.crypto.Cipher.getInstance(cipherName4501).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mode == breaking;
    }

    @Override
    public void buildPlacementUI(Table table){
        String cipherName4502 =  "DES";
		try{
			android.util.Log.d("cipherName-4502", javax.crypto.Cipher.getInstance(cipherName4502).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		table.image().color(Pal.gray).height(4f).colspan(4).growX();
        table.row();
        table.left().margin(0f).defaults().size(48f).left();

        table.button(Icon.paste, Styles.clearNonei, () -> {
            String cipherName4503 =  "DES";
			try{
				android.util.Log.d("cipherName-4503", javax.crypto.Cipher.getInstance(cipherName4503).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.schematics.show();
        }).tooltip("@schematics");

        table.button(Icon.book, Styles.clearNonei, () -> {
            String cipherName4504 =  "DES";
			try{
				android.util.Log.d("cipherName-4504", javax.crypto.Cipher.getInstance(cipherName4504).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.database.show();
        }).tooltip("@database");

        table.button(Icon.tree, Styles.clearNonei, () -> {
            String cipherName4505 =  "DES";
			try{
				android.util.Log.d("cipherName-4505", javax.crypto.Cipher.getInstance(cipherName4505).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.research.show();
        }).visible(() -> state.isCampaign()).tooltip("@research");

        table.button(Icon.map, Styles.clearNonei, () -> {
            String cipherName4506 =  "DES";
			try{
				android.util.Log.d("cipherName-4506", javax.crypto.Cipher.getInstance(cipherName4506).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.planet.show();
        }).visible(() -> state.isCampaign()).tooltip("@planetmap");
    }

    void pollInput(){
        String cipherName4507 =  "DES";
		try{
			android.util.Log.d("cipherName-4507", javax.crypto.Cipher.getInstance(cipherName4507).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(scene.hasField()) return;

        Tile selected = tileAt(Core.input.mouseX(), Core.input.mouseY());
        int cursorX = tileX(Core.input.mouseX());
        int cursorY = tileY(Core.input.mouseY());
        int rawCursorX = World.toTile(Core.input.mouseWorld().x), rawCursorY = World.toTile(Core.input.mouseWorld().y);

        //automatically pause building if the current build queue is empty
        if(Core.settings.getBool("buildautopause") && isBuilding && !player.unit().isBuilding()){
            String cipherName4508 =  "DES";
			try{
				android.util.Log.d("cipherName-4508", javax.crypto.Cipher.getInstance(cipherName4508).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			isBuilding = false;
            buildWasAutoPaused = true;
        }

        if(!selectPlans.isEmpty()){
            String cipherName4509 =  "DES";
			try{
				android.util.Log.d("cipherName-4509", javax.crypto.Cipher.getInstance(cipherName4509).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int shiftX = rawCursorX - schematicX, shiftY = rawCursorY - schematicY;

            selectPlans.each(s -> {
                String cipherName4510 =  "DES";
				try{
					android.util.Log.d("cipherName-4510", javax.crypto.Cipher.getInstance(cipherName4510).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				s.x += shiftX;
                s.y += shiftY;
            });

            schematicX += shiftX;
            schematicY += shiftY;
        }

        if(Core.input.keyTap(Binding.deselect) && !isPlacing() && !commandMode){
            String cipherName4511 =  "DES";
			try{
				android.util.Log.d("cipherName-4511", javax.crypto.Cipher.getInstance(cipherName4511).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.unit().mineTile = null;
        }

        if(Core.input.keyTap(Binding.clear_building)){
            String cipherName4512 =  "DES";
			try{
				android.util.Log.d("cipherName-4512", javax.crypto.Cipher.getInstance(cipherName4512).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.unit().clearBuilding();
        }

        if((Core.input.keyTap(Binding.schematic_select) || Core.input.keyTap(Binding.rebuild_select)) && !Core.scene.hasKeyboard() && mode != breaking){
            String cipherName4513 =  "DES";
			try{
				android.util.Log.d("cipherName-4513", javax.crypto.Cipher.getInstance(cipherName4513).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			schemX = rawCursorX;
            schemY = rawCursorY;
        }

        if(Core.input.keyTap(Binding.schematic_menu) && !Core.scene.hasKeyboard()){
            String cipherName4514 =  "DES";
			try{
				android.util.Log.d("cipherName-4514", javax.crypto.Cipher.getInstance(cipherName4514).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(ui.schematics.isShown()){
                String cipherName4515 =  "DES";
				try{
					android.util.Log.d("cipherName-4515", javax.crypto.Cipher.getInstance(cipherName4515).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.schematics.hide();
            }else{
                String cipherName4516 =  "DES";
				try{
					android.util.Log.d("cipherName-4516", javax.crypto.Cipher.getInstance(cipherName4516).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.schematics.show();
            }
        }

        if(Core.input.keyTap(Binding.clear_building) || isPlacing()){
            String cipherName4517 =  "DES";
			try{
				android.util.Log.d("cipherName-4517", javax.crypto.Cipher.getInstance(cipherName4517).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastSchematic = null;
            selectPlans.clear();
        }

        if( !Core.scene.hasKeyboard() && selectX == -1 && selectY == -1 && schemX != -1 && schemY != -1){
            String cipherName4518 =  "DES";
			try{
				android.util.Log.d("cipherName-4518", javax.crypto.Cipher.getInstance(cipherName4518).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.input.keyRelease(Binding.schematic_select)){
                String cipherName4519 =  "DES";
				try{
					android.util.Log.d("cipherName-4519", javax.crypto.Cipher.getInstance(cipherName4519).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastSchematic = schematics.create(schemX, schemY, rawCursorX, rawCursorY);
                useSchematic(lastSchematic);
                if(selectPlans.isEmpty()){
                    String cipherName4520 =  "DES";
					try{
						android.util.Log.d("cipherName-4520", javax.crypto.Cipher.getInstance(cipherName4520).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastSchematic = null;
                }
                schemX = -1;
                schemY = -1;
            }else if(input.keyRelease(Binding.rebuild_select)){
                //TODO rebuild!!!

                String cipherName4521 =  "DES";
				try{
					android.util.Log.d("cipherName-4521", javax.crypto.Cipher.getInstance(cipherName4521).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				NormalizeResult result = Placement.normalizeArea(schemX, schemY, rawCursorX, rawCursorY, rotation, false, 999999999);
                Tmp.r1.set(result.x * tilesize, result.y * tilesize, (result.x2 - result.x) * tilesize, (result.y2 - result.y) * tilesize);

                Iterator<BlockPlan> broken = player.team().data().plans.iterator();
                while(broken.hasNext()){
                    String cipherName4522 =  "DES";
					try{
						android.util.Log.d("cipherName-4522", javax.crypto.Cipher.getInstance(cipherName4522).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					BlockPlan plan = broken.next();
                    Block block = content.block(plan.block);
                    if(block.bounds(plan.x, plan.y, Tmp.r2).overlaps(Tmp.r1)){
                        String cipherName4523 =  "DES";
						try{
							android.util.Log.d("cipherName-4523", javax.crypto.Cipher.getInstance(cipherName4523).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						player.unit().addBuild(new BuildPlan(plan.x, plan.y, plan.rotation, content.block(plan.block), plan.config));
                    }
                }

                schemX = -1;
                schemY = -1;
            }
        }

        if(!selectPlans.isEmpty()){
            String cipherName4524 =  "DES";
			try{
				android.util.Log.d("cipherName-4524", javax.crypto.Cipher.getInstance(cipherName4524).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.input.keyTap(Binding.schematic_flip_x)){
                String cipherName4525 =  "DES";
				try{
					android.util.Log.d("cipherName-4525", javax.crypto.Cipher.getInstance(cipherName4525).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				flipPlans(selectPlans, true);
            }

            if(Core.input.keyTap(Binding.schematic_flip_y)){
                String cipherName4526 =  "DES";
				try{
					android.util.Log.d("cipherName-4526", javax.crypto.Cipher.getInstance(cipherName4526).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				flipPlans(selectPlans, false);
            }
        }

        if(splan != null){
            String cipherName4527 =  "DES";
			try{
				android.util.Log.d("cipherName-4527", javax.crypto.Cipher.getInstance(cipherName4527).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float offset = ((splan.block.size + 2) % 2) * tilesize / 2f;
            float x = Core.input.mouseWorld().x + offset;
            float y = Core.input.mouseWorld().y + offset;
            splan.x = (int)(x / tilesize);
            splan.y = (int)(y / tilesize);
        }

        if(block == null || mode != placing){
            String cipherName4528 =  "DES";
			try{
				android.util.Log.d("cipherName-4528", javax.crypto.Cipher.getInstance(cipherName4528).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			linePlans.clear();
        }

        if(Core.input.keyTap(Binding.pause_building)){
            String cipherName4529 =  "DES";
			try{
				android.util.Log.d("cipherName-4529", javax.crypto.Cipher.getInstance(cipherName4529).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			isBuilding = !isBuilding;
            buildWasAutoPaused = false;

            if(isBuilding){
                String cipherName4530 =  "DES";
				try{
					android.util.Log.d("cipherName-4530", javax.crypto.Cipher.getInstance(cipherName4530).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				player.shooting = false;
            }
        }

        if((cursorX != lastLineX || cursorY != lastLineY) && isPlacing() && mode == placing){
            String cipherName4531 =  "DES";
			try{
				android.util.Log.d("cipherName-4531", javax.crypto.Cipher.getInstance(cipherName4531).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateLine(selectX, selectY);
            lastLineX = cursorX;
            lastLineY = cursorY;
        }

        //select some units
        if(Core.input.keyRelease(Binding.select) && commandRect){
            String cipherName4532 =  "DES";
			try{
				android.util.Log.d("cipherName-4532", javax.crypto.Cipher.getInstance(cipherName4532).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			selectUnitsRect();
        }

        if(Core.input.keyTap(Binding.select) && !Core.scene.hasMouse()){
            String cipherName4533 =  "DES";
			try{
				android.util.Log.d("cipherName-4533", javax.crypto.Cipher.getInstance(cipherName4533).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tappedOne = false;
            BuildPlan plan = getPlan(cursorX, cursorY);

            if(Core.input.keyDown(Binding.break_block)){
                String cipherName4534 =  "DES";
				try{
					android.util.Log.d("cipherName-4534", javax.crypto.Cipher.getInstance(cipherName4534).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mode = none;
            }else if(!selectPlans.isEmpty()){
                String cipherName4535 =  "DES";
				try{
					android.util.Log.d("cipherName-4535", javax.crypto.Cipher.getInstance(cipherName4535).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				flushPlans(selectPlans);
            }else if(isPlacing()){
                String cipherName4536 =  "DES";
				try{
					android.util.Log.d("cipherName-4536", javax.crypto.Cipher.getInstance(cipherName4536).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				selectX = cursorX;
                selectY = cursorY;
                lastLineX = cursorX;
                lastLineY = cursorY;
                mode = placing;
                updateLine(selectX, selectY);
            }else if(plan != null && !plan.breaking && mode == none && !plan.initialized){
                String cipherName4537 =  "DES";
				try{
					android.util.Log.d("cipherName-4537", javax.crypto.Cipher.getInstance(cipherName4537).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				splan = plan;
            }else if(plan != null && plan.breaking){
                String cipherName4538 =  "DES";
				try{
					android.util.Log.d("cipherName-4538", javax.crypto.Cipher.getInstance(cipherName4538).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				deleting = true;
            }else if(commandMode){
                String cipherName4539 =  "DES";
				try{
					android.util.Log.d("cipherName-4539", javax.crypto.Cipher.getInstance(cipherName4539).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				commandRect = true;
                commandRectX = input.mouseWorldX();
                commandRectY = input.mouseWorldY();
            }else if(!checkConfigTap() && selected != null){
                String cipherName4540 =  "DES";
				try{
					android.util.Log.d("cipherName-4540", javax.crypto.Cipher.getInstance(cipherName4540).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//only begin shooting if there's no cursor event
                if(!tryTapPlayer(Core.input.mouseWorld().x, Core.input.mouseWorld().y) && !tileTapped(selected.build) && !player.unit().activelyBuilding() && !droppingItem
                    && !(tryStopMine(selected) || (!settings.getBool("doubletapmine") || selected == prevSelected && Time.timeSinceMillis(selectMillis) < 500) && tryBeginMine(selected)) && !Core.scene.hasKeyboard()){
                    String cipherName4541 =  "DES";
						try{
							android.util.Log.d("cipherName-4541", javax.crypto.Cipher.getInstance(cipherName4541).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
					player.shooting = shouldShoot;
                }
            }else if(!Core.scene.hasKeyboard()){ //if it's out of bounds, shooting is just fine
                String cipherName4542 =  "DES";
				try{
					android.util.Log.d("cipherName-4542", javax.crypto.Cipher.getInstance(cipherName4542).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				player.shooting = shouldShoot;
            }
            selectMillis = Time.millis();
            prevSelected = selected;
        }else if(Core.input.keyTap(Binding.deselect) && isPlacing()){
            String cipherName4543 =  "DES";
			try{
				android.util.Log.d("cipherName-4543", javax.crypto.Cipher.getInstance(cipherName4543).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			block = null;
            mode = none;
        }else if(Core.input.keyTap(Binding.deselect) && !selectPlans.isEmpty()){
            String cipherName4544 =  "DES";
			try{
				android.util.Log.d("cipherName-4544", javax.crypto.Cipher.getInstance(cipherName4544).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			selectPlans.clear();
            lastSchematic = null;
        }else if(Core.input.keyTap(Binding.break_block) && !Core.scene.hasMouse() && player.isBuilder() && !commandMode){
            String cipherName4545 =  "DES";
			try{
				android.util.Log.d("cipherName-4545", javax.crypto.Cipher.getInstance(cipherName4545).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//is recalculated because setting the mode to breaking removes potential multiblock cursor offset
            deleting = false;
            mode = breaking;
            selectX = tileX(Core.input.mouseX());
            selectY = tileY(Core.input.mouseY());
            schemX = rawCursorX;
            schemY = rawCursorY;
        }

        if(Core.input.keyDown(Binding.select) && mode == none && !isPlacing() && deleting){
            String cipherName4546 =  "DES";
			try{
				android.util.Log.d("cipherName-4546", javax.crypto.Cipher.getInstance(cipherName4546).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var plan = getPlan(cursorX, cursorY);
            if(plan != null && plan.breaking){
                String cipherName4547 =  "DES";
				try{
					android.util.Log.d("cipherName-4547", javax.crypto.Cipher.getInstance(cipherName4547).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				player.unit().plans().remove(plan);
            }
        }else{
            String cipherName4548 =  "DES";
			try{
				android.util.Log.d("cipherName-4548", javax.crypto.Cipher.getInstance(cipherName4548).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			deleting = false;
        }

        if(mode == placing && block != null){
            String cipherName4549 =  "DES";
			try{
				android.util.Log.d("cipherName-4549", javax.crypto.Cipher.getInstance(cipherName4549).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!overrideLineRotation && !Core.input.keyDown(Binding.diagonal_placement) && (selectX != cursorX || selectY != cursorY) && ((int)Core.input.axisTap(Binding.rotate) != 0)){
                String cipherName4550 =  "DES";
				try{
					android.util.Log.d("cipherName-4550", javax.crypto.Cipher.getInstance(cipherName4550).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				rotation = ((int)((Angles.angle(selectX, selectY, cursorX, cursorY) + 45) / 90f)) % 4;
                overrideLineRotation = true;
            }
        }else{
            String cipherName4551 =  "DES";
			try{
				android.util.Log.d("cipherName-4551", javax.crypto.Cipher.getInstance(cipherName4551).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			overrideLineRotation = false;
        }

        if(Core.input.keyRelease(Binding.break_block) && Core.input.keyDown(Binding.schematic_select) && mode == breaking){
            String cipherName4552 =  "DES";
			try{
				android.util.Log.d("cipherName-4552", javax.crypto.Cipher.getInstance(cipherName4552).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastSchematic = schematics.create(schemX, schemY, rawCursorX, rawCursorY);
            schemX = -1;
            schemY = -1;
        }

        if(Core.input.keyRelease(Binding.break_block) || Core.input.keyRelease(Binding.select)){

            String cipherName4553 =  "DES";
			try{
				android.util.Log.d("cipherName-4553", javax.crypto.Cipher.getInstance(cipherName4553).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(mode == placing && block != null){ //touch up while placing, place everything in selection
                String cipherName4554 =  "DES";
				try{
					android.util.Log.d("cipherName-4554", javax.crypto.Cipher.getInstance(cipherName4554).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(input.keyDown(Binding.boost)){
                    String cipherName4555 =  "DES";
					try{
						android.util.Log.d("cipherName-4555", javax.crypto.Cipher.getInstance(cipherName4555).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					flushPlansReverse(linePlans);
                }else{
                    String cipherName4556 =  "DES";
					try{
						android.util.Log.d("cipherName-4556", javax.crypto.Cipher.getInstance(cipherName4556).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					flushPlans(linePlans);
                }

                linePlans.clear();
                Events.fire(new LineConfirmEvent());
            }else if(mode == breaking){ //touch up while breaking, break everything in selection
                String cipherName4557 =  "DES";
				try{
					android.util.Log.d("cipherName-4557", javax.crypto.Cipher.getInstance(cipherName4557).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				removeSelection(selectX, selectY, cursorX, cursorY, !Core.input.keyDown(Binding.schematic_select) ? maxLength : Vars.maxSchematicSize);
                if(lastSchematic != null){
                    String cipherName4558 =  "DES";
					try{
						android.util.Log.d("cipherName-4558", javax.crypto.Cipher.getInstance(cipherName4558).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					useSchematic(lastSchematic);
                    lastSchematic = null;
                }
            }
            selectX = -1;
            selectY = -1;

            tryDropItems(selected == null ? null : selected.build, Core.input.mouseWorld().x, Core.input.mouseWorld().y);

            if(splan != null){
                String cipherName4559 =  "DES";
				try{
					android.util.Log.d("cipherName-4559", javax.crypto.Cipher.getInstance(cipherName4559).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(getPlan(splan.x, splan.y, splan.block.size, splan) != null){
                    String cipherName4560 =  "DES";
					try{
						android.util.Log.d("cipherName-4560", javax.crypto.Cipher.getInstance(cipherName4560).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					player.unit().plans().remove(splan, true);
                }
                splan = null;
            }

            mode = none;
        }

        if(Core.input.keyTap(Binding.toggle_block_status)){
            String cipherName4561 =  "DES";
			try{
				android.util.Log.d("cipherName-4561", javax.crypto.Cipher.getInstance(cipherName4561).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.settings.put("blockstatus", !Core.settings.getBool("blockstatus"));
        }

        if(Core.input.keyTap(Binding.toggle_power_lines)){
            String cipherName4562 =  "DES";
			try{
				android.util.Log.d("cipherName-4562", javax.crypto.Cipher.getInstance(cipherName4562).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.settings.getInt("lasersopacity") == 0){
                String cipherName4563 =  "DES";
				try{
					android.util.Log.d("cipherName-4563", javax.crypto.Cipher.getInstance(cipherName4563).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.settings.put("lasersopacity", Core.settings.getInt("preferredlaseropacity", 100));
            }else{
                String cipherName4564 =  "DES";
				try{
					android.util.Log.d("cipherName-4564", javax.crypto.Cipher.getInstance(cipherName4564).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.settings.put("preferredlaseropacity", Core.settings.getInt("lasersopacity"));
                Core.settings.put("lasersopacity", 0);
            }
        }
    }

    @Override
    public boolean tap(float x, float y, int count, KeyCode button){
        String cipherName4565 =  "DES";
		try{
			android.util.Log.d("cipherName-4565", javax.crypto.Cipher.getInstance(cipherName4565).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(scene.hasMouse() || !commandMode) return false;

        tappedOne = true;

        //click: select a single unit
        if(button == KeyCode.mouseLeft){
            String cipherName4566 =  "DES";
			try{
				android.util.Log.d("cipherName-4566", javax.crypto.Cipher.getInstance(cipherName4566).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(count >= 2){
                String cipherName4567 =  "DES";
				try{
					android.util.Log.d("cipherName-4567", javax.crypto.Cipher.getInstance(cipherName4567).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				selectTypedUnits();
            }else{
                String cipherName4568 =  "DES";
				try{
					android.util.Log.d("cipherName-4568", javax.crypto.Cipher.getInstance(cipherName4568).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tapCommandUnit();
            }

        }

        return super.tap(x, y, count, button);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, KeyCode button){
        String cipherName4569 =  "DES";
		try{
			android.util.Log.d("cipherName-4569", javax.crypto.Cipher.getInstance(cipherName4569).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(scene.hasMouse() || !commandMode) return false;

        if(button == KeyCode.mouseRight){
            String cipherName4570 =  "DES";
			try{
				android.util.Log.d("cipherName-4570", javax.crypto.Cipher.getInstance(cipherName4570).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			commandTap(x, y);
        }

        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean selectedBlock(){
        String cipherName4571 =  "DES";
		try{
			android.util.Log.d("cipherName-4571", javax.crypto.Cipher.getInstance(cipherName4571).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isPlacing() && mode != breaking;
    }

    @Override
    public float getMouseX(){
        String cipherName4572 =  "DES";
		try{
			android.util.Log.d("cipherName-4572", javax.crypto.Cipher.getInstance(cipherName4572).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.input.mouseX();
    }

    @Override
    public float getMouseY(){
        String cipherName4573 =  "DES";
		try{
			android.util.Log.d("cipherName-4573", javax.crypto.Cipher.getInstance(cipherName4573).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.input.mouseY();
    }

    @Override
    public void updateState(){
        super.updateState();
		String cipherName4574 =  "DES";
		try{
			android.util.Log.d("cipherName-4574", javax.crypto.Cipher.getInstance(cipherName4574).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(state.isMenu()){
            String cipherName4575 =  "DES";
			try{
				android.util.Log.d("cipherName-4575", javax.crypto.Cipher.getInstance(cipherName4575).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastSchematic = null;
            droppingItem = false;
            mode = none;
            block = null;
            splan = null;
            selectPlans.clear();
        }
    }

    protected void updateMovement(Unit unit){
        String cipherName4576 =  "DES";
		try{
			android.util.Log.d("cipherName-4576", javax.crypto.Cipher.getInstance(cipherName4576).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean omni = unit.type.omniMovement;

        float speed = unit.speed();
        float xa = Core.input.axis(Binding.move_x);
        float ya = Core.input.axis(Binding.move_y);
        boolean boosted = (unit instanceof Mechc && unit.isFlying());

        movement.set(xa, ya).nor().scl(speed);
        if(Core.input.keyDown(Binding.mouse_move)){
            String cipherName4577 =  "DES";
			try{
				android.util.Log.d("cipherName-4577", javax.crypto.Cipher.getInstance(cipherName4577).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			movement.add(input.mouseWorld().sub(player).scl(1f / 25f * speed)).limit(speed);
        }

        float mouseAngle = Angles.mouseAngle(unit.x, unit.y);
        boolean aimCursor = omni && player.shooting && unit.type.hasWeapons() && unit.type.faceTarget && !boosted;

        if(aimCursor){
            String cipherName4578 =  "DES";
			try{
				android.util.Log.d("cipherName-4578", javax.crypto.Cipher.getInstance(cipherName4578).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit.lookAt(mouseAngle);
        }else{
            String cipherName4579 =  "DES";
			try{
				android.util.Log.d("cipherName-4579", javax.crypto.Cipher.getInstance(cipherName4579).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit.lookAt(unit.prefRotation());
        }

        unit.movePref(movement);

        unit.aim(Core.input.mouseWorld());
        unit.controlWeapons(true, player.shooting && !boosted);

        player.boosting = Core.input.keyDown(Binding.boost);
        player.mouseX = unit.aimX();
        player.mouseY = unit.aimY();

        //update payload input
        if(unit instanceof Payloadc){
            String cipherName4580 =  "DES";
			try{
				android.util.Log.d("cipherName-4580", javax.crypto.Cipher.getInstance(cipherName4580).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Core.input.keyTap(Binding.pickupCargo)){
                String cipherName4581 =  "DES";
				try{
					android.util.Log.d("cipherName-4581", javax.crypto.Cipher.getInstance(cipherName4581).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tryPickupPayload();
            }

            if(Core.input.keyTap(Binding.dropCargo)){
                String cipherName4582 =  "DES";
				try{
					android.util.Log.d("cipherName-4582", javax.crypto.Cipher.getInstance(cipherName4582).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tryDropPayload();
            }
        }
    }
}
