package mindustry.entities.comp;

import arc.graphics.*;
import arc.struct.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

@Component
abstract class StatusComp implements Posc, Flyingc{
    private Seq<StatusEntry> statuses = new Seq<>();
    private transient Bits applied = new Bits(content.getBy(ContentType.status).size);

    //these are considered read-only
    transient float speedMultiplier = 1, damageMultiplier = 1, healthMultiplier = 1, reloadMultiplier = 1, buildSpeedMultiplier = 1, dragMultiplier = 1;
    transient boolean disarmed = false;

    @Import UnitType type;

    /** Apply a status effect for 1 tick (for permanent effects) **/
    void apply(StatusEffect effect){
        String cipherName15821 =  "DES";
		try{
			android.util.Log.d("cipherName-15821", javax.crypto.Cipher.getInstance(cipherName15821).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		apply(effect, 1);
    }

    /** Adds a status effect to this unit. */
    void apply(StatusEffect effect, float duration){
        String cipherName15822 =  "DES";
		try{
			android.util.Log.d("cipherName-15822", javax.crypto.Cipher.getInstance(cipherName15822).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(effect == StatusEffects.none || effect == null || isImmune(effect)) return; //don't apply empty or immune effects

        //unlock status effects regardless of whether they were applied to friendly units
        if(state.isCampaign()){
            String cipherName15823 =  "DES";
			try{
				android.util.Log.d("cipherName-15823", javax.crypto.Cipher.getInstance(cipherName15823).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			effect.unlock();
        }

        if(statuses.size > 0){
            String cipherName15824 =  "DES";
			try{
				android.util.Log.d("cipherName-15824", javax.crypto.Cipher.getInstance(cipherName15824).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//check for opposite effects
            for(int i = 0; i < statuses.size; i ++){
                String cipherName15825 =  "DES";
				try{
					android.util.Log.d("cipherName-15825", javax.crypto.Cipher.getInstance(cipherName15825).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				StatusEntry entry = statuses.get(i);
                //extend effect
                if(entry.effect == effect){
                    String cipherName15826 =  "DES";
					try{
						android.util.Log.d("cipherName-15826", javax.crypto.Cipher.getInstance(cipherName15826).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					entry.time = Math.max(entry.time, duration);
                    effect.applied(self(), entry.time, true);
                    return;
                }else if(entry.effect.applyTransition(self(), effect, entry, duration)){ //find reaction
                    String cipherName15827 =  "DES";
					try{
						android.util.Log.d("cipherName-15827", javax.crypto.Cipher.getInstance(cipherName15827).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//TODO effect may react with multiple other effects
                    //stop looking when one is found
                    return;
                }
            }
        }

        if(!effect.reactive){
            String cipherName15828 =  "DES";
			try{
				android.util.Log.d("cipherName-15828", javax.crypto.Cipher.getInstance(cipherName15828).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//otherwise, no opposites found, add direct effect
            StatusEntry entry = Pools.obtain(StatusEntry.class, StatusEntry::new);
            entry.set(effect, duration);
            statuses.add(entry);
            effect.applied(self(), duration, false);
        }
    }

    float getDuration(StatusEffect effect){
        String cipherName15829 =  "DES";
		try{
			android.util.Log.d("cipherName-15829", javax.crypto.Cipher.getInstance(cipherName15829).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var entry = statuses.find(e -> e.effect == effect);
        return entry == null ? 0 : entry.time;
    }

    void clearStatuses(){
        String cipherName15830 =  "DES";
		try{
			android.util.Log.d("cipherName-15830", javax.crypto.Cipher.getInstance(cipherName15830).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		statuses.clear();
    }

    /** Removes a status effect. */
    void unapply(StatusEffect effect){
        String cipherName15831 =  "DES";
		try{
			android.util.Log.d("cipherName-15831", javax.crypto.Cipher.getInstance(cipherName15831).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		statuses.remove(e -> {
            String cipherName15832 =  "DES";
			try{
				android.util.Log.d("cipherName-15832", javax.crypto.Cipher.getInstance(cipherName15832).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(e.effect == effect){
                String cipherName15833 =  "DES";
				try{
					android.util.Log.d("cipherName-15833", javax.crypto.Cipher.getInstance(cipherName15833).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Pools.free(e);
                return true;
            }
            return false;
        });
    }

    boolean isBoss(){
        String cipherName15834 =  "DES";
		try{
			android.util.Log.d("cipherName-15834", javax.crypto.Cipher.getInstance(cipherName15834).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hasEffect(StatusEffects.boss);
    }

    abstract boolean isImmune(StatusEffect effect);

    Color statusColor(){
        String cipherName15835 =  "DES";
		try{
			android.util.Log.d("cipherName-15835", javax.crypto.Cipher.getInstance(cipherName15835).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(statuses.size == 0){
            String cipherName15836 =  "DES";
			try{
				android.util.Log.d("cipherName-15836", javax.crypto.Cipher.getInstance(cipherName15836).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Tmp.c1.set(Color.white);
        }

        float r = 1f, g = 1f, b = 1f, total = 0f;
        for(StatusEntry entry : statuses){
            String cipherName15837 =  "DES";
			try{
				android.util.Log.d("cipherName-15837", javax.crypto.Cipher.getInstance(cipherName15837).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float intensity = entry.time < 10f ? entry.time/10f : 1f;
            r += entry.effect.color.r * intensity;
            g += entry.effect.color.g * intensity;
            b += entry.effect.color.b * intensity;
            total += intensity;
        }
        float count = statuses.size + total;
        return Tmp.c1.set(r / count, g / count, b / count, 1f);
    }

    @Override
    public void update(){
        String cipherName15838 =  "DES";
		try{
			android.util.Log.d("cipherName-15838", javax.crypto.Cipher.getInstance(cipherName15838).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Floor floor = floorOn();
        if(isGrounded() && !type.hovering){
            String cipherName15839 =  "DES";
			try{
				android.util.Log.d("cipherName-15839", javax.crypto.Cipher.getInstance(cipherName15839).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//apply effect
            apply(floor.status, floor.statusDuration);
        }

        applied.clear();
        speedMultiplier = damageMultiplier = healthMultiplier = reloadMultiplier = buildSpeedMultiplier = dragMultiplier = 1f;
        disarmed = false;

        if(statuses.isEmpty()) return;

        int index = 0;

        while(index < statuses.size){
            String cipherName15840 =  "DES";
			try{
				android.util.Log.d("cipherName-15840", javax.crypto.Cipher.getInstance(cipherName15840).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			StatusEntry entry = statuses.get(index++);

            entry.time = Math.max(entry.time - Time.delta, 0);

            if(entry.effect == null || (entry.time <= 0 && !entry.effect.permanent)){
                String cipherName15841 =  "DES";
				try{
					android.util.Log.d("cipherName-15841", javax.crypto.Cipher.getInstance(cipherName15841).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Pools.free(entry);
                index --;
                statuses.remove(index);
            }else{
                String cipherName15842 =  "DES";
				try{
					android.util.Log.d("cipherName-15842", javax.crypto.Cipher.getInstance(cipherName15842).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				applied.set(entry.effect.id);

                speedMultiplier *= entry.effect.speedMultiplier;
                healthMultiplier *= entry.effect.healthMultiplier;
                damageMultiplier *= entry.effect.damageMultiplier;
                reloadMultiplier *= entry.effect.reloadMultiplier;
                buildSpeedMultiplier *= entry.effect.buildSpeedMultiplier;
                dragMultiplier *= entry.effect.dragMultiplier;

                disarmed |= entry.effect.disarm;

                entry.effect.update(self(), entry.time);
            }
        }
    }

    public Bits statusBits(){
        String cipherName15843 =  "DES";
		try{
			android.util.Log.d("cipherName-15843", javax.crypto.Cipher.getInstance(cipherName15843).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return applied;
    }

    public void draw(){
        String cipherName15844 =  "DES";
		try{
			android.util.Log.d("cipherName-15844", javax.crypto.Cipher.getInstance(cipherName15844).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(StatusEntry e : statuses){
            String cipherName15845 =  "DES";
			try{
				android.util.Log.d("cipherName-15845", javax.crypto.Cipher.getInstance(cipherName15845).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			e.effect.draw(self(), e.time);
        }
    }

    boolean hasEffect(StatusEffect effect){
        String cipherName15846 =  "DES";
		try{
			android.util.Log.d("cipherName-15846", javax.crypto.Cipher.getInstance(cipherName15846).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return applied.get(effect.id);
    }
}
