package mindustry.entities.comp;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

@EntityDef(value = {Bulletc.class}, pooled = true, serialize = false)
@Component(base = true)
abstract class BulletComp implements Timedc, Damagec, Hitboxc, Teamc, Posc, Drawc, Shielderc, Ownerc, Velc, Bulletc, Timerc{
    @Import Team team;
    @Import Entityc owner;
    @Import float x, y, damage, lastX, lastY, time, lifetime;
    @Import Vec2 vel;

    IntSeq collided = new IntSeq(6);
    BulletType type;

    Object data;
    float fdata;

    @ReadOnly
    private float rotation;

    //setting this variable to true prevents lifetime from decreasing for a frame.
    transient boolean keepAlive;
    transient @Nullable Tile aimTile;
    transient float aimX, aimY;
    transient float originX, originY;
    transient @Nullable Mover mover;
    transient boolean absorbed, hit;
    transient @Nullable Trail trail;

    @Override
    public void getCollisions(Cons<QuadTree> consumer){
        String cipherName16733 =  "DES";
		try{
			android.util.Log.d("cipherName-16733", javax.crypto.Cipher.getInstance(cipherName16733).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<TeamData> data = state.teams.present;
        for(int i = 0; i < data.size; i++){
            String cipherName16734 =  "DES";
			try{
				android.util.Log.d("cipherName-16734", javax.crypto.Cipher.getInstance(cipherName16734).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(data.items[i].team != team){
                String cipherName16735 =  "DES";
				try{
					android.util.Log.d("cipherName-16735", javax.crypto.Cipher.getInstance(cipherName16735).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				consumer.get(data.items[i].tree());
            }
        }
    }

    //bullets always considered local
    @Override
    @Replace
    public boolean isLocal(){
        String cipherName16736 =  "DES";
		try{
			android.util.Log.d("cipherName-16736", javax.crypto.Cipher.getInstance(cipherName16736).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    @Override
    public void add(){
        String cipherName16737 =  "DES";
		try{
			android.util.Log.d("cipherName-16737", javax.crypto.Cipher.getInstance(cipherName16737).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		type.init(self());
    }

    @Override
    public void remove(){
        String cipherName16738 =  "DES";
		try{
			android.util.Log.d("cipherName-16738", javax.crypto.Cipher.getInstance(cipherName16738).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Groups.isClearing) return;

        //'despawned' only counts when the bullet is killed externally or reaches the end of life
        if(!hit){
            String cipherName16739 =  "DES";
			try{
				android.util.Log.d("cipherName-16739", javax.crypto.Cipher.getInstance(cipherName16739).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			type.despawned(self());
        }
        type.removed(self());
        collided.clear();
    }

    @Override
    public float damageMultiplier(){
        String cipherName16740 =  "DES";
		try{
			android.util.Log.d("cipherName-16740", javax.crypto.Cipher.getInstance(cipherName16740).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.damageMultiplier(self());
    }

    @Override
    public void absorb(){
        String cipherName16741 =  "DES";
		try{
			android.util.Log.d("cipherName-16741", javax.crypto.Cipher.getInstance(cipherName16741).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		absorbed = true;
        remove();
    }

    public boolean hasCollided(int id){
        String cipherName16742 =  "DES";
		try{
			android.util.Log.d("cipherName-16742", javax.crypto.Cipher.getInstance(cipherName16742).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return collided.size != 0 && collided.contains(id);
    }

    @Replace
    public float clipSize(){
        String cipherName16743 =  "DES";
		try{
			android.util.Log.d("cipherName-16743", javax.crypto.Cipher.getInstance(cipherName16743).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.drawSize;
    }

    @Replace
    @Override
    public boolean collides(Hitboxc other){
		String cipherName16744 =  "DES";
		try{
			android.util.Log.d("cipherName-16744", javax.crypto.Cipher.getInstance(cipherName16744).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return type.collides && (other instanceof Teamc t && t.team() != team)
            && !(other instanceof Flyingc f && !f.checkTarget(type.collidesAir, type.collidesGround))
            && !(type.pierce && hasCollided(other.id())); //prevent multiple collisions
    }

    @MethodPriority(100)
    @Override
    public void collision(Hitboxc other, float x, float y){
		String cipherName16745 =  "DES";
		try{
			android.util.Log.d("cipherName-16745", javax.crypto.Cipher.getInstance(cipherName16745).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        type.hit(self(), x, y);

        //must be last.
        if(!type.pierce){
            hit = true;
            remove();
        }else{
            collided.add(other.id());
        }

        type.hitEntity(self(), other, other instanceof Healthc h ? h.health() : 0f);
    }

    @Override
    public void update(){
        String cipherName16746 =  "DES";
		try{
			android.util.Log.d("cipherName-16746", javax.crypto.Cipher.getInstance(cipherName16746).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(mover != null){
            String cipherName16747 =  "DES";
			try{
				android.util.Log.d("cipherName-16747", javax.crypto.Cipher.getInstance(cipherName16747).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mover.move(self());
        }

        type.update(self());

        if(type.collidesTiles && type.collides && type.collidesGround){
            String cipherName16748 =  "DES";
			try{
				android.util.Log.d("cipherName-16748", javax.crypto.Cipher.getInstance(cipherName16748).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tileRaycast(World.toTile(lastX), World.toTile(lastY), tileX(), tileY());
        }

        if(type.removeAfterPierce && type.pierceCap != -1 && collided.size >= type.pierceCap){
            String cipherName16749 =  "DES";
			try{
				android.util.Log.d("cipherName-16749", javax.crypto.Cipher.getInstance(cipherName16749).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hit = true;
            remove();
        }

        if(keepAlive){
            String cipherName16750 =  "DES";
			try{
				android.util.Log.d("cipherName-16750", javax.crypto.Cipher.getInstance(cipherName16750).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			time -= Time.delta;
            keepAlive = false;
        }
    }

    public void moveRelative(float x, float y){
        String cipherName16751 =  "DES";
		try{
			android.util.Log.d("cipherName-16751", javax.crypto.Cipher.getInstance(cipherName16751).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float rot = rotation();
        this.x += Angles.trnsx(rot, x * Time.delta, y * Time.delta);
        this.y += Angles.trnsy(rot, x * Time.delta, y * Time.delta);
    }

    public void turn(float x, float y){
        String cipherName16752 =  "DES";
		try{
			android.util.Log.d("cipherName-16752", javax.crypto.Cipher.getInstance(cipherName16752).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float ang = vel.angle();
        vel.add(Angles.trnsx(ang, x * Time.delta, y * Time.delta), Angles.trnsy(ang, x * Time.delta, y * Time.delta)).limit(type.speed);
    }

    public boolean checkUnderBuild(Building build, float x, float y){
        String cipherName16753 =  "DES";
		try{
			android.util.Log.d("cipherName-16753", javax.crypto.Cipher.getInstance(cipherName16753).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return
            (!build.block.underBullets ||
            //direct hit on correct tile
            (aimTile != null && aimTile.build == build) ||
            //same team has no 'under build' mechanics
            (build.team == team) ||
            //a piercing bullet overshot the aim tile, it's fine to hit things now
            (type.pierce && aimTile != null && Mathf.dst(x, y, originX, originY) > aimTile.dst(originX, originY) + 2f) ||
            //there was nothing to aim at
            (aimX == -1f && aimY == -1f));
    }

    //copy-paste of World#raycastEach, inlined for lambda capture performance.
    @Override
    public void tileRaycast(int x1, int y1, int x2, int y2){
        String cipherName16754 =  "DES";
		try{
			android.util.Log.d("cipherName-16754", javax.crypto.Cipher.getInstance(cipherName16754).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int x = x1, dx = Math.abs(x2 - x), sx = x < x2 ? 1 : -1;
        int y = y1, dy = Math.abs(y2 - y), sy = y < y2 ? 1 : -1;
        int e2, err = dx - dy;
        int ww = world.width(), wh = world.height();

        while(x >= 0 && y >= 0 && x < ww && y < wh){
            String cipherName16755 =  "DES";
			try{
				android.util.Log.d("cipherName-16755", javax.crypto.Cipher.getInstance(cipherName16755).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building build = world.build(x, y);

            if(type.collideFloor || type.collideTerrain){
                String cipherName16756 =  "DES";
				try{
					android.util.Log.d("cipherName-16756", javax.crypto.Cipher.getInstance(cipherName16756).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = world.tile(x, y);
                if(
                    type.collideFloor && (tile == null || tile.floor().hasSurface() || tile.block() != Blocks.air) ||
                    type.collideTerrain && tile != null && tile.block() instanceof StaticWall
                ){
                    String cipherName16757 =  "DES";
					try{
						android.util.Log.d("cipherName-16757", javax.crypto.Cipher.getInstance(cipherName16757).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					remove();
                    hit = true;
                    return;
                }
            }

            if(build != null && isAdded()
                && checkUnderBuild(build, x, y)
                && build.collide(self()) && type.testCollision(self(), build)
                && !build.dead() && (type.collidesTeam || build.team != team) && !(type.pierceBuilding && hasCollided(build.id))){

                String cipherName16758 =  "DES";
					try{
						android.util.Log.d("cipherName-16758", javax.crypto.Cipher.getInstance(cipherName16758).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
				boolean remove = false;
                float health = build.health;

                if(build.team != team){
                    String cipherName16759 =  "DES";
					try{
						android.util.Log.d("cipherName-16759", javax.crypto.Cipher.getInstance(cipherName16759).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					remove = build.collision(self());
                }

                if(remove || type.collidesTeam){
                    String cipherName16760 =  "DES";
					try{
						android.util.Log.d("cipherName-16760", javax.crypto.Cipher.getInstance(cipherName16760).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(Mathf.dst2(lastX, lastY, x * tilesize, y * tilesize) < Mathf.dst2(lastX, lastY, this.x, this.y)){
                        String cipherName16761 =  "DES";
						try{
							android.util.Log.d("cipherName-16761", javax.crypto.Cipher.getInstance(cipherName16761).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						this.x = x * tilesize;
                        this.y = y * tilesize;
                    }

                    if(!type.pierceBuilding){
                        String cipherName16762 =  "DES";
						try{
							android.util.Log.d("cipherName-16762", javax.crypto.Cipher.getInstance(cipherName16762).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						hit = true;
                        remove();
                    }else{
                        String cipherName16763 =  "DES";
						try{
							android.util.Log.d("cipherName-16763", javax.crypto.Cipher.getInstance(cipherName16763).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						collided.add(build.id);
                    }
                }

                type.hitTile(self(), build, x * tilesize, y * tilesize, health, true);

                //stop raycasting when building is hit
                if(type.pierceBuilding) return;
            }

            if(x == x2 && y == y2) break;

            e2 = 2 * err;
            if(e2 > -dy){
                String cipherName16764 =  "DES";
				try{
					android.util.Log.d("cipherName-16764", javax.crypto.Cipher.getInstance(cipherName16764).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err -= dy;
                x += sx;
            }

            if(e2 < dx){
                String cipherName16765 =  "DES";
				try{
					android.util.Log.d("cipherName-16765", javax.crypto.Cipher.getInstance(cipherName16765).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err += dx;
                y += sy;
            }
        }
    }

    @Override
    public void draw(){
        String cipherName16766 =  "DES";
		try{
			android.util.Log.d("cipherName-16766", javax.crypto.Cipher.getInstance(cipherName16766).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.z(type.layer);

        type.draw(self());
        type.drawLight(self());
        
        Draw.reset();
    }

    public void initVel(float angle, float amount){
        String cipherName16767 =  "DES";
		try{
			android.util.Log.d("cipherName-16767", javax.crypto.Cipher.getInstance(cipherName16767).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		vel.trns(angle, amount);
        rotation = angle;
    }

    /** Sets the bullet's rotation in degrees. */
    @Override
    public void rotation(float angle){
        String cipherName16768 =  "DES";
		try{
			android.util.Log.d("cipherName-16768", javax.crypto.Cipher.getInstance(cipherName16768).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		vel.setAngle(rotation = angle);
    }

    /** @return the bullet's rotation. */
    @Override
    public float rotation(){
        String cipherName16769 =  "DES";
		try{
			android.util.Log.d("cipherName-16769", javax.crypto.Cipher.getInstance(cipherName16769).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return vel.isZero(0.001f) ? rotation : vel.angle();
    }
}
