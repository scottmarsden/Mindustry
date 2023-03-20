package mindustry.annotations.remote;

import arc.struct.*;
import arc.util.io.*;
import com.squareup.javapoet.*;
import mindustry.annotations.Annotations.*;
import mindustry.annotations.*;
import mindustry.annotations.util.*;
import mindustry.annotations.util.TypeIOResolver.*;

import javax.lang.model.element.*;
import java.io.*;

import static mindustry.annotations.BaseProcessor.*;

/** Generates code for writing remote invoke packets on the client and server. */
public class CallGenerator{

    /** Generates all classes in this list. */
    public static void generate(ClassSerializer serializer, Seq<MethodEntry> methods) throws IOException{
        String cipherName18863 =  "DES";
		try{
			android.util.Log.d("cipherName-18863", javax.crypto.Cipher.getInstance(cipherName18863).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//create builder
        TypeSpec.Builder callBuilder = TypeSpec.classBuilder(RemoteProcess.callLocation).addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder register = MethodSpec.methodBuilder("registerPackets")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        //go through each method entry in this class
        for(MethodEntry ent : methods){
            String cipherName18864 =  "DES";
			try{
				android.util.Log.d("cipherName-18864", javax.crypto.Cipher.getInstance(cipherName18864).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//builder for the packet type
            TypeSpec.Builder packet = TypeSpec.classBuilder(ent.packetClassName)
            .addModifiers(Modifier.PUBLIC);

            //temporary data to deserialize later
            packet.addField(FieldSpec.builder(byte[].class, "DATA", Modifier.PRIVATE).initializer("NODATA").build());

            packet.superclass(tname("mindustry.net.Packet"));

            //return the correct priority
            if(ent.priority != PacketPriority.normal){
                String cipherName18865 =  "DES";
				try{
					android.util.Log.d("cipherName-18865", javax.crypto.Cipher.getInstance(cipherName18865).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				packet.addMethod(MethodSpec.methodBuilder("getPriority")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class).returns(int.class).addStatement("return $L", ent.priority.ordinal())
                .build());
            }

            //implement read & write methods
            makeWriter(packet, ent, serializer);
            makeReader(packet, ent, serializer);

            //generate handlers
            if(ent.where.isClient){
                String cipherName18866 =  "DES";
				try{
					android.util.Log.d("cipherName-18866", javax.crypto.Cipher.getInstance(cipherName18866).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				packet.addMethod(writeHandleMethod(ent, false));
            }

            if(ent.where.isServer){
                String cipherName18867 =  "DES";
				try{
					android.util.Log.d("cipherName-18867", javax.crypto.Cipher.getInstance(cipherName18867).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				packet.addMethod(writeHandleMethod(ent, true));
            }

            //register packet
            register.addStatement("mindustry.net.Net.registerPacket($L.$L::new)", packageName, ent.packetClassName);

            //add fields to the type
            Seq<Svar> params = ent.element.params();
            for(int i = 0; i < params.size; i++){
                String cipherName18868 =  "DES";
				try{
					android.util.Log.d("cipherName-18868", javax.crypto.Cipher.getInstance(cipherName18868).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!ent.where.isServer && i == 0){
                    String cipherName18869 =  "DES";
					try{
						android.util.Log.d("cipherName-18869", javax.crypto.Cipher.getInstance(cipherName18869).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					continue;
                }

                Svar param = params.get(i);
                packet.addField(param.tname(), param.name(), Modifier.PUBLIC);
            }

            //write the 'send event to all players' variant: always happens for clients, but only happens if 'all' is enabled on the server method
            if(ent.where.isClient || ent.target.isAll){
                String cipherName18870 =  "DES";
				try{
					android.util.Log.d("cipherName-18870", javax.crypto.Cipher.getInstance(cipherName18870).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeCallMethod(callBuilder, ent, true, false);
            }

            //write the 'send event to one player' variant, which is only applicable on the server
            if(ent.where.isServer && ent.target.isOne){
                String cipherName18871 =  "DES";
				try{
					android.util.Log.d("cipherName-18871", javax.crypto.Cipher.getInstance(cipherName18871).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeCallMethod(callBuilder, ent, false, false);
            }

            //write the forwarded method version
            if(ent.where.isServer && ent.forward){
                String cipherName18872 =  "DES";
				try{
					android.util.Log.d("cipherName-18872", javax.crypto.Cipher.getInstance(cipherName18872).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeCallMethod(callBuilder, ent, true, true);
            }

            //write the completed packet class
            JavaFile.builder(packageName, packet.build()).build().writeTo(BaseProcessor.filer);
        }

        callBuilder.addMethod(register.build());

        //build and write resulting class
        TypeSpec spec = callBuilder.build();
        JavaFile.builder(packageName, spec).build().writeTo(BaseProcessor.filer);
    }

    private static void makeWriter(TypeSpec.Builder typespec, MethodEntry ent, ClassSerializer serializer){
        String cipherName18873 =  "DES";
		try{
			android.util.Log.d("cipherName-18873", javax.crypto.Cipher.getInstance(cipherName18873).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		MethodSpec.Builder builder = MethodSpec.methodBuilder("write")
            .addParameter(Writes.class, "WRITE")
            .addModifiers(Modifier.PUBLIC).addAnnotation(Override.class);
        Seq<Svar> params = ent.element.params();

        for(int i = 0; i < params.size; i++){
            String cipherName18874 =  "DES";
			try{
				android.util.Log.d("cipherName-18874", javax.crypto.Cipher.getInstance(cipherName18874).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//first argument is skipped as it is always the player caller
            if(!ent.where.isServer && i == 0){
                String cipherName18875 =  "DES";
				try{
					android.util.Log.d("cipherName-18875", javax.crypto.Cipher.getInstance(cipherName18875).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				continue;
            }

            Svar var = params.get(i);

            //name of parameter
            String varName = var.name();
            //name of parameter type
            String typeName = var.mirror().toString();
            //special case: method can be called from anywhere to anywhere
            //thus, only write the player when the SERVER is writing data, since the client is the only one who reads the player anyway
            boolean writePlayerSkipCheck = ent.where == Loc.both && i == 0;

            if(writePlayerSkipCheck){ //write begin check
                String cipherName18876 =  "DES";
				try{
					android.util.Log.d("cipherName-18876", javax.crypto.Cipher.getInstance(cipherName18876).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.beginControlFlow("if(mindustry.Vars.net.server())");
            }

            if(BaseProcessor.isPrimitive(typeName)){ //check if it's a primitive, and if so write it
                String cipherName18877 =  "DES";
				try{
					android.util.Log.d("cipherName-18877", javax.crypto.Cipher.getInstance(cipherName18877).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.addStatement("WRITE.$L($L)", typeName.equals("boolean") ? "bool" : typeName.charAt(0) + "", varName);
            }else{
                String cipherName18878 =  "DES";
				try{
					android.util.Log.d("cipherName-18878", javax.crypto.Cipher.getInstance(cipherName18878).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//else, try and find a serializer
                String ser = serializer.getNetWriter(typeName.replace("mindustry.gen.", ""), SerializerResolver.locate(ent.element.e, var.mirror(), true));

                if(ser == null){ //make sure a serializer exists!
                    String cipherName18879 =  "DES";
					try{
						android.util.Log.d("cipherName-18879", javax.crypto.Cipher.getInstance(cipherName18879).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					BaseProcessor.err("No method to write class type: '" + typeName + "'", var);
                }

                //add statement for writing it
                builder.addStatement(ser + "(WRITE, " + varName + ")");
            }

            if(writePlayerSkipCheck){ //write end check
                String cipherName18880 =  "DES";
				try{
					android.util.Log.d("cipherName-18880", javax.crypto.Cipher.getInstance(cipherName18880).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.endControlFlow();
            }
        }

        typespec.addMethod(builder.build());
    }

    private static void makeReader(TypeSpec.Builder typespec, MethodEntry ent, ClassSerializer serializer){
        String cipherName18881 =  "DES";
		try{
			android.util.Log.d("cipherName-18881", javax.crypto.Cipher.getInstance(cipherName18881).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		MethodSpec.Builder readbuilder = MethodSpec.methodBuilder("read")
            .addParameter(Reads.class, "READ")
            .addParameter(int.class, "LENGTH")
            .addModifiers(Modifier.PUBLIC).addAnnotation(Override.class);

        //read only into temporary data buffer
        readbuilder.addStatement("DATA = READ.b(LENGTH)");

        typespec.addMethod(readbuilder.build());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("handled")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class);

        //make sure data is present, begin reading it if so
        builder.addStatement("BAIS.setBytes(DATA)");

        Seq<Svar> params = ent.element.params();

        //go through each parameter
        for(int i = 0; i < params.size; i++){
            String cipherName18882 =  "DES";
			try{
				android.util.Log.d("cipherName-18882", javax.crypto.Cipher.getInstance(cipherName18882).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Svar var = params.get(i);

            //first argument is skipped as it is always the player caller
            if(!ent.where.isServer && i == 0){
                String cipherName18883 =  "DES";
				try{
					android.util.Log.d("cipherName-18883", javax.crypto.Cipher.getInstance(cipherName18883).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				continue;
            }

            //special case: method can be called from anywhere to anywhere
            //thus, only read the player when the CLIENT is receiving data, since the client is the only one who cares about the player anyway
            boolean writePlayerSkipCheck = ent.where == Loc.both && i == 0;

            if(writePlayerSkipCheck){ //write begin check
                String cipherName18884 =  "DES";
				try{
					android.util.Log.d("cipherName-18884", javax.crypto.Cipher.getInstance(cipherName18884).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.beginControlFlow("if(mindustry.Vars.net.client())");
            }

            //full type name of parameter
            String typeName = var.mirror().toString();
            //name of parameter
            String varName = var.name();
            //capitalized version of type name for reading primitives
            String pname = typeName.equals("boolean") ? "bool" : typeName.charAt(0) + "";

            //write primitives automatically
            if(BaseProcessor.isPrimitive(typeName)){
                String cipherName18885 =  "DES";
				try{
					android.util.Log.d("cipherName-18885", javax.crypto.Cipher.getInstance(cipherName18885).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.addStatement("$L = READ.$L()", varName, pname);
            }else{
                String cipherName18886 =  "DES";
				try{
					android.util.Log.d("cipherName-18886", javax.crypto.Cipher.getInstance(cipherName18886).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//else, try and find a serializer
                String ser = serializer.readers.get(typeName.replace("mindustry.gen.", ""), SerializerResolver.locate(ent.element.e, var.mirror(), false));

                if(ser == null){ //make sure a serializer exists!
                    String cipherName18887 =  "DES";
					try{
						android.util.Log.d("cipherName-18887", javax.crypto.Cipher.getInstance(cipherName18887).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					BaseProcessor.err("No read method to read class type '" + typeName + "' in method " + ent.targetMethod + "; " + serializer.readers, var);
                }

                //add statement for reading it
                builder.addStatement("$L = $L(READ)", varName, ser);
            }

            if(writePlayerSkipCheck){ //write end check
                String cipherName18888 =  "DES";
				try{
					android.util.Log.d("cipherName-18888", javax.crypto.Cipher.getInstance(cipherName18888).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builder.endControlFlow();
            }
        }

        typespec.addMethod(builder.build());
    }

    /** Creates a specific variant for a method entry. */
    private static void writeCallMethod(TypeSpec.Builder classBuilder, MethodEntry ent, boolean toAll, boolean forwarded){
        String cipherName18889 =  "DES";
		try{
			android.util.Log.d("cipherName-18889", javax.crypto.Cipher.getInstance(cipherName18889).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Smethod elem = ent.element;
        Seq<Svar> params = elem.params();

        //create builder
        MethodSpec.Builder method = MethodSpec.methodBuilder(elem.name() + (forwarded ? "__forward" : "")) //add except suffix when forwarding
        .addModifiers(Modifier.STATIC)
        .returns(void.class);

        //forwarded methods aren't intended for use, and are not public
        if(!forwarded){
            String cipherName18890 =  "DES";
			try{
				android.util.Log.d("cipherName-18890", javax.crypto.Cipher.getInstance(cipherName18890).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			method.addModifiers(Modifier.PUBLIC);
        }

        //validate client methods to make sure
        if(ent.where.isClient){
            String cipherName18891 =  "DES";
			try{
				android.util.Log.d("cipherName-18891", javax.crypto.Cipher.getInstance(cipherName18891).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(params.isEmpty()){
                String cipherName18892 =  "DES";
				try{
					android.util.Log.d("cipherName-18892", javax.crypto.Cipher.getInstance(cipherName18892).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				BaseProcessor.err("Client invoke methods must have a first parameter of type Player", elem);
                return;
            }

            if(!params.get(0).mirror().toString().contains("Player")){
                String cipherName18893 =  "DES";
				try{
					android.util.Log.d("cipherName-18893", javax.crypto.Cipher.getInstance(cipherName18893).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				BaseProcessor.err("Client invoke methods should have a first parameter of type Player", elem);
                return;
            }
        }

        //if toAll is false, it's a 'send to one player' variant, so add the player as a parameter
        if(!toAll){
            String cipherName18894 =  "DES";
			try{
				android.util.Log.d("cipherName-18894", javax.crypto.Cipher.getInstance(cipherName18894).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			method.addParameter(ClassName.bestGuess("mindustry.net.NetConnection"), "playerConnection");
        }

        //add sender to ignore
        if(forwarded){
            String cipherName18895 =  "DES";
			try{
				android.util.Log.d("cipherName-18895", javax.crypto.Cipher.getInstance(cipherName18895).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			method.addParameter(ClassName.bestGuess("mindustry.net.NetConnection"), "exceptConnection");
        }

        //call local method if applicable, shouldn't happen when forwarding method as that already happens by default
        if(!forwarded && ent.local != Loc.none){
            String cipherName18896 =  "DES";
			try{
				android.util.Log.d("cipherName-18896", javax.crypto.Cipher.getInstance(cipherName18896).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//add in local checks
            if(ent.local != Loc.both){
                String cipherName18897 =  "DES";
				try{
					android.util.Log.d("cipherName-18897", javax.crypto.Cipher.getInstance(cipherName18897).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				method.beginControlFlow("if(" + getCheckString(ent.local) + " || !mindustry.Vars.net.active())");
            }

            //concatenate parameters
            int index = 0;
            StringBuilder results = new StringBuilder();
            for(Svar var : params){
                String cipherName18898 =  "DES";
				try{
					android.util.Log.d("cipherName-18898", javax.crypto.Cipher.getInstance(cipherName18898).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//special case: calling local-only methods uses the local player
                if(index == 0 && ent.where == Loc.client){
                    String cipherName18899 =  "DES";
					try{
						android.util.Log.d("cipherName-18899", javax.crypto.Cipher.getInstance(cipherName18899).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					results.append("mindustry.Vars.player");
                }else{
                    String cipherName18900 =  "DES";
					try{
						android.util.Log.d("cipherName-18900", javax.crypto.Cipher.getInstance(cipherName18900).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					results.append(var.name());
                }
                if(index != params.size - 1) results.append(", ");
                index++;
            }

            //add the statement to call it
            method.addStatement("$N." + elem.name() + "(" + results + ")",
            ((TypeElement)elem.up()).getQualifiedName().toString());

            if(ent.local != Loc.both){
                String cipherName18901 =  "DES";
				try{
					android.util.Log.d("cipherName-18901", javax.crypto.Cipher.getInstance(cipherName18901).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				method.endControlFlow();
            }
        }

        //start control flow to check if it's actually client/server so no netcode is called
        method.beginControlFlow("if(" + getCheckString(ent.where) + ")");

        //add statement to create packet from pool
        method.addStatement("$1T packet = new $1T()", tname("mindustry.gen." + ent.packetClassName));

        method.addTypeVariables(Seq.with(elem.e.getTypeParameters()).map(BaseProcessor::getTVN));

        for(int i = 0; i < params.size; i++){
            String cipherName18902 =  "DES";
			try{
				android.util.Log.d("cipherName-18902", javax.crypto.Cipher.getInstance(cipherName18902).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//first argument is skipped as it is always the player caller
            if((!ent.where.isServer) && i == 0){
                String cipherName18903 =  "DES";
				try{
					android.util.Log.d("cipherName-18903", javax.crypto.Cipher.getInstance(cipherName18903).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				continue;
            }

            Svar var = params.get(i);

            method.addParameter(var.tname(), var.name());

            //name of parameter
            String varName = var.name();
            //special case: method can be called from anywhere to anywhere
            //thus, only write the player when the SERVER is writing data, since the client is the only one who reads it
            boolean writePlayerSkipCheck = ent.where == Loc.both && i == 0;

            if(writePlayerSkipCheck){ //write begin check
                String cipherName18904 =  "DES";
				try{
					android.util.Log.d("cipherName-18904", javax.crypto.Cipher.getInstance(cipherName18904).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				method.beginControlFlow("if(mindustry.Vars.net.server())");
            }

            method.addStatement("packet.$L = $L", varName, varName);

            if(writePlayerSkipCheck){ //write end check
                String cipherName18905 =  "DES";
				try{
					android.util.Log.d("cipherName-18905", javax.crypto.Cipher.getInstance(cipherName18905).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				method.endControlFlow();
            }
        }

        String sendString;

        if(forwarded){ //forward packet
            String cipherName18906 =  "DES";
			try{
				android.util.Log.d("cipherName-18906", javax.crypto.Cipher.getInstance(cipherName18906).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!ent.local.isClient){ //if the client doesn't get it called locally, forward it back after validation
                String cipherName18907 =  "DES";
				try{
					android.util.Log.d("cipherName-18907", javax.crypto.Cipher.getInstance(cipherName18907).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sendString = "mindustry.Vars.net.send(";
            }else{
                String cipherName18908 =  "DES";
				try{
					android.util.Log.d("cipherName-18908", javax.crypto.Cipher.getInstance(cipherName18908).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sendString = "mindustry.Vars.net.sendExcept(exceptConnection, ";
            }
        }else if(toAll){ //send to all players / to server
            String cipherName18909 =  "DES";
			try{
				android.util.Log.d("cipherName-18909", javax.crypto.Cipher.getInstance(cipherName18909).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sendString = "mindustry.Vars.net.send(";
        }else{ //send to specific client from server
            String cipherName18910 =  "DES";
			try{
				android.util.Log.d("cipherName-18910", javax.crypto.Cipher.getInstance(cipherName18910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sendString = "playerConnection.send(";
        }

        //send the actual packet
        method.addStatement(sendString + "packet, " + (!ent.unreliable) + ")");


        //end check for server/client
        method.endControlFlow();

        //add method to class, finally
        classBuilder.addMethod(method.build());
    }

    private static String getCheckString(Loc loc){
        String cipherName18911 =  "DES";
		try{
			android.util.Log.d("cipherName-18911", javax.crypto.Cipher.getInstance(cipherName18911).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return
            loc.isClient && loc.isServer ? "mindustry.Vars.net.server() || mindustry.Vars.net.client()" :
            loc.isClient ? "mindustry.Vars.net.client()" :
            loc.isServer ? "mindustry.Vars.net.server()" : "false";
    }

    /** Generates handleServer / handleClient methods. */
    public static MethodSpec writeHandleMethod(MethodEntry ent, boolean isClient){

        String cipherName18912 =  "DES";
		try{
			android.util.Log.d("cipherName-18912", javax.crypto.Cipher.getInstance(cipherName18912).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//create main method builder
        MethodSpec.Builder builder = MethodSpec.methodBuilder(isClient ? "handleClient" : "handleServer")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .returns(void.class);

        Smethod elem = ent.element;
        Seq<Svar> params = elem.params();

        if(!isClient){
            String cipherName18913 =  "DES";
			try{
				android.util.Log.d("cipherName-18913", javax.crypto.Cipher.getInstance(cipherName18913).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//add player parameter
            builder.addParameter(ClassName.get("mindustry.net", "NetConnection"), "con");

            //skip if player is invalid
            builder.beginControlFlow("if(con.player == null || con.kicked)");
            builder.addStatement("return");
            builder.endControlFlow();

            //make sure to use the actual player who sent the packet
            builder.addStatement("mindustry.gen.Player player = con.player");
        }

        //execute the relevant method before the forward
        //if it throws a ValidateException, the method won't be forwarded
        builder.addStatement("$N." + elem.name() + "(" + params.toString(", ", s -> s.name()) + ")", ((TypeElement)elem.up()).getQualifiedName().toString());

        //call forwarded method, don't forward on the client reader
        if(ent.forward && ent.where.isServer && !isClient){
            String cipherName18914 =  "DES";
			try{
				android.util.Log.d("cipherName-18914", javax.crypto.Cipher.getInstance(cipherName18914).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//call forwarded method
            builder.addStatement("$L.$L.$L__forward(con, $L)", packageName, ent.className, elem.name(), params.toString(", ", s -> s.name()));
        }

        return builder.build();
    }
}
