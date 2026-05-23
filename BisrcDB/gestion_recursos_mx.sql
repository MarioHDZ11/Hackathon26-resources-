-- ============================================================================
-- BASE DE DATOS: SISTEMA DE GESTIÓN DE RECURSOS POR ESTADO Y REGIÓN
-- ============================================================================
-- Diseño normalizado (3NF) con relaciones y llaves foráneas
-- Para aplicación web de gestión de recursos con análisis de riesgos
-- ============================================================================

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS gestion_recursos_mx
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE gestion_recursos_mx;

-- ============================================================================
-- TABLAS CATÁLOGO (Tablas de referencia)
-- ============================================================================

-- Tabla: Regiones geográficas de México
-- Agrupa estados por zona geográfica para análisis de riesgos
CREATE TABLE regiones (
    id_region INT PRIMARY KEY AUTO_INCREMENT,
    nombre_region VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Niveles de estado/condición
-- Define los 4 niveles de estado que puede tener un estado (modificable desde frontend)
CREATE TABLE niveles_estado (
    id_nivel INT PRIMARY KEY,
    nombre_nivel VARCHAR(50) NOT NULL,
    descripcion TEXT,
    color_indicador VARCHAR(7), -- Código hexadecimal para UI
    prioridad INT, -- 1=más crítico, 4=menos crítico
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Categorías de recursos
-- Clasifica los tipos de recursos (Económico, Social, Infraestructura)
CREATE TABLE categorias_recurso (
    id_categoria INT PRIMARY KEY AUTO_INCREMENT,
    nombre_categoria VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    icono VARCHAR(50), -- Nombre del icono para UI
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Niveles de riesgo
-- Define la severidad de los riesgos (1=Bajo, 2=Medio, 3=Alto)
CREATE TABLE niveles_riesgo (
    id_nivel_riesgo INT PRIMARY KEY,
    nombre_nivel VARCHAR(50) NOT NULL,
    descripcion TEXT,
    color_alerta VARCHAR(7), -- Código hexadecimal
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Tipos de población
-- Clasifica diferentes segmentos poblacionales
CREATE TABLE tipos_poblacion (
    id_tipo_poblacion INT PRIMARY KEY AUTO_INCREMENT,
    nombre_tipo VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- TABLAS PRINCIPALES
-- ============================================================================

-- Tabla: Estados de México
-- Información de los 32 estados con su estado/condición actual (dinámico)
CREATE TABLE estados (
    id_estado INT PRIMARY KEY,
    nombre_estado VARCHAR(100) NOT NULL UNIQUE,
    capital VARCHAR(100) NOT NULL,
    id_region INT NOT NULL,
    estado_actual INT NOT NULL DEFAULT 1, -- Nivel actual (1-4), modificable desde frontend
    poblacion_total BIGINT,
    superficie_km2 DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_region) REFERENCES regiones(id_region) ON UPDATE CASCADE,
    FOREIGN KEY (estado_actual) REFERENCES niveles_estado(id_nivel) ON UPDATE CASCADE,
    INDEX idx_region (id_region),
    INDEX idx_estado_actual (estado_actual)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Datos curiosos por estado
-- Almacena 5 datos curiosos por cada estado para mostrar aleatoriamente
CREATE TABLE datos_curiosos (
    id_dato_curioso INT PRIMARY KEY AUTO_INCREMENT,
    id_estado INT NOT NULL,
    numero_dato INT NOT NULL, -- 1 a 5
    dato_curioso TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_estado) REFERENCES estados(id_estado) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY unique_estado_numero (id_estado, numero_dato),
    INDEX idx_estado (id_estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Recursos
-- Catálogo de recursos gestionables (Presupuesto, Agua, Energía, etc.)
CREATE TABLE recursos (
    id_recurso INT PRIMARY KEY,
    nombre_recurso VARCHAR(100) NOT NULL UNIQUE,
    id_categoria INT NOT NULL,
    descripcion TEXT NOT NULL,
    unidad_medida VARCHAR(50), -- Ej: "millones MXN", "m³", "MW"
    icono VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_categoria) REFERENCES categorias_recurso(id_categoria) ON UPDATE CASCADE,
    INDEX idx_categoria (id_categoria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Riesgos
-- Catálogo de riesgos que pueden afectar estados y recursos
CREATE TABLE riesgos (
    id_riesgo INT PRIMARY KEY,
    nombre_riesgo VARCHAR(100) NOT NULL UNIQUE,
    id_nivel_riesgo INT NOT NULL,
    descripcion TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_nivel_riesgo) REFERENCES niveles_riesgo(id_nivel_riesgo) ON UPDATE CASCADE,
    INDEX idx_nivel_riesgo (id_nivel_riesgo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Población por estado
-- Registra información demográfica de cada estado
CREATE TABLE poblacion_estados (
    id_poblacion INT PRIMARY KEY AUTO_INCREMENT,
    id_estado INT NOT NULL,
    id_tipo_poblacion INT NOT NULL,
    cantidad BIGINT NOT NULL,
    indice_poblacion DECIMAL(5,2), -- Índice relativo (0-100)
    porcentaje DECIMAL(5,2), -- Porcentaje del total del estado
    anio_registro YEAR NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_estado) REFERENCES estados(id_estado) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_tipo_poblacion) REFERENCES tipos_poblacion(id_tipo_poblacion) ON UPDATE CASCADE,
    UNIQUE KEY unique_estado_tipo_anio (id_estado, id_tipo_poblacion, anio_registro),
    INDEX idx_estado (id_estado),
    INDEX idx_tipo (id_tipo_poblacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- TABLAS DE RELACIÓN (Muchos a Muchos)
-- ============================================================================

-- Tabla: Riesgos por región
-- Define qué riesgos son comunes en cada región geográfica
CREATE TABLE region_riesgo (
    id_region INT NOT NULL,
    id_riesgo INT NOT NULL,
    probabilidad DECIMAL(5,2) NOT NULL, -- 0-100 (porcentaje)
    impacto_estimado ENUM('Bajo', 'Medio', 'Alto', 'Crítico') DEFAULT 'Medio',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_region, id_riesgo),
    FOREIGN KEY (id_region) REFERENCES regiones(id_region) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_riesgo) REFERENCES riesgos(id_riesgo) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_region (id_region),
    INDEX idx_riesgo (id_riesgo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Recursos afectados por riesgos
-- Define qué recursos son impactados por cada riesgo y el nivel de afectación
CREATE TABLE riesgo_recurso_afectacion (
    id_riesgo INT NOT NULL,
    id_recurso INT NOT NULL,
    nivel_afectacion ENUM('Leve', 'Moderado', 'Severo', 'Crítico') NOT NULL,
    porcentaje_impacto DECIMAL(5,2), -- 0-100 (reducción estimada del recurso)
    descripcion_impacto TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_riesgo, id_recurso),
    FOREIGN KEY (id_riesgo) REFERENCES riesgos(id_riesgo) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_recurso) REFERENCES recursos(id_recurso) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_riesgo (id_riesgo),
    INDEX idx_recurso (id_recurso)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Distribución de recursos por estado
-- Registra la cantidad y disponibilidad de recursos en cada estado
CREATE TABLE distribucion_recursos (
    id_distribucion INT PRIMARY KEY AUTO_INCREMENT,
    id_estado INT NOT NULL,
    id_recurso INT NOT NULL,
    cantidad_disponible DECIMAL(15,2) NOT NULL,
    cantidad_asignada DECIMAL(15,2) DEFAULT 0,
    cantidad_reserva DECIMAL(15,2) DEFAULT 0,
    indice_disponibilidad DECIMAL(5,2), -- 0-100
    fecha_actualizacion DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_estado) REFERENCES estados(id_estado) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_recurso) REFERENCES recursos(id_recurso) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY unique_estado_recurso_fecha (id_estado, id_recurso, fecha_actualizacion),
    INDEX idx_estado (id_estado),
    INDEX idx_recurso (id_recurso),
    INDEX idx_fecha (fecha_actualizacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: Historial de estados
-- Auditoría de cambios en el estado/condición de cada estado
CREATE TABLE historial_estados (
    id_historial INT PRIMARY KEY AUTO_INCREMENT,
    id_estado INT NOT NULL,
    estado_anterior INT NOT NULL,
    estado_nuevo INT NOT NULL,
    motivo TEXT,
    usuario VARCHAR(100), -- Usuario que realizó el cambio desde frontend
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_estado) REFERENCES estados(id_estado) ON DELETE CASCADE,
    FOREIGN KEY (estado_anterior) REFERENCES niveles_estado(id_nivel),
    FOREIGN KEY (estado_nuevo) REFERENCES niveles_estado(id_nivel),
    INDEX idx_estado (id_estado),
    INDEX idx_fecha (fecha_cambio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- INSERCIÓN DE DATOS CATÁLOGO
-- ============================================================================

-- Insertar regiones de México
INSERT INTO regiones (id_region, nombre_region, descripcion) VALUES
(1, 'Sureste', 'Región sureste de México - Alta vulnerabilidad a fenómenos tropicales'),
(2, 'Centro', 'Región central de México - Alta densidad poblacional'),
(3, 'Occidente', 'Región occidental de México - Zona de alto riesgo sísmico'),
(4, 'Norte', 'Región norte de México - Clima árido y semiárido'),
(5, 'Centro-Sur', 'Región centro-sur de México - Zona urbana principal');

-- Insertar niveles de estado
INSERT INTO niveles_estado (id_nivel, nombre_nivel, descripcion, color_indicador, prioridad) VALUES
(1, 'Crítico', 'Estado en condición crítica - Requiere atención inmediata', '#FF0000', 1),
(2, 'Precaución', 'Estado en precaución - Monitoreo continuo requerido', '#FFA500', 2),
(3, 'Estable', 'Estado estable - Condiciones normales', '#FFFF00', 3),
(4, 'Óptimo', 'Estado óptimo - Condiciones favorables', '#00FF00', 4);

-- Insertar categorías de recursos
INSERT INTO categorias_recurso (id_categoria, nombre_categoria, descripcion, icono) VALUES
(1, 'Económico y Administrativo', 'Recursos relacionados con presupuesto, finanzas y gestión', 'dollar-sign'),
(2, 'Social y Comunitario', 'Recursos para desarrollo social, salud y alimentación', 'users'),
(3, 'Infraestructura y Servicios', 'Recursos de infraestructura, servicios básicos y logística', 'building');

-- Insertar niveles de riesgo
INSERT INTO niveles_riesgo (id_nivel_riesgo, nombre_nivel, descripcion, color_alerta) VALUES
(1, 'Riesgo Bajo', 'Probabilidad baja de ocurrencia - Impacto menor', '#00FF00'),
(2, 'Riesgo Medio', 'Probabilidad moderada - Impacto considerable', '#FFA500'),
(3, 'Riesgo Alto', 'Probabilidad alta - Impacto severo', '#FF0000');

-- Insertar tipos de población
INSERT INTO tipos_poblacion (nombre_tipo, descripcion) VALUES
('Población Urbana', 'Habitantes en zonas urbanas y metropolitanas'),
('Población Rural', 'Habitantes en zonas rurales y comunidades dispersas'),
('Población Indígena', 'Población perteneciente a pueblos originarios'),
('Población Económicamente Activa', 'Personas en edad laboral activas'),
('Población Vulnerable', 'Grupos en situación de vulnerabilidad social');

-- ============================================================================
-- INSERCIÓN DE DATOS PRINCIPALES - ESTADOS
-- ============================================================================

INSERT INTO estados (id_estado, nombre_estado, capital, id_region, estado_actual) VALUES
(1, 'Aguascalientes', 'Aguascalientes', 5, 1),
(2, 'Baja California', 'Mexicali', 4, 4),
(3, 'Baja California Sur', 'La Paz', 4, 4),
(4, 'Campeche', 'San Francisco de Campeche', 1, 4),
(5, 'Chiapas', 'Tuxtla Gutiérrez', 1, 1),
(6, 'Chihuahua', 'Chihuahua', 4, 4),
(7, 'Ciudad de México', 'Ciudad de México', 5, 1),
(8, 'Coahuila', 'Saltillo', 4, 1),
(9, 'Colima', 'Colima', 3, 3),
(10, 'Durango', 'Victoria de Durango', 4, 1),
(11, 'Estado de México', 'Toluca de Lerdo', 5, 1),
(12, 'Guanajuato', 'Guanajuato', 5, 4),
(13, 'Guerrero', 'Chilpancingo de los Bravo', 1, 1),
(14, 'Hidalgo', 'Pachuca de Soto', 5, 1),
(15, 'Jalisco', 'Guadalajara', 3, 3),
(16, 'Michoacán', 'Morelia', 3, 3),
(17, 'Morelos', 'Cuernavaca', 5, 1),
(18, 'Nayarit', 'Tepic', 3, 4),
(19, 'Nuevo León', 'Monterrey', 4, 2),
(20, 'Oaxaca', 'Oaxaca de Juárez', 1, 4),
(21, 'Puebla', 'Puebla de Zaragoza', 5, 3),
(22, 'Querétaro', 'Santiago de Querétaro', 5, 2),
(23, 'Quintana Roo', 'Chetumal', 1, 4),
(24, 'San Luis Potosí', 'San Luis Potosí', 2, 2),
(25, 'Sinaloa', 'Culiacán Rosales', 4, 2),
(26, 'Sonora', 'Hermosillo', 4, 1),
(27, 'Tabasco', 'Villahermosa', 1, 1),
(28, 'Tamaulipas', 'Ciudad Victoria', 4, 4),
(29, 'Tlaxcala', 'Tlaxcala de Xicohténcatl', 5, 3),
(30, 'Veracruz', 'Xalapa de Enríquez', 1, 3),
(31, 'Yucatán', 'Mérida', 1, 3),
(32, 'Zacatecas', 'Zacatecas', 2, 2);

-- ============================================================================
-- DATOS CURIOSOS POR ESTADO (5 por estado)
-- ============================================================================

-- Aguascalientes
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(1, 1, 'Aguascalientes es conocida como "La ciudad de la gente buena" y es famosa por su Feria Nacional de San Marcos, una de las más importantes de México.'),
(1, 2, 'El estado tiene la mayor producción de guayaba en México, siendo el principal exportador de esta fruta.'),
(1, 3, 'Aguascalientes alberga importantes empresas automotrices como Nissan, haciendo de la industria automotriz su principal motor económico.'),
(1, 4, 'El Museo Nacional de la Muerte en Aguascalientes es único en América Latina y alberga más de 2,000 piezas relacionadas con la muerte.'),
(1, 5, 'Aguascalientes tiene el acuífero más grande de México, del cual deriva su nombre "aguas calientes" por sus aguas termales.');

-- Baja California
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(2, 1, 'Baja California es el único estado de México donde la mayor parte del territorio se encuentra en una península.'),
(2, 2, 'Tijuana, en Baja California, es la ciudad más visitada del mundo con más de 50 millones de cruces fronterizos anuales.'),
(2, 3, 'El Valle de Guadalupe en Baja California produce más del 90% del vino mexicano y es conocido como la "Napa Valley de México".'),
(2, 4, 'Baja California alberga el Observatorio Astronómico Nacional en el Parque Nacional Sierra de San Pedro Mártir.'),
(2, 5, 'El estado tiene una de las tasas más altas de industria maquiladora en México, siendo un important centro manufacturero.');

-- Baja California Sur
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(3, 1, 'Baja California Sur es hogar del fenómeno natural de avistamiento de ballenas grises más importante del mundo.'),
(3, 2, 'El estado tiene la densidad poblacional más baja de México con menos de 10 habitantes por kilómetro cuadrado.'),
(3, 3, 'La Paz, su capital, es reconocida por la UNESCO como Patrimonio Natural de la Humanidad por su biodiversidad marina.'),
(3, 4, 'Cabo San Lucas es uno de los destinos turísticos más exclusivos de México, famoso por su icónico Arco de Cabo San Lucas.'),
(3, 5, 'El Mar de Cortés, que baña las costas de Baja California Sur, es llamado "el acuario del mundo" por Jacques Cousteau.');

-- Campeche
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(4, 1, 'Campeche es el único estado de México cuya capital amurallada está declarada Patrimonio de la Humanidad por UNESCO.'),
(4, 2, 'El estado produce más del 50% del petróleo de México a través de plataformas en el Golfo de México.'),
(4, 3, 'Campeche alberga más de 1,500 sitios arqueológicos mayas, siendo Calakmul el segundo más grande después de Tikal.'),
(4, 4, 'El centro histórico de San Francisco de Campeche conserva su traza urbana original del siglo XVI.'),
(4, 5, 'Campeche es el principal productor de camarón de México y uno de los más importantes exportadores.');

-- Chiapas
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(5, 1, 'Chiapas genera aproximadamente el 40% de la energía hidroeléctrica de México gracias a sus numerosas presas.'),
(5, 2, 'El Cañón del Sumidero en Chiapas tiene paredes de más de 1,000 metros de altura y es una maravilla natural.'),
(5, 3, 'Chiapas tiene la mayor diversidad biológica de México con más de 1,200 especies de mariposas.'),
(5, 4, 'Palenque, en Chiapas, es una de las zonas arqueológicas mayas más importantes y mejor conservadas del mundo.'),
(5, 5, 'El estado produce el 40% del café mexicano, siendo uno de los principales productores de café orgánico.');

-- Chihuahua
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(6, 1, 'Chihuahua es el estado más grande de México, ocupando el 12.6% del territorio nacional.'),
(6, 2, 'Las Barrancas del Cobre en Chihuahua son más grandes y profundas que el Gran Cañón de Estados Unidos.'),
(6, 3, 'Chihuahua es el principal productor de manzanas en México, generando más del 70% de la producción nacional.'),
(6, 4, 'El estado alberga a los Tarahumaras (Rarámuri), famosos por ser los mejores corredores de larga distancia del mundo.'),
(6, 5, 'Chihuahua tiene la segunda economía más grande del norte de México después de Nuevo León.');

-- Ciudad de México
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(7, 1, 'La Ciudad de México es la ciudad más poblada de América del Norte con más de 9 millones de habitantes.'),
(7, 2, 'El Metro de la CDMX es el segundo más extenso de América Latina y transporta más de 4 millones de usuarios diarios.'),
(7, 3, 'La ciudad tiene más museos que cualquier otra ciudad del mundo, con más de 150 museos reconocidos.'),
(7, 4, 'El Centro Histórico de la CDMX es Patrimonio de la Humanidad y fue construido sobre la antigua Tenochtitlán.'),
(7, 5, 'Xochimilco, en la CDMX, conserva los únicos canales prehispánicos navegables y es Patrimonio de la Humanidad.');

-- Coahuila
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(8, 1, 'Coahuila es el principal productor de carbón en México, generando más del 95% de la producción nacional.'),
(8, 2, 'El estado alberga Cuatro Ciénegas, un valle con ecosistemas únicos considerado un laboratorio natural de evolución.'),
(8, 3, 'Coahuila tiene uno de los yacimientos paleontológicos más importantes del mundo con fósiles de dinosaurios.'),
(8, 4, 'Saltillo, la capital, es conocida como la "Detroit mexicana" por su industria automotriz.'),
(8, 5, 'El Desierto de Coahuila es el más grande de América del Norte y alberga especies endémicas únicas.');

-- Colima
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(9, 1, 'Colima es el estado más pequeño de México pero tiene uno de los volcanes más activos del país.'),
(9, 2, 'El Volcán de Fuego de Colima es uno de los volcanes más estudiados y monitoreados del mundo.'),
(9, 3, 'Colima produce el 20% del limón mexicano, siendo uno de los principales exportadores del país.'),
(9, 4, 'El estado tiene la tasa de alfabetización más alta de México con más del 96% de la población.'),
(9, 5, 'Manzanillo, en Colima, es el puerto más importante del Pacífico mexicano en movimiento de contenedores.');

-- Durango
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(10, 1, 'Durango es conocido como "La Tierra del Cine" por haber sido locación de más de 120 películas de Hollywood.'),
(10, 2, 'El estado tiene la mayor producción forestal de México, generando el 25% de la madera del país.'),
(10, 3, 'Durango alberga el Puente Baluarte, el puente atirantado más alto de las Américas con 402 metros.'),
(10, 4, 'La Zona del Silencio en Durango es conocida por sus anomalías electromagnéticas y fenómenos inexplicables.'),
(10, 5, 'Durango produce el 33% del oro de México, siendo uno de los principales estados mineros.');

-- Estado de México
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(11, 1, 'El Estado de México es la entidad más poblada del país con más de 16 millones de habitantes.'),
(11, 2, 'Teotihuacán, en el Estado de México, fue la ciudad más grande de América prehispánica con 200,000 habitantes.'),
(11, 3, 'El estado genera el 9% del PIB nacional, siendo la segunda economía más grande de México.'),
(11, 4, 'El Nevado de Toluca es el cuarto volcán más alto de México con 4,680 metros de altura.'),
(11, 5, 'El Estado de México tiene la red de autopistas más extensa del país con más de 1,000 kilómetros.');

-- Guanajuato
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(12, 1, 'Guanajuato es Patrimonio de la Humanidad por su arquitectura colonial y su importancia en la independencia de México.'),
(12, 2, 'El estado produce el 30% de las fresas de México y es el principal exportador al mundo.'),
(12, 3, 'Guanajuato tiene la mayor concentración de empresas automotrices japonesas en América Latina.'),
(12, 4, 'El Festival Internacional Cervantino en Guanajuato es el evento cultural más importante de América Latina.'),
(12, 5, 'Las momias de Guanajuato son un fenómeno natural único causado por las condiciones del suelo y clima.');

-- Guerrero
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(13, 1, 'Acapulco, en Guerrero, fue el destino turístico más famoso de México en las décadas de 1950-1970.'),
(13, 2, 'Guerrero produce más del 50% del oro de México, siendo el principal estado minero del país.'),
(13, 3, 'Los clavadistas de La Quebrada en Acapulco se lanzan desde 35 metros de altura, tradición desde 1934.'),
(13, 4, 'Taxco, Guerrero, es la capital mundial de la plata y produce las piezas de platería más finas del mundo.'),
(13, 5, 'Guerrero tiene 500 kilómetros de costa en el Pacífico con playas de clase mundial como Zihuatanejo.');

-- Hidalgo
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(14, 1, 'Hidalgo es conocido como "El Corazón de México" por su ubicación geográfica central.'),
(14, 2, 'Los Prismas Basálticos de Hidalgo son formaciones geológicas únicas con más de 2.5 millones de años.'),
(14, 3, 'Real del Monte en Hidalgo es famoso por sus pastes, introducidos por mineros ingleses en el siglo XIX.'),
(14, 4, 'Hidalgo produce el 30% de la cal de México y es líder en producción de cemento.'),
(14, 5, 'El Parque Nacional El Chico en Hidalgo es el parque nacional más antiguo de México, fundado en 1898.');

-- Jalisco
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(15, 1, 'Jalisco es la cuna del mariachi, el tequila y la charrería, símbolos por excelencia de México.'),
(15, 2, 'Guadalajara, capital de Jalisco, es la segunda ciudad más grande de México con 5 millones de habitantes.'),
(15, 3, 'El estado produce el 100% del tequila del mundo, protegido por denominación de origen.'),
(15, 4, 'Jalisco es el principal productor de electrónica de México, conocido como el "Silicon Valley mexicano".'),
(15, 5, 'La Feria Internacional del Libro de Guadalajara es la más importante en lengua española del mundo.');

-- Michoacán
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(16, 1, 'Michoacán es el único lugar del mundo donde hibernan las mariposas Monarca, fenómeno Patrimonio de la Humanidad.'),
(16, 2, 'El estado es el principal productor de aguacate del mundo, generando el 30% de la producción global.'),
(16, 3, 'Pátzcuaro, Michoacán, conserva tradiciones purépechas milenarias y su arquitectura colonial intacta.'),
(16, 4, 'Michoacán tiene la costa más larga del Pacífico mexicano con 228 kilómetros de playas.'),
(16, 5, 'El estado produce el 25% de la fresa de México y es el principal exportador a Estados Unidos.');

-- Morelos
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(17, 1, 'Morelos es conocido como "La Ciudad de la Eterna Primavera" por su clima templado todo el año.'),
(17, 2, 'Tepoztlán en Morelos es considerado uno de los Pueblos Mágicos más visitados de México.'),
(17, 3, 'Morelos fue el lugar de nacimiento de Emiliano Zapata, héroe de la Revolución Mexicana.'),
(17, 4, 'El estado tiene la mayor concentración de balnearios de aguas termales en México.'),
(17, 5, 'Xochicalco, en Morelos, es Patrimonio de la Humanidad y fue un importante centro de comercio prehispánico.');

-- Nayarit
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(18, 1, 'Las Islas Marietas en Nayarit albergan la Playa Escondida, una de las playas más extraordinarias del mundo.'),
(18, 2, 'Nayarit tiene la mayor producción de camarón cultivado de México.'),
(18, 3, 'San Blas, Nayarit, fue el puerto más importante del Pacífico durante la época colonial.'),
(18, 4, 'El estado es hogar de los huicholes, una de las culturas indígenas mejor preservadas de México.'),
(18, 5, 'Nayarit produce el 15% del tabaco de México, siendo uno de los principales productores.');

-- Nuevo León
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(19, 1, 'Nuevo León tiene el PIB per cápita más alto de México, siendo el estado más próspero del país.'),
(19, 2, 'Monterrey es la tercera ciudad más grande de México y el principal centro industrial del norte.'),
(19, 3, 'El Cerro de la Silla es el símbolo icónico de Monterrey y tiene una forma distintiva de silla de montar.'),
(19, 4, 'Nuevo León genera el 8% del PIB nacional siendo solo el 4% de la población del país.'),
(19, 5, 'El estado alberga las empresas más grandes de México como FEMSA, CEMEX y Grupo Alfa.');

-- Oaxaca
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(20, 1, 'Oaxaca tiene la mayor diversidad cultural de México con 16 grupos étnicos diferentes.'),
(20, 2, 'El estado tiene más especies de mezcal registradas que el tequila, con denominación de origen protegida.'),
(20, 3, 'Monte Albán y Mitla en Oaxaca son sitios arqueológicos zapotecas Patrimonio de la Humanidad.'),
(20, 4, 'Oaxaca es mundialmente famosa por su gastronomía, considerada una de las mejores de México.'),
(20, 5, 'El estado tiene las playas más vírgenes del Pacífico mexicano, incluyendo Puerto Escondido y Huatulco.');

-- Puebla
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(21, 1, 'Puebla es Patrimonio de la Humanidad por su arquitectura colonial y talavera poblana única en el mundo.'),
(21, 2, 'El Volkswagen Beetle (Vocho) se produjo en Puebla durante 70 años, siendo la última fábrica en el mundo.'),
(21, 3, 'Puebla es la cuna del mole poblano, uno de los platillos más emblemáticos de México.'),
(21, 4, 'El estado tiene la industria automotriz más importante de México con plantas de VW, Audi y Nissan.'),
(21, 5, 'Cholula en Puebla tiene la pirámide más grande del mundo por volumen, la Gran Pirámide de Cholula.');

-- Querétaro
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(22, 1, 'Querétaro tiene el acueducto colonial más grande de América con 74 arcos y 1,280 metros de longitud.'),
(22, 2, 'El estado es la segunda economía de más rápido crecimiento en México después de Aguascalientes.'),
(22, 3, 'Querétaro fue el lugar donde se firmó la Constitución Mexicana de 1917.'),
(22, 4, 'El estado tiene la tasa de desempleo más baja de México y una de las más bajas de América Latina.'),
(22, 5, 'La Sierra Gorda de Querétaro es Patrimonio de la Humanidad por su biodiversidad excepcional.');

-- Quintana Roo
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(23, 1, 'Cancún, en Quintana Roo, es el destino turístico más visitado de México con 30 millones de turistas anuales.'),
(23, 2, 'El estado alberga la segunda barrera de coral más grande del mundo, el Gran Arrecife Maya.'),
(23, 3, 'Tulum combina zonas arqueológicas mayas con playas paradisíacas, siendo único en el mundo.'),
(23, 4, 'Quintana Roo tiene los cenotes más espectaculares de México, sistemas de cavernas inundadas únicas.'),
(23, 5, 'El estado genera el 40% del turismo internacional de México, siendo la principal fuente de divisas.');

-- San Luis Potosí
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(24, 1, 'San Luis Potosí alberga el Sótano de las Golondrinas, un abismo de 512 metros de profundidad.'),
(24, 2, 'Real de Catorce es un pueblo fantasma del siglo XVIII convertido en destino turístico y espiritual.'),
(24, 3, 'El estado tiene la Huasteca Potosina, una región de cascadas, ríos turquesa y selva exuberante.'),
(24, 4, 'San Luis Potosí fue el centro de la Revolución Mexicana donde se firmó el Plan de San Luis.'),
(24, 5, 'El estado produce el 25% del cemento de México y es líder en minería de plata.');

-- Sinaloa
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(25, 1, 'Sinaloa es el granero de México, produciendo el 40% de los vegetales y granos del país.'),
(25, 2, 'El estado es el mayor productor de tomate de México y uno de los principales exportadores del mundo.'),
(25, 3, 'Mazatlán, Sinaloa, tiene el malecón más largo de América Latina con 21 kilómetros.'),
(25, 4, 'Sinaloa es líder nacional en producción de garbanzo, maíz y frijol.'),
(25, 5, 'El estado tiene la flota pesquera más grande del Pacífico mexicano.');

-- Sonora
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(26, 1, 'Sonora es el estado con mayor producción de trigo en México, siendo el granero del norte.'),
(26, 2, 'El Desierto de Sonora es el desierto más biodiverso del mundo con especies únicas.'),
(26, 3, 'Hermosillo es conocida como la "Capital del Sol" por tener más de 300 días de sol al año.'),
(26, 4, 'Sonora comparte la frontera más larga con Estados Unidos de todos los estados mexicanos.'),
(26, 5, 'El estado es líder en producción de carne de res certificada de alta calidad en México.');

-- Tabasco
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(27, 1, 'Tabasco es conocido como "El Edén de México" por su abundante vegetación y recursos hídricos.'),
(27, 2, 'La cultura olmeca, la más antigua de Mesoamérica, se desarrolló en Tabasco hace 3,500 años.'),
(27, 3, 'El estado produce el 70% del cacao de México, siendo la cuna del chocolate.'),
(27, 4, 'Villahermosa alberga el Parque La Venta con las cabezas olmecas más impresionantes.'),
(27, 5, 'Tabasco tiene la mayor producción de plátano de México con el 50% de la producción nacional.');

-- Tamaulipas
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(28, 1, 'Tamaulipas tiene el puerto de Altamira, uno de los más importantes del Golfo de México.'),
(28, 2, 'El estado es líder en producción de sorgo y uno de los principales productores de cítricos.'),
(28, 3, 'Tampico fue el primer puerto petrolero de México y cuna de la industria petrolera mexicana.'),
(28, 4, 'La Pesca, Tamaulipas, es uno de los mejores lugares de México para avistamiento de tortugas marinas.'),
(28, 5, 'Tamaulipas tiene 420 kilómetros de costa en el Golfo de México con importantes reservas naturales.');

-- Tlaxcala
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(29, 1, 'Tlaxcala es el estado más pequeño de México pero jugó un papel crucial en la conquista de Tenochtitlán.'),
(29, 2, 'El estado conserva más de 1,000 templos y conventos coloniales del siglo XVI.'),
(29, 3, 'Tlaxcala tiene la mayor tradición de tauromaquia en México con plazas históricas.'),
(29, 4, 'El Santuario de las Luciérnagas en Tlaxcala es un fenómeno natural único en México.'),
(29, 5, 'Tlaxcala nunca fue conquistada por los aztecas, manteniendo su independencia hasta la llegada española.');

-- Veracruz
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(30, 1, 'Veracruz tiene el puerto más importante de México, moviendo el 60% del comercio marítimo del país.'),
(30, 2, 'El Pico de Orizaba en Veracruz es el volcán y montaña más alta de México con 5,636 metros.'),
(30, 3, 'El estado produce el 50% del café de México, siendo el principal productor nacional.'),
(30, 4, 'Veracruz tiene 745 kilómetros de costa en el Golfo de México, la más larga del país.'),
(30, 5, 'El Carnaval de Veracruz es el más importante y antiguo de México, celebrado desde 1925.');

-- Yucatán
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(31, 1, 'Chichén Itzá en Yucatán es una de las Siete Maravillas del Mundo Moderno.'),
(31, 2, 'Mérida, la capital, es la ciudad más segura de México y una de las más seguras de América Latina.'),
(31, 3, 'Yucatán tiene más de 6,000 cenotes, formaciones naturales únicas en el mundo.'),
(31, 4, 'El estado conserva la cultura maya viva con más del 30% de la población hablando maya.'),
(31, 5, 'Yucatán es líder en producción de henequén (sisal), fibra natural usada mundialmente.');

-- Zacatecas
INSERT INTO datos_curiosos (id_estado, numero_dato, dato_curioso) VALUES
(32, 1, 'Zacatecas es Patrimonio de la Humanidad por su arquitectura colonial y riqueza minera histórica.'),
(32, 2, 'El estado produjo el 35% de toda la plata del mundo durante la época colonial.'),
(32, 3, 'El Cerro de la Bufa es el símbolo de Zacatecas y fue crucial en la Revolución Mexicana.'),
(32, 4, 'Zacatecas tiene el teleférico más espectacular de México con vistas panorámicas de la ciudad.'),
(32, 5, 'El estado es líder en producción de frijol y uno de los principales productores de chile seco.');

-- ============================================================================
-- INSERCIÓN DE RECURSOS
-- ============================================================================

INSERT INTO recursos (id_recurso, nombre_recurso, id_categoria, descripcion, unidad_medida, icono) VALUES
(1, 'Presupuesto', 1, 'Recursos financieros disponibles para inversión pública, programas sociales y operación gubernamental. Incluye ingresos fiscales, participaciones federales y recursos propios del estado.', 'Millones MXN', 'dollar'),
(2, 'Agua', 3, 'Recursos hídricos disponibles para consumo humano, agrícola e industrial. Incluye agua potable, sistemas de distribución, plantas de tratamiento y reservas hídricas.', 'Millones m³', 'droplet'),
(3, 'Energía', 3, 'Capacidad de generación y distribución eléctrica. Incluye plantas generadoras, redes de transmisión, energías renovables y disponibilidad energética para desarrollo.', 'Megawatts (MW)', 'zap'),
(4, 'Alimentación', 2, 'Programas de seguridad alimentaria, comedores comunitarios, despensas y sistemas de abasto popular. Garantiza acceso a alimentación nutritiva para la población.', 'Toneladas/año', 'utensils'),
(5, 'Salud', 2, 'Infraestructura hospitalaria, personal médico, medicamentos, equipamiento y programas de salud pública. Sistema integral de atención médica y prevención.', 'Camas hospitalarias', 'heart-pulse'),
(6, 'Infraestructura', 3, 'Red carretera, puentes, edificios públicos, sistemas de transporte y obras públicas. Desarrollo de infraestructura física para conectividad y servicios.', 'Kilómetros', 'building-2'),
(7, 'Desarrollo Social y Cultural', 2, 'Programas educativos, culturales, deportivos y de inclusión social. Centros comunitarios, bibliotecas, espacios recreativos y proyectos de cohesión social.', 'Programas activos', 'users'),
(8, 'Sustentabilidad Ambiental', 3, 'Proyectos de conservación, áreas naturales protegidas, programas de reciclaje, reforestación y gestión de residuos. Protección del medio ambiente.', 'Hectáreas protegidas', 'leaf'),
(9, 'Distribución', 1, 'Sistemas logísticos para distribución de recursos a comunidades. Incluye almacenes, transporte, centros de acopio y redes de distribución eficiente.', 'Centros de distribución', 'truck');

-- ============================================================================
-- INSERCIÓN DE RIESGOS
-- ============================================================================

INSERT INTO riesgos (id_riesgo, nombre_riesgo, id_nivel_riesgo, descripcion) VALUES
(1, 'Tornado', 2, 'Fenómeno meteorológico extremo con vientos violentos que puede causar daños severos a infraestructura y afectar el suministro de servicios básicos.'),
(2, 'Cambio Climático', 2, 'Alteración de patrones climáticos que afecta producción agrícola, disponibilidad de agua y ecosistemas naturales a largo plazo.'),
(3, 'Ciberataques', 3, 'Ataques informáticos dirigidos a sistemas de información gubernamentales que pueden comprometer datos sensibles, servicios públicos y gestión financiera.'),
(4, 'Inseguridad', 2, 'Situación de violencia y criminalidad que afecta la movilidad, distribución de recursos, operación de servicios y desarrollo económico.'),
(5, 'Escasez', 1, 'Falta de recursos básicos (agua, alimentos, energía) por sequía, sobrepoblación o mala gestión, afectando calidad de vida y desarrollo.');

-- ============================================================================
-- RELACIÓN: RIESGOS POR REGIÓN
-- ============================================================================

-- Región Sureste (1) - Alta vulnerabilidad a fenómenos tropicales y cambio climático
INSERT INTO region_riesgo (id_region, id_riesgo, probabilidad, impacto_estimado) VALUES
(1, 2, 85.00, 'Alto'), -- Cambio climático
(1, 5, 45.00, 'Medio'), -- Escasez
(1, 4, 60.00, 'Medio'); -- Inseguridad

-- Región Centro (2) - Sobrepoblación y gestión de recursos
INSERT INTO region_riesgo (id_region, id_riesgo, probabilidad, impacto_estimado) VALUES
(2, 5, 70.00, 'Alto'), -- Escasez
(2, 3, 55.00, 'Medio'), -- Ciberataques
(2, 4, 65.00, 'Alto'); -- Inseguridad

-- Región Occidente (3) - Zona sísmica y fenómenos naturales
INSERT INTO region_riesgo (id_region, id_riesgo, probabilidad, impacto_estimado) VALUES
(3, 1, 40.00, 'Medio'), -- Tornado
(3, 2, 60.00, 'Medio'), -- Cambio climático
(3, 4, 70.00, 'Alto'); -- Inseguridad

-- Región Norte (4) - Sequía, inseguridad y ciberataques
INSERT INTO region_riesgo (id_region, id_riesgo, probabilidad, impacto_estimado) VALUES
(4, 5, 80.00, 'Alto'), -- Escasez (sequía)
(4, 4, 75.00, 'Alto'), -- Inseguridad
(4, 3, 60.00, 'Medio'), -- Ciberataques
(4, 1, 35.00, 'Medio'); -- Tornado

-- Región Centro-Sur (5) - Alta densidad urbana, ciberataques y escasez
INSERT INTO region_riesgo (id_region, id_riesgo, probabilidad, impacto_estimado) VALUES
(5, 3, 90.00, 'Crítico'), -- Ciberataques (zona urbana)
(5, 5, 65.00, 'Alto'), -- Escasez
(5, 4, 55.00, 'Medio'), -- Inseguridad
(5, 2, 70.00, 'Alto'); -- Cambio climático

-- ============================================================================
-- RELACIÓN: RECURSOS AFECTADOS POR RIESGOS
-- ============================================================================

-- Tornado (1) afecta:
INSERT INTO riesgo_recurso_afectacion (id_riesgo, id_recurso, nivel_afectacion, porcentaje_impacto, descripcion_impacto) VALUES
(1, 6, 'Severo', 75.00, 'Destrucción de carreteras, puentes y edificios. Infraestructura dañada requiere reconstrucción inmediata.'),
(1, 2, 'Moderado', 50.00, 'Daño a sistemas de distribución de agua, tuberías rotas y contaminación de fuentes hídricas.');

-- Cambio Climático (2) afecta:
INSERT INTO riesgo_recurso_afectacion (id_riesgo, id_recurso, nivel_afectacion, porcentaje_impacto, descripcion_impacto) VALUES
(2, 4, 'Severo', 65.00, 'Reducción de producción agrícola por sequías, inundaciones y cambios en patrones de lluvias.'),
(2, 8, 'Crítico', 80.00, 'Pérdida de biodiversidad, degradación de ecosistemas y afectación de áreas naturales protegidas.');

-- Ciberataques (3) afecta:
INSERT INTO riesgo_recurso_afectacion (id_riesgo, id_recurso, nivel_afectacion, porcentaje_impacto, descripcion_impacto) VALUES
(3, 5, 'Severo', 70.00, 'Compromiso de sistemas hospitalarios, expedientes médicos y gestión de inventarios de medicamentos.'),
(3, 1, 'Crítico', 85.00, 'Robo o manipulación de información financiera, sistemas de pago y gestión presupuestal.');

-- Inseguridad (4) afecta:
INSERT INTO riesgo_recurso_afectacion (id_riesgo, id_recurso, nivel_afectacion, porcentaje_impacto, descripcion_impacto) VALUES
(4, 9, 'Severo', 75.00, 'Robo de mercancías, bloqueos de carreteras y dificultad para distribuir recursos a comunidades.'),
(4, 3, 'Moderado', 45.00, 'Sabotaje a infraestructura energética, robo de cable y afectación a redes de distribución.');

-- Escasez (5) afecta:
INSERT INTO riesgo_recurso_afectacion (id_riesgo, id_recurso, nivel_afectacion, porcentaje_impacto, descripcion_impacto) VALUES
(5, 4, 'Crítico', 90.00, 'Falta de alimentos básicos, incremento de precios y necesidad de programas de emergencia alimentaria.'),
(5, 6, 'Moderado', 55.00, 'Retraso en proyectos de infraestructura por falta de materiales y recursos financieros.'),
(5, 5, 'Severo', 70.00, 'Escasez de medicamentos, equipo médico y personal de salud, afectando atención a pacientes.');

-- ============================================================================
-- TRIGGERS PARA AUDITORÍA Y VALIDACIÓN
-- ============================================================================

-- Trigger: Registrar cambios en el estado de los estados
DELIMITER //
CREATE TRIGGER after_estado_update
AFTER UPDATE ON estados
FOR EACH ROW
BEGIN
    IF OLD.estado_actual != NEW.estado_actual THEN
        INSERT INTO historial_estados (id_estado, estado_anterior, estado_nuevo, motivo)
        VALUES (NEW.id_estado, OLD.estado_actual, NEW.estado_actual, 
                CONCAT('Cambio automático registrado el ', NOW()));
    END IF;
END;//
DELIMITER ;

-- Trigger: Validar que cantidad_asignada no exceda cantidad_disponible
DELIMITER //
CREATE TRIGGER before_distribucion_insert
BEFORE INSERT ON distribucion_recursos
FOR EACH ROW
BEGIN
    IF NEW.cantidad_asignada > NEW.cantidad_disponible THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad asignada no puede exceder la cantidad disponible';
    END IF;
    
    -- Calcular índice de disponibilidad automáticamente
    SET NEW.indice_disponibilidad = 
        ((NEW.cantidad_disponible - NEW.cantidad_asignada) / NEW.cantidad_disponible) * 100;
END;//
DELIMITER ;

DELIMITER //
CREATE TRIGGER before_distribucion_update
BEFORE UPDATE ON distribucion_recursos
FOR EACH ROW
BEGIN
    IF NEW.cantidad_asignada > NEW.cantidad_disponible THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad asignada no puede exceder la cantidad disponible';
    END IF;
    
    -- Calcular índice de disponibilidad automáticamente
    SET NEW.indice_disponibilidad = 
        ((NEW.cantidad_disponible - NEW.cantidad_asignada) / NEW.cantidad_disponible) * 100;
END;//
DELIMITER ;

-- ============================================================================
-- VISTAS ÚTILES PARA LA APLICACIÓN WEB
-- ============================================================================

-- Vista: Resumen completo de estados con región y riesgos
CREATE VIEW vista_estados_completo AS
SELECT 
    e.id_estado,
    e.nombre_estado,
    e.capital,
    r.nombre_region,
    e.estado_actual,
    ne.nombre_nivel AS nivel_estado,
    ne.color_indicador,
    COUNT(DISTINCT rr.id_riesgo) AS total_riesgos_region,
    e.created_at,
    e.updated_at
FROM estados e
JOIN regiones r ON e.id_region = r.id_region
JOIN niveles_estado ne ON e.estado_actual = ne.id_nivel
LEFT JOIN region_riesgo rr ON r.id_region = rr.id_region
GROUP BY e.id_estado, e.nombre_estado, e.capital, r.nombre_region, 
         e.estado_actual, ne.nombre_nivel, ne.color_indicador, e.created_at, e.updated_at;

-- Vista: Recursos con categoría y total de riesgos que los afectan
CREATE VIEW vista_recursos_riesgos AS
SELECT 
    rec.id_recurso,
    rec.nombre_recurso,
    cat.nombre_categoria,
    rec.descripcion,
    rec.unidad_medida,
    COUNT(rra.id_riesgo) AS total_riesgos_afectan,
    GROUP_CONCAT(rie.nombre_riesgo SEPARATOR ', ') AS riesgos_asociados
FROM recursos rec
JOIN categorias_recurso cat ON rec.id_categoria = cat.id_categoria
LEFT JOIN riesgo_recurso_afectacion rra ON rec.id_recurso = rra.id_recurso
LEFT JOIN riesgos rie ON rra.id_riesgo = rie.id_riesgo
GROUP BY rec.id_recurso, rec.nombre_recurso, cat.nombre_categoria, 
         rec.descripcion, rec.unidad_medida;

-- Vista: Distribución de recursos por estado con disponibilidad
CREATE VIEW vista_distribucion_estado AS
SELECT 
    e.nombre_estado,
    r.nombre_recurso,
    dr.cantidad_disponible,
    dr.cantidad_asignada,
    dr.cantidad_reserva,
    dr.indice_disponibilidad,
    dr.fecha_actualizacion,
    CASE 
        WHEN dr.indice_disponibilidad >= 75 THEN 'Óptimo'
        WHEN dr.indice_disponibilidad >= 50 THEN 'Adecuado'
        WHEN dr.indice_disponibilidad >= 25 THEN 'Bajo'
        ELSE 'Crítico'
    END AS nivel_disponibilidad
FROM distribucion_recursos dr
JOIN estados e ON dr.id_estado = e.id_estado
JOIN recursos r ON dr.id_recurso = r.id_recurso;

-- Vista: Riesgos por región con nivel de amenaza
CREATE VIEW vista_riesgos_region AS
SELECT 
    reg.nombre_region,
    ries.nombre_riesgo,
    nr.nombre_nivel AS nivel_riesgo,
    rr.probabilidad,
    rr.impacto_estimado,
    CASE 
        WHEN rr.probabilidad >= 75 AND rr.impacto_estimado IN ('Alto', 'Crítico') THEN 'Alerta Máxima'
        WHEN rr.probabilidad >= 50 THEN 'Alerta Moderada'
        ELSE 'Monitoreo'
    END AS nivel_alerta
FROM region_riesgo rr
JOIN regiones reg ON rr.id_region = reg.id_region
JOIN riesgos ries ON rr.id_riesgo = ries.id_riesgo
JOIN niveles_riesgo nr ON ries.id_nivel_riesgo = nr.id_nivel_riesgo;

-- ============================================================================
-- PROCEDIMIENTOS ALMACENADOS ÚTILES
-- ============================================================================

-- Procedimiento: Obtener dato curioso aleatorio de un estado
DELIMITER //
CREATE PROCEDURE obtener_dato_curioso_aleatorio(IN p_id_estado INT)
BEGIN
    SELECT dato_curioso
    FROM datos_curiosos
    WHERE id_estado = p_id_estado
    ORDER BY RAND()
    LIMIT 1;
END;//
DELIMITER ;

-- Procedimiento: Cambiar estado de un estado (con auditoría)
DELIMITER //
CREATE PROCEDURE cambiar_estado_estado(
    IN p_id_estado INT,
    IN p_nuevo_estado INT,
    IN p_motivo TEXT,
    IN p_usuario VARCHAR(100)
)
BEGIN
    DECLARE v_estado_anterior INT;
    
    SELECT estado_actual INTO v_estado_anterior
    FROM estados
    WHERE id_estado = p_id_estado;
    
    UPDATE estados
    SET estado_actual = p_nuevo_estado
    WHERE id_estado = p_id_estado;
    
    INSERT INTO historial_estados (id_estado, estado_anterior, estado_nuevo, motivo, usuario)
    VALUES (p_id_estado, v_estado_anterior, p_nuevo_estado, p_motivo, p_usuario);
    
    SELECT 'Estado actualizado exitosamente' AS mensaje;
END;//
DELIMITER ;

-- Procedimiento: Obtener riesgos de un estado según su región
DELIMITER //
CREATE PROCEDURE obtener_riesgos_estado(IN p_id_estado INT)
BEGIN
    SELECT 
        r.nombre_riesgo,
        nr.nombre_nivel,
        rr.probabilidad,
        rr.impacto_estimado,
        GROUP_CONCAT(rec.nombre_recurso SEPARATOR ', ') AS recursos_afectados
    FROM estados e
    JOIN region_riesgo rr ON e.id_region = rr.id_region
    JOIN riesgos r ON rr.id_riesgo = r.id_riesgo
    JOIN niveles_riesgo nr ON r.id_nivel_riesgo = nr.id_nivel_riesgo
    LEFT JOIN riesgo_recurso_afectacion rra ON r.id_riesgo = rra.id_riesgo
    LEFT JOIN recursos rec ON rra.id_recurso = rec.id_recurso
    WHERE e.id_estado = p_id_estado
    GROUP BY r.id_riesgo, r.nombre_riesgo, nr.nombre_nivel, rr.probabilidad, rr.impacto_estimado
    ORDER BY rr.probabilidad DESC;
END;//
DELIMITER ;

-- Procedimiento: Resumen ejecutivo de un estado
DELIMITER //
CREATE PROCEDURE resumen_ejecutivo_estado(IN p_id_estado INT)
BEGIN
    -- Información básica del estado
    SELECT 
        e.nombre_estado,
        e.capital,
        r.nombre_region,
        ne.nombre_nivel AS estado_actual,
        ne.color_indicador
    FROM estados e
    JOIN regiones r ON e.id_region = r.id_region
    JOIN niveles_estado ne ON e.estado_actual = ne.id_nivel
    WHERE e.id_estado = p_id_estado;
    
    -- Riesgos asociados
    SELECT 
        rie.nombre_riesgo,
        rr.probabilidad,
        rr.impacto_estimado
    FROM estados e
    JOIN region_riesgo rr ON e.id_region = rr.id_region
    JOIN riesgos rie ON rr.id_riesgo = rie.id_riesgo
    WHERE e.id_estado = p_id_estado
    ORDER BY rr.probabilidad DESC;
    
    -- Datos curiosos
    SELECT dato_curioso
    FROM datos_curiosos
    WHERE id_estado = p_id_estado;
END;//
DELIMITER ;

-- ============================================================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN DE CONSULTAS
-- ============================================================================

CREATE INDEX idx_distribucion_fecha ON distribucion_recursos(fecha_actualizacion DESC);
CREATE INDEX idx_historial_fecha ON historial_estados(fecha_cambio DESC);
CREATE INDEX idx_estado_nombre ON estados(nombre_estado);
CREATE INDEX idx_recurso_nombre ON recursos(nombre_recurso);
CREATE INDEX idx_riesgo_nombre ON riesgos(nombre_riesgo);