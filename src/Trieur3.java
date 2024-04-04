//Question 3 : Trieur avec semaphore

import java.util.concurrent.Semaphore;

public class Trieur3 extends Thread {
    private int[] t; // tableau à trier
    private int debut, fin; // tranche de ce tableau qu'il faut trier
    private Semaphore semaphore;
    public Trieur3(int[] t, Semaphore semaphore) {
        this(t, 0, t.length - 1, semaphore);
    }

    private Trieur3(int[] t, int debut, int fin, Semaphore semaphore) {
        this.t = t;
        this.debut = debut;
        this.fin = fin;
        this.semaphore = semaphore;
    }

    public void run() {
        triFusion(debut, fin);
        semaphore.release();
    }

    private void triFusion(int debut, int fin) {
        if (debut == fin) {
            return;
        }
        int milieu = (debut + fin) / 2;
        Semaphore leftSemaphore = new Semaphore(0);
        Trieur3 trieur1 = new Trieur3(t, debut, milieu, leftSemaphore);
        Semaphore rightSemaphore = new Semaphore(0);
        Trieur3 trieur2 = new Trieur3(t, milieu + 1, fin, rightSemaphore);

        trieur1.start();
        trieur2.start();

        try {
            leftSemaphore.acquire();
            rightSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fusionner(debut, milieu, fin);
    }

    private void fusionner(int debut, int milieu, int fin) {
        int[] tFusion = new int[fin - debut + 1];
        int i = debut, j = milieu + 1, k = 0;

        while (i <= milieu && j <= fin) {
            if (t[i] < t[j]) {
                tFusion[k++] = t[i++];
            } else {
                tFusion[k++] = t[j++];
            }
        }

        while (i <= milieu) {
            tFusion[k++] = t[i++];
        }

        while (j <= fin) {
            tFusion[k++] = t[j++];
        }

        for (i = debut; i <= fin; i++) {
            t[i] = tFusion[i - debut];
        }
    }

    public static void main(String[] args) {
        int[] t = { 5, 8, 3, 2, 0, 7, 1, 1 };
        Semaphore semaphore = new Semaphore(0);
        Trieur3 trieur = new Trieur3(t, semaphore);
        trieur.start();
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < t.length; i++) {
            System.out.print(t[i] + "; ");
        }
        System.out.println();
    }
}