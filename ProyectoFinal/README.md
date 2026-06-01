# Proyecto Final - Sistema Académico (Python)

Aplicación CRUD de **Estudiantes** construida con arquitectura **MVC**, persistencia en **JSON**, servicio de notificación por correo implementado con el patrón **GoF Decorator** e interfaz gráfica con **Tkinter**.

## Estructura

```
ProyectoFinal/
├── src/
│   ├── model/          # Entidades de dominio (Estudiante)
│   ├── view/           # Interfaz gráfica Tkinter
│   ├── controller/     # Coordinación model <-> view + reglas de negocio
│   ├── repository/     # Persistencia JSON
│   ├── services/       # EmailService + decoradores de notificación
│   ├── exceptions/     # Excepciones personalizadas del dominio
│   └── main.py         # Punto de entrada de la aplicación
├── tests/              # Pruebas unitarias (unittest)
├── data/               # Archivo JSON con los datos persistidos
└── docs/               # Diagramas BPMN, clases, estados y manual
```

## Requisitos

- Python 3.10+
- Tkinter (incluido con Python en Windows)

## Ejecución

```powershell
cd ProyectoFinal
python -m src.main
```

## Pruebas

```powershell
cd ProyectoFinal
python -m unittest discover -s tests -v
```

## Criterios de evaluación cubiertos

| # | Criterio | Implementación |
|---|----------|----------------|
| 1 | Patrón arquitectónico MVC | `src/model`, `src/view`, `src/controller` |
| 2 | CRUD | `EstudianteController` (crear, leer, actualizar, eliminar, listar) |
| 3 | Validaciones de dominio | `Estudiante.__post_init__` + `_validar()` |
| 4 | Excepciones personalizadas | `src/exceptions/dominio.py` |
| 5 | Regla de negocio | `EstudianteController.matricular()` (promedio mínimo 3.0) |
| 6 | 10+ pruebas unitarias | `tests/` (15 casos) |
| 7 | EmailService con Decorator | `src/services/email_service.py` + `notificacion_decorator.py` |
| 8 | Persistencia | `src/repository/estudiante_repository.py` (JSON) |
| 9 | UI (Tkinter + Heurísticas de Nielsen) | `src/view/estudiante_view.py` |
| 10 | Buenas prácticas + múltiples commits | Historial de git |
