import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Application a = new Application();
        a.load();

        int choice = menu();

        int generationChoice = generationBaseSolutionChoice();

        switch (choice) {
            case 1:
                switch (generationChoice) {
                    case 1:
                        a.generateBaseSolutionWithMinimalRoads();
                        break;
                    case 2:
                        a.generateBaseSolutionWithMaximalRoads();
                        break;
                    case 3:
                        a.generateBaseSolutionRandom();
                        break;
                }
                a.taboo(2000, 30);
                break;
            case 2:
                a.algoGenetique(10, 2000);
                break;
            default:
                break;
        }
    }

    private static int generationBaseSolutionChoice() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("Choix de la génération de la solution initiale");
        System.out.println("-------------------------\n");
        System.out.println("1 - Solution avec un nombre minimum de routes (optimisée)");
        System.out.println("2 - Solution avec un nombre maximal de routes (non optimisée)");
        System.out.println("3 - Solution aléatoire");

        selection = input.nextInt();
        return selection;
    }

    public static int menu() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("Projet d'optimisation discrète");
        System.out.println("-------------------------\n");
        System.out.println("0 - Quitter");
        System.out.println("1 - Taboo");
        System.out.println("2 - Algorithme génétique");

        selection = input.nextInt();
        return selection;
    }
}