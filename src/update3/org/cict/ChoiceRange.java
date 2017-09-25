/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 *
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY.
 * LINKED SYSTEM.
 *
 * PROJECT MANAGER: JHON MELVIN N. PERELLO
 * DEVELOPERS:
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package update3.org.cict;

import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * A class that manages combo box ranges.
 *
 * @author Jhon Melvin
 */
public class ChoiceRange {

    public static void addComboBoxContents(int index, ArrayList<String> charList, ComboBox<String> comboBox) {

        /**
         * Add to observable.
         */
        ObservableList<String> listModel = FXCollections.observableArrayList();
        for (int x = index; x < charList.size(); x++) {
            try {
                listModel.add(charList.get(x));
            } catch (Exception e) {
                //
            }
        }
        comboBox.getItems().clear();
        comboBox.getItems().addAll(listModel);
    }

    /**
     * Use this in to two or more combo box related ranges.
     *
     * @param charList
     * @param source
     * @param self
     * @param extra
     * @param padding
     */
    public static void setComboBoxLimit(ArrayList<String> charList, ComboBox<String> source, ComboBox<String> self, int extra, ComboBox<String>... padding) {

        addComboBoxContents(0, charList, source);
        source.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            int selfStart = (int) newValue;

            for (ComboBox<String> comboBox : padding) {
                selfStart += comboBox.getSelectionModel().getSelectedIndex();
            }
            selfStart += extra;
            addComboBoxContents(selfStart, charList, self);
            self.getSelectionModel().select(0);

        });
        source.getSelectionModel().select(0);

        if (padding.length != 0) {
            ComboBox refBox = padding[0];
            int storeIndex = refBox.getSelectionModel().getSelectedIndex();
            try {
                refBox.getSelectionModel().select(1);
            } catch (Exception e) {
                // list is only 1
            }
            refBox.getSelectionModel().select(storeIndex);
        }

        if (padding.length == 2) {

            source.getItems().addListener((ListChangeListener.Change<? extends String> c) -> {
                if (c.getList().isEmpty()) {
                    self.getItems().clear();
                }
            });

        }
    }

    /**
     * For 2 combo box only.
     *
     * @param charList
     * @param source
     * @param self
     */
    public static void setComboBoxLimit(ArrayList<String> charList, ComboBox<String> source, ComboBox<String> self) {

        addComboBoxContents(0, charList, source);
        source.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            int selfStart = (int) newValue;
            addComboBoxContents(selfStart, charList, self);
            self.getSelectionModel().select(0);

        });
        source.getSelectionModel().select(0);

    }

    /**
     * Modified for time table.
     *
     * @param charList
     * @param source
     * @param self
     */
    public static void setComboBoxLimitTime(ArrayList<String> charList, ComboBox<String> source, ComboBox<String> self) {
        final Integer gap = 4;
        ArrayList<String> subList = new ArrayList<>();
        subList.addAll(charList.subList(0, charList.size() - gap));
        addComboBoxContents(0, subList, source);

        source.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            int selfStart = (int) newValue;
            addComboBoxContents(selfStart + gap, charList, self);
            self.getSelectionModel().select(0);

        });
        source.getSelectionModel().select(0);

    }
}
