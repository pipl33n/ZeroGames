package pagos;

public class Pagos {
    private int id;
    private String descripcion;
    private double monto;

    public Pagos(int id, String descripcion, double monto) {
        this.id = id;
        this.descripcion = descripcion;
        this.monto = monto;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}
