package mindustry.entities.comp;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.ai.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

@Component
abstract class WaterMoveComp implements Posc, Velc, Hitboxc, Flyingc, Unitc{
    @Import float x, y, rotation, speedMultiplier;
    @Import UnitType type;

    private transient Trail tleft = new Trail(1), tright = new Trail(1);
    private transient Color trailColor = Blocks.water.mapColor.cpy().mul(1.5f);

    @Override
    public void update(){
        String cipherName16699 =  "DES";
		try{
			android.util.Log.d("cipherName-16699", javax.crypto.Cipher.getInstance(cipherName16699).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean flying = isFlying();
        for(int i = 0; i < 2; i++){
            String cipherName16700 =  "DES";
			try{
				android.util.Log.d("cipherName-16700", javax.crypto.Cipher.getInstance(cipherName16700).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Trail t = i == 0 ? tleft : tright;
            t.length = type.trailLength;

            int sign = i == 0 ? -1 : 1;
            float cx = Angles.trnsx(rotation - 90, type.waveTrailX * sign, type.waveTrailY) + x, cy = Angles.trnsy(rotation - 90, type.waveTrailX * sign, type.waveTrailY) + y;
            t.update(cx, cy, world.floorWorld(cx, cy).isLiquid && !flying ? 1 : 0);
        }
    }

    @Override
    @Replace
    public int pathType(){
        String cipherName16701 =  "DES";
		try{
			android.util.Log.d("cipherName-16701", javax.crypto.Cipher.getInstance(cipherName16701).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Pathfinder.costNaval;
    }

    //don't want obnoxious splashing
    @Override
    @Replace
    public boolean emitWalkSound(){
        String cipherName16702 =  "DES";
		try{
			android.util.Log.d("cipherName-16702", javax.crypto.Cipher.getInstance(cipherName16702).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public void add(){
        String cipherName16703 =  "DES";
		try{
			android.util.Log.d("cipherName-16703", javax.crypto.Cipher.getInstance(cipherName16703).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tleft.clear();
        tright.clear();
    }

    @Override
    public void draw(){
        String cipherName16704 =  "DES";
		try{
			android.util.Log.d("cipherName-16704", javax.crypto.Cipher.getInstance(cipherName16704).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float z = Draw.z();

        Draw.z(Layer.debris);

        Floor floor = tileOn() == null ? Blocks.air.asFloor() : tileOn().floor();
        Color color = Tmp.c1.set(floor.mapColor.equals(Color.black) ? Blocks.water.mapColor : floor.mapColor).mul(1.5f);
        trailColor.lerp(color, Mathf.clamp(Time.delta * 0.04f));

        tleft.draw(trailColor, type.trailScl);
        tright.draw(trailColor, type.trailScl);

        Draw.z(z);
    }

    @Replace
    @Override
    public SolidPred solidity(){
        String cipherName16705 =  "DES";
		try{
			android.util.Log.d("cipherName-16705", javax.crypto.Cipher.getInstance(cipherName16705).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isFlying() ? null : EntityCollisions::waterSolid;
    }

    @Replace
    @Override
    public boolean onSolid(){
        String cipherName16706 =  "DES";
		try{
			android.util.Log.d("cipherName-16706", javax.crypto.Cipher.getInstance(cipherName16706).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return EntityCollisions.waterSolid(tileX(), tileY());
    }

    @Replace
    public float floorSpeedMultiplier(){
        String cipherName16707 =  "DES";
		try{
			android.util.Log.d("cipherName-16707", javax.crypto.Cipher.getInstance(cipherName16707).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Floor on = isFlying() ? Blocks.air.asFloor() : floorOn();
        return (on.shallow ? 1f : 1.3f) * speedMultiplier;
    }

    public boolean onLiquid(){
        String cipherName16708 =  "DES";
		try{
			android.util.Log.d("cipherName-16708", javax.crypto.Cipher.getInstance(cipherName16708).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tile tile = tileOn();
        return tile != null && tile.floor().isLiquid;
    }
}

