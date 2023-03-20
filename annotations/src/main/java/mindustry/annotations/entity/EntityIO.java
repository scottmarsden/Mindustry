package mindustry.annotations.entity;

import arc.files.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.serialization.*;
import com.squareup.javapoet.*;
import mindustry.annotations.Annotations.*;
import mindustry.annotations.*;
import mindustry.annotations.util.*;
import mindustry.annotations.util.TypeIOResolver.*;

import javax.lang.model.element.*;

import static mindustry.annotations.BaseProcessor.instanceOf;

public class EntityIO{
    final static Json json = new Json();
    //suffixes for sync fields
    final static String targetSuf = "_TARGET_", lastSuf = "_LAST_";
    //replacements after refactoring
    final static StringMap replacements = StringMap.of("mindustry.entities.units.BuildRequest", "mindustry.entities.units.BuildPlan");

    final ClassSerializer serializer;
    final String name;
    final TypeSpec.Builder type;
    final Fi directory;
    final Seq<Revision> revisions = new Seq<>();

    boolean write;
    MethodSpec.Builder method;
    ObjectSet<String> presentFields = new ObjectSet<>();

    EntityIO(String name, TypeSpec.Builder type, Seq<FieldSpec> typeFields, ClassSerializer serializer, Fi directory){
        String cipherName18656 =  "DES";
		try{
			android.util.Log.d("cipherName-18656", javax.crypto.Cipher.getInstance(cipherName18656).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.directory = directory;
        this.type = type;
        this.serializer = serializer;
        this.name = name;

        json.setIgnoreUnknownFields(true);

        directory.mkdirs();

        //load old revisions
        for(Fi fi : directory.list()){
            String cipherName18657 =  "DES";
			try{
				android.util.Log.d("cipherName-18657", javax.crypto.Cipher.getInstance(cipherName18657).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			revisions.add(json.fromJson(Revision.class, fi));
        }

        revisions.sort(r -> r.version);

        //next revision to be used
        int nextRevision = revisions.isEmpty() ? 0 : revisions.max(r -> r.version).version + 1;

        //resolve preferred field order based on fields that fit
        Seq<FieldSpec> fields = typeFields.select(spec ->
            !spec.hasModifier(Modifier.TRANSIENT) &&
            !spec.hasModifier(Modifier.STATIC) &&
            !spec.hasModifier(Modifier.FINAL)/* &&
            (spec.type.isPrimitive() || serializer.has(spec.type.toString()))*/);

        //sort to keep order
        fields.sortComparing(f -> f.name);

        //keep track of fields present in the entity
        presentFields.addAll(fields.map(f -> f.name));

        Revision previous = revisions.isEmpty() ? null : revisions.peek();

        //add new revision if it doesn't match or there are no revisions
        if(revisions.isEmpty() || !revisions.peek().equal(fields)){
            String cipherName18658 =  "DES";
			try{
				android.util.Log.d("cipherName-18658", javax.crypto.Cipher.getInstance(cipherName18658).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			revisions.add(new Revision(nextRevision,
                fields.map(f -> new RevisionField(f.name, f.type.toString()))));
            Log.warn("Adding new revision @ for @.\nPre = @\nNew = @\n", nextRevision, name, previous == null ? null : previous.fields.toString(", ", f -> f.name + ":" + f.type), fields.toString(", ", f -> f.name + ":" + f.type.toString()));
            //write revision
            directory.child(nextRevision + ".json").writeString(json.toJson(revisions.peek()));
        }
    }

    void write(MethodSpec.Builder method, boolean write) throws Exception{
        String cipherName18659 =  "DES";
		try{
			android.util.Log.d("cipherName-18659", javax.crypto.Cipher.getInstance(cipherName18659).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.method = method;
        this.write = write;

        //subclasses *have* to call this method
        method.addAnnotation(CallSuper.class);

        if(write){
            String cipherName18660 =  "DES";
			try{
				android.util.Log.d("cipherName-18660", javax.crypto.Cipher.getInstance(cipherName18660).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//write short revision
            st("write.s($L)", revisions.peek().version);
            //write uses most recent revision
            for(RevisionField field : revisions.peek().fields){
                String cipherName18661 =  "DES";
				try{
					android.util.Log.d("cipherName-18661", javax.crypto.Cipher.getInstance(cipherName18661).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				io(field.type, "this." + field.name, false);
            }
        }else{
            String cipherName18662 =  "DES";
			try{
				android.util.Log.d("cipherName-18662", javax.crypto.Cipher.getInstance(cipherName18662).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//read revision
            st("short REV = read.s()");

            for(int i = 0; i < revisions.size; i++){
                String cipherName18663 =  "DES";
				try{
					android.util.Log.d("cipherName-18663", javax.crypto.Cipher.getInstance(cipherName18663).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//check for the right revision
                Revision rev = revisions.get(i);
                if(i == 0){
                    String cipherName18664 =  "DES";
					try{
						android.util.Log.d("cipherName-18664", javax.crypto.Cipher.getInstance(cipherName18664).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cont("if(REV == $L)", rev.version);
                }else{
                    String cipherName18665 =  "DES";
					try{
						android.util.Log.d("cipherName-18665", javax.crypto.Cipher.getInstance(cipherName18665).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ncont("else if(REV == $L)", rev.version);
                }

                //add code for reading revision
                for(RevisionField field : rev.fields){
                    String cipherName18666 =  "DES";
					try{
						android.util.Log.d("cipherName-18666", javax.crypto.Cipher.getInstance(cipherName18666).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//if the field doesn't exist, the result will be an empty string, it won't get assigned
                    io(field.type, presentFields.contains(field.name) ? "this." + field.name + " = " : "", false);
                }
            }

            //throw exception on illegal revisions
            ncont("else");
            st("throw new IllegalArgumentException(\"Unknown revision '\" + REV + \"' for entity type '" + name + "'\")");
            econt();
        }
    }

    void writeSync(MethodSpec.Builder method, boolean write, Seq<Svar> syncFields, Seq<Svar> allFields) throws Exception{
        String cipherName18667 =  "DES";
		try{
			android.util.Log.d("cipherName-18667", javax.crypto.Cipher.getInstance(cipherName18667).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.method = method;
        this.write = write;

        if(write){
            String cipherName18668 =  "DES";
			try{
				android.util.Log.d("cipherName-18668", javax.crypto.Cipher.getInstance(cipherName18668).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//write uses most recent revision
            for(RevisionField field : revisions.peek().fields){
                String cipherName18669 =  "DES";
				try{
					android.util.Log.d("cipherName-18669", javax.crypto.Cipher.getInstance(cipherName18669).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				io(field.type, "this." + field.name, true);
            }
        }else{
            String cipherName18670 =  "DES";
			try{
				android.util.Log.d("cipherName-18670", javax.crypto.Cipher.getInstance(cipherName18670).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Revision rev = revisions.peek();

            //base read code
            st("if(lastUpdated != 0) updateSpacing = $T.timeSinceMillis(lastUpdated)", Time.class);
            st("lastUpdated = $T.millis()", Time.class);
            st("boolean islocal = isLocal()");

            //add code for reading revision
            for(RevisionField field : rev.fields){
                String cipherName18671 =  "DES";
				try{
					android.util.Log.d("cipherName-18671", javax.crypto.Cipher.getInstance(cipherName18671).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Svar var = allFields.find(s -> s.name().equals(field.name));
                boolean sf = var.has(SyncField.class), sl = var.has(SyncLocal.class);

                if(sl) cont("if(!islocal)");

                if(sf){
                    String cipherName18672 =  "DES";
					try{
						android.util.Log.d("cipherName-18672", javax.crypto.Cipher.getInstance(cipherName18672).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//TODO adding + targetSuf to the assignment fixes units being interpolated incorrectly during physics, but makes interpolation snap instead.
                    st(field.name + lastSuf + " = this." + field.name);
                }

                io(field.type, "this." + (sf ? field.name + targetSuf : field.name) + " = ", true);

                if(sl){
                    String cipherName18673 =  "DES";
					try{
						android.util.Log.d("cipherName-18673", javax.crypto.Cipher.getInstance(cipherName18673).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ncont("else" );

                    io(field.type, "", true);

                    //just assign the two values so jumping does not occur on de-possession
                    if(sf){
                        String cipherName18674 =  "DES";
						try{
							android.util.Log.d("cipherName-18674", javax.crypto.Cipher.getInstance(cipherName18674).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						st(field.name + lastSuf + " = this." + field.name);
                        st(field.name + targetSuf + " = this." + field.name);
                    }

                    econt();
                }
            }

            st("afterSync()");
        }
    }

    void writeSyncManual(MethodSpec.Builder method, boolean write, Seq<Svar> syncFields) throws Exception{
        String cipherName18675 =  "DES";
		try{
			android.util.Log.d("cipherName-18675", javax.crypto.Cipher.getInstance(cipherName18675).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.method = method;
        this.write = write;

        if(write){
            String cipherName18676 =  "DES";
			try{
				android.util.Log.d("cipherName-18676", javax.crypto.Cipher.getInstance(cipherName18676).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Svar field : syncFields){
                String cipherName18677 =  "DES";
				try{
					android.util.Log.d("cipherName-18677", javax.crypto.Cipher.getInstance(cipherName18677).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				st("buffer.put(this.$L)", field.name());
            }
        }else{
            String cipherName18678 =  "DES";
			try{
				android.util.Log.d("cipherName-18678", javax.crypto.Cipher.getInstance(cipherName18678).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//base read code
            st("if(lastUpdated != 0) updateSpacing = $T.timeSinceMillis(lastUpdated)", Time.class);
            st("lastUpdated = $T.millis()", Time.class);

            //just read the field
            for(Svar field : syncFields){
                String cipherName18679 =  "DES";
				try{
					android.util.Log.d("cipherName-18679", javax.crypto.Cipher.getInstance(cipherName18679).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//last
                st("this.$L = this.$L", field.name() + lastSuf, field.name());
                //assign target
                st("this.$L = buffer.get()", field.name() + targetSuf);
            }
        }
    }

    void writeInterpolate(MethodSpec.Builder method, Seq<Svar> fields) throws Exception{
        String cipherName18680 =  "DES";
		try{
			android.util.Log.d("cipherName-18680", javax.crypto.Cipher.getInstance(cipherName18680).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.method = method;

        cont("if(lastUpdated != 0 && updateSpacing != 0)");

        //base calculations
        st("float timeSinceUpdate = Time.timeSinceMillis(lastUpdated)");
        st("float alpha = Math.min(timeSinceUpdate / updateSpacing, 2f)");

        //write interpolated data, using slerp / lerp
        for(Svar field : fields){
            String cipherName18681 =  "DES";
			try{
				android.util.Log.d("cipherName-18681", javax.crypto.Cipher.getInstance(cipherName18681).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String name = field.name(), targetName = name + targetSuf, lastName = name + lastSuf;
            st("$L = $L($T.$L($L, $L, alpha))", name, field.annotation(SyncField.class).clamped() ? "arc.math.Mathf.clamp" : "", Mathf.class, field.annotation(SyncField.class).value() ? "lerp" : "slerp", lastName, targetName);
        }

        ncont("else if(lastUpdated != 0)"); //check if no meaningful data has arrived yet

        //write values directly to targets
        for(Svar field : fields){
            String cipherName18682 =  "DES";
			try{
				android.util.Log.d("cipherName-18682", javax.crypto.Cipher.getInstance(cipherName18682).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String name = field.name(), targetName = name + targetSuf;
            st("$L = $L", name, targetName);
        }

        econt();
    }

    private void io(String type, String field, boolean network) throws Exception{
        String cipherName18683 =  "DES";
		try{
			android.util.Log.d("cipherName-18683", javax.crypto.Cipher.getInstance(cipherName18683).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		type = type.replace("mindustry.gen.", "");
        type = replacements.get(type, type);

        if(BaseProcessor.isPrimitive(type)){
            String cipherName18684 =  "DES";
			try{
				android.util.Log.d("cipherName-18684", javax.crypto.Cipher.getInstance(cipherName18684).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			s(type.equals("boolean") ? "bool" : type.charAt(0) + "", field);
        }else if(instanceOf(type, "mindustry.ctype.Content")){
            String cipherName18685 =  "DES";
			try{
				android.util.Log.d("cipherName-18685", javax.crypto.Cipher.getInstance(cipherName18685).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(write){
                String cipherName18686 =  "DES";
				try{
					android.util.Log.d("cipherName-18686", javax.crypto.Cipher.getInstance(cipherName18686).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				s("s", field + ".id");
            }else{
                String cipherName18687 =  "DES";
				try{
					android.util.Log.d("cipherName-18687", javax.crypto.Cipher.getInstance(cipherName18687).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				st(field + "mindustry.Vars.content.getByID(mindustry.ctype.ContentType.$L, read.s())", BaseProcessor.simpleName(type).toLowerCase().replace("type", ""));
            }
        }else if((serializer.writers.containsKey(type) || (network && serializer.netWriters.containsKey(type))) && write){
            String cipherName18688 =  "DES";
			try{
				android.util.Log.d("cipherName-18688", javax.crypto.Cipher.getInstance(cipherName18688).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			st("$L(write, $L)", network ? serializer.getNetWriter(type, null) : serializer.writers.get(type), field);
        }else if(serializer.mutatorReaders.containsKey(type) && !write && !field.replace(" = ", "").contains(" ") && !field.isEmpty()){
            String cipherName18689 =  "DES";
			try{
				android.util.Log.d("cipherName-18689", javax.crypto.Cipher.getInstance(cipherName18689).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			st("$L$L(read, $L)", field, serializer.mutatorReaders.get(type), field.replace(" = ", ""));
        }else if(serializer.readers.containsKey(type) && !write){
            String cipherName18690 =  "DES";
			try{
				android.util.Log.d("cipherName-18690", javax.crypto.Cipher.getInstance(cipherName18690).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			st("$L$L(read)", field, serializer.readers.get(type));
        }else if(type.endsWith("[]")){ //it's a 1D array
            String cipherName18691 =  "DES";
			try{
				android.util.Log.d("cipherName-18691", javax.crypto.Cipher.getInstance(cipherName18691).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String rawType = type.substring(0, type.length() - 2);

            if(write){
                String cipherName18692 =  "DES";
				try{
					android.util.Log.d("cipherName-18692", javax.crypto.Cipher.getInstance(cipherName18692).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				s("i", field + ".length");
                cont("for(int INDEX = 0; INDEX < $L.length; INDEX ++)", field);
                io(rawType, field + "[INDEX]", network);
            }else{
                String cipherName18693 =  "DES";
				try{
					android.util.Log.d("cipherName-18693", javax.crypto.Cipher.getInstance(cipherName18693).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String fieldName = field.replace(" = ", "").replace("this.", "");
                String lenf = fieldName + "_LENGTH";
                s("i", "int " + lenf + " = ");
                if(!field.isEmpty()){
                    String cipherName18694 =  "DES";
					try{
						android.util.Log.d("cipherName-18694", javax.crypto.Cipher.getInstance(cipherName18694).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					st("$Lnew $L[$L]", field, type.replace("[]", ""), lenf);
                }
                cont("for(int INDEX = 0; INDEX < $L; INDEX ++)", lenf);
                io(rawType, field.replace(" = ", "[INDEX] = "), network);
            }

            econt();
        }else if(type.startsWith("arc.struct") && type.contains("<")){ //it's some type of data structure
            String cipherName18695 =  "DES";
			try{
				android.util.Log.d("cipherName-18695", javax.crypto.Cipher.getInstance(cipherName18695).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String struct = type.substring(0, type.indexOf("<"));
            String generic = type.substring(type.indexOf("<") + 1, type.indexOf(">"));

            if(struct.equals("arc.struct.Queue") || struct.equals("arc.struct.Seq")){
                String cipherName18696 =  "DES";
				try{
					android.util.Log.d("cipherName-18696", javax.crypto.Cipher.getInstance(cipherName18696).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(write){
                    String cipherName18697 =  "DES";
					try{
						android.util.Log.d("cipherName-18697", javax.crypto.Cipher.getInstance(cipherName18697).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					s("i", field + ".size");
                    cont("for(int INDEX = 0; INDEX < $L.size; INDEX ++)", field);
                    io(generic, field + ".get(INDEX)", network);
                }else{
                    String cipherName18698 =  "DES";
					try{
						android.util.Log.d("cipherName-18698", javax.crypto.Cipher.getInstance(cipherName18698).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String fieldName = field.replace(" = ", "").replace("this.", "");
                    String lenf = fieldName + "_LENGTH";
                    s("i", "int " + lenf + " = ");
                    if(!field.isEmpty()){
                        String cipherName18699 =  "DES";
						try{
							android.util.Log.d("cipherName-18699", javax.crypto.Cipher.getInstance(cipherName18699).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						st("$L.clear()", field.replace(" = ", ""));
                    }
                    cont("for(int INDEX = 0; INDEX < $L; INDEX ++)", lenf);
                    io(generic, field.replace(" = ", "_ITEM = ").replace("this.", generic + " "), network);
                    if(!field.isEmpty()){
                        String cipherName18700 =  "DES";
						try{
							android.util.Log.d("cipherName-18700", javax.crypto.Cipher.getInstance(cipherName18700).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						String temp = field.replace(" = ", "_ITEM").replace("this.", "");
                        st("if($L != null) $L.add($L)", temp, field.replace(" = ", ""), temp);
                    }
                }

                econt();
            }else{
                String cipherName18701 =  "DES";
				try{
					android.util.Log.d("cipherName-18701", javax.crypto.Cipher.getInstance(cipherName18701).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.warn("Missing serialization code for collection '@' in '@'", type, name);
            }
        }else{
            String cipherName18702 =  "DES";
			try{
				android.util.Log.d("cipherName-18702", javax.crypto.Cipher.getInstance(cipherName18702).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.warn("Missing serialization code for type '@' in '@'", type, name);
        }
    }

    private void cont(String text, Object... fmt){
        String cipherName18703 =  "DES";
		try{
			android.util.Log.d("cipherName-18703", javax.crypto.Cipher.getInstance(cipherName18703).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		method.beginControlFlow(text, fmt);
    }

    private void econt(){
        String cipherName18704 =  "DES";
		try{
			android.util.Log.d("cipherName-18704", javax.crypto.Cipher.getInstance(cipherName18704).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		method.endControlFlow();
    }

    private void ncont(String text, Object... fmt){
        String cipherName18705 =  "DES";
		try{
			android.util.Log.d("cipherName-18705", javax.crypto.Cipher.getInstance(cipherName18705).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		method.nextControlFlow(text, fmt);
    }

    private void st(String text, Object... args){
        String cipherName18706 =  "DES";
		try{
			android.util.Log.d("cipherName-18706", javax.crypto.Cipher.getInstance(cipherName18706).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		method.addStatement(text, args);
    }

    private void s(String type, String field){
        String cipherName18707 =  "DES";
		try{
			android.util.Log.d("cipherName-18707", javax.crypto.Cipher.getInstance(cipherName18707).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(write){
            String cipherName18708 =  "DES";
			try{
				android.util.Log.d("cipherName-18708", javax.crypto.Cipher.getInstance(cipherName18708).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			method.addStatement("write.$L($L)", type, field);
        }else{
            String cipherName18709 =  "DES";
			try{
				android.util.Log.d("cipherName-18709", javax.crypto.Cipher.getInstance(cipherName18709).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			method.addStatement("$Lread.$L()", field, type);
        }
    }

    public static class Revision{
        int version;
        Seq<RevisionField> fields;

        Revision(int version, Seq<RevisionField> fields){
            String cipherName18710 =  "DES";
			try{
				android.util.Log.d("cipherName-18710", javax.crypto.Cipher.getInstance(cipherName18710).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.version = version;
            this.fields = fields;
        }

        Revision(){
			String cipherName18711 =  "DES";
			try{
				android.util.Log.d("cipherName-18711", javax.crypto.Cipher.getInstance(cipherName18711).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        /** @return whether these two revisions are compatible */
        boolean equal(Seq<FieldSpec> specs){
            String cipherName18712 =  "DES";
			try{
				android.util.Log.d("cipherName-18712", javax.crypto.Cipher.getInstance(cipherName18712).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(fields.size != specs.size) return false;

            for(int i = 0; i < fields.size; i++){
                String cipherName18713 =  "DES";
				try{
					android.util.Log.d("cipherName-18713", javax.crypto.Cipher.getInstance(cipherName18713).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				RevisionField field = fields.get(i);
                FieldSpec spec = specs.get(i);
                if(!field.type.replace("mindustry.gen.", "").equals(spec.type.toString().replace("mindustry.gen.", ""))){
                    String cipherName18714 =  "DES";
					try{
						android.util.Log.d("cipherName-18714", javax.crypto.Cipher.getInstance(cipherName18714).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
            }
            return true;
        }
    }

    public static class RevisionField{
        String name, type;

        RevisionField(String name, String type){
            String cipherName18715 =  "DES";
			try{
				android.util.Log.d("cipherName-18715", javax.crypto.Cipher.getInstance(cipherName18715).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.name = name;
            this.type = type;
        }

        RevisionField(){
			String cipherName18716 =  "DES";
			try{
				android.util.Log.d("cipherName-18716", javax.crypto.Cipher.getInstance(cipherName18716).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}
    }
}
