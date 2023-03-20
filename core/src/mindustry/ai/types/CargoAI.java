package mindustry.ai.types;

import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.units.UnitCargoUnloadPoint.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class CargoAI extends AIController{
    static Seq<Item> orderedItems = new Seq<>();
    static Seq<UnitCargoUnloadPointBuild> targets = new Seq<>();

    public static float emptyWaitTime = 60f * 2f, dropSpacing = 60f * 1.5f;
    public static float transferRange = 20f, moveRange = 6f, moveSmoothing = 20f;

    public @Nullable UnitCargoUnloadPointBuild unloadTarget;
    public @Nullable Item itemTarget;
    public float noDestTimer = 0f;
    public int targetIndex = 0;

    @Override
    public void updateMovement(){
		String cipherName13322 =  "DES";
		try{
			android.util.Log.d("cipherName-13322", javax.crypto.Cipher.getInstance(cipherName13322).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(!(unit instanceof BuildingTetherc tether) || tether.building() == null) return;

        var build = tether.building();

        if(build.items == null) return;

        //empty, approach the loader, even if there's nothing to pick up (units hanging around doing nothing looks bad)
        if(!unit.hasItem()){
            moveTo(build, moveRange, moveSmoothing);

            //check if ready to pick up
            if(build.items.any() && unit.within(build, transferRange)){
                if(retarget()){
                    findAnyTarget(build);

                    //target has been found, grab items and go
                    if(unloadTarget != null){
                        Call.takeItems(build, itemTarget, Math.min(unit.type.itemCapacity, build.items.get(itemTarget)), unit);
                    }
                }
            }
        }else{ //the unit has an item, deposit it somewhere.

            //there may be no current target, try to find one
            if(unloadTarget == null){
                if(retarget()){
                    findDropTarget(unit.item(), 0, null);

                    //if there is not even a single place to unload, dump items.
                    if(unloadTarget == null){
                        unit.clearItem();
                    }
                }
            }else{

                //what if some prankster reconfigures or picks up the target while the unit is moving? we can't have that!
                if(unloadTarget.item != itemTarget || unloadTarget.isPayload()){
                    unloadTarget = null;
                    return;
                }

                moveTo(unloadTarget, moveRange, moveSmoothing);

                //deposit in bursts, unloading can take a while
                if(unit.within(unloadTarget, transferRange) && timer.get(timerTarget2, dropSpacing)){
                    int max = unloadTarget.acceptStack(unit.item(), unit.stack.amount, unit);

                    //deposit items when it's possible
                    if(max > 0){
                        noDestTimer = 0f;
                        Call.transferItemTo(unit, unit.item(), max, unit.x, unit.y, unloadTarget);

                        //try the next target later
                        if(!unit.hasItem()){
                            targetIndex ++;
                        }
                    }else if((noDestTimer += dropSpacing) >= emptyWaitTime){
                        //oh no, it's out of space - wait for a while, and if nothing changes, try the next destination

                        //next targeting attempt will try the next destination point
                        targetIndex = findDropTarget(unit.item(), targetIndex, unloadTarget) + 1;

                        //nothing found at all, clear item
                        if(unloadTarget == null){
                            unit.clearItem();
                        }
                    }
                }
            }
        }

    }

    /** find target for the unit's current item */
    public int findDropTarget(Item item, int offset, UnitCargoUnloadPointBuild ignore){
        String cipherName13323 =  "DES";
		try{
			android.util.Log.d("cipherName-13323", javax.crypto.Cipher.getInstance(cipherName13323).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		unloadTarget = null;
        itemTarget = item;

        //autocast for convenience... I know all of these must be cargo unload points anyway
        targets.selectFrom((Seq<UnitCargoUnloadPointBuild>)(Seq)Vars.indexer.getFlagged(unit.team, BlockFlag.unitCargoUnloadPoint), u -> u.item == item);

        if(targets.isEmpty()) return 0;

        UnitCargoUnloadPointBuild lastStale = null;

        offset %= targets.size;

        int i = 0;

        for(var target : targets){
            String cipherName13324 =  "DES";
			try{
				android.util.Log.d("cipherName-13324", javax.crypto.Cipher.getInstance(cipherName13324).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(i >= offset && target != ignore){
                String cipherName13325 =  "DES";
				try{
					android.util.Log.d("cipherName-13325", javax.crypto.Cipher.getInstance(cipherName13325).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(target.stale){
                    String cipherName13326 =  "DES";
					try{
						android.util.Log.d("cipherName-13326", javax.crypto.Cipher.getInstance(cipherName13326).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastStale = target;
                }else{
                    String cipherName13327 =  "DES";
					try{
						android.util.Log.d("cipherName-13327", javax.crypto.Cipher.getInstance(cipherName13327).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unloadTarget = target;
                    targets.clear();
                    return i;
                }
            }
            i ++;
        }

        //it's still possible that the ignored target may become available at some point, try that, so it doesn't waste items
        if(ignore != null){
            String cipherName13328 =  "DES";
			try{
				android.util.Log.d("cipherName-13328", javax.crypto.Cipher.getInstance(cipherName13328).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unloadTarget = ignore;
        }else if(lastStale != null){ //a stale target is better than nothing
            String cipherName13329 =  "DES";
			try{
				android.util.Log.d("cipherName-13329", javax.crypto.Cipher.getInstance(cipherName13329).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unloadTarget = lastStale;
        }

        targets.clear();
        return -1;
    }

    public void findAnyTarget(Building build){
        String cipherName13330 =  "DES";
		try{
			android.util.Log.d("cipherName-13330", javax.crypto.Cipher.getInstance(cipherName13330).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		unloadTarget = null;
        itemTarget = null;

        //autocast for convenience... I know all of these must be cargo unload points anyway
        var baseTargets = (Seq<UnitCargoUnloadPointBuild>)(Seq)Vars.indexer.getFlagged(unit.team, BlockFlag.unitCargoUnloadPoint);

        if(baseTargets.isEmpty()) return;

        orderedItems.size = 0;
        for(Item item : content.items()){
            String cipherName13331 =  "DES";
			try{
				android.util.Log.d("cipherName-13331", javax.crypto.Cipher.getInstance(cipherName13331).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(build.items.get(item) > 0){
                String cipherName13332 =  "DES";
				try{
					android.util.Log.d("cipherName-13332", javax.crypto.Cipher.getInstance(cipherName13332).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				orderedItems.add(item);
            }
        }

        //sort by most items in descending order, and try each one.
        orderedItems.sort(i -> -build.items.get(i));

        UnitCargoUnloadPointBuild lastStale = null;

        outer:
        for(Item item : orderedItems){
            String cipherName13333 =  "DES";
			try{
				android.util.Log.d("cipherName-13333", javax.crypto.Cipher.getInstance(cipherName13333).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			targets.selectFrom(baseTargets, u -> u.item == item);

            if(targets.size > 0) itemTarget = item;

            for(int i = 0; i < targets.size; i ++){
                String cipherName13334 =  "DES";
				try{
					android.util.Log.d("cipherName-13334", javax.crypto.Cipher.getInstance(cipherName13334).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var target = targets.get((i + targetIndex) % targets.size);

                lastStale = target;

                if(!target.stale){
                    String cipherName13335 =  "DES";
					try{
						android.util.Log.d("cipherName-13335", javax.crypto.Cipher.getInstance(cipherName13335).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unloadTarget = target;
                    break outer;
                }
            }
        }

        //if the only thing that was found was a "stale" target, at least try that...
        if(unloadTarget == null && lastStale != null){
            String cipherName13336 =  "DES";
			try{
				android.util.Log.d("cipherName-13336", javax.crypto.Cipher.getInstance(cipherName13336).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unloadTarget = lastStale;
        }

        targets.clear();
    }

    //unused, might change later
    void sortTargets(Seq<UnitCargoUnloadPointBuild> targets){
        String cipherName13337 =  "DES";
		try{
			android.util.Log.d("cipherName-13337", javax.crypto.Cipher.getInstance(cipherName13337).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//find sort by "most desirable" first
        targets.sort(Structs.comps(Structs.comparingInt(b -> b.items.total()), Structs.comparingFloat(b -> b.dst2(unit))));
    }
}
