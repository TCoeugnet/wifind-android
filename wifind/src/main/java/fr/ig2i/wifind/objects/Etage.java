package fr.ig2i.wifind.objects;

/**
 * Created by Thomas on 14/12/2015.
 */
public class Etage {

    private Plan plan;

    private CentreCommercial centreCommercial;

    private int niveau;

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public CentreCommercial getCentreCommercial() {
        return centreCommercial;
    }

    public void setCentreCommercial(CentreCommercial centreCommercial) {
        this.centreCommercial = centreCommercial;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public Etage(Plan plan, CentreCommercial centreCommercial, int niveau) {
        this.plan = plan;
        this.centreCommercial = centreCommercial;
        this.niveau = niveau;
    }
}
