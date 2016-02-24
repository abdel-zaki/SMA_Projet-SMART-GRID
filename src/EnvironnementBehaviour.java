import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/***************************************************************************
	Behaviour de l'agent environnement
	 - DONNER LE FEU VERT À L'AGENT HORLOGE
****************************************************************************/

public class EnvironnementBehaviour extends OneShotBehaviour{
	ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
	public EnvironnementBehaviour(){
		
	}
	public void action(){
		// DONNER LE FEU VERT À L'AGENT HORLOGE
		msg.addReceiver(new AID("Horloge", AID.ISLOCALNAME));
		String obj = "start";
		msg.setContent(obj);
		myAgent.send(msg);
	}
}