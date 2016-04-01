package mario;

/**
 * Created by Vipi on 18/03/2016.
 */
public class Animal {
    int codi;
    String nom;
    int ordre;
    String especie;
    String descripcio;
    String estat;
    String imatge;

    public Animal(){}

    public Animal(int codi, String nom, int ordre, String especie, String descripcio, String estat, String imatge) {
        this.codi = codi;
        this.nom = nom;
        this.ordre = ordre;
        this.especie = especie;
        this.descripcio = descripcio;
        this.estat = estat;
        this.imatge = imatge;
    }

    public int getCodi() {
        return codi;
    }

    public String getNom() {
        return nom;
    }

    public String getEspecie() {
        return especie;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public String getEstat() {
        return estat;
    }

    public String getImatge() {
        return imatge;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "codi=" + codi +
                ", nom='" + nom + '\'' +
                ", ordre=" + ordre +
                ", especie='" + especie + '\'' +
                ", descripcio='" + descripcio + '\'' +
                ", estat='" + estat + '\'' +
                ", imatge='" + imatge + '\'' +
                '}';
    }
}
