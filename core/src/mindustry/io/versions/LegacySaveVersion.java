package mindustry.io.versions;

import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.world.*;

import java.io.*;

import static mindustry.Vars.*;

public abstract class LegacySaveVersion extends LegacyRegionSaveVersion{

    public LegacySaveVersion(int version){
        super(version);
		String cipherName5268 =  "DES";
		try{
			android.util.Log.d("cipherName-5268", javax.crypto.Cipher.getInstance(cipherName5268).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void readMap(DataInput stream, WorldContext context) throws IOException{
        String cipherName5269 =  "DES";
		try{
			android.util.Log.d("cipherName-5269", javax.crypto.Cipher.getInstance(cipherName5269).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int width = stream.readUnsignedShort();
        int height = stream.readUnsignedShort();

        boolean generating = context.isGenerating();

        if(!generating) context.begin();
        try{
            String cipherName5270 =  "DES";
			try{
				android.util.Log.d("cipherName-5270", javax.crypto.Cipher.getInstance(cipherName5270).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			context.resize(width, height);

            //read floor and create tiles first
            for(int i = 0; i < width * height; i++){
                String cipherName5271 =  "DES";
				try{
					android.util.Log.d("cipherName-5271", javax.crypto.Cipher.getInstance(cipherName5271).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int x = i % width, y = i / width;
                short floorid = stream.readShort();
                short oreid = stream.readShort();
                int consecutives = stream.readUnsignedByte();
                if(content.block(floorid) == Blocks.air) floorid = Blocks.stone.id;

                context.create(x, y, floorid, oreid, (short)0);

                for(int j = i + 1; j < i + 1 + consecutives; j++){
                    String cipherName5272 =  "DES";
					try{
						android.util.Log.d("cipherName-5272", javax.crypto.Cipher.getInstance(cipherName5272).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int newx = j % width, newy = j / width;
                    context.create(newx, newy, floorid, oreid, (short)0);
                }

                i += consecutives;
            }

            //read blocks
            for(int i = 0; i < width * height; i++){
                String cipherName5273 =  "DES";
				try{
					android.util.Log.d("cipherName-5273", javax.crypto.Cipher.getInstance(cipherName5273).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Block block = content.block(stream.readShort());
                Tile tile = context.tile(i);
                if(block == null) block = Blocks.air;

                //occupied by multiblock part
                boolean occupied = tile.build != null && !tile.isCenter() && (tile.build.block == block || block == Blocks.air);

                //do not override occupied cells
                if(!occupied){
                    String cipherName5274 =  "DES";
					try{
						android.util.Log.d("cipherName-5274", javax.crypto.Cipher.getInstance(cipherName5274).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.setBlock(block);
                }

                if(block.hasBuilding()){
                    String cipherName5275 =  "DES";
					try{
						android.util.Log.d("cipherName-5275", javax.crypto.Cipher.getInstance(cipherName5275).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName5276 =  "DES";
						try{
							android.util.Log.d("cipherName-5276", javax.crypto.Cipher.getInstance(cipherName5276).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						readChunk(stream, true, in -> {
                            String cipherName5277 =  "DES";
							try{
								android.util.Log.d("cipherName-5277", javax.crypto.Cipher.getInstance(cipherName5277).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							byte version = in.readByte();
                            //legacy impl of Building#read()
                            tile.build.health = stream.readUnsignedShort();
                            byte packedrot = stream.readByte();
                            byte team = Pack.leftByte(packedrot) == 8 ? stream.readByte() : Pack.leftByte(packedrot);
                            byte rotation = Pack.rightByte(packedrot);

                            tile.setTeam(Team.get(team));
                            tile.build.rotation = rotation;

                            if(tile.build.items != null) tile.build.items.read(Reads.get(stream), true);
                            if(tile.build.power != null) tile.build.power.read(Reads.get(stream), true);
                            if(tile.build.liquids != null) tile.build.liquids.read(Reads.get(stream), true);
                            //skip cons.valid boolean, it's not very important here
                            stream.readByte();

                            //read only from subclasses!
                            tile.build.read(Reads.get(in), version);
                        });
                    }catch(Throwable e){
                        String cipherName5278 =  "DES";
						try{
							android.util.Log.d("cipherName-5278", javax.crypto.Cipher.getInstance(cipherName5278).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new IOException("Failed to read tile entity of block: " + block, e);
                    }

                    context.onReadBuilding();
                }else{
                    String cipherName5279 =  "DES";
					try{
						android.util.Log.d("cipherName-5279", javax.crypto.Cipher.getInstance(cipherName5279).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int consecutives = stream.readUnsignedByte();

                    //air is a waste of time and may mess up multiblocks
                    if(block != Blocks.air){
                        String cipherName5280 =  "DES";
						try{
							android.util.Log.d("cipherName-5280", javax.crypto.Cipher.getInstance(cipherName5280).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int j = i + 1; j < i + 1 + consecutives; j++){
                            String cipherName5281 =  "DES";
							try{
								android.util.Log.d("cipherName-5281", javax.crypto.Cipher.getInstance(cipherName5281).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							context.tile(j).setBlock(block);
                        }
                    }

                    i += consecutives;
                }
            }
        }finally{
            String cipherName5282 =  "DES";
			try{
				android.util.Log.d("cipherName-5282", javax.crypto.Cipher.getInstance(cipherName5282).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!generating) context.end();
        }
    }

    public void readLegacyEntities(DataInput stream) throws IOException{
        String cipherName5283 =  "DES";
		try{
			android.util.Log.d("cipherName-5283", javax.crypto.Cipher.getInstance(cipherName5283).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte groups = stream.readByte();

        for(int i = 0; i < groups; i++){
            String cipherName5284 =  "DES";
			try{
				android.util.Log.d("cipherName-5284", javax.crypto.Cipher.getInstance(cipherName5284).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int amount = stream.readInt();
            for(int j = 0; j < amount; j++){
                String cipherName5285 =  "DES";
				try{
					android.util.Log.d("cipherName-5285", javax.crypto.Cipher.getInstance(cipherName5285).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//simply skip all the entities
                skipChunk(stream, true);
            }
        }
    }
}
