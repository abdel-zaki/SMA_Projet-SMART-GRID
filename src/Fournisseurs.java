import jade.core.Agent;

/***************************************************************************
	Agent fournisseur
	 - répond au demandes de tarifs, d'abonnement et de désabonnement
	 - réagit aux préventions de l'agent horloge et facture ses clients
****************************************************************************/

public class Fournisseurs extends Agent{
	Fournisseurs(){
		
	}
	public void setup(){
		addBehaviour(new FournisseursBehaviour());
	}
}
