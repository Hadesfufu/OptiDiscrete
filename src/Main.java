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
                int populationSize = geneticPopulationSizeChoice();
                int tries = geneticTriesChoice();

                System.out.println("\n----Lancement de l'algorithme génétique");
                a.algoGenetique(populationSize, tries);
                break;
            default:
                break;
        }
    }

    private static int geneticPopulationSizeChoice() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("\nEntrez la taille de la population : ");

        selection = input.nextInt();
        return selection;
    }

    private static int geneticTriesChoice() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("\nEntrez le nombre d'itérations : ");

        selection = input.nextInt();
        return selection;
    }

    private static int tabooMethodChoice() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("\nChoix de la méthode de génération des voisins");
        System.out.println("-------------------------");
        System.out.println("1 - Move : Echange deux clients de deux routes différentes");
        System.out.println("2 - Swap : Déplacement d'un client dans une route aléatoire");
        System.out.println("3 - Move et Swap");


        selection = input.nextInt();
        return selection;
    }

    private static int tabooTriesChoice() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("\nEntrez le nombre d'essais : ");

        selection = input.nextInt();
        return selection;
    }

    private static int tabooSizeChoice() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("\nEntrez la taille de la liste : ");

        selection = input.nextInt();
        return selection;
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

        System.out.println("\nChoix de la métaheuristique");
        System.out.println("-------------------------");
        System.out.println("1 - Taboo");
        System.out.println("2 - Algorithme génétique");

        selection = input.nextInt();
        return selection;
    }
}