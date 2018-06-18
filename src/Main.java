import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Application a = new Application();
        a.load(dataChoice());

        int choice = menu();

        switch (choice) {
            case 1:
                int generationChoice = generationBaseSolutionChoice();
                int methodChoice = tabooMethodChoice();
                boolean move = methodChoice != 2;
                boolean swap = methodChoice != 1;
                int tabooTries = tabooTriesChoice();
                int tabooSize = tabooSizeChoice();

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

                System.out.println("----Lancement de Taboo");
                System.out.println("    ...");
                a.taboo(tabooTries, tabooSize, move, swap);
                break;
            case 2:
                int populationSize = geneticPopulationSizeChoice();
                int tries = geneticTriesChoice();
                int geneticMethodChoice = geneticMethodChoice();
                float geneticRate = geneticRateChoice();

                System.out.println("----Lancement de l'algorithme génétique");
                a.algoGenetique(populationSize, tries, geneticMethodChoice, geneticRate);
                break;
            default:
                break;
        }
    }

    private static float geneticRateChoice() {
        float selection;
        Scanner input = new Scanner(System.in);

        System.out.println("\nEntrez le taux de mutation (entre 0 et 1) : ");

        selection = input.nextFloat();
        return selection;
    }

    private static int geneticMethodChoice() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("\nChoix de la méthode de croisement");
        System.out.println("-------------------------");

        System.out.println("1 - Mix de la séquence génétique");
        System.out.println("2 - Coupage de la séquence génétique");


        selection = input.nextInt();
        return selection;
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

        System.out.println("\nChoix de la génération de la solution initiale");
        System.out.println("-------------------------");
        System.out.println("1 - Solution avec un nombre minimum de routes (optimisée)");
        System.out.println("2 - Solution avec un nombre maximal de routes (non optimisée)");
        System.out.println("3 - Solution aléatoire");

        selection = input.nextInt();
        return selection;
    }

    private static String dataChoice() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("Choix du jeu de données");
        System.out.println("-------------------------");
        System.out.println("1 - Jeu de données 1");
        System.out.println("2 - Jeu de données 2");
        System.out.println("3 - Jeu de données 3");

        selection = input.nextInt();

        String data = "data01.txt";
        if(selection == 2) data = "data02.txt";
        if(selection == 3) data = "data03.txt";

        return data;
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