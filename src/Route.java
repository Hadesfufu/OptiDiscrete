import java.util.ArrayList;

public class Route{

    private static int maxCharge = 100;
    private ArrayList<Client> clients = new ArrayList<>();
    private int charge;

    public Route() {
        charge = 0;
    }

    public static Route clone(Route r){
        Route returner = new Route();
        for(Client c : r.getClients()){
            returner.add(c);
        }
        returner.getCharge();
        return returner;
    }


    public boolean add(Client c) {
        if (charge + c.getQ() > maxCharge)
            return false;
        charge += c.getQ();
        clients.add(c);
        return true;
    }

    public boolean addAtInteger(Integer index, Client c){
        if (charge + c.getQ() > maxCharge)
            return false;
        charge += c.getQ();
        clients.add(index, c);
        return true;
    }

    public void remove(Client c){
        clients.remove(c);
        charge -= c.getQ();
    }

    public boolean isChargeOk(Client c) {
        return ((charge + c.getQ()) <= 100);
    }

    public boolean isEmpty(){
        return clients.isEmpty();
    }

    public boolean isChargeOk() {
        return (charge <= 100);
    }

    public double getDistance(Client root, Double[][] distances) {
        double d = 0.d;
        d+= distances[root.getI()][clients.get(0).getI()];
        for (int i = 0; i < clients.size() - 1; i++) {
            d+= distances[clients.get(i).getI()][clients.get(i+1).getI()];
        }
        d+= distances[clients.get(clients.size()-1).getI()][root.getI()];
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
        for(Client c: clients){
            i += c.getQ();
        }
        charge = i;
        return i;
    }

    public Client getLast() {
        return clients.get(clients.size() - 1);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setRoute(ArrayList<Client> route) {
        this.clients = route;
    }

    public String serialize(){
        String s = "R";
        for(Client c: clients){
            s += "C"+c.getI();
        }
        return s;
    }

}
