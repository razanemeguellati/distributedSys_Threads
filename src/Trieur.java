// Question 1

public class Trieur extends Thread{
    private int[] t ; // le tableau a trier
    private int debut, fin ;  // les index
    //constructeur
    public Trieur(int[] t ){
        this(t,0,t.length-1 ) ;
        // this() function is defined below
    }
    private Trieur(int[] t, int debut , int fin){
        this.t=t ;
        this.debut=debut ;
        this.fin=fin;
    }

    public void run(){
        // the run function mentionned in the course that is inherited from THREAD
        // and should be redefined
        if (fin - debut < 2 ){
            if ( t[debut]> t[fin])
            {
                permuter(debut,fin);
            }
        }
        else
        {
            // - - - - - - USING THREAD - - - - - - - - -
            int milieu = debut + (fin - debut)/2 ;
            Trieur trieur1 = new Trieur(t,debut,milieu);
            trieur1.start(); //running first thread
            // while this new one is sorting this half , the other one is sorting the rest simultaneously ( en parallel )
            Trieur trieur2 = new Trieur(t, milieu+1 , fin );
            trieur2.start(); //running second thread

            try{
                trieur1.join();
                trieur2.join();

               //join() method is used so that the main thread waits for the completion of the 2 threads before continuing
            }
            catch( InterruptedException e ){
                System.out.println(e.getMessage());
                System.out.println("problem in RUN ");
            }

            // once they're both finished we fuse the erray
            triFusion(debut,fin);

        }
    }
    private void permuter(int i , int j ){
        //we cant change i and j values (java, duh)
        // so we're modifying the array
        int permutVal = t[i] ;
        t[i]= t[j] ;
        t[j] = permutVal ;
    }
    private void triFusion(int debut, int fin ){
        // fusionner les 2 tranches triees du tableau

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
        int[] t = {5, 8, 3, 2, 7, 1, 1};
        Trieur trieur = new Trieur(t);
        trieur.start();
        try {
            trieur.join(); //running this will run the two
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
            System.out.println("problem in main ");
        }
        for (int i = 0; i < t.length; i++) {
            System.out.print(t[i] + " | ");
        }
        System.out.println(" yey ");
    }
}

// notes
// had a minor error bcz i started a thread already starting ,
// attaempted touse trieur1.start() twice
