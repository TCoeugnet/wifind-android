package fr.ig2i.wifind.objects;

/**
 * Created by Thomas on 14/12/2015.
 */
public class Image {

    private String chemin;

    private String hash;

    private String description;

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image(String chemin, String hash, String description) {
        this.chemin = chemin;
        this.hash = hash;
        this.description = description;
    }
}
