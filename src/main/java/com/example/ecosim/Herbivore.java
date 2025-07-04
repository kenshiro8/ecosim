// Herbivore
public class Herbivore extends Animal {
    private double stomachCapacity;
    @Override
    public void hunt() { /* 無効／例外 */ }
    public void graze(Plant p) { /* エネルギー獲得 */ }
    @Override
    public AbstractOrganism reproduce() { /* 草食動物の繁殖 */ }
}