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

    public void load(String data) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/" + data));
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
                poidTotal += c.getQ();
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateBaseSolutionWithMinimalRoads() {
        ArrayList<Client> nonadded = clients;
        nonadded.remove(root);
        Route currentRoute;
        while (!nonadded.isEmpty()) {
            currentRoute = new Route();
            routes.add(currentRoute);
            boolean cont = true;
            while (cont) {
                Client newClient;
                if (currentRoute.isEmpty())
                    newClient = getVoisinageLess(root, root, nonadded, currentRoute);
                else
                    newClient = getVoisinageLess(root, currentRoute.getLast(), nonadded, currentRoute);
                if (newClient != null) {
                    currentRoute.add(newClient);
                    nonadded.remove(newClient);
                } else
                    cont = false;
            }
            currentRoute.add(root);
        }
        baseSolution = new Solution(routes);
        printBaseSolution();
    }

    public void generateBaseSolutionRandom() {
        baseSolution = Solution.generateRandom(this);
        printBaseSolution();
    }

    public void generateBaseSolutionWithMaximalRoads() {
        ArrayList<Client> nonadded = clients;
        nonadded.remove(root);
        Route currentRoute;
        currentRoute = new Route();
        routes.add(currentRoute);
        currentRoute.add(root);

        for (Client c : clients) {
            currentRoute = new Route();
            routes.add(currentRoute);
            currentRoute.add(c);
        }

        baseSolution = new Solution(routes);
        printBaseSolution();
    }

    public void printBaseSolution() {
        System.out.println("\n----Solution de base ");
        baseSolution.displayWithoutSerialize(root, distances);
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

    public Double taboo(int nbIteration, int tabooSize, boolean move, boolean swap) {
        ArrayList<String> taboo = new ArrayList<>();
        Solution globalBestSolution = baseSolution;
        Solution currentBestSolution = baseSolution;
        Solution bestCandidat = baseSolution;

        int i = 0;
        while (i < nbIteration && bestCandidat != null) {
            ArrayList<Solution> allNeighbors = new ArrayList<>();
            if (move) {
                allNeighbors.addAll(generateAllNeighborsByMove(bestCandidat));
            }
            if (swap) {
                ArrayList<Solution> swapNeighbors = generateAllNeighborsBySwap(bestCandidat);
                allNeighbors.addAll(swapNeighbors);
            }
            bestCandidat = null;
            int stopper = 0;
            Boolean containedInTaboo;
            for (Solution s : allNeighbors) {
                containedInTaboo = taboo.contains(s.serialize());
                if (bestCandidat == null) {
                    if (!containedInTaboo) {
                        bestCandidat = s;
                    }
                } else if (!containedInTaboo && (Double.compare(s.getSommeDistance(root, distances), bestCandidat.getSommeDistance(root, distances)) < 0)) { // peu pas reselect best
                    bestCandidat = s;
                }
                if (containedInTaboo)
                    stopper++;
            }

            if (stopper == taboo.size() && stopper == allNeighbors.size()) {
                System.out.println("ALL SOLUTIONS EXPLORED ! stopper : " + stopper + " taboo list : " + taboo.size() + " neighbors :" + allNeighbors.size());
                break;
            }
            if (bestCandidat == null) {
                globalBestSolution.display(root, distances);
                System.out.println("EXCEPTION INCOMING ! stopper : " + stopper + " taboo list : " + taboo.size() + " neighbors :" + allNeighbors.size());
                System.out.println("place break here");
            } else {
                if (bestCandidat.getSommeDistance(root, distances) < currentBestSolution.getSommeDistance(root, distances)) {
                    if (currentBestSolution.getSommeDistance(root, distances) < globalBestSolution.getSommeDistance(root, distances)) {
                        globalBestSolution = currentBestSolution;
                    }
                } else {
                    taboo.add(bestCandidat.serialize());
                    if (taboo.size() > tabooSize) {
                        taboo.remove(taboo.get(0));
                    }
                }
                currentBestSolution = bestCandidat;
            }

            i++;
        }
        System.out.println("----Solution finale ");
        globalBestSolution.displayWithoutSerialize(root, distances);
        return globalBestSolution.getSommeDistance(root, distances);
    }

    public ArrayList<Solution> generateAllNeighborsBySwap(Solution s) {
        ArrayList<Solution> result = new ArrayList<>();
        ArrayList<Route> routes = s.getRoutes();
        for (int ir = 0; ir < routes.size(); ir++) {
            Route route1 = routes.get(ir);
            for (int jr = ir + 1; jr < routes.size(); jr++) {
                Route route2 = routes.get(jr);
                for (int ic = 1; ic < route1.getClients().size() - 1; ic++) {
                    Client c1 = route1.getClients().get(ic);
                    for (int jc = 1; jc < route2.getClients().size() - 1; jc++) {
                        Client c2 = route2.getClients().get(jc);
                        route1.remove(c1);
                        route2.remove(c2);
                        if (route1.isChargeOk(c2) && route2.isChargeOk(c1)) {
                            Solution news = Solution.clone(s);
                            news.getRoutes().get(ir).addAtInteger(ic, c2);
                            news.getRoutes().get(jr).addAtInteger(jc, c1);
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

    public ArrayList<Solution> generateAllNeighborsByMove(Solution s) {
        ArrayList<Solution> result = new ArrayList<>();
        ArrayList<Route> routes = s.getRoutes();
        for (int ir = 0; ir < routes.size(); ir++) {
            Route route1 = routes.get(ir);
            for (int ic = 0; ic < route1.getClients().size(); ic++) {
                Client c1 = route1.getClients().get(ic);
                for (int jr = 0; jr < routes.size(); jr++) {
                    Route route2 = routes.get(jr);
                    if (route2.isChargeOk(c1)) {
                        for (int jc = 0; jc < route2.getClients().size(); jc++) {
                            Solution news = Solution.clone(s);
                            news.getRoutes().get(ir).remove(c1);
                            news.getRoutes().get(jr).addAtInteger(jc, c1);
                            if (news.getRoutes().get(ir).isEmpty()) {
                                news.getRoutes().remove(ir);
                            }
                            result.add(news);
                        }
                    }
                }
            }
        }
        return result;
    }

    public void algoGenetique(int population, int s_nbIteration, int method, float rate) {
        //generation aletaoire de base
        ArrayList<Solution> baseGeneration = new ArrayList<>();
        ArrayList<Solution> newGeneration = new ArrayList<>();
        HashMap<Solution, Double> proba = new HashMap<>();
        Random random = new Random();
        double sommefitness;
        Solution bestSolution = null;
        for (int nbIteration = 0; nbIteration < s_nbIteration; nbIteration++) {
            //if (nbIteration % 200 == 0)
            System.out.println("===== Generating new Generation ==== : " + nbIteration);
            sommefitness = 0.d;
            for (int i = 0; i < population; i++) {
                baseGeneration.add(Solution.generateRandom(this));
                sommefitness += baseGeneration.get(i).getSommeDistance(root, distances);
            }
            for (int i = 0; i < population; i++) {
                proba.put(baseGeneration.get(i), baseGeneration.get(i).getSommeDistance(root, distances) / sommefitness);
            }


            //generation
            for (int i = 0; i < population; i++) {
                Solution p1 = tirageSolution(proba, null);
                Solution p2 = tirageSolution(proba, p1);
                Solution child = null;
                if(method == 1) child = croisementMethod1(p1, p2, proba);
                else child = croisementMethod2(p1, p2, proba);
                child = mutate(child, rate);
                if (bestSolution == null || child.getSommeDistance(root, distances) < bestSolution.getSommeDistance(root, distances))
                    bestSolution = child;
                newGeneration.add(child);
            }
        }
        System.out.println("Best gen:");
        bestSolution.display(root, distances);
        //generer une nouvelle generation
        //on prend els 10

    }

    public Solution tirageSolution(HashMap<Solution, Double> proba, Solution exclude) {
        Random random = new Random();
        Solution returner = exclude;
        while (returner == exclude) {
            double tirage = random.nextDouble();
            double sommeProba = 0.d;
            for (Map.Entry<Solution, Double> entry : proba.entrySet()) {
                sommeProba += entry.getValue();
                if (tirage < sommeProba && entry.getKey() != exclude) {
                    returner = entry.getKey();
                    break;
                }
            }
        }
        return returner;
    }


    public Solution croisementMethod1(Solution p1, Solution p2, HashMap<Solution, Double> proba){
        Solution returner = new Solution();
        ArrayList<Client> compteur = new ArrayList<Client>(clients);
        Random random = new Random();
        Double p1Proba = proba.get(p1)/(proba.get(p1) + proba.get(p2));
        for(int i = 0; i < Math.max(p1.getRoutes().size()-1, p2.getRoutes().size()-1); i++){
            Route r1 = null;
            Route r2 = null;
            if(i < p1.getRoutes().size())
                r1 = p1.getRoutes().get(i);

            if(i < p2.getRoutes().size())
                r2 = p2.getRoutes().get(i);

            Route newRoute = new Route();
            if(r1 == null && r2 != null){
                for(Client c: r2.getClients()){
                    if(compteur.contains(c)) {
                        if(newRoute.add(c))
                            compteur.remove(c);
                    }
                }
            }
            else if (r2 == null && r1 != null){
                for(Client c: r1.getClients()){
                    if(compteur.contains(c)) {
                        if(newRoute.add(c))
                            compteur.remove(c);
                    }
                }
            }
            else if(r1 != null && r2 != null){
                //System.out.println(r1.getClients().size());
                //System.out.println(r2.getClients().size());
                for(int j = 0; j < Math.max(r1.getClients().size()-1, r2.getClients().size()-1); j++){
                    Client c1 = null;
                    if( j < r1.getClients().size()) {
                        c1 = r1.getClients().get(j);
                        if (!newRoute.isChargeOk(c1)) {
                            c1 = null;
                        }
                    }

                    Client c2 = null;
                    if(j < r2.getClients().size()) {
                        c2 = r2.getClients().get(j);
                        if (!newRoute.isChargeOk(c2)) {
                            c2 = null;
                        }
                    }

                    if(c1 == null && c2 != null){
                        newRoute.add(c2);
                        compteur.remove(c2);
                    }
                    else if(c2 == null && c1 != null){
                        newRoute.add(c1);
                        compteur.remove(c1);
                    }
                    else if(c1 != null && c2 != null){
                        if(random.nextFloat() < p1Proba && compteur.contains(c1)){
                            if(newRoute.add(c1))
                                compteur.remove(c1);
                        }
                        else if(compteur.contains(c2)){
                            if(newRoute.add(c2))
                                compteur.remove(c2);
                        }
                        else{
                            Client tempC = compteur.get(random.nextInt(compteur.size()-1));
                            if(newRoute.add(tempC))
                                compteur.remove(tempC);
                        }
                    }
                    else{
                        //System.out.println("Empty client - La charge n'est pas ok");
                        //System.out.println(newRoute.getCharge());
                    }
                }
            }
            else{
                System.out.println("Empty route");
            }
            if(!newRoute.isEmpty())
                returner.addRoute(newRoute);
        }
        return returner;
    }

    public Solution croisementMethod2(Solution p1, Solution p2, HashMap<Solution, Double> proba) {
        Solution returner = new Solution();
        ArrayList<Client> clients1 = new ArrayList<>();
        ArrayList<Client> compteur = new ArrayList<Client>(clients);
        for(Route r: p1.getRoutes()){
            for(Client c: r.getClients()){
                clients1.add(c);
            }
        }
        ArrayList<Client> clients2 = new ArrayList<>();
        for(Route r: p2.getRoutes()) {
            for (Client c : r.getClients()) {
                clients2.add(c);
            }
        }
        Random random = new Random();
        ArrayList<Client> newClients = new ArrayList<>();
        int cut = random.nextInt(clients1.size()-1);
        Client clientToAdd;
        for(int i = 0; i < clients1.size(); i++){
            if(i < cut && compteur.contains(clients1.get(i))) {
                newClients.add(clients1.get(i));
                compteur.remove(clients1.get(i));
            }
            else if(compteur.contains(clients2.get(i))) {
                newClients.add(clientToAdd = clients2.get(i));
                compteur.remove(clients2.get(i));
            }
        }

        for(Client c: compteur){
            //newClients.add(random.nextInt(newClients.size()-1), c);
            newClients.add(c);
        }

        Iterator<Client> it = newClients.iterator();
        Route newRoute = new Route();

        while(it.hasNext()){
            Client c = it.next();
            if(newRoute.add(c)){
                it.remove();
            }
            else {
                Boolean stop = false;
                for (int i = 0; i < returner.getRoutes().size() && !stop; i++) {
                    stop = returner.getRoutes().get(i).add(c);
                    if (stop)
                        it.remove();
                }
                if (!stop) {
                    returner.addRoute(newRoute);
                    newRoute = new Route();
                    newRoute.add(c);
                    it.remove();
                }
            }
        }
        returner.addRoute(newRoute);
        return returner;
    }

    public Solution mutate(Solution child, float rate){
        Random random = new Random();
        Solution returner = child;
        if(child.getRoutes().size() < 5)
            System.out.println("BUG child size : " + child.getRoutes().size());

        ArrayList<Solution> solutions = generateAllNeighborsByMove(child);
        if(random.nextFloat() < rate){
            returner = solutions.get(random.nextInt(solutions.size()-1));
        }
        return returner;
    }
}
