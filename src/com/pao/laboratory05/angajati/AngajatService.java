package com.pao.laboratory05.angajati;

import com.pao.laboratory05.biblioteca.Carte;

import java.util.Arrays;

public class AngajatService {
    
    private Angajat[] angajati;
    
    private AngajatService() {
        this.angajati = new Angajat[0];
    }
    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {return Holder.INSTANCE;}

    public void addAngajat(Angajat ang) {
        Angajat[] newAngajat = new Angajat[this.angajati.length + 1];
        System.arraycopy(this.angajati, 0, newAngajat, 0, this.angajati.length);

        newAngajat[newAngajat.length - 1] = ang;
        this.angajati = newAngajat;

        System.out.println("Angajat adaugata: " + ang.getNume());
    }

    public void printAll() {
        for(Angajat ang : this.angajati) {
            System.out.println(ang);
        }
    }

    public void listBySalary() {
        Angajat[] newAngajati = this.angajati.clone();
        Arrays.sort(newAngajati);

        System.out.println(Arrays.toString(newAngajati).replace(", A", "\nA"));
    }

    public void findByDepartament(String numeDept) {
        boolean found = false;
        for(Angajat ang : angajati) {
            if (ang.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                found = true;
                System.out.println(ang);
            }
        }
        if(!found) {
            System.out.println("Niciun angajat în departamentul:" + numeDept);
        }
    }
}
