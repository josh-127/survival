package net.survival.block.state;

public interface BlockStateVisitor {
    void visit(AirBlock block);
    void visit(BedrockBlock block);
    void visit(CactusBlock block);
    void visit(CobblestoneBlock block);
    void visit(DirtBlock block);
    void visit(GrassBlock block);
    void visit(GravelBlock block);
    void visit(OakLeavesBlock block);
    void visit(OakLogBlock block);
    void visit(OakSaplingBlock block);
    void visit(SandBlock block);
    void visit(SandstoneBlock block);
    void visit(StoneBlock block);
    void visit(TempSolidBlock block);
    void visit(WaterBlock block);
}