import java.util.ArrayList;

public class Route{

    private static Integer maxCharge = 100;
    private Integer charge = 0;
    private ArrayList<Client> route = new ArrayList<>();

    public Route() {

    }

    public Route(Route r){
        charge = new Integer(r.charge);
        route = (ArrayList<Client>) r.route.clone();
    }

    public boolean add(Client c) {
        if (charge + c.getQ() > maxCharge)
            return false;
        charge += c.getQ();
        route.add(c);
        return true;
    }

    public boolean add(Integer index, Client c){
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
        //System.out.println(charge + c.getQ());
        return (charge + c.getQ() <= 100);
    }

    public boolean isChargeOk() {
        return (charge <= 100);
    }

    public double getDistance(Client root, Double[][] distances) {
        double d = 0;
        d+= distances[root.getI()][route.get(0).getI()];
        for (int i = 0; i < route.size() - 2; i++) {
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
        return charge;
    }

    public Client getLast() {
        return route.get(route.size() - 1);
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
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
            s += c.getI();
        }
        return s;
    }
}
