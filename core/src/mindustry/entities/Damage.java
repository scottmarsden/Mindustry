package mindustry.entities;

import arc.*;
import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

/** Utility class for damaging in an area. */
public class Damage{
    private static final UnitDamageEvent bulletDamageEvent = new UnitDamageEvent();
    private static final Rect rect = new Rect();
    private static final Rect hitrect = new Rect();
    private static final Vec2 vec = new Vec2(), seg1 = new Vec2(), seg2 = new Vec2();
    private static final Seq<Unit> units = new Seq<>();
    private static final IntSet collidedBlocks = new IntSet();
    private static final IntFloatMap damages = new IntFloatMap();
    private static final Seq<Collided> collided = new Seq<>();
    private static final Pool<Collided> collidePool = Pools.get(Collided.class, Collided::new);
    private static final Seq<Building> builds = new Seq<>();
    private static final FloatSeq distances = new FloatSeq();

    private static Tile furthest;
    private static float maxDst = 0f;
    private static Building tmpBuilding;
    private static Unit tmpUnit;

    public static void applySuppression(Team team, float x, float y, float range, float reload, float maxDelay, float applyParticleChance, @Nullable Position source){
        String cipherName17142 =  "DES";
		try{
			android.util.Log.d("cipherName-17142", javax.crypto.Cipher.getInstance(cipherName17142).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		builds.clear();
        indexer.eachBlock(null, x, y, range, build -> build.team != team, build -> {
            String cipherName17143 =  "DES";
			try{
				android.util.Log.d("cipherName-17143", javax.crypto.Cipher.getInstance(cipherName17143).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float prev = build.healSuppressionTime;
            build.applyHealSuppression(reload + 1f);

            //TODO maybe should be block field instead of instanceof check
            if(build.wasRecentlyHealed(60f * 12f) || build.block.suppressable){

                String cipherName17144 =  "DES";
				try{
					android.util.Log.d("cipherName-17144", javax.crypto.Cipher.getInstance(cipherName17144).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//add prev check so ability spam doesn't lead to particle spam (essentially, recently suppressed blocks don't get new particles)
                if(!headless && prev - Time.time <= reload/2f){
                    String cipherName17145 =  "DES";
					try{
						android.util.Log.d("cipherName-17145", javax.crypto.Cipher.getInstance(cipherName17145).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					builds.add(build);
                }
            }
        });

        //to prevent particle spam, the amount of particles is to remain constant (scales with number of buildings)
        float scaledChance = applyParticleChance / builds.size;
        for(var build : builds){
            String cipherName17146 =  "DES";
			try{
				android.util.Log.d("cipherName-17146", javax.crypto.Cipher.getInstance(cipherName17146).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Mathf.chance(scaledChance)){
                String cipherName17147 =  "DES";
				try{
					android.util.Log.d("cipherName-17147", javax.crypto.Cipher.getInstance(cipherName17147).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Time.run(Mathf.random(maxDelay), () -> {
                    String cipherName17148 =  "DES";
					try{
						android.util.Log.d("cipherName-17148", javax.crypto.Cipher.getInstance(cipherName17148).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fx.regenSuppressSeek.at(build.x + Mathf.range(build.block.size * tilesize / 2f), build.y + Mathf.range(build.block.size * tilesize / 2f), 0f, source);
                });
            }
        }
    }

    /** Creates a dynamic explosion based on specified parameters. */
    public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage){
        String cipherName17149 =  "DES";
		try{
			android.util.Log.d("cipherName-17149", javax.crypto.Cipher.getInstance(cipherName17149).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dynamicExplosion(x, y, flammability, explosiveness, power, radius, damage, true, null, Fx.dynamicExplosion);
    }

    /** Creates a dynamic explosion based on specified parameters. */
    public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, Effect explosionFx){
        String cipherName17150 =  "DES";
		try{
			android.util.Log.d("cipherName-17150", javax.crypto.Cipher.getInstance(cipherName17150).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dynamicExplosion(x, y, flammability, explosiveness, power, radius, damage, true, null, explosionFx);
    }

    /** Creates a dynamic explosion based on specified parameters. */
    public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, boolean fire, @Nullable Team ignoreTeam){
        String cipherName17151 =  "DES";
		try{
			android.util.Log.d("cipherName-17151", javax.crypto.Cipher.getInstance(cipherName17151).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dynamicExplosion(x, y, flammability, explosiveness, power, radius, damage, fire, ignoreTeam, Fx.dynamicExplosion);
    }

    /** Creates a dynamic explosion based on specified parameters. */
    public static void dynamicExplosion(float x, float y, float flammability, float explosiveness, float power, float radius, boolean damage, boolean fire, @Nullable Team ignoreTeam, Effect explosionFx){
        String cipherName17152 =  "DES";
		try{
			android.util.Log.d("cipherName-17152", javax.crypto.Cipher.getInstance(cipherName17152).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(damage){
            String cipherName17153 =  "DES";
			try{
				android.util.Log.d("cipherName-17153", javax.crypto.Cipher.getInstance(cipherName17153).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = 0; i < Mathf.clamp(power / 700, 0, 8); i++){
                String cipherName17154 =  "DES";
				try{
					android.util.Log.d("cipherName-17154", javax.crypto.Cipher.getInstance(cipherName17154).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int length = 5 + Mathf.clamp((int)(Mathf.pow(power, 0.98f) / 500), 1, 18);
                Time.run(i * 0.8f + Mathf.random(4f), () -> Lightning.create(Team.derelict, Pal.power, 3 + Mathf.pow(power, 0.35f), x, y, Mathf.random(360f), length + Mathf.range(2)));
            }

            if(fire){
                String cipherName17155 =  "DES";
				try{
					android.util.Log.d("cipherName-17155", javax.crypto.Cipher.getInstance(cipherName17155).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < Mathf.clamp(flammability / 4, 0, 30); i++){
                    String cipherName17156 =  "DES";
					try{
						android.util.Log.d("cipherName-17156", javax.crypto.Cipher.getInstance(cipherName17156).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Time.run(i / 2f, () -> Call.createBullet(Bullets.fireball, Team.derelict, x, y, Mathf.random(360f), Bullets.fireball.damage, 1, 1));
                }
            }

            int waves = explosiveness <= 2 ? 0 : Mathf.clamp((int)(explosiveness / 11), 1, 25);

            for(int i = 0; i < waves; i++){
                String cipherName17157 =  "DES";
				try{
					android.util.Log.d("cipherName-17157", javax.crypto.Cipher.getInstance(cipherName17157).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int f = i;
                Time.run(i * 2f, () -> {
                    String cipherName17158 =  "DES";
					try{
						android.util.Log.d("cipherName-17158", javax.crypto.Cipher.getInstance(cipherName17158).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					damage(ignoreTeam, x, y, Mathf.clamp(radius + explosiveness, 0, 50f) * ((f + 1f) / waves), explosiveness / 2f, false);
                    Fx.blockExplosionSmoke.at(x + Mathf.range(radius), y + Mathf.range(radius));
                });
            }
        }

        if(explosiveness > 15f){
            String cipherName17159 =  "DES";
			try{
				android.util.Log.d("cipherName-17159", javax.crypto.Cipher.getInstance(cipherName17159).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fx.shockwave.at(x, y);
        }

        if(explosiveness > 30f){
            String cipherName17160 =  "DES";
			try{
				android.util.Log.d("cipherName-17160", javax.crypto.Cipher.getInstance(cipherName17160).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fx.bigShockwave.at(x, y);
        }

        float shake = Math.min(explosiveness / 4f + 3f, 9f);
        Effect.shake(shake, shake, x, y);
        explosionFx.at(x, y, radius / 8f);
    }

    public static void createIncend(float x, float y, float range, int amount){
        String cipherName17161 =  "DES";
		try{
			android.util.Log.d("cipherName-17161", javax.crypto.Cipher.getInstance(cipherName17161).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < amount; i++){
            String cipherName17162 =  "DES";
			try{
				android.util.Log.d("cipherName-17162", javax.crypto.Cipher.getInstance(cipherName17162).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float cx = x + Mathf.range(range);
            float cy = y + Mathf.range(range);
            Tile tile = world.tileWorld(cx, cy);
            if(tile != null){
                String cipherName17163 =  "DES";
				try{
					android.util.Log.d("cipherName-17163", javax.crypto.Cipher.getInstance(cipherName17163).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fires.create(tile);
            }
        }
    }

    public static @Nullable Building findAbsorber(Team team, float x1, float y1, float x2, float y2){
        String cipherName17164 =  "DES";
		try{
			android.util.Log.d("cipherName-17164", javax.crypto.Cipher.getInstance(cipherName17164).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tmpBuilding = null;

        boolean found = World.raycast(World.toTile(x1), World.toTile(y1), World.toTile(x2), World.toTile(y2),
        (x, y) -> (tmpBuilding = world.build(x, y)) != null && tmpBuilding.team != team && tmpBuilding.block.absorbLasers);

        return found ? tmpBuilding : null;
    }

    public static float findLaserLength(Bullet b, float length){
        String cipherName17165 =  "DES";
		try{
			android.util.Log.d("cipherName-17165", javax.crypto.Cipher.getInstance(cipherName17165).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		vec.trnsExact(b.rotation(), length);

        furthest = null;

        boolean found = World.raycast(b.tileX(), b.tileY(), World.toTile(b.x + vec.x), World.toTile(b.y + vec.y),
        (x, y) -> (furthest = world.tile(x, y)) != null && furthest.team() != b.team && (furthest.build != null && furthest.build.absorbLasers()));

        return found && furthest != null ? Math.max(6f, b.dst(furthest.worldx(), furthest.worldy())) : length;
    }

    public static float findPierceLength(Bullet b, int pierceCap, float length){
        String cipherName17166 =  "DES";
		try{
			android.util.Log.d("cipherName-17166", javax.crypto.Cipher.getInstance(cipherName17166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		vec.trnsExact(b.rotation(), length);
        rect.setPosition(b.x, b.y).setSize(vec.x, vec.y).normalize().grow(3f);

        maxDst = Float.POSITIVE_INFINITY;

        distances.clear();

        World.raycast(b.tileX(), b.tileY(), World.toTile(b.x + vec.x), World.toTile(b.y + vec.y), (x, y) -> {
            String cipherName17167 =  "DES";
			try{
				android.util.Log.d("cipherName-17167", javax.crypto.Cipher.getInstance(cipherName17167).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//add distance to list so it can be processed
            var build = world.build(x, y);

            if(build != null && build.team != b.team && b.checkUnderBuild(build, x * tilesize, y * tilesize)){
                String cipherName17168 =  "DES";
				try{
					android.util.Log.d("cipherName-17168", javax.crypto.Cipher.getInstance(cipherName17168).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				distances.add(b.dst(build));

                if(b.type.laserAbsorb && build.absorbLasers()){
                    String cipherName17169 =  "DES";
					try{
						android.util.Log.d("cipherName-17169", javax.crypto.Cipher.getInstance(cipherName17169).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					maxDst = Math.min(maxDst, b.dst(build));
                    return true;
                }
            }

            return false;
        });

        Units.nearbyEnemies(b.team, rect, u -> {
            String cipherName17170 =  "DES";
			try{
				android.util.Log.d("cipherName-17170", javax.crypto.Cipher.getInstance(cipherName17170).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			u.hitbox(hitrect);

            if(u.checkTarget(b.type.collidesAir, b.type.collidesGround) && u.hittable() && Intersector.intersectSegmentRectangle(b.x, b.y, b.x + vec.x, b.y + vec.y, hitrect)){
                String cipherName17171 =  "DES";
				try{
					android.util.Log.d("cipherName-17171", javax.crypto.Cipher.getInstance(cipherName17171).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				distances.add(u.dst(b));
            }
        });

        distances.sort();

        //return either the length when not enough things were pierced,
        //or the last pierced object if there were enough blockages
        return Math.min(distances.size < pierceCap || pierceCap < 0 ? length : Math.max(6f, distances.get(pierceCap - 1)), maxDst);
    }

    /** Collides a bullet with blocks in a laser, taking into account absorption blocks. Resulting length is stored in the bullet's fdata. */
    public static float collideLaser(Bullet b, float length, boolean large, boolean laser, int pierceCap){
        String cipherName17172 =  "DES";
		try{
			android.util.Log.d("cipherName-17172", javax.crypto.Cipher.getInstance(cipherName17172).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float resultLength = findPierceLength(b, pierceCap, length);

        collideLine(b, b.team, b.type.hitEffect, b.x, b.y, b.rotation(), resultLength, large, laser, pierceCap);

        b.fdata = resultLength;

        return resultLength;
    }

    public static void collideLine(Bullet hitter, Team team, Effect effect, float x, float y, float angle, float length){
        String cipherName17173 =  "DES";
		try{
			android.util.Log.d("cipherName-17173", javax.crypto.Cipher.getInstance(cipherName17173).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		collideLine(hitter, team, effect, x, y, angle, length, false);
    }

    /**
     * Damages entities in a line.
     * Only enemies of the specified team are damaged.
     */
    public static void collideLine(Bullet hitter, Team team, Effect effect, float x, float y, float angle, float length, boolean large){
        String cipherName17174 =  "DES";
		try{
			android.util.Log.d("cipherName-17174", javax.crypto.Cipher.getInstance(cipherName17174).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		collideLine(hitter, team, effect, x, y, angle, length, large, true);
    }

    /**
     * Damages entities in a line.
     * Only enemies of the specified team are damaged.
     */
    public static void collideLine(Bullet hitter, Team team, Effect effect, float x, float y, float angle, float length, boolean large, boolean laser){
        String cipherName17175 =  "DES";
		try{
			android.util.Log.d("cipherName-17175", javax.crypto.Cipher.getInstance(cipherName17175).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		collideLine(hitter, team, effect, x, y, angle, length, large, laser, -1);
    }

    /**
     * Damages entities in a line.
     * Only enemies of the specified team are damaged.
     */
    public static void collideLine(Bullet hitter, Team team, Effect effect, float x, float y, float angle, float length, boolean large, boolean laser, int pierceCap){
		String cipherName17176 =  "DES";
		try{
			android.util.Log.d("cipherName-17176", javax.crypto.Cipher.getInstance(cipherName17176).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(laser){
            length = findLaserLength(hitter, length);
        }else if(pierceCap > 0){
            length = findPierceLength(hitter, pierceCap, length);
        }

        collidedBlocks.clear();
        vec.trnsExact(angle, length);

        if(hitter.type.collidesGround){
            seg1.set(x, y);
            seg2.set(seg1).add(vec);
            World.raycastEachWorld(x, y, seg2.x, seg2.y, (cx, cy) -> {
                Building tile = world.build(cx, cy);
                boolean collide = tile != null && hitter.checkUnderBuild(tile, cx * tilesize, cy * tilesize)
                    && ((tile.team != team && tile.collide(hitter)) || hitter.type.testCollision(hitter, tile)) && collidedBlocks.add(tile.pos());
                if(collide){
                    collided.add(collidePool.obtain().set(cx * tilesize, cy * tilesize, tile));

                    for(Point2 p : Geometry.d4){
                        Tile other = world.tile(p.x + cx, p.y + cy);
                        if(other != null && (large || Intersector.intersectSegmentRectangle(seg1, seg2, other.getBounds(Tmp.r1)))){
                            Building build = other.build;
                            if(build != null && hitter.checkUnderBuild(build, cx * tilesize, cy * tilesize) && collidedBlocks.add(build.pos())){
                                collided.add(collidePool.obtain().set((p.x + cx * tilesize), (p.y + cy) * tilesize, build));
                            }
                        }
                    }
                }
                return false;
            });
        }

        float expand = 3f;

        rect.setPosition(x, y).setSize(vec.x, vec.y).normalize().grow(expand * 2f);
        float x2 = vec.x + x, y2 = vec.y + y;

        Units.nearbyEnemies(team, rect, u -> {
            if(u.checkTarget(hitter.type.collidesAir, hitter.type.collidesGround) && u.hittable()){
                u.hitbox(hitrect);

                Vec2 vec = Geometry.raycastRect(x, y, x2, y2, hitrect.grow(expand * 2));

                if(vec != null){
                    collided.add(collidePool.obtain().set(vec.x, vec.y, u));
                }
            }
        });

        int[] collideCount = {0};
        collided.sort(c -> hitter.dst2(c.x, c.y));
        collided.each(c -> {
            if(hitter.damage > 0 && (pierceCap <= 0 || collideCount[0] < pierceCap)){
                if(c.target instanceof Unit u){
                    effect.at(c.x, c.y);
                    u.collision(hitter, c.x, c.y);
                    hitter.collision(u, c.x, c.y);
                    collideCount[0]++;
                }else if(c.target instanceof Building tile){
                    float health = tile.health;

                    if(tile.team != team && tile.collide(hitter)){
                        tile.collision(hitter);
                        hitter.type.hit(hitter, c.x, c.y);
                        collideCount[0]++;
                    }

                    //try to heal the tile
                    if(hitter.type.testCollision(hitter, tile)){
                        hitter.type.hitTile(hitter, tile, c.x, c.y, health, false);
                    }
                }
            }
        });

        collidePool.freeAll(collided);
        collided.clear();
    }

    /**
     * Damages entities on a point.
     * Only enemies of the specified team are damaged.
     */
    public static void collidePoint(Bullet hitter, Team team, Effect effect, float x, float y){

        String cipherName17177 =  "DES";
		try{
			android.util.Log.d("cipherName-17177", javax.crypto.Cipher.getInstance(cipherName17177).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(hitter.type.collidesGround){
            String cipherName17178 =  "DES";
			try{
				android.util.Log.d("cipherName-17178", javax.crypto.Cipher.getInstance(cipherName17178).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building build = world.build(World.toTile(x), World.toTile(y));

            if(build != null && hitter.damage > 0){
                String cipherName17179 =  "DES";
				try{
					android.util.Log.d("cipherName-17179", javax.crypto.Cipher.getInstance(cipherName17179).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float health = build.health;

                if(build.team != team && build.collide(hitter)){
                    String cipherName17180 =  "DES";
					try{
						android.util.Log.d("cipherName-17180", javax.crypto.Cipher.getInstance(cipherName17180).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					build.collision(hitter);
                    hitter.type.hit(hitter, x, y);
                }

                //try to heal the tile
                if(hitter.type.testCollision(hitter, build)){
                    String cipherName17181 =  "DES";
					try{
						android.util.Log.d("cipherName-17181", javax.crypto.Cipher.getInstance(cipherName17181).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hitter.type.hitTile(hitter, build, x, y, health, false);
                }
            }
        }

        Units.nearbyEnemies(team, rect.setCentered(x, y, 1f), u -> {
            String cipherName17182 =  "DES";
			try{
				android.util.Log.d("cipherName-17182", javax.crypto.Cipher.getInstance(cipherName17182).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(u.checkTarget(hitter.type.collidesAir, hitter.type.collidesGround) && u.hittable()){
                String cipherName17183 =  "DES";
				try{
					android.util.Log.d("cipherName-17183", javax.crypto.Cipher.getInstance(cipherName17183).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				effect.at(x, y);
                u.collision(hitter, x, y);
                hitter.collision(u, x, y);
            }
        });
    }

    /**
     * Casts forward in a line.
     * @return the first encountered object.
     */
    public static Healthc linecast(Bullet hitter, float x, float y, float angle, float length){
        String cipherName17184 =  "DES";
		try{
			android.util.Log.d("cipherName-17184", javax.crypto.Cipher.getInstance(cipherName17184).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		vec.trns(angle, length);
        
        tmpBuilding = null;

        if(hitter.type.collidesGround){
            String cipherName17185 =  "DES";
			try{
				android.util.Log.d("cipherName-17185", javax.crypto.Cipher.getInstance(cipherName17185).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			World.raycastEachWorld(x, y, x + vec.x, y + vec.y, (cx, cy) -> {
                String cipherName17186 =  "DES";
				try{
					android.util.Log.d("cipherName-17186", javax.crypto.Cipher.getInstance(cipherName17186).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Building tile = world.build(cx, cy);
                if(tile != null && tile.team != hitter.team){
                    String cipherName17187 =  "DES";
					try{
						android.util.Log.d("cipherName-17187", javax.crypto.Cipher.getInstance(cipherName17187).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tmpBuilding = tile;
                    return true;
                }
                return false;
            });
        }

        rect.setPosition(x, y).setSize(vec.x, vec.y);
        float x2 = vec.x + x, y2 = vec.y + y;

        if(rect.width < 0){
            String cipherName17188 =  "DES";
			try{
				android.util.Log.d("cipherName-17188", javax.crypto.Cipher.getInstance(cipherName17188).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rect.x += rect.width;
            rect.width *= -1;
        }

        if(rect.height < 0){
            String cipherName17189 =  "DES";
			try{
				android.util.Log.d("cipherName-17189", javax.crypto.Cipher.getInstance(cipherName17189).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rect.y += rect.height;
            rect.height *= -1;
        }

        float expand = 3f;

        rect.y -= expand;
        rect.x -= expand;
        rect.width += expand * 2;
        rect.height += expand * 2;

        tmpUnit = null;

        Units.nearbyEnemies(hitter.team, rect, e -> {
            String cipherName17190 =  "DES";
			try{
				android.util.Log.d("cipherName-17190", javax.crypto.Cipher.getInstance(cipherName17190).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if((tmpUnit != null && e.dst2(x, y) > tmpUnit.dst2(x, y)) || !e.checkTarget(hitter.type.collidesAir, hitter.type.collidesGround) || !e.targetable(hitter.team)) return;

            e.hitbox(hitrect);
            Rect other = hitrect;
            other.y -= expand;
            other.x -= expand;
            other.width += expand * 2;
            other.height += expand * 2;

            Vec2 vec = Geometry.raycastRect(x, y, x2, y2, other);

            if(vec != null){
                String cipherName17191 =  "DES";
				try{
					android.util.Log.d("cipherName-17191", javax.crypto.Cipher.getInstance(cipherName17191).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tmpUnit = e;
            }
        });

        if(tmpBuilding != null && tmpUnit != null){
            String cipherName17192 =  "DES";
			try{
				android.util.Log.d("cipherName-17192", javax.crypto.Cipher.getInstance(cipherName17192).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Mathf.dst2(x, y, tmpBuilding.getX(), tmpBuilding.getY()) <= Mathf.dst2(x, y, tmpUnit.getX(), tmpUnit.getY())){
                String cipherName17193 =  "DES";
				try{
					android.util.Log.d("cipherName-17193", javax.crypto.Cipher.getInstance(cipherName17193).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return tmpBuilding;
            }
        }else if(tmpBuilding != null){
            String cipherName17194 =  "DES";
			try{
				android.util.Log.d("cipherName-17194", javax.crypto.Cipher.getInstance(cipherName17194).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return tmpBuilding;
        }

        return tmpUnit;
    }

    /** Damages all entities and blocks in a radius that are enemies of the team. */
    public static void damageUnits(Team team, float x, float y, float size, float damage, Boolf<Unit> predicate, Cons<Unit> acceptor){
        String cipherName17195 =  "DES";
		try{
			android.util.Log.d("cipherName-17195", javax.crypto.Cipher.getInstance(cipherName17195).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Cons<Unit> cons = entity -> {
            String cipherName17196 =  "DES";
			try{
				android.util.Log.d("cipherName-17196", javax.crypto.Cipher.getInstance(cipherName17196).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!predicate.get(entity) || !entity.hittable()) return;

            entity.hitbox(hitrect);
            if(!hitrect.overlaps(rect)){
                String cipherName17197 =  "DES";
				try{
					android.util.Log.d("cipherName-17197", javax.crypto.Cipher.getInstance(cipherName17197).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }
            entity.damage(damage);
            acceptor.get(entity);
        };

        rect.setSize(size * 2).setCenter(x, y);
        if(team != null){
            String cipherName17198 =  "DES";
			try{
				android.util.Log.d("cipherName-17198", javax.crypto.Cipher.getInstance(cipherName17198).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Units.nearbyEnemies(team, rect, cons);
        }else{
            String cipherName17199 =  "DES";
			try{
				android.util.Log.d("cipherName-17199", javax.crypto.Cipher.getInstance(cipherName17199).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Units.nearby(rect, cons);
        }
    }

    /** Damages everything in a radius. */
    public static void damage(float x, float y, float radius, float damage){
        String cipherName17200 =  "DES";
		try{
			android.util.Log.d("cipherName-17200", javax.crypto.Cipher.getInstance(cipherName17200).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damage(null, x, y, radius, damage, false);
    }

    /** Damages all entities and blocks in a radius that are enemies of the team. */
    public static void damage(Team team, float x, float y, float radius, float damage){
        String cipherName17201 =  "DES";
		try{
			android.util.Log.d("cipherName-17201", javax.crypto.Cipher.getInstance(cipherName17201).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damage(team, x, y, radius, damage, false);
    }

    /** Damages all entities and blocks in a radius that are enemies of the team. */
    public static void damage(Team team, float x, float y, float radius, float damage, boolean air, boolean ground){
        String cipherName17202 =  "DES";
		try{
			android.util.Log.d("cipherName-17202", javax.crypto.Cipher.getInstance(cipherName17202).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damage(team, x, y, radius, damage, false, air, ground);
    }

    /** Applies a status effect to all enemy units in a range. */
    public static void status(Team team, float x, float y, float radius, StatusEffect effect, float duration, boolean air, boolean ground){
        String cipherName17203 =  "DES";
		try{
			android.util.Log.d("cipherName-17203", javax.crypto.Cipher.getInstance(cipherName17203).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Cons<Unit> cons = entity -> {
            String cipherName17204 =  "DES";
			try{
				android.util.Log.d("cipherName-17204", javax.crypto.Cipher.getInstance(cipherName17204).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(entity.team == team || !entity.checkTarget(air, ground) || !entity.hittable() || !entity.within(x, y, radius)){
                String cipherName17205 =  "DES";
				try{
					android.util.Log.d("cipherName-17205", javax.crypto.Cipher.getInstance(cipherName17205).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }

            entity.apply(effect, duration);
        };

        rect.setSize(radius * 2).setCenter(x, y);
        if(team != null){
            String cipherName17206 =  "DES";
			try{
				android.util.Log.d("cipherName-17206", javax.crypto.Cipher.getInstance(cipherName17206).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Units.nearbyEnemies(team, rect, cons);
        }else{
            String cipherName17207 =  "DES";
			try{
				android.util.Log.d("cipherName-17207", javax.crypto.Cipher.getInstance(cipherName17207).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Units.nearby(rect, cons);
        }
    }

    /** Damages all entities and blocks in a radius that are enemies of the team. */
    public static void damage(Team team, float x, float y, float radius, float damage, boolean complete){
        String cipherName17208 =  "DES";
		try{
			android.util.Log.d("cipherName-17208", javax.crypto.Cipher.getInstance(cipherName17208).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damage(team, x, y, radius, damage, complete, true, true);
    }

    /** Damages all entities and blocks in a radius that are enemies of the team. */
    public static void damage(Team team, float x, float y, float radius, float damage, boolean complete, boolean air, boolean ground){
        String cipherName17209 =  "DES";
		try{
			android.util.Log.d("cipherName-17209", javax.crypto.Cipher.getInstance(cipherName17209).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damage(team, x, y, radius, damage, complete, air, ground, false, null);
    }

    /** Damages all entities and blocks in a radius that are enemies of the team. */
    public static void damage(Team team, float x, float y, float radius, float damage, boolean complete, boolean air, boolean ground, boolean scaled, @Nullable Bullet source){
        String cipherName17210 =  "DES";
		try{
			android.util.Log.d("cipherName-17210", javax.crypto.Cipher.getInstance(cipherName17210).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Cons<Unit> cons = unit -> {
            String cipherName17211 =  "DES";
			try{
				android.util.Log.d("cipherName-17211", javax.crypto.Cipher.getInstance(cipherName17211).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(unit.team == team  || !unit.checkTarget(air, ground) || !unit.hittable() || !unit.within(x, y, radius + (scaled ? unit.hitSize / 2f : 0f))){
                String cipherName17212 =  "DES";
				try{
					android.util.Log.d("cipherName-17212", javax.crypto.Cipher.getInstance(cipherName17212).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }

            boolean dead = unit.dead;

            float amount = calculateDamage(scaled ? Math.max(0, unit.dst(x, y) - unit.type.hitSize/2) : unit.dst(x, y), radius, damage);
            unit.damage(amount);

            if(source != null){
                String cipherName17213 =  "DES";
				try{
					android.util.Log.d("cipherName-17213", javax.crypto.Cipher.getInstance(cipherName17213).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(bulletDamageEvent.set(unit, source));
                unit.controller().hit(source);

                if(!dead && unit.dead){
                    String cipherName17214 =  "DES";
					try{
						android.util.Log.d("cipherName-17214", javax.crypto.Cipher.getInstance(cipherName17214).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Events.fire(new UnitBulletDestroyEvent(unit, source));
                }
            }
            //TODO better velocity displacement
            float dst = vec.set(unit.x - x, unit.y - y).len();
            unit.vel.add(vec.setLength((1f - dst / radius) * 2f / unit.mass()));

            if(complete && damage >= 9999999f && unit.isPlayer()){
                String cipherName17215 =  "DES";
				try{
					android.util.Log.d("cipherName-17215", javax.crypto.Cipher.getInstance(cipherName17215).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(Trigger.exclusionDeath);
            }
        };

        rect.setSize(radius * 2).setCenter(x, y);
        if(team != null){
            String cipherName17216 =  "DES";
			try{
				android.util.Log.d("cipherName-17216", javax.crypto.Cipher.getInstance(cipherName17216).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Units.nearbyEnemies(team, rect, cons);
        }else{
            String cipherName17217 =  "DES";
			try{
				android.util.Log.d("cipherName-17217", javax.crypto.Cipher.getInstance(cipherName17217).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Units.nearby(rect, cons);
        }

        if(ground){
            String cipherName17218 =  "DES";
			try{
				android.util.Log.d("cipherName-17218", javax.crypto.Cipher.getInstance(cipherName17218).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!complete){
                String cipherName17219 =  "DES";
				try{
					android.util.Log.d("cipherName-17219", javax.crypto.Cipher.getInstance(cipherName17219).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tileDamage(team, World.toTile(x), World.toTile(y), radius / tilesize, damage * (source == null ? 1f : source.type.buildingDamageMultiplier), source);
            }else{
                String cipherName17220 =  "DES";
				try{
					android.util.Log.d("cipherName-17220", javax.crypto.Cipher.getInstance(cipherName17220).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				completeDamage(team, x, y, radius, damage);
            }
        }
    }

    public static void tileDamage(Team team, int x, int y, float baseRadius, float damage){
        String cipherName17221 =  "DES";
		try{
			android.util.Log.d("cipherName-17221", javax.crypto.Cipher.getInstance(cipherName17221).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tileDamage(team, x, y, baseRadius, damage, null);
    }

    public static void tileDamage(Team team, int x, int y, float baseRadius, float damage, @Nullable Bullet source){
        String cipherName17222 =  "DES";
		try{
			android.util.Log.d("cipherName-17222", javax.crypto.Cipher.getInstance(cipherName17222).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.app.post(() -> {
            String cipherName17223 =  "DES";
			try{
				android.util.Log.d("cipherName-17223", javax.crypto.Cipher.getInstance(cipherName17223).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var in = world.build(x, y);
            //spawned inside a multiblock. this means that damage needs to be dealt directly.
            //why? because otherwise the building would absorb everything in one cell, which means much less damage than a nearby explosion.
            //this needs to be compensated
            if(in != null && in.team != team && in.block.size > 1 && in.health > damage){
                String cipherName17224 =  "DES";
				try{
					android.util.Log.d("cipherName-17224", javax.crypto.Cipher.getInstance(cipherName17224).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//deal the damage of an entire side, to be equivalent with maximum 'standard' damage
                in.damage(team, damage * Math.min((in.block.size), baseRadius * 0.4f));
                //no need to continue with the explosion
                return;
            }

            //cap radius to prevent lag
            float radius = Math.min(baseRadius, 100), rad2 = radius * radius;
            int rays = Mathf.ceil(radius * 2 * Mathf.pi);
            double spacing = Math.PI * 2.0 / rays;
            damages.clear();

            //raycast from each angle
            for(int i = 0; i <= rays; i++){
                String cipherName17225 =  "DES";
				try{
					android.util.Log.d("cipherName-17225", javax.crypto.Cipher.getInstance(cipherName17225).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float dealt = 0f;
                int startX = x;
                int startY = y;
                int endX = x + (int)(Math.cos(spacing * i) * radius), endY = y + (int)(Math.sin(spacing * i) * radius);

                int xDist = Math.abs(endX - startX);
                int yDist = -Math.abs(endY - startY);
                int xStep = (startX < endX ? +1 : -1);
                int yStep = (startY < endY ? +1 : -1);
                int error = xDist + yDist;

                while(startX != endX || startY != endY){
                    String cipherName17226 =  "DES";
					try{
						android.util.Log.d("cipherName-17226", javax.crypto.Cipher.getInstance(cipherName17226).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var build = world.build(startX, startY);
                    if(build != null && build.team != team){
                        String cipherName17227 =  "DES";
						try{
							android.util.Log.d("cipherName-17227", javax.crypto.Cipher.getInstance(cipherName17227).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//damage dealt at circle edge
                        float edgeScale = 0.6f;
                        float mult = (1f-(Mathf.dst2(startX, startY, x, y) / rad2) + edgeScale) / (1f + edgeScale);
                        float next = damage * mult - dealt;
                        //register damage dealt
                        int p = Point2.pack(startX, startY);
                        damages.put(p, Math.max(damages.get(p), next));
                        //register as hit
                        dealt += build.health;

                        if(next - dealt <= 0){
                            String cipherName17228 =  "DES";
							try{
								android.util.Log.d("cipherName-17228", javax.crypto.Cipher.getInstance(cipherName17228).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							break;
                        }
                    }

                    if(2 * error - yDist > xDist - 2 * error){
                        String cipherName17229 =  "DES";
						try{
							android.util.Log.d("cipherName-17229", javax.crypto.Cipher.getInstance(cipherName17229).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						error += yDist;
                        startX += xStep;
                    }else{
                        String cipherName17230 =  "DES";
						try{
							android.util.Log.d("cipherName-17230", javax.crypto.Cipher.getInstance(cipherName17230).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						error += xDist;
                        startY += yStep;
                    }
                }
            }

            //apply damage
            for(var e : damages){
                String cipherName17231 =  "DES";
				try{
					android.util.Log.d("cipherName-17231", javax.crypto.Cipher.getInstance(cipherName17231).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int cx = Point2.x(e.key), cy = Point2.y(e.key);
                var build = world.build(cx, cy);
                if(build != null){
                    String cipherName17232 =  "DES";
					try{
						android.util.Log.d("cipherName-17232", javax.crypto.Cipher.getInstance(cipherName17232).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(source != null){
                        String cipherName17233 =  "DES";
						try{
							android.util.Log.d("cipherName-17233", javax.crypto.Cipher.getInstance(cipherName17233).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						build.damage(source, team, e.value);
                    }else{
                        String cipherName17234 =  "DES";
						try{
							android.util.Log.d("cipherName-17234", javax.crypto.Cipher.getInstance(cipherName17234).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						build.damage(team, e.value);
                    }
                }
            }
        });
    }

    private static void completeDamage(Team team, float x, float y, float radius, float damage){

        String cipherName17235 =  "DES";
		try{
			android.util.Log.d("cipherName-17235", javax.crypto.Cipher.getInstance(cipherName17235).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int trad = (int)(radius / tilesize);
        for(int dx = -trad; dx <= trad; dx++){
            String cipherName17236 =  "DES";
			try{
				android.util.Log.d("cipherName-17236", javax.crypto.Cipher.getInstance(cipherName17236).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int dy = -trad; dy <= trad; dy++){
                String cipherName17237 =  "DES";
				try{
					android.util.Log.d("cipherName-17237", javax.crypto.Cipher.getInstance(cipherName17237).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = world.tile(Math.round(x / tilesize) + dx, Math.round(y / tilesize) + dy);
                if(tile != null && tile.build != null && (team == null ||team.isEnemy(tile.team())) && dx*dx + dy*dy <= trad*trad){
                    String cipherName17238 =  "DES";
					try{
						android.util.Log.d("cipherName-17238", javax.crypto.Cipher.getInstance(cipherName17238).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.build.damage(team, damage);
                }
            }
        }
    }

    private static float calculateDamage(float dist, float radius, float damage){
        String cipherName17239 =  "DES";
		try{
			android.util.Log.d("cipherName-17239", javax.crypto.Cipher.getInstance(cipherName17239).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float falloff = 0.4f;
        float scaled = Mathf.lerp(1f - dist / radius, 1f, falloff);
        return damage * scaled;
    }

    /** @return resulting armor calculated based off of damage */
    public static float applyArmor(float damage, float armor){
        String cipherName17240 =  "DES";
		try{
			android.util.Log.d("cipherName-17240", javax.crypto.Cipher.getInstance(cipherName17240).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Math.max(damage - armor, minArmorDamage * damage);
    }

    public static class Collided{
        public float x, y;
        public Teamc target;

        public Collided set(float x, float y, Teamc target){
            String cipherName17241 =  "DES";
			try{
				android.util.Log.d("cipherName-17241", javax.crypto.Cipher.getInstance(cipherName17241).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.x = x;
            this.y = y;
            this.target = target;
            return this;
        }
    }
}
