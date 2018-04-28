import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Application {

    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Route> routes = new ArrayList<>();
    private HashMap<String, Solution> solutionsAlreadyExplored = new HashMap<>();
    private Solution bestSolution;
    private Solution lastSolution;
    private Solution baseSolution;
    private Double distances[][];
    private Client root;

    public Application() {

    }

    public void load() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/data01.txt"));
            String line = br.readLine();
            line = br.readLine();
            root = new Client(line);
            clients.add(root);
            line = br.readLine();

            while (line != null) {
                clients.add(new Client(line));
                line = br.readLine();
            }

            distances = new Double[clients.size()][clients.size()];
            for (Client c : clients) {
                for (Client d : clients) {
                    distances[c.getI()][d.getI()] = c.distance(d);
                }
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateBaseSolution() {
        ArrayList<Client> nonadded = clients;
        nonadded.remove(root);
        Route currentRoute;
        //System.out.println("DEBUT !");
        while (!nonadded.isEmpty()) {
            //System.out.println("DEBUT ROUTE!");
            currentRoute = new Route();
            routes.add(currentRoute);
            currentRoute.add(root);
            boolean cont = true;
            while (cont) {
                Client newClient = getVoisinageLess(root, currentRoute.getLast(), nonadded, currentRoute);
                if (newClient != null) {
                    currentRoute.add(newClient);
                    nonadded.remove(newClient);
                } else
                    cont = false;
            }
            //System.out.println("FINI ROUTE!");
            currentRoute.add(root);
        }
        bestSolution = new Solution(routes);
        lastSolution = bestSolution;
        baseSolution = bestSolution;
        solutionsAlreadyExplored.put(lastSolution.serialize(), new Solution(lastSolution));
        //System.out.println(bestSolution.getSommeDistance(root, distances));
        //System.out.println("FINI !");
    }

    public Client getVoisinageLess(Client root, Client origin, ArrayList<Client> ar, Route r) {
        Client good = null;
        Double d = 10000000000000.d;
        for (Client c : ar) {
            Double distance = distances[origin.getI()][c.getI()] + ((r.getCharge() / Route.getMaxCharge()) * distances[root.getI()][c.getI()]);
            if (distance < d && r.isChargeOk(c)) {
                d = distances[root.getI()][c.getI()];
                good = c;
            }
        }
        return good;
    }

    public void Taboo() {
        Random r = new Random();
        System.out.println("DEBUT TABOO");
        int nbEnd = 0;
        Integer i = 10;
        while (nbEnd < 50) {
            lastSolution = new Solution(lastSolution);
            Route r1;
            if (i == 0)
                r1 = lastSolution.getRoutes().get(r.nextInt(lastSolution.getRoutes().size() - 1));
            else
                r1 = lastSolution.getLightestRoute();
            Route r2 = r1;
            Integer r2i = 0;
            while (r2 == r1) {
                r2i = r.nextInt(lastSolution.getRoutes().size() - 1);
                r2 = lastSolution.getRoutes().get(r2i);
            }
            Client c1;
            Client c2;
            boolean done = false;
            i = 10;
            do {
                //System.out.println(i);
                //System.out.println("new Generation");
                int r1Size = r1.getRoute().size();
                Integer c1i = r1Size == 3 ? 1 : r.nextInt(r1Size - 2 - 1) + 1;
                c1 = r1.getRoute().get(c1i);
                int r2Size = r2.getRoute().size();
                Integer c2i = r2Size == 3 ? 1 : r.nextInt(r2Size - 2 - 1) + 1;
                c2 = r2.getRoute().get(c2i);
                //System.out.println(c1i + " " + c2i);
                r1.remove(c1);
                r2.remove(c2);
                if (r1.isChargeOk(c2) && r2.isChargeOk(c1)) {
                    done = true;
                    r1.add(c1i, c2);
                    r2.add(c2i, c1);
                } else {
                    if (!r1.add(c1i, c1))
                        System.out.println("BUG avec les index sur r1");
                    if (!r2.add(c2i, c2))
                        System.out.println("Bug avec les" +
                                " index sur r2");
                }
                i--;
            } while (!done && i > 0);
            if (solutionsAlreadyExplored.get(lastSolution.serialize()) != null && i > 0) {
                nbEnd++;
            } else {
                nbEnd = 0;
                if (lastSolution.isBetterThan(bestSolution, root, distances))
                    bestSolution = lastSolution;
                solutionsAlreadyExplored.put(lastSolution.serialize(), lastSolution);
                if (lastSolution.getIndex() % 20000 == 0)
                    lastSolution.display(root, distances);
            }
        }
        System.out.println("BASE SOLUTION:");
        baseSolution.display(root, distances);
        System.out.println("BEST SOLUTION:");
        bestSolution.display(root, distances);
    }


    public void descente() {
        Solution LocalBest;
        Random r = new Random();
        Boolean finished = false;
        Solution oldSolution = lastSolution;
        HashMap<String, Solution> solutionsAlreadyExplored = new HashMap<>();
        LocalBest = lastSolution;
        list.add(lastSolution);
        while(!list.isEmpty()) {
            oldSolution = list.poll();
            int fails = 0;
            while(fails < 10){
                Solution newSolution = new Solution(oldSolution);
                Route r1;
                r1 = newSolution.getRoutes().get(r.nextInt(newSolution.getRoutes().size() - 1));
                Route r2 = r1;
                Integer r2i = 0;
                while (r2 == r1) {
                    r2i = r.nextInt(newSolution.getRoutes().size() - 1);
                    r2 = newSolution.getRoutes().get(r2i);
                }
                Client c1;
                Client c2;
                boolean done = false;
                Integer i = 10;
                do {
                    //System.out.println(i);
                    //System.out.println("new Generation");
                    int r1Size = r1.getRoute().size();
                    Integer c1i = r1Size == 3 ? 1 : r.nextInt(r1Size - 2 - 1) + 1;
                    c1 = r1.getRoute().get(c1i);
                    int r2Size = r2.getRoute().size();
                    Integer c2i = r2Size == 3 ? 1 : r.nextInt(r2Size - 2 - 1) + 1;
                    c2 = r2.getRoute().get(c2i);
                    //System.out.println(c1i + " " + c2i);
                    r1.remove(c1);
                    r2.remove(c2);
                    if (r1.isChargeOk(c2) && r2.isChargeOk(c1)) {
                        done = true;
                        r1.add(c1i, c2);
                        r2.add(c2i, c1);
                    } else {
                        if (!r1.add(c1i, c1))
                            System.out.println("BUG avec les index sur r1");
                        if (!r2.add(c2i, c2))
                            System.out.println("Bug avec les" +
                                    " index sur r2");
                    }
                    i--;
                } while (!done && i > 0);
                if(i == 0)
                    fails++;
                else {
                    fails = 0;
                    list.addLast(newSolution);
                    System.out.println("new Add in list" + newSolution.getIndex());
                    if(newSolution.isBetterThan(LocalBest, root, distances))
                        LocalBest = newSolution;
                }
            }
        }
        LocalBest.display(root, distances);
    }

}
