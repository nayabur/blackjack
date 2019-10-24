import java.util.Random;
import java.util.Scanner;

public class Blackjack {
    
    static Scanner keyboard = new Scanner(System.in);
    static Random  random   = new Random();
    
    public static void main(String [] args) {
	
	// Create a new array to store the 52-card deck.
	int[] deck = new int[52];
	initializeDeck(deck);
	
	// START YOUR CODE HERE.  WRITE HELPER METHODS SO THAT THE	
	// CODE RIGHT HERE IN main() REMAINS SIMPLE.
	shuffle(deck);
	int money = 100;
	while (money > 0) {
	    int wager = placeWager(money);
	    int[] playersCards = new int [9];
	    int[] dealersCards = new int [9];
	    initializeCards(playersCards, dealersCards);
	    reshuffleDeck(deck); 
	    int playerSum = dealPlayer(deck, playersCards);
	    int dealerSum = dealDealer(deck, dealersCards);
	    playerSum = player(deck, playersCards, playerSum);
	    dealerSum = dealer(deck, dealersCards, dealerSum, playerSum);
	    int whoWon = whoLost(dealerSum, playerSum);
	    money = makeMoney(wager, money, whoWon);
	    System.out.println();
	    }
	System.out.println("YOU HAVE NO MONEY LEFT");
	System.out.println("GAME OVER");
    }
    
    // Set up the deck to start with cards in-order.
    // 0-12 represent Clubs, 13-25 Diamonds, 26-38 Hearts, 39-52 Spades.
    // Within each suit, the first number represents A, then 2-10, then J,Q,K  
    public static void initializeDeck (int[] deck) {
	for (int i = 0; i < deck.length; i = i + 1) {
	    deck[i] = i;
	}
    }
    
    // shuffles deck
    public static int[] shuffle (int[] deck) {
	for (int i= deck.length-1; ((i < deck.length) && (i>=0)) ; i= i-1){
	    int j = random.nextInt(i+1);
	    int tempStore = deck [i];
	    deck[i] = deck[j];
	    deck[j] = tempStore;
	}
	return deck;
    }
    
    // set up player's cards
    // filled with -1 as place holder
    // -1 allows print() and sum() to stop
    public static void initializeCards(int[] playersCards, int[] dealersCards) {
	for (int i = 0; (i < playersCards.length); i = i + 1) {
	    playersCards[i] = -1;
	    dealersCards[i] = -1;
	}
    }
    
    // if there is less than 20 cards, deck will reset and shuffle
    public static void reshuffleDeck (int[] deck) {
	int i = 0;
	while (i < deck.length) { // loop will stop when (i = first card available)
	    if (deck[i] != -1){
		break;
	    }
	    i = i+1;
	}
	if ((deck.length-i) <= 20){ // (deck.length-i) is cards remaining
	    System.out.println("shuffling...");
	    initializeDeck(deck);
	    shuffle(deck);
	}
    }
    
    // deals player their first 2 cards
    public static int dealPlayer (int [] deck, int[] playersCards) {
	int i = 0;
	System.out.println("Player's Cards: ");
	while (i < 2) {
	    playersCards[i] = dealOne(deck);
	    printCard(playersCards[i]);
	    i = i + 1;
	}
	int playerSum = sumCards(playersCards);
	return playerSum;
    }

    // deals dealer their first 2 cards
    public static int dealDealer (int [] deck, int[] dealersCards) {
	int i = 0;
	while (i < 2) {
	    dealersCards[i] = dealOne(deck);
	    i = i + 1;
	}
	System.out.println("Dealer's Cards: ");
	System.out.println("\t(Hidden)"); 
	printCard(dealersCards[i-1]);
	int dealerSum = sumCards(dealersCards);
	return dealerSum;
    }
    
    // player plays out their hand
    // until they bust or stop
    // returns sum of their cards
    public static int player (int [] deck, int[] playersCards, int playerSum) {
	int i = 2; // carried over from dealPlayer()
	while (playerSum < 21) {
	    System.out.println("Your Options:");
	    System.out.println("\t(1) hit");
	    System.out.println("\t(2) stay");
	    System.out.print("Enter your choice: ");
	    int choice = keyboard.nextInt();
	    
	    if (choice == 1) {
		System.out.println("\tHIT");
		playersCards[i] = dealOne(deck);
		i = i + 1;		
		System.out.println("Player's Cards: ");
		print(playersCards);
		playerSum = sumCards(playersCards);
	    } else {
		System.out.println("\tSTAY");
		break;
	    }
	}
	return playerSum;
	
}
    
    // dealer plays out their hand
    // until they bust or reach at least 17
    // returns sum of their cards
    public static int dealer (int [] deck, int[] dealersCards, int dealerSum, int playerSum) {
	System.out.println("Dealer's Cards: ");
	print(dealersCards);
	dealerSum = sumCards(dealersCards);
	int i = 2; // carried over from dealDealer()
	while ((dealerSum < 17) && (playerSum<=21)) { // does not draw more if player busted
	    dealersCards[i] = dealOne(deck);	
	    printCard(dealersCards[i]);
	    i = i + 1;	
	    dealerSum = sumCards(dealersCards);
	}
	return dealerSum;	
    }
    
    // determines who lost
    public static int whoLost (int dealerSum, int playerSum) {
	if (dealerSum > 21) { // player won
	    System.out.println("DEALER BUSTED");
	    return 1;
	} if (playerSum > 21) { // dealer won
	    System.out.println("PLAYER BUSTED");
	    return -1;
	} else if (dealerSum > playerSum) { // dealer won
	    return -1;
	} else if (dealerSum < playerSum) { // player won
	    return 1;
	} else { // (dealerSum == playerSum)
	    System.out.println("PUSH");
	    return 0; 
	}
    }
    
    // prints winner
    // distributes money based on who won
    public static int makeMoney (int wager, int money, int whoWon) {
	if (whoWon == -1) {
	    System.out.println("DEALER WON");
	}
	if (whoWon == 1) {
	    System.out.println("PLAYER WON");
	}
	if (whoWon == 0) {	
	    System.out.println("NO WINNER");
	}
	money = money + (whoWon * wager);
	return money;
    }
    
    // takes player's wagers
    public static int placeWager(int money) {
	System.out.println("You have $" + money);
	System.out.print("Place your wager: ");
	int wager = keyboard.nextInt();
	while ((wager < 1) || (wager > money)) {
	    System.out.println("Your wager must be between $1 and $" + money);
	    System.out.print("Place your wager: ");
	    wager = keyboard.nextInt();
	}
	return wager;
    }
    
    // deals one card from deck starting at index 0
    public static int dealOne (int[] deck) {
	int topCard = -1;
	for (int i = 0; (i < deck.length); i = i + 1) {
	    if (deck[i] != -1) {
		topCard = deck[i];
		deck[i] = -1; // when a card is used it is replaces with -1
		break;
	    }
	}
	return topCard;
    }

    // Prints an array of cards (could be a hand, could be a deck).
    public static void print (int[] cards) {
	for (int i = 0; (i < cards.length) && (cards[i] != -1); i = i + 1) {
	    printCard(cards[i]);
	}
    }
    
    // sums value of hand
    public static int sumCards (int[] cards) {
	int sum = 0;
	for (int i = 0; ((i < cards.length) && (cards[i] != -1)); i = i + 1) {
	    sum = sum + getValue(cards[i]);
	}
	return sum;
    }
    
    // Prints a card, e.g., "Three of Spades"
    public static void printCard (int card) {
	System.out.println("\t" + getRank(card) + " of " + getSuit(card));
    }
    
    
    // Return the rank of a card (2-10,J,Q,K,A), given the numeric
    // representation of the card
    public static String getRank (int card) {
	String[] ranks = new String[] { "Ace",
				        "Two",
				        "Three",
				        "Four",
				        "Five",
				        "Six",
				        "Seven",
				        "Eight",
				        "Nine",
				        "Ten",
				        "Jack",
				        "Queen",
				        "King" };
	return ranks[card % 13];
    }
    

    // Return the value of a card given the numeric representation of the card
    public static int getValue (int card) {
	int[] value = new int[] { 11,
				  2,
				  3,
				  4,
				  5,
				  6,
				  7,
				  8,
				  9,
				  10,
				  10,
				  10,
				  10 };
	return value[card % 13];
    }
    
    
    // Returns the suit of a card, given the numeric representation of
    // the card
    public static String getSuit (int card) {
	String[] suits = new String[] { "spades",
				        "hearts",
				        "clubs",
				        "diamonds" };
	return suits[card / 13];
    }
}

