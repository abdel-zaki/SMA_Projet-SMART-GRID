import java.io.IOException;
import java.util.HashMap;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

/***************************************************************************
	Behaviour des agents Consommateurs
	 - permet d'executer les tâches d'un agent consommateur à partir de la réception des tarifs du fournisseur
****************************************************************************/

public class ConsommateursBehaviour extends CyclicBehaviour {
	float tarifsAbonnement = 20.13f;
	Fonctions f = new Fonctions();
	DFAgentDescription [] lesFournisseurs;
	HashMap<String, Float> mapTarifs = new HashMap<String, Float>();
	String monFournisseur = "";
	String senderID = "";
	float tarif_prod = 0;
	boolean producteur = false;
	ACLMessage msgFournisseur = new ACLMessage(ACLMessage.INFORM);
	ConsommateursBehaviour(DFAgentDescription [] lesFournisseurs, float tarif_prod, boolean producteur){
		this.lesFournisseurs = lesFournisseurs;
		this.tarif_prod = tarif_prod;
		this.producteur = producteur;
	}
	public void action() {
		// RECUPERATION DES [TARIFS & CONFIRMATIONS D'ABONNEMENT ET DESABONNEMENT]
		ACLMessage msg = myAgent.receive();
		Object reponse = "";
		if(msg != null){
			try {
				reponse = msg.getContentObject();
			} catch (UnreadableException e) {
				reponse = msg.getContent();
			}
			senderID = msg.getSender().getLocalName();
			if(reponse instanceof String){
				String rep = (String)reponse;
				// CONFIRMATIONS D'ABONNEMENT
				if(rep.equals("abonOK")){
					monFournisseur = senderID;
				}
				// ON CHANGE LE TARIF DE PRODUCTION D'ELECTRICITE A CHAQUE TIC D'HORLOGE
				if(rep.equals("change_prix_prod")){
					float tarif = mapTarifs.get(monFournisseur);
					if(producteur){
						tarif_prod += f.rand(-2, 2);
						if(tarif_prod < 5) tarif_prod = 5;
					}
					for(int i=0; i<lesFournisseurs.length;i++){
						msgFournisseur.clearAllReceiver();
						msgFournisseur.addReceiver(new AID(lesFournisseurs[i].getName().getLocalName(), AID.ISLOCALNAME));
						String [] obj = {"tarif_prod",""+tarif_prod};
						try{
							msgFournisseur.setContentObject(obj);
							myAgent.send(msgFournisseur);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(reponse instanceof String []){
				String [] rep = (String [])reponse;
				float val = Float.parseFloat(rep[1]);
				// LES TARIFS
				if(rep[0].equals("tarifs")){
					if(mapTarifs.size() < lesFournisseurs.length){
						mapTarifs.put(senderID, val);
						// RECEPTION DES TARIFS DE TOUT LES FOURNISSEURS
						if(mapTarifs.size() == lesFournisseurs.length){
							// DEMANDE D'ABONNEMENT
							int index = (int)f.rand(1, lesFournisseurs.length);
							String fournisChoisi = lesFournisseurs[index-1].getName().getLocalName();
							tarif_prod *= tarifsAbonnement;
							msgFournisseur.clearAllReceiver();
							msgFournisseur.addReceiver(new AID(fournisChoisi, AID.ISLOCALNAME));
							String [] obj = {"abonner", ""+tarif_prod};
							try{
								msgFournisseur.setContentObject(obj);
								myAgent.send(msgFournisseur);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				// RECEPTION DE LA FACTURE
				if(rep[0].equals("facture_clients")){
					msgFournisseur.clearAllReceiver();
					msgFournisseur.addReceiver(new AID(senderID, AID.ISLOCALNAME));
					String obj = "facture_clients_recue";
					msgFournisseur.setContent(obj);
					myAgent.send(msgFournisseur);
				}
			}
		}
		else block();
	}
}