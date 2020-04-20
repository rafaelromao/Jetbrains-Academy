import java.util.Stack;

interface Movable {
    int getX();
    int getY();
    void setX(int newX);
    void setY(int newY);
}

interface Storable {
    int getInventoryLength();
    String getInventoryItem(int index);
    void setInventoryItem(int index, String item);
}

interface Command {
    void execute();
    void undo();
}

class CommandMove implements Command {
    Movable entity;
    int xMovement;
    int yMovement;
    boolean isUndo;

    Stack<CommandMove> undos = new Stack<>();

    @Override
    public void execute() {
        if (!isUndo) {
            entity.setX(entity.getX() + xMovement);
            entity.setY(entity.getY() + yMovement);

            var undo = new CommandMove();
            undo.entity = entity;
            undo.xMovement = xMovement;
            undo.yMovement = yMovement;
            undo.isUndo = true;
            undos.push(undo);
        } else {
            entity.setX(entity.getX() - xMovement);
            entity.setY(entity.getY() - yMovement);
        }
    }

    @Override
    public void undo() {
        if (!undos.empty()) {
            var undo = undos.pop();
            undo.execute();
        }
    }
}

class CommandPutItem implements Command {
    Storable entity;
    String item;
    boolean isUndo;
    int undoIndex;

    Stack<CommandPutItem> undos = new Stack<>();

    @Override
    public void execute() {
        if (!isUndo) {
            for (var i = 0; i < entity.getInventoryLength(); i++) {
                if (entity.getInventoryItem(i) == null) {
                    entity.setInventoryItem(i, item);

                    var undo = new CommandPutItem();
                    undo.entity = entity;
                    undo.item = item;
                    undo.isUndo = true;
                    undo.undoIndex = i;
                    undos.push(undo);
                    break;
                }
            }
        } else {
            entity.setInventoryItem(undoIndex, null);
        }
    }

    @Override
    public void undo() {
        if (!undos.empty()) {
            var undo = undos.pop();
            undo.execute();
        }
    }
}