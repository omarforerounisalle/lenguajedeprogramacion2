#OMAR FORERO CÁCERES

from __future__ import annotations

from abc import ABC, abstractmethod
from dataclasses import dataclass, field
from datetime import date
from typing import List, Optional


# =========================================================
# Interfaces
# =========================================================

class InventarioService(ABC):
    @abstractmethod
    def obtener_producto(self, codigo: str) -> Optional["Producto"]:
        pass

    @abstractmethod
    def consultar_disponibilidad(self, codigo: str) -> int:
        pass

    @abstractmethod
    def actualizar_stock(self, codigo: str, cantidad: int) -> None:
        pass


class NotificadorEmail(ABC):
    @abstractmethod
    def enviar(self, destinatario: str, asunto: str, contenido: str) -> None:
        pass


# =========================================================
# Clases base abstractas
# =========================================================

class Usuario(ABC):
    def __init__(self, id_usuario: int, nombre: str, correo: str, activo: bool = True) -> None:
        self.id_usuario = id_usuario
        self.nombre = nombre
        self.correo = correo
        self.activo = activo

    def autenticar(self) -> bool:
        return self.activo

    def obtener_datos(self) -> str:
        return f"Usuario(id={self.id_usuario}, nombre={self.nombre}, correo={self.correo})"


class Pago(ABC):
    def __init__(self, monto: float, fecha_pago: date, estado: str = "pendiente") -> None:
        self.monto = monto
        self.fecha_pago = fecha_pago
        self.estado = estado

    @abstractmethod
    def procesar(self) -> bool:
        pass

    @abstractmethod
    def validar(self) -> bool:
        pass


# =========================================================
# Entidades principales
# =========================================================

@dataclass
class Producto:
    codigo: str
    nombre: str
    descripcion: str
    precio: float
    cantidad_disponible: int

    def esta_disponible(self, cantidad: int) -> bool:
        return self.cantidad_disponible >= cantidad

    def actualizar_precio(self, nuevo_precio: float) -> None:
        if nuevo_precio <= 0:
            raise ValueError("El nuevo precio debe ser mayor que cero.")
        self.precio = nuevo_precio

    def actualizar_stock(self, cantidad: int) -> None:
        if cantidad < 0:
            raise ValueError("La cantidad disponible no puede ser negativa.")
        self.cantidad_disponible = cantidad


@dataclass
class Catalogo:
    productos: List[Producto] = field(default_factory=list)
    fecha_actualizacion: date = field(default_factory=date.today)

    def listar_productos(self) -> List[Producto]:
        return self.productos

    def buscar_producto(self, codigo: str) -> Optional[Producto]:
        for producto in self.productos:
            if producto.codigo == codigo:
                return producto
        return None

    def agregar_producto(self, producto: Producto) -> None:
        if self.buscar_producto(producto.codigo) is not None:
            raise ValueError(f"Ya existe un producto con código {producto.codigo}.")
        self.productos.append(producto)
        self.fecha_actualizacion = date.today()

    def eliminar_producto(self, codigo: str) -> None:
        producto = self.buscar_producto(codigo)
        if producto is None:
            raise ValueError(f"No existe un producto con código {codigo}.")
        self.productos.remove(producto)
        self.fecha_actualizacion = date.today()


@dataclass
class SuscripcionCatalogo:
    id_suscripcion: int
    correo_destino: str
    frecuencia: str
    activa: bool = True

    def activar(self) -> None:
        self.activa = True

    def desactivar(self) -> None:
        self.activa = False

    def enviar_catalogo(self, catalogo: Catalogo, notificador: NotificadorEmail) -> None:
        if not self.activa:
            raise ValueError("La suscripción está inactiva.")
        nombres = ", ".join([producto.nombre for producto in catalogo.listar_productos()])
        asunto = "Catálogo TeleVentas"
        contenido = f"Productos disponibles: {nombres}"
        notificador.enviar(self.correo_destino, asunto, contenido)


@dataclass
class DetalleOrden:
    cantidad: int
    precio_unitario: float
    producto: Producto

    def calcular_subtotal(self) -> float:
        return self.cantidad * self.precio_unitario


class OrdenCompra:
    def __init__(self, id_orden: int, fecha_creacion: date, cliente: "Cliente") -> None:
        self.id_orden = id_orden
        self.fecha_creacion = fecha_creacion
        self.estado = "pendiente"
        self.total = 0.0
        self.cliente = cliente
        self.detalles: List[DetalleOrden] = []
        self.pago: Optional[Pago] = None

    def agregar_detalle(self, detalle: DetalleOrden) -> None:
        if self.estado != "pendiente":
            raise ValueError("Solo se pueden agregar detalles a una orden pendiente.")
        self.detalles.append(detalle)
        self.total = self.calcular_total()

    def calcular_total(self) -> float:
        return sum(detalle.calcular_subtotal() for detalle in self.detalles)

    def asignar_pago(self, pago: Pago) -> None:
        self.pago = pago

    def confirmar(self, inventario_service: InventarioService) -> None:
        if not self.detalles:
            raise ValueError("No se puede confirmar una orden sin productos.")
        if self.pago is None:
            raise ValueError("No se puede confirmar una orden sin pago asignado.")
        if not self.pago.procesar():
            raise ValueError("El pago no pudo ser procesado.")

        for detalle in self.detalles:
            disponible = inventario_service.consultar_disponibilidad(detalle.producto.codigo)
            if disponible < detalle.cantidad:
                raise ValueError(
                    f"No hay suficiente stock para el producto {detalle.producto.nombre}."
                )

        for detalle in self.detalles:
            disponible = inventario_service.consultar_disponibilidad(detalle.producto.codigo)
            inventario_service.actualizar_stock(
                detalle.producto.codigo,
                disponible - detalle.cantidad
            )

        self.estado = "confirmada"
        self.total = self.calcular_total()

    def cancelar(self) -> None:
        if self.estado == "cancelada":
            raise ValueError("La orden ya está cancelada.")
        if self.estado == "despachada":
            raise ValueError("No se puede cancelar una orden ya despachada.")
        self.estado = "cancelada"


@dataclass
class Queja:
    id_queja: int
    fecha: date
    motivo: str
    descripcion: str
    estado: str = "registrada"
    orden: Optional[OrdenCompra] = None

    def registrar(self) -> None:
        self.estado = "registrada"

    def remitir(self) -> None:
        self.estado = "remitida"

    def cerrar(self) -> None:
        self.estado = "cerrada"


@dataclass
class Pedido:
    id_pedido: int
    estado: str
    fecha_preparacion: date
    empaquetado: bool
    orden: OrdenCompra

    def preparar(self) -> None:
        self.estado = "en_preparacion"

    def empaquetar(self) -> None:
        self.empaquetado = True
        self.estado = "empaquetado"

    def marcar_listo(self) -> None:
        if not self.empaquetado:
            raise ValueError("No se puede marcar como listo un pedido no empaquetado.")
        self.estado = "listo"


@dataclass
class Envio:
    id_envio: int
    direccion_destino: str
    fecha_salida: Optional[date]
    estado: str = "pendiente"

    def programar(self) -> None:
        self.estado = "programado"

    def despachar(self) -> None:
        self.estado = "despachado"
        self.fecha_salida = date.today()

    def entregar(self) -> None:
        self.estado = "entregado"


@dataclass
class EmpresaTransporte:
    id_empresa: int
    nombre: str
    cobertura: str

    def calcular_tarifa(self) -> float:
        return 15000.0

    def asignar_envio(self, envio: Envio) -> None:
        envio.programar()


@dataclass
class ServicioLogistica:
    empresas: List[EmpresaTransporte] = field(default_factory=list)

    def seleccionar_empresa(self, pedido: Pedido) -> EmpresaTransporte:
        if not self.empresas:
            raise ValueError("No hay empresas de transporte disponibles.")
        return self.empresas[0]

    def generar_envio(self, pedido: Pedido) -> Envio:
        envio = Envio(
            id_envio=pedido.id_pedido,
            direccion_destino=pedido.orden.cliente.direccion_envio,
            fecha_salida=None,
            estado="pendiente"
        )
        empresa = self.seleccionar_empresa(pedido)
        empresa.asignar_envio(envio)
        return envio


# =========================================================
# Implementaciones concretas de interfaces
# =========================================================

class InventarioMemoria(InventarioService):
    def __init__(self, productos: List[Producto]) -> None:
        self._productos = {producto.codigo: producto for producto in productos}

    def obtener_producto(self, codigo: str) -> Optional[Producto]:
        return self._productos.get(codigo)

    def consultar_disponibilidad(self, codigo: str) -> int:
        producto = self.obtener_producto(codigo)
        if producto is None:
            return 0
        return producto.cantidad_disponible

    def actualizar_stock(self, codigo: str, cantidad: int) -> None:
        producto = self.obtener_producto(codigo)
        if producto is None:
            raise ValueError(f"Producto con código {codigo} no encontrado.")
        producto.actualizar_stock(cantidad)


class NotificadorEmailConsola(NotificadorEmail):
    def enviar(self, destinatario: str, asunto: str, contenido: str) -> None:
        print("\n--- CORREO ENVIADO ---")
        print(f"Para: {destinatario}")
        print(f"Asunto: {asunto}")
        print(f"Contenido: {contenido}")
        print("----------------------\n")


# =========================================================
# Usuarios concretos
# =========================================================

class Cliente(Usuario):
    def __init__(
        self,
        id_usuario: int,
        nombre: str,
        correo: str,
        direccion_envio: str,
        telefono: str,
        activo: bool = True
    ) -> None:
        super().__init__(id_usuario, nombre, correo, activo)
        self.direccion_envio = direccion_envio
        self.telefono = telefono
        self.ordenes: List[OrdenCompra] = []
        self.quejas: List[Queja] = []
        self.suscripciones: List[SuscripcionCatalogo] = []

    def consultar_catalogo(self, catalogo: Catalogo) -> List[Producto]:
        return catalogo.listar_productos()

    def solicitar_catalogo(
        self,
        id_suscripcion: int,
        frecuencia: str
    ) -> SuscripcionCatalogo:
        suscripcion = SuscripcionCatalogo(
            id_suscripcion=id_suscripcion,
            correo_destino=self.correo,
            frecuencia=frecuencia,
            activa=True
        )
        self.suscripciones.append(suscripcion)
        return suscripcion

    def crear_orden(self, id_orden: int) -> OrdenCompra:
        orden = OrdenCompra(id_orden=id_orden, fecha_creacion=date.today(), cliente=self)
        self.ordenes.append(orden)
        return orden

    def presentar_queja(
        self,
        id_queja: int,
        motivo: str,
        descripcion: str,
        orden: Optional[OrdenCompra] = None
    ) -> Queja:
        queja = Queja(
            id_queja=id_queja,
            fecha=date.today(),
            motivo=motivo,
            descripcion=descripcion,
            orden=orden
        )
        self.quejas.append(queja)
        return queja

    def cancelar_orden(self, orden: OrdenCompra) -> None:
        if orden not in self.ordenes:
            raise ValueError("La orden no pertenece a este cliente.")
        orden.cancelar()


class AgenteDeposito(Usuario):
    def __init__(
        self,
        id_usuario: int,
        nombre: str,
        correo: str,
        codigo_empleado: str,
        activo: bool = True
    ) -> None:
        super().__init__(id_usuario, nombre, correo, activo)
        self.codigo_empleado = codigo_empleado

    def consultar_ordenes_confirmadas(self, ordenes: List[OrdenCompra]) -> List[OrdenCompra]:
        return [orden for orden in ordenes if orden.estado == "confirmada"]

    def armar_pedido(self, orden: OrdenCompra) -> Pedido:
        if orden.estado != "confirmada":
            raise ValueError("Solo se puede armar un pedido a partir de una orden confirmada.")
        return Pedido(
            id_pedido=orden.id_orden,
            estado="pendiente",
            fecha_preparacion=date.today(),
            empaquetado=False,
            orden=orden
        )

    def empaquetar_pedido(self, pedido: Pedido) -> None:
        pedido.preparar()
        pedido.empaquetar()
        pedido.marcar_listo()

    def coordinar_entrega(self, pedido: Pedido, servicio_logistica: ServicioLogistica) -> Envio:
        return servicio_logistica.generar_envio(pedido)


class GerenteRelaciones(Usuario):
    def __init__(
        self,
        id_usuario: int,
        nombre: str,
        correo: str,
        area: str,
        activo: bool = True
    ) -> None:
        super().__init__(id_usuario, nombre, correo, activo)
        self.area = area
        self.quejas_recibidas: List[Queja] = []

    def recibir_queja(self, queja: Queja) -> None:
        queja.remitir()
        self.quejas_recibidas.append(queja)

    def gestionar_queja(self, queja: Queja) -> None:
        if queja not in self.quejas_recibidas:
            raise ValueError("La queja no ha sido recibida por este gerente.")
        queja.estado = "en_gestion"

    def cerrar_queja(self, queja: Queja) -> None:
        if queja not in self.quejas_recibidas:
            raise ValueError("La queja no ha sido recibida por este gerente.")
        queja.cerrar()


# =========================================================
# Pago concreto
# =========================================================

class PagoTarjetaCredito(Pago):
    def __init__(
        self,
        monto: float,
        fecha_pago: date,
        numero_enmascarado: str,
        titular: str,
        franquicia: str,
        fecha_vencimiento: str
    ) -> None:
        super().__init__(monto, fecha_pago)
        self.numero_enmascarado = numero_enmascarado
        self.titular = titular
        self.franquicia = franquicia
        self.fecha_vencimiento = fecha_vencimiento

    def validar(self) -> bool:
        return (
            self.monto > 0 and
            len(self.numero_enmascarado) >= 4 and
            bool(self.titular.strip()) and
            bool(self.franquicia.strip()) and
            bool(self.fecha_vencimiento.strip())
        )

    def procesar(self) -> bool:
        if not self.validar():
            self.estado = "rechazado"
            return False
        self.estado = "aprobado"
        return True


# =========================================================
# Flujo de demostración
# =========================================================

def mostrar_catalogo(catalogo: Catalogo) -> None:
    print("\n=== CATÁLOGO DE PRODUCTOS ===")
    for producto in catalogo.listar_productos():
        print(
            f"Código: {producto.codigo} | "
            f"Nombre: {producto.nombre} | "
            f"Precio: ${producto.precio:,.0f} | "
            f"Stock: {producto.cantidad_disponible}"
        )


def main() -> None:
    # Productos e inventario
    producto_1 = Producto("P001", "Portátil", "Portátil de 14 pulgadas", 2500000.0, 10)
    producto_2 = Producto("P002", "Mouse", "Mouse inalámbrico", 80000.0, 25)
    producto_3 = Producto("P003", "Teclado", "Teclado mecánico", 180000.0, 15)

    catalogo = Catalogo([producto_1, producto_2, producto_3])
    inventario = InventarioMemoria(catalogo.listar_productos())
    notificador = NotificadorEmailConsola()

    # Usuarios
    cliente = Cliente(
        id_usuario=1,
        nombre="Carlos Gómez",
        correo="carlos@email.com",
        direccion_envio="Calle 10 # 20-30, Floridablanca",
        telefono="3001234567"
    )

    agente = AgenteDeposito(
        id_usuario=2,
        nombre="Laura Pérez",
        correo="laura@televendas.com",
        codigo_empleado="AD-001"
    )

    gerente = GerenteRelaciones(
        id_usuario=3,
        nombre="Marta Rodríguez",
        correo="marta@televendas.com",
        area="Relaciones"
    )

    # Empresas de transporte
    empresa_1 = EmpresaTransporte(1, "Envíos Nacionales", "Nacional")
    servicio_logistica = ServicioLogistica([empresa_1])

    # Cliente consulta catálogo
    mostrar_catalogo(catalogo)

    # Cliente solicita envío periódico del catálogo
    suscripcion = cliente.solicitar_catalogo(id_suscripcion=1, frecuencia="semanal")
    suscripcion.enviar_catalogo(catalogo, notificador)

    # Cliente crea orden
    orden = cliente.crear_orden(id_orden=1001)

    detalle_1 = DetalleOrden(cantidad=1, precio_unitario=producto_1.precio, producto=producto_1)
    detalle_2 = DetalleOrden(cantidad=2, precio_unitario=producto_2.precio, producto=producto_2)

    orden.agregar_detalle(detalle_1)
    orden.agregar_detalle(detalle_2)

    # Cliente paga la orden
    pago = PagoTarjetaCredito(
        monto=orden.calcular_total(),
        fecha_pago=date.today(),
        numero_enmascarado="****1234",
        titular="Carlos Gómez",
        franquicia="Visa",
        fecha_vencimiento="12/28"
    )
    orden.asignar_pago(pago)
    orden.confirmar(inventario)

    print("\n=== ORDEN CONFIRMADA ===")
    print(f"Orden #{orden.id_orden} | Estado: {orden.estado} | Total: ${orden.total:,.0f}")

    # Agente de depósito arma y empaqueta pedido
    ordenes_confirmadas = agente.consultar_ordenes_confirmadas(cliente.ordenes)
    pedido = agente.armar_pedido(ordenes_confirmadas[0])
    agente.empaquetar_pedido(pedido)

    print("\n=== PEDIDO PREPARADO ===")
    print(f"Pedido #{pedido.id_pedido} | Estado: {pedido.estado}")

    # Coordinar envío
    envio = agente.coordinar_entrega(pedido, servicio_logistica)
    envio.despachar()

    print("\n=== ENVÍO GENERADO ===")
    print(f"Envío #{envio.id_envio} | Estado: {envio.estado} | Fecha salida: {envio.fecha_salida}")

    # Cliente presenta una queja
    queja = cliente.presentar_queja(
        id_queja=5001,
        motivo="Demora en la entrega",
        descripcion="El pedido tardó más de lo esperado.",
        orden=orden
    )
    gerente.recibir_queja(queja)
    gerente.gestionar_queja(queja)
    gerente.cerrar_queja(queja)

    print("\n=== QUEJA GESTIONADA ===")
    print(f"Queja #{queja.id_queja} | Estado: {queja.estado} | Motivo: {queja.motivo}")

    # Estado final de inventario
    print("\n=== INVENTARIO ACTUALIZADO ===")
    mostrar_catalogo(catalogo)


if __name__ == "__main__":
    main()
