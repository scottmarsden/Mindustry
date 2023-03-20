package mindustry.content;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.type.*;

import static mindustry.Vars.*;

public class StatusEffects{
    public static StatusEffect none, burning, freezing, unmoving, slow, wet, muddy, melting, sapped, tarred, overdrive, overclock, shielded, shocked, blasted, corroded, boss, sporeSlowed, disarmed, electrified, invincible;

    public static void load(){

        String cipherName11577 =  "DES";
		try{
			android.util.Log.d("cipherName-11577", javax.crypto.Cipher.getInstance(cipherName11577).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		none = new StatusEffect("none");

        burning = new StatusEffect("burning"){{
            String cipherName11578 =  "DES";
			try{
				android.util.Log.d("cipherName-11578", javax.crypto.Cipher.getInstance(cipherName11578).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Color.valueOf("ffc455");
            damage = 0.167f;
            effect = Fx.burning;
            transitionDamage = 8f;

            init(() -> {
                String cipherName11579 =  "DES";
				try{
					android.util.Log.d("cipherName-11579", javax.crypto.Cipher.getInstance(cipherName11579).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				opposite(wet, freezing);
                affinity(tarred, (unit, result, time) -> {
                    String cipherName11580 =  "DES";
					try{
						android.util.Log.d("cipherName-11580", javax.crypto.Cipher.getInstance(cipherName11580).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unit.damagePierce(transitionDamage);
                    Fx.burning.at(unit.x + Mathf.range(unit.bounds() / 2f), unit.y + Mathf.range(unit.bounds() / 2f));
                    result.set(burning, Math.min(time + result.time, 300f));
                });
            });
        }};

        freezing = new StatusEffect("freezing"){{
            String cipherName11581 =  "DES";
			try{
				android.util.Log.d("cipherName-11581", javax.crypto.Cipher.getInstance(cipherName11581).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Color.valueOf("6ecdec");
            speedMultiplier = 0.6f;
            healthMultiplier = 0.8f;
            effect = Fx.freezing;
            transitionDamage = 18f;

            init(() -> {
                String cipherName11582 =  "DES";
				try{
					android.util.Log.d("cipherName-11582", javax.crypto.Cipher.getInstance(cipherName11582).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				opposite(melting, burning);

                affinity(blasted, (unit, result, time) -> {
                    String cipherName11583 =  "DES";
					try{
						android.util.Log.d("cipherName-11583", javax.crypto.Cipher.getInstance(cipherName11583).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unit.damagePierce(transitionDamage);
                    if(unit.team == state.rules.waveTeam){
                        String cipherName11584 =  "DES";
						try{
							android.util.Log.d("cipherName-11584", javax.crypto.Cipher.getInstance(cipherName11584).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Events.fire(Trigger.blastFreeze);
                    }
                });
            });
        }};

        unmoving = new StatusEffect("unmoving"){{
            String cipherName11585 =  "DES";
			try{
				android.util.Log.d("cipherName-11585", javax.crypto.Cipher.getInstance(cipherName11585).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.gray;
            speedMultiplier = 0f;
        }};

        slow = new StatusEffect("slow"){{
            String cipherName11586 =  "DES";
			try{
				android.util.Log.d("cipherName-11586", javax.crypto.Cipher.getInstance(cipherName11586).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.lightishGray;
            speedMultiplier = 0.4f;
        }};

        wet = new StatusEffect("wet"){{
            String cipherName11587 =  "DES";
			try{
				android.util.Log.d("cipherName-11587", javax.crypto.Cipher.getInstance(cipherName11587).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Color.royal;
            speedMultiplier = 0.94f;
            effect = Fx.wet;
            effectChance = 0.09f;
            transitionDamage = 14;

            init(() -> {
                String cipherName11588 =  "DES";
				try{
					android.util.Log.d("cipherName-11588", javax.crypto.Cipher.getInstance(cipherName11588).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				affinity(shocked, (unit, result, time) -> {
                    String cipherName11589 =  "DES";
					try{
						android.util.Log.d("cipherName-11589", javax.crypto.Cipher.getInstance(cipherName11589).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unit.damagePierce(transitionDamage);
                    if(unit.team == state.rules.waveTeam){
                        String cipherName11590 =  "DES";
						try{
							android.util.Log.d("cipherName-11590", javax.crypto.Cipher.getInstance(cipherName11590).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Events.fire(Trigger.shock);
                    }
                });
                opposite(burning, melting);
            });
        }};
		
        muddy = new StatusEffect("muddy"){{
            String cipherName11591 =  "DES";
			try{
				android.util.Log.d("cipherName-11591", javax.crypto.Cipher.getInstance(cipherName11591).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Color.valueOf("46382a");
            speedMultiplier = 0.94f;
            effect = Fx.muddy;
            effectChance = 0.09f;
            show = false;
        }};

        melting = new StatusEffect("melting"){{
            String cipherName11592 =  "DES";
			try{
				android.util.Log.d("cipherName-11592", javax.crypto.Cipher.getInstance(cipherName11592).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Color.valueOf("ffa166");
            speedMultiplier = 0.8f;
            healthMultiplier = 0.8f;
            damage = 0.3f;
            effect = Fx.melting;

            init(() -> {
                String cipherName11593 =  "DES";
				try{
					android.util.Log.d("cipherName-11593", javax.crypto.Cipher.getInstance(cipherName11593).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				opposite(wet, freezing);
                affinity(tarred, (unit, result, time) -> {
                    String cipherName11594 =  "DES";
					try{
						android.util.Log.d("cipherName-11594", javax.crypto.Cipher.getInstance(cipherName11594).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unit.damagePierce(8f);
                    Fx.burning.at(unit.x + Mathf.range(unit.bounds() / 2f), unit.y + Mathf.range(unit.bounds() / 2f));
                    result.set(melting, Math.min(time + result.time, 200f));
                });
            });
        }};

        sapped = new StatusEffect("sapped"){{
            String cipherName11595 =  "DES";
			try{
				android.util.Log.d("cipherName-11595", javax.crypto.Cipher.getInstance(cipherName11595).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.sap;
            speedMultiplier = 0.7f;
            healthMultiplier = 0.8f;
            effect = Fx.sapped;
            effectChance = 0.1f;
        }};

        electrified = new StatusEffect("electrified"){{
            String cipherName11596 =  "DES";
			try{
				android.util.Log.d("cipherName-11596", javax.crypto.Cipher.getInstance(cipherName11596).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.heal;
            speedMultiplier = 0.7f;
            reloadMultiplier = 0.6f;
            effect = Fx.electrified;
            effectChance = 0.1f;
        }};

        sporeSlowed = new StatusEffect("spore-slowed"){{
            String cipherName11597 =  "DES";
			try{
				android.util.Log.d("cipherName-11597", javax.crypto.Cipher.getInstance(cipherName11597).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.spore;
            speedMultiplier = 0.8f;
            effect = Fx.sapped;
            effectChance = 0.04f;
        }};

        tarred = new StatusEffect("tarred"){{
            String cipherName11598 =  "DES";
			try{
				android.util.Log.d("cipherName-11598", javax.crypto.Cipher.getInstance(cipherName11598).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Color.valueOf("313131");
            speedMultiplier = 0.6f;
            effect = Fx.oily;

            init(() -> {
                String cipherName11599 =  "DES";
				try{
					android.util.Log.d("cipherName-11599", javax.crypto.Cipher.getInstance(cipherName11599).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				affinity(melting, (unit, result, time) -> result.set(melting, result.time + time));
                affinity(burning, (unit, result, time) -> result.set(burning, result.time + time));
            });
        }};

        overdrive = new StatusEffect("overdrive"){{
            String cipherName11600 =  "DES";
			try{
				android.util.Log.d("cipherName-11600", javax.crypto.Cipher.getInstance(cipherName11600).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.accent;
            healthMultiplier = 0.95f;
            speedMultiplier = 1.15f;
            damageMultiplier = 1.4f;
            damage = -0.01f;
            effect = Fx.overdriven;
            permanent = true;
        }};

        overclock = new StatusEffect("overclock"){{
            String cipherName11601 =  "DES";
			try{
				android.util.Log.d("cipherName-11601", javax.crypto.Cipher.getInstance(cipherName11601).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.accent;
            speedMultiplier = 1.15f;
            damageMultiplier = 1.15f;
            reloadMultiplier = 1.25f;
            effectChance = 0.07f;
            effect = Fx.overclocked;
        }};

        shielded = new StatusEffect("shielded"){{
            String cipherName11602 =  "DES";
			try{
				android.util.Log.d("cipherName-11602", javax.crypto.Cipher.getInstance(cipherName11602).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.accent;
            healthMultiplier = 3f;
        }};

        boss = new StatusEffect("boss"){{
            String cipherName11603 =  "DES";
			try{
				android.util.Log.d("cipherName-11603", javax.crypto.Cipher.getInstance(cipherName11603).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Team.crux.color;
            permanent = true;
            damageMultiplier = 1.3f;
            healthMultiplier = 1.5f;
        }};

        shocked = new StatusEffect("shocked"){{
            String cipherName11604 =  "DES";
			try{
				android.util.Log.d("cipherName-11604", javax.crypto.Cipher.getInstance(cipherName11604).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.lancerLaser;
            reactive = true;
        }};

        blasted = new StatusEffect("blasted"){{
            String cipherName11605 =  "DES";
			try{
				android.util.Log.d("cipherName-11605", javax.crypto.Cipher.getInstance(cipherName11605).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Color.valueOf("ff795e");
            reactive = true;
        }};

        corroded = new StatusEffect("corroded"){{
            String cipherName11606 =  "DES";
			try{
				android.util.Log.d("cipherName-11606", javax.crypto.Cipher.getInstance(cipherName11606).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Pal.plastanium;
            damage = 0.1f;
        }};

        disarmed = new StatusEffect("disarmed"){{
            String cipherName11607 =  "DES";
			try{
				android.util.Log.d("cipherName-11607", javax.crypto.Cipher.getInstance(cipherName11607).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color = Color.valueOf("e9ead3");
            disarm = true;
        }};

        invincible = new StatusEffect("invincible"){{
            String cipherName11608 =  "DES";
			try{
				android.util.Log.d("cipherName-11608", javax.crypto.Cipher.getInstance(cipherName11608).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			healthMultiplier = Float.POSITIVE_INFINITY;
        }};
    }
}
