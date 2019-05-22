package octillect.controls.validators;

import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.scene.Node;

public class ValidationManager {

    public static void addValidator(Boolean attachListener, ValidatorBase validator, IFXValidatableControl... nodes) {
        for (IFXValidatableControl node : nodes) {
            node.getValidators().add(validator);
            if (attachListener) {
                addFocusListener(node);
            }
        }
    }

    private static void addFocusListener(IFXValidatableControl node) {
        ((Node) node).focusedProperty().addListener((observable, oldValue, newValue) -> {
            node.resetValidation();
            if (!newValue) {
                node.validate();
            }
        });
    }

}
