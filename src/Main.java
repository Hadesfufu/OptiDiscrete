public class Main {


    public static void main(String[] args) {
        Application a = new Application();
        a.load();
        //a.localMin();
        /*Double distance;
        do {
            a.generateBaseSolution();
            distance= a.taboo(2000, 30);
        }while (distance > 800);*/
        a.algoGenetique(10, 2000);
    }
}
