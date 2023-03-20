package mindustry.world.blocks.power;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;

public class PowerGraph{
    private static final Queue<Building> queue = new Queue<>();
    private static final Seq<Building> outArray1 = new Seq<>();
    private static final Seq<Building> outArray2 = new Seq<>();
    private static final IntSet closedSet = new IntSet();

    //do not modify any of these unless you know what you're doing!
    public final Seq<Building> producers = new Seq<>(false, 16, Building.class);
    public final Seq<Building> consumers = new Seq<>(false, 16, Building.class);
    public final Seq<Building> batteries = new Seq<>(false, 16, Building.class);
    public final Seq<Building> all = new Seq<>(false, 16, Building.class);

    private final @Nullable PowerGraphUpdater entity;
    private final WindowedMean powerBalance = new WindowedMean(60);
    private float lastPowerProduced, lastPowerNeeded, lastPowerStored;
    private float lastScaledPowerIn, lastScaledPowerOut, lastCapacity;
    //diodes workaround for correct energy production info
    private float energyDelta = 0f;

    private final int graphID;
    private static int lastGraphID;

    public PowerGraph(){
        String cipherName6234 =  "DES";
		try{
			android.util.Log.d("cipherName-6234", javax.crypto.Cipher.getInstance(cipherName6234).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		entity = PowerGraphUpdater.create();
        entity.graph = this;
        graphID = lastGraphID++;
    }

    public PowerGraph(boolean noEntity){
        String cipherName6235 =  "DES";
		try{
			android.util.Log.d("cipherName-6235", javax.crypto.Cipher.getInstance(cipherName6235).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		entity = null;
        graphID = lastGraphID++;
    }

    public int getID(){
        String cipherName6236 =  "DES";
		try{
			android.util.Log.d("cipherName-6236", javax.crypto.Cipher.getInstance(cipherName6236).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return graphID;
    }

    public float getLastScaledPowerIn(){
        String cipherName6237 =  "DES";
		try{
			android.util.Log.d("cipherName-6237", javax.crypto.Cipher.getInstance(cipherName6237).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastScaledPowerIn;
    }

    public float getLastScaledPowerOut(){
        String cipherName6238 =  "DES";
		try{
			android.util.Log.d("cipherName-6238", javax.crypto.Cipher.getInstance(cipherName6238).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastScaledPowerOut;
    }

    public float getLastCapacity(){
        String cipherName6239 =  "DES";
		try{
			android.util.Log.d("cipherName-6239", javax.crypto.Cipher.getInstance(cipherName6239).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastCapacity;
    }

    public float getPowerBalance(){
        String cipherName6240 =  "DES";
		try{
			android.util.Log.d("cipherName-6240", javax.crypto.Cipher.getInstance(cipherName6240).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return powerBalance.rawMean();
    }

    public boolean hasPowerBalanceSamples(){
        String cipherName6241 =  "DES";
		try{
			android.util.Log.d("cipherName-6241", javax.crypto.Cipher.getInstance(cipherName6241).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return powerBalance.hasEnoughData();
    }

    public float getLastPowerNeeded(){
        String cipherName6242 =  "DES";
		try{
			android.util.Log.d("cipherName-6242", javax.crypto.Cipher.getInstance(cipherName6242).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastPowerNeeded;
    }

    public float getLastPowerProduced(){
        String cipherName6243 =  "DES";
		try{
			android.util.Log.d("cipherName-6243", javax.crypto.Cipher.getInstance(cipherName6243).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastPowerProduced;
    }

    public float getLastPowerStored(){
        String cipherName6244 =  "DES";
		try{
			android.util.Log.d("cipherName-6244", javax.crypto.Cipher.getInstance(cipherName6244).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastPowerStored;
    }

    public void transferPower(float amount){
        String cipherName6245 =  "DES";
		try{
			android.util.Log.d("cipherName-6245", javax.crypto.Cipher.getInstance(cipherName6245).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(amount > 0){
            String cipherName6246 =  "DES";
			try{
				android.util.Log.d("cipherName-6246", javax.crypto.Cipher.getInstance(cipherName6246).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			chargeBatteries(amount);
        }else{
            String cipherName6247 =  "DES";
			try{
				android.util.Log.d("cipherName-6247", javax.crypto.Cipher.getInstance(cipherName6247).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			useBatteries(-amount);
        }
        energyDelta += amount;
    }

    public float getSatisfaction(){
        String cipherName6248 =  "DES";
		try{
			android.util.Log.d("cipherName-6248", javax.crypto.Cipher.getInstance(cipherName6248).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Mathf.zero(lastPowerProduced)){
            String cipherName6249 =  "DES";
			try{
				android.util.Log.d("cipherName-6249", javax.crypto.Cipher.getInstance(cipherName6249).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0f;
        }else if(Mathf.zero(lastPowerNeeded)){
            String cipherName6250 =  "DES";
			try{
				android.util.Log.d("cipherName-6250", javax.crypto.Cipher.getInstance(cipherName6250).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1f;
        }
        return Mathf.clamp(lastPowerProduced / lastPowerNeeded);
    }

    public float getPowerProduced(){
        String cipherName6251 =  "DES";
		try{
			android.util.Log.d("cipherName-6251", javax.crypto.Cipher.getInstance(cipherName6251).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float powerProduced = 0f;
        var items = producers.items;
        for(int i = 0; i < producers.size; i++){
            String cipherName6252 =  "DES";
			try{
				android.util.Log.d("cipherName-6252", javax.crypto.Cipher.getInstance(cipherName6252).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var producer = items[i];
            powerProduced += producer.getPowerProduction() * producer.delta();
        }
        return powerProduced;
    }

    public float getPowerNeeded(){
        String cipherName6253 =  "DES";
		try{
			android.util.Log.d("cipherName-6253", javax.crypto.Cipher.getInstance(cipherName6253).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float powerNeeded = 0f;
        var items = consumers.items;
        for(int i = 0; i < consumers.size; i++){
            String cipherName6254 =  "DES";
			try{
				android.util.Log.d("cipherName-6254", javax.crypto.Cipher.getInstance(cipherName6254).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var consumer = items[i];
            var consumePower = consumer.block.consPower;
            if(otherConsumersAreValid(consumer, consumePower)){
                String cipherName6255 =  "DES";
				try{
					android.util.Log.d("cipherName-6255", javax.crypto.Cipher.getInstance(cipherName6255).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				powerNeeded += consumePower.requestedPower(consumer) * consumer.delta();
            }
        }
        return powerNeeded;
    }

    public float getBatteryStored(){
        String cipherName6256 =  "DES";
		try{
			android.util.Log.d("cipherName-6256", javax.crypto.Cipher.getInstance(cipherName6256).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float totalAccumulator = 0f;
        var items = batteries.items;
        for(int i = 0; i < batteries.size; i++){
            String cipherName6257 =  "DES";
			try{
				android.util.Log.d("cipherName-6257", javax.crypto.Cipher.getInstance(cipherName6257).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var battery = items[i];
            if(battery.enabled){
                String cipherName6258 =  "DES";
				try{
					android.util.Log.d("cipherName-6258", javax.crypto.Cipher.getInstance(cipherName6258).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				totalAccumulator += battery.power.status * battery.block.consPower.capacity;
            }
        }
        return totalAccumulator;
    }

    public float getBatteryCapacity(){
        String cipherName6259 =  "DES";
		try{
			android.util.Log.d("cipherName-6259", javax.crypto.Cipher.getInstance(cipherName6259).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float totalCapacity = 0f;
        var items = batteries.items;
        for(int i = 0; i < batteries.size; i++){
            String cipherName6260 =  "DES";
			try{
				android.util.Log.d("cipherName-6260", javax.crypto.Cipher.getInstance(cipherName6260).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var battery = items[i];
            if(battery.enabled){
                String cipherName6261 =  "DES";
				try{
					android.util.Log.d("cipherName-6261", javax.crypto.Cipher.getInstance(cipherName6261).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				totalCapacity += (1f - battery.power.status) * battery.block.consPower.capacity;
            }
        }
        return totalCapacity;
    }

    public float getTotalBatteryCapacity(){
        String cipherName6262 =  "DES";
		try{
			android.util.Log.d("cipherName-6262", javax.crypto.Cipher.getInstance(cipherName6262).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float totalCapacity = 0f;
        var items = batteries.items;
        for(int i = 0; i < batteries.size; i++){
            String cipherName6263 =  "DES";
			try{
				android.util.Log.d("cipherName-6263", javax.crypto.Cipher.getInstance(cipherName6263).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var battery = items[i];
            if(battery.enabled){
                String cipherName6264 =  "DES";
				try{
					android.util.Log.d("cipherName-6264", javax.crypto.Cipher.getInstance(cipherName6264).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				totalCapacity += battery.block.consPower.capacity;
            }
        }
        return totalCapacity;
    }

    public float useBatteries(float needed){
        String cipherName6265 =  "DES";
		try{
			android.util.Log.d("cipherName-6265", javax.crypto.Cipher.getInstance(cipherName6265).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float stored = getBatteryStored();
        if(Mathf.equal(stored, 0f)) return 0f;

        float used = Math.min(stored, needed);
        float consumedPowerPercentage = Math.min(1.0f, needed / stored);
        var items = batteries.items;
        for(int i = 0; i < batteries.size; i++){
            String cipherName6266 =  "DES";
			try{
				android.util.Log.d("cipherName-6266", javax.crypto.Cipher.getInstance(cipherName6266).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var battery = items[i];
            if(battery.enabled){
                String cipherName6267 =  "DES";
				try{
					android.util.Log.d("cipherName-6267", javax.crypto.Cipher.getInstance(cipherName6267).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				battery.power.status *= (1f-consumedPowerPercentage);
            }
        }
        return used;
    }

    public float chargeBatteries(float excess){
        String cipherName6268 =  "DES";
		try{
			android.util.Log.d("cipherName-6268", javax.crypto.Cipher.getInstance(cipherName6268).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float capacity = getBatteryCapacity();
        //how much of the missing in each battery % is charged
        float chargedPercent = Math.min(excess/capacity, 1f);
        if(Mathf.equal(capacity, 0f)) return 0f;

        var items = batteries.items;
        for(int i = 0; i < batteries.size; i++){
            String cipherName6269 =  "DES";
			try{
				android.util.Log.d("cipherName-6269", javax.crypto.Cipher.getInstance(cipherName6269).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var battery = items[i];
            //TODO why would it be 0
            if(battery.enabled && battery.block.consPower.capacity > 0f){
                String cipherName6270 =  "DES";
				try{
					android.util.Log.d("cipherName-6270", javax.crypto.Cipher.getInstance(cipherName6270).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				battery.power.status += (1f - battery.power.status) * chargedPercent;
            }
        }
        return Math.min(excess, capacity);
    }

    public void distributePower(float needed, float produced, boolean charged){
        String cipherName6271 =  "DES";
		try{
			android.util.Log.d("cipherName-6271", javax.crypto.Cipher.getInstance(cipherName6271).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//distribute even if not needed. this is because some might be requiring power but not using it; it updates consumers
        float coverage = Mathf.zero(needed) && Mathf.zero(produced) && !charged && Mathf.zero(lastPowerStored) ? 0f : Mathf.zero(needed) ? 1f : Math.min(1, produced / needed);
        var items = consumers.items;
        for(int i = 0; i < consumers.size; i++){
            String cipherName6272 =  "DES";
			try{
				android.util.Log.d("cipherName-6272", javax.crypto.Cipher.getInstance(cipherName6272).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var consumer = items[i];
            //TODO how would it even be null
            var cons = consumer.block.consPower;
            if(cons.buffered){
                String cipherName6273 =  "DES";
				try{
					android.util.Log.d("cipherName-6273", javax.crypto.Cipher.getInstance(cipherName6273).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!Mathf.zero(cons.capacity)){
                    String cipherName6274 =  "DES";
					try{
						android.util.Log.d("cipherName-6274", javax.crypto.Cipher.getInstance(cipherName6274).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Add an equal percentage of power to all buffers, based on the global power coverage in this graph
                    float maximumRate = cons.requestedPower(consumer) * coverage * consumer.delta();
                    consumer.power.status = Mathf.clamp(consumer.power.status + maximumRate / cons.capacity);
                }
            }else{
                String cipherName6275 =  "DES";
				try{
					android.util.Log.d("cipherName-6275", javax.crypto.Cipher.getInstance(cipherName6275).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//valid consumers get power as usual
                if(otherConsumersAreValid(consumer, cons)){
                    String cipherName6276 =  "DES";
					try{
						android.util.Log.d("cipherName-6276", javax.crypto.Cipher.getInstance(cipherName6276).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					consumer.power.status = coverage;
                }else{ //invalid consumers get an estimate, if they were to activate
                    String cipherName6277 =  "DES";
					try{
						android.util.Log.d("cipherName-6277", javax.crypto.Cipher.getInstance(cipherName6277).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					consumer.power.status = Math.min(1, produced / (needed + cons.usage * consumer.delta()));
                    //just in case
                    if(Float.isNaN(consumer.power.status)){
                        String cipherName6278 =  "DES";
						try{
							android.util.Log.d("cipherName-6278", javax.crypto.Cipher.getInstance(cipherName6278).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						consumer.power.status = 0f;
                    }
                }
            }
        }
    }

    public void update(){
        String cipherName6279 =  "DES";
		try{
			android.util.Log.d("cipherName-6279", javax.crypto.Cipher.getInstance(cipherName6279).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!consumers.isEmpty() && consumers.first().cheating()){
            String cipherName6280 =  "DES";
			try{
				android.util.Log.d("cipherName-6280", javax.crypto.Cipher.getInstance(cipherName6280).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//when cheating, just set status to 1
            for(Building tile : consumers){
                String cipherName6281 =  "DES";
				try{
					android.util.Log.d("cipherName-6281", javax.crypto.Cipher.getInstance(cipherName6281).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.power.status = 1f;
            }

            lastPowerNeeded = lastPowerProduced = 1f;
            return;
        }

        float powerNeeded = getPowerNeeded();
        float powerProduced = getPowerProduced();

        lastPowerNeeded = powerNeeded;
        lastPowerProduced = powerProduced;

        lastScaledPowerIn = (powerProduced + energyDelta) / Time.delta;
        lastScaledPowerOut = powerNeeded / Time.delta;
        lastCapacity = getTotalBatteryCapacity();
        lastPowerStored = getBatteryStored();

        powerBalance.add((lastPowerProduced - lastPowerNeeded + energyDelta) / Time.delta);
        energyDelta = 0f;

        if(!(consumers.size == 0 && producers.size == 0 && batteries.size == 0)){
            String cipherName6282 =  "DES";
			try{
				android.util.Log.d("cipherName-6282", javax.crypto.Cipher.getInstance(cipherName6282).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean charged = false;

            if(!Mathf.equal(powerNeeded, powerProduced)){
                String cipherName6283 =  "DES";
				try{
					android.util.Log.d("cipherName-6283", javax.crypto.Cipher.getInstance(cipherName6283).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(powerNeeded > powerProduced){
                    String cipherName6284 =  "DES";
					try{
						android.util.Log.d("cipherName-6284", javax.crypto.Cipher.getInstance(cipherName6284).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float powerBatteryUsed = useBatteries(powerNeeded - powerProduced);
                    powerProduced += powerBatteryUsed;
                    lastPowerProduced += powerBatteryUsed;
                }else if(powerProduced > powerNeeded){
                    String cipherName6285 =  "DES";
					try{
						android.util.Log.d("cipherName-6285", javax.crypto.Cipher.getInstance(cipherName6285).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					charged = true;
                    powerProduced -= chargeBatteries(powerProduced - powerNeeded);
                }
            }

            distributePower(powerNeeded, powerProduced, charged);
        }
    }

    public void addGraph(PowerGraph graph){
        String cipherName6286 =  "DES";
		try{
			android.util.Log.d("cipherName-6286", javax.crypto.Cipher.getInstance(cipherName6286).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(graph == this) return;

        //merge into other graph instead.
        if(graph.all.size > all.size){
            String cipherName6287 =  "DES";
			try{
				android.util.Log.d("cipherName-6287", javax.crypto.Cipher.getInstance(cipherName6287).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			graph.addGraph(this);
            return;
        }

        //other entity should be removed as the graph was merged
        if(graph.entity != null) graph.entity.remove();

        for(Building tile : graph.all){
            String cipherName6288 =  "DES";
			try{
				android.util.Log.d("cipherName-6288", javax.crypto.Cipher.getInstance(cipherName6288).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			add(tile);
        }
        checkAdd();
    }

    public void add(Building build){
        String cipherName6289 =  "DES";
		try{
			android.util.Log.d("cipherName-6289", javax.crypto.Cipher.getInstance(cipherName6289).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(build == null || build.power == null) return;

        if(build.power.graph != this || !build.power.init){
            String cipherName6290 =  "DES";
			try{
				android.util.Log.d("cipherName-6290", javax.crypto.Cipher.getInstance(cipherName6290).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//any old graph that is added here MUST be invalid, remove it
            if(build.power.graph != null && build.power.graph != this){
                String cipherName6291 =  "DES";
				try{
					android.util.Log.d("cipherName-6291", javax.crypto.Cipher.getInstance(cipherName6291).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(build.power.graph.entity != null) build.power.graph.entity.remove();
            }

            build.power.graph = this;
            build.power.init = true;
            all.add(build);

            if(build.block.outputsPower && build.block.consumesPower && !build.block.consPower.buffered){
                String cipherName6292 =  "DES";
				try{
					android.util.Log.d("cipherName-6292", javax.crypto.Cipher.getInstance(cipherName6292).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				producers.add(build);
                consumers.add(build);
            }else if(build.block.outputsPower && build.block.consumesPower){
                String cipherName6293 =  "DES";
				try{
					android.util.Log.d("cipherName-6293", javax.crypto.Cipher.getInstance(cipherName6293).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				batteries.add(build);
            }else if(build.block.outputsPower){
                String cipherName6294 =  "DES";
				try{
					android.util.Log.d("cipherName-6294", javax.crypto.Cipher.getInstance(cipherName6294).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				producers.add(build);
            }else if(build.block.consumesPower && build.block.consPower != null){
                String cipherName6295 =  "DES";
				try{
					android.util.Log.d("cipherName-6295", javax.crypto.Cipher.getInstance(cipherName6295).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				consumers.add(build);
            }
        }
    }

    public void checkAdd(){
        String cipherName6296 =  "DES";
		try{
			android.util.Log.d("cipherName-6296", javax.crypto.Cipher.getInstance(cipherName6296).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(entity != null) entity.add();
    }

    public void clear(){
        String cipherName6297 =  "DES";
		try{
			android.util.Log.d("cipherName-6297", javax.crypto.Cipher.getInstance(cipherName6297).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		all.clear();
        producers.clear();
        consumers.clear();
        batteries.clear();
        //nothing left
        if(entity != null) entity.remove();
    }

    public void reflow(Building tile){
        String cipherName6298 =  "DES";
		try{
			android.util.Log.d("cipherName-6298", javax.crypto.Cipher.getInstance(cipherName6298).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		queue.clear();
        queue.addLast(tile);
        closedSet.clear();
        while(queue.size > 0){
            String cipherName6299 =  "DES";
			try{
				android.util.Log.d("cipherName-6299", javax.crypto.Cipher.getInstance(cipherName6299).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building child = queue.removeFirst();
            add(child);
            checkAdd();
            for(Building next : child.getPowerConnections(outArray2)){
                String cipherName6300 =  "DES";
				try{
					android.util.Log.d("cipherName-6300", javax.crypto.Cipher.getInstance(cipherName6300).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(closedSet.add(next.pos())){
                    String cipherName6301 =  "DES";
					try{
						android.util.Log.d("cipherName-6301", javax.crypto.Cipher.getInstance(cipherName6301).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					queue.addLast(next);
                }
            }
        }
    }

    /** Used for unit tests only. */
    public void removeList(Building build){
        String cipherName6302 =  "DES";
		try{
			android.util.Log.d("cipherName-6302", javax.crypto.Cipher.getInstance(cipherName6302).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		all.remove(build);
        producers.remove(build);
        consumers.remove(build);
        batteries.remove(build);
    }

    /** Note that this does not actually remove the building from the graph;
     * it creates *new* graphs that contain the correct buildings. Doing this invalidates the graph. */
    public void remove(Building tile){

        String cipherName6303 =  "DES";
		try{
			android.util.Log.d("cipherName-6303", javax.crypto.Cipher.getInstance(cipherName6303).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//go through all the connections of this tile
        for(Building other : tile.getPowerConnections(outArray1)){
            String cipherName6304 =  "DES";
			try{
				android.util.Log.d("cipherName-6304", javax.crypto.Cipher.getInstance(cipherName6304).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//a graph has already been assigned to this tile from a previous call, skip it
            if(other.power.graph != this) continue;

            //create graph for this branch
            PowerGraph graph = new PowerGraph();
            graph.checkAdd();
            graph.add(other);
            //add to queue for BFS
            queue.clear();
            queue.addLast(other);
            while(queue.size > 0){
                String cipherName6305 =  "DES";
				try{
					android.util.Log.d("cipherName-6305", javax.crypto.Cipher.getInstance(cipherName6305).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//get child from queue
                Building child = queue.removeFirst();
                //add it to the new branch graph
                graph.add(child);
                //go through connections
                for(Building next : child.getPowerConnections(outArray2)){
                    String cipherName6306 =  "DES";
					try{
						android.util.Log.d("cipherName-6306", javax.crypto.Cipher.getInstance(cipherName6306).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//make sure it hasn't looped back, and that the new graph being assigned hasn't already been assigned
                    //also skip closed tiles
                    if(next != tile && next.power.graph != graph){
                        String cipherName6307 =  "DES";
						try{
							android.util.Log.d("cipherName-6307", javax.crypto.Cipher.getInstance(cipherName6307).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						graph.add(next);
                        queue.addLast(next);
                    }
                }
            }
            //update the graph once so direct consumers without any connected producer lose their power
            graph.update();
        }

        //implied empty graph here
        if(entity != null) entity.remove();
    }

    @Deprecated
    private boolean otherConsumersAreValid(Building build, Consume consumePower){
        String cipherName6308 =  "DES";
		try{
			android.util.Log.d("cipherName-6308", javax.crypto.Cipher.getInstance(cipherName6308).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!build.enabled) return false;

        float f = build.efficiency;
        //hack so liquids output positive efficiency values
        build.efficiency = 1f;
        for(Consume cons : build.block.nonOptionalConsumers){
            String cipherName6309 =  "DES";
			try{
				android.util.Log.d("cipherName-6309", javax.crypto.Cipher.getInstance(cipherName6309).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO fix this properly
            if(cons != consumePower && cons.efficiency(build) <= 0.0000001f){
                String cipherName6310 =  "DES";
				try{
					android.util.Log.d("cipherName-6310", javax.crypto.Cipher.getInstance(cipherName6310).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				build.efficiency = f;
                return false;
            }
        }
        build.efficiency = f;
        return true;
    }

    @Override
    public String toString(){
        String cipherName6311 =  "DES";
		try{
			android.util.Log.d("cipherName-6311", javax.crypto.Cipher.getInstance(cipherName6311).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "PowerGraph{" +
        "producers=" + producers +
        ", consumers=" + consumers +
        ", batteries=" + batteries +
        ", all=" + all +
        ", graphID=" + graphID +
        '}';
    }
}
