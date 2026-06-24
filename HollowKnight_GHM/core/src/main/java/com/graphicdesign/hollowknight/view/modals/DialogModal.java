package com.graphicdesign.hollowknight.view.modals;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class DialogModal extends Modal{
    private Label textLabel;
    private final String[] dialogLines = {
        "I am ZOTE the Mighty!",
        "I have earned endless VICTORIES!"
    };
    private int currentIndex = 0;

    public DialogModal() {
        super();

        textLabel = new Label(dialogLines[currentIndex], skin);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);

        add(textLabel).width(500).pad(20);

        addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ENTER) {
                    continueDialog();
                    return true;
                }
                return false;
            }
        });

    }

    private void continueDialog() {
        currentIndex++;

        if(currentIndex < dialogLines.length) {
            textLabel.setText(dialogLines[currentIndex]);
            //TODO -> Play random sound music here.
        }
        else
        {
            hide();
        }
    }

    @Override
    public void show() {
        super.show();
        getStage().setKeyboardFocus(this);
    }
}
