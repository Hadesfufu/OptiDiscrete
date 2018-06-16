public class Main {


    public static void main(String[] args) {
        Application a = new Application();
        a.load();
        a.generateBaseSolution();
        //a.localMin();
        a.taboo(2000, 30);
        //a.algoGenetique(10, 2000);
    }
}
