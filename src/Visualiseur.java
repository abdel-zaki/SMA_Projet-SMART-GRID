import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

/***************************************************************************
	agent visualiseur
	 - affiche les informations à jour sur le fournisseur et ses cients (à chaque tic d'horloge)
****************************************************************************/

public class Visualiseur extends Agent{
	Fonctions f = new Fonctions();
	Interface inter;
	Visualiseur(Interface inter){
		this.inter = inter;
	}
	public void setup(){
		addBehaviour(new VisualiseurBehaviour(inter));
	}
}
