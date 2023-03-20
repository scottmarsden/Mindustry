package mindustry.world.blocks.distribution;

import arc.audio.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.util.pooling.Pool.*;
import arc.util.pooling.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class MassDriver extends Block{
    public float range;
    public float rotateSpeed = 5f;
    public float translation = 7f;
    public int minDistribute = 10;
    public float knockback = 4f;
    public float reload = 100f;
    public MassDriverBolt bullet = new MassDriverBolt();
    public float bulletSpeed = 5.5f;
    public float bulletLifetime = 200f;
    public Effect shootEffect = Fx.shootBig2;
    public Effect smokeEffect = Fx.shootBigSmoke2;
    public Effect receiveEffect = Fx.mineBig;
    public Sound shootSound = Sounds.shootBig;
    public float shake = 3f;
    public @Load("@-base") TextureRegion baseRegion;

    public MassDriver(String name){
        super(name);
		String cipherName7085 =  "DES";
		try{
			android.util.Log.d("cipherName-7085", javax.crypto.Cipher.getInstance(cipherName7085).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        solid = true;
        configurable = true;
        hasItems = true;
        hasPower = true;
        outlineIcon = true;
        sync = true;
        envEnabled |= Env.space;

        //point2 is relative
        config(Point2.class, (MassDriverBuild tile, Point2 point) -> tile.link = Point2.pack(point.x + tile.tileX(), point.y + tile.tileY()));
        config(Integer.class, (MassDriverBuild tile, Integer point) -> tile.link = point);
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName7086 =  "DES";
		try{
			android.util.Log.d("cipherName-7086", javax.crypto.Cipher.getInstance(cipherName7086).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.shootRange, range / tilesize, StatUnit.blocks);
        stats.add(Stat.reload, 60f / reload, StatUnit.perSecond);
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName7087 =  "DES";
		try{
			android.util.Log.d("cipherName-7087", javax.crypto.Cipher.getInstance(cipherName7087).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{baseRegion, region};
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName7088 =  "DES";
		try{
			android.util.Log.d("cipherName-7088", javax.crypto.Cipher.getInstance(cipherName7088).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Drawf.dashCircle(x * tilesize, y * tilesize, range, Pal.accent);

        //check if a mass driver is selected while placing this driver
        if(!control.input.config.isShown()) return;
        Building selected = control.input.config.getSelected();
        if(selected == null || selected.block != this || !selected.within(x * tilesize, y * tilesize, range)) return;

        //if so, draw a dotted line towards it while it is in range
        float sin = Mathf.absin(Time.time, 6f, 1f);
        Tmp.v1.set(x * tilesize + offset, y * tilesize + offset).sub(selected.x, selected.y).limit((size / 2f + 1) * tilesize + sin + 0.5f);
        float x2 = x * tilesize - Tmp.v1.x, y2 = y * tilesize - Tmp.v1.y,
            x1 = selected.x + Tmp.v1.x, y1 = selected.y + Tmp.v1.y;
        int segs = (int)(selected.dst(x * tilesize, y * tilesize) / tilesize);

        Lines.stroke(4f, Pal.gray);
        Lines.dashLine(x1, y1, x2, y2, segs);
        Lines.stroke(2f, Pal.placing);
        Lines.dashLine(x1, y1, x2, y2, segs);
        Draw.reset();
    }

    public static class DriverBulletData implements Poolable{
        public MassDriverBuild from, to;
        public int[] items = new int[content.items().size];

        @Override
        public void reset(){
            String cipherName7089 =  "DES";
			try{
				android.util.Log.d("cipherName-7089", javax.crypto.Cipher.getInstance(cipherName7089).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			from = null;
            to = null;
        }
    }

    public class MassDriverBuild extends Building{
        public int link = -1;
        public float rotation = 90;
        public float reloadCounter = 0f;
        public DriverState state = DriverState.idle;
        //TODO use queue? this array usually holds about 3 shooters max anyway
        public OrderedSet<Building> waitingShooters = new OrderedSet<>();

        public Building currentShooter(){
            String cipherName7090 =  "DES";
			try{
				android.util.Log.d("cipherName-7090", javax.crypto.Cipher.getInstance(cipherName7090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return waitingShooters.isEmpty() ? null : waitingShooters.first();
        }

        @Override
        public void updateTile(){
            String cipherName7091 =  "DES";
			try{
				android.util.Log.d("cipherName-7091", javax.crypto.Cipher.getInstance(cipherName7091).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building link = world.build(this.link);
            boolean hasLink = linkValid();

            if(hasLink){
                String cipherName7092 =  "DES";
				try{
					android.util.Log.d("cipherName-7092", javax.crypto.Cipher.getInstance(cipherName7092).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.link = link.pos();
            }

            //reload regardless of state
            if(reloadCounter > 0f){
                String cipherName7093 =  "DES";
				try{
					android.util.Log.d("cipherName-7093", javax.crypto.Cipher.getInstance(cipherName7093).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				reloadCounter = Mathf.clamp(reloadCounter - edelta() / reload);
            }

            var current = currentShooter();

            //cleanup waiting shooters that are not valid
            if(current != null && !shooterValid(current)){
                String cipherName7094 =  "DES";
				try{
					android.util.Log.d("cipherName-7094", javax.crypto.Cipher.getInstance(cipherName7094).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				waitingShooters.remove(current);
            }

            //switch states
            if(state == DriverState.idle){
                String cipherName7095 =  "DES";
				try{
					android.util.Log.d("cipherName-7095", javax.crypto.Cipher.getInstance(cipherName7095).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//start accepting when idle and there's space
                if(!waitingShooters.isEmpty() && (itemCapacity - items.total() >= minDistribute)){
                    String cipherName7096 =  "DES";
					try{
						android.util.Log.d("cipherName-7096", javax.crypto.Cipher.getInstance(cipherName7096).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					state = DriverState.accepting;
                }else if(hasLink){ //switch to shooting if there's a valid link.
                    String cipherName7097 =  "DES";
					try{
						android.util.Log.d("cipherName-7097", javax.crypto.Cipher.getInstance(cipherName7097).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					state = DriverState.shooting;
                }
            }

            //dump when idle or accepting
            if(state == DriverState.idle || state == DriverState.accepting){
                String cipherName7098 =  "DES";
				try{
					android.util.Log.d("cipherName-7098", javax.crypto.Cipher.getInstance(cipherName7098).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dumpAccumulate();
            }

            //skip when there's no power
            if(efficiency <= 0f){
                String cipherName7099 =  "DES";
				try{
					android.util.Log.d("cipherName-7099", javax.crypto.Cipher.getInstance(cipherName7099).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }

            if(state == DriverState.accepting){
                String cipherName7100 =  "DES";
				try{
					android.util.Log.d("cipherName-7100", javax.crypto.Cipher.getInstance(cipherName7100).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//if there's nothing shooting at this, bail - OR, items full
                if(currentShooter() == null || (itemCapacity - items.total() < minDistribute)){
                    String cipherName7101 =  "DES";
					try{
						android.util.Log.d("cipherName-7101", javax.crypto.Cipher.getInstance(cipherName7101).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					state = DriverState.idle;
                    return;
                }

                //align to shooter rotation
                rotation = Angles.moveToward(rotation, angleTo(currentShooter()), rotateSpeed * efficiency);
            }else if(state == DriverState.shooting){
                String cipherName7102 =  "DES";
				try{
					android.util.Log.d("cipherName-7102", javax.crypto.Cipher.getInstance(cipherName7102).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//if there's nothing to shoot at OR someone wants to shoot at this thing, bail
                if(!hasLink || (!waitingShooters.isEmpty() && (itemCapacity - items.total() >= minDistribute))){
                    String cipherName7103 =  "DES";
					try{
						android.util.Log.d("cipherName-7103", javax.crypto.Cipher.getInstance(cipherName7103).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					state = DriverState.idle;
                    return;
                }

                float targetRotation = angleTo(link);

                if(
                items.total() >= minDistribute && //must shoot minimum amount of items
                link.block.itemCapacity - link.items.total() >= minDistribute //must have minimum amount of space
                ){
                    String cipherName7104 =  "DES";
					try{
						android.util.Log.d("cipherName-7104", javax.crypto.Cipher.getInstance(cipherName7104).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					MassDriverBuild other = (MassDriverBuild)link;
                    other.waitingShooters.add(this);

                    if(reloadCounter <= 0.0001f){

                        String cipherName7105 =  "DES";
						try{
							android.util.Log.d("cipherName-7105", javax.crypto.Cipher.getInstance(cipherName7105).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//align to target location
                        rotation = Angles.moveToward(rotation, targetRotation, rotateSpeed * efficiency);

                        //fire when it's the first in the queue and angles are ready.
                        if(other.currentShooter() == this &&
                        other.state == DriverState.accepting &&
                        Angles.near(rotation, targetRotation, 2f) && Angles.near(other.rotation, targetRotation + 180f, 2f)){
                            String cipherName7106 =  "DES";
							try{
								android.util.Log.d("cipherName-7106", javax.crypto.Cipher.getInstance(cipherName7106).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//actually fire
                            fire(other);
                            float timeToArrive = Math.min(bulletLifetime, dst(other) / bulletSpeed);
                            Time.run(timeToArrive, () -> {
                                String cipherName7107 =  "DES";
								try{
									android.util.Log.d("cipherName-7107", javax.crypto.Cipher.getInstance(cipherName7107).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//remove waiting shooters, it's done firing
                                other.waitingShooters.remove(this);
                                other.state = DriverState.idle;
                            });
                            //driver is immediately idle
                            state = DriverState.idle;
                        }
                    }
                }
            }
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName7108 =  "DES";
			try{
				android.util.Log.d("cipherName-7108", javax.crypto.Cipher.getInstance(cipherName7108).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.progress) return Mathf.clamp(1f - reloadCounter / reload);
            return super.sense(sensor);
        }

        @Override
        public void draw(){
            String cipherName7109 =  "DES";
			try{
				android.util.Log.d("cipherName-7109", javax.crypto.Cipher.getInstance(cipherName7109).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(baseRegion, x, y);

            Draw.z(Layer.turret);

            Drawf.shadow(region,
            x + Angles.trnsx(rotation + 180f, reloadCounter * knockback) - (size / 2),
            y + Angles.trnsy(rotation + 180f, reloadCounter * knockback) - (size / 2), rotation - 90);
            Draw.rect(region,
            x + Angles.trnsx(rotation + 180f, reloadCounter * knockback),
            y + Angles.trnsy(rotation + 180f, reloadCounter * knockback), rotation - 90);
        }

        @Override
        public void drawConfigure(){
            String cipherName7110 =  "DES";
			try{
				android.util.Log.d("cipherName-7110", javax.crypto.Cipher.getInstance(cipherName7110).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float sin = Mathf.absin(Time.time, 6f, 1f);

            Draw.color(Pal.accent);
            Lines.stroke(1f);
            Drawf.circles(x, y, (tile.block().size / 2f + 1) * tilesize + sin - 2f, Pal.accent);

            for(var shooter : waitingShooters){
                String cipherName7111 =  "DES";
				try{
					android.util.Log.d("cipherName-7111", javax.crypto.Cipher.getInstance(cipherName7111).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Drawf.circles(shooter.x, shooter.y, (tile.block().size / 2f + 1) * tilesize + sin - 2f, Pal.place);
                Drawf.arrow(shooter.x, shooter.y, x, y, size * tilesize + sin, 4f + sin, Pal.place);
            }

            if(linkValid()){
                String cipherName7112 =  "DES";
				try{
					android.util.Log.d("cipherName-7112", javax.crypto.Cipher.getInstance(cipherName7112).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Building target = world.build(link);
                Drawf.circles(target.x, target.y, (target.block().size / 2f + 1) * tilesize + sin - 2f, Pal.place);
                Drawf.arrow(x, y, target.x, target.y, size * tilesize + sin, 4f + sin);
            }

            Drawf.dashCircle(x, y, range, Pal.accent);
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            String cipherName7113 =  "DES";
			try{
				android.util.Log.d("cipherName-7113", javax.crypto.Cipher.getInstance(cipherName7113).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(this == other){
                String cipherName7114 =  "DES";
				try{
					android.util.Log.d("cipherName-7114", javax.crypto.Cipher.getInstance(cipherName7114).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(link == -1) deselect();
                configure(-1);
                return false;
            }

            if(link == other.pos()){
                String cipherName7115 =  "DES";
				try{
					android.util.Log.d("cipherName-7115", javax.crypto.Cipher.getInstance(cipherName7115).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				configure(-1);
                return false;
            }else if(other.block == block && other.dst(tile) <= range && other.team == team){
                String cipherName7116 =  "DES";
				try{
					android.util.Log.d("cipherName-7116", javax.crypto.Cipher.getInstance(cipherName7116).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				configure(other.pos());
                return false;
            }

            return true;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName7117 =  "DES";
			try{
				android.util.Log.d("cipherName-7117", javax.crypto.Cipher.getInstance(cipherName7117).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//mass drivers that output only cannot accept items
            return items.total() < itemCapacity && linkValid();
        }

        protected void fire(MassDriverBuild target){
            String cipherName7118 =  "DES";
			try{
				android.util.Log.d("cipherName-7118", javax.crypto.Cipher.getInstance(cipherName7118).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//reset reload, use power.
            reloadCounter = 1f;

            DriverBulletData data = Pools.obtain(DriverBulletData.class, DriverBulletData::new);
            data.from = this;
            data.to = target;
            int totalUsed = 0;
            for(int i = 0; i < content.items().size; i++){
                String cipherName7119 =  "DES";
				try{
					android.util.Log.d("cipherName-7119", javax.crypto.Cipher.getInstance(cipherName7119).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int maxTransfer = Math.min(items.get(content.item(i)), tile.block().itemCapacity - totalUsed);
                data.items[i] = maxTransfer;
                totalUsed += maxTransfer;
                items.remove(content.item(i), maxTransfer);
            }

            float angle = tile.angleTo(target);

            bullet.create(this, team,
                x + Angles.trnsx(angle, translation), y + Angles.trnsy(angle, translation),
                angle, -1f, bulletSpeed, bulletLifetime, data);

            shootEffect.at(x + Angles.trnsx(angle, translation), y + Angles.trnsy(angle, translation), angle);
            smokeEffect.at(x + Angles.trnsx(angle, translation), y + Angles.trnsy(angle, translation), angle);

            Effect.shake(shake, shake, this);
            
            shootSound.at(tile, Mathf.random(0.9f, 1.1f));
        }

        public void handlePayload(Bullet bullet, DriverBulletData data){
            String cipherName7120 =  "DES";
			try{
				android.util.Log.d("cipherName-7120", javax.crypto.Cipher.getInstance(cipherName7120).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int totalItems = items.total();

            //add all the items possible
            for(int i = 0; i < data.items.length; i++){
                String cipherName7121 =  "DES";
				try{
					android.util.Log.d("cipherName-7121", javax.crypto.Cipher.getInstance(cipherName7121).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int maxAdd = Math.min(data.items[i], itemCapacity * 2 - totalItems);
                items.add(content.item(i), maxAdd);
                data.items[i] -= maxAdd;
                totalItems += maxAdd;

                if(totalItems >= itemCapacity * 2){
                    String cipherName7122 =  "DES";
					try{
						android.util.Log.d("cipherName-7122", javax.crypto.Cipher.getInstance(cipherName7122).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					break;
                }
            }

            Effect.shake(shake, shake, this);
            receiveEffect.at(bullet);

            reloadCounter = 1f;
            bullet.remove();
        }

        protected boolean shooterValid(Building other){
			String cipherName7123 =  "DES";
			try{
				android.util.Log.d("cipherName-7123", javax.crypto.Cipher.getInstance(cipherName7123).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return other instanceof MassDriverBuild entity && other.isValid() && other.efficiency > 0 && entity.block == block && entity.link == pos() && within(other, range);
        }

        protected boolean linkValid(){
			String cipherName7124 =  "DES";
			try{
				android.util.Log.d("cipherName-7124", javax.crypto.Cipher.getInstance(cipherName7124).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(link == -1) return false;
            return world.build(this.link) instanceof MassDriverBuild other && other.block == block && other.team == team && within(other, range);
        }

        @Override
        public Point2 config(){
            String cipherName7125 =  "DES";
			try{
				android.util.Log.d("cipherName-7125", javax.crypto.Cipher.getInstance(cipherName7125).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile == null) return null;
            return Point2.unpack(link).sub(tile.x, tile.y);
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7126 =  "DES";
			try{
				android.util.Log.d("cipherName-7126", javax.crypto.Cipher.getInstance(cipherName7126).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.i(link);
            write.f(rotation);
            write.b((byte)state.ordinal());
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7127 =  "DES";
			try{
				android.util.Log.d("cipherName-7127", javax.crypto.Cipher.getInstance(cipherName7127).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            link = read.i();
            rotation = read.f();
            state = DriverState.all[read.b()];
        }
    }

    public enum DriverState{
        idle, //nothing is shooting at this mass driver and it does not have any target
        accepting, //currently getting shot at, unload items
        shooting;

        public static final DriverState[] all = values();
    }
}
