import java.util.ArrayList;

public class Solution {
    private ArrayList<Route> routes = new ArrayList<>();
    private static Integer s_index = 0;
    private Integer index;

    public Solution(ArrayList<Route> routes) {
        index = s_index;
        s_index++;
        this.routes = routes;
    }

    public Integer getIndex() {
        return index;
    }

    public Solution(Solution s){
        index = s_index;
        s_index++;
        for(Route r: s.routes){
            routes.add(new Route(r));
        }
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
        System.out.println("Solution n°" + index);
        System.out.println("Distance : " + getSommeDistance(root, distances));
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
}
