package mindustry.net;

import arc.*;
import arc.func.*;
import arc.net.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.net.Packets.*;
import mindustry.net.Streamable.*;
import net.jpountz.lz4.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.*;

import static arc.util.Log.*;
import static mindustry.Vars.*;

@SuppressWarnings("unchecked")
public class Net{
    private static Seq<Prov<? extends Packet>> packetProvs = new Seq<>();
    private static Seq<Class<? extends Packet>> packetClasses = new Seq<>();
    private static ObjectIntMap<Class<?>> packetToId = new ObjectIntMap<>();

    private boolean server;
    private boolean active;
    private boolean clientLoaded;
    private @Nullable StreamBuilder currentStream;

    private final Seq<Packet> packetQueue = new Seq<>();
    private final ObjectMap<Class<?>, Cons> clientListeners = new ObjectMap<>();
    private final ObjectMap<Class<?>, Cons2<NetConnection, Object>> serverListeners = new ObjectMap<>();
    private final IntMap<StreamBuilder> streams = new IntMap<>();
    private final ExecutorService pingExecutor = OS.isWindows && !OS.is64Bit ? Threads.boundedExecutor("Ping Servers", 5) : Threads.unboundedExecutor();

    private final NetProvider provider;

    static{
        String cipherName3286 =  "DES";
		try{
			android.util.Log.d("cipherName-3286", javax.crypto.Cipher.getInstance(cipherName3286).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		registerPacket(StreamBegin::new);
        registerPacket(StreamChunk::new);
        registerPacket(WorldStream::new);
        registerPacket(ConnectPacket::new);

        //register generated packet classes
        Call.registerPackets();
    }

    /** Registers a new packet type for serialization. */
    public static <T extends Packet> void registerPacket(Prov<T> cons){
        String cipherName3287 =  "DES";
		try{
			android.util.Log.d("cipherName-3287", javax.crypto.Cipher.getInstance(cipherName3287).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		packetProvs.add(cons);
        var t = cons.get();
        packetClasses.add(t.getClass());
        packetToId.put(t.getClass(), packetProvs.size - 1);
    }

    public static byte getPacketId(Packet packet){
        String cipherName3288 =  "DES";
		try{
			android.util.Log.d("cipherName-3288", javax.crypto.Cipher.getInstance(cipherName3288).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int id = packetToId.get(packet.getClass(), -1);
        if(id == -1) throw new ArcRuntimeException("Unknown packet type: " + packet.getClass());
        return (byte)id;
    }

    public static <T extends Packet> T newPacket(byte id){
        String cipherName3289 =  "DES";
		try{
			android.util.Log.d("cipherName-3289", javax.crypto.Cipher.getInstance(cipherName3289).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ((Prov<T>)packetProvs.get(id & 0xff)).get();
    }

    public Net(NetProvider provider){
        String cipherName3290 =  "DES";
		try{
			android.util.Log.d("cipherName-3290", javax.crypto.Cipher.getInstance(cipherName3290).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.provider = provider;
    }

    public void handleException(Throwable e){
        String cipherName3291 =  "DES";
		try{
			android.util.Log.d("cipherName-3291", javax.crypto.Cipher.getInstance(cipherName3291).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(e instanceof ArcNetException){
            String cipherName3292 =  "DES";
			try{
				android.util.Log.d("cipherName-3292", javax.crypto.Cipher.getInstance(cipherName3292).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.app.post(() -> showError(new IOException("mismatch", e)));
        }else if(e instanceof ClosedChannelException){
            String cipherName3293 =  "DES";
			try{
				android.util.Log.d("cipherName-3293", javax.crypto.Cipher.getInstance(cipherName3293).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.app.post(() -> showError(new IOException("alreadyconnected", e)));
        }else{
            String cipherName3294 =  "DES";
			try{
				android.util.Log.d("cipherName-3294", javax.crypto.Cipher.getInstance(cipherName3294).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.app.post(() -> showError(e));
        }
    }

    /** Display a network error. Call on the graphics thread. */
    public void showError(Throwable e){

        String cipherName3295 =  "DES";
		try{
			android.util.Log.d("cipherName-3295", javax.crypto.Cipher.getInstance(cipherName3295).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!headless){

            String cipherName3296 =  "DES";
			try{
				android.util.Log.d("cipherName-3296", javax.crypto.Cipher.getInstance(cipherName3296).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Throwable t = e;
            while(t.getCause() != null){
                String cipherName3297 =  "DES";
				try{
					android.util.Log.d("cipherName-3297", javax.crypto.Cipher.getInstance(cipherName3297).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t = t.getCause();
            }

            String baseError = Strings.getFinalMessage(e);

            String error = baseError == null ? "" : baseError.toLowerCase();
            String type = t.getClass().toString().toLowerCase();
            boolean isError = false;

            if(e instanceof BufferUnderflowException || e instanceof BufferOverflowException || e.getCause() instanceof EOFException){
                String cipherName3298 =  "DES";
				try{
					android.util.Log.d("cipherName-3298", javax.crypto.Cipher.getInstance(cipherName3298).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				error = Core.bundle.get("error.io");
            }else if(error.equals("mismatch") || e instanceof LZ4Exception || (e instanceof IndexOutOfBoundsException && e.getStackTrace().length > 0 && e.getStackTrace()[0].getClassName().contains("java.nio"))){
                String cipherName3299 =  "DES";
				try{
					android.util.Log.d("cipherName-3299", javax.crypto.Cipher.getInstance(cipherName3299).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				error = Core.bundle.get("error.mismatch");
            }else if(error.contains("port out of range") || error.contains("invalid argument") || (error.contains("invalid") && error.contains("address")) || Strings.neatError(e).contains("address associated")){
                String cipherName3300 =  "DES";
				try{
					android.util.Log.d("cipherName-3300", javax.crypto.Cipher.getInstance(cipherName3300).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				error = Core.bundle.get("error.invalidaddress");
            }else if(error.contains("connection refused") || error.contains("route to host") || type.contains("unknownhost")){
                String cipherName3301 =  "DES";
				try{
					android.util.Log.d("cipherName-3301", javax.crypto.Cipher.getInstance(cipherName3301).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				error = Core.bundle.get("error.unreachable");
            }else if(type.contains("timeout")){
                String cipherName3302 =  "DES";
				try{
					android.util.Log.d("cipherName-3302", javax.crypto.Cipher.getInstance(cipherName3302).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				error = Core.bundle.get("error.timedout");
            }else if(error.equals("alreadyconnected") || error.contains("connection is closed")){
                String cipherName3303 =  "DES";
				try{
					android.util.Log.d("cipherName-3303", javax.crypto.Cipher.getInstance(cipherName3303).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				error = Core.bundle.get("error.alreadyconnected");
            }else if(!error.isEmpty()){
                String cipherName3304 =  "DES";
				try{
					android.util.Log.d("cipherName-3304", javax.crypto.Cipher.getInstance(cipherName3304).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				error = Core.bundle.get("error.any");
                isError = true;
            }

            if(isError){
                String cipherName3305 =  "DES";
				try{
					android.util.Log.d("cipherName-3305", javax.crypto.Cipher.getInstance(cipherName3305).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showException("@error.any", e);
            }else{
                String cipherName3306 =  "DES";
				try{
					android.util.Log.d("cipherName-3306", javax.crypto.Cipher.getInstance(cipherName3306).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showText("", Core.bundle.format("connectfail", error));
            }
            ui.loadfrag.hide();

            if(client()){
                String cipherName3307 =  "DES";
				try{
					android.util.Log.d("cipherName-3307", javax.crypto.Cipher.getInstance(cipherName3307).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				netClient.disconnectQuietly();
            }
        }

        Log.err(e);
    }

    /**
     * Sets the client loaded status, or whether it will receive normal packets from the server.
     */
    public void setClientLoaded(boolean loaded){
        String cipherName3308 =  "DES";
		try{
			android.util.Log.d("cipherName-3308", javax.crypto.Cipher.getInstance(cipherName3308).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clientLoaded = loaded;

        if(loaded){
            String cipherName3309 =  "DES";
			try{
				android.util.Log.d("cipherName-3309", javax.crypto.Cipher.getInstance(cipherName3309).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//handle all packets that were skipped while loading
            for(int i = 0; i < packetQueue.size; i++){
                String cipherName3310 =  "DES";
				try{
					android.util.Log.d("cipherName-3310", javax.crypto.Cipher.getInstance(cipherName3310).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				handleClientReceived(packetQueue.get(i));
            }
        }
        //clear inbound packet queue
        packetQueue.clear();
    }

    public void setClientConnected(){
        String cipherName3311 =  "DES";
		try{
			android.util.Log.d("cipherName-3311", javax.crypto.Cipher.getInstance(cipherName3311).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		active = true;
        server = false;
    }

    /**
     * Connect to an address.
     */
    public void connect(String ip, int port, Runnable success){
        String cipherName3312 =  "DES";
		try{
			android.util.Log.d("cipherName-3312", javax.crypto.Cipher.getInstance(cipherName3312).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName3313 =  "DES";
			try{
				android.util.Log.d("cipherName-3313", javax.crypto.Cipher.getInstance(cipherName3313).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!active){
                String cipherName3314 =  "DES";
				try{
					android.util.Log.d("cipherName-3314", javax.crypto.Cipher.getInstance(cipherName3314).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(new ClientServerConnectEvent(ip, port));
                provider.connectClient(ip, port, success);
                active = true;
                server = false;
            }else{
                String cipherName3315 =  "DES";
				try{
					android.util.Log.d("cipherName-3315", javax.crypto.Cipher.getInstance(cipherName3315).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IOException("alreadyconnected");
            }
        }catch(IOException e){
            String cipherName3316 =  "DES";
			try{
				android.util.Log.d("cipherName-3316", javax.crypto.Cipher.getInstance(cipherName3316).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			showError(e);
        }
    }

    /**
     * Host a server at an address.
     */
    public void host(int port) throws IOException{
        String cipherName3317 =  "DES";
		try{
			android.util.Log.d("cipherName-3317", javax.crypto.Cipher.getInstance(cipherName3317).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		provider.hostServer(port);
        active = true;
        server = true;

        Time.runTask(60f, platform::updateRPC);
    }

    /**
     * Closes the server.
     */
    public void closeServer(){
        String cipherName3318 =  "DES";
		try{
			android.util.Log.d("cipherName-3318", javax.crypto.Cipher.getInstance(cipherName3318).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(NetConnection con : getConnections()){
            String cipherName3319 =  "DES";
			try{
				android.util.Log.d("cipherName-3319", javax.crypto.Cipher.getInstance(cipherName3319).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.kick(con, KickReason.serverClose);
        }

        provider.closeServer();
        server = false;
        active = false;
    }

    public void reset(){
        String cipherName3320 =  "DES";
		try{
			android.util.Log.d("cipherName-3320", javax.crypto.Cipher.getInstance(cipherName3320).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		closeServer();
        netClient.disconnectNoReset();
    }

    public void disconnect(){
        String cipherName3321 =  "DES";
		try{
			android.util.Log.d("cipherName-3321", javax.crypto.Cipher.getInstance(cipherName3321).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(active && !server){
            String cipherName3322 =  "DES";
			try{
				android.util.Log.d("cipherName-3322", javax.crypto.Cipher.getInstance(cipherName3322).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.info("Disconnecting.");
        }
        provider.disconnectClient();
        server = false;
        active = false;
    }

    /**
     * Starts discovering servers on a different thread.
     * Callback is run on the main Arc thread.
     */
    public void discoverServers(Cons<Host> cons, Runnable done){
        String cipherName3323 =  "DES";
		try{
			android.util.Log.d("cipherName-3323", javax.crypto.Cipher.getInstance(cipherName3323).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		provider.discoverServers(cons, done);
    }

    /**
     * Returns a list of all connections IDs.
     */
    public Iterable<NetConnection> getConnections(){
        String cipherName3324 =  "DES";
		try{
			android.util.Log.d("cipherName-3324", javax.crypto.Cipher.getInstance(cipherName3324).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (Iterable<NetConnection>)provider.getConnections();
    }

    /** Send an object to all connected clients, or to the server if this is a client.*/
    public void send(Object object, boolean reliable){
        String cipherName3325 =  "DES";
		try{
			android.util.Log.d("cipherName-3325", javax.crypto.Cipher.getInstance(cipherName3325).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(server){
            String cipherName3326 =  "DES";
			try{
				android.util.Log.d("cipherName-3326", javax.crypto.Cipher.getInstance(cipherName3326).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(NetConnection con : provider.getConnections()){
                String cipherName3327 =  "DES";
				try{
					android.util.Log.d("cipherName-3327", javax.crypto.Cipher.getInstance(cipherName3327).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.send(object, reliable);
            }
        }else{
            String cipherName3328 =  "DES";
			try{
				android.util.Log.d("cipherName-3328", javax.crypto.Cipher.getInstance(cipherName3328).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			provider.sendClient(object, reliable);
        }
    }

    /** Send an object to everyone EXCEPT a certain client. Server-side only.*/
    public void sendExcept(NetConnection except, Object object, boolean reliable){
        String cipherName3329 =  "DES";
		try{
			android.util.Log.d("cipherName-3329", javax.crypto.Cipher.getInstance(cipherName3329).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(NetConnection con : getConnections()){
            String cipherName3330 =  "DES";
			try{
				android.util.Log.d("cipherName-3330", javax.crypto.Cipher.getInstance(cipherName3330).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(con != except){
                String cipherName3331 =  "DES";
				try{
					android.util.Log.d("cipherName-3331", javax.crypto.Cipher.getInstance(cipherName3331).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.send(object, reliable);
            }
        }
    }

    public @Nullable StreamBuilder getCurrentStream(){
        String cipherName3332 =  "DES";
		try{
			android.util.Log.d("cipherName-3332", javax.crypto.Cipher.getInstance(cipherName3332).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return currentStream;
    }

    /**
     * Registers a client listener for when an object is received.
     */
    public <T> void handleClient(Class<T> type, Cons<T> listener){
        String cipherName3333 =  "DES";
		try{
			android.util.Log.d("cipherName-3333", javax.crypto.Cipher.getInstance(cipherName3333).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clientListeners.put(type, listener);
    }

    /**
     * Registers a server listener for when an object is received.
     */
    public <T> void handleServer(Class<T> type, Cons2<NetConnection, T> listener){
        String cipherName3334 =  "DES";
		try{
			android.util.Log.d("cipherName-3334", javax.crypto.Cipher.getInstance(cipherName3334).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		serverListeners.put(type, (Cons2<NetConnection, Object>)listener);
    }

    /**
     * Call to handle a packet being received for the client.
     */
    public void handleClientReceived(Packet object){
		String cipherName3335 =  "DES";
		try{
			android.util.Log.d("cipherName-3335", javax.crypto.Cipher.getInstance(cipherName3335).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        object.handled();

        if(object instanceof StreamBegin b){
            streams.put(b.id, currentStream = new StreamBuilder(b));

        }else if(object instanceof StreamChunk c){
            StreamBuilder builder = streams.get(c.id);
            if(builder == null){
                throw new RuntimeException("Received stream chunk without a StreamBegin beforehand!");
            }
            builder.add(c.data);

            ui.loadfrag.setProgress(builder.progress());
            ui.loadfrag.snapProgress();
            netClient.resetTimeout();

            if(builder.isDone()){
                streams.remove(builder.id);
                handleClientReceived(builder.build());
                currentStream = null;
            }
        }else{
            int p = object.getPriority();

            if(clientLoaded || p == Packet.priorityHigh){
                if(clientListeners.get(object.getClass()) != null){
                    clientListeners.get(object.getClass()).get(object);
                }else{
                    object.handleClient();
                }
            }else if(p != Packet.priorityLow){
                packetQueue.add(object);
            }
        }
    }

    /**
     * Call to handle a packet being received for the server.
     */
    public void handleServerReceived(NetConnection connection, Packet object){
		String cipherName3336 =  "DES";
		try{
			android.util.Log.d("cipherName-3336", javax.crypto.Cipher.getInstance(cipherName3336).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        object.handled();

        try{
            //handle object normally
            if(serverListeners.get(object.getClass()) != null){
                serverListeners.get(object.getClass()).get(connection, object);
            }else{
                object.handleServer(connection);
            }
        }catch(ValidateException e){
            //ignore invalid actions
            debug("Validation failed for '@': @", e.player, e.getMessage());
        }catch(RuntimeException e){
            //ignore indirect ValidateException-s
            if(e.getCause() instanceof ValidateException v){
                debug("Validation failed for '@': @", v.player, v.getMessage());
            }else{
                //rethrow if not ValidateException
                throw e;
            }
        }
    }

    /**
     * Pings a host in a pooled thread. If an error occurred, failed() should be called with the exception.
     * If the port is the default mindustry port, SRV records are checked too.
     */
    public void pingHost(String address, int port, Cons<Host> valid, Cons<Exception> failed){
        String cipherName3337 =  "DES";
		try{
			android.util.Log.d("cipherName-3337", javax.crypto.Cipher.getInstance(cipherName3337).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pingExecutor.submit(() -> provider.pingHost(address, port, valid, failed));
    }

    /**
     * Whether the net is active, e.g. whether this is a multiplayer game.
     */
    public boolean active(){
        String cipherName3338 =  "DES";
		try{
			android.util.Log.d("cipherName-3338", javax.crypto.Cipher.getInstance(cipherName3338).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return active;
    }

    /**
     * Whether this is a server or not.
     */
    public boolean server(){
        String cipherName3339 =  "DES";
		try{
			android.util.Log.d("cipherName-3339", javax.crypto.Cipher.getInstance(cipherName3339).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return server && active;
    }

    /**
     * Whether this is a client or not.
     */
    public boolean client(){
        String cipherName3340 =  "DES";
		try{
			android.util.Log.d("cipherName-3340", javax.crypto.Cipher.getInstance(cipherName3340).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !server && active;
    }

    public void dispose(){
        String cipherName3341 =  "DES";
		try{
			android.util.Log.d("cipherName-3341", javax.crypto.Cipher.getInstance(cipherName3341).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		provider.dispose();
        server = false;
        active = false;
    }

    /** Networking implementation. */
    public interface NetProvider{
        /** Connect to a server. */
        void connectClient(String ip, int port, Runnable success) throws IOException;

        /** Send an object to the server. */
        void sendClient(Object object, boolean reliable);

        /** Disconnect from the server. */
        void disconnectClient();

        /**
         * Discover servers. This should run the callback regardless of whether any servers are found. Should not block.
         * Callback should be run on the main thread.
         * @param done is the callback that should run after discovery.
         */
        void discoverServers(Cons<Host> callback, Runnable done);

        /**
         * Ping a host. If an error occurred, failed() should be called with the exception. This method should block.
         * If the port is the default mindustry port (6567), SRV records are checked too.
         */
        void pingHost(String address, int port, Cons<Host> valid, Cons<Exception> failed);

        /** Host a server at specified port. */
        void hostServer(int port) throws IOException;

        /** Return all connected users. */
        Iterable<? extends NetConnection> getConnections();

        /** Close the server connection. */
        void closeServer();

        /** Close all connections. */
        default void dispose(){
            disconnectClient();
            closeServer();
        }
    }
}
