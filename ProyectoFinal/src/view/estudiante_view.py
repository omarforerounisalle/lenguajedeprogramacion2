"""Vista Tkinter del CRUD de Estudiantes.

Aplica las **Heurísticas de Nielsen** más relevantes para este formulario:

* **Visibilidad del estado del sistema:** barra de estado en la parte
  inferior con el resultado de cada operación.
* **Coincidencia entre el sistema y el mundo real:** etiquetas en
  español y mensajes claros (no mostramos *stack traces*).
* **Control y libertad del usuario:** botón "Limpiar" siempre disponible
  para deshacer la edición y volver al modo creación.
* **Consistencia y estándares:** el mismo formulario sirve para crear y
  para editar; los botones cambian de etiqueta según el modo.
* **Prevención de errores:** validación previa al enviar y confirmación
  obligatoria antes de eliminar.
* **Reconocimiento mejor que recuerdo:** la tabla muestra todos los
  campos relevantes y se autoselecciona el estudiante editado.
* **Ayuda a reconocer, diagnosticar y recuperarse de errores:** mensajes
  amigables (no técnicos) mostrados en diálogos y en la barra de estado.
"""
from __future__ import annotations

import tkinter as tk
from tkinter import messagebox, ttk

from src.controller import EstudianteController
from src.exceptions import DominioError


class EstudianteView(tk.Tk):
    """Ventana principal del CRUD de Estudiantes."""

    _COLUMNAS = (
        ("id_estudiante", "ID", 50),
        ("nombre", "Nombre", 120),
        ("apellido", "Apellido", 120),
        ("correo", "Correo", 200),
        ("programa", "Programa", 160),
        ("promedio", "Promedio", 80),
        ("activo", "Activo", 70),
    )

    def __init__(self, controller: EstudianteController) -> None:
        super().__init__()
        self.controller = controller
        self._id_en_edicion: int | None = None

        self.title("Sistema Académico - CRUD Estudiantes")
        self.geometry("980x560")
        self.minsize(880, 520)

        self._construir_formulario()
        self._construir_tabla()
        self._construir_botones()
        self._construir_barra_estado()
        self.recargar_tabla()

    # ----- Construcción de widgets -------------------------------------
    def _construir_formulario(self) -> None:
        marco = ttk.LabelFrame(self, text="Datos del estudiante", padding=10)
        marco.pack(fill="x", padx=10, pady=(10, 5))

        self.vars: dict[str, tk.Variable] = {
            "nombre": tk.StringVar(),
            "apellido": tk.StringVar(),
            "correo": tk.StringVar(),
            "programa": tk.StringVar(),
            "promedio": tk.StringVar(),
            "telefono": tk.StringVar(),
            "direccion": tk.StringVar(),
            "activo": tk.BooleanVar(value=True),
        }

        campos = [
            ("Nombre*",   "nombre"),
            ("Apellido*", "apellido"),
            ("Correo*",   "correo"),
            ("Programa*", "programa"),
            ("Promedio* (0.0 - 5.0)", "promedio"),
            ("Teléfono",  "telefono"),
            ("Dirección", "direccion"),
        ]
        for i, (etiqueta, clave) in enumerate(campos):
            fila, col = divmod(i, 2)
            ttk.Label(marco, text=etiqueta).grid(row=fila, column=col * 2, sticky="w", padx=4, pady=4)
            ttk.Entry(marco, textvariable=self.vars[clave], width=30).grid(
                row=fila, column=col * 2 + 1, sticky="we", padx=4, pady=4
            )
        marco.columnconfigure(1, weight=1)
        marco.columnconfigure(3, weight=1)

        ttk.Checkbutton(marco, text="Activo", variable=self.vars["activo"]).grid(
            row=len(campos) // 2 + 1, column=0, columnspan=2, sticky="w", padx=4, pady=4
        )

    def _construir_tabla(self) -> None:
        marco = ttk.Frame(self)
        marco.pack(fill="both", expand=True, padx=10, pady=5)

        self.tabla = ttk.Treeview(
            marco,
            columns=[c[0] for c in self._COLUMNAS],
            show="headings",
            selectmode="browse",
        )
        for clave, titulo, ancho in self._COLUMNAS:
            self.tabla.heading(clave, text=titulo)
            self.tabla.column(clave, width=ancho, anchor="w")

        scroll = ttk.Scrollbar(marco, orient="vertical", command=self.tabla.yview)
        self.tabla.configure(yscrollcommand=scroll.set)

        self.tabla.pack(side="left", fill="both", expand=True)
        scroll.pack(side="right", fill="y")

        self.tabla.bind("<<TreeviewSelect>>", self._al_seleccionar)

    def _construir_botones(self) -> None:
        marco = ttk.Frame(self)
        marco.pack(fill="x", padx=10, pady=5)

        self.btn_guardar = ttk.Button(marco, text="Crear", command=self._guardar)
        self.btn_guardar.pack(side="left", padx=4)
        ttk.Button(marco, text="Limpiar", command=self._modo_creacion).pack(side="left", padx=4)
        ttk.Button(marco, text="Eliminar", command=self._eliminar).pack(side="left", padx=4)
        ttk.Separator(marco, orient="vertical").pack(side="left", fill="y", padx=8)
        ttk.Button(marco, text="Matricular (R1)", command=self._matricular).pack(side="left", padx=4)
        ttk.Button(marco, text="Desmatricular", command=self._desmatricular).pack(side="left", padx=4)
        ttk.Button(marco, text="Recargar", command=self.recargar_tabla).pack(side="right", padx=4)

    def _construir_barra_estado(self) -> None:
        self.estado_var = tk.StringVar(value="Listo.")
        ttk.Label(self, textvariable=self.estado_var, relief="sunken", anchor="w").pack(
            fill="x", side="bottom"
        )

    # ----- Manejo de estado --------------------------------------------
    def _modo_creacion(self) -> None:
        self._id_en_edicion = None
        for clave, var in self.vars.items():
            var.set(True if clave == "activo" else "")
        if self.tabla.selection():
            self.tabla.selection_remove(self.tabla.selection())
        self.btn_guardar.configure(text="Crear")
        self._estado("Modo creación.")

    def _modo_edicion(self, est) -> None:
        self._id_en_edicion = est.id_estudiante
        self.vars["nombre"].set(est.nombre)
        self.vars["apellido"].set(est.apellido)
        self.vars["correo"].set(est.correo)
        self.vars["programa"].set(est.programa)
        self.vars["promedio"].set(f"{est.promedio:.2f}")
        self.vars["telefono"].set(est.telefono or "")
        self.vars["direccion"].set(est.direccion or "")
        self.vars["activo"].set(est.activo)
        self.btn_guardar.configure(text="Actualizar")
        self._estado(f"Editando estudiante id={est.id_estudiante}.")

    # ----- Acciones ----------------------------------------------------
    def _guardar(self) -> None:
        try:
            promedio = float(self.vars["promedio"].get() or 0)
        except ValueError:
            messagebox.showwarning("Dato inválido", "El promedio debe ser un número.")
            return

        try:
            if self._id_en_edicion is None:
                creado = self.controller.crear(
                    nombre=self.vars["nombre"].get(),
                    apellido=self.vars["apellido"].get(),
                    correo=self.vars["correo"].get(),
                    programa=self.vars["programa"].get(),
                    promedio=promedio,
                    telefono=self.vars["telefono"].get() or None,
                    direccion=self.vars["direccion"].get() or None,
                )
                self._estado(f"Estudiante creado (id={creado.id_estudiante}).")
            else:
                actualizado = self.controller.actualizar(
                    self._id_en_edicion,
                    nombre=self.vars["nombre"].get(),
                    apellido=self.vars["apellido"].get(),
                    correo=self.vars["correo"].get(),
                    programa=self.vars["programa"].get(),
                    promedio=promedio,
                    telefono=self.vars["telefono"].get() or None,
                    direccion=self.vars["direccion"].get() or None,
                    activo=bool(self.vars["activo"].get()),
                )
                self._estado(f"Estudiante actualizado (id={actualizado.id_estudiante}).")
        except DominioError as exc:
            messagebox.showerror("Error", str(exc))
            self._estado(f"Error: {exc}")
            return

        self._modo_creacion()
        self.recargar_tabla()

    def _eliminar(self) -> None:
        seleccionado = self._id_seleccionado()
        if seleccionado is None:
            messagebox.showinfo("Eliminar", "Selecciona primero un estudiante en la tabla.")
            return
        if not messagebox.askyesno("Confirmar", f"¿Eliminar el estudiante id={seleccionado}?"):
            return
        try:
            self.controller.eliminar(seleccionado)
            self._estado(f"Estudiante eliminado (id={seleccionado}).")
        except DominioError as exc:
            messagebox.showerror("Error", str(exc))
            self._estado(f"Error: {exc}")
            return
        self._modo_creacion()
        self.recargar_tabla()

    def _matricular(self) -> None:
        seleccionado = self._id_seleccionado()
        if seleccionado is None:
            messagebox.showinfo("Matricular", "Selecciona primero un estudiante.")
            return
        try:
            self.controller.matricular(seleccionado)
            self._estado(f"Estudiante matriculado (id={seleccionado}).")
        except DominioError as exc:
            messagebox.showerror("Regla de negocio", str(exc))
            self._estado(f"Regla R1 bloqueó la matrícula: {exc}")
            return
        self.recargar_tabla()

    def _desmatricular(self) -> None:
        seleccionado = self._id_seleccionado()
        if seleccionado is None:
            messagebox.showinfo("Desmatricular", "Selecciona primero un estudiante.")
            return
        try:
            self.controller.desmatricular(seleccionado)
            self._estado(f"Estudiante desmatriculado (id={seleccionado}).")
        except DominioError as exc:
            messagebox.showerror("Error", str(exc))
            return
        self.recargar_tabla()

    # ----- Sincronización con la tabla ---------------------------------
    def recargar_tabla(self) -> None:
        for iid in self.tabla.get_children():
            self.tabla.delete(iid)
        for est in self.controller.listar():
            self.tabla.insert(
                "",
                "end",
                iid=str(est.id_estudiante),
                values=(
                    est.id_estudiante,
                    est.nombre,
                    est.apellido,
                    est.correo,
                    est.programa,
                    f"{est.promedio:.2f}",
                    "Sí" if est.activo else "No",
                ),
            )

    def _id_seleccionado(self) -> int | None:
        seleccion = self.tabla.selection()
        return int(seleccion[0]) if seleccion else None

    def _al_seleccionar(self, _evento) -> None:
        id_sel = self._id_seleccionado()
        if id_sel is None:
            return
        try:
            est = self.controller.obtener(id_sel)
        except DominioError as exc:
            self._estado(f"Error al cargar: {exc}")
            return
        self._modo_edicion(est)

    # ----- Utilidades ---------------------------------------------------
    def _estado(self, texto: str) -> None:
        self.estado_var.set(texto)
