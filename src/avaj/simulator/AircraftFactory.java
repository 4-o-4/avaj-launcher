package avaj.simulator;

import java.util.Locale;

public abstract class AircraftFactory {
    public static Flyable newAircraft(String type, String name, int longitude, int latitude, int height) {
        return switch (type.toLowerCase(Locale.ROOT)) {
            case "helicopter" -> new Helicopter(name, new Coordinates(longitude, latitude, height));
            default -> null;
        };
    }
}
