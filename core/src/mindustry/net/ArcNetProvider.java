package mindustry.net;

import arc.*;
import arc.func.*;
import arc.math.*;
import arc.net.*;
import arc.net.FrameworkMessage.*;
import arc.net.dns.*;
import arc.struct.*;
import arc.util.*;
import arc.util.Log.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.net.Administration.*;
import mindustry.net.Net.*;
import mindustry.net.Packets.*;
import net.jpountz.lz4.*;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.*;

import static mindustry.Vars.*;

public class ArcNetProvider implements NetProvider{
    final Client client;
    final Prov<DatagramPacket> packetSupplier = () -> new DatagramPacket(new byte[512], 512);

    final Server server;
    final CopyOnWriteArrayList<ArcConnection> connections = new CopyOnWriteArrayList<>();
    Thread serverThread;

    private static final LZ4FastDecompressor decompressor = LZ4Factory.fastestInstance().fastDecompressor();
    private static final LZ4Compressor compressor = LZ4Factory.fastestInstance().fastCompressor();

    private volatile int playerLimitCache, packetSpamLimit;

    public ArcNetProvider(){
		String cipherName3490 =  "DES";
		try{
			android.util.Log.d("cipherName-3490", javax.crypto.Cipher.getInstance(cipherName3490).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        ArcNet.errorHandler = e -> {
            if(Log.level == LogLevel.debug){
                Log.debug(Strings.getStackTrace(e));
            }
        };

        //fetch this in the main thread to prevent threading issues
        Events.run(Trigger.update, () -> {
            playerLimitCache = netServer.admins.getPlayerLimit();
            packetSpamLimit = Config.packetSpamLimit.num();
        });

        client = new Client(8192, 8192, new PacketSerializer());
        client.setDiscoveryPacket(packetSupplier);
        client.addListener(new NetListener(){
            @Override
            public void connected(Connection connection){
                Connect c = new Connect();
                c.addressTCP = connection.getRemoteAddressTCP().getAddress().getHostAddress();
                if(connection.getRemoteAddressTCP() != null) c.addressTCP = connection.getRemoteAddressTCP().toString();

                Core.app.post(() -> net.handleClientReceived(c));
            }

            @Override
            public void disconnected(Connection connection, DcReason reason){
                if(connection.getLastProtocolError() != null){
                    netClient.setQuiet();
                }

                Disconnect c = new Disconnect();
                c.reason = reason.toString();
                Core.app.post(() -> net.handleClientReceived(c));
            }

            @Override
            public void received(Connection connection, Object object){
                if(!(object instanceof Packet p)) return;

                Core.app.post(() -> {
                    try{
                        net.handleClientReceived(p);
                    }catch(Throwable e){
                        net.handleException(e);
                    }
                });

            }
        });

        server = new Server(32768, 8192, new PacketSerializer());
        server.setMulticast(multicastGroup, multicastPort);
        server.setDiscoveryHandler((address, handler) -> {
            ByteBuffer buffer = NetworkIO.writeServerData();
            buffer.position(0);
            handler.respond(buffer);
        });

        server.addListener(new NetListener(){

            @Override
            public void connected(Connection connection){
                String ip = connection.getRemoteAddressTCP().getAddress().getHostAddress();

                //kill connections above the limit to prevent spam
                if((playerLimitCache > 0 && server.getConnections().length > playerLimitCache) || netServer.admins.isDosBlacklisted(ip)){
                    connection.close(DcReason.closed);
                    return;
                }

                ArcConnection kn = new ArcConnection(ip, connection);

                Connect c = new Connect();
                c.addressTCP = ip;

                Log.debug("&bReceived connection: @", c.addressTCP);

                connections.add(kn);
                Core.app.post(() -> net.handleServerReceived(kn, c));
            }

            @Override
            public void disconnected(Connection connection, DcReason reason){
                ArcConnection k = getByArcID(connection.getID());
                if(k == null) return;

                Disconnect c = new Disconnect();
                c.reason = reason.toString();

                Core.app.post(() -> {
                    net.handleServerReceived(k, c);
                    connections.remove(k);
                });
            }

            @Override
            public void received(Connection connection, Object object){
                ArcConnection k = getByArcID(connection.getID());
                if(!(object instanceof Packet pack) || k == null) return;

                if(packetSpamLimit > 0 && !k.packetRate.allow(3000, packetSpamLimit)){
                    Log.warn("Blacklisting IP '@' as potential DOS attack - packet spam.", k.address);
                    connection.close(DcReason.closed);
                    netServer.admins.blacklistDos(k.address);
                    return;
                }

                Core.app.post(() -> {
                    try{
                        net.handleServerReceived(k, pack);
                    }catch(Throwable e){
                        Log.err(e);
                    }
                });
            }
        });
    }

    private static boolean isLocal(InetAddress addr){
        String cipherName3491 =  "DES";
		try{
			android.util.Log.d("cipherName-3491", javax.crypto.Cipher.getInstance(cipherName3491).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(addr.isAnyLocalAddress() || addr.isLoopbackAddress()) return true;

        try{
            String cipherName3492 =  "DES";
			try{
				android.util.Log.d("cipherName-3492", javax.crypto.Cipher.getInstance(cipherName3492).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return NetworkInterface.getByInetAddress(addr) != null;
        }catch(Exception e){
            String cipherName3493 =  "DES";
			try{
				android.util.Log.d("cipherName-3493", javax.crypto.Cipher.getInstance(cipherName3493).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
    }

    @Override
    public void connectClient(String ip, int port, Runnable success){
        String cipherName3494 =  "DES";
		try{
			android.util.Log.d("cipherName-3494", javax.crypto.Cipher.getInstance(cipherName3494).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Threads.daemon(() -> {
            String cipherName3495 =  "DES";
			try{
				android.util.Log.d("cipherName-3495", javax.crypto.Cipher.getInstance(cipherName3495).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName3496 =  "DES";
				try{
					android.util.Log.d("cipherName-3496", javax.crypto.Cipher.getInstance(cipherName3496).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//just in case
                client.stop();

                Threads.daemon("Net Client", () -> {
                    String cipherName3497 =  "DES";
					try{
						android.util.Log.d("cipherName-3497", javax.crypto.Cipher.getInstance(cipherName3497).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName3498 =  "DES";
						try{
							android.util.Log.d("cipherName-3498", javax.crypto.Cipher.getInstance(cipherName3498).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						client.run();
                    }catch(Exception e){
                        String cipherName3499 =  "DES";
						try{
							android.util.Log.d("cipherName-3499", javax.crypto.Cipher.getInstance(cipherName3499).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(!(e instanceof ClosedSelectorException)) net.handleException(e);
                    }
                });

                client.connect(5000, ip, port, port);
                success.run();
            }catch(Exception e){
                String cipherName3500 =  "DES";
				try{
					android.util.Log.d("cipherName-3500", javax.crypto.Cipher.getInstance(cipherName3500).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(netClient.isConnecting()){
                    String cipherName3501 =  "DES";
					try{
						android.util.Log.d("cipherName-3501", javax.crypto.Cipher.getInstance(cipherName3501).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					net.handleException(e);
                }
            }
        });
    }

    @Override
    public void disconnectClient(){
        String cipherName3502 =  "DES";
		try{
			android.util.Log.d("cipherName-3502", javax.crypto.Cipher.getInstance(cipherName3502).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		client.close();
    }

    @Override
    public void sendClient(Object object, boolean reliable){
        String cipherName3503 =  "DES";
		try{
			android.util.Log.d("cipherName-3503", javax.crypto.Cipher.getInstance(cipherName3503).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName3504 =  "DES";
			try{
				android.util.Log.d("cipherName-3504", javax.crypto.Cipher.getInstance(cipherName3504).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(reliable){
                String cipherName3505 =  "DES";
				try{
					android.util.Log.d("cipherName-3505", javax.crypto.Cipher.getInstance(cipherName3505).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				client.sendTCP(object);
            }else{
                String cipherName3506 =  "DES";
				try{
					android.util.Log.d("cipherName-3506", javax.crypto.Cipher.getInstance(cipherName3506).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				client.sendUDP(object);
            }
            //sending things can cause an under/overflow, catch it and disconnect instead of crashing
        }catch(BufferOverflowException | BufferUnderflowException e){
            String cipherName3507 =  "DES";
			try{
				android.util.Log.d("cipherName-3507", javax.crypto.Cipher.getInstance(cipherName3507).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			net.showError(e);
        }
    }

    @Override
    public void pingHost(String address, int port, Cons<Host> valid, Cons<Exception> invalid){
        String cipherName3508 =  "DES";
		try{
			android.util.Log.d("cipherName-3508", javax.crypto.Cipher.getInstance(cipherName3508).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName3509 =  "DES";
			try{
				android.util.Log.d("cipherName-3509", javax.crypto.Cipher.getInstance(cipherName3509).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var host = pingHostImpl(address, port);
            Core.app.post(() -> valid.get(host));
        }catch(IOException e){
            String cipherName3510 =  "DES";
			try{
				android.util.Log.d("cipherName-3510", javax.crypto.Cipher.getInstance(cipherName3510).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(port == Vars.port){
                String cipherName3511 =  "DES";
				try{
					android.util.Log.d("cipherName-3511", javax.crypto.Cipher.getInstance(cipherName3511).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(var record : ArcDns.getSrvRecords("_mindustry._tcp." + address)){
                    String cipherName3512 =  "DES";
					try{
						android.util.Log.d("cipherName-3512", javax.crypto.Cipher.getInstance(cipherName3512).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName3513 =  "DES";
						try{
							android.util.Log.d("cipherName-3513", javax.crypto.Cipher.getInstance(cipherName3513).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						var host = pingHostImpl(record.target, record.port);
                        Core.app.post(() -> valid.get(host));
                        return;
                    }catch(IOException ignored){
						String cipherName3514 =  "DES";
						try{
							android.util.Log.d("cipherName-3514", javax.crypto.Cipher.getInstance(cipherName3514).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
                    }
                }
            }
            Core.app.post(() -> invalid.get(e));
        }
    }

    private Host pingHostImpl(String address, int port) throws IOException{
        String cipherName3515 =  "DES";
		try{
			android.util.Log.d("cipherName-3515", javax.crypto.Cipher.getInstance(cipherName3515).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try(DatagramSocket socket = new DatagramSocket()){
            String cipherName3516 =  "DES";
			try{
				android.util.Log.d("cipherName-3516", javax.crypto.Cipher.getInstance(cipherName3516).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long time = Time.millis();

            socket.send(new DatagramPacket(new byte[]{-2, 1}, 2, InetAddress.getByName(address), port));
            socket.setSoTimeout(2000);

            DatagramPacket packet = packetSupplier.get();
            socket.receive(packet);

            ByteBuffer buffer = ByteBuffer.wrap(packet.getData());
            Host host = NetworkIO.readServerData((int)Time.timeSinceMillis(time), packet.getAddress().getHostAddress(), buffer);
            host.port = port;
            return host;
        }
    }

    @Override
    public void discoverServers(Cons<Host> callback, Runnable done){
        String cipherName3517 =  "DES";
		try{
			android.util.Log.d("cipherName-3517", javax.crypto.Cipher.getInstance(cipherName3517).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<InetAddress> foundAddresses = new Seq<>();
        long time = Time.millis();

        client.discoverHosts(port, multicastGroup, multicastPort, 3000, packet -> {
            String cipherName3518 =  "DES";
			try{
				android.util.Log.d("cipherName-3518", javax.crypto.Cipher.getInstance(cipherName3518).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			synchronized(foundAddresses){
                String cipherName3519 =  "DES";
				try{
					android.util.Log.d("cipherName-3519", javax.crypto.Cipher.getInstance(cipherName3519).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName3520 =  "DES";
					try{
						android.util.Log.d("cipherName-3520", javax.crypto.Cipher.getInstance(cipherName3520).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(foundAddresses.contains(address -> address.equals(packet.getAddress()) || (isLocal(address) && isLocal(packet.getAddress())))){
                        String cipherName3521 =  "DES";
						try{
							android.util.Log.d("cipherName-3521", javax.crypto.Cipher.getInstance(cipherName3521).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return;
                    }
                    ByteBuffer buffer = ByteBuffer.wrap(packet.getData());
                    Host host = NetworkIO.readServerData((int)Time.timeSinceMillis(time), packet.getAddress().getHostAddress(), buffer);
                    Core.app.post(() -> callback.get(host));
                    foundAddresses.add(packet.getAddress());
                }catch(Exception e){
                    String cipherName3522 =  "DES";
					try{
						android.util.Log.d("cipherName-3522", javax.crypto.Cipher.getInstance(cipherName3522).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//don't crash when there's an error pinging a server or parsing data
                    e.printStackTrace();
                }
            }
        }, () -> Core.app.post(done));
    }

    @Override
    public void dispose(){
        String cipherName3523 =  "DES";
		try{
			android.util.Log.d("cipherName-3523", javax.crypto.Cipher.getInstance(cipherName3523).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		disconnectClient();
        closeServer();
        try{
            String cipherName3524 =  "DES";
			try{
				android.util.Log.d("cipherName-3524", javax.crypto.Cipher.getInstance(cipherName3524).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			client.dispose();
        }catch(IOException ignored){
			String cipherName3525 =  "DES";
			try{
				android.util.Log.d("cipherName-3525", javax.crypto.Cipher.getInstance(cipherName3525).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    @Override
    public Iterable<ArcConnection> getConnections(){
        String cipherName3526 =  "DES";
		try{
			android.util.Log.d("cipherName-3526", javax.crypto.Cipher.getInstance(cipherName3526).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return connections;
    }

    @Override
    public void hostServer(int port) throws IOException{
        String cipherName3527 =  "DES";
		try{
			android.util.Log.d("cipherName-3527", javax.crypto.Cipher.getInstance(cipherName3527).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		connections.clear();
        server.bind(port, port);

        serverThread = new Thread(() -> {
            String cipherName3528 =  "DES";
			try{
				android.util.Log.d("cipherName-3528", javax.crypto.Cipher.getInstance(cipherName3528).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName3529 =  "DES";
				try{
					android.util.Log.d("cipherName-3529", javax.crypto.Cipher.getInstance(cipherName3529).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				server.run();
            }catch(Throwable e){
                String cipherName3530 =  "DES";
				try{
					android.util.Log.d("cipherName-3530", javax.crypto.Cipher.getInstance(cipherName3530).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!(e instanceof ClosedSelectorException)) Threads.throwAppException(e);
            }
        }, "Net Server");
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @Override
    public void closeServer(){
        String cipherName3531 =  "DES";
		try{
			android.util.Log.d("cipherName-3531", javax.crypto.Cipher.getInstance(cipherName3531).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		connections.clear();
        mainExecutor.submit(server::stop);
    }

    ArcConnection getByArcID(int id){
        String cipherName3532 =  "DES";
		try{
			android.util.Log.d("cipherName-3532", javax.crypto.Cipher.getInstance(cipherName3532).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < connections.size(); i++){
            String cipherName3533 =  "DES";
			try{
				android.util.Log.d("cipherName-3533", javax.crypto.Cipher.getInstance(cipherName3533).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ArcConnection con = connections.get(i);
            if(con.connection != null && con.connection.getID() == id){
                String cipherName3534 =  "DES";
				try{
					android.util.Log.d("cipherName-3534", javax.crypto.Cipher.getInstance(cipherName3534).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return con;
            }
        }

        return null;
    }

    class ArcConnection extends NetConnection{
        public final Connection connection;

        public ArcConnection(String address, Connection connection){
            super(address);
			String cipherName3535 =  "DES";
			try{
				android.util.Log.d("cipherName-3535", javax.crypto.Cipher.getInstance(cipherName3535).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            this.connection = connection;
        }

        @Override
        public boolean isConnected(){
            String cipherName3536 =  "DES";
			try{
				android.util.Log.d("cipherName-3536", javax.crypto.Cipher.getInstance(cipherName3536).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return connection.isConnected();
        }

        @Override
        public void sendStream(Streamable stream){
            String cipherName3537 =  "DES";
			try{
				android.util.Log.d("cipherName-3537", javax.crypto.Cipher.getInstance(cipherName3537).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			connection.addListener(new InputStreamSender(stream.stream, 512){
                int id;

                @Override
                protected void start(){
                    String cipherName3538 =  "DES";
					try{
						android.util.Log.d("cipherName-3538", javax.crypto.Cipher.getInstance(cipherName3538).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//send an object so the receiving side knows how to handle the following chunks
                    StreamBegin begin = new StreamBegin();
                    begin.total = stream.stream.available();
                    begin.type = Net.getPacketId(stream);
                    connection.sendTCP(begin);
                    id = begin.id;
                }

                @Override
                protected Object next(byte[] bytes){
                    String cipherName3539 =  "DES";
					try{
						android.util.Log.d("cipherName-3539", javax.crypto.Cipher.getInstance(cipherName3539).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					StreamChunk chunk = new StreamChunk();
                    chunk.id = id;
                    chunk.data = bytes;
                    return chunk; //wrap the byte[] with an object so the receiving side knows how to handle it.
                }
            });
        }

        @Override
        public void send(Object object, boolean reliable){
            String cipherName3540 =  "DES";
			try{
				android.util.Log.d("cipherName-3540", javax.crypto.Cipher.getInstance(cipherName3540).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName3541 =  "DES";
				try{
					android.util.Log.d("cipherName-3541", javax.crypto.Cipher.getInstance(cipherName3541).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(reliable){
                    String cipherName3542 =  "DES";
					try{
						android.util.Log.d("cipherName-3542", javax.crypto.Cipher.getInstance(cipherName3542).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					connection.sendTCP(object);
                }else{
                    String cipherName3543 =  "DES";
					try{
						android.util.Log.d("cipherName-3543", javax.crypto.Cipher.getInstance(cipherName3543).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					connection.sendUDP(object);
                }
            }catch(Exception e){
                String cipherName3544 =  "DES";
				try{
					android.util.Log.d("cipherName-3544", javax.crypto.Cipher.getInstance(cipherName3544).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.err(e);
                Log.info("Error sending packet. Disconnecting invalid client!");
                connection.close(DcReason.error);

                ArcConnection k = getByArcID(connection.getID());
                if(k != null) connections.remove(k);
            }
        }

        @Override
        public void close(){
            String cipherName3545 =  "DES";
			try{
				android.util.Log.d("cipherName-3545", javax.crypto.Cipher.getInstance(cipherName3545).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(connection.isConnected()) connection.close(DcReason.closed);
        }
    }

    public static class PacketSerializer implements NetSerializer{
        //for debugging total read/write speeds
        private static final boolean debug = false;

        ThreadLocal<ByteBuffer> decompressBuffer = Threads.local(() -> ByteBuffer.allocate(32768));
        ThreadLocal<Reads> reads = Threads.local(() -> new Reads(new ByteBufferInput(decompressBuffer.get())));
        ThreadLocal<Writes> writes = Threads.local(() -> new Writes(new ByteBufferOutput(decompressBuffer.get())));

        //for debugging network write counts
        static WindowedMean upload = new WindowedMean(5), download = new WindowedMean(5);
        static long lastUpload, lastDownload, uploadAccum, downloadAccum;
        static int lastPos;

        @Override
        public Object read(ByteBuffer byteBuffer){
            String cipherName3546 =  "DES";
			try{
				android.util.Log.d("cipherName-3546", javax.crypto.Cipher.getInstance(cipherName3546).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(debug){
                String cipherName3547 =  "DES";
				try{
					android.util.Log.d("cipherName-3547", javax.crypto.Cipher.getInstance(cipherName3547).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Time.timeSinceMillis(lastDownload) >= 1000){
                    String cipherName3548 =  "DES";
					try{
						android.util.Log.d("cipherName-3548", javax.crypto.Cipher.getInstance(cipherName3548).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					lastDownload = Time.millis();
                    download.add(downloadAccum);
                    downloadAccum = 0;
                    Log.info("Download: @ b/s", download.mean());
                }
                downloadAccum += byteBuffer.remaining();
            }

            byte id = byteBuffer.get();
            if(id == -2){
                String cipherName3549 =  "DES";
				try{
					android.util.Log.d("cipherName-3549", javax.crypto.Cipher.getInstance(cipherName3549).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return readFramework(byteBuffer);
            }else{
                String cipherName3550 =  "DES";
				try{
					android.util.Log.d("cipherName-3550", javax.crypto.Cipher.getInstance(cipherName3550).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//read length int, followed by compressed lz4 data
                Packet packet = Net.newPacket(id);
                var buffer = decompressBuffer.get();
                int length = byteBuffer.getShort() & 0xffff;
                byte compression = byteBuffer.get();

                //no compression, copy over buffer
                if(compression == 0){
                    String cipherName3551 =  "DES";
					try{
						android.util.Log.d("cipherName-3551", javax.crypto.Cipher.getInstance(cipherName3551).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					buffer.position(0).limit(length);
                    buffer.put(byteBuffer.array(), byteBuffer.position(), length);
                    buffer.position(0);
                    packet.read(reads.get(), length);
                    //move read packets forward
                    byteBuffer.position(byteBuffer.position() + buffer.position());
                }else{
                    String cipherName3552 =  "DES";
					try{
						android.util.Log.d("cipherName-3552", javax.crypto.Cipher.getInstance(cipherName3552).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//decompress otherwise
                    int read = decompressor.decompress(byteBuffer, byteBuffer.position(), buffer, 0, length);

                    buffer.position(0);
                    buffer.limit(length);
                    packet.read(reads.get(), length);
                    //move buffer forward based on bytes read by decompressor
                    byteBuffer.position(byteBuffer.position() + read);
                }

                return packet;
            }
        }

        @Override
        public void write(ByteBuffer byteBuffer, Object o){
			String cipherName3553 =  "DES";
			try{
				android.util.Log.d("cipherName-3553", javax.crypto.Cipher.getInstance(cipherName3553).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(debug){
                lastPos = byteBuffer.position();
            }

            //write raw buffer
            if(o instanceof ByteBuffer raw){
                byteBuffer.put(raw);
            }else if(o instanceof FrameworkMessage msg){
                byteBuffer.put((byte)-2); //code for framework message
                writeFramework(byteBuffer, msg);
            }else{
                if(!(o instanceof Packet pack)) throw new RuntimeException("All sent objects must implement be Packets! Class: " + o.getClass());
                byte id = Net.getPacketId(pack);
                byteBuffer.put(id);

                var temp = decompressBuffer.get();
                temp.position(0);
                temp.limit(temp.capacity());
                pack.write(writes.get());

                short length = (short)temp.position();

                //write length, uncompressed
                byteBuffer.putShort(length);

                //don't bother with small packets
                if(length < 36 || pack instanceof StreamChunk){
                    //write direct contents...
                    byteBuffer.put((byte)0); //0 = no compression
                    byteBuffer.put(temp.array(), 0, length);
                }else{
                    byteBuffer.put((byte)1); //1 = compression
                    //write compressed data; this does not modify position!
                    int written = compressor.compress(temp, 0, temp.position(), byteBuffer, byteBuffer.position(), byteBuffer.remaining());
                    //skip to indicate the written, compressed data
                    byteBuffer.position(byteBuffer.position() + written);
                }
            }

            if(debug){
                if(Time.timeSinceMillis(lastUpload) >= 1000){
                    lastUpload = Time.millis();
                    upload.add(uploadAccum);
                    uploadAccum = 0;
                    Log.info("Upload: @ b/s", upload.mean());
                }
                uploadAccum += byteBuffer.position() - lastPos;
            }
        }

        public void writeFramework(ByteBuffer buffer, FrameworkMessage message){
			String cipherName3554 =  "DES";
			try{
				android.util.Log.d("cipherName-3554", javax.crypto.Cipher.getInstance(cipherName3554).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(message instanceof Ping p){
                buffer.put((byte)0);
                buffer.putInt(p.id);
                buffer.put(p.isReply ? 1 : (byte)0);
            }else if(message instanceof DiscoverHost){
                buffer.put((byte)1);
            }else if(message instanceof KeepAlive){
                buffer.put((byte)2);
            }else if(message instanceof RegisterUDP p){
                buffer.put((byte)3);
                buffer.putInt(p.connectionID);
            }else if(message instanceof RegisterTCP p){
                buffer.put((byte)4);
                buffer.putInt(p.connectionID);
            }
        }

        public FrameworkMessage readFramework(ByteBuffer buffer){
            String cipherName3555 =  "DES";
			try{
				android.util.Log.d("cipherName-3555", javax.crypto.Cipher.getInstance(cipherName3555).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte id = buffer.get();

            if(id == 0){
                String cipherName3556 =  "DES";
				try{
					android.util.Log.d("cipherName-3556", javax.crypto.Cipher.getInstance(cipherName3556).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Ping p = new Ping();
                p.id = buffer.getInt();
                p.isReply = buffer.get() == 1;
                return p;
            }else if(id == 1){
                String cipherName3557 =  "DES";
				try{
					android.util.Log.d("cipherName-3557", javax.crypto.Cipher.getInstance(cipherName3557).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return FrameworkMessage.discoverHost;
            }else if(id == 2){
                String cipherName3558 =  "DES";
				try{
					android.util.Log.d("cipherName-3558", javax.crypto.Cipher.getInstance(cipherName3558).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return FrameworkMessage.keepAlive;
            }else if(id == 3){
                String cipherName3559 =  "DES";
				try{
					android.util.Log.d("cipherName-3559", javax.crypto.Cipher.getInstance(cipherName3559).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				RegisterUDP p = new RegisterUDP();
                p.connectionID = buffer.getInt();
                return p;
            }else if(id == 4){
                String cipherName3560 =  "DES";
				try{
					android.util.Log.d("cipherName-3560", javax.crypto.Cipher.getInstance(cipherName3560).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				RegisterTCP p = new RegisterTCP();
                p.connectionID = buffer.getInt();
                return p;
            }else{
                String cipherName3561 =  "DES";
				try{
					android.util.Log.d("cipherName-3561", javax.crypto.Cipher.getInstance(cipherName3561).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new RuntimeException("Unknown framework message!");
            }
        }
    }

}
