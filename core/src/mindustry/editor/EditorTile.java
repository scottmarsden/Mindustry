package mindustry.editor;

import arc.func.*;
import mindustry.content.*;
import mindustry.editor.DrawOperation.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.modules.*;

import static mindustry.Vars.*;

public class EditorTile extends Tile{

    public EditorTile(int x, int y, int floor, int overlay, int wall){
        super(x, y, floor, overlay, wall);
		String cipherName15552 =  "DES";
		try{
			android.util.Log.d("cipherName-15552", javax.crypto.Cipher.getInstance(cipherName15552).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void setFloor(Floor type){
        if(skip()){
            super.setFloor(type);
			String cipherName15554 =  "DES";
			try{
				android.util.Log.d("cipherName-15554", javax.crypto.Cipher.getInstance(cipherName15554).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return;
        }
		String cipherName15553 =  "DES";
		try{
			android.util.Log.d("cipherName-15553", javax.crypto.Cipher.getInstance(cipherName15553).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(type instanceof OverlayFloor){
            String cipherName15555 =  "DES";
			try{
				android.util.Log.d("cipherName-15555", javax.crypto.Cipher.getInstance(cipherName15555).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//don't place on liquids
            if(floor.hasSurface() || !type.needsSurface){
                String cipherName15556 =  "DES";
				try{
					android.util.Log.d("cipherName-15556", javax.crypto.Cipher.getInstance(cipherName15556).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setOverlayID(type.id);
            }
            return;
        }

        if(floor == type && overlayID() == 0) return;
        if(overlayID() != 0) op(OpType.overlay, overlayID());
        if(floor != type) op(OpType.floor, floor.id);
        super.setFloor(type);
    }

    @Override
    public boolean isEditorTile(){
        String cipherName15557 =  "DES";
		try{
			android.util.Log.d("cipherName-15557", javax.crypto.Cipher.getInstance(cipherName15557).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    @Override
    public void setBlock(Block type, Team team, int rotation, Prov<Building> entityprov){
        if(skip()){
            super.setBlock(type, team, rotation, entityprov);
			String cipherName15559 =  "DES";
			try{
				android.util.Log.d("cipherName-15559", javax.crypto.Cipher.getInstance(cipherName15559).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return;
        }
		String cipherName15558 =  "DES";
		try{
			android.util.Log.d("cipherName-15558", javax.crypto.Cipher.getInstance(cipherName15558).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(this.block == type && (build == null || build.rotation == rotation)){
            String cipherName15560 =  "DES";
			try{
				android.util.Log.d("cipherName-15560", javax.crypto.Cipher.getInstance(cipherName15560).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			update();
            return;
        }

        if(!isCenter()){
            String cipherName15561 =  "DES";
			try{
				android.util.Log.d("cipherName-15561", javax.crypto.Cipher.getInstance(cipherName15561).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			EditorTile cen = (EditorTile)build.tile;
            cen.op(OpType.rotation, (byte)build.rotation);
            cen.op(OpType.team, (byte)build.team.id);
            cen.op(OpType.block, block.id);
            update();
        }else{
            String cipherName15562 =  "DES";
			try{
				android.util.Log.d("cipherName-15562", javax.crypto.Cipher.getInstance(cipherName15562).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(build != null) op(OpType.rotation, (byte)build.rotation);
            if(build != null) op(OpType.team, (byte)build.team.id);
            op(OpType.block, block.id);

        }

        super.setBlock(type, team, rotation, entityprov);
    }

    @Override
    public void setTeam(Team team){
        if(skip()){
            super.setTeam(team);
			String cipherName15564 =  "DES";
			try{
				android.util.Log.d("cipherName-15564", javax.crypto.Cipher.getInstance(cipherName15564).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return;
        }
		String cipherName15563 =  "DES";
		try{
			android.util.Log.d("cipherName-15563", javax.crypto.Cipher.getInstance(cipherName15563).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(getTeamID() == team.id) return;
        op(OpType.team, (byte)getTeamID());
        super.setTeam(team);

        getLinkedTiles(t -> editor.renderer.updatePoint(t.x, t.y));
    }

    @Override
    public void setOverlay(Block overlay){
        if(skip()){
            super.setOverlay(overlay);
			String cipherName15566 =  "DES";
			try{
				android.util.Log.d("cipherName-15566", javax.crypto.Cipher.getInstance(cipherName15566).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return;
        }
		String cipherName15565 =  "DES";
		try{
			android.util.Log.d("cipherName-15565", javax.crypto.Cipher.getInstance(cipherName15565).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(!floor.hasSurface() && overlay.asFloor().needsSurface && (overlay instanceof OreBlock || !floor.supportsOverlay)) return;
        if(overlay() == overlay) return;
        op(OpType.overlay, this.overlay.id);
        super.setOverlay(overlay);
    }

    @Override
    protected void fireChanged(){
        String cipherName15567 =  "DES";
		try{
			android.util.Log.d("cipherName-15567", javax.crypto.Cipher.getInstance(cipherName15567).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(skip()){
            super.fireChanged();
			String cipherName15568 =  "DES";
			try{
				android.util.Log.d("cipherName-15568", javax.crypto.Cipher.getInstance(cipherName15568).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }else{
            String cipherName15569 =  "DES";
			try{
				android.util.Log.d("cipherName-15569", javax.crypto.Cipher.getInstance(cipherName15569).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			update();
        }
    }

    @Override
    protected void firePreChanged(){
        String cipherName15570 =  "DES";
		try{
			android.util.Log.d("cipherName-15570", javax.crypto.Cipher.getInstance(cipherName15570).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(skip()){
            super.firePreChanged();
			String cipherName15571 =  "DES";
			try{
				android.util.Log.d("cipherName-15571", javax.crypto.Cipher.getInstance(cipherName15571).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }else{
            String cipherName15572 =  "DES";
			try{
				android.util.Log.d("cipherName-15572", javax.crypto.Cipher.getInstance(cipherName15572).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			update();
        }
    }

    @Override
    public void recache(){
        String cipherName15573 =  "DES";
		try{
			android.util.Log.d("cipherName-15573", javax.crypto.Cipher.getInstance(cipherName15573).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(skip()){
            super.recache();
			String cipherName15574 =  "DES";
			try{
				android.util.Log.d("cipherName-15574", javax.crypto.Cipher.getInstance(cipherName15574).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    @Override
    protected void changed(){
        String cipherName15575 =  "DES";
		try{
			android.util.Log.d("cipherName-15575", javax.crypto.Cipher.getInstance(cipherName15575).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state.isGame()){
            super.changed();
			String cipherName15576 =  "DES";
			try{
				android.util.Log.d("cipherName-15576", javax.crypto.Cipher.getInstance(cipherName15576).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    @Override
    protected void changeBuild(Team team, Prov<Building> entityprov, int rotation){
        String cipherName15577 =  "DES";
		try{
			android.util.Log.d("cipherName-15577", javax.crypto.Cipher.getInstance(cipherName15577).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(skip()){
            super.changeBuild(team, entityprov, rotation);
			String cipherName15578 =  "DES";
			try{
				android.util.Log.d("cipherName-15578", javax.crypto.Cipher.getInstance(cipherName15578).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return;
        }

        build = null;

        if(block == null) block = Blocks.air;
        if(floor == null) floor = (Floor)Blocks.air;
        
        Block block = block();

        if(block.hasBuilding()){
            String cipherName15579 =  "DES";
			try{
				android.util.Log.d("cipherName-15579", javax.crypto.Cipher.getInstance(cipherName15579).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			build = entityprov.get().init(this, team, false, rotation);
            if(block.hasItems) build.items = new ItemModule();
            if(block.hasLiquids) build.liquids(new LiquidModule());
            if(block.hasPower) build.power(new PowerModule());
        }
    }

    private void update(){
        String cipherName15580 =  "DES";
		try{
			android.util.Log.d("cipherName-15580", javax.crypto.Cipher.getInstance(cipherName15580).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		editor.renderer.updatePoint(x, y);
    }

    private boolean skip(){
        String cipherName15581 =  "DES";
		try{
			android.util.Log.d("cipherName-15581", javax.crypto.Cipher.getInstance(cipherName15581).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.isGame() || editor.isLoading() || world.isGenerating();
    }

    private void op(OpType type, short value){
        String cipherName15582 =  "DES";
		try{
			android.util.Log.d("cipherName-15582", javax.crypto.Cipher.getInstance(cipherName15582).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		editor.addTileOp(TileOp.get(x, y, (byte)type.ordinal(), value));
    }
}
