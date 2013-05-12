package chalmers.dax021308.ecosystem.view.populationsettings;

import java.awt.Checkbox;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.text.TextBox;


import net.miginfocom.swing.MigLayout;
import chalmers.dax021308.ecosystem.model.genetics.GeneralGeneTypes;
import chalmers.dax021308.ecosystem.model.genetics.GeneticSettings;
import chalmers.dax021308.ecosystem.model.genetics.GeneticSettings.GenomeSpecification;
import chalmers.dax021308.ecosystem.model.genetics.newV.IGene;
import chalmers.dax021308.ecosystem.model.genetics.newV.IGenome;
import chalmers.dax021308.ecosystem.model.util.Stat;

/**
 * Panel for Genetic settings.
 * <p>
 * Dynamically adds gui elements depending on the values  in the given GeneticSettings.
 *
 * @author Erik Ramqvist
 *
 */
public class GeneticPanel extends JPanel {

	public GeneticPanel(GeneticSettings geneticContent) {
		setLayout(new MigLayout("", "[][][65.00][65.00][65]", "[][][][][]"));

		JLabel lblGeneticSettings = new JLabel("Genetic Settings");
		lblGeneticSettings.setFont(new Font("Tahoma", Font.PLAIN, 16));
		add(lblGeneticSettings, "cell 0 0 4 1");

		JLabel lblActiveOnBirt = new JLabel("Active on birth");
		add(lblActiveOnBirt, "cell 3 1,alignx center");

		JLabel lblMutable = new JLabel("Mutable");
		add(lblMutable, "cell 4 1");

		int currentRow = 1;
		for(final GenomeSpecification g : geneticContent.getGenomes()) {
			if(g.getGenomeType() != GenomeSpecification.TYPE_BOOLEAN) {
				continue;
			}

			final IGene gene = g.getGene();

			currentRow++;
			JLabel lblGroupBehavoir = new JLabel(g.getName());
			add(lblGroupBehavoir, "cell 2 "+currentRow+",alignx right");

			final JCheckBox chkbxActiveBirth = new JCheckBox("",gene.isGeneActive());
			chkbxActiveBirth.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gene.setHaveGene(chkbxActiveBirth.isSelected());
				}
			});
			add(chkbxActiveBirth, "cell 3 "+currentRow+",alignx center");

			final JCheckBox chckbxMutable = new JCheckBox("", gene.isMutable());
			chckbxMutable.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gene.setMutable(chckbxMutable.isSelected());
				}
			});
			add(chckbxMutable, "cell 4 "+currentRow+",alignx left");

			List<JComponent> jList = new ArrayList<JComponent>();
			jList.add(chkbxActiveBirth);
			jList.add(chckbxMutable);
		}
		currentRow++;
		JLabel emptyLabel = new JLabel(" ");
		add(emptyLabel, "cell 2 "+currentRow+",alignx right");
		currentRow++;
		JLabel startLabel = new JLabel("Start");
		add(startLabel, "cell 3 "+currentRow+",alignx center");
		JLabel minLabel = new JLabel("Min");
		add(minLabel, "cell 4 "+currentRow+",alignx center");
		JLabel maxLabel = new JLabel("Max");
		add(maxLabel, "cell 5 "+currentRow+",alignx center");
		JLabel mutableLabel = new JLabel("Mutable");
		add(mutableLabel, "cell 6 "+currentRow+",alignx center");
		JLabel randomStartLabel = new JLabel("Random Start");
		add(randomStartLabel, "cell 7 "+currentRow+",alignx center");
		currentRow++;
		for(final GenomeSpecification g : geneticContent.getGenomes()) {
			if(g.getGenomeType() == GenomeSpecification.TYPE_BOOLEAN) {
				continue;
			}

			final IGene gene = g.getGene();

			currentRow++;
			JLabel lblGroupBehavoir = new JLabel(g.getName());
			add(lblGroupBehavoir, "cell 2 "+currentRow+",alignx right");

			//Start value

			final JTextField tfStartValue = new JTextField("" + Stat.roundNDecimals(gene.getCurrentDoubleValue(),2));
			tfStartValue.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) { }
				@Override
				public void keyReleased(KeyEvent e) {
					try {
						double value = Double.parseDouble(tfStartValue.getText());
						gene.setCurrentDoubleValue(value);
					} catch (Exception ex) {

					}
				}
				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			add(tfStartValue, "cell 3 "+currentRow+",growx");

			//Min value
			final JTextField tfMaxValue = new JTextField("" +  Stat.roundNDecimals(gene.getMaxValue(),2));

			final JTextField tfMinValue = new JTextField("" +  Stat.roundNDecimals(gene.getMinValue(),2));
			tfMinValue.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) { }
				@Override
				public void keyReleased(KeyEvent e) {
					try {
						double value = Double.parseDouble(tfMinValue.getText());
						if (value > gene.getMaxValue()) {
							tfMaxValue.setText(tfMinValue.getText());
							gene.setMaxValue(value);
						}
						gene.setMinValue(value);
					} catch (Exception ex) {
					}
				}
				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			add(tfMinValue, "cell 4 "+currentRow+",growx");

			//Max value
			tfMaxValue.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) { }
				@Override
				public void keyReleased(KeyEvent e) {
					try {
						double value = Double.parseDouble(tfMaxValue.getText());
						if (value < gene.getMinValue()) {
							tfMinValue.setText(tfMaxValue.getText());
							gene.setMinValue(value);
						}
						gene.setMaxValue(value);
					} catch (Exception ex) {
					}
				}
				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			add(tfMaxValue, "cell 5 "+currentRow+",growx");

			//Mutable
			final JCheckBox chkbxMutable = new JCheckBox("", gene.isMutable());
			chkbxMutable.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gene.setMutable(chkbxMutable.isSelected());
				}
			});
			add(chkbxMutable, "cell 6 "+currentRow+",alignx center");

			//Random Start
			final JCheckBox chkbxRandomStart = new JCheckBox("", gene.hasRandomStartValue());
			chkbxRandomStart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gene.setRandomStartValue(chkbxRandomStart.isSelected());
				}
			});
			add(chkbxRandomStart, "cell 7 "+currentRow+",alignx center");
			
		}
	}
	private static final long serialVersionUID = 1L;

}
