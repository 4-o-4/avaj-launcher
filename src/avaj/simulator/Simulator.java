package avaj.simulator;

import avaj.simulator.air.AircraftFactory;
import avaj.simulator.tower.Flyable;
import avaj.simulator.exception.NumberCheckException;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Simulator {
    private static final String ERROR = "\u001B[31m[Error]\u001B[0m";
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(ERROR + " - программа принимаеть один и только один аргумент");
        } else {
            try {
                BufferedReader fr = new BufferedReader(new FileReader(args[0]));

                /* Парсер */
                Parser parser = new Parser();
                parser.setCount(fr.readLine());
                parser.lines(fr);
                fr.close();

                /* Register tower */
                WeatherTower tower = new WeatherTower();
                parser.getFlyable().forEach(f -> f.registerTower(tower));

                /* Смена погоды */
                for (int i = 0; i < parser.getCount() && !tower.isNull(); i++) {
                    tower.changeWeather();
                }
                tower.close();
            } catch (IOException e) {
                System.out.println(ERROR + " - файл не найден");
            } catch (NullPointerException | NumberCheckException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
    }

    private static class Parser {
        private int count;
        private final List<Flyable> flyable = new LinkedList<>();

        public int getCount() { return count; }

        public List<Flyable> getFlyable() { return flyable; }

        public void setCount(String number) {
            if (number == null)
                throw new NullPointerException(" - пустой файл");
            String[] l = number.split("\\s+");
            if (l.length != 1 || l[0].isEmpty())
                throw new NumberCheckException(" - ...");
            count = numberCheck(l[0], 1);
        }

        private void setFlyable(String[] l, int line_n) {
            List<Integer> n = new ArrayList<>(3);
            for (int i = 2; i < l.length; i++)
                n.add(numberCheck(l[i], line_n));
            if (n.get(2) > 100)
                throw new NumberCheckException(" - ...");
            flyable.add(AircraftFactory.newAircraft(l[0], l[1], n.get(0), n.get(1), n.get(2)));
        }

        private int numberCheck(String number, int line_n) {
            int n = parseNumber(number, line_n);
            if (n < 0) {
                throw new NumberCheckException(" - ...");
            }
            return n;
        }

        private int parseNumber(String number, int line_n) {
            try {
                return Integer.parseInt(number);
            } catch (NumberFormatException e) {
                throw new NumberCheckException(" - ...");
            }
        }

        void lines(BufferedReader fr) throws IOException {
            String line;
            for (int line_n = 2; (line = fr.readLine()) != null; ++line_n) {
                String[] l = line.split("\\s+");
                if (l.length != 5) {
                    throw new NumberCheckException(" - ...");
                }
                setFlyable(l, line_n);
            }
        }
    }
}
