package mario;

/**
 * Created by Vipi on 01/04/2016.
 */
public class Ordre {
    int codi;
    String nom;

    public Ordre(int codi, String nom) {
        this.codi = codi;
        this.nom = nom;
    }

    public int getCodi() {
        return codi;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return "Ordre{" +
                "codi=" + codi +
                ", nom='" + nom + '\'' +
                '}';
    }
}


