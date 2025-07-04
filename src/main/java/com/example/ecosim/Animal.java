// Animal (abstract)
public abstract class Animal extends AbstractOrganism {
    protected int speed;
    public Animal(String id, Point2D pos, double e, int speed) {
        super(id, pos, e);
        this.speed = speed;
    }
    public abstract void hunt();
    public void flee() { /* 逃走ロジック */ }
}

