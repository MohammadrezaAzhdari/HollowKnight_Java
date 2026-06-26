package com.graphicdesign.hollowknight.view.modals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.graphicdesign.hollowknight.model.AssetManagerLocal;
import com.graphicdesign.hollowknight.model.Constants;
import com.graphicdesign.hollowknight.model.Knight;
import com.graphicdesign.hollowknight.view.UiManager;


public class InventoryModal extends Modal{
    private Knight knight;
    private Label description;
    private Table charmGrid;
    private Label capacityLabel;
    // Initialize the array with your actual charm data here
    private final String[][] allCharms = {
        {"Unbreakable Strength", "Increases strength of Nail and damage to enemies", "UnbreakableStrength.png"},
        {"Soul Catcher", "Increases the amount of SOUL gained", "SoulCatcher.png"},
        {"Quick Slash", "Increases attack speed and decreases its cooldown", "QuickSlash.png"},
        {"Quick Focus", "Increases FOCUS speed", "QuickFocus.png"},
        {"Dash Master", "Decreases DASH cooldown", "Dashmaster.png"},
        {"Heavy Blow", "Increases Knockback strength", "HeavyBlow.png"},
    };

    public InventoryModal(Knight knight) {
        super();
        this.knight = knight;
        buildUI();
    }
    private void buildUI() {
        clearChildren();

        capacityLabel = new Label("", skin);
        updateCapacityText();

        description = new Label("Select a charm to see its description.", skin);
        description.setWrap(true);

        charmGrid = new Table();
        populateCharmsGrid();

        // Layout
        add(new Label("INVENTORY", skin)).padBottom(20).row();
        add(capacityLabel).padBottom(20).row();
        add(charmGrid).padBottom(30).row();
        add(description).width(400).center(); // Fixed width for text wrapping
    }

    private void populateCharmsGrid() {
        charmGrid.clearChildren();

        for (int i = 0; i < allCharms.length; i++) {
            String charmName = allCharms[i][0];
            String charmDesc = allCharms[i][1];
            String regionName = allCharms[i][2];

            Texture texture = AssetManagerLocal.getInstance().getTexture(regionName);
            Image charmIcon = new Image(texture);


            if (knight.charms.contains(charmName)) {

            } else {
                charmIcon.setColor(Color.DARK_GRAY); // Unequipped
            }

            charmIcon.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    description.setText(charmDesc);
                    toggleCharm(charmName);
                }
            });
            charmGrid.add(charmIcon).pad(10);

            if ((i + 1) % 4 == 0) charmGrid.row();
        }
    }

    private void toggleCharm(String name) {
        if(knight.charms.contains(name)) {
            knight.charms.remove(name);
        }
        else
        {
            if(knight.charms.size() < Constants.MAX_CHARM_CAPACITY) {
                knight.charms.add(name);
            }
            else
            {
                UiManager.getScreen().openToast("Not enough Notches ! Max capacity is " + Constants.MAX_CHARM_CAPACITY);
                return;
            }
        }
        updateCapacityText();
        populateCharmsGrid();
    }

    private void updateCapacityText() {
        capacityLabel.setText("Notches Used: " + knight.charms.size() + " / " + Constants.MAX_CHARM_CAPACITY);
    }
}
