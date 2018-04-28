public class Client {

    private Integer i, x, y,q;

    public Client(String s) {
        String[] parts = s.split(";");
        i = Integer.parseInt(parts[0]);
        x = Integer.parseInt(parts[1]);
        y = Integer.parseInt(parts[2]);
        q = Integer.parseInt(parts[3]);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public double distance(Client c){
        double deltax = x - c.getX();
        double deltay = y - c.getY();
        return Math.sqrt((deltax*deltax) + (deltay*deltay));
    }


}
