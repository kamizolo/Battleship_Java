package battleship;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

enum Char {
    A(0, "A"),
    B(1, "B"),
    C(2, "C"),
    D(3, "D"),
    E(4, "E"),
    F(5, "F"),
    G(6, "G"),
    H(7, "H"),
    I(8, "I"),
    J(9, "J"),
    DEFAULT(-1,"");

    final int value;
    final String letter;
    Char(int value, String letter){
        this.value = value;
        this.letter = letter;
    }
    public static int valueByLetter(String l) {
        Char temp = Arrays.stream(Char.values()).filter(aEnum -> aEnum.letter.equals(l)).findFirst().orElse(Char.DEFAULT);
        return temp.value;
    }
    public static String letterByValue(int i) {
        Char temp = Arrays.stream(Char.values()).filter(aEnum -> aEnum.value == (i)).findFirst().orElse(Char.DEFAULT);
        return temp.letter;
    }
}
enum Field {
    FOG("~"),
    MISS("M"),
    SHIP("O"),
    HIT("X");

    final String symbol;
    Field(String symbol) {
        this.symbol = symbol;
    }
}

public class Main {

    //prints a field
    public static void printMap(String[][] map) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            char n = 'A';
            n += i;
            System.out.print(n + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    //checks if coordinates are valid
    static boolean checkCoordinates(String start, String end, int length) {

        int a = Integer.parseInt(start.substring(1));
        int b = Integer.parseInt(end.substring(1));

        //check for outside bord
        if(a > 10 || a < 0 || b > 10 || b < 0) {
            System.out.println("Error! coordinates outside of field");
            return false;
        }
        //check vertical
       // if(Math.abs(a - b) != length) return false;

        int x = Char.valueByLetter(start.substring(0,1));
        int y = Char.valueByLetter(end.substring(0,1));

        //check for outside bord
        if(x > 10 || x < 0 || y > 10 || y < 0) {
            System.out.println("Error! coordinates outside of field");
            return false;
        }
        //check length
        if(Math.abs(x - y) != length - 1 && Math.abs(a - b) != length - 1){
            System.out.println("Error wrong length");
            return false;
        }

        //check for diagonals
        if (!(x == y || a == b)){
            System.out.println("Error wrong coordinates");
            return false;
        }
        return true;
    }

    //checks if coordinates are valid and sets ship on map
    public static boolean placeShip(String start, String end, String[][] map) {
        int temp = Integer.parseInt(start.substring(1));
        int x;
        int y;
        if (temp < Integer.parseInt(end.substring(1))) {
            x = temp - 1;
            y = Integer.parseInt(end.substring(1)) - 1;
        } else {
            x = Integer.parseInt(end.substring(1)) - 1;
            y = temp - 1;
        }

        if(start.substring(0, 1).equals(end.substring(0, 1))){
            if(checkIfFree(x, y, Char.valueByLetter(start.substring(0,1)), true, map)) {
                for (int i = x ; i <= y; i++) {
                   // if(neighbour(x -1, y, Char.valueByLetter(start.substring(0,1)), map, true)) {
                        map[Char.valueByLetter(start.substring(0,1))][i] = Field.SHIP.symbol;
                   // }
                }
            } else {
                return false;
            }
        } else {
            temp = Char.valueByLetter(start.substring(0,1));
            if (temp < Char.valueByLetter(end.substring(0,1))) {
                x = temp;
                y = Char.valueByLetter(end.substring(0,1));
            } else {
                x = Char.valueByLetter(end.substring(0,1));
                y = temp;
            }
            if(checkIfFree(x, y, Integer.parseInt(start.substring(1)) - 1, false, map)) {
                for (int i = x; i <= y; i++) {
                    //if(neighbour(x -1, y, Char.valueByLetter(start.substring(0,1)), map, true)) {
                        map[i][Integer.parseInt(start.substring(1)) - 1] = Field.SHIP.symbol;
                    //}
                }
            } else {
                return false;
            }
        }
        return true;
    }
    //checks if coordinates are occupied
    public static boolean checkIfFree(int s, int e, int o, boolean horizontal, String[][] map) {
        if (horizontal) {
            for (int i = s; i <= e; i++) {
                if (Objects.equals(map[o][i], Field.SHIP.symbol)){
                    System.out.println("Error location taken");
                    return false;
                }
            }
        } else {
            for (int i = s; i <= e; i++) {
                if (Objects.equals(map[i][o], Field.SHIP.symbol)) {
                    System.out.println("Error location taken");
                    return false;
                }
            }
        }
        return checkNeighbourSpaces(s, e, o, map, horizontal);
    }
    //checks if neighbour cells are occupied by a ship
    public static boolean checkNeighbourSpaces(int s, int e, int o, String[][] map, boolean horizontal) {

        if(horizontal) {
            if (s != 0) {
                if (Objects.equals(map[o][s - 1], Field.SHIP.symbol)) {
                    System.out.println("Error! Too close to another ship");
                    return false;
                }
            }
            if(e != 9) {
                if (Objects.equals(map[o][e + 1], Field.SHIP.symbol)) {
                    System.out.println("Error! Too close to another ship");
                    return false;
                }
            }
            for (int i = s; i <= e; i++) {
                if (o != 9) {
                    if (Objects.equals(map[o + 1][i], Field.SHIP.symbol)) {
                        System.out.println("Error! Too close to another ship");
                        return false;
                    }
                }
                if(o != 0) {
                    if (Objects.equals(map[o - 1][i], Field.SHIP.symbol)) {
                        System.out.println("Error! Too close to another ship");
                        return false;
                    }
                }
            }
        } else {
            if (s != 0) {
                if (Objects.equals(map[s - 1][o], Field.SHIP.symbol)) {
                    System.out.println("Error! Too close to another ship");
                    return false;
                }
            }
            if(e != 9) {
                if (Objects.equals(map[e + 1][o], Field.SHIP.symbol)) {
                    System.out.println("Error! Too close to another ship");
                    return false;
                }
            }
            for (int i = s; i <= e; i++) {
                if(o != 9) {
                    if (Objects.equals(map[i][o + 1], Field.SHIP.symbol)) {
                        System.out.println("Error! Too close to another ship");
                        return false;
                    }
                }
                if(o != 0) {
                    if (Objects.equals(map[i][o - 1], Field.SHIP.symbol)) {
                        System.out.println("Error! Too close to another ship");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    static class Ship {
        int life;

        String[] placement;
        //fills the placement array with ships coordinate
        Ship(int l, String start, String end){
            life = l;
            placement = new String[l];
            int j = 0;
            if(start.charAt(0) == end.charAt(0)) {

                int temp = Integer.parseInt(start.substring(1));
                int x;
                int y;
                if (temp < Integer.parseInt(end.substring(1))) {
                    x = temp;
                    y = Integer.parseInt(end.substring(1));
                } else {
                    x = Integer.parseInt(end.substring(1));
                    y = temp;
                }

                for (int i = x; i <= y; i++) {
                    placement[j++] = start.substring(0,1) + i;
                }
            } else {
                int temp = Char.valueByLetter(start.substring(0,1));
                int x;
                int y;
                if (temp < Char.valueByLetter(end.substring(0,1))) {
                    x = temp;
                    y = Char.valueByLetter(end.substring(0,1));
                } else {
                    x = Char.valueByLetter(end.substring(0,1));
                    y = temp;
                }
                for (int i = x; i <= y; i++) {
                    placement[j++] = Char.letterByValue(i) + start.substring(1);
                }
            }
            System.out.println(Arrays.toString(placement));
        }
        //checks if ship has been sunk
        boolean shotDown (String coordinates) {
            if (life >= 1) {
                for (int i = 0; i < placement.length; i++){
                    if (placement[i].equals(coordinates)) {
                        life--;
                        placement[i] = "";
                        if (life == 0) return true;
                    }
                }
            }

            return false;
        }
    }
    //clears field and switches player
    public static void switchPlayer(String[][] fog, String[][] field, boolean playerOneTurn){
        Scanner in = new Scanner(System.in);

        System.out.printf("%nPress Enter and pass the move to another player");
        System.out.println();
        in.nextLine();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        printMap(fog);
        System.out.println("---------------------");
        printMap(field);
        System.out.println();
        if (playerOneTurn) {
            System.out.println("Player 1, it's your turn: ");
        } else {
            System.out.println("Player 2, it's your turn: ");
        }
        in.close();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        //player 1 and 2s maps
        String[][] field = new String[10][10];
        String[][] field2 = new String[10][10];
        String[][] fog = new String[10][10];
        String[][] fog2 = new String[10][10];
        //whose turn it is
        boolean playerOneTurn = true;
        //ship specks
        int[] shipLength = {5,4,3,3,2};
        String[] shipName = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        //number of ships not sunk
        int shipsAlive = shipLength.length;
        int shipsAlive2 = shipLength.length;
        //players ships
        Ship[] ships = new Ship[shipLength.length];
        Ship[] ships2 = new Ship[shipLength.length];
        //fills maps
        for (int i = 0; i < 10; i++) {
            Arrays.fill(field[i], Field.FOG.symbol);
            Arrays.fill(field2[i], Field.FOG.symbol);
            Arrays.fill(fog[i], Field.FOG.symbol);
            Arrays.fill(fog2[i], Field.FOG.symbol);
        }

        printMap(field);
        boolean placed = false;

        System.out.println("Player 1, place your ships on the game field");
        //checks if coordinates are valid and then tries to place ship
        System.out.println();
        for (int i = 0; i < shipName.length;) {
            System.out.println("Enter the coordinates of the " + shipName[i] + " (" + shipLength[i] + " cells):\n");
            String[] input = in.nextLine().toUpperCase().split(" ");

            if(checkCoordinates(input[0],input[1],shipLength[i])) {
                placed = placeShip(input[0],input[1],field);
            }

            if(placed) {
                ships[i] = new Ship(shipLength[i], input[0], input[1]);
                printMap(field);
                placed = false;
                i++;
            }
        }

        System.out.println("Press Enter and pass the move to another player");
        in.nextLine();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Player 2, place your ships to the game field");

        for (int i = 0; i < shipName.length;) {
            System.out.println("Enter the coordinates of the " + shipName[i] + " (" + shipLength[i] + " cells):\n");
            String[] input = in.nextLine().toUpperCase().split(" ");

            if(checkCoordinates(input[0],input[1],shipLength[i])) {
                placed = placeShip(input[0],input[1],field2);
            }

            if(placed) {
                ships2[i] = new Ship(shipLength[i], input[0], input[1]);
                printMap(field2);
                placed = false;
                i++;
            }
        }

        in.close();
        //main game starts here
        while (shipsAlive != 0 && shipsAlive2 != 0) {
            Scanner test = new Scanner(System.in);
            //for switching players
            String[][] map;
            String[][] fogMap;
            String[][] mapOther;
            Ship[] boatsOther;
            if (playerOneTurn) {
                map = field;
                fogMap = fog;
                mapOther = field2;
                boatsOther = ships2;
            } else {
                map = field2;
                fogMap = fog2;
                mapOther = field;
                boatsOther = ships;
            }

            switchPlayer(fogMap,map,playerOneTurn);
            System.out.println();
            String shot = test.nextLine();
            test.close();
            int x = Char.valueByLetter(shot.substring(0,1));
            int y = Integer.parseInt(shot.substring(1));

            //checks if inside map
            if (x != -1 && y >= 1 && y <= 10) {
                System.out.println("ship1  " + shipsAlive);
                System.out.println("ship2  " + shipsAlive2);
                //checks to see if we hit or miss
                if (Objects.equals(mapOther[x][y-1], Field.SHIP.symbol)) {
                    boolean hit = false;
                    fogMap[x][y-1] = Field.HIT.symbol;
                    mapOther[x][y-1] = Field.HIT.symbol;
                    //sees if we sunk a new ship
                    for (Ship s: boatsOther) {
                        if (s.shotDown(shot)) {
                            if(playerOneTurn) {
                                shipsAlive--;
                            } else {
                                shipsAlive2--;
                            }
                            hit = true;
                            System.out.println("You sank a ship!");
                        }
                    }
                    if (!hit){
                        System.out.println("You hit a ship!");
                    }
                } else if (Objects.equals(mapOther[x][y-1], Field.FOG.symbol)) {
                    fogMap[x][y-1] = Field.MISS.symbol;
                    mapOther[x][y-1] = Field.MISS.symbol;
                    System.out.println("You missed!");

                }
            }
            //changes player
            playerOneTurn = !playerOneTurn;
        }

        System.out.println("You sank the last ship. You won. Congratulations!");
    }

}
