package mindustry.desktop.steam;

import arc.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;
import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.SteamMatchmaking.*;
import com.codedisaster.steamworks.SteamNetworking.*;
import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.net.ArcNetProvider.*;
import mindustry.net.*;
import mindustry.net.Net.*;
import mindustry.net.Packets.*;

import java.io.*;
import java.nio.*;
import java.util.concurrent.*;

import static mindustry.Vars.*;

public class SNet implements SteamNetworkingCallback, SteamMatchmakingCallback, SteamFriendsCallback, NetProvider{
    public final SteamNetworking snet = new SteamNetworking(this);
    public final SteamMatchmaking smat = new SteamMatchmaking(this);
    public final SteamFriends friends = new SteamFriends(this);

    final NetProvider provider;

    final PacketSerializer serializer = new PacketSerializer();
    final ByteBuffer writeBuffer = ByteBuffer.allocateDirect(16384);
    final ByteBuffer readBuffer = ByteBuffer.allocateDirect(16384);
    final ByteBuffer readCopyBuffer = ByteBuffer.allocate(writeBuffer.capacity());

    final CopyOnWriteArrayList<SteamConnection> connections = new CopyOnWriteArrayList<>();
    final IntMap<SteamConnection> steamConnections = new IntMap<>(); //maps steam ID -> valid net connection

    SteamID currentLobby, currentServer;
    Cons<Host> lobbyCallback;
    Runnable lobbyDoneCallback, joinCallback;

    public SNet(NetProvider provider){
        String cipherName17874 =  "DES";
		try{
			android.util.Log.d("cipherName-17874", javax.crypto.Cipher.getInstance(cipherName17874).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.provider = provider;

        Events.on(ClientLoadEvent.class, e -> Core.app.addListener(new ApplicationListener(){
            //read packets
            int length;
            SteamID from = new SteamID();

            @Override
            public void update(){
                String cipherName17875 =  "DES";
				try{
					android.util.Log.d("cipherName-17875", javax.crypto.Cipher.getInstance(cipherName17875).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				while((length = snet.isP2PPacketAvailable(0)) != 0){
                    String cipherName17876 =  "DES";
					try{
						android.util.Log.d("cipherName-17876", javax.crypto.Cipher.getInstance(cipherName17876).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName17877 =  "DES";
						try{
							android.util.Log.d("cipherName-17877", javax.crypto.Cipher.getInstance(cipherName17877).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						readBuffer.position(0).limit(readBuffer.capacity());
                        //lz4 chokes on direct buffers, so copy the bytes over
                        int len = snet.readP2PPacket(from, readBuffer, 0);
                        readBuffer.limit(len);
                        readCopyBuffer.position(0);
                        readCopyBuffer.put(readBuffer);
                        readCopyBuffer.position(0);
                        int fromID = from.getAccountID();
                        Object output = serializer.read(readCopyBuffer);

                        //it may be theoretically possible for this to be a framework message, if the packet is malicious or corrupted
                        if(!(output instanceof Packet)) return;

                        Packet pack = (Packet)output;

                        if(net.server()){
                            String cipherName17878 =  "DES";
							try{
								android.util.Log.d("cipherName-17878", javax.crypto.Cipher.getInstance(cipherName17878).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							SteamConnection con = steamConnections.get(fromID);
                            try{
                                String cipherName17879 =  "DES";
								try{
									android.util.Log.d("cipherName-17879", javax.crypto.Cipher.getInstance(cipherName17879).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//accept users on request
                                if(con == null){
                                    String cipherName17880 =  "DES";
									try{
										android.util.Log.d("cipherName-17880", javax.crypto.Cipher.getInstance(cipherName17880).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									con = new SteamConnection(SteamID.createFromNativeHandle(from.handle()));
                                    Connect c = new Connect();
                                    c.addressTCP = "steam:" + from.getAccountID();

                                    Log.info("&bReceived STEAM connection: @", c.addressTCP);

                                    steamConnections.put(from.getAccountID(), con);
                                    connections.add(con);
                                    net.handleServerReceived(con, c);
                                }

                                net.handleServerReceived(con, pack);
                            }catch(Throwable e){
                                String cipherName17881 =  "DES";
								try{
									android.util.Log.d("cipherName-17881", javax.crypto.Cipher.getInstance(cipherName17881).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Log.err(e);
                            }
                        }else if(currentServer != null && fromID == currentServer.getAccountID()){
                            String cipherName17882 =  "DES";
							try{
								android.util.Log.d("cipherName-17882", javax.crypto.Cipher.getInstance(cipherName17882).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							try{
                                String cipherName17883 =  "DES";
								try{
									android.util.Log.d("cipherName-17883", javax.crypto.Cipher.getInstance(cipherName17883).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								net.handleClientReceived(pack);
                            }catch(Throwable t){
                                String cipherName17884 =  "DES";
								try{
									android.util.Log.d("cipherName-17884", javax.crypto.Cipher.getInstance(cipherName17884).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								net.handleException(t);
                            }
                        }
                    }catch(Exception e){
                        String cipherName17885 =  "DES";
						try{
							android.util.Log.d("cipherName-17885", javax.crypto.Cipher.getInstance(cipherName17885).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(net.server()){
                            String cipherName17886 =  "DES";
							try{
								android.util.Log.d("cipherName-17886", javax.crypto.Cipher.getInstance(cipherName17886).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Log.err(e);
                        }else{
                            String cipherName17887 =  "DES";
							try{
								android.util.Log.d("cipherName-17887", javax.crypto.Cipher.getInstance(cipherName17887).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							net.showError(e);
                        }
                    }
                }
            }
        }));

        Events.on(WaveEvent.class, e -> updateWave());
        Events.run(Trigger.newGame, this::updateWave);
    }

    public boolean isSteamClient(){
        String cipherName17888 =  "DES";
		try{
			android.util.Log.d("cipherName-17888", javax.crypto.Cipher.getInstance(cipherName17888).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return currentServer != null;
    }

    @Override
    public void connectClient(String ip, int port, Runnable success) throws IOException{
        String cipherName17889 =  "DES";
		try{
			android.util.Log.d("cipherName-17889", javax.crypto.Cipher.getInstance(cipherName17889).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(ip.startsWith("steam:")){
            String cipherName17890 =  "DES";
			try{
				android.util.Log.d("cipherName-17890", javax.crypto.Cipher.getInstance(cipherName17890).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String lobbyname = ip.substring("steam:".length());
            try{
                String cipherName17891 =  "DES";
				try{
					android.util.Log.d("cipherName-17891", javax.crypto.Cipher.getInstance(cipherName17891).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SteamID lobby = SteamID.createFromNativeHandle(Long.parseLong(lobbyname));
                joinCallback = success;
                smat.joinLobby(lobby);
            }catch(NumberFormatException e){
                String cipherName17892 =  "DES";
				try{
					android.util.Log.d("cipherName-17892", javax.crypto.Cipher.getInstance(cipherName17892).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new IOException("Invalid Steam ID: " + lobbyname);
            }
        }else{
            String cipherName17893 =  "DES";
			try{
				android.util.Log.d("cipherName-17893", javax.crypto.Cipher.getInstance(cipherName17893).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			provider.connectClient(ip, port, success);
        }
    }

    @Override
    public void sendClient(Object object, boolean reliable){
        String cipherName17894 =  "DES";
		try{
			android.util.Log.d("cipherName-17894", javax.crypto.Cipher.getInstance(cipherName17894).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isSteamClient()){
            String cipherName17895 =  "DES";
			try{
				android.util.Log.d("cipherName-17895", javax.crypto.Cipher.getInstance(cipherName17895).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(currentServer == null){
                String cipherName17896 =  "DES";
				try{
					android.util.Log.d("cipherName-17896", javax.crypto.Cipher.getInstance(cipherName17896).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.info("Not connected, quitting.");
                return;
            }

            try{
                String cipherName17897 =  "DES";
				try{
					android.util.Log.d("cipherName-17897", javax.crypto.Cipher.getInstance(cipherName17897).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeBuffer.limit(writeBuffer.capacity());
                writeBuffer.position(0);
                serializer.write(writeBuffer, object);
                int length = writeBuffer.position();
                writeBuffer.flip();

                snet.sendP2PPacket(currentServer, writeBuffer, reliable || length >= 1000 ? P2PSend.Reliable : P2PSend.UnreliableNoDelay, 0);
            }catch(Exception e){
                String cipherName17898 =  "DES";
				try{
					android.util.Log.d("cipherName-17898", javax.crypto.Cipher.getInstance(cipherName17898).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				net.showError(e);
            }
        }else{
            String cipherName17899 =  "DES";
			try{
				android.util.Log.d("cipherName-17899", javax.crypto.Cipher.getInstance(cipherName17899).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			provider.sendClient(object, reliable);
        }
    }

    @Override
    public void disconnectClient(){
        String cipherName17900 =  "DES";
		try{
			android.util.Log.d("cipherName-17900", javax.crypto.Cipher.getInstance(cipherName17900).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isSteamClient()){
            String cipherName17901 =  "DES";
			try{
				android.util.Log.d("cipherName-17901", javax.crypto.Cipher.getInstance(cipherName17901).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(currentLobby != null){
                String cipherName17902 =  "DES";
				try{
					android.util.Log.d("cipherName-17902", javax.crypto.Cipher.getInstance(cipherName17902).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				smat.leaveLobby(currentLobby);
                snet.closeP2PSessionWithUser(currentServer);
                currentServer = null;
                currentLobby = null;
                net.handleClientReceived(new Disconnect());
            }
        }else{
            String cipherName17903 =  "DES";
			try{
				android.util.Log.d("cipherName-17903", javax.crypto.Cipher.getInstance(cipherName17903).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			provider.disconnectClient();
        }
    }

    @Override
    public void discoverServers(Cons<Host> callback, Runnable done){
        String cipherName17904 =  "DES";
		try{
			android.util.Log.d("cipherName-17904", javax.crypto.Cipher.getInstance(cipherName17904).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		smat.addRequestLobbyListResultCountFilter(32);
        smat.addRequestLobbyListDistanceFilter(LobbyDistanceFilter.Worldwide);
        smat.requestLobbyList();
        lobbyCallback = callback;

        //after the steam lobby is done discovering, look for local network servers.
        lobbyDoneCallback = () -> provider.discoverServers(callback, done);
    }

    @Override
    public void pingHost(String address, int port, Cons<Host> valid, Cons<Exception> failed){
        String cipherName17905 =  "DES";
		try{
			android.util.Log.d("cipherName-17905", javax.crypto.Cipher.getInstance(cipherName17905).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		provider.pingHost(address, port, valid, failed);
    }

    @Override
    public void hostServer(int port) throws IOException{
        String cipherName17906 =  "DES";
		try{
			android.util.Log.d("cipherName-17906", javax.crypto.Cipher.getInstance(cipherName17906).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		provider.hostServer(port);
        smat.createLobby(Core.settings.getBool("publichost") ? LobbyType.Public : LobbyType.FriendsOnly, Core.settings.getInt("playerlimit"));

        Core.app.post(() -> Core.app.post(() -> Core.app.post(() -> Log.info("Server: @\nClient: @\nActive: @", net.server(), net.client(), net.active()))));
    }

    public void updateLobby(){
        String cipherName17907 =  "DES";
		try{
			android.util.Log.d("cipherName-17907", javax.crypto.Cipher.getInstance(cipherName17907).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(currentLobby != null && net.server()){
            String cipherName17908 =  "DES";
			try{
				android.util.Log.d("cipherName-17908", javax.crypto.Cipher.getInstance(cipherName17908).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			smat.setLobbyType(currentLobby, Core.settings.getBool("publichost") ? LobbyType.Public : LobbyType.FriendsOnly);
            smat.setLobbyMemberLimit(currentLobby, Core.settings.getInt("playerlimit"));
        }
    }
    
    void updateWave(){
        String cipherName17909 =  "DES";
		try{
			android.util.Log.d("cipherName-17909", javax.crypto.Cipher.getInstance(cipherName17909).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(currentLobby != null && net.server()){
            String cipherName17910 =  "DES";
			try{
				android.util.Log.d("cipherName-17910", javax.crypto.Cipher.getInstance(cipherName17910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			smat.setLobbyData(currentLobby, "mapname", state.map.name());
            smat.setLobbyData(currentLobby, "wave", state.wave + "");
            smat.setLobbyData(currentLobby, "gamemode", state.rules.mode().name() + "");
        }
    }

    @Override
    public void closeServer(){
        String cipherName17911 =  "DES";
		try{
			android.util.Log.d("cipherName-17911", javax.crypto.Cipher.getInstance(cipherName17911).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		provider.closeServer();

        if(currentLobby != null){
            String cipherName17912 =  "DES";
			try{
				android.util.Log.d("cipherName-17912", javax.crypto.Cipher.getInstance(cipherName17912).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			smat.leaveLobby(currentLobby);
            for(SteamConnection con : steamConnections.values()){
                String cipherName17913 =  "DES";
				try{
					android.util.Log.d("cipherName-17913", javax.crypto.Cipher.getInstance(cipherName17913).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.close();
            }
            currentLobby = null;
        }

        steamConnections.clear();
    }

    @Override
    public Iterable<? extends NetConnection> getConnections(){
        String cipherName17914 =  "DES";
		try{
			android.util.Log.d("cipherName-17914", javax.crypto.Cipher.getInstance(cipherName17914).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//merge provider connections
        CopyOnWriteArrayList<NetConnection> connectionsOut = new CopyOnWriteArrayList<>(connections);
        for(NetConnection c : provider.getConnections()) connectionsOut.add(c);
        return connectionsOut;
    }

    void disconnectSteamUser(SteamID steamid){
        String cipherName17915 =  "DES";
		try{
			android.util.Log.d("cipherName-17915", javax.crypto.Cipher.getInstance(cipherName17915).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//a client left
        int sid = steamid.getAccountID();
        snet.closeP2PSessionWithUser(steamid);

        if(steamConnections.containsKey(sid)){
            String cipherName17916 =  "DES";
			try{
				android.util.Log.d("cipherName-17916", javax.crypto.Cipher.getInstance(cipherName17916).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SteamConnection con = steamConnections.get(sid);
            net.handleServerReceived(con, new Disconnect());
            steamConnections.remove(sid);
            connections.remove(con);
        }
    }

    @Override
    public void onLobbyInvite(SteamID steamIDUser, SteamID steamIDLobby, long gameID){
        String cipherName17917 =  "DES";
		try{
			android.util.Log.d("cipherName-17917", javax.crypto.Cipher.getInstance(cipherName17917).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.info("onLobbyInvite @ @ @", steamIDLobby.getAccountID(), steamIDUser.getAccountID(), gameID);
    }

    @Override
    public void onLobbyEnter(SteamID steamIDLobby, int chatPermissions, boolean blocked, ChatRoomEnterResponse response){
        String cipherName17918 =  "DES";
		try{
			android.util.Log.d("cipherName-17918", javax.crypto.Cipher.getInstance(cipherName17918).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.info("onLobbyEnter @ @", steamIDLobby.getAccountID(), response);

        if(response != ChatRoomEnterResponse.Success){
            String cipherName17919 =  "DES";
			try{
				android.util.Log.d("cipherName-17919", javax.crypto.Cipher.getInstance(cipherName17919).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.loadfrag.hide();
            ui.showErrorMessage(Core.bundle.format("cantconnect", response.toString()));
            return;
        }

        int version = Strings.parseInt(smat.getLobbyData(steamIDLobby, "version"), -1);

        //check version
        if(version != Version.build){
            String cipherName17920 =  "DES";
			try{
				android.util.Log.d("cipherName-17920", javax.crypto.Cipher.getInstance(cipherName17920).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.loadfrag.hide();
            ui.showInfo("[scarlet]" + (version > Version.build ? KickReason.clientOutdated : KickReason.serverOutdated).toString() + "\n[]" +
                Core.bundle.format("server.versions", Version.build, version));
            smat.leaveLobby(steamIDLobby);
            return;
        }

        logic.reset();
        net.reset();

        currentLobby = steamIDLobby;
        currentServer = smat.getLobbyOwner(steamIDLobby);

        Log.info("Connect to owner @: @", currentServer.getAccountID(), friends.getFriendPersonaName(currentServer));

        if(joinCallback != null){
            String cipherName17921 =  "DES";
			try{
				android.util.Log.d("cipherName-17921", javax.crypto.Cipher.getInstance(cipherName17921).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			joinCallback.run();
            joinCallback = null;
        }

        Connect con = new Connect();
        con.addressTCP = "steam:" + currentServer.getAccountID();

        net.setClientConnected();
        net.handleClientReceived(con);

        Core.app.post(() -> Core.app.post(() -> Core.app.post(() -> Log.info("Server: @\nClient: @\nActive: @", net.server(), net.client(), net.active()))));
    }

    @Override
    public void onLobbyChatUpdate(SteamID lobby, SteamID who, SteamID changer, ChatMemberStateChange change){
        String cipherName17922 =  "DES";
		try{
			android.util.Log.d("cipherName-17922", javax.crypto.Cipher.getInstance(cipherName17922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.info("lobby @: @ caused @'s change: @", lobby.getAccountID(), who.getAccountID(), changer.getAccountID(), change);
        if(change == ChatMemberStateChange.Disconnected || change == ChatMemberStateChange.Left){
            String cipherName17923 =  "DES";
			try{
				android.util.Log.d("cipherName-17923", javax.crypto.Cipher.getInstance(cipherName17923).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(net.client()){
                String cipherName17924 =  "DES";
				try{
					android.util.Log.d("cipherName-17924", javax.crypto.Cipher.getInstance(cipherName17924).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//host left, leave as well
                if(who.equals(currentServer) || who.equals(currentLobby)){
                    String cipherName17925 =  "DES";
					try{
						android.util.Log.d("cipherName-17925", javax.crypto.Cipher.getInstance(cipherName17925).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					net.disconnect();
                    Log.info("Current host left.");
                }
            }else{
                String cipherName17926 =  "DES";
				try{
					android.util.Log.d("cipherName-17926", javax.crypto.Cipher.getInstance(cipherName17926).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//a client left
                disconnectSteamUser(who);
            }
        }
    }

    @Override
    public void onLobbyMatchList(int matches){
        String cipherName17927 =  "DES";
		try{
			android.util.Log.d("cipherName-17927", javax.crypto.Cipher.getInstance(cipherName17927).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.info("found @ matches", matches);

        if(lobbyDoneCallback != null){
            String cipherName17928 =  "DES";
			try{
				android.util.Log.d("cipherName-17928", javax.crypto.Cipher.getInstance(cipherName17928).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Seq<Host> hosts = new Seq<>();
            for(int i = 0; i < matches; i++){
                String cipherName17929 =  "DES";
				try{
					android.util.Log.d("cipherName-17929", javax.crypto.Cipher.getInstance(cipherName17929).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName17930 =  "DES";
					try{
						android.util.Log.d("cipherName-17930", javax.crypto.Cipher.getInstance(cipherName17930).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					SteamID lobby = smat.getLobbyByIndex(i);
                    if(smat.getLobbyData(lobby, "hidden").equals("true")) continue;
                    String mode = smat.getLobbyData(lobby, "gamemode");
                    //make sure versions are equal, don't list incompatible lobbies
                    if(mode == null || mode.isEmpty() || (Version.build != -1 && Strings.parseInt(smat.getLobbyData(lobby, "version"), -1) != Version.build)) continue;
                    Host out = new Host(
                        -1, //invalid ping
                        smat.getLobbyData(lobby, "name"),
                        "steam:" + lobby.handle(),
                        smat.getLobbyData(lobby, "mapname"),
                        Strings.parseInt(smat.getLobbyData(lobby, "wave"), -1),
                        smat.getNumLobbyMembers(lobby),
                        Strings.parseInt(smat.getLobbyData(lobby, "version"), -1),
                        smat.getLobbyData(lobby, "versionType"),
                        Gamemode.valueOf(mode),
                        smat.getLobbyMemberLimit(lobby),
                        "",
                        null
                    );
                    hosts.add(out);
                }catch(Exception e){
                    String cipherName17931 =  "DES";
					try{
						android.util.Log.d("cipherName-17931", javax.crypto.Cipher.getInstance(cipherName17931).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.err(e);
                }
            }

            hosts.sort(Structs.comparingInt(h -> -h.players));
            hosts.each(lobbyCallback);

            lobbyDoneCallback.run();
        }
    }

    @Override
    public void onLobbyCreated(SteamResult result, SteamID steamID){
        String cipherName17932 =  "DES";
		try{
			android.util.Log.d("cipherName-17932", javax.crypto.Cipher.getInstance(cipherName17932).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!net.server()){
            String cipherName17933 =  "DES";
			try{
				android.util.Log.d("cipherName-17933", javax.crypto.Cipher.getInstance(cipherName17933).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.info("Lobby created on server: @, ignoring.", steamID);
            return;
        }

        Log.info("Lobby @ created? @", result, steamID.getAccountID());
        if(result == SteamResult.OK){
            String cipherName17934 =  "DES";
			try{
				android.util.Log.d("cipherName-17934", javax.crypto.Cipher.getInstance(cipherName17934).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			currentLobby = steamID;

            smat.setLobbyData(steamID, "name", player.name);
            smat.setLobbyData(steamID, "mapname", state.map.name());
            smat.setLobbyData(steamID, "version", Version.build + "");
            smat.setLobbyData(steamID, "versionType", Version.type);
            smat.setLobbyData(steamID, "wave", state.wave + "");
            smat.setLobbyData(steamID, "gamemode", state.rules.mode().name() + "");
        }
    }

    public void showFriendInvites(){
        String cipherName17935 =  "DES";
		try{
			android.util.Log.d("cipherName-17935", javax.crypto.Cipher.getInstance(cipherName17935).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(currentLobby != null){
            String cipherName17936 =  "DES";
			try{
				android.util.Log.d("cipherName-17936", javax.crypto.Cipher.getInstance(cipherName17936).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			friends.activateGameOverlayInviteDialog(currentLobby);
            Log.info("Activating overlay dialog");
        }
    }

    @Override
    public void onP2PSessionConnectFail(SteamID steamIDRemote, P2PSessionError sessionError){
        String cipherName17937 =  "DES";
		try{
			android.util.Log.d("cipherName-17937", javax.crypto.Cipher.getInstance(cipherName17937).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(net.server()){
            String cipherName17938 =  "DES";
			try{
				android.util.Log.d("cipherName-17938", javax.crypto.Cipher.getInstance(cipherName17938).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.info("@ has disconnected: @", steamIDRemote.getAccountID(), sessionError);
            disconnectSteamUser(steamIDRemote);
        }else if(steamIDRemote.equals(currentServer)){
            String cipherName17939 =  "DES";
			try{
				android.util.Log.d("cipherName-17939", javax.crypto.Cipher.getInstance(cipherName17939).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.info("Disconnected! @: @", steamIDRemote.getAccountID(), sessionError);
            net.handleClientReceived(new Disconnect());
        }
    }

    @Override
    public void onP2PSessionRequest(SteamID steamIDRemote){
        String cipherName17940 =  "DES";
		try{
			android.util.Log.d("cipherName-17940", javax.crypto.Cipher.getInstance(cipherName17940).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.info("Connection request: @", steamIDRemote.getAccountID());
        if(net.server()){
            String cipherName17941 =  "DES";
			try{
				android.util.Log.d("cipherName-17941", javax.crypto.Cipher.getInstance(cipherName17941).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.info("Am server, accepting request from " + steamIDRemote.getAccountID());
            snet.acceptP2PSessionWithUser(steamIDRemote);
        }
    }

    @Override
    public void onGameLobbyJoinRequested(SteamID lobby, SteamID steamIDFriend){
        String cipherName17942 =  "DES";
		try{
			android.util.Log.d("cipherName-17942", javax.crypto.Cipher.getInstance(cipherName17942).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.info("onGameLobbyJoinRequested @ @", lobby, steamIDFriend);
        smat.joinLobby(lobby);
    }

    public class SteamConnection extends NetConnection{
        final SteamID sid;

        public SteamConnection(SteamID sid){
            super(sid.getAccountID() + "");
			String cipherName17943 =  "DES";
			try{
				android.util.Log.d("cipherName-17943", javax.crypto.Cipher.getInstance(cipherName17943).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            this.sid = sid;
            Log.info("Created STEAM connection: @", sid.getAccountID());
        }

        @Override
        public void send(Object object, boolean reliable){
            String cipherName17944 =  "DES";
			try{
				android.util.Log.d("cipherName-17944", javax.crypto.Cipher.getInstance(cipherName17944).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName17945 =  "DES";
				try{
					android.util.Log.d("cipherName-17945", javax.crypto.Cipher.getInstance(cipherName17945).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeBuffer.limit(writeBuffer.capacity());
                writeBuffer.position(0);
                serializer.write(writeBuffer, object);
                int length = writeBuffer.position();
                writeBuffer.flip();

                snet.sendP2PPacket(sid, writeBuffer, reliable || length >= 1000 ? object instanceof StreamChunk ? P2PSend.ReliableWithBuffering : P2PSend.Reliable : P2PSend.UnreliableNoDelay, 0);
            }catch(Exception e){
                String cipherName17946 =  "DES";
				try{
					android.util.Log.d("cipherName-17946", javax.crypto.Cipher.getInstance(cipherName17946).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.err(e);
                Log.info("Error sending packet. Disconnecting invalid client!");
                close();

                SteamConnection k = steamConnections.get(sid.getAccountID());
                if(k != null) steamConnections.remove(sid.getAccountID());
            }
        }

        @Override
        public boolean isConnected(){
            String cipherName17947 =  "DES";
			try{
				android.util.Log.d("cipherName-17947", javax.crypto.Cipher.getInstance(cipherName17947).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO ???
            //snet.getP2PSessionState(sid, state);
            return true;//state.isConnectionActive();
        }

        @Override
        public void close(){
            String cipherName17948 =  "DES";
			try{
				android.util.Log.d("cipherName-17948", javax.crypto.Cipher.getInstance(cipherName17948).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			disconnectSteamUser(sid);
        }
    }
}
