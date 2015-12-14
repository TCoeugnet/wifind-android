package fr.ig2i.wifind.objects;

/**
 * Created by Thomas on 14/12/2015.
 */
public class Plan {

    private Etage etage;

    private Image image;

    private float origineX;

    private float origineY;

    private float echelle;

    public Etage getEtage() {
        return etage;
    }

    public void setEtage(Etage etage) {
        this.etage = etage;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public float getOrigineX() {
        return origineX;
    }

    public void setOrigineX(float origineX) {
        this.origineX = origineX;
    }

    public float getOrigineY() {
        return origineY;
    }

    public void setOrigineY(float origineY) {
        this.origineY = origineY;
    }

    public float getEchelle() {
        return echelle;
    }

    public void setEchelle(float echelle) {
        this.echelle = echelle;
    }

    public Plan(Etage etage, Image image, float origineX, float origineY, float echelle) {
        this.etage = etage;
        this.image = image;
        this.origineX = origineX;
        this.origineY = origineY;
        this.echelle = echelle;
    }
}
