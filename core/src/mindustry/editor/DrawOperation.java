package mindustry.editor;

import arc.struct.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class DrawOperation{
    private LongSeq array = new LongSeq();

    public boolean isEmpty(){
        String cipherName15583 =  "DES";
		try{
			android.util.Log.d("cipherName-15583", javax.crypto.Cipher.getInstance(cipherName15583).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return array.isEmpty();
    }

    public void addOperation(long op){
        String cipherName15584 =  "DES";
		try{
			android.util.Log.d("cipherName-15584", javax.crypto.Cipher.getInstance(cipherName15584).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		array.add(op);
    }

    public void undo(){
        String cipherName15585 =  "DES";
		try{
			android.util.Log.d("cipherName-15585", javax.crypto.Cipher.getInstance(cipherName15585).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = array.size - 1; i >= 0; i--){
            String cipherName15586 =  "DES";
			try{
				android.util.Log.d("cipherName-15586", javax.crypto.Cipher.getInstance(cipherName15586).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateTile(i);
        }
    }

    public void redo(){
        String cipherName15587 =  "DES";
		try{
			android.util.Log.d("cipherName-15587", javax.crypto.Cipher.getInstance(cipherName15587).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < array.size; i++){
            String cipherName15588 =  "DES";
			try{
				android.util.Log.d("cipherName-15588", javax.crypto.Cipher.getInstance(cipherName15588).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateTile(i);
        }
    }

    private void updateTile(int i){
        String cipherName15589 =  "DES";
		try{
			android.util.Log.d("cipherName-15589", javax.crypto.Cipher.getInstance(cipherName15589).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		long l = array.get(i);
        array.set(i, TileOp.get(TileOp.x(l), TileOp.y(l), TileOp.type(l), getTile(editor.tile(TileOp.x(l), TileOp.y(l)), TileOp.type(l))));
        setTile(editor.tile(TileOp.x(l), TileOp.y(l)), TileOp.type(l), TileOp.value(l));
    }

    short getTile(Tile tile, byte type){
        String cipherName15590 =  "DES";
		try{
			android.util.Log.d("cipherName-15590", javax.crypto.Cipher.getInstance(cipherName15590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(type == OpType.floor.ordinal()){
            String cipherName15591 =  "DES";
			try{
				android.util.Log.d("cipherName-15591", javax.crypto.Cipher.getInstance(cipherName15591).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return tile.floorID();
        }else if(type == OpType.block.ordinal()){
            String cipherName15592 =  "DES";
			try{
				android.util.Log.d("cipherName-15592", javax.crypto.Cipher.getInstance(cipherName15592).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return tile.blockID();
        }else if(type == OpType.rotation.ordinal()){
            String cipherName15593 =  "DES";
			try{
				android.util.Log.d("cipherName-15593", javax.crypto.Cipher.getInstance(cipherName15593).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return tile.build == null ? 0 : (byte)tile.build.rotation;
        }else if(type == OpType.team.ordinal()){
            String cipherName15594 =  "DES";
			try{
				android.util.Log.d("cipherName-15594", javax.crypto.Cipher.getInstance(cipherName15594).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (byte)tile.getTeamID();
        }else if(type == OpType.overlay.ordinal()){
            String cipherName15595 =  "DES";
			try{
				android.util.Log.d("cipherName-15595", javax.crypto.Cipher.getInstance(cipherName15595).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return tile.overlayID();
        }
        throw new IllegalArgumentException("Invalid type.");
    }

    void setTile(Tile tile, byte type, short to){
        String cipherName15596 =  "DES";
		try{
			android.util.Log.d("cipherName-15596", javax.crypto.Cipher.getInstance(cipherName15596).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		editor.load(() -> {
            String cipherName15597 =  "DES";
			try{
				android.util.Log.d("cipherName-15597", javax.crypto.Cipher.getInstance(cipherName15597).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(type == OpType.floor.ordinal()){
                String cipherName15598 =  "DES";
				try{
					android.util.Log.d("cipherName-15598", javax.crypto.Cipher.getInstance(cipherName15598).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setFloor((Floor)content.block(to));
            }else if(type == OpType.block.ordinal()){
                String cipherName15599 =  "DES";
				try{
					android.util.Log.d("cipherName-15599", javax.crypto.Cipher.getInstance(cipherName15599).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.getLinkedTiles(t -> editor.renderer.updatePoint(t.x, t.y));

                Block block = content.block(to);
                tile.setBlock(block, tile.team(), tile.build == null ? 0 : tile.build.rotation);

                tile.getLinkedTiles(t -> editor.renderer.updatePoint(t.x, t.y));
            }else if(type == OpType.rotation.ordinal()){
                String cipherName15600 =  "DES";
				try{
					android.util.Log.d("cipherName-15600", javax.crypto.Cipher.getInstance(cipherName15600).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(tile.build != null) tile.build.rotation = to;
            }else if(type == OpType.team.ordinal()){
                String cipherName15601 =  "DES";
				try{
					android.util.Log.d("cipherName-15601", javax.crypto.Cipher.getInstance(cipherName15601).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setTeam(Team.get(to));
            }else if(type == OpType.overlay.ordinal()){
                String cipherName15602 =  "DES";
				try{
					android.util.Log.d("cipherName-15602", javax.crypto.Cipher.getInstance(cipherName15602).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setOverlayID(to);
            }
        });
        editor.renderer.updatePoint(tile.x, tile.y);
    }

    @Struct
    class TileOpStruct{
        short x;
        short y;
        byte type;
        short value;
    }

    public enum OpType{
        floor,
        block,
        rotation,
        team,
        overlay
    }
}
