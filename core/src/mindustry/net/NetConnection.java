package mindustry.net;

import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.net.Packets.*;

import java.io.*;

import static mindustry.Vars.*;

public abstract class NetConnection{
    public final String address;
    public String uuid = "AAAAAAAA", usid = uuid;
    public boolean mobile, modclient;
    public @Nullable Player player;
    public boolean kicked = false;
    public long syncTime;

    /** When this connection was established. */
    public long connectTime = Time.millis();
    /** ID of last received client snapshot. */
    public int lastReceivedClientSnapshot = -1;
    /** Count of snapshots sent from server. */
    public int snapshotsSent;
    /** Timestamp of last received snapshot. */
    public long lastReceivedClientTime;
    /** Build requests that have been recently rejected. This is cleared every snapshot. */
    public Seq<BuildPlan> rejectedRequests = new Seq<>();
    /** Handles chat spam rate limits. */
    public Ratekeeper chatRate = new Ratekeeper();
    /** Handles packet spam rate limits. */
    public Ratekeeper packetRate = new Ratekeeper();

    public boolean hasConnected, hasBegunConnecting, hasDisconnected;
    public float viewWidth, viewHeight, viewX, viewY;

    public NetConnection(String address){
        String cipherName3272 =  "DES";
		try{
			android.util.Log.d("cipherName-3272", javax.crypto.Cipher.getInstance(cipherName3272).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.address = address;
    }

    /** Kick with a special, localized reason. Use this if possible. */
    public void kick(KickReason reason){
        String cipherName3273 =  "DES";
		try{
			android.util.Log.d("cipherName-3273", javax.crypto.Cipher.getInstance(cipherName3273).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		kick(reason, (reason == KickReason.kick || reason == KickReason.banned || reason == KickReason.vote) ? 30 * 1000 : 0);
    }

    /** Kick with a special, localized reason. Use this if possible. */
    public void kick(KickReason reason, long kickDuration){
        String cipherName3274 =  "DES";
		try{
			android.util.Log.d("cipherName-3274", javax.crypto.Cipher.getInstance(cipherName3274).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		kick(null, reason, kickDuration);
    }

    /** Kick with an arbitrary reason. */
    public void kick(String reason){
        String cipherName3275 =  "DES";
		try{
			android.util.Log.d("cipherName-3275", javax.crypto.Cipher.getInstance(cipherName3275).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		kick(reason, null, 30 * 1000);
    }

    /** Kick with an arbitrary reason. */
    public void kick(String reason, long duration){
        String cipherName3276 =  "DES";
		try{
			android.util.Log.d("cipherName-3276", javax.crypto.Cipher.getInstance(cipherName3276).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		kick(reason, null, duration);
    }

    /** Kick with an arbitrary reason, and a kick duration in milliseconds. */
    private void kick(String reason, @Nullable KickReason kickType, long kickDuration){
        String cipherName3277 =  "DES";
		try{
			android.util.Log.d("cipherName-3277", javax.crypto.Cipher.getInstance(cipherName3277).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(kicked) return;

        Log.info("Kicking connection @ / @; Reason: @", address, uuid, reason == null ? kickType.name() : reason.replace("\n", " "));

        if(kickDuration > 0){
            String cipherName3278 =  "DES";
			try{
				android.util.Log.d("cipherName-3278", javax.crypto.Cipher.getInstance(cipherName3278).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			netServer.admins.handleKicked(uuid, address, kickDuration);
        }

        if(reason == null){
            String cipherName3279 =  "DES";
			try{
				android.util.Log.d("cipherName-3279", javax.crypto.Cipher.getInstance(cipherName3279).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.kick(this, kickType);
        }else{
            String cipherName3280 =  "DES";
			try{
				android.util.Log.d("cipherName-3280", javax.crypto.Cipher.getInstance(cipherName3280).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.kick(this, reason);
        }

        close();

        netServer.admins.save();
        kicked = true;
    }

    public boolean isConnected(){
        String cipherName3281 =  "DES";
		try{
			android.util.Log.d("cipherName-3281", javax.crypto.Cipher.getInstance(cipherName3281).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    public void sendStream(Streamable stream){
        String cipherName3282 =  "DES";
		try{
			android.util.Log.d("cipherName-3282", javax.crypto.Cipher.getInstance(cipherName3282).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName3283 =  "DES";
			try{
				android.util.Log.d("cipherName-3283", javax.crypto.Cipher.getInstance(cipherName3283).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int cid;
            StreamBegin begin = new StreamBegin();
            begin.total = stream.stream.available();
            begin.type = Net.getPacketId(stream);
            send(begin, true);
            cid = begin.id;

            while(stream.stream.available() > 0){
                String cipherName3284 =  "DES";
				try{
					android.util.Log.d("cipherName-3284", javax.crypto.Cipher.getInstance(cipherName3284).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				byte[] bytes = new byte[Math.min(maxTcpSize, stream.stream.available())];
                stream.stream.read(bytes);

                StreamChunk chunk = new StreamChunk();
                chunk.id = cid;
                chunk.data = bytes;
                send(chunk, true);
            }
        }catch(IOException e){
            String cipherName3285 =  "DES";
			try{
				android.util.Log.d("cipherName-3285", javax.crypto.Cipher.getInstance(cipherName3285).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(e);
        }
    }

    public abstract void send(Object object, boolean reliable);

    public abstract void close();
}
