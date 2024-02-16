import java.sql.Date;
import java.util.Scanner;
import java.util.ArrayList;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class BestellungInteractions {
    private static Scanner scanner;
    private static MongoCollection<Document> collection;
    private static ArrayList<Bestellungspositionen> bestellungen;

    //Konstruktor
    public BestellungInteractions() {
        scanner = new Scanner(System.in);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("PC_Shop");
        collection = database.getCollection("bestellungen");
        bestellungen = new ArrayList<>();

        loadBestellungenFromDatabase();
    }

    // Methode zum Laden von Bestellungen aus der Datenbank
    private void loadBestellungenFromDatabase() {
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Bestellungspositionen bestellungspositionen = new Bestellungspositionen();
                Document computerDoc = doc.get("computer", Document.class);
                Computer computer = new Computer();
                // Computerdaten aus dem Dokument extrahieren und setzen
                // (Hersteller, Modell, Arbeitsspeicher usw.)
                computer.setHersteller(computerDoc.getString("hersteller"));
                computer.setModell(computerDoc.getString("modell"));
                computer.setArbeitsspeicher(computerDoc.getInteger("arbeitsspeicher"));
                computer.setCpu(computerDoc.getString("cpu"));
                computer.setMassenspeicher(computerDoc.getInteger("massenspeicher"));
                computer.setTyp(computerDoc.getString("typ"));
                computer.setEinzelpreis(computerDoc.getDouble("einzelpreis"));
                computer.setSchnittstellen(computerDoc.getString("schnittstellen"));
                // Kundendaten aus dem Dokument extrahieren und setzen
                // (Geschlecht, Name, Adresse usw.)
                Document kundeDoc = doc.get("kunde", Document.class);
                Kunde customer = new Kunde();
                customer.setGeschlecht(kundeDoc.getString("geschlecht"));
                customer.setNachname(kundeDoc.getString("nachname"));
                customer.setVorname(kundeDoc.getString("vorname"));
                customer.setAdresse(kundeDoc.getString("adresse"));
                customer.setTelefon(kundeDoc.getString("telefon"));
                customer.setEmail(kundeDoc.getString("email"));
                customer.setSprache(kundeDoc.getString("sprache"));
                customer.setGeburtsdatum(kundeDoc.getDate("geburtsdatum"));
                // Bestellungspositionsdaten aus dem Dokument extrahieren und ersetzen
                // (Preis, Stückzahl)
                bestellungspositionen.setComputer(computer);
                bestellungspositionen.setKunde(customer);
                bestellungspositionen.setPreis(doc.getDouble("preis"));
                bestellungspositionen.setStueckzahl(doc.getInteger("stueckzahl"));
                bestellungen.add(bestellungspositionen);
            }
        } finally {
            cursor.close();
        }
    }

    // Methode zur Interaktion mit Bestellungen
    public static void startInteraction() {
        while (true) {
            System.out.println("1. Neue Bestellung erstellen");
            System.out.println("2. Bestellungen bearbeiten");
            System.out.println("3. Bestellungen anzeigen");
            System.out.println("4. Bestellungen löschen");
            System.out.println("5. Zurück zum Hauptmenü");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createNewBestellung(collection);
                case 2 -> editBestellung(collection);
                case 3 -> displayBestellungen(collection);
                case 4 -> deleteBestellung(collection);
                case 5 -> {
                    System.out.println("Zurück zum Hauptmenü.");
                    return;
                }
                default -> System.out.println("Ungültige Option. Bitte wählen Sie erneut.");
            }
        }
    }

    // Methode zur Erstellung einer neuen Bestellung
    public static void createNewBestellung(MongoCollection<Document> collection) {
        Scanner scanner = new Scanner(System.in);

        // Computer-Informationen abfragen und setzen
        Computer computer = new Computer();
        System.out.println("Computerdaten eintragen:");
        System.out.println("Hersteller:");
        computer.setHersteller(scanner.nextLine());
        System.out.println("Modell:");
        computer.setModell(scanner.nextLine());
        System.out.println("Arbeitsspeicher (GB):");
        computer.setArbeitsspeicher(scanner.nextInt());
        scanner.nextLine();
        System.out.println("CPU:");
        computer.setCpu(scanner.nextLine());
        System.out.println("Massenspeicher (GB):");
        computer.setMassenspeicher(scanner.nextInt());
        scanner.nextLine();
        System.out.println("Typ:");
        computer.setTyp(scanner.nextLine());
        System.out.println("Einzelpreis:");
        computer.setEinzelpreis(scanner.nextDouble());
        scanner.nextLine();
        System.out.println("Schnittstellen:");
        computer.setSchnittstellen(scanner.nextLine());

        // Kunden-Informationen abfragen und setzen
        Kunde kunde = new Kunde();
        System.out.println("Kundendaten eintragen:");
        System.out.println("Geschlecht:");
        kunde.setGeschlecht(scanner.nextLine());
        System.out.println("Nachname:");
        kunde.setNachname(scanner.nextLine());
        System.out.println("Vorname:");
        kunde.setVorname(scanner.nextLine());
        System.out.println("Adresse:");
        kunde.setAdresse(scanner.nextLine());
        System.out.println("Telefon:");
        kunde.setTelefon(scanner.nextLine());
        System.out.println("Email:");
        kunde.setEmail(scanner.nextLine());
        System.out.println("Sprache:");
        kunde.setSprache(scanner.nextLine());
        System.out.println("Geburtsdatum (yyyy-mm-dd):");
        kunde.setGeburtsdatum(Date.valueOf(scanner.nextLine()));


        // Bestellungsinformationen setzen
        System.out.println("Stueckzahl der Computer:");
        int stueckzahl = scanner.nextInt();

        // Berechnen Sie den Gesamtpreis und erstellen Sie die Bestellungsposition
        double gesamtPreis = computer.getEinzelpreis() * stueckzahl;

        // Erstellen Sie Dokumente für Computer, Kunde und Bestellungsposition
        Document computerDoc = new Document()
                .append("hersteller", computer.getHersteller())
                .append("modell", computer.getModell())
                .append("arbeitsspeicher", computer.getArbeitsspeicher())
                .append("cpu", computer.getCpu())
                .append("massenspeicher", computer.getMassenspeicher())
                .append("typ", computer.getTyp())
                .append("einzelpreis", computer.getEinzelpreis())
                .append("schnittstellen", computer.getSchnittstellen());

        Document kundeDoc = new Document()
                .append("geschlecht", kunde.getGeschlecht())
                .append("nachname", kunde.getNachname())
                .append("vorname", kunde.getVorname())
                .append("adresse", kunde.getAdresse())
                .append("telefon", kunde.getTelefon())
                .append("email", kunde.getEmail())
                .append("sprache", kunde.getSprache())
                .append("geburtsdatum", kunde.getGeburtsdatum());

        Document bestellungDoc = new Document()
                .append("computer", computerDoc)
                .append("kunde", kundeDoc)
                .append("preis", gesamtPreis)
                .append("stueckzahl", stueckzahl);

        // Fügen Sie das Bestelldokument der Sammlung hinzu
        collection.insertOne(bestellungDoc);

        System.out.println("Bestellung erfolgreich erstellt:");
        System.out.println(bestellungDoc);
    }

    // Methode zur Bearbeitung einer Bestellung
    public static void editBestellung(MongoCollection<Document> collection) {
        Scanner scanner = new Scanner(System.in); // Scanner-Objekt initialisieren

        // Bestellungen anzeigen und Indexnummer abfragen
        System.out.println("Alle Bestellungen:");
        FindIterable<Document> documents = collection.find();
        if (!documents.iterator().hasNext()) {
            System.out.println("Keine Bestellungen vorhanden.");
            return;
        }

        int index = 0;
        for (Document doc : documents) {
            String computerInfo = doc.get("computer", Document.class).getString("hersteller") + " " + doc.get("computer", Document.class).getString("modell");
            int stueckzahl = doc.getInteger("stueckzahl");
            double gesamtPreis = doc.getDouble("preis");

            System.out.println("Indexnummer: " + index);
            System.out.println("Computer: " + computerInfo);
            System.out.println("Stückzahl: " + stueckzahl);
            System.out.println("Gesamtpreis: " + gesamtPreis);
            System.out.println("-----------------------------------------");
            index++;
        }

        System.out.println("Geben Sie die Indexnummer der Bestellung ein, die Sie bearbeiten möchten:");
        int selectedIndex = scanner.nextInt();
        scanner.nextLine();

        // Bestellung anhand des Index finden
        index = 0;
        Document bestellungDoc = null;
        for (Document doc : documents) {
            if (index == selectedIndex) {
                bestellungDoc = doc;
                break;
            }
            index++;
        }

        if (bestellungDoc == null) {
            System.out.println("Bestellung nicht gefunden.");
            return;
        }

        // Änderungen abfragen und Bestellung aktualisieren
        System.out.println("Geben Sie die neuen Daten für die Bestellung ein:");

        // Computer-Informationen bearbeiten
        Document computerDoc = (Document) bestellungDoc.get("computer");
        System.out.println("Computerdaten eintragen: ");
        System.out.println("Hersteller (" + computerDoc.get("hersteller") + "):");
        computerDoc.put("hersteller", scanner.nextLine());
        System.out.println("Modell (" + computerDoc.get("modell") + "):");
        computerDoc.put("modell", scanner.nextLine());
        System.out.println("Arbeitsspeicher (" + computerDoc.get("arbeitsspeicher") + "):");
        computerDoc.put("arbeitsspeicher", scanner.nextInt());
        scanner.nextLine();
        System.out.println("CPU (" + computerDoc.get("cpu") + "):");
        computerDoc.put("cpu", scanner.nextLine());
        System.out.println("Massenspeicher (" + computerDoc.get("massenspeicher") + "):");
        computerDoc.put("massenspeicher", scanner.nextInt());
        scanner.nextLine();
        System.out.println("Typ (" + computerDoc.get("typ") + "):");
        computerDoc.put("typ", scanner.nextLine());
        System.out.println("Einzelpreis (" + computerDoc.get("einzelpreis") + "):");
        computerDoc.put("einzelpreis", scanner.nextDouble());
        scanner.nextLine();
        System.out.println("Schnittstellen (" + computerDoc.get("schnittstellen") + "):");
        computerDoc.put("schnittstellen", scanner.nextLine());

        // Kunden-Informationen bearbeiten
        Document kundeDoc = (Document) bestellungDoc.get("kunde");
        System.out.println("Kundendaten eintragen: ");
        System.out.println("Nachname (" + kundeDoc.get("nachname") + "):");
        kundeDoc.put("nachname", scanner.nextLine());
        System.out.println("Vorname (" + kundeDoc.get("vorname") + "):");
        kundeDoc.put("vorname", scanner.nextLine());
        System.out.println("Adresse (" + kundeDoc.get("adresse") + "):");
        kundeDoc.put("adresse", scanner.nextLine());
        System.out.println("Telefon (" + kundeDoc.get("telefon") + "):");
        kundeDoc.put("telefon", scanner.nextLine());
        System.out.println("Email (" + kundeDoc.get("email") + "):");
        kundeDoc.put("email", scanner.nextLine());
        System.out.println("Sprache (" + kundeDoc.get("sprache") + "):");
        kundeDoc.put("sprache", scanner.nextLine());
        System.out.println("Geburtsdatum (" + kundeDoc.get("geburtsdatum") + "):");
        kundeDoc.put("geburtsdatum", Date.valueOf(scanner.nextLine()));

        // Bestellungsdaten bearbeiten
        System.out.println("Bestellungsdaten eintragen: ");
        System.out.println("Stückzahl der Computer (" + bestellungDoc.get("stueckzahl") + "):");
        bestellungDoc.put("stueckzahl", scanner.nextInt());
        scanner.nextLine(); // Clear buffer
        System.out.println("Preis (" + bestellungDoc.get("preis") + "):");
        double preis = scanner.nextDouble();
        bestellungDoc.put("preis", preis);

        // Update der Bestellung in der Datenbank
        collection.replaceOne(new Document("_id", bestellungDoc.getObjectId("_id")), bestellungDoc);

        System.out.println("Bestellung erfolgreich bearbeitet.");
    }

    // Methode zur Anzeige aller Bestellungen mit Index und Auswahlmöglichkeit
    public static void displayBestellungen(MongoCollection<Document> collection) {
        System.out.println("Alle Bestellungen:");

        FindIterable<Document> documents = collection.find();
        if (!documents.iterator().hasNext()) {
            System.out.println("Keine Bestellungen vorhanden.");
            return;
        }

        int index = 0;
        for (Document doc : documents) {
            String computerInfo = doc.get("computer", Document.class).getString("hersteller") + " " + doc.get("computer", Document.class).getString("modell");
            int stueckzahl = doc.getInteger("stueckzahl");
            double gesamtPreis = doc.getDouble("preis");

            System.out.println("Indexnummer: " + index);
            System.out.println("Computer: " + computerInfo);
            System.out.println("Stückzahl: " + stueckzahl);
            System.out.println("Gesamtpreis: " + gesamtPreis);
            System.out.println("-----------------------------------------");
            index++;
        }
    }

    // Methode zum Löschen einer Bestellung
    public static void deleteBestellung(MongoCollection<Document> collection) {
        Scanner scanner = new Scanner(System.in); // Scanner-Objekt initialisieren

        displayBestellungen(collection);

        System.out.println("Geben Sie die Indexnummer der Bestellung ein, die Sie löschen möchten:");
        int index = scanner.nextInt();
        scanner.nextLine();

        FindIterable<Document> documents = collection.find();
        Document bestellungDoc = null;
        int currentIndex = 0;
        for (Document doc : documents) {
            if (currentIndex == index) {
                bestellungDoc = doc;
                break;
            }
            currentIndex++;
        }

        if (bestellungDoc == null) {
            System.out.println("Bestellung nicht gefunden.");
            return;
        }

        collection.deleteOne(new Document("_id", bestellungDoc.getObjectId("_id")));

        System.out.println("Bestellung erfolgreich gelöscht.");
    }
}
