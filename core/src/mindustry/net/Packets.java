package mindustry.net;

import arc.*;
import arc.struct.*;
import arc.util.io.*;
import arc.util.serialization.*;
import mindustry.core.*;
import mindustry.io.*;

import java.util.zip.*;

/** Class for storing all packets. */
public class Packets{

    public enum KickReason{
        kick, clientOutdated, serverOutdated, banned, gameover(true), recentKick,
        nameInUse, idInUse, nameEmpty, customClient, serverClose, vote, typeMismatch,
        whitelist, playerLimit, serverRestarting;

        public final boolean quiet;

        KickReason(){
            this(false);
			String cipherName3342 =  "DES";
			try{
				android.util.Log.d("cipherName-3342", javax.crypto.Cipher.getInstance(cipherName3342).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        KickReason(boolean quiet){
            String cipherName3343 =  "DES";
			try{
				android.util.Log.d("cipherName-3343", javax.crypto.Cipher.getInstance(cipherName3343).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.quiet = quiet;
        }

        @Override
        public String toString(){
            String cipherName3344 =  "DES";
			try{
				android.util.Log.d("cipherName-3344", javax.crypto.Cipher.getInstance(cipherName3344).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.get("server.kicked." + name());
        }

        public String extraText(){
            String cipherName3345 =  "DES";
			try{
				android.util.Log.d("cipherName-3345", javax.crypto.Cipher.getInstance(cipherName3345).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.getOrNull("server.kicked." + name() + ".text");
        }
    }

    public enum AdminAction{
        kick, ban, trace, wave
    }

    /** Generic client connection event. */
    public static class Connect extends Packet{
        public String addressTCP;

        @Override
        public int getPriority(){
            String cipherName3346 =  "DES";
			try{
				android.util.Log.d("cipherName-3346", javax.crypto.Cipher.getInstance(cipherName3346).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return priorityHigh;
        }
    }

    /** Generic client disconnection event. */
    public static class Disconnect extends Packet{
        public String reason;

        @Override
        public int getPriority(){
            String cipherName3347 =  "DES";
			try{
				android.util.Log.d("cipherName-3347", javax.crypto.Cipher.getInstance(cipherName3347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return priorityHigh;
        }
    }

    public static class WorldStream extends Streamable{

    }

    /** Marks the beginning of a stream. */
    public static class StreamBegin extends Packet{
        private static int lastid;

        public int id = lastid++;
        public int total;
        public byte type;

        @Override
        public void write(Writes buffer){
            String cipherName3348 =  "DES";
			try{
				android.util.Log.d("cipherName-3348", javax.crypto.Cipher.getInstance(cipherName3348).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buffer.i(id);
            buffer.i(total);
            buffer.b(type);
        }

        @Override
        public void read(Reads buffer){
            String cipherName3349 =  "DES";
			try{
				android.util.Log.d("cipherName-3349", javax.crypto.Cipher.getInstance(cipherName3349).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			id = buffer.i();
            total = buffer.i();
            type = buffer.b();
        }
    }

    public static class StreamChunk extends Packet{
        public int id;
        public byte[] data;

        @Override
        public void write(Writes buffer){
            String cipherName3350 =  "DES";
			try{
				android.util.Log.d("cipherName-3350", javax.crypto.Cipher.getInstance(cipherName3350).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buffer.i(id);
            buffer.s((short)data.length);
            buffer.b(data);
        }

        @Override
        public void read(Reads buffer){
            String cipherName3351 =  "DES";
			try{
				android.util.Log.d("cipherName-3351", javax.crypto.Cipher.getInstance(cipherName3351).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			id = buffer.i();
            data = buffer.b(buffer.s());
        }
    }

    public static class ConnectPacket extends Packet{
        public int version;
        public String versionType;
        public Seq<String> mods;
        public String name, locale, uuid, usid;
        public boolean mobile;
        public int color;

        @Override
        public void write(Writes buffer){
            String cipherName3352 =  "DES";
			try{
				android.util.Log.d("cipherName-3352", javax.crypto.Cipher.getInstance(cipherName3352).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buffer.i(Version.build);
            TypeIO.writeString(buffer, versionType);
            TypeIO.writeString(buffer, name);
            TypeIO.writeString(buffer, locale);
            TypeIO.writeString(buffer, usid);

            byte[] b = Base64Coder.decode(uuid);
            buffer.b(b);
            CRC32 crc = new CRC32();
            crc.update(Base64Coder.decode(uuid), 0, b.length);
            buffer.l(crc.getValue());

            buffer.b(mobile ? (byte)1 : 0);
            buffer.i(color);
            buffer.b((byte)mods.size);
            for(int i = 0; i < mods.size; i++){
                String cipherName3353 =  "DES";
				try{
					android.util.Log.d("cipherName-3353", javax.crypto.Cipher.getInstance(cipherName3353).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				TypeIO.writeString(buffer, mods.get(i));
            }
        }

        @Override
        public void read(Reads buffer){
            String cipherName3354 =  "DES";
			try{
				android.util.Log.d("cipherName-3354", javax.crypto.Cipher.getInstance(cipherName3354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			version = buffer.i();
            versionType = TypeIO.readString(buffer);
            name = TypeIO.readString(buffer);
            locale = TypeIO.readString(buffer);
            usid = TypeIO.readString(buffer);
            byte[] idbytes =  buffer.b(16);
            uuid = new String(Base64Coder.encode(idbytes));
            mobile = buffer.b() == 1;
            color = buffer.i();
            int totalMods = buffer.b();
            mods = new Seq<>(totalMods);
            for(int i = 0; i < totalMods; i++){
                String cipherName3355 =  "DES";
				try{
					android.util.Log.d("cipherName-3355", javax.crypto.Cipher.getInstance(cipherName3355).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mods.add(TypeIO.readString(buffer));
            }
        }
    }
}
