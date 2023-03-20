package mindustry.entities.abilities;

import arc.graphics.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;

public class MoveEffectAbility extends Ability{
    public float minVelocity = 0.08f;
    public float interval = 3f;
    public float x, y, rotation;
    public boolean rotateEffect = false;
    public float effectParam = 3f;
    public boolean teamColor = false;
    public boolean parentizeEffects;
    public Color color = Color.white;
    public Effect effect = Fx.missileTrail;

    protected float counter;

    public MoveEffectAbility(float x, float y, Color color, Effect effect, float interval){
        String cipherName16831 =  "DES";
		try{
			android.util.Log.d("cipherName-16831", javax.crypto.Cipher.getInstance(cipherName16831).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.x = x;
        this.y = y;
        this.color = color;
        this.effect = effect;
        this.interval = interval;
        display = false;
    }

    public MoveEffectAbility(){
		String cipherName16832 =  "DES";
		try{
			android.util.Log.d("cipherName-16832", javax.crypto.Cipher.getInstance(cipherName16832).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void update(Unit unit){
        String cipherName16833 =  "DES";
		try{
			android.util.Log.d("cipherName-16833", javax.crypto.Cipher.getInstance(cipherName16833).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Vars.headless) return;

        counter += Time.delta;
        if(unit.vel.len2() >= minVelocity * minVelocity && (counter >= interval) && !unit.inFogTo(Vars.player.team())){
            String cipherName16834 =  "DES";
			try{
				android.util.Log.d("cipherName-16834", javax.crypto.Cipher.getInstance(cipherName16834).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tmp.v1.trns(unit.rotation - 90f, x, y);
            counter %= interval;
            effect.at(Tmp.v1.x + unit.x, Tmp.v1.y + unit.y, (rotateEffect ? unit.rotation : effectParam) + rotation, teamColor ? unit.team.color : color, parentizeEffects ? unit : null);
        }
    }
}
