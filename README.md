# Proyecto

---

## 1. Cambios en la Base de Datos

Vi que le faltaban columnas para que el sistema funcionara

### Tabla `medicamento` — Se agregaron 10 columnas nuevas

La tabla original tenía (`id_medicamento`, `tipo_medicamento`, `precio`, `unidad_medida`, `formula`, `stock_medicamento`, `contenido_unidad`, `nombre_medicamento`). 

Le añadí estas columnas extra para poder entre pastillas y líquidos, genéricos y de marca:

| Columna nueva | Para qué sirve                                                    |
|---|-------------------------------------------------------------------|
| `tipo_forma` | Dice si es "pastilla" o "liquido"                                 |
| `tipo_comercial` | Dice si es "generico" o "marca"                                   |
| `ingrediente_activo` | Para los genéricos: el principio activo (ej: "Paracetamol 500mg") |
| `laboratorio` | Para los genéricos: quién lo fabrica                              |
| `marca` | Para los de marca: el nombre comercial (ej: "Tylenol")            |
| `patente` | Para los de marca: número de patente                              |
| `volumen_ml` | Para líquidos: cuántos mililitros trae                            |
| `tipo_liquido` | Para líquidos: si es "jarabe", etc.                               |
| `cantidad_unidades` | Para pastillas: cuántas tabletas/cápsulas vienen                  |
| `tipo_pastilla` | Para pastillas: si es "tableta", "capsula", etc.                  |

**¿Por qué?** 

Porque antes el sistema pensaba que todo igual. Ahora cuando guarda un medicamento, la base de datos guarda solo los datos que son. Los campos que no aplican se guardan como `NULL`.

---
### Tabla `lote` — Se agregó la columna `cantidad`

La tabla original no tenía forma de saber cuántas unidades había en cada lote. Se agregó:

| Columna nueva | Para qué sirve |
|---|---|
| `cantidad` | Cuántas unidades trae ese lote específico |

**¿Por qué?** 

Porque un lote sin cantidad no sirve.

---
### Tabla `proveedor` — Se agregó la columna `estado` 
La clase `Proveedor` tiene un campo `estado` que dice si el proveedor está "Activo" o "Inactivo". La clase `ProveedorDAO` usa esta columna en las consultas.

---

### Datos de prueba que añadi

El archivo `db_migration.sql` ya viene con datos de ejemplo para que no arranquen con la base de datos vacía:
- **3 proveedores:** Drogueria Nacional, FarmaCol SAS, Medifar Ltda
- **8 medicamentos:** Acetaminofén, Ibuprofeno, Amoxicilina, Loratadina, Omeprazol, Metformina, Losartán, Diclofenaco
- **5 lotes** vinculados a medicamentos y proveedores
- **2 roles:** Administrador y Cajero

---

## 2. Medicamentos

Antes solo usaba una clase `Medicamento`. Ahora esta es la jerarquía que use:

```
Medicamento (clase base, package medicamentos)
    │
    ├── MedicamentoPastilla (clase abstracta, package medicamentos)
    │       │
    │       ├── PastillaGenerica (package entidades)
    │       └── PastillaMarca (package entidades)
    │
    └── MedicamentoLiquido (clase abstracta, package medicamentos)
            │
            ├── LiquidoGenerico (package entidades)
            └── LiquidoMarca (package entidades)
```

**¿Por qué?**
- `medicamentos/`: tiene las clases base y abstractas 
- `entidades/`: tiene las clases concretas que se guardan en la base de datos 

**Interfaces que se agregaron:**
- `interfaces.Generico`: define `getActiveIngredient()`, `getLaboratory()` y `getCommercialInfo()` para medicamentos genéricos
- `interfaces.Marca`: define `getBrand()`, `getPatent()` y `getCommercialInfo()` para medicamentos de marca
- `interfaces.TipoComercial`: interfaz base que comparten ambas
- `interfaces.Autenticable`: define `logIn()` y `logOut()` para el sistema de login

---

## 3. Nuevos DAOs

Antes solo existían `MedicamentoDAO` y `LoteInventarioDAO`.

---
### `data/interfaces/CrudSimpleInterface.java`
Una interfaz genérica que define los métodos básicos que TODOS los DAOs deben tener: `listar`, `insertar`, `actualizar`, `total`, `existe`.

---
### `data/FacturaDAO.java`
Maneja todo lo relacionado con facturas y ventas. Métodos:
- `getVentasHoy()` → Cuánto vendió hoy en pesos
- `getVentasSemanales()` → Lista con las ventas de los últimos 7 días (para el gráfico de barras)
- `getFacturasHoy()` → Cuántas facturas se hicieron
- `getFacturasRecientes(limite)` → Lista las últimas facturas con nombre del vendedor
- `insertar(Factura)` → Guarda una factura nueva
- `insertarDetalle(...)` → Guarda cada línea de la venta (qué producto, cuántos)
- `actualizarEstado(...)` → Cambia el estado de una factura (pendiente, pagada, etc.)
- `getAlertasStock()` → Cuenta cuántos medicamentos tienen stock bajo (10 o menos)

---
### `data/DetalleVentaDAO.java`
Maneja el detalle de cada venta. Métodos:
- `listarPorFactura(idFactura)` → Dice qué productos se vendieron en esa factura
- `insertar(...)` → Agrega un producto 
- `eliminar(...)` → Borra un producto
- `eliminarPorFactura(...)` → Borra todo el detalle de una factura
- `total()` → Cuántos registros de detalle hay en total

---
### `data/PersonasDAO.java`
Maneja las personas (empleados, admin). Métodos:
- `listar(texto)` → Lista personas filtradas por nombre, con un subquery que cuenta cuántos usuarios tiene cada una
- `listarNombresIds()` → Devuelve IDs y nombres para llenar dropdowns
- `insertar(...)` → Guarda una persona nueva con la fecha de registro
- `actualizar(...)` → Actualiza datos de una persona
- `eliminar(...)` → Borra una persona
- `total()` y `existe(...)` → Conteo y verificación

---
### `data/UsuarioDAO.java`
Maneja los usuarios del sistema (el login). Métodos:
- `listar(texto)` → Lista usuarios con info de la persona (JOIN con tabla personas)
- `insertar(...)` → Crea un usuario nuevo (activo por defecto)
- `actualizar(...)` → Actualiza datos de un usuario
- `toggleEstado(...)` → Activa o desactiva un usuario
- `actualizarUltimoAcceso(...)` → Guarda la fecha y hora del último login
- `eliminar(...)` → Borra un usuario
- `verificarCredenciales(usuario, contraseña)` → Revisa si el usuario y contraseña son correctos y si está activo
- `getIdUsuario(usuario, contraseña)` → Devuelve el ID del usuario después de verificar credenciales
- `total()`, `totalActivos()`, `existe(...)` → Conteos y verificación
- `getNombrePersona(idUsuario)` → Devuelve el nombre real de la persona detrás del usuario

---
### `data/RolDAO.java`
Maneja los roles (Administrador, Cajero, etc.). Métodos:
- `listar(texto)` → Lista roles por nombre
- `listarNombresIds()` → IDs y nombres para dropdowns
- `insertar(...)`, `actualizar(...)`, `eliminar(...)` → CRUD básico
- `total()` y `existe(...)` → Conteo y verificación

---
### `data/RolUsuariosDAO.java`
Maneja la relación entre usuarios y roles (quién tiene qué permiso). Métodos:
- `listarPorUsuario(idUsuario)` → Dice qué roles tiene un usuario
- `asignarRol(idUsuario, idRol)` → Le da un rol
- `removerRol(idUsuario, idRol)` → Le quita un rol
- `removerTodosRoles(idUsuario)` → Le quita TODOS los roles
- `tieneRol(idUsuario, idRol)` → Dice si un usuario tiene un rol específico

---
### `data/ProveedorDAO.java` 
Maneja los proveedores. Métodos:
- `insertar(...)`, `actualizar(...)`, `eliminar(...)` → CRUD básico
- `total()` y `existe(...)` → Conteo y verificación
- `getProductosCount(idProveedor)` → Cuántos lotes tiene ese proveedor
- `toggleEstado(idProveedor)` → Cambia entre "Activo" e "Inactivo"

---
### `data/MedicamentoDAO.java` 
Maneja todo lo relacionado con medicamentos: crear, editar, buscar, controlar stock.  o líquido, genérico o de marca). Implementa la interfaz `CrudSimpleInterface<Medicamento>`.

| Método | Qué hace                                                                                                                                                                                                                                                                                                                                                                                     |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `listar(String texto)` | Devuelve una lista de todos los medicamentos que coincidan con el texto de búsqueda (busca por `nombre_medicamento` con LIKE).                                                                                                                                                                                                                                                               |
| `insertar(Medicamento obj)` | Guarda un medicamento nuevo en la BD. Usa 18 parámetros en el INSERT. Revisa de qué tipo es el objeto (`instanceof`) y llena solo los campos que son: si es `LiquidoGenerico` llena `ingrediente_activo`, `laboratorio`, `volumen_ml`, `tipo_liquido`; si es `PastillaMarca` llena `marca`, `patente`, `cantidad_unidades`, `tipo_pastilla`. Los campos que no aplican se guardan como `NULL` |
| `actualizar(Medicamento obj)` | Igual que `insertar()` pero con un UPDATE. Actualiza todos los campos del medicamento por `id_medicamento`. También detecta el tipo y llena solo los campos correspondientes                                                                                                                                                                                                                 |
| `total()` | Devuelve la cantidad total de medicamentos registrados en la tabla (`SELECT COUNT(*)`)                                                                                                                                                                                                                                                                                                       |
| `existe(String texto)` | Revisa si ya existe un medicamento con ese nombre exacto. Devuelve `true` o `false`                                                                                                                                                                                                                                                                                                          |
| `buscarPorId(String id)` | Busca un medicamento por su ID único. Si lo encuentra, usa `crearMedicamento(rs)` para devolver el objeto concreto correcto (puede ser `PastillaGenerica`, `PastillaMarca`, `LiquidoGenerico` o `LiquidoMarca`). Si no existe, devuelve `null`                                                                                                                                               |
| `reducirStock(String idMedicamento, int cantidad)` | Resta la cantidad indicada al stock del medicamento. Tiene una validación de seguridadasí que si no hay suficiente stock, no se ejecuta nada y devuelve `false`. Si la resta se hace bien, devuelve `true`                                                                                                                                                                                   |
| `getStockBajo()` | Cuenta cuántos medicamentos tienen stock menor o igual a 10. Se usa en el dashboard para mostrar la alerta de "productos por agotarse"                                                                                                                                                                                                                                                       |
| `crearMedicamento(ResultSet rs)` | Método private. Es una creador que lee los campos `tipo_forma` y `tipo_comercial` y dice qué clase instancia. Si `tipo_forma = "pastilla"` y `tipo_comercial = "generico"`, crea una `PastillaGenerica`. Si es `"liquido"` y `"marca"`, crea una `LiquidoMarca`, etc. |
| `cerrar()` | Método private. Cierra el PreparedStatement, el ResultSet y desconecta la conexión.                                                                                                                                                                                                                                  |

---

## 4. Nuevos Archivos en `presentacion/ventanas/`

### Pantallas principales

| Archivo | Para qué sirve                                                                                                                                                               |
|---|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `formVentas.java` | Ventana principal de la app. Tiene un menú lateral a la izquierda y un área central que cambia según lo que se seleccione (Dashboard, Registrar Venta, Consultar Inventario) |
| `formAdmin.java` | Ventana para el administrador. Similar a formVentas pero con opciones extra de gestión                                                                                       |
| `formInventario.java` | Panel para que el cajero consulte el inventario mientras vende. Muestra los productos disponibles y los puede agregar al carrito                                             |
| `formRegistrarVenta.java` | Donde se arma la venta. Tiene un carrito con botones de + y -, muestra subtotal, impuesto (19%) y total. Al finalizar crea la factura, guarda el detalle y reduce el stock   |

### Paneles internos

| Archivo | Para qué sirve                                                                                                                                                                                                         |
|---|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `panelDashboard.java` | Dashboard del cajero: tarjetas con acciones rápidas y estadísticas (ventas de hoy, productos activos, alertas de stock)                                                                                                |
| `panelAdminDashboard.java` | Dashboard del admin: estadísticas más detalladas, gráfico de barras con ventas semanales, lista de actividad reciente                                                                                                  |
| `panelAdminInventario.java` | Gestión de inventario: tabla con todos los lotes, búsqueda, botón para agregar nuevo lote, botón para ajustar stock.                                                                                                   |
| `panelAgregarMedicamentos.java` | Catálogo de medicamentos: tabla con todos los medicamentos, form para agregar nuevos con campos dinámicos (si elige pastilla pide unidades, si es líquido te pide ml). También permite activar/desactivar medicamentos |
| `panelGestionProveedores.java` | Gestión de proveedores: tabla con proveedores, formulario para agregar nuevos, botón para activar/desactivar con un click                                                                                              |

### Componentes reutilizables

| Archivo | Para qué sirve |
|---|---|
| `panelBorder.java` | Un JPanel con bordes redondeados que se usa de fondo en varias pantallas |
| `listMenu.java` | Una lista personalizada que renderiza los items del menú lateral |
| `menuItem.java` | Cada item del menú lateral (con ícono, nombre y efecto de hover) |

### Otros

| Archivo | Para qué sirve |
|---|---|
| `presentacion/components/Menu.java` | El menú lateral completo: fondo con gradiente, items de navegación, botón de cerrar sesión |
| `models/menuModel.java` | Modelo para los items del menú. Tiene un enum `MenuType` con TITLE (título de sección), MENU (item navegable) y EMPTY (separador) |

---

## 5. Lógica de Negocio 

### El flujo completo de una venta

1. El vendedor abre "Registrar Venta"
2. Va a "Consultar Inventario", busca un medicamento y lo agrega al carrito
3. En el carrito puede cambiar cantidades con los botones + y -
4. Cuando le da "Finalizar Venta":
   - Se genera un ID para la factura
   - Se calcula subtotal, IVA (18%) y total
   - Se inserta la factura en la BD con `FacturaDAO.insertar()`
   - Por cada producto en el carrito se llama a `FacturaDAO.insertarDetalle()`
   - Por cada producto se llama a `MedicamentoDAO.reducirStock()` para descontar del inventario
   - Si algo falla en el medio, se muestra un error y no se guarda nada

### Control de stock

- `MedicamentoDAO.reducirStock()` tiene una validación: solo reduce si hay suficiente stock (`stock_medicamento >= cantidad`). Si no hay suficiente, no hace nada.
- `MedicamentoDAO.getStockBajo()` cuenta cuántos productos tienen 10 o menos unidades para mostrar alertas en el dashboard.
- **Los paneles muestran badges de colores: verde = "Disponible", amarillo = "Bajo", rojo = "Agotado".**

### Sistema de roles

- La tabla `rol_usuarios` conecta usuarios con roles.
- `RolUsuariosDAO.tieneRol()` permite verificar si un usuario tiene un permiso específico.
- Los roles ya están en la BD (Administrador, vendedor) 

### Estados de proveedores y medicamentos

- Los proveedores tienen estado "Activo" o "Inactivo". Se puede cambiar con un click desde el panel de gestión.
- Los medicamentos también se pueden activar/desactivar desde el catálogo.

---

## 6. Bugs que se Arreglaron

| Bug | Qué pasaba | Arreglo                                                                       |
|---|---|-------------------------------------------------------------------------------|
| Stock en 0 rompía el sistema | Si un medicamento tenía 0 unidades, la validación `stock > 0` lanzaba excepción | Se cambió a `stock >= 0`|
| Clases en paquetes equivocados | Las clases concretas estaban mezcladas entre `medicamentos` y `entidades` | Se movieron todas las concretas a `entidades/` y las abstractas a `medicamentos/` |
| Las ventas no guardaban detalle | Se creaba la factura pero no se registraba qué productos se vendieron | Se agregó `DetalleVentaDAO` y `FacturaDAO.insertarDetalle()`|
| Faltaba columna `cantidad` en la tabla `lote` | El código la usaba pero no existía en el SQL | Se creó `db_migration.sql` con el ALTER TABLE |

---

## 7. Cosas que Todavía Faltan

- **Conectar el login con `UsuarioDAO`:** La lógica de autenticación ya está hecha (`verificarCredenciales`).
- **Restringir pantallas por rol:** Los roles están en la BD pero cualquier usuario puede entrar a todo.

---

## 8. ¿Cómo lo usan?

1. Prende **XAMPP** y yo lo tengo en el puerto `3308`
2. Ejecutá `db_migration.sql` para agregar las columnas que faltan y los datos de prueba
3. Abran el proyecto
4. MIren antes si estan estas librerias sino me las piden:
   - `lib/mysql-connector-j-9.7.0.jar`
   - `lib/AbsoluteLayout-RELEASE290.jar`
5. Delen run file a `src/presentacion/Login.java` o a `src/presentacion/ventanas/formVentas.java`

---
