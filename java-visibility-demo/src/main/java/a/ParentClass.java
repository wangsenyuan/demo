package a;

public abstract class ParentClass {
    protected abstract void foo();

    protected void bar() {
        System.out.println("in ParentClass#bar");
    }

    void buzz() {
        System.out.println("in ParentClass#buzz");
    }
}
