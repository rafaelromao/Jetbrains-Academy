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

class CommandMove {
    Movable entity;
    int xMovement;
    int yMovement;
}

class CommandPutItem {
    Storable entity;
    String item;
}