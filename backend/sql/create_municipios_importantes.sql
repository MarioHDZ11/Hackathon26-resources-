-- Active: 1779528522527@@127.0.0.1@3306
-- ============================================================================
-- BISourcesMX - Create municipios_importantes table
-- Stores real important municipalities per state (excluding capital)
-- Capital is still taken from estados.capital
-- ============================================================================

USE gestion_recursos_mx;

CREATE TABLE IF NOT EXISTS municipios_importantes (
    id_municipio INT AUTO_INCREMENT PRIMARY KEY,
    id_estado INT NOT NULL,
    nombre_municipio VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_estado) REFERENCES estados(id_estado),
    UNIQUE KEY uk_estado_municipio (id_estado, nombre_municipio)
);

-- Aguascalientes (capital: Aguascalientes)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (1, 'Calvillo'), (1, 'Jesús María'), (1, 'Pabellón de Arteaga'), (1, 'Rincón de Romos');

-- Baja California (capital: Mexicali)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (2, 'Tijuana'), (2, 'Ensenada'), (2, 'Tecate'), (2, 'Playas de Rosarito');

-- Baja California Sur (capital: La Paz)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (3, 'Cabo San Lucas'), (3, 'San José del Cabo'), (3, 'Loreto'), (3, 'Santa Rosalía');

-- Campeche (capital: Campeche)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (4, 'Ciudad del Carmen'), (4, 'Champotón'), (4, 'Escárcega'), (4, 'Calkiní');

-- Chiapas (capital: Tuxtla Gutiérrez)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (5, 'San Cristóbal de Las Casas'), (5, 'Tapachula'), (5, 'Comitán'), (5, 'Palenque');

-- Chihuahua (capital: Chihuahua)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (6, 'Ciudad Juárez'), (6, 'Delicias'), (6, 'Cuauhtémoc'), (6, 'Parral');

-- CDMX (capital: CDMX) - principales alcaldías
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (7, 'Iztapalapa'), (7, 'Gustavo A. Madero'), (7, 'Álvaro Obregón'), (7, 'Coyoacán');

-- Coahuila (capital: Saltillo)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (8, 'Torreón'), (8, 'Monclova'), (8, 'Piedras Negras'), (8, 'Ciudad Acuña');

-- Colima (capital: Colima)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (9, 'Manzanillo'), (9, 'Tecomán'), (9, 'Villa de Álvarez'), (9, 'Comala');

-- Durango (capital: Durango)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (10, 'Gómez Palacio'), (10, 'Lerdo'), (10, 'Santiago Papasquiaro'), (10, 'El Salto');

-- México (capital: Toluca)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (11, 'Ecatepec'), (11, 'Nezahualcóyotl'), (11, 'Naucalpan'), (11, 'Tlalnepantla');

-- Guanajuato (capital: Guanajuato)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (12, 'León'), (12, 'Irapuato'), (12, 'Celaya'), (12, 'Salamanca');

-- Guerrero (capital: Chilpancingo)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (13, 'Acapulco'), (13, 'Iguala'), (13, 'Taxco'), (13, 'Zihuatanejo');

-- Hidalgo (capital: Pachuca)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (14, 'Tula'), (14, 'Tulancingo'), (14, 'Huejutla'), (14, 'Actopan');

-- Jalisco (capital: Guadalajara)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (15, 'Puerto Vallarta'), (15, 'Zapopan'), (15, 'Tlaquepaque'), (15, 'Lagos de Moreno');

-- Michoacán (capital: Morelia)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (16, 'Uruapan'), (16, 'Lázaro Cárdenas'), (16, 'Zamora'), (16, 'Pátzcuaro');

-- Morelos (capital: Cuernavaca)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (17, 'Cuautla'), (17, 'Jiutepec'), (17, 'Temixco'), (17, 'Yautepec');

-- Nayarit (capital: Tepic)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (18, 'Nuevo Vallarta'), (18, 'Bahía de Banderas'), (18, 'Compostela'), (18, 'San Blas');

-- Nuevo León (capital: Monterrey)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (19, 'San Pedro Garza García'), (19, 'Guadalupe'), (19, 'Apodaca'), (19, 'Escobedo');

-- Oaxaca (capital: Oaxaca)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (20, 'Puerto Escondido'), (20, 'Salina Cruz'), (20, 'Huatulco'), (20, 'Tlaxiaco');

-- Puebla (capital: Puebla)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (21, 'Tehuacán'), (21, 'Cholula'), (21, 'Atlixco'), (21, 'Izúcar de Matamoros');

-- Querétaro (capital: Querétaro)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (22, 'San Juan del Río'), (22, 'Tequisquiapan'), (22, 'Cadereyta'), (22, 'Jalpan');

-- Quintana Roo (capital: Chetumal)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (23, 'Cancún'), (23, 'Playa del Carmen'), (23, 'Tulum'), (23, 'Cozumel');

-- San Luis Potosí (capital: San Luis Potosí)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (24, 'Ciudad Valles'), (24, 'Matehuala'), (24, 'Rioverde'), (24, 'Tamazunchale');

-- Sinaloa (capital: Culiacán)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (25, 'Mazatlán'), (25, 'Los Mochis'), (25, 'Guasave'), (25, 'Guamúchil');

-- Sonora (capital: Hermosillo)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (26, 'Nogales'), (26, 'San Luis Río Colorado'), (26, 'Guaymas'), (26, 'Ciudad Obregón');

-- Tabasco (capital: Villahermosa)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (27, 'Paraíso'), (27, 'Cárdenas'), (27, 'Comalcalco'), (27, 'Tenosique');

-- Tamaulipas (capital: Ciudad Victoria)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (28, 'Tampico'), (28, 'Reynosa'), (28, 'Matamoros'), (28, 'Nuevo Laredo');

-- Tlaxcala (capital: Tlaxcala)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (29, 'Apizaco'), (29, 'Huamantla'), (29, 'Calpulalpan'), (29, 'Chiautempan');

-- Veracruz (capital: Xalapa)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (30, 'Veracruz Puerto'), (30, 'Córdoba'), (30, 'Orizaba'), (30, 'Coatzacoalcos');

-- Yucatán (capital: Mérida)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (31, 'Valladolid'), (31, 'Tizimín'), (31, 'Progreso'), (31, 'Motul');

-- Zacatecas (capital: Zacatecas)
INSERT INTO municipios_importantes (id_estado, nombre_municipio) VALUES (32, 'Fresnillo'), (32, 'Jerez'), (32, 'Guadalupe'), (32, 'Sombrerete');
