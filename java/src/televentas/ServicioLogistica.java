package televentas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicioLogistica {

    private final ArrayList<EmpresaTransporte> empresas = new ArrayList<>();

    public ServicioLogistica() {
    }

    public ServicioLogistica(List<EmpresaTransporte> empresasIniciales) {
        this.empresas.addAll(empresasIniciales);
    }

    public List<EmpresaTransporte> getEmpresas() {
        return Collections.unmodifiableList(new ArrayList<>(empresas));
    }

    public EmpresaTransporte seleccionarEmpresa(Pedido pedido) {
        if (empresas.isEmpty()) {
            throw new IllegalStateException("No hay empresas de transporte disponibles.");
        }
        return empresas.get(0);
    }

    public Envio generarEnvio(Pedido pedido) {
        Envio envio = new Envio(
                pedido.getIdPedido(),
                pedido.getOrden().getCliente().getDireccionEnvio(),
                null,
                "pendiente"
        );
        EmpresaTransporte empresa = seleccionarEmpresa(pedido);
        empresa.asignarEnvio(envio);
        return envio;
    }
}
