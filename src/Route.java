import java.util.ArrayList;

public class Route{

    private static int maxCharge = 100;
    private ArrayList<Client> route = new ArrayList<>();
    private int charge;

    public Route() {

    }

    public static Route clone(Route r){
        Route returner = new Route();
        for(Client c : r.getRoute()){
            returner.add(c);
        }
        returner.getCharge();
        return returner;
    }


    public boolean add(Client c) {
        if (charge + c.getQ() > maxCharge)
            return false;
        charge += c.getQ();
        route.add(c);
        return true;
    }

    public boolean addAtInteger(Integer index, Client c){
        if (charge + c.getQ() > maxCharge)
            return false;
        charge += c.getQ();
        route.add(index, c);
        return true;
    }

    public void remove(Client c){
        route.remove(c);
        charge -= c.getQ();
    }

    public boolean isChargeOk(Client c) {
        return ((charge + c.getQ()) <= 100);
    }

    public boolean isEmpty(){
        return route.isEmpty();
    }

    public boolean isChargeOk() {
        return (charge <= 100);
    }

    public double getDistance(Client root, Double[][] distances) {
        double d = 0.d;
        d+= distances[root.getI()][route.get(0).getI()];
        for (int i = 0; i < route.size() - 1; i++) {
            d+= distances[route.get(i).getI()][route.get(i+1).getI()];
        }
        d+= distances[route.get(route.size()-1).getI()][root.getI()];
        return d;
    }

    public static Integer getMaxCharge() {
        return maxCharge;
    }

    public static void setMaxCharge(Integer maxCharge) {
        Route.maxCharge = maxCharge;
    }

    public Integer getCharge() {
        int i = 0;
        for(Client c: route){
            i += c.getQ();
        }
        charge = i;
        return i;
    }

    public Client getLast() {
        return route.get(route.size() - 1);
    }

    public ArrayList<Client> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<Client> route) {
        this.route = route;
    }

    public String serialize(){
        String s = "R";
        for(Client c: route){
            s += "C"+c.getI();
        }
        return s;
    }

}
