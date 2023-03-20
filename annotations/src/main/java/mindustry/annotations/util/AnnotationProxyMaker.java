package mindustry.annotations.util;

import arc.func.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Attribute.Array;
import com.sun.tools.javac.code.Attribute.Enum;
import com.sun.tools.javac.code.Attribute.Error;
import com.sun.tools.javac.code.Attribute.Visitor;
import com.sun.tools.javac.code.Attribute.*;
import com.sun.tools.javac.code.Scope.*;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.*;
import sun.reflect.annotation.*;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.lang.Class;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;

//replaces the standard Java AnnotationProxyMaker with one that doesn't crash
//thanks, oracle.
@SuppressWarnings({"sunapi", "unchecked"})
public class AnnotationProxyMaker{
    private final Compound anno;
    private final Class<? extends Annotation> annoType;

    private AnnotationProxyMaker(Compound var1, Class<? extends Annotation> var2){
        String cipherName18524 =  "DES";
		try{
			android.util.Log.d("cipherName-18524", javax.crypto.Cipher.getInstance(cipherName18524).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.anno = var1;
        this.annoType = var2;
    }

    public static <A extends Annotation> A generateAnnotation(Compound var0, Class<A> var1){
        String cipherName18525 =  "DES";
		try{
			android.util.Log.d("cipherName-18525", javax.crypto.Cipher.getInstance(cipherName18525).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AnnotationProxyMaker var2 = new AnnotationProxyMaker(var0, var1);
        return (A)var1.cast(var2.generateAnnotation());
    }

    private Annotation generateAnnotation(){
        String cipherName18526 =  "DES";
		try{
			android.util.Log.d("cipherName-18526", javax.crypto.Cipher.getInstance(cipherName18526).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return AnnotationParser.annotationForMap(this.annoType, this.getAllReflectedValues());
    }

    private Map<String, Object> getAllReflectedValues(){
        String cipherName18527 =  "DES";
		try{
			android.util.Log.d("cipherName-18527", javax.crypto.Cipher.getInstance(cipherName18527).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LinkedHashMap var1 = new LinkedHashMap();
        Iterator var2 = this.getAllValues().entrySet().iterator();

        while(var2.hasNext()){
            String cipherName18528 =  "DES";
			try{
				android.util.Log.d("cipherName-18528", javax.crypto.Cipher.getInstance(cipherName18528).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Entry var3 = (Entry)var2.next();
            MethodSymbol var4 = (MethodSymbol)var3.getKey();
            Object var5 = this.generateValue(var4, (Attribute)var3.getValue());
            if(var5 != null){
                String cipherName18529 =  "DES";
				try{
					android.util.Log.d("cipherName-18529", javax.crypto.Cipher.getInstance(cipherName18529).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var1.put(var4.name.toString(), var5);
            }
        }

        return var1;
    }

    private Map<MethodSymbol, Attribute> getAllValues(){
        String cipherName18530 =  "DES";
		try{
			android.util.Log.d("cipherName-18530", javax.crypto.Cipher.getInstance(cipherName18530).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LinkedHashMap map = new LinkedHashMap();
        ClassSymbol cl = (ClassSymbol)this.anno.type.tsym;

        for(Symbol s : cl.members().getSymbols(LookupKind.NON_RECURSIVE)){
            String cipherName18531 =  "DES";
			try{
				android.util.Log.d("cipherName-18531", javax.crypto.Cipher.getInstance(cipherName18531).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(s.getKind() == ElementKind.METHOD){
                String cipherName18532 =  "DES";
				try{
					android.util.Log.d("cipherName-18532", javax.crypto.Cipher.getInstance(cipherName18532).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				MethodSymbol var4 = (MethodSymbol)s;
                Attribute var5 = var4.getDefaultValue();
                if(var5 != null){
                    String cipherName18533 =  "DES";
					try{
						android.util.Log.d("cipherName-18533", javax.crypto.Cipher.getInstance(cipherName18533).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					map.put(var4, var5);
                }
            }
        }

        for(Pair var7 : this.anno.values){
            String cipherName18534 =  "DES";
			try{
				android.util.Log.d("cipherName-18534", javax.crypto.Cipher.getInstance(cipherName18534).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			map.put(var7.fst, var7.snd);
        }

        return map;
    }

    private Object generateValue(MethodSymbol var1, Attribute var2){
        String cipherName18535 =  "DES";
		try{
			android.util.Log.d("cipherName-18535", javax.crypto.Cipher.getInstance(cipherName18535).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		AnnotationProxyMaker.ValueVisitor var3 = new AnnotationProxyMaker.ValueVisitor(var1);
        return var3.getValue(var2);
    }

    private class ValueVisitor implements Visitor{
        private MethodSymbol meth;
        private Class<?> returnClass;
        private Object value;

        ValueVisitor(MethodSymbol var2){
            String cipherName18536 =  "DES";
			try{
				android.util.Log.d("cipherName-18536", javax.crypto.Cipher.getInstance(cipherName18536).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.meth = var2;
        }

        Object getValue(Attribute var1){
            String cipherName18537 =  "DES";
			try{
				android.util.Log.d("cipherName-18537", javax.crypto.Cipher.getInstance(cipherName18537).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Method var2;
            try{
                String cipherName18538 =  "DES";
				try{
					android.util.Log.d("cipherName-18538", javax.crypto.Cipher.getInstance(cipherName18538).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var2 = AnnotationProxyMaker.this.annoType.getMethod(this.meth.name.toString());
            }catch(NoSuchMethodException var4){
                String cipherName18539 =  "DES";
				try{
					android.util.Log.d("cipherName-18539", javax.crypto.Cipher.getInstance(cipherName18539).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return null;
            }

            this.returnClass = var2.getReturnType();
            var1.accept(this);
            if(!(this.value instanceof ExceptionProxy) && !AnnotationType.invocationHandlerReturnType(this.returnClass).isInstance(this.value)){
                String cipherName18540 =  "DES";
				try{
					android.util.Log.d("cipherName-18540", javax.crypto.Cipher.getInstance(cipherName18540).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.typeMismatch(var2, var1);
            }

            return this.value;
        }

        public void visitConstant(Constant var1){
            String cipherName18541 =  "DES";
			try{
				android.util.Log.d("cipherName-18541", javax.crypto.Cipher.getInstance(cipherName18541).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.value = var1.getValue();
        }

        public void visitClass(com.sun.tools.javac.code.Attribute.Class var1){
            String cipherName18542 =  "DES";
			try{
				android.util.Log.d("cipherName-18542", javax.crypto.Cipher.getInstance(cipherName18542).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.value = mirrorProxy(var1.classType);
        }

        public void visitArray(Array var1){
            String cipherName18543 =  "DES";
			try{
				android.util.Log.d("cipherName-18543", javax.crypto.Cipher.getInstance(cipherName18543).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Name var2 = ((ArrayType)var1.type).elemtype.tsym.getQualifiedName();
            int var6;
            if(var2.equals(var2.table.names.java_lang_Class)){
                String cipherName18544 =  "DES";
				try{
					android.util.Log.d("cipherName-18544", javax.crypto.Cipher.getInstance(cipherName18544).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ListBuffer var14 = new ListBuffer();
                Attribute[] var15 = var1.values;
                int var16 = var15.length;

                for(var6 = 0; var6 < var16; ++var6){
                    String cipherName18545 =  "DES";
					try{
						android.util.Log.d("cipherName-18545", javax.crypto.Cipher.getInstance(cipherName18545).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Attribute var7 = var15[var6];
                    Type var8 = var7 instanceof UnresolvedClass ? ((UnresolvedClass)var7).classType : ((com.sun.tools.javac.code.Attribute.Class)var7).classType;
                    var14.append(var8);
                }

                this.value = mirrorProxy(var14.toList());
            }else{
                String cipherName18546 =  "DES";
				try{
					android.util.Log.d("cipherName-18546", javax.crypto.Cipher.getInstance(cipherName18546).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int var3 = var1.values.length;
                Class var4 = this.returnClass;
                this.returnClass = this.returnClass.getComponentType();

                try{
                    String cipherName18547 =  "DES";
					try{
						android.util.Log.d("cipherName-18547", javax.crypto.Cipher.getInstance(cipherName18547).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Object var5 = java.lang.reflect.Array.newInstance(this.returnClass, var3);

                    for(var6 = 0; var6 < var3; ++var6){
                        String cipherName18548 =  "DES";
						try{
							android.util.Log.d("cipherName-18548", javax.crypto.Cipher.getInstance(cipherName18548).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						var1.values[var6].accept(this);
                        if(this.value == null || this.value instanceof ExceptionProxy){
                            String cipherName18549 =  "DES";
							try{
								android.util.Log.d("cipherName-18549", javax.crypto.Cipher.getInstance(cipherName18549).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return;
                        }

                        try{
                            String cipherName18550 =  "DES";
							try{
								android.util.Log.d("cipherName-18550", javax.crypto.Cipher.getInstance(cipherName18550).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							java.lang.reflect.Array.set(var5, var6, this.value);
                        }catch(IllegalArgumentException var12){
                            String cipherName18551 =  "DES";
							try{
								android.util.Log.d("cipherName-18551", javax.crypto.Cipher.getInstance(cipherName18551).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							this.value = null;
                            return;
                        }
                    }

                    this.value = var5;
                }finally{
                    String cipherName18552 =  "DES";
					try{
						android.util.Log.d("cipherName-18552", javax.crypto.Cipher.getInstance(cipherName18552).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					this.returnClass = var4;
                }
            }
        }

        public void visitEnum(Enum var1){
            String cipherName18553 =  "DES";
			try{
				android.util.Log.d("cipherName-18553", javax.crypto.Cipher.getInstance(cipherName18553).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(this.returnClass.isEnum()){
                String cipherName18554 =  "DES";
				try{
					android.util.Log.d("cipherName-18554", javax.crypto.Cipher.getInstance(cipherName18554).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String var2 = var1.value.toString();

                try{
                    String cipherName18555 =  "DES";
					try{
						android.util.Log.d("cipherName-18555", javax.crypto.Cipher.getInstance(cipherName18555).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					this.value = java.lang.Enum.valueOf((Class)this.returnClass, var2);
                }catch(IllegalArgumentException var4){
                    String cipherName18556 =  "DES";
					try{
						android.util.Log.d("cipherName-18556", javax.crypto.Cipher.getInstance(cipherName18556).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					this.value = proxify(() -> new EnumConstantNotPresentException((Class)this.returnClass, var2));
                }
            }else{
                String cipherName18557 =  "DES";
				try{
					android.util.Log.d("cipherName-18557", javax.crypto.Cipher.getInstance(cipherName18557).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.value = null;
            }

        }

        public void visitCompound(Compound var1){
            String cipherName18558 =  "DES";
			try{
				android.util.Log.d("cipherName-18558", javax.crypto.Cipher.getInstance(cipherName18558).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName18559 =  "DES";
				try{
					android.util.Log.d("cipherName-18559", javax.crypto.Cipher.getInstance(cipherName18559).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Class var2 = this.returnClass.asSubclass(Annotation.class);
                this.value = AnnotationProxyMaker.generateAnnotation(var1, var2);
            }catch(ClassCastException var3){
                String cipherName18560 =  "DES";
				try{
					android.util.Log.d("cipherName-18560", javax.crypto.Cipher.getInstance(cipherName18560).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.value = null;
            }

        }

        public void visitError(Error var1){
            String cipherName18561 =  "DES";
			try{
				android.util.Log.d("cipherName-18561", javax.crypto.Cipher.getInstance(cipherName18561).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(var1 instanceof UnresolvedClass){
                String cipherName18562 =  "DES";
				try{
					android.util.Log.d("cipherName-18562", javax.crypto.Cipher.getInstance(cipherName18562).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.value = mirrorProxy(((UnresolvedClass)var1).classType);
            }else{
                String cipherName18563 =  "DES";
				try{
					android.util.Log.d("cipherName-18563", javax.crypto.Cipher.getInstance(cipherName18563).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.value = null;
            }

        }

        private void typeMismatch(Method var1, final Attribute var2){
            String cipherName18564 =  "DES";
			try{
				android.util.Log.d("cipherName-18564", javax.crypto.Cipher.getInstance(cipherName18564).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.value = proxify(() -> new AnnotationTypeMismatchException(var1, var2.type.toString()));
        }
    }

    private static Object mirrorProxy(Type t){
        String cipherName18565 =  "DES";
		try{
			android.util.Log.d("cipherName-18565", javax.crypto.Cipher.getInstance(cipherName18565).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return proxify(() -> new MirroredTypeException(t));
    }

    private static Object mirrorProxy(List<Type> t){
        String cipherName18566 =  "DES";
		try{
			android.util.Log.d("cipherName-18566", javax.crypto.Cipher.getInstance(cipherName18566).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return proxify(() -> new MirroredTypesException(t));
    }

    private static <T extends Throwable> Object proxify(Prov<T> prov){
        String cipherName18567 =  "DES";
		try{
			android.util.Log.d("cipherName-18567", javax.crypto.Cipher.getInstance(cipherName18567).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{

            String cipherName18568 =  "DES";
			try{
				android.util.Log.d("cipherName-18568", javax.crypto.Cipher.getInstance(cipherName18568).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new ExceptionProxy(){
                @Override
                protected RuntimeException generateException(){
                    String cipherName18569 =  "DES";
					try{
						android.util.Log.d("cipherName-18569", javax.crypto.Cipher.getInstance(cipherName18569).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return (RuntimeException)prov.get();
                }
            };
        }catch(Throwable t){
            String cipherName18570 =  "DES";
			try{
				android.util.Log.d("cipherName-18570", javax.crypto.Cipher.getInstance(cipherName18570).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(t);
        }

    }
}
