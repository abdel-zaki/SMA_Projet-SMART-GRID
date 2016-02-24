import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/***************************************************************************
	Classe principale contenant la methode main()
	 - instancie la classe Interface et crée les agents Fournisseurs, Consommateurs, horloge et Observateur
	 - inscrit les agents consommateurs et fournisseur(s) dans DF
****************************************************************************/

public class Environnement extends Agent{
	static Fonctions fo = new Fonctions();
	DFAgentDescription dfd = new DFAgentDescription();
	ServiceDescription sd = new ServiceDescription();
	static Interface inter = new Interface();
	static JFrame f;
	Fonctions fonc = new Fonctions();
	static AgentContainer ac;
	int nbFourn = 1;
	int nbConsom = 10;
	Environnement(){
		
	}
	public static void main(String[]args) throws StaleProxyException{
		Environnement me = new Environnement();
		// L'INTERFACE
		f = new JFrame("Smart Grid v1.0");
		f.setLayout(null);
		f.setContentPane(inter);
		f.setBackground(Color.getHSBColor(0,0,0.4f));
		Dimension SizeEcran = Toolkit.getDefaultToolkit().getScreenSize();
		f.setBounds((SizeEcran.width - 790) / 2, (SizeEcran.height - 400) / 2,790,400);
		f.setResizable(false);
		f.setDefaultCloseOperation(3);
		// INSTANCIER L'AGENT Environnement
		Runtime rt = Runtime.instance();
		rt.setCloseVM(true);
		Profile p = new ProfileImpl(null,1234,null);
		ac = rt.createMainContainer(p);
		fo.createAgent(me, ac, "environnement");
	}
	public void setup(){
		// CREER LES AGENTS FOURNISSEURS
		for(int i=1; i<=nbFourn; i++){
			Fournisseurs fournisseur = new Fournisseurs();
			fo.createAgent(fournisseur, ac, "fournisseur"+i);
			// AJOUTER L'AGENT FOURNISSEUR DANS DF
			fo.setPageJaune(this, fournisseur.getAID(), "fournisseur", "f"+i);
		}
		// CREER LES AGENTS CONSOMMATEURS
		for(int i=1; i<=nbConsom; i++){
			Consommateurs consommateur = new Consommateurs();
			consommateur.producteur = fo.randBool();
			if(consommateur.producteur) consommateur.tarif_prod = (fonc.rand(0.25f,2));
			else consommateur.tarif_prod = 0;
			fo.createAgent(consommateur, ac, "consommateur"+fo.addZeros(i));
			// AJOUTER L'AGENT CONSOMMATEUR DANS DF
			fo.setPageJaune(this, consommateur.getAID(), "consommateur", "consommateur"+i);
		}
		// CREER L'AGENT VISUALISEUR
		Visualiseur visualiseur = new Visualiseur(inter);
		fo.createAgent(visualiseur, ac, "visualiseur");
		// CREER L'AGENT HORLOGE
		Horloge horloge = new Horloge();
		fo.createAgent(horloge, ac, "horloge");
		// AFFICHER L'INTERFACE
		f.show();
		// AJOUTER UN BEHAVIOUR A L'AGENT ENVIRONNEMENT
		addBehaviour(new EnvironnementBehaviour());
	}
}
