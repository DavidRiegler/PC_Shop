import org.bson.types.ObjectId;
import java.util.Date;

public class Bestellung {
    // Attribute
    private ObjectId bestellnummer;
    private Date bestelldatum;
    private Kunde kunde;
    private Bestellungspositionen bestellungspositionen;



    // Getter und Setter
    public ObjectId getBestellnummer() {
        return bestellnummer;
    }

    public void setBestellnummer(ObjectId bestellnummer) {
        this.bestellnummer = bestellnummer;
    }

    public Date getBestelldatum() {
        return bestelldatum;
    }

    public void setBestelldatum(Date bestelldatum) {
        this.bestelldatum = bestelldatum;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public Bestellungspositionen getBestellungspositionen() {
        return bestellungspositionen;
    }

    public void setBestellungspositionen(Bestellungspositionen bestellungspositionen) {
        this.bestellungspositionen = bestellungspositionen;
    }
}
