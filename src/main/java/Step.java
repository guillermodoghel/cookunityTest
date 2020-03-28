public class Step {
    public int val;
    public int x;
    public int y;
    public int cant;
    public boolean end;
    public int path[][];

    //implementation for debugging reasons
    @Override
    public String toString() {
        return "Step{" +
            "val=" + val +
            ", x=" + x +
            ", y=" + y +
            '}';
    }
}