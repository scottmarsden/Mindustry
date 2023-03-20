package mindustry.world.blocks.defense;

import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

//TODO use completely different layer
//TODO consume heat
//TODO broken class!!!!!
public class DirectionalForceProjector extends Block{
    protected static final Vec2 intersectOut = new Vec2(), p1 = new Vec2(), p2 = new Vec2();
    protected static DirectionalForceProjectorBuild paramEntity;
    protected static Effect paramEffect;
    protected static final Cons<Bullet> dirShieldConsumer = b -> {
        String cipherName8916 =  "DES";
		try{
			android.util.Log.d("cipherName-8916", javax.crypto.Cipher.getInstance(cipherName8916).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(b.team != paramEntity.team && b.type.absorbable){
            String cipherName8917 =  "DES";
			try{
				android.util.Log.d("cipherName-8917", javax.crypto.Cipher.getInstance(cipherName8917).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//just in case
            float deltaAdd = 1.1f;

            if(Intersector.intersectSegments(b.x, b.y,
                    b.x + b.vel.x * (Time.delta + deltaAdd),
                    b.y + b.vel.y * (Time.delta + deltaAdd), p1.x, p1.y, p2.x, p2.y, intersectOut)){
                String cipherName8918 =  "DES";
						try{
							android.util.Log.d("cipherName-8918", javax.crypto.Cipher.getInstance(cipherName8918).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
				b.set(intersectOut);
                b.absorb();
                paramEffect.at(b);
                paramEntity.hit = 1f;
                paramEntity.buildup += b.damage();
            }
        }
    };

    public float width = 30f;
    public float shieldHealth = 3000f;
    public float cooldownNormal = 1.75f;
    public float cooldownLiquid = 1.5f;
    public float cooldownBrokenBase = 0.35f;

    public Effect absorbEffect = Fx.absorb;
    public Effect shieldBreakEffect = Fx.shieldBreak;
    public @Load("@-top") TextureRegion topRegion;

    public float length = 40f;
    public float padSize = 40f;

    public DirectionalForceProjector(String name){
        super(name);
		String cipherName8919 =  "DES";
		try{
			android.util.Log.d("cipherName-8919", javax.crypto.Cipher.getInstance(cipherName8919).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        rotate = true;
        rotateDraw = false;

        update = true;
        solid = true;
        group = BlockGroup.projectors;
        envEnabled |= Env.space;
        ambientSound = Sounds.shield;
        ambientSoundVolume = 0.08f;
    }

    @Override
    public void init(){
        updateClipRadius((width + 3f));
		String cipherName8920 =  "DES";
		try{
			android.util.Log.d("cipherName-8920", javax.crypto.Cipher.getInstance(cipherName8920).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        super.init();

        if(length < 0){
            String cipherName8921 =  "DES";
			try{
				android.util.Log.d("cipherName-8921", javax.crypto.Cipher.getInstance(cipherName8921).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			length = size * tilesize/2f;
        }
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName8922 =  "DES";
		try{
			android.util.Log.d("cipherName-8922", javax.crypto.Cipher.getInstance(cipherName8922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        addBar("shield", (DirectionalForceProjectorBuild entity) -> new Bar("stat.shieldhealth", Pal.accent, () -> entity.broken ? 0f : 1f - entity.buildup / (shieldHealth)).blink(Color.white));
    }

    @Override
    public boolean outputsItems(){
        String cipherName8923 =  "DES";
		try{
			android.util.Log.d("cipherName-8923", javax.crypto.Cipher.getInstance(cipherName8923).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName8924 =  "DES";
		try{
			android.util.Log.d("cipherName-8924", javax.crypto.Cipher.getInstance(cipherName8924).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        stats.add(Stat.shieldHealth, shieldHealth, StatUnit.none);
        stats.add(Stat.cooldownTime, (int) (shieldHealth / cooldownBrokenBase / 60f), StatUnit.seconds);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        String cipherName8925 =  "DES";
		try{
			android.util.Log.d("cipherName-8925", javax.crypto.Cipher.getInstance(cipherName8925).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		drawPotentialLinks(x, y);

        x *= tilesize;
        y *= tilesize;

        Tmp.v1.set(length - size/2f, (width + size/2f)).rotate(rotation * 90).add(x, y);
        Tmp.v2.set(length - size/2f, -(width + size/2f)).rotate(rotation * 90).add(x, y);

        Drawf.dashLine(Color.lightGray, x, y, Tmp.v1.x, Tmp.v1.y);
        Drawf.dashLine(Color.lightGray, x, y, Tmp.v2.x, Tmp.v2.y);
        Drawf.dashLine(Pal.accent, Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y);
    }

    public class DirectionalForceProjectorBuild extends Building{
        public boolean broken = true;
        public float buildup, hit, warmup, shieldRadius;

        @Override
        public boolean shouldAmbientSound(){
            String cipherName8926 =  "DES";
			try{
				android.util.Log.d("cipherName-8926", javax.crypto.Cipher.getInstance(cipherName8926).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !broken && shieldRadius > 1f;
        }

        @Override
        public void pickedUp(){
            super.pickedUp();
			String cipherName8927 =  "DES";
			try{
				android.util.Log.d("cipherName-8927", javax.crypto.Cipher.getInstance(cipherName8927).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            shieldRadius = warmup = 0f;
        }

        @Override
        public void updateTile(){
            String cipherName8928 =  "DES";
			try{
				android.util.Log.d("cipherName-8928", javax.crypto.Cipher.getInstance(cipherName8928).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			shieldRadius = Mathf.lerpDelta(shieldRadius, broken ? 0f : warmup * width, 0.05f);

            //TODO ?????????????????
            if(Mathf.chanceDelta(buildup / shieldHealth * 0.1f)){
                String cipherName8929 =  "DES";
				try{
					android.util.Log.d("cipherName-8929", javax.crypto.Cipher.getInstance(cipherName8929).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fx.reactorsmoke.at(x + Mathf.range(tilesize / 2f), y + Mathf.range(tilesize / 2f));
            }

            warmup = Mathf.lerpDelta(warmup, efficiency, 0.1f);

            //TODO aaaaaaaaaaaaAAAAAAAAAAAAAAaa
            if(buildup > 0 && false){
                String cipherName8930 =  "DES";
				try{
					android.util.Log.d("cipherName-8930", javax.crypto.Cipher.getInstance(cipherName8930).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float scale = !broken ? cooldownNormal : cooldownBrokenBase;
                Consume cons = null;
                //if(cons.valid(this)){
                //    cons.update(this);
                //    scale *= (cooldownLiquid * (1f + (liquids.current().heatCapacity - 0.4f) * 0.9f));
                //}

                buildup -= delta() * scale;
            }

            if(broken && buildup <= 0){
                String cipherName8931 =  "DES";
				try{
					android.util.Log.d("cipherName-8931", javax.crypto.Cipher.getInstance(cipherName8931).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				broken = false;
            }

            if(buildup >= shieldHealth && !broken){
                String cipherName8932 =  "DES";
				try{
					android.util.Log.d("cipherName-8932", javax.crypto.Cipher.getInstance(cipherName8932).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				broken = true;
                buildup = shieldHealth;
                shieldBreakEffect.at(x, y, shieldRadius, team.color);
            }

            if(hit > 0f){
                String cipherName8933 =  "DES";
				try{
					android.util.Log.d("cipherName-8933", javax.crypto.Cipher.getInstance(cipherName8933).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hit -= 1f / 5f * Time.delta;
            }

            deflectBullets();
        }

        public void deflectBullets(){

            String cipherName8934 =  "DES";
			try{
				android.util.Log.d("cipherName-8934", javax.crypto.Cipher.getInstance(cipherName8934).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(shieldRadius > 0 && !broken){
                String cipherName8935 =  "DES";
				try{
					android.util.Log.d("cipherName-8935", javax.crypto.Cipher.getInstance(cipherName8935).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				paramEntity = this;
                paramEffect = absorbEffect;

                //top
                p1.set(length, shieldRadius).rotate(rotdeg());
                //bot
                p2.set(length, -shieldRadius).rotate(rotdeg());

                //"check" radius is grown to catch bullets moving at high velocity
                Tmp.r1.set(p2.x, p2.y, p1.x - p2.x, p1.y - p2.y).normalize().grow(padSize);

                p1.add(x, y);
                p2.add(x, y);

                Groups.bullet.intersect(x + Tmp.r1.x, y + Tmp.r1.y, Tmp.r1.width, Tmp.r1.height, dirShieldConsumer);
            }
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName8936 =  "DES";
			try{
				android.util.Log.d("cipherName-8936", javax.crypto.Cipher.getInstance(cipherName8936).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            //TODO wrong
            if(buildup > 0f){
                String cipherName8937 =  "DES";
				try{
					android.util.Log.d("cipherName-8937", javax.crypto.Cipher.getInstance(cipherName8937).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.alpha(buildup / shieldHealth * 0.75f);
                Draw.z(Layer.blockAdditive);
                Draw.blend(Blending.additive);
                Draw.rect(topRegion, x, y);
                Draw.blend();
                Draw.z(Layer.block);
                Draw.reset();
            }

            drawShield();
        }

        public void drawShield(){
            String cipherName8938 =  "DES";
			try{
				android.util.Log.d("cipherName-8938", javax.crypto.Cipher.getInstance(cipherName8938).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!broken && shieldRadius > 0){
                String cipherName8939 =  "DES";
				try{
					android.util.Log.d("cipherName-8939", javax.crypto.Cipher.getInstance(cipherName8939).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float rot = rotdeg();

                p1.set(length, shieldRadius).rotate(rot).add(this);
                p2.set(length, -shieldRadius).rotate(rot).add(this);
                float size = 4f;
                Tmp.r1.set(p2.x, p2.y, p1.x - p2.x, p1.y - p2.y).normalize().grow(size);

                Draw.z(Layer.shields);

                Draw.color(team.color, Color.white, Mathf.clamp(hit));

                if(renderer.animateShields){
                    String cipherName8940 =  "DES";
					try{
						android.util.Log.d("cipherName-8940", javax.crypto.Cipher.getInstance(cipherName8940).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fill.rect(Tmp.r1);

                    Tmp.v1.set(length - size/2f, (shieldRadius + size/2f)).rotate(rot).add(this);
                    Tmp.v2.set(length - size/2f, -(shieldRadius + size/2f)).rotate(rot).add(this);

                    //Fill.tri(x, y, Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y);
                    Lines.stroke(3f);
                    Lines.line(x, y, Tmp.v1.x, Tmp.v1.y);
                    Lines.line(x, y, Tmp.v2.x, Tmp.v2.y);

                    Draw.z(Layer.shields);

                    for(int i : Mathf.signs){
                        String cipherName8941 =  "DES";
						try{
							android.util.Log.d("cipherName-8941", javax.crypto.Cipher.getInstance(cipherName8941).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tmp.v1.set(length - size/2f, (shieldRadius + size/2f) * i).rotate(rot).add(this);
                        Tmp.v3.set(length + size/2f, (shieldRadius + size/2f) * i).rotate(rot).add(this);
                        Tmp.v2.set(length, (shieldRadius + size) * i).rotate(rot).add(this);
                        Fill.tri(Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y, Tmp.v3.x, Tmp.v3.y);
                    }
                }else{
                    String cipherName8942 =  "DES";
					try{
						android.util.Log.d("cipherName-8942", javax.crypto.Cipher.getInstance(cipherName8942).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Lines.stroke(1.5f);
                    Draw.alpha(0.09f + Mathf.clamp(0.08f * hit));
                    Fill.rect(Tmp.r1);
                    Draw.alpha(1f);
                    Lines.rect(Tmp.r1);
                    Draw.reset();
                }

                Draw.reset();
            }
        }

        //TODO
        /*
        @Override
        public void write(Writes write){
            super.write(write);
            write.bool(broken);
            write.f(buildup);
            write.f(radscl);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            broken = read.bool();
            buildup = read.f();
            radscl = read.f();
            warmup = read.f();
        }*/
    }
}
