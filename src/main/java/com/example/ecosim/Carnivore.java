// Carnivore
public class Carnivore extends Animal {
    private double huntingSkill;
    @Override
    public void hunt() { /* 他動物を捕食 */ }
    public void chase(AbstractOrganism prey) { /* 追跡 */ }
    @Override
    public AbstractOrganism reproduce() { /* 肉食動物の繁殖 */ }
}