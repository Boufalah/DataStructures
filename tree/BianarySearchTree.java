package tree;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Implantation de l'interface Collection basée sur les arbres binaires de
 * recherche. Les éléments sont ordonnés soit en utilisant l'ordre naturel (cf
 * Comparable) soit avec un Comparator fourni à la création.
 * </p>
 * 
 * <p>
 * Certaines méthodes de AbstractCollection doivent être surchargées pour plus
 * d'efficacité.
 * </p>
 * 
 * @param <E>
 *            le type des clés stockées dans l'arbre
 */
public class BianarySearchTree<E> extends AbstractCollection<E> {
	private Noeud racine;
	private int taille;
	private Comparator<? super E> cmp;

	private class Noeud {
		E cle;
		Noeud gauche;
		Noeud droit;
		Noeud pere;

		Noeud(E cle) {
			// Done
			gauche=null;
			droit=null;
			pere=null;
			this.cle=cle;
		}

		/**
		 * Renvoie le noeud contenant la clé minimale du sous-arbre enraciné
		 * dans ce noeud
		 * 
		 * @return le noeud contenant la clé minimale du sous-arbre enraciné
		 *         dans ce noeud
		 */
		Noeud minimum() {
			// Done
			Noeud n=this;
			while(n.gauche!=null) {
				n=n.gauche;
			}
			return n;
		}
		Noeud maximum() {
			// Done
			Noeud n=this;
			while(n.droit!=null) {
				n=n.droit;
			}
			return n;
		}
		/**
		 * Renvoie le successeur de ce noeud
		 * 
		 * @return le noeud contenant la clé qui suit la clé de ce noeud dans
		 *         l'ordre des clés, null si c'es le noeud contenant la plus
		 *         grande clé
		 */
		
		Noeud suivant() {
			// Done
			Noeud n=this;
			if(n.droit!=null) return n.droit.minimum();
			Noeud p=this.pere;
			while(p!=null && n==p.droit) {
				n=p;
				p=n.pere;
			}
			return p;
		}
	}

	// Consructeurs

	/**
	 * Crée un arbre vide. Les éléments sont ordonnés selon l'ordre naturel
	 */
	public BianarySearchTree() {
		// DONE
		racine=null;
		taille=0;
		cmp=(cle1,cle2)->((Comparable<E>) cle1).compareTo(cle2);
	}

	/**
	 * Crée un arbre vide. Les éléments sont comparés selon l'ordre imposé par
	 * le comparateur
	 * 
	 * @param cmp
	 *            le comparateur utilisé pour définir l'ordre des éléments
	 */
	public BianarySearchTree(Comparator<? super E> cmp) {
		// DONE
		racine=null;
		taille=0;
		this.cmp=cmp;
	}

	/**
	 * Constructeur par recopie. Crée un arbre qui contient les mêmes éléments
	 * que c. L'ordre des éléments est l'ordre naturel.
	 * 
	 * @param c
	 *            la collection à copier
	 */
	public BianarySearchTree(Collection<? extends E> c) {
		//
		this();
		for(E cle : c) {
			this.add(cle);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new ABRIterator();
	}

	@Override
	public int size() {
		return taille;
	}

	// Quelques méthodes utiles

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
		while(n != null && (cmp.compare(n.cle,(E) o) != 0)) {
			if((cmp.compare(n.cle,(E) o)<0)) {
				n=n.droit;
			}else{
				n=n.gauche;
			}
		}
		return n;
	}
	
	/**
	 * Insere le noeud x. Cette méthode peut être utilisée dans
	 * {@link #add(Object)} et {@link Iterator#add()}
	 * 
	 * @param z
	 *            le noeud à inserer
	 */
	private void inserer(Noeud z) {
		Noeud y = null;
		Noeud x = racine;
		while(x != null) {
			y = x;
			if(cmp.compare(z.cle,x.cle)<0) {
				x = x.gauche;
			}else {
				x = x.droit;
			}
		}
		z.pere = y;
		if(y == null) {
			racine = z;
		}else {
			if(cmp.compare(z.cle,y.cle)<0) {
				y.gauche = z;
			}else {
				y.droit = z;
			}
		}
		z.gauche = z.droit =null;
		taille++;
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
	private Noeud supprimer(Noeud z) {
		// Done
		Noeud y,x;
		if(z.gauche==null && z.droit==null) {
			y=z;
			z=z.suivant();
			if(y.pere.droit==y) y.pere.droit= null;
			else y.pere.gauche=null;		
			recycler(y);
			return z;
		}
		if(z.gauche==null || z.droit==null) {
			 y= z;
		}else {
			y=z.suivant();
		}
		if(y.gauche!=null) {
			x=y.gauche;
		}else {
			x=y.droit;
		}
		if(x!=null) {
			x.pere=y.pere;
		}
		if(y.pere==null) {
			racine=x;
		}else {
			if(y==y.pere.gauche){
				y.pere.gauche=x;
			}else {
				y.pere.droit=x;
			}
		}			
		if(y!=z) {
			z.cle=y.cle;
		}else {
			z=z.suivant();
		}
		recycler(y);
		taille--;
		return z;
	}
	/**
	 * Met le noeud x a null. Cette methode est utilisée dans 
	 * {@link BianarySearchTree <E>#supprimer(Noeud)}
	 *  @param x
	 *            le noeud à recycler
	 * */
	private void recycler(Noeud x) {
		x.pere=null;
		x.droit=null;
		x.gauche=null;
	}
	
	

	/**
	 * Les itérateurs doivent parcourir les éléments dans l'ordre ! Ceci peut se
	 * faire facilement en utilisant {@link Noeud#minimum()} et
	 * {@link Noeud#suivant()}
	 */
	private class ABRIterator implements Iterator<E> {
		private Noeud position;
		private Noeud precedent;

		public ABRIterator() {
			if(!isEmpty()) {
				position = racine.minimum();
				precedent = null;
			}else {
				position =  null;
				precedent = null;
			}
		}

		public boolean hasNext() {
			return (position != null);
		}

		public E next() {
			
			if (position == null)
				throw new NoSuchElementException("plus de suivant ");
			if(hasNext()) {
				precedent = position;
				position = position.suivant();
				return precedent.cle;
			}else return null;
		}

		public void remove() {
			if (precedent == null)
				throw new IllegalStateException(" next() n'a pas été appelée ");
			if(precedent != racine.maximum()) {
				position  = supprimer(precedent);
			}
			else {
				 supprimer(precedent);
				 position =null;
			}
			precedent = null;
		}
	}

	// Pour un "joli" affichage

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		toString(racine, buf, "", maxStrLen(racine));
		return buf.toString();
	}

	private void toString(Noeud x, StringBuffer buf, String path, int len) {
		if (x == null)
			return;
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
		buf.append("-- " + x.cle.toString());
		if (x.gauche != null || x.droit != null) {
			buf.append(" --");
			for (int j = x.cle.toString().length(); j < len; j++)
				buf.append('-');
			buf.append('|');
		}
		buf.append("\n");
		toString(x.gauche, buf, path + "G", len);
	}

	private int maxStrLen(Noeud x) {
		return x == null ? 0 : Math.max(x.cle.toString().length(),
				Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
	}

	// TODO : voir quelles autres méthodes il faut surcharger
	@Override	
 	public boolean contains(Object o) {
		return rechercher(o) !=null;
	}
	@Override
	public boolean remove(Object o){
		Noeud z=rechercher(o);
		if(z==null) return false;
		supprimer(z);
		return true;
	}

	@Override
	public boolean add(E e){//check si le valeur existe 
		Noeud n = new Noeud(e);
		inserer(n);
		return true;
	}

	@Override
	public void clear() {// changer decendre 
		if(racine!=null) {
			racine = null;
			taille=0;		
		}
	}

}