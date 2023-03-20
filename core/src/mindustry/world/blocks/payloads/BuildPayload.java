package mindustry.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class BuildPayload implements Payload{
    public Building build;

    public BuildPayload(Block block, Team team){
        String cipherName6617 =  "DES";
		try{
			android.util.Log.d("cipherName-6617", javax.crypto.Cipher.getInstance(cipherName6617).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.build = block.newBuilding().create(block, team);
        this.build.tile = emptyTile;
    }

    public BuildPayload(Building build){
        String cipherName6618 =  "DES";
		try{
			android.util.Log.d("cipherName-6618", javax.crypto.Cipher.getInstance(cipherName6618).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.build = build;
    }

    public Block block(){
        String cipherName6619 =  "DES";
		try{
			android.util.Log.d("cipherName-6619", javax.crypto.Cipher.getInstance(cipherName6619).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return build.block;
    }

    public void place(Tile tile){
        String cipherName6620 =  "DES";
		try{
			android.util.Log.d("cipherName-6620", javax.crypto.Cipher.getInstance(cipherName6620).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		place(tile, 0);
    }

    public void place(Tile tile, int rotation){
        String cipherName6621 =  "DES";
		try{
			android.util.Log.d("cipherName-6621", javax.crypto.Cipher.getInstance(cipherName6621).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tile.setBlock(build.block, build.team, rotation, () -> build);
        build.dropped();
    }

    @Override
    public UnlockableContent content(){
        String cipherName6622 =  "DES";
		try{
			android.util.Log.d("cipherName-6622", javax.crypto.Cipher.getInstance(cipherName6622).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return build.block;
    }

    @Override
    public void update(@Nullable Unit unitHolder, @Nullable Building buildingHolder){
        String cipherName6623 =  "DES";
		try{
			android.util.Log.d("cipherName-6623", javax.crypto.Cipher.getInstance(cipherName6623).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(unitHolder != null && (!build.block.updateInUnits || (!state.rules.unitPayloadUpdate && !build.block.alwaysUpdateInUnits))) return;

        build.tile = emptyTile;
        build.updatePayload(unitHolder, buildingHolder);
    }

    @Override
    public ItemStack[] requirements(){
        String cipherName6624 =  "DES";
		try{
			android.util.Log.d("cipherName-6624", javax.crypto.Cipher.getInstance(cipherName6624).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return build.block.requirements;
    }

    @Override
    public float buildTime(){
        String cipherName6625 =  "DES";
		try{
			android.util.Log.d("cipherName-6625", javax.crypto.Cipher.getInstance(cipherName6625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return build.block.buildCost;
    }

    @Override
    public float x(){
        String cipherName6626 =  "DES";
		try{
			android.util.Log.d("cipherName-6626", javax.crypto.Cipher.getInstance(cipherName6626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return build.x;
    }

    @Override
    public float y(){
        String cipherName6627 =  "DES";
		try{
			android.util.Log.d("cipherName-6627", javax.crypto.Cipher.getInstance(cipherName6627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return build.y;
    }

    @Override
    public float size(){
        String cipherName6628 =  "DES";
		try{
			android.util.Log.d("cipherName-6628", javax.crypto.Cipher.getInstance(cipherName6628).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return build.block.size * tilesize;
    }

    @Override
    public void write(Writes write){
        String cipherName6629 =  "DES";
		try{
			android.util.Log.d("cipherName-6629", javax.crypto.Cipher.getInstance(cipherName6629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write.b(payloadBlock);
        write.s(build.block.id);
        write.b(build.version());
        build.writeAll(write);
    }

    @Override
    public void set(float x, float y, float rotation){
        String cipherName6630 =  "DES";
		try{
			android.util.Log.d("cipherName-6630", javax.crypto.Cipher.getInstance(cipherName6630).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		build.set(x, y);
        build.payloadRotation = rotation;
    }

    @Override
    public void drawShadow(float alpha){
        String cipherName6631 =  "DES";
		try{
			android.util.Log.d("cipherName-6631", javax.crypto.Cipher.getInstance(cipherName6631).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Drawf.squareShadow(build.x, build.y, build.block.size * tilesize * 1.85f, alpha);
    }

    @Override
    public void draw(){
        String cipherName6632 =  "DES";
		try{
			android.util.Log.d("cipherName-6632", javax.crypto.Cipher.getInstance(cipherName6632).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float prevZ = Draw.z();
        Draw.z(prevZ - 0.0001f);
        drawShadow(1f);
        Draw.z(prevZ);
        Draw.zTransform(z -> z >= Layer.flyingUnitLow + 1f ? z : 0.0011f + Math.min(Mathf.clamp(z, prevZ - 0.001f, prevZ + 0.9f), Layer.flyingUnitLow - 1f));
        build.tile = emptyTile;
        build.payloadDraw();
        Draw.zTransform();
        Draw.z(prevZ);
    }

    @Override
    public TextureRegion icon(){
        String cipherName6633 =  "DES";
		try{
			android.util.Log.d("cipherName-6633", javax.crypto.Cipher.getInstance(cipherName6633).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return block().fullIcon;
    }
}
