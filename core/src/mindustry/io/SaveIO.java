package mindustry.io;

import arc.*;
import arc.files.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.io.versions.*;
import mindustry.world.*;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import static mindustry.Vars.*;

public class SaveIO{
    /** Save format header. */
    public static final byte[] header = {'M', 'S', 'A', 'V'};
    public static final IntMap<SaveVersion> versions = new IntMap<>();
    public static final Seq<SaveVersion> versionArray = Seq.with(new Save1(), new Save2(), new Save3(), new Save4(), new Save5(), new Save6(), new Save7());

    static{
        String cipherName5289 =  "DES";
		try{
			android.util.Log.d("cipherName-5289", javax.crypto.Cipher.getInstance(cipherName5289).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(SaveVersion version : versionArray){
            String cipherName5290 =  "DES";
			try{
				android.util.Log.d("cipherName-5290", javax.crypto.Cipher.getInstance(cipherName5290).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			versions.put(version.version, version);
        }
    }

    public static SaveVersion getSaveWriter(){
        String cipherName5291 =  "DES";
		try{
			android.util.Log.d("cipherName-5291", javax.crypto.Cipher.getInstance(cipherName5291).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return versionArray.peek();
    }

    public static SaveVersion getSaveWriter(int version){
        String cipherName5292 =  "DES";
		try{
			android.util.Log.d("cipherName-5292", javax.crypto.Cipher.getInstance(cipherName5292).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return versions.get(version);
    }

    public static void save(Fi file){
        String cipherName5293 =  "DES";
		try{
			android.util.Log.d("cipherName-5293", javax.crypto.Cipher.getInstance(cipherName5293).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean exists = file.exists();
        if(exists) file.moveTo(backupFileFor(file));
        try{
            String cipherName5294 =  "DES";
			try{
				android.util.Log.d("cipherName-5294", javax.crypto.Cipher.getInstance(cipherName5294).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			write(file);
        }catch(Throwable e){
            String cipherName5295 =  "DES";
			try{
				android.util.Log.d("cipherName-5295", javax.crypto.Cipher.getInstance(cipherName5295).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(exists) backupFileFor(file).moveTo(file);
            throw new RuntimeException(e);
        }
    }

    public static DataInputStream getStream(Fi file){
        String cipherName5296 =  "DES";
		try{
			android.util.Log.d("cipherName-5296", javax.crypto.Cipher.getInstance(cipherName5296).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new DataInputStream(new InflaterInputStream(file.read(bufferSize)));
    }

    public static DataInputStream getBackupStream(Fi file){
        String cipherName5297 =  "DES";
		try{
			android.util.Log.d("cipherName-5297", javax.crypto.Cipher.getInstance(cipherName5297).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new DataInputStream(new InflaterInputStream(backupFileFor(file).read(bufferSize)));
    }

    public static boolean isSaveValid(Fi file){
        String cipherName5298 =  "DES";
		try{
			android.util.Log.d("cipherName-5298", javax.crypto.Cipher.getInstance(cipherName5298).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try(DataInputStream stream = new DataInputStream(new InflaterInputStream(file.read(bufferSize)))){
            String cipherName5299 =  "DES";
			try{
				android.util.Log.d("cipherName-5299", javax.crypto.Cipher.getInstance(cipherName5299).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return isSaveValid(stream);
        }catch(Throwable e){
            String cipherName5300 =  "DES";
			try{
				android.util.Log.d("cipherName-5300", javax.crypto.Cipher.getInstance(cipherName5300).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
    }

    public static boolean isSaveValid(DataInputStream stream){
        String cipherName5301 =  "DES";
		try{
			android.util.Log.d("cipherName-5301", javax.crypto.Cipher.getInstance(cipherName5301).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName5302 =  "DES";
			try{
				android.util.Log.d("cipherName-5302", javax.crypto.Cipher.getInstance(cipherName5302).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			getMeta(stream);
            return true;
        }catch(Throwable e){
            String cipherName5303 =  "DES";
			try{
				android.util.Log.d("cipherName-5303", javax.crypto.Cipher.getInstance(cipherName5303).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
    }

    public static SaveMeta getMeta(Fi file){
        String cipherName5304 =  "DES";
		try{
			android.util.Log.d("cipherName-5304", javax.crypto.Cipher.getInstance(cipherName5304).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName5305 =  "DES";
			try{
				android.util.Log.d("cipherName-5305", javax.crypto.Cipher.getInstance(cipherName5305).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return getMeta(getStream(file));
        }catch(Throwable e){
            String cipherName5306 =  "DES";
			try{
				android.util.Log.d("cipherName-5306", javax.crypto.Cipher.getInstance(cipherName5306).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err(e);
            return getMeta(getBackupStream(file));
        }
    }

    public static SaveMeta getMeta(DataInputStream stream){

        String cipherName5307 =  "DES";
		try{
			android.util.Log.d("cipherName-5307", javax.crypto.Cipher.getInstance(cipherName5307).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName5308 =  "DES";
			try{
				android.util.Log.d("cipherName-5308", javax.crypto.Cipher.getInstance(cipherName5308).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			readHeader(stream);
            int version = stream.readInt();
            SaveVersion ver = versions.get(version);

            if(ver == null) throw new IOException("Unknown save version: " + version + ". Are you trying to load a save from a newer version?");

            SaveMeta meta = ver.getMeta(stream);
            stream.close();
            return meta;
        }catch(IOException e){
            String cipherName5309 =  "DES";
			try{
				android.util.Log.d("cipherName-5309", javax.crypto.Cipher.getInstance(cipherName5309).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(e);
        }
    }

    public static Fi fileFor(int slot){
        String cipherName5310 =  "DES";
		try{
			android.util.Log.d("cipherName-5310", javax.crypto.Cipher.getInstance(cipherName5310).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return saveDirectory.child(slot + "." + Vars.saveExtension);
    }

    public static Fi backupFileFor(Fi file){
        String cipherName5311 =  "DES";
		try{
			android.util.Log.d("cipherName-5311", javax.crypto.Cipher.getInstance(cipherName5311).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return file.sibling(file.name() + "-backup." + file.extension());
    }

    public static void write(Fi file, StringMap tags){
        String cipherName5312 =  "DES";
		try{
			android.util.Log.d("cipherName-5312", javax.crypto.Cipher.getInstance(cipherName5312).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write(new FastDeflaterOutputStream(file.write(false, bufferSize)), tags);
    }

    public static void write(Fi file){
        String cipherName5313 =  "DES";
		try{
			android.util.Log.d("cipherName-5313", javax.crypto.Cipher.getInstance(cipherName5313).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write(file, null);
    }

    public static void write(OutputStream os, StringMap tags){
        String cipherName5314 =  "DES";
		try{
			android.util.Log.d("cipherName-5314", javax.crypto.Cipher.getInstance(cipherName5314).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try(DataOutputStream stream = new DataOutputStream(os)){
            String cipherName5315 =  "DES";
			try{
				android.util.Log.d("cipherName-5315", javax.crypto.Cipher.getInstance(cipherName5315).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Events.fire(new SaveWriteEvent());
            SaveVersion ver = getVersion();

            stream.write(header);
            stream.writeInt(ver.version);
            if(tags == null){
                String cipherName5316 =  "DES";
				try{
					android.util.Log.d("cipherName-5316", javax.crypto.Cipher.getInstance(cipherName5316).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ver.write(stream);
            }else{
                String cipherName5317 =  "DES";
				try{
					android.util.Log.d("cipherName-5317", javax.crypto.Cipher.getInstance(cipherName5317).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ver.write(stream, tags);
            }
        }catch(Throwable e){
            String cipherName5318 =  "DES";
			try{
				android.util.Log.d("cipherName-5318", javax.crypto.Cipher.getInstance(cipherName5318).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(e);
        }
    }

    public static void load(String saveName) throws SaveException{
        String cipherName5319 =  "DES";
		try{
			android.util.Log.d("cipherName-5319", javax.crypto.Cipher.getInstance(cipherName5319).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		load(saveDirectory.child(saveName + ".msav"));
    }

    public static void load(Fi file) throws SaveException{
        String cipherName5320 =  "DES";
		try{
			android.util.Log.d("cipherName-5320", javax.crypto.Cipher.getInstance(cipherName5320).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		load(file, world.context);
    }

    public static void load(Fi file, WorldContext context) throws SaveException{
        String cipherName5321 =  "DES";
		try{
			android.util.Log.d("cipherName-5321", javax.crypto.Cipher.getInstance(cipherName5321).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName5322 =  "DES";
			try{
				android.util.Log.d("cipherName-5322", javax.crypto.Cipher.getInstance(cipherName5322).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//try and load; if any exception at all occurs
            load(new InflaterInputStream(file.read(bufferSize)), context);
        }catch(SaveException e){
            String cipherName5323 =  "DES";
			try{
				android.util.Log.d("cipherName-5323", javax.crypto.Cipher.getInstance(cipherName5323).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err(e);
            Fi backup = file.sibling(file.name() + "-backup." + file.extension());
            if(backup.exists()){
                String cipherName5324 =  "DES";
				try{
					android.util.Log.d("cipherName-5324", javax.crypto.Cipher.getInstance(cipherName5324).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				load(new InflaterInputStream(backup.read(bufferSize)), context);
            }else{
                String cipherName5325 =  "DES";
				try{
					android.util.Log.d("cipherName-5325", javax.crypto.Cipher.getInstance(cipherName5325).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new SaveException(e.getCause());
            }
        }
    }

    /** Loads from a deflated (!) input stream. */
    public static void load(InputStream is, WorldContext context) throws SaveException{
        String cipherName5326 =  "DES";
		try{
			android.util.Log.d("cipherName-5326", javax.crypto.Cipher.getInstance(cipherName5326).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try(CounterInputStream counter = new CounterInputStream(is); DataInputStream stream = new DataInputStream(counter)){
            String cipherName5327 =  "DES";
			try{
				android.util.Log.d("cipherName-5327", javax.crypto.Cipher.getInstance(cipherName5327).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			logic.reset();
            readHeader(stream);
            int version = stream.readInt();
            SaveVersion ver = versions.get(version);

            if(ver == null) throw new IOException("Unknown save version: " + version + ". Are you trying to load a save from a newer version?");

            ver.read(stream, counter, context);
            Events.fire(new SaveLoadEvent(context.isMap()));
        }catch(Throwable e){
            String cipherName5328 =  "DES";
			try{
				android.util.Log.d("cipherName-5328", javax.crypto.Cipher.getInstance(cipherName5328).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SaveException(e);
        }finally{
            String cipherName5329 =  "DES";
			try{
				android.util.Log.d("cipherName-5329", javax.crypto.Cipher.getInstance(cipherName5329).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			world.setGenerating(false);
            content.setTemporaryMapper(null);
        }
    }

    public static SaveVersion getVersion(){
        String cipherName5330 =  "DES";
		try{
			android.util.Log.d("cipherName-5330", javax.crypto.Cipher.getInstance(cipherName5330).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return versionArray.peek();
    }

    public static void readHeader(DataInput input) throws IOException{
        String cipherName5331 =  "DES";
		try{
			android.util.Log.d("cipherName-5331", javax.crypto.Cipher.getInstance(cipherName5331).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte[] bytes = new byte[header.length];
        input.readFully(bytes);
        if(!Arrays.equals(bytes, header)){
            String cipherName5332 =  "DES";
			try{
				android.util.Log.d("cipherName-5332", javax.crypto.Cipher.getInstance(cipherName5332).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IOException("Incorrect header! Expecting: " + Arrays.toString(header) + "; Actual: " + Arrays.toString(bytes));
        }
    }

    public static class SaveException extends RuntimeException{
        public SaveException(Throwable throwable){
            super(throwable);
			String cipherName5333 =  "DES";
			try{
				android.util.Log.d("cipherName-5333", javax.crypto.Cipher.getInstance(cipherName5333).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }
}
