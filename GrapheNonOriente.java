import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class GrapheNonOriente {

    ArrayList<Character> noeud = new ArrayList<>();
    HashMap<Character,HashMap<Character,Integer>> liste_adjacence = new HashMap<>();
    ArrayList<ArrayList<Integer>> mat = new ArrayList<>();

    GrapheNonOriente(char[][] liaison){
        ajouterLiaisons(liaison);
    }

    GrapheNonOriente(){}

    public void ajouterLiaisons(char[][] liaison){

        System.out.println();
        System.out.println("AJOUTS des liaisons suivantes : ");
        for (char[] l : liaison)
            System.out.println(Arrays.toString(l));
        System.out.println();

        // maj de la liste de noeud du graphe et de la mat :
        for (char[] couple : liaison){
            if (!noeud.contains(couple[0])) {
                noeud.add(couple[0]);
                mat.add(new ArrayList<>());
                for (int j = 0; j < noeud.size() - 1 ; j++) {
                    if (j == noeud.indexOf(couple[1])) {
                        mat.get(noeud.indexOf(couple[0])).add(couple.length == 3 ? (int) couple[2] : 1);
                        mat.get(j).add(couple.length == 3 ? (int) couple[2] : 1);
                    }
                    else {
                        mat.get(noeud.indexOf(couple[0])).add(0);
                        mat.get(j).add(0);
                    }
                }
                mat.get(noeud.indexOf(couple[0])).add(0);
            }
            if (!noeud.contains(couple[1])) {
                noeud.add(couple[1]);
                mat.add(new ArrayList<>());
                for (int j = 0; j < noeud.size() - 1 ; j++) {
                    if (j == noeud.indexOf(couple[0])) {
                        mat.get(noeud.indexOf(couple[1])).add(couple.length == 3 ? (int) couple[2] : 1);
                        mat.get(j).add(couple.length == 3 ? (int) couple[2] : 1);
                    }
                    else {
                        mat.get(noeud.indexOf(couple[1])).add(0);
                        mat.get(j).add(0);
                    }
                }
                mat.get(noeud.indexOf(couple[1])).add(0);
            }
            if (noeud.contains(couple[1]) && noeud.contains(couple[0])){
                mat.get(noeud.indexOf(couple[0])).set(noeud.indexOf(couple[1]),couple.length == 3 ? (int) couple[2] : 1);
                mat.get(noeud.indexOf(couple[1])).set(noeud.indexOf(couple[0]),couple.length == 3 ? (int) couple[2] : 1);
            }
        }

        System.out.println("update des noeud : ");
        System.out.println(noeud);
        System.out.println();
        System.out.println("update de la mat :");
        for ( ArrayList<Integer> a : mat)
            System.out.println(a);
        System.out.println();


        //maj de la liste d'adjacence
        //par defaut, dans un hashmap les clé sont trié par ordre alphabetique (pour les clé en Character en tout cas)
        for (Character n : noeud)
            if (!liste_adjacence.containsKey(n))
                liste_adjacence.put(n,new HashMap<>());
        for (char[] couple : liaison){
            liste_adjacence.get(couple[0]).put(couple[1],couple.length == 3 ? (int) couple[2] : 1);
            liste_adjacence.get(couple[1]).put(couple[0],couple.length == 3 ? (int) couple[2] : 1);
        }

        System.out.println("update de liste d'adjacence :");
        System.out.println(liste_adjacence);
        System.out.println();
        System.out.println("FIN");
        System.out.println();

    }

    public ArrayList<Character> parcourLargeur(char noeud_depart){

        LinkedList<Character> file = new LinkedList<>();// structure de données FIFO.
        ArrayList<Character> parcourLargeur = new ArrayList<>();
        Character x;

        file.add(noeud_depart);
        parcourLargeur.add(noeud_depart);

        while (file.size() > 0){

            x = file.removeFirst();
            for (char noeud : liste_adjacence.get(x).keySet())
                if (!parcourLargeur.contains(noeud)){
                    file.add(noeud);
                    parcourLargeur.add(noeud);
                }

        }

        return parcourLargeur;
    }

    /**
     * affiche le plus court chemin entre les noeud mis en paramettre
     * bassée sur l'algorithme de dijkstra
     * @return la distance du pls court chemin entre le noeud_depart et le noeud_arrive
     */
    public double plusCourtCheminDijkstra(char noeud_depart, char noeud_arrive){

        //INNIT :
        ArrayList<Integer> distance = new ArrayList<>(); //liste qui contient les distances relatif a la liste de noeud par rapport au noeud de depart
        ArrayList<Character> sousGraphe = new ArrayList<>(); //copie de la liste de noeud qui permet de jouer avec sans la cassé
        ArrayList<Character> cheminPlusCourt = new ArrayList<>();
        HashMap<Character,Character> predecesseur = new HashMap<>();

        char s1;
        double longueurDuChemin = 0;

        //DEBUT:


        for (char n : noeud) {
            //on met la distance a 0 pour le noeud de depart et a -1 pour les autres
            if (n == noeud_depart)
                distance.add(0);
            else
                distance.add(-1);

            sousGraphe.add(n);
            //predecesseur.put(n,null);
        }

        while (sousGraphe.size() > 0 ){

            s1 = trouve_min(sousGraphe,distance);
            sousGraphe.remove( sousGraphe.indexOf(s1) );

            for (char s2 : liste_adjacence.get(s1).keySet())
                maj_distance(s1,s2,distance,predecesseur);

        }

        longueurDuChemin = distance.get( noeud.indexOf(noeud_arrive) ) ;

        //on retrace la liste des predecesseur pour construire la liste du plus court chemin
        while (noeud_depart != noeud_arrive){
            cheminPlusCourt.add(0,noeud_arrive);
            noeud_arrive = predecesseur.get(noeud_arrive);
        }
        cheminPlusCourt.add(0,noeud_arrive);

        System.out.println("le chemin le plus court est : " + cheminPlusCourt);

        return longueurDuChemin ;
    }

    public char trouve_min ( ArrayList<Character> sousGraphe, ArrayList<Integer> distance){

        int min = -1;
        char sommet = ' ';

        for (char n : sousGraphe)
            if (min == -1 && distance.get( noeud.indexOf(n) ) != -1){
                min = distance.get( noeud.indexOf(n) );
                sommet = n;
            }else if ( distance.get( noeud.indexOf(n) ) < min && distance.get( noeud.indexOf(n) ) != -1 ){
                min = distance.get( noeud.indexOf(n) );
                sommet = n;
            }

        return sommet;

    }

    public void maj_distance (char s1, char s2, ArrayList<Integer> distance, HashMap<Character,Character> predecesseur){

        if (distance.get( noeud.indexOf(s2) ) == -1 || distance.get( noeud.indexOf(s2) ) > distance.get( noeud.indexOf(s1) ) + liste_adjacence.get(s1).get(s2) ){

            distance.set( noeud.indexOf(s2) , distance.get( noeud.indexOf(s1) ) + liste_adjacence.get(s1).get(s2) );
            predecesseur.put(s2,s1);
        }

    }

}
