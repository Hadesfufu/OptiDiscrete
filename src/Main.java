public class Main {


    public static void main(String[] args) {
        Application a = new Application();
        a.load();
        a.generateBaseSolution();
        //a.Taboo();
        //a.localMin();
        a.taboo();
    }
}
