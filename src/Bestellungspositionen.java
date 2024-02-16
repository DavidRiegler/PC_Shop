public class Bestellungspositionen extends Bestellung {
    //Attribute
    private static Computer computer;
    private static double preis;
    private static int stueckzahl;


    //getter und setter
    public static Computer getComputer() {
        return computer;
    }

    public static void setComputer(Computer computer) {
        Bestellungspositionen.computer = computer;
    }

    public static double getPreis() {
        return preis;
    }

    public static void setPreis(double preis) {
        Bestellungspositionen.preis = preis;
    }

    public static int getStueckzahl() {
        return stueckzahl;
    }

    public static void setStueckzahl(int stueckzahl) {
        Bestellungspositionen.stueckzahl = stueckzahl;
    }
}
