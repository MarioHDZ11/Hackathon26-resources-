package com.BiSourceMX.Proyect.dto;

public record IntercambioDTO(
    DetalleIntercambioDTO agua,
    DetalleIntercambioDTO energia,
    DetalleIntercambioDTO presupuesto,
    DetalleIntercambioDTO alimento,
    DetalleIntercambioDTO trabajadores
) {}
