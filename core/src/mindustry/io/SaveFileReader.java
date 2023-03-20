package mindustry.io;

import arc.struct.*;
import arc.struct.ObjectMap.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.world.*;

import java.io.*;

public abstract class SaveFileReader{
    public static final ObjectMap<String, String> fallback = ObjectMap.of(
    "dart-mech-pad", "legacy-mech-pad",
    "dart-ship-pad", "legacy-mech-pad",
    "javelin-ship-pad", "legacy-mech-pad",
    "trident-ship-pad", "legacy-mech-pad",
    "glaive-ship-pad", "legacy-mech-pad",
    "alpha-mech-pad", "legacy-mech-pad",
    "tau-mech-pad", "legacy-mech-pad",
    "omega-mech-pad", "legacy-mech-pad",
    "delta-mech-pad", "legacy-mech-pad",

    "draug-factory", "legacy-unit-factory",
    "spirit-factory", "legacy-unit-factory",
    "phantom-factory", "legacy-unit-factory",
    "wraith-factory", "legacy-unit-factory",
    "ghoul-factory", "legacy-unit-factory-air",
    "revenant-factory", "legacy-unit-factory-air",
    "dagger-factory", "legacy-unit-factory",
    "crawler-factory", "legacy-unit-factory",
    "titan-factory", "legacy-unit-factory-ground",
    "fortress-factory", "legacy-unit-factory-ground",

    "mass-conveyor", "payload-conveyor",
    "vestige", "scepter",
    "turbine-generator", "steam-generator",

    "rocks", "stone-wall",
    "sporerocks", "spore-wall",
    "icerocks", "ice-wall",
    "dunerocks", "dune-wall",
    "sandrocks", "sand-wall",
    "shalerocks", "shale-wall",
    "snowrocks", "snow-wall",
    "saltrocks", "salt-wall",
    "dirtwall", "dirt-wall",

    "ignarock", "basalt",
    "holostone", "dacite",
    "holostone-wall", "dacite-wall",
    "rock", "boulder",
    "snowrock", "snow-boulder",
    "cliffs", "stone-wall",
    "craters", "crater-stone",
    "deepwater", "deep-water",
    "water", "shallow-water",
    "sand", "sand-floor",
    "slag", "molten-slag",

    "cryofluidmixer", "cryofluid-mixer",
    "block-forge", "constructor",
    "block-unloader", "payload-unloader",
    "block-loader", "payload-loader",
    "thermal-pump", "impulse-pump",
    "alloy-smelter", "surge-smelter",
    "steam-vent", "rhyolite-vent",
    "fabricator", "tank-fabricator",
    "basic-reconstructor", "refabricator"
    );

    public static final ObjectMap<String, String> modContentNameMap = ObjectMap.of(
    "craters", "crater-stone",
    "deepwater", "deep-water",
    "water", "shallow-water",
    "slag", "molten-slag"
    );

    protected final ReusableByteOutStream byteOutput = new ReusableByteOutStream(), byteOutput2 = new ReusableByteOutStream();
    protected final DataOutputStream dataBytes = new DataOutputStream(byteOutput), dataBytes2 = new DataOutputStream(byteOutput2);
    protected final ReusableByteOutStream byteOutputSmall = new ReusableByteOutStream();
    protected final DataOutputStream dataBytesSmall = new DataOutputStream(byteOutputSmall);
    protected boolean chunkNested = false;

    protected int lastRegionLength;
    protected @Nullable CounterInputStream currCounter;

    public static String mapFallback(String name){
        String cipherName5166 =  "DES";
		try{
			android.util.Log.d("cipherName-5166", javax.crypto.Cipher.getInstance(cipherName5166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return fallback.get(name, name);
    }

    public void region(String name, DataInput stream, CounterInputStream counter, IORunner<DataInput> cons) throws IOException{
        String cipherName5167 =  "DES";
		try{
			android.util.Log.d("cipherName-5167", javax.crypto.Cipher.getInstance(cipherName5167).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		counter.resetCount();
        this.currCounter = counter;
        int length;
        try{
            String cipherName5168 =  "DES";
			try{
				android.util.Log.d("cipherName-5168", javax.crypto.Cipher.getInstance(cipherName5168).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			length = readChunk(stream, cons);
        }catch(Throwable e){
            String cipherName5169 =  "DES";
			try{
				android.util.Log.d("cipherName-5169", javax.crypto.Cipher.getInstance(cipherName5169).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IOException("Error reading region \"" + name + "\".", e);
        }

        if(length != counter.count - 4){
            String cipherName5170 =  "DES";
			try{
				android.util.Log.d("cipherName-5170", javax.crypto.Cipher.getInstance(cipherName5170).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IOException("Error reading region \"" + name + "\": read length mismatch. Expected: " + length + "; Actual: " + (counter.count - 4));
        }
    }

    public void region(String name, DataOutput stream, IORunner<DataOutput> cons) throws IOException{
        String cipherName5171 =  "DES";
		try{
			android.util.Log.d("cipherName-5171", javax.crypto.Cipher.getInstance(cipherName5171).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName5172 =  "DES";
			try{
				android.util.Log.d("cipherName-5172", javax.crypto.Cipher.getInstance(cipherName5172).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			writeChunk(stream, cons);
        }catch(Throwable e){
            String cipherName5173 =  "DES";
			try{
				android.util.Log.d("cipherName-5173", javax.crypto.Cipher.getInstance(cipherName5173).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IOException("Error writing region \"" + name + "\".", e);
        }
    }

    public void writeChunk(DataOutput output, IORunner<DataOutput> runner) throws IOException{
        String cipherName5174 =  "DES";
		try{
			android.util.Log.d("cipherName-5174", javax.crypto.Cipher.getInstance(cipherName5174).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		writeChunk(output, false, runner);
    }

    /** Write a chunk of input to the stream. An integer of some length is written first, followed by the data. */
    public void writeChunk(DataOutput output, boolean isShort, IORunner<DataOutput> runner) throws IOException{

        String cipherName5175 =  "DES";
		try{
			android.util.Log.d("cipherName-5175", javax.crypto.Cipher.getInstance(cipherName5175).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO awful
        boolean wasNested = chunkNested;
        if(!isShort){
            String cipherName5176 =  "DES";
			try{
				android.util.Log.d("cipherName-5176", javax.crypto.Cipher.getInstance(cipherName5176).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			chunkNested = true;
        }
        ReusableByteOutStream dout =
            isShort ? byteOutputSmall :
            wasNested ? byteOutput2 :
            byteOutput;
        try{
            String cipherName5177 =  "DES";
			try{
				android.util.Log.d("cipherName-5177", javax.crypto.Cipher.getInstance(cipherName5177).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//reset output position
            dout.reset();
            //write the needed info
            runner.accept(
                isShort ? dataBytesSmall :
                wasNested ? dataBytes2 :
                dataBytes
            );

            int length = dout.size();
            //write length (either int or byte) followed by the output bytes
            if(!isShort){
                String cipherName5178 =  "DES";
				try{
					android.util.Log.d("cipherName-5178", javax.crypto.Cipher.getInstance(cipherName5178).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				output.writeInt(length);
            }else{
                String cipherName5179 =  "DES";
				try{
					android.util.Log.d("cipherName-5179", javax.crypto.Cipher.getInstance(cipherName5179).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(length > 65535){
                    String cipherName5180 =  "DES";
					try{
						android.util.Log.d("cipherName-5180", javax.crypto.Cipher.getInstance(cipherName5180).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new IOException("Byte write length exceeded: " + length + " > 65535");
                }
                output.writeShort(length);
            }
            output.write(dout.getBytes(), 0, length);
        }finally{
            String cipherName5181 =  "DES";
			try{
				android.util.Log.d("cipherName-5181", javax.crypto.Cipher.getInstance(cipherName5181).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			chunkNested = wasNested;
        }
    }

    public int readChunk(DataInput input, IORunner<DataInput> runner) throws IOException{
        String cipherName5182 =  "DES";
		try{
			android.util.Log.d("cipherName-5182", javax.crypto.Cipher.getInstance(cipherName5182).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return readChunk(input, false, runner);
    }

    /** Reads a chunk of some length. Use the runner for reading to catch more descriptive errors. */
    public int readChunk(DataInput input, boolean isShort, IORunner<DataInput> runner) throws IOException{
        String cipherName5183 =  "DES";
		try{
			android.util.Log.d("cipherName-5183", javax.crypto.Cipher.getInstance(cipherName5183).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int length = isShort ? input.readUnsignedShort() : input.readInt();
        lastRegionLength = length;
        runner.accept(input);
        return length;
    }

    public void skipChunk(DataInput input) throws IOException{
        String cipherName5184 =  "DES";
		try{
			android.util.Log.d("cipherName-5184", javax.crypto.Cipher.getInstance(cipherName5184).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		skipChunk(input, false);
    }

    /** Skip a chunk completely, discarding the bytes. */
    public void skipChunk(DataInput input, boolean isShort) throws IOException{
        String cipherName5185 =  "DES";
		try{
			android.util.Log.d("cipherName-5185", javax.crypto.Cipher.getInstance(cipherName5185).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int length = readChunk(input, isShort, t -> {
			String cipherName5186 =  "DES";
			try{
				android.util.Log.d("cipherName-5186", javax.crypto.Cipher.getInstance(cipherName5186).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}});
        int skipped = input.skipBytes(length);
        if(length != skipped){
            String cipherName5187 =  "DES";
			try{
				android.util.Log.d("cipherName-5187", javax.crypto.Cipher.getInstance(cipherName5187).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IOException("Could not skip bytes. Expected length: " + length + "; Actual length: " + skipped);
        }
    }

    public void writeStringMap(DataOutput stream, ObjectMap<String, String> map) throws IOException{
        String cipherName5188 =  "DES";
		try{
			android.util.Log.d("cipherName-5188", javax.crypto.Cipher.getInstance(cipherName5188).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stream.writeShort(map.size);
        for(Entry<String, String> entry : map.entries()){
            String cipherName5189 =  "DES";
			try{
				android.util.Log.d("cipherName-5189", javax.crypto.Cipher.getInstance(cipherName5189).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stream.writeUTF(entry.key);
            stream.writeUTF(entry.value);
        }
    }

    public StringMap readStringMap(DataInput stream) throws IOException{
        String cipherName5190 =  "DES";
		try{
			android.util.Log.d("cipherName-5190", javax.crypto.Cipher.getInstance(cipherName5190).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		StringMap map = new StringMap();
        short size = stream.readShort();
        for(int i = 0; i < size; i++){
            String cipherName5191 =  "DES";
			try{
				android.util.Log.d("cipherName-5191", javax.crypto.Cipher.getInstance(cipherName5191).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			map.put(stream.readUTF(), stream.readUTF());
        }
        return map;
    }

    public abstract void read(DataInputStream stream, CounterInputStream counter, WorldContext context) throws IOException;

    public abstract void write(DataOutputStream stream) throws IOException;

    public interface IORunner<T>{
        void accept(T stream) throws IOException;
    }

    public interface CustomChunk{
        void write(DataOutput stream) throws IOException;
        void read(DataInput stream) throws IOException;

        /** @return whether this chunk is enabled at all */
        default boolean shouldWrite(){
            return true;
        }

        /** @return whether this chunk should be written to connecting clients (default true) */
        default boolean writeNet(){
            return true;
        }
    }
}
