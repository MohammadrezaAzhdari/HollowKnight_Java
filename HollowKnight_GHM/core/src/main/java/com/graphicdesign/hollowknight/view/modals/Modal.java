package com.graphicdesign.hollowknight.view.modals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.view.UiManager;

public abstract class Modal extends Table {
    protected Skin skin;
    private Table wrapperTable;

    public Modal() {
        skin = AssetManagerLocal.getInstance().getSkin();
        wrapperTable = new Table();

        wrapperTable.setTouchable(Touchable.enabled);
        setTouchable(Touchable.enabled);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(new Color(0, 0, 0, 0.5f)); // TODO -> Make this right!
        pixmap.fill();
        wrapperTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));
        pixmap.dispose();

        //setBackground(skin.getDrawable("win3")); // TODO -> Change this to change background
        pad(10);

        wrapperTable.add(this);

        wrapperTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(event.getTarget() == wrapperTable) {
                    hide();
                }
            }
        });

        wrapperTable.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {return true;}

            @Override
            public boolean keyUp(InputEvent event, int keycode) {return true;}
        });

    }

    public void show() {
        UiManager.getScreen().getModalStack().add(wrapperTable);
        wrapperTable.getStage().setKeyboardFocus(wrapperTable);
    }

    public void hide() {
        wrapperTable.remove();
    }
}
