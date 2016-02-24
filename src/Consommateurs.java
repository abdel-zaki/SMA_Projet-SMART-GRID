import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

/***************************************************************************
	Les agents consommateurs de l'électricité
	 - recherchent dans DF l'agent fournisseur
	 - demandent à l'agent fournisseur ses tarifs
	 - s'abonnent chez l'agent fournisseur en indiquant éventuellement leurs capacités de production d'électricité
	 - produisent de l'électricité et la vendent au fournisseur
****************************************************************************/

public class Consommateurs extends Agent{
	Fonctions f = new Fonctions();
	float tarif_prod = 0;
	boolean producteur = false;
	DFAgentDescription [] lesFournisseurs;
	Consommateurs(){
		
	}
	public void setup(){
		lesFournisseurs = f.getPageJaune("fournisseur", this);
		addBehaviour(new ConsommateursBehaviour(lesFournisseurs, tarif_prod, producteur));
		addBehaviour(new ConsommateursBehaviourTarifs(lesFournisseurs));
	}
}
