import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class BestellungInteractions {

    private static Scanner scanner;
    private static MongoCollection<Document> collection;
    private static ArrayList<Bestellung> bestellungen;

    public BestellungInteractions() {
        scanner = new Scanner(System.in);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("PC_Shop");
        collection = database.getCollection("bestellungen");
        bestellungen = new ArrayList<>();

        loadBestellungenFromDatabase();
    }

    private void loadBestellungenFromDatabase() {
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Bestellung bestellung = new Bestellung();
                Document computerDoc = doc.get("computer", Document.class);
                Computer computer = new Computer();
                computer.setHersteller(computerDoc.getString("hersteller"));
                computer.setModell(computerDoc.getString("modell"));
                computer.setArbeitsspeicher(computerDoc.getInteger("arbeitsspeicher"));
                computer.setCpu(computerDoc.getString("cpu"));
                computer.setMassenspeicher(computerDoc.getInteger("massenspeicher"));
                computer.setTyp(computerDoc.getString("typ"));
                computer.setEinzelpreis(computerDoc.getDouble("einzelpreis"));
                computer.setSchnittstellen(computerDoc.getString("schnittstellen"));
                bestellung.setComputer(computer);
                bestellung.setPreis(doc.getDouble("preis"));
                bestellung.setStueckzahl(doc.getInteger("stueckzahl"));
                bestellungen.add(bestellung);
            }
        } finally {
            cursor.close();
        }
    }

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
                case 1 -> createNewBestellung();
                case 2 -> editBestellung();
                case 3 -> displayBestellungen();
                case 4 -> deleteBestellung();
                case 5 -> {
                    System.out.println("Zurück zum Hauptmenü.");
                    return;
                }
                default -> System.out.println("Ungültige Option. Bitte wählen Sie erneut.");
            }
        }
    }

    public static void createNewBestellung() {
        Computer computer = new Computer();
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

        System.out.println("Stückzahl:");
        int stueckzahl = scanner.nextInt();
        scanner.nextLine();

        double gesamtPreis = computer.getEinzelpreis() * stueckzahl;

        Bestellung bestellung = new Bestellung();
        bestellung.setComputer(computer);
        bestellung.setPreis(gesamtPreis);
        bestellung.setStueckzahl(stueckzahl);
        bestellungen.add(bestellung);

        Document bestellungDoc = new Document("computer", new Document()
                .append("hersteller", computer.getHersteller())
                .append("modell", computer.getModell())
                .append("arbeitsspeicher", computer.getArbeitsspeicher())
                .append("cpu", computer.getCpu())
                .append("massenspeicher", computer.getMassenspeicher())
                .append("typ", computer.getTyp())
                .append("einzelpreis", computer.getEinzelpreis())
                .append("schnittstellen", computer.getSchnittstellen()))
                .append("preis", gesamtPreis)
                .append("stueckzahl", stueckzahl);
        collection.insertOne(bestellungDoc);

        System.out.println("Bestellung erfolgreich erstellt:");
        System.out.println(bestellung);
    }

    public static void editBestellung() {
        displayBestellungen();

        System.out.println("Geben Sie die Indexnummer der Bestellung ein, die Sie bearbeiten möchten:");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index < 0 || index >= bestellungen.size()) {
            System.out.println("Ungültige Indexnummer.");
            return;
        }

        Bestellung bestellung = bestellungen.get(index);
        Computer computer = bestellung.getComputer();

        System.out.println("Aktuelle Bestellung:");
        System.out.println(bestellung);

        System.out.println("Möchten Sie den Computer bearbeiten? (ja/nein)");
        String bearbeiten = scanner.nextLine();
        if (bearbeiten.equalsIgnoreCase("ja")) {
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
        }

        System.out.println("Möchten Sie die Stückzahl ändern? (ja/nein)");
        bearbeiten = scanner.nextLine();
        if (bearbeiten.equalsIgnoreCase("ja")) {
            System.out.println("Neue Stückzahl:");
            int neueStueckzahl = scanner.nextInt();
            scanner.nextLine();
            bestellung.setStueckzahl(neueStueckzahl);
        }

        bestellung.setPreis(computer.getEinzelpreis() * bestellung.getStueckzahl());

        collection.updateOne(new Document("computer.hersteller", computer.getHersteller())
                        .append("computer.modell", computer.getModell())
                        .append("preis", bestellung.getPreis())
                        .append("stueckzahl", bestellung.getStueckzahl()),
                new Document("$set", new Document("computer.arbeitsspeicher", computer.getArbeitsspeicher())
                        .append("computer.cpu", computer.getCpu())
                        .append("computer.massenspeicher", computer.getMassenspeicher())
                        .append("computer.typ", computer.getTyp())
                        .append("computer.einzelpreis", computer.getEinzelpreis())
                        .append("computer.schnittstellen", computer.getSchnittstellen())
                        .append("stueckzahl", bestellung.getStueckzahl())));

        System.out.println("Bestellung erfolgreich bearbeitet:");
        System.out.println(bestellung);
    }

    public static void displayBestellungen() {
        System.out.println("Alle Bestellungen:");

        if (bestellungen.isEmpty()) {
            System.out.println("Keine Bestellungen vorhanden.");
            return;
        }

        Iterator<Bestellung> iterator = bestellungen.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Bestellung bestellung = iterator.next();
            System.out.println("Indexnummer: " + index++);
            System.out.println(bestellung);
        }
    }

    public static void deleteBestellung() {
        displayBestellungen();

        System.out.println("Geben Sie die Indexnummer der Bestellung ein, die Sie löschen möchten:");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index < 0 || index >= bestellungen.size()) {
            System.out.println("Ungültige Indexnummer.");
            return;
        }

        Bestellung bestellung = bestellungen.remove(index);

        Document query = new Document("computer.hersteller", bestellung.getComputer().getHersteller())
                .append("computer.modell", bestellung.getComputer().getModell())
                .append("preis", bestellung.getPreis())
                .append("stueckzahl", bestellung.getStueckzahl());
        collection.deleteOne(query);

        System.out.println("Bestellung erfolgreich gelöscht:");
        System.out.println(bestellung);
    }
}
