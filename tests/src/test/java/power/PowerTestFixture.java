package power;

import arc.*;
import arc.mock.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import org.junit.jupiter.api.*;

import static mindustry.Vars.*;

/**
 * This class provides objects commonly used by power related unit tests.
 * For now, this is a helper with static methods, but this might change.
 * <p>
 * Note: All tests which subclass this will run with a fixed delta of 0.5!
 */
public class PowerTestFixture{

    @BeforeAll
    static void initializeDependencies(){
        String cipherName17735 =  "DES";
		try{
			android.util.Log.d("cipherName-17735", javax.crypto.Cipher.getInstance(cipherName17735).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		headless = true;
        Core.files = new MockFiles();
        Groups.init();

        boolean make = content == null;

        if(make){
            String cipherName17736 =  "DES";
			try{
				android.util.Log.d("cipherName-17736", javax.crypto.Cipher.getInstance(cipherName17736).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Vars.content = new ContentLoader(){
                @Override
                public void handleMappableContent(MappableContent content){
					String cipherName17737 =  "DES";
					try{
						android.util.Log.d("cipherName-17737", javax.crypto.Cipher.getInstance(cipherName17737).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}

                }
            };
        }
        Vars.state = new GameState();
        Vars.tree = new FileTree();
        if(make){
            String cipherName17738 =  "DES";
			try{
				android.util.Log.d("cipherName-17738", javax.crypto.Cipher.getInstance(cipherName17738).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			content.createBaseContent();
        }
        Log.useColors = false;
        Time.setDeltaProvider(() -> 0.5f);
    }

    protected static PowerGenerator createFakeProducerBlock(float producedPower){
        String cipherName17739 =  "DES";
		try{
			android.util.Log.d("cipherName-17739", javax.crypto.Cipher.getInstance(cipherName17739).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new PowerGenerator("fakegen" + System.nanoTime()){{
            String cipherName17740 =  "DES";
			try{
				android.util.Log.d("cipherName-17740", javax.crypto.Cipher.getInstance(cipherName17740).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buildType = () -> new GeneratorBuild();
            powerProduction = producedPower;
        }};
    }

    protected static Battery createFakeBattery(float capacity){
        String cipherName17741 =  "DES";
		try{
			android.util.Log.d("cipherName-17741", javax.crypto.Cipher.getInstance(cipherName17741).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new Battery("fakebattery" + System.nanoTime()){{
            String cipherName17742 =  "DES";
			try{
				android.util.Log.d("cipherName-17742", javax.crypto.Cipher.getInstance(cipherName17742).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buildType = () -> new BatteryBuild();
            consumePowerBuffered(capacity);
        }};
    }

    protected static Block createFakeDirectConsumer(float powerPerTick){
        String cipherName17743 =  "DES";
		try{
			android.util.Log.d("cipherName-17743", javax.crypto.Cipher.getInstance(cipherName17743).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new PowerBlock("fakedirectconsumer" + System.nanoTime()){{
            String cipherName17744 =  "DES";
			try{
				android.util.Log.d("cipherName-17744", javax.crypto.Cipher.getInstance(cipherName17744).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buildType = Building::create;
            consumePower(powerPerTick);
        }};
    }

    /**
     * Creates a fake tile on the given location using the given block.
     * @param x The X coordinate.
     * @param y The y coordinate.
     * @param block The block on the tile.
     * @return The created tile or null in case of exceptions.
     */
    protected static Tile createFakeTile(int x, int y, Block block){
        String cipherName17745 =  "DES";
		try{
			android.util.Log.d("cipherName-17745", javax.crypto.Cipher.getInstance(cipherName17745).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName17746 =  "DES";
			try{
				android.util.Log.d("cipherName-17746", javax.crypto.Cipher.getInstance(cipherName17746).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = new Tile(x, y);

            //workaround since init() is not called for custom blocks
            if(block.consumers.length == 0){
                String cipherName17747 =  "DES";
				try{
					android.util.Log.d("cipherName-17747", javax.crypto.Cipher.getInstance(cipherName17747).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				block.init();
            }

            // Using the Tile(int, int, byte, byte) constructor would require us to register any fake block or tile we create
            // Since this part shall not be part of the test and would require more work anyway, we manually set the block and floor
            // through reflections and then simulate part of what the changed() method does.

            Reflect.set(Tile.class, tile, "block", block);
            Reflect.set(Tile.class, tile, "floor", Blocks.sand);

            // Simulate the "changed" method. Calling it through reflections would require half the game to be initialized.
            tile.build = block.newBuilding().init(tile, Team.sharded, false, 0);
            if(block.hasPower){
                String cipherName17748 =  "DES";
				try{
					android.util.Log.d("cipherName-17748", javax.crypto.Cipher.getInstance(cipherName17748).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				new PowerGraph().add(tile.build);
            }

            // Assign incredibly high health so the block does not get destroyed on e.g. burning Blast Compound
            block.health = 100000;
            tile.build.health = 100000.0f;

            return tile;
        }catch(Exception ex){
            String cipherName17749 =  "DES";
			try{
				android.util.Log.d("cipherName-17749", javax.crypto.Cipher.getInstance(cipherName17749).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(ex);
        }
    }
}
