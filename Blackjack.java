import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Blackjack {
    private static ArrayList<String> deck;
    private static ArrayList<String> dHand;
    private static ArrayList<String> pHand;
    private static int dealerPoint = 0;
    private static int playerPoint = 0;
    private static int i = 0;
    private static String dealerHidden = null;
    private static boolean finished;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome,");
        Scanner userResp = new Scanner(System.in);
        userResp.nextLine();

        initCardSet();
        System.out.println("Shuffling Deck...");

        shuffleDeck(deck);
        finished = true;
        try{
            while (true){
                if (finished){
                    newHand();
                }
                display(dHand, pHand);

                int pCount = calcHand(pHand);
                System.out.print("Player: " + pCount);
                if (pCount > 21){
                    dealerPoint++;
                    System.out.println("\nBust!! Dealer won.");
                    finished = true;
                }

                if (pCount == 21 && dealerHidden != null){
                    System.out.println();
                    dHand.remove(dHand.size() - 1);
                    dHand.add(dealerHidden);
                    Thread.sleep(1000);
                    clearScreen();
                    dealerHidden = null;
                    continue;
                }
                if (dealerHidden == null){
                    int dCount = calcHand(dHand);
                    while (calcHand(dHand) < 17){
                        System.out.println();
                        dHand.add(deck.get(i));
                        i++;
                        display(dHand, pHand);
                        Thread.sleep(3000);
                        clearScreen();
                    }
                    System.out.printf(" Dealer: %d\n", dCount);
                    if (pCount == 21 && dCount != 21){
                        playerPoint++;
                        System.out.println("BlackJack!! Player won.");
                        finished = true;
                    } else if (pCount != 21 && dCount == 21){
                        dealerPoint++;
                        System.out.println("BlackJack!! Dealer won.");
                        finished = true;
                    } else if (dCount > 21){
                        playerPoint++;
                        System.out.println("Dealer bust! Player won!");
                        finished = true;
                    } else if (dCount < pCount){
                        playerPoint++;
                        System.out.println("Player won!");
                        finished = true;
                    } else if (dCount > pCount){
                        dealerPoint++;
                        System.out.println("Dealer won!");
                        finished = true;
                    } else if (pCount == dCount){
                        System.out.println("Tie!!");
                        finished = true;
                    } else if (pCount < 21 && dCount == 21){
                        dealerPoint++;
                        System.out.println("Dealer won!");
                        finished = true;
                    }
                }

                System.out.println();

                if (finished){
                    System.out.println("Starting a new game...");
                    System.out.printf("Score- Dealer: %d   Player: %d\n", dealerPoint, playerPoint);
                    Thread.sleep(5000);
                    clearScreen();
                    continue;
                }

                System.out.println("Would you like to Hit(hit/h) or Stand(stand/s)?");
                String answer = userResp.nextLine();
                if (!answer.equals("hit") && !answer.equals("h") && !answer.equals("stand") && !answer.equals("s")){
                    System.out.println("I don't understand. Please say \"hit/h\" or \"stand/s\"");
                } else if (answer.equals("hit") || answer.equals("h")){
                    pHand.add(deck.get(i));
                    i++;

                } else if (answer.equals("stand") || answer.equals("s")){
                    if (dealerHidden != null){
                        dHand.remove(dHand.size() - 1);
                        dHand.add(dealerHidden);
                        dealerHidden = null;
                        Thread.sleep(3000);
                        clearScreen();
                    }
                    display(dHand, pHand);
                    Thread.sleep(3000);
                    clearScreen();

                    while (calcHand(dHand) < 17){
                        dHand.add(deck.get(i));
                        i++;
                        display(dHand, pHand);
                        Thread.sleep(3000);
                        clearScreen();
                    }

                }
            }
        }catch (IndexOutOfBoundsException e){
            System.out.println("\nDeck ran out!!!!!!!!!");
        }



    }

    public static int calcHand(ArrayList<String> hand){
        int countA = 0;
        int sum = 0;
        for (String s : hand){
            String hold = s.substring(1);
            if (isNumber(hold)){
                sum += Integer.parseInt(hold);
            } else {
                if (hold.equals("A")){
                    countA++;
                } else {
                    sum += 10;
                }
            }
        }
        if (countA != 0){
            for (int i = 0; i < countA; i++){
                if (sum + 11 > 21){
                    sum += 1;
                } else {
                    sum += 11;
                }
            }
        }
        return sum;
    }

    public static boolean isNumber(String s){
        if (s.equals("K") || s.equals("Q") || s.equals("J") || s.equals("A")){
            return false;
        }
        return true;
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void newHand(){
        pHand = new ArrayList<>();
        dHand = new ArrayList<>();


        pHand.add(deck.get(i));
        i++;
        dHand.add(deck.get(i));
        i++;
        pHand.add(deck.get(i));
        i++;
        dHand.add("?");
        dealerHidden = deck.get(i);
        i++;

        finished = false;
    }

    public static void display(ArrayList<String> dealerHand, ArrayList<String> playerHand){
        StringBuilder s = new StringBuilder();
        StringBuilder e = new StringBuilder();
        e.append("  ");
        for (int j = 0; j < dealerHand.size() - 1; j++){
            s.append("  ");
        }
        for (int i = 0; i < dealerHand.size(); i++){
            System.out.printf("|  " + s + "%s" + e + "\n", dealerHand.get(i));
            s.delete(0, 2);
            e.append("  ");
        }

        System.out.println("|------------------");

        s = new StringBuilder();
        e = new StringBuilder();
        e.append("  ");
        for (int j = 0; j < playerHand.size() - 1; j++){
            s.append("  ");
        }

        for (int i = 0; i < playerHand.size(); i++){
            System.out.printf("|  " + s + "%s" + e + "\n", playerHand.get(i));
            s.delete(0, 2);
            e.append("  ");
        }

        System.out.println("\n\n");
    }

    public static void initCardSet(){
        deck = new ArrayList<>();

        for (int decks = 0; decks < 8; decks++){
            deck.add("♠A");
            deck.add("♡A");
            deck.add("♣A");
            deck.add("♢A");
            for (int i = 2; i < 11; i++){
                deck.add("♠" + i);
                deck.add("♡" + i);
                deck.add("♣" + i);
                deck.add("♢" + i);
            }
            deck.add("♠K");
            deck.add("♡K");
            deck.add("♣K");
            deck.add("♢K");
            deck.add("♠Q");
            deck.add("♡Q");
            deck.add("♣Q");
            deck.add("♢Q");
            deck.add("♠J");
            deck.add("♡J");
            deck.add("♣J");
            deck.add("♢J");
        }

    }

    public static void shuffleDeck(ArrayList a){
        Collections.shuffle(a);
        i = 0;
    }


}
