package br.ufmg.dcc.controleincidentes.model;

import java.util.Objects;

public class Localizacao {
    private static final double RAIO_TERRA_KM = 6371.0;

    private double latitude;
    private double longitude;
    private String endereco;
    private String bairro;

    public Localizacao(double latitude, double longitude, String endereco, String bairro) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.endereco = endereco;
        this.bairro = bairro;
    }

    // Cálculo da distância em km pela fórmula de Haversine
    public double calcularDistancia(Localizacao outra) {
        if (outra == null) {
            throw new IllegalArgumentException("Localização de destino não pode ser nula");
        }

        double dLat = Math.toRadians(outra.latitude - this.latitude);
        double dLon = Math.toRadians(outra.longitude - this.longitude);

        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(outra.latitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RAIO_TERRA_KM * c;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Localizacao)) return false;
        Localizacao that = (Localizacao) o;
        return Double.compare(that.latitude, latitude) == 0 &&
               Double.compare(that.longitude, longitude) == 0 &&
               Objects.equals(endereco, that.endereco) &&
               Objects.equals(bairro, that.bairro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, endereco, bairro);
    }

    @Override
    public String toString() {
        return String.format("%s, %s (%.4f, %.4f)", endereco, bairro, latitude, longitude);
    }
}
