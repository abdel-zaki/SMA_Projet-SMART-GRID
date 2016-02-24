import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

/***************************************************************************
	Agent horloge
	 - prévient le fournisseur pour qu'il facture ses clients
	 - prévient les consommateurs de réestimer leurs capacités de production
****************************************************************************/

public class Horloge extends Agent{
	Fonctions f = new Fonctions();
	DFAgentDescription [] lesFournisseurs;
	DFAgentDescription [] lesConsommateurs;
	Horloge(){
		
	}
	public void setup(){
		lesFournisseurs = f.getPageJaune("fournisseur", this);
		lesConsommateurs = f.getPageJaune("consommateur", this);
		addBehaviour(new HorlogeBehaviour(this, lesFournisseurs, lesConsommateurs));
	}
}
