//Question 2 : trieur avec synchronized

public class Trieur2 extends Thread {

    private int[] t; // tableau à trier
    private int debut, fin; // tranche de ce tableau qu'il faut trier
    private Trieur2 parent; // thread Trieur qui a lancé ce (this) Trieur
    private int nbNotify = 0;

    public Trieur2(int[] t) {
        this(null, t, 0, t.length - 1);
    }

    private Trieur2(Trieur2 parent, int[] t, int debut, int fin) {
        this.parent = parent;
        this.t = t;
        this.debut = debut;
        this.fin = fin;
        start();
    }

    //synchronized means only one thread can execute it at a time
    //
    public synchronized void notifier() {
        this.nbNotify++;
        this.notifyAll();
    }

    public void run() {
        if (fin - debut < 2) {
            if (t[debut] > t[fin]) {
                echanger(debut, fin);
            }
        } else {
            int milieu = debut + (fin - debut) / 2;
            Trieur2 trieur1 = new Trieur2(this, t, debut, milieu);
            Trieur2 trieur2 = new Trieur2(this, t, milieu + 1, fin);
            synchronized (this) {
                try {
                    while (nbNotify < 2) {
                        wait();
                    }
                } catch (InterruptedException e) {}
            }
            triFusion(debut, fin);
        }

        if (parent != null) {
            parent.notifier();
        }
    }

    private void echanger(int i, int j) {
        int valeur = t[i];
        t[i] = t[j];
        t[j] = valeur;
    }

    private void triFusion(int debut, int fin) {
        int[] tFusion = new int[fin - debut + 1];
        int milieu = (debut + fin) / 2;
        int i1 = debut, i2 = milieu + 1;
        int iFusion = 0;

        while (i1 <= milieu && i2 <= fin) {
            if (t[i1] < t[i2]) {
                tFusion[iFusion++] = t[i1++];
            } else {
                tFusion[iFusion++] = t[i2++];
            }
        }

        if (i1 > milieu) {
            for (int i = i2; i <= fin; ) {
                tFusion[iFusion++] = t[i++];
            }
        } else {
            for (int i = i1; i <= milieu; ) {
                tFusion[iFusion++] = t[i++];
            }
        }

        for (int i = 0, j = debut; i <= fin - debut; ) {
            t[j++] = tFusion[i++];
        }
    }

    public static void main(String[] args) {
        int[] t = {5, 8, 3, 2, 7, 10, 1};
        Trieur2 trieur = new Trieur2(t);
        try {
            trieur.join();
        } catch (InterruptedException e) {}
        for (int i = 0; i < t.length; i++) {
            System.out.print(t[i] + " | ");
        }
        System.out.println();
    }
}