package space.cloud4b.verein.einstellungen;

import java.io.FileInputStream;
import java.util.Properties;

public abstract class Einstellung {


    public static String getdbURL() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream("../VereinsManager/src/space/cloud4b/verein/einstellungen/Systemeinstellungen.xml"));
            String propValue = loadProps.getProperty("dbURL");
            return propValue;
        }
        catch (Exception e ) {
            System.out.println("dbURL konnte nicht ermittelt werden.");
            e.printStackTrace();
        }
        return null;
    }

    public static String getVereinsName() {
        try {
            // Load Settings
            Properties loadProps = new Properties();
            loadProps.loadFromXML(new FileInputStream("../VereinsManager/src/space/cloud4b/verein/einstellungen/Mandanteneinstellungen.xml"));
            String propValue = loadProps.getProperty("VereinsName");
            return propValue;
        }
        catch (Exception e ) {
            return "Verein ohne Namen";
            //e.printStackTrace();
        }


    }
}
