package televentas;

public class EmpresaTransporte {

    private final int idEmpresa;
    private final String nombre;
    private final String cobertura;

    public EmpresaTransporte(int idEmpresa, String nombre, String cobertura) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.cobertura = cobertura;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCobertura() {
        return cobertura;
    }

    public double calcularTarifa() {
        return 15000.0;
    }

    public void asignarEnvio(Envio envio) {
        envio.programar();
    }
}
