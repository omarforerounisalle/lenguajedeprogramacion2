# Manual de pruebas unitarias

Este manual describe cómo ejecutar la suite de pruebas, su organización
y el catálogo completo de **21 casos de prueba** (el criterio del
proyecto exige mínimo 10).

> Las pruebas usan únicamente la librería estándar (`unittest`), por lo
> que **no requieren instalar dependencias**.

---

## 1. Requisitos

- Python 3.10 o superior.
- Tener clonado el repositorio y posicionarse en `ProyectoFinal/`.

## 2. Ejecución

Desde la carpeta `ProyectoFinal/`:

```powershell
python -m unittest discover -s tests -v
```

Si se quiere ejecutar un único archivo:

```powershell
python -m unittest tests.test_controller -v
```

Resultado esperado (resumen):

```
----------------------------------------------------------------------
Ran 21 tests in 0.045s

OK
```

## 3. Organización

| Archivo | Propósito | Casos |
|---------|-----------|-------|
| `tests/test_estudiante.py`     | Validaciones del modelo de dominio    | 6 |
| `tests/test_email_service.py`  | Cadena GoF Decorator + EmailService   | 6 |
| `tests/test_controller.py`     | CRUD, duplicados, regla R1, persistencia | 9 |
| **Total** |  | **21** |

Cada caso usa **archivos temporales** (`tempfile.TemporaryDirectory`)
para no contaminar el archivo real `data/estudiantes.json`.

---

## 4. Catálogo de casos

### 4.1 `test_estudiante.py` — Validaciones del modelo

| # | Caso | Entrada | Resultado esperado |
|---|------|---------|--------------------|
| 1 | `test_creacion_valida` | Datos correctos | Instancia creada, `activo == True` |
| 2 | `test_id_negativo_lanza_validacion` | `id_estudiante = -1` | `ValidacionError(campo="id_estudiante")` |
| 3 | `test_nombre_vacio_lanza_validacion` | `nombre = "   "` | `ValidacionError` |
| 4 | `test_correo_invalido_lanza_excepcion_especifica` | `correo = "no-es-correo"` | `CorreoInvalidoError` |
| 5 | `test_promedio_fuera_de_rango_lanza_excepcion` | `promedio = 5.5` y `-0.1` | `PromedioInvalidoError` |
| 6 | `test_roundtrip_dict` | `to_dict()` → `from_dict()` | Instancia equivalente |

### 4.2 `test_email_service.py` — Patrón Decorator + EmailService

| # | Caso | Entrada | Resultado esperado |
|---|------|---------|--------------------|
| 7  | `test_base_sin_decoradores` | `NotificacionBase().enviar("hola")` | `"[InApp] hola"` |
| 8  | `test_email_envuelve_base` | `EmailDecorator(NotificacionBase()).enviar("hola")` | `"[InApp] hola + [Email]"` |
| 9  | `test_cadena_de_tres_decoradores` | Email + SMS + WhatsApp | `"[InApp] hola + [Email] + [SMS] + [WhatsApp]"` |
| 10 | `test_notificar_creacion_envia_correo_y_traza` | `EmailService` con sender mock | 1 correo capturado, traza contiene `[Email]` |
| 11 | `test_agregar_sms_incluye_canal_en_la_traza` | `agregar_sms()` + notificar actualización | Traza contiene `[Email]` y `[SMS]` |
| 12 | `test_historial_se_acumula` | 1 creación + 1 actualización | `len(historial) == 2` |

### 4.3 `test_controller.py` — CRUD + regla de negocio

| # | Caso | Acción | Resultado esperado |
|---|------|--------|--------------------|
| 13 | `test_crear_y_listar` | Crear 1 estudiante | `id=1`, `len(listar()) == 1`, 1 correo enviado |
| 14 | `test_crear_correo_duplicado_lanza` | Crear con `dup@x.com` luego con `DUP@X.COM` | `EstudianteDuplicadoError` |
| 15 | `test_actualizar_envia_notificacion` | Cambiar programa | Entidad actualizada + 2 correos |
| 16 | `test_eliminar_quita_del_repositorio` | `eliminar(id)` | `listar() == []` y `obtener(id)` lanza `EstudianteNoEncontradoError` |
| 17 | `test_obtener_inexistente_lanza` | `obtener(999)` | `EstudianteNoEncontradoError` |
| 18 | `test_matricular_con_promedio_suficiente` | `promedio = 3.2` + desmatricular + matricular | `activo == True` |
| 19 | `test_matricular_con_promedio_bajo_lanza_regla_negocio` | `promedio = 2.4` + matricular | `ReglaNegocioError`, `regla == "R1-MatriculaPromedio"` |
| 20 | `test_matricular_es_idempotente` | Estudiante ya activo + matricular | `activo == True` (sin error, sin cambios) |
| 21 | `test_persistencia_sobrevive_reapertura` | Crear, instanciar nuevo controller sobre el mismo JSON | `len(listar()) == 1` |

---

## 5. Salida completa de la última corrida

```text
test_actualizar_envia_notificacion (test_controller.TestEstudianteController.test_actualizar_envia_notificacion) ... ok
test_crear_correo_duplicado_lanza (test_controller.TestEstudianteController.test_crear_correo_duplicado_lanza) ... ok
test_crear_y_listar (test_controller.TestEstudianteController.test_crear_y_listar) ... ok
test_eliminar_quita_del_repositorio (test_controller.TestEstudianteController.test_eliminar_quita_del_repositorio) ... ok
test_matricular_con_promedio_bajo_lanza_regla_negocio (test_controller.TestEstudianteController.test_matricular_con_promedio_bajo_lanza_regla_negocio) ... ok
test_matricular_con_promedio_suficiente (test_controller.TestEstudianteController.test_matricular_con_promedio_suficiente) ... ok
test_matricular_es_idempotente (test_controller.TestEstudianteController.test_matricular_es_idempotente) ... ok
test_obtener_inexistente_lanza (test_controller.TestEstudianteController.test_obtener_inexistente_lanza) ... ok
test_persistencia_sobrevive_reapertura (test_controller.TestEstudianteController.test_persistencia_sobrevive_reapertura) ... ok
test_agregar_sms_incluye_canal_en_la_traza (test_email_service.TestEmailService.test_agregar_sms_incluye_canal_en_la_traza) ... ok
test_historial_se_acumula (test_email_service.TestEmailService.test_historial_se_acumula) ... ok
test_notificar_creacion_envia_correo_y_traza (test_email_service.TestEmailService.test_notificar_creacion_envia_correo_y_traza) ... ok
test_base_sin_decoradores (test_email_service.TestNotificacionDecorator.test_base_sin_decoradores) ... ok
test_cadena_de_tres_decoradores (test_email_service.TestNotificacionDecorator.test_cadena_de_tres_decoradores) ... ok
test_email_envuelve_base (test_email_service.TestNotificacionDecorator.test_email_envuelve_base) ... ok
test_correo_invalido_lanza_excepcion_especifica (test_estudiante.TestEstudiante.test_correo_invalido_lanza_excepcion_especifica) ... ok
test_creacion_valida (test_estudiante.TestEstudiante.test_creacion_valida) ... ok
test_id_negativo_lanza_validacion (test_estudiante.TestEstudiante.test_id_negativo_lanza_validacion) ... ok
test_nombre_vacio_lanza_validacion (test_estudiante.TestEstudiante.test_nombre_vacio_lanza_validacion) ... ok
test_promedio_fuera_de_rango_lanza_excepcion (test_estudiante.TestEstudiante.test_promedio_fuera_de_rango_lanza_excepcion) ... ok
test_roundtrip_dict (test_estudiante.TestEstudiante.test_roundtrip_dict) ... ok

----------------------------------------------------------------------
Ran 21 tests in 0.045s

OK
```

## 6. Trazabilidad con los criterios del proyecto

| Criterio | Cubierto por |
|----------|--------------|
| 3 — Validaciones | Casos 1–6 |
| 4 — Excepciones personalizadas | Casos 2, 3, 4, 5, 14, 16, 17, 19 |
| 5 — Regla de negocio (R1) | Casos 18, 19, 20 |
| 6 — Mínimo 10 casos + regla del punto 5 | 21 casos totales, incluyendo R1 |
| 7 — EmailService con Decorator | Casos 7–12, 13, 15 |
| 8 — Persistencia JSON | Caso 21 (sobrevive a reapertura) |
