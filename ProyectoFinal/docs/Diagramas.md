# Diagramas del proyecto

Los diagramas están escritos en **Mermaid**, que GitHub renderiza
automáticamente en la vista web del repositorio.

---

## 1. Mapa de procesos BPMN — *Matricular estudiante* (Regla R1)

El proceso de matrícula evidencia la regla de negocio **R1: el promedio
del estudiante debe ser mayor o igual a 3.0**. Si no se cumple, el
proceso termina con un evento de error y se notifica al usuario.

```mermaid
flowchart LR
    inicio([Inicio]) --> sel[Seleccionar estudiante]
    sel --> obtiene[Obtener datos del repositorio]
    obtiene --> decision{¿promedio >= 3.0?}
    decision -- No --> err[/Mostrar ReglaNegocioError R1/]
    err --> finErr([Fin con error])
    decision -- Sí --> actualizar[Marcar activo = true]
    actualizar --> persistir[(Persistir en JSON)]
    persistir --> email[/EmailService notifica actualización/]
    email --> fin([Fin OK])
```

### Flujo CRUD general (sub-proceso *Gestionar Estudiante*)

```mermaid
flowchart TD
    A([Inicio]) --> B{Acción}
    B -->|Crear| C[Validar campos] --> C2{¿correo duplicado?}
    C2 -- Sí --> EX1[/EstudianteDuplicadoError/] --> Z([Fin])
    C2 -- No --> C3[Asignar id + persistir] --> C4[EmailService notifica creación] --> Z
    B -->|Editar| D[Cargar por id] --> D2[Aplicar cambios] --> D3[Persistir] --> D4[EmailService notifica actualización] --> Z
    B -->|Eliminar| E[Confirmar] --> E2[Borrar del repositorio] --> Z
    B -->|Matricular| F[Sub-proceso R1] --> Z
```

---

## 2. Diagrama de clases

```mermaid
classDiagram
    class Estudiante {
        +int id_estudiante
        +str nombre
        +str apellido
        +str correo
        +str programa
        +float promedio
        +str telefono
        +str direccion
        +bool activo
        +to_dict()
        +from_dict(d)
        -_validar()
    }

    class EstudianteRepository {
        -Path _ruta
        +crear(e)
        +listar()
        +obtener(id)
        +buscar_por_correo(correo)
        +actualizar(e)
        +eliminar(id)
        +siguiente_id()
    }

    class EstudianteController {
        -EstudianteRepository _repositorio
        -EmailService _email
        +crear(...)
        +listar()
        +obtener(id)
        +actualizar(id, ...)
        +eliminar(id)
        +matricular(id)
        +desmatricular(id)
    }

    class EstudianteView {
        -EstudianteController controller
        +recargar_tabla()
    }

    class INotificacion {
        <<interface>>
        +enviar(mensaje)
    }
    class NotificacionBase
    class NotificacionDecorator {
        <<abstract>>
        -INotificacion _wrappee
    }
    class EmailDecorator
    class SmsDecorator
    class WhatsAppDecorator

    class EmailService {
        -INotificacion _cadena
        -Callable _sender
        +notificar_creacion(...)
        +notificar_actualizacion(...)
        +agregar_sms()
        +agregar_whatsapp()
    }

    class DominioError {
        <<exception>>
    }
    class ValidacionError
    class CorreoInvalidoError
    class PromedioInvalidoError
    class EstudianteNoEncontradoError
    class EstudianteDuplicadoError
    class ReglaNegocioError

    INotificacion <|.. NotificacionBase
    INotificacion <|.. NotificacionDecorator
    NotificacionDecorator <|-- EmailDecorator
    NotificacionDecorator <|-- SmsDecorator
    NotificacionDecorator <|-- WhatsAppDecorator
    NotificacionDecorator o-- INotificacion : wrappee

    EmailService --> INotificacion : _cadena
    EstudianteController --> EstudianteRepository
    EstudianteController --> EmailService
    EstudianteRepository ..> Estudiante : devuelve
    EstudianteView --> EstudianteController

    DominioError <|-- ValidacionError
    DominioError <|-- EstudianteNoEncontradoError
    DominioError <|-- EstudianteDuplicadoError
    DominioError <|-- ReglaNegocioError
    ValidacionError <|-- CorreoInvalidoError
    ValidacionError <|-- PromedioInvalidoError
```

---

## 3. Diagrama de estados del Estudiante

```mermaid
stateDiagram-v2
    [*] --> Registrado : crear()

    Registrado --> Activo : matricular() (R1: promedio >= 3.0)
    Registrado --> Bloqueado : matricular() fallida (R1 viola)

    Activo --> Inactivo : desmatricular()
    Inactivo --> Activo : matricular() (R1 cumple)

    Activo --> Eliminado : eliminar()
    Inactivo --> Eliminado : eliminar()
    Bloqueado --> Activo : actualizar(promedio>=3.0) + matricular()
    Bloqueado --> Eliminado : eliminar()

    Eliminado --> [*]

    note right of Bloqueado
        Estado lógico: el estudiante existe en el JSON
        pero la regla de negocio R1 impide matricularlo.
        Para salir, debe actualizarse el promedio.
    end note
```
