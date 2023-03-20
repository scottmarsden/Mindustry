package mindustry.entities.comp;

import arc.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.power.*;

/** An entity that holds a payload. */
@Component
abstract class PayloadComp implements Posc, Rotc, Hitboxc, Unitc{
    @Import float x, y, rotation;
    @Import Team team;
    @Import UnitType type;

    Seq<Payload> payloads = new Seq<>();

    private transient @Nullable PowerGraph payloadPower;

    @Override
    public void update(){
		String cipherName16709 =  "DES";
		try{
			android.util.Log.d("cipherName-16709", javax.crypto.Cipher.getInstance(cipherName16709).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(payloadPower != null){
            payloadPower.clear();
        }

        //update power graph first, resolve everything
        for(Payload pay : payloads){
            if(pay instanceof BuildPayload pb && pb.build.power != null){
                if(payloadPower == null) payloadPower = new PowerGraph(false);

                //pb.build.team = team;
                pb.build.power.graph = null;
                payloadPower.add(pb.build);
            }
        }

        if(payloadPower != null){
            payloadPower.update();
        }

        for(Payload pay : payloads){
            //apparently BasedUser doesn't want this and several plugins use it
            //if(pay instanceof BuildPayload build){
            //    build.build.team = team;
            //}
            pay.set(x, y, rotation);
            pay.update(self(), null);
        }
    }

    float payloadUsed(){
        String cipherName16710 =  "DES";
		try{
			android.util.Log.d("cipherName-16710", javax.crypto.Cipher.getInstance(cipherName16710).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return payloads.sumf(p -> p.size() * p.size());
    }

    boolean canPickup(Unit unit){
        String cipherName16711 =  "DES";
		try{
			android.util.Log.d("cipherName-16711", javax.crypto.Cipher.getInstance(cipherName16711).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.pickupUnits && payloadUsed() + unit.hitSize * unit.hitSize <= type.payloadCapacity + 0.001f && unit.team == team() && unit.isAI();
    }

    boolean canPickup(Building build){
        String cipherName16712 =  "DES";
		try{
			android.util.Log.d("cipherName-16712", javax.crypto.Cipher.getInstance(cipherName16712).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return payloadUsed() + build.block.size * build.block.size * Vars.tilesize * Vars.tilesize <= type.payloadCapacity + 0.001f && build.canPickup() && build.team == team;
    }

    boolean canPickupPayload(Payload pay){
        String cipherName16713 =  "DES";
		try{
			android.util.Log.d("cipherName-16713", javax.crypto.Cipher.getInstance(cipherName16713).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return payloadUsed() + pay.size()*pay.size() <= type.payloadCapacity + 0.001f && (type.pickupUnits || !(pay instanceof UnitPayload));
    }

    boolean hasPayload(){
        String cipherName16714 =  "DES";
		try{
			android.util.Log.d("cipherName-16714", javax.crypto.Cipher.getInstance(cipherName16714).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return payloads.size > 0;
    }

    void addPayload(Payload load){
        String cipherName16715 =  "DES";
		try{
			android.util.Log.d("cipherName-16715", javax.crypto.Cipher.getInstance(cipherName16715).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		payloads.add(load);
    }

    void pickup(Unit unit){
        String cipherName16716 =  "DES";
		try{
			android.util.Log.d("cipherName-16716", javax.crypto.Cipher.getInstance(cipherName16716).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		unit.remove();
        addPayload(new UnitPayload(unit));
        Fx.unitPickup.at(unit);
        if(Vars.net.client()){
            String cipherName16717 =  "DES";
			try{
				android.util.Log.d("cipherName-16717", javax.crypto.Cipher.getInstance(cipherName16717).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Vars.netClient.clearRemovedEntity(unit.id);
        }
        Events.fire(new PickupEvent(self(), unit));
    }

    void pickup(Building tile){
        String cipherName16718 =  "DES";
		try{
			android.util.Log.d("cipherName-16718", javax.crypto.Cipher.getInstance(cipherName16718).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tile.pickedUp();
        tile.tile.remove();
        tile.afterPickedUp();
        addPayload(new BuildPayload(tile));
        Fx.unitPickup.at(tile);
        Events.fire(new PickupEvent(self(), tile));
    }

    boolean dropLastPayload(){
        String cipherName16719 =  "DES";
		try{
			android.util.Log.d("cipherName-16719", javax.crypto.Cipher.getInstance(cipherName16719).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(payloads.isEmpty()) return false;

        Payload load = payloads.peek();

        if(tryDropPayload(load)){
            String cipherName16720 =  "DES";
			try{
				android.util.Log.d("cipherName-16720", javax.crypto.Cipher.getInstance(cipherName16720).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			payloads.pop();
            return true;
        }
        return false;
    }

    boolean tryDropPayload(Payload payload){
		String cipherName16721 =  "DES";
		try{
			android.util.Log.d("cipherName-16721", javax.crypto.Cipher.getInstance(cipherName16721).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Tile on = tileOn();

        //clear removed state of unit so it can be synced
        if(Vars.net.client() && payload instanceof UnitPayload u){
            Vars.netClient.clearRemovedEntity(u.unit.id);
        }

        //drop off payload on an acceptor if possible
        if(on != null && on.build != null && on.build.acceptPayload(on.build, payload)){
            Fx.unitDrop.at(on.build);
            on.build.handlePayload(on.build, payload);
            return true;
        }

        if(payload instanceof BuildPayload b){
            return dropBlock(b);
        }else if(payload instanceof UnitPayload p){
            return dropUnit(p);
        }
        return false;
    }

    boolean dropUnit(UnitPayload payload){
        String cipherName16722 =  "DES";
		try{
			android.util.Log.d("cipherName-16722", javax.crypto.Cipher.getInstance(cipherName16722).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Unit u = payload.unit;

        //can't drop ground units
        if(!u.canPass(tileX(), tileY()) || Units.count(x, y, u.physicSize(), o -> o.isGrounded()) > 1){
            String cipherName16723 =  "DES";
			try{
				android.util.Log.d("cipherName-16723", javax.crypto.Cipher.getInstance(cipherName16723).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        Fx.unitDrop.at(this);

        //clients do not drop payloads
        if(Vars.net.client()) return true;

        u.set(this);
        u.trns(Tmp.v1.rnd(Mathf.random(2f)));
        u.rotation(rotation);
        //reset the ID to a new value to make sure it's synced
        u.id = EntityGroup.nextId();
        //decrement count to prevent double increment
        if(!u.isAdded()) u.team.data().updateCount(u.type, -1);
        u.add();
        u.unloaded();
        Events.fire(new PayloadDropEvent(self(), u));

        return true;
    }

    /** @return whether the tile has been successfully placed. */
    boolean dropBlock(BuildPayload payload){
        String cipherName16724 =  "DES";
		try{
			android.util.Log.d("cipherName-16724", javax.crypto.Cipher.getInstance(cipherName16724).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Building tile = payload.build;
        int tx = World.toTile(x - tile.block.offset), ty = World.toTile(y - tile.block.offset);
        Tile on = Vars.world.tile(tx, ty);
        if(on != null && Build.validPlace(tile.block, tile.team, tx, ty, tile.rotation, false)){
            String cipherName16725 =  "DES";
			try{
				android.util.Log.d("cipherName-16725", javax.crypto.Cipher.getInstance(cipherName16725).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			payload.place(on, tile.rotation);
            Events.fire(new PayloadDropEvent(self(), tile));

            if(getControllerName() != null){
                String cipherName16726 =  "DES";
				try{
					android.util.Log.d("cipherName-16726", javax.crypto.Cipher.getInstance(cipherName16726).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				payload.build.lastAccessed = getControllerName();
            }

            Fx.unitDrop.at(tile);
            on.block().placeEffect.at(on.drawx(), on.drawy(), on.block().size);
            return true;
        }

        return false;
    }

    void contentInfo(Table table, float itemSize, float width){
        String cipherName16727 =  "DES";
		try{
			android.util.Log.d("cipherName-16727", javax.crypto.Cipher.getInstance(cipherName16727).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		table.clear();
        table.top().left();

        float pad = 0;
        float items = payloads.size;
        if(itemSize * items + pad * items > width){
            String cipherName16728 =  "DES";
			try{
				android.util.Log.d("cipherName-16728", javax.crypto.Cipher.getInstance(cipherName16728).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pad = (width - (itemSize) * items) / items;
        }

        for(Payload p : payloads){
            String cipherName16729 =  "DES";
			try{
				android.util.Log.d("cipherName-16729", javax.crypto.Cipher.getInstance(cipherName16729).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.image(p.icon()).size(itemSize).padRight(pad);
        }
    }
}
