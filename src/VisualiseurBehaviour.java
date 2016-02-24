import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/***************************************************************************
	behaviour de l'agent visualiseur
	 - permet d'executer la tâche de l'agent visualiseur
****************************************************************************/

public class VisualiseurBehaviour extends CyclicBehaviour{
	HashMap<String, String[]> donnees = new HashMap<String, String[]>();
	List<Entry<String, String[]>> entries = new ArrayList<Entry<String, String[]>>();
	Fonctions f = new Fonctions();
	DFAgentDescription [] lesFournisseurs;
	Interface inter;
	VisualiseurBehaviour(Interface inter){
		this.inter = inter;
	}
	public void action() {
		int nbClients;
		float chiffreA;
		float benefices;
		ACLMessage msg = myAgent.receive();
		Object reponse = new Object();
		if(msg != null){
			try {
				reponse = msg.getContentObject();
			} catch (UnreadableException e) {
				reponse = msg.getContent();
			}
			// AFFICHER LES INFORMATIONS RECUS DU FOURNISSEUR
			if(reponse instanceof Object []){
				Object[] rep = (Object []) reponse;
				nbClients = (int)(rep[0]);
				chiffreA = (float)rep[1];
				benefices = (float)rep[2];
				donnees = (HashMap<String, String[]>)rep[3];
				inter.lblNbClientsVal.setText(nbClients+"");
				inter.lblCAVal.setText(f.arrondir(chiffreA)+" Euros");
				inter.lblBeneficesVal.setText(f.arrondir(benefices)+" Euros");
				// Ajout des entrées de la map à une liste
				entries = new ArrayList<Entry<String, String[]>>(donnees.entrySet());
				// Tri de la liste sur la valeur de l'entrée
				Collections.sort(entries, new Comparator<Entry<String, String[]>>() {
					public int compare(final Entry<String, String[]> e1, final Entry<String, String[]> e2) {
						return e1.getKey().compareTo(e2.getKey());
					}
				});
				inter.entries = entries;
				inter.repaint();
			}
		}
	}
}
