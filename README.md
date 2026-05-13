# HealthPharmacy - Sistema de Gestion para Farmacias

Proyecto Java Swing + MySQL para gestionar ventas, inventario, medicamentos, proveedores y usuarios con roles.

## Tecnologias
- Java (Swing)
- MySQL
- NetBeans (proyecto Ant)

## Estructura principal
- `src/presentacion/`: pantallas y componentes Swing.
- `src/data/`: DAOs y acceso a datos.
- `src/database/`: conexion singleton a BD.
- `src/medicamentos/` y `src/entidades/`: modelo de dominio de medicamentos.
- `src/roles/`: sesion y roles de usuario.

## Funcionalidades implementadas
- Login con validacion en BD y enrutamiento por rol.
- Registro de usuarios (`SignUp`) con seleccion de rol (`Administrador` / `Cajero`).
- Sesion global con singleton (`SesionUsuario`).
- Panel de Admin (`formAdmin`) y panel de Cajero (`formVentas`).
- Gestion de inventario y ajuste de stock.
- Gestion de medicamentos con formularios dinamicos por tipo:
  - `pastilla` / `liquido`
  - `generico` / `marca`
- Catalogo y gestion de proveedores.
- Registro de ventas con detalle y reduccion de stock.

## Roles y acceso
- `Administrador`: acceso a panel administrativo.
- `Cajero`: acceso a panel operativo de ventas/inventario.

El flujo de login usa `UsuarioDAO.autenticarConRol(...)` y abre:
- `formAdmin` si el rol es `Administrador`
- `formVentas` en caso contrario.

## Cambios recientes relevantes
- Correccion de autenticacion por rol y session handling.
- Correccion de inconsistencias de columna de password a `contraseña_usuario`.
- Asignacion de rol en registro de usuario (`rol_usuarios`).
- Mejora visual minima en `Login` y `SignUp`.
- Ocultacion de decoracion de ventana (`setUndecorated(true)`) en login/registro.
- Correccion en alta de medicamentos para evitar `tipo_forma` / `tipo_comercial` nulos:
  - En form se setean ambos valores antes de insertar.
  - En DAO existen metodos de respaldo (`resolverTipoForma`, `resolverTipoComercial`).
- Mejora de formularios con scroll para no ocultar campos:
  - Alta de medicamento (admin).
  - Ajuste de stock (admin).
- Comentarios agregados en clases y metodos (forms, paneles, DAOs y core).

## Base de datos
Revisa y aplica tus scripts SQL del repo:
- `proyecto_farmacia.sql` (estructura base)
- `db_migration.sql` (ajustes/migraciones y datos de apoyo)

Tablas clave:
- `usuario`
- `rol`
- `rol_usuarios`
- `medicamento`
- `lote`
- `factura`
- `detalle_venta`
- `proveedor`

## Configuracion de conexion
En `src/database/Conexion.java`:
- URL: `jdbc:mysql://localhost:3308/`
- DB: `proyecto_farmacia`
- USER: `root`
- PASSWORD: ``

Ajusta estos valores segun tu entorno.

## Librerias necesarias
En la raiz del proyecto:
- `mysql-connector-j-9.7.0.jar`
- `AbsoluteLayout-RELEASE290.jar`

## Ejecutar en NetBeans
1. Abre el proyecto.
2. Verifica que `nbproject/project.properties` tenga:
   - `main.class=presentacion.Login`
3. Ejecuta Run Project.

## Notas
- El proyecto usa singleton de conexion y singleton de sesion.
- Se recomienda restringir creacion de admins en produccion (actualmente puede seleccionarse en `SignUp`).
