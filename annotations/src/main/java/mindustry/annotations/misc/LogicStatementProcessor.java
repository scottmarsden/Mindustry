package mindustry.annotations.misc;

import arc.func.*;
import arc.struct.*;
import com.squareup.javapoet.*;
import mindustry.annotations.Annotations.*;
import mindustry.annotations.*;
import mindustry.annotations.util.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;

@SupportedAnnotationTypes("mindustry.annotations.Annotations.RegisterStatement")
public class LogicStatementProcessor extends BaseProcessor{

    @Override
    public void process(RoundEnvironment env) throws Exception{
        String cipherName18483 =  "DES";
		try{
			android.util.Log.d("cipherName-18483", javax.crypto.Cipher.getInstance(cipherName18483).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TypeSpec.Builder type = TypeSpec.classBuilder("LogicIO")
            .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder writer = MethodSpec.methodBuilder("write")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(Object.class, "obj")
            .addParameter(StringBuilder.class, "out");

        MethodSpec.Builder reader = MethodSpec.methodBuilder("read")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(tname("mindustry.logic.LStatement"))
            .addParameter(String[].class, "tokens")
            .addParameter(int.class, "length");

        Seq<Stype> types = types(RegisterStatement.class);

        type.addField(FieldSpec.builder(
            ParameterizedTypeName.get(
            ClassName.get(Seq.class),
            ParameterizedTypeName.get(ClassName.get(Prov.class),
                tname("mindustry.logic.LStatement"))), "allStatements", Modifier.PUBLIC, Modifier.STATIC)
            .initializer("Seq.with(" + types.toString(", ", t -> "" + t.toString() + "::new") + ")").build());

        boolean beganWrite = false, beganRead = false;

        for(Stype c : types){
            String cipherName18484 =  "DES";
			try{
				android.util.Log.d("cipherName-18484", javax.crypto.Cipher.getInstance(cipherName18484).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String name = c.annotation(RegisterStatement.class).value();

            if(beganWrite){
                String cipherName18485 =  "DES";
				try{
					android.util.Log.d("cipherName-18485", javax.crypto.Cipher.getInstance(cipherName18485).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writer.nextControlFlow("else if(obj.getClass() == $T.class)", c.mirror());
            }else{
                String cipherName18486 =  "DES";
				try{
					android.util.Log.d("cipherName-18486", javax.crypto.Cipher.getInstance(cipherName18486).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writer.beginControlFlow("if(obj.getClass() == $T.class)", c.mirror());
                beganWrite = true;
            }

            //write the name & individual fields
            writer.addStatement("out.append($S)", name);

            Seq<Svar> fields = c.fields();
            fields.addAll(c.superclass().fields());

            String readSt = "if(tokens[0].equals($S))";
            if(beganRead){
                String cipherName18487 =  "DES";
				try{
					android.util.Log.d("cipherName-18487", javax.crypto.Cipher.getInstance(cipherName18487).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				reader.nextControlFlow("else " + readSt, name);
            }else{
                String cipherName18488 =  "DES";
				try{
					android.util.Log.d("cipherName-18488", javax.crypto.Cipher.getInstance(cipherName18488).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				reader.beginControlFlow(readSt, name);
                beganRead = true;
            }

            reader.addStatement("$T result = new $T()", c.mirror(), c.mirror());

            int index = 0;

            for(Svar field : fields){
                String cipherName18489 =  "DES";
				try{
					android.util.Log.d("cipherName-18489", javax.crypto.Cipher.getInstance(cipherName18489).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(field.isAny(Modifier.TRANSIENT, Modifier.STATIC)) continue;

                writer.addStatement("out.append(\" \")");
                writer.addStatement("out.append((($T)obj).$L$L)", c.mirror(), field.name(),
                    Seq.with(typeu.directSupertypes(field.mirror())).contains(t -> t.toString().contains("java.lang.Enum")) ? ".name()" :
                    "");

                //reading primitives, strings and enums is supported; nothing else is
                reader.addStatement("if(length > $L) result.$L = $L(tokens[$L])",
                index + 1,
                field.name(),
                field.mirror().toString().equals("java.lang.String") ?
                "" : (field.tname().isPrimitive() ? field.tname().box().toString() :
                field.mirror().toString()) + ".valueOf", //if it's not a string, it must have a valueOf method
                index + 1
                );

                index ++;
            }

            reader.addStatement("result.afterRead()");
            reader.addStatement("return result");
        }

        reader.endControlFlow();
        writer.endControlFlow();

        reader.addStatement("return null");

        type.addMethod(writer.build());
        type.addMethod(reader.build());

        write(type);
    }
}
