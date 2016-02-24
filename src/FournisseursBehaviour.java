import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.HashMap;

/***************************************************************************
	Behaviour de l'agent Fournisseur
	 - permet d'executer les deux tâches d'un agent fournisseur
****************************************************************************/

public class FournisseursBehaviour extends CyclicBehaviour{
	Fonctions f = new Fonctions();
	HashMap<String, String[]> donnees = new HashMap<String, String[]>();
	ACLMessage msgReponse = new ACLMessage(ACLMessage.INFORM);
	// Prix / unité
	float tarifsAbonne = 0.32f;
	HashMap<String, Float> tarifs = new HashMap<String, Float>();
	String obj = "";
	String senderID = "";
	Object demande = "";
	HashMap<String, Float> listClients = new HashMap<String, Float>();
	float chiffreA = 0;
	float benefices = 0;
	FournisseursBehaviour(){
		
	}
	public void action(){
		// RECEPTION DES DEMANDES DE [TARIFS, ABONNEMENT & DESABONNEMENT]
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			try {
				demande = msg.getContentObject();
			} catch (UnreadableException e) {
				demande = msg.getContent();
			}
			senderID = msg.getSender().getLocalName();
			if(demande instanceof String){
				msgReponse.clearAllReceiver();
				msgReponse.addReceiver(new AID(senderID, AID.ISLOCALNAME));
				String demand = (String)demande;
				// REPONSE A LA DEMANDE DES TARIFS
				if(demand.equals("tarifs")) {
					String [] ob = {"tarifs", tarifsAbonne+""};
					try {
						msgReponse.setContentObject(ob);
						myAgent.send(msgReponse);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// DESABONNER UN CONSOMMATEUR
				if(demand.equals("desabonner")){
					listClients.remove(senderID);
					obj = "desabonOK";
					msgReponse.setContent(obj);
					myAgent.send(msgReponse);
				}
				// L'HORLOGE ME PREVIENT POUR FACTURER MES CLIENTS
				if(demand.equals("facturer")){
					Object [] keys = listClients.keySet().toArray();
					for(int i=0; i<keys.length; i++){
						// REESTIMER LES TARIFS DE MES CLIENTS
						tarifs.put((String)keys[i],f.arrondir(f.rand(18,22)));
					}
					for(int i=0; i<keys.length; i++){
						// Facture = tarif - capacité
						String ob [] = {"facture_clients", (tarifs.get(keys[i])-listClients.get(keys[i]))+""};
						msgReponse.clearAllReceiver();
						msgReponse.addReceiver(new AID((String)keys[i], AID.ISLOCALNAME));
						try {
							msgReponse.setContentObject(ob);
							myAgent.send(msgReponse);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					chiffreA = 0;
					benefices = 0;
				}
				// CONFIRMATION DE LA RECEPTION DE LA FACTURE DE LA PART DU CLIENT
				if(demand.equals("facture_clients_recue")){
					float facture = tarifs.get(senderID) - listClients.get(senderID);
					if(facture >= 0) chiffreA += facture;
					benefices += facture;
					toVisualiseur();
				}
			}
			if(demande instanceof String []){
				String [] demand = (String [])demande;
				float val = Float.parseFloat(demand[1]);
				msgReponse.clearAllReceiver();
				msgReponse.addReceiver(new AID(senderID, AID.ISLOCALNAME));
				// ABONNER UN CONSOMMATEUR
				if(demand[0].equals("abonner")){
					listClients.put(senderID,val);
					obj = "abonOK";
					msgReponse.setContent(obj);
					myAgent.send(msgReponse);
				}
				// UN CONSOMMATEUR VIENT DE METTRE A JOUR SA CAPACITE
				if(demand[0].equals("tarif_prod")){
					listClients.put(senderID, val);
				}
			}
		}
		else block();
	}
	// ENVOYER A L'AGENT VISUALISEUR DE :
	//  - [nombre clients, chiffre d'affaire, benefices]
	//  - [noms clients, tarifs mensuel, tarifs production, facture]
	void toVisualiseur(){
		Object [] keys = listClients.keySet().toArray();
		for(int i=0; i<listClients.size(); i++){
			String [] values = {""+keys[i], ""+tarifs.get(keys[i]) ,""+f.arrondir(listClients.get(keys[i])), ""+f.arrondir(tarifs.get(keys[i])-listClients.get(keys[i]))};
			donnees.put(""+keys[i], values);
		}
		Object [] obj = {listClients.size(), chiffreA, benefices, donnees};
		msgReponse.clearAllReceiver();
		msgReponse.addReceiver(new AID("Visualiseur", AID.ISLOCALNAME));
		msgReponse.setInReplyTo(senderID);
		try {
			msgReponse.setContentObject(obj);
			myAgent.send(msgReponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
