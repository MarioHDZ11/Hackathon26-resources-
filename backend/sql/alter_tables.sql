-- ============================================================================
-- BISourcesMX - Database Migration Script
-- Adds columns needed by the frontend that don't exist in the original schema
-- Run this AFTER the main gestion_recursos_mx.sql script
-- ============================================================================

USE gestion_recursos_mx;

-- 1. Add 3-letter code for frontend state identification
ALTER TABLE estados
  ADD COLUMN codigo_3 VARCHAR(3) UNIQUE AFTER capital,
  ADD COLUMN simbolo_emoji VARCHAR(10) AFTER codigo_3,
  ADD COLUMN descripcion_corta VARCHAR(200) AFTER simbolo_emoji,
  ADD COLUMN svg_path TEXT AFTER descripcion_corta,
  ADD COLUMN es_costero TINYINT(1) DEFAULT 0 AFTER superficie_km2;

-- 2. Update estados with 3-letter codes, symbols, descriptions, coastal flags
UPDATE estados SET codigo_3 = 'agu', simbolo_emoji = '🏭', descripcion_corta = 'Principal centro automotriz y tecnológico del país.', es_costero = 0 WHERE id_estado = 1;
UPDATE estados SET codigo_3 = 'bcn', simbolo_emoji = '🎬', descripcion_corta = 'Capital de la industria cinematográfica mexicana.', es_costero = 1 WHERE id_estado = 2;
UPDATE estados SET codigo_3 = 'bcs', simbolo_emoji = '🐋', descripcion_corta = 'Santuario de la ballena gris, Patrimonio de la Humanidad.', es_costero = 1 WHERE id_estado = 3;
UPDATE estados SET codigo_3 = 'cam', simbolo_emoji = '🏰', descripcion_corta = 'Zona arqueológica maya y ciudad amurallada.', es_costero = 1 WHERE id_estado = 4;
UPDATE estados SET codigo_3 = 'chp', simbolo_emoji = '🌿', descripcion_corta = 'Mayor productor de café de altura en México.', es_costero = 1 WHERE id_estado = 5;
UPDATE estados SET codigo_3 = 'chh', simbolo_emoji = '🌰', descripcion_corta = 'Principal exportador de nuez a nivel nacional.', es_costero = 0 WHERE id_estado = 6;
UPDATE estados SET codigo_3 = 'cmx', simbolo_emoji = '🏛️', descripcion_corta = 'Capital del país, centro político y cultural de México.', es_costero = 0 WHERE id_estado = 7;
UPDATE estados SET codigo_3 = 'coa', simbolo_emoji = '🍇', descripcion_corta = 'Región vitivinícola más importante de Latinoamérica.', es_costero = 0 WHERE id_estado = 8;
UPDATE estados SET codigo_3 = 'col', simbolo_emoji = '🌋', descripcion_corta = 'Hogar del Volcán de Fuego, el más activo de México.', es_costero = 1 WHERE id_estado = 9;
UPDATE estados SET codigo_3 = 'dur', simbolo_emoji = '🤠', descripcion_corta = 'Cuna del cine mexicano y la charrería.', es_costero = 0 WHERE id_estado = 10;
UPDATE estados SET codigo_3 = 'mex', simbolo_emoji = '🏗️', descripcion_corta = 'Estado más poblado, motor industrial del país.', es_costero = 0 WHERE id_estado = 11;
UPDATE estados SET codigo_3 = 'gua', simbolo_emoji = '🪙', descripcion_corta = 'Mayor productor de plata en la historia de México.', es_costero = 0 WHERE id_estado = 12;
UPDATE estados SET codigo_3 = 'gro', simbolo_emoji = '🏖️', descripcion_corta = 'Icónico destino turístico de playa Acapulco.', es_costero = 1 WHERE id_estado = 13;
UPDATE estados SET codigo_3 = 'hid', simbolo_emoji = '⛏️', descripcion_corta = 'Rica en minería de plata y cantera rosa.', es_costero = 0 WHERE id_estado = 14;
UPDATE estados SET codigo_3 = 'jal', simbolo_emoji = '🌵', descripcion_corta = 'Cuna del tequila y el mariachi, Patrimonio Mundial.', es_costero = 1 WHERE id_estado = 15;
UPDATE estados SET codigo_3 = 'mic', simbolo_emoji = '🦋', descripcion_corta = 'Santuario de la Mariposa Monarca, Biosfera UNESCO.', es_costero = 1 WHERE id_estado = 16;
UPDATE estados SET codigo_3 = 'mor', simbolo_emoji = '🌺', descripcion_corta = 'Tierra de jardines y tradición floral.', es_costero = 0 WHERE id_estado = 17;
UPDATE estados SET codigo_3 = 'nay', simbolo_emoji = '🌴', descripcion_corta = 'Riviera Nayarit, paraíso del surf y ecoturismo.', es_costero = 1 WHERE id_estado = 18;
UPDATE estados SET codigo_3 = 'nle', simbolo_emoji = '🏭', descripcion_corta = 'Potencia industrial con la mayor competitividad del país.', es_costero = 0 WHERE id_estado = 19;
UPDATE estados SET codigo_3 = 'oax', simbolo_emoji = '🏺', descripcion_corta = 'Capital mundial del mezcal y la artesanía textil.', es_costero = 1 WHERE id_estado = 20;
UPDATE estados SET codigo_3 = 'pue', simbolo_emoji = '🎆', descripcion_corta = 'Cuna de la Talavera y la cocina mestiza mexicana.', es_costero = 0 WHERE id_estado = 21;
UPDATE estados SET codigo_3 = 'que', simbolo_emoji = '🚀', descripcion_corta = 'Hub aeroespacial y de innovación tecnológica.', es_costero = 0 WHERE id_estado = 22;
UPDATE estados SET codigo_3 = 'roo', simbolo_emoji = '🐠', descripcion_corta = 'Arrecife Mesoamericano, segundo más grande del mundo.', es_costero = 1 WHERE id_estado = 23;
UPDATE estados SET codigo_3 = 'slp', simbolo_emoji = '🪨', descripcion_corta = 'Histórica zona minera de oro y zinc.', es_costero = 0 WHERE id_estado = 24;
UPDATE estados SET codigo_3 = 'sin', simbolo_emoji = '🐟', descripcion_corta = 'Mayor productor pesquero de camarón del país.', es_costero = 1 WHERE id_estado = 25;
UPDATE estados SET codigo_3 = 'son', simbolo_emoji = '🐄', descripcion_corta = 'Líder nacional en producción ganadera.', es_costero = 1 WHERE id_estado = 26;
UPDATE estados SET codigo_3 = 'tab', simbolo_emoji = '🍫', descripcion_corta = 'Cuna del cacao y la cultura olmeca.', es_costero = 1 WHERE id_estado = 27;
UPDATE estados SET codigo_3 = 'tam', simbolo_emoji = '🌾', descripcion_corta = 'Gran productor de sorgo y maíz del norte.', es_costero = 1 WHERE id_estado = 28;
UPDATE estados SET codigo_3 = 'tla', simbolo_emoji = '🧶', descripcion_corta = 'Famoso por sus textiles artesanales de sarapes.', es_costero = 0 WHERE id_estado = 29;
UPDATE estados SET codigo_3 = 'ver', simbolo_emoji = '☕', descripcion_corta = 'Principal productor de café y vainilla de México.', es_costero = 1 WHERE id_estado = 30;
UPDATE estados SET codigo_3 = 'yuc', simbolo_emoji = '🏛️', descripcion_corta = 'Chichén Itzá, Maravilla del Mundo Moderno.', es_costero = 1 WHERE id_estado = 31;
UPDATE estados SET codigo_3 = 'zac', simbolo_emoji = '🪙', descripcion_corta = 'Histórico centro minero de plata y zinc.', es_costero = 0 WHERE id_estado = 32;

-- 3. Note: The SVG path data is too large for UPDATE statements.
--    It will be loaded via a Java data initialization script at application startup
--    See data-loader section in the backend application.
--    The svg_path column exists and can be populated via the import-svg.sql script.
