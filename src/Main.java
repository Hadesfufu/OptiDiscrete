public class Main {


    public static void main(String[] args) {
        Application a = new Application();
        a.load();
        //a.generateBaseSolution();
        //a.localMin();
        //a.taboo();
        a.algoGenetique(5);
    }
}
