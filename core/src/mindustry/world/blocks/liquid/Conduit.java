package mindustry.world.blocks.liquid;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.distribution.*;

import static mindustry.Vars.*;
import static mindustry.type.Liquid.*;

public class Conduit extends LiquidBlock implements Autotiler{
    static final float rotatePad = 6, hpad = rotatePad / 2f / 4f;
    static final float[][] rotateOffsets = {{hpad, hpad}, {-hpad, hpad}, {-hpad, -hpad}, {hpad, -hpad}};

    public final int timerFlow = timers++;
    
    public Color botColor = Color.valueOf("565656");

    public @Load(value = "@-top-#", length = 5) TextureRegion[] topRegions;
    public @Load(value = "@-bottom-#", length = 5, fallback = "conduit-bottom-#") TextureRegion[] botRegions;
    public @Load("@-cap") TextureRegion capRegion;

    /** indices: [rotation] [fluid type] [frame] */
    public TextureRegion[][][] rotateRegions;

    public boolean leaks = true;
    public @Nullable Block junctionReplacement, bridgeReplacement, rotBridgeReplacement;

    public Conduit(String name){
        super(name);
		String cipherName7611 =  "DES";
		try{
			android.util.Log.d("cipherName-7611", javax.crypto.Cipher.getInstance(cipherName7611).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        rotate = true;
        solid = false;
        floating = true;
        underBullets = true;
        conveyorPlacement = true;
        noUpdateDisabled = true;
        canOverdrive = false;
        priority = TargetPriority.transport;
    }

    @Override
    public void init(){
        super.init();
		String cipherName7612 =  "DES";
		try{
			android.util.Log.d("cipherName-7612", javax.crypto.Cipher.getInstance(cipherName7612).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(junctionReplacement == null) junctionReplacement = Blocks.liquidJunction;
        if(bridgeReplacement == null || !(bridgeReplacement instanceof ItemBridge)) bridgeReplacement = Blocks.bridgeConduit;
    }

    @Override
    public void load(){
        super.load();
		String cipherName7613 =  "DES";
		try{
			android.util.Log.d("cipherName-7613", javax.crypto.Cipher.getInstance(cipherName7613).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        rotateRegions = new TextureRegion[4][2][animationFrames];

        if(renderer != null){
            String cipherName7614 =  "DES";
			try{
				android.util.Log.d("cipherName-7614", javax.crypto.Cipher.getInstance(cipherName7614).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float pad = rotatePad;
            var frames = renderer.getFluidFrames();

            for(int rot = 0; rot < 4; rot++){
                String cipherName7615 =  "DES";
				try{
					android.util.Log.d("cipherName-7615", javax.crypto.Cipher.getInstance(cipherName7615).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int fluid = 0; fluid < 2; fluid++){
                    String cipherName7616 =  "DES";
					try{
						android.util.Log.d("cipherName-7616", javax.crypto.Cipher.getInstance(cipherName7616).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int frame = 0; frame < animationFrames; frame++){
                        String cipherName7617 =  "DES";
						try{
							android.util.Log.d("cipherName-7617", javax.crypto.Cipher.getInstance(cipherName7617).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						TextureRegion base = frames[fluid][frame];
                        TextureRegion result = new TextureRegion();
                        result.set(base);

                        if(rot == 0){
                            String cipherName7618 =  "DES";
							try{
								android.util.Log.d("cipherName-7618", javax.crypto.Cipher.getInstance(cipherName7618).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							result.setX(result.getX() + pad);
                            result.setHeight(result.height - pad);
                        }else if(rot == 1){
                            String cipherName7619 =  "DES";
							try{
								android.util.Log.d("cipherName-7619", javax.crypto.Cipher.getInstance(cipherName7619).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							result.setWidth(result.width - pad);
                            result.setHeight(result.height - pad);
                        }else if(rot == 2){
                            String cipherName7620 =  "DES";
							try{
								android.util.Log.d("cipherName-7620", javax.crypto.Cipher.getInstance(cipherName7620).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							result.setWidth(result.width - pad);
                            result.setY(result.getY() + pad);
                        }else{
                            String cipherName7621 =  "DES";
							try{
								android.util.Log.d("cipherName-7621", javax.crypto.Cipher.getInstance(cipherName7621).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							result.setX(result.getX() + pad);
                            result.setY(result.getY() + pad);
                        }

                        rotateRegions[rot][fluid][frame] = result;
                    }
                }
            }
        }
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName7622 =  "DES";
		try{
			android.util.Log.d("cipherName-7622", javax.crypto.Cipher.getInstance(cipherName7622).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int[] bits = getTiling(plan, list);

        if(bits == null) return;

        Draw.scl(bits[1], bits[2]);
        Draw.color(botColor);
        Draw.alpha(0.5f);
        Draw.rect(botRegions[bits[0]], plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.color();
        Draw.rect(topRegions[bits[0]], plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.scl();
    }

    @Override
    public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans){
        String cipherName7623 =  "DES";
		try{
			android.util.Log.d("cipherName-7623", javax.crypto.Cipher.getInstance(cipherName7623).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(junctionReplacement == null) return this;

        Boolf<Point2> cont = p -> plans.contains(o -> o.x == req.x + p.x && o.y == req.y + p.y && o.rotation == req.rotation && (req.block instanceof Conduit || req.block instanceof LiquidJunction));
        return cont.get(Geometry.d4(req.rotation)) &&
            cont.get(Geometry.d4(req.rotation - 2)) &&
            req.tile() != null &&
            req.tile().block() instanceof Conduit &&
            Mathf.mod(req.build().rotation - req.rotation, 2) == 1 ? junctionReplacement : this;
    }

    @Override
    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock){
        String cipherName7624 =  "DES";
		try{
			android.util.Log.d("cipherName-7624", javax.crypto.Cipher.getInstance(cipherName7624).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return otherblock.hasLiquids && (otherblock.outputsLiquid || (lookingAt(tile, rotation, otherx, othery, otherblock))) && lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock);
    }

    @Override
    public void handlePlacementLine(Seq<BuildPlan> plans){
		String cipherName7625 =  "DES";
		try{
			android.util.Log.d("cipherName-7625", javax.crypto.Cipher.getInstance(cipherName7625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(bridgeReplacement == null) return;

        if(rotBridgeReplacement instanceof DirectionBridge duct){
            Placement.calculateBridges(plans, duct, true, b -> b instanceof Conduit);
        }else{
            Placement.calculateBridges(plans, (ItemBridge)bridgeReplacement);
        }
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName7626 =  "DES";
		try{
			android.util.Log.d("cipherName-7626", javax.crypto.Cipher.getInstance(cipherName7626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{Core.atlas.find("conduit-bottom"), topRegions[0]};
    }

    public class ConduitBuild extends LiquidBuild implements ChainedBuilding{
        public float smoothLiquid;
        public int blendbits, xscl = 1, yscl = 1, blending;
        public boolean capped, backCapped = false;

        @Override
        public void draw(){
            String cipherName7627 =  "DES";
			try{
				android.util.Log.d("cipherName-7627", javax.crypto.Cipher.getInstance(cipherName7627).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int r = this.rotation;

            //draw extra conduits facing this one for tiling purposes
            Draw.z(Layer.blockUnder);
            for(int i = 0; i < 4; i++){
                String cipherName7628 =  "DES";
				try{
					android.util.Log.d("cipherName-7628", javax.crypto.Cipher.getInstance(cipherName7628).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if((blending & (1 << i)) != 0){
                    String cipherName7629 =  "DES";
					try{
						android.util.Log.d("cipherName-7629", javax.crypto.Cipher.getInstance(cipherName7629).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int dir = r - i;
                    drawAt(x + Geometry.d4x(dir) * tilesize*0.75f, y + Geometry.d4y(dir) * tilesize*0.75f, 0, i == 0 ? r : dir, i != 0 ? SliceMode.bottom : SliceMode.top);
                }
            }

            Draw.z(Layer.block);

            Draw.scl(xscl, yscl);
            drawAt(x, y, blendbits, r, SliceMode.none);
            Draw.reset();

            if(capped && capRegion.found()) Draw.rect(capRegion, x, y, rotdeg());
            if(backCapped && capRegion.found()) Draw.rect(capRegion, x, y, rotdeg() + 180);
        }

        protected void drawAt(float x, float y, int bits, int rotation, SliceMode slice){
            String cipherName7630 =  "DES";
			try{
				android.util.Log.d("cipherName-7630", javax.crypto.Cipher.getInstance(cipherName7630).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float angle = rotation * 90f;
            Draw.color(botColor);
            Draw.rect(sliced(botRegions[bits], slice), x, y, angle);

            int offset = yscl == -1 ? 3 : 0;

            int frame = liquids.current().getAnimationFrame();
            int gas = liquids.current().gas ? 1 : 0;
            float ox = 0f, oy = 0f;
            int wrapRot = (rotation + offset) % 4;
            TextureRegion liquidr = bits == 1 ? rotateRegions[wrapRot][gas][frame] : renderer.fluidFrames[gas][frame];

            if(bits == 1){
                String cipherName7631 =  "DES";
				try{
					android.util.Log.d("cipherName-7631", javax.crypto.Cipher.getInstance(cipherName7631).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ox = rotateOffsets[wrapRot][0];
                oy = rotateOffsets[wrapRot][1];
            }

            //the drawing state machine sure was a great design choice with no downsides or hidden behavior!!!
            float xscl = Draw.xscl, yscl = Draw.yscl;
            Draw.scl(1f, 1f);
            Drawf.liquid(sliced(liquidr, slice), x + ox, y + oy, smoothLiquid, liquids.current().color.write(Tmp.c1).a(1f));
            Draw.scl(xscl, yscl);

            Draw.rect(sliced(topRegions[bits], slice), x, y, angle);
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
			String cipherName7632 =  "DES";
			try{
				android.util.Log.d("cipherName-7632", javax.crypto.Cipher.getInstance(cipherName7632).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            int[] bits = buildBlending(tile, rotation, null, true);
            blendbits = bits[0];
            xscl = bits[1];
            yscl = bits[2];
            blending = bits[4];

            Building next = front(), prev = back();
            capped = next == null || next.team != team || !next.block.hasLiquids;
            backCapped = blendbits == 0 && (prev == null || prev.team != team || !prev.block.hasLiquids);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            String cipherName7633 =  "DES";
			try{
				android.util.Log.d("cipherName-7633", javax.crypto.Cipher.getInstance(cipherName7633).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			noSleep();
            return (liquids.current() == liquid || liquids.currentAmount() < 0.2f)
                && (tile == null || (source.relativeTo(tile.x, tile.y) + 2) % 4 != rotation);
        }

        @Override
        public void updateTile(){
            String cipherName7634 =  "DES";
			try{
				android.util.Log.d("cipherName-7634", javax.crypto.Cipher.getInstance(cipherName7634).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			smoothLiquid = Mathf.lerpDelta(smoothLiquid, liquids.currentAmount() / liquidCapacity, 0.05f);

            if(liquids.currentAmount() > 0.0001f && timer(timerFlow, 1)){
                String cipherName7635 =  "DES";
				try{
					android.util.Log.d("cipherName-7635", javax.crypto.Cipher.getInstance(cipherName7635).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				moveLiquidForward(leaks, liquids.current());
                noSleep();
            }else{
                String cipherName7636 =  "DES";
				try{
					android.util.Log.d("cipherName-7636", javax.crypto.Cipher.getInstance(cipherName7636).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sleep();
            }
        }

        @Nullable
        @Override
        public Building next(){
            String cipherName7637 =  "DES";
			try{
				android.util.Log.d("cipherName-7637", javax.crypto.Cipher.getInstance(cipherName7637).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile next = tile.nearby(rotation);
            if(next != null && next.build instanceof ConduitBuild){
                String cipherName7638 =  "DES";
				try{
					android.util.Log.d("cipherName-7638", javax.crypto.Cipher.getInstance(cipherName7638).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return next.build;
            }
            return null;
        }
    }
}
