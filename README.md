# HealthPharmacy 

La idea principal es separar:

- lo que el usuario ve (formularios/paneles),
- lo que pasa en base de datos (DAOs),
- y las reglas del negocio (clases)
---

## 1. Arquitectura 

El sistema funciona en 3 capas:

1. **Presentacion (`src/presentacion`)**
   - Son las pantallas (`Login`, `SignUp`, `formAdmin`, `formVentas`, paneles de inventario, medicamentos, proveedores).
   - Solo se captura lo que el usuario escribe, se valida y se muestran mensajes.

2. **Datos (`src/data`)**
   - Son los DAOs (`UsuarioDAO`, `MedicamentoDAO`, `FacturaDAO`, etc.).
   - Aqui se ejecuta queries SQL: `SELECT`, `INSERT`, `UPDATE`, `DELETE`.

3. **Clases (`src/medicamentos`, `src/entidades`, `src/inventario`, etc.)**
   - Son las clases que representan cosas reales del negocio:
     - medicamento,
     - lote,
     - factura,
     - proveedor.

---

## 2. Flujo del sistema 

### Login

1. El usuario escribe `usuario` y `password` en `Login`.
2. `Login` llama a `UsuarioDAO.autenticarConRol(...)`.
3. El DAO consulta `usuario + rol_usuarios + rol` y devuelve:
   - id del usuario,
   - nombre del usuario,
   - nombre del rol.
4. `Login` guarda esos datos en `SesionUsuario`.
5. Segun el rol:
   - `Administrador` -> abre `formAdmin`.
   - `Cajero` -> abre `formVentas`.

### Registro (`SignUp`)

1. El usuario llena nombre, telefono, correo, password y rol.
2. Se crea una fila en `personas`.
3. Se crea una fila en `usuario`.
4. Se crea la relacion en `rol_usuarios` (`ROL001` admin / `ROL002` cajero).

---

## 3. Logica de formularios

### `Login` y `SignUp`
- Son la entrada al sistema.

### `formAdmin`
- Es la ventana principal del admin.
- Tiene menu lateral y cambia vistas con `CardLayout`.
- Carga paneles como:
  - dashboard admin,
  - inventario admin,
  - agregar medicamentos,
  - proveedores.
  - logOut

### `formVentas`
- Es la ventana del cajero.
- Tambien usa `CardLayout`.
- Permite:
  - ver dashboard operativo,
  - registrar venta,
  - consultar inventario.
  - logOut

### `panelAgregarMedicamentos`
- Muestra tabla de medicamentos.
- Abre formulario para crear medicamento.
- El formulario cambia campos segun:
  - forma: `pastilla` o `liquido`,
  - tipo comercial: `generico` o `marca`.

### `panelAdminInventario`
- Muestra lotes, stock y estado.
- Permite:
  - crear lote nuevo,
  - ajustar stock de un lote.

---

## 4. Logica de los DAOs

### `Conexion`
- Es un singleton que centraliza la conexion a MySQL.
- Todos los DAOs usan esa misma manera de conectarse.

### `UsuarioDAO`
- Administra usuarios:
  - crear,
  - editar,
  - activar/desactivar,
  - autenticar.
- Tambien actualiza `ultimo_acceso`.

### `RolDAO` y `RolUsuariosDAO`
- `RolDAO`: administra los roles.
- `RolUsuariosDAO`: administra la relacion entre usuario-rol.

### `MedicamentoDAO`
- Es el mas importantes.
- Guarda y lee medicamentos de varios tipos concretos:
  - `PastillaGenerica`,
  - `PastillaMarca`,
  - `LiquidoGenerico`,
  - `LiquidoMarca`.
- Tiene validaciones de stock y consultas de alertas.

### `LoteInventarioDAO`
- Maneja lotes de inventario por medicamento.
- Permite crear y actualizar cantidad disponible por lote.

### `FacturaDAO` y `DetalleVentaDAO`
- `FacturaDAO`: muestra: (quien vendio, total, fecha, estado).
- `DetalleVentaDAO`: Muestra detalles como: (producto, cantidad, precio).
- Trabajan juntos para registrar una venta completa.

### `ProveedorDAO` y `PersonasDAO`
- `ProveedorDAO`: CRUD y estado de proveedores.
- `PersonasDAO`: datos personales de usuarios.
---

## 5. Modelo de clases de medicamentos

El sistema no debe tratar a todos los medicamentos como si fueran iguales.

Jerarquia:

- `Medicamento` (Clase base)
  - `MedicamentoPastilla` (abstracta)
    - `PastillaGenerica`
    - `PastillaMarca`
  - `MedicamentoLiquido` (abstracta)
    - `LiquidoGenerico`
    - `LiquidoMarca`

Se hace con el fin de:

- No mezclar campos que no aplican.
  - como: un liquido no necesita `tipo_pastilla`.
- Se guarda en BD solo lo que se debe y el resto en `NULL`.

---

## 6. Base de datos que espera el sistema y archivos

Tablas :

- `usuario`
- `rol`
- `rol_usuarios`
- `personas`
- `medicamento`
- `lote`
- `factura`
- `detalle_venta`
- `proveedor`

Archivos SQL en repo:

- `proyecto_farmacia.sql` (base)
- `db_migration.sql` (ajustes/migraciones) -- CREADA PARA DATOS DE PRUEBA

---
Dependencias :

- `mysql-connector-j-9.7.0.jar`
- `AbsoluteLayout-RELEASE290.jar`

---
