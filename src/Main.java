import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ComputerInteractions computerManager = new ComputerInteractions();
        KundeInteractions customerManager = new KundeInteractions();
        BestellungInteractions orderManager = new BestellungInteractions();

        // Verbindung zur Datenbank herstellen
        DbAccess.connect("PC_Shop");

        while (true) {
            System.out.println("Hauptmenü:");
            System.out.println("1. Computer verwalten");
            System.out.println("2. Kunden verwalten");
            System.out.println("3. Bestellungen verwalten");
            System.out.println("4. Beenden");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                // Fall, um die Interaktion mit Computern zu starten
                case 1 -> ComputerInteractions.startInteraction();
                // Fall, um die Interaktion mit Kunden zu starten
                case 2 -> KundeInteractions.startInteraction();
                // Fall, um die Interaktion mit Bestellungen zu starten
                case 3 -> BestellungInteractions.startInteraction();
                // Fall, um das Programm zu beenden
                case 4 -> {
                    System.out.println("Auf Wiedersehen!");
                    // Datenbankverbindung schließen
                    DbAccess.close();
                    System.exit(0);
                }
                // Standardfall für ungültige Eingaben
                default -> System.out.println("Ungültige Eingabe. Bitte wählen Sie erneut.");
            }
        }
    }
}
