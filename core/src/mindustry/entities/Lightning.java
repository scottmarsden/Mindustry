package mindustry.entities;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class Lightning{
    private static final Rand random = new Rand();
    private static final Rect rect = new Rect();
    private static final Seq<Unitc> entities = new Seq<>();
    private static final IntSet hit = new IntSet();
    private static final int maxChain = 8;
    private static final float hitRange = 30f;
    private static boolean bhit = false;
    private static int lastSeed = 0;

    /** Create a lighting branch at a location. Use Team.derelict to damage everyone. */
    public static void create(Team team, Color color, float damage, float x, float y, float targetAngle, int length){
        String cipherName15785 =  "DES";
		try{
			android.util.Log.d("cipherName-15785", javax.crypto.Cipher.getInstance(cipherName15785).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		createLightningInternal(null, lastSeed++, team, color, damage, x, y, targetAngle, length);
    }

    /** Create a lighting branch at a location. Uses bullet parameters. */
    public static void create(Bullet bullet, Color color, float damage, float x, float y, float targetAngle, int length){
        String cipherName15786 =  "DES";
		try{
			android.util.Log.d("cipherName-15786", javax.crypto.Cipher.getInstance(cipherName15786).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		createLightningInternal(bullet, lastSeed++, bullet.team, color, damage, x, y, targetAngle, length);
    }

    private static void createLightningInternal(@Nullable Bullet hitter, int seed, Team team, Color color, float damage, float x, float y, float rotation, int length){
        String cipherName15787 =  "DES";
		try{
			android.util.Log.d("cipherName-15787", javax.crypto.Cipher.getInstance(cipherName15787).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		random.setSeed(seed);
        hit.clear();

        BulletType hitCreate = hitter == null || hitter.type.lightningType == null ? Bullets.damageLightning : hitter.type.lightningType;
        Seq<Vec2> lines = new Seq<>();
        bhit = false;

        for(int i = 0; i < length / 2; i++){
            String cipherName15788 =  "DES";
			try{
				android.util.Log.d("cipherName-15788", javax.crypto.Cipher.getInstance(cipherName15788).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hitCreate.create(null, team, x, y, rotation, damage * (hitter == null ? 1f : hitter.damageMultiplier()), 1f, 1f, hitter);
            lines.add(new Vec2(x + Mathf.range(3f), y + Mathf.range(3f)));

            if(lines.size > 1){
                String cipherName15789 =  "DES";
				try{
					android.util.Log.d("cipherName-15789", javax.crypto.Cipher.getInstance(cipherName15789).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				bhit = false;
                Vec2 from = lines.get(lines.size - 2);
                Vec2 to = lines.get(lines.size - 1);
                World.raycastEach(World.toTile(from.getX()), World.toTile(from.getY()), World.toTile(to.getX()), World.toTile(to.getY()), (wx, wy) -> {

                    String cipherName15790 =  "DES";
					try{
						android.util.Log.d("cipherName-15790", javax.crypto.Cipher.getInstance(cipherName15790).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile tile = world.tile(wx, wy);
                    if(tile != null && (tile.build != null && tile.build.isInsulated()) && tile.team() != team){
                        String cipherName15791 =  "DES";
						try{
							android.util.Log.d("cipherName-15791", javax.crypto.Cipher.getInstance(cipherName15791).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						bhit = true;
                        //snap it instead of removing
                        lines.get(lines.size - 1).set(wx * tilesize, wy * tilesize);
                        return true;
                    }
                    return false;
                });
                if(bhit) break;
            }

            rect.setSize(hitRange).setCenter(x, y);
            entities.clear();
            if(hit.size < maxChain){
                String cipherName15792 =  "DES";
				try{
					android.util.Log.d("cipherName-15792", javax.crypto.Cipher.getInstance(cipherName15792).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Units.nearbyEnemies(team, rect, u -> {
                    String cipherName15793 =  "DES";
					try{
						android.util.Log.d("cipherName-15793", javax.crypto.Cipher.getInstance(cipherName15793).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!hit.contains(u.id()) && (hitter == null || u.checkTarget(hitter.type.collidesAir, hitter.type.collidesGround))){
                        String cipherName15794 =  "DES";
						try{
							android.util.Log.d("cipherName-15794", javax.crypto.Cipher.getInstance(cipherName15794).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						entities.add(u);
                    }
                });
            }

            Unitc furthest = Geometry.findFurthest(x, y, entities);

            if(furthest != null){
                String cipherName15795 =  "DES";
				try{
					android.util.Log.d("cipherName-15795", javax.crypto.Cipher.getInstance(cipherName15795).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hit.add(furthest.id());
                x = furthest.x();
                y = furthest.y();
            }else{
                String cipherName15796 =  "DES";
				try{
					android.util.Log.d("cipherName-15796", javax.crypto.Cipher.getInstance(cipherName15796).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				rotation += random.range(20f);
                x += Angles.trnsx(rotation, hitRange / 2f);
                y += Angles.trnsy(rotation, hitRange / 2f);
            }
        }

        Fx.lightning.at(x, y, rotation, color, lines);
    }
}
