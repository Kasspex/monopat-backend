package com.backend.MonoPat.utils;

public class GeoUtils {

    // Hacemos el constructor privado para que nadie pueda crear
    // una instancia de esta clase. Es solo un contenedor de herramientas.
    private GeoUtils() {
    }

    /**
     * Calcula la distancia en kilómetros entre dos puntos geográficos usando la fórmula de Haversine.
     * Es un método 'static' para que podamos llamarlo directamente sin crear un objeto GeoUtils.
     * Ejemplo de llamada: GeoUtils.calcularDistancia(...);
     */
    public static double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en kilómetros

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}