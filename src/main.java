import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.api.Equipment;
import org.osbot.rs07.api.ui.Tab;

@ScriptManifest(author="Bud_Trichs", info="Makes Fucking Larders", name="SimpleLarder", version=1.0D, logo="")
public class main
        extends Script
{
    String state;
    Area BankArea = new Area(new Position(2437, 3093, 0), new Position(2443, 3082, 0));


    private static enum State
    {
        BANKING,  TELEPORT,  BUILDING,  MAKING,  RETURN;
    }

    private main.State getState()
    {
        if (this.BankArea.contains(myPlayer()) && (!getInventory().isFull())) {
            return State.BANKING;
        }
        if (this.BankArea.contains(myPlayer()) && (getInventory().isFull())) {
            return State.TELEPORT;
        }
        if (!this.BankArea.contains(myPlayer()) && (getInventory().isFull())) {
            return State.BUILDING;
        }
        if (!this.BankArea.contains(myPlayer()) && (getInventory().contains("Oak Plank"))) {
            return State.MAKING;
        }

        if (!this.BankArea.contains(myPlayer()) && (!getInventory().contains("Oak Plank"))) {
            {
                return main.State.RETURN;
            }
        }
        return main.State.BANKING;
    }

    @Override
    public int onLoop() throws InterruptedException
    {
        switch (getState())
        {
            case BANKING:
                objects.closest("Bank chest").interact("Use");
                sleep(random(3000,3000));
                getBank().withdrawAll("Oak Plank");
            case TELEPORT:
                this.magic.castSpell(Spells.NormalSpells.HOUSE_TELEPORT);
            case BUILDING:
                this.widgets.interact(548, 39, "Options");
                this.widgets.interact(261, 69, "View House Options");
                this.widgets.interact(370, 5, "On");
                this.widgets.closeOpenInterface();
            case MAKING:
                while(getInventory().contains("Oak Plank")) {
                    objects.closest("Larder space").interact("Build");
                    sleep(random(3000, 3000));
                    this.widgets.interact(458, 5, 4, "Build");
                    sleep(random(3000, 3000));
                    objects.closest("Larder").interact("Remove");
                    sleep(random(2000, 2000));
                    this.dialogues.selectOption(1);
                }
            case RETURN:
                if (!this.BankArea.contains(myPlayer()) && (!getInventory().contains("Oak Plank")))
                {
                    this.widgets.interact(548, 56, "Worn Equipment");
                    this.widgets.interact(387, 15, 1, "Castle Wars");
                }




        }
        return gRandom(300, 550.0D);

    }

    @Override
    public void onExit() {
    }