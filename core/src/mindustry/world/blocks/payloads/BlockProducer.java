package mindustry.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;

import static mindustry.Vars.*;

/** Generic building that produces other buildings. */
public abstract class BlockProducer extends PayloadBlock{
    public float buildSpeed = 0.4f;

    public BlockProducer(String name){
        super(name);
		String cipherName6843 =  "DES";
		try{
			android.util.Log.d("cipherName-6843", javax.crypto.Cipher.getInstance(cipherName6843).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        size = 3;
        update = true;
        outputsPayload = true;
        hasItems = true;
        solid = true;
        hasPower = true;
        rotate = true;
        regionRotated1 = 1;

        consume(new ConsumeItemDynamic((BlockProducerBuild e) -> e.recipe() != null ? e.recipe().requirements : ItemStack.empty));
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName6844 =  "DES";
		try{
			android.util.Log.d("cipherName-6844", javax.crypto.Cipher.getInstance(cipherName6844).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{region, outRegion, topRegion};
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName6845 =  "DES";
		try{
			android.util.Log.d("cipherName-6845", javax.crypto.Cipher.getInstance(cipherName6845).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.rect(region, plan.drawx(), plan.drawy());
        Draw.rect(outRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.rect(topRegion, plan.drawx(), plan.drawy());
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName6846 =  "DES";
		try{
			android.util.Log.d("cipherName-6846", javax.crypto.Cipher.getInstance(cipherName6846).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("progress", (BlockProducerBuild entity) -> new Bar("bar.progress", Pal.ammo, () -> entity.recipe() == null ? 0f : (entity.progress / entity.recipe().buildCost)));
    }
    
    public abstract class BlockProducerBuild extends PayloadBlockBuild<BuildPayload>{
        public float progress, time, heat;

        public abstract @Nullable Block recipe();

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName6847 =  "DES";
			try{
				android.util.Log.d("cipherName-6847", javax.crypto.Cipher.getInstance(cipherName6847).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public int getMaximumAccepted(Item item){
            String cipherName6848 =  "DES";
			try{
				android.util.Log.d("cipherName-6848", javax.crypto.Cipher.getInstance(cipherName6848).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(recipe() == null) return 0;
            for(ItemStack stack : recipe().requirements){
                String cipherName6849 =  "DES";
				try{
					android.util.Log.d("cipherName-6849", javax.crypto.Cipher.getInstance(cipherName6849).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(stack.item == item) return stack.amount * 2;
            }
            return 0;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            String cipherName6850 =  "DES";
			try{
				android.util.Log.d("cipherName-6850", javax.crypto.Cipher.getInstance(cipherName6850).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public boolean shouldConsume(){
            String cipherName6851 =  "DES";
			try{
				android.util.Log.d("cipherName-6851", javax.crypto.Cipher.getInstance(cipherName6851).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return super.shouldConsume() && recipe() != null;
        }

        @Override
        public void updateTile(){
            super.updateTile();
			String cipherName6852 =  "DES";
			try{
				android.util.Log.d("cipherName-6852", javax.crypto.Cipher.getInstance(cipherName6852).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            var recipe = recipe();
            boolean produce = recipe != null && efficiency > 0 && payload == null;

            if(produce){
                String cipherName6853 =  "DES";
				try{
					android.util.Log.d("cipherName-6853", javax.crypto.Cipher.getInstance(cipherName6853).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				progress += buildSpeed * edelta();

                if(progress >= recipe.buildCost){
                    String cipherName6854 =  "DES";
					try{
						android.util.Log.d("cipherName-6854", javax.crypto.Cipher.getInstance(cipherName6854).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					consume();
                    payload = new BuildPayload(recipe, team);
                    payload.block().placeEffect.at(x, y, payload.size() / tilesize);
                    payVector.setZero();
                    progress %= 1f;
                }
            }

            heat = Mathf.lerpDelta(heat, Mathf.num(produce), 0.15f);
            time += heat * delta();

            moveOutPayload();
        }

        @Override
        public void draw(){
            String cipherName6855 =  "DES";
			try{
				android.util.Log.d("cipherName-6855", javax.crypto.Cipher.getInstance(cipherName6855).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(region, x, y);
            Draw.rect(outRegion, x, y, rotdeg());

            var recipe = recipe();
            if(recipe != null){
                String cipherName6856 =  "DES";
				try{
					android.util.Log.d("cipherName-6856", javax.crypto.Cipher.getInstance(cipherName6856).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Drawf.shadow(x, y, recipe.size * tilesize * 2f, progress / recipe.buildCost);
                Draw.draw(Layer.blockBuilding, () -> {
                    String cipherName6857 =  "DES";
					try{
						android.util.Log.d("cipherName-6857", javax.crypto.Cipher.getInstance(cipherName6857).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.color(Pal.accent);

                    for(TextureRegion region : recipe.getGeneratedIcons()){
                        String cipherName6858 =  "DES";
						try{
							android.util.Log.d("cipherName-6858", javax.crypto.Cipher.getInstance(cipherName6858).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Shaders.blockbuild.region = region;
                        Shaders.blockbuild.time = time;
                        Shaders.blockbuild.progress = progress / recipe.buildCost;

                        Draw.rect(region, x, y, recipe.rotate ? rotdeg() : 0);
                        Draw.flush();
                    }

                    Draw.color();
                });
                Draw.z(Layer.blockBuilding + 1);
                Draw.color(Pal.accent, heat);

                Lines.lineAngleCenter(x + Mathf.sin(time, 10f, Vars.tilesize / 2f * recipe.size + 1f), y, 90, recipe.size * Vars.tilesize + 1f);

                Draw.reset();
            }

            drawPayload();

            Draw.z(Layer.blockBuilding + 1.1f);
            Draw.rect(topRegion, x, y);
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName6859 =  "DES";
			try{
				android.util.Log.d("cipherName-6859", javax.crypto.Cipher.getInstance(cipherName6859).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.progress) return progress;
            return super.sense(sensor);
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName6860 =  "DES";
			try{
				android.util.Log.d("cipherName-6860", javax.crypto.Cipher.getInstance(cipherName6860).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName6861 =  "DES";
			try{
				android.util.Log.d("cipherName-6861", javax.crypto.Cipher.getInstance(cipherName6861).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            progress = read.f();
        }
    }
}
