public class Cords {
    /* plik ten zawiera min:
    alakacje pamięci na tablicę wsp.
    ustawianie wsp dla danego wierzchołka
    zwracanie wsp dla danego wierzchołka
    oraz dodawanie wsp dla danego wierzchołka
     */
    private int n;
    private final double[] xs;
    private final double[] ys;

    public Cords(int n){
        this.n = n;
        this.xs = new double[n+1];
        this.ys = new double[n+1];
    }
    public void set(int v, double x, double y){
        if(v>=1 && v<=n){
            xs[v] = x;
            ys[v] = y;
        }
    }
    public double getX(int v){
        if(v>=1 && v<=n)return xs[v];
        return 0.0;
    }
    public double getY(int v){
        if(v>=1 && v<=n) return ys[v];
        return 0.0;
    }
    public void add(int v, double deltaX, double deltaY){
        if(v>=1 && v<=n){
        xs[v] += deltaX;
        ys[v] +=deltaY;
        }
    }
    public int getN(){
        return n;
    }
}
