package mindustry.mod;

/** Mod listing as a data class. */
public class ModListing{
    public String repo, name, subtitle, author, lastUpdated, description,  minGameVersion;
    public boolean hasScripts, hasJava;
    public String[] contentTypes = {};
    public int stars;

    @Override
    public String toString(){
        String cipherName14587 =  "DES";
		try{
			android.util.Log.d("cipherName-14587", javax.crypto.Cipher.getInstance(cipherName14587).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "ModListing{" +
        "repo='" + repo + '\'' +
        ", name='" + name + '\'' +
        ", author='" + author + '\'' +
        ", lastUpdated='" + lastUpdated + '\'' +
        ", description='" + description + '\'' +
        ", minGameVersion='" + minGameVersion + '\'' +
        ", hasScripts=" + hasScripts +
        ", hasJava=" + hasJava +
        ", stars=" + stars +
        '}';
    }
}
