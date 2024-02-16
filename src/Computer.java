import org.bson.types.ObjectId;

public class Computer {
    private ObjectId id;
    private String hersteller;
    private String modell;
    private int arbeitsspeicher;
    private String cpu;
    private int massenspeicher;
    private String typ;
    private double einzelpreis;
    private String schnittstellen;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public int getArbeitsspeicher() {
        return arbeitsspeicher;
    }

    public void setArbeitsspeicher(int arbeitsspeicher) {
        this.arbeitsspeicher = arbeitsspeicher;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public int getMassenspeicher() {
        return massenspeicher;
    }

    public void setMassenspeicher(int massenspeicher) {
        this.massenspeicher = massenspeicher;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public double getEinzelpreis() {
        return einzelpreis;
    }

    public void setEinzelpreis(double einzelpreis) {
        this.einzelpreis = einzelpreis;
    }

    public String getSchnittstellen() {
        return schnittstellen;
    }

    public void setSchnittstellen(String schnittstellen) {
        this.schnittstellen = schnittstellen;
    }
}
