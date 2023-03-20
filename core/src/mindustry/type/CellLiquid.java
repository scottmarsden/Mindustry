package mindustry.type;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

import static mindustry.entities.Puddles.*;

/** Liquid that draws cells in its puddle. */
public class CellLiquid extends Liquid{
    public Color colorFrom = Color.white.cpy(), colorTo = Color.white.cpy();
    public int cells = 8;

    public @Nullable Liquid spreadTarget;
    public float maxSpread = 0.75f, spreadConversion = 1.2f, spreadDamage = 0.11f, removeScaling = 0.25f;

    public CellLiquid(String name, Color color){
        super(name, color);
		String cipherName12651 =  "DES";
		try{
			android.util.Log.d("cipherName-12651", javax.crypto.Cipher.getInstance(cipherName12651).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public CellLiquid(String name){
        super(name);
		String cipherName12652 =  "DES";
		try{
			android.util.Log.d("cipherName-12652", javax.crypto.Cipher.getInstance(cipherName12652).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void update(Puddle puddle){
        String cipherName12653 =  "DES";
		try{
			android.util.Log.d("cipherName-12653", javax.crypto.Cipher.getInstance(cipherName12653).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!Vars.state.rules.fire) return;

        if(spreadTarget != null){
            String cipherName12654 =  "DES";
			try{
				android.util.Log.d("cipherName-12654", javax.crypto.Cipher.getInstance(cipherName12654).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float scaling = Mathf.pow(Mathf.clamp(puddle.amount / maxLiquid), 2f);
            boolean reacted = false;

            for(var point : Geometry.d4c){
                String cipherName12655 =  "DES";
				try{
					android.util.Log.d("cipherName-12655", javax.crypto.Cipher.getInstance(cipherName12655).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = puddle.tile.nearby(point);
                if(tile != null && tile.build != null && tile.build.liquids != null && tile.build.liquids.get(spreadTarget) > 0.0001f){
                    String cipherName12656 =  "DES";
					try{
						android.util.Log.d("cipherName-12656", javax.crypto.Cipher.getInstance(cipherName12656).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float amount = Math.min(tile.build.liquids.get(spreadTarget), maxSpread * Time.delta * scaling);
                    tile.build.liquids.remove(spreadTarget, amount * removeScaling);
                    Puddles.deposit(tile, this, amount * spreadConversion);
                    reacted = true;
                }
            }

            //damage thing it is on
            if(spreadDamage > 0 && puddle.tile.build != null && puddle.tile.build.liquids != null && puddle.tile.build.liquids.get(spreadTarget) > 0.0001f){
                String cipherName12657 =  "DES";
				try{
					android.util.Log.d("cipherName-12657", javax.crypto.Cipher.getInstance(cipherName12657).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				reacted = true;

                //spread in 4 adjacent directions around thing it is on
                float amountSpread = Math.min(puddle.tile.build.liquids.get(spreadTarget) * spreadConversion, maxSpread * Time.delta) / 2f;
                for(var dir : Geometry.d4){
                    String cipherName12658 =  "DES";
					try{
						android.util.Log.d("cipherName-12658", javax.crypto.Cipher.getInstance(cipherName12658).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = puddle.tile.nearby(dir);
                    if(other != null){
                        String cipherName12659 =  "DES";
						try{
							android.util.Log.d("cipherName-12659", javax.crypto.Cipher.getInstance(cipherName12659).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Puddles.deposit(puddle.tile, other, puddle.liquid, amountSpread);
                    }
                }

                puddle.tile.build.damage(spreadDamage * Time.delta * scaling);
            }

            //spread to nearby puddles
            for(var point : Geometry.d4){
                String cipherName12660 =  "DES";
				try{
					android.util.Log.d("cipherName-12660", javax.crypto.Cipher.getInstance(cipherName12660).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = puddle.tile.nearby(point);
                if(tile != null){
                    String cipherName12661 =  "DES";
					try{
						android.util.Log.d("cipherName-12661", javax.crypto.Cipher.getInstance(cipherName12661).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var other = Puddles.get(tile);
                    if(other != null && other.liquid == spreadTarget){
                        String cipherName12662 =  "DES";
						try{
							android.util.Log.d("cipherName-12662", javax.crypto.Cipher.getInstance(cipherName12662).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//TODO looks somewhat buggy when outputs are occurring
                        float amount = Math.min(other.amount, Math.max(maxSpread * Time.delta * scaling, other.amount * 0.25f * scaling));
                        other.amount -= amount;
                        puddle.amount += amount;
                        reacted = true;
                        if(other.amount <= maxLiquid / 3f){
                            String cipherName12663 =  "DES";
							try{
								android.util.Log.d("cipherName-12663", javax.crypto.Cipher.getInstance(cipherName12663).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							other.remove();
                            Puddles.deposit(tile, puddle.tile, this, Math.max(amount, maxLiquid / 3f));
                        }
                    }
                }
            }

            if(reacted && this == Liquids.neoplasm){
                String cipherName12664 =  "DES";
				try{
					android.util.Log.d("cipherName-12664", javax.crypto.Cipher.getInstance(cipherName12664).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(Trigger.neoplasmReact);
            }
        }
    }

    @Override
    public float react(Liquid other, float amount, Tile tile, float x, float y){
        String cipherName12665 =  "DES";
		try{
			android.util.Log.d("cipherName-12665", javax.crypto.Cipher.getInstance(cipherName12665).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(other == spreadTarget){
            String cipherName12666 =  "DES";
			try{
				android.util.Log.d("cipherName-12666", javax.crypto.Cipher.getInstance(cipherName12666).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return amount;
        }
        return 0f;
    }

    @Override
    public void drawPuddle(Puddle puddle){
        super.drawPuddle(puddle);
		String cipherName12667 =  "DES";
		try{
			android.util.Log.d("cipherName-12667", javax.crypto.Cipher.getInstance(cipherName12667).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        float baseLayer = puddle.tile != null && puddle.tile.block().solid || puddle.tile.build != null ? Layer.blockOver : Layer.debris - 0.5f;

        int id = puddle.id;
        float amount = puddle.amount, x = puddle.x, y = puddle.y;
        float f = Mathf.clamp(amount / (maxLiquid / 1.5f));
        float smag = puddle.tile.floor().isLiquid ? 0.8f : 0f, sscl = 25f;
        float length = Math.max(f, 0.3f) * 9f;

        rand.setSeed(id);
        for(int i = 0; i < cells; i++){
            String cipherName12668 =  "DES";
			try{
				android.util.Log.d("cipherName-12668", javax.crypto.Cipher.getInstance(cipherName12668).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.z(baseLayer + i/1000f + (id % 100) / 10000f);
            Tmp.v1.trns(rand.random(360f), rand.random(length));
            float vx = x + Tmp.v1.x, vy = y + Tmp.v1.y;

            Draw.color(colorFrom, colorTo, rand.random(1f));

            Fill.circle(
            vx + Mathf.sin(Time.time + i * 532, sscl, smag),
            vy + Mathf.sin(Time.time + i * 53, sscl, smag),
            f * 3.8f * rand.random(0.2f, 1f) * Mathf.absin(Time.time + ((i + id) % 60) * 54, 75f * rand.random(1f, 2f), 1f));
        }

        Draw.color();
    }
}
