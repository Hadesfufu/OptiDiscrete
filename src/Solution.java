import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Solution {
    private ArrayList<Route> routes = new ArrayList<>();
    private static Integer s_index = 0;
    private Integer index;

    public Solution(ArrayList<Route> routes) {
        index = s_index;
        s_index++;
        this.routes = routes;
    }

    public Solution(){
        index = s_index;
        s_index++;
    }

    public Integer getIndex() {
        return index;
    }

    public static Solution clone(Solution s){
        Solution returner = new Solution();
        for(Route r : s.getRoutes()){
            returner.addRoute(Route.clone(r));
        }
        return returner;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public boolean isBetterThan(Solution s, Client root, Double[][] distances){
        return(getSommeDistance(root, distances) < s.getSommeDistance(root, distances));
    }

    public Double getSommeDistance(Client root, Double[][] distances){
        Double distance = 0.d;
        for(Route r: routes){
            /*System.out.println("Route");
            System.out.println("Distance : "+r.getDistance(root, distances));
            System.out.println("Charge : "+ r.getCharge());*/
            distance += r.getDistance(root, distances);
        }
        return distance;
    }

    public void display(Client root, Double[][] distances){
        System.out.println("Solution :" + serialize() + " Distance : " + getSommeDistance(root, distances) + " nb Routes : " + getRoutes().size());
    }

    public Route getLightestRoute(){
        Route ret = null;
        for(Route r: routes){
            if(ret == null)
                ret = r;
            else if(r.getCharge() < ret.getCharge())
                ret = r;
        }
        return ret;
    }

    public String serialize(){
        String s = "S";
        for(Route r : routes){
            s += r.serialize();
        }
        return s;
    }

    public Integer globalWeight(){
        Integer i = 0;
        for(Route r : routes){
            i+=r.getCharge();
        }
        return i;
    }

    public void addRoute(Route r){
        routes.add(r);
    }

    public static Solution generateRandom(Application appli){
        ArrayList<Client> myclients = (ArrayList<Client>) appli.clients.clone();

        for(Client c: myclients){
            if(c.getI() == 0) {
                myclients.remove(c);
                break;
            }
        }

        Random rand = new Random();
        int nbMinRoute = (appli.poidTotal / 100) + (appli.poidTotal%100 != 0 ? 1 : 0);
        int nbRoutes = rand.nextInt(appli.clients.size()-nbMinRoute) + nbMinRoute;
        ArrayList<Route> routes = new ArrayList<>();
        for(int i = 0; i < nbRoutes; i++){
            routes.add(new Route());
        }
        while(!myclients.isEmpty()){
            int indexClient = rand.nextInt(myclients.size());
            int indexRoute = rand.nextInt(routes.size());
            if(routes.get(indexRoute).add(myclients.get(indexClient)))
                myclients.remove(indexClient);
        }
        Iterator<Route> i = routes.iterator();
        while(i.hasNext()){
            Route r = i.next();
            if(r.isEmpty())
                i.remove();
        }
        Solution returner = new Solution(routes);
        return returner;
    }
}

