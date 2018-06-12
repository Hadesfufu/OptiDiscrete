import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("Duplicates")

public class Application {

    public ArrayList<Client> clients = new ArrayList<>();
    public ArrayList<Route> routes = new ArrayList<>();
    public Solution baseSolution;
    public Double distances[][];
    public int poidTotal = 0;
    public Client root;

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
                poidTotal+=c.getQ();
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
        baseSolution = new Solution(routes);
        System.out.println("Base solution :");
        baseSolution.display(root, distances);
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
/*
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
    */

    public void localMin(){
        Solution lastRoundBestSolution;
        Solution currentBestSolution = baseSolution;
        int i = 0;
        do{
            lastRoundBestSolution = currentBestSolution;
            ArrayList<Solution> allNeighbors = generateAllNeighborsByMove(currentBestSolution);
            for(Solution s: allNeighbors){
                if(s.getSommeDistance(root, distances) < currentBestSolution.getSommeDistance(root, distances))
                    currentBestSolution = s;
            }
            i++;
        }while(lastRoundBestSolution != currentBestSolution);
        System.out.println("Minimum local atteint aprés " + i + " essais");
        currentBestSolution.display(root, distances);
    }

    public void taboo(){
        //HashMap<String, Solution> taboo = new HashMap<>();
        ArrayList<String> taboo = new ArrayList<>();
        Solution globalBestSolution = baseSolution;
        Solution currentBestSolution = baseSolution;
        Solution bestCandidat;
        Random r = new Random();
        int i = 0;
        while(taboo.size() < 500) {
            taboo.add(currentBestSolution.serialize());
            ArrayList<Solution> allNeighbors = generateAllNeighborsByMove(currentBestSolution);
            /////////
            ArrayList<Solution> bonus = generateAllNeighborsBySwap(currentBestSolution);
            for(Solution s: bonus){
                allNeighbors.add(s);
            }
            /////////
            bestCandidat = null;
            int stopper = 0;
            for (Solution s : allNeighbors) {
                if(bestCandidat == null){
                    if(!taboo.contains(s.serialize())) {
                        bestCandidat = s;
                    }
                }
                else if (!taboo.contains(s.serialize()) && (Double.compare(s.getSommeDistance(root, distances), bestCandidat.getSommeDistance(root, distances)) < 0 )){ // peu pas reselect best
                    bestCandidat = s;
                //    System.out.println(s.serialize());
                }
                if(taboo.contains(s.serialize()))
                    stopper++;
            }
            if(stopper == taboo.size() && stopper == allNeighbors.size()) {
                System.out.println("ALL SOLUTIONS EXPLORED ! stopper : " + stopper + " taboo list : " + taboo.size() + " neighbors :" + allNeighbors.size());
                break;
            }
            if(bestCandidat == null){
                globalBestSolution.display(root, distances);
                System.out.println("EXCEPTION INCOMING ! stopper : " + stopper + " taboo list : " + taboo.size() + " neighbors :" + allNeighbors.size());
                System.out.println("place break here");
            }
            //bestCandidat.display(root, distances);

            /*System.out.println("candidat    " + bestCandidat.serialize());
            bestCandidat.display(root, distances);
            System.out.println("current best" + currentBestSolution.serialize());
            currentBestSolution.display(root, distances);
            System.out.println(taboo.contains(bestCandidat.serialize()));*/

            currentBestSolution = bestCandidat;
            if(currentBestSolution.getSommeDistance(root, distances) < globalBestSolution.getSommeDistance(root, distances)){
                globalBestSolution = currentBestSolution;
            }

            /*if(lastRoundBestSolution == currentBestSolution){
                if(currentBestSolution.getSommeDistance(root, distances) < globalBestSolution.getSommeDistance(root, distances))
                    globalBestSolution = currentBestSolution;
                taboo.put(currentBestSolution.serialize(), currentBestSolution);
                System.out.println(taboo.size());
                currentBestSolution = null;
                for (Solution s : allNeighbors) {
                    if(taboo.get(s.serialize()) != null){
                        if (currentBestSolution == null)
                            currentBestSolution = s;
                        else if (s.getSommeDistance(root, distances) < currentBestSolution.getSommeDistance(root, distances))
                            currentBestSolution = s;
                    }
                }
            }*/

            i++;
            //currentBestSolution.display(root, distances);
        }
        System.out.println("Minimum local atteint aprés " + i + " essais");
        globalBestSolution.display(root, distances);
    }

    public ArrayList<Solution> generateAllNeighborsBySwap(Solution s){
        ArrayList<Solution> result = new ArrayList<>();
        ArrayList<Route> routes = s.getRoutes();
        for(int ir = 0; ir < routes.size(); ir++){
            Route route1 = routes.get(ir);
            for(int jr = ir+1; jr < routes.size(); jr++){
                Route route2 = routes.get(jr);
                for(int ic = 1; ic < route1.getRoute().size()-1; ic++) {
                    Client c1 = route1.getRoute().get(ic);
                    for (int jc = 1; jc < route2.getRoute().size()-1; jc++) {
                        Client c2 = route2.getRoute().get(jc);
                        route1.remove(c1);
                        route2.remove(c2);
                        if (route1.isChargeOk(c2) && route2.isChargeOk(c1)) {
                            Solution news = new Solution(baseSolution);
                            news.getRoutes().get(ir).addAtInteger(ic,c2);
                            news.getRoutes().get(jr).addAtInteger(jc,c1);
                            result.add(news);
                        }
                        if (!route1.addAtInteger(ic, c1))
                            System.out.println("BUG avec les index sur r1");
                        if (!route2.addAtInteger(jc, c2))
                            System.out.println("Bug avec les index sur r2");
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Solution> generateAllNeighborsByMove(Solution s){
        ArrayList<Solution> result = new ArrayList<>();
        ArrayList<Route> routes = s.getRoutes();
        Random r = new Random();
        for(int ir = 0; ir < routes.size(); ir++) {
            Route route1 = routes.get(ir);
            for (int ic = 1; ic < route1.getRoute().size() - 1; ic++) {
                Client c1 = route1.getRoute().get(ic);
                for (int jr = ir + 1; jr < routes.size(); jr++) {
                    Route route2 = routes.get(jr);
                    if (route2.isChargeOk(c1)) {
                        for (int jc = 1; jc < route2.getRoute().size() - 1; jc++) {
                            Solution news = new Solution(baseSolution);
                            news.getRoutes().get(ir).remove(c1);
                            if(news.getRoutes().get(jr).addAtInteger(jc, c1) == false) {
                                System.out.println(news.getRoutes().get(jr).getCharge());
                                System.out.println(route2.getCharge());
                                System.out.println(c1.getQ());
                            }
                            result.add(news);
                            if(news.globalWeight() != 410)
                                System.out.println("SOUCIS " + news.getIndex());
                        }
                    }
                }
            }
        }
        return result;
    }

    public void algoGenetique(int population){
        //generation aletaoire de base
        ArrayList<Solution> baseGeneration = new ArrayList<>();
        ArrayList<Solution> newGeneration = new ArrayList<>();
        ArrayList<Double>   proba = new ArrayList<>();

        double sommefitness = 0.d;
        for(int i = 0; i < population; i++){
            baseGeneration.add(Solution.generateRandom(this));
            sommefitness+=baseGeneration.get(i).getSommeDistance(root, distances);
        }
        for(int i = 0; i < population; i++){
            proba.add(baseGeneration.get(i).getSommeDistance(root, distances)/sommefitness);
        }

        //faire la proba (n-fitness / sumfitness)
        //generer une nouvelle generation
    }
}
