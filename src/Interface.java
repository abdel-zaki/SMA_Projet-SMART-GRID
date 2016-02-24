import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
 
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/***************************************************************************
	JPanel pour afficher les informations liées au fournisseur :
	 - nombre de clients
	 - bénéfice
	 - chiffre d'affaire
	et les informations liées au consommateurs :
	 - nom
	 - consommation
	 - production
	 - facture
****************************************************************************/

public class Interface extends JPanel{
	List<Entry<String, String[]>> entries = new ArrayList<Entry<String, String[]>>();
	HashMap<String, String[]> donnees = new HashMap<String, String[]>();
	JLabel lblFournisseur = new JLabel("Fournisseur");
	JLabel lblClients = new JLabel("Clients");
	JLabel lblNbClients = new JLabel("Nombre de clients :");
	JLabel lblNbClientsVal = new JLabel("0");
	JLabel lblCA = new JLabel("Chiffre d'affaire     :");
	JLabel lblCAVal = new JLabel("0");
	JLabel lblBenefices = new JLabel("Benefices             :");
	JLabel lblBeneficesVal = new JLabel("0");
	
	String[] entetes = {"Nom", "Consommation (E)", "Production (E)", "Facture (E)"};
	int nbCols = entetes.length;
	int nbRows;
	Object[][] tDonnees;
	int cmp = 0;
	JTable tableau;
	JScrollPane sp;
	Interface(){
		setBackground(Color.getHSBColor(0,0,0.8f));
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		setLayout(null);
		lblFournisseur.setBounds(30,20,280,30);
		add(lblFournisseur);
		lblClients.setBounds(300,20,280,30);
		add(lblClients);
		lblNbClients.setBounds(50,70,170,30);
		add(lblNbClients);
		lblNbClientsVal.setBounds(200,70,100,30);
		add(lblNbClientsVal);
		lblCA.setBounds(50,110,170,30);
		add(lblCA);
		lblCAVal.setBounds(200,110,100,30);
		add(lblCAVal);
		lblBenefices.setBounds(50,150,170,30);
		add(lblBenefices);
		lblBeneficesVal.setBounds(200,150,100,30);
		add(lblBeneficesVal);
		
		nbRows = entries.size();
		tDonnees = new Object[nbRows][nbCols];
		for (final Entry<String, String[]> entry : entries){
			for(int i=0; i<nbCols; i++){
				tDonnees[cmp][i] = entry.getValue()[i];
			}
			cmp++;
		}
		cmp = 0;
		tableau = new JTable(tDonnees, entetes);
		tableau.setEnabled(false);
		tableau.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for(int i=0; i<tableau.getColumnModel().getColumnCount(); i++){
			tableau.getColumnModel().getColumn(i).setResizable(false);
		}
		tableau.setRowHeight(24);
		tableau.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableau.getColumnModel().getColumn(1).setPreferredWidth(140);
		tableau.getColumnModel().getColumn(2).setPreferredWidth(110);
		tableau.getColumnModel().getColumn(3).setPreferredWidth(90);
		tableau.getTableHeader().setFont(new Font("Arial",Font.BOLD,12));
		
		sp = new JScrollPane(tableau);
		sp.setBounds(300, 60, 480, 330);
		add(sp);
	}
}
