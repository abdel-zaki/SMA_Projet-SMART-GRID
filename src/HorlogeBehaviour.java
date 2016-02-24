import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/***************************************************************************
	behaviour de l'agent horloge
	 - permet d'executer les tâches de l'agent horloge
****************************************************************************/

public class HorlogeBehaviour extends TickerBehaviour{
	ACLMessage msgFournisseur = new ACLMessage(ACLMessage.INFORM);
	ACLMessage msgConsommateur = new ACLMessage(ACLMessage.INFORM);
	DFAgentDescription [] lesFournisseurs;
	DFAgentDescription [] lesConsommateurs;
	Interface inter;
	boolean start = false;
	HorlogeBehaviour(Agent a, DFAgentDescription [] lesFournisseurs, DFAgentDescription [] lesConsommateurs){
		super(a,2000);
		this.lesFournisseurs = lesFournisseurs;
		this.lesConsommateurs = lesConsommateurs;
	}
	protected void onTick() {
		ACLMessage msg = myAgent.receive();
		Object reponse = "";
		if(msg != null){
			try {
				reponse = msg.getContentObject();
			} catch (UnreadableException e) {
				reponse = msg.getContent();
			}
			if(reponse instanceof String){
				String rep = (String)reponse;
				// L'ENVIRONNEMENT SIGNALE QUE TOUT LES AGENTS SONT CRÉES
				if(reponse.equals("start")){
					start = true;
				}
			}
		}
		if(start){
			// PREVENIR LES FOURNISSEURS DE FACTURER LEURS CLIENTS
			msgFournisseur.clearAllReceiver();
			for(int i=0; i<lesFournisseurs.length; i++){
				msgFournisseur.addReceiver(new AID(lesFournisseurs[i].getName().getLocalName(), AID.ISLOCALNAME));
			}
			String obj = "facturer";
			msgFournisseur.setContent(obj);
			myAgent.send(msgFournisseur);
			// PREVENIR LES CONSOMMATEURS DE REESTIMER LEURS CAPACITES DE PRODUCTION D'ELECTRICITE
			msgConsommateur.clearAllReceiver();
			for(int i=0; i<lesConsommateurs.length; i++){
				msgConsommateur.addReceiver(new AID(lesConsommateurs[i].getName().getLocalName(), AID.ISLOCALNAME));
			}
			obj = "change_prix_prod";
			msgConsommateur.setContent(obj);
			myAgent.send(msgConsommateur);
		}
	}
}
