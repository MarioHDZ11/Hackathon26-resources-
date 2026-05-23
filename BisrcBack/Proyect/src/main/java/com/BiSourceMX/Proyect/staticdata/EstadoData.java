package com.BiSourceMX.Proyect.staticdata;

import java.util.List;
import java.util.Map;

public class EstadoData {

    public static class DetalleIntercambio {
        private String nivel;
        private String razon;
        private String como;

        public DetalleIntercambio() {}

        public DetalleIntercambio(String nivel, String razon, String como) {
            this.nivel = nivel;
            this.razon = razon;
            this.como = como;
        }

        public String getNivel() { return nivel; }
        public void setNivel(String nivel) { this.nivel = nivel; }

        public String getRazon() { return razon; }
        public void setRazon(String razon) { this.razon = razon; }

        public String getComo() { return como; }
        public void setComo(String como) { this.como = como; }
    }

    public static class Intercambio {
        private DetalleIntercambio agua;
        private DetalleIntercambio energia;
        private DetalleIntercambio presupuesto;
        private DetalleIntercambio alimento;
        private DetalleIntercambio trabajadores;

        public Intercambio() {}

        public Intercambio(DetalleIntercambio agua, DetalleIntercambio energia, DetalleIntercambio presupuesto, DetalleIntercambio alimento, DetalleIntercambio trabajadores) {
            this.agua = agua;
            this.energia = energia;
            this.presupuesto = presupuesto;
            this.alimento = alimento;
            this.trabajadores = trabajadores;
        }

        public DetalleIntercambio getAgua() { return agua; }
        public void setAgua(DetalleIntercambio agua) { this.agua = agua; }

        public DetalleIntercambio getEnergia() { return energia; }
        public void setEnergia(DetalleIntercambio energia) { this.energia = energia; }

        public DetalleIntercambio getPresupuesto() { return presupuesto; }
        public void setPresupuesto(DetalleIntercambio presupuesto) { this.presupuesto = presupuesto; }

        public DetalleIntercambio getAlimento() { return alimento; }
        public void setAlimento(DetalleIntercambio alimento) { this.alimento = alimento; }

        public DetalleIntercambio getTrabajadores() { return trabajadores; }
        public void setTrabajadores(DetalleIntercambio trabajadores) { this.trabajadores = trabajadores; }
    }

    public static class Estado {
        private String nombre;
        private String region;
        private String clima;
        private List<String> riesgos;
        private String aguaInfo;
        private String luzInfo;
        private Intercambio intercambio;

        public Estado() {}

        public Estado(String nombre, String region, String clima, List<String> riesgos, String aguaInfo, String luzInfo, Intercambio intercambio) {
            this.nombre = nombre;
            this.region = region;
            this.clima = clima;
            this.riesgos = riesgos;
            this.aguaInfo = aguaInfo;
            this.luzInfo = luzInfo;
            this.intercambio = intercambio;
        }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }

        public String getClima() { return clima; }
        public void setClima(String clima) { this.clima = clima; }

        public List<String> getRiesgos() { return riesgos; }
        public void setRiesgos(List<String> riesgos) { this.riesgos = riesgos; }

        public String getAguaInfo() { return aguaInfo; }
        public void setAguaInfo(String aguaInfo) { this.aguaInfo = aguaInfo; }

        public String getLuzInfo() { return luzInfo; }
        public void setLuzInfo(String luzInfo) { this.luzInfo = luzInfo; }

        public Intercambio getIntercambio() { return intercambio; }
        public void setIntercambio(Intercambio intercambio) { this.intercambio = intercambio; }
    }

    public static final Map<String, String> REGION_COLORS = Map.of(
        "Norte", "#ef4444",
        "Noroeste", "#f97316",
        "Noreste", "#eab308",
        "Centro-Norte", "#84cc16",
        "Centro", "#22c55e",
        "Centro-Sur", "#14b8a6",
        "Occidente", "#3b82f6",
        "Sur", "#8b5cf6",
        "Sureste", "#ec4899",
        "Golfo-Centro", "#06b6d4"
    );

    public static final Map<String, String> RISK_COLORS = Map.of(
        "Huracanes", "#ef4444",
        "Terremotos", "#f97316",
        "Inundaciones", "#3b82f6",
        "Sequ\u00edas", "#eab308",
        "Volcanes", "#dc2626",
        "Deslaves", "#92400e",
        "Tsunamis", "#0284c7",
        "Heladas", "#7dd3fc",
        "Incendios", "#b45309"
    );

    public static final List<Estado> ESTADOS = List.of(
        new Estado(
            "Aguascalientes",
            "Centro-Norte",
            "Semiseco templado. Veranos c\u00e1lidos (30\u201335\u00b0C), inviernos fr\u00edos con heladas ocasionales. Lluvias en verano (junio\u2013septiembre).",
            List.of("Sequ\u00edas prolongadas", "Granizadas", "Heladas", "Lluvias torrenciales ocasionales"),
            "Acu\u00edferos subterr\u00e1neos (principal fuente), Presa Las Fraguas, Presa Calles. Alta sobreexplotaci\u00f3n de mantos fre\u00e1ticos.",
            "Red Nacional CFE. Energ\u00eda t\u00e9rmica convencional como base, con creciente integraci\u00f3n de parques solares en zona norte.",
            new Intercambio(
                new DetalleIntercambio("no", "Acu\u00edferos sobreexplotados al 150% de su capacidad de recarga. Estado en d\u00e9ficit h\u00eddrico permanente.", "No aplica. Requiere importar agua de estados vecinos."),
                new DetalleIntercambio("limitado", "Generaci\u00f3n t\u00e9rmica b\u00e1sica cubre demanda local. Parques solares incipientes podr\u00edan tener excedentes futuros.", "V\u00eda red interconectada CFE Centro-Norte. Capacidad de exportaci\u00f3n solar proyectada hacia 2026\u20132030."),
                new DetalleIntercambio("alto", "PIB industrial s\u00f3lido (manufactura automotriz, textil). Ingresos fiscales estables. Baja deuda p\u00fablica estatal.", "Participaciones federales, inversi\u00f3n directa, cr\u00e9dito a estados vecinos in crisis. Atracci\u00f3n de inversi\u00f3n a la regi\u00f3n."),
                new DetalleIntercambio("limitado", "Producci\u00f3n agropecuaria existe (guayaba, ave, cerdo) pero el agua limitada restringe escala de producci\u00f3n.", "Exporta guayaba y productos av\u00edcolas. No puede escalar sin resolver d\u00e9ficit h\u00eddrico primero."),
                new DetalleIntercambio("alto", "Baja desempleo, alta escolaridad. PEA excedente en manufactura y servicios puede movilizarse.", "Migraci\u00f3n circular a CDMX, Jalisco, Nuevo Le\u00f3n. Programas de movilidad laboral industrial.")
            )
        ),
        new Estado(
            "Baja California",
            "Noroeste",
            "Mediterr\u00e1neo en costa noroeste (Tijuana/Ensenada), des\u00e9rtico seco en el este (Mexicali). Veranos muy calurosos hasta 48\u00b0C en Mexicali.",
            List.of("Terremotos (falla de San Andr\u00e9s)", "Tsunamis en costa", "Sequ\u00edas extremas", "Olas de calor", "Incendios forestales"),
            "Acueducto R\u00edo Colorado\u2013Tijuana (principal), R\u00edo Colorado, desaladoras en proyectos. Alta dependencia de acuerdo bilateral con EUA.",
            "CFE + importaci\u00f3n desde EUA. Central Termoel\u00e9ctrica Presidente Ju\u00e1rez. Parques e\u00f3licos en Sierra de Ju\u00e1rez. Proyectos solares en Mexicali.",
            new Intercambio(
                new DetalleIntercambio("no", "Dependencia cr\u00edtica del R\u00edo Colorado (EUA). Sin fuentes propias suficientes. Cualquier exportaci\u00f3n agravar\u00eda la crisis local.", "No aplica. Es receptor, no exportador."),
                new DetalleIntercambio("alto", "Parques e\u00f3licos en Sierra de Ju\u00e1rez y proyectos solares en Mexicali generan excedentes. Interconexi\u00f3n con EUA.", "Exporta energ\u00eda e\u00f3lica y solar a red CFE nacional y vende excedentes al mercado el\u00e9ctrico de California (EUA) v\u00eda l\u00edneas de interconexi\u00f3n."),
                new DetalleIntercambio("alto", "Econom\u00eda maquiladora y turismo de Tijuana/Ensenada generan PIB per c\u00e1pita alto. Ingresos aduanales significativos.", "Inversi\u00f3n regional, participaciones al fondo federal, cr\u00e9dito interestatal."),
                new DetalleIntercambio("alto", "Valle de Mexicali: mayor zona arrocera y triguera del noroeste. Ensenada: producci\u00f3n vin\u00edcola y olivarera. Valle de San Quint\u00edn: hortalizas.", "Exporta vino, aceite de oliva, trigo, arroz y hortalizas a nivel nacional e internacional (EUA). Cadenas de fr\u00edo y log\u00edstica desarrolladas."),
                new DetalleIntercambio("alto", "Alta demanda de mano de obra en maquiladoras. Atrae trabajadores de todo M\u00e9xico. Tambi\u00e9n puede exportar t\u00e9cnicos especializados.", "Migraci\u00f3n interna masiva hacia Tijuana y Mexicali. Programas de reclutamiento industrial.")
            )
        ),
        new Estado(
            "Baja California Sur",
            "Noroeste",
            "Des\u00e9rtico en norte y centro, semitropical en Los Cabos. Veranos muy calurosos (38\u201342\u00b0C). Temporada de huracanes junio\u2013noviembre.",
            List.of("Huracanes (zona de alto impacto)", "Sequ\u00edas extremas", "Tsunamis", "Erosi\u00f3n costera", "Escasez h\u00eddrica cr\u00f3nica"),
            "Acu\u00edferos locales sobreexplotados, plantas desaladoras (La Paz, Los Cabos), agua pluvial captada. Importaci\u00f3n limitada por cami\u00f3n.",
            "Sistemas aislados CFE. Termoel\u00e9ctrica en La Paz. Alta penetraci\u00f3n solar y e\u00f3lica por pol\u00edtica de zonas aisladas. Di\u00e9sel como respaldo.",
            new Intercambio(
                new DetalleIntercambio("no", "Escasez extrema. \u00danica fuente son acu\u00edferos sobreexplotados y desaladoras. No tiene excedente alguno.", "No aplica. Es el estado con mayor estr\u00e9s h\u00eddrico per c\u00e1pita del pa\u00eds."),
                new DetalleIntercambio("limitado", "Sistemas aislados de la red nacional. Alta penetraci\u00f3n solar y e\u00f3lica pero para consumo propio en zonas remotas.", "No tiene interconexi\u00f3n f\u00edsica con la red nacional. Sin capacidad de exportaci\u00f3n actual."),
                new DetalleIntercambio("alto", "Los Cabos y La Paz generan ingresos tur\u00edsticos muy elevados. Alta captaci\u00f3n de d\u00f3lares y divisas.", "Aporta al fondo federal via impuestos tur\u00edsticos. Puede financiar proyectos regionales de agua o infraestructura."),
                new DetalleIntercambio("limitado", "Pesca: at\u00fan, marl\u00edn, camar\u00f3n, langosta en abundancia. Agricultura muy limitada por agua.", "Exporta productos pesqueros al mercado nacional e internacional. No puede exportar cultivos."),
                new DetalleIntercambio("alto", "Alta demanda de trabajadores en hoteler\u00eda, restaurantes y construcci\u00f3n tur\u00edstica.", "Atrae mano de obra de Sinaloa, Guerrero, Oaxaca. Puede exportar pescadores y t\u00e9cnicos marinos.")
            )
        ),
        new Estado(
            "Campeche",
            "Sureste",
            "C\u00e1lido h\u00famedo. Temperaturas de 24\u201336\u00b0C todo el a\u00f1o. Lluvias intensas mayo\u2013octubre. Alta humedad relativa.",
            List.of("Huracanes", "Inundaciones costeras", "Ciclones tropicales", "Mareas de tormenta", "Deforestaci\u00f3n y hundimiento de suelos"),
            "R\u00edos Candelaria, Champot\u00f3n y Palizada. Lagunas costeras. Pozos subterr\u00e1neos. Abundancia relativa pero contaminaci\u00f3n creciente.",
            "CFE Red peninsular. Termoel\u00e9ctrica M\u00e9rida como respaldo regional. Proyectos solares en zonas rurales. Petr\u00f3leo y gas local como fuente hist\u00f3rica.",
            new Intercambio(
                new DetalleIntercambio("alto", "Abundancia h\u00eddrica: r\u00edos caudalosos, lluvias intensas, lagunas. Super\u00e1vit hist\u00f3rico.", "R\u00edos Candelaria y Champot\u00f3n pueden abastecer a Yucat\u00e1n y Quintana Roo v\u00eda infraestructura hidr\u00e1ulica o trasvase regulado."),
                new DetalleIntercambio("limitado", "Producci\u00f3n de petr\u00f3leo y gas (Pemex) es enorme, pero la generaci\u00f3n el\u00e9ctrica local es limitada.", "Exporta gas natural y petr\u00f3leo crudo a nivel nacional. Generaci\u00f3n el\u00e9ctrica depende de red peninsular CFE."),
                new DetalleIntercambio("alto", "Regal\u00edas petroleras hist\u00f3ricamente altas. Campeche ha sido el mayor contribuyente fiscal per c\u00e1pita de M\u00e9xico.", "Fondo de participaciones federales. Inversi\u00f3n p\u00fablica en infraestructura del Sureste. Tren Maya."),
                new DetalleIntercambio("alto", "Agricultura tropical: ma\u00edz, soya, sorgo, mango, coco. Ganader\u00eda bovina extensiva. Pesca abundante.", "Exporta ganado bovino, ma\u00edz y pescado al sureste y centro del pa\u00eds v\u00eda carretera y mar\u00edtimo."),
                new DetalleIntercambio("limitado", "Poblaci\u00f3n peque\u00f1a. Sector petrolero absorbe mano de obra. Puede aportar t\u00e9cnicos petroleros a otras regiones.", "Movilidad de t\u00e9cnicos Pemex hacia otras zonas de extracci\u00f3n. Recibe m\u00e1s trabajadores de los que exporta.")
            )
        ),
        new Estado(
            "Chiapas",
            "Sureste",
            "Muy variado: tropical h\u00famedo en selva (Lacandona), templado en Los Altos, semiseco en Ca\u00f1\u00f3n del Sumidero. Lluvias intensas.",
            List.of("Terremotos (alta sismicidad)", "Deslaves", "Inundaciones severas", "Huracanes en costa", "Erupciones volc\u00e1nicas (Volc\u00e1n Tacan\u00e1)"),
            "R\u00edo Grijalva, R\u00edo Usumacinta, R\u00edo Lacant\u00fan. Presas: Chicoas\u00e9n (mayor en M\u00e9xico), Malpaso, La Angostura, Pe\u00f1itas. Gran riqueza h\u00eddrica.",
            "Principal generador hidroel\u00e9ctrico de M\u00e9xico. Complejo Grijalva produce ~30% energ\u00eda hidroel\u00e9ctrica nacional. CFE presas Chicoas\u00e9n y Malpaso.",
            new Intercambio(
                new DetalleIntercambio("alto", "El estado con mayor riqueza h\u00eddrica de M\u00e9xico. R\u00edos Grijalva y Usumacinta entre los m\u00e1s caudalosos del pa\u00eds.", "Abastece a Tabasco naturalmente por cuencas compartidas. Presas regulan flujo hacia Oaxaca y Veracruz. Potencial de trasvase al Pac\u00edfico."),
                new DetalleIntercambio("alto", "Genera ~30% de la energ\u00eda hidroel\u00e9ctrica nacional. Las presas del Grijalva son el mayor complejo hidroel\u00e9ctrico de M\u00e9xico.", "Exporta energ\u00eda el\u00e9ctrica al Sistema Interconectado Nacional v\u00eda CFE. Presas Chicoas\u00e9n, Malpaso, La Angostura y Pe\u00f1itas."),
                new DetalleIntercambio("no", "A pesar de su riqueza en recursos naturales, Chiapas es el estado m\u00e1s pobre de M\u00e9xico. Alta dependencia de transferencias federales.", "No puede aportar. Requiere inversi\u00f3n federal y cooperaci\u00f3n de otros estados."),
                new DetalleIntercambio("alto", "Caf\u00e9 de los Altos (uno de los mejores del mundo), cacao, pl\u00e1tano, ma\u00edz, ganader\u00eda bovina extensiva, miel, ca\u00f1a.", "Exporta caf\u00e9, cacao, pl\u00e1tano y ganado al mercado nacional e internacional. Corredor agroindustrial con Oaxaca y Veracruz."),
                new DetalleIntercambio("alto", "Alta poblaci\u00f3n joven, desempleo elevado, migraci\u00f3n hist\u00f3rica hacia CDMX, Quintana Roo y EUA.", "Exporta mano de obra agr\u00edcola y de construcci\u00f3n. Jornaleros que van a Sinaloa, Sonora, BCS en temporadas.")
            )
        ),
        new Estado(
            "Chihuahua",
            "Norte",
            "Extremoso: des\u00e9rtico en Bols\u00f3n de Mapim\u00ed (hasta 45\u00b0C), templado serrano en Sierra Tarahumara. Inviernos muy fr\u00edos con nieve en sierra.",
            List.of("Sequ\u00edas extremas (ciclos recurrentes)", "Heladas severas", "Inundaciones en valles", "Incendios forestales en sierra", "Granizadas"),
            "R\u00edo Conchos (principal), Presa La Boquilla, Presa Chihuahua, Presa Francisco I. Madero. Acu\u00edferos sobreexplotados. Conflicto h\u00eddrico con EUA (Tratado 1944).",
            "CFE Red norte. Termoel\u00e9ctrica Chihuahua II. Parques e\u00f3licos y solares crecientes. Gas natural como fuente principal. Hidroel\u00e9ctrica menor.",
            new Intercambio(
                new DetalleIntercambio("no", "Sequ\u00edas cr\u00f3nicas. Conflicto internacional con EUA por el Tratado de 1944 del R\u00edo Bravo. Acu\u00edferos al l\u00edmite.", "No aplica. Disputa legal por el agua del R\u00edo Conchos con EUA y presi\u00f3n de Coahuila."),
                new DetalleIntercambio("limitado", "Red norte interconectada. Termoel\u00e9ctrica local cubre demanda. Parques solares en desarrollo con potencial exportador.", "Exporta gas natural y puede compartir energ\u00eda solar futura v\u00eda red norte CFE."),
                new DetalleIntercambio("alto", "Maquiladoras en Ciudad Ju\u00e1rez, miner\u00eda, ganader\u00eda y agricultura del Valle de Ju\u00e1rez generan PIB robusto.", "Inversi\u00f3n industrial hacia estados vecinos. Participaciones federales. Exportaciones generan divisas al pa\u00eds."),
                new DetalleIntercambio("alto", "Mayor productor nacional de manzana, nuez y avena. Ganader\u00eda bovina de exportaci\u00f3n (carne de res premium). Producci\u00f3n de chile.", "Exporta carne de res a EUA y mercado nacional, manzana, nuez y ganado vivo. Cadenas log\u00edsticas bien desarrolladas."),
                new DetalleIntercambio("alto", "Ciudad Ju\u00e1rez es la mayor ciudad maquiladora de M\u00e9xico. Alta demanda laboral absorbe trabajadores de todo el pa\u00eds.", "Recibe mano de obra masiva de Durango, Zacatecas, Oaxaca. Exporta t\u00e9cnicos industriales especializados.")
            )
        ),
        new Estado(
            "Ciudad de M\u00e9xico",
            "Centro",
            "Templado subh\u00famedo. Temperaturas 7\u201324\u00b0C. Lluvia concentrada mayo\u2013octubre. Fen\u00f3meno de isla de calor urbano. Heladas en invierno.",
            List.of("Terremotos (zona de lago, alta amplificaci\u00f3n)", "Hundimiento del suelo (subsidencia)", "Inundaciones urbanas", "Sequ\u00edas y desabasto de agua", "Deslaves en periferias"),
            "Sistema Cutzamala (40% del suministro), acu\u00edfero sobreexplotado del Valle de M\u00e9xico, R\u00edo Lerma, captaci\u00f3n pluvial incipiente. Crisis h\u00eddrica cr\u00edtica.",
            "100% red interconectada CFE. Consumo enorme. Centrales de ciclo combinado en Estado de M\u00e9xico. Sin generaci\u00f3n propia significativa.",
            new Intercambio(
                new DetalleIntercambio("no", "Crisis h\u00eddrica severa. Hundimiento del suelo. Dependencia del 40% de agua exterior (Cutzamala). Est\u00e1 en d\u00e9ficit cr\u00f3nico.", "No aplica. Es el mayor receptor de agua del pa\u00eds."),
                new DetalleIntercambio("no", "No tiene generaci\u00f3n propia. Consume enormes cantidades. 100% dependiente de la red interconectada.", "No aplica. Es el mayor consumidor neto de energ\u00eda del pa\u00eds."),
                new DetalleIntercambio("alto", "Mayor concentraci\u00f3n de PIB de M\u00e9xico (~17% del PIB nacional). Centro financiero, fiscal y de servicios del pa\u00eds.", "Transfiere participaciones al fondo federal. Financia proyectos nacionales. Sede de banca de desarrollo (Banobras, Nafin)."),
                new DetalleIntercambio("no", "Sin tierra agr\u00edcola significativa. Produce \u00fanicamente en chinampas de Xochimilco (producci\u00f3n m\u00ednima y simb\u00f3lica).", "No aplica. Es el mayor importador de alimentos de M\u00e9xico."),
                new DetalleIntercambio("alto", "Mayor concentraci\u00f3n de profesionistas, t\u00e9cnicos y servicios especializados del pa\u00eds. Exporta talento a todo M\u00e9xico.", "Movilidad de profesionistas a estados que desarrollan polos industriales o tecnol\u00f3gicos. Nearshoring atrae talento de CDMX.")
            )
        )
    );

    private EstadoData() {}
}
