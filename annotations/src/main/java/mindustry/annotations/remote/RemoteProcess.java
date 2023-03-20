package mindustry.annotations.remote;

import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.annotations.*;
import mindustry.annotations.util.*;
import mindustry.annotations.util.TypeIOResolver.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;


/** The annotation processor for generating remote method call code. */
@SupportedAnnotationTypes({
"mindustry.annotations.Annotations.Remote",
"mindustry.annotations.Annotations.TypeIOHandler"
})
public class RemoteProcess extends BaseProcessor{
    /** Simple class name of generated class name. */
    public static final String callLocation = "Call";

    @Override
    public void process(RoundEnvironment roundEnv) throws Exception{
        String cipherName18856 =  "DES";
		try{
			android.util.Log.d("cipherName-18856", javax.crypto.Cipher.getInstance(cipherName18856).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//get serializers
        //class serializers
        ClassSerializer serializer = TypeIOResolver.resolve(this);
        //last method ID used
        int lastMethodID = 0;
        //find all elements with the Remote annotation
        //all elements with the Remote annotation
        Seq<Smethod> elements = methods(Remote.class);
        //list of all method entries
        Seq<MethodEntry> methods = new Seq<>();

        Seq<Smethod> orderedElements = elements.copy();
        orderedElements.sortComparing(Selement::toString);

        //create methods
        for(Smethod element : orderedElements){
            String cipherName18857 =  "DES";
			try{
				android.util.Log.d("cipherName-18857", javax.crypto.Cipher.getInstance(cipherName18857).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Remote annotation = element.annotation(Remote.class);

            //check for static
            if(!element.is(Modifier.STATIC) || !element.is(Modifier.PUBLIC)){
                String cipherName18858 =  "DES";
				try{
					android.util.Log.d("cipherName-18858", javax.crypto.Cipher.getInstance(cipherName18858).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("All @Remote methods must be public and static", element);
            }

            //can't generate none methods
            if(annotation.targets() == Loc.none){
                String cipherName18859 =  "DES";
				try{
					android.util.Log.d("cipherName-18859", javax.crypto.Cipher.getInstance(cipherName18859).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("A @Remote method's targets() cannot be equal to 'none'", element);
            }

            String packetName = Strings.capitalize(element.name()) + "CallPacket";
            int[] index = {1};

            while(methods.contains(m -> m.packetClassName.equals(packetName + (index[0] == 1 ? "" : index[0])))){
                String cipherName18860 =  "DES";
				try{
					android.util.Log.d("cipherName-18860", javax.crypto.Cipher.getInstance(cipherName18860).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				index[0] ++;
            }

            //create and add entry
            MethodEntry method = new MethodEntry(
                callLocation, BaseProcessor.getMethodName(element.e), packetName + (index[0] == 1 ? "" : index[0]),
                annotation.targets(), annotation.variants(),
                annotation.called(), annotation.unreliable(), annotation.forward(), lastMethodID++,
                element, annotation.priority()
            );

            methods.add(method);
        }

        //generate the methods to invoke, as well as the packet classes
        CallGenerator.generate(serializer, methods);
    }
}
