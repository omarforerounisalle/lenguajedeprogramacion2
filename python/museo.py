# Omar Hernando Forero

from __future__ import annotations

from abc import ABC, abstractmethod
from datetime import date, timedelta
from typing import Optional

# --- Constantes de dominio ---
ESTADO_EXPUESTA = "expuesta"
ESTADO_RESTAURACION = "restauracion"
ESTADO_CEDIDA = "cedida"
ESTADO_DAÑADA = "dañada"

ANIOS_CICLO_RESTAURACION = 5


def _agregar_anios(f: date, anios: int) -> date:
    try:
        return f.replace(year=f.year + anios)
    except ValueError:
        return f.replace(year=f.year + anios, month=2, day=28)


# =========================================================
# Usuario (abstracto)
# =========================================================


class Usuario(ABC):
    def __init__(
        self,
        id_usuario: int,
        nombre: str,
        correo: str,
        activo: bool,
        contrasena: str,
    ) -> None:
        self.id_usuario = id_usuario
        self.nombre = nombre
        self.correo = correo
        self.activo = activo
        self.contrasena = contrasena
        self._sesion_activa = False

    def iniciar_sesion(self, contrasena: str) -> bool:
        """Valida credenciales; requisito de seguridad previo al uso del software."""
        if not self.activo:
            self._sesion_activa = False
            return False
        ok = self.contrasena == contrasena
        self._sesion_activa = ok
        return ok

    def cerrar_sesion(self) -> None:
        self._sesion_activa = False

    def autenticar(self) -> bool:
        """Indica si la cuenta está activa y la sesión fue iniciada correctamente."""
        return self.activo and self._sesion_activa

    def obtener_datos(self) -> str:
        return (
            f"Usuario(id={self.id_usuario}, nombre={self.nombre}, "
            f"correo={self.correo}, activo={self.activo})"
        )


# =========================================================
# Obra de arte (abstracto)
# =========================================================


class ObraArte(ABC):
    def __init__(
        self,
        id_obra: int,
        titulo: str,
        autor: str,
        periodo: str,
        valor_economico: float,
        fecha_creacion: date,
        fecha_ingreso: date,
        estado: str,
    ) -> None:
        self.id_obra = id_obra
        self.titulo = titulo
        self.autor = autor
        self.periodo = periodo
        self.valor_economico = valor_economico
        self.fecha_creacion = fecha_creacion
        self.fecha_ingreso = fecha_ingreso
        self.estado = estado
        self._sala: Optional["Sala"] = None
        self._restauraciones: list["Restauracion"] = []
        self._cola_solicitudes_cesion: list["MuseoColaborador"] = []
        self._cesion_activa: Optional["Cesion"] = None

    def enviar_a_restauracion(self) -> None:
        self.estado = ESTADO_RESTAURACION

    def marcar_expuesta(self) -> None:
        self.estado = ESTADO_EXPUESTA

    def esta_disponible_para_cesion(self) -> bool:
        if self.estado == ESTADO_RESTAURACION:
            return False
        if self.estado == ESTADO_CEDIDA:
            return False
        if self.estado == ESTADO_DAÑADA:
            return False
        return self.estado == ESTADO_EXPUESTA

    def calcular_antiguedad(self) -> int:
        """Antigüedad de la pieza en años desde la fecha de creación."""
        hoy = date.today()
        años = hoy.year - self.fecha_creacion.year
        if (hoy.month, hoy.day) < (self.fecha_creacion.month, self.fecha_creacion.day):
            años -= 1
        return max(0, años)

    def registrar_restauracion(self, r: Restauracion) -> None:
        self._restauraciones.append(r)

    def restauraciones_ordenadas_por_antiguedad(self) -> list[Restauracion]:
        """Restauraciones de esta obra, de la más antigua a la más reciente (por fecha de inicio)."""
        return sorted(self._restauraciones, key=lambda x: x.fecha_inicio)

    def fecha_referencia_proximo_ciclo_restauracion(self) -> date:
        """Última fecha de fin de restauración, o fecha de ingreso si nunca se restauró."""
        finalizadas = [r.fecha_fin for r in self._restauraciones if r.fecha_fin is not None]
        if finalizadas:
            return max(finalizadas)
        return self.fecha_ingreso

    def necesita_restauracion_ciclo(self, dia_consulta: date) -> bool:
        """Ciclo automático cada cinco años desde el último fin de restauración o desde el ingreso."""
        if self.estado == ESTADO_RESTAURACION or self.estado == ESTADO_CEDIDA:
            return False
        ref = self.fecha_referencia_proximo_ciclo_restauracion()
        limite = _agregar_anios(ref, ANIOS_CICLO_RESTAURACION)
        return dia_consulta >= limite

    @abstractmethod
    def mostrar_detalle(self) -> str:
        pass


class Cuadro(ObraArte):
    def __init__(
        self,
        id_obra: int,
        titulo: str,
        autor: str,
        periodo: str,
        valor_economico: float,
        fecha_creacion: date,
        fecha_ingreso: date,
        estado: str,
        tecnica: str,
        estilo: str,
    ) -> None:
        super().__init__(
            id_obra, titulo, autor, periodo, valor_economico,
            fecha_creacion, fecha_ingreso, estado,
        )
        self.tecnica = tecnica
        self.estilo = estilo

    def mostrar_detalle(self) -> str:
        return (
            f"Cuadro: {self.titulo} ({self.autor}, {self.periodo}) - "
            f"técnica: {self.tecnica}, estilo: {self.estilo}"
        )


class Escultura(ObraArte):
    def __init__(
        self,
        id_obra: int,
        titulo: str,
        autor: str,
        periodo: str,
        valor_economico: float,
        fecha_creacion: date,
        fecha_ingreso: date,
        estado: str,
        material: str,
        estilo: str,
    ) -> None:
        super().__init__(
            id_obra, titulo, autor, periodo, valor_economico,
            fecha_creacion, fecha_ingreso, estado,
        )
        self.material = material
        self.estilo = estilo

    def mostrar_detalle(self) -> str:
        return (
            f"Escultura: {self.titulo} ({self.autor}, {self.periodo}) - "
            f"material: {self.material}, estilo: {self.estilo}"
        )


class OtroObjeto(ObraArte):
    def __init__(
        self,
        id_obra: int,
        titulo: str,
        autor: str,
        periodo: str,
        valor_economico: float,
        fecha_creacion: date,
        fecha_ingreso: date,
        estado: str,
        tipo_objeto: str,
    ) -> None:
        super().__init__(
            id_obra, titulo, autor, periodo, valor_economico,
            fecha_creacion, fecha_ingreso, estado,
        )
        self.tipo_objeto = tipo_objeto

    def mostrar_detalle(self) -> str:
        return (
            f"Otro objeto ({self.tipo_objeto}): {self.titulo} "
            f"({self.autor}, {self.periodo})"
        )


# =========================================================
# Restauración (declaración adelantada resuelta en runtime)
# =========================================================


class Restauracion:
    _id_seq = 0

    def __init__(
        self,
        obra: ObraArte,
        tipo_restauracion: str,
        fecha_inicio: date,
        fecha_fin: Optional[date],
        motivo: str,
    ) -> None:
        Restauracion._id_seq += 1
        self.id_restauracion = Restauracion._id_seq
        self.obra = obra
        self.tipo_restauracion = tipo_restauracion
        self.fecha_inicio = fecha_inicio
        self.fecha_fin = fecha_fin
        self.motivo = motivo

    def iniciar(self) -> None:
        self.obra.enviar_a_restauracion()

    def finalizar(self) -> None:
        self.fecha_fin = date.today()
        self.obra.marcar_expuesta()

    def esta_activa(self) -> bool:
        return self.fecha_fin is None


# =========================================================
# Catálogo y salas
# =========================================================


class CatalogoObras:
    def __init__(self) -> None:
        self.obras: list[ObraArte] = []
        self.fecha_actualizacion = date.today()

    def _tocar(self) -> None:
        self.fecha_actualizacion = date.today()

    def listar_obras(self) -> list[ObraArte]:
        return list(self.obras)

    def buscar_obra(self, id_obra: int) -> ObraArte:
        for o in self.obras:
            if o.id_obra == id_obra:
                return o
        raise KeyError(f"No existe obra con id {id_obra}")

    def agregar_obra(self, obra: ObraArte) -> None:
        self.obras.append(obra)
        self._tocar()

    def eliminar_obra(self, id_obra: int) -> None:
        self.obras = [o for o in self.obras if o.id_obra != id_obra]
        self._tocar()


class Sala:
    def __init__(self, id_sala: int, nombre: str, ubicacion: str) -> None:
        self.id_sala = id_sala
        self.nombre = nombre
        self.ubicacion = ubicacion
        self._obras_en_sala: list[ObraArte] = []

    def agregar_obra(self, obra: ObraArte) -> None:
        if obra in self._obras_en_sala:
            return
        if obra._sala is not None and obra._sala is not self:
            obra._sala.retirar_obra(obra)
        obra._sala = self
        self._obras_en_sala.append(obra)

    def retirar_obra(self, obra: ObraArte) -> None:
        if obra in self._obras_en_sala:
            self._obras_en_sala.remove(obra)
        if obra._sala is self:
            obra._sala = None

    def listar_obras(self) -> list[ObraArte]:
        return list(self._obras_en_sala)


# =========================================================
# Cesión y museos colaboradores
# =========================================================


class MuseoColaborador:
    def __init__(self, id_museo: int, nombre: str, ciudad: str, pais: str) -> None:
        self.id_museo = id_museo
        self.nombre = nombre
        self.ciudad = ciudad
        self.pais = pais

    def recibir_cesion(self) -> None:
        pass

    def solicitar_obra(self) -> None:
        pass


class Cesion:
    _id_seq = 0

    def __init__(
        self,
        obra: ObraArte,
        museo: MuseoColaborador,
        fecha_inicio: date,
        fecha_fin: date,
        importe_pagado: float,
        estado: str,
    ) -> None:
        Cesion._id_seq += 1
        self.id_cesion = Cesion._id_seq
        self.obra = obra
        self.museo = museo
        self.fecha_inicio = fecha_inicio
        self.fecha_fin = fecha_fin
        self.importe_pagado = importe_pagado
        self.estado = estado

    def iniciar(self) -> None:
        self.obra.estado = ESTADO_CEDIDA
        self.obra._cesion_activa = self
        self.estado = "activa"

    def finalizar(self) -> None:
        self.estado = "finalizada"
        if self.obra._cesion_activa is self:
            self.obra._cesion_activa = None
        self.obra.marcar_expuesta()

    def esta_activa(self) -> bool:
        return self.estado == "activa"


# =========================================================
# Roles de usuario
# =========================================================


class EncargadoCatalogo(Usuario):
    def __init__(
        self,
        id_usuario: int,
        nombre: str,
        correo: str,
        activo: bool,
        contrasena: str,
        codigo_empleado: str,
        catalogo: CatalogoObras,
    ) -> None:
        super().__init__(id_usuario, nombre, correo, activo, contrasena)
        self.codigo_empleado = codigo_empleado
        self.catalogo = catalogo

    def registrar_obra(self, obra: ObraArte) -> None:
        if not self.autenticar():
            raise PermissionError("Debe autenticarse antes de registrar obras.")
        self.catalogo.agregar_obra(obra)

    def actualizar_obra(self, obra: ObraArte) -> None:
        if not self.autenticar():
            raise PermissionError("Debe autenticarse antes de actualizar obras.")
        if obra not in self.catalogo.obras:
            self.catalogo.agregar_obra(obra)
        self.catalogo._tocar()

    def asignar_sala(self, obra: ObraArte, sala: Sala) -> None:
        if not self.autenticar():
            raise PermissionError("Debe autenticarse antes de asignar salas.")
        sala.agregar_obra(obra)


class Director(Usuario):
    def __init__(
        self,
        id_usuario: int,
        nombre: str,
        correo: str,
        activo: bool,
        contrasena: str,
        cargo: str,
        catalogo: CatalogoObras,
    ) -> None:
        super().__init__(id_usuario, nombre, correo, activo, contrasena)
        self.cargo = cargo
        self.catalogo = catalogo
        self.cesiones: list[Cesion] = []
        self.museos_colaboradores: list[MuseoColaborador] = []

    def registrar_museo_colaborador(self, museo: MuseoColaborador) -> None:
        if not self.autenticar():
            raise PermissionError("Debe autenticarse.")
        if museo not in self.museos_colaboradores:
            self.museos_colaboradores.append(museo)

    def ceder_obra(
        self,
        obra: ObraArte,
        museo: MuseoColaborador,
        fecha_inicio: date,
        fecha_fin: date,
        importe_pagado: float,
    ) -> Optional[Cesion]:
        if not self.autenticar():
            raise PermissionError("Debe autenticarse.")
        if obra.estado == ESTADO_CEDIDA:
            obra._cola_solicitudes_cesion.append(museo)
            museo.solicitar_obra()
            return None
        if not obra.esta_disponible_para_cesion():
            raise RuntimeError("La obra no está disponible para cesión.")
        cesion = Cesion(
            obra=obra,
            museo=museo,
            fecha_inicio=fecha_inicio,
            fecha_fin=fecha_fin,
            importe_pagado=importe_pagado,
            estado="pendiente",
        )
        cesion.iniciar()
        self.cesiones.append(cesion)
        museo.recibir_cesion()
        return cesion

    def finalizar_cesion_y_asignar_siguiente(self, cesion: Cesion) -> Optional[Cesion]:
        """Al terminar una cesión, si hay museo en cola, cede la obra al siguiente."""
        if not self.autenticar():
            raise PermissionError("Debe autenticarse.")
        obra = cesion.obra
        duracion = (cesion.fecha_fin - cesion.fecha_inicio).days
        cesion.finalizar()
        if obra._cola_solicitudes_cesion:
            siguiente = obra._cola_solicitudes_cesion.pop(0)
            inicio = date.today()
            nueva_fin = inicio + timedelta(days=duracion)
            return self.ceder_obra(
                obra,
                siguiente,
                fecha_inicio=inicio,
                fecha_fin=nueva_fin,
                importe_pagado=cesion.importe_pagado,
            )
        return None

    def consultar_valor_total(self) -> float:
        if not self.autenticar():
            raise PermissionError("Debe autenticarse.")
        return sum(o.valor_economico for o in self.catalogo.listar_obras())


class RestauradorJefe(Usuario):
    def __init__(
        self,
        id_usuario: int,
        nombre: str,
        correo: str,
        activo: bool,
        contrasena: str,
        especialidad: str,
    ) -> None:
        super().__init__(id_usuario, nombre, correo, activo, contrasena)
        self.especialidad = especialidad
        self._restauraciones_gestionadas: list[Restauracion] = []

    def iniciar_restauracion(
        self,
        obra: ObraArte,
        tipo_restauracion: str,
        fecha_inicio: date,
        motivo: str,
    ) -> Restauracion:
        if not self.autenticar():
            raise PermissionError("Debe autenticarse.")
        r = Restauracion(
            obra=obra,
            tipo_restauracion=tipo_restauracion,
            fecha_inicio=fecha_inicio,
            fecha_fin=None,
            motivo=motivo,
        )
        r.iniciar()
        obra.registrar_restauracion(r)
        self._restauraciones_gestionadas.append(r)
        return r

    def finalizar_restauracion(self, restauracion: Restauracion) -> None:
        if not self.autenticar():
            raise PermissionError("Debe autenticarse.")
        restauracion.finalizar()

    def consultar_restauraciones(self) -> list[Restauracion]:
        """Listado de restauraciones gestionadas por el jefe, por antigüedad (fecha de inicio)."""
        if not self.autenticar():
            raise PermissionError("Debe autenticarse.")
        return sorted(self._restauraciones_gestionadas, key=lambda x: x.fecha_inicio)

    def consultar_restauraciones_por_obra(self, obra: ObraArte) -> list[Restauracion]:
        """Requisito: todas las restauraciones de una obra, ordenadas por antigüedad."""
        if not self.autenticar():
            raise PermissionError("Debe autenticarse.")
        return obra.restauraciones_ordenadas_por_antiguedad()


# =========================================================
# Visitante y monitor del vestíbulo
# =========================================================


class MonitorVestibulo:
    def __init__(self, id_monitor: int, ubicacion: str, salas: list[Sala]) -> None:
        self.id_monitor = id_monitor
        self.ubicacion = ubicacion
        self._salas = {s.id_sala: s for s in salas}

    def mostrar_listado_por_sala(self, id_sala: int) -> list[ObraArte]:
        sala = self._salas.get(id_sala)
        if sala is None:
            return []
        return sala.listar_obras()


class Visitante:
    def __init__(self, tipo_consulta: str, monitor: MonitorVestibulo) -> None:
        self.tipo_consulta = tipo_consulta
        self.monitor = monitor

    def consultar_obras_por_sala(self, id_sala: int) -> list[ObraArte]:
        return self.monitor.mostrar_listado_por_sala(id_sala)


# =========================================================
# Proceso diario de restauración automática (cada 5 años)
# =========================================================


class ProcesoRestauracionAutomatica:
    def __init__(self, catalogo: CatalogoObras) -> None:
        self.catalogo = catalogo

    def identificar_obras_para_restauracion(self, dia: Optional[date] = None) -> list[ObraArte]:
        dia = dia or date.today()
        return [
            o for o in self.catalogo.listar_obras()
            if o.necesita_restauracion_ciclo(dia)
        ]


# =========================================================
# Flujo de demostración
# =========================================================


def mostrar_catalogo_obras(catalogo: CatalogoObras) -> None:
    print("\n=== CATÁLOGO DE OBRAS ===")
    for obra in catalogo.listar_obras():
        print(
            f"ID {obra.id_obra} | {obra.titulo} | {obra.autor} | {obra.periodo} | "
            f"Valor: ${obra.valor_economico:,.0f} | Estado: {obra.estado}"
        )
        print(f"    {obra.mostrar_detalle()}")


def mostrar_obras_en_sala(nombre_sala: str, obras: list[ObraArte]) -> None:
    print(f"\n--- Obras en sala: {nombre_sala} ---")
    if not obras:
        print("  (ninguna)")
        return
    for obra in obras:
        print(f"  - {obra.titulo} - {obra.autor}")


def mostrar_restauraciones_de_obra(obra: ObraArte, lista: list[Restauracion]) -> None:
    print(f"\n--- Restauraciones de '{obra.titulo}' (por antiguedad) ---")
    if not lista:
        print("  (ninguna registrada)")
        return
    for r in lista:
        fin = r.fecha_fin.isoformat() if r.fecha_fin else "en curso"
        print(
            f"  - #{r.id_restauracion} | {r.tipo_restauracion} | "
            f"inicio {r.fecha_inicio} | fin {fin} | motivo: {r.motivo}"
        )


def main() -> None:
    print("\n" + "=" * 60)
    print("SIMULACIÓN: GESTIÓN DEL MUSEO (demostración en consola)")
    print("=" * 60)

    catalogo = CatalogoObras()
    sala_renacimiento = Sala(1, "Renacimiento", "Planta 1 - Ala norte")
    sala_moderna = Sala(2, "Arte moderno", "Planta 2")

    encargado = EncargadoCatalogo(
        id_usuario=1,
        nombre="Ana Martínez",
        correo="ana@museo.org",
        activo=True,
        contrasena="cat2024",
        codigo_empleado="EC-001",
        catalogo=catalogo,
    )

    director = Director(
        id_usuario=2,
        nombre="Luis Herrera",
        correo="director@museo.org",
        activo=True,
        contrasena="dir2024",
        cargo="Director general",
        catalogo=catalogo,
    )

    restaurador = RestauradorJefe(
        id_usuario=3,
        nombre="Elena Vargas",
        correo="restauracion@museo.org",
        activo=True,
        contrasena="res2024",
        especialidad="Pintura sobre lienzo",
    )

    # --- Seguridad: operación sin sesión ---
    print("\n=== INTENTO SIN AUTENTICACIÓN (encargado del catálogo) ===")
    cuadro = Cuadro(
        id_obra=1,
        titulo="Noche estrellada",
        autor="Vincent van Gogh",
        periodo="s. XIX",
        valor_economico=850_000_000.0,
        fecha_creacion=date(1889, 6, 1),
        fecha_ingreso=date(2010, 3, 15),
        estado=ESTADO_EXPUESTA,
        tecnica="óleo sobre lienzo",
        estilo="postimpresionismo",
    )
    try:
        encargado.registrar_obra(cuadro)
    except PermissionError as e:
        print(f"Rechazado (correcto): {e}")

    # --- Encargado: alta de obras y asignación a salas ---
    print("\n=== ENCARGADO DEL CATÁLOGO: sesión y registro de obras ===")
    encargado.iniciar_sesion("cat2024")
    print(f"Sesión iniciada: {encargado.obtener_datos()}")

    escultura = Escultura(
        id_obra=2,
        titulo="El pensador",
        autor="Auguste Rodin",
        periodo="s. XIX",
        valor_economico=420_000_000.0,
        fecha_creacion=date(1902, 1, 1),
        fecha_ingreso=date(2005, 6, 1),
        estado=ESTADO_EXPUESTA,
        material="bronce",
        estilo="modernista",
    )
    otro = OtroObjeto(
        id_obra=3,
        titulo="Sarcófago etrusco",
        autor="Anónimo",
        periodo="s. III a.C.",
        valor_economico=95_000_000.0,
        fecha_creacion=date(1750, 1, 1),
        fecha_ingreso=date(1998, 11, 20),
        estado=ESTADO_EXPUESTA,
        tipo_objeto="pieza arqueológica",
    )

    encargado.registrar_obra(cuadro)
    encargado.registrar_obra(escultura)
    encargado.registrar_obra(otro)
    encargado.asignar_sala(cuadro, sala_renacimiento)
    encargado.asignar_sala(escultura, sala_moderna)
    encargado.asignar_sala(otro, sala_renacimiento)

    mostrar_catalogo_obras(catalogo)
    print(f"\nCatálogo actualizado el: {catalogo.fecha_actualizacion}")

    # --- Visitante: monitor del vestíbulo (sin login de usuario interno) ---
    print("\n=== VISITANTE: consulta en monitor del vestíbulo ===")
    monitor = MonitorVestibulo(
        id_monitor=1,
        ubicacion="Vestíbulo principal",
        salas=[sala_renacimiento, sala_moderna],
    )
    visitante = Visitante(tipo_consulta="por_sala", monitor=monitor)
    mostrar_obras_en_sala(
        sala_renacimiento.nombre,
        visitante.consultar_obras_por_sala(sala_renacimiento.id_sala),
    )
    mostrar_obras_en_sala(
        sala_moderna.nombre,
        visitante.consultar_obras_por_sala(sala_moderna.id_sala),
    )

    # --- Proceso diario: obras que entran en ciclo de 5 años ---
    print("\n=== PROCESO DIARIO: RESTAURACIÓN AUTOMÁTICA (cada 5 años) ===")
    proceso = ProcesoRestauracionAutomatica(catalogo)
    candidatas = proceso.identificar_obras_para_restauracion()
    print(
        f"Obras que, según la fecha de hoy ({date.today()}), "
        f"deben pasar a revisión/restauración periódica: {len(candidatas)}"
    )
    for o in candidatas:
        print(f"  - ID {o.id_obra} - {o.titulo} (referencia ciclo desde {o.fecha_referencia_proximo_ciclo_restauracion()})")

    # --- Restaurador jefe: daño inmediato + ciclo; consulta histórico ---
    print("\n=== RESTAURADOR JEFE: restauraciones ===")
    restaurador.iniciar_sesion("res2024")
    cuadro.estado = ESTADO_DAÑADA
    rest_urgente = restaurador.iniciar_restauracion(
        obra=cuadro,
        tipo_restauracion="consolidación de capa pictórica",
        fecha_inicio=date.today(),
        motivo="daño por humedad en sala (envío inmediato)",
    )
    print(
        f"Restauración urgente #{rest_urgente.id_restauracion} iniciada | "
        f"obra en estado: {cuadro.estado}"
    )
    restaurador.finalizar_restauracion(rest_urgente)
    print(
        f"Restauración #{rest_urgente.id_restauracion} finalizada | "
        f"fin: {rest_urgente.fecha_fin} | obra: {cuadro.estado}"
    )

    rest_ciclo = restaurador.iniciar_restauracion(
        obra=escultura,
        tipo_restauracion="limpieza y pátina",
        fecha_inicio=date.today(),
        motivo="mantenimiento periódico",
    )
    restaurador.finalizar_restauracion(rest_ciclo)

    mostrar_restauraciones_de_obra(
        cuadro,
        restaurador.consultar_restauraciones_por_obra(cuadro),
    )
    print(
        "\n--- Todas las restauraciones gestionadas por el jefe (por antigüedad) ---"
    )
    for r in restaurador.consultar_restauraciones():
        print(
            f"  - #{r.id_restauracion} | obra ID {r.obra.id_obra} | "
            f"{r.tipo_restauracion} | {r.fecha_inicio}"
        )

    # --- Director: museos colaboradores, cesión y cola ---
    print("\n=== DIRECTOR: museos colaboradores y cesiones ===")
    director.iniciar_sesion("dir2024")
    museo_bogota = MuseoColaborador(1, "Museo Nacional de Colombia", "Bogotá", "Colombia")
    museo_madrid = MuseoColaborador(2, "Museo Thyssen-Bornemisza", "Madrid", "España")
    director.registrar_museo_colaborador(museo_bogota)
    director.registrar_museo_colaborador(museo_madrid)
    print(f"Museos colaboradores registrados: {len(director.museos_colaboradores)}")

    inicio_cesion = date.today()
    fin_cesion = inicio_cesion + timedelta(days=180)
    cesion_1 = director.ceder_obra(
        obra=otro,
        museo=museo_bogota,
        fecha_inicio=inicio_cesion,
        fecha_fin=fin_cesion,
        importe_pagado=12_500_000.0,
    )
    print(
        f"Cesion #{cesion_1.id_cesion if cesion_1 else '?'} | "
        f"obra '{otro.titulo}' -> {museo_bogota.nombre} | "
        f"importe ${cesion_1.importe_pagado:,.0f} | estado obra: {otro.estado}"
    )

    cesion_en_cola = director.ceder_obra(
        obra=otro,
        museo=museo_madrid,
        fecha_inicio=inicio_cesion,
        fecha_fin=fin_cesion,
        importe_pagado=0.0,
    )
    if cesion_en_cola is None:
        print(
            "Segundo museo solicitó la misma obra mientras sigue cedida: "
            f"queda en cola ({len(otro._cola_solicitudes_cesion)} pendiente(s))."
        )

    # Simular fin de cesión y paso al siguiente museo (fecha_fin ya estaba fijada al crear la cesión)
    cesion_siguiente = director.finalizar_cesion_y_asignar_siguiente(cesion_1)
    if cesion_siguiente:
        print(
            f"Tras finalizar la primera cesion: nueva cesion #{cesion_siguiente.id_cesion} "
            f"-> {cesion_siguiente.museo.nombre} | estado: {cesion_siguiente.estado}"
        )
    else:
        print("No hubo cesión siguiente (cola vacía o obra no disponible).")

    # --- Valor total del museo ---
    print("\n=== VALORACIÓN TOTAL DEL MUSEO ===")
    print(f"Suma de valores de todas las obras: ${director.consultar_valor_total():,.0f}")

    encargado.cerrar_sesion()
    director.cerrar_sesion()
    restaurador.cerrar_sesion()
    print("\n=== FIN DE LA SIMULACIÓN ===\n")


if __name__ == "__main__":
    main()
