package blockchain;

import java.util.Stack;

public class BlockChain {

    private Stack<Block> blocks = new Stack<>();

    public Block put() {
        var id = blocks.size();
        var previous = blocks.size() == 0 ? null : blocks.peek().hash();
        var block = new Block(id, previous);
        blocks.add(block);
        return block;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var block: blocks) {
            sb.append(block);
            sb.append("\n");
        }
        return sb.toString();
    }
}
