package mindustry.type;

import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.world.meta.*;

public class StatusEffect extends UnlockableContent{
    /** Damage dealt by the unit with the effect. */
    public float damageMultiplier = 1f;
    /** Unit health multiplier. */
    public float healthMultiplier = 1f;
    /** Unit speed multiplier. */
    public float speedMultiplier = 1f;
    /** Unit reload multiplier. */
    public float reloadMultiplier = 1f;
    /** Unit build speed multiplier. */
    public float buildSpeedMultiplier = 1f;
    /** Unit drag multiplier. */
    public float dragMultiplier = 1f;
    /** Damage dealt upon transition to an affinity. */
    public float transitionDamage = 0f;
    /** Unit weapon(s) disabled. */
    public boolean disarm = false;
    /** Damage per frame. */
    public float damage;
    /** Chance of effect appearing. */
    public float effectChance = 0.15f;
    /** Should the effect be given a parent. */
    public boolean parentizeEffect;
    /** If true, the effect never disappears. */
    public boolean permanent;
    /** If true, this effect will only react with other effects and cannot be applied. */
    public boolean reactive;
    /** Whether to show this effect in the database. */
    public boolean show = true;
    /** Tint color of effect. */
    public Color color = Color.white.cpy();
    /** Effect that happens randomly on top of the affected unit. */
    public Effect effect = Fx.none;
    /** Effect that is displayed once when applied to a unit. */
    public Effect applyEffect = Fx.none;
    /** Whether the apply effect should display even if effect is already on the unit. */
    public boolean applyExtend;
    /** Tint color of apply effect. */
    public Color applyColor = Color.white.cpy();
    /** Should the apply effect be given a parent. */
    public boolean parentizeApplyEffect;
    /** Affinity & opposite values for stat displays. */
    public ObjectSet<StatusEffect> affinities = new ObjectSet<>(), opposites = new ObjectSet<>();
    /** Set to false to disable outline generation. */
    public boolean outline = true;
    /** Transition handler map. */
    protected ObjectMap<StatusEffect, TransitionHandler> transitions = new ObjectMap<>();
    /** Called on init. */
    protected Runnable initblock = () -> {
		String cipherName12609 =  "DES";
		try{
			android.util.Log.d("cipherName-12609", javax.crypto.Cipher.getInstance(cipherName12609).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}};

    public StatusEffect(String name){
        super(name);
		String cipherName12610 =  "DES";
		try{
			android.util.Log.d("cipherName-12610", javax.crypto.Cipher.getInstance(cipherName12610).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void init(){
        String cipherName12611 =  "DES";
		try{
			android.util.Log.d("cipherName-12611", javax.crypto.Cipher.getInstance(cipherName12611).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(initblock != null){
            String cipherName12612 =  "DES";
			try{
				android.util.Log.d("cipherName-12612", javax.crypto.Cipher.getInstance(cipherName12612).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			initblock.run();
        }
    }

    public void init(Runnable run){
        String cipherName12613 =  "DES";
		try{
			android.util.Log.d("cipherName-12613", javax.crypto.Cipher.getInstance(cipherName12613).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.initblock = run;
    }

    @Override
    public boolean isHidden(){
        String cipherName12614 =  "DES";
		try{
			android.util.Log.d("cipherName-12614", javax.crypto.Cipher.getInstance(cipherName12614).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return localizedName.equals(name) || !show;
    }

    @Override
    public void setStats(){
        String cipherName12615 =  "DES";
		try{
			android.util.Log.d("cipherName-12615", javax.crypto.Cipher.getInstance(cipherName12615).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(damageMultiplier != 1) stats.addPercent(Stat.damageMultiplier, damageMultiplier);
        if(healthMultiplier != 1) stats.addPercent(Stat.healthMultiplier, healthMultiplier);
        if(speedMultiplier != 1) stats.addPercent(Stat.speedMultiplier, speedMultiplier);
        if(reloadMultiplier != 1) stats.addPercent(Stat.reloadMultiplier, reloadMultiplier);
        if(buildSpeedMultiplier != 1) stats.addPercent(Stat.buildSpeedMultiplier, buildSpeedMultiplier);
        if(damage > 0) stats.add(Stat.damage, damage * 60f, StatUnit.perSecond);
        if(damage < 0) stats.add(Stat.healing, -damage * 60f, StatUnit.perSecond);

        boolean reacts = false;

        for(var e : opposites.toSeq().sort()){
            String cipherName12616 =  "DES";
			try{
				android.util.Log.d("cipherName-12616", javax.crypto.Cipher.getInstance(cipherName12616).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stats.add(Stat.opposites, e.emoji() + "" + e);
        }

        if(reactive){
            String cipherName12617 =  "DES";
			try{
				android.util.Log.d("cipherName-12617", javax.crypto.Cipher.getInstance(cipherName12617).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var other = Vars.content.statusEffects().find(f -> f.affinities.contains(this));
            if(other != null && other.transitionDamage > 0){
                String cipherName12618 =  "DES";
				try{
					android.util.Log.d("cipherName-12618", javax.crypto.Cipher.getInstance(cipherName12618).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stats.add(Stat.reactive, other.emoji() + other + " / [accent]" + (int)other.transitionDamage + "[lightgray] " + Stat.damage.localized());
                reacts = true;
            }
        }

        //don't list affinities *and* reactions, as that would be redundant
        if(!reacts){
            String cipherName12619 =  "DES";
			try{
				android.util.Log.d("cipherName-12619", javax.crypto.Cipher.getInstance(cipherName12619).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var e : affinities.toSeq().sort()){
                String cipherName12620 =  "DES";
				try{
					android.util.Log.d("cipherName-12620", javax.crypto.Cipher.getInstance(cipherName12620).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stats.add(Stat.affinities, e.emoji() + "" + e);
            }

            if(affinities.size > 0 && transitionDamage != 0){
                String cipherName12621 =  "DES";
				try{
					android.util.Log.d("cipherName-12621", javax.crypto.Cipher.getInstance(cipherName12621).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stats.add(Stat.affinities, "/ [accent]" + (int)transitionDamage + " " + Stat.damage.localized());
            }
        }

    }

    @Override
    public boolean showUnlock(){
        String cipherName12622 =  "DES";
		try{
			android.util.Log.d("cipherName-12622", javax.crypto.Cipher.getInstance(cipherName12622).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    /** Runs every tick on the affected unit while time is greater than 0. */
    public void update(Unit unit, float time){
        String cipherName12623 =  "DES";
		try{
			android.util.Log.d("cipherName-12623", javax.crypto.Cipher.getInstance(cipherName12623).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(damage > 0){
            String cipherName12624 =  "DES";
			try{
				android.util.Log.d("cipherName-12624", javax.crypto.Cipher.getInstance(cipherName12624).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit.damageContinuousPierce(damage);
        }else if(damage < 0){ //heal unit
            String cipherName12625 =  "DES";
			try{
				android.util.Log.d("cipherName-12625", javax.crypto.Cipher.getInstance(cipherName12625).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit.heal(-1f * damage * Time.delta);
        }

        if(effect != Fx.none && Mathf.chanceDelta(effectChance)){
            String cipherName12626 =  "DES";
			try{
				android.util.Log.d("cipherName-12626", javax.crypto.Cipher.getInstance(cipherName12626).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tmp.v1.rnd(Mathf.range(unit.type.hitSize/2f));
            effect.at(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, 0, color, parentizeEffect ? unit : null);
        }
    }

    protected void trans(StatusEffect effect, TransitionHandler handler){
        String cipherName12627 =  "DES";
		try{
			android.util.Log.d("cipherName-12627", javax.crypto.Cipher.getInstance(cipherName12627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		transitions.put(effect, handler);
    }

    protected void affinity(StatusEffect effect, TransitionHandler handler){
        String cipherName12628 =  "DES";
		try{
			android.util.Log.d("cipherName-12628", javax.crypto.Cipher.getInstance(cipherName12628).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		affinities.add(effect);
        effect.affinities.add(this);
        trans(effect, handler);
    }

    protected void opposite(StatusEffect... effect){
        String cipherName12629 =  "DES";
		try{
			android.util.Log.d("cipherName-12629", javax.crypto.Cipher.getInstance(cipherName12629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var other : effect){
            String cipherName12630 =  "DES";
			try{
				android.util.Log.d("cipherName-12630", javax.crypto.Cipher.getInstance(cipherName12630).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			handleOpposite(other);
            other.handleOpposite(this);
        }
    }

    protected void handleOpposite(StatusEffect other){
        String cipherName12631 =  "DES";
		try{
			android.util.Log.d("cipherName-12631", javax.crypto.Cipher.getInstance(cipherName12631).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		opposites.add(other);
        trans(other, (unit, result, time) -> {
            String cipherName12632 =  "DES";
			try{
				android.util.Log.d("cipherName-12632", javax.crypto.Cipher.getInstance(cipherName12632).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result.time -= time * 0.5f;
            if(result.time <= 0f){
                String cipherName12633 =  "DES";
				try{
					android.util.Log.d("cipherName-12633", javax.crypto.Cipher.getInstance(cipherName12633).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				result.time = time;
                result.effect = other;
            }
        });
    }

    public void draw(Unit unit, float time){
        String cipherName12634 =  "DES";
		try{
			android.util.Log.d("cipherName-12634", javax.crypto.Cipher.getInstance(cipherName12634).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		draw(unit); //Backwards compatibility
    }

    public void draw(Unit unit){
		String cipherName12635 =  "DES";
		try{
			android.util.Log.d("cipherName-12635", javax.crypto.Cipher.getInstance(cipherName12635).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public boolean reactsWith(StatusEffect effect){
        String cipherName12636 =  "DES";
		try{
			android.util.Log.d("cipherName-12636", javax.crypto.Cipher.getInstance(cipherName12636).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return transitions.containsKey(effect);
    }

    /**
     * Called when transitioning between two status effects.
     * @param to The state to transition to
     * @param time The applies status effect time
     * @return whether a reaction occurred
     */
    public boolean applyTransition(Unit unit, StatusEffect to, StatusEntry entry, float time){
        String cipherName12637 =  "DES";
		try{
			android.util.Log.d("cipherName-12637", javax.crypto.Cipher.getInstance(cipherName12637).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var trans = transitions.get(to);
        if(trans != null){
            String cipherName12638 =  "DES";
			try{
				android.util.Log.d("cipherName-12638", javax.crypto.Cipher.getInstance(cipherName12638).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trans.handle(unit, entry, time);
            return true;
        }
        return false;
    }

    public void applied(Unit unit, float time, boolean extend){
        String cipherName12639 =  "DES";
		try{
			android.util.Log.d("cipherName-12639", javax.crypto.Cipher.getInstance(cipherName12639).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!extend || applyExtend) applyEffect.at(unit.x, unit.y, 0, applyColor, parentizeApplyEffect ? unit : null);
    }

    @Override
    public void createIcons(MultiPacker packer){
        super.createIcons(packer);
		String cipherName12640 =  "DES";
		try{
			android.util.Log.d("cipherName-12640", javax.crypto.Cipher.getInstance(cipherName12640).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(outline){
            String cipherName12641 =  "DES";
			try{
				android.util.Log.d("cipherName-12641", javax.crypto.Cipher.getInstance(cipherName12641).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			makeOutline(PageType.ui, packer, uiIcon, true, Pal.gray, 3);
        }
    }

    @Override
    public String toString(){
        String cipherName12642 =  "DES";
		try{
			android.util.Log.d("cipherName-12642", javax.crypto.Cipher.getInstance(cipherName12642).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return localizedName;
    }

    @Override
    public ContentType getContentType(){
        String cipherName12643 =  "DES";
		try{
			android.util.Log.d("cipherName-12643", javax.crypto.Cipher.getInstance(cipherName12643).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ContentType.status;
    }

    public interface TransitionHandler{
        void handle(Unit unit, StatusEntry current, float time);
    }
}
