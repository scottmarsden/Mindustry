package mindustry.world.blocks.units;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.ai.types.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.units.UnitAssemblerModule.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class UnitAssembler extends PayloadBlock{
    public @Load("@-side1") TextureRegion sideRegion1;
    public @Load("@-side2") TextureRegion sideRegion2;

    public int areaSize = 11;
    public UnitType droneType = UnitTypes.assemblyDrone;
    public int dronesCreated = 4;
    public float droneConstructTime = 60f * 4f;

    public Seq<AssemblerUnitPlan> plans = new Seq<>(4);

    protected @Nullable ConsumePayloadDynamic consPayload;

    public UnitAssembler(String name){
        super(name);
		String cipherName7867 =  "DES";
		try{
			android.util.Log.d("cipherName-7867", javax.crypto.Cipher.getInstance(cipherName7867).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = solid = true;
        rotate = true;
        rotateDraw = false;
        acceptsPayload = true;
        flags = EnumSet.of(BlockFlag.unitAssembler);
        regionRotated1 = 1;
        sync = true;
        group = BlockGroup.units;
        commandable = true;
        quickRotate = false;
    }

    public Rect getRect(Rect rect, float x, float y, int rotation){
        String cipherName7868 =  "DES";
		try{
			android.util.Log.d("cipherName-7868", javax.crypto.Cipher.getInstance(cipherName7868).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rect.setCentered(x, y, areaSize * tilesize);
        float len = tilesize * (areaSize + size)/2f;

        rect.x += Geometry.d4x(rotation) * len;
        rect.y += Geometry.d4y(rotation) * len;

        return rect;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName7869 =  "DES";
		try{
			android.util.Log.d("cipherName-7869", javax.crypto.Cipher.getInstance(cipherName7869).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        x *= tilesize;
        y *= tilesize;
        x += offset;
        y += offset;

        Rect rect = getRect(Tmp.r1, x, y, rotation);

        Drawf.dashRect(valid ? Pal.accent : Pal.remove, rect);
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        String cipherName7870 =  "DES";
		try{
			android.util.Log.d("cipherName-7870", javax.crypto.Cipher.getInstance(cipherName7870).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//overlapping construction areas not allowed; grow by a tiny amount so edges can't overlap either.
        Rect rect = getRect(Tmp.r1, tile.worldx() + offset, tile.worldy() + offset, rotation).grow(0.1f);
        return !indexer.getFlagged(team, BlockFlag.unitAssembler).contains(b -> getRect(Tmp.r2, b.x, b.y, b.rotation).overlaps(rect));
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName7871 =  "DES";
		try{
			android.util.Log.d("cipherName-7871", javax.crypto.Cipher.getInstance(cipherName7871).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("progress", (UnitAssemblerBuild e) -> new Bar("bar.progress", Pal.ammo, () -> e.progress));

        addBar("units", (UnitAssemblerBuild e) ->
            new Bar(() ->
            Core.bundle.format("bar.unitcap",
                Fonts.getUnicodeStr(e.unit().name),
                e.team.data().countType(e.unit()),
                Units.getStringCap(e.team)
            ),
            () -> Pal.power,
            () -> (float)e.team.data().countType(e.unit()) / Units.getCap(e.team)
        ));
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName7872 =  "DES";
		try{
			android.util.Log.d("cipherName-7872", javax.crypto.Cipher.getInstance(cipherName7872).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.rect(region, plan.drawx(), plan.drawy());
        Draw.rect(plan.rotation >= 2 ? sideRegion2 : sideRegion1, plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.rect(topRegion, plan.drawx(), plan.drawy());
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName7873 =  "DES";
		try{
			android.util.Log.d("cipherName-7873", javax.crypto.Cipher.getInstance(cipherName7873).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{region, sideRegion1, topRegion};
    }

    @Override
    public void init(){
        updateClipRadius(areaSize * tilesize);
		String cipherName7874 =  "DES";
		try{
			android.util.Log.d("cipherName-7874", javax.crypto.Cipher.getInstance(cipherName7874).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        consume(consPayload = new ConsumePayloadDynamic((UnitAssemblerBuild build) -> build.plan().requirements));

        consumeBuilder.each(c -> c.multiplier = b -> state.rules.unitCost(b.team));

        super.init();
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName7875 =  "DES";
		try{
			android.util.Log.d("cipherName-7875", javax.crypto.Cipher.getInstance(cipherName7875).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.output, table -> {
            String cipherName7876 =  "DES";
			try{
				android.util.Log.d("cipherName-7876", javax.crypto.Cipher.getInstance(cipherName7876).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.row();

            int tier = 0;
            for(var plan : plans){
                String cipherName7877 =  "DES";
				try{
					android.util.Log.d("cipherName-7877", javax.crypto.Cipher.getInstance(cipherName7877).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int ttier = tier;
                table.table(Styles.grayPanel, t -> {

                    String cipherName7878 =  "DES";
					try{
						android.util.Log.d("cipherName-7878", javax.crypto.Cipher.getInstance(cipherName7878).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(plan.unit.isBanned()){
                        String cipherName7879 =  "DES";
						try{
							android.util.Log.d("cipherName-7879", javax.crypto.Cipher.getInstance(cipherName7879).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.image(Icon.cancel).color(Pal.remove).size(40).pad(10);
                        return;
                    }

                    if(plan.unit.unlockedNow()){
                        String cipherName7880 =  "DES";
						try{
							android.util.Log.d("cipherName-7880", javax.crypto.Cipher.getInstance(cipherName7880).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.image(plan.unit.uiIcon).scaling(Scaling.fit).size(40).pad(10f).left();
                        t.table(info -> {
                            String cipherName7881 =  "DES";
							try{
								android.util.Log.d("cipherName-7881", javax.crypto.Cipher.getInstance(cipherName7881).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							info.defaults().left();
                            info.add(plan.unit.localizedName);
                            info.row();
                            info.add(Strings.autoFixed(plan.time / 60f, 1) + " " + Core.bundle.get("unit.seconds")).color(Color.lightGray);
                            if(ttier > 0){
                                String cipherName7882 =  "DES";
								try{
									android.util.Log.d("cipherName-7882", javax.crypto.Cipher.getInstance(cipherName7882).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								info.row();
                                info.add(Stat.moduleTier.localized() + ": " + ttier).color(Color.lightGray);
                            }
                        }).left();

                        t.table(req -> {
                            String cipherName7883 =  "DES";
							try{
								android.util.Log.d("cipherName-7883", javax.crypto.Cipher.getInstance(cipherName7883).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							req.right();
                            for(int i = 0; i < plan.requirements.size; i++){
                                String cipherName7884 =  "DES";
								try{
									android.util.Log.d("cipherName-7884", javax.crypto.Cipher.getInstance(cipherName7884).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(i % 6 == 0){
                                    String cipherName7885 =  "DES";
									try{
										android.util.Log.d("cipherName-7885", javax.crypto.Cipher.getInstance(cipherName7885).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									req.row();
                                }

                                PayloadStack stack = plan.requirements.get(i);
                                req.add(new ItemImage(stack)).pad(5);
                            }
                        }).right().grow().pad(10f);
                    }else{
                        String cipherName7886 =  "DES";
						try{
							android.util.Log.d("cipherName-7886", javax.crypto.Cipher.getInstance(cipherName7886).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.image(Icon.lock).color(Pal.darkerGray).size(40).pad(10);
                    }
                }).growX().pad(5);
                table.row();
                tier++;
            }
        });
    }

    public static class AssemblerUnitPlan{
        public UnitType unit;
        public Seq<PayloadStack> requirements;
        public float time;

        public AssemblerUnitPlan(UnitType unit, float time, Seq<PayloadStack> requirements){
            String cipherName7887 =  "DES";
			try{
				android.util.Log.d("cipherName-7887", javax.crypto.Cipher.getInstance(cipherName7887).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.unit = unit;
            this.time = time;
            this.requirements = requirements;
        }

        AssemblerUnitPlan(){
			String cipherName7888 =  "DES";
			try{
				android.util.Log.d("cipherName-7888", javax.crypto.Cipher.getInstance(cipherName7888).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}
    }

    /** hbgwerjhbagjegwg */
    public static class YeetData{
        public Vec2 target;
        public UnlockableContent item;

        public YeetData(Vec2 target, UnlockableContent item){
            String cipherName7889 =  "DES";
			try{
				android.util.Log.d("cipherName-7889", javax.crypto.Cipher.getInstance(cipherName7889).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.target = target;
            this.item = item;
        }
    }

    @Remote(called = Loc.server)
    public static void assemblerUnitSpawned(Tile tile){
		String cipherName7890 =  "DES";
		try{
			android.util.Log.d("cipherName-7890", javax.crypto.Cipher.getInstance(cipherName7890).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(tile == null || !(tile.build instanceof UnitAssemblerBuild build)) return;
        build.spawned();
    }

    @Remote(called = Loc.server)
    public static void assemblerDroneSpawned(Tile tile, int id){
		String cipherName7891 =  "DES";
		try{
			android.util.Log.d("cipherName-7891", javax.crypto.Cipher.getInstance(cipherName7891).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(tile == null || !(tile.build instanceof UnitAssemblerBuild build)) return;
        build.droneSpawned(id);
    }

    public class UnitAssemblerBuild extends PayloadBlockBuild<Payload>{
        protected IntSeq readUnits = new IntSeq();
        //holds drone IDs that have been sent, but not synced yet - add to list as soon as possible
        protected IntSeq whenSyncedUnits = new IntSeq();

        public @Nullable Vec2 commandPos;
        public Seq<Unit> units = new Seq<>();
        public Seq<UnitAssemblerModuleBuild> modules = new Seq<>();
        public PayloadSeq blocks = new PayloadSeq();
        public float progress, warmup, droneWarmup, powerWarmup, sameTypeWarmup;
        public float invalidWarmup = 0f;
        public int currentTier = 0;
        public int lastTier = -2;
        public boolean wasOccupied = false;

        public float droneProgress, totalDroneProgress;

        public Vec2 getUnitSpawn(){
            String cipherName7892 =  "DES";
			try{
				android.util.Log.d("cipherName-7892", javax.crypto.Cipher.getInstance(cipherName7892).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float len = tilesize * (areaSize + size)/2f;
            float unitX = x + Geometry.d4x(rotation) * len, unitY = y + Geometry.d4y(rotation) * len;
            return Tmp.v4.set(unitX, unitY);
        }

        public boolean moduleFits(Block other, float ox, float oy, int rotation){
            String cipherName7893 =  "DES";
			try{
				android.util.Log.d("cipherName-7893", javax.crypto.Cipher.getInstance(cipherName7893).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float
            dx = ox + Geometry.d4x(rotation) * (other.size/2 + 1) * tilesize,
            dy = oy + Geometry.d4y(rotation) * (other.size/2 + 1) * tilesize;

            Vec2 spawn = getUnitSpawn();

            if(Tile.relativeTo(ox, oy, spawn.x, spawn.y) != rotation){
                String cipherName7894 =  "DES";
				try{
					android.util.Log.d("cipherName-7894", javax.crypto.Cipher.getInstance(cipherName7894).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }

            float dst = Math.max(Math.abs(dx - spawn.x), Math.abs(dy - spawn.y));
            return Mathf.equal(dst, tilesize * areaSize / 2f - tilesize/2f);
        }

        public void updateModules(UnitAssemblerModuleBuild build){
            String cipherName7895 =  "DES";
			try{
				android.util.Log.d("cipherName-7895", javax.crypto.Cipher.getInstance(cipherName7895).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			modules.addUnique(build);
            checkTier();
        }

        public void removeModule(UnitAssemblerModuleBuild build){
            String cipherName7896 =  "DES";
			try{
				android.util.Log.d("cipherName-7896", javax.crypto.Cipher.getInstance(cipherName7896).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			modules.remove(build);
            checkTier();
        }

        public void checkTier(){
            String cipherName7897 =  "DES";
			try{
				android.util.Log.d("cipherName-7897", javax.crypto.Cipher.getInstance(cipherName7897).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			modules.sort(b -> b.tier());
            int max = 0;
            for(int i = 0; i < modules.size; i++){
                String cipherName7898 =  "DES";
				try{
					android.util.Log.d("cipherName-7898", javax.crypto.Cipher.getInstance(cipherName7898).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var mod = modules.get(i);
                if(mod.tier() == max || mod.tier() == max + 1){
                    String cipherName7899 =  "DES";
					try{
						android.util.Log.d("cipherName-7899", javax.crypto.Cipher.getInstance(cipherName7899).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					max = mod.tier();
                }else{
                    String cipherName7900 =  "DES";
					try{
						android.util.Log.d("cipherName-7900", javax.crypto.Cipher.getInstance(cipherName7900).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//tier gap, TODO warning?
                    break;
                }
            }

            currentTier = max;
        }

        public UnitType unit(){
            String cipherName7901 =  "DES";
			try{
				android.util.Log.d("cipherName-7901", javax.crypto.Cipher.getInstance(cipherName7901).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return plan().unit;
        }

        public AssemblerUnitPlan plan(){
            String cipherName7902 =  "DES";
			try{
				android.util.Log.d("cipherName-7902", javax.crypto.Cipher.getInstance(cipherName7902).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//clamp plan pos
            return plans.get(Math.min(currentTier, plans.size - 1));
        }

        @Override
        public boolean shouldConsume(){
            String cipherName7903 =  "DES";
			try{
				android.util.Log.d("cipherName-7903", javax.crypto.Cipher.getInstance(cipherName7903).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//liquid is only consumed when building is being done
            return enabled && !wasOccupied && Units.canCreate(team, plan().unit) && consPayload.efficiency(this) > 0;
        }

        @Override
        public void drawSelect(){
            String cipherName7904 =  "DES";
			try{
				android.util.Log.d("cipherName-7904", javax.crypto.Cipher.getInstance(cipherName7904).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var module : modules){
                String cipherName7905 =  "DES";
				try{
					android.util.Log.d("cipherName-7905", javax.crypto.Cipher.getInstance(cipherName7905).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Drawf.selected(module, Pal.accent);
            }

            Drawf.dashRect(Tmp.c1.set(Pal.accent).lerp(Pal.remove, invalidWarmup), getRect(Tmp.r1, x, y, rotation));
        }

        @Override
        public void display(Table table){
            super.display(table);
			String cipherName7906 =  "DES";
			try{
				android.util.Log.d("cipherName-7906", javax.crypto.Cipher.getInstance(cipherName7906).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(team != player.team()) return;

            table.row();
            table.table(t -> {
                String cipherName7907 =  "DES";
				try{
					android.util.Log.d("cipherName-7907", javax.crypto.Cipher.getInstance(cipherName7907).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.left().defaults().left();

                Block prev = null;
                for(int i = 0; i < modules.size; i++){
                    String cipherName7908 =  "DES";
					try{
						android.util.Log.d("cipherName-7908", javax.crypto.Cipher.getInstance(cipherName7908).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var mod = modules.get(i);
                    if(prev == mod.block) continue;
                    //TODO crosses for missing reqs?
                    t.image(mod.block.uiIcon).size(iconMed).padRight(4);

                    prev = mod.block;
                }

                t.label(() -> "[accent] -> []" + unit().emoji() + " " + unit().localizedName);
            }).pad(4).padLeft(0f).fillX().left();
        }

        @Override
        public void updateTile(){
			String cipherName7909 =  "DES";
			try{
				android.util.Log.d("cipherName-7909", javax.crypto.Cipher.getInstance(cipherName7909).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(!readUnits.isEmpty()){
                units.clear();
                readUnits.each(i -> {
                    var unit = Groups.unit.getByID(i);
                    if(unit != null){
                        units.add(unit);
                    }
                });
                readUnits.clear();
            }

            if(lastTier != currentTier){
                if(lastTier >= 0f){
                    progress = 0f;
                }

                lastTier =
                    lastTier == -2 ? -1 : currentTier;
            }

            //read newly synced drones on client end
            if(units.size < dronesCreated && whenSyncedUnits.size > 0){
                whenSyncedUnits.each(id -> {
                    var unit = Groups.unit.getByID(id);
                    if(unit != null){
                        units.addUnique(unit);
                    }
                });
            }

            units.removeAll(u -> !u.isAdded() || u.dead || !(u.controller() instanceof AssemblerAI));

            //unsupported
            if(!allowUpdate()){
                progress = 0f;
                units.each(Unit::kill);
                units.clear();
            }

            float powerStatus = power == null ? 1f : power.status;
            powerWarmup = Mathf.lerpDelta(powerStatus, powerStatus > 0.0001f ? 1f : 0f, 0.1f);
            droneWarmup = Mathf.lerpDelta(droneWarmup, units.size < dronesCreated ? powerStatus : 0f, 0.1f);
            totalDroneProgress += droneWarmup * delta();

            if(units.size < dronesCreated && (droneProgress += delta() * state.rules.unitBuildSpeed(team) * powerStatus / droneConstructTime) >= 1f){
                if(!net.client()){
                    var unit = droneType.create(team);
                    if(unit instanceof BuildingTetherc bt){
                        bt.building(this);
                    }
                    unit.set(x, y);
                    unit.rotation = 90f;
                    unit.add();
                    units.add(unit);
                    Call.assemblerDroneSpawned(tile, unit.id);
                }
            }

            if(units.size >= dronesCreated){
                droneProgress = 0f;
            }

            Vec2 spawn = getUnitSpawn();

            if(moveInPayload() && !wasOccupied){
                yeetPayload(payload);
                payload = null;
            }

            //arrange units around perimeter
            for(int i = 0; i < units.size; i++){
                var unit = units.get(i);
                var ai = (AssemblerAI)unit.controller();

                ai.targetPos.trns(i * 90f + 45f, areaSize / 2f * Mathf.sqrt2 * tilesize).add(spawn);
                ai.targetAngle = i * 90f + 45f + 180f;
            }

            wasOccupied = checkSolid(spawn, false);
            boolean visualOccupied = checkSolid(spawn, true);
            float eff = (units.count(u -> ((AssemblerAI)u.controller()).inPosition()) / (float)dronesCreated);

            sameTypeWarmup = Mathf.lerpDelta(sameTypeWarmup, wasOccupied && !visualOccupied ? 0f : 1f, 0.1f);
            invalidWarmup = Mathf.lerpDelta(invalidWarmup, visualOccupied ? 1f : 0f, 0.1f);

            var plan = plan();

            //check if all requirements are met
            if(!wasOccupied && efficiency > 0 && Units.canCreate(team, plan.unit)){
                warmup = Mathf.lerpDelta(warmup, efficiency, 0.1f);

                if((progress += edelta() * state.rules.unitBuildSpeed(team) * eff / plan.time) >= 1f){
                    Call.assemblerUnitSpawned(tile);
                }
            }else{
                warmup = Mathf.lerpDelta(warmup, 0f, 0.1f);
            }
        }

        public void droneSpawned(int id){
            String cipherName7910 =  "DES";
			try{
				android.util.Log.d("cipherName-7910", javax.crypto.Cipher.getInstance(cipherName7910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fx.spawn.at(x, y);
            droneProgress = 0f;
            if(net.client()){
                String cipherName7911 =  "DES";
				try{
					android.util.Log.d("cipherName-7911", javax.crypto.Cipher.getInstance(cipherName7911).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				whenSyncedUnits.add(id);
            }
        }

        public void spawned(){
            String cipherName7912 =  "DES";
			try{
				android.util.Log.d("cipherName-7912", javax.crypto.Cipher.getInstance(cipherName7912).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var plan = plan();
            Vec2 spawn = getUnitSpawn();
            consume();

            if(!net.client()){
                String cipherName7913 =  "DES";
				try{
					android.util.Log.d("cipherName-7913", javax.crypto.Cipher.getInstance(cipherName7913).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var unit = plan.unit.create(team);
                if(unit != null && unit.isCommandable()){
                    String cipherName7914 =  "DES";
					try{
						android.util.Log.d("cipherName-7914", javax.crypto.Cipher.getInstance(cipherName7914).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unit.command().commandPosition(commandPos);
                }
                unit.set(spawn.x + Mathf.range(0.001f), spawn.y + Mathf.range(0.001f));
                unit.rotation = 90f;
                unit.add();
            }

            progress = 0f;
            Fx.unitAssemble.at(spawn.x, spawn.y, 0f, plan.unit);
            blocks.clear();
        }

        @Override
        public void draw(){
            String cipherName7915 =  "DES";
			try{
				android.util.Log.d("cipherName-7915", javax.crypto.Cipher.getInstance(cipherName7915).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(region, x, y);

            //draw input conveyors
            for(int i = 0; i < 4; i++){
                String cipherName7916 =  "DES";
				try{
					android.util.Log.d("cipherName-7916", javax.crypto.Cipher.getInstance(cipherName7916).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(blends(i) && i != rotation){
                    String cipherName7917 =  "DES";
					try{
						android.util.Log.d("cipherName-7917", javax.crypto.Cipher.getInstance(cipherName7917).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.rect(inRegion, x, y, (i * 90) - 180);
                }
            }

            Draw.rect(rotation >= 2 ? sideRegion2 : sideRegion1, x, y, rotdeg());

            Draw.z(Layer.blockOver);

            payRotation = rotdeg();
            drawPayload();

            Draw.z(Layer.blockOver + 0.1f);

            Draw.rect(topRegion, x, y);

            if(isPayload()) return;

            //draw drone construction
            if(droneWarmup > 0.001f){
                String cipherName7918 =  "DES";
				try{
					android.util.Log.d("cipherName-7918", javax.crypto.Cipher.getInstance(cipherName7918).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.draw(Layer.blockOver + 0.2f, () -> {
                    String cipherName7919 =  "DES";
					try{
						android.util.Log.d("cipherName-7919", javax.crypto.Cipher.getInstance(cipherName7919).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Drawf.construct(this, droneType.fullIcon, Pal.accent, 0f, droneProgress, droneWarmup, totalDroneProgress, 14f);
                });
            }

            Vec2 spawn = getUnitSpawn();
            float sx = spawn.x, sy = spawn.y;

            var plan = plan();

            //draw the unit construction as outline
            //TODO flashes when no gallium
            Draw.draw(Layer.blockBuilding, () -> {
                String cipherName7920 =  "DES";
				try{
					android.util.Log.d("cipherName-7920", javax.crypto.Cipher.getInstance(cipherName7920).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(Pal.accent, warmup);

                Shaders.blockbuild.region = plan.unit.fullIcon;
                Shaders.blockbuild.time = Time.time;
                //margin due to units not taking up whole region
                Shaders.blockbuild.progress = Mathf.clamp(progress + 0.05f);

                Draw.rect(plan.unit.fullIcon, sx, sy);
                Draw.flush();
                Draw.color();
            });

            Draw.reset();

            Draw.z(Layer.buildBeam);

            //draw unit silhouette
            Draw.mixcol(Tmp.c1.set(Pal.accent).lerp(Pal.remove, invalidWarmup), 1f);
            Draw.alpha(Math.min(powerWarmup, sameTypeWarmup));
            Draw.rect(plan.unit.fullIcon, spawn.x, spawn.y);

            //build beams do not draw when invalid
            Draw.alpha(Math.min(1f - invalidWarmup, warmup));

            //draw build beams
            for(var unit : units){
                String cipherName7921 =  "DES";
				try{
					android.util.Log.d("cipherName-7921", javax.crypto.Cipher.getInstance(cipherName7921).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!((AssemblerAI)unit.controller()).inPosition()) continue;

                float
                    px = unit.x + Angles.trnsx(unit.rotation, unit.type.buildBeamOffset),
                    py = unit.y + Angles.trnsy(unit.rotation, unit.type.buildBeamOffset);

                Drawf.buildBeam(px, py, spawn.x, spawn.y, plan.unit.hitSize/2f);
            }

            //fill square in middle
            Fill.square(spawn.x, spawn.y, plan.unit.hitSize/2f);

            Draw.reset();

            Draw.z(Layer.buildBeam);

            float fulls = areaSize * tilesize/2f;

            //draw full area
            Lines.stroke(2f, Pal.accent);
            Draw.alpha(powerWarmup);
            Drawf.dashRectBasic(spawn.x - fulls, spawn.y - fulls, fulls*2f, fulls*2f);

            Draw.reset();

            float outSize = plan.unit.hitSize + 9f;

            if(invalidWarmup > 0){
                String cipherName7922 =  "DES";
				try{
					android.util.Log.d("cipherName-7922", javax.crypto.Cipher.getInstance(cipherName7922).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//draw small square for area
                Lines.stroke(2f, Tmp.c3.set(Pal.accent).lerp(Pal.remove, invalidWarmup).a(invalidWarmup));
                Drawf.dashSquareBasic(spawn.x, spawn.y, outSize);
            }

            Draw.reset();
        }

        public boolean checkSolid(Vec2 v, boolean same){
            String cipherName7923 =  "DES";
			try{
				android.util.Log.d("cipherName-7923", javax.crypto.Cipher.getInstance(cipherName7923).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var output = unit();
            float hsize = output.hitSize * 1.4f;
            return ((!output.flying && collisions.overlapsTile(Tmp.r1.setCentered(v.x, v.y, output.hitSize), EntityCollisions::solid)) ||
                Units.anyEntities(v.x - hsize/2f, v.y - hsize/2f, hsize, hsize, u -> (!same || u.type != output) && !u.spawnedByCore &&
                    ((u.type.allowLegStep && output.allowLegStep) || (output.flying && u.isFlying()) || (!output.flying && u.isGrounded()))));
        }

        /** @return true if this block is ready to produce units, e.g. requirements met */
        public boolean ready(){
            String cipherName7924 =  "DES";
			try{
				android.util.Log.d("cipherName-7924", javax.crypto.Cipher.getInstance(cipherName7924).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return efficiency > 0 && !wasOccupied;
        }

        public void yeetPayload(Payload payload){
            String cipherName7925 =  "DES";
			try{
				android.util.Log.d("cipherName-7925", javax.crypto.Cipher.getInstance(cipherName7925).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var spawn = getUnitSpawn();
            blocks.add(payload.content(), 1);
            float rot = payload.angleTo(spawn);
            Fx.shootPayloadDriver.at(payload.x(), payload.y(), rot);
            Fx.payloadDeposit.at(payload.x(), payload.y(), rot, new YeetData(spawn.cpy(), payload.content()));
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName7926 =  "DES";
			try{
				android.util.Log.d("cipherName-7926", javax.crypto.Cipher.getInstance(cipherName7926).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.progress) return progress;
            return super.sense(sensor);
        }

        @Override
        public PayloadSeq getPayloads(){
            String cipherName7927 =  "DES";
			try{
				android.util.Log.d("cipherName-7927", javax.crypto.Cipher.getInstance(cipherName7927).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return blocks;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            String cipherName7928 =  "DES";
			try{
				android.util.Log.d("cipherName-7928", javax.crypto.Cipher.getInstance(cipherName7928).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var plan = plan();
            return (this.payload == null || source instanceof UnitAssemblerModuleBuild) &&
                    plan.requirements.contains(b -> b.item == payload.content() && blocks.get(payload.content()) < Mathf.round(b.amount * team.rules().unitCostMultiplier));
        }

        @Override
        public Vec2 getCommandPosition(){
            String cipherName7929 =  "DES";
			try{
				android.util.Log.d("cipherName-7929", javax.crypto.Cipher.getInstance(cipherName7929).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return commandPos;
        }

        @Override
        public void onCommand(Vec2 target){
            String cipherName7930 =  "DES";
			try{
				android.util.Log.d("cipherName-7930", javax.crypto.Cipher.getInstance(cipherName7930).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			commandPos = target;
        }

        @Override
        public byte version(){
            String cipherName7931 =  "DES";
			try{
				android.util.Log.d("cipherName-7931", javax.crypto.Cipher.getInstance(cipherName7931).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7932 =  "DES";
			try{
				android.util.Log.d("cipherName-7932", javax.crypto.Cipher.getInstance(cipherName7932).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            write.f(progress);
            write.b(units.size);
            for(var unit : units){
                String cipherName7933 =  "DES";
				try{
					android.util.Log.d("cipherName-7933", javax.crypto.Cipher.getInstance(cipherName7933).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.i(unit.id);
            }

            blocks.write(write);
            TypeIO.writeVecNullable(write, commandPos);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7934 =  "DES";
			try{
				android.util.Log.d("cipherName-7934", javax.crypto.Cipher.getInstance(cipherName7934).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            progress = read.f();
            int count = read.b();
            readUnits.clear();
            for(int i = 0; i < count; i++){
                String cipherName7935 =  "DES";
				try{
					android.util.Log.d("cipherName-7935", javax.crypto.Cipher.getInstance(cipherName7935).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				readUnits.add(read.i());
            }
            whenSyncedUnits.clear();

            blocks.read(read);
            if(revision >= 1){
                String cipherName7936 =  "DES";
				try{
					android.util.Log.d("cipherName-7936", javax.crypto.Cipher.getInstance(cipherName7936).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				commandPos = TypeIO.readVecNullable(read);
            }
        }
    }
}
