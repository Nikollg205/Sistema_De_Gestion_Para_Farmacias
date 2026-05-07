-- Migration: Add cantidad column to lote table
-- This column is referenced by LoteInventarioDAO but missing from schema

USE proyecto_farmacia;

ALTER TABLE lote
ADD COLUMN cantidad INT NOT NULL DEFAULT 0
AFTER fecha_caducidad;

-- Insert sample data for testing
-- Proveedores
INSERT INTO proveedor (id_proveedor, nombre_proveedor, producto_proveedor, telefono_proveedor, correo_proveedor)
VALUES ('PROV001', 'Drogueria Nacional', 'Farmacos Generales', '310-555-0101', 'ventas@drogueria.com')
ON DUPLICATE KEY UPDATE nombre_proveedor='Drogueria Nacional';

INSERT INTO proveedor (id_proveedor, nombre_proveedor, producto_proveedor, telefono_proveedor, correo_proveedor)
VALUES ('PROV002', 'FarmaCol SAS', 'Medicamentos Especializados', '320-555-0202', 'pedidos@farmacol.com')
ON DUPLICATE KEY UPDATE nombre_proveedor='FarmaCol SAS';

INSERT INTO proveedor (id_proveedor, nombre_proveedor, producto_proveedor, telefono_proveedor, correo_proveedor)
VALUES ('PROV003', 'Medifar Ltda', 'Insumos Medicos', '315-555-0303', 'info@medifar.com')
ON DUPLICATE KEY UPDATE nombre_proveedor='Medifar Ltda';

-- Medicamentos
INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento)
VALUES ('MED001', 'Angeticos', 12000, 'UND', 'Acetaminofen 500mg', 150, 20, 'Acetaminofen')
ON DUPLICATE KEY UPDATE precio=12000, stock_medicamento=150;

INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento)
VALUES ('MED002', 'Antiinflamatorios', 18000, 'UND', 'Ibuprofeno 400mg', 85, 20, 'Ibuprofeno')
ON DUPLICATE KEY UPDATE precio=18000, stock_medicamento=85;

INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento)
VALUES ('MED003', 'Antibioticos', 35000, 'UND', 'Amoxicilina 500mg', 12, 21, 'Amoxicilina')
ON DUPLICATE KEY UPDATE precio=35000, stock_medicamento=12;

INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento)
VALUES ('MED004', 'Antialergicos', 15000, 'UND', 'Loratadina 10mg', 200, 20, 'Loratadina')
ON DUPLICATE KEY UPDATE precio=15000, stock_medicamento=200;

INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento)
VALUES ('MED005', 'Gastricos', 22000, 'UND', 'Omeprazol 20mg', 5, 20, 'Omeprazol')
ON DUPLICATE KEY UPDATE precio=22000, stock_medicamento=5;

INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento)
VALUES ('MED006', 'Antidiabeticos', 28000, 'UND', 'Metformina 850mg', 45, 20, 'Metformina')
ON DUPLICATE KEY UPDATE precio=28000, stock_medicamento=45;

INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento)
VALUES ('MED007', 'Antihipertensivos', 32000, 'UND', 'Losartan 50mg', 0, 20, 'Losartan')
ON DUPLICATE KEY UPDATE precio=32000, stock_medicamento=0;

INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento)
VALUES ('MED008', 'Antiinflamatorios', 14000, 'UND', 'Diclofenaco 50mg', 60, 20, 'Diclofenaco')
ON DUPLICATE KEY UPDATE precio=14000, stock_medicamento=60;

-- Lotes with cantidad
INSERT INTO lote (id_lote, id_medicamento, id_proveedor, fecha_caducidad, cantidad)
VALUES ('LOT001', 'MED001', 'PROV001', '2025-12-15', 150)
ON DUPLICATE KEY UPDATE cantidad=150;

INSERT INTO lote (id_lote, id_medicamento, id_proveedor, fecha_caducidad, cantidad)
VALUES ('LOT002', 'MED002', 'PROV002', '2025-08-20', 85)
ON DUPLICATE KEY UPDATE cantidad=85;

INSERT INTO lote (id_lote, id_medicamento, id_proveedor, fecha_caducidad, cantidad)
VALUES ('LOT003', 'MED003', 'PROV001', '2025-06-30', 12)
ON DUPLICATE KEY UPDATE cantidad=12;

INSERT INTO lote (id_lote, id_medicamento, id_proveedor, fecha_caducidad, cantidad)
VALUES ('LOT004', 'MED004', 'PROV003', '2026-03-10', 200)
ON DUPLICATE KEY UPDATE cantidad=200;

INSERT INTO lote (id_lote, id_medicamento, id_proveedor, fecha_caducidad, cantidad)
VALUES ('LOT005', 'MED005', 'PROV002', '2025-05-15', 5)
ON DUPLICATE KEY UPDATE cantidad=5;

-- Roles
INSERT INTO rol (id_rol, nombre_rol, descripcion_rol)
VALUES ('ROL001', 'Administrador', 'Acceso completo al sistema')
ON DUPLICATE KEY UPDATE nombre_rol='Administrador';

INSERT INTO rol (id_rol, nombre_rol, descripcion_rol)
VALUES ('ROL002', 'Cajero', 'Gestion de ventas y caja')
ON DUPLICATE KEY UPDATE nombre_rol='Cajero';
