package mindustry.world.blocks.power;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

/** A generator that just takes in certain items or liquids. */
public class ConsumeGenerator extends PowerGenerator{
    /** The time in number of ticks during which a single item will produce power. */
    public float itemDuration = 120f;

    public float warmupSpeed = 0.05f;
    public float effectChance = 0.01f;
    public Effect generateEffect = Fx.none, consumeEffect = Fx.none;
    public float generateEffectRange = 3f;

    public @Nullable LiquidStack outputLiquid;
    /** If true, this block explodes when outputLiquid exceeds capacity. */
    public boolean explodeOnFull = false;

    public @Nullable ConsumeItemFilter filterItem;
    public @Nullable ConsumeLiquidFilter filterLiquid;

    public ConsumeGenerator(String name){
        super(name);
		String cipherName6312 =  "DES";
		try{
			android.util.Log.d("cipherName-6312", javax.crypto.Cipher.getInstance(cipherName6312).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName6313 =  "DES";
		try{
			android.util.Log.d("cipherName-6313", javax.crypto.Cipher.getInstance(cipherName6313).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(outputLiquid != null){
            String cipherName6314 =  "DES";
			try{
				android.util.Log.d("cipherName-6314", javax.crypto.Cipher.getInstance(cipherName6314).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addLiquidBar(outputLiquid.liquid);
        }
    }

    @Override
    public void init(){
        filterItem = findConsumer(c -> c instanceof ConsumeItemFilter);
		String cipherName6315 =  "DES";
		try{
			android.util.Log.d("cipherName-6315", javax.crypto.Cipher.getInstance(cipherName6315).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        filterLiquid = findConsumer(c -> c instanceof ConsumeLiquidFilter);

        if(outputLiquid != null){
            String cipherName6316 =  "DES";
			try{
				android.util.Log.d("cipherName-6316", javax.crypto.Cipher.getInstance(cipherName6316).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			outputsLiquid = true;
            hasLiquids = true;
        }

        if(explodeOnFull && outputLiquid != null && explosionPuddleLiquid == null){
            String cipherName6317 =  "DES";
			try{
				android.util.Log.d("cipherName-6317", javax.crypto.Cipher.getInstance(cipherName6317).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			explosionPuddleLiquid = outputLiquid.liquid;
        }

        //TODO hardcoded
        emitLight = true;
        lightRadius = 65f * size;
        super.init();
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName6318 =  "DES";
		try{
			android.util.Log.d("cipherName-6318", javax.crypto.Cipher.getInstance(cipherName6318).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(hasItems){
            String cipherName6319 =  "DES";
			try{
				android.util.Log.d("cipherName-6319", javax.crypto.Cipher.getInstance(cipherName6319).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stats.add(Stat.productionTime, itemDuration / 60f, StatUnit.seconds);
        }

        if(outputLiquid != null){
            String cipherName6320 =  "DES";
			try{
				android.util.Log.d("cipherName-6320", javax.crypto.Cipher.getInstance(cipherName6320).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stats.add(Stat.output, StatValues.liquid(outputLiquid.liquid, outputLiquid.amount * 60f, true));
        }
    }

    public class ConsumeGeneratorBuild extends GeneratorBuild{
        public float warmup, totalTime, efficiencyMultiplier = 1f;

        @Override
        public void updateEfficiencyMultiplier(){
            String cipherName6321 =  "DES";
			try{
				android.util.Log.d("cipherName-6321", javax.crypto.Cipher.getInstance(cipherName6321).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(filterItem != null){
                String cipherName6322 =  "DES";
				try{
					android.util.Log.d("cipherName-6322", javax.crypto.Cipher.getInstance(cipherName6322).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float m = filterItem.efficiencyMultiplier(this);
                if(m > 0) efficiencyMultiplier = m;
            }else if(filterLiquid != null){
                String cipherName6323 =  "DES";
				try{
					android.util.Log.d("cipherName-6323", javax.crypto.Cipher.getInstance(cipherName6323).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float m = filterLiquid.efficiencyMultiplier(this);
                if(m > 0) efficiencyMultiplier = m;
            }
        }

        @Override
        public void updateTile(){
            String cipherName6324 =  "DES";
			try{
				android.util.Log.d("cipherName-6324", javax.crypto.Cipher.getInstance(cipherName6324).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean valid = efficiency > 0;

            warmup = Mathf.lerpDelta(warmup, valid ? 1f : 0f, warmupSpeed);

            productionEfficiency = efficiency * efficiencyMultiplier;
            totalTime += warmup * Time.delta;

            //randomly produce the effect
            if(valid && Mathf.chanceDelta(effectChance)){
                String cipherName6325 =  "DES";
				try{
					android.util.Log.d("cipherName-6325", javax.crypto.Cipher.getInstance(cipherName6325).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				generateEffect.at(x + Mathf.range(generateEffectRange), y + Mathf.range(generateEffectRange));
            }

            //take in items periodically
            if(hasItems && valid && generateTime <= 0f){
                String cipherName6326 =  "DES";
				try{
					android.util.Log.d("cipherName-6326", javax.crypto.Cipher.getInstance(cipherName6326).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				consume();
                consumeEffect.at(x + Mathf.range(generateEffectRange), y + Mathf.range(generateEffectRange));
                generateTime = 1f;
            }

            if(outputLiquid != null){
                String cipherName6327 =  "DES";
				try{
					android.util.Log.d("cipherName-6327", javax.crypto.Cipher.getInstance(cipherName6327).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float added = Math.min(productionEfficiency * delta() * outputLiquid.amount, liquidCapacity - liquids.get(outputLiquid.liquid));
                liquids.add(outputLiquid.liquid, added);
                dumpLiquid(outputLiquid.liquid);

                if(explodeOnFull && liquids.get(outputLiquid.liquid) >= liquidCapacity - 0.0001f){
                    String cipherName6328 =  "DES";
					try{
						android.util.Log.d("cipherName-6328", javax.crypto.Cipher.getInstance(cipherName6328).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					kill();
                    Events.fire(new GeneratorPressureExplodeEvent(this));
                }
            }

            //generation time always goes down, but only at the end so consumeTriggerValid doesn't assume fake items
            generateTime -= delta() / itemDuration;
        }

        @Override
        public boolean consumeTriggerValid(){
            String cipherName6329 =  "DES";
			try{
				android.util.Log.d("cipherName-6329", javax.crypto.Cipher.getInstance(cipherName6329).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return generateTime > 0;
        }

        @Override
        public float warmup(){
            String cipherName6330 =  "DES";
			try{
				android.util.Log.d("cipherName-6330", javax.crypto.Cipher.getInstance(cipherName6330).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return warmup;
        }

        @Override
        public float totalProgress(){
            String cipherName6331 =  "DES";
			try{
				android.util.Log.d("cipherName-6331", javax.crypto.Cipher.getInstance(cipherName6331).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return totalTime;
        }

        @Override
        public void drawLight(){
            String cipherName6332 =  "DES";
			try{
				android.util.Log.d("cipherName-6332", javax.crypto.Cipher.getInstance(cipherName6332).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//???
            drawer.drawLight(this);
            //TODO hard coded
            Drawf.light(x, y, (60f + Mathf.absin(10f, 5f)) * size, Color.orange, 0.5f * warmup);
        }
    }
}
