package mindustry.world.blocks.units;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Reconstructor extends UnitBlock{
    public float constructTime = 60 * 2;
    public Seq<UnitType[]> upgrades = new Seq<>();
    public int[] capacities = {};

    public Reconstructor(String name){
        super(name);
		String cipherName8042 =  "DES";
		try{
			android.util.Log.d("cipherName-8042", javax.crypto.Cipher.getInstance(cipherName8042).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        regionRotated1 = 1;
        regionRotated2 = 2;
        commandable = true;
        ambientSound = Sounds.respawning;
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName8043 =  "DES";
		try{
			android.util.Log.d("cipherName-8043", javax.crypto.Cipher.getInstance(cipherName8043).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.rect(region, plan.drawx(), plan.drawy());
        Draw.rect(inRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.rect(outRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.rect(topRegion, plan.drawx(), plan.drawy());
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName8044 =  "DES";
		try{
			android.util.Log.d("cipherName-8044", javax.crypto.Cipher.getInstance(cipherName8044).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{region, inRegion, outRegion, topRegion};
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName8045 =  "DES";
		try{
			android.util.Log.d("cipherName-8045", javax.crypto.Cipher.getInstance(cipherName8045).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("progress", (ReconstructorBuild entity) -> new Bar("bar.progress", Pal.ammo, entity::fraction));
        addBar("units", (ReconstructorBuild e) ->
        new Bar(
            () -> e.unit() == null ? "[lightgray]" + Iconc.cancel :
                Core.bundle.format("bar.unitcap",
                Fonts.getUnicodeStr(e.unit().name),
                e.team.data().countType(e.unit()),
                Units.getCap(e.team)
            ),
            () -> Pal.power,
            () -> e.unit() == null ? 0f : (float)e.team.data().countType(e.unit()) / Units.getCap(e.team)
        ));
    }

    @Override
    public void setStats(){
        stats.timePeriod = constructTime;
		String cipherName8046 =  "DES";
		try{
			android.util.Log.d("cipherName-8046", javax.crypto.Cipher.getInstance(cipherName8046).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        super.setStats();

        stats.add(Stat.productionTime, constructTime / 60f, StatUnit.seconds);
        stats.add(Stat.output, table -> {
            String cipherName8047 =  "DES";
			try{
				android.util.Log.d("cipherName-8047", javax.crypto.Cipher.getInstance(cipherName8047).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.row();
            for(var upgrade : upgrades){
                String cipherName8048 =  "DES";
				try{
					android.util.Log.d("cipherName-8048", javax.crypto.Cipher.getInstance(cipherName8048).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(upgrade[0].unlockedNow() && upgrade[1].unlockedNow()){
                    String cipherName8049 =  "DES";
					try{
						android.util.Log.d("cipherName-8049", javax.crypto.Cipher.getInstance(cipherName8049).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					table.table(Styles.grayPanel, t -> {
                        String cipherName8050 =  "DES";
						try{
							android.util.Log.d("cipherName-8050", javax.crypto.Cipher.getInstance(cipherName8050).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.left();

                        t.image(upgrade[0].uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
                        t.table(info -> {
                            String cipherName8051 =  "DES";
							try{
								android.util.Log.d("cipherName-8051", javax.crypto.Cipher.getInstance(cipherName8051).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							info.add(upgrade[0].localizedName).left();
                            info.row();
                        }).pad(10).left();
                    }).fill().padTop(5).padBottom(5);

                    table.table(Styles.grayPanel, t -> {

                        String cipherName8052 =  "DES";
						try{
							android.util.Log.d("cipherName-8052", javax.crypto.Cipher.getInstance(cipherName8052).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.image(Icon.right).color(Pal.darkishGray).size(40).pad(10f);
                    }).fill().padTop(5).padBottom(5);

                    table.table(Styles.grayPanel, t -> {
                        String cipherName8053 =  "DES";
						try{
							android.util.Log.d("cipherName-8053", javax.crypto.Cipher.getInstance(cipherName8053).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.left();

                        t.image(upgrade[1].uiIcon).size(40).pad(10f).right().scaling(Scaling.fit);
                        t.table(info -> {
                            String cipherName8054 =  "DES";
							try{
								android.util.Log.d("cipherName-8054", javax.crypto.Cipher.getInstance(cipherName8054).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							info.add(upgrade[1].localizedName).right();
                            info.row();
                        }).pad(10).right();
                    }).fill().padTop(5).padBottom(5);

                    table.row();
                }
            }
        });
    }

    @Override
    public void init(){
        capacities = new int[Vars.content.items().size];
		String cipherName8055 =  "DES";
		try{
			android.util.Log.d("cipherName-8055", javax.crypto.Cipher.getInstance(cipherName8055).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        ConsumeItems cons = findConsumer(c -> c instanceof ConsumeItems);
        if(cons != null){
            String cipherName8056 =  "DES";
			try{
				android.util.Log.d("cipherName-8056", javax.crypto.Cipher.getInstance(cipherName8056).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(ItemStack stack : cons.items){
                String cipherName8057 =  "DES";
				try{
					android.util.Log.d("cipherName-8057", javax.crypto.Cipher.getInstance(cipherName8057).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				capacities[stack.item.id] = Math.max(capacities[stack.item.id], stack.amount * 2);
                itemCapacity = Math.max(itemCapacity, stack.amount * 2);
            }
        }

        consumeBuilder.each(c -> c.multiplier = b -> state.rules.unitCost(b.team));

        super.init();
    }

    public void addUpgrade(UnitType from, UnitType to){
        String cipherName8058 =  "DES";
		try{
			android.util.Log.d("cipherName-8058", javax.crypto.Cipher.getInstance(cipherName8058).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		upgrades.add(new UnitType[]{from, to});
    }

    public class ReconstructorBuild extends UnitBuild{
        public @Nullable Vec2 commandPos;

        public float fraction(){
            String cipherName8059 =  "DES";
			try{
				android.util.Log.d("cipherName-8059", javax.crypto.Cipher.getInstance(cipherName8059).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return progress / constructTime;
        }

        @Override
        public boolean shouldActiveSound(){
            String cipherName8060 =  "DES";
			try{
				android.util.Log.d("cipherName-8060", javax.crypto.Cipher.getInstance(cipherName8060).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return shouldConsume();
        }

        @Override
        public Vec2 getCommandPosition(){
            String cipherName8061 =  "DES";
			try{
				android.util.Log.d("cipherName-8061", javax.crypto.Cipher.getInstance(cipherName8061).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return commandPos;
        }

        @Override
        public void onCommand(Vec2 target){
            String cipherName8062 =  "DES";
			try{
				android.util.Log.d("cipherName-8062", javax.crypto.Cipher.getInstance(cipherName8062).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			commandPos = target;
        }

        @Override
        public boolean acceptUnitPayload(Unit unit){
            String cipherName8063 =  "DES";
			try{
				android.util.Log.d("cipherName-8063", javax.crypto.Cipher.getInstance(cipherName8063).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return hasUpgrade(unit.type) && !upgrade(unit.type).isBanned();
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
			String cipherName8064 =  "DES";
			try{
				android.util.Log.d("cipherName-8064", javax.crypto.Cipher.getInstance(cipherName8064).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(!(this.payload == null
            && (this.enabled || source == this)
            && relativeTo(source) != rotation
            && payload instanceof UnitPayload pay)){
                return false;
            }

            var upgrade = upgrade(pay.unit.type);

            if(upgrade != null){
                if(!upgrade.unlockedNowHost() && !team.isAI()){
                    //flash "not researched"
                    pay.showOverlay(Icon.tree);
                }

                if(upgrade.isBanned()){
                    //flash an X, meaning 'banned'
                    pay.showOverlay(Icon.cancel);
                }
            }

            return upgrade != null && (team.isAI() || upgrade.unlockedNowHost()) && !upgrade.isBanned();
        }

        @Override
        public int getMaximumAccepted(Item item){
            String cipherName8065 =  "DES";
			try{
				android.util.Log.d("cipherName-8065", javax.crypto.Cipher.getInstance(cipherName8065).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return capacities[item.id];
        }

        @Override
        public void overwrote(Seq<Building> builds){
            String cipherName8066 =  "DES";
			try{
				android.util.Log.d("cipherName-8066", javax.crypto.Cipher.getInstance(cipherName8066).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(builds.first().block == block){
                String cipherName8067 =  "DES";
				try{
					android.util.Log.d("cipherName-8067", javax.crypto.Cipher.getInstance(cipherName8067).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				items.add(builds.first().items);
            }
        }

        @Override
        public void draw(){
            String cipherName8068 =  "DES";
			try{
				android.util.Log.d("cipherName-8068", javax.crypto.Cipher.getInstance(cipherName8068).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(region, x, y);

            //draw input
            boolean fallback = true;
            for(int i = 0; i < 4; i++){
                String cipherName8069 =  "DES";
				try{
					android.util.Log.d("cipherName-8069", javax.crypto.Cipher.getInstance(cipherName8069).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(blends(i) && i != rotation){
                    String cipherName8070 =  "DES";
					try{
						android.util.Log.d("cipherName-8070", javax.crypto.Cipher.getInstance(cipherName8070).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.rect(inRegion, x, y, (i * 90) - 180);
                    fallback = false;
                }
            }
            if(fallback) Draw.rect(inRegion, x, y, rotation * 90);

            Draw.rect(outRegion, x, y, rotdeg());

            if(constructing() && hasArrived()){
                String cipherName8071 =  "DES";
				try{
					android.util.Log.d("cipherName-8071", javax.crypto.Cipher.getInstance(cipherName8071).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.draw(Layer.blockOver, () -> {
                    String cipherName8072 =  "DES";
					try{
						android.util.Log.d("cipherName-8072", javax.crypto.Cipher.getInstance(cipherName8072).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.alpha(1f - progress/ constructTime);
                    Draw.rect(payload.unit.type.fullIcon, x, y, payload.rotation() - 90);
                    Draw.reset();
                    Drawf.construct(this, upgrade(payload.unit.type), payload.rotation() - 90f, progress / constructTime, speedScl, time);
                });
            }else{
                String cipherName8073 =  "DES";
				try{
					android.util.Log.d("cipherName-8073", javax.crypto.Cipher.getInstance(cipherName8073).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.z(Layer.blockOver);

                drawPayload();
            }

            Draw.z(Layer.blockOver + 0.1f);
            Draw.rect(topRegion, x, y);
        }

        @Override
        public Object senseObject(LAccess sensor){
            String cipherName8074 =  "DES";
			try{
				android.util.Log.d("cipherName-8074", javax.crypto.Cipher.getInstance(cipherName8074).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.config) return unit();
            return super.senseObject(sensor);
        }

        @Override
        public void updateTile(){
            String cipherName8075 =  "DES";
			try{
				android.util.Log.d("cipherName-8075", javax.crypto.Cipher.getInstance(cipherName8075).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean valid = false;

            if(payload != null){
                String cipherName8076 =  "DES";
				try{
					android.util.Log.d("cipherName-8076", javax.crypto.Cipher.getInstance(cipherName8076).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//check if offloading
                if(!hasUpgrade(payload.unit.type)){
                    String cipherName8077 =  "DES";
					try{
						android.util.Log.d("cipherName-8077", javax.crypto.Cipher.getInstance(cipherName8077).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					moveOutPayload();
                }else{ //update progress
                    String cipherName8078 =  "DES";
					try{
						android.util.Log.d("cipherName-8078", javax.crypto.Cipher.getInstance(cipherName8078).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(moveInPayload()){
                        String cipherName8079 =  "DES";
						try{
							android.util.Log.d("cipherName-8079", javax.crypto.Cipher.getInstance(cipherName8079).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(efficiency > 0){
                            String cipherName8080 =  "DES";
							try{
								android.util.Log.d("cipherName-8080", javax.crypto.Cipher.getInstance(cipherName8080).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							valid = true;
                            progress += edelta() * state.rules.unitBuildSpeed(team);
                        }

                        //upgrade the unit
                        if(progress >= constructTime){
                            String cipherName8081 =  "DES";
							try{
								android.util.Log.d("cipherName-8081", javax.crypto.Cipher.getInstance(cipherName8081).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							payload.unit = upgrade(payload.unit.type).create(payload.unit.team());
                            if(commandPos != null && payload.unit.isCommandable()){
                                String cipherName8082 =  "DES";
								try{
									android.util.Log.d("cipherName-8082", javax.crypto.Cipher.getInstance(cipherName8082).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								payload.unit.command().commandPosition(commandPos);
                            }
                            progress %= 1f;
                            Effect.shake(2f, 3f, this);
                            Fx.producesmoke.at(this);
                            consume();
                            Events.fire(new UnitCreateEvent(payload.unit, this));
                        }
                    }
                }
            }

            speedScl = Mathf.lerpDelta(speedScl, Mathf.num(valid), 0.05f);
            time += edelta() * speedScl * state.rules.unitBuildSpeed(team);
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName8083 =  "DES";
			try{
				android.util.Log.d("cipherName-8083", javax.crypto.Cipher.getInstance(cipherName8083).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.progress) return Mathf.clamp(fraction());
            return super.sense(sensor);
        }

        @Override
        public boolean shouldConsume(){
            String cipherName8084 =  "DES";
			try{
				android.util.Log.d("cipherName-8084", javax.crypto.Cipher.getInstance(cipherName8084).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return constructing() && enabled;
        }

        public UnitType unit(){
            String cipherName8085 =  "DES";
			try{
				android.util.Log.d("cipherName-8085", javax.crypto.Cipher.getInstance(cipherName8085).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(payload == null) return null;

            UnitType t = upgrade(payload.unit.type);
            return t != null && (t.unlockedNowHost() || team.isAI()) ? t : null;
        }

        public boolean constructing(){
            String cipherName8086 =  "DES";
			try{
				android.util.Log.d("cipherName-8086", javax.crypto.Cipher.getInstance(cipherName8086).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payload != null && hasUpgrade(payload.unit.type);
        }

        public boolean hasUpgrade(UnitType type){
            String cipherName8087 =  "DES";
			try{
				android.util.Log.d("cipherName-8087", javax.crypto.Cipher.getInstance(cipherName8087).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			UnitType t = upgrade(type);
            return t != null && (t.unlockedNowHost() || team.isAI()) && !type.isBanned();
        }

        public UnitType upgrade(UnitType type){
            String cipherName8088 =  "DES";
			try{
				android.util.Log.d("cipherName-8088", javax.crypto.Cipher.getInstance(cipherName8088).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			UnitType[] r =  upgrades.find(u -> u[0] == type);
            return r == null ? null : r[1];
        }

        @Override
        public byte version(){
            String cipherName8089 =  "DES";
			try{
				android.util.Log.d("cipherName-8089", javax.crypto.Cipher.getInstance(cipherName8089).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 2;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName8090 =  "DES";
			try{
				android.util.Log.d("cipherName-8090", javax.crypto.Cipher.getInstance(cipherName8090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            write.f(progress);
            TypeIO.writeVecNullable(write, commandPos);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName8091 =  "DES";
			try{
				android.util.Log.d("cipherName-8091", javax.crypto.Cipher.getInstance(cipherName8091).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(revision >= 1){
                String cipherName8092 =  "DES";
				try{
					android.util.Log.d("cipherName-8092", javax.crypto.Cipher.getInstance(cipherName8092).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				progress = read.f();
            }

            if(revision >= 2){
                String cipherName8093 =  "DES";
				try{
					android.util.Log.d("cipherName-8093", javax.crypto.Cipher.getInstance(cipherName8093).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				commandPos = TypeIO.readVecNullable(read);
            }
        }

    }
}
