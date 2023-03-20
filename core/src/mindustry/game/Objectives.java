package mindustry.game;

import arc.*;
import arc.scene.ui.layout.*;
import mindustry.ctype.*;
import mindustry.type.*;

/** Holds objective classes. */
public class Objectives{

    public static class Research implements Objective{
        public UnlockableContent content;

        public Research(UnlockableContent content){
            String cipherName11769 =  "DES";
			try{
				android.util.Log.d("cipherName-11769", javax.crypto.Cipher.getInstance(cipherName11769).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.content = content;
        }

        protected Research(){
			String cipherName11770 =  "DES";
			try{
				android.util.Log.d("cipherName-11770", javax.crypto.Cipher.getInstance(cipherName11770).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean complete(){
            String cipherName11771 =  "DES";
			try{
				android.util.Log.d("cipherName-11771", javax.crypto.Cipher.getInstance(cipherName11771).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return content.unlocked();
        }

        @Override
        public String display(){
            String cipherName11772 =  "DES";
			try{
				android.util.Log.d("cipherName-11772", javax.crypto.Cipher.getInstance(cipherName11772).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("requirement.research",
                //TODO broken for multi tech nodes.
                (content.techNode == null || content.techNode.parent == null || content.techNode.parent.content.unlocked()) && !(content instanceof Item) ?
                    (content.emoji() + " " + content.localizedName) : "???");
        }
    }

    public static class Produce implements Objective{
        public UnlockableContent content;

        public Produce(UnlockableContent content){
            String cipherName11773 =  "DES";
			try{
				android.util.Log.d("cipherName-11773", javax.crypto.Cipher.getInstance(cipherName11773).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.content = content;
        }

        protected Produce(){
			String cipherName11774 =  "DES";
			try{
				android.util.Log.d("cipherName-11774", javax.crypto.Cipher.getInstance(cipherName11774).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean complete(){
            String cipherName11775 =  "DES";
			try{
				android.util.Log.d("cipherName-11775", javax.crypto.Cipher.getInstance(cipherName11775).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return content.unlocked();
        }

        @Override
        public String display(){
            String cipherName11776 =  "DES";
			try{
				android.util.Log.d("cipherName-11776", javax.crypto.Cipher.getInstance(cipherName11776).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("requirement.produce",
                content.unlocked() ? (content.emoji() + " " + content.localizedName) : "???");
        }
    }

    public static class SectorComplete implements Objective{
        public SectorPreset preset;

        public SectorComplete(SectorPreset zone){
            String cipherName11777 =  "DES";
			try{
				android.util.Log.d("cipherName-11777", javax.crypto.Cipher.getInstance(cipherName11777).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.preset = zone;
        }

        protected SectorComplete(){
			String cipherName11778 =  "DES";
			try{
				android.util.Log.d("cipherName-11778", javax.crypto.Cipher.getInstance(cipherName11778).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean complete(){
            String cipherName11779 =  "DES";
			try{
				android.util.Log.d("cipherName-11779", javax.crypto.Cipher.getInstance(cipherName11779).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return preset.sector.save != null && preset.sector.isCaptured() && preset.sector.hasBase();
        }

        @Override
        public String display(){
            String cipherName11780 =  "DES";
			try{
				android.util.Log.d("cipherName-11780", javax.crypto.Cipher.getInstance(cipherName11780).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("requirement.capture", preset.localizedName);
        }
    }

    public static class OnSector implements Objective{
        public SectorPreset preset;

        public OnSector(SectorPreset zone){
            String cipherName11781 =  "DES";
			try{
				android.util.Log.d("cipherName-11781", javax.crypto.Cipher.getInstance(cipherName11781).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.preset = zone;
        }

        protected OnSector(){
			String cipherName11782 =  "DES";
			try{
				android.util.Log.d("cipherName-11782", javax.crypto.Cipher.getInstance(cipherName11782).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean complete(){
            String cipherName11783 =  "DES";
			try{
				android.util.Log.d("cipherName-11783", javax.crypto.Cipher.getInstance(cipherName11783).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return preset.sector.hasBase();
        }

        @Override
        public String display(){
            String cipherName11784 =  "DES";
			try{
				android.util.Log.d("cipherName-11784", javax.crypto.Cipher.getInstance(cipherName11784).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("requirement.onsector", preset.localizedName);
        }
    }

    public static class OnPlanet implements Objective{
        public Planet planet;

        public OnPlanet(Planet planet){
            String cipherName11785 =  "DES";
			try{
				android.util.Log.d("cipherName-11785", javax.crypto.Cipher.getInstance(cipherName11785).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.planet = planet;
        }

        protected OnPlanet(){
			String cipherName11786 =  "DES";
			try{
				android.util.Log.d("cipherName-11786", javax.crypto.Cipher.getInstance(cipherName11786).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean complete(){
            String cipherName11787 =  "DES";
			try{
				android.util.Log.d("cipherName-11787", javax.crypto.Cipher.getInstance(cipherName11787).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return planet.sectors.contains(Sector::hasBase);
        }

        @Override
        public String display(){
            String cipherName11788 =  "DES";
			try{
				android.util.Log.d("cipherName-11788", javax.crypto.Cipher.getInstance(cipherName11788).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("requirement.onplanet", planet.localizedName);
        }
    }

    /** Defines a specific objective for a game. */
    public interface Objective{

        /** @return whether this objective is met. */
        boolean complete();

        /** @return the string displayed when this objective is completed, in imperative form.
         * e.g. when the objective is 'complete 10 waves', this would display "complete 10 waves". */
        String display();

        /** Build a display for this zone requirement.*/
        default void build(Table table){

        }
    }
}
