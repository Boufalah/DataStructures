package tree;

import java.awt.Color;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
/** les diferent stuct cours l 2 */
/** general les collections ..*/
/** complexite*/
/** conclu  comparaison des arbre b et arbres nr*/

public class redBlackSearchTree<E> extends AbstractCollection<E> {
    private Noeud racine;
    private int taille;
    private Comparator<? super E> cmp;
    private Noeud sentinelle;

    private class Noeud {
        E cle;
        Noeud gauche;
        Noeud droit;
        Noeud pere;
        Color couleur;
        /**
         * Crée un noeud vide de couleur noir
         */
        Noeud() {
            this.cle = null;
            this.gauche = null;
            this.droit = null;
            this.pere = null;
            this.couleur = Color.BLACK;
        }
        /**
         * Constructeur avec parametre. Crée un noeud rouge avec o comme cle
         *
         * @param o
         *            la cle du noeud
         */
        Noeud(E o) {
            couleur = Color.red;
            cle = o;
            gauche = sentinelle;
            droit = sentinelle;
            pere = sentinelle;
        }
        /**
         * Renvoie le minumum du sous-arbre
         * @return le noeud contenant la plus petite clé du sous arbre dont la racine est le noeud courant
         *
         */
        Noeud minimum() {
            Noeud n=this;
            while(n.gauche!=sentinelle) {
                n=n.gauche;
            }
            return n;
        }

        /**
         * Renvoie le minumum du sous-arbre
         *
         * @return le noeud contenant la plus grande clé du sous arbre dont la racine est le noeud courant
         *
         */
        Noeud maximum() {
            Noeud n=this;
            while(n.droit!=sentinelle) {
                n=n.droit;
            }
            return n;
        }

        /**
         * Renvoie le successeur du noeud courant
         *
         * @return le noeud contenant la clé qui suit la clé de ce noeud dans
         *         l'ordre des clés, null si c'est le noeud contenant la plus
         *         grande clé
         */
        Noeud suivant() {
            Noeud n=this;
            if(n.droit!=sentinelle) return n.droit.minimum();
            Noeud p=this.pere;
            while(p!=sentinelle && n==p.droit) {
                n=p;
                p=n.pere;
            }
            return p;
        }
    }

    /**
     * Crée un arbre vide. Les éléments sont ordonnés selon l'ordre naturel
     */
    public redBlackSearchTree() {
        taille = 0;
        sentinelle = new Noeud();
        racine = sentinelle;
        cmp=(cle1,cle2)->((Comparable<E>) cle1).compareTo(cle2);
    }

    /**
     * Crée un arbre vide. Les éléments sont comparés selon l'ordre imposé par
     * le comparateur
     *
     * @param cmp
     *            le comparateur utilisé pour définir l'ordre des éléments
     */
    public redBlackSearchTree(Comparator<? super E> cmp) {
        this();
        this.cmp = cmp;
    }
    /**
     * Constructeur par recopie. Crée un arbre qui contient les mêmes éléments
     * que c. L'ordre des éléments est l'ordre naturel.
     *
     * @param c
     *            la collection à copier
     */
    public redBlackSearchTree(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    /**
     *  Rotation gauche du noeud passé en parametre
     *
     * @param n
     *            le noeud
     */
    private void rotationGauche(Noeud n) {
        Noeud y = n.droit;
        n.droit = y.gauche;
        if (y.gauche!=sentinelle)
            y.gauche.pere = n;
        y.pere = n.pere;
        if (n.pere == sentinelle)
            racine = y;
        else if (n.pere.gauche == n)
            n.pere.gauche = y;
        else
            n.pere.droit = y;
        y.gauche = n;
        n.pere = y;
    }

    /**
     *  Rotation droite du noeud passé en parametre
     *
     * @param n
     *            le noeud
     */
    private void rotationDroite(Noeud n) {
        Noeud y = n.gauche;
        n.gauche = y.droit;
        if (y.droit!=sentinelle)
            y.droit.pere = n;
        y.pere = n.pere;
        if (n.pere == sentinelle)
            racine = y;
        else if (n.pere.droit == n)
            n.pere.droit = y;
        else
            n.pere.gauche = y;
        y.droit = n;
        n.pere = y;
    }

    /**
     * Organisation de l'arbre apres l'insertion pour que les proprietes de l'arbre ne soient pas violées
     *
     * @param n
     *            le nouveau noeud inseré
     */
    private void organizeIns(Noeud n) {
        while (n != racine && n.pere.couleur == Color.RED) {
            if (n.pere == n.pere.pere.gauche) {
                Noeud y = n.pere.pere.droit;
                if (y.couleur == Color.RED) {
                    n.pere.couleur = Color.BLACK;
                    y.couleur = Color.BLACK;
                    n.pere.pere.couleur = Color.RED;
                    n = n.pere.pere;
                } else {
                    if (n == n.pere.droit) {
                        n = n.pere;
                        rotationGauche(n);
                    }
                    n.pere.couleur = Color.BLACK;
                    n.pere.pere.couleur = Color.RED;
                    rotationDroite(n.pere.pere);
                }
            } else {
                Noeud y = n.pere.pere.gauche;
                if (y.couleur == Color.RED) {
                    n.pere.couleur = Color.BLACK;
                    y.couleur = Color.BLACK;
                    n.pere.pere.couleur = Color.RED;
                    n = n.pere.pere;
                } else {
                    if (n == n.pere.gauche) {
                        n = n.pere;
                        rotationDroite(n);
                    }
                    n.pere.couleur = Color.BLACK;
                    n.pere.pere.couleur = Color.RED;
                    rotationGauche(n.pere.pere);
                }
            }
        }
        racine.couleur = Color.BLACK;
    }
    /**
     * Insere le noeud x. Cette méthode peut être utilisée dans
     * {@link #add(Object)} et {@link Iterator add()}
     *
     * @param z
     *            le noeud à inserer
     */
    public void inserer(Noeud z) {
        Noeud y = sentinelle;
        Noeud x = racine;
        while (x != sentinelle) {
            y = x;
            if (cmp.compare(z.cle, x.cle) < 0) {
                x = x.gauche;
            } else
                x = x.droit;
        }
        z.pere = y;
        if (y == sentinelle) {
            racine = z;
        } else {
            if (cmp.compare(z.cle, y.cle) < 0) {
                y.gauche = z;
            } else
                y.droit = z;
        }
        z.gauche = z.droit = sentinelle;
        taille++;
        organizeIns(z);
    }

    /**
     * Recherche une clé. Cette méthode peut être utilisée par
     * {@link #contains(Object)} et {@link #remove(Object)}
     *
     * @param o
     *            la clé à chercher
     * @return le noeud qui contient la clé ou null si la clé n'est pas trouvée.
     */
    private Noeud rechercher(Object o) {
        // DONE
        Noeud n=racine;
        while(n != sentinelle && (cmp.compare(n.cle,(E) o) != 0)) {
            if((cmp.compare(n.cle,(E) o)<0)) {
                n=n.droit;
            }else{
                n=n.gauche;
            }
        }
        return n;
    }

    /**
     * Supprime le noeud z. Cette méthode peut être utilisée dans
     * {@link #remove(Object)} et {@link Iterator#remove()}
     *
     * @param z
     *            le noeud à supprimer
     * @return le noeud contenant la clé qui suit celle de z dans l'ordre des
     *         clés. Cette valeur de retour peut être utile dans
     *         {@link Iterator#remove()}
     */
    private Noeud supprimer(Noeud z){
        Noeud x, y;

        if(z.gauche == sentinelle || z.droit == sentinelle ) {
            y=z;
        }else {
            y= z.suivant();
        }
        if(y.gauche!=sentinelle) {
            x=y.gauche;
        }
        else {
            x=y.droit;
        }
        x.pere=y.pere;
        if(y.pere== sentinelle) {
            racine= x;
        }
        else{
            if(y==y.pere.gauche) {
                y.pere.gauche=x;
            }
            else {
                y.pere.droit=x;
            }
        }
        if(y !=z) {
            z.cle=y.cle;
        }
        if(y.couleur==Color.BLACK) {
            organizeSuppression(x);
        }
        return z.suivant();
    }

    /**
     * Organisation de l'arbre apres la suppression pour que les proprietes de l'arbre ne soient pas violées
     *
     * @param n
     *            le suivant du noeud supprimé
     */
    private void organizeSuppression(Noeud n) {
        while (n != racine && n.couleur == Color.BLACK) {
            if (n == n.pere.gauche) {
                Noeud y = n.pere.droit;
                if (y.couleur == Color.RED) {
                    y.couleur = Color.BLACK;
                    n.pere.couleur = Color.RED;
                    rotationGauche(n.pere);
                    y = n.pere.droit;
                }

                if (y.gauche.couleur == Color.BLACK && y.droit.couleur == Color.BLACK) {
                    y.couleur = Color.RED;
                    n = n.pere;
                } else {
                    if (y.droit.couleur == Color.BLACK) {
                        y.gauche.couleur = Color.BLACK;
                        y.couleur = Color.RED;
                        rotationDroite(y);
                        y = n.pere.droit;
                    }
                    y.couleur = n.pere.couleur;
                    n.pere.couleur = Color.BLACK;
                    y.droit.couleur = Color.BLACK;
                    rotationGauche(n.pere);
                    n=racine;
                }
            } else {
                Noeud y = n.pere.gauche;
                if (y.couleur == Color.RED) {
                    y.couleur = Color.BLACK;
                    n.pere.couleur = Color.RED;
                    rotationDroite(n.pere);
                    y = n.pere.gauche;
                }
                if (y.droit.couleur == Color.BLACK && y.gauche.couleur == Color.BLACK) {
                    y.couleur = Color.RED;
                    n = n.pere;
                } else {
                    if (y.gauche.couleur == Color.BLACK) {
                        y.droit.couleur = Color.BLACK;
                        y.couleur = Color.RED;
                        rotationGauche(y);
                        y = n.pere.gauche;
                    }
                    y.couleur = n.pere.couleur;
                    n.pere.couleur = Color.BLACK;
                    y.gauche.couleur = Color.BLACK;
                    rotationDroite(n.pere);
                    n=racine;
                }
            }
        }
        racine.couleur=Color.BLACK;
    }

    @Override
    public boolean add(E e) {
        Noeud z = new Noeud(e);
        inserer(z);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return (rechercher(o) != sentinelle);
    }

    @Override
    public Iterator<E> iterator() {
        return new ABRIterator();
    }

    @Override
    public int size() {
        return taille;
    }
    @Override
    public boolean remove(Object o){
        Noeud z=rechercher(o);
        if(z==sentinelle) return false;
        supprimer(z);
        return true;
    }
/*
	public void recycler(Noeud x) {
		x=sentinelle;
	}*/

    private class ABRIterator implements Iterator<E> {
        private Noeud position;
        private Noeud precedent;
        /**
         * Crée un iterateur sur l'arbre courant.
         */
        public ABRIterator() {
            if(!isEmpty()) {
                position = racine.minimum();
                precedent = sentinelle;
            }else {
                position =  sentinelle;
                precedent = sentinelle;
            }
        }
        @Override
        public boolean hasNext() {
            return (position != sentinelle);
        }
        @Override
        public E next() {
            if (position == sentinelle)
                throw new NoSuchElementException("plus de suivant ");
            if(hasNext()) {
                precedent = position;
                position = position.suivant();
                return precedent.cle;
            }else return null;
        }
        @Override
        public void remove() {
            if (precedent == sentinelle)
                throw new IllegalStateException(" next() n'a pas été appelée ");
            if(precedent != racine.maximum()) {
                position  = supprimer(precedent);
            }
            else {
                supprimer(precedent);
                position =sentinelle;
            }
            precedent = sentinelle;
        }
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        toString(racine, buf, "", maxStrLen(racine));
        return buf.toString();
    }

    private void toString(Noeud x, StringBuffer buf, String path, int len) {
        if (x == sentinelle) {
            return;
        }
        toString(x.droit, buf, path + "D", len);
        for (int i = 0; i < path.length(); i++) {
            for (int j = 0; j < len + 6; j++)
                buf.append(' ');
            char c = ' ';
            if (i == path.length() - 1)
                c = '+';
            else if (path.charAt(i) != path.charAt(i + 1))
                c = '|';
            buf.append(c);
        }
        if (x.couleur == Color.BLACK)
            buf.append("-- " + x.cle.toString() + "BLACK");
        else
            buf.append("-- " + x.cle.toString() + "RED");
        if (x.gauche != sentinelle || x.droit != sentinelle) {
            buf.append(" --");
            for (int j = x.cle.toString().length(); j < len; j++)
                buf.append('-');
            buf.append('|');
        }
        buf.append("\n");
        toString(x.gauche, buf, path + "G", len);
    }

    private int maxStrLen(Noeud x) {
        return x == sentinelle ? 0
                : Math.max(x.cle.toString().length(), Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
    }

}
